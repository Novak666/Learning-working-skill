# 1. RabbitMQ生产者可靠性投递

## 1.1 简介

在使用RabbitMQ的时候，作为消息发送方希望杜绝任何消息丢失或者投递失败场景。RabbitMQ为我们提供了两种方式用来控制消息的投递可靠性模式，MQ提供了如下两种模式：

+ confirm模式，生产者发送消息到交换机的时机
+ return模式，交换机转发消息给queue的时机

MQ投递消息的流程如下：

1. 生产者发送消息到交换机
2. 交换机根据routing key转发消息给队列
3. 消费者监控队列，获取队列中信息
4. 消费成功删除队列中的消息

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.26/pics/1.png)

+ 消息从product到exchange则会返回一个confirmCallback
+ 消息从exchange到queue投递失败则会返回一个returnCallback

## 1.2 confirm模式代码实现

和前一天其他代码类似

1. 创建工程
2. 添加起步依赖
3. 设置confirm回调函数

```java
@Component
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback {
    //回调函数

    /**
     * @param correlationData
     * @param ack             是否确认发送成功 如果是true 标识成功  false 表示失败
     * @param cause           如果是成功 cause 是null值 如果是失败 有失败的信息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            // 下单 成功 扣款成功 通知 另外一个系统 +钱
            System.out.println("成功======");
        } else {
            System.out.println("失败，失败的原因：" + cause);
        }
    }
}
```

4. 配置文件开启confirm

```yaml
spring:
  rabbitmq:
    virtual-host: /pay
    username: lisi
    password: lisi
    port: 5672
    publisher-confirms: true # 开启confirm 模式
```

5. controller发送消息

```java
@RequestMapping("/send1")
public String send1(){
    //发送消息 有可能丢失

    //可以使用confirm模式
    rabbitTemplate.setConfirmCallback(myConfirmCallback);

    rabbitTemplate.convertAndSend("exchange_direct_demo01","item.insert","数据");

    return "ok";
}
```

## 1.3 return模式代码实现

1. 设置return回调函数

```java
@Component
public class MyReturnCallBack  implements RabbitTemplate.ReturnCallback {
    /**
     * 一旦出现错误则调用该方法  人工去做
     * @param message  消息本身
     * @param replyCode 响应的状态码
     * @param replyText 错误的信息描述
     * @param exchange 交换机
     * @param routingKey 路由key
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("消息本身："+new String(message.getBody()));
        System.out.println("退回的replyCode是："+replyCode);
        System.out.println("退回的replyText是："+replyText);
        System.out.println("退回的exchange是："+exchange);
        System.out.println("退回的routingKey是："+routingKey);
    }
}
```

2. 配置文件开启confirm

```yaml
spring:
  rabbitmq:
    virtual-host: /pay
    username: lisi
    password: lisi
    port: 5672
    publisher-confirms: true # 开启confirm 模式
    publisher-returns: true # 开启return模式
```

3. controller发送消息

```java
@RequestMapping("/send1")
public String send1(){
    //发送消息 有可能丢失

    //可以使用confirm模式
    rabbitTemplate.setConfirmCallback(myConfirmCallback);

    //设置return模式
    rabbitTemplate.setReturnCallback(myReturnCallBack);

    rabbitTemplate.convertAndSend("exchange_direct_demo01","item.insert","数据");

    return "ok";
}
```

## 1.4 小结

confirm模式用于在消息发送到交换机时机使用，return模式用于在消息被交换机路由到队列中发送错误时使用

但是一般情况下我们使用confirm即可，因为路由key由开发人员指定，一般不会出现错误。如果要保证消息在交换机和routing key的时候那么需要结合两者的方式来进行设置

# 2. RabbitMQ消费者确认机制(ACK)

## 2.1 简介

消费方也有可能出现问题，比如没有接受消息，比如接受到消息之后，在代码执行过程中出现了异常，这种情况下我们需要额外的处理，那么就需要手动进行确认签收消息。RabbitMQ给我们提供了一个机制：ACK机制

ACK机制，有三种方式：

+ 自动确认，acknowledge="**none**"
+ 手动确认，acknowledge="**manual**"
+ 根据异常情况来确认(暂时不怎么用)，acknowledge="**auto**"

