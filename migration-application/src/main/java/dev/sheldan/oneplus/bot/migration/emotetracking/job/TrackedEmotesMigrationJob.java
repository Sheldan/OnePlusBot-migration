package dev.sheldan.oneplus.bot.migration.emotetracking.job;

import dev.sheldan.abstracto.core.models.database.AServer;
import dev.sheldan.abstracto.core.service.BotService;
import dev.sheldan.abstracto.core.service.GuildService;
import dev.sheldan.abstracto.core.service.StartupServiceBean;
import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.abstracto.statistic.emote.model.database.TrackedEmote;
import dev.sheldan.abstracto.statistic.emote.service.management.TrackedEmoteManagementService;
import dev.sheldan.abstracto.statistic.emote.service.management.UsedEmoteManagementService;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.emotetracking.models.LegacyEmotes;
import dev.sheldan.oneplus.bot.migration.emotetracking.repository.LegacyEmotesRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ListedEmote;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
public class TrackedEmotesMigrationJob extends ListenerAdapter implements MigrationJob {

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private ServerManagementService serverManagementService;

    @Autowired
    private LegacyEmotesRepository legacyEmotesRepository;

    @Autowired
    private TrackedEmoteManagementService trackedEmoteManagementService;

    @Autowired
    private UsedEmoteManagementService usedEmoteManagementService;

    @Autowired
    private GuildService guildService;

    @Autowired
    private TrackedEmotesMigrationJob self;

    @Autowired
    private StartupServiceBean startupServiceBean;

    @Autowired
    private BotService botService;

    @Override
    @Transactional
    public void execute() {
        try {
            startupServiceBean.startBot();
        } catch (LoginException e) {
            log.error("Failed to login to discord.", e);
        }
    }

    @Transactional
    public void startJob() {
        Guild guild = guildService.getGuildById(migrationConfig.getServerId());
        guild.retrieveEmotes()
                .queue(listedEmotes -> self.convertEmotes(listedEmotes));
    }

    @Transactional
    public void convertEmotes(List<ListedEmote> guildEmotes) {
        List<LegacyEmotes> legacyEmotes = legacyEmotesRepository.findAll();
        log.info("Found {} legacy emotes.", legacyEmotes.size());
        AServer server = serverManagementService.loadServer(migrationConfig.getServerId());
        for (int i = 0; i < legacyEmotes.size(); i++) {
            LegacyEmotes emote = legacyEmotes.get(i);
            log.info("Converting emote {} with {} uses.", i + 1, emote.getEmoteUses().size());
            if(!emote.getTrackingDisabled()) {
                Long emoteId = emote.getEmoteId();
                if(emoteId == 0) {
                    continue;
                }
                Boolean exists = guildEmotes.stream().anyMatch(listedEmote -> listedEmote.getIdLong() == emote.getEmoteId());
                String emoteName = emote.getName();
                Boolean animated = emote.getAnimated();
                TrackedEmote trackedEmote = trackedEmoteManagementService.createTrackedEmote(emoteId, emoteName, animated, true, server);
                if(!exists) {
                    log.info("Emote {} has been deleted - marking it as such.", emote.getEmoteId());
                    trackedEmote.setDeleted(true);
                }
                emote.getEmoteUses().forEach(legacyEmoteHeatMap -> {
                    Instant usageDate = legacyEmoteHeatMap.getUsageDate().plus(2, ChronoUnit.HOURS).truncatedTo(ChronoUnit.DAYS);
                    usedEmoteManagementService.createEmoteUsageFor(trackedEmote, legacyEmoteHeatMap.getUsageCount(), usageDate);
                });
            } else {
                log.info("Emote {} has tracking disabled - mot migrating.", emote.getEmoteId());
            }
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        self.startJob();
    }

    @Override
    public String getKey() {
        return "trackedEmotes";
    }
}
