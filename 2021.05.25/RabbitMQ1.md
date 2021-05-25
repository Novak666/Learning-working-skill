# 1. 消息队列概述

## 1.1 存在的问题

1. 耗时用于体验差，服务器资源消耗大
2. 耦合度高，维护升级麻烦

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/1.png)

## 1.2 消息队列MQ

MQ全称为Message Queue，消息队列是应用程序和应用程序之间的通信方法

为什么使用MQ：

在项目中，可将一些无需即时返回且耗时的操作提取出来，进行异步处理，而这种异步处理的方式大大的节省了服务器的请求响应时间，从而提高了系统的吞吐量

能解决问题：

1. 耗时操作
2. 解耦合
3. 限流(削峰填谷)

应用场景：

1. 任务异步处理，将不需要同步处理的并且耗时长的操作由消息队列通知消息接收方进行异步处理。提高了应用程序的响应时间
2. 应用程序解耦合，MQ相当于一个中介，生产方通过MQ与消费方交互，它将应用程序进行解耦合

## 1.3 AMQP和JMS

MQ是消息通信的模型，实现MQ的大致有两种主流方式：AMQP、JMS

### 1.3.1 AMQP

AMQP高级消息队列协议，是一个进程间传递异步消息的网络协议，更准确的说是一种binary wire-level protocol(链接协议)。这是其和JMS的本质差别，AMQP不从API层进行限定，而是直接定义网络交换的数据格式

### 1.3.2 JMS

JMS即Java消息服务(Java Message Service)应用程序接口，是一个Java平台中关于面向消息中间件(MOM)的API，用于在两个应用程序之间，或分布式系统中发送消息，进行异步通信

### 1.3.3 AMQP与JMS区别

+ JMS是定义了统一的接口，来对消息操作进行统一；AMQP是通过规定协议来统一数据交互的格式

- JMS限定了必须使用Java语言；AMQP只是协议，不规定实现方式，因此是跨语言的。
- JMS规定了两种消息模式—订阅模式和点对点消息模式；而AMQP的消息模式更加丰富

## 1.4 消息队列产品

市场上常见的消息队列有如下：

目前市面上成熟主流的MQ有Kafka 、RocketMQ、RabbitMQ，我们这里对每款MQ做一个简单介绍

+ Kafka：Apache下的一个子项目，使用scala实现的一个高性能分布式Publish/Subscribe消息队列系统

1. 快速持久化：通过磁盘顺序读写与零拷贝机制，可以在O(1)的系统开销下进行消息持久化
2. 高吞吐：在一台普通的服务器上既可以达到10W/s的吞吐速率
3. 高堆积：支持topic下消费者较长时间离线，消息堆积量大
4. 完全的分布式系统：Broker、Producer、Consumer都原生自动支持分布式，依赖zookeeper自动实现复杂均衡
5. 支持Hadoop数据并行加载：对于像Hadoop的一样的日志数据和离线分析系统，但又要求实时处理的限制，这是一个可行的解决方案

+ RocketMQ：RocketMQ的前身是Metaq，当Metaq3.0发布时，产品名称改为RocketMQ。RocketMQ是一款分布式、队列模型的消息中间件，具有以下特点：

1. 能够保证严格的消息顺序
2. 提供丰富的消息拉取模式
3. 高效的订阅者水平扩展能力
4. 实时的消息订阅机制
5. 支持事务消息
6. 亿级消息堆积能力

+ RabbitMQ：使用Erlang编写的一个开源的消息队列，本身支持很多的协议：AMQP、XMPP、SMTP、STOMP，也正是如此，使的它变的非常重量级，更适合于企业级的开发。同时实现了Broker架构，核心思想是生产者不会将消息直接发送给队列，消息在发送给客户端时先在中心队列排队。对路由(Routing)，负载均衡(Load balance)、数据持久化都有很好的支持。多用于进行企业级的ESB整合

# 2. 安装和配置RabbitMQ

## 2.1 安装说明

1. 安装erlang环境，如下图：注意使用**管理员身份**打开

2. 使用**管理员身份**打开那个rabbitmq-server-3.7.4.exe文件

3. 配置环境变量D:\erl9.2到path

4. cmd到rabbitmq-server的sbin目录下打开cmd命令行工具，执行命令：

   ```shell
   rabbitmq-plugins.bat enable rabbitmq_management
   ```

   ![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/2.png)

