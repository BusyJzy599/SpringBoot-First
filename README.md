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

 &ensp; &ensp; 先在 __GitHubProvider__ 中通过OkHttp传入封装好的AccessTokenDTO获取GitHub端传输回来的access_token，再将access_token通过OkHttp传到GitHub端获取user信息并赋值给GitHubUser对象。再在 __AuthorizeController__ 中将获取到的GitHubUser对象以及生成的token令牌存入User对象并进行数据库User表的增加或更新，最后将user的token写入cookie以及session，传到页面进行其他操作。
 ___
  &ensp; &ensp;导航栏 __navigation.html__ 通过对session内是否存在user对象进行判断从而显示登陆与未登录的不同状态。
 
 ___
 #### 2.实现发布问题以及首页展示功能
 * 在service层定义QuestionService，用于实现发布问题以及获取问题列表的逻辑
 * 定义Question以及QuestionDTO，对于数据库和页面所需的属性
 * 定义QuestionController以及publish.html。
 * 创建PaginationDTO，用于实现分页功能
 ___
 
  &ensp; &ensp;先实现 __publish.html__ 发布问题页面， 然后在 __PublishController__ post请求中获取前端页面数据存入question对象，再通过 __QuestionService__ 将问题存入数据库。
 ___
  &ensp; &ensp;首页展示功能是通过 __QuestionService__ 将user对象与question对象进行绑定，再包装到 __PaginationDTO__ 内的data，接着把前端请求的page传入 __PaginationDTO__ 对每一页的数据及其他属性进行封装，再model到前端页面进行展示，从而实现分页展示功能。
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
 &ensp; &ensp; 在idea内setting->Build,Execution,Development->compiler->Build project automatically打钩。并用<kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>?</kbd>打开Registry，这里面是修改idea相关配置的地方，找到compiler.automake.allow.when.app.running打钩（实现项目运行时可自动编译）,从而实现热部署.
___
 &ensp; &ensp;__拦截器__ 功能参考[Spring文档](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support),在 __WebConfig__ 里重写addInterceptors方法：
```java
@Override
public void addInterceptors(InterceptorRegistry registry){
	registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
}
```
对"/"目录下的请求进行拦截，并且在SessionInterceptor重写preHandle方法，在判断当前登录状态后进行拦截
___
 &ensp; &ensp;导航栏完善登出以及个人中心页面及功能，登出功能直接在AuthorizeController内定义方法移除session并设置cookie的token为空。在 __profile__ 内实现 __questions__ 与 __replies__ 两个分支对应我的问题以及我的回复，这里先实现我的问题页面，原理与主页实现问题列表及分页相同。
____
 &ensp; &ensp;question页面是由主页面上问题展示的链接进入，根据问题id获取question对象，再在 __questionService__ 内包装成QuestionDTO对象传输到前端页面
___
#### 4.实现回复功能以及消息通知功能
* 定义 CommentController，CommentService，用于实现评论以及二级评论
* 定义CommentCreateDTO，CommentDTO，用于存储获取的评论信息
* 定义ResultDTO，用于传输请求结果信息。
* 定义NotificationController，NotificationDTO以及NotificationService，用于实现通知消息功能
 
 ___
 
 &ensp; &ensp;前端问题界面通过js获取问题与评论数据，用ajax异步以及JSON的格式拿到评论的数据传到服务器端，再在 __CommentController__ 里用@RequestBody注解自动接收封装成 __CommentCreateDTO__ 对象，包装到 __Comment__ 对象内插入数据库并返回一个 __ResultDTO__ 对象进行解析操作，最后返回前端页面。
 ___
 
  &ensp; &ensp;二级评论面板的展开由js点击事件控制，然后用post请求到 __CommentController__ 服务器获取到二级回复数据集合，再用js循环拼接html代码显示到前端。
 
 ___
 &ensp; &ensp;消息通知功能包含消息通知面板，通知数量标签，通知阅读状态。通知消息在评论产生时创建并标记属性插入数据库，消息通知面板基于个人中心，从 __NotificationService__  中获取消息通知列表及状态显示到前端。消息数量标签是由 __NotificationService__ 内unreadCount方法获取并在 __SessionInterceptor__ 内写入session，继而展示到导航栏。通知阅读状态在用户点击未读通知链接时调用 __NotificationService__ 中的方法进行标记。
 
 ___
 
 #### 5.实现异常处理机制
 * error.html用于提示错误页面
 * CustomizeErrorController
 * CustomizeExceptionHandler，用于抓取异常信息并进行处理跳转
 * CustomizeException，用于实现自定义异常处理
 * ICustomizeErrorCode，用于实现一个接口对应多种异常（这里只定义了一类异常）
 * CustomizeErrorCode枚举类，用于存放错误代码与错误信息

___
###### @ControllerAdvice与  @ExceptionHandler
 &ensp; &ensp;异常处理分服务器端与用户端，这里使用springboot的Error Handling来进行处理。先定义 __ICustomizeErrorCode__ 接口，并在 __CustomizeErrorCode__ 中实现该接口，再在定义 __CustomizeException__ 中构造方法传入 __ICustomizeErrorCode__ 并重写message方法。在 __CustomizeExceptionHandler__ 中利用@ExceptionHandler抓取异常，接着进行contentType判断，如果是json格式则new一个ResultDTO对象，并从中获取错误信息和代码，再用输出流写到前端；如果是其他格式，则判断是否为定义中的异常，再将错误信息写入model，跳转到error.html页面，这样便实现了 __通用异常处理__。
###### ErrorController接口
 &ensp; &ensp;这里通过getStatus方法获取request状态，并在将请求错误信息返回到error页面，以此处理handle不能抓取的异常。







