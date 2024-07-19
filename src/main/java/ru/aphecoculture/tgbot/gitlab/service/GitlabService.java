package ru.aphecoculture.tgbot.gitlab.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.MergeRequestFilter;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GitlabService {

    GitLabApi gitLabApi;


    GitlabService(@Value("${gitlab.url}") String gitlabUrl, @Value("${gitlab.token}") String gitlabToken) {
        this.gitLabApi = new GitLabApi(gitlabUrl, gitlabToken);
    }

    public List<Project> getAllProjects() throws GitLabApiException {
        return gitLabApi.getProjectApi().getProjects();
    }

    @SneakyThrows
    public String getProjectNameByProjectId(Long projectId) {

        return gitLabApi.getProjectApi().getProject(projectId).getName();
    }

    public List<MergeRequest> getLatestRelease(Long projectId) throws GitLabApiException {

        log.info("starting fetching latestRelease");
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

}
