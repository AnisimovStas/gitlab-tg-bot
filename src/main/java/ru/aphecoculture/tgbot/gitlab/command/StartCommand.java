package ru.aphecoculture.tgbot.gitlab.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aphecoculture.ecovision.tgbot.commons.command.Command;
import ru.aphecoculture.tgbot.gitlab.ui.markup.MainMenuMarkup;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    public static final String START_COMMAND = "/start";
    //TODO написать нормальный интро текст
    public static final String INTRO = """
            Привет!
                        
            Я бот который упрощает разработчикам взаимодействие с gitlab!  
            """;


    @Override
    public String getName() {
        return START_COMMAND;
    }

    @Override
    public List<BotApiMethod> execute(Update update) {
        List<BotApiMethod> messages = new ArrayList<>();
        Long chatId = update.getMessage().getChatId();

        messages.add(SendMessage.builder().text(INTRO).replyMarkup(MainMenuMarkup.MARKUP).chatId(chatId).build());

        return messages;
    }


}
