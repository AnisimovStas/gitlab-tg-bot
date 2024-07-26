package ru.aphecoculture.tgbot.gitlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WebhookException extends ResponseStatusException {
     
    public WebhookException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public WebhookException(String reason, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, reason, cause);
    }

    public WebhookException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public WebhookException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
