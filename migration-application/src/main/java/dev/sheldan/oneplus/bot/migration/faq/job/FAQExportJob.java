package dev.sheldan.oneplus.bot.migration.faq.job;

import com.google.gson.Gson;
import dev.sheldan.oneplus.bot.migration.common.MigrationJob;
import dev.sheldan.oneplus.bot.migration.faq.models.FAQCommand;
import dev.sheldan.oneplus.bot.migration.faq.models.FAQCommandChannelEntry;
import dev.sheldan.oneplus.bot.migration.faq.repository.LegacyFAQCommandRepository;
import dev.sheldan.oneplus.bot.modules.faq.models.command.config.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FAQExportJob implements MigrationJob {

    @Autowired
    private LegacyFAQCommandRepository repository;

    @Autowired
    private Gson gson;

    @Override
    @Transactional
    public void execute() {
        List<FAQCommand> commands = repository.findAll();
        log.info("Found {} commands.", commands.size());
        List<FaqCommandConfig> exportingCommands = commands
                .stream()
                .map(this::convertFAQCommand)
                .collect(Collectors.toList());

        String config = gson.toJson(exportingCommands);
        File target = new File("export.json");
        try (FileWriter fileWriter = new FileWriter(target)) {
            log.info("Writing to {}.", target.getAbsolutePath());
            fileWriter.write(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FaqCommandConfig convertFAQCommand(FAQCommand faqCommand) {
        log.info("Converting command {}.", faqCommand.getName());
        List<FaqCommandResponseConfig> responses = faqCommand
                .getChannels()
                .stream()
                .map(this::convertFAQCommandResponseConfig)
                .collect(Collectors.toList());
        Set<String> aliases = Arrays.stream(faqCommand.getAliases().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
        return FaqCommandConfig
                .builder()
                .faqCommandName(faqCommand.getName())
                .aliases(new ArrayList<>(aliases))
                .responses(responses)
                .global(false)
                .build();
    }

    private FaqCommandResponseConfig convertFAQCommandResponseConfig(dev.sheldan.oneplus.bot.migration.faq.models.FAQCommandChannel faqCommandChannel) {
        log.info("Converting response for channel group {}.", faqCommandChannel.getChannelGroup().getName());
        List<FaqCommandResponseMessageConfig> messages = faqCommandChannel
                .getEntries()
                .stream()
                .map(this::getFromEntry)
                .collect(Collectors.toList());
        return FaqCommandResponseConfig
                .builder()
                .messages(messages)
                .channelGroupName(faqCommandChannel.getChannelGroup().getName())
                .build();
    }

    private FaqCommandResponseMessageConfig getFromEntry(FAQCommandChannelEntry entry) {

        FaqCommandResponseMessageConfig.FaqCommandResponseMessageConfigBuilder builder = FaqCommandResponseMessageConfig
                .builder()
                .position(entry.getPosition());
        if(entry.getIsEmbed()) {
            FaqCommandResponseEmbedConfig embed = FaqCommandResponseEmbedConfig
                    .builder()
                    .description(entry.getText())
                    .imageUrl(entry.getImageURL())
                    .color(FaqCommandResponseEmbedColorConfig
                            .builder()
                            .red(0)
                            .green(0)
                            .blue(0)
                            .build())
                    .author(FaqCommandResponseEmbedAuthorConfig
                            .builder()
                            .useBot(true)
                            .build())
                    .build();

            builder = builder.embed(embed);
        } else {
            builder = builder.additionalMessage(entry.getText());
        }

        return builder.build();
    }

    @Override
    public String getKey() {
        return "faq";
    }
}
