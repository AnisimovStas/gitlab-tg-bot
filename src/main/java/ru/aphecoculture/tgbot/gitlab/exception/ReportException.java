package ru.aphecoculture.tgbot.gitlab.exception;

import ru.aphecoculture.ecovision.tgbot.commons.exception.BotApplicationException;

public class ReportException extends BotApplicationException {

    public static final String REPORT_NOT_FOUND = "Не смог найти отчет о релизе";

    public ReportException(String message) {
        super(message);
    }

    public ReportException(Throwable cause) {
        super(cause);
    }
}
