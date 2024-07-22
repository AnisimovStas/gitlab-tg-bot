package ru.aphecoculture.tgbot.gitlab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.aphecoculture.tgbot.gitlab.model.GitlabProject;
import ru.aphecoculture.tgbot.gitlab.model.User;
import ru.aphecoculture.tgbot.gitlab.repository.GitlabProjectCacheRepository;

import java.util.List;

@SpringBootApplication
@ComponentScan("ru.aphecoculture")
@EnableConfigurationProperties()
@ConfigurationPropertiesScan("ru.aphecoculture")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner loadProject(GitlabProjectCacheRepository repository) {
        return (args -> {
            //TODO ecovision chatid = -1002083279042L;
            // front topicId 2932;
            //backend topicId  41;
            //QA topicId  46;
            Long testGroupChatId = -1002173725195L;
            Long testFrontTopicId = 2L;
            Long testBackTopicId = 1L;
            User frontend1 = User.builder().gitlabUsername("SergeiCN").telegramUsername("JStathem").build();
            User frontend2 = User.builder().gitlabUsername("SAnisimov").telegramUsername("otpbard").build();

            GitlabProject frontend = GitlabProject.builder()
                    .id(4L)
                    .name("ECOVISION WEB")
                    .chatId(testGroupChatId)
                    .topicId(testFrontTopicId)
                    .lastMRId(null)
                    .users(List.of(frontend1, frontend2))
                    .build();
            GitlabProject backend = GitlabProject.builder()
                    .id(2L)
                    .name("ECOVISION")
                    .chatId(testGroupChatId)
                    .topicId(testBackTopicId)
                    .lastMRId(null)
                    .build();

            repository.addProject(frontend);
            repository.addProject(backend);
        });
    }

}
