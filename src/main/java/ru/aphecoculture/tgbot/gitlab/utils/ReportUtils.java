package ru.aphecoculture.tgbot.gitlab.utils;

import org.gitlab4j.api.models.MergeRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.aphecoculture.tgbot.gitlab.utils.MergeRequestUtils.*;

public class ReportUtils {

    public static String processMrListToReport(List<MergeRequest> mrs, String projectName) {
        String releaseTitle = processReportTitle(mrs.getFirst().getTitle(), projectName);

        Map<String, List<String>> mrsMap = sortMrsByCategory(mrs);

        return releaseTitle + "\n \n" +
                processReportMainSection(mrsMap, "feat", "Новый функционал", projectName) +
                processReportMainSection(mrsMap, "fix", "Исправлено", projectName) +
                processReportMainSection(mrsMap, "refactor", "Улучшено", projectName) +
                processReportMainSection(mrsMap, "others", "Прочее", projectName);
    }

    private static Map<String, List<String>> sortMrsByCategory(List<MergeRequest> mrs) {
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

    private static String processReportMainSection(Map<String, List<String>> mrs, String sectionName, String sectionRuTitle, String projectName) {
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


    private static String processReportTitle(String mrTitle, String projectName) {
        String trimmedMrTitle = trimPrefix(mrTitle);
        return "Новый релиз <b>" + projectName + "</b> \n \n" + "<b><i>" + trimmedMrTitle + "</i></b>";
    }

    private static String processMRTitle(String title, String projectName) {
        String trimmedTitle = trimPrefix(title);
        String formattedTitle = addBulletAndSpace(trimmedTitle);
        return replaceNumbersWithIssueLink(formattedTitle, projectName.replaceAll("\\s", "-"));
    }
}