其中自动确认是指：
	当消息一旦被Consumer接收到，则自动确认收到，并将相应message从RabbitMQ的消息缓存中移除。但是在实际业务处理中，很可能消息接收到，业务处理出现异常，那么该消息就会丢失
其中手动确认方式是指：
	则需要在业务处理成功后，调用channel.basicAck()，手动签收，如果出现异常，则调用channel.basicNack()等方法，让其按照业务功能进行处理，比如：重新发送，比如拒绝签收进入死信队列等等

## 2.2 代码实现

1. 配置文件

```yaml
spring:
  rabbitmq:
    virtual-host: /pay
    username: lisi
    password: lisi
    port: 5672
    publisher-confirms: true # 开启confirm 模式
    publisher-returns: true # 开启return模式
    listener:
      simple:
        acknowledge-mode: manual  # 手动模式 需要消费端自己自定义ack
```

2. 监听类

```java
@Component
@RabbitListener(queues = "queue_demo01",concurrency = "10-100")//指定要监听的队列名称
public class MyRabbitListener {
    
    /**
     *
     * @param message 消息本身（消息的内容 和包括消息包含一些别的数据：比如：交换机 消息的序号）
     * @param channel 通道
     * @param msg  消息本身 只是 消息内容
     */
    @RabbitHandler//处理消息
    public void msg(Message message, Channel channel, String msg){

        //System.out.println("接收到的消息是："+msg);
        //模拟处理业务
        try {
            System.out.println("开始=================");
            Thread.sleep(1000);
            //int i=1/0;
            //System.out.println("结束=================");
            //成功 就应该要签收消息
            //参数1 指定消息的序号
            //参数2 指定是否批量的进行签收 true 表示批量处理签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        } catch (Exception e) {
            e.printStackTrace();
            //异常 就要拒绝签收

            try {
                //参数1 指定消息的序号
                //参数2 指定是否批量的进行签收 true 表示批量处理拒绝签收
                //参数3 指定 是否重回队列 true 表示消息重回队列  false 表示 丢弃消息
                //channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);

                //参数1 指定消息的序号
                //参数2 指定 是否重回队列 true 表示消息重回队列  false 表示 丢弃消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);


            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
```

第一种：签收
channel.basicAck()
第二种：拒绝签收 批量处理 
channel.basicNack() 
第三种：拒绝签收 不批量处理
channel.basicReject()

## 2.3 小结

- 设置acknowledge属性，设置ack方式none：自动确认，manual：手动确认
- 如果在消费端没有出现异常，则调用channel.basicAck(deliveryTag,false)，方法确认签收消息
- 如果出现异常，则在catch中调用basicNack或 basicReject，拒绝消息，让MQ重新发送消息

# 3. 消费端限流

如果并发量大的情况下，生产方不停的发送消息，可能处理不了那么多消息，此时消息在队列中堆积很多，当消费端启动，瞬间就会涌入很多消息，消费端有可能瞬间垮掉，这时我们可以在消费端进行限流操作，每秒钟放行多少个消息。这样就可以进行并发量的控制，减轻系统的负载，提供系统的可用性，这种效果往往可以在秒杀和抢购中进行使用。在RabbitMQ中也有限流的一些配置

配置文件

```yaml
prefetch: 1 # 设置每一个消费端 最多处理的未确认的消息的数量
```

默认是250个

# 4. TTL

TTL全称Time To Live(存活时间/过期时间)。当消息到达存活时间后，还没有被消费，会被自动清除

RabbitMQ设置过期时间有两种：

- 针对某一个队列设置过期时间，队列中的所有消息在过期时间到之后，如果没有被消费则被全部清除
- 针对某一个特定的消息设置过期时间；队列中的消息设置过期时间之后，如果这个消息没有被消息则被清除

1. 配置类

