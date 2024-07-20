package ru.aphecoculture.tgbot.gitlab.service;

import org.gitlab4j.api.models.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.aphecoculture.tgbot.gitlab.repository.ReportRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReportService {

    private final String gitlabInstance;
    private final String gitlabURL;

    @Autowired
    private final ReportRepository reportRepository;

    ReportService(@Value("${gitlab.instance}") String gitlabInstance, @Value("${gitlab.url}") String gitlabURL, ReportRepository reportRepository) {
        this.gitlabInstance = gitlabInstance;
        this.gitlabURL = gitlabURL;
        this.reportRepository = reportRepository;
    }

    public Integer addReport(String reportData) {
        return reportRepository.addReport(reportData);
    }

    public Optional<String> getReportDataById(Integer id) {
        return reportRepository.getById(id);
    }

    public Long getProjectIdFromCallback(String callback) {
        String fieldValue = getFieldFromCallback(callback, "projectId");
        return Long.parseLong(fieldValue);
    }

    public Long getFromMRIdFromCallback(String callback) {
        String fieldValue = getFieldFromCallback(callback, "fromMRId");
        return Long.parseLong(fieldValue);
    }

    public Long getToMRIdFromCallback(String callback) {
        String fieldValue = getFieldFromCallback(callback, "toMRId");
        return Long.parseLong(fieldValue);
    }

    public String processMrListToReport(List<MergeRequest> mrs, String projectName) {
        String releaseTitle = processReportTitle(mrs.getFirst().getTitle(), projectName);

        Map<String, List<String>> mrsMap = sortMrsByCategory(mrs);

        return releaseTitle + "\n \n" +
                processReportMainSection(mrsMap, "feat", "Новый функционал", projectName) +
                processReportMainSection(mrsMap, "fix", "Исправлено", projectName) +
                processReportMainSection(mrsMap, "refactor", "Улучшено", projectName) +
                processReportMainSection(mrsMap, "others", "Прочее", projectName);
    }

    private Map<String, List<String>> sortMrsByCategory(List<MergeRequest> mrs) {
        List<String> bugfixes = new ArrayList<>();
        List<String> features = new ArrayList<>();
        List<String> refactors = new ArrayList<>();
        List<String> others = new ArrayList<>();


        for (MergeRequest mr : mrs) {
            String mrTitle = mr.getTitle();
            if (mrTitle.startsWith("fix:")) {
                bugfixes.add(mrTitle);
                continue;
            }

            if (mrTitle.startsWith("feat:")) {
                features.add(mrTitle);
                continue;
            }

            if (mrTitle.startsWith("refactor:")) {
                refactors.add(mrTitle);
                continue;
            }
            others.add(mrTitle);
        }

        Map<String, List<String>> mrMap = new HashMap<>();

        mrMap.put("fix", bugfixes);
        mrMap.put("feat", features);
        mrMap.put("refactor", refactors);
        mrMap.put("others", others);
        return mrMap;
    }

    private String processReportMainSection(Map<String, List<String>> mrs, String sectionName, String sectionRuTitle, String projectName) {
        StringBuilder section = new StringBuilder();

        if (!mrs.get(sectionName).isEmpty()) {
            section.append("<b> %s: \n</b>".formatted(sectionRuTitle));
            for (String title : mrs.get(sectionName)) {
                section.append(processMRTitle(title, projectName));
            }
            section.append("\n");
        }

        return section.toString();
    }

    private String getFieldFromCallback(String callback, String field) {
        String[] parts = callback.split("_");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals(field)) {
                if (i < parts.length - 1) {
                    return parts[i + 1];
                } else {
                    return null; // if field is found but there is no next part
                }
            }
        }
        return null; // if field is not found
    }

    private String processReportTitle(String mrTitle, String projectName) {
        String trimmedMrTitle = trimPrefix(mrTitle);
        return "Новый релиз <b>" + projectName + "</b> \n \n" + "<b><i>" + trimmedMrTitle + "</i></b>";
    }

    private String processMRTitle(String title, String projectName) {
        String trimmedTitle = trimPrefix(title);
        String formattedTitle = addBulletAndSpace(trimmedTitle);
        return replaceNumbersWithIssueLink(formattedTitle, projectName.replaceAll("\\s", "-"));
    }


    private String trimPrefix(String title) {
        String[] splitTitle = title.split(": ");

        return splitTitle.length == 2 ? splitTitle[1] : title;
    }

    private String addBulletAndSpace(String title) {
        return "• %s".formatted(title) + "\n";
    }


    private String replaceNumbersWithIssueLink(String title, String projectName) {
        StringBuffer buffer = new StringBuffer();
        Pattern pattern = Pattern.compile("(?<!\\.)\\d+(?!\\.)");
        Matcher matcher = pattern.matcher(title);

        while (matcher.find()) {
            String number = matcher.group();
            String issueLink = "<a href=\"%s/%s/%s/-/issues/%s\">%s</a>".formatted(gitlabURL, gitlabInstance, projectName, number, number);
            matcher.appendReplacement(buffer, issueLink);
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }


}
