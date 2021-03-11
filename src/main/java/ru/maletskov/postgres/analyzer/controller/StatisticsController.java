package ru.maletskov.postgres.analyzer.controller;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.maletskov.postgres.analyzer.api.DbInfoService;
import ru.maletskov.postgres.analyzer.api.StatisticsService;
import ru.maletskov.postgres.analyzer.dto.DataContentDto;
import ru.maletskov.postgres.analyzer.dto.DbInfoDto;
import ru.maletskov.postgres.analyzer.dto.FileType;
import ru.maletskov.postgres.analyzer.dto.QueryType;
import ru.maletskov.postgres.analyzer.dto.StatisticFilter;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticService;

    private final DbInfoService dbInfoService;

    @SneakyThrows
    @GetMapping("/statistics/file")
    public void getFileWithStat(HttpServletResponse response,
                                @RequestParam(required = false) LocalDateTime startDateTime,
                                @RequestParam(required = false) LocalDateTime endDateTime,
                                @RequestParam String table,
                                @RequestParam String schema,
                                @RequestParam QueryType queryType,
                                @RequestParam FileType fileType) {
        DataContentDto contentDto = statisticService.getFileWithStatistics(
                StatisticFilter.builder()
                        .startDateTime(startDateTime)
                        .endDateTime(endDateTime)
                        .fileType(fileType)
                        .schema(schema)
                        .table(table)
                        .queryType(queryType)
                        .build()
        );
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", contentDto.getFileName());
        response.setHeader(headerKey, headerValue);
        response.setContentType("text/csv;charset=UTF-8");
        response.getWriter().write(contentDto.getContent());
    }

    @GetMapping("/database/info")
    public DbInfoDto getDbInfo() {
        return dbInfoService.getDbInfo();
    }
}
