# 1. MyBatis简单入门

## 1.1 框架简介

框架是一款半成品软件，我们可以基于这个半成品软件继续开发，来完成我们个性化的需求

## 1.2 ORM简介

- ORM(Object Relational Mapping)： 对象关系映射
- 指的是持久化数据和实体对象的映射模式，为了解决面向对象与关系型数据库存在的互不匹配的现象的技术。

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/1.png)

## 1.3 对JDBC的分析

- 原始 JDBC 的操作问题分析

  1. 频繁创建和销毁数据库的连接会造成系统资源浪费从而影响系统性能

  2. sql 语句在代码中硬编码，如果要修改 sql 语句，就需要修改 java 代码，造成代码不易维护
  3. 查询操作时，需要手动将结果集中的数据封装到实体对象中
  4. 增删改查操作需要参数时，需要手动将实体对象的数据设置到 sql 语句的占位符

- 原始 JDBC 的操作问题解决方案

  1. 使用数据库连接池初始化连接资源

  2. 将 sql 语句抽取到配置文件中
  3. 使用反射、内省等底层技术，将实体与表进行属性与字段的自动映射

## 1.4 MyBatis简介

mybatis 是一个优秀的基于java的持久层框架，它内部封装了jdbc，使开发者只需要关注sql语句本身，而不需要花费精力去处理加载驱动、创建连接、创建statement等繁杂的过程。

mybatis通过xml或注解的方式将要执行的各种 statement配置起来，并通过java对象和statement中sql的动态参数进行映射生成最终执行的sql语句。

最后mybatis框架执行sql并将结果映射为java对象并返回。采用ORM思想解决了实体和数据库映射的问题，对jdbc 进行了封装，屏蔽了jdbc api 底层访问细节，使我们不用与jdbc api 打交道，就可以完成对数据库的持久化操作。

MyBatis官网地址：<http://www.mybatis.org/mybatis-3/> 

## 1.5 简单案例

1. 数据准备

2. 导入jar包

   mysql-connector-java-5.1.37-bin.jar

   mybatis-3.5.3.jar

   log4j-1.2.17.jar

3. 在src下创建映射配置文件

StudentMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--MyBatis的DTD约束-->
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--
    mapper：核心根标签
    namespace属性：名称空间
-->
<mapper namespace="StudentMapper">
    <!--
        select：查询功能的标签
        id属性：唯一标识
        resultType属性：指定结果映射对象类型
        parameterType属性：指定参数映射对象类型
    -->
    <select id="selectAll" resultType="student">
        SELECT * FROM student
    </select>
</mapper>
```

4. 在src下创建核心配置文件

MyBatisConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--MyBatis的DTD约束-->
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">

<!--configuration 核心根标签-->
<configuration>

    <!--引入数据库连接的配置文件-->
    <properties resource="jdbc.properties"/>

    <!--配置LOG4J-->
    <settings>
        <setting name="logImpl" value="log4j"/>
    </settings>

    <!--起别名-->
    <typeAliases>
        <typeAlias type="com.itheima.bean.Student" alias="student"/>
        <!--<package name="com.itheima.bean"/>-->
    </typeAliases>

    <!--environments配置数据库环境，环境可以有多个。default属性指定使用的是哪个-->
    <environments default="mysql">
        <!--environment配置数据库环境  id属性唯一标识-->
        <environment id="mysql">
            <!-- transactionManager事务管理。  type属性，采用JDBC默认的事务-->
            <transactionManager type="JDBC"></transactionManager>
            <!-- dataSource数据源信息   type属性 连接池-->
            <dataSource type="POOLED">
                <!-- property获取数据库连接的配置信息 -->
                <property name="driver" value="${driver}" />
                <property name="url" value="${url}" />
                <property name="username" value="${username}" />
                <property name="password" value="${password}" />
            </dataSource>
        </environment>
    </environments>

    <!-- mappers引入映射配置文件 -->
    <mappers>
        <!-- mapper 引入指定的映射配置文件   resource属性指定映射配置文件的名称 -->
        <mapper resource="StudentMapper.xml"/>
    </mappers>
</configuration>
```

5. 编写测试类

