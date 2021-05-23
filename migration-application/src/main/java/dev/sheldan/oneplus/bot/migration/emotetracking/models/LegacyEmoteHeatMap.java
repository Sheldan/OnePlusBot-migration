package dev.sheldan.oneplus.bot.migration.emotetracking.models;

import dev.sheldan.oneplus.bot.migration.emotetracking.models.embeds.LegacyEmoteHeatMapId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "EmoteHeatMap")
@Getter
@Setter
public class LegacyEmoteHeatMap implements Serializable {

    @EmbeddedId
    private LegacyEmoteHeatMapId heatMapId;

    @Column(name = "usage_date")
    private Instant usageDate;

    @Column(name = "usage_count")
    private Long usageCount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("emoteId")
    @JoinColumn(name = "emote_id", referencedColumnName = "id", nullable = false)
    private LegacyEmotes legacyEmote;

}
