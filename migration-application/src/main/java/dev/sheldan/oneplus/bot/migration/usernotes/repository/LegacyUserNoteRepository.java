package dev.sheldan.oneplus.bot.migration.usernotes.repository;

import dev.sheldan.oneplus.bot.migration.usernotes.models.LegacyUserNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyUserNoteRepository extends JpaRepository<LegacyUserNote, Long> {
}
