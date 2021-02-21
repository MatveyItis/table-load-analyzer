package ru.maletskov.postgres.analyzer.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maletskov.postgres.analyzer.api.StatisticsService;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final StatisticsService statisticService;

    @GetMapping("/statistics")
    public List<StatIoView> getStatistics() {
        return statisticService.getAllStatistics();
    }
}
