# 学习目标

- 登录页的配置
- 登录成功跳转实现
- 结算页查询实现
- 下单实现
- 变更库存
- 增加积分
- 支付流程介绍
- 微信扫码支付介绍

# 1. 登录页面配置

前面使用的都是采用Postman实现登录，接着我们实现一次oauth自定义登录。

## 1.1 准备工作

(1)静态资源导入

将`资料/页面/前端`登录相关的静态资源导入到changgou-user-oauth中,如下图。

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/1.png)

并替换掉./ 为/ 如图 按CTR+R 并在第一个输入框输入 ./ 再第二个输入框输入/  并点击替换所有。

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/2.png)

(2)引入thymeleaf

修改changgou-user-oauth，引入thymeleaf模板引擎起步依赖

```xml
<!--thymeleaf-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

(3)登录配置

修改changgou-user-oauth,编写一个控制器`com.changgou.oauth.controller.LoginRedirect`，实现登录页跳转，代码如下：

```java
@Controller
@RequestMapping(value = "/oauth")
public class LoginRedirect {

    /***
     * 跳转到登录页面
     * @return
     */
    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }
}
```

(4)登录页配置

针对静态资源和登录页面，我们需要实现忽略安全配置，并且要指定登录页面，修改`com.changgou.oauth.config.MyWebSecurityConfig`的`configure`方法，代码如下：

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/3.png)

测试:浏览器输入`http://localhost:9001/oauth/login`

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/4.png)

## 1.2 登录实现

点击登录按钮，访问之前的登录方法实现登录，我们需要对登录页做一下调整。

(1)引入thymeleaf命名空间

修改login.html，引入命名空间

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/5.png)

(2)登录脚本

点击登录按钮，使用vue+axios实现登录，我们需要定义脚本访问后台登录方法。

先添加vue入口标签：修改login.html，在73行左右的标签上添加id="app",代码如下：

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/6.png)

引入js

```html
<script src="/js/vue.js"></script>
<script src="/js/axios.js"></script>
```

登录脚本实现：

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/7.png)

(3)表单修改

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/8.png)

(4)配置config1:

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/9.png)

修改config2

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/10.png)

（5）测试

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/11.png)

## 1.3 登录跳转

用户没有登录的时候，我们直接访问购物车，效果如下：

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/12.png)

我们可以发现，返回的只是个错误状态码，不方便测试，我们可以重定向到登录页面，让用户登录，我们可以修改网关的头文件，让用户每次没登录的时候，都跳转到登录页面。

修改changgou-gateway-web的`com.changgou.filter.AuthorizeFilter`，代码如下：

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/13.png)

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/14.png)

代码如下：

```java
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_TOKEN = "Authorization";
    private static final String LOGIN_URL="http://localhost:9001/oauth/login";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //需要校验令牌的信息是否合法

        //1.获取请求对象 request
        ServerHttpRequest request = exchange.getRequest();

        //2.获取响应对象 response
        ServerHttpResponse response = exchange.getResponse();

        //3.判断请求的路径是否是登录请求，如果是登录请求，放行请求
        String path = request.getURI().getPath();//路径
        if (path.startsWith("/api/user/login")) {
            //放行
            return chain.filter(exchange);
        }
        //4.判断请求不是登录请求，就要校验，

        //4.1.先从header信息中获取令牌信息 如果没有 头名为AUTHORIZE_TOKEN的值
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        if (StringUtils.isEmpty(token)) {
            //4.2 再去从请求参数中获取令牌新 如果没有
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        if(StringUtils.isEmpty(token)) {
            //4.3 再从cookie中获取令牌信息 如果也没有 说明无权限 直接返回
            HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(httpCookie!=null){
                token = httpCookie.getValue();//
            }
        }
        if(StringUtils.isEmpty(token)){
            //没有令牌  说明无权限 直接返回
            response.getHeaders().set("Location",LOGIN_URL+"?FROM="+request.getURI().toString());
            response.setStatusCode(HttpStatus.SEE_OTHER);
            return response.setComplete();//请求完成
        }

        //5 如果有令牌信息 就要校验令牌的合法性，校验通过放行 不通过就直接返回

        /*try {
            JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //没有令牌  说明无权限 直接返回
            System.out.println("解析失败");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();//请求完成
        }*/

        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

```

