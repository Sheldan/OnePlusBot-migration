package dev.sheldan.oneplus.bot.migration.experience.job;

import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.abstracto.core.service.management.UserInServerManagementService;
import dev.sheldan.abstracto.experience.model.database.AExperienceLevel;
import dev.sheldan.abstracto.experience.model.database.AUserExperience;
import dev.sheldan.abstracto.experience.service.AUserExperienceService;
import dev.sheldan.abstracto.experience.service.management.ExperienceLevelManagementService;
import dev.sheldan.abstracto.experience.service.management.UserExperienceManagementService;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.experience.models.LegacyUserExperience;
import dev.sheldan.oneplus.bot.migration.experience.repository.LegacyUserExperienceRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ExperienceMigrationJob implements MigrationJob {

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private LegacyUserExperienceRepository repository;

    @Autowired
    private AUserExperienceService aUserExperienceService;

    @Autowired
    private ExperienceLevelManagementService experienceLevelManagementService;

    @Autowired
    private UserInServerManagementService userInServerManagementService;

    @Autowired
    private UserExperienceManagementService userExperienceManagementService;

    @Autowired
    private ExperienceMigrationJob self;

    private static final Integer BATCH_SIZE = 500;

    @Override
    @Transactional
    public void execute() {
        List<LegacyUserExperience> userExperiences = repository.findAll();
        log.info("Found {} experiences to migrate.",  userExperiences.size());
        List<LegacyUserInfo> simpleUsers = convertToSimpleUser(userExperiences);
        self.convertNextUsers(simpleUsers, 0, 0);
    }

    @Transactional
    public void convertNextUsers(List<LegacyUserInfo> legacyUserInfos, Integer startingIndex, Integer counter) {
        List<AExperienceLevel> levels = experienceLevelManagementService.getLevelConfig();
        Long serverId = migrationConfig.getServerId();
        Integer listOffset = startingIndex;
        for (int i = 0; i < BATCH_SIZE && startingIndex + i < legacyUserInfos.size(); i++, startingIndex++) {
            int listIndex = listOffset + i;
            LegacyUserInfo userInfo = legacyUserInfos.get(listIndex);
            AUserInAServer aUserInAServer = userInServerManagementService.loadOrCreateUser(serverId, userInfo.getUserId());
            log.info("Migrating user {} at list index {}.", userInfo.getUserId(), listIndex);
            AUserExperience userExperience = aUserExperienceService.createUserExperienceForUser(aUserInAServer, userInfo.getExperience(), userInfo.getMessageCount(), levels);
            userExperienceManagementService.saveUser(userExperience);
            counter++;
        }
        log.info("Migrated {} users.", counter);
        if(startingIndex < legacyUserInfos.size()) {
            log.info("Starting next batch. Index {}.", startingIndex);
            Integer finalStartingIndex = startingIndex;
            Integer finalCounter = counter;
            CompletableFuture.runAsync(() -> self.convertNextUsers(legacyUserInfos, finalStartingIndex, finalCounter)).exceptionally(throwable -> {
                log.error("Next job failed with exception.", throwable);
                return null;
            });
        }
    }

    private List<LegacyUserInfo> convertToSimpleUser(List<LegacyUserExperience> legacyUserExperiences) {
        return legacyUserExperiences
                .stream()
                .map(legacyUserExperience -> LegacyUserInfo
                        .builder()
                        .userId(legacyUserExperience.getUserId())
                        .experience(legacyUserExperience.getExperience())
                        .messageCount(legacyUserExperience.getMessages())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String getKey() {
        return "experience";
    }
}

@Getter
@Setter
@Builder
class LegacyUserInfo {
    private Long userId;
    private Long experience;
    private Long messageCount;
}