```java
/*
    查询全部
 */
@Test
public void selectAll() throws Exception{
    //1.加载核心配置文件
    //InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");
    InputStream is = StudentTest01.class.getClassLoader().getResourceAsStream("MyBatisConfig.xml");

    //2.获取SqlSession工厂对象
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

    //3.通过SqlSession工厂对象获取SqlSession对象
    SqlSession sqlSession = sqlSessionFactory.openSession();

    //4.执行映射配置文件中的sql语句，并接收结果
    List<Student> list = sqlSession.selectList("StudentMapper.selectAll");

    //5.处理结果
    for (Student stu : list) {
        System.out.println(stu);
    }

    //6.释放资源
    sqlSession.close();
    is.close();
}
```

# 2. MyBatis相关API

## 2.1 加载资源

org.apache.ibatis.io.Resources：加载资源的工具类，Resources类帮助你从类路径下、文件系统或一个web URL中加载资源文件

核心方法

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/2.png)

## 2.2 获取SqlSessionFactory

org.apache.ibatis.session.SqlSessionFactoryBuilder：获取SqlSessionFactory工厂对象的功能类

核心方法

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/3.png)

```java
String resource = "org/mybatis/builder/mybatis-config.xml"; 
InputStream inputStream = Resources.getResourceAsStream(resource); 
SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder(); 
SqlSessionFactory factory = builder.build(inputStream);
```

## 2.3 获取SqlSession

org.apache.ibatis.session.SqlSessionFactory：获取SqlSession构建者对象的工厂接口

核心方法

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/4.png)

## 2.4 SqlSession

org.apache.ibatis.session.SqlSession：构建者对象接口。用于执行 SQL、管理事务、接口代理

核心方法

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/5.png)

# 3. MyBatis映射配置文件

## 3.1 基本介绍

映射配置文件包含了数据和对象之间的映射关系以及要执行的 SQL 语句

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/6.png)

id：唯一标识，配合名称空间使用

parameterType：指定参数映射的对象类型    

resultType：指定结果映射的对象类型

SQL 获取参数：#{属性名}

## 3.2 查询

StudentMapper.xml文件中添加标签

```xml
<select id="selectById" resultType="student" parameterType="int">
    SELECT * FROM student WHERE id = #{id}
</select>
```

示例代码

```java
/*
    根据id查询
 */
@Test
public void selectById() throws Exception{
    //1.加载核心配置文件
    InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

    //2.获取SqlSession工厂对象
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

    //3.通过工厂对象获取SqlSession对象
    SqlSession sqlSession = sqlSessionFactory.openSession();

    //4.执行映射配置文件中的sql语句，并接收结果
    Student stu = sqlSession.selectOne("StudentMapper.selectById", 3);

    //5.处理结果
    System.out.println(stu);

    //6.释放资源
    sqlSession.close();
    is.close();
}
```

## 3.3 新增

StudentMapper.xml文件中添加标签

```xml
<insert id="insert" parameterType="student">
    INSERT INTO student VALUES (#{id},#{name},#{age})
</insert>
```

示例代码

```java
/*
    新增功能
 */
@Test
public void insert() throws Exception{
    //1.加载核心配置文件
    InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

    //2.获取SqlSession工厂对象
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

    //3.通过工厂对象获取SqlSession对象
    //SqlSession sqlSession = sqlSessionFactory.openSession();
    SqlSession sqlSession = sqlSessionFactory.openSession(true);

    //4.执行映射配置文件中的sql语句，并接收结果
    Student stu = new Student(5,"周七",27);
    int result = sqlSession.insert("StudentMapper.insert", stu);

    //5.提交事务
    //sqlSession.commit();

    //6.处理结果
    System.out.println(result);

    //7.释放资源
    sqlSession.close();
    is.close();
}
```

## 3.4 修改

StudentMapper.xml文件中添加标签

```xml
<update id="update" parameterType="student">
    UPDATE student SET name = #{name},age = #{age} WHERE id = #{id}
</update>
```

示例代码

