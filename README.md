[TOC]
 
## oneData 统一数据管理平台项目配置

### 项目整体说明
    wiki地址：http://wiki.bi.org/pages/viewpage.action?spaceKey=bi&title=OneDataV2.0
    jira地址：
    功能点描述: 见wiki

### Spring profile + Maven profile 分离测试和生产环境资源
#### 顶层 pom.xml 文件设置
```$xslt
<profiles>
    <profile>
        <!-- 生产环境 -->
        <id>product</id>
        <properties>
            <profiles.activation>product</profiles.activation>
        </properties>
    </profile>
    <profile>
        <!-- BI测试环境 -->
        <id>test</id>
        <properties>
            <profiles.activation>test</profiles.activation>
        </properties>
    </profile>
    <profile>
        <!-- 本地开发环境 -->
        <id>dev</id>
        <properties>
            <profiles.activation>dev</profiles.activation>
        </properties>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    </profiles>
```
#### java-web pom.xml 设置
```$xslt
<resources>
    <resource>
        <directory>src/main/java</directory>
        <includes>
            <include>**/*.xml</include>
            <include>**/*.js</include>
        </includes>
    </resource>

    <resource>
        <filtering>true</filtering>
        <directory>src/main/resources</directory>
        <!-- **/*.properties 是指包括根目录或子目录所有properties类型的文件 -->
        <includes>
            <include>**/*.properties</include>
            <include>**/*.xml</include>
        </includes>
        <!-- 先排除dev、product 目录下的文件 -->
        <excludes>
            <exclude>dev/*</exclude>
            <exclude>test/*</exclude>
            <exclude>product/*</exclude>
        </excludes>
    </resource>

    <resource>
        <filtering>true</filtering>
        <directory>src/main/resources</directory>
        <!-- 包含，若没有指定则默认为 activeByDefault 标签定义的 profile -->
        <includes>
            <include>spring-mvc.xml</include>
            <include>spring-sso-redis.xml</include>
            <include>${profiles.activation}/*</include>
        </includes>
    </resource>
</resources>
```
#### bi-common pom.xml 文件设置
```$xslt
<resources>
    <resource>
        <directory>src/main/java</directory>
        <includes>
            <include>**/*.xml</include>
            <include>**/*.js</include>
        </includes>
    </resource>

    <resource>
        <filtering>true</filtering>
        <directory>src/main/resources</directory>
        <!-- **/*.properties 是指包括根目录或子目录所有properties类型的文件 -->
        <includes>
            <include>**/*.properties</include>
            <include>**/*.xml</include>
        </includes>
        <!-- 排除dev、product 目录下的文件 -->
        <excludes>
            <exclude>dev/*</exclude>
            <exclude>test/*</exclude>
            <exclude>product/*</exclude>
        </excludes>
    </resource>

    <resource>
        <filtering>true</filtering>
        <directory>src/main/resources</directory>
        <!-- 包含，若没有指定则默认为 activeByDefault 标签定义的profile -->
        <includes>
            <include>spring-jdbc.xml</include>
            <include>spring-redis.xml</include>
            <include>${profiles.activation}/*</include>
        </includes>
    </resource>
</resources>
```

#### 与 Spring 结合
整个项目中有两个模块需要加载property文件，分别是：bi-common 和 bi-web。两处都需要单独配置，请多留意此处。

##### 首先 bi-common
 ```$xslt
<bean id="jdbcPropertyPlaceholder"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="ignoreUnresolvablePlaceholders" value="true" />
        <property name="locations">
            <list>
                <value>classpath*:${profiles.activation:dev}/db.properties</value>
            </list>
        </property>
    </bean>
```
##### 然后 bi-web
将通过spring载配置文件的设置在 spring-mvc.xml 文件中
```$xslt
    <bean id="propertyConfig"
          class="com.bi.init.CustomizedPropertyPlaceholderConfigurer" parent="jdbcPropertyPlaceholder">
        <property name="ignoreResourceNotFound" value="true"/>
        <property name="locations">
            <list>
                <value>classpath*:${profiles.activation:dev}/sys.properties</value>
            </list>
        </property>
    </bean>
```
**特别注意**：为了避免配置文件被加载后属性值被覆盖，在spring-mvc.xml这个配置中，通过parent="jdbcPropertyPlaceholder"关联到bi-common

#### bi-web web.xml 设置
```$xslt
<context-param>
    <param-name>profiles.activation</param-name>
    <param-value>${profiles.activation}</param-value>
</context-param>
```

此项配置玩策完成。

### Spring AOP 动态切换数据源配置
此项目中，通过aop切换数据源，用户信息都从观星台2.0的数据库ibase4j.star下的sys_user表出；其他信息都从oneData库下的其他表出，不再从oneData.sys_user 出。

#### java 控制代码
在 bi-common 下的包路径：com.bi.dsRouting

