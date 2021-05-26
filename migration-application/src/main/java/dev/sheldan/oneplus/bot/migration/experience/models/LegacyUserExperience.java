package dev.sheldan.oneplus.bot.migration.experience.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "User")
@Getter
@Setter
public class LegacyUserExperience implements Serializable {

    @Id
    @Column(name = "id")
    private Long userId;

    @Column(name = "xp")
    private Long experience;

    @Column(name = "message_count")
    private Long messages;

}
