create table if not exists tb_user
(
    uid   bigint not null primary key auto_increment,
    username varchar(100),
    email varchar(100) not null,
    two_factor_auth_key varchar(100),
    two_factor_auth_enabled smallint not null default 0
);
