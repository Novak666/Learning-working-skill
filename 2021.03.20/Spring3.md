# 1. AOP简介

## 1.1 AOP概念

AOP(Aspect Oriented Programing)面向切面编程，是通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术

+ AOP弥补了OOP的不足，基于OOP基础之上进行横向开发
  - uOOP规定程序开发以类为主体模型，一切围绕对象进行，完成某个任务先构建模型
  - uAOP程序开发主要关注基于OOP开发中的共性功能，一切围绕共性功能进行，完成某个任务先构建可能遇到的所有共性功能（当所有功能都开发出来也就没有共性与非共性之分）

## 1.2 AOP作用

在程序运行期间，在不修改源码的情况下对方法进行功能增强

- 伴随着AOP时代的降临，可以从各个行业的标准化、规范化开始入手，一步一步将所有共性功能逐一开发完毕，最终以功能组合来完成个别业务模块乃至整体业务系统的开发
- 目标：将软件开发由手工制作走向半自动化/全自动化阶段，实现“插拔式组件体系结构”搭建

## 1.3 AOP相关术语

- Joinpoint(连接点)：就是方法
- Pointcut(切入点)：就是挖掉共性功能的方法
- Advice(通知)：就是共性功能，最终以一个方法的形式呈现
- Aspect(切面)：就是共性功能与挖的位置的对应关系
- Target(目标对象)：就是挖掉功能的方法对应的类产生的对象，这种对象是无法直接完成最终工作的
- Weaving(织入)：就是将挖掉的功能回填的动态过程
- Proxy(代理)：目标对象无法直接完成工作，需要对其进行功能回填，通过创建原始对象的代理对象实现
- Introduction(引入/引介) ：就是对原始对象无中生有的添加成员变量或成员方法

## 1.4 AOP开发过程

- 开发阶段(开发者完成)
  - 正常的制作程序
  - 将非共性功能开发到对应的目标对象类中，并制作成切入点方法
  - 将共性功能独立开发出来，制作成**通知**
  - 在配置文件中，声明**切入点**
  - 在配置文件中，声明**切入点**与**通知**间的关系（含**通知类型**），即**切面**
- 运行阶段(AOP完成)
  - Spring容器加载配置文件，监控所有配置的**切入点**方法的执行
  - 当监控到**切入点**方法被运行，使用**代理**机制，动态创建**目标对象**的**代理对象**，根据**通知类别**，在**代理对象**的对应位置将**通知**对应的功能**织入**，完成完整的代码逻辑并运行

# 2. AOP配置—基于XML

## 2.1 简单案例

1. 导入 AOP 相关坐标

2. 创建目标接口和目标类（内部有切点）

3. 创建切面类（内部有增强方法）

4. 将目标类和切面类的对象创建权交给 spring

5. 在 applicationContext.xml 中配置织入关系

6. 测试代码



1. 导入 AOP 相关坐标

```xml
<!--导入spring的context坐标，context依赖aop-->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>5.0.5.RELEASE</version>
</dependency>
<!-- aspectj的织入 -->
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjweaver</artifactId>
  <version>1.8.13</version>
</dependency>
```

2. 创建目标接口和目标类（内部有切点）

```java
public interface TargetInterface {
    public void method();
}

public class Target implements TargetInterface {
    @Override
    public void method() {
        System.out.println("Target running....");
    }
}
```

3. 创建切面类（内部有增强方法）

```java
public class MyAspect {
    //前置增强方法
    public void before(){
        System.out.println("前置代码增强.....");
    }
}
```

4. 将目标类和切面类的对象创建权交给 spring

```xml
<!--配置目标类-->
<bean id="target" class="com.itheima.aop.Target"></bean>
<!--配置切面类-->
<bean id="myAspect" class="com.itheima.aop.MyAspect"></bean>

```

5. 在 applicationContext.xml 中配置织入关系

导入aop命名空间

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

```

5. 在 applicationContext.xml 中配置织入关系

   配置切点表达式和前置增强的织入关系

```xml
<aop:config>
    <!--引用myAspect的Bean为切面对象-->
    <aop:aspect ref="myAspect">
        <!--配置Target的method方法执行时要进行myAspect的before方法前置增强-->
        <aop:before method="before" pointcut="execution(public void com.itheima.aop.Target.method())"></aop:before>
    </aop:aspect>
</aop:config>
```

6. 测试代码

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AopTest {
    @Autowired
    private TargetInterface target;
    @Test
    public void test1(){
        target.method();
    }
}
```

## 2.2 aop:config标签

- 名称：aop:config

- 类型：**标签**

- 归属：beans标签

