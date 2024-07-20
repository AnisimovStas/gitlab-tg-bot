package ru.aphecoculture.tgbot.gitlab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aphecoculture.tgbot.gitlab.repository.ReportRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {
     
    @Autowired
    private final ReportRepository reportRepository;


    public Integer addReport(String reportData) {
        return reportRepository.addReport(reportData);
    }

    public Optional<String> getReportDataById(Integer id) {
        return reportRepository.getById(id);
    }
}
