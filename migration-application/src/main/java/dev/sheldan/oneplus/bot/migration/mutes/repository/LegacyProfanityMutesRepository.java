package dev.sheldan.oneplus.bot.migration.mutes.repository;

import dev.sheldan.oneplus.bot.migration.mutes.models.LegacyMutes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyProfanityMutesRepository extends JpaRepository<LegacyMutes, Long> {
}
