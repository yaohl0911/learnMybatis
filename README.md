

教程链接：https://www.bilibili.com/video/BV1NE411Q7Nx?p=7

# 初识Mybatis

学习Mybatis思路：搭建环境 -->导入框架/依赖-->编写代码-->测试



## 搭建Mysql环境

```sql
CREATE DATABASE `mybatis`; 

USE `mybatis`;

CREATE TABLE `users`(
	id INT NOT NULL PRIMARY KEY,
	name VARCHAR(30) DEFAULT NULL,
	age INT NOT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO users(id, name, age) VALUES(1, 'aaa', 30);
INSERT INTO users(id, name, age) VALUES(2, 'bbb', 29);
INSERT INTO users(id, name, age) VALUES(3, 'ccc', 2);
```



## 导入Mybatis依赖

这里用Maven管理工程

注意需要把资源文件所在的目录加入到资源文件扫面路径，否则找不到资源文件。

```xml
<build>
    <!--
     Maven约定大于配置，默认只包含src/main/resources下的资源文件
     想要包含其他路径下的资源文件，需要修改Maven配置把对应的路径加进来
     -->
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>
    </resources>
</build>
```



## 编写代码

### JDBC方式的思路

实体类

接口

实现类

### Mybatis方式的思路

按照Mybatis官方教程的思路，要以Mybatis实现，首先要实现SqlSessionFactory和SqlSession等，由于是公共代码，所以提炼成一个Utils工具

```java
package com.yaohl0911.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
```

上述代码需要`mybatis-config.xml`配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="com/yaohl0911/dao/UserMapper.xml"/>
    </mappers>
</configuration>
```

实体类

```java
package com.yaohl0911.pojo;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private int age;
}
```

接口

```java
package com.yaohl0911.dao;


import com.yaohl0911.pojo.User;

import java.util.List;

public interface UserMapper {
    // 获取全部数据
    List<User> getUserList();

    //根据id查询数据
    User getUserByID(int id);

    // 插入一条数据
    int addUser(User user);

    // 修改一条数据
    int updateUser(User user);

    // 删除一条数据
    int deleteUser(int id);
}
```

Mapper配置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.yaohl0911.dao.UserMapper">

    <select id="getUserList" resultType="com.yaohl0911.pojo.User">
        select * from mybatis.users;
    </select>

    <select id="getUserByID" parameterType="int" resultType="com.yaohl0911.pojo.User">
        select * from mybatis.users where id=#{id};
    </select>

    <insert id="addUser" parameterType="com.yaohl0911.pojo.User">
        insert into mybatis.users (id, name, age) values (#{id}, #{name}, #{age});
    </insert>

    <update id="updateUser" parameterType="com.yaohl0911.pojo.User">
        update mybatis.users set name=#{name}, age=#{age} where id=#{id};
    </update>

    <delete id="deleteUser" parameterType="int">
        delete from mybatis.users where id=#{id};
    </delete>
</mapper>
```

测试

```java
package com.yaohl0911;

import com.yaohl0911.dao.UserMapper;
import com.yaohl0911.pojo.User;
import com.yaohl0911.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class TestDriver {
    @Test
    public void getUserListTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = userMapper.getUserList();
        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }

    @Test
    public void getUserByIDTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.getUserByID(1);
        System.out.println("Add user: " + user.toString());
        sqlSession.close();
    }

    @Test
    public void addUserTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int res = userMapper.addUser(new User(4, "dd2", 2));
        if(res != 0) {
            System.out.println("Add user succeed.");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUserTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int res = userMapper.updateUser(new User(4, "dd2", 1));
        if(res != 0) {
            System.out.println("Update user succeed.");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteUserTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int res = userMapper.deleteUser(4);
        if(res != 0) {
            System.out.println("Delete user succeed.");
        }
        sqlSession.commit();
        sqlSession.close();
    }
}
```


## Map和模糊查询扩展

### map比较容易定制化

- 使用map可以方便地自定义字段

- 多个参数时用map或者注解比较方便



### 模糊查找

```xml
<select id="getUserLike" resultType="com.yaohl0911.pojo.User">
    select * from mybatis.users where name like #{valus};
</select>
```

```java
@Test
public void getUserLikeTest() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    List<User> userList= userMapper.getUserLike("%d%"); // sql代码执行的时候，传递通配符"% %"
    for (User user : userList) {
        System.out.println(user);
    }

    sqlSession.close();
}
```



# 配置解析

##  属性优化

属性配置文件`sqlconfig.properties`    注：文件名随意

```properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis
username=root
password=123456
```

修改`mybatis-config.xml`文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties resource="sqlconfig.properties">
    		<!-- 这里也可以写一部分配置 -->
    </properties>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="com/yaohl0911/dao/UserMapper.xml"/>
    </mappers>
</configuration>
```

注意，如果在`sqlconfig.properties`和<properties resource="sqlconfig.properties"></properties>里都设置了某个字段，配置文件优先生效。

## 别名配置

### 直接指定别名

在配置文件合适的位置添加：

```xml
<typeAliases>
    <typeAlias type="com.yaohl0911.pojo.User" alias="user" ></typeAlias>
</typeAliases>
```

### 扫描某个包

这种用法，默认别名为首字母小写的类名，例如User类的别名是user。

```xml
<typeAliases>
  <package name="com.yaohl0911.pojo"/>
</typeAliases>
```

直接指定别名可以比较灵活的用任意别名，扫描包的方式不行，但是扫描包的方式可以用用注解实现。

## 映射器配置





# ResultMap

结果集映射，解决属性名和字段名不一致的问题



# 日志工厂

定位的好手段。

Mybatis支持的日志类型：

SLF4J 

LOG4J 【掌握】

LOG4J2 

JDK_LOGGING 

COMMONS_LOGGING 

STDOUT_LOGGING 【掌握】

NO_LOGGING

在Mybatis具体使用哪一个，在设置中配置，默认未设置

## 使用STDOUT_LOGGING

```xml
<settings>
    <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```

## 使用LOG4J

Mybatis-config.xml增加配置：

```xml
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>__
```

logconfig.properties配置文件 注：文件名任意
```properties
# 将等级为DEBUG的日志信息输出到console和file这两个目的地
log4j.rootLogger = DEBUG,console,file

# 控制台输出设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold = DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern = [%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} method:%l%n%m%n

# 文件输出设置
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File = ./log/log.log
log4j.appender.file.MaxFileSize = 10mb
log4j.appender.file.Threshold = DEBUG
log4j.appender.file.layout = org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern = [%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} method:%l%n%m%n

# 日志输出级别设置
log4j.logger.org.mybatis = DEBUG
log4j.logger.java.sql = DEBUG
log4j.logger.java.sql.Statement = DEBUG
log4j.logger.java.sql.ResultSet = DEBUG
log4j.logger.java.sql.PreparedStatement = DEBUG
```



# 分页

作用：减少数据的处理量

## 使用SQL的limite实现分页

```sql
SELECT * FROM user limite startIndex,pageSize;
```

## 使用Mybatis实现分页

使用前面学的map实现：

```xml
<select id="getUserList" resultType="user">
    select * from mybatis.users limit #{startInde}, #{pageSize};
</select>
```

## 使用RowBounds实现分页

不推荐使用