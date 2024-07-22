package ru.aphecoculture.tgbot.gitlab.model;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class WebhookMRDetails {
    private String object_kind;
    private String event_type;
    private User user;
    private Project project;
    private Repository repository;
    private ObjectAttributes object_attributes;

    @Data
    public static class User {
        private int id;
        private String name;
        private String username;
        private String avatar_url;
        private String email;
    }

    @Data
    public static class Project {
        private Long id;
        private String name;
        private String description;
        private String web_url;
        private String avatar_url;
        private String git_ssh_url;
        private String git_http_url;
        private String namespace;
        private int visibility_level;
        private String path_with_namespace;
        private String default_branch;
        private String homepage;
        private String url;
        private String ssh_url;
        private String http_url;
    }

    @Data
    public static class Repository {
        private String name;
        private String url;
        private String description;
        private String homepage;
    }

    @Data
    public static class ObjectAttributes {
        private int id;
        private Long iid;
        private String target_branch;
        private String source_branch;
        private int source_project_id;
        private int author_id;
        private int assignee_id;
        private String title;
        private String created_at;
        private String updated_at;
        private String last_edited_at;
        private int last_edited_by_id;
        private Integer milestone_id;
        private String state_id;
        private String state;
        private boolean blocking_discussions_resolved;
        private boolean work_in_progress;
        private boolean draft;
        private boolean first_contribution;
        private String merge_status;
        private int target_project_id;
        private String description;
        private String prepared_at;
        private int total_time_spent;
        private int time_change;
        private String human_total_time_spent;
        private String human_time_change;
        private String human_time_estimate;
        private String url;
        private Source source;
        private Target target;
        private LastCommit last_commit;
        private String action;
        private String detailed_merge_status;
    }

    @Data
    public static class Source {
        private String name;
        private String description;
        private String web_url;
        private String avatar_url;
        private String git_ssh_url;
        private String git_http_url;
        private String namespace;
        private int visibility_level;
        private String path_with_namespace;
        private String default_branch;
        private String homepage;
        private String url;
        private String ssh_url;
        private String http_url;
    }

    @Data
    public static class Target {
        private String name;
        private String description;
        private String web_url;
        private String avatar_url;
        private String git_ssh_url;
        private String git_http_url;
        private String namespace;
        private int visibility_level;
        private String path_with_namespace;
        private String default_branch;
        private String homepage;
        private String url;
        private String ssh_url;
        private String http_url;
    }

    @Data
    public static class LastCommit {
        private String id;
        private String message;
        private String title;
        private String timestamp;
        private String url;
        private Author author;
    }

    @Data
    public static class Author {
        private String name;
        private String email;
    }
}

   