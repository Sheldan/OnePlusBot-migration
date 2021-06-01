package dev.sheldan.oneplus.bot.migration.warnings.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "Warnings")
@Getter
@Setter
public class LegacyWarnings {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "warned_user_id")
    private Long warnedUserId;

    @Column(name = "warned_by_id")
    private Long warningUserId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "date")
    private Instant warnDate;

    @Column(name = "decayed_date")
    private Instant decayDate;

    @Column(name = "decayed")
    private Boolean decayed;

}
