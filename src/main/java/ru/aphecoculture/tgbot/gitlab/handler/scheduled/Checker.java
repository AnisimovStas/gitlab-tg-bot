package ru.aphecoculture.tgbot.gitlab.handler.scheduled;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;

public interface Checker {
    List<BotApiMethod> check();
}
