package dev.sheldan.oneplus.bot.migration.remind.repository;

import dev.sheldan.oneplus.bot.migration.remind.models.LegacyReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyReminderRepository extends JpaRepository<LegacyReminder, Long> {
}
