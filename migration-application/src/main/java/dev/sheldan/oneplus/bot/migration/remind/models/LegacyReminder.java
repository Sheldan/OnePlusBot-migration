package dev.sheldan.oneplus.bot.migration.remind.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "Reminders")
@Getter
@Setter
public class LegacyReminder {

    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "reminded_user_id")
    private Long remindedUserId;

    @Column(name = "remind_text")
    private String reminderText;

    @Column(name = "reminder_date")
    private Instant reminderDate;

    @Column(name = "target_date")
    private Instant targetDate;

    @Column(name = "reminded")
    private Boolean reminded;

    @Column(name = "reminder_scheduled")
    private Boolean reminderScheduled;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "message_id")
    private Long messageId;
}
