package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.aphecoculture.ecovision.tgbot.commons.callbackquery.CallbackQueryProcessor;
import ru.aphecoculture.tgbot.gitlab.exception.ReportException;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.repository.ReportRepository;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;
import ru.aphecoculture.tgbot.gitlab.utils.CallbackDataUtils;

import java.util.List;
import java.util.Optional;

import static ru.aphecoculture.tgbot.gitlab.utils.WikiPageUtils.processPageContent;
import static ru.aphecoculture.tgbot.gitlab.utils.WikiPageUtils.processPageTitle;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateWikiPageProcessor implements CallbackQueryProcessor {

    @Autowired
    GitlabProjectCacheRepository projectCacheRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    GitlabService gitlabService;

    @Override
    public boolean doesProcess(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith("create_wiki_page");
    }

    @Override
    public List<BotApiMethod> processCallbackQuery(CallbackQuery callbackQuery) throws ReportException {
        Long userId = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();
        Long projectId = CallbackDataUtils.getProjectIdFromCallback(data);
        Integer reportId = CallbackDataUtils.getReportIdFromCallback(data);
        Optional<String> reportData = reportRepository.getById(reportId);
        if (reportData.isEmpty()) {
            throw new ReportException(ReportException.REPORT_NOT_FOUND);
        }

        String pageTitle = processPageTitle(reportData.get());
        String pageContent = processPageContent(reportData.get());

        String link = gitlabService.createWikiPage(projectId, pageTitle, pageContent);

        String messageText = "Создана страница на wiki: <a href=\"%s\">%s</a>".formatted(link, pageTitle);

        return List.of(SendMessage
                .builder()
                .chatId(userId)
                .text(messageText)
                .parseMode("html")
                .build()
        );
    }
}
