package ru.maletskov.postgres.analyzer.mapper;

import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;
import ru.maletskov.postgres.analyzer.entity.own.TableStat;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TableStatMapper {

    @Mapping(target = "tableName", source = "relname")
    @Mapping(target = "schemaName", source = "schemaname")
    @Mapping(target = "initReadVal", expression = "java(statIoView.getSeqScan() + statIoView.getIdxScan())")
    @Mapping(target = "readVal", expression = "java(0L)")
    @Mapping(target = "initDelVal", source = "NTupDel")
    @Mapping(target = "delVal", expression = "java(0L)")
    @Mapping(target = "initInsVal", source = "NTupIns")
    @Mapping(target = "insVal", expression = "java(0L)")
    @Mapping(target = "initUpdVal", source = "NTupUpd")
    @Mapping(target = "updVal", expression = "java(0L)")
    TableStat toInitTableStat(StatIoView statIoView);

    default void updateTableStat(@MappingTarget TableStat actual, StatIoView stat, LocalDateTime created) {
        actual.setReadVal((resolveNotNull(stat.getSeqScan()) + resolveNotNull(stat.getIdxScan())) - actual.getInitReadVal());
        actual.setDelVal(stat.getNTupDel() - actual.getInitDelVal());
        actual.setUpdVal(stat.getNTupUpd() - actual.getInitUpdVal());
        actual.setInsVal(stat.getNTupIns() - actual.getInitInsVal());
        actual.setInitReadVal(resolveNotNull(stat.getSeqScan()) + resolveNotNull(stat.getIdxScan()));
        actual.setInitDelVal(stat.getNTupDel());
        actual.setInitUpdVal(stat.getNTupUpd());
        actual.setInitInsVal(stat.getNTupIns());
        actual.setCreated(created);
    }

    default Long resolveNotNull(Long number) {
        return number == null ? 0L : number;
    }
}
