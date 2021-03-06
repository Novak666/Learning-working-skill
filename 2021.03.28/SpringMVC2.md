# 1. 异步调用

## 1.1 发送异步请求(回顾) 

```js
<a href="javascript:void(0);" id="testAjax">访问controller</a>
<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
    $(function(){
    $("#testAjax").click(function(){ //为id="testAjax"的组件绑定点击事件
        $.ajax({ //发送异步调用
            type:"POST", //请求方式： POST请求
            url:"ajaxController", //请求参数（也就是请求内容）
            data:'ajax message', //请求参数（也就是请求内容）
            dataType:"text", //响应正文类型
            contentType:"application/text", //请求正文的MIME类型
        });
    });
});
</script>
```

## 1.2 接收异步请求参数

名称： @RequestBody
类型： 形参注解
位置：处理器类中的方法形参之前
作用：将异步提交数据组织成标准请求参数格式，并赋值给形参

```java
@RequestMapping("/ajaxController")
//使用@RequestBody注解，可以将请求体内容封装到指定参数中
public String ajaxController(@RequestBody String message){
    System.out.println("ajax request is running..."+message);
    return "page.jsp";
}
```

注解添加到Pojo参数前方时，封装的异步提交数据按照Pojo的属性格式进行关系映射

```java
@RequestMapping("/ajaxPojoToController")
//如果处理参数是POJO，且页面发送的请求数据格式与POJO中的属性对应，@RequestBody注解可以自动映射对应请求数据到POJO中
//注意：POJO中的属性如果请求数据中没有，属性值为null，POJO中没有的属性如果请求数据中有，不进行映射
public String  ajaxPojoToController(@RequestBody User user){
    System.out.println("controller pojo :"+user);
    return "page.jsp";
}
```

注解添加到集合参数前方时，封装的异步提交数据按照集合的存储结构进行关系映射 

```java
@RequestMapping("/ajaxListToController")
//如果处理参数是List集合且封装了POJO，且页面发送的数据是JSON格式的对象数组，数据将自动映射到集合参数中
public String  ajaxListToController(@RequestBody List<User> userList){
    System.out.println("controller list :"+userList);
    return "page.jsp";
}
```

## 1.3 接收异步请求并响应数据

```java
//使用注解@ResponseBody可以将返回的页面不进行解析，直接返回字符串，该注解可以添加到方法上方或返回值前面
    @RequestMapping("/ajaxReturnString")
//    @ResponseBody
    public @ResponseBody String ajaxReturnString(){
        System.out.println("controller return string ...");
        return "page.jsp";
    }
```

方法返回值为Pojo时，自动封装数据成json对象数据

```java
@RequestMapping("/ajaxReturnJson")
@ResponseBody
//基于jackon技术，使用@ResponseBody注解可以将返回的POJO对象转成json格式数据
public User ajaxReturnJson(){
    System.out.println("controller return json pojo...");
    User user = new User();
    user.setName("Jockme");
    user.setAge(39);
    return user;
}
```

方法返回值为List时，自动封装数据成json对象数组数据  

```java
@RequestMapping("/ajaxReturnJsonList")
@ResponseBody
//基于jackon技术，使用@ResponseBody注解可以将返回的保存POJO对象的集合转成json数组格式数据
public List ajaxReturnJsonList(){
    System.out.println("controller return json list...");
    User user1 = new User();
    user1.setName("Tom");
    user1.setAge(3);

    User user2 = new User();
    user2.setName("Jerry");
    user2.setAge(5);

    ArrayList al = new ArrayList();
    al.add(user1);
    al.add(user2);

    return al;
}
```

# 2. 跨域访问

当协议，IP地址，端口，域名中有一个不一样时，称为跨域访问

跨域访问模拟：

- 为当前主机添加备用域名
  - 修改windows安装目录中的host文件
  - 格式： ip 域名
- 动态刷新DNS
  - 命令： ipconfig /displaydns
  - 命令： ipconfig /flushdns  

解决：

名称： @CrossOrigin
类型： 方法注解 、 类注解
位置：处理器类中的方法上方 或 类上方
作用：设置当前处理器方法/处理器类中所有方法支持跨域访问

```java
@RequestMapping("/cross")
@ResponseBody
//使用@CrossOrigin开启跨域访问
//标注在处理器方法上方表示该方法支持跨域访问
//标注在处理器类上方表示该处理器类中的所有处理器方法均支持跨域访问
@CrossOrigin
public User cross(HttpServletRequest request){
    System.out.println("controller cross..."+request.getRequestURL());
    User user = new User();
    user.setName("Jockme");
    user.setAge(39);
    return user;
}
```