此时再测试，就可以跳转到登录页面了。当然，在工作中，这里不能直接跳转到登录页，应该提示状态给页面，让页面根据判断跳转，这里只是为了方便测试,如下图所示：

![15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/15.png)

## 1.4 成功登录跳转到原访问页

![16](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/16.png)

上面虽然实现了登录跳转，但登录成功后却并没有返回到要访问的购物车页面，我们可以将用户要访问的页面作为参数传递给登录控制器，登录控制器记录下来，每次登录成功后，再跳转记录访问路劲参数指定的页面即可。

(1)认证服务器获取FROM参数

修改changgou-user-oauth的`com.changgou.oauth.controller.LoginRedirect`记录访问来源页，代码如下：

![17](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/17.png)

修改页面，获取来源页信息，并存到from变量中，登录成功后跳转到该地址。

![18](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/18.png)

此时再测试，就可以识别未登录用户，跳转到登录页，然后根据登录状态，如果登录成功，则跳转到来源页。

# 2. 订单结页

## 2.1 收件地址分析

用户从购物车页面点击结算，跳转到订单结算页，结算页需要加载用户对应的收件地址，如下图：

![19](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/19.png)

表结构分析：

```sql
CREATE TABLE `tb_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `provinceid` varchar(20) DEFAULT NULL COMMENT '省',
  `cityid` varchar(20) DEFAULT NULL COMMENT '市',
  `areaid` varchar(20) DEFAULT NULL COMMENT '县/区',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `is_default` varchar(1) DEFAULT NULL COMMENT '是否是默认 1默认 0否',
  `alias` varchar(50) DEFAULT NULL COMMENT '别名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;
```

我们可以根据用户登录名去tb_address表中查询对应的数据。

## 2.2 实现用户收件地址查询

### 2.2.1 代码实现

(1)业务层

业务层接口

修改changgou-service-user微服务，需改com.changgou.user.service.AddressService接口，添加根据用户名字查询用户收件地址信息，代码如下：

```java
/***
 * 收件地址查询
 * @param username
 * @return
 */
List<Address> list(String username);
```

业务层接口实现类

修改changgou-service-user微服务，修改com.changgou.user.service.impl.AddressServiceImpl类，添加根据用户查询用户收件地址信息实现方法，如下代码：

```java
/***
 * 收件地址查询
 * @param username
 * @return
 */
@Override
public List<Address> list(String username) {
    Address address = new Address();
    address.setUsername(username);
    return addressMapper.select(address);
}
```

(2)控制层

修改changgou-service-user微服务，修改com.changgou.user.controller.AddressController，添加根据用户名查询用户收件信息方法，代码如下：

```java
/****
 * 用户收件地址
 */
