package ru.aphecoculture.tgbot.gitlab.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.aphecoculture.tgbot.gitlab.cache.GitlabProjectCache;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.model.User;
import ru.aphecoculture.tgbot.gitlab.utils.TestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GitlabProjectCacheRepository.class})
class GitlabProjectCacheRepositoryTest {
    @Autowired
    GitlabProjectCacheRepository repository;

    @MockBean
    GitlabProjectCache projects;

    @Test
    void testGetById() {
        Long projectId = 1L;
        GitlabProject project = TestUtils.projectStub();
        when(projects.get(projectId)).thenReturn(project);

        Optional<GitlabProject> expectedProject = repository.getById(projectId);

        assertEquals(project, expectedProject.get());
    }

    @Test
    void getAll() {
        GitlabProject project = TestUtils.projectStub();
        when(projects.values()).thenReturn(List.of(project));

        List<GitlabProject> expectedProjects = repository.getAll();

        assertEquals(1, expectedProjects.size());
        assertEquals(List.of(project), expectedProjects);

    }

    @Test
    void addProject() {
        GitlabProject project = TestUtils.projectStub();
        repository.addProject(project);
        verify(projects).put(project.getId(), project);
    }

    @Test
    void save() {
        GitlabProject project = TestUtils.projectStub();
        repository.addProject(project);
        verify(projects).put(project.getId(), project);
    }

    @Test
    void getUsersTelegramUsernameExceptMRCreator() {
        GitlabProject project = TestUtils.projectStub();
        User mockUser1 = TestUtils.UserStub(1);
        User mockUser2 = TestUtils.UserStub(2);

        project.setUsers(List.of(mockUser1, mockUser2));

        when(projects.get(project.getId())).thenReturn(project);

        List<String> expected = List.of("2");
        List<String> result = repository.getUsersTelegramUsernameExceptMRCreator(project.getId(), mockUser1.getId());

        assertEquals(1, result.size());
        assertEquals(expected, result);


    }
}
