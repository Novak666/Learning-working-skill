

# 学习目标

- 能够说出微信支付开发的整体思路

- 思路：生成支付二维码

- 思路：查询支付状态

- 实现支付成功修改订单的状态

  - MQ发送消息
  - MQ接收消息

- 支付成功之后微信需要通知商户进行支付状态的信息

# 1. 开发准备

## 1.1 开发文档

微信支付接口调用的整体思路：

按API要求组装参数，以XML方式发送（POST）给微信支付接口（URL）,微信支付接口也是以XML方式给予响应。程序根据返回的结果（其中包括支付URL）生成二维码或判断订单状态。

在线微信支付开发文档：

<https://pay.weixin.qq.com/wiki/doc/api/index.html>

如果你不能联网，请查阅讲义配套资源 （资源\配套软件\微信扫码支付\开发文档）

我们在本章课程中会用到”统一下单”和”查询订单”两组API  

 ```
1. appid：微信公众账号或开放平台APP的唯一标识
2. mch_id：商户号  (配置文件中的partner)
3. partnerkey：商户密钥
4. sign:数字签名, 根据微信官方提供的密钥和一套算法生成的一个加密信息, 就是为了保证交易的安全性
 ```

## 1.2 微信支付模式

**模式二**

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/1.png)

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

## 1.3 微信支付SDK

微信支付提供了SDK, 大家下载后打开源码，install到本地仓库。

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/2.png)

课程配套的本地仓库已经提供jar包，所以安装SDK步骤省略。使用微信支付SDK,在使用微信SDK的maven工程中引入依赖

````xml
<!--微信支付-->
<dependency>
    <groupId>com.github.wxpay</groupId>
    <artifactId>wxpay-sdk</artifactId>
    <version>0.0.3</version>
</dependency>
````

我们主要会用到微信支付SDK的以下功能：

获取随机字符串

```java
WXPayUtil.generateNonceStr()
```

MAP转换为XML字符串（自动添加签名）

```java
 WXPayUtil.generateSignedXml(param, partnerkey)
```

XML字符串转换为MAP

```java
WXPayUtil.xmlToMap(result)
```

为了方便微信支付开发，我们可以在`changgou-common`工程下引入依赖

```xml
<!--微信支付-->
<dependency>
    <groupId>com.github.wxpay</groupId>
    <artifactId>wxpay-sdk</artifactId>
    <version>0.0.3</version>
</dependency>
```

## 1.4 HttpClient工具类

HttpClient是Apache Jakarta Common下的子项目，用来提供高效的、最新的、功能丰富的支持HTTP协议的客户端编程工具包，并且它支持HTTP协议最新的版本和建议。HttpClient已经应用在很多的项目中，比如Apache Jakarta上很著名的另外两个开源项目Cactus和HTMLUnit都使用了HttpClient。

HttpClient通俗的讲就是模拟了浏览器的行为，如果我们需要在后端向某一地址提交数据获取结果，就可以使用HttpClient.

关于HttpClient（原生）具体的使用不属于我们本章的学习内容，我们这里这里为了简化HttpClient的使用，提供了工具类HttpClient（对原生HttpClient进行了封装）

HttpClient工具类代码：

