package dev.sheldan.oneplus.bot.migration.faq.repository;

import dev.sheldan.oneplus.bot.migration.faq.models.FAQCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyFAQCommandRepository extends JpaRepository<FAQCommand, Long> {
}