#### bi-common 数据源配置
```
<bean id="dataSourceOD" class="com.alibaba.druid.pool.DruidDataSource"
          init-method="init" destroy-method="close">
        <property name="url">
            <value>${jdbc.url}</value>
        </property>
        <property name="username">
            <value>${jdbc.username}</value>
        </property>
        <property name="password">
            <value>${jdbc.password}</value>
        </property>
        <property name="initialSize">
            <value>${jdbc.initialSize}</value>
        </property>
        <property name="maxActive">
            <value>${jdbc.maxActive}</value>
        </property>
        <property name="proxyFilters">
            <list>
                <ref bean="stat-filter"/>
                <ref bean="wall-filter"/>
            </list>
        </property>
    </bean>

    <bean id="dataSourceStar" class="com.alibaba.druid.pool.DruidDataSource"
          init-method="init" destroy-method="close">
        <property name="url">
            <value>${star_jdbc.url}</value>
        </property>
        <property name="username">
            <value>${star_jdbc.username}</value>
        </property>
        <property name="password">
            <value>${star_jdbc.password}</value>
        </property>
        <property name="initialSize">
            <value>${star_jdbc.initialSize}</value>
        </property>
        <property name="maxActive">
            <value>${star_jdbc.maxActive}</value>
        </property>
        <property name="proxyFilters">
            <list>
                <ref bean="stat-filter"/>
                <ref bean="wall-filter"/>
            </list>
        </property>
    </bean>

    <!-- 动态数据源 -->
    <bean id="dataSource" class="com.bi.dsRouting.RoutingDataSource">
        <property name="targetDataSources">
            <map key-type="com.bi.dsRouting.RoutingStrategy">
                <!-- jdbc-key-1 和 jdbc-key-2 配置在 db.properties 中 -->
                <entry key="${jdbc-key-1}" value-ref="dataSourceOD"/>
                <entry key="${jdbc-key-2}" value-ref="dataSourceStar"/>
            </map>
        </property>
        <!-- 默认目标数据源为 onedata 数据源 -->
        <property name="defaultTargetDataSource" ref="dataSourceOD"/>
    </bean>
```

#### bi-web 中 spring application xml 配置
bi-web 项目有两个spring 配置文件：spring-sso-redis.xml 和 spring-mvc.xml
此处我将 Spring AOP 配置在 spring-mvc.xml 这个文件中：
```$xslt
<!-- 配置AOP -->
<bean id="dataSourceAspect" class="com.bi.dsRouting.DataSourceAspect" />

<!--  配置参与事务的类 -->
<!-- Advisor定义，切入点和通知分别为txPointcut、txAdvice -->
<aop:config proxy-target-class="true">
    <aop:pointcut id="pc" expression="execution(* com.bi.service..*(..))"/>
    <aop:advisor pointcut-ref="pc" advice-ref="dataSourceAspect" order="1"/>
    <aop:advisor pointcut-ref="pc" advice-ref="txAdvice" order="2"/>
</aop:config>

```

### logback + slf4j 配置
此项目找那个common为公共模块，因此将 logback.xml 放在此模块中。

#### bi-common logback.xml 设置 
```$xslt
<?xml version="1.0" encoding="UTF-8"?>

<!-- https://logback.qos.ch/manual/appenders.html -->
<!-- 如果不指定scanPeriod，默认情况下是每分钟重新读取的配置文件。这里可以通过scanPeriod属性配置时间间隔，单位有：milliseconds, seconds, minutes or hours，如上面的三十秒（数值跟单位中间空格隔开）。这里没有指定单位的情况下默认单位是milliseconds。 -->
<configuration scan="true" scanPeriod="30 seconds">

    <property name="DEV_HOME" value="E:/javaLogs/oneData2" />

    <!-- console -->
    <appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>[%level]-%d{yyyy-MM-dd HH:mm:ss.SSS} %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- file debug -->
    <appender name="DEBUG_ROLLING"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${DEV_HOME}/debug.log</file>

        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- rollover daily -->
            <fileNamePattern>${DEV_HOME}/debug.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!-- each file should be at most 20MB, keep 60 days worth of history, but at most 1GB -->
            <maxFileSize>20MB</maxFileSize>
            <maxHistory>60</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>

        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                <pattern>[%level]-%d{yyyy-MM-dd HH:mm:ss.SSS}-%-4relative- [%thread] - %class - %msg%n</pattern>
            </Pattern>
        </encoder>
    </appender>

    <!-- file warn  -->
    <appender name="WARN_ROLLING"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${DEV_HOME}/debug.log</file>

        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- rollover daily -->
            <fileNamePattern>${DEV_HOME}/debug.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!-- each file should be at most 20MB, keep 60 days worth of history, but at most 1GB -->
            <maxFileSize>20MB</maxFileSize>
            <maxHistory>60</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>

        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                <pattern>[%level]-%d{yyyy-MM-dd HH:mm:ss.SSS}-%-4relative- [%thread] - %class - %msg%n</pattern>
            </Pattern>
        </encoder>
    </appender>

    <!-- file error -->
    <appender name="ERROR_ROLLING"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${DEV_HOME}/error.log</file>

        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- rollover daily -->
            <fileNamePattern>${DEV_HOME}/error.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!-- each file should be at most 20MB, keep 60 days worth of history, but at most 1GB -->
            <maxFileSize>20MB</maxFileSize>
            <maxHistory>60</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>

        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                <pattern>[%level]-%d{yyyy-MM-dd HH:mm:ss.SSS}-%-4relative- [%thread] - %class - %msg%n</pattern>
            </Pattern>
        </encoder>
    </appender>

    <!-- 将org.springframework.web包下的类的日志级别设置为DEBUG，
        我们开发Spring MVC经常出现和参数类型相关的4XX错误，设置此项我们可以看到更相信的信息 -->
    <logger name="org.springframework" level="warn" additivity="false">
        <appender-ref ref="WARN_ROLLING" />
    </logger>

    <logger name="com.bi" level="debug" additivity="false">
        <appender-ref ref="consoleAppender" />
        <appender-ref ref="DEBUG_ROLLING" />
    </logger>

    <root level="error">
        <appender-ref ref="ERROR_ROLLING" />
    </root>
</configuration>
```

