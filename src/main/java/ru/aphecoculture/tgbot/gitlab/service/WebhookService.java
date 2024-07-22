package ru.aphecoculture.tgbot.gitlab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.model.WebhookMRDetails;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.repository.WebhookQueueRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    @Autowired
    WebhookQueueRepository webhookQueueRepository;

    @Autowired
    GitlabProjectCacheRepository projectRepository;

    public void addMRToQueue(WebhookMRDetails details) {
        try {
            Long projectId = details.getProject().getId();
            Optional<GitlabProject> project = projectRepository.getById(projectId);

            if (project.isEmpty()) {
                throw new RuntimeException();
            }


            String messageContent = createMRMessageContent(project.get(), details);

            SendMessage message = SendMessage.builder()
                    .chatId(project.get().getChatId())
                    .messageThreadId(Math.toIntExact(project.get().getTopicId()))
                    .text(messageContent)
                    .parseMode("html")
                    .build();


            webhookQueueRepository.add(message);
        } catch (Exception e) {
            log.error("Error during addMRToQueue() in WebhookService: {}", e.getMessage());
            //400 because 500 errors not allowed for gitlab webhooks
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during addMRToQueue() in WebhookService: %s".formatted(e.getMessage()));
        }
    }

    private String createMRMessageContent(GitlabProject project, WebhookMRDetails details) {
        int authorId = details.getObject_attributes().getAuthor_id();
        String mrTitle = details.getObject_attributes().getTitle();
        String mrURL = details.getObject_attributes().getUrl();

        List<String> tgUsersToNotify = projectRepository.getUsersTelegramUsernameExceptMRCreator(project.getId(), authorId);

        StringBuilder message = new StringBuilder();
        for (String tgUser : tgUsersToNotify) {
            String notification = "@%s".formatted(tgUser) + " ";
            message.append(notification);
        }
        message.append("\n \n");

        String projectNameText = "<b>%s</b>\nНовый мр к рассмотрению: \n\n".formatted(project.getName());

        message.append(projectNameText);

        String mrLink = "<a href=\"%s\">%s</a>".formatted(mrURL, mrTitle);
        message.append(mrLink);

        return message.toString();
    }
}
