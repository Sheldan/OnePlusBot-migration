package dev.sheldan.oneplus.bot.migration.faq.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FaqCommand")
@Getter
@Setter
public class FAQCommand {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "aliases")
    private String aliases;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "command")
    private List<FAQCommandChannel> channels;
}
