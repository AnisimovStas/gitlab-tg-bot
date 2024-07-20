package ru.aphecoculture.tgbot.gitlab.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.aphecoculture.tgbot.gitlab.cache.GitlabProjectCache;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GitlabProjectCacheRepository {

    private final GitlabProjectCache projects;

    public Optional<GitlabProject> getById(Long projectId) {
        return Optional.ofNullable(projects.get(projectId));
    }

    public List<GitlabProject> getAll() {
        return new ArrayList<>(projects.values());
    }

    public void addProject(GitlabProject project) {
        projects.put(project.getId(), project);
    }
}
