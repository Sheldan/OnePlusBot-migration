package dev.sheldan.oneplus.bot.migration.warnings.job;

import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.abstracto.core.service.management.UserInServerManagementService;
import dev.sheldan.abstracto.moderation.model.database.Warning;
import dev.sheldan.abstracto.moderation.service.management.WarnManagementService;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.warnings.models.LegacyWarnings;
import dev.sheldan.oneplus.bot.migration.warnings.repository.LegacyProfanityWarningsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class WarningsMigrationJob implements MigrationJob {

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private UserInServerManagementService userInServerManagementService;

    @Autowired
    private ServerManagementService serverManagementService;

    @Autowired
    private LegacyProfanityWarningsRepository legacyProfanityWarningsRepository;

    @Autowired
    private WarnManagementService warnManagementService;

    @Override
    @Transactional
    public void execute() {
        List<LegacyWarnings> legacyWarnings = legacyProfanityWarningsRepository.findAll();
        Long serverId = migrationConfig.getServerId();
        log.info("Loaded {} warnings.", legacyWarnings.size());
        legacyWarnings.forEach(legacyWarning -> {
            log.info("Migration warning {}.", legacyWarning.getId());
            AUserInAServer warnedUser = userInServerManagementService.loadOrCreateUser(serverId, legacyWarning.getWarnedUserId());
            AUserInAServer warningUser = userInServerManagementService.loadOrCreateUser(serverId, legacyWarning.getWarningUserId());
            Warning createdWarning = warnManagementService.createWarning(warnedUser, warningUser, legacyWarning.getReason(), legacyWarning.getId());
            createdWarning.setDecayed(legacyWarning.getDecayed());
            createdWarning.setWarnDate(legacyWarning.getWarnDate());
        });

    }

    @Override
    public String getKey() {
        return "warnings";
    }
}
