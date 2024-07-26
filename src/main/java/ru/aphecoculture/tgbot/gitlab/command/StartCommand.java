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
    public static final String INTRO = """
            Привет!
                        
            Я бот который упрощает разработчикам взаимодействие с gitlab!  
                        
            На текущий момент, у меня 2 основных функционала:
            • Создавать сообщения о релизах с возможностью отправлять их в нужный тг чат и добавлять в gitlab wiki
            • В фоновом режиме отслеживать новые merge request и информировать о них в тг чат         
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
