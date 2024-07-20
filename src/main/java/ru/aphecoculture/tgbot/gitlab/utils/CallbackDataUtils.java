package ru.aphecoculture.tgbot.gitlab.utils;

import org.springframework.stereotype.Component;

@Component
public class CallbackDataUtils {


    private static String getFieldFromCallback(String callback, String field) {
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

    public static Long getProjectIdFromCallback(String callback) {
        String fieldValue = getFieldFromCallback(callback, "projectId");
        return Long.parseLong(fieldValue);
    }

    public static Long getFromMRIdFromCallback(String callback) {
        String fieldValue = getFieldFromCallback(callback, "fromMRId");
        return Long.parseLong(fieldValue);
    }

    public static Long getToMRIdFromCallback(String callback) {
        String fieldValue = getFieldFromCallback(callback, "toMRId");
        return Long.parseLong(fieldValue);
    }

    public static Integer getReportIdFromCallback(String callback) {
        String fieldValue = getFieldFromCallback(callback, "reportId");
        return Integer.parseInt(fieldValue);
    }


}
