# Web开发总览

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.07.01/pics/1.png)

# 1. SpringMVC自动配置概览

Spring Boot provides auto-configuration for Spring MVC that **works well with most applications.(大多场景我们都无需自定义配置)**

The auto-configuration adds the following features on top of Spring’s defaults:

- Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans 内容协商视图解析器和BeanName视图解析器

- Support for serving static resources, including support for WebJars (covered [later in this document](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-spring-mvc-static-content))) 静态资源（包括webjars）

- Automatic registration of `Converter`, `GenericConverter`, and `Formatter` beans 自动注册 `Converter，GenericConverter，Formatter `

- Support for `HttpMessageConverters` (covered [later in this document](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-spring-mvc-message-converters)) 支持 `HttpMessageConverters` （后来我们配合内容协商理解原理）

- Automatic registration of `MessageCodesResolver` (covered [later in this document](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-spring-message-codes)) 自动注册 `MessageCodesResolver` （国际化用）

- Static `index.html` support 静态index.html 页支持

- Custom `Favicon` support (covered [later in this document](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-spring-mvc-favicon)) 自定义 `Favicon`  

- Automatic use of a `ConfigurableWebBindingInitializer` bean (covered [later in this document](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-spring-mvc-web-binding-initializer)) 自动使用 `ConfigurableWebBindingInitializer` ，（DataBinder负责将请求数据绑定到JavaBean上）

