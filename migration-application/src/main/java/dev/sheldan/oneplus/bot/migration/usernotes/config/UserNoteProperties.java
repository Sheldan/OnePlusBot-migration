package dev.sheldan.oneplus.bot.migration.usernotes.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "migration.config.profanities")
public class UserNoteProperties {

}