```java
public class HttpClient {
    private String url;
    private Map<String, String> param;
    private int statusCode;
    private String content;
    private String xmlParam;
    private boolean isHttps;

    public boolean isHttps() {
        return isHttps;
    }

    public void setHttps(boolean isHttps) {
        this.isHttps = isHttps;
    }

    public String getXmlParam() {
        return xmlParam;
    }

    public void setXmlParam(String xmlParam) {
        this.xmlParam = xmlParam;
    }

    public HttpClient(String url, Map<String, String> param) {
        this.url = url;
        this.param = param;
    }

    public HttpClient(String url) {
        this.url = url;
    }

    public void setParameter(Map<String, String> map) {
        param = map;
    }

    public void addParameter(String key, String value) {
        if (param == null)
            param = new HashMap<String, String>();
        param.put(key, value);
    }

    public void post() throws ClientProtocolException, IOException {
        HttpPost http = new HttpPost(url);
        setEntity(http);
        execute(http);
    }

    public void put() throws ClientProtocolException, IOException {
        HttpPut http = new HttpPut(url);
        setEntity(http);
        execute(http);
    }

    public void get() throws ClientProtocolException, IOException {
        if (param != null) {
            StringBuilder url = new StringBuilder(this.url);
            boolean isFirst = true;
            for (String key : param.keySet()) {
                if (isFirst) {
                    url.append("?");
                }else {
                    url.append("&");
                }
                url.append(key).append("=").append(param.get(key));
            }
            this.url = url.toString();
        }
        HttpGet http = new HttpGet(url);
        execute(http);
    }

    /**
     * set http post,put param
     */
    private void setEntity(HttpEntityEnclosingRequestBase http) {
        if (param != null) {
            List<NameValuePair> nvps = new LinkedList<NameValuePair>();
            for (String key : param.keySet()) {
                nvps.add(new BasicNameValuePair(key, param.get(key))); // 参数
            }
            http.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8)); // 设置参数
        }
        if (xmlParam != null) {
            http.setEntity(new StringEntity(xmlParam, Consts.UTF_8));
        }
    }

    private void execute(HttpUriRequest http) throws ClientProtocolException,
            IOException {
        CloseableHttpClient httpClient = null;
        try {
            if (isHttps) {
                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, new TrustStrategy() {
                            // 信任所有
                            @Override
                            public boolean isTrusted(X509Certificate[] chain,
                                                     String authType)
                                    throws CertificateException {
                                return true;
                            }
                        }).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        sslContext);
                httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                        .build();
            } else {
                httpClient = HttpClients.createDefault();
            }
            CloseableHttpResponse response = httpClient.execute(http);
            try {
                if (response != null) {
                    if (response.getStatusLine() != null) {
                        statusCode = response.getStatusLine().getStatusCode();
                    }
                    HttpEntity entity = response.getEntity();
                    // 响应内容
                    content = EntityUtils.toString(entity, Consts.UTF_8);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() throws ParseException, IOException {
        return content;
    }
}
```

HttpClient工具类使用的步骤：

```java
HttpClient client=new HttpClient(请求的url地址);
client.setHttps(true);//是否是https协议
client.setXmlParam(xmlParam);//发送的xml数据
client.post();//执行post请求
String result = client.getContent(); //获取结果
```

将HttpClient工具包放到common工程下并引入依赖，引入依赖后就可以直接使用上述的工具包了。

```xml
<!--httpclient支持-->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
</dependency>
```

## 1.5 支付微服务搭建

(1)创建changgou-service-pay

创建支付微服务changgou-service-pay，只要实现支付相关操作。

(2)application.yml

创建application.yml，配置文件如下：

```properties
server:
  port: 18092
spring:
  application:
    name: pay
  main:
    allow-bean-definition-overriding: true
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:7001/eureka
  instance:
    prefer-ip-address: true
feign:
  hystrix:
    enabled: true
#hystrix 配置
hystrix:
  command:
    default:
      execution:
        timeout:
        #如果enabled设置为false，则请求超时交给ribbon控制
          enabled: true
        isolation:
          strategy: SEMAPHORE

#微信支付信息配置
weixin:
  appid: wx8397f8696b538317
  partner: 1473426802
  partnerkey: T6m9iK73b0kn9g5v426MKfHQH7X8rKwb
  notifyurl: http://www.itcast.cn
```

appid： 微信公众账号或开放平台APP的唯一标识

partner：财付通平台的商户账号

partnerkey：财付通平台的商户密钥

notifyurl:  回调地址

(3)启动类创建

在`changgou-service-pay`中创建`com.changgou.WeixinPayApplication`，代码如下：

```java
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class WeixinPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinPayApplication.class,args);
    }
}
```

# 2. 微信支付二维码生成

## 2.1 需求分析与实现思路

在支付页面上生成支付二维码，并显示订单号和金额

用户拿出手机,打开微信扫描页面上的二维码,然后在微信中完成支付

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/3.png)

## 2.2 实现思路