# 3. 拦截器

## 3.1 拦截器概念

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.28/pics/1.png)

拦截器(Interceptor)是一种动态拦截方法调用的机制

作用：

+ 在指定的方法调用前后执行预先设定后的的代码
+ 阻止原始方法的执行

核心原理： AOP思想

拦截器链：多个拦截器按照一定的顺序，对原始被调用功能进行增强

拦截器和过滤器的区别：

归属不同： Filter属于Servlet技术， Interceptor属于SpringMVC技术
拦截内容不同： Filter对所有访问进行增强， Interceptor仅针对SpringMVC的访问进行增强

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.28/pics/2.png)

## 3.2 自定义拦截器开发过程

1. 制作拦截器的功能类(通知)

   实现HandlerInterceptor接口

```java
//自定义拦截器需要实现HandleInterceptor接口
public class MyInterceptor implements HandlerInterceptor {
    //处理器运行之前执行
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        System.out.println("前置运行----a1");
        //返回值为false将拦截原始处理器的运行
        //如果配置多拦截器，返回值为false将终止当前拦截器后面配置的拦截器的运行
        return true;
    }

    //处理器运行之后执行
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("后置运行----b1");
    }

    //所有拦截器的后置执行全部结束后，执行该操作
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        System.out.println("完成运行----c1");
    }

    //三个方法的运行顺序为    preHandle -> postHandle -> afterCompletion
    //如果preHandle返回值为false，三个方法仅运行preHandle
}
```

2. 配置拦截器的执行位置(切入点)

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:mapping path="/showPage"/>
        <bean class="com.itheima.interceptor.MyInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```

  注意：配置顺序为先配置执行位置，后配置执行类

## 3.3 拦截器执行顺序

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.28/pics/3.png)

## 3.4 拦截器的方法参数与配置

### 3.4.1 前置处理方法

原始方法之前运行

```java
public boolean preHandle(HttpServletRequest request,
                         HttpServletResponse response,
                         Object handler) throws Exception {
    System.out.println("preHandle");
    return true;
}
```

- 参数
  request:请求对象
  response:响应对象
  handler:被调用的处理器对象，本质上是一个方法对象，对反射中的Method对象进行了再包装
- 返回值
  <font color='red'>返回值为false，被拦截的处理器将不执行</font>

### 3.4.2 后置处理方法

原始方法运行后运行，如果原始方法被拦截，则不执行  

```java
public void postHandle(HttpServletRequest request,
                       HttpServletResponse response,
                       Object handler,
                       ModelAndView modelAndView) throws Exception {
    System.out.println("postHandle");
}
```

+ 参数
  modelAndView:如果处理器执行完成具有返回结果，可以读取到对应数据与页面信息，并进行调整  

### 3.4.3 完成处理方法

```java
public void afterCompletion(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler,
                            Exception ex) throws Exception {
    System.out.println("afterCompletion");
}
```

+ 参数
  ex:如果处理器执行过程中出现异常对象，可以针对异常情况进行单独处理  

## 3.5 拦截器配置项

```xml
<mvc:interceptors>
    <!--开启具体的拦截器的使用，可以配置多个-->
    <mvc:interceptor>
        <!--设置拦截器的拦截路径，支持*通配-->
        <!--/**         表示拦截所有映射-->
        <!--/*          表示拦截所有/开头的映射-->
        <!--/user/*     表示拦截所有/user/开头的映射-->
        <!--/user/add*  表示拦截所有/user/开头，且具体映射名称以add开头的映射-->
        <!--/user/*All  表示拦截所有/user/开头，且具体映射名称以All结尾的映射-->
        <mvc:mapping path="/*"/>
        <mvc:mapping path="/**"/>
        <mvc:mapping path="/handleRun*"/>
        <!--设置拦截排除的路径，配置/**或/*，达到快速配置的目的-->
        <mvc:exclude-mapping path="/b*"/>
        <!--指定具体的拦截器类-->
        <bean class="MyInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```

## 3.6 多拦截器配置

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.28/pics/4.png)

