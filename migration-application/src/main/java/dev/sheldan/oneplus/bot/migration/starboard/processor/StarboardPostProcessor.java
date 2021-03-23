package dev.sheldan.oneplus.bot.migration.starboard.processor;

import dev.sheldan.abstracto.core.models.database.AChannel;
import dev.sheldan.abstracto.core.models.database.AChannelType;
import dev.sheldan.abstracto.core.models.database.AServer;
import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.abstracto.core.service.management.ChannelManagementService;
import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.abstracto.core.service.management.UserInServerManagementService;
import dev.sheldan.abstracto.starboard.model.database.StarboardPost;
import dev.sheldan.oneplus.bot.migration.common.Processor;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.starboard.models.StarboardMessage;
import dev.sheldan.oneplus.bot.migration.starboard.provided.StarboardChannelProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StarboardPostProcessor implements Processor<StarboardMessage, StarboardPost> {

    @Autowired
    private UserInServerManagementService userInServerManagementService;

    @Autowired
    private ServerManagementService serverManagementService;

    @Autowired
    private ChannelManagementService channelManagementService;

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private StarboardChannelProvider channelProvider;

    @Override
    public StarboardPost process(StarboardMessage starboardMessage) {
        Long serverId = migrationConfig.getServerId();
        AServer server = serverManagementService.loadOrCreate(serverId);
        AUserInAServer author = userInServerManagementService.loadOrCreateUser(serverId, starboardMessage.getAuthorId());
        Long channelID = channelProvider.getChannelIdForMessageId(starboardMessage.getMessageId());
        AChannel sourceChannel = channelManagementService.createChannel(channelID, AChannelType.TEXT, server);
        AChannel starboardChannel = channelManagementService.createChannel(migrationConfig.getStarboardChannelId(), AChannelType.TEXT, server);
        return StarboardPost.builder()
                .server(server)
                .author(author)
                .postMessageId(starboardMessage.getMessageId())
                .ignored(starboardMessage.getIgnored())
                .reactionCount(starboardMessage.getStarCount().intValue())
                .sourceChannel(sourceChannel)
                .starboardMessageId(starboardMessage.getStarboardMessageId())
                .starboardChannel(starboardChannel)
                .build();
    }
}
