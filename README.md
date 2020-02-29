## Springboot 从零开始的学习记录

学习目标：基于idea以及springboot完成一个简易的社区交流型web项目

### 学习工具:
  >* [bookstrap前端框架](https://v3.bootcss.com/)
  >* [maven 仓库](https://mvnrepository.com/)
  >* [spring-boot](https://spring.io/projects/spring-boot/)
  >* [lombok 插件](https://projectlombok.org/)
  >* [mybatis orm](http://mybatis.org/)
  >* [jquery](https://api.jquery.com/jQuery.post/)
  >* [postman插件](https://www.postman.com/)
  >* [bilibili视频](https://www.bilibili.com/video/av50200264?from=search&seid=6210538564540303903)
  >* [OkHttp](https://square.github.io/okhttp/)
----
### Mysql脚本:
>用户表
```sql
create table user
(
    id           int          auto_increment primary key,
    account_id   varchar(100) null,
    name         varchar(50)  null,
    token        char(36)     null,
    gmt_create   bigint       null,
    gmt_modified bigint       null
);
```
>问题表
```sql
create table question
(
    id            int           auto_increment primary key,
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
>回复表
```sql
create table comment
(
    id            int           auto_increment primary key,
    parent_id     int           not null comment '父类ID',
    content       varchar(1024) not null,
    gmt_create    bigint        not null comment '创建时间',
    gmt_modified  bigint        not null comment '修改时间',
    like_count    int default 0 null,
    commentator   int           not null comment '评论人ID',
    type          int           null comment '父类类型',
    comment_count int default 0 null
);

```
>通知表
```sql
create table notification
(
    id            int           auto_increment primary key,
    notifier      int           null comment '评论的人',
    notifier_name varchar(100)  null,
    outerId       int           null,
    outer_title   varchar(256)  null,
    gmt_create    bigint        null,
    status        int default 0 not null,
    receiver      int           null comment '收到回复的人',
    type          int           null
);
```
### mybatis配置generatorComfig.xml并引入依赖
> 简化数据库操作以及mapper代码
```maven
<plugin>
  <groupId>org.mybatis.generator</groupId>
  <artifactId>mybatis-generator-maven-plugin</artifactId>
  <version>1.4.0</version>
  <dependencies>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.35</version>
    </dependency>
  </dependencies>
</plugin>
```
> generatorComfig.xml配置数据库表
```xml
<table  tableName="user" domainObjectName="User" ></table>
<table  tableName="question" domainObjectName="Question"></table>
<table  tableName="comment" domainObjectName="Comment"></table>
<table  tableName="notification" domainObjectName="Notification"></table>
```
---
> mybatis配置xml的mvn运行命令：
>>mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

### 引入Lombok
> 简化代码，这里主要用的@Data注释
```maven
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <version>1.18.12</version>
  <scope>provided</scope>
</dependency>
```
### 引入OKHTTP
> 实现GitHub登陆时传输验证信息
```
<dependency>
  <groupId>com.squareup.okhttp3</groupId>
  <artifactId>okhttp</artifactId>
  <version>4.3.1</version>
</dependency>

```

---
### 学习笔记:

#### 1.实现GitHub登录
* 在GitHub上申请一个新APP
* 定义AccessDTO，用于传输GitHub的accesstoken
* 定义GitHubUser，用于存放获取的用户属性
* 定义GitHubProvider，用于实现获取用户信息
* 定义AuthorizeController，IndexContro用于实现登录与登出的控制类以及首页
* 定义navigation.html，以及index.html用于实现导航栏和首页
____

 先在 __GitHubProvider__ 中通过OkHttp传入封装好的AccessTokenDTO获取GitHub端传输回来的access_token，再将access_token通过OkHttp传到GitHub端获取user信息并赋值给GitHubUser对象。再在 __AuthorizeController__ 中将获取到的GitHubUser对象以及生成的token令牌存入User对象并进行数据库User表的增加或更新，最后将user的token写入cookie以及session，传到页面进行其他操作。
 ___
 导航栏 __navigation.html__ 通过对session内是否存在user对象进行判断从而显示登陆与未登录的不同状态。
 
 ___
 #### 2.实现发布问题以及首页展示功能
 * 在service层定义QuestionService，用于实现发布问题以及获取问题列表的逻辑
 * 定义Question以及QuestionDTO，对于数据库和页面所需的属性
 * 定义QuestionController以及publish.html。
 * 创建PaginationDTO，用于实现分页功能
 ___
 
 先实现 __publish.html__ 发布问题页面， 然后在 __PublishController__ post请求中获取前端页面数据存入question对象，再通过 __QuestionService__ 将问题存入数据库。
 ___
 首页展示功能是通过 __QuestionService__ 将user对象与question对象进行绑定，再包装到 __PaginationDTO__ 内的data，接着把前端请求的page传入 __PaginationDTO__ 对每一页的数据及其他属性进行封装，再model到前端页面进行展示，从而实现分页展示功能。
 >页码表前端
 ```html
   <!--首页跳转标签-->
<li th:if="${pagination.showFirstPage}">
   <a href="/" aria-label="Previous">
   		<span aria-hidden="true">&laquo;</span>
   </a>
</li>
	<!--具体页码跳转标签-->
<li th:each="page:${pagination.pages}"><a th:href="@{/(page=${page})}"th:text="${page}"></a></li>
	<!--尾页跳转标签-->
<li>
	<a th:href="@{/(page=${pagination.countPage})}" 
    	th:if="${pagination.showLastPage}" aria-label="Next">
		<span aria-hidden="true">&raquo;</span>
	</a>
</li>
```

___
#### 3.实现拦截器，自动部署以及导航栏等功能
* 在interpret目录下创建WebConfig实现WebMvcConfigurer接口
* 创建SessionInterceptor实现HandlerInterceptor接口
* 导入maven依赖进行热部署
* 定义ProfileController以及profile.html
* 定义question.html以及QuestionController，用于展示问题页面和控制类
 >热部署依赖
```
 <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-devtools</artifactId>
   <optional>true</optional>
 </dependency>
 ```
 在idea内setting->Build,Execution,Development->compiler->Build project automatically打钩。并用<kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>?</kbd>打开Registry，这里面是修改idea相关配置的地方，找到compiler.automake.allow.when.app.running打钩（实现项目运行时可自动编译）,从而实现热部署.
___
__拦截器__ 功能参考[Spring文档](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support),在 __WebConfig__ 里重写addInterceptors方法：
```java
@Override
public void addInterceptors(InterceptorRegistry registry){
	registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
}
```
对"/"目录下的请求进行拦截，并且在SessionInterceptor重写preHandle方法，在判断当前登录状态后进行拦截
___
导航栏完善登出以及个人中心页面及功能，登出功能直接在AuthorizeController内定义方法移除session并设置cookie的token为空。在 __profile__ 内实现 __questions__ 与 __replies__ 两个分支对应我的问题以及我的回复，这里先实现我的问题页面，原理与主页实现问题列表及分页相同。
____
question页面是由主页面上问题展示的链接进入，根据问题id获取question对象，再在 __questionService__ 内包装成QuestionDTO对象传输到前端页面
___
#### 4.实现回复功能以及异常处理
* 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
