package ru.aphecoculture.tgbot.gitlab.service;

import org.gitlab4j.api.GitLabApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.utils.TestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GitlabService.class})
class GitlabServiceTest {
     
    @MockBean
    GitlabProjectCacheRepository projects;
    @MockBean
    GitLabApi gitLabApi;
    @Autowired
    private GitlabService gitlabService;

    @Test
    void getAllProjects() {
        GitlabProject project = TestUtils.projectStub();
        when(projects.getAll()).thenReturn(List.of(project));

        List<GitlabProject> expected = List.of(project);
        List<GitlabProject> result = gitlabService.getAllProjects();
        assertEquals(expected, result);

    }


    @Test
    void getProjectById() {
        GitlabProject project = TestUtils.projectStub();
        when(projects.getById(project.getId())).thenReturn(Optional.ofNullable(project));
        Optional<GitlabProject> result = gitlabService.getProjectById(project.getId());
        assertEquals(project, result.get());
    }

    @Test
    void generateMrWikiPageLink() {
        String projectName = "project 1";
        String mrTitle = "mr 1";
        String result = gitlabService.generateMrWikiPageLink(projectName, mrTitle);
        String expected = "https://dev1.apheco.ru/gitlab-instance-4f4a9c0a/project-1/-/wikis/Релизы/mr-1";
        assertEquals(expected, result);
    }
}