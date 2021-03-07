package dev.sheldan.oneplus.bot.migration.starboard.models;

import dev.sheldan.oneplus.bot.migration.starboard.models.embeds.StarboardPostRelationId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "StarboardPostRelations")
@Getter
@Setter
public class StarboardPostRelations {
    @EmbeddedId
    private StarboardPostRelationId relationIds;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("messageId")
    @JoinColumn(name = "message_id", referencedColumnName = "message_id", nullable = false)
    private StarboardMessage starboardMessage;
}
