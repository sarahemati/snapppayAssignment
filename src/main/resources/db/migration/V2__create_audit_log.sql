
create table if not exists AUDIT_LOG (
                                         id          bigserial primary key,
                                         ts          timestamp not null,
                                         username    varchar(64) not null,
    action      varchar(32) not null,       -- DEPOSIT / TRANSFER / LOGIN / SIGNUP ...
    amount      numeric(18,2),
    ref         varchar(100),
    details     varchar(200),
    request_id  varchar(40)
    );

create index if not exists ix_audit_ts   on AUDIT_LOG(ts);
create index if not exists ix_audit_user on AUDIT_LOG(username);
