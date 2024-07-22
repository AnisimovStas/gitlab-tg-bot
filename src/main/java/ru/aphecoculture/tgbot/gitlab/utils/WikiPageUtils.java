package ru.aphecoculture.tgbot.gitlab.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiPageUtils {

    public static String processPageContent(String report) {
        String trimmedReport = removeFirstTwoLines(report);
        return addLineBreaks(trimmedReport);
    }


    public static String processPageTitle(String report) {
        String pattern = "<b><i>(.*?)</i></b>";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(report);
        if (m.find()) {
            return m.group(1);
        } else {
            return "";
        }
    }

    public static String addLineBreaks(String input) {
        return input.replaceAll("\\n", "<br>");
    }

    public static String removeFirstTwoLines(String input) {
        String[] lines = input.split("\\r?\\n");
        StringBuilder result = new StringBuilder();
        for (int i = 2; i < lines.length; i++) {
            result.append(lines[i]).append("\n");
        }
        return result.toString().trim();
    }
}
