package dev.sheldan.oneplus.bot.migration.faq.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ChannelGroups")
@Getter
@Setter
public class ChannelGroup {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