@GetMapping(value = "/user/list")
public Result<List<Address>> list(){
    //获取用户登录信息
    Map<String, String> userMap = TokenDecode.getUserInfo();
    String username = userMap.get("username");
    //查询用户收件地址
    List<Address> addressList = addressService.list(username);
    return new Result(true, StatusCode.OK,"查询成功！",addressList);
}
```

### 2.2.2 测试

访问 <http://localhost:8001/api/address/user/list>

![20](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/20.png)

### 2.2.3 运送清单

![21](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/21.png)

运送清单其实就是购物车列表，直接查询之前的购物车列表即可，这里不做说明了。

# 3. 下单

## 3.1 业务分析

点击结算页的时候，会立即创建订单数据，创建订单数据会将数据存入到2张表中，分别是订单表和订单明细表，此处还需要修改商品对应的库存数量。

![22](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/22.png)

订单表结构如下：

```sql
CREATE TABLE `tb_order` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单id',
  `total_num` int(11) DEFAULT NULL COMMENT '数量合计',
  `total_money` int(11) DEFAULT NULL COMMENT '金额合计',
  `pre_money` int(11) DEFAULT NULL COMMENT '优惠金额',
  `post_fee` int(11) DEFAULT NULL COMMENT '邮费',
  `pay_money` int(11) DEFAULT NULL COMMENT '实付金额',
  `pay_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '支付类型，1、在线支付、0 货到付款',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '订单更新时间',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `consign_time` datetime DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `shipping_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流名称',
  `shipping_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流单号',
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名称',
  `buyer_message` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '买家留言',
  `buyer_rate` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否评价',
  `receiver_contact` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
  `receiver_mobile` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机',
  `receiver_address` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人地址',
  `source_type` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面',
  `transaction_id` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '交易流水号',
  `order_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单状态,0:未完成,1:已完成，2：已退货',
  `pay_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '支付状态,0:未支付，1：已支付，2：支付失败',
  `consign_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '发货状态,0:未发货，1：已发货，2：已收货',
  `is_delete` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `create_time` (`create_time`),
  KEY `status` (`order_status`),
  KEY `payment_type` (`pay_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```

订单明细表结构如下：

```sql
CREATE TABLE `tb_order_item` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `category_id1` int(11) DEFAULT NULL COMMENT '1级分类',
  `category_id2` int(11) DEFAULT NULL COMMENT '2级分类',
  `category_id3` int(11) DEFAULT NULL COMMENT '3级分类',
  `spu_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'SPU_ID',
  `sku_id` bigint(20) NOT NULL COMMENT 'SKU_ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  `price` int(20) DEFAULT NULL COMMENT '单价',
  `num` int(10) DEFAULT NULL COMMENT '数量',
  `money` int(20) DEFAULT NULL COMMENT '总金额',
  `pay_money` int(11) DEFAULT NULL COMMENT '实付金额',
  `image` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '图片地址',
  `weight` int(11) DEFAULT NULL COMMENT '重量',
  `post_fee` int(11) DEFAULT NULL COMMENT '运费',
  `is_return` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否退货,0:未退货，1：已退货',
  PRIMARY KEY (`id`),
  KEY `item_id` (`sku_id`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```

## 3.2 下单实现

下单的时候，先添加订单往tb_order表中增加数据，再添加订单明细，往tb_order_item表中增加数据。

### 3.2.1 代码实现

这里先修改changgou-service-order微服务，实现下单操作，这里会生成订单号，我们首先需要在启动类中创建一个IdWorker对象。

在`com.changgou.OrderApplication`中创建IdWorker，代码如下：

```java
@Bean
public IdWorker idWorker(){
    return new IdWorker(1,1);
}
```

(1)业务层

修改changgou-service-order微服务，修改com.changgou.order.service.impl.OrderServiceImpl,代码如下：

修改订单微服务添加com.changgou.order.service.impl.OrderServiceImpl,代码如下：

```java
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 添加订单
     * @param order
     * @return
     */
    @Override
    public int add(Order order) {
        //查询出用户的所有购物车
        List<OrderItem> orderItems = cartService.list(order.getUsername());

        //统计计算
        int totalMoney = 0;
        int totalPayMoney=0;
        int num = 0;
        for (OrderItem orderItem : orderItems) {
            //总金额
            totalMoney+=orderItem.getMoney();

            //实际支付金额
            totalPayMoney+=orderItem.getPayMoney();
            //总数量
            num+=orderItem.getNum();
        }
        order.setTotalNum(num);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalPayMoney);
        order.setPreMoney(totalMoney-totalPayMoney);

        //其他数据完善
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");        //0:未评价，1：已评价
        order.setSourceType("1");       //来源，1：WEB
        order.setOrderStatus("0");      //0:未完成,1:已完成，2：已退货
        order.setPayStatus("0");        //0:未支付，1：已支付，2：支付失败
        order.setConsignStatus("0");    //0:未发货，1：已发货，2：已收货
        order.setId("NO."+idWorker.nextId());
        int count = orderMapper.insertSelective(order);

        //添加订单明细
        for (OrderItem orderItem : orderItems) {
            orderItem.setId("NO."+idWorker.nextId());
            orderItem.setIsReturn("0");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);
        }

        //清除Redis缓存购物车数据
        redisTemplate.delete("Cart_"+order.getUsername());
        return count;
    }
}
```

(2)控制层

修改changgou-service-order微服务，修改com.changgou.order.controller.OrderController类，代码如下：

```java
@Autowired
private TokenDecode tokenDecode;
/**
     * 添加订单 1.添加订单 2.更新库存 3 添加积分 4.清空购物车
     * @param
     * @return
     */
