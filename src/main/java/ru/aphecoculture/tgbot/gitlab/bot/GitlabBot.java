package ru.aphecoculture.tgbot.gitlab.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aphecoculture.ecovision.tgbot.commons.exception.BotException;
import ru.aphecoculture.ecovision.tgbot.commons.exception.UpdateHandlerException;
import ru.aphecoculture.ecovision.tgbot.commons.update.handler.UpdateHandler;
import ru.aphecoculture.tgbot.gitlab.config.properties.GitlabBotProperties;
import ru.aphecoculture.tgbot.gitlab.handler.scheduled.SchedulerChecker;

import java.util.List;

@Slf4j
public class GitlabBot extends TelegramLongPollingBot {


    private final GitlabBotProperties properties;
    private final UpdateHandler updateHandler;
    private final SchedulerChecker schedulerChecker;


    public GitlabBot(GitlabBotProperties properties, UpdateHandler updateHandler, SchedulerChecker schedulerChecker) {
        super(properties.botToken());
        this.properties = properties;
        this.updateHandler = updateHandler;
        this.schedulerChecker = schedulerChecker;
    }


    @Override
    public void onUpdateReceived(Update update) {
        log.info(update.toString());

        List<BotApiMethod> responseMessages;
        try {
            responseMessages = updateHandler.handleUpdate(update);

        } catch (UpdateHandlerException e) {
            throw new BotException(e);
        }

        responseMessages.forEach(message -> {
            try {
                execute(message);

            } catch (TelegramApiException e) {
                throw new BotException(e);
            }
        });
    }

    @Scheduled(fixedRate = 5000)
    private void scheduledMessages() {
        List<BotApiMethod> responseMessages = schedulerChecker.scheduledHandler();

        responseMessages.forEach(message -> {
            try {
                execute(message);

            } catch (TelegramApiException e) {
                throw new BotException(e);
            }
        });


    }


    @Override
    public String getBotUsername() {
        return properties.botName();
    }

}