package ru.aphecoculture.tgbot.gitlab.ui.markup;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainMenuMarkup {
    public static final ReplyKeyboardMarkup MARKUP;

    public static final String CREATE_RELEASE_NOTE = "Сформировать сообщение о релизе";

    static {
        MARKUP = ReplyKeyboardMarkup.builder()
                .keyboard(
                        List.of(new KeyboardRow(List.of(
                                KeyboardButton.builder().text(CREATE_RELEASE_NOTE).build()))))
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();

    }

}
