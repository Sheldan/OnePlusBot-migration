package dev.sheldan.oneplus.bot.migration.remind;

import dev.sheldan.oneplus.bot.migration.remind.models.LegacyReminder;
import dev.sheldan.oneplus.bot.migration.remind.repository.LegacyReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderServiceBean {
    @Autowired
    private LegacyReminderRepository repository;

    public List<LegacyReminder> getAllReminders() {
        return repository.findAll();
    }
}
