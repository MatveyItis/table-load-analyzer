package ru.maletskov.postgres.analyzer.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
}
