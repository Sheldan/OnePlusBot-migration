package dev.sheldan.oneplus.bot.migration.usernotes.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserNote")
@Getter
@Setter
public class LegacyUserNote {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userNoteUserId;

    @Column(name = "note_text")
    private String noteText;
}