我们通过HttpClient工具类实现对远程支付接口的调用。

接口链接：https://api.mch.weixin.qq.com/pay/unifiedorder

具体参数参见“统一下单”API, 构建参数发送给统一下单的url ，返回的信息中有支付url，根据url生成二维码，显示的订单号和金额也在返回的信息中。

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/4.png)

```
1.用户打开支付的页面
2.支付页面发送请求到后台畅购支付系统
3.畅购支付系统发送https的POST请求（调用统一下单的API） 给微信支付系统
	+ 使用自定义封装过的httpclient来模拟浏览器发送https请求
	+ 使用wx sdk进行数据处理（xml转成map ,map转成XML）
4.微信支付系统返回一个code_url
5.畅购支付系统返回给前端，前端通过qrious.js 生成二维码展示给用户
```

httpclient：

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/5.png)

```
请求：/create/native  GET
参数：金额 订单号
返回值：Map:{code_url,金额，订单号

controller service dao
```

## 2.3 代码实现

(1)业务层

新增`com.changgou.service.WeixinPayService`接口，代码如下：

```java
public interface WeixinPayService {
    /*****
     * 创建二维码
     * @param out_trade_no : 客户端自定义订单编号
     * @param total_fee    : 交易金额,单位：分
     * @return
     */
    public Map createNative(String out_trade_no, String total_fee);
}
```

创建`com.changgou.service.impl.WeixinPayServiceImpl`类,并发送Post请求获取预支付信息，包含二维码扫码支付地址。代码如下：

```java
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;

    /****
     * 创建二维码
     * @param out_trade_no : 客户端自定义订单编号
     * @param total_fee    : 交易金额,单位：分
     * @return
     */
    @Override
    public Map createNative(String out_trade_no, String total_fee){
        try {
            //1、封装参数
            Map param = new HashMap();
            param.put("appid", appid);                              //应用ID
            param.put("mch_id", partner);                           //商户ID号
            param.put("nonce_str", WXPayUtil.generateNonceStr());   //随机数
            param.put("body", "畅购");                            	//订单描述
            param.put("out_trade_no",out_trade_no);                 //商户订单号
            param.put("total_fee", total_fee);                      //交易金额
            param.put("spbill_create_ip", "127.0.0.1");           //终端IP
            param.put("notify_url", notifyurl);                    //回调地址
            param.put("trade_type", "NATIVE");                     //交易类型

            //2、将参数转成xml字符，并携带签名
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);

            ///3、执行请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("stringMap:"+stringMap);

            //5、获取部分页面所需参数
            Map<String,String> dataMap = new HashMap<String,String>();
            dataMap.put("code_url",stringMap.get("code_url"));
            dataMap.put("out_trade_no",out_trade_no);
            dataMap.put("total_fee",total_fee);

            return dataMap;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

(2) 控制层

创建`com.changgou.controller.WeixinPayController`,主要调用WeixinPayService的方法获取创建二维码的信息，代码如下：

```java
@RestController
@RequestMapping(value = "/weixin/pay")
@CrossOrigin
public class WeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    /***
     * 创建二维码
     * @return
     */
    @RequestMapping(value = "/create/native")
    public Result createNative(String out_trade_no, String total_fee){
        Map<String,String> resultMap = weixinPayService.createNative(out_trade_no,total_fee);
        return new Result(true, StatusCode.OK,"创建二维码预付订单成功！",resultMap);
    }
}
```

这里我们订单号通过随机数生成，金额暂时写死，后续开发我们再对接业务系统得到订单号和金额

Postman测试

`http://localhost:18092/weixin/pay/create/native?out_trade_no=No000000001&total_fee=1`

如下效果

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/6.png)打开支付页面/pay.html，copy 上边如图的code_url的值 到pay.html中的 value的值，再次打开，会出现二维码，可以扫码试试

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/7.png)

测试如下：

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/8.png)

# 3. 检测支付状态

## 3.1 需求分析

当用户支付成功后跳转到成功页面

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/9.png)

当返回异常时跳转到错误页面

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/10.png)

## 3.2 实现思路