@PostMapping("/add")
public Result add(@RequestBody Order order) {
    String username = tokenDecode.getUsername();
    order.setUsername(username);
    orderService.add(order);
    return new Result(true, StatusCode.OK,"添加订单成功");
}
```

注意:获取用户名的方式,可以参考之前的方式:直接在微服务中创建javabean即可,如下图所示:

![23](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/23.png)

#### 3.2.2 测试

保存订单测试，表数据变化如下：

tb_order表数据：

![24](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/24.png)

tb_order_item表数据：

![25](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/25.png)

## 3.3 库存变更

### 3.3.1 业务分析

上面操作只实现了下单操作，但对应的库存还没跟着一起减少，我们在下单之后，应该调用商品微服务，将下单的商品库存减少，销量增加。每次订单微服务只需要将用户名传到商品微服务，商品微服务通过用户名到Redis中查询对应的购物车数据，然后执行库存减少，库存减少需要控制当前商品库存>=销售数量。

![26](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/26.png)

如何控制库存数量>=销售数量呢？其实可以通过SQL语句实现，每次减少数量的时候，加个条件判断。

`where num>=#{num}`即可。

该工程中一会儿需要查询购物车数据，所以需要引入订单的api，在pom.xml中添加如下依赖：

```xml
<!--order api 依赖-->
<dependency>
    <groupId>com.changgou</groupId>
    <artifactId>changgou-service-order-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 3.3.2 代码实现

(1)Dao层

修改changgou-service-goods微服务的`com.changgou.goods.dao.SkuMapper`接口，增加库存递减方法,代码如下：

```java
public interface SkuMapper extends Mapper<Sku> {
    @Update(value="update tb_sku set num=num-#{num} where id=#{id} and num>=#{num}")
    int decCount(@Param(value="id") Long id,@Param(value="num") Integer num);
}
```

(2)业务层

修改changgou-service-goods微服务的`com.changgou.goods.service.SkuService`接口，添加如下方法：

```java
//扣减库存
int decCount(Long id, Integer num);
```

修改changgou-service-goods微服务的`com.changgou.goods.service.impl.SkuServiceImpl`实现类，添加一个实现方法，代码如下：

```java
@Override
public int  decCount(Long id, Integer num) {
    /* Sku sku = skuMapper.selectByPrimaryKey(id);
        sku.setNum(sku.getNum()-num);
        skuMapper.updateByPrimaryKeySelective(sku);*/

    return skuMapper.decCount(id,num);
}
```

(3)控制层

修改changgou-service-goods的`com.changgou.goods.controller.SkuController`类，添加库存递减方法，代码如下：

```java
/**
     *  给指定的商品的ID 扣库存
     * @param id  要扣库存的商品的ID skuid
     * @param num  要扣的数量
     * @return
     */
