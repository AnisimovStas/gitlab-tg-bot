package ru.aphecoculture.tgbot.gitlab.sheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.aphecoculture.tgbot.gitlab.handler.strategy.ScheduleStrategy;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerHandlerImpl implements SchedulerHandler {

    private final List<ScheduleStrategy> strategies;

    public List<BotApiMethod> handleSchedule() {

        List<BotApiMethod> messages = new ArrayList<>();

        for (ScheduleStrategy strategy : strategies) {
            List<BotApiMethod> strategyMessages = strategy.check();
            messages.addAll(strategyMessages);
        }
        return messages;
    }


}
