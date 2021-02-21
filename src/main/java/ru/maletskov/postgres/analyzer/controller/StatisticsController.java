package ru.maletskov.postgres.analyzer.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.maletskov.postgres.analyzer.api.StatisticsService;
import ru.maletskov.postgres.analyzer.dto.FileType;
import ru.maletskov.postgres.analyzer.dto.QueryType;
import ru.maletskov.postgres.analyzer.dto.StatisticFilter;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticService;

    //todo doesn't work now with text/csv type
    @SneakyThrows
    @GetMapping(value = "/statistics/file.csv", produces = "text/csv")
    public byte[] getFileWithStat(@RequestParam(required = false) LocalDateTime startDateTime,
                                  @RequestParam(required = false) LocalDateTime endDateTime,
                                  @RequestParam String table,
                                  @RequestParam String schema,
                                  @RequestParam QueryType queryType,
                                  @RequestParam FileType fileType) {
        return statisticService.getFileWithStatistics(
                StatisticFilter.builder()
                        .startDateTime(startDateTime)
                        .endDateTime(endDateTime)
                        .fileType(fileType)
                        .schema(schema)
                        .table(table)
                        .queryType(queryType)
                        .build()
        );
    }
}
