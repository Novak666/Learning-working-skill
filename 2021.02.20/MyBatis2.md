# 1. MyBatis接口代理方式实现Dao层

## 1.1 简介

传统方式实现Dao层，我们既要编写接口，还要编写实现类。MyBatis框架可以帮助我们省略编写Dao层接口实现类的步骤。程序员只需要编写接口，由MyBatis框架根据接口的定义来创建该接口的<font color='red'>**动态代理**</font>对象

实现规则

1. <font color='red'>映射配置文件中的名称空间必须和Dao层接口的全类名相同</font>
2. <font color='red'>映射配置文件中的增删改查标签的id属性必须和Dao层接口的方法名相同</font>
3. <font color='red'>映射配置文件中的增删改查标签的parameterType属性必须和Dao层接口方法的参数相同</font>
4. <font color='red'>映射配置文件中的增删改查标签的resultType属性必须和Dao层接口方法的返回值相同</font>

## 1.2 代码实现

1. 删除mapper层（Dao层）接口的实现类
2. 修改映射配置文件
3. 修改service层接口的实现类，采用接口代理方式实现功能

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
<mapper namespace="com.itheima.mapper.StudentMapper">

    <sql id="select" >SELECT * FROM student</sql>

    <!--
        select：查询功能的标签
        id属性：唯一标识
        resultType属性：指定结果映射对象类型
        parameterType属性：指定参数映射对象类型
    -->
    <select id="selectAll" resultType="student">
        <include refid="select"/>
    </select>

    <select id="selectById" resultType="student" parameterType="int">
        <include refid="select"/> WHERE id = #{id}
    </select>

    <insert id="insert" parameterType="student">
        INSERT INTO student VALUES (#{id},#{name},#{age})
    </insert>

    <update id="update" parameterType="student">
        UPDATE student SET name = #{name},age = #{age} WHERE id = #{id}
    </update>

    <delete id="delete" parameterType="int">
        DELETE FROM student WHERE id = #{id}
    </delete>
……
</mapper>
```

业务层

<font color='red'>接口实现与之前的区别在于：测试代码是主要写在service层的，而持久层的实例mapper是通过接口代理实现的</font>

```java
public class StudentServiceImpl implements StudentService {

    @Override
    public List<Student> selectAll() {
        List<Student> list = null;
        SqlSession sqlSession = null;
        InputStream is = null;
        try{
            //1.加载核心配置文件
            is = Resources.getResourceAsStream("MyBatisConfig.xml");

            //2.获取SqlSession工厂对象
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

            //3.通过工厂对象获取SqlSession对象
            sqlSession = sqlSessionFactory.openSession(true);

            //4.获取StudentMapper接口的实现类对象
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class); // StudentMapper mapper = new StudentMapperImpl();

            //5.通过实现类对象调用方法，接收结果
            list = mapper.selectAll();

        } catch (Exception e) {

        } finally {
            //6.释放资源
            if(sqlSession != null) {
                sqlSession.close();
            }
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //7.返回结果
        return list;
    }
    ……
}
```

## 1.3 源码分析

- 分析动态代理对象如何生成的？ 

  通过动态代理开发模式，我们只编写一个接口，不写实现类，我们通过 getMapper() 方法最终获取到 org.apache.ibatis.binding.MapperProxy 代理对象，然后执行功能，而这个代理对象正是 MyBatis 使用了 JDK 的动态代理技术，帮助我们生成了代理实现类对象。从而可以进行相关持久化操作。 

  ```java
  StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
  ```

  mapper这个接口实现类对象，底层就是运用了JDK的动态代理，利用Proxy.newProxyInstance()生成代理对象

- 分析方法是如何执行的？

  <font color='red'>打断点查看，动态代理需要程序运行起来才能查看</font>

  动态代理实现类对象在执行方法的时候最终调用了 mapperMethod.execute() 方法，这个方法中通过 switch 语句根据操作类型来判断是新增、修改、删除、查询操作，最后一步回到了 MyBatis 最原生的 SqlSession 方式来执行增删改查。   

# 2. MyBatis映射配置文件 - 动态SQL

## 2.1 简介

Mybatis 的映射文件中，前面我们的 SQL 都是比较简单的，有些时候业务逻辑复杂时，我们的 SQL是动态变化的，此时在前面的学习中我们的 SQL 就不能满足要求了

## 2.2 if标签

<where>：条件标签。如果有动态条件，则使用该标签代替 where 关键字。

<if>：条件判断标签。

```xml
<if test="条件判断">
	查询条件拼接
</if>
```

StudentMapper.xml

```xml
<select id="selectCondition" resultType="student" parameterType="student">
    <include refid="select"/>
    <where>
        <if test="id != null">
            id = #{id}
        </if>
        <if test="name != null">
            AND name = #{name}
        </if>
        <if test="age != null">
            AND age = #{age}
        </if>
    </where>
</select>
```

示例代码

```java
public class Test01 {
    @Test
    public void selectCondition() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取StudentMapper接口的实现类对象
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);

        Student stu = new Student();
        stu.setId(2);
        stu.setName("李四");
        //stu.setAge(24);

        //5.调用实现类的方法，接收结果
        List<Student> list = mapper.selectCondition(stu);

        //6.处理结果
        for (Student student : list) {
            System.out.println(student);
        }

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
```

## 2.3 foreach标签

<foreach>：循环遍历标签。适用于多个参数或者的关系。

```xml
<foreach collection=“”open=“”close=“”item=“”separator=“”>
		获取参数
</foreach>
```

属性
collection：参数容器类型， (list-集合， array-数组)。
open：开始的 SQL 语句。
close：结束的 SQL 语句。
item：参数变量名。
separator：分隔符。

StudentMapper.xml

```xml
<select id="selectByIds" resultType="student" parameterType="list">
    <include refid="select"/>
    <where>
        <foreach collection="list" open="id IN (" close=")" item="id" separator=",">
            #{id}
        </foreach>
    </where>
</select>
```

示例代码

```java
public class Test01 {
    @Test
    public void selectByIds() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取StudentMapper接口的实现类对象
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);

        //5.调用实现类的方法，接收结果
        List<Student> list = mapper.selectByIds(ids);

        //6.处理结果
        for (Student student : list) {
            System.out.println(student);
        }

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
```

## 2.4 sql标签

sql 中可将重复的 sql 提取出来，使用时用 include 引用即可，最终达到 sql 重用的目的

<sql>：抽取 SQL 语句标签。 

<include>：引入 SQL 片段标签。 

```xml
<sql id="片段唯一标识">抽取的 SQL 语句</sql>
<include refid="片段唯一标识"/>
```

StudentMapper.xml

```xml
<!--抽取sql片段简化编写-->
<sql id="selectStudent" select * from student</sql>
<select id="findById" parameterType="int" resultType="student">
    <include refid="selectStudent"></include> where id=#{id}
</select>
<select id="findByIds" parameterType="list" resultType="student">
    <include refid="selectStudent"></include>
    <where>
        <foreach collection="array" open="id in(" close=")" item="id" separator=",">
            #{id}
        </foreach>
    </where>
</select>
```

# 3. MyBatis核心配置文件 - 分页插件

## 3.1 简介

- 分页可以将很多条结果进行分页显示。 
- 如果当前在第一页，则没有上一页。如果当前在最后一页，则没有下一页。 
- 需要明确当前是第几页，这一页中显示多少条结果。    
- MyBatis分页插件总结
  1. 在企业级开发中，分页也是一种常见的技术。而目前使用的 MyBatis 是不带分页功能的，如果想实现分页的 功能，需要我们手动编写 LIMIT 语句。但是不同的数据库实现分页的 SQL 语句也是不同的，所以手写分页 成本较高。这个时候就可以借助分页插件来帮助我们实现分页功能。 
  2. PageHelper：第三方分页助手。将复杂的分页操作进行封装，从而让分页功能变得非常简单。

## 3.2 使用

1. 导入jar包

   jsqlparser-3.1.jar

   pagehelper-5.1.10.jar

2. 在核心配置文件中集成分页助手插件

```xml
<!-- 注意：分页助手的插件  配置在通用mapper之前 -->
<!--集成分页助手插件-->
    <plugins>
        <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
    </plugins>
```

3. 示例代码

```java
public class Test01 {
    @Test
    public void selectPaging() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取StudentMapper接口的实现类对象
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);

        //通过分页助手来实现分页功能
        // 第一页：显示3条数据
        //PageHelper.startPage(1,3);
        // 第二页：显示3条数据
        //PageHelper.startPage(2,3);
        // 第三页：显示3条数据
        PageHelper.startPage(3,3);

        //5.调用实现类的方法，接收结果
        List<Student> list = mapper.selectAll();

        //6.处理结果
        for (Student student : list) {
            System.out.println(student);
        }

        //获取分页相关参数
        PageInfo<Student> info = new PageInfo<>(list);
        System.out.println("总条数：" + info.getTotal());
        System.out.println("总页数：" + info.getPages());
        System.out.println("当前页：" + info.getPageNum());
        System.out.println("每页显示条数：" + info.getPageSize());
        System.out.println("上一页：" + info.getPrePage());
        System.out.println("下一页：" + info.getNextPage());
        System.out.println("是否是第一页：" + info.isIsFirstPage());
        System.out.println("是否是最后一页：" + info.isIsLastPage());

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
```

# 4. MyBatis的多表操作

## 4.1 多表模型

多表模型分类

一对一：在任意一方建立外键，关联对方的主键。

一对多：在多的一方建立外键，关联一的一方的主键。

多对多：借助中间表，中间表至少两个字段，分别关联两张表的主键。   

## 4.2 一对一

一对一模型： 人和身份证，一个人只有一个身份证

代码见mybatis03

数据准备

```sql
CREATE TABLE person(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20),
	age INT
);
INSERT INTO person VALUES (NULL,'张三',23);
INSERT INTO person VALUES (NULL,'李四',24);
INSERT INTO person VALUES (NULL,'王五',25);

