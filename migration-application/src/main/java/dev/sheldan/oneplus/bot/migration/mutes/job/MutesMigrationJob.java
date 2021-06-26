package dev.sheldan.oneplus.bot.migration.mutes.job;

import dev.sheldan.abstracto.core.models.AServerAChannelMessage;
import dev.sheldan.abstracto.core.models.database.AServer;
import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.abstracto.core.service.management.UserInServerManagementService;
import dev.sheldan.abstracto.moderation.model.database.Mute;
import dev.sheldan.abstracto.moderation.service.MuteService;
import dev.sheldan.abstracto.moderation.service.management.MuteManagementService;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.mutes.models.LegacyMutes;
import dev.sheldan.oneplus.bot.migration.mutes.repository.LegacyMutesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class MutesMigrationJob implements MigrationJob {

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private UserInServerManagementService userInServerManagementService;

    @Autowired
    private ServerManagementService serverManagementService;

    @Autowired
    private LegacyMutesRepository legacyMutesRepository;

    @Autowired
    private MuteManagementService muteManagementService;

    @Autowired
    private MuteService muteService;

    @Override
    @Transactional
    public void execute() {
        List<LegacyMutes> legacyMutes = legacyMutesRepository.findAll();
        Long serverId = migrationConfig.getServerId();
        AServer server = serverManagementService.loadServer(serverId);
        log.info("Found {} legacy mutes.", legacyMutes.size());

        legacyMutes.forEach(legacyMute -> {
            AUserInAServer mutingUser = userInServerManagementService.loadOrCreateUser(serverId, legacyMute.getMutedById());
            AUserInAServer mutedUser = userInServerManagementService.loadOrCreateUser(serverId, legacyMute.getMutedUserId());
            AServerAChannelMessage  serverAChannelMessage = AServerAChannelMessage
                    .builder()
                    .server(server)
                    .build();
            String triggerKey = null;
            log.info("Migration mute {}.", legacyMute.getId());
            Mute createdMute = muteManagementService.createMute(mutedUser, mutingUser, legacyMute.getReason(), legacyMute.getUnMuteDate(), serverAChannelMessage, triggerKey, legacyMute.getId());
            createdMute.setMuteEnded(legacyMute.getMuteEnded());
            createdMute.setMuteDate(legacyMute.getMuteDate());
        });
    }

    @Override
    public String getKey() {
        return "mutes";
    }
}
