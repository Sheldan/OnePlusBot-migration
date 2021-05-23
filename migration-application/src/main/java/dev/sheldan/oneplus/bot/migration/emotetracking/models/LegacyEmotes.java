package dev.sheldan.oneplus.bot.migration.emotetracking.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Emotes")
@Getter
@Setter
public class LegacyEmotes implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "emote_key")
    private String emoteKey;

    @Column(name = "animated")
    private Boolean animated;

    @Column(name = "emote_id")
    private Long emoteId;

    @Column(name = "custom")
    private Boolean custom;

    @Column(name = "tracking_disabled")
    private Boolean trackingDisabled;

    @Getter
    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "legacyEmote")
    private List<LegacyEmoteHeatMap> emoteUses;

}
