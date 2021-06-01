package dev.sheldan.oneplus.bot.migration.mutes.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "Mutes")
@Getter
@Setter
public class LegacyMutes {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "muted_user_id")
    private Long mutedUserId;

    @Column(name = "muted_by_id")
    private Long mutedById;

    @Column(name = "reason")
    private String reason;

    @Column(name = "mute_date")
    private Instant muteDate;

    @Column(name = "unmute_date")
    private Instant unMuteDate;

    @Column(name = "mute_ended")
    private Boolean muteEnded;

}
