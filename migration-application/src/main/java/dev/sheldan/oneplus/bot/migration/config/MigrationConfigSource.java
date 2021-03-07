package dev.sheldan.oneplus.bot.migration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:migration-config.properties")
public class MigrationConfigSource {
}
