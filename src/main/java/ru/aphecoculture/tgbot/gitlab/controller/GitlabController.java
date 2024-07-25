package ru.aphecoculture.tgbot.gitlab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aphecoculture.tgbot.gitlab.model.WebhookMRDetails;
import ru.aphecoculture.tgbot.gitlab.service.WebhookService;

@RestController
@Slf4j
@RequestMapping(value = "/gitlab-webhook")
public class GitlabController {

    @Autowired
    WebhookService webhookService;


    @PostMapping("/create-mr-event")
    void processWebhook(@RequestBody WebhookMRDetails details) {
        log.info("get new webhook event");
        String webhookEventType = details.getEvent_type();
 
        if (webhookEventType.equals("merge_request")) {
            String mrState = details.getObject_attributes().getState();
            log.info("get new webhook merge request event");
            if (mrState.equals("opened")) {
                log.info("get new webhook opened merge request event");
                webhookService.addMRToQueue(details);
            }

        }
    }
}
