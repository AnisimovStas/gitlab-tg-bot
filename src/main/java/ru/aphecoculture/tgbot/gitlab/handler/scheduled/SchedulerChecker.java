package ru.aphecoculture.tgbot.gitlab.handler.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SchedulerChecker {

    @Autowired
    MergeRequestChecker mergeRequestChecker;

    // List<Checker> checkers;

    public List<BotApiMethod> scheduledHandler() {
        List<BotApiMethod> responseMessages = new ArrayList<>();

        List<BotApiMethod> mrMessage = mergeRequestChecker.check();
 
        responseMessages.addAll(mrMessage);


        return responseMessages;
    }
}
