package ru.aphecoculture.tgbot.gitlab.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.aphecoculture.tgbot.gitlab.cache.ReportCache;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ReportRepository.class})
class ReportRepositoryTest {

    @Autowired
    ReportRepository repository;

    @MockBean
    ReportCache reports;

    @Test
    void getById() {
        int reportId = 1;
        when(reports.get(reportId)).thenReturn("report data");
        Optional<String> expectedReport = repository.getById(reportId);
        assertEquals("report data", expectedReport.get());
    }

    @Test
    void addReport() {
        String reportData = "report data";
        repository.addReport(reportData);
        when(reports.size()).thenReturn(0);
        verify(reports).put(1, reportData);
    }
}