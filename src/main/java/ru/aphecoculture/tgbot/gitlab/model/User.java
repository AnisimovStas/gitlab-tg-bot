package ru.aphecoculture.tgbot.gitlab.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    private int id;
    private String gitlabUsername;
    private String telegramUsername;
}