5. 重启rabbitmq服务器，此电脑—右击点击管理界面—双击服务和应用程序—双击服务—点击重启按钮即可

## 2.2 用户和Virtual Hosts配置

几个概念：

+ 生产者producer，发送消息的一方
+ 消费者consumer，接收消息的一方
+ broker，用来存储消息的逻辑概念的组件
+ 虚拟主机VirtualHosts，使用来区分不同的业务的组件
+ 队列queue，真正存储消息的组件

### 2.2.1 用户角色

RabbitMQ在安装好后，可以访问http://localhost:15672，其自带了guest/guest的用户名和密码。如果需要创建自定义用户，那么也可以登录管理界面后，点击Add a user

**角色说明**：

1. 超级管理员(administrator)

可登陆管理控制台，可查看所有的信息，并且可以对用户，策略(policy)进行操作

2. 监控者(monitoring)

可登陆管理控制台，同时可以查看rabbitmq节点的相关信息(进程数，内存使用情况，磁盘使用情况等)

3. 策略制定者(policymaker)

可登陆管理控制台, 同时可以对policy进行管理。但无法查看节点的相关信息(上图红框标识的部分)

4. 普通管理者(management)

仅可登陆管理控制台，无法看到节点信息，也无法对策略进行管理

5. 其他

无法登陆管理控制台，通常就是普通的生产者和消费者。

### 2.2.2 Virtual Hosts配置

像mysql拥有数据库的概念并且可以指定用户对库和表等操作的权限，RabbitMQ也有类似的权限管理。在RabbitMQ中可以虚拟消息服务器Virtual Host，每个Virtual Hosts相当于一个相对独立的RabbitMQ服务器，每个Virtual Host之间是相互隔离的。exchange、queue、message不能互通，相当于mysql的db。Virtual Name一般以/开头

1. 创建Virtual Hosts

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/3.png)

2. 设置Virtual Hosts权限

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/4.png)

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/5.png)

参数说明：

user：用户名
configure ：一个正则表达式，用户对符合该正则表达式的所有资源拥有configure操作的权限
write：一个正则表达式，用户对符合该正则表达式的所有资源拥有write操作的权限
read：一个正则表达式，用户对符合该正则表达式的所有资源拥有read操作的权限

# 3. RabbitMQ简单案例

以下为简单模式

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/6.png)

## 3.1 创建工程

工程坐标如下：

```xml
<groupId>com.itheima</groupId>
<artifactId>rabbitmq-demo</artifactId>
<version>1.0-SNAPSHOT</version>
```

## 3.2 添加依赖

往rabbitmq-demo的pom.xml文件中添加如下依赖：

```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.6.0</version>
</dependency>
```

## 3.3 生产者

生产者的创建分为如下几个步骤：

```properties
//创建链接工厂对象
//设置RabbitMQ服务主机地址,默认localhost
//设置RabbitMQ服务端口,默认5672
//设置虚拟主机名字，默认/
//设置用户连接名，默认guest
//设置链接密码，默认guest
//创建链接
//创建频道
//声明队列
//创建消息
//消息发送
//关闭资源
```

按照上面的步骤，我们创建一个消息生产者，创建com.itheima.rabbitmq.simple.Producer类，代码如下：

```java
public class Producer {

    /***
     * 消息生产者
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建链接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //设置RabbitMQ服务主机地址,默认localhost
        connectionFactory.setHost("localhost");

        //设置RabbitMQ服务端口,默认5672
        connectionFactory.setPort(5672);

        //设置虚拟主机名字，默认/
        connectionFactory.setVirtualHost("/szitheima");

        //设置用户连接名，默认guest
        connectionFactory.setUsername("admin");

        //设置链接密码，默认guest
        connectionFactory.setPassword("admin");

        //创建链接
        Connection connection = connectionFactory.newConnection();

        //创建频道
        Channel channel = connection.createChannel();

        /**
         * 声明队列
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         * **/
        channel.queueDeclare("simple_queue",true,false,false,null);

        //创建消息
        String message = "hello!welcome to itheima!";

        /**
         * 消息发送
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
         * 参数2：路由key,简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        channel.basicPublish("","simple_queue",null,message.getBytes());

        //关闭资源
        channel.close();
        connection.close();
    }
}
```

在执行上述的消息发送之后，可以登录rabbitMQ的管理控制台，可以发现队列和其消息：

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/7.png)