If you want to keep those Spring Boot MVC customizations and make more [MVC customizations](https://docs.spring.io/spring/docs/5.2.9.RELEASE/spring-framework-reference/web.html#mvc) (interceptors, formatters, view controllers, and other features), you can add your own `@Configuration` class of type `WebMvcConfigurer` but **without** `@EnableWebMvc`.

**不用@EnableWebMvc注解。使用** `**@Configuration**` **+** `**WebMvcConfigurer**` **自定义规则**

If you want to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`, or `ExceptionHandlerExceptionResolver`, and still keep the Spring Boot MVC customizations, you can declare a bean of type `WebMvcRegistrations` and use it to provide custom instances of those components.

**声明** `**WebMvcRegistrations**` **改变默认底层组件**

If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`, or alternatively add your own `@Configuration`-annotated `DelegatingWebMvcConfiguration` as described in the Javadoc of `@EnableWebMvc`.

**使用** `**@EnableWebMvc+@Configuration+DelegatingWebMvcConfiguration 全面接管SpringMVC**`

# 2. 简单功能分析

## 2.1 静态资源访问

### 2.1.1 静态资源目录

只要静态资源放在类路径下： called `/static` (or `/public` or `/resources` or `/META-INF/resources`

访问 ： 当前项目根路径/ + 静态资源名 

原理： 静态映射/**

请求进来，先去找Controller看能不能处理。不能处理的所有请求又都交给静态资源处理器。静态资源也找不到则响应404页面

### 2.1.2 静态资源访问前缀

静态资源访问前缀

```yaml
spring:
	mvc:
		static-path-pattern: /res/**
```

### 2.1.3 改变默认的静态资源路径

```yaml
spring:
  mvc:
    static-path-pattern: /res/**
    
  resources:
    static-locations: [classpath:/haha/]
```

### 2.1.4 webjar

可用jar方式添加css，js等资源文件，https://www.webjars.org

例如，添加jquery

```xml
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>jquery</artifactId>
    <version>3.5.1</version>
</dependency>
```

访问地址：[http://localhost:8080/webjars/**jquery/3.5.1/jquery.js**](http://localhost:8080/webjars/jquery/3.5.1/jquery.js) 后面地址要按照依赖里面的包路径

## 2.2 欢迎页支持

静态资源路径下index.html

- 可以配置静态资源路径
- 但是不可以配置静态资源的访问前缀。否则导致 index.html不能被默认访问

```yaml
spring:
#  mvc:
#    static-path-pattern: /res/** 这个会导致welcome page功能失效

  resources:
    static-locations: [classpath:/haha/]
```

controller能处理/index

## 2.3 自定义Favicon

指网页标签上的小图标，favicon.ico放在静态资源目录下即可

```yaml
spring:
#  mvc:
#    static-path-pattern: /res/**  这个会导致Favicon功能失效
```

## 2.4 静态资源配置原理

- SpringBoot启动默认加载 xxxAutoConfiguration 类（自动配置类）
- SpringMVC功能的自动配置类`WebMvcAutoConfiguration`，生效

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
    ...
}
```

配置文件的相关属性的绑定：

WebMvcProperties==**spring.mvc**、ResourceProperties==**spring.resources**

```java
@Configuration(proxyBeanMethods = false)
@Import(EnableWebMvcConfiguration.class)
@EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class })
@Order(0)
public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer {
    ...
}
```

### 2.4.1 配置类只有一个有参构造器

有参构造器所有参数的值都会从容器中确定

```java
//ResourceProperties resourceProperties；获取和spring.resources绑定的所有的值的对象
//WebMvcProperties mvcProperties 获取和spring.mvc绑定的所有的值的对象
//ListableBeanFactory beanFactory Spring的beanFactory
//HttpMessageConverters 找到所有的HttpMessageConverters
//ResourceHandlerRegistrationCustomizer 找到 资源处理器的自定义器
//DispatcherServletPath  
//ServletRegistrationBean   给应用注册Servlet、Filter....
public WebMvcAutoConfigurationAdapter(WebProperties webProperties, WebMvcProperties mvcProperties,
		ListableBeanFactory beanFactory, ObjectProvider<HttpMessageConverters> messageConvertersProvider,
		ObjectProvider<ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider,
		ObjectProvider<DispatcherServletPath> dispatcherServletPath,
		ObjectProvider<ServletRegistrationBean<?>> servletRegistrations) {
	this.mvcProperties = mvcProperties;
	this.beanFactory = beanFactory;
	this.messageConvertersProvider = messageConvertersProvider;
	this.resourceHandlerRegistrationCustomizer = resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
	this.dispatcherServletPath = dispatcherServletPath;
	this.servletRegistrations = servletRegistrations;
	this.mvcProperties.checkConfiguration();
}
```

### 2.4.2 资源处理的默认规则

```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
if (!this.resourceProperties.isAddMappings()) {
logger.debug("Default resource handling disabled");
return;
}
Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
//webjars的规则
if (!registry.hasMappingForPattern("/webjars/**")) {
customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
.addResourceLocations("classpath:/META-INF/resources/webjars/")
.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
}
//
String staticPathPattern = this.mvcProperties.getStaticPathPattern();
if (!registry.hasMappingForPattern(staticPathPattern)) {
customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern)
.addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
}
}
```

根据上述代码，我们可以同过配置禁止所有静态资源规则

```yaml
spring:
  resources:
    add-mappings: false   #禁用所有静态资源规则
```

静态资源规则

```java
@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
public class ResourceProperties {
private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
"classpath:/resources/", "classpath:/static/", "classpath:/public/" };
/**

Locations of static resources. Defaults to classpath:[/META-INF/resources/,

/resources/, /static/, /public/].
*/
private String[] staticLocations = CLASSPATH_RESOURCE_LOCATIONS;
```

### 2.4.3 欢迎页的处理规则

HandlerMapping：处理器映射。保存了每一个Handler能处理哪些请求

```java
...
public class WebMvcAutoConfiguration {
    ...
	public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration implements ResourceLoaderAware {
        ...
		@Bean
		public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext,
				FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
			WelcomePageHandlerMapping welcomePageHandlerMapping = new WelcomePageHandlerMapping(
					new TemplateAvailabilityProviders(applicationContext), applicationContext, getWelcomePage(),
					this.mvcProperties.getStaticPathPattern());
			welcomePageHandlerMapping.setInterceptors(getInterceptors(mvcConversionService, mvcResourceUrlProvider));
			welcomePageHandlerMapping.setCorsConfigurations(getCorsConfigurations());
			return welcomePageHandlerMapping;
		}
...
}
```

`WelcomePageHandlerMapping`的构造方法如下

```java
WelcomePageHandlerMapping(TemplateAvailabilityProviders templateAvailabilityProviders,
                          ApplicationContext applicationContext, Resource welcomePage, String staticPathPattern) {
    if (welcomePage != null && "/**".equals(staticPathPattern)) {
        //要用欢迎页功能，必须是/**
        logger.info("Adding welcome page: " + welcomePage);
        setRootViewName("forward:index.html");
    }
    else if (welcomeTemplateExists(templateAvailabilityProviders, applicationContext)) {
        //调用Controller /index
        logger.info("Adding welcome page template: index");
        setRootViewName("index");
    }
}
```

这构造方法内的代码也解释了web场景-welcome与favicon功能中配置`static-path-pattern`了，welcome页面和小图标失效的问题

# 3. 请求

## 3.1 请求映射

### 3.1.1 Rest使用与原理

Rest风格支持（使用**HTTP**请求方式动词来表示对资源的操作）

过去：

- /getUser 获取用户
- /deleteUser 删除用户
- /editUser 修改用户
- /saveUser保存用户

现在：

- GET-获取用户
- DELETE-删除用户
- PUT-修改用户
- POST-保存用户

对于表单数据，用法如下：

1. 开启页面表单的Rest功能

```yaml
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true   #开启页面表单的Rest功能
```

2. 页面 form的属性method=post，隐藏域 _method=put、delete等（如果直接get或post，无需隐藏域）

```html
<form action="/user" method="get">
    <input value="REST-GET提交" type="submit" />
</form>
<form action="/user" method="post">
    <input value="REST-POST提交" type="submit" />
</form>
<form action="/user" method="post">
    <input name="_method" type="hidden" value="DELETE"/>
    <input value="REST-DELETE 提交" type="submit"/>
</form>
<form action="/user" method="post">
    <input name="_method" type="hidden" value="PUT" />
    <input value="REST-PUT提交"type="submit" />
<form>
```

3. 编写请求映射

```java
@GetMapping("/user")
//@RequestMapping(value = "/user",method = RequestMethod.GET)
public String getUser(){
    return "GET-张三";
}

@PostMapping("/user")
//@RequestMapping(value = "/user",method = RequestMethod.POST)
public String saveUser(){
    return "POST-张三";
}

@PutMapping("/user")
//@RequestMapping(value = "/user",method = RequestMethod.PUT)
public String putUser(){
    return "PUT-张三";
}

@DeleteMapping("/user")
//@RequestMapping(value = "/user",method = RequestMethod.DELETE)
public String deleteUser(){
    return "DELETE-张三";
}
```

源码

```java
@Bean
  @ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
  @ConditionalOnProperty(prefix = "spring.mvc.hiddenmethod.filter", name = "enabled", matchIfMissing = false)
  public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
    return new OrderedHiddenHttpMethodFilter();
  }
```

~~~java
public class HiddenHttpMethodFilter extends OncePerRequestFilter {

```
private static final List<String> ALLOWED_METHODS =
		Collections.unmodifiableList(Arrays.asList(HttpMethod.PUT.name(),
				HttpMethod.DELETE.name(), HttpMethod.PATCH.name()));

/** Default method parameter: {@code _method}. */
public static final String DEFAULT_METHOD_PARAM = "_method";

private String methodParam = DEFAULT_METHOD_PARAM;
```

```
/**
 * Set the parameter name to look for HTTP methods.
 * @see #DEFAULT_METHOD_PARAM
 */
public void setMethodParam(String methodParam) {
	Assert.hasText(methodParam, "'methodParam' must not be empty");
	this.methodParam = methodParam;
}

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

	HttpServletRequest requestToUse = request;

	if ("POST".equals(request.getMethod()) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
		String paramValue = request.getParameter(this.methodParam);
		if (StringUtils.hasLength(paramValue)) {
			String method = paramValue.toUpperCase(Locale.ENGLISH);
			if (ALLOWED_METHODS.contains(method)) {
				requestToUse = new HttpMethodRequestWrapper(request, method);
			}
		}
	}

	filterChain.doFilter(requestToUse, response);
}
```

```
/**
 * Simple {@link HttpServletRequest} wrapper that returns the supplied method for
 * {@link HttpServletRequest#getMethod()}.
 */
