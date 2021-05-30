package ru.maletskov.postgres.analyzer.repository.own;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.maletskov.postgres.analyzer.entity.own.TableStat;

public interface TableStatRepository extends JpaRepository<TableStat, Long> {

    @Query(value = "select * from main_stat " +
            " where created = (select max(created) from main_stat)", nativeQuery = true)
    List<TableStat> findAllByLastCreated();

    @Query(value = "select * from main_stat " +
            " where table_name = :table and schema_name = :schema " +
            "order by created", nativeQuery = true)
    List<TableStat> findAllBy(String table, String schema);

    @Query(value = "select * from main_stat " +
            " where table_name = :table and schema_name = :schema and " +
            "(created >= :startDateTime and created <= :endDateTime) " +
            "order by created", nativeQuery = true)
    List<TableStat> findAllBy(String table, String schema,
                              LocalDateTime startDateTime,
                              LocalDateTime endDateTime);

    @Query(value = "select * from main_stat " +
            " where table_name = :table and schema_name = :schema and " +
            " created >= :startDate " +
            "order by created", nativeQuery = true)
    List<TableStat> findAllByStartDate(String table, String schema, LocalDate startDate);
}
