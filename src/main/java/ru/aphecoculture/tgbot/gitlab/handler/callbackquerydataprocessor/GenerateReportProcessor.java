package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.gitlab4j.api.models.MergeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.aphecoculture.ecovision.tgbot.commons.callbackquery.CallbackQueryProcessor;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;
import ru.aphecoculture.tgbot.gitlab.service.ReportService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateReportProcessor implements CallbackQueryProcessor {

    private static final Logger log = LoggerFactory.getLogger(GenerateReportProcessor.class);
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
        Long projectId = reportService.getProjectIdFromCallback(data);
        Long fromMRId = reportService.getFromMRIdFromCallback(data);
        Long toMRId = reportService.getToMRIdFromCallback(data);

        List<MergeRequest> releaseMrs = gitlabService.getRangeOfMRs(projectId, fromMRId, toMRId);


        String projectName = gitlabService.getProjectNameByProjectId(projectId);

        String report = reportService.processMrListToReport(releaseMrs, projectName);

        //-100 + chat id
//        Long chatId = -100217XXX195L;

        return List.of(SendMessage
                        .builder()
                        .chatId(userId)
//                .chatId(chatId)
//                .messageThreadId(2)
                        .text(report)
                        .parseMode("html")
                        .build()
        );
    }

}
