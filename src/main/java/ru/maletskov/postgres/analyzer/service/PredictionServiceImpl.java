package ru.maletskov.postgres.analyzer.service;

import java.time.Period;
import org.springframework.stereotype.Service;
import ru.maletskov.postgres.analyzer.api.PredictionService;

@Service
public class PredictionServiceImpl implements PredictionService {

    @Override
    public Period predictPeriod(String schemaName, String tableName, Period targetPeriod) {
        return null;
    }
}
