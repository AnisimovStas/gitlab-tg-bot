package ru.aphecoculture.tgbot.gitlab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WebhookMRDetails {
    private String event_type;
    private Project project;
    private ObjectAttributes object_attributes;


    @Data
    @SuperBuilder
    public static class Project {
        private Long id;
    }

    @Data
    @SuperBuilder
    public static class ObjectAttributes {
        private int id;
        private Long iid;
        //        private String target_branch;
        //        private String source_branch;
        private int author_id;
        private String title;
        //        private Integer milestone_id;
        private String state_id;
        private String state;
        //        private String merge_status;
        private String url;
    }


    @Data
    @SuperBuilder
    public static class Author {
        private String name;
        private String email;
    }
}

   