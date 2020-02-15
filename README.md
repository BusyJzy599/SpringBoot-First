## Springboot 学习

## 学习记录

### Mysql脚本
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

