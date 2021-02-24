package ru.maletskov.postgres.analyzer.entity.own;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "main_stat")
public class TableStat {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schema_name")
    private String schemaName;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "init_read_val")
    private Long initReadVal;

    @Column(name = "read_val")
    private Long readVal;

    @Column(name = "init_ins_val")
    private Long initInsVal;

    @Column(name = "ins_val")
    private Long insVal;

    @Column(name = "init_upd_val")
    private Long initUpdVal;

    @Column(name = "upd_val")
    private Long updVal;

    @Column(name = "init_del_val")
    private Long initDelVal;

    @Column(name = "del_val")
    private Long delVal;

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime updated;
}
