package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.aphecoculture.ecovision.tgbot.commons.callbackquery.CallbackQueryProcessor;
import ru.aphecoculture.tgbot.gitlab.exception.GitlabProjectException;
import ru.aphecoculture.tgbot.gitlab.exception.ReportException;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.repository.ReportRepository;
import ru.aphecoculture.tgbot.gitlab.utils.CallbackDataUtils;

import java.util.List;
import java.util.Optional;

@Component
public class SendMessageToGroupProcessor implements CallbackQueryProcessor {

    @Autowired
    GitlabProjectCacheRepository projectCacheRepository;

    @Autowired
    ReportRepository reportRepository;

    @Override
    public boolean doesProcess(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith("send_to_group");
    }


    @Override
    public List<BotApiMethod> processCallbackQuery(CallbackQuery callbackQuery) throws GitlabProjectException, ReportException {
        Long userId = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();

        Long projectId = CallbackDataUtils.getProjectIdFromCallback(data);
        Optional<GitlabProject> project = projectCacheRepository.getById(projectId);

        if (project.isEmpty()) {
            throw new GitlabProjectException(GitlabProjectException.PROJECT_NOT_FOUND);
        }

        Long topicId = project.get().getTopicId();
        Long groupChatId = project.get().getChatId();

        Integer reportId = CallbackDataUtils.getReportIdFromCallback(data);

        Optional<String> reportData = reportRepository.getById(reportId);

        if (reportData.isEmpty()) {
            throw new ReportException(ReportException.REPORT_NOT_FOUND);
        }

        SendMessage messageToGroup = SendMessage.builder()
                .chatId(groupChatId)
                .messageThreadId(Math.toIntExact(topicId))
                .text(reportData.get())
                .parseMode("html")
                .build();

        SendMessage messageToBotChat = SendMessage.builder()
                .chatId(userId)
                .text("Сообщение отправлено в группу")
                .build();
        return List.of(messageToGroup, messageToBotChat);
    }
}
