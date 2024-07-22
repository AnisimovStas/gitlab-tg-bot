package ru.aphecoculture.tgbot.gitlab.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.aphecoculture.tgbot.gitlab.cache.GitlabProjectCache;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.model.User;

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

    public void save(GitlabProject project) {
        projects.put(project.getId(), project);
    }

    public List<String> getUsersTelegramUsernameExceptMRCreator(Long projectId, int gitlabUserId) {
        List<User> users = projects.get(projectId).getUsers();
        List<String> result = new ArrayList<>();
 
        for (User user : users) {
            if (user.getId() != gitlabUserId) {
                result.add(user.getTelegramUsername());
            }
        }
        return result;
    }
}