- 作用：设置AOP

- 格式：

  ```xml
  <beans>
      <aop:config>……</aop:config>
      <aop:config>……</aop:config>
  </beans>
  ```

- 说明：一个beans标签中可以配置多个aop:config标签

## 2.3 aop:aspect标签

- 名称：aop:aspect

- 类型：**标签**

- 归属：aop:config标签

- 作用：设置具体的AOP通知对应的切入点所在类

- 格式：

  ```xml
  <aop:config>
      <aop:aspect ref="beanId">……</aop:aspect>
      <aop:aspect ref="beanId">……</aop:aspect>
  </aop:config>
  ```

- 说明：

  一个aop:config标签中可以配置多个aop:aspect标签

- 基本属性：

  - ref ：通知所在的bean的id

## 2.4 aop:pointcut标签

- 名称：aop:pointcut

- 类型：**标签**

- 归属：aop:config标签、aop:aspect标签

- 作用：设置切入点

- 格式：

  ```xml
  <aop:config>
      <aop:pointcut id="pointcutId" expression="……"/>
      <aop:aspect>
          <aop:pointcut id="pointcutId" expression="……"/>
      </aop:aspect>
  </aop:config>
  ```

- 说明：

  一个aop:config标签中可以配置多个aop:pointcut标签，且该标签可以配置在aop:aspect标签内

- 基本属性：

  - id ：识别切入点的名称
  - expression ：切入点表达式

## 2.5 切点表达式的组成

- 切入点描述的是某个方法

- 切入点表达式是一个快速匹配方法描述的通配格式，类似于正则表达式

  ```xml
  关键字（访问修饰符  返回值  包名.类名.方法名（参数）异常名）
  ```

​	关键字：描述表达式的匹配模式（参看关键字列表）

​	访问修饰符：方法的访问控制权限修饰符

​	类名：方法所在的类（此处可以配置接口名称）

​	异常：方法定义中指定抛出的异常

- 范例：

  ```xml
  execution（public User com.itheima.service.UserService.findById（int））
  ```

### 2.5.1 关键字

- execution ：匹配执行指定方法
- args ：匹配带有指定参数类型的方法
- within ：…… 
- this ：…… 
- target ：…… 
- @within ：…… 
- @target ：…… 
- @args ：…… 
- @annotation ：…… 
- bean ：……
- reference pointcut ：……

### 2.5.2 通配符

- *：单个独立的任意符号，可以独立出现，也可以作为前缀或者后缀的匹配符出现

  ```xml
  execution（public * com.itheima.*.UserService.find*（*））
  ```

​	匹配com.itheima包下的任意包中的UserService类或接口中所有find开头的带有一个参数的方法

- .. ：表示当前包及其子包下的类，可以独立出现，常用于简化包名与参数的书写

  ```xml
  execution（public User com..UserService.findById（..））
  ```

​	匹配com包下的任意包中的UserService类或接口中所有名称为findById的方法

- +：专用于匹配子类类型

  ```xml
  execution(* *..*Service+.*(..))
  ```

示例：

```xml
execution(public void com.itheima.aop.Target.method())	
execution(void com.itheima.aop.Target.*(..))
execution(* com.itheima.aop.*.*(..))
execution(* com.itheima.aop..*.*(..))
execution(* *..*.*(..))
```

### 2.5.3 逻辑运算符

- && ：连接两个切入点表达式，表示两个切入点表达式同时成立的匹配
- || ：连接两个切入点表达式，表示两个切入点表达式成立任意一个的匹配
- ! ：连接单个切入点表达式，表示该切入点表达式不成立的匹配

## 2.6 切入点的三种配置方式

```xml
<aop:config>
    <!--配置公共切入点-->
    <aop:pointcut id="pt1" expression="execution(* *(..))"/>
    <aop:aspect ref="myAdvice">
        <!--配置局部切入点-->
        <aop:pointcut id="pt2" expression="execution(* *(..))"/>
        <!--引用公共切入点-->
        <aop:before method="logAdvice" pointcut-ref="pt1"/>
        <!--引用局部切入点-->
        <aop:before method="logAdvice" pointcut-ref="pt2"/>
        <!--直接配置切入点-->
        <aop:before method="logAdvice" pointcut="execution(* *(..))"/>
    </aop:aspect>
</aop:config>
```

## 2.7 通知的类型

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.20/pics/1.png)

**注意：后置通知应改为返回后通知**

### 2.7.1 aop:before标签

- 名称：aop:before

- 类型：**标签**

- 归属：aop:aspect标签

- 作用：最终通知

