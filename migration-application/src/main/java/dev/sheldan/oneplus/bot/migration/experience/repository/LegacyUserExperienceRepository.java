package dev.sheldan.oneplus.bot.migration.experience.repository;

import dev.sheldan.oneplus.bot.migration.experience.models.LegacyUserExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyUserExperienceRepository extends JpaRepository<LegacyUserExperience, Long> {
}
