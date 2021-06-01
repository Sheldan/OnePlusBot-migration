package dev.sheldan.oneplus.bot.migration.warnings.repository;

import dev.sheldan.oneplus.bot.migration.warnings.models.LegacyWarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyWarningsRepository extends JpaRepository<LegacyWarnings, Long> {
}