- 格式：

  ```xml
  <aop:aspect ref="adviceId">
      <aop:before method="methodName" pointcut="……"/>
  </aop:aspect>
  ```

- 说明：一个aop:aspect标签中可以配置多个aop:before标签

- 基本属性：

  - method ：在通知类中设置当前通知类别对应的方法
  - pointcut ：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突
  - pointcut-ref ：设置当前通知对应的切入点id，与pointcut属性冲突

### 2.7.2 aop:after标签

- 名称：aop:after

- 类型：**标签**

- 归属：aop:aspect标签

- 作用：后置通知

- 格式：

  ```xml
  <aop:aspect ref="adviceId">
      <aop:after method="methodName" pointcut="……"/>
  </aop:aspect>
  ```

- 说明：一个aop:aspect标签中可以配置多个aop:after标签

- 基本属性：

  - method ：在通知类中设置当前通知类别对应的方法
  - pointcut ：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突
  - pointcut-ref ：设置当前通知对应的切入点id，与pointcut属性冲突

### 2.7.3 aop:after-returning标签

- 名称：aop:after-returning

- 类型：**标签**

- 归属：aop:aspect标签

- 作用：返回后通知

- 格式：

  ```xml
  <aop:aspect ref="adviceId">
      <aop:after-returning method="methodName" pointcut="……"/>
  </aop:aspect>
  ```

- 说明：一个aop:aspect标签中可以配置多个aop:after-returning标签

- 基本属性：

  - method ：在通知类中设置当前通知类别对应的方法
  - pointcut ：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突
  - pointcut-ref ：设置当前通知对应的切入点id，与pointcut属性冲突

### 2.7.4 aop:after-throwing标签

- 名称：aop:after-throwing

- 类型：**标签**

- 归属：aop:aspect标签

- 作用：设置抛出异常后通知

- 格式：

  ```xml
  <aop:aspect ref="adviceId">
      <aop:after-throwing method="methodName" pointcut="……"/>
  </aop:aspect>
  ```

- 说明：一个aop:aspect标签中可以配置多个aop:after-throwing标签

- 基本属性：

  - method ：在通知类中设置当前通知类别对应的方法
  - pointcut ：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突
  - pointcut-ref ：设置当前通知对应的切入点id，与pointcut属性冲突

### 2.7.5 aop:around标签

- 名称：aop:around

- 类型：**标签**

- 归属：aop:aspect标签

- 作用：设置环绕通知

- 格式：

  ```xml
  <aop:aspect ref="adviceId">
      <aop:around method="methodName" pointcut="……"/>
  </aop:aspect>
  ```

- 说明：一个aop:aspect标签中可以配置多个aop:around标签

- 基本属性：

  - method ：在通知类中设置当前通知类别对应的方法
  - pointcut ：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突
  - pointcut-ref ：设置当前通知对应的切入点id，与pointcut属性冲突

环绕通知的开发方式

- 环绕通知是在原始方法的前后添加功能，在环绕通知中，存在对原始方法的显式调用

  ```java
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
      Object ret = pjp.proceed();
      return ret;
  }
  ```

- 环绕通知方法相关说明：

  - 方法须设定Object类型的返回值，否则会拦截原始方法的返回。如果原始方法返回值类型为void，通知方也可以设定返回值类型为void，最终返回null
  - 方法需在第一个参数位置设定ProceedingJoinPoint对象，通过该对象调用proceed()方法，实现对原始方法的调用。如省略该参数，原始方法将无法执行
  - 使用proceed()方法调用原始方法时，因无法预知原始方法运行过程中是否会出现异常，强制抛出Throwable对象，封装原始方法中可能出现的异常信息

## 2.8 通知的类型

### 2.8.1 通知获取参数数据

第一种情况：

- 设定通知方法第一个参数为JoinPoint，通过该对象调用getArgs()方法，获取原始方法运行的参数数组

  ```java
  public void before(JoinPoint jp) throws Throwable {
      Object[] args = jp.getArgs();
  }
  ```

- 所有的通知均可以获取参数

### 2.8.2 通知获取返回值数据

第一种：返回值变量名

- 设定返回值变量名

- 原始方法

  ```java
  public int save() {
  	System.out.println("user service running...");
      return 100;
  }
  ```

- AOP配置

  ```xml
  <aop:aspect ref="myAdvice">
      <aop:pointcut id="pt3" expression="execution(* *(..))  "/>
      <aop:after-returning method="afterReturning" pointcut-ref="pt3" returning="ret"/>
  </aop:aspect>
  ```

- 通知类

  ```java
  public void afterReturning(Object ret) {
      System.out.println(ret);
  }
  ```

