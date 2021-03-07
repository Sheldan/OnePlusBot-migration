package dev.sheldan.oneplus.bot.migration.common.setup;

import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.oneplus.bot.migration.common.SetupJob;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ServerSetup implements SetupJob {

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private ServerManagementService serverManagementService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute() {
        log.info("Creating server.");
        serverManagementService.loadOrCreate(migrationConfig.getServerId());
    }
}
