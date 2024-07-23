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
import ru.aphecoculture.ecovision.tgbot.commons.exception.BotApplicationException;
import ru.aphecoculture.tgbot.gitlab.BaseSuiteTest;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;
import ru.aphecoculture.tgbot.gitlab.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.aphecoculture.tgbot.gitlab.BaseSuiteTest.TELEGRAM_ID;

@SpringBootTest(classes = {SelectProjectProcessor.class})
class SelectProjectProcessorTest {

    private final String CALLBACK_DATA = "select_projectId_1";

    @Autowired
    SelectProjectProcessor selectProjectProcessor;

    @MockBean
    GitlabService gitlabService;

    @Test
    void doesProcess() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        when(callbackQuery.getData()).thenReturn(CALLBACK_DATA);

        assertTrue(selectProjectProcessor.doesProcess(callbackQuery));
    }

    @Test
    void processZeroReleaseMR() throws GitLabApiException, BotApplicationException {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);

        when(gitlabService.getLatestRelease(1L)).thenReturn(new ArrayList<>());

        BotApiMethod expectedMessage = SendMessage.builder()
                .chatId(TELEGRAM_ID)
                .text("Не смог найти последний релиз, убедитесь, что вы создали MR, который начинается с \"Release\" и попробуйте заново")
                .build();

        List<BotApiMethod> result = selectProjectProcessor.processCallbackQuery(callbackQuery);

        assertEquals(1, result.size());
        assertEquals(expectedMessage, result.getFirst());
    }

    @Test
    void processTwoReleaseMR() throws GitLabApiException, BotApplicationException {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);

        MergeRequest mockMR1 = TestUtils.mergeRequestStub(1L);
        MergeRequest mockMR2 = TestUtils.mergeRequestStub(2L);
        when(gitlabService.getLatestRelease(1L)).thenReturn(List.of(mockMR1, mockMR2));

        String responseText = ("Последний релиз: %s. Если все так, то после нажатия кнопки \"Сформировать\" " +
                "появится отчет включающий все МРы между %s и %s").formatted(
                mockMR1.getTitle(), mockMR1.getTitle(), mockMR2.getTitle()
        );

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Сформировать");
        button.setCallbackData("generate_report_projectId_%d_fromMRId_%d_toMRId_%d".formatted(1L, mockMR1.getIid(), mockMR2.getIid()));
        keyboard.add(List.of(button));

        BotApiMethod expectedMessage = SendMessage.builder()
                .text(responseText)
                .chatId(TELEGRAM_ID)
                .replyMarkup(
                        InlineKeyboardMarkup.builder()
                                .keyboard(keyboard)
                                .build())
                .build();

        List<BotApiMethod> result = selectProjectProcessor.processCallbackQuery(callbackQuery);

        assertEquals(1, result.size());
        assertEquals(expectedMessage, result.getFirst());
    }

    @Test
    void processOneReleaseMr() throws GitLabApiException, BotApplicationException {
        CallbackQuery callbackQuery = BaseSuiteTest.mockCallbackQuery(CALLBACK_DATA);
        MergeRequest mockMR1 = TestUtils.mergeRequestStub(1L);

        when(gitlabService.getLatestRelease(1L)).thenReturn(List.of(mockMR1));

        String responseText = ("К сожалению, я смог найти только один релизный МР: %s \n" +
                "Для формирования границ релиза нужно минимум 2 МРа, которые содержат слово \"release\" \n" +
                "Попробуйте изменить название предыдущего мра, добавив в него \"release\" или попробуйте" +
                " воспользоваться функционалом при формировании следующего релиза")
                .formatted(mockMR1.getTitle());

        BotApiMethod expectedMessage = SendMessage.builder()
                .text(responseText)
                .chatId(TELEGRAM_ID)

                .build();

        List<BotApiMethod> result = selectProjectProcessor.processCallbackQuery(callbackQuery);

        assertEquals(1, result.size());
        assertEquals(expectedMessage, result.getFirst());
    }
}
