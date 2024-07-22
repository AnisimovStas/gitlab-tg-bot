package ru.aphecoculture.tgbot.gitlab.ui.button;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ProjectButton {
    //TODO Добавить остальные проекты
    ECOVISION_WEB("Ecovision WEB"),
    ECOVISION("Ecovision");

    public static final Set<String> DATA_LIST =
            Arrays.stream(ProjectButton.values())
                    .map(Enum::name)
                    .collect(Collectors.toUnmodifiableSet());

    private final InlineKeyboardButton button;
    private final String value;

    ProjectButton(String value) {
        this.value = value;
        this.button = InlineKeyboardButton.builder()
                .text(value)
                .callbackData(this.name())
                .build();
    }

    @Override
    public String toString() {
        return value;
    }

}
