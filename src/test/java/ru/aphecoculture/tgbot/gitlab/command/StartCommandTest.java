package ru.aphecoculture.tgbot.gitlab.command;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aphecoculture.tgbot.gitlab.ui.markup.MainMenuMarkup;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static ru.aphecoculture.tgbot.gitlab.BaseSuiteTest.TELEGRAM_ID;
import static ru.aphecoculture.tgbot.gitlab.BaseSuiteTest.mockUpdateWithMessage;
import static ru.aphecoculture.tgbot.gitlab.command.StartCommand.INTRO;
import static ru.aphecoculture.tgbot.gitlab.command.StartCommand.START_COMMAND;

@SpringBootTest(classes = {StartCommand.class})
class StartCommandTest {

    @Autowired
    StartCommand command;

    @Test
    void getName() {
        assertEquals(START_COMMAND, command.getName());
    }

    @Test
    void execute() {
        Update update = mockUpdateWithMessage();
        List<BotApiMethod> messages = command.execute(update);
 
        assertIterableEquals(
                List.of(SendMessage.builder().chatId(TELEGRAM_ID).text(INTRO).replyMarkup(MainMenuMarkup.MARKUP).build()),
                messages
        );
    }
}
