package ru.aphecoculture.tgbot.gitlab.cache;

import org.springframework.stereotype.Component;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class GitlabProjectCache extends ConcurrentHashMap<Long, GitlabProject> {
}
