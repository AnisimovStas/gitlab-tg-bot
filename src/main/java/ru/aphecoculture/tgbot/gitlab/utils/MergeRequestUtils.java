package ru.aphecoculture.tgbot.gitlab.utils;

import org.springframework.beans.factory.annotation.Autowired;
import ru.aphecoculture.tgbot.gitlab.config.properties.GitlabProperties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MergeRequestUtils {

    @Autowired
    static GitlabProperties gitlabProperties;

    MergeRequestUtils(GitlabProperties gitlabProperties) {
        MergeRequestUtils.gitlabProperties = gitlabProperties;
    }

    public static String trimPrefix(String title) {
        String[] splitTitle = title.split(": ");

        return splitTitle.length == 2 ? splitTitle[1] : title;
    }

    public static String addBulletAndSpace(String title) {
        return "• %s".formatted(title) + "\n";
    }

    public static String replaceNumbersWithIssueLink(String title, String projectName) {

        StringBuffer buffer = new StringBuffer();
        Pattern pattern = Pattern.compile("(?<!\\.)\\d+(?!\\.)");
        Matcher matcher = pattern.matcher(title);


        while (matcher.find()) {
            String number = matcher.group();
            String issueLink = "<a href=\"%s/%s/%s/-/issues/%s\">%s</a>".formatted(gitlabProperties.url(), gitlabProperties.instance(), projectName, number, number);
            matcher.appendReplacement(buffer, issueLink);
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }
}
