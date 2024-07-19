package ru.aphecoculture.tgbot.gitlab.handler.strategy;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.aphecoculture.ecovision.tgbot.commons.update.strategy.UpdateStrategy;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateReleaseNoteStrategy implements UpdateStrategy {

    @Autowired
    GitlabService gitlabService;

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public boolean doesFollowCondition(Update update) {
        Message message = update.getMessage();
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().equals("Сформировать сообщение о релизе") &&
                message.isUserMessage() &&
                !message.isCommand() &&
                !message.hasPhoto() &&
                !message.hasLocation() &&
                !message.hasContact();
    }

    @Override
    public Long getUserId(Update update) {
        return update.getMessage().getFrom().getId();
    }

    @SneakyThrows
    @Override
    public List<BotApiMethod> execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String responseText = "Выберите проект для которого нужно сформировать релиз";


        List<Project> projects = gitlabService.getAllProjects();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Project project : projects) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(project.getName());
            button.setCallbackData("select_projectId_%s".formatted(project.getId()));
            keyboard.add(List.of(button));
        }
        InlineKeyboardMarkup markup =
                InlineKeyboardMarkup.builder()
                        .keyboard(keyboard)
                        .build();


        return List.of(SendMessage.builder()
                .chatId(chatId)
                .text(responseText)
                .replyMarkup(markup)
                .build());
    }
}
