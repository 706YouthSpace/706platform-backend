
# 技术说明文档

这个文档是为了让大家更好地理解代码，想要贡献代码的请看 [CONTRIBUTING.md](CONTRIBUTING.md)

## 如何启动项目
当我们配置好开发环境，把项目 clone 下来之后，我们只需要找到 BackendApplication，先 run 一次，让 IDEA 生成配置文件，
然后编辑这个启动配置在 active profiles 里填入 develop 保存，就配置好了本地的开发环境可以直接启动了。如果是用别的ide的小伙伴，在开发环境启动时加入参数 -Dspring.profiles.active=develop 就可以启动了

## 架构
因为业务逻辑还比较简单，现在我们不采用复杂的微服务架构，一切都建立在 spring-boot 之上，当然这也为了将来有一天需要升级为 spring cloud 打下基础。

总的来说，我们使用了如下的模块完成对应的功能：

* actuator 服务器健康状况监控
* lombok 自动生成代码
* prometheus 更多服务运维数据
* cache 我们使用注解的方式来做缓存，避免直接使用 redis 或者 memcached来完成缓存的工作，关于缓存下面还有更多说明
* data-jpa 我们使用 data-jpa 的框架来避免直接操作数据库的链接，这一方面解放了我们自己去深度管理 session 的麻烦，一方面让我们更好地在本地开发，避免依赖复杂数据库环境，另一方面也是为了之后在访问量上来之后做扩容的准备
* h2 开发环境本地数据库的实现
* mysql 线上环境数据库的实现
* mail-sender 我们用来发邮件的框架，将会把邮件集中起来用第三方的邮件服务发送
* swagger2 开发环境里给前端小伙伴的接口文档

### 具体框架的文档和学习资料

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring cache abstraction](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-caching)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Java Mail Sender](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-email)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#production-ready)
* [Prometheus](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/html/production-ready-features.html#production-ready-metrics-export-prometheus)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-redis)

### 框架的一些示例

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Caching Data with Spring](https://spring.io/guides/gs/caching/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)



### 缓存

不在代码里手动操作缓存的原因有如下几个：
* 开发环境我们是用 simple 类型的缓存，这样就不需要在本地搭建 redis服务了，simple类型的缓存用的是 ConcurrentHashMap，有助于我们在开发的时候快速启动服务
* 如果深度使用 redis 的复杂类型的话，未来缓存扩容是一个会面临的问题

当然如果我们是把 redis 当作别的用途，比方说 pubsub，那我们另说

### 接口查看地址
启动服务之后 [API接口查看](http://localhost:8080/api/swagger-ui/index.html)