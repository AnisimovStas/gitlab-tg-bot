package ru.aphecoculture.tgbot.gitlab.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.aphecoculture.tgbot.gitlab.repository.ReportRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ReportService.class})
class ReportServiceTest {

    private final String reportData = "report Data";
    private final int reportId = 1;
    @Autowired
    ReportService reportService;
    @MockBean
    ReportRepository reportRepository;

    @Test
    void addReport() {
        when(reportRepository.addReport(reportData)).thenReturn(reportId);
        Integer result = reportService.addReport(reportData);
        assertEquals(reportId, result);
    }

    @Test
    void getReportDataById() {
        when(reportRepository.getById(reportId)).thenReturn(Optional.ofNullable(reportData));
        Optional<String> result = reportService.getReportDataById(reportId);
        assertEquals(reportData, result.get());
    }
}
