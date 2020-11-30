package ru.maletskov.postgres.analyzer.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maletskov.postgres.analyzer.api.StatisticService;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;
import ru.maletskov.postgres.analyzer.entity.own.TableStat;
import ru.maletskov.postgres.analyzer.mapper.TableStatMapper;
import ru.maletskov.postgres.analyzer.repository.analyzer.StatIoViewRepository;
import ru.maletskov.postgres.analyzer.repository.own.TableStatRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatIoViewRepository statIoViewRepository;

    private final TableStatRepository tableStatRepository;

    private final TableStatMapper tableStatMapper;

    @Override
    @Transactional("analyzerTransactionManager")
    public List<StatIoView> getAllStatistic() {
        return statIoViewRepository.findAll();
    }

    @Override
    @Transactional("ownTransactionManager")
    public void updateStatistic(List<StatIoView> stats) {
        List<TableStat> tableStats = tableStatRepository.findAllByCreated(LocalDateTime.now().minusMinutes(1));
        if (tableStats.isEmpty()) {
            List<TableStat> newStats = new ArrayList<>();
            for (StatIoView stat : stats) {
                TableStat tableStat = tableStatMapper.toInitTableStat(stat);
                newStats.add(tableStat);
            }
            tableStatRepository.saveAll(newStats);
        } else {
            for (StatIoView stat : stats) {
                TableStat actualTableStat = getTableStat(stat.getSchemaname(), stat.getRelname(), tableStats);
                if (actualTableStat == null) {
                    continue;
                }
                actualTableStat.setReadVal(stat.getSeqScan() - actualTableStat.getInitReadVal());
                actualTableStat.setDelVal(stat.getNTupDel() - actualTableStat.getInitDelVal());
                actualTableStat.setUpdVal(stat.getNTupUpd() - actualTableStat.getInitUpdVal());
                actualTableStat.setInsVal(stat.getNTupIns() - actualTableStat.getInitInsVal());
                tableStatRepository.save(actualTableStat);
            }
        }
    }

    private TableStat getTableStat(String schemaName, String tableName, List<TableStat> tableStats) {
        for (TableStat stat : tableStats) {
            if (stat.getTableName().equals(tableName) && stat.getSchemaName().equals(schemaName)) {
                return stat;
            }
        }
        return null;
    }
}
