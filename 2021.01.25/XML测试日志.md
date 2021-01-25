# 1. XML

## 1.1 概述

- xml概述

  XML的全称为(EXtensible Markup Language)，是一种可扩展的标记语言
  标记语言: 通过标签来描述数据的一门语言(标签有时我们也将其称之为元素)
  可扩展：标签的名字是可以自定义的,XML文件是由很多标签组成的,而标签名是可以自定义的

- 作用

  - 用于进行存储数据和传输数据
  - 作为软件的配置文件

- 作为配置文件的优势

  - 可读性好
  - 可维护性高

## 1.2 标签的规则【应用】

- 标签由一对尖括号和合法标识符组成

  ```java
  <student>
  ```

- 标签必须成对出现

  ```java
  <student> </student>
  前边的是开始标签，后边的是结束标签
  ```

- 特殊的标签可以不成对,但是必须有结束标记

  ```java
  <address/>
  ```

- 标签中可以定义属性,属性和标签名空格隔开,属性值必须用引号引起来

  ```java
  <student id="1"> </student>
  ```

- 标签需要正确的嵌套

## 1.3 语法规则【应用】

- 语法规则

  - XML文件的后缀名为：xml

  - 文档声明必须是第一行第一列

    <?xml version="1.0" encoding="UTF-8" standalone="yes”?>
    version：该属性是必须存在的
    encoding：该属性不是必须的

    ​	打开当前xml文件的时候应该是使用什么字符编码表(一般取值都是UTF-8)

    standalone: 该属性不是必须的，描述XML文件是否依赖其他的xml文件，取值为yes/no

  - 必须存在一个根标签，有且只能有一个

  - XML文件中可以定义注释信息

  - XML文件中可以存在以下特殊字符

    ```java
    &lt; < 小于
    &gt; > 大于
    &amp; & 和号
    &apos; ' 单引号
    &quot; " 引号
    ```

  - XML文件中可以存在CDATA区

    <![CDATA[ …内容… ]]>