private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

	private final String method;

	public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
		super(request);
		this.method = method;
	}

	@Override
	public String getMethod() {
		return this.method;
	}
}
```

}
~~~

Rest原理（表单提交要使用REST的时候）

- 表单提交会带上**_method=PUT**
- **请求过来被**HiddenHttpMethodFilter拦截

- 请求是否正常，并且是POST

- 获取到**_method**的值。
- 兼容以下请求；**PUT**.**DELETE**.**PATCH**

- **原生request（post），包装模式requesWrapper重写了getMethod方法，返回的是传入的值。**
- **过滤器链放行的时候用wrapper。以后的方法调用getMethod是调用**requesWrapper的

Rest使用客户端工具，如PostMan直接发送Put、delete等方式请求，无需Filter

SpringMVC新注解：

- @GetMapping
- @PostMapping
- @PutMapping
- @DeleteMapping

自定义filter，改变默认的`\_method`

```java
@Configuration(proxyBeanMethods = false)
public class WebConfig{
    //自定义filter
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        HiddenHttpMethodFilter methodFilter = new HiddenHttpMethodFilter();
        methodFilter.setMethodParam("_m");
        return methodFilter;
    }    
}
```

表单

```html
<input name="_m" type="hidden" value="delete"/>
```

### 3.1.2 请求映射原理

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.07.01/pics/2.png)

SpringMVC功能分析都从 `org.springframework.web.servlet.DispatcherServlet` -> `doDispatch()`

~~~java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
    boolean multipartRequestParsed = false;

```
WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