**责任链模式**
责任链模式是一种行为模式
**特征：**
沿着一条预先设定的任务链顺序执行，每个节点具有独立的工作任务
**优势：**
独立性：只关注当前节点的任务，对其他任务直接放行到下一节点
隔离性：具备链式传递特征，无需知晓整体链路结构，只需等待请求到达后进行处理即可
灵活性：可以任意修改链路结构动态新增或删减整体链路责任
解耦：将动态任务与原始任务解耦
**弊端：**
链路过长时，处理效率低下
可能存在节点上的循环引用现象，造成死循环，导致系统崩溃

# 4. 异常处理

## 4.1 异常处理器

自定义一个类实现HandlerExceptionResolver接口(只要加上@Component注解，出现异常时程序会自动调用该异常处理器)

```java
@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {
        System.out.println("异常处理器正在执行中");
        ModelAndView modelAndView = new ModelAndView();
        //定义异常现象出现后，反馈给用户查看的信息
        modelAndView.addObject("msg","出错啦！ ");
        //定义异常现象出现后，反馈给用户查看的页面
        modelAndView.setViewName("error.jsp");
        return modelAndView;
    }
}
```

根据异常的种类不同，进行分门别类的管理，返回不同的信息  

```java
@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {
        System.out.println("my exception is running ...."+ex);
        ModelAndView modelAndView = new ModelAndView();
        if( ex instanceof NullPointerException){
            modelAndView.addObject("msg","空指针异常");
        }else if ( ex instanceof  ArithmeticException){
            modelAndView.addObject("msg","算数运算异常");
        }else{
            modelAndView.addObject("msg","未知的异常");
        }
        modelAndView.setViewName("error.jsp");
        return modelAndView;
    }
}
```

Controller

```java
@Controller
public class UserController {
    @RequestMapping("/save")
    @ResponseBody
    public List<User> save(@RequestBody User user) {
        System.out.println("user controller save is running ...");

        //模拟业务层发起调用产生了异常
//        int i = 1/0;
        String str = null;
        str.length();

        User u1 = new User();
        u1.setName("Tom");
        u1.setAge(3);
        User u2 = new User();
        u2.setName("Jerry");
        u2.setAge(5);
        ArrayList<User> al = new ArrayList<User>();
        al.add(u1);
        al.add(u2);

        return al;
    }
}
```

## 4.2 注解开发异常处理器

使用注解实现异常分类管理
名称： @ControllerAdvice
类型： 类注解
位置：异常处理器类上方
作用：设置当前类为异常处理器类

```java
@Component
@ControllerAdvice
public class ExceptionAdvice {
}  
```

使用注解实现异常分类管理
名称： @ExceptionHandler
类型： 方法注解
位置：异常处理器类中针对指定异常进行处理的方法上方
作用：设置指定异常的处理方式
说明：处理器方法可以设定多个

```java
@ExceptionHandler(Exception.class)
@ResponseBody
public String doOtherException(Exception ex){
    return "出错啦，请联系管理员！ ";
}  
```

```java
@ControllerAdvice
public class ExceptionAdvice {

    //类中定义的方法携带@ExceptionHandler注解的会被作为异常处理器，后面添加实际处理的异常类型
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public String doNullException(Exception ex){
        return "空指针异常";
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String doException(Exception ex){
        return "all";
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public String doArithmeticException(Exception ex){
        return "ArithmeticException";
    }

}
```

<font color='red'>注意：注解处理器可以拦截到入参类型转换异常，非注解不可以(Controller之后加载)</font>

## 4.3 异常处理解决方案

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.28/pics/5.png)

- 异常处理方案
  - 业务异常：
    发送对应消息传递给用户，提醒规范操作
  - 系统异常：
    发送固定消息传递给用户，安抚用户
    发送特定消息给运维人员，提醒维护
    记录日志
  - 其他异常：
    发送固定消息传递给用户，安抚用户
    发送特定消息给编程人员，提醒维护
    纳入预期范围内
    记录日志 

## 4.4 自定义异常

用户行为异常

```java
//自定义异常继承RuntimeException，覆盖父类所有的构造方法
public class BusinessException extends RuntimeException {
    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```

系统异常

```java
//自定义异常继承RuntimeException，覆盖父类所有的构造方法
public class SystemException extends RuntimeException {
    public SystemException() {
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```

模拟业务类异常

