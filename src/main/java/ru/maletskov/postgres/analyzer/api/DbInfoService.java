package ru.maletskov.postgres.analyzer.api;

import ru.maletskov.postgres.analyzer.dto.DbInfoDto;

public interface DbInfoService {

    DbInfoDto getDbInfo();
}
