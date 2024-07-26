package ru.aphecoculture.tgbot.gitlab.exception;

import ru.aphecoculture.ecovision.tgbot.commons.exception.BotApplicationException;

public class GitlabProjectException extends BotApplicationException {

    public static final String PROJECT_NOT_FOUND = "Не смог найти проект";

    public GitlabProjectException(String message) {
        super(message);
    }

    public GitlabProjectException(Throwable cause) {
        super(cause);
    }
}