```java
public class UserServlceImpl {
    public void save(){
        //业务层中如果出现了异常，就对出现异常的代码进行try...catch...处理
        //在catch中将出现的异常包装成自定义异常，同时务必将当前异常对象传入自定义异常，避免真正的异常信息消失
        try {
            throw new SQLException();
        } catch (SQLException e) {
            throw new SystemException("数据库连接超时！",e);
        }
    }
}
```

项目异常(包括三类异常：用户异常、系统异常、其他异常)

```java
@ControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public String doBusinessException(Exception ex, Model m){
        //使用参数Model将要保存的数据传递到页面上，功能等同于ModelAndView
        //业务异常出现的消息要发送给用户查看
        m.addAttribute("msg",ex.getMessage());
        return "error.jsp";
    }

    @ExceptionHandler(SystemException.class)
    public String doSystemException(Exception ex, Model m){
        //系统异常出现的消息不要发送给用户查看，发送统一的信息给用户看
        m.addAttribute("msg","服务器出现问题，请联系管理员！");
        //实际的问题现象应该传递给redis服务器，运维人员通过后台系统查看
        //实际的问题显现更应该传递给redis服务器，运维人员通过后台系统查看
        return "error.jsp";
    }

    @ExceptionHandler(Exception.class)
    public String doException(Exception ex, Model m){
        m.addAttribute("msg",ex.getMessage());
        //将ex对象保存起来
        return "error.jsp";
    }

}
```

Controller

```java
@Controller
public class UserController {
    @RequestMapping("/save")
    @ResponseBody
    public List<User> save(@RequestBody User user) {
        System.out.println("user controller save is running ...");

        //模拟业务层发起调用产生了异常
//        int i = 1/0;
//        String str = null;
//        str.length();

        //对用户的非法操作进行判定，并包装成异常对象进行处理，便于统一管理
        if(user.getName().trim().length() < 8){
            throw new BusinessException("对不起，用户名长度不满足要求，请重新输入！");
        }
        if(user.getAge() < 0){
            throw new BusinessException("对不起，年龄必须是0到100之间的数字！");
        }
        if(user.getAge() > 100){
            throw  new SystemException("服务器连接失败，请尽快检查处理！");
        }


        User u1 = new User();
        u1.setName("Tom");
        u1.setAge(3);
        User u2 = new User();
        u2.setName("Jerry");
        u2.setAge(5);
        ArrayList<User> al = new ArrayList<User>();
        al.add(u1);
        al.add(u2);

        return al;
    }
}
```

# 5. 实用技术-文件

## 5.1 文件上传和下载

+ 上传文件过程分析

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.28/pics/6.png)

- MultipartResolver接口  

  - MultipartResolver接口定义了文件上传过程中的相关操作，并对通用性操作进行了封装
  - MultipartResolver接口底层实现类CommonsMultipartResovler
  - CommonsMultipartResovler并未自主实现文件上传下载对应的功能，而是调用了apache的文件上传下载组件  

  ```xml
  <dependency>
      <groupId>commons-fileupload</groupId>
      <artifactId>commons-fileupload</artifactId>
      <version>1.4</version>
  </dependency>
  ```

- 文件上传下载实现

  - 页面表单(.jsp)

    ```html
    <form action="/fileupload" method="post" enctype="multipart/form-data">
        上传LOGO： <input type="file" name="file"/><br/>
        <input type="submit" value="上传"/>
    </form>
    ```

  - SpringMVC配置

    ```xml
    <bean id="multipartResolver"
          class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    </bean>
    ```

  - 控制器

    ```java
    @RequestMapping(value = "/fileupload")
    public void fileupload(MultipartFile file){
        file.transferTo(new File("file.png"));
    }
    ```

## 5.2 文件上传注意事项

1. 文件命名问题， 获取上传文件名，并解析文件名与扩展名
2. 文件名过长问题
3. 文件保存路径
4. 重名问题

