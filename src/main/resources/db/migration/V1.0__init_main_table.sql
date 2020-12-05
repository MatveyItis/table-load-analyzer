create table main_stat
(
    id            bigserial primary key,
    schema_name   varchar,
    table_name    varchar,
    init_read_val bigint,
    read_val      bigint,
    init_ins_val  bigint,
    ins_val       bigint,
    init_upd_val  bigint,
    upd_val       bigint,
    init_del_val  bigint,
    del_val       bigint,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null
);