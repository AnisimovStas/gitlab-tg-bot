package ru.aphecoculture.tgbot.gitlab.handler.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.aphecoculture.tgbot.gitlab.BaseSuiteTest;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.aphecoculture.tgbot.gitlab.BaseSuiteTest.TELEGRAM_ID;

@SpringBootTest(classes = {CreateReleaseNoteStrategy.class})
class CreateReleaseNoteStrategyTest {

    @Autowired
    CreateReleaseNoteStrategy createReleaseNoteStrategy;

    @MockBean
    GitlabService service;


    @Test
    void getPriority() {
        assertEquals(4, createReleaseNoteStrategy.getPriority());
    }

    @Test
    void doesFollowCondition() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().hasText()).thenReturn(true);
        when(update.getMessage().getText()).thenReturn("Сформировать сообщение о релизе");
        when(message.isCommand()).thenReturn(false);
        when(message.isUserMessage()).thenReturn(true);
        when(message.hasPhoto()).thenReturn(false);
        when(message.hasLocation()).thenReturn(false);
        when(message.hasContact()).thenReturn(false);


        assertTrue(createReleaseNoteStrategy.doesFollowCondition(update));
    }

    @Test
    void getUserId() {
        Update update = BaseSuiteTest.mockUpdateWithMessage();

        assertEquals(TELEGRAM_ID, createReleaseNoteStrategy.getUserId(update));
    }

    @Test
    void processUpdate_ShouldReturnMessageWithSelectProjectButton() {
        Update update = BaseSuiteTest.mockUpdateWithMessage();

        GitlabProject project = BaseSuiteTest.getDefaultProject();
        when(service.getAllProjects()).thenReturn(List.of(project));
        List<BotApiMethod> result = createReleaseNoteStrategy.execute(update);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (GitlabProject gitlabProject : List.of(project)) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(gitlabProject.getName());
            button.setCallbackData("select_projectId_%s".formatted(gitlabProject.getId()));
            keyboard.add(List.of(button));
        }
        InlineKeyboardMarkup markup =
                InlineKeyboardMarkup.builder()
                        .keyboard(keyboard)
                        .build();


        BotApiMethod expectedMessage = SendMessage.builder()
                .chatId(TELEGRAM_ID)
                .text("Выберите проект для которого нужно сформировать релиз")
                .replyMarkup(markup)
                .build();

        assertEquals(1, result.size());
        assertEquals(expectedMessage, result.get(0));
    }

}
