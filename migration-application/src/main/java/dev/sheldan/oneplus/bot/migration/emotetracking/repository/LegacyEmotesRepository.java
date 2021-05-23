package dev.sheldan.oneplus.bot.migration.emotetracking.repository;

import dev.sheldan.oneplus.bot.migration.emotetracking.models.LegacyEmotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyEmotesRepository extends JpaRepository<LegacyEmotes, Long> {
}
