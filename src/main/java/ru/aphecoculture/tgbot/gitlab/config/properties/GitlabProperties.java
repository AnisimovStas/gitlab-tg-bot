package ru.aphecoculture.tgbot.gitlab.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gitlab")
public record GitlabProperties(String url, String token, String instance) {
}
