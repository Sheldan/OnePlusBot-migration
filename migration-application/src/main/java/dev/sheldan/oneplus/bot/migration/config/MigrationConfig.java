package dev.sheldan.oneplus.bot.migration.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "migration")
public class MigrationConfig {
    private Map<String, String> config;
    private Map<String, Long> longConfig = new HashMap<>();

    public Long getServerId() {
        return longConfig.get("serverId");
    }

    public Long getStarboardChannelId() {
        return longConfig.get("starboardChannelId");
    }
    public Long getDefaultStarboardSourceChannel() {
        return longConfig.get("defaultStarboardSourceChannelId");
    }

    @PostConstruct
    public void convertIds() {
        config.keySet().forEach(s -> {
            if(NumberUtils.isParsable(config.get(s))) {
                longConfig.put(s, Long.parseLong(config.get(s)));
            }
        });
    }
}
