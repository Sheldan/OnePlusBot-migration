package dev.sheldan.oneplus.bot.migration.remind.job;

import dev.sheldan.abstracto.core.models.database.AChannelType;
import dev.sheldan.abstracto.core.models.database.AServer;
import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.abstracto.core.service.management.ChannelManagementService;
import dev.sheldan.abstracto.core.service.management.ServerManagementService;
import dev.sheldan.abstracto.core.service.management.UserInServerManagementService;
import dev.sheldan.abstracto.remind.model.database.Reminder;
import dev.sheldan.abstracto.remind.service.ReminderService;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import dev.sheldan.oneplus.bot.migration.remind.ReminderServiceBean;
import dev.sheldan.oneplus.bot.migration.remind.models.LegacyReminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RemindMigrationJob implements MigrationJob {

    @Autowired
    private ReminderServiceBean reminderServiceBean;

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private MigrationConfig migrationConfig;

    @Autowired
    private UserInServerManagementService userInServerManagementService;

    @Autowired
    private ServerManagementService serverManagementService;

    @Autowired
    private ChannelManagementService channelManagementService;

    @Override
    @Transactional
    public void execute() {
        log.info("Loading legacy reminders.");
        List<LegacyReminder> reminders = reminderServiceBean.getAllReminders()
                .stream()
                .filter(legacyReminder -> !legacyReminder.getReminded())
                .collect(Collectors.toList());
        Long serverId = migrationConfig.getServerId();
        AServer server = serverManagementService.loadOrCreate(serverId);

        log.info("Loaded {} reminders. Scheduling reminders.", reminders.size());
        List<Long> channelIds = reminders.stream().map(LegacyReminder::getChannelId).distinct().collect(Collectors.toList());
        log.info("Found {} independent channels.", channelIds.size());
        channelIds.forEach(channelId -> channelManagementService.createChannel(channelId, AChannelType.TEXT, server));
        reminders.forEach(legacyReminder -> {
            AUserInAServer toBeRemindedUser = userInServerManagementService.loadOrCreateUser(serverId, legacyReminder.getRemindedUserId());
            log.info("Migrating reminder {}.", legacyReminder.getId());
            Duration reminderDuration = Duration.between(Instant.now(), legacyReminder.getTargetDate());
            Reminder createdReminder = reminderService.createReminderInForUser(toBeRemindedUser, legacyReminder.getReminderText(), reminderDuration, legacyReminder.getChannelId(), legacyReminder.getMessageId());
            createdReminder.setReminderDate(legacyReminder.getReminderDate());
        });
        log.info("Finished scheduling reminders.");
    }

    @Override
    public String getKey() {
        return "reminder";
    }
}
