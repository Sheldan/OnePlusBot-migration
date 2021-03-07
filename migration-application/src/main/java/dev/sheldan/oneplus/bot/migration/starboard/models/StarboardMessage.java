package dev.sheldan.oneplus.bot.migration.starboard.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "StarboardMessages")
@Getter
@Setter
public class StarboardMessage {
    @Id
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "starboard_message_id")
    private Long starboardMessageId;

    @Column(name = "star_count")
    private Long starCount;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "ignored")
    private Boolean ignored;

    @Getter
    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "starboardMessage")
    private List<StarboardPostRelations> reactors;
}
