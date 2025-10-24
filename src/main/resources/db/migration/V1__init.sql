-- USERS
create table users(
                      id bigserial primary key,
                      version int,
                      username varchar(64) not null unique,
                      password_hash varchar(200) not null,
                      roles varchar(100) not null,
                      created_at timestamp,
                      updated_at timestamp
);

-- WALLET (یک-به-یک با کاربر)
create table wallet(
                       id bigserial primary key,
                       version int,
                       user_id bigint not null unique,
                       balance numeric(18,2) not null default 0,
                       created_at timestamp,
                       updated_at timestamp,
                       constraint fk_wallet_user foreign key (user_id) references users(id)
);

-- LEDGER
create table ledger(
                       id bigserial primary key,
                       version int,
                       wallet_id bigint not null,
                       amount numeric(18,2) not null,
                       type varchar(20) not null,
                       ref varchar(100) not null,
                       created_at timestamp,
                       updated_at timestamp,
                       constraint fk_ledger_wallet foreign key (wallet_id) references wallet(id)
);

-- Idempotency scoped to wallet
create unique index ix_ledger_wallet_ref on ledger(wallet_id, ref);
-- (اختیاری) برای جستجو
create index ix_ledger_ref on ledger(ref);