```java
@RequestMapping(value = "/fileupload")
//参数中定义MultipartFile参数，用于接收页面提交的type=file类型的表单，要求表单名称与参数名相同
public String fileupload(MultipartFile file,MultipartFile file1,MultipartFile file2, HttpServletRequest request) throws IOException {
    System.out.println("file upload is running ..."+file);
    //        MultipartFile参数中封装了上传的文件的相关信息
    //        System.out.println(file.getSize());
    //        System.out.println(file.getBytes().length);
    //        System.out.println(file.getContentType());
    //        System.out.println(file.getName());
    //        System.out.println(file.getOriginalFilename());
    //        System.out.println(file.isEmpty());
    //首先判断是否是空文件，也就是存储空间占用为0的文件
    if(!file.isEmpty()){
        //如果大小在范围要求内正常处理，否则抛出自定义异常告知用户（未实现）
        //获取原始上传的文件名，可以作为当前文件的真实名称保存到数据库中备用
        String fileName = file.getOriginalFilename();
        //设置保存的路径
        String realPath = request.getServletContext().getRealPath("/images");
        //保存文件的方法，指定保存的位置和文件名即可，通常文件名使用随机生成策略产生，避免文件名冲突问题
        file.transferTo(new File(realPath,file.getOriginalFilename()));
    }
    //测试一次性上传多个文件
    if(!file1.isEmpty()){
        String fileName = file1.getOriginalFilename();
        //可以根据需要，对不同种类的文件做不同的存储路径的区分，修改对应的保存位置即可
        String realPath = request.getServletContext().getRealPath("/images");
        file1.transferTo(new File(realPath,file1.getOriginalFilename()));
    }
    if(!file2.isEmpty()){
        String fileName = file2.getOriginalFilename();
        String realPath = request.getServletContext().getRealPath("/images");
        file2.transferTo(new File(realPath,file2.getOriginalFilename()));
    }
    return "page.jsp";
}
```

# 6. 实用技术-Restful风格配置

## 6.1 Rest简介

- Rest（ REpresentational State Transfer）一种网络资源的访问风格，定义了网络资源的访问方式
  - 传统风格访问路径
    http://localhost/user/get?id=1
    http://localhost/deleteUser?id=1
  - Rest风格访问路径
    http://localhost/user/1
- Restful是按照Rest风格访问网络资源
- 优点
  + 隐藏资源的访问行为，通过地址无法得知做的是何种操作
  + 书写简化

## 6.2 Rest行为约定方式

GET（查询） http://localhost/user/1 GET
POST（新增） http://localhost/user POST
PUT（更新） http://localhost/user PUT
DELETE（删除） http://localhost/user DELETE
**注意：**上述行为是约定方式，约定不是规范，可以打破，所以称Rest风格，而不是Rest规范

## 6.3 Restful开发入门

### 6.3.1 注解@RestController，@PathVariable

注解@RestController，@PathVariable

```java
//设置rest风格的控制器
@RestController
//设置公共访问路径，配合下方访问路径使用
@RequestMapping("/user/")
public class UserController {

    //rest风格访问路径完整书写方式
    @RequestMapping("/user/{id}")
    //使用@PathVariable注解获取路径上配置的具名变量，该配置可以使用多次
    public String restLocation(@PathVariable Integer id){
        System.out.println("restful is running ....");
        return "success.jsp";
    }

    //rest风格访问路径简化书写方式，配合类注解@RequestMapping使用
    @RequestMapping("{id}")
    public String restLocation2(@PathVariable Integer id){
        System.out.println("restful is running ....get:"+id);
        return "success.jsp";
    }

    //接收GET请求配置方式
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    //接收GET请求简化配置方式
    @GetMapping("{id}")
    public String get(@PathVariable Integer id){
        System.out.println("restful is running ....get:"+id);
        return "success.jsp";
    }

    //接收POST请求配置方式
    @RequestMapping(value = "{id}",method = RequestMethod.POST)
    //接收POST请求简化配置方式
    @PostMapping("{id}")
    public String post(@PathVariable Integer id){
        System.out.println("restful is running ....post:"+id);
        return "success.jsp";
    }

    //接收PUT请求简化配置方式
    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    //接收PUT请求简化配置方式
    @PutMapping("{id}")
    public String put(@PathVariable Integer id){
        System.out.println("restful is running ....put:"+id);
        return "success.jsp";
    }

    //接收DELETE请求简化配置方式
    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    //接收DELETE请求简化配置方式
    @DeleteMapping("{id}")
    public String delete(@PathVariable Integer id){
        System.out.println("restful is running ....delete:"+id);
        return "success.jsp";
    }
}
```

### 6.3.2 GET访问

默认是GET访问

### 6.3.3 POST访问

page.jsp

```jsp
<form action="/user/1" method="post">
    <input type="submit"/>
</form>
```

### 6.3.4 PUT访问

page.jsp

