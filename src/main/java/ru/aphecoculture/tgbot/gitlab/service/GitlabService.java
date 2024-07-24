package ru.aphecoculture.tgbot.gitlab.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.MergeRequestFilter;
import org.gitlab4j.api.models.WikiPage;
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

    private final GitLabApi gitLabApi;
    private final GitlabProjectCacheRepository projects;
    @Value("${gitlab.url}")
    private String gitlabURL;
    @Value("${gitlab.token}")
    private String gitlabToken;
    @Value("${gitlab.instance}")
    private String gitlabInstance;

    GitlabService(
            @Value("${gitlab.url}") String url,
            @Value("${gitlab.token}") String token,
            @Value("${gitlab.instance}") String instance,
            GitlabProjectCacheRepository repository) {
        this.gitlabURL = url;
        this.gitlabToken = token;
        this.gitlabInstance = instance;
        this.projects = repository;
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


    void updateMainReleaseWikiPage(GitlabProject project, String mrTitle) throws GitLabApiException {
        String mrLink = generateMrWikiPageLink(project.getName(), mrTitle);
        WikiPage mainPage;
        try {
            mainPage = gitLabApi.getWikisApi().getPage(project.getId(), "/Релизы");
            String modifiedMainPageContent = "<a href=\"%s\">%s</a><br>".formatted(mrLink, mrTitle) + mainPage.getContent();
            gitLabApi.getWikisApi().updatePage(project.getId(), "/Релизы", "Релизы", modifiedMainPageContent);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
        }
    }

    String generateMrWikiPageLink(String projectName, String mrTitle) {
        String projectNameInLink = projectName.replaceAll("\\s", "-");
        String titleInLink = mrTitle.replaceAll("\\s", "-");
        return this.gitlabURL + "/" + this.gitlabInstance + "/" + projectNameInLink + "/-/wikis/Релизы/" + titleInLink;
    }

    @SneakyThrows
    public String createWikiPage(Long projectId, String title, String content) {
        Optional<GitlabProject> project = this.getProjectById(projectId);
        if (project.isEmpty()) {
            //TODO добавить кастомный эксепшен
            throw new RuntimeException();
        }

        gitLabApi.getWikisApi().createPage(projectId, "/Релизы/" + title, content);
        updateMainReleaseWikiPage(project.get(), title);

        return generateMrWikiPageLink(project.get().getName(), title);
    }
}
