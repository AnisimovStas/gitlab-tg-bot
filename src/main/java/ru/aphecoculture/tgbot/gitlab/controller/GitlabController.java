package ru.aphecoculture.tgbot.gitlab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/gitlab-webhook")
public class GitlabController {

 
    @PostMapping("/create-mr-event")
    Object test(@RequestBody Object body) {
        log.info(body.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