如果想查看消息，可以点击队列名称->Get Messages，如下图：

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/8.png)

## 3.4 消费者

消费者创建可以按照如下步骤实现：

```properties
//创建链接工厂对象
//设置RabbitMQ服务主机地址,默认localhost
//设置RabbitMQ服务端口,默认5672
//设置虚拟主机名字，默认/
//设置用户连接名，默认guest
//设置链接密码，默认guest
//创建链接
//创建频道
//声明队列
//创建消费者，并设置消息处理
//消息监听
//关闭资源(不建议关闭，建议一直监听消息)
```

按照上面的步骤创建消息消费者com.itheima.rabbitmq.simple.Consumer代码如下：

```java
public class Consumer {

    public static void main(String[] args) throws Exception {
        //创建链接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置RabbitMQ服务主机地址,默认localhost
        connectionFactory.setHost("localhost");
        //设置RabbitMQ服务端口,默认5672
        connectionFactory.setPort(5672);
        //设置虚拟主机名字，默认/
        connectionFactory.setVirtualHost("/pay");
        //设置用户连接名，默认guest
        connectionFactory.setUsername("lisi");
        //设置链接密码，默认guest
        connectionFactory.setPassword("lisi");
        //创建链接
        Connection connection = connectionFactory.newConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("simple_queue1", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("simple_queue1",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

执行后，控制台输入如下：

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/9.png)

RabbitMQ控制台如下：

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/10.png)

# 4. 工作模式

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/11.png)

Work Queues与入门程序的简单模式相比，多了一个或一些消费端，多个消费端共同消费同一个队列中的消息

**应用场景**：对于任务过重或任务较多情况使用工作队列可以提高任务处理的速度

## 4.1 工具类抽取

无论是消费者，还是生产者，我们发现前面的几个步骤几乎一模一样，所以可以抽取一个工具类，创建com.itheima.rabbitmq.util.ConnectionUtil工具类对象，用于创建Connection，代码如下：

```java
public class ConnectionUtil {
    public static Connection getConnection() throws Exception {
        //创建链接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //设置RabbitMQ服务主机地址,默认localhost
        connectionFactory.setHost("localhost");

        //设置RabbitMQ服务端口,默认5672
        connectionFactory.setPort(5672);

        //设置虚拟主机名字，默认/
        connectionFactory.setVirtualHost("/pay");

        //设置用户连接名，默认guest
        connectionFactory.setUsername("lisi");

        //设置链接密码，默认guest
        connectionFactory.setPassword("lisi");

        //创建链接
        Connection connection = connectionFactory.newConnection();
        return connection;
    }
}
```

## 4.2 生产者

```java
public class Producer {
    public static void main(String[] args) throws Exception{
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列(创建队列)
        //参数1 指定队列的名称
        //参数2 指定是消息否持久化  一般设置为true 要保存到磁盘中
        //参数3 指定是否独占通道  不独占
        //参数4 指定是否自动删除 不要自动删除。
        //参数5 指定额外的参数 可以不指定
        channel.queueDeclare("simple_queue2", true, false, false, null);

        for (int i = 0; i < 20; i++) {
            //创建消息
            String msg = "hello i am from simple producer "+i;

            //消息发送
            //参数1 指定交换机 简单模式中使用默认的交换机 指定空字符串
            //参数2 指定routingkey  简单模式 只需要指定队列名称即可
            //参数3 指定携带的额外的参数 null
            //参数4 要发送的消息本身（字节数组）
            channel.basicPublish("","simple_queue2",null,msg.getBytes());
        }


        //关闭资源
        channel.close();
        connection.close();
    }
}
```

## 4.3 消费者

消费者1

```java
public class Consumer1 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("simple_queue2", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("=========consumer1=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("simple_queue2",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

消费者2

```java
public class Consumer2 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("simple_queue2", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("=========consumer2=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("simple_queue2",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

在一个队列中如果有多个消费者，那么消费者之间对于同一个消息的关系是**竞争**的关系

# 5. 发布/订阅模式—广播模式

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/12.png)

在订阅模型中，多了一个exchange角色，而且过程略有变化：

P：生产者，也就是要发送消息的程序，但是不再发送到队列中，而是发给X(交换机)
C：消费者，消息的接受者，会一直等待消息到来
Queue：消息队列，接收消息、缓存消息
Exchange：交换机，图中的X。一方面，接收生产者发送的消息。另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于Exchange的类型。Exchange有常见以下3种类型：

1. Fanout：广播，将消息交给所有绑定到交换机的队列
2. Direct：定向，把消息交给符合指定routing key 的队列
3. Topic：通配符，把消息交给符合routing pattern(路由模式)的队列

**Exchange（交换机）只负责转发消息，不具备存储消息的能力**，因此如果Exchange没有和任何队列绑定，或者没有符合路由规则的队列，那么消息会丢失

## 5.1 生产者

生产者需要注意如下3点：

1. 声明交换机
2. 声明队列
3. 队列需要绑定指定的交换机

```java
public class Producer {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();

        //创建交换机 广播类型（广播模式）
        channel.exchangeDeclare("exchange_fanout", BuiltinExchangeType.FANOUT);


        //创建声明队列(创建队列) 2个以上
        //参数1 指定队列的名称
        //参数2 指定是消息否持久化  一般设置为true 要保存到磁盘中
        //参数3 指定是否独占通道  不独占
        //参数4 指定是否自动删除 不要自动删除。
        //参数5 指定额外的参数 可以不指定
        channel.queueDeclare("fanout_queue1", true, false, false, null);
        channel.queueDeclare("fanout_queue2", true, false, false, null);

        //将队列绑定到指定的交换机上
        //要绑定的队列
        //要绑定到的交换机
        //设置routingkey 在广播模式的时候可以设置为 ""
        channel.queueBind("fanout_queue1","exchange_fanout","");
        channel.queueBind("fanout_queue2","exchange_fanout","");

        //创建消息
        String msg = "hello i am from fanout producer";

        //消息发送
        //参数1 指定交换机 广播模式需要指定交换机
        //参数2 指定routingkey  简单模式 只需要指定队列名称即可
        //参数3 指定携带的额外的参数 null
        //参数4 要发送的消息本身（字节数组）
        channel.basicPublish("exchange_fanout", "", null, msg.getBytes());


        //关闭资源
        channel.close();
        connection.close();
    }
}
```

## 5.2 消费者

和工作模式类似

```java
public class Consumer1 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("fanout_queue1", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("====广播模式=====consumer1=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("fanout_queue1",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

```java
public class Consumer2 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("fanout_queue2", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("====广播模式=====consumer2=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("fanout_queue2",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

## 5.3 小结

发布订阅模式与work队列模式的区别：

1. work队列模式不用定义交换机，而发布/订阅模式需要定义交换机
2. 发布/订阅模式的生产方是面向交换机发送消息，work队列模式的生产方是面向队列发送消息(底层使用默认交换机)
3. 发布/订阅模式需要设置队列和交换机的绑定，work队列模式不需要设置，实际上work队列模式会将队列绑定到默认的交换机

# 6. 路由模式

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/13.png)

P：生产者，向Exchange发送消息，发送消息时，会指定一个routing key。
X：Exchange(交换机)，接收生产者的消息，然后把消息递交给与routing key完全匹配的队列
C1：消费者，其所在队列指定了需要routing key为error的消息
C2：消费者，其所在队列指定了需要routing key为info、error、warning的消息

Exchange不再把消息交给每一个绑定的队列，而是根据消息的Routing Key进行判断，只有队列的Routing key与消息的Routing key完全一致，才会接收到消息

## 6.1 生产者

在编码上与Publish/Subscribe发布与订阅模式的区别是交换机的类型为：Direct，还有队列绑定交换机的时候需要指定routing key

```java
public class Producer {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();

        //创建交换机 广播类型（路由模式）
        channel.exchangeDeclare("exchange_direct", BuiltinExchangeType.DIRECT);


        //创建声明队列(创建队列) 2个以上
        //参数1 指定队列的名称
        //参数2 指定是消息否持久化  一般设置为true 要保存到磁盘中
        //参数3 指定是否独占通道  不独占
        //参数4 指定是否自动删除 不要自动删除。
        //参数5 指定额外的参数 可以不指定
        channel.queueDeclare("direct_queue1", true, false, false, null);
        channel.queueDeclare("direct_queue2", true, false, false, null);

        //将队列绑定到指定的交换机上
        //要绑定的队列
        //要绑定到的交换机
        //设置routingkey 路由模式中一定要指定一个routingkey 标识他的标记位
        channel.queueBind("direct_queue1","exchange_direct","order.insert");
        channel.queueBind("direct_queue2","exchange_direct","order.select");

        //创建消息
        String msg1 = "hello i am from direct producer 做insert";
        String msg2 = "hello i am from direct producer 做select";

        //消息发送
        //参数1 指定交换机 需要指定交换机
        //参数2 指定routingkey  指定routingkey 标识不同的业务操作类型等。
        //参数3 指定携带的额外的参数 null
        //参数4 要发送的消息本身（字节数组）
        channel.basicPublish("exchange_direct", "order.insert", null, msg1.getBytes());
        channel.basicPublish("exchange_direct", "order.select", null, msg2.getBytes());


        //关闭资源
        channel.close();
        connection.close();
    }
}
```

## 6.2 消费者

```java
public class Consumer1 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("direct_queue1", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("====路由模式=====consumer1=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("direct_queue1",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

```java
public class Consumer2 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("direct_queue2", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("====路由模式=====consumer2=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("direct_queue2",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

# 7. 主题模式(通配符模式)

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.25/pics/14.png)

Topic类型与Direct相比，都是可以根据Routing Key把消息路由到不同的队列。只不过Topic类型Exchange可以让队列在绑定Routing key的时候**使用通配符**

Routing key一般都是有一个或多个单词组成，多个单词之间以”.”分割，例如：item.insert

通配符规则：

#：匹配一个或多个词

*：匹配不多不少恰好1个词

举例：

item.#：能够匹配item.insert.abc或者item.insert

item.*：只能匹配item.insert

## 7.1 生产者

```java
public class Producer {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();

        //创建交换机  主题模式（通配符模式）
        channel.exchangeDeclare("exchange_topic", BuiltinExchangeType.TOPIC);


        //创建声明队列(创建队列) 2个以上
        //参数1 指定队列的名称
        //参数2 指定是消息否持久化  一般设置为true 要保存到磁盘中
        //参数3 指定是否独占通道  不独占
        //参数4 指定是否自动删除 不要自动删除。
        //参数5 指定额外的参数 可以不指定
        channel.queueDeclare("topic_queue1", true, false, false, null);
        channel.queueDeclare("topic_queue2", true, false, false, null);

        //将队列绑定到指定的交换机上
        //要绑定的队列
        //要绑定到的交换机
        //设置routingkey 通配符模式

        //队列1 只处理 订单相关
        channel.queueBind("topic_queue1","exchange_topic","order.*");

        //队列2 只处理商品相关
        channel.queueBind("topic_queue2","exchange_topic","item.*");

        //创建消息
        String msg1 = "hello i am from topic producer order.insert";
        String msg2 = "hello i am from topic producer order.select";
        String msg3 = "hello i am from topic producer item.insert";

        //消息发送
        //参数1 指定交换机 需要指定交换机
        //参数2 指定routingkey  指定routingkey 标识不同的业务操作类型等。
        //参数3 指定携带的额外的参数 null
        //参数4 要发送的消息本身（字节数组）
        channel.basicPublish("exchange_topic", "order.insert", null, msg1.getBytes());
        channel.basicPublish("exchange_topic", "order.select", null, msg2.getBytes());
        channel.basicPublish("exchange_topic", "item.insert", null, msg3.getBytes());


        //关闭资源
        channel.close();
        connection.close();
    }
}
```

## 7.2 消费者

```java
public class Consumer1 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("topic_queue1", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("====主题模式只处理订单相关=====consumer1=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("topic_queue1",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

```java
public class Consumer2 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("topic_queue2", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("====主题模式只处理商品相关=====consumer2=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("topic_queue2",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
```

## 7.3 小结

Topic主题模式可以实现Publish/Subscribe发布与订阅模式和Routing路由模式的功能，只是Topic在配置routing key的时候可以使用通配符，显得更加灵活

# 8. 模式总结

RabbitMQ工作模式：
**1、简单模式 HelloWorld**
一个生产者、一个消费者，不需要设置交换机(使用默认的交换机)

**2、工作队列模式 Work Queue**
一个生产者、多个消费者(竞争关系)，不需要设置交换机(使用默认的交换机)

**3、发布订阅模式 Publish/subscribe**
需要设置类型为fanout的交换机，并且交换机和队列进行绑定，当发送消息到交换机后，交换机会将消息发送到绑定的队列

**4、路由模式 Routing**
需要设置类型为direct的交换机，交换机和队列进行绑定，并且指定routing key，当发送消息到交换机后，交换机会根据routing key将消息发送到对应的队列

**5、通配符模式 Topic**
需要设置类型为topic的交换机，交换机和队列进行绑定，并且指定通配符方式的routing key，当发送消息到交换机后，交换机会根据routing key将消息发送到对应的队列

# 9. SpringBoot整合RabbitMQ

## 9.1 总体步骤

1. 添加parent起步依赖
2. 启动类
3. 发送消息
   + controller触发发送消息的动作
   + RabbitTemplate发送
   + 在启动类中创建队列 创建交换机 创建绑定
   + 配置yml
4. 监听消息
   + 在启动类中创建队列 创建交换机 创建绑定
   + 创建一个类，通过注解的方式来监听
   + 配置yml

## 9.2 生产者

### 9.2.1 创建工程

创建生产者工程springboot-rabbitmq-producer，工程坐标如下：

```xml
<groupId>com.itheima</groupId>
<artifactId>springboot-rabbitmq-producer</artifactId>
<version>1.0-SNAPSHOT</version>
```

### 9.2.2 添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!--父工程-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <groupId>com.itheima</groupId>
    <artifactId>springboot-rabbitmq-producer</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--依赖-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
</project>
```

### 9.2.3 启动类

创建启动类com.itheima.ProducerApplication，代码如下：

```java
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class,args);
    }
}
```

### 9.2.4 配置RabbitMQ

创建application.yml，内容如下：

```yml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    virtual-host: /szitheima
    username: admin
    password: admin
```

### 9.2.5 消息发送类

创建RabbitMQ队列与交换机绑定的配置类com.itheima.config.RabbitMQConfig，代码如下：

```java
@Configuration
public class RabbitMQConfig {

    /***
     * 声明交换机
     */
    @Bean(name = "itemTopicExchange")
    public Exchange topicExchange(){
        return ExchangeBuilder.topicExchange("item_topic_exchange").durable(true).build();
    }

    /***
     * 声明队列
     */
    @Bean(name = "itemQueue")
    public Queue itemQueue(){
        return QueueBuilder.durable("item_queue").build();
    }

    /***
     * 队列绑定到交换机上
     */
    @Bean
    public Binding itemQueueExchange(@Qualifier("itemQueue")Queue queue,
                                     @Qualifier("itemTopicExchange")Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();
    }
}
```

## 9.3 消费者

### 9.3.1 创建工程

创建消费者工程springboot-rabbitmq-consumer,工程坐标如下：

```xml
<groupId>com.itheima</groupId>
<artifactId>springboot-rabbitmq-consumer</artifactId>
<version>1.0-SNAPSHOT</version>
```

### 9.3.2 添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!--父工程-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <groupId>com.itheima</groupId>
    <artifactId>springboot-rabbitmq-consumer</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--依赖-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
    </dependencies>
</project>
```

### 9.3.3 启动类

创建启动类com.itheima.ConsumerApplication，代码如下：

```java
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
    }
}
```

### 9.3.4 配置RabbitMQ

创建application.yml，内容如下：

```yml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    virtual-host: /szitheima
    username: admin
    password: admin
```

### 9.3.5 消息监听处理

编写消息监听器com.itheima.listener.MessageListener，代码如下：

```java
@Component
public class MessageListener {

    /**
     * 监听某个队列的消息
     * @param message 接收到的消息
     */
    @RabbitListener(queues = "item_queue")
    public void myListener1(String message){
        System.out.println("消费者接收到的消息为：" + message);
    }
}
```

## 9.4 测试

在生产者工程springboot-rabbitmq-producer中创建测试类com.itheima.test.RabbitMQTest，发送消息：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {

    //用于发送MQ消息
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /***
     * 消息生产测试
     */
    @Test
    public void testCreateMessage(){
        rabbitTemplate.convertAndSend("item_topic_exchange", "item.insert", "商品新增，routing key 为item.insert");
        rabbitTemplate.convertAndSend("item_topic_exchange", "item.update", "商品修改，routing key 为item.update");
        rabbitTemplate.convertAndSend("item_topic_exchange", "item.delete", "商品删除，routing key 为item.delete");
    }
}
```

先运行上述测试程序(交换机和队列才能先被声明和绑定)，然后启动消费者，在消费者工程springboot-rabbitmq-consumer中控制台查看是否接收到对应消息。另外，也可以在RabbitMQ的管理控制台中查看到交换机与队列的绑定