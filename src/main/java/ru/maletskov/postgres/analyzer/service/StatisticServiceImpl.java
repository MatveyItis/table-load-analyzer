package ru.maletskov.postgres.analyzer.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String EXCLUDE_TABLE = "main_stat";
    public static final int MINUTE_PERIOD_BETWEEN_VALUES = 1;

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
                //todo move to mapper
                actualTableStat.setReadVal(stat.getSeqScan() - actualTableStat.getInitReadVal());
                actualTableStat.setDelVal(stat.getNTupDel() - actualTableStat.getInitDelVal());
                actualTableStat.setUpdVal(stat.getNTupUpd() - actualTableStat.getInitUpdVal());
                actualTableStat.setInsVal(stat.getNTupIns() - actualTableStat.getInitInsVal());
                actualTableStat.setInitReadVal(stat.getSeqScan());
                actualTableStat.setInitDelVal(stat.getNTupDel());
                actualTableStat.setInitUpdVal(stat.getNTupUpd());
                actualTableStat.setInitInsVal(stat.getNTupIns());
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
        var listStat = findAllByFilter(statFilter);
        if (listStat.isEmpty()) {
            //todo think about what the response should be
            return DataContentDto.builder().content("").build();
        }
        var isDataNormalized = isDataNormalized(listStat);
        if (!isDataNormalized) {
            //todo
            normalizeData(listStat);
        }
        List<String[]> dataLines = new ArrayList<>();
        var queryType = statFilter.getQueryType();
        listStat.forEach(s -> dataLines.add(new String[]{
                getNeededValue(s, queryType),
                s.getCreated().format(DateTimeFormatter.ofPattern(DATE_PATTERN))
        }));
        var sb = new StringBuilder();
        dataLines.forEach(d -> sb.append(convertToCSV(d)).append("\n"));
        var fileName = statFilter.getSchema() + "__" + statFilter.getTable() + ".csv";
        return DataContentDto.builder().fileName(fileName).content(sb.toString()).build();
    }

    @Transactional("ownTransactionManager")
    public List<TableStat> findAllByFilter(StatisticFilter filter) {
        String table = filter.getTable();
        String schema = filter.getSchema();
        LocalDateTime startDate = filter.getStartDateTime();
        LocalDateTime endDate = filter.getEndDateTime();
        if (startDate == null || endDate == null) {
            return tableStatRepository.findAllBy(table, schema);
        }
        return tableStatRepository.findAllBy(table, schema, startDate, endDate);
    }

    private String getNeededValue(TableStat tableStat, QueryType queryType) {
        switch (queryType) {
            case DELETE:
                return tableStat.getDelVal().toString();
            case INSERT:
                return tableStat.getInsVal().toString();
            case UPDATE:
                return tableStat.getUpdVal().toString();
            default:
                return tableStat.getReadVal().toString();
        }
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
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
                .map(s -> {
                    TableStat ts = new TableStat();
                    BeanUtils.copyProperties(s, ts);
                    ts.setId(null);
                    ts.setCreated(null);
                    ts.setUpdated(null);
                    return ts;
                })
                .findFirst().orElse(null);
    }

    private boolean isDataNormalized(List<TableStat> stats) {
        for (int i = 1; i < stats.size(); i++) {
            var created = stats.get(i).getCreated();
            var prevCreated = stats.get(i - 1).getCreated();
            if (created.getMinute() - prevCreated.getMinute() > MINUTE_PERIOD_BETWEEN_VALUES) {
                return false;
            }
        }
        return false;
    }

    private void normalizeData(List<TableStat> stats) {

    }
}