- 示例代码

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!--注释的内容-->
  <!--本xml文件用来描述多个学生信息-->
  <students>
  
      <!--第一个学生信息-->
      <student id="1">
          <name>张三</name>
          <age>23</age>
          <info>学生&lt; &gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;的信息</info>
          <message> <![CDATA[内容 <<<<<< >>>>>> ]]]></message>
      </student>
  
      <!--第二个学生信息-->
      <student id="2">
          <name>李四</name>
          <age>24</age>
      </student>
  
  </students>
  ```

## 1.4 xml解析【应用】

- 概述

  xml解析就是从xml中获取到数据

- 常见的解析思想

  DOM(Document Object Model)文档对象模型:就是把文档的各个组成部分看做成对应的对象。
  会把xml文件全部加载到内存,在内存中形成一个树形结构,再获取对应的值

  ![02_dom解析概述](F:/%E9%BB%91%E9%A9%ACJava%EF%BC%88V10%E7%89%88%EF%BC%89%E5%90%AB%E8%BF%9B%E4%BF%AE/01_%E9%98%B6%E6%AE%B5%E4%B8%80%20Java%E5%9F%BA%E7%A1%80%EF%BC%88V10%E7%89%88%E6%9C%AC%EF%BC%89/00_%E9%98%B6%E6%AE%B5%E4%B8%80-Java%E5%9F%BA%E7%A1%80%E8%AF%BE%E4%BB%B6/12.%E5%9F%BA%E7%A1%80%E5%8A%A0%E5%BC%BA/day26-%E5%9F%BA%E7%A1%80%E5%8A%A0%E5%BC%BA02/%E7%AC%94%E8%AE%B0/img/02_dom%E8%A7%A3%E6%9E%90%E6%A6%82%E8%BF%B0.png)

- 常见的解析工具

  - JAXP: SUN公司提供的一套XML的解析的API
  - JDOM: 开源组织提供了一套XML的解析的API-jdom
  - DOM4J: 开源组织提供了一套XML的解析的API-dom4j,全称：Dom For Java
  - pull: 主要应用在Android手机端解析XML

- 解析的准备工作

  1. 我们可以通过网站：https://dom4j.github.io/ 去下载dom4j

     今天的资料中已经提供,我们不用再单独下载了,直接使用即可

  2. 将提供好的dom4j-1.6.1.zip解压,找到里面的dom4j-1.6.1.jar

  3. 在idea中当前模块下新建一个libs文件夹,将jar包复制到文件夹中

  4. 选中jar包 -> 右键 -> 选择add as library即可

- 需求

  - 解析提供好的xml文件
  - 将解析到的数据封装到学生对象中
  - 并将学生对象存储到ArrayList集合中
  - 遍历集合

- 代码实现

  ```java
  <?xml version="1.0" encoding="UTF-8" ?>
  <!--注释的内容-->
  <!--本xml文件用来描述多个学生信息-->
  <students>
  
      <!--第一个学生信息-->
      <student id="1">
          <name>张三</name>
          <age>23</age>
      </student>
  
      <!--第二个学生信息-->
      <student id="2">
          <name>李四</name>
          <age>24</age>
      </student>
  
  </students>
  
  // 上边是已经准备好的student.xml文件
  public class Student {
      private String id;
      private String name;
      private int age;
  
      public Student() {
      }
  
      public Student(String id, String name, int age) {
          this.id = id;
          this.name = name;
          this.age = age;
      }
  
      public String getId() {
          return id;
      }
  
      public void setId(String id) {
          this.id = id;
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public int getAge() {
          return age;
      }
  
      public void setAge(int age) {
          this.age = age;
      }
  
      @Override
      public String toString() {
          return "Student{" +
                  "id='" + id + '\'' +
                  ", name='" + name + '\'' +
                  ", age=" + age +
                  '}';
      }
  }
  
  /**
   * 利用dom4j解析xml文件
   */
  public class XmlParse {
      public static void main(String[] args) throws DocumentException {
          //1.获取一个解析器对象
          SAXReader saxReader = new SAXReader();
          //2.利用解析器把xml文件加载到内存中,并返回一个文档对象
          Document document = saxReader.read(new File("myxml\\xml\\student.xml"));
          //3.获取到根标签
          Element rootElement = document.getRootElement();
          //4.通过根标签来获取student标签
          //elements():可以获取调用者所有的子标签.会把这些子标签放到一个集合中返回.
          //elements("标签名"):可以获取调用者所有的指定的子标签,会把这些子标签放到一个集合中并返回
          //List list = rootElement.elements();
          List<Element> studentElements = rootElement.elements("student");
          //System.out.println(list.size());
  
          //用来装学生对象
          ArrayList<Student> list = new ArrayList<>();
  
          //5.遍历集合,得到每一个student标签
          for (Element element : studentElements) {
              //element依次表示每一个student标签
    
              //获取id这个属性
              Attribute attribute = element.attribute("id");
              //获取id的属性值
              String id = attribute.getValue();
  
              //获取name标签
              //element("标签名"):获取调用者指定的子标签
              Element nameElement = element.element("name");
              //获取这个标签的标签体内容
              String name = nameElement.getText();
  
              //获取age标签
              Element ageElement = element.element("age");
              //获取age标签的标签体内容
              String age = ageElement.getText();
  
  //            System.out.println(id);
  //            System.out.println(name);
  //            System.out.println(age);
  
              Student s = new Student(id,name,Integer.parseInt(age));
              list.add(s);
          }
          //遍历操作
          for (Student student : list) {
              System.out.println(student);
          }
      }
  }
  ```

# 2. 单元测试

## 2.1 概述【理解】

JUnit是一个 Java 编程语言的单元测试工具。JUnit 是一个非常重要的测试工具

## 2.2 特点【理解】

- JUnit是一个开放源代码的测试工具。
- 提供注解来识别测试方法。
- JUnit测试可以让你编写代码更快，并能提高质量。
- JUnit优雅简洁。没那么复杂，花费时间较少。
- JUnit在一个条中显示进度。如果<font color='green'>运行良好则是绿色</font>；如果<font color='red'>运行失败则变成红色</font>。

## 2.3 使用步骤【应用】

- 使用步骤

  1. 将junit的jar包导入到工程中 junit-4.9.jar
  2. 编写测试方法该测试方法必须是<font color='red'>公共的无参数无返回值的非静态方法</font>
  3. 在测试方法上使用<font color='red'>@Test</font>注解标注该方法是一个测试方法
  4. 选中测试方法右键通过junit运行该方法

- 代码示例

  ```java
  public class JunitDemo1 {
      @Test
      public void add() {
          System.out.println(2 / 0);
          int a = 10;
          int b = 20;
          int sum = a + b;
          System.out.println(sum);
      }
  }
  ```

## 2.4 相关注解【应用】

- 注解说明

  | 注解    | 含义               |
  | ------- | ------------------ |
  | @Test   | 表示测试该方法     |
  | @Before | 在测试的方法前运行 |
  | @After  | 在测试的方法后运行 |

- 代码示例

  ```java
  public class JunitDemo2 {
      @Before
      public void before() {
        	// 在执行测试代码之前执行，一般用于初始化操作
          System.out.println("before");
      }
      @Test
      public void test() {
        	// 要执行的测试代码
          System.out.println("test");
      }
      @After
      public void after() {
        	// 在执行测试代码之后执行，一般用于释放资源
          System.out.println("after");
      }
  }
  ```

# 3. 日志

## 3.1 概述【理解】

- 概述

  程序中的日志可以用来记录程序在运行的时候点点滴滴。并可以进行永久存储。

- 日志与输出语句的区别

  |          | 输出语句                                         | 日志技术                                 |
  | -------- | ------------------------------------------------ | ---------------------------------------- |
  | 取消日志 | 需要修改代码，灵活性比较差                       | 不需要修改代码，灵活性比较好             |
  | 输出位置 | 只能是控制台，不能记录到其他位置（文件，数据库） | 可以将日志信息写入到文件或者数据库中     |
  | 多线程   | 和业务代码处于一个线程中                         | 多线程方式记录日志，不影响业务代码的性能 |

## 3.2 日志体系结构和Log4J【理解】

- 体系结构

  ![06_日志体系结构](F:/%E9%BB%91%E9%A9%ACJava%EF%BC%88V10%E7%89%88%EF%BC%89%E5%90%AB%E8%BF%9B%E4%BF%AE/01_%E9%98%B6%E6%AE%B5%E4%B8%80%20Java%E5%9F%BA%E7%A1%80%EF%BC%88V10%E7%89%88%E6%9C%AC%EF%BC%89/00_%E9%98%B6%E6%AE%B5%E4%B8%80-Java%E5%9F%BA%E7%A1%80%E8%AF%BE%E4%BB%B6/12.%E5%9F%BA%E7%A1%80%E5%8A%A0%E5%BC%BA/day27_%E5%9F%BA%E7%A1%80%E5%8A%A0%E5%BC%BA03/%E7%AC%94%E8%AE%B0/img/06_%E6%97%A5%E5%BF%97%E4%BD%93%E7%B3%BB%E7%BB%93%E6%9E%84.png)

- Log4J

  Log4j是Apache的一个开源项目。

  通过使用Log4j，我们可以控制日志信息输送的<font color='red'>目的地是控制台、文件等位置</font>。

  我们也可以控制每一条日志的<font color='red'>输出格式</font>。

  通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。

  最令人感兴趣的就是，这些可以通过<font color='red'>一个配置文件来灵活地进行配置，而不需要修改应用的代码</font>。

- Apache基金会

  Apache软件基金会（也就是Apache Software Foundation，简称为ASF），为支持开源软件项目而办的一个非盈利性组织。

## 3.3 入门案例【应用】

- 使用步骤
  1. 导入log4j的相关jar包
  2. 编写log4j配置文件
  3. 在代码中获取日志的对象
  4. 按照级别设置记录日志信息

## 3.4 配置文件详解【理解】

- 三个核心

  - Loggers(记录器)        <font color='red'>日志的级别</font>

    Loggers组件在此系统中常见的五个级别：DEBUG、INFO、WARN、ERROR 和 FATAL。

    DEBUG < INFO < WARN < ERROR < FATAL。

    Log4j有一个规则：只输出级别不低于设定级别的日志信息。

  - Appenders(输出源)   <font color='red'>日志要输出的地方</font>

    把日志输出到不同的地方，如控制台（Console）、文件（Files）等。

    - org.apache.log4j.ConsoleAppender（控制台）
    - org.apache.log4j.FileAppender（文件）

  - Layouts(布局)             <font color='red'>日志输出的格式</font>

    可以根据自己的喜好规定日志输出的格式

    常用的布局管理器：

    ​		org.apache.log4j.PatternLayout（可以灵活地指定布局模式）

    ​          	org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串）

     		org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等信息）

+ 配置根Logger

  - 格式

    log4j.rootLogger=日志级别，appenderName1，appenderName2，…

  - 日志级别

    OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL或者自定义的级别。

  - appenderName1

    就是指定日志信息要输出到哪里。可以同时指定多个输出目的地，用逗号隔开。

    例如：log4j.rootLogger＝INFO，ca，fa

+ ConsoleAppender常用的选项

  - ImmediateFlush=true

    表示所有消息都会被立即输出，设为false则不输出，默认值是true。

  - Target=System.err

    默认值是System.out。

+ FileAppender常用的选项

  - ImmediateFlush=true

    表示所有消息都会被立即输出。设为false则不输出，默认值是true

  - Append=false

    true表示将消息添加到指定文件中，原来的消息不覆盖。

    false则将消息覆盖指定的文件内容，默认值是true。

  - File=D:/logs/logging.log4j

    指定消息输出到logging.log4j文件中

- PatternLayout常用的选项

  - ConversionPattern=%m%n

    设定以怎样的格式显示消息

    ![07_PatternLayout常用的选项](F:/%E9%BB%91%E9%A9%ACJava%EF%BC%88V10%E7%89%88%EF%BC%89%E5%90%AB%E8%BF%9B%E4%BF%AE/01_%E9%98%B6%E6%AE%B5%E4%B8%80%20Java%E5%9F%BA%E7%A1%80%EF%BC%88V10%E7%89%88%E6%9C%AC%EF%BC%89/00_%E9%98%B6%E6%AE%B5%E4%B8%80-Java%E5%9F%BA%E7%A1%80%E8%AF%BE%E4%BB%B6/12.%E5%9F%BA%E7%A1%80%E5%8A%A0%E5%BC%BA/day27_%E5%9F%BA%E7%A1%80%E5%8A%A0%E5%BC%BA03/%E7%AC%94%E8%AE%B0/img/07_PatternLayout%E5%B8%B8%E7%94%A8%E7%9A%84%E9%80%89%E9%A1%B9.png)

## 3.5 在项目中的应用【应用】

- 步骤

  1. 导入相关的依赖
  2. 将资料中的properties配置文件复制到src目录下
  3. 在代码中获取日志的对象
  4. 按照级别设置记录日志信息

- 代码实现

  ```java
  @WebServlet(urlPatterns = "/servlet/loginservlet")
  public class LoginServlet implements HttpServlet{
  
      //获取日志的对象
      private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);
  
      @Override
      public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
         //处理
          System.out.println("LoginServlet处理了登录请求");
  
          LOGGER.info("现在已经处理了登录请求，准备给浏览器响应");
  
         //响应
          httpResponse.setContentTpye("text/html;charset=UTF-8");
          httpResponse.write("登录成功");
      }
  }
  ```