- 适用于返回后通知（after-returning）

第二种：

- 在通知类的方法中调用原始方法获取返回值

- 原始方法

  ```java
  public int save() {
      System.out.println("user service running...");
      return 100;
  }
  ```

- AOP配置l

  ```xml
  <aop:aspect ref="myAdvice">
      <aop:pointcut id="pt2" expression="execution(* *(..))  "/>
      <aop:around method="around" pointcut-ref="pt2" />
  </aop:aspect>
  ```

- 通知类

  ```java
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
      Object ret = pjp.proceed();
      return ret;
  }
  ```

- 适用于环绕通知（around）

### 2.8.3 通知获取异常数据

第一种：通知类的方法中调用原始方法捕获异常

- 在通知类的方法中调用原始方法捕获异常

- 原始方法

  ```java
  public void save() {
      System.out.println("user service running...");
      int i = 1/0;
  }
  ```

- AOP配置

  ```xml
  <aop:aspect ref="myAdvice">
      <aop:pointcut id="pt4" expression="execution(* *(..))  "/>
      <aop:around method="around" pointcut-ref="pt4" />
  </aop:aspect>
  ```

- 通知类

  ```java
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
      Object ret = pjp.proceed();	//对此处调用进行try……catch……捕获异常，或抛出异常
      return ret;
  }
  ```

- 适用于环绕通知（around）

第二种：

- 设定异常对象变量名

- 原始方法

  ```java
  public void save() {
      System.out.println("user service running...");
      int i = 1/0;
  }
  ```

- AOP配置

  ```xml
  <aop:aspect ref="myAdvice">
  	<aop:pointcut id="pt4" expression="execution(* *(..))  "/>
      <aop:after-throwing method="afterThrowing" pointcut-ref="pt4" throwing="t"/>
  </aop:aspect>
  ```

- 通知类

  ```java
  public void afterThrowing(Throwable t){
      System.out.println(t.getMessage());
  }
  ```

- 适用于返回后通知（after-throwing）

# 3. AOP配置—基于注解

## 3.1 简单案例

1. 创建目标接口和目标类（内部有切点）

2. 创建切面类（内部有增强方法）
3. 将目标类和切面类的对象创建权交给 spring

4. 在切面类中使用注解配置织入关系

5. 在配置文件中开启组件扫描和 AOP 的自动代理

6. 测试代码



1. 创建目标接口和目标类（内部有切点）

```java
public interface TargetInterface {
    public void method();
}

public class Target implements TargetInterface {
    @Override
    public void method() {
        System.out.println("Target running....");
    }
}
```

2. 创建切面类（内部有增强方法)

```java
public class MyAspect {
    //前置增强方法
    public void before(){
        System.out.println("前置代码增强.....");
    }
}
```

3. 将目标类和切面类的对象创建权交给 spring

```java
@Component("target")
public class Target implements TargetInterface {
    @Override
    public void method() {
        System.out.println("Target running....");
    }
}
@Component("myAspect")
public class MyAspect {
    public void before(){
        System.out.println("前置代码增强.....");
    }
}
```

4. 在切面类中使用注解配置织入关系

```java
@Component("myAspect")
@Aspect
public class MyAspect {
    @Before("execution(* com.itheima.aop.*.*(..))")
    public void before(){
        System.out.println("前置代码增强.....");
    }
}
```

5. 在配置文件中开启组件扫描和 AOP 的自动代理

```xml
<!--组件扫描-->
<context:component-scan base-package="com.itheima.aop"/>

<!--aop的自动代理-->
<aop:aspectj-autoproxy></aop:aspectj-autoproxy>

```

