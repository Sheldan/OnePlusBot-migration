package dev.sheldan.oneplus.bot.migration.profanities.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UsedProfanity")
@Getter
@Setter
public class LegacyProfanityUses {

    @Id
    @Column(name = "report_message_id")
    private Long reportMessageId;

    @Column(name = "user_id")
    private Long profaneUserId;

    @Column(name = "profanity_id")
    private Long profanityId;

    @Column(name = "valid")
    private Boolean valid;

    @Column(name = "message_id")
    private Long profaneMessageId;

    @Column(name = "channel_id")
    private Long profaneChannelId;

}
