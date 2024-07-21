package ru.aphecoculture.tgbot.gitlab.sheduler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;

public interface SchedulerHandler {
    List<BotApiMethod> handleSchedule();

}