try {
    ModelAndView mv = null;
    Exception dispatchException = null;

    try {
        processedRequest = checkMultipart(request);
        multipartRequestParsed = (processedRequest != request);

        // 找到当前请求使用哪个Handler（Controller的方法）处理
        mappedHandler = getHandler(processedRequest);

        //HandlerMapping：处理器映射。/xxx->>xxxx
...
```

}
~~~

`getHandler()`方法如下：

```java
@Nullable
protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
    if (this.handlerMappings != null) {
        for (HandlerMapping mapping : this.handlerMappings) {
            HandlerExecutionChain handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
    }
    return null;
}
```

`this.handlerMappings`在Debug模式下展现的内容：

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.07.01/pics/3.png)

**RequestMappingHandlerMapping**：保存了所有@RequestMapping 和handler的映射规则：

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.07.01/pics/4.png)

所有的请求映射都在HandlerMapping中：

+ SpringBoot自动配置欢迎页的 WelcomePageHandlerMapping 。访问 /能访问到index.html

- SpringBoot自动配置了默认的RequestMappingHandlerMapping
- 请求进来，挨个尝试所有的HandlerMapping看是否有请求信息
  + 如果有就找到这个请求对应的handler
  + 如果没有就是下一个 HandlerMapping

- 我们需要一些自定义的映射处理，我们也可以自己给容器中放**HandlerMapping**。自定义 **HandlerMapping**

IDEA快捷键

Ctrl + H : 以树形方式展现类层次结构图

## 3.2 普通参数与基本注解

### 3.2.1 注解

+ @PathVariable 路径变量
+ @RequestHeader 获取请求头
+ @RequestParam 获取请求参数（指问号后的参数，url?a=1&b=2
+ @CookieValue 获取Cookie值
+ @RequestBody 获取请求体[POST]
+ @MatrixVariable 矩阵变量
+ @ModelAttribute

~~~java
@RestController
public class ParameterTestController {

```
//  car/2/owner/zhangsan
@GetMapping("/car/{id}/owner/{username}")
public Map<String,Object> getCar(@PathVariable("id") Integer id,
                                 @PathVariable("username") String name,
                                 @PathVariable Map<String,String> pv,
                                 @RequestHeader("User-Agent") String userAgent,
                                 @RequestHeader Map<String,String> header,
                                 @RequestParam("age") Integer age,
                                 @RequestParam("inters") List<String> inters,
                                 @RequestParam Map<String,String> params,
                                 @CookieValue("_ga") String _ga,
                                 @CookieValue("_ga") Cookie cookie){

    Map<String,Object> map = new HashMap<>();
```

//        map.put("id",id);
//        map.put("name",name);
//        map.put("pv",pv);
//        map.put("userAgent",userAgent);
//        map.put("headers",header);
        map.put("age",age);
        map.put("inters",inters);
        map.put("params",params);
        map.put("_ga",_ga);
        System.out.println(cookie.getName()+"===>"+cookie.getValue());
        return map;
    }

```
@PostMapping("/save")
public Map postMethod(@RequestBody String content){
    Map<String,Object> map = new HashMap<>();
    map.put("content",content);
    return map;
}
```

}
~~~

@RequestAttribute 获取request域属性

```java
@Controller
public class RequestController {
    @GetMapping("/goto")
    public String goToPage(HttpServletRequest request){

        request.setAttribute("msg","成功了...");
        request.setAttribute("code",200);
        return "forward:/success";  //转发到  /success请求
    }

    @ResponseBody
    @GetMapping("/success")
    public Map success(@RequestAttribute(value = "msg",required = false) String msg,
                       @RequestAttribute(value = "code",required = false)Integer code,
                       HttpServletRequest request){
        Object msg1 = request.getAttribute("msg");

        Map<String,Object> map = new HashMap<>();
        Object hello = request.getAttribute("hello");
        Object world = request.getAttribute("world");
        Object message = request.getAttribute("message");

        map.put("reqMethod_msg",msg1);
        map.put("annotation_msg",msg);
        map.put("hello",hello);
        map.put("world",world);
        map.put("message",message);

        return map;

    }
}
```

@MatrixVariable与UrlPathHelper

1. SpringBoot默认是禁用了矩阵变量的功能，需要手动开启。对于路径的处理。UrlPathHelper的removeSemicolonContent设置为false，让其支持矩阵变量

2. 矩阵变量**必须**有url路径变量才能被解析

手动开启矩阵变量

方法1：实现`WebMvcConfigurer`接口

~~~java
@Configuration(proxyBeanMethods = false)
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

```
    UrlPathHelper urlPathHelper = new UrlPathHelper();
    // 不移除；后面的内容。矩阵变量功能就可以生效
    urlPathHelper.setRemoveSemicolonContent(false);
    configurer.setUrlPathHelper(urlPathHelper);
}
```

}
~~~

方法2：创建返回`WebMvcConfigurer`Bean

```java
@Configuration(proxyBeanMethods = false)
public class WebConfig{
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
                        @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper = new UrlPathHelper();
                // 不移除；后面的内容。矩阵变量功能就可以生效
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }
        }
    }
}
```

测试代码：

~~~java
@RestController
public class ParameterTestController {

```
///cars/sell;low=34;brand=byd,audi,yd
@GetMapping("/cars/{path}")
public Map carsSell(@MatrixVariable("low") Integer low,
                    @MatrixVariable("brand") List<String> brand,
                    @PathVariable("path") String path){
    Map<String,Object> map = new HashMap<>();

    map.put("low",low);
    map.put("brand",brand);
    map.put("path",path);
    return map;
}