@GetMapping("/decCount")
public Result decCount(@RequestParam(name="id") Long id, @RequestParam(name="num") Integer num){
    int count =  skuService.decCount(id,num);
    if (count > 0) {
        return new Result<List<Sku>>(true, StatusCode.OK,"扣库存成功") ;
    }
    return new Result<List<Sku>>(false, StatusCode.ERROR,"扣库存失败") ;
}
```

(4)创建feign

同时在changgou-service-goods-api工程添加`com.changgou.goods.feign.SkuFeign`的实现，代码如下：

```java
/***
 * 库存递减
 * @param username
 * @return
 */
@GetMapping("/decCount")
public Result decCount(@RequestParam(name="id") Long id, @RequestParam(name="num") Integer num);
```

### 3.3.3  调用库存递减

修改changgou-service-order微服务的com.changgou.order.service.impl.OrderServiceImpl类的add方法，增加库存递减的调用。

先注入SkuFeign

```java
@Autowired
private SkuFeign skuFeign;
```

再调用库存递减方法

```java
//库存减库存
 skuFeign.decCount(skuId, orderItem.getNum());
```

完整代码如下：

![27](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/27.png)

### 3.3.4 测试

库存减少前，查询数据库Sku数据如下：个数98，销量0

![28](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/28.png)

使用Postman执行 http://localhost:18081/api/order/add 

![29](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/29.png)

执行测试后，剩余库存97，销量1

![30](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/30.png)

## 3.4 增加积分

比如每次下单完成之后，给用户增加10个积分，支付完成后赠送优惠券，优惠券可用于支付时再次抵扣。我们先完成增加积分功能。如下表：points表示用户积分

```sql
CREATE TABLE `tb_user` (
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '修改时间',
  `source_type` varchar(1) DEFAULT NULL COMMENT '会员来源：1:PC，2：H5，3：Android，4：IOS',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `status` varchar(1) DEFAULT NULL COMMENT '使用状态（1正常 0非正常）',
  `head_pic` varchar(150) DEFAULT NULL COMMENT '头像地址',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ号码',
  `is_mobile_check` varchar(1) DEFAULT '0' COMMENT '手机是否验证 （0否  1是）',
  `is_email_check` varchar(1) DEFAULT '0' COMMENT '邮箱是否检测（0否  1是）',
  `sex` varchar(1) DEFAULT '1' COMMENT '性别，1男，0女',
  `user_level` int(11) DEFAULT NULL COMMENT '会员等级',
  `points` int(11) DEFAULT NULL COMMENT '积分',
  `experience_value` int(11) DEFAULT NULL COMMENT '经验值',
  `birthday` datetime DEFAULT NULL COMMENT '出生年月日',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`username`),
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
```

### 3.4.1 代码实现

(1)dao层

修改changgou-service-user微服务的`com.changgou.user.dao.UserMapper`接口，增加用户积分方法，代码如下：

```java
public interface UserMapper extends Mapper<User> {

    @Update(value="update tb_user set points=points+#{points} where username =#{username}")
    int addPoints(@Param(value="username") String username, @Param(value="points")Integer points);

}
```

(2)业务层

修改changgou-service-user微服务的`com.changgou.user.service.UserService`接口，代码如下：

```java
int addPoints(String username, Integer points);
```

修改changgou-service-user微服务的`com.changgou.user.service.impl.UserServiceImpl`，增加添加积分方法实现，代码如下：

```java
@Override
public int addPoints(String username, Integer points) {
    // update tb_user set points=points+#{points} where username =#{username}
    return userMapper.addPoints(username,points);
}
```

(3)控制层

修改changgou-service-user微服务的`com.changgou.user.controller.UserController`，添加增加用户积分方法，代码如下：

```java

