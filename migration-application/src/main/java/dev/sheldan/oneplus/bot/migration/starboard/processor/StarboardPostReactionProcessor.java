package dev.sheldan.oneplus.bot.migration.starboard.processor;

import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.abstracto.core.service.management.UserInServerManagementService;
import dev.sheldan.abstracto.utility.models.database.StarboardPost;
import dev.sheldan.abstracto.utility.models.database.StarboardPostReaction;
import dev.sheldan.abstracto.utility.service.management.StarboardPostManagementService;
import dev.sheldan.abstracto.utility.service.management.StarboardPostReactorManagementService;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.starboard.models.StarboardPostRelations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StarboardPostReactionProcessor {

    @Autowired
    private UserInServerManagementService userInServerManagementService;

    @Autowired
    private ServerManagementService serverManagementService;

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private StarboardPostManagementService starboardPostManagementService;

    @Autowired
    private StarboardPostReactorManagementService reactorManagementService;

    public StarboardPostReaction process(StarboardPostRelations original, StarboardPost associatedPost) {
        Long serverId = migrationConfig.getServerId();
        AUserInAServer reactor = userInServerManagementService.loadOrCreateUser(serverId, original.getRelationIds().getUserId());
        return reactorManagementService.addReactor(associatedPost, reactor);
    }
}
