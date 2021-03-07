package dev.sheldan.oneplus.bot.migration.starboard.models.embeds;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StarboardPostRelationId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "message_id")
    private Long messageId;
}
