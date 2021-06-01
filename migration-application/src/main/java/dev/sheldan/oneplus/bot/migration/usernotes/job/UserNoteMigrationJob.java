package dev.sheldan.oneplus.bot.migration.usernotes.job;

import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.abstracto.core.service.management.UserInServerManagementService;
import dev.sheldan.abstracto.moderation.service.management.UserNoteManagementService;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.usernotes.models.LegacyUserNote;
import dev.sheldan.oneplus.bot.migration.usernotes.repository.LegacyUserNoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class UserNoteMigrationJob implements MigrationJob {

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private UserInServerManagementService userInServerManagementService;

    @Autowired
    private ServerManagementService serverManagementService;

    @Autowired
    private LegacyUserNoteRepository legacyUserNoteRepository;

    @Autowired
    private UserNoteManagementService userNoteManagementService;

    @Override
    @Transactional
    public void execute() {
        List<LegacyUserNote> legacyUserNotes = legacyUserNoteRepository.findAll();
        Long serverId = migrationConfig.getServerId();
        log.info("Loaded {} user notes.", legacyUserNotes.size());
        legacyUserNotes.forEach(legacyUserNote -> {
            log.info("Migrating user note {}.", legacyUserNote.getId());
            AUserInAServer userNoteUser = userInServerManagementService.loadOrCreateUser(serverId, legacyUserNote.getUserNoteUserId());
            userNoteManagementService.createUserNote(userNoteUser, legacyUserNote.getNoteText());
        });
    }

    @Override
    public String getKey() {
        return "userNote";
    }
}