我们通过HttpClient工具类实现对远程支付接口的调用。

接口链接：https://api.mch.weixin.qq.com/pay/orderquery

具体参数参见“查询订单”API, 我们在前端方法中轮询调用查询订单（例如间隔3秒），当返回状态为success时，我们会在controller方法返回结果。前端代码收到结果后跳转到成功页面。

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/11.png)

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/12.png)

```
1.一旦二维码生成就开始定时轮询发送请求到后台的畅购支付系统（3S一次）
2.畅购支付系统接收到请求之后，调用查询订单的API 查询订单的支付状态
3.畅购支付系统 返回给前端的状态的信息
4.前端需要判断 如果成功，则跳转到成功的页面，如果没有成功，继续发送请求，直到超时为止
```

```
请求 查询状态

请求： /weixin/pay/status/query  GET
参数： 订单号
返回值：Result<Map<String,String> 存储到是各种支付的状态
```

## 3.3 代码实现

(1)业务层

修改`com.changgou.service.WeixinPayService`，新增方法定义

```java
/***
 * 查询订单状态
 * @param out_trade_no : 客户端自定义订单编号
 * @return
 */
public Map queryPayStatus(String out_trade_no);
```

在com.changgou.pay.service.impl.WeixinPayServiceImpl中增加实现方法

```java
/***
 * 查询订单状态
 * @param out_trade_no : 客户端自定义订单编号
 * @return
 */
@Override
public Map queryPayStatus(String out_trade_no) {
    try {
        //1.封装参数
        Map param = new HashMap();
        param.put("appid",appid);                            //应用ID
        param.put("mch_id",partner);                         //商户号
        param.put("out_trade_no",out_trade_no);              //商户订单编号
        param.put("nonce_str",WXPayUtil.generateNonceStr()); //随机字符

        //2、将参数转成xml字符，并携带签名
        String paramXml = WXPayUtil.generateSignedXml(param,partnerkey);

        //3、发送请求
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        httpClient.setHttps(true);
        httpClient.setXmlParam(paramXml);
        httpClient.post();

        //4、获取返回值，并将返回值转成Map
        String content = httpClient.getContent();
        return WXPayUtil.xmlToMap(content);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
```

(2)控制层

在`com.changgou.controller.WeixinPayController`新增方法，用于查询支付状态，代码如下：

上图代码如下：

```java
/***
 * 查询支付状态
 * @param outtradeno
 * @return
 */
@GetMapping(value = "/status/query")
public Result queryStatus(String out_trade_no){
    Map<String,String> resultMap = weixinPayService.queryPayStatus(out_trade_no);
    return new Result(true,StatusCode.OK,"查询状态成功！",resultMap);
}
```

# 4. 订单状态操作

## 4.1 需求分析

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/13.png)

我们现在系统还有个问题需要解决：支付后订单状态没有改变

订单状态修改的流程说明

```
1.用户提交订单发送请求到支付微服务
2.支付微服务调用【统一下单API】接口生成支付订单返回给前端
3.前端获取到code_url之后使用qrious.js生成支付二维码
4.用户使用手机扫码支付成功
5.微信支付系统收到用户扫码成功，并通过notifyurl设置的值异步通知到支付微服务
6.支付微服务发送 MQ消息到MQ服务器 
7.订单微服务监听消息 并修改订单的状态即可
```

定时轮询判断订单支付状态的流程（了解）

```
1.在用户生成二维码未支付之前，前端不停的轮询发送请求 查询支付状态 例如3秒
2.定时任务每隔30秒启动一次，找出最近10分钟内创建并且未支付的订单，调用《微信支付查单接口》核实订单状态。系统记录订单查询的次数，在10次查询之后状态还是未支付成功，则停止后续查询，并调用《关单接口》关闭订单。（轮询时间间隔和次数，商户可以根据自身业务场景灵活设置）
```

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/14.png)上图流程说明如下：

```
1.支付微服务定义定时任务
2.通过feign调用查询订单微服务的最新10分钟的订单列表
3.判断是否已经支付 如果支付 则更新状态 删除统计信息
4.如果没有支付，则判断是否已经达到统计次数上限 如果没有 则累计统计次数
5.如果已经达到上限 说明还没有支付，则关闭交易订单，并删除订单 把库存恢复即可。
```

