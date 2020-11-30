package ru.maletskov.postgres.analyzer.entity.analyzer;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pg_stat_user_tables")
public class StatIoView implements Serializable {

    @Id
    @Column(name = "relid")
    private Long relid;

    @Column(name = "schemaname")
    private String schemaname;

    @Column(name = "relname")
    private String relname;

    @Column(name = "seq_scan")
    private Long seqScan;

    @Column(name = "seq_tup_read")
    private Long seqTupRead;

    @Column(name = "idx_scan")
    private Long idxScan;

    @Column(name = "idx_tup_fetch")
    private Long idxTupFetch;

    @Column(name = "n_tup_ins")
    private Long nTupIns;

    @Column(name = "n_tup_upd")
    private Long nTupUpd;

    @Column(name = "n_tup_del")
    private Long nTupDel;

    @Column(name = "n_tup_hot_upd")
    private Long nTupHotUpd;

    @Column(name = "n_live_tup")
    private Long nLiveTup;

    @Column(name = "n_dead_tup")
    private Long nDeadTup;

    @Column(name = "n_mod_since_analyze")
    private Long nModSinceAnalyze;

    @Column(name = "last_vacuum")
    private LocalDateTime lastVacuum;

    @Column(name = "last_autovacuum")
    private LocalDateTime lastAutoVacuum;

    @Column(name = "last_analyze")
    private LocalDateTime lastAnalyze;

    @Column(name = "last_autoanalyze")
    private LocalDateTime lastAutoAnalyze;

    @Column(name = "vacuum_count")
    private Long vacuumCount;

    @Column(name = "autovacuum_count")
    private Long autovacuumCount;

    @Column(name = "analyze_count")
    private Long analyzeCount;

    @Column(name = "autoanalyze_count")
    private Long autoanalyzeCount;

}
