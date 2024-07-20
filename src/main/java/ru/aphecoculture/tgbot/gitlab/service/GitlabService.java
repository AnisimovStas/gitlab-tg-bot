package ru.aphecoculture.tgbot.gitlab.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.MergeRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GitlabService {

    private final String gitlabURL;
    private final String gitlabToken;
    private final String gitlabInstance;

    GitLabApi gitLabApi;

    @Autowired
    GitlabProjectCacheRepository projects;


    GitlabService(@Value("${gitlab.url}") String gitlabUrl,
                  @Value("${gitlab.token}") String gitlabToken,
                  @Value("${gitlab.instance}") String gitlabInstance
    ) {
        this.gitlabURL = gitlabUrl;
        this.gitlabToken = gitlabToken;
        this.gitlabInstance = gitlabInstance;
        this.gitLabApi = new GitLabApi(this.gitlabURL, this.gitlabToken);
    }

    public List<GitlabProject> getAllProjects() {
        return projects.getAll();
    }

    @SneakyThrows
    public Optional<GitlabProject> getProjectById(Long projectId) {
        return projects.getById(projectId);
    }

    public List<MergeRequest> getLatestRelease(Long projectId) throws GitLabApiException {
        MergeRequestFilter mergeRequestFilter = new MergeRequestFilter();
        mergeRequestFilter.setProjectId(projectId);
        mergeRequestFilter.setState(Constants.MergeRequestState.MERGED);
        mergeRequestFilter.setSearch("Release");
        return gitLabApi.getMergeRequestApi().getMergeRequests(mergeRequestFilter, 2).all();
    }

    //from >to mrs as stack
    public List<MergeRequest> getRangeOfMRs(Long projectId, Long fromMRId, Long toMRId) throws GitLabApiException {
        MergeRequestFilter mergeRequestFilter = new MergeRequestFilter();
        List<Long> ids = new ArrayList<>();

        for (long i = fromMRId; i > toMRId; i--) {
            ids.add(i);
        }

        mergeRequestFilter.setIids(ids);
        mergeRequestFilter.setProjectId(projectId);
        mergeRequestFilter.setState(Constants.MergeRequestState.MERGED);
        return gitLabApi.getMergeRequestApi().getMergeRequests(mergeRequestFilter);
    }

    @SneakyThrows
    public String createWikiPage(Long projectId, String title, String content) {
        gitLabApi.getWikisApi().createPage(projectId, title, content);

        Optional<GitlabProject> project = this.getProjectById(projectId);
        if (project.isEmpty()) {
            throw new RuntimeException();
        }
        String projectNameInLink = project.get().getName().replaceAll("\\s", "-");
        String titleInLink = title.replaceAll("\\s", "-");

        return this.gitlabURL + "/" + this.gitlabInstance + "/" + projectNameInLink + "/-/wikis/" + titleInLink;

    }
}