```jsp
<form action="/user/1" method="post">
    <%--当添加了name为_method的隐藏域时，可以通过设置该隐藏域的值，修改请求的提交方式，切换为PUT请求或DELETE请求，但是form表单的提交方式method属性必须填写post--%>
    <%--该配置需要配合HiddenHttpMethodFilter过滤器使用，单独使用无效，请注意检查web.xml中是否配置了对应过滤器--%>
    <input type="hidden" name="_method" value="PUT"/>
    <input type="submit"/>
</form>
```

web.xml

```xml
<!--配置拦截器，解析请求中的参数_method，否则无法发起PUT请求与DELETE请求，配合页面表单使用-->
<filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <servlet-name>DispatcherServlet</servlet-name>
</filter-mapping>
```

### 6.3.5 DELETE访问

同上

## 6.4 简化@RequestMapping配置

@GetMapping

```java
//接收GET请求配置方式
    //@RequestMapping(value = "{id}",method = RequestMethod.GET)
    //接收GET请求简化配置方式
    @GetMapping("{id}")
    public String get(@PathVariable Integer id){
        System.out.println("restful is running ....get:"+id);
        return "success.jsp";
    }
```

@PostMapping、@PutMapping、@DeleteMapping……

## 6.5 Postman

一款发送Restful风格请求的工具，可以在开发时快速进行测试

# 7. 实用技术-校验框架

## 7.1 校验框架简介

### 7.1.1 表单校验作用

数据可以随意输入，导致错误的结果。后端表单校验的重要性

表单校验保障了数据有效性、安全性

### 7.1.2 表单校验分类

- 校验位置：
  - 客户端校验
  - 服务端校验
- 校验内容与对应方式：
  - 格式校验
    - 客户端：使用Js技术，利用正则表达式校验
    - 服务端：使用校验框架 
  - 逻辑校验
    - 客户端：使用ajax发送要校验的数据，在服务端完成逻辑校验，返回校验结果
    - 服务端：接收到完整的请求后，在执行业务操作前，完成逻辑校验

### 7.1.3 表单校验规则

- 长度：例如用户名长度，评论字符数量
- 非法字符：例如用户名组成
- 数据格式：例如Email格式、 IP地址格式
- 边界值：例如转账金额上限，年龄上下限
- 重复性：例如用户名是否重复

### 7.1.4 表单校验框架

- JSR（Java Specification Requests）：Java 规范提案  

  303：提供bean属性相关校验规则  

- JSR规范列表

  - 企业应用技术
    Contexts and Dependency Injection for Java (Web Beans 1.0) (JSR 299)
    Dependency Injection for Java 1.0 (JSR 330)@postConstruct, @PreDestroy
    Bean Validation 1.0 (JSR 303)
    Enterprise JavaBeans 3.1 (includes Interceptors 1.1) (JSR 318)
    Java EE Connector Architecture 1.6 (JSR 322)
    Java Persistence 2.0 (JSR 317)
    Common Annotations for the Java Platform 1.1 (JSR 250)
    Java Message Service API 1.1 (JSR 914)
    Java Transaction API (JTA) 1.1 (JSR 907)
    JavaMail 1.4 (JSR 919)
  - Web应用技术
    Java Servlet 3.0 (JSR 315)
    JavaServer Faces 2.0 (JSR 314)
    JavaServer Pages 2.2/Expression Language 2.2 (JSR 245)
    Standard Tag Library for JavaServer Pages (JSTL) 1.2 (JSR 52)
    Debugging Support for Other Languages 1.0 (JSR 45)
    模块化 (JSR 294)
    Swing应用框架 (JSR 296)
    JavaBeans Activation Framework (JAF) 1.1 (JSR 925)
    Streaming API for XML (StAX) 1.0 (JSR 173)
  - 管理与安全技术
    Java Authentication Service Provider Interface for Containers (JSR 196)
    Java Authorization Contract for Containers 1.3 (JSR 115)
    Java EE Application Deployment 1.2 (JSR 88)
    J2EE Management 1.1 (JSR 77)
    Java SE中与Java EE有关的规范
    JCache API (JSR 107)
    Java Memory Model (JSR 133)
    Concurrency Utilitie (JSR 166)
    Java API for XML Processing (JAXP) 1.3 (JSR 206)
    Java Database Connectivity 4.0 (JSR 221)
    Java Management Extensions (JMX) 2.0 (JSR 255)
    Java Portlet API (JSR 286)

