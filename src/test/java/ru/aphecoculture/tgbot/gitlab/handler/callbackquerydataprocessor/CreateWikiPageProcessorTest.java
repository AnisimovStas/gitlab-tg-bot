package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.aphecoculture.tgbot.gitlab.BaseSuiteTest;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.repository.ReportRepository;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static ru.aphecoculture.tgbot.gitlab.BaseSuiteTest.TELEGRAM_ID;

@SpringBootTest(classes = {CreateWikiPageProcessor.class})
class CreateWikiPageProcessorTest {
    private final String CALLBACK_DATA = "create_wiki_page_reportId_1_projectId_1";
    @Autowired
    CreateWikiPageProcessor createWikiPageProcessor;

    @MockBean
    GitlabProjectCacheRepository projectCacheRepository;

    @MockBean
    ReportRepository reportRepository;

    @MockBean
    GitlabService gitlabService;

    @Test
    void doesProcess() {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);
        assertTrue(createWikiPageProcessor.doesProcess(callbackQuery));
    }

    @Test
    void processCallbackQuery() {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);

        int reportId = 1;
        Long projectId = 1L;

        when(reportRepository.getById(reportId)).thenReturn(Optional.of("<b><i>title</i></b> \n \n content"));
        when(gitlabService.createWikiPage(projectId, "title", "content")).thenReturn("wiki-page-link");

        String expectedText = "Создана страница на wiki: <a href=\"wiki-page-link\">title</a>";

        BotApiMethod expectedMessage = SendMessage
                .builder()
                .chatId(TELEGRAM_ID)
                .text(expectedText)
                .parseMode("html")
                .build();

        List<BotApiMethod> result = createWikiPageProcessor.processCallbackQuery(callbackQuery);

        assertEquals(1, result.size());
        assertEquals(expectedMessage, result.getFirst());

    }

}
