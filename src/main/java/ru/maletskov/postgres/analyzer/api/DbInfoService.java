package ru.maletskov.postgres.analyzer.api;

import java.util.Set;
import ru.maletskov.postgres.analyzer.dto.DbInfoDto;

public interface DbInfoService {

    DbInfoDto getDbInfo();

    Set<String> getSchemas();

    Set<String> getTables(String schema);
}
