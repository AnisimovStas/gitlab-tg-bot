package ru.aphecoculture.tgbot.gitlab.cache;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebhookQueueCache extends ConcurrentHashMap<Integer, BotApiMethod> {
}
