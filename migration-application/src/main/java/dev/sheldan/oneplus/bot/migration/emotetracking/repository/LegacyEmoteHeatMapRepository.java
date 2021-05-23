package dev.sheldan.oneplus.bot.migration.emotetracking.repository;

import dev.sheldan.oneplus.bot.migration.emotetracking.models.LegacyEmoteHeatMap;
import dev.sheldan.oneplus.bot.migration.emotetracking.models.embeds.LegacyEmoteHeatMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyEmoteHeatMapRepository extends JpaRepository<LegacyEmoteHeatMap, LegacyEmoteHeatMapId> {
}
