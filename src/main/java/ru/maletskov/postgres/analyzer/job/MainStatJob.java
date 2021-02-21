package ru.maletskov.postgres.analyzer.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.maletskov.postgres.analyzer.api.StatisticsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainStatJob {

    private final StatisticsService statisticService;

    @Scheduled(cron = "${main-stat-job-cron-expression:* * * * * ?}")
    public void execute() {
        log.info("Start executing statistic job");
        statisticService.updateStatistics(statisticService.getAllStatistics());
    }
}
