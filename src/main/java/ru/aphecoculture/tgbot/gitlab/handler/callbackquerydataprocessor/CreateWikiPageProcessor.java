package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.aphecoculture.ecovision.tgbot.commons.callbackquery.CallbackQueryProcessor;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.repository.ReportRepository;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;
import ru.aphecoculture.tgbot.gitlab.utils.CallbackDataUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public List<BotApiMethod> processCallbackQuery(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();
        Long projectId = CallbackDataUtils.getProjectIdFromCallback(data);
        Integer reportId = CallbackDataUtils.getReportIdFromCallback(data);
        Optional<String> reportData = reportRepository.getById(reportId);
        if (reportData.isEmpty()) {
            //TODO Убрать RuntimeException
            throw new RuntimeException();
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

    private String processPageContent(String report) {
        String trimmedReport = removeFirstTwoLines(report);
        return addLineBreaks(trimmedReport);
    }


    private String processPageTitle(String report) {
        String pattern = "<b><i>(.*?)</i></b>";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(report);
        if (m.find()) {
            return m.group(1);
        } else {
            return "";
        }
    }

    private String addLineBreaks(String input) {
        return input.replaceAll("\\n", "<br>");
    }

    private String removeFirstTwoLines(String input) {
        String[] lines = input.split("\\r?\\n");
        StringBuilder result = new StringBuilder();
        for (int i = 2; i < lines.length; i++) {
            result.append(lines[i]).append("\n");
        }
        return result.toString().trim();
    }
}
