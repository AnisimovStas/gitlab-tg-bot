package ru.aphecoculture.tgbot.gitlab.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aphecoculture.ecovision.tgbot.commons.exception.BotException;
import ru.aphecoculture.ecovision.tgbot.commons.exception.UpdateHandlerException;
import ru.aphecoculture.ecovision.tgbot.commons.update.handler.UpdateHandler;
import ru.aphecoculture.tgbot.gitlab.config.properties.GitlabBotProperties;

import java.util.List;

@Slf4j
public class GitlabBot extends TelegramLongPollingBot {


    private final GitlabBotProperties properties;
    private final UpdateHandler updateHandler;


    public GitlabBot(GitlabBotProperties properties, UpdateHandler updateHandler) {
        super(properties.botToken());
        this.properties = properties;
        this.updateHandler = updateHandler;
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


//        // We check if the update has a message and the message has text
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            // Set variables
//            String message_text = update.getMessage().getText();
//            long chat_id = update.getMessage().getChatId();
//
//            GitLabApi gitLabApi = new GitLabApi(gitlabURL, gitlabToken);
//            try {
//                List<Project> projects = gitLabApi.getProjectApi().getProjects();
//                for (Project project : projects) {
//                    message_text += project.getName() + "\n";
//                }
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
//
//            SendMessage message = SendMessage // Create a message object
//                    .builder()
//                    .chatId(chat_id)
//                    .text(message_text)
//                    .build();
//            try {
//                telegramClient.execute(message); // Sending our message object to user
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }


    @Override
    public String getBotUsername() {
        return properties.botName();
    }

}