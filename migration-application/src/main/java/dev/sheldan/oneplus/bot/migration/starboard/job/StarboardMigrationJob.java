package dev.sheldan.oneplus.bot.migration.starboard.job;

import dev.sheldan.abstracto.starboard.model.database.StarboardPost;
import dev.sheldan.abstracto.starboard.service.management.StarboardPostManagementService;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.starboard.models.StarboardMessage;
import dev.sheldan.oneplus.bot.migration.starboard.processor.StarboardPostProcessor;
import dev.sheldan.oneplus.bot.migration.starboard.processor.StarboardPostReactionProcessor;
import dev.sheldan.oneplus.bot.migration.starboard.provided.StarboardChannelProvider;
import dev.sheldan.oneplus.bot.migration.starboard.service.StarboardPostServiceBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StarboardMigrationJob implements MigrationJob {

    @Autowired
    private StarboardPostServiceBean postServiceBean;

    @Autowired
    private StarboardPostProcessor postProcessor;

    @Autowired
    private StarboardPostManagementService starboardPostManagementService;

    @Autowired
    private StarboardChannelProvider channelProvider;

    @Autowired
    private StarboardPostReactionProcessor reactionProcessor;

    @Override
    @Transactional
    public void execute() {
        channelProvider.loadPostChannelMapping();
        log.info("Loading legacy messages.");
        List<StarboardMessage> allMessages = postServiceBean.getStarboardMessages()
                .stream()
                .filter(starboardMessage -> !starboardMessage.getIgnored())
                .collect(Collectors.toList());
        log.info("Loaded {} messages.", allMessages.size());
        allMessages
                .forEach(starboardMessage -> {
                    StarboardPost post = postProcessor.process(starboardMessage);
                    starboardPostManagementService.createStarboardPost(post);
                    starboardMessage.getReactors().forEach(starboardPostRelations -> reactionProcessor.process(starboardPostRelations, post));
                });
        log.info("Persisted messages.");
    }

    @Override
    public String getKey() {
        return "starboard";
    }

}
