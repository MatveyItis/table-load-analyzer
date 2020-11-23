package ru.maletskov.postgres.analyzer.repository.own;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maletskov.postgres.analyzer.entity.own.TableStat;

public interface TableStatRepository extends JpaRepository<TableStat, Long> {
}