@GetMapping("/points/add")
public Result addPoints(@RequestParam(name="username") String username,
                        @RequestParam(name="points") Integer points){
    int i = userService.addPoints(username, points);
    if(i>0){
        return new Result(true, StatusCode.OK, "添加积分成功");
    }else{
        return new Result(false, StatusCode.ERROR, "没有更新");
    }

}
```

(4)Feign添加

修改changgou-service-user-api工程，修改`com.changgou.user.feign.UserFeign`，添加增加用户积分方法，代码如下：

```java
/**
     * 给指定的用户名 添加积分
     * @param username  用户名
     * @param points  积分数
     * @return
     */
@GetMapping("/points/add")
public Result addPoints(@RequestParam(name="username") String username,
                        @RequestParam(name="points") Integer points);
```

### 3.4.2 增加积分调用

修改changgou-service-order，添加changgou-service-user-api的依赖，修改pom.xml,添加如下依赖：

```xml
<!--user api 依赖-->
<dependency>
    <groupId>com.changgou</groupId>
    <artifactId>changgou-service-user-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

在增加订单的时候，同时添加用户积分，修改changgou-service-order微服务的`com.changgou.order.service.impl.OrderServiceImpl`下单方法，增加调用添加积分方法，代码如下：

```java
userFeign.addPoints(order.getUsername(), 10);
```

整体代码如下：

```java
@Autowired
private UserFeign userFeign;

 public int add(Order order) {
        //略

        //3.添加用户的积分 默认加 10分
        userFeign.addPoints(order.getUsername(), 10);

		//略
        
}
```

修改changgou-service-order的启动类`com.changgou.OrderApplication`，添加feign的包路径：

![31](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/31.png)

## 3.5 测试

整体代码：

```java
@Override
    @Transactional(rollbackFor = Exception.class)//本地的mysql的事务 !!!!!分布式事务的问题
    public void add(Order order) {
        //1.添加数据到订单表
        //1.1 设置主键
        String orderId = idWorker.nextId()+"";
        order.setId(orderId);
        //1.2 设置总金额 总数量 todo    获取redis中的购物车的数据 循环遍历获取到总金额和总数量
        Integer totalNum=0;
        Integer totalMoney=0;
        List<OrderItem> values = redisTemplate.boundHashOps("Cart_" + order.getUsername()).values();
        for (OrderItem orderItem : values) {
            Integer num = orderItem.getNum();
            Integer money = orderItem.getMoney();
            totalNum+=num;
            totalMoney+=money;
            //补充属性
            String orderItemId = idWorker.nextId() + "";
            orderItem.setId(orderItemId);
            orderItem.setOrderId(orderId);
            orderItem.setIsReturn("0");//未退货
            orderItemMapper.insertSelective(orderItem);


            //减库存
            skuFeign.decCount(orderItem.getSkuId(),num);//todo 优化
        }


        //减库存  update tb_sku set num=num-#{num} where id=#{id} and num>=#{num}
        //1.在changgou-service-goods-api 创建feign接口 写一个方法 根据ID 和数量进行更新的方法
        //2.在changgou-service-goods微服务中实现接口
        //3.在changgou-service-order微服务中添加依赖  启用feignclients
        //4.注入使用

        //思考下批量进行添加数据  todo


        //加积分  update tb_user set points =points+#{points} where username = #{username}
        //调用积分微服务 获取到该活动或者该用户的应该给的积分的值
        userFeign.addPoints(order.getUsername(),10);




        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalMoney);

        //1.3 设置时间
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        //1.4 设置订单所属的用户名 在controller已经设置 这里不用
        order.setBuyerRate("0");//未评价

        //1.5 设置状态
        order.setPayStatus("0");
        order.setConsignStatus("0");
        order.setOrderStatus("0");
        order.setIsDelete("0");
        //2.添加数据到订单选项表中
        orderMapper.insertSelective(order);

        // 3 删除购物车的数据
        redisTemplate.delete("Cart_"+order.getUsername());

    }
```

测试数据

```
{
    "receiverContact":"张云",
    "receiverMobile":"13888888888",
    "receiverAddress":"马路上",
    "payType":"1",
    "buyerMessage":"快点",
    "sourceType":"1"
}
```

