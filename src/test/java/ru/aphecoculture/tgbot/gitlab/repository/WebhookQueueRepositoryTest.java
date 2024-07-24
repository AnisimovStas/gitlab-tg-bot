package ru.aphecoculture.tgbot.gitlab.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.aphecoculture.tgbot.gitlab.cache.WebhookQueueCache;
import ru.aphecoculture.tgbot.gitlab.utils.TestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {WebhookQueueRepository.class})
class WebhookQueueRepositoryTest {

    @Autowired
    WebhookQueueRepository repository;

    @MockBean
    WebhookQueueCache queue;

    @Test
    void add() {
        BotApiMethod message = TestUtils.botApiMethodStub("text");
        when(queue.size()).thenReturn(0);
        repository.add(message);
        verify(queue).put(1, message);
    }

    @Test
    void clearQueue() {
        repository.clearQueue();
        verify(queue).clear();
    }

    @Test
    void getAllAsList() {
        BotApiMethod message = TestUtils.botApiMethodStub("text");
        when(queue.values()).thenReturn(List.of(message));
        List<BotApiMethod> expected = List.of(message);
        List<BotApiMethod> result = repository.getAllAsList();
        assertEquals(1, result.size());
        assertEquals(expected, result);

    }
}