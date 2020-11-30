package ru.maletskov.postgres.analyzer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    @Mapping(target = "initReadVal", source = "seqScan")
    @Mapping(target = "readVal", expression = "java(0L)")
    @Mapping(target = "initDelVal", source = "NTupDel")
    @Mapping(target = "delVal", expression = "java(0L)")
    @Mapping(target = "initInsVal", source = "NTupIns")
    @Mapping(target = "insVal", expression = "java(0L)")
    @Mapping(target = "initUpdVal", source = "NTupUpd")
    @Mapping(target = "updVal", expression = "java(0L)")
    TableStat toInitTableStat(StatIoView statIoView);
}