# 4. 支付流程分析

## 4.1 订单支付分析

![32](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/32.png)

如上图，步骤分析如下：

```
1.用户下单之后，订单数据会存入到MySQL中，同时会将订单对应的支付日志存入到Redis，以队列的方式存储。
2.用户下单后，进入支付页面，支付页面调用支付系统，从微信支付获取二维码数据，并在页面生成支付二维码。
3.用户扫码支付后，微信支付服务器会通调用前预留的回调地址，并携带支付状态信息。
4.支付系统接到支付状态信息后，将支付状态信息发送给RabbitMQ
5.订单系统监听RabbitMQ中的消息获取支付状态，并根据支付状态修改订单状态
6.为了防止网络问题导致notifyurl没有接到对应数据，定时任务定时获取Redis中队列数据去微信支付接口查询状态，并定时更新对应状态。
```

## 4.2 二维码创建(了解)

今天主要讲微信支付，后面为了看到效果，我们简单说下利用qrious制作二维码插件。

qrious是一款基于HTML5 Canvas的纯JS二维码生成插件。通过qrious.js可以快速生成各种二维码，你可以控制二维码的尺寸颜色，还可以将生成的二维码进行Base64编码。

qrious.js二维码插件的可用配置参数如下：

| 参数       | 类型   | 默认值      | 描述                               |
| ---------- | ------ | ----------- | ---------------------------------- |
| background | String | "white"     | 二维码的背景颜色。                 |
| foreground | String | "black"     | 二维码的前景颜色。                 |
| level      | String | "L"         | 二维码的误差校正级别(L, M, Q, H)。 |
| mime       | String | "image/png" | 二维码输出为图片时的MIME类型。     |
| size       | Number | 100         | 二维码的尺寸，单位像素。           |
| value      | String | ""          | 需要编码为二维码的值               |

下面的代码即可生成一张二维码

```html
<html>
<head>
<title>二维码入门小demo</title>
</head>
<body>
<img id="qrious">
<script src="qrious.js"></script>
<script>
 var qr = new QRious({
	    element:document.getElementById('qrious'),
	    size:250, 	   
     	level:'H',	   
     	value:'http://www.itheima.com'
	});
</script>
</body>
</html>
```

运行效果：

![33](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/33.png)

大家掏出手机，扫一下看看是否会看到黑马的官网呢？

# 5. 微信扫码支付简介

## 5.1微信扫码支付申请

微信扫码支付是商户系统按微信支付协议生成支付二维码，用户再用微信“扫一扫”完成支付的模式。该模式适用于PC网站支付、实体店单品或订单支付、媒体广告支付等场景。

申请步骤：（了解）

**第一步：注册公众号（类型须为：服务号）**

