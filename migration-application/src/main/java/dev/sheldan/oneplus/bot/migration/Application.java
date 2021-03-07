package dev.sheldan.oneplus.bot.migration;

import dev.sheldan.oneplus.bot.migration.common.JobExecutor;
import dev.sheldan.oneplus.bot.migration.common.SetupJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
@ComponentScan(basePackages = {"dev.sheldan.abstracto", "dev.sheldan.oneplus"})
public class Application implements CommandLineRunner {

    @Autowired
    private JobExecutor executor;

    @Autowired
    private List<SetupJob> setupJobs;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Executing setup jobs.");
        setupJobs.forEach(setupJob -> {
            if(setupJob.enabled()) {
                log.info("Executing setup job {}.", setupJob.getClass().getSimpleName());
                setupJob.execute();
            }
        });
        log.info("Finished setup jobs.");
        Arrays.stream(args).forEach(s -> executor.executeJob(s));
    }
}
