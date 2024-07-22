package ru.aphecoculture.tgbot.gitlab.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.aphecoculture.tgbot.gitlab.cache.WebhookQueueCache;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebhookQueueRepository {

    @Autowired
    WebhookQueueCache whQueue;


    public void add(BotApiMethod method) {
        Integer methodId = whQueue.size() + 1;
        whQueue.put(methodId, method);
    }

    public void clearQueue() {
        whQueue.clear();
    }

    public List<BotApiMethod> getAllAsList() {
        return List.copyOf(whQueue.values());
    }

}
