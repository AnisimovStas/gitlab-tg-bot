package ru.aphecoculture.tgbot.gitlab.utils;

import org.gitlab4j.api.models.MergeRequest;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;

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

}
