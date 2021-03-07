package dev.sheldan.oneplus.bot.migration.starboard.repository;

import dev.sheldan.oneplus.bot.migration.starboard.models.StarboardMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarboardMessagesRepository extends JpaRepository<StarboardMessage, Long> {
}
