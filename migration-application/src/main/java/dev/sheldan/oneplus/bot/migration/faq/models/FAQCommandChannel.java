package dev.sheldan.oneplus.bot.migration.faq.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FaqCommandChannel")
@Getter
@Setter
public class FAQCommandChannel {
    @Id
    @Column(name = "command_channel_id")
    private Long commandChannelId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "command_id", nullable = false)
    private FAQCommand command;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "channel_group_id", nullable = false)
    private ChannelGroup channelGroup;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "faqCommandChannel")
    private List<FAQCommandChannelEntry> entries;

}
