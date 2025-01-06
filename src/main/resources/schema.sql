-- テーブルスキーマの初期化
create table tasks
(
id BIGINT not null primary key AUTO_INCREMENT,
summary VARCHAR(256) not null,
description TEXT,
status VARCHAR(256) not null
);