- Web Service技术
  Java Date与Time API (JSR 310)
  Java API for RESTful Web Services (JAX-RS) 1.1 (JSR 311)
  Implementing Enterprise Web Services 1.3 (JSR 109)
  Java API for XML-Based Web Services (JAX-WS) 2.2 (JSR 224)
  Java Architecture for XML Binding (JAXB) 2.2 (JSR 222)
  Web Services Metadata for the Java Platform (JSR 181)
  Java API for XML-Based RPC (JAX-RPC) 1.1 (JSR 101)
  Java APIs for XML Messaging 1.3 (JSR 67)
  Java API for XML Registries (JAXR) 1.0 (JSR 93)

- JCP（Java Community Process）：Java社区

- Hibernate框架中包含一套独立的校验框架hibernate-validator  

  导入坐标

  ```xml
  <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-validator</artifactId>
      <version>6.1.0.Final</version>
  </dependency>
  ```

  **注意：**
  tomcat7 ：搭配hibernate-validator版本5.*.*.Final
  tomcat8.5↑ ：搭配hibernate-validator版本6.*.*.Final

## 7.2 快速入门

pom.xml

```xml
<!--导入校验的jsr303规范-->
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version>
</dependency>
<!--导入校验框架实现技术-->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.1.0.Final</version>
</dependency>
```

**1. 开启校验**

名称：@Valid 、 @Validated
类型：形参注解
位置：处理器类Controller中的实体类类型的方法形参前方
作用：设定对当前实体类类型参数进行校验
范例：  

```java
@RequestMapping(value = "/addemployee")
public String addEmployee(@Valid Employee employee) {
    System.out.println(employee);
}
```

**2.设置校验规则**

名称：@NotNull
类型：属性注解 等
位置：实体类属性上方
作用：设定当前属性校验规则
范例：
    每个校验规则所携带的参数不同，根据校验规则进行相应的调整
    具体的校验规则查看对应的校验框架进行获取

```java
public class Employee{
    @NotNull(message = "姓名不能为空")
    private String name;//员工姓名
}  
```

**3.获取错误信息**

通过形参Errors获取校验结果数据，通过Model接口将数据封装后传递到页面显示

```java
@RequestMapping(value = "/addemployee")
public String addEmployee(@Validated({GroupA.class}) Employee employee, Errors errors, Model m){
    if(errors.hasErrors()){
        List<FieldError> fieldErrors = errors.getFieldErrors();
        System.out.println(fieldErrors.size());
        for(FieldError error : fieldErrors){
            System.out.println(error.getField());
            System.out.println(error.getDefaultMessage());
            m.addAttribute(error.getField(),error.getDefaultMessage());
        }
        return "addemployee.jsp";
    }
    return "success.jsp";
}
```

jsp页面获取后台封装的校验结果信息

```html
<form action="/addemployee" method="post">
    员工姓名：<input type="text" name="name"><span style="color:red">${name}</span><br/>
    员工年龄：<input type="text" name="age"><span style="color:red">${age}</span><br/>
    <input type="submit" value="提交">
</form>
```

## 7.3 多规则校验

- 同一个属性可以添加多个校验器

```java
@NotNull(message = "请输入您的年龄")
@Max(value = 60,message = "年龄最大值不允许超过60岁")
@Min(value = 18,message = "年龄最小值不允许低于18岁")
private Integer age;//员工年龄
```

- 3种判定空校验器的区别

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.28/pics/7.png)

## 7.4 嵌套校验

名称：@Valid
类型：属性注解
位置：实体类中的引用类型属性上方
作用：设定当前应用类型属性中的属性开启校验
范例：

```java
public class Employee {
    //实体类中的引用类型通过标注@Valid注解，设定开启当前引用类型字段中的属性参与校验
    @Valid
    private Address address;
}
```

注意：引用对象开启嵌套校验后，嵌套校验的实体中，对每个属性正常添加校验规则即可

## 7.5 分组校验

- 同一个模块，根据执行的业务不同，需要校验的属性会有不同
  - 新增用户
  - 修改用户
- 对不同种类的属性进行分组，在校验时可以指定参与校验的字段所属的组类别
  - 定义组（通用）
  - 为属性设置所属组，可以设置多个
  - 开启组校验

用@Validated注解

```java
//用于设定分组校验中的组名，当前接口仅提供字节码，用于识别
public interface GroupA {
}
```

```java
public String addEmployee(@Validated({GroupOne.class}) Employee employee){
}  
```

```java
@NotEmpty(message = "姓名不能为空",groups = {GroupOne.class})
private String name;//员工姓名
```