package ru.maletskov.postgres.analyzer.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maletskov.postgres.analyzer.api.StatisticsService;
import ru.maletskov.postgres.analyzer.dto.DataContentDto;
import ru.maletskov.postgres.analyzer.dto.QueryType;
import ru.maletskov.postgres.analyzer.dto.StatisticFilter;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;
import ru.maletskov.postgres.analyzer.entity.own.TableStat;
import ru.maletskov.postgres.analyzer.mapper.TableStatMapper;
import ru.maletskov.postgres.analyzer.repository.analyzer.StatIoViewRepository;
import ru.maletskov.postgres.analyzer.repository.own.TableStatRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticsService {

    private static final String EXCLUDE_TABLE = "main_stat";

    private final StatIoViewRepository statIoViewRepository;

    private final TableStatRepository tableStatRepository;

    private final TableStatMapper tableStatMapper;

    @Override
    @Transactional("analyzerTransactionManager")
    public List<StatIoView> getAllStatistics() {
        return statIoViewRepository.findAll();
    }

    @Override
    @Transactional("ownTransactionManager")
    public void updateStatistics(List<StatIoView> stats) {
        List<TableStat> tableStats = tableStatRepository.findAllByLastCreated();
        if (tableStats.isEmpty()) {
            List<TableStat> newStats = new ArrayList<>();
            LocalDateTime created = LocalDateTime.now();
            for (StatIoView stat : stats) {
                if (stat.getRelname().equals(EXCLUDE_TABLE)) {
                    continue;
                }
                TableStat tableStat = tableStatMapper.toInitTableStat(stat);
                tableStat.setCreated(created);
                newStats.add(tableStat);
            }
            tableStatRepository.saveAll(newStats);
            log.debug("Successfully saved statistic for {} tables", newStats.size());
        } else {
            List<TableStat> actualTableStats = new ArrayList<>();
            LocalDateTime created = LocalDateTime.now();
            for (StatIoView stat : stats) {
                if (stat.getRelname().equals(EXCLUDE_TABLE)) {
                    continue;
                }
                TableStat actualTableStat = getTableStat(stat.getSchemaname(), stat.getRelname(), tableStats);
                if (actualTableStat == null) {
                    continue;
                }
                actualTableStat.setReadVal(stat.getSeqScan() - actualTableStat.getInitReadVal());
                actualTableStat.setDelVal(stat.getNTupDel() - actualTableStat.getInitDelVal());
                actualTableStat.setUpdVal(stat.getNTupUpd() - actualTableStat.getInitUpdVal());
                actualTableStat.setInsVal(stat.getNTupIns() - actualTableStat.getInitInsVal());
                actualTableStat.setCreated(created);
                actualTableStats.add(actualTableStat);
            }
            tableStatRepository.saveAll(actualTableStats);
            log.debug("Successfully updated statistic for {} tables", actualTableStats.size());
        }
    }

    @Override
    @SneakyThrows
    @Transactional("ownTransactionManager")
    public DataContentDto getFileWithStatistics(StatisticFilter statFilter) {
        //todo checks if table or schema exists
        List<TableStat> listStat = tableStatRepository.findAllByTableNameAndSchemaName(statFilter.getTable(), statFilter.getSchema());
        List<String[]> dataLines = new ArrayList<>();
        listStat.forEach(s -> {
            if (statFilter.getQueryType().equals(QueryType.SELECT)) {
                dataLines.add(new String[]{s.getReadVal().toString(), s.getCreated().toString()});
            } else if (statFilter.getQueryType().equals(QueryType.INSERT)) {
                dataLines.add(new String[]{s.getInsVal().toString(), s.getCreated().toString()});
            }
            //todo
        });
        StringBuilder sb = new StringBuilder();
        sb.append("value,time\n");
        dataLines.forEach(d -> sb.append(convertToCSV(d)).append("\n"));
        String fileName = statFilter.getSchema() + "__" + statFilter.getTable() + ".csv";
        return DataContentDto.builder()
                .fileName(fileName)
                .content(sb.toString())
                .build();
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private TableStat getTableStat(String schemaName, String tableName, List<TableStat> tableStats) {
        return tableStats.stream()
                .filter(stat -> stat.getTableName().equals(tableName) && stat.getSchemaName().equals(schemaName))
                .findFirst().orElse(null);
    }
}
