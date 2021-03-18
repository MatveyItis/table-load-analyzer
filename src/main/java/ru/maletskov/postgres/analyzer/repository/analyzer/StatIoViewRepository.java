package ru.maletskov.postgres.analyzer.repository.analyzer;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;

public interface StatIoViewRepository extends JpaRepository<StatIoView, Long> {

    List<StatIoView> findDistinctBySchemaname(String schema);
}