CREATE TABLE card(
	id INT PRIMARY KEY AUTO_INCREMENT,
	number VARCHAR(30),
	pid INT,
	CONSTRAINT cp_fk FOREIGN KEY (pid) REFERENCES person(id)
);
INSERT INTO card VALUES (NULL,'12345',1);
INSERT INTO card VALUES (NULL,'23456',2);
INSERT INTO card VALUES (NULL,'34567',3);
```

<font color='red'>OneToOneMapper.xml映射配置文件（重点）</font>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.itheima.table01.OneToOneMapper">
    <!--配置字段和实体对象属性的映射关系-->
    <resultMap id="oneToOne" type="card">
        <id column="cid" property="id" />
        <result column="number" property="number" />
        <!--
            association：配置被包含对象的映射关系
            property：被包含对象的变量名
            javaType：被包含对象的数据类型
        -->
        <association property="p" javaType="person">
            <id column="pid" property="id" />
            <result column="name" property="name" />
            <result column="age" property="age" />
        </association>
    </resultMap>

    <select id="selectAll" resultMap="oneToOne">
        SELECT c.id cid,number,pid,NAME,age FROM card c,person p WHERE c.pid=p.id
    </select>
</mapper>
```

MyBatisConfig.xml核心配置文件

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
        <package name="com.itheima.bean"/>
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
        <mapper resource="com/itheima/one_to_one/OneToOneMapper.xml"/>
        <mapper resource="com/itheima/one_to_many/OneToManyMapper.xml"/>
        <mapper resource="com/itheima/many_to_many/ManyToManyMapper.xml"/>
    </mappers>
