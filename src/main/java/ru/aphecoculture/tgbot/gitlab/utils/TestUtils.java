package ru.aphecoculture.tgbot.gitlab.utils;

import org.gitlab4j.api.models.MergeRequest;

public class TestUtils {

    public static MergeRequest mergeRequestStub(Long number) {
        MergeRequest mockMr = new MergeRequest();
        mockMr.setTitle("mr %d".formatted(number));
        mockMr.setIid(number);

        return mockMr;
    }

}
