package ru.maletskov.postgres.analyzer.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maletskov.postgres.analyzer.api.DbInfoService;
import ru.maletskov.postgres.analyzer.dto.DbInfoDto;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;
import ru.maletskov.postgres.analyzer.repository.analyzer.StatIoViewRepository;

@Service
@RequiredArgsConstructor
public class DbInfoServiceImpl implements DbInfoService {

    public static final String IGNORE_TABLE = "main_stat";

    private final StatIoViewRepository statIoViewRepository;

    @Override
    @Transactional("analyzerTransactionManager")
    public DbInfoDto getDbInfo() {
        List<StatIoView> stats = statIoViewRepository.findAll();
        Map<String, Set<String>> map = new HashMap<>();

        for (StatIoView stat : stats) {
            Set<String> set = map.get(stat.getSchemaname());
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(stat.getRelname());
            map.put(stat.getSchemaname(), set);
        }

        Set<DbInfoDto.SchemaDto> schemas = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            schemas.add(DbInfoDto.SchemaDto.builder()
                    .name(entry.getKey())
                    .tables(entry.getValue())
                    .build());
        }

        return DbInfoDto.builder()
                .schemas(schemas)
                .build();
    }

    @Override
    @Transactional("analyzerTransactionManager")
    public Set<String> getSchemas() {
        return statIoViewRepository.findAll()
                .stream()
                .map(StatIoView::getSchemaname)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional("analyzerTransactionManager")
    public Set<String> getTables(String schema) {
        return statIoViewRepository.findDistinctBySchemaname(schema)
                .stream()
                .map(StatIoView::getRelname)
                .filter(relname -> !relname.equals(IGNORE_TABLE))
                .collect(Collectors.toSet());
    }
}
