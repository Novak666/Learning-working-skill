# 1. Swagger简介

Swagger是一个规范和完整的框架，用于生成、描述、调用和可视化RESTful风格的Web服务，作用是:

1. 使得前后端分离开发更加方便，有利于团队协作
2. 接口的文档在线自动生成，降低后端开发人员编写接口文档的负担
3. 功能测试，Spring已经将Swagger纳入自身的标准，建立了Spring-swagger项目，现在叫Springfox。通过在项目中引入Springfox ，即可非常简单快捷的使用Swagger

官网:

https://swagger.io/

# 2. SpringBoot集成Swagger

1. 在shanjupay-common项目中添加依赖，只需要在shanjupay-common中进行配置即可，因为其他微服务工程都直接或间接依赖shanjupay-common

```xml
<!--Swagger依赖-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox‐swagger2</artifactId>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox‐swagger‐ui</artifactId>
</dependency>
```

2. 在shanjupay-merchant-application工程的config包中添加一个Swagger配置类

```java
package com.shanjupay.merchant.config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

    @Configuration
    @ConditionalOnProperty(prefix = "swagger", value = {"enable"}, havingValue = "true")
    @EnableSwagger2
    public class SwaggerConfiguration {
        @Bean
        public Docket buildDocket() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(buildApiInfo())
                    .select()
// 要扫描的API(Controller)基础包
                    .apis(RequestHandlerSelectors.basePackage("com.shanjupay.merchant.controller"))
                    .paths(PathSelectors.any())
                    .build();
        }

        /**
         * @param
         * @return springfox.documentation.service.ApiInfo
         * <p>
         * * @Title: 构建API基本信息
         * * @methodName: buildApiInfo
         */

        private ApiInfo buildApiInfo() {
            Contact contact = new Contact("开发者", "", "");
            return new ApiInfoBuilder()
                    .title("闪聚支付‐商户应用API文档")
                    .description("")
                    .contact(contact)
                    .version("1.0.0").build();
        }
    }
```

3. 添加SpringMVC配置类：WebMvcConfig，让外部可直接访问Swagger文档

```java
package com.shanjupay.merchant.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加静态资源文件，外部可以直接访问地址
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger‐ui.html")
                .addResourceLocations("classpath:/META‐INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META‐INF/resources/webjars/");
    }
}
```

# 3. Swagger常用注解

在Java类中添加Swagger的注解即可生成Swagger接口文档，常用Swagger注解如下：

@Api：修饰整个类，描述Controller的作用 @ApiOperation：描述一个类的一个方法，或者说一个接口

@ApiParam：单个参数的描述信息

@ApiModel：用对象来接收参数

@ApiModelProperty：用对象接收参数时，描述对象的一个字

@ApiResponse：HTTP响应其中1个描述

@ApiResponses：HTTP响应整体描述

@ApiIgnore：使用该注解忽略这个API

@ApiError ：发生错误返回的信息

@ApiImplicitParam：一个请求参数

@ApiImplicitParams：多个请求参数的描述信息

@ApiImplicitParam属性：

| 属性         | 取值   | 作用                                           |
| ------------ | ------ | ---------------------------------------------- |
| paramType    |        | 查询参数类型                                   |
|              | path   | 以地址的形式提交数据                           |
|              | query  | 直接跟参数完成自动映射赋值                     |
|              | body   | 以流的形式提交，仅支持POST                     |
|              | header | 参数在request headers里边提交                  |
|              | form   | 以form表单的形式提交，仅支持POST               |
| dataType     |        | 参数的数据类型，只作为标志说明，并没有实际验证 |
|              | Long   |                                                |
|              | String |                                                |
| name         |        | 接收参数名                                     |
| value        |        | 接收参数的意义描述                             |
| required     |        | 参数是否必填                                   |
|              | true   | 必填                                           |
|              | false  | 非必填                                         |
| defaultValue |        | 默认值                                         |

上边的属性后边编写程序时用到哪个我再详细讲解，下边写一个swagger的简单例子，我们在MerchantController中添加Swagger注解，代码如下所示：

```java
@Api(value = "商户平台‐商户相关", tags = "商户平台‐商户相关", description = "商户平台‐商户相关")
@RestController
public class MerchantController {
    @Reference
    private MerchantService merchantService;
    
    @ApiOperation("根据id查询商户")
    @GetMapping("/merchants/{id}")
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {
        MerchantDTO merchantDTO = merchantService.queryMerchantById(id);
        return merchantDTO;
    }
    @ApiOperation("测试")
    @GetMapping(path = "/hello")
    public String hello(){
        return "hello";
    }
    @ApiOperation("测试")
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "string")
    @PostMapping(value = "/hi")
    public String hi(String name) {
        return "hi,"+name;
    }
}
```

# 4. Swagger测试

1. 启动商户应用和商户中心服务，访问http://localhost:57010/merchant/swagger-ui.html

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.12/pics/1.png)

2. 点击其中任意一项即可打开接口详情，如下图所示：

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.12/pics/2.png)

3. 点击“Try it out”开始测试，并录入参数信息，然后点击“Execute"发送请求，执行测试返回结果：“hi,李四”

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.12/pics/3.png)

Swagger生成API文档的工作原理：

+ shanjupay-merchant-application启动时会扫描到SwaggerConfiguration类
+ 在此类中指定了扫描包路径com.shanjupay.merchant.controller，会找到在此包下及子包下标记有@RestController注解的controller类
+ 根据controller类中的Swagger注解生成API文档