```java
/*
    修改功能
 */
@Test
public void update() throws Exception{
    //1.加载核心配置文件
    InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

    //2.获取SqlSession工厂对象
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

    //3.通过工厂对象获取SqlSession对象
    SqlSession sqlSession = sqlSessionFactory.openSession();
    //SqlSession sqlSession = sqlSessionFactory.openSession(true);

    //4.执行映射配置文件中的sql语句，并接收结果
    Student stu = new Student(5,"周七",37);
    int result = sqlSession.update("StudentMapper.update",stu);

    //5.提交事务
    sqlSession.commit();

    //6.处理结果
    System.out.println(result);

    //7.释放资源
    sqlSession.close();
    is.close();
}
```

## 3.5 删除

StudentMapper.xml文件中添加标签

```xml
<delete id="delete" parameterType="int">
    DELETE FROM student WHERE id = #{id}
</delete>
```

示例代码

```java
/*
    删除功能
 */
@Test
public void delete() throws Exception{
    //1.加载核心配置文件
    InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

    //2.获取SqlSession工厂对象
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

    //3.通过工厂对象获取SqlSession对象
    SqlSession sqlSession = sqlSessionFactory.openSession();
    //SqlSession sqlSession = sqlSessionFactory.openSession(true);

    //4.执行映射配置文件中的sql语句，并接收结果
    int result = sqlSession.delete("StudentMapper.delete",5);

    //5.提交事务
    sqlSession.commit();

    //6.处理结果
    System.out.println(result);

    //7.释放资源
    sqlSession.close();
    is.close();
}
```

## 3.6 总结

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/7.png)

# 4. MyBatis核心配置文件

## 4.1 基本介绍

核心配置文件包含了 MyBatis 最核心的设置和属性信息。如数据库的连接、事务、连接池信息等

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--MyBatis的DTD约束-->
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">

<!--configuration 核心根标签-->
<configuration>

    <!--environments配置数据库环境，环境可以有多个。default属性指定使用的是哪个-->
    <environments default="mysql">
        <!--environment配置数据库环境  id属性唯一标识-->
        <environment id="mysql">
            <!-- transactionManager事务管理。  type属性，采用JDBC默认的事务-->
            <transactionManager type="JDBC"></transactionManager>
            <!-- dataSource数据源信息   type属性 连接池-->
            <dataSource type="POOLED">
                <!-- property获取数据库连接的配置信息 -->
                <property name="driver" value="com.mysql.jdbc.Driver" />
                <property name="url" value="jdbc:mysql:///db1" />
                <property name="username" value="root" />
                <property name="password" value="root" />
            </dataSource>
        </environment>
    </environments>

    <!-- mappers引入映射配置文件 -->
    <mappers>
        <!-- mapper 引入指定的映射配置文件   resource属性指定映射配置文件的名称 -->
        <mapper resource="StudentMapper.xml"/>
    </mappers>
</configuration>
```

## 4.2 数据库连接配置文件引入

jdbc.properties

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://192.168.59.143:3306/db1
username=root
password=itheima
```

properties标签引入外部文件

```xml
<!--引入数据库连接的配置文件-->
<properties resource="jdbc.properties"/>
```

具体使用，如下配置

```xml
<!-- property获取数据库连接的配置信息 -->
	<property name="driver" value="${driver}" />
    <property name="url" value="${url}" />
    <property name="username" value="${username}" />
    <property name="password" value="${password}" />
```

## 4.3 起别名

<typeAliases>：为全类名起别名的父标签。

<typeAlias>：为全类名起别名的子标签。

属性

type：指定全类名

alias：指定别名

<package>：为指定包下所有类起别名的子标签。(别名就是类名)

具体如下配置

```xml
<!--起别名-->
    <typeAliases>
        <typeAlias type="com.itheima.bean.Student" alias="student"/>
        <!--<package name="com.itheima.bean"/>-->
    </typeAliase
```

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/8.png)

## 4.4 总结

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/9.png)

# 5. MyBatis传统方式实现Dao层

## 5.1 分层思想

分层思想：控制层(controller)、业务层(service)、持久层(dao)

调用流程

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/10.png)

## 5.2 LOG4J的配置和使用

在日常开发过程中，排查问题时难免需要输出 MyBatis 真正执行的 SQL 语句、参数、结果等信息，我们就可以借助 LOG4J 的功能来实现执行信息的输出。

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.19/pics/11.png)