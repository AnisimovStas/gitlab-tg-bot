package ru.aphecoculture.tgbot.gitlab.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.model.WebhookMRDetails;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;
import ru.aphecoculture.tgbot.gitlab.repository.WebhookQueueRepository;
import ru.aphecoculture.tgbot.gitlab.utils.TestUtils;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {WebhookService.class, WebhookQueueRepository.class})
class WebhookServiceTest {

    @Autowired
    WebhookService webhookService;

    @MockBean
    WebhookQueueRepository webhookQueueRepository;

    @MockBean
    GitlabProjectCacheRepository projectRepository;

    @Test
    void testAddMRToQueue_Success() {
        WebhookMRDetails details = WebhookMRDetails
                .builder()
                .project(WebhookMRDetails.Project.builder().id(1L).build())
                .object_attributes(
                        WebhookMRDetails.ObjectAttributes.builder()
                                .author_id(1).title("MR Title").url("https://example.com/mr").build()
                )
                .build();
 
        GitlabProject project = TestUtils.projectStub();

        when(projectRepository.getById(1L)).thenReturn(Optional.of(project));

        webhookService.addMRToQueue(details);

        SendMessage expectedMessage = SendMessage.builder()
                .chatId(project.getChatId())
                .messageThreadId(Math.toIntExact(project.getTopicId()))
                .text("\n \n<b>project 1</b>\nНовый мр к рассмотрению: \n\n<a href=\"https://example.com/mr\">MR Title</a>")
                .parseMode("html")
                .build();

        verify(webhookQueueRepository).add(expectedMessage);
    }
}
