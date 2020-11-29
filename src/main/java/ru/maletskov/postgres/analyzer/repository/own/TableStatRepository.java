package ru.maletskov.postgres.analyzer.repository.own;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.maletskov.postgres.analyzer.entity.own.TableStat;

public interface TableStatRepository extends JpaRepository<TableStat, Long> {

    @Query(value = "select * from main_stat where created > :time", nativeQuery = true)
    List<TableStat> findAllByCreated(LocalDateTime time);

/*    @Query(value = "select schema_name, " +
            "       table_name, " +
            "       sum(read_val) as read_val, " +
            "       sum(ins_val)  as ins_val, " +
            "       sum(upd_val)  as upd_val, " +
            "       sum(del_val)  as del_val " +
            "from main_stat " +
            "where created between :start and :date " +
            "group by schema_name, table_name", nativeQuery = true)
    List<TableStat> findAllBetweenTwoDates(LocalDateTime start, LocalDateTime end);*/
}
