package ru.aphecoculture.tgbot.gitlab.handler.strategy;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;

public interface ScheduleStrategy {

    List<BotApiMethod> check();
}