// /boss/1;age=20/2;age=10

@GetMapping("/boss/{bossId}/{empId}")
public Map boss(@MatrixVariable(value = "age",pathVar = "bossId") Integer bossAge,
                @MatrixVariable(value = "age",pathVar = "empId") Integer empAge){
    Map<String,Object> map = new HashMap<>();

    map.put("bossAge",bossAge);
    map.put("empAge",empAge);
    return map;

}
```

}
~~~

### 3.2.2 Servlet API

WebRequest、ServletRequest、MultipartRequest、 HttpSession、javax.servlet.http.PushBuilder、Principal、InputStream、Reader、HttpMethod、Locale、TimeZone、ZoneId

**ServletRequestMethodArgumentResolver** 

```java
@Override
public boolean supportsParameter(MethodParameter parameter) {
Class<?> paramType = parameter.getParameterType();
return (WebRequest.class.isAssignableFrom(paramType) ||
ServletRequest.class.isAssignableFrom(paramType) ||
MultipartRequest.class.isAssignableFrom(paramType) ||
HttpSession.class.isAssignableFrom(paramType) ||
(pushBuilder != null && pushBuilder.isAssignableFrom(paramType)) ||
Principal.class.isAssignableFrom(paramType) ||
InputStream.class.isAssignableFrom(paramType) ||
Reader.class.isAssignableFrom(paramType) ||
HttpMethod.class == paramType ||
Locale.class == paramType ||
TimeZone.class == paramType ||
ZoneId.class == paramType);
}
```

### 3.2.3 复杂参数

### 3.2.4 自定义对象参数

### 3.2.5 自定义Converter

将字符串`“啊猫,3”`转换成`Pet`对象

```java
//1、WebMvcConfigurer定制化SpringMVC的功能
@Bean
public WebMvcConfigurer webMvcConfigurer(){
    return new WebMvcConfigurer() {

        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(new Converter<String, Pet>() {

                @Override
                public Pet convert(String source) {
                    // 啊猫,3
                    if(!StringUtils.isEmpty(source)){
                        Pet pet = new Pet();
                        String[] split = source.split(",");
                        pet.setName(split[0]);
                        pet.setAge(Integer.parseInt(split[1]));
                        return pet;
                    }
                    return null;
                }
            });
        }
    };
}
```
## <font color='red'>3.3 参数处理原理</font>

<font color='red'>视频32-36，实在是太长了</font>

# 4. 响应

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.07.01/pics/5.png)

## 4.1 响应JSON

### 4.1.1 使用jackson.jar+@ResponseBody

引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- web场景自动引入了json场景 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-json</artifactId>
    <version>2.3.4.RELEASE</version>
    <scope>compile</scope>
</dependency>
```

