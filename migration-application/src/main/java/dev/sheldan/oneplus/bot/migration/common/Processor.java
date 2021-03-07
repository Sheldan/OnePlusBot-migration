package dev.sheldan.oneplus.bot.migration.common;

public interface Processor<S, T> {
    T process(S original);
}
