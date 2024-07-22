package ru.aphecoculture.tgbot.gitlab.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gitlab-bot")
public record GitlabBotProperties(String botToken, String botName) {
}

