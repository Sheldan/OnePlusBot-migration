package dev.sheldan.oneplus.bot.migration.starboard.service;

import dev.sheldan.oneplus.bot.migration.starboard.models.StarboardMessage;
import dev.sheldan.oneplus.bot.migration.starboard.repository.StarboardMessagesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

@Slf4j
public class StarboardPostServiceBean {

    @Autowired
    private StarboardMessagesRepository repository;

    public List<StarboardMessage> getStarboardMessages() {
        return repository.findAll();
    }

}