controller

~~~java
@Controller
public class ResponseTestController {
    

```
@ResponseBody  //利用返回值处理器里面的消息转换器进行处理
@GetMapping(value = "/test/person")
public Person getPerson(){
    Person person = new Person();
    person.setAge(28);
    person.setBirth(new Date());
    person.setUserName("zhangsan");
    return person;
}
```

}
~~~

### 4.1.2 原理

<font color='red'>暂时跳过</font>

## 4.2 内容协商

根据客户端接收能力不同，返回不同媒体类型的数据

引入依赖

```xml
 <dependency>
     <groupId>com.fasterxml.jackson.dataformat</groupId>
     <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

Http协议中规定的，Accept字段告诉服务器本客户端可以接收的数据类型

可用Postman软件分别测试返回json和xml，只需要改变请求头中Accept字段为application/json、application/xml

### 4.2.1 原理

<font color='red'>暂时跳过</font>

### 4.2.2 自定义 MessageConverter

想改变SpringMVC的功能，都是在容器中添加WebMvcConfigurer，然后重写相应的方法

1. 自定义Converter

创建GuiguMessageConverter

2. 重写WebMvcConfigurer的方法

```java
@Override
public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new GuiguMessageConverter());
}
```

3. 测试代码

```java
/**
 * 1、浏览器发请求直接返回 xml    [application/xml]        jacksonXmlConverter
 * 2、如果是ajax请求 返回 json   [application/json]      jacksonJsonConverter
 * 3、如果硅谷app发请求，返回自定义协议数据  [appliaction/x-guigu]   xxxxConverter
 *          属性值1;属性值2;
 *
 * 步骤：
 * 1、添加自定义的MessageConverter进系统底层
 * 2、系统底层就会统计出所有MessageConverter能操作哪些类型
 * 3、客户端内容协商 [guigu--->guigu]
 *
 * 作业：如何以参数的方式进行内容协商
 * @return
 */
@ResponseBody  //利用返回值处理器里面的消息转换器进行处理
@GetMapping(value = "/test/person")
public Person getPerson(){
    Person person = new Person();
    person.setAge(28);
    person.setBirth(new Date());
    person.setUserName("zhangsan");
    return person;
}
```

4. 用Postman发送`/test/person`（请求头`Accept:application/x-guigu`)，将返回自定义协议数据的写出