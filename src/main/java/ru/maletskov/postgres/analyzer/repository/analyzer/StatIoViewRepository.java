package ru.maletskov.postgres.analyzer.repository.analyzer;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maletskov.postgres.analyzer.entity.analyzer.StatIoView;

public interface StatIoViewRepository extends JpaRepository<StatIoView, Long> {
}