# 5. 支付信息回调

## 5.1 接口分析

每次实现支付之后，微信支付都会将用户支付结果返回到指定路径，而指定路径是指创建二维码的时候填写的`notifyurl`参数,响应的数据以及相关文档参考一下地址：`https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7&index=8`

![15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/15.png)

### 5.1.1 返回参数分析

通知参数如下：

| 字段名     | 变量名      | 必填 | 类型        | 示例值  | 描述    |
| :--------- | :---------- | :--- | :---------- | :------ | :------ |
| 返回状态码 | return_code | 是   | String(16)  | SUCCESS | SUCCESS |
| 返回信息   | return_msg  | 是   | String(128) | OK      | OK      |

以下字段在return_code为SUCCESS的时候有返回

| 字段名         | 变量名         | 必填 | 类型       | 示例值                       | 描述                                            |
| :------------- | :------------- | :--- | :--------- | :--------------------------- | :---------------------------------------------- |
| 公众账号ID     | appid          | 是   | String(32) | wx8888888888888888           | 微信分配的公众账号ID（企业号corpid即为此appId） |
| 业务结果       | result_code    | 是   | String(16) | SUCCESS                      | SUCCESS/FAIL                                    |
| 商户订单号     | out_trade_no   | 是   | String(32) | 1212321211201407033568112322 | 商户系统内部订单号                              |
| 微信支付订单号 | transaction_id | 是   | String(32) | 1217752501201407033233368018 | 微信支付订单号                                  |

### 5.1.2 响应分析

回调地址接收到数据后，需要响应信息给微信服务器，告知已经收到数据，不然微信服务器会再次发送4次请求推送支付信息。

| 字段名     | 变量名      | 必填 | 类型        | 示例值  | 描述           |
| :--------- | :---------- | :--- | :---------- | :------ | :------------- |
| 返回状态码 | return_code | 是   | String(16)  | SUCCESS | 请按示例值填写 |
| 返回信息   | return_msg  | 是   | String(128) | OK      | 请按示例值填写 |

举例如下：

```xml
<xml>
  <return_code><![CDATA[SUCCESS]]></return_code>
  <return_msg><![CDATA[OK]]></return_msg>
</xml>
```

## 5.2 回调接收数据实现

修改`changgou-service-pay`微服务的com.changgou.pay.controller.WeixinPayController,添加回调方法，代码如下：

```java
/***
 * 支付回调
 * @param request
 * @return
 */
@RequestMapping(value = "/notify/url")
public String notifyUrl(HttpServletRequest request){
    InputStream inStream;
    try {
        //读取支付回调数据
        inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        // 将支付回调数据转换成xml字符串
        String result = new String(outSteam.toByteArray(), "utf-8");
        //将xml字符串转换成Map结构
        Map<String, String> map = WXPayUtil.xmlToMap(result);

        //响应数据设置
        Map respMap = new HashMap();
        respMap.put("return_code","SUCCESS");
        respMap.put("return_msg","OK");
        return WXPayUtil.mapToXml(respMap);
    } catch (Exception e) {
        e.printStackTrace();
        //记录错误日志
    }
    return null;
}
```

## 5.3 测试

由于我们微信通知的地址值需要外网访问使用所以没有办法接收到，这里我们可以使用一个工具utools配置内网穿透即可

（1）下载utools

如下图已经下载好了，如果有需要可以自己在官网下载`<http://u.tools/download.html>`

![16](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/16.png)

(2)安装之后打开软件

![17](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/17.png)

（3）配置地址：

重新点击utools 在弹出框栏位输入地址：NAT

![18](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/18.png)

配置如下

![19](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/19.png)

再点击链接

![20](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/20.png)

![21](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/21.png)

# 6. MQ处理支付回调状态

## 6.1 业务分析

