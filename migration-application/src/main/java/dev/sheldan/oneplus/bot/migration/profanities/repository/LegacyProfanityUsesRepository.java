package dev.sheldan.oneplus.bot.migration.profanities.repository;

import dev.sheldan.oneplus.bot.migration.profanities.models.LegacyProfanityUses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyProfanityUsesRepository extends JpaRepository<LegacyProfanityUses, Long> {
}
