package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.aphecoculture.ecovision.tgbot.commons.callbackquery.CallbackQueryProcessor;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;
import ru.aphecoculture.tgbot.gitlab.service.ReportService;
import ru.aphecoculture.tgbot.gitlab.utils.CallbackDataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateReportProcessor implements CallbackQueryProcessor {

    @Autowired
    ReportService reportService;

    @Autowired
    GitlabService gitlabService;

    @Override
    public boolean doesProcess(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith("generate_report_projectId");
    }

    @SneakyThrows
    @Override
    public List<BotApiMethod> processCallbackQuery(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();
        Long projectId = CallbackDataUtils.getProjectIdFromCallback(data);
        Long fromMRId = CallbackDataUtils.getFromMRIdFromCallback(data);
        Long toMRId = CallbackDataUtils.getToMRIdFromCallback(data);

        List<MergeRequest> releaseMrs = gitlabService.getRangeOfMRs(projectId, fromMRId, toMRId);

        Optional<GitlabProject> project = gitlabService.getProjectById(projectId);

        if (project.isEmpty()) {
            //TODO Убрать RuntimeException
            throw new RuntimeException();
        }

        String reportData = reportService.processMrListToReport(releaseMrs, project.get().getName());

        Integer reportId = reportService.addReport(reportData);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Отправить сообщение о релизе в группу");
        button.setCallbackData("send_to_group_reportId_%d_projectId_%d".formatted(reportId, project.get().getId()));
        keyboard.add(List.of(button));


        return List.of(SendMessage
                .builder()
                .chatId(userId)
                .text(reportData)
                .replyMarkup(
                        InlineKeyboardMarkup.builder()
                                .keyboard(keyboard)
                                .build()
                )
                .parseMode("html")
                .build()
        );
    }

}
