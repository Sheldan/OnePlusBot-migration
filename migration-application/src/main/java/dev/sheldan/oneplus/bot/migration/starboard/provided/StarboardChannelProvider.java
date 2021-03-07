package dev.sheldan.oneplus.bot.migration.starboard.provided;

import dev.sheldan.abstracto.core.exception.AbstractoRunTimeException;
import dev.sheldan.oneplus.bot.migration.config.MigrationConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * This component is needed, because in legacy there is no information about the source channel of a starboard post
 */
@Slf4j
@Component
public class StarboardChannelProvider {

    @Value("classpath:data/starboard-source.csv")
    private Resource resourceFile;

    @Autowired
    private MigrationConfig migrationConfig;

    private static final String[] HEADERS = { "messageId", "channelId"};

    private Map<Long, Long> postChannelMapping = new HashMap<>();

    public void loadPostChannelMapping() {
        Reader in = null;
        try {
            in = new FileReader(resourceFile.getFile());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(in);
            for (CSVRecord record : records) {
                Long messageId = Long.parseLong(record.get("messageId"));
                Long channelId = Long.parseLong(record.get("channelId"));
                postChannelMapping.put(messageId, channelId);
            }
            log.info("Loaded {} post-channel mappings.", postChannelMapping.keySet().size());
        } catch (IOException e) {
            log.error("Failed to load channel mappings.", e);
            throw new AbstractoRunTimeException(e);
        }

    }

    public Long getChannelIdForMessageId(Long messageId) {
        if(postChannelMapping.containsKey(messageId)) {
            return postChannelMapping.get(messageId);
        } else {
            log.warn("Message ID {} not found in mapping - using default channel.", messageId);
            return migrationConfig.getDefaultStarboardSourceChannel();
        }
    }

}
