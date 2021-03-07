package dev.sheldan.oneplus.bot.migration.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class JobExecutor {
    @Autowired
    private List<MigrationJob> jobs;

    public void executeJob(String key) {
        log.info("Trying to execute job with key {}.", key);
        jobs.forEach(migrationJob -> {
            if(migrationJob.getKey().equalsIgnoreCase(key)) {
                log.info("Executing job {}.", migrationJob.getClass().getCanonicalName());
                migrationJob.execute();
            }
        });
    }
}