#### bi-common spring application context 配置
junapi-common项目有两个spring 配置文件：spring-sso-jdbc.xml 和 spring-redis.xml
此处我将 logback 配置在spring-mvc这个文件中：
```$xslt
    <bean class="ch.qos.logback.ext.spring.ApplicationContextHolder"/>
    <bean class="ch.qos.logback.core.rolling.RollingFileAppender"/>
```

#### java-web web.xml 配置
```$xslt
<context-param>
    <param-name>logbackConfigLocation</param-name>
    <param-value>classpath:${profiles.activation:dev}/logback.xml</param-value>
</context-param>

<!-- https://github.com/qos-ch/logback-extensions/wiki/Spring
This listener should be registered before ContextLoaderListener in web.xml, when using custom Logback initialization -->
<listener>
    <listener-class>ch.qos.logback.ext.spring.web.LogbackConfigListener</listener-class>
</listener>
```

#### 怎么用 logback
```$xslt
public class DataSourceAspect implements MethodBeforeAdvice, AfterReturningAdvice {

    private final static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        DataSourceHolder.clearDataSourceType();
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (method.isAnnotationPresent(DataSource.class)) {
            DataSource datasource = method.getAnnotation(DataSource.class);
            DataSourceHolder.setDataSourceType(datasource.value());
            logger.info("==>>切换数据源切换成功<<== 数据源:{} class:{} method:{}", datasource.value(), DataSource.class.getName(), method);
        }
    }
}
```
日志级别根据实际需要调整。

### OA代理设置 
因为账号使用的是oa的账号，开发、测试过程中需要连接oa的接口，本地环境无法连接正式的OA库，根据连接OA开发同学提供的OA账户接口连接正式环境，还需要代理。

此处我将代理配置在 bi-web项目的 sys.properties 文件中。不同环境的设置在各环境目录下的sys.properties中。开发同学请留意下。

#### dev 环境，即开发环境，也即是本地环境
```$xslt
#############开发环境###############
#### OA
oaHost= https://oa.bi.org/
ssoHost = b9603b9ece8b.bi.org
userAgent=yuze
```

#### test 环境，即测试环境
```$xslt
#############正式环境###############
#### OA
oaHost= https://oa.bi.org/
ssoHost = b9603b9ece8b.bi.org
userAgent=""
```


#### product 环境，即正式环境
正式环境不需要代理
```$xslt
#############正式环境###############
#### OA
oaHost= https://oa.bi.org/
ssoHost = b9603b9ece8b.bi.org
userAgent=""
```

#### 连接 OA 接口

com.bi.filter.SsoFilter 和 com.bi.controller.SysLoginController 两个类都有需要连OA接口，根据配置动态设置接口代理
```$xslt
String userAgent = System.getProperty("userAgent");
if(!StringUtils.isEmpty(userAgent))
{
    LOGGER.info("Get user info from OA accounts: userAgent is: [{}], OA api is: [{}]", userAgent, uri);
    httpPost.setHeader("User-Agent", userAgent);
}
```

### mvn 打包
```$xslt
### 将 dev 目录下的properties打包到resources中
mvn clean package -Dmaven.test.skip=true -P dev

### 将 test 目录下的properties打包到resources中
mvn clean package -Dmaven.test.skip=true -P test

### 将 product 目录下的properties打包到resources中
mvn clean package -Dmaven.test.skip=true -P product
```