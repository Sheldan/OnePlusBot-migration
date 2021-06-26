package dev.sheldan.oneplus.bot.migration.faq.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "FAQCommandChannelEntry")
@Getter
@Setter
public class FAQCommandChannelEntry {
    @Id
    @Column(name = "entry_id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "is_embed")
    private Boolean isEmbed;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "hex_color")
    private Integer hexColor;

    @Column(name = "author")
    private String author;

    @Column(name = "author_avatar_url")
    private String authorAvatarURL;

    @Column(name = "position")
    private Integer position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "command_channel_id_reference", nullable = false)
    private FAQCommandChannel faqCommandChannel;
}
