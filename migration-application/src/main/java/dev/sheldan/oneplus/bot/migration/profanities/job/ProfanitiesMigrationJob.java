package dev.sheldan.oneplus.bot.migration.profanities.job;

import dev.sheldan.abstracto.core.models.ServerChannelMessage;
import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.abstracto.core.models.database.ProfanityGroup;
import dev.sheldan.abstracto.core.service.management.ChannelManagementService;
import dev.sheldan.abstracto.core.service.management.ProfanityGroupManagementService;
import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.abstracto.core.service.management.UserInServerManagementService;
import dev.sheldan.abstracto.profanityfilter.model.database.ProfanityUse;
import dev.sheldan.abstracto.profanityfilter.model.database.ProfanityUserInAServer;
import dev.sheldan.abstracto.profanityfilter.service.management.ProfanityUseManagementService;
import dev.sheldan.abstracto.profanityfilter.service.management.ProfanityUserInServerManagementService;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.profanities.config.ProfanitiesProperties;
import dev.sheldan.oneplus.bot.migration.profanities.models.LegacyProfanityUses;
import dev.sheldan.oneplus.bot.migration.profanities.repository.LegacyProfanityUsesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class ProfanitiesMigrationJob implements MigrationJob {

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private UserInServerManagementService userInServerManagementService;

    @Autowired
    private ServerManagementService serverManagementService;

    @Autowired
    private ChannelManagementService channelManagementService;

    @Autowired
    private LegacyProfanityUsesRepository legacyProfanityUsesRepository;

    @Autowired
    private ProfanitiesProperties profanitiesProperties;

    @Autowired
    private ProfanityUseManagementService profanityUseManagementService;

    @Autowired
    private ProfanityUserInServerManagementService profanityUserInServerManagementService;

    @Autowired
    private ProfanityGroupManagementService profanityGroupManagementService;

    @Override
    @Transactional
    public void execute() {
        List<LegacyProfanityUses> legacyUses = legacyProfanityUsesRepository.findAll();
        log.info("Found {} legacy uses.", legacyUses.size());
        for (int i = 0; i < legacyUses.size(); i++) {
            LegacyProfanityUses legacyProfanityUses = legacyUses.get(i);
            log.info("Converting item {}", i + 1);
            boolean channelExists = channelManagementService.channelExists(legacyProfanityUses.getProfaneChannelId());
            if(!channelExists) {
                log.info("Channel did not exist. Using default channel.");
            }
            Long channelId = channelExists ? legacyProfanityUses.getProfaneChannelId() : migrationConfig.getDefaultProfanityChannel();
            ServerChannelMessage profaneMessage = ServerChannelMessage
                    .builder()
                    .messageId(legacyProfanityUses.getProfaneMessageId())
                    .channelId(channelId)
                    .serverId(migrationConfig.getServerId())
                    .build();
            ServerChannelMessage reportMessage = ServerChannelMessage
                    .builder()
                    .messageId(legacyProfanityUses.getReportMessageId())
                    .channelId(migrationConfig.getProfanityReportChannel())
                    .serverId(migrationConfig.getServerId())
                    .build();
            AUserInAServer aUserInAServer = userInServerManagementService.loadOrCreateUser(migrationConfig.getServerId(), legacyProfanityUses.getProfaneUserId());
            ProfanityUserInAServer user = profanityUserInServerManagementService.getOrCreateProfanityUser(aUserInAServer);
            Long profanityGroupId = profanitiesProperties.getProfanitiesGroupMapping().get(legacyProfanityUses.getProfanityId());
            ProfanityGroup profanityGroup = profanityGroupManagementService.getProfanityGroupById(profanityGroupId);
            ProfanityUse use = profanityUseManagementService.createProfanityUse(profaneMessage, reportMessage, user, profanityGroup);
            use.setVerified(true);
            if (legacyProfanityUses.getValid()) {
                use.setConfirmed(true);
            }
        }
    }

    @Override
    public String getKey() {
        return "profanities";
    }
}
