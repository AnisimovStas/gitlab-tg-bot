package ru.aphecoculture.tgbot.gitlab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.aphecoculture.ecovision.tgbot.commons.update.handler.UpdateHandler;
import ru.aphecoculture.tgbot.gitlab.bot.GitlabBot;
import ru.aphecoculture.tgbot.gitlab.config.properties.GitlabBotProperties;
import ru.aphecoculture.tgbot.gitlab.sheduler.SchedulerHandler;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GitlabBotProperties.class)
@EnableScheduling
public class BotConfig {

    @Bean
    public GitlabBot gitlabBot(UpdateHandler updateHandler, GitlabBotProperties properties, SchedulerHandler schedulerHandler) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        GitlabBot bot = new GitlabBot(properties, updateHandler, schedulerHandler);
        botsApi.registerBot(bot);
        return bot;
    }

}