6. 测试代码

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AopTest {
    @Autowired
    private TargetInterface target;
    @Test
    public void test1(){
        target.method();
    }
}
```

## 3.2 注解的类型

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.20/pics/2.png)

**注意：后置通知应改为返回后通知**

### 3.2.1 @Aspect

- 名称：@Aspect

- 类型：**注解**

- 位置：类定义上方

- 作用：设置当前类为切面类

- 格式：

  ```java
  @Aspect
  public class AopAdvice {
  }
  ```

- 说明：一个beans标签中可以配置多个aop:config标签

### 3.2.2 @Pointcut

- 名称：@Pointcut

- 类型：**注解**

- 位置：方法定义上方

- 作用：使用当前方法名作为切入点引用名称

- 格式：

  ```java
  @Pointcut("execution(* *(..))")
  public void pt() {
  }
  ```

- 说明：被修饰的方法忽略其业务功能，格式设定为无参无返回值的方法，方法体内空实现（非抽象）

### 3.2.3 @Before

- 名称：@Before

- 类型：**注解**

- 位置：方法定义上方

- 作用：标注当前方法作为前置通知

- 格式：

  ```java
  @Before("pt()")
  public void before(){
  }
  ```

- 特殊参数：

  - 无

### 3.2.4 @After

- 名称：@After

- 类型：**注解**

- 位置：方法定义上方

- 作用：标注当前方法作为后置通知

- 格式：

  ```java
  @After("pt()")
  public void after(){
  }
  ```

- 特殊参数：

  - 无

### 3.2.5 @AfterReturning

- 名称：@AfterReturning

- 类型：**注解**

- 位置：方法定义上方

- 作用：标注当前方法作为返回后通知

- 格式：

  ```java
  @AfterReturning(value="pt()",returning = "ret")
  public void afterReturning(Object ret) {
  }
  ```

- 特殊参数：

  - returning ：设定使用通知方法参数接收返回值的变量名

### 3.2.6 @AfterThrowing

- 名称：@AfterThrowing

- 类型：**注解**

- 位置：方法定义上方

- 作用：标注当前方法作为异常后通知

- 格式：

  ```java
  @AfterThrowing(value="pt()",throwing = "t")
  public void afterThrowing(Throwable t){
  }
  ```

- 特殊参数：

  - throwing ：设定使用通知方法参数接收原始方法中抛出的异常对象名

### 3.2.7 @Around

- 名称：@Around

- 类型：**注解**

- 位置：方法定义上方

- 作用：标注当前方法作为环绕通知

- 格式：

  ```java
  @Around("pt()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
      Object ret = pjp.proceed();
      return ret;
  }
  ```

- 特殊参数：

  - 无

## 3.3 AOP注解驱动

- 名称：@EnableAspectJAutoProxy

- 类型：**注解**

- 位置：Spring注解配置类定义上方

- 作用：设置当前类开启AOP注解驱动的支持，加载AOP注解

- 格式：

  ```java
  @Configuration
  @ComponentScan("com.itheima")
  @EnableAspectJAutoProxy
  public class SpringConfig {
  }
  ```

# 4. AOP底层原理

## 4.1 静态代理

接口

```java
public interface Person {
        void method();
}
```

被代理对象

```java
public class Teacher implements Person {
    void method() {
        System.out.println("I am a teacher");
    }
}
```

代理对象

```java
public class MathTeacher implements Person {

    private Person person;

    public MathTeacher(Person person) {
        this.person = person;
    }

    @Override
    void method() {
        person.method();
        System.out.println("I am a math teacher");
    }
}
```

## 4.2 动态代理—JDK

JDKProxy动态代理是针对对象做代理，要求原始对象具有接口实现，并对接口方法进行增强

```java
public class UserServiceJDKProxy {
    public UserService createUserServiceJDKProxy(final UserService userService){
        //获取被代理对象的类加载器
        ClassLoader classLoader = userService.getClass().getClassLoader();
        //获取被代理对象实现的接口
        Class[] classes = userService.getClass().getInterfaces();
        //对原始方法执行进行拦截并增强
        InvocationHandler ih = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //前置增强内容
                Object ret = method.invoke(userService, args);
                //后置增强内容
                System.out.println("刮大白2");
                return ret;
            }
        };
        //使用原始被代理对象创建新的代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(classLoader,classes,ih);
        return proxy;
    }
}
```

## 4.3 动态代理—CGLIB

- CGLIB(Code Generation Library)，Code生成类库
- CGLIB动态代理不限定是否具有接口，可以对任意操作进行增强
- CGLIB动态代理无需要原始被代理对象，动态创建出新的代理对象

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.20/pics/3.png)



```java
public class UserServiceImplCglibProxy {
    public static UserServiceImpl createUserServiceCglibProxy(Class clazz){
        //创建Enhancer对象（可以理解为内存中动态创建了一个类的字节码）
        Enhancer enhancer = new Enhancer();
        //设置Enhancer对象的父类是指定类型UserServerImpl
        enhancer.setSuperclass(clazz);
        Callback cb = new MethodInterceptor() {
            public Object intercept(Object o, Method m, Object[] a, MethodProxy mp) throws Throwable {
                Object ret = mp.invokeSuper(o, a);
                if(m.getName().equals("save")) {
                    System.out.println("刮大白");
                }
                return ret;
            }
        };
        //设置回调方法
        enhancer.setCallback(cb);
        //使用Enhancer对象创建对应的对象
        return (UserServiceImpl)enhancer.create();
    }
}
```

## 4.4 织入时机

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.20/pics/4.png)