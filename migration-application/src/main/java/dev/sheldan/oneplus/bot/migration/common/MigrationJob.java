package dev.sheldan.oneplus.bot.migration.common;

public interface MigrationJob {
    String getKey();
    void execute();
}
