package ru.aphecoculture.tgbot.gitlab.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GitlabProject {
    private Long id;
    private String name;
    private Long chatId;
    private Long topicId;
    private Long lastMRId;
    private List<User> users;
}