</configuration>
```

示例代码

```java
public class Test01 {
    @Test
    public void selectAll() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取OneToOneMapper接口的实现类对象
        OneToOneMapper mapper = sqlSession.getMapper(OneToOneMapper.class);

        //5.调用实现类的方法，接收结果
        List<Card> list = mapper.selectAll();

        //6.处理结果
        for (Card c : list) {
            System.out.println(c);
        }

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
```

小节

```xml
<resultMap>：配置字段和对象属性的映射关系标签，自定义复杂结构。
    id 属性：唯一标识
    type 属性：实体对象类型
<id>：配置主键映射关系标签。
<result>：配置非主键映射关系标签。
    column 属性：表中字段名称
    property 属性： 实体对象变量名称
<association>：配置被包含对象的映射关系标签。
    property 属性：被包含对象的变量名
    javaType 属性：被包含对象的数据类型
```

## 4.3 一对多

一对多模型： 一对多模型：班级和学生，一个班级可以有多个学生

数据准备

```sql
CREATE TABLE classes(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20)
);
INSERT INTO classes VALUES (NULL,'黑马一班');
INSERT INTO classes VALUES (NULL,'黑马二班');


CREATE TABLE student(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(30),
	age INT,
	cid INT,
	CONSTRAINT cs_fk FOREIGN KEY (cid) REFERENCES classes(id)
);
INSERT INTO student VALUES (NULL,'张三',23,1);
INSERT INTO student VALUES (NULL,'李四',24,1);
INSERT INTO student VALUES (NULL,'王五',25,2);
INSERT INTO student VALUES (NULL,'赵六',26,2);
```

<font color='red'>OneToManyMapper.xml映射配置文件（重点）</font>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.itheima.table02.OneToManyMapper">
    <resultMap id="oneToMany" type="classes">
        <id column="cid" property="id"/>
        <result column="cname" property="name"/>

        <!--
            collection：配置被包含的集合对象映射关系
            property：被包含对象的变量名
            ofType：被包含对象的实际数据类型
        -->
        <collection property="students" ofType="student">
            <id column="sid" property="id"/>
            <result column="sname" property="name"/>
            <result column="sage" property="age"/>
        </collection>
    </resultMap>
    <select id="selectAll" resultMap="oneToMany">
        SELECT c.id cid,c.name cname,s.id sid,s.name sname,s.age sage FROM classes c,student s WHERE c.id=s.cid
    </select>
</mapper>
```

