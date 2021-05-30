package ru.maletskov.postgres.analyzer.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticFilter {

    private LocalDate startDate;

    private String table;

    private String schema;

    private QueryType queryType;

    private FileType fileType;
}
