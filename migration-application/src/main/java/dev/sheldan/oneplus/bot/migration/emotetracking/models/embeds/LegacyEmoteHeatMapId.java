package dev.sheldan.oneplus.bot.migration.emotetracking.models.embeds;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegacyEmoteHeatMapId implements Serializable {
    @Column(name = "id")
    private Long id;
    @Column(name = "emote_id")
    private Long emoteId;
}
