package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.aphecoculture.tgbot.gitlab.BaseSuiteTest;
import ru.aphecoculture.tgbot.gitlab.exception.GitlabProjectException;
import ru.aphecoculture.tgbot.gitlab.exception.ReportException;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.repository.ReportRepository;
import ru.aphecoculture.tgbot.gitlab.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static ru.aphecoculture.tgbot.gitlab.BaseSuiteTest.TELEGRAM_ID;

@SpringBootTest(classes = {SendMessageToGroupProcessor.class})
class SendMessageToGroupProcessorTest {

    private final String CALLBACK_DATA = "send_to_group_reportId_1_projectId_1";

    @Autowired
    SendMessageToGroupProcessor sendMessageToGroupProcessor;

    @MockBean
    GitlabProjectCacheRepository projectCacheRepository;

    @MockBean
    ReportRepository reportRepository;

    @Test
    void doesProcess() {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);
        assertTrue(sendMessageToGroupProcessor.doesProcess(callbackQuery));
    }

    @Test
    void processCallbackQuery() throws ReportException, GitlabProjectException {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);
        Long projectId = 1L;
        int reportId = 1;

        when(projectCacheRepository.getById(projectId)).thenReturn(Optional.ofNullable(TestUtils.projectStub()));
        when(reportRepository.getById(reportId)).thenReturn(Optional.of("report data"));


        List<BotApiMethod> expectedMessages = new ArrayList<>();

        SendMessage messageToGroup = SendMessage.builder()
                .chatId(TestUtils.projectStub().getChatId())
                .messageThreadId(Math.toIntExact(TestUtils.projectStub().getTopicId()))
                .text("report data")
                .parseMode("html")
                .build();

        expectedMessages.add(messageToGroup);

        SendMessage messageToBotChat = SendMessage.builder()
                .chatId(TELEGRAM_ID)
                .text("Сообщение отправлено в группу")
                .build();

        expectedMessages.add(messageToBotChat);

        List<BotApiMethod> result = sendMessageToGroupProcessor.processCallbackQuery(callbackQuery);

        assertEquals(2, result.size());
        assertEquals(expectedMessages.getFirst(), result.getFirst());
        assertEquals(expectedMessages.getLast(), result.getLast());

    }
}