```java
@Configuration
public class TtlConfig {

    //创建过期队列 过期时间（队列的过期时间 10S）
    @Bean
    public Queue createqueuettl1(){
        //设置队列过期时间为10000 10S钟
        //设置参数 x-message-ttl 设置过期时间的参数名
        // 值 10000 单位是毫秒
        return QueueBuilder.durable("queue_demo02").withArgument("x-message-ttl",10000).build();
    }

    //创建交换机

    @Bean
    public DirectExchange createExchangettl(){
        return new DirectExchange("exchange_direct_demo02");
    }

    //创建绑定
    @Bean
    public Binding createBindingttl(){
        return BindingBuilder.bind(createqueuettl1()).to(createExchangettl()).with("item.ttl");
    }
}
```

2. controller发送消息

```java
@RequestMapping("/send2")
public String send2(){

    rabbitTemplate.convertAndSend("exchange_direct_demo02","item.ttl","测试ttl数据");

    return "ok";
}
```

# 5. 死信队列

## 5.1 简介

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.26/pics/2.png)

成为死信的三种条件：

1. 队列消息长度到达限制
2. 消费者拒接消费消息，basicNack/basicReject,并且不把消息重新放入原目标队列，requeue=false
3. 原队列存在消息过期设置，消息到达超时时间未被消费

死信队列：当消息成为Dead message后，可以被重新发送到另一个交换机，这个交换机就是Dead Letter Exchange(死信交换机简写：DLX)

## 5.2 死信的处理过程

DLX也是一个正常的Exchange，和一般的Exchange没有区别，它能在任何的队列上被指定，实际上就是设置某个队列的属性。

当这个队列中有死信时，RabbitMQ就会自动的将这个消息重新发布到设置的Exchange上去，进而被路由到另一个队列。

可以监听这个队列中的消息做相应的处理。

## 5.3 超过队列长度

```java
@Bean
public Queue createqueuetdelq2(){
    return QueueBuilder
        .durable("queue_demo03_deq")
        .withArgument("x-max-length",1)//设置队列的长度
        .withArgument("x-message-ttl",10000)//设置队列的消息过期时间 10S
        .withArgument("x-dead-letter-exchange","exchange_direct_demo03_dlx")//设置死信交换机名称
        .withArgument("x-dead-letter-routing-key","item.dlx")//设置死信路由key  item.dlx 就是routingkey
        .build();
}
```

## 5.4 消费者拒绝消费

```java
@Component
@RabbitListener(queues = "queue_demo03_deq")
public class DLxListner {

    @RabbitHandler
    public void jieshouMsg(Message message, Channel channel,String msg){
        System.out.println(msg);

        //模拟拒绝消息 不重回队列
        try {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
```

## 5.5 超过时间未消费

1. 死信配置类

```java
@Configuration
public class DlxConfig {


    //创建队列 1  这个接收转发过来的死信 queue1
    @Bean
    public Queue createqueuetdlq(){
        return QueueBuilder.durable("queue_demo03").build();
    }

    //创建队列 2  queue2 用来接收生产者发送过来的消息 然后要过期 变成死信 转发给了queue1
    @Bean
    public Queue createqueuetdelq2(){
        return QueueBuilder
            .durable("queue_demo03_deq")
            .withArgument("x-message-ttl",10000)//设置队列的消息过期时间 10S
            .withArgument("x-dead-letter-exchange","exchange_direct_demo03_dlx")//设置死信交换机名称
            .withArgument("x-dead-letter-routing-key","item.dlx")//设置死信路由key  item.dlx 就是routingkey
            .build();
    }

    //创建死信交换机

    @Bean
    public DirectExchange createExchangedel(){
        return new DirectExchange("exchange_direct_demo03_dlx");
    }

    // queue1 绑定给 死信交换机  routingkey 和 队列转发消息时指定的死信routingkey 要一致
    @Bean
    public Binding createBindingdel(){
        return BindingBuilder.bind(createqueuetdlq()).to(createExchangedel()).with("item.dlx");
    }
    
}
```

2. controller发送消息

```java
@RequestMapping("/send3")
public String send3(){

    rabbitTemplate.convertAndSend("queue_demo03_deq","dlx消息本身");

    return "ok";
}
```

# 6. 延迟队列

## 6.1 简介

延迟队列，即消息进入队列后不会立即被消费，只有到达指定时间后，才会被消费。在RabbitMQ中，并没有延迟队列概念，但是我们可以使用TTL和死信队列的方式进行达到延迟的效果。这种需求往往在某些应用场景中出现。当然还可以使用插件

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.26/pics/3.png)