用户扫码支付成功之后，通过notifyurl通知到我们，我们需要正确的处理这些消息。那么我们支付系统只会处理支付相关的业务，不处理支付之外的业务。可以监听微信通知的消息，立即发送消息到MQ中，其他微服务监听消息 然后修改订单的状态即可。这样有以下好处：

```
1.业务解耦
2.异步处理提升系统吞吐量，及时反馈给微信，耗时操作可以放到别的系统进行处理
```

参考图如下：

 ![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/13.png)

支付成功后，需要修改订单的状态

![22](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/22.png)

```
1.用户扫描支付成功
2.微信支付系统就会根据之前的下单的API中提供的notify_url的地址 进行通知（发送请求）
3.畅购支付系统接收到通知（来自于微信的请求）
4.发送消息给MQ 并立即响应给微信
5.订单的微服务监听到消息，实现业务的处理（修改订单的支付的状态 支付的时间，支付的流水号）
```

```
1.发送消息的一方 生产者
2.接收消息的一方 消费者

消息类型：
	简单模式
	工作模式
	发布订阅模式：（使用的多）
		+ 广播
		+ 路由(交换机+队列+绑定)
		+ 通配符
springboot整合rabbitmq的使用：（1.安装rabbitmq的服务端）
	+ 生产者
		+ 1.添加起步 依赖
		+ 2.配置配置文件（链接到服务端的ip和端口 和用户名和密码 和虚拟主机vhosts）
		+ 3.创建启动类（创建交换机 队列  绑定）
		+ 4.使用rabbitTemplate 发送消息
	+ 消费者
		+ 1.添加起步 依赖
		+ 2.配置配置文件（链接到服务端的ip和端口 和用户名和密码 和虚拟主机vhosts）
		+ 3.创建启动类（创建交换机 队列  绑定）
		+ 4.创建监听类（通过注解@rabbitListener(queues="队列名")） （实现业务逻辑）
```

		+ 广播
		+ 路由(交换机+队列+绑定)
		+ 通配符
springboot整合rabbitmq的使用：（1.安装rabbitmq的服务端）
	+ 生产者
		+ 1.添加起步 依赖
		+ 2.配置配置文件（链接到服务端的ip和端口 和用户名和密码 和虚拟主机vhosts）
		+ 3.创建启动类（创建交换机 队列  绑定）
		+ 4.使用rabbitTemplate 发送消息
	+ 消费者
		+ 1.添加起步 依赖
		+ 2.配置配置文件（链接到服务端的ip和端口 和用户名和密码 和虚拟主机vhosts）
		+ 3.创建启动类（创建交换机 队列  绑定）
		+ 4.创建监听类（通过注解@rabbitListener(queues="队列名")） （实现业务逻辑）

## 6.2 步骤实现

### 6.2.1 支付微服务环境配置准备

(1)集成RabbitMQ

修改支付微服务changgou-service-pay，集成RabbitMQ，添加如下依赖：

```xml
<!--加入ampq-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

这里我们建议在后台手动创建队列，并绑定队列。如果使用程序创建队列，可以按照如下方式实现。

修改changgou-service-pay微服务中的application.yml，配置支付队列和交换机信息，代码如下：

```properties
#位置支付交换机和队列
mq:
  pay:
    exchange:
      order: exchange.order
    queue:
      order: queue.order
    routing:
      key: queue.order
```

创建队列以及交换机并让队列和交换机绑定，修改com.changgou.WeixinPayApplication,添加如下代码：

```java
   @Autowired
    private Environment environment;


    //创建队列
    @Bean
    public Queue queueOrder(){
        return new Queue(environment.getProperty("mq.pay.queue.order"));
    }

    //创建交互机 路由模式的交换机
    @Bean
    public DirectExchange createExchange(){
        return new DirectExchange(environment.getProperty("mq.pay.exchange.order"));
    }

    //创建绑定
    @Bean
    public Binding createBinding(){
        return BindingBuilder.bind(queueOrder()).to(createExchange()).with(environment.getProperty("mq.pay.routing.key"));
    }
