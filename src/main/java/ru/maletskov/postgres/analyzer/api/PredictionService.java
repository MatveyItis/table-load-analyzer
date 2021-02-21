package ru.maletskov.postgres.analyzer.api;

import java.time.Period;

public interface PredictionService {

    Period predictPeriod(String schemaName, String tableName, Period targetPeriod);
}
