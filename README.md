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
commnet表
```sql
-- auto-generated definition
create table comment
(
  id           int auto_increment
      primary key,
  parent_id    int           not null comment '父类ID',
  content      varchar(1024) not null,
  gmt_create   bigint        not null comment '创建时间',
  gmt_modified bigint        not null comment '修改时间',
  like_count   int default 0 null,
  commentator  int           not null comment '评论人ID',
  type         int           null comment '父类类型'
);

```

### xml配置mybatis的mvn运行命令：
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate