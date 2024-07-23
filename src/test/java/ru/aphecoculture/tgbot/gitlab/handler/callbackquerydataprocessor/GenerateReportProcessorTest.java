package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.aphecoculture.tgbot.gitlab.BaseSuiteTest;
import ru.aphecoculture.tgbot.gitlab.config.properties.GitlabProperties;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;
import ru.aphecoculture.tgbot.gitlab.service.ReportService;
import ru.aphecoculture.tgbot.gitlab.utils.MergeRequestUtils;
import ru.aphecoculture.tgbot.gitlab.utils.ReportUtils;
import ru.aphecoculture.tgbot.gitlab.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static ru.aphecoculture.tgbot.gitlab.BaseSuiteTest.TELEGRAM_ID;

@SpringBootTest(classes = {GenerateReportProcessor.class})
class GenerateReportProcessorTest {

    private final String CALLBACK_DATA = "generate_report_projectId_1_fromMRId_10_toMRId_2";

    @Autowired
    GenerateReportProcessor generateReportProcessor;

    @MockBean
    ReportService reportService;

    @MockBean
    GitlabService gitlabService;

    MergeRequestUtils mergeRequestUtils = new MergeRequestUtils(new GitlabProperties("1", "1", "1"));


    @Test
    void doesProcess() {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);
        assertTrue(generateReportProcessor.doesProcess(callbackQuery));
    }

    @Test
    void processCallbackQuery() throws GitLabApiException {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);
        Long projectId = 1L;
        Long fromMR = 10L;
        Long toMR = 2L;

        List<MergeRequest> mrs = List.of(TestUtils.mergeRequestStub(1L),
                TestUtils.mergeRequestStub(2L),
                TestUtils.mergeRequestStub(3L));

        when(gitlabService.getRangeOfMRs(projectId, fromMR, toMR)).thenReturn(mrs);
        when(gitlabService.getProjectById(projectId)).thenReturn(Optional.ofNullable(TestUtils.projectStub()));

        String reportData = ReportUtils.processMrListToReport(mrs, TestUtils.projectStub().getName());
        when(reportService.addReport(reportData)).thenReturn(1);


        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        InlineKeyboardButton sendToGroupButton = new InlineKeyboardButton();
        sendToGroupButton.setText("Отправить сообщение о релизе в группу");
        sendToGroupButton.setCallbackData("send_to_group_reportId_%d_projectId_%d".formatted(1, TestUtils.projectStub().getId()));

        InlineKeyboardButton createWikiButton = new InlineKeyboardButton();
        createWikiButton.setText("Создать страницу на вики");
        createWikiButton.setCallbackData("create_wiki_page_reportId_%d_projectId_%d".formatted(1, TestUtils.projectStub().getId()));


        keyboard.add(List.of(sendToGroupButton));
        keyboard.add(List.of(createWikiButton));


        BotApiMethod expectedMessage = SendMessage
                .builder()
                .chatId(TELEGRAM_ID)
                .text(reportData)
                .replyMarkup(
                        InlineKeyboardMarkup.builder()
                                .keyboard(keyboard)
                                .build()
                )
                .parseMode("html")
                .build();


        List<BotApiMethod> result = generateReportProcessor.processCallbackQuery(callbackQuery);

        assertEquals(1, result.size());
        assertEquals(expectedMessage, result.getFirst());
    }

}
