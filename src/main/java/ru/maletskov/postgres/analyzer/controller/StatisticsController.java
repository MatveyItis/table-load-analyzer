package ru.maletskov.postgres.analyzer.controller;

import java.time.LocalDate;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.maletskov.postgres.analyzer.api.DbInfoService;
import ru.maletskov.postgres.analyzer.api.StatisticsService;
import ru.maletskov.postgres.analyzer.dto.DataContentDto;
import ru.maletskov.postgres.analyzer.dto.DbInfoDto;
import ru.maletskov.postgres.analyzer.dto.FileType;
import ru.maletskov.postgres.analyzer.dto.QueryType;
import ru.maletskov.postgres.analyzer.dto.StatisticFilter;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticService;

    private final DbInfoService dbInfoService;

    @SneakyThrows
    @GetMapping("/statistics/file")
    public void getFileWithStat(HttpServletResponse response,
                                @RequestParam String table,
                                @RequestParam String schema,
                                @RequestParam QueryType queryType,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam FileType fileType) {
        DataContentDto contentDto = statisticService.getFileWithStatistics(
                StatisticFilter.builder()
                        .startDate(startDate)
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

    @ApiIgnore
    @GetMapping("/database/info")
    public DbInfoDto getDbInfo() {
        return dbInfoService.getDbInfo();
    }

    @GetMapping("/database/schema")
    public Set<String> getSchemas() {
        return dbInfoService.getSchemas();
    }

    @GetMapping("/database/schema/{schemaName}")
    public Set<String> getTables(@PathVariable String schemaName) {
        return dbInfoService.getTables(schemaName);
    }
}