```

支付微服务changgou-service-pay中的application.yml配置rabbitmq的配置项：

![23](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/23.png)

### 6.2.2 支付微服务发送MQ消息

修改回调方法，在接到支付信息后，立即将支付信息发送给RabbitMQ，代码如下：

![24](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/24.png)

上图代码如下：

```java
@Value("${mq.pay.exchange.order}")
private String exchange;
@Value("${mq.pay.queue.order}")
private String queue;
@Value("${mq.pay.routing.key}")
private String routing;

@Autowired
private WeixinPayService weixinPayService;

@Autowired
private RabbitTemplate rabbitTemplate;

/***
 * 支付回调
 * @param request
 * @return
 */
@RequestMapping(value = "/notify/url")
public String notifyUrl(HttpServletRequest request){
    InputStream inStream;
    try {
        //读取支付回调数据
        inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        // 将支付回调数据转换成xml字符串
        String result = new String(outSteam.toByteArray(), "utf-8");
        //将xml字符串转换成Map结构
        Map<String, String> map = WXPayUtil.xmlToMap(result);
        //将消息发送给RabbitMQ
        rabbitTemplate.convertAndSend(exchange,routing, JSON.toJSONString(map));

        //响应数据设置
        Map respMap = new HashMap();
        respMap.put("return_code","SUCCESS");
        respMap.put("return_msg","OK");
        return WXPayUtil.mapToXml(respMap);
    } catch (Exception e) {
        e.printStackTrace();
        //记录错误日志
    }
    return null;
}
```

### 6.2.3 订单微服务环境配置准备

在订单微服务中，先集成RabbitMQ，再监听队列消息。changgou-service-order微服务中在pom.xml中引入如下依赖：

```xml
<!--加入ampq-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

在application.yml中配置rabbitmq配置，代码如下：

![25](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/25.png)

在application.yml中配置队列名字，代码如下：

```properties
mq:
  pay:
    exchange:
      order: exchange.order
    queue:
      order: queue.order
    routing:
      key: queue.order
```

在启动类中添加如下代码：

```yaml
   @Autowired
    private Environment environment;

    //创建队列 交给spring容器
    @Bean
    public Queue queueOrder(){
        return new Queue(environment.getProperty("mq.pay.queue.order"));
    }

    //创建交换机 交给spring

    @Bean
    public DirectExchange createExchange(){
        return new DirectExchange(environment.getProperty("mq.pay.exchange.order"));
    }

    //创建绑定 交给spring容器

    @Bean
    public Binding createBinding(){
        return BindingBuilder.bind(queueOrder()).to(createExchange()).with(environment.getProperty("mq.pay.routing.key"));
    }
```

### 6.2.4 订单微服务监听消息修改状态

在订单微服务于中创建com.changgou.order.consumer.OrderPayMessageListener，并在该类中consumeMessage方法，用于监听消息，并根据支付状态处理订单，代码如下：

```java
@Component
@RabbitListener(queues = "queue.order")
public class PayOrderUpdateListener {

    @Autowired
    private OrderMapper orderMapper;


    @RabbitHandler//方法用处理监听到queue.order队列的消息  可靠性消息
    public void handler(String msg) {
        System.out.println(msg);
        //1.获取消息本身
        //2.将其转换成MAP对象
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        if (map != null) {
            String out_trade_no = map.get("out_trade_no");
            //3.判断是否支付成功 如果成功 则 更新状态
            if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
                //1.获取订单号 更新数据到数据库

                Order order = orderMapper.selectByPrimaryKey(out_trade_no);
                order.setPayStatus("1");//设置支付柱状图为1
                String time_end = map.get("time_end");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                try {
                    Date parse = simpleDateFormat.parse(time_end);
                    order.setPayTime(parse);//付款时间
                    order.setUpdateTime(parse);
                    order.setTransactionId(map.get("transaction_id"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                orderMapper.updateByPrimaryKeySelective(order);
            } else {
                Order order = orderMapper.selectByPrimaryKey(out_trade_no);
                order.setIsDelete("1");//删除
                //4.如果失败  删除相关信息 为了简单我们 删除掉订单
                orderMapper.updateByPrimaryKeySelective(order);
            }
        }

    }
}
```

# 7. 总结

![26](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.12.06/pics/26.png)