MyBatisConfig.xml核心配置文件同上

示例代码

```java
public class Test01 {
    @Test
    public void selectAll() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取OneToManyMapper接口的实现类对象
        OneToManyMapper mapper = sqlSession.getMapper(OneToManyMapper.class);

        //5.调用实现类的方法，接收结果
        List<Classes> classes = mapper.selectAll();

        //6.处理结果
        for (Classes cls : classes) {
            System.out.println(cls.getId() + "," + cls.getName());
            List<Student> students = cls.getStudents();
            for (Student student : students) {
                System.out.println("\t" + student);
            }
        }

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
```

小节

```xml
<resultMap>：配置字段和对象属性的映射关系标签。
    id 属性：唯一标识
    type 属性：实体对象类型
<id>：配置主键映射关系标签。
<result>：配置非主键映射关系标签。
    column 属性：表中字段名称
    property 属性： 实体对象变量名称
<collection>：配置被包含集合对象的映射关系标签。
    property 属性：被包含集合对象的变量名
    ofType 属性：集合中保存的对象数据类型
```

## 4.4 多对多

多对多模型：学生和课程，一个学生可以选择多门课程、一个课程也可以被多个学生所选择

数据准备

```sql
CREATE TABLE course(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20)
);
INSERT INTO course VALUES (NULL,'语文');
INSERT INTO course VALUES (NULL,'数学');


CREATE TABLE stu_cr(
	id INT PRIMARY KEY AUTO_INCREMENT,
	sid INT,
	cid INT,
	CONSTRAINT sc_fk1 FOREIGN KEY (sid) REFERENCES student(id),
	CONSTRAINT sc_fk2 FOREIGN KEY (cid) REFERENCES course(id)
);
INSERT INTO stu_cr VALUES (NULL,1,1);
INSERT INTO stu_cr VALUES (NULL,1,2);
INSERT INTO stu_cr VALUES (NULL,2,1);
INSERT INTO stu_cr VALUES (NULL,2,2);
```

<font color='red'>ManyToManyMapper.xml映射配置文件（重点）</font>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.itheima.table03.ManyToManyMapper">
    <resultMap id="manyToMany" type="student">
        <id column="sid" property="id"/>
        <result column="sname" property="name"/>
        <result column="sage" property="age"/>

        <collection property="courses" ofType="course">
            <id column="cid" property="id"/>
            <result column="cname" property="name"/>
        </collection>
    </resultMap>
    <select id="selectAll" resultMap="manyToMany">
        SELECT sc.sid,s.name sname,s.age sage,sc.cid,c.name cname FROM student s,course c,stu_cr sc WHERE sc.sid=s.id AND sc.cid=c.id
    </select>
</mapper>
```

MyBatisConfig.xml核心配置文件同上

示例代码

```java
public class Test01 {
    @Test
    public void selectAll() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取ManyToManyMapper接口的实现类对象
        ManyToManyMapper mapper = sqlSession.getMapper(ManyToManyMapper.class);

        //5.调用实现类的方法，接收结果
        List<Student> students = mapper.selectAll();

        //6.处理结果
        for (Student student : students) {
            System.out.println(student.getId() + "," + student.getName() + "," + student.getAge());
            List<Course> courses = student.getCourses();
            for (Course cours : courses) {
                System.out.println("\t" + cours);
            }
        }

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
```