请根据营业执照类型选择以下主体注册：[个体工商户](http://kf.qq.com/faq/120911VrYVrA151009JB3i2Q.html)| [企业/公司](http://kf.qq.com/faq/120911VrYVrA151013MfYvYV.html)| [政府](http://kf.qq.com/faq/161220eaAJjE161220IJn6zU.html)| [媒体](http://kf.qq.com/faq/161220IFBJFv161220YnqAbQ.html)| [其他类型](http://kf.qq.com/faq/120911VrYVrA151013nYFZ7Z.html)。

**第二步：认证公众号**

公众号认证后才可申请微信支付，认证费：300元/次。

**第三步：提交资料申请微信支付**

登录公众平台，点击左侧菜单【微信支付】，开始填写资料等待审核，审核时间为1-5个工作日内。

**第四步：开户成功，登录商户平台进行验证**

资料审核通过后，请登录联系人邮箱查收商户号和密码，并登录商户平台填写财付通备付金打的小额资金数额，完成账户验证。

**第五步：在线签署协议**

本协议为线上电子协议，签署后方可进行交易及资金结算，签署完立即生效。

本课程已经提供好“传智播客”的微信支付账号，学员无需申请。

## 5.2 开发文档

微信支付接口调用的整体思路：

按API要求组装参数，以XML方式发送（POST）给微信支付接口（URL）,微信支付接口也是以XML方式给予响应。程序根据返回的结果（其中包括支付URL）生成二维码或判断订单状态。

在线微信支付开发文档：

<https://pay.weixin.qq.com/wiki/doc/api/index.html>

如果你不能联网，请查阅讲义配套资源 （资源\配套软件\微信扫码支付\开发文档）

我们在本章课程中会用到”统一下单”和”查询订单”两组API  

```properties
1. appid：微信公众账号或开放平台APP的唯一标识
2. mch_id：商户号  (配置文件中的partner)
3. partnerkey：商户密钥
4. sign:数字签名, 根据微信官方提供的密钥和一套算法生成的一个加密信息, 就是为了保证交易的安全性
```

## 5.3 微信支付模式介绍

### 5.3.1 模式一

![34](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/34.png)

业务流程说明：

```
1.商户后台系统根据微信支付规定格式生成二维码（规则见下文），展示给用户扫码。
2.用户打开微信“扫一扫”扫描二维码，微信客户端将扫码内容发送到微信支付系统。
3.微信支付系统收到客户端请求，发起对商户后台系统支付回调URL的调用。调用请求将带productid和用户的openid等参数，并要求商户系统返回交数据包,详细请见"本节3.1回调数据输入参数"
4.商户后台系统收到微信支付系统的回调请求，根据productid生成商户系统的订单。
5.商户系统调用微信支付【统一下单API】请求下单，获取交易会话标识（prepay_id）
6.微信支付系统根据商户系统的请求生成预支付交易，并返回交易会话标识（prepay_id）。
7.商户后台系统得到交易会话标识prepay_id（2小时内有效）。
8.商户后台系统将prepay_id返回给微信支付系统。返回数据见"本节3.2回调数据输出参数"
9.微信支付系统根据交易会话标识，发起用户端授权支付流程。
10.用户在微信客户端输入密码，确认支付后，微信客户端提交支付授权。
11.微信支付系统验证后扣款，完成支付交易。
12.微信支付系统完成支付交易后给微信客户端返回交易结果，并将交易结果通过短信、微信消息提示用户。微信客户端展示支付交易结果页面。
13.微信支付系统通过发送异步消息通知商户后台系统支付结果。商户后台系统需回复接收情况，通知微信后台系统不再发送该单的支付通知。
14.未收到支付通知的情况，商户后台系统调用【查询订单API】。
15.商户确认订单已支付后给用户发货。
```

### 5.3.2 模式二

![35](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/35.png)

业务流程说明：

```
1.商户后台系统根据用户选购的商品生成订单。
2.用户确认支付后调用微信支付【统一下单API】生成预支付交易；
3.微信支付系统收到请求后生成预支付交易单，并返回交易会话的二维码链接code_url。
4.商户后台系统根据返回的code_url生成二维码。
5.用户打开微信“扫一扫”扫描二维码，微信客户端将扫码内容发送到微信支付系统。
6.微信支付系统收到客户端请求，验证链接有效性后发起用户支付，要求用户授权。
7.用户在微信客户端输入密码，确认支付后，微信客户端提交授权。
8.微信支付系统根据用户授权完成支付交易。
9.微信支付系统完成支付交易后给微信客户端返回交易结果，并将交易结果通过短信、微信消息提示用户。微信客户端展示支付交易结果页面。
10.微信支付系统通过发送异步消息通知商户后台系统支付结果。商户后台系统需回复接收情况，通知微信后台系统不再发送该单的支付通知。
11.未收到支付通知的情况，商户后台系统调用【查询订单API】。
12.商户确认订单已支付后给用户发货。
```

# 6 总结

![36](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.22/pics/36.png)