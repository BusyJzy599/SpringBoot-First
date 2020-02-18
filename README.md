## Springboot 学习

## 学习记录

### Mysql脚本
user表
```sql
-- auto-generated definition
create table user
(
    id           int auto_increment primary key,
    account_id   varchar(100) null,
    name         varchar(50)  null,
    token        char(36)     null,
    gmt_create   bigint       null,
    gmt_modified bigint       null
);
```
question表
```sql
-- auto-generated definition
create table question
(
    id            int           AUTO_INCREMENT primary key,
    title         varchar(60)   null,
    description   text          null,
    gmt_create    bigint        null,
    gmt_modified  bigint        null,
    creator       int           null,
    comment_count int default 0 null,
    view_count    int default 0 null,
    like_count    int default 0 null,
    tag           varchar(256)  null
);
```
