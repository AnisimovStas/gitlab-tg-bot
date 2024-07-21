package ru.aphecoculture.tgbot.gitlab;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.aphecoculture.tgbot.gitlab.config.properties.GitlabBotProperties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Disabled("This is a base test suite")
@AutoConfigureMockMvc
@EnableConfigurationProperties(value = {GitlabBotProperties.class, GitlabBotProperties.class})
@SpringBootTest
@ActiveProfiles("test")
public class BaseSuiteTest {

    public static final int MESSAGE_ID = 1;

    public static final Long TELEGRAM_ID = 1L;

    public static Update mockUpdateWithMessage() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User tgUser = mock(User.class);

        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getChatId()).thenReturn(TELEGRAM_ID);
        when(message.getFrom()).thenReturn(tgUser);
        when(tgUser.getId()).thenReturn(TELEGRAM_ID);
        return update;
    }

    public static CallbackQuery mockCallbackQuery(String data) {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        String callbackQueryId = "1";
        when(callbackQuery.getId()).thenReturn(callbackQueryId);

        User user =
                mock(User.class);
        when(user.getId()).thenReturn(TELEGRAM_ID);
        when(callbackQuery.getFrom()).thenReturn(user);

        Message message = mock(Message.class);
        when(message.getMessageId()).thenReturn(1);
        when(callbackQuery.getMessage()).thenReturn(message);

        when(callbackQuery.getData()).thenReturn(data);

        return callbackQuery;
    }

}


