package ru.maletskov.postgres.analyzer.api;

import java.util.List;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;

public interface StatisticService {

    List<StatIoView> getAllStatistic();

    void updateStatistic(List<StatIoView> stats);
}
