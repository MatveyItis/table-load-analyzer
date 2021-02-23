package ru.maletskov.postgres.analyzer.api;

import java.util.List;
import ru.maletskov.postgres.analyzer.dto.DataContentDto;
import ru.maletskov.postgres.analyzer.dto.StatisticFilter;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;

public interface StatisticsService {

    List<StatIoView> getAllStatistics();

    void updateStatistics(List<StatIoView> stats);

    DataContentDto getFileWithStatistics(StatisticFilter statFilter);
}