1. 生产者产生一个消息发送到queue1
2. queue1中的消息过期则转发到queue2
3. 消费者在queue2中获取消息进行消费

## 6.2 业务场景

<font color='red'>订单下单30分钟内付款，否则回滚库存</font>

1. 下单，库存扣减，发送一个消息(延迟消息 存活30分钟)
2. 过了30分钟，消息过期变成死信，转发给另外一个队列
3. 另一个队列有消费者在监听，判断该用户是否已经完成支付，若没有支付，则回滚库存

## 6.3 代码实现

1. 延迟队列配置类

```java
@Configuration
public class DelayConfig {
    //正常的队列    接收死信队列转移过来的消息
    @Bean
    public Queue createQueue2(){
        return QueueBuilder.durable("queue_order_queue2").build();
    }

    //死信队列   --->将来消息发送到这里  这里不设置过期时间，我们应该在发送消息时设置某一个消息（某一个用户下单的）的过期时间
    @Bean
    public Queue createQueue1(){
        return QueueBuilder
                .durable("queue_order_queue1")
                .withArgument("x-dead-letter-exchange","exchange_order_delay")//设置死信交换机
                .withArgument("x-dead-letter-routing-key","item.order")//设置死信路由key
                .build();
    }

    //创建交换机
    @Bean
    public DirectExchange createOrderExchangeDelay(){
        return new DirectExchange("exchange_order_delay");
    }

    //创建绑定 将正常队列绑定到死信交换机上
    @Bean
    public Binding createBindingDelay(){
        return BindingBuilder.bind(createQueue2()).to(createOrderExchangeDelay()).with("item.order");
    }
}
```

2. 监听类

```java
/**
 * 监听queue2 逻辑： 先获取消息 获取到消息中的订单的ID 2.从数据库获取订单的状态 3.判断订单是否已经支付，如果没有支付，回滚库存
 */
@Component
@RabbitListener(queues = "queue_order_queue2")
public class OrderListener {

    @RabbitHandler
    public void orderhandler(Message message, Channel channel, String msg) {
        System.out.println("获取到消息:" + msg + ":时间为:" + new Date());
        try {
            System.out.println("模拟检查开始=====start");
            Thread.sleep(1000);
            System.out.println("模拟检查结束=====end");
            System.out.println("用户没付款，检查没通过，进入回滚库存处理");
            //签收消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            //拒绝。。。。。别的处理的方式
        }
    }
}
```

3. controller发送消息

```java
//模拟下单
@RequestMapping("/send5")
public String send5(){
    //模拟下单
    System.out.println("下单成功=================");
    //模拟减库存
    System.out.println("减库存成功================");

    System.out.println("下单的时间："+new Date());

    //生产者 发送消息
    rabbitTemplate.convertAndSend("queue_order_queue1", (Object) "延迟队列的消息：orderId的值:123456", new MessagePostProcessor() {
        //设置过期时间 设置消息的一些属性
        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            message.getMessageProperties().setExpiration("10000");//设置过期时间 单位是毫秒
            return message;
        }
    });

    return "ok";
}
```

## 6.4 注意点

针对某一个特定的消息设置过期时间时，一定是消息在队列中在队头的时候进行计算，如果某一个消息A设置过期时间5秒，消息B在队头，消息B没有设置过期时间，B此时过了已经5秒钟了还没被消费。注意，此时A消息并不会被删除，因为它并没有在队头

# 7. RabbitMQ幂等性问题

幂等性指一次和多次请求某一个资源，对于资源本身应该具有同样的结果。也就是说，其任意多次执行对资源本身所产生的影响均与一次执行的影响相同。在MQ中指，消费多条相同的消息，得到与消费该消息一次相同的结果

解决方案，以转账为例：

1. 发送消息
2. 消息内容包含了id的版本和金额
3. 消费者接收到消息，则根据id的版本执行sql语句update account set money=money-?, version=version+1 where id=? and version=?
4. 如果消费第二次，根据版本号判断，同一个消息内容是不会修改成功的