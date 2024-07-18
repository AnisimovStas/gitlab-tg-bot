package ru.aphecoculture.tgbot.gitlab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.aphecoculture.ecovision.tgbot.commons.update.handler.UpdateHandler;
import ru.aphecoculture.tgbot.gitlab.bot.GitlabBot;
import ru.aphecoculture.tgbot.gitlab.config.properties.GitlabBotProperties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GitlabBotProperties.class)
public class BotConfig {
     
    //TODO  monitoringBot?
    @Bean
    public GitlabBot monitoringBot(UpdateHandler updateHandler, GitlabBotProperties properties) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        GitlabBot bot = new GitlabBot(properties, updateHandler);
        botsApi.registerBot(bot);
        return bot;
    }

}
