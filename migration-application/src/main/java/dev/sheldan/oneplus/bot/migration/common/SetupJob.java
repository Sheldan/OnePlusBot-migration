package dev.sheldan.oneplus.bot.migration.common;

public interface SetupJob {
    void execute();
    default boolean enabled() {
        return true;
    }
}
