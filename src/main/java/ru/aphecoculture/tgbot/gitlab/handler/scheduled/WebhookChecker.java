package ru.aphecoculture.tgbot.gitlab.handler.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.aphecoculture.tgbot.gitlab.handler.strategy.ScheduleStrategy;
import ru.aphecoculture.tgbot.gitlab.repository.WebhookQueueRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebhookChecker implements ScheduleStrategy {

    @Autowired
    WebhookQueueRepository webhookQueueRepository;

    public List<BotApiMethod> check() {

        List<BotApiMethod> messagesInQueue = webhookQueueRepository.getAllAsList();

        List<BotApiMethod> messages = new ArrayList<>(messagesInQueue);


        webhookQueueRepository.clearQueue();

        return messages;

    }
}
