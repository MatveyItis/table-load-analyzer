package ru.maletskov.postgres.analyzer.repository.own;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.maletskov.postgres.analyzer.entity.own.TableStat;

public interface TableStatRepository extends JpaRepository<TableStat, Long> {

    @Query(value = "select * from main_stat where created > :time", nativeQuery = true)
    List<TableStat> findAllByCreated(LocalDateTime time);
}
