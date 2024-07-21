package ru.aphecoculture.tgbot.gitlab.handler.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.aphecoculture.tgbot.gitlab.handler.strategy.ScheduleStrategy;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class MergeRequestChecker implements ScheduleStrategy {

    @Autowired
    GitlabService gitlabService;

    @Autowired
    GitlabProjectCacheRepository projectRepository;

    public List<BotApiMethod> check() {
        List<BotApiMethod> messages = new ArrayList<>();

        Long frontendId = 4L;
        Optional<GitlabProject> frontendProject = projectRepository.getById(frontendId);

        if (frontendProject.isEmpty()) {
            throw new RuntimeException();
        }

        MergeRequest lastMR = gitlabService.getLastCreatedMergeRequest(frontendProject.get().getId());

        if (lastMR == null) {
            return messages;
        }

        if (!Objects.equals(lastMR.getId(), frontendProject.get().getLastMRId())) {
            log.info("new frontend mr created: {}", lastMR.getTitle());
            if (frontendProject.get().getLastMRId() != null) {
                String messageContent = createMRMessageContent(frontendProject.get(), lastMR);

                SendMessage message = SendMessage.builder()
                        .chatId(frontendProject.get().getChatId())
                        .messageThreadId(Math.toIntExact(frontendProject.get().getTopicId()))
                        .text(messageContent)
                        .parseMode("html")
                        .build();

                messages.add(message);
            }

            frontendProject.get().setLastMRId(lastMR.getId());
            projectRepository.save(frontendProject.get());
        }


        return messages;
    }

    private String createMRMessageContent(GitlabProject project, MergeRequest mr) {
        String mrCreator = mr.getAuthor().getUsername();
        List<String> tgUsersToNotify = projectRepository.getUsersTelegramUsernameExceptMRCreator(project.getId(), mrCreator);

        StringBuilder message = new StringBuilder();
        for (String tgUser : tgUsersToNotify) {
            String notification = "@%s".formatted(tgUser) + " ";
            message.append(notification);
        }
        message.append("\n \n");

        String projectNameText = "<b>%s</b>\nНовый мр к рассмотрению: \n\n".formatted(project.getName());

        message.append(projectNameText);

        String mrLink = "<a href=\"%s\">%s</a>".formatted(mr.getWebUrl(), mr.getTitle());
        message.append(mrLink);

        return message.toString();
    }

}
