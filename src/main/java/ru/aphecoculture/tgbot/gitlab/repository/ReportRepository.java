package ru.aphecoculture.tgbot.gitlab.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aphecoculture.tgbot.gitlab.cache.ReportCache;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReportRepository {

    @Autowired
    ReportCache reports;

    public Optional<String> getById(Integer id) {
        return Optional.ofNullable(reports.get(id));
    }


    public Integer addReport(String reportData) {
        Integer reportId = reports.size() + 1;
        reports.put(reportId, reportData);
 
        return reportId;
    }
}
