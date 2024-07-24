package ru.aphecoculture.tgbot.gitlab.utils;

import org.gitlab4j.api.models.MergeRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.model.User;

public class TestUtils {

    public static MergeRequest mergeRequestStub(Long number) {
        MergeRequest mockMr = new MergeRequest();
        mockMr.setTitle("fix: mr %d".formatted(number));
        mockMr.setIid(number);

        return mockMr;
    }

    public static GitlabProject projectStub() {
        return GitlabProject.builder()
                .id(1L)
                .name("project 1")
                .chatId(1L)
                .topicId(1L)
                .lastMRId(null)
                .build();
    }

    public static User UserStub(int number) {
        return User.builder()
                .id(number)
                .gitlabUsername(String.valueOf(number))
                .telegramUsername(String.valueOf(number))
                .build();
    }

    public static BotApiMethod botApiMethodStub(String text) {
        return SendMessage.builder()
                .text(text)
                .chatId(1L)
                .build();
    }

}
