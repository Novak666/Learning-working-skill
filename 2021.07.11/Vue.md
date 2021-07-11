# 1. VueJS简介

## 1.1 什么是VueJS

​	Vue.js是一个渐进式JavaScript 框架。Vue.js 的目标是通过尽可能简单的 API 实现响应的数据绑定和组合的视图组件。它不仅易于上手，还便于与第三方库或既有项目整合。

​	官网:https://cn.vuejs.org/

常见的前端的框架:

​	jQuery、Anglar、Vue、React、Node

## 1.2 特点

+ 易用
+ 灵活
+ 高效

## 1.3 MVVM模式

​	MVVM是Model-View-View-Model的简写。它本质上就是MVC的改进版。

​	MVVM 就是将其中的View的状态和行为抽象化，让我们将视图UI和业务逻辑分开. MVVM模式和MVC模式一样，主要目的是分离视图（View）和模型（Model）Vue.js 是一个提供了 MVVM 风格的**双向数据绑定**的 Javascript 库，专注于View 层。它的核心是 MVVM 中的 VM，也就是 ViewModel。 ViewModel负责连接 View 和 Model，保证视图和数据的一致性，这种轻量级的架构让前端开发更加高效、便捷.

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/1.png)

# 2. VueJs快速入门

## 2.1 需求

​	使用vue，对message赋值，并把值显示到页面

## 2.2 步骤

1. 创建工程,拷贝vue.js到工程
2. 创建demo01.html把vue.js引入到页面
3. 创建vue实例, 定义message显示

## 2.3 实现

1. 创建工程(war),导入vuejs

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/2.png)

2. 创建demo01.js(引入vuejs,定义div,创建vue实例)

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>vue的入门案例</title>
    <script src="js/vuejs-2.5.16.js"></script>
</head>
<body>
    <!--
        指定一块区域，在这块区域中可以使用vue
        只有这块区域中的标签，才能使用vue和数据模型绑定
    -->
    <div id="app">
        <!--
            将vue的视图中的message绑定到div中,使用插值表达式{{message}}
        -->
        <div>{{username}}</div>
    </div>

    <script>
        var vue = new Vue({
            el:"#app",//指定哪个区域可以使用vue
            data:{
                message:"hello world",
                username:"周杰棍"
            }//数据模型：要展示到视图上的数据
        });
    </script>
</body>
</html>
```

data ：用于定义数据。

## 2.4 小结

​	数据绑定最常见的形式就是使用“Mustache”语法 (双大括号) 的文本**插值表达式**，Mustache 标签将会被替代为对应数据对象上属性的值。无论何时，绑定的数据对象上属性发生了改变，插值处的内容都会更新.

# 3. VueJS常用系统指令

## 3.1 常用的事件(重点)

### 3.1.1 @click

说明: 点击事件(等同于v-on:click)

【需求】：点击按钮事件，改变message的值

+ demo02.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:v-on="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>vue绑定事件</title>
    <script src="js/vuejs-2.5.16.js"></script>
</head>
<body>
    <div id="app">
        <div>{{message}}</div>
        <!--
            点击按钮，改变div中的内容
            使用vue绑定点击事件:
                1. v-on:click="函数()"
                2.@click="函数()"
        -->
        <input type="button" value="改变" @click="fn1()">
    </div>
    <script>
        var vue = new Vue({
           el:"#app",
           data:{
               message:"hello world"
           },
           methods:{
               fn1(){
                    //改变div中的内容,只需要改变message的值就行
                   this.message = "hello vue"
               }
           }
        });
    </script>
</body>
</html>
```

### 3.1.2 @keydown  

说明: 键盘按下事件(等同于v-on:keydown)

【需求】：对文本输入框做校验，使用键盘按下事件，如果按下0-9的数字，正常显示，其他按键则阻止事件执行。

+ demo03.js

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>vue绑定键盘按下事件</title>
    <script src="js/vuejs-2.5.16.js"></script>
</head>
<body>
    <!--
        对文本输入框做校验，使用键盘按下事件，如果按下0-9的数字，正常显示，其他按键则阻止事件执行。
    -->
    <div id="app">
        <!--
            $event就是你当前触发的事件
        -->
        <input type="text" @keydown="fn1($event)">
    </div>
    <script>
        var vue = new Vue({
           el:"#app",
           data:{

           },
           methods:{
               fn1(event){
                    //判断你按下的那个按键是否是0-9，如果不是0-9则阻止事件触发
                   //event.keyCode获取当前按键的键码值
                   var keyCode = event.keyCode;
                   //先判断，按键是否是删除键
                   if (keyCode != 8) {
                       if (keyCode < 48 || keyCode > 57) {
                           //说明输入的不是0-9，则阻止事件发生
                           event.preventDefault()
                       }
                   }
               }
           }
        });
    </script>
</body>
</html>
```

### 3.1.3 @mouseover

说明:鼠标移入区域事件(等同于v-on:mouseover)

【需求1】：给指定区域大小的div中添加样式，鼠标移到div中，弹出窗口。

+ demo04.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>vue绑定鼠标移入事件</title>
    <script src="js/vuejs-2.5.16.js"></script>
    <style>
        .box {
            width: 300px;
            height: 400px;
            border: 1px solid red;
        }

    </style>
</head>
<body>
    <div id="app">
        <div class="box" @mouseover="fn1()">
            div
        </div>
    </div>
    <script>
        var vue = new Vue({
           el:"#app",
           methods:{
                fn1(){
                    alert("鼠标移入了...")
                }
           }
        });
    </script>
</body>
</html>
```

小结

1. 事件规则: 把js的事件的on换成了`@`或者`v-on:`【重点】

   + onclick==>@click
   + onkeydown==>@keydown
   + onmouseover==>@mouseover

   ...

## 3.2 v-text与v-html(重点)

v-text：输出文本内容，不会解析html元素

v-html：输出文本内容，会解析html元素

用在标签的属性里面

【需求】：分别使用v-text, v-html 赋值 `<h1>hello world<h1>` ，查看页面输出内容。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>vue绑定标签体的内容</title>
    <script src="js/vuejs-2.5.16.js"></script>
</head>
<body>
<div id="app">
    <!--
        使用v-text可以绑定标签体中文本内容
    -->
    <div style="color: red" v-text="msg1"></div>

    <!--使用v-html可以绑定标签体中的所有内容-->
    <div style="color: blue" v-html="msg2"></div>
</div>
<script>
    var vue = new Vue({
       el:"#app",
       data:{
           msg1:"hello div1",
           msg2:"hello div2"
       }
    });
</script>
</body>
</html>
```

小结

1. v-text: 输出文本, ==不支持标签, 把标签当做文本==
2. v-html:输出标签, ==支持标签, 解析标签==

> 用在标签的属性

## 3.3 v-bind和v-model(重点)

### 3.3.1 v-bind

**插值语法不能作用在HTML属性上**，遇到这种情况应该使用 v-bind指令

```html
<!DOCTYPE html>
<html lang="en" xmlns:v-bind="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>vue绑定属性</title>
    <script src="js/vuejs-2.5.16.js"></script>
</head>
<body>
<div id="app">
    <!--
        vue绑定html标签的属性，使用v-bind进行绑定: v-bind:属性名="数据模型"
        简写  :属性名="数据模型"
        绑定a标签的href属性
    -->
    <a :href="url1">跳转到百度</a><br>
    <a :href="'https://www.qq.com?id='+id">跳转到腾讯网，并且携带请求参数id的值</a><br>
    <font :color="ys">你好世界</font>
</div>
<script>
    new Vue({
        el:"#app",
        data:{
            url1:"https://www.baidu.com",
            id:30,
            ys:"red"
        }
    })
</script>
</body>
</html>
```

### 3.3.2 v-model(重点)  

用于数据的绑定,数据的读取，主要是用于对表单数据进行双向绑定

【需求】：使用vue赋值json(对象)数据，并显示到页面的输入框中（表单回显）. 点击获取数据，在控制台输出表单中的数据；点击回显数据，设置表单的数据

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>v-model对表单数据进行双向绑定</title>
    <script src="js/vuejs-2.5.16.js"></script>
</head>
<body>
<div id="app">
    <form>
        用户名<input type="text" name="username" v-model="user.username"><br>
        密码<input type="text" name="password" v-model="user.password"><br>
        昵称<input type="text" name="nickname" v-model="user.nickname"><br>
        地址<input type="text" name="address" v-model="user.address"><br>

        <!--
            1. 需求1: 点击获取表单数据的按钮的时候，将表单的所有数据获取到
        -->
        <input type="button" value="获取表单的数据" @click="obtainFormData()"><br>

        <!--
            2. 需求2: 点击回显表单数据，重新设置表单中的内容
        -->
        <input type="button" value="回显表单数据" @click="setFormData()">
    </form>

</div>

<script>
    var vue = new Vue({
       el:"#app",
       data:{
           user:{
               username:"张三",
               password:"123456",
               nickname:"张三疯",
               address:"武当山"
           }
       } ,
       methods:{
           //获取表单数据
           obtainFormData(){
                //其实就是获取数据模型user
                console.log(this.user)
           },
           setFormData(){
                //假设: 接收到了服务器端的响应数据
               var responseData = {
                   username:"李四",
                   password:"654321",
                   nickname:"李四疯",
                   address:"峨眉山"
               }

               //目的：将表单的值设置为responseData里面的值
               //只要将responseData的值设置给user就行了
               this.user = responseData
           }
       }
    });
</script>
</body>
</html>
```

小结

1. `v-bind:`   就是让vue能够用在`html的标签的属性上`  简写`:` 

```
<font color='ys'></font>   --读取不到ys的值
<font :color='ys'></font>  --读取到ys的值
```

2. v-model: 绑定
   + 数据变化了 视图就变化
   + 视图变化了 数据就变化

## 3.4 v-for,v-if,v-show

### 3.4.1 v-for(重点)

用于操作array/集合，遍历

语法:  `v-for="(元素,index) in 数组/集合"`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>使用v-for遍历</title>
    <script src="js/vuejs-2.5.16.js"></script>
</head>
<body>
    <div id="app">
        <!--城市列表-->
        <ul>
            <!--
                使用v-for绑定数组内容
            -->
            <li v-for="(cityName,index) in cityList" :id="index" v-html="cityName"></li>
        </ul>

        <table border="1" cellspacing="0" align="center" width="500">
            <tr>
                <th>编号</th>
                <th>姓名</th>
                <th>年龄</th>
                <th>地址</th>
            </tr>

            <tr v-for="(linkman,index) in linkmanList">
                <td v-html="index+1"></td>
                <td v-html="linkman.name"></td>
                <td v-html="linkman.age"></td>
                <td v-html="linkman.address"></td>
            </tr>
        </table>
    </div>
<script>
    var vue = new Vue({
       el:"#app",
       data:{
           cityList:["北京","上海","深圳","广州","长沙"],
           linkmanList:[
               {
                   name:"张三",
                   age:18,
                   address:"深圳"
               },
               {
                   name:"李四",
                   age:28,
                   address:"广州"
               },
               {
                   name:"王五",
                   age:18,
                   address:"惠州"
               }
           ]
       }
    });
</script>
</body>
</html>
```

### 3.4.2 v-if(重点)与v-show

v-if是根据表达式的值来决定是否渲染元素(标签都没有了)

v-show是根据表达式的值来切换元素的display css属性(标签还在)。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>vue绑定标签的显示和隐藏</title>
    <script src="../js/vuejs-2.5.16.js"></script>
</head>
<body>
    <div id="app">
        <!--
            给img标签绑定v-if，如果值为true就显示，值为false就隐藏; v-if是通过直接删除标签来隐藏标签的

            给img标签绑定v-show，如果值为true就显示，值为false就隐藏;v-show是通过设置display为none来隐藏标签的
        -->
        <img v-show="isShow" src="../img/mm.jpg" width="400px" height="400px"><br>
        <input type="button" value="切换显示和隐藏" @click="toggleImg()">
    </div>
    <script>
        var vue = new Vue({
           el:"#app",
           data:{
               isShow:true
           },
            methods:{
               toggleImg(){
                    this.isShow = !this.isShow
               }
            }
        });
    </script>
</body>
</html>
```

小结

1. v-for:作为标签的属性使用的, 遍历

```
<标签 v-for="(元素,索引) in 数组"></标签>
//1.元素的变量名随便取
//2.索引的变量名随便取

```

2. v-if: 作为标签的属性使用的, 决定了标签是否展示

```
<标签 v-if="boolean类型的"></标签>

//1.v-if里面是true, 展示
//2.v-if里面是false, 不展示,标签都没有
```

## 3.5 Vue的常用指令的回顾

1. 在vue的入门案例中，需要注意的点:
   1. JavaScript的版本应该设置为ECMAScript6
   2. el表示vue的容器
   3. data表示数据模型
   4. methods表示vue的函数
2. 绑定事件:
   1. v-on:click="函数"
   2. @click="函数"
3. v-text 和 v-html 设置标签体的内容
4. v-bind绑定属性
5. v-model绑定表单的内容
6. v-for 进行遍历
7. v-if 和 v-show 控制标签的显示和隐藏

# 4. VueJS生命周期(了解)

## 4.1 什么是VueJS生命周期

​	就是vue实例从创建到销毁的过程.

​	每个 Vue 实例在被创建到销毁都要经过一系列的初始化过程——例如，需要设置数据监听、编译模板、将实例挂载到 DOM 并在数据变化时更新 DOM 等。同时在这个过程中也会运行一些叫做生命周期钩子的函数，这给了用户在不同阶段添加自己的代码的机会。

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/3.png)

+ beforeCreate ：数据还没有监听，没有绑定到vue对象实例，同时也没有挂载对象

+ **created**：数据已经绑定到了对象实例，但是还没有挂载对象（使用ajax可在此方法中查询数据，调用函数）

+ beforeMount: 模板已经编译好了，根据数据和模板已经生成了对应的元素对象，将数据对象关联到了对象的

  el属性，el属性是一个HTMLElement对象，也就是这个阶段，vue实例通过原生的createElement等方法来创
  建这个html片段，准备注入到我们vue实例指明的el属性所对应的挂载点

+ **mounted**:将el的内容挂载到了el，相当于我们在jquery执行了(el).html(el),生成页面上真正的dom，上面我们
  就会发现dom的元素和我们el的元素是一致的。在此之后，我们能够用方法来获取到el元素下的dom对象，并
  进行各种操作当我们的data发生改变时，会调用beforeUpdate和updated方法

+ beforeUpdate ：数据更新到dom之前，我们可以看到$el对象已经修改，但是我们页面上dom的数据还
  没有发生改变

+ updated: dom结构会通过虚拟dom的原则，找到需要更新页面dom结构的最小路径，将改变更新到
  dom上面，完成更新

+ beforeDestroy,destroyed :实例的销毁，vue实例还是存在的，只是解绑了事件的监听、还有watcher对象数据
  与view的绑定，即数据驱动

## 4.2 vuejs生命周期的演示

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>01_vue入门</title>
    <script src="js/vuejs-2.5.16.js"></script>

</head>
<body>
<div id="app">
    <div id="msg">{{message}}</div>
</div>

<script>
    var vue =  new Vue({
        //表示当前vue对象接管了div区域
        el: '#app',
        //定义数据
        data: {
            message: 'hello word',
        },
        methods:{
            showMsg(msg, obj) {
                console.log(msg);//钩子函数的名称,obj就是vue对象
                console.log("data:" + obj.message);
                console.log("el元素:" + obj.$el);
                console.log("元素的内容:" + document.getElementById("app").innerHTML);
            }
        },
        //beforeCreate:vue对象还没有实例化出来，vue对象中的数据模型还未创建出来
        beforeCreate() {
            this.showMsg('---beforeCreate---', this);
        },
        //created ：数据已经绑定到了对象实例，但是还没有挂载对象
        created() {
            // created钩子函数执行的时候，vue对象已经创建出来了，vue对象中的数据模型已经创建出来了
            // 所以我们可以发送异步请求给服务器获取数据并且赋值给数据模型
            this.message = "你好世界"
            this.showMsg('---created---', this);
        },
        //beforeMount: 模板已经编译好了，根据数据和模板已经生成了对应的元素对象，将数据对象关联到了对象的
        beforeMount() {
            this.showMsg('---beforeMount---', this);
        },
        //mounted:将el的内容挂载到了el，相当于我们在jquery执行了(el).html(el)
        //生成页面上真正的dom，上面我们就会发现dom的元素和我们el的元素是一致的。在此之后，我们能够用方法来获取到el元素下的dom对象，并进行各种操作当我们的data发生改变时，会调用beforeUpdate和updated方法
        mounted() {
            //在mounted里面可以获取视图上的数据
            this.showMsg('---mounted---', this);
        }
    });
</script>
</body>
</html>
```

+ 结果

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/4.png)

小结

1.  created()钩子函数中，数据模型就可以赋值了，那么我们可以在这个钩子函数中发送异步请求，获取响应数据然后赋值给数据模型(重点)
2. mounted()钩子函数中，视图上就已经绑定了数据模型，那么我们就可以获取视图的内容

# 5. VueJS的Ajax

## 5.1 vue-resource(了解) 

​	vue-resource是Vue.js的插件提供了使用XMLHttpRequest或JSONP进行Web请求和处理响应的服务。 当vue更新到2.0之后，作者就宣告不再对vue-resource更新，而是推荐的axios，在这里大家了解一下vue-resource就可以。

vue-resource的github: [https://github.com/pagekit/vue-resource](https://github.com/pagekit/vue-resource)

+ Get方式

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/5.png)

+ Post方式

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/6.png)

## 5.2 axios(重点)

### 5.2.1 什么是axios

Axios 是一个基于 promise 的 HTTP 库，可以用在浏览器和 node.js 中

注: Promise 对象用于表示一个异步操作的最终状态（完成或失败），以及其返回的值。

axios的github:https://github.com/axios/axios

中文说明: https://www.kancloud.cn/yunye/axios/234845

### 5.2.2 axios的语法

+ get请求

```js
// 为给定 ID 的 user 创建请求
axios.get('/user?id=12')
  .then(response=>{
    console.log(response);
  })
  .catch(error=>{
    console.log(error);
  });

// 可选地，上面的请求可以这样做
axios.get('/user', {
    params: {
      id: 12
    }
  })
  .then(response=>{
    console.log(response);
  })
  .catch(error=>{
    console.log(error);
  });
```

+ post请求

```js
axios.post('/user', {
    id: 12,
    username:"jay"
  })
  .then(response=>{
    console.log(response);
  })
  .catch((error=>{
    console.log(error);
  });
```

### 5.2.3 axios的使用

需求:使用axios发送异步请求给ServletDemo01,并在页面上输出内容

步骤:

1. 创建ServletDemo01
2. 把axios和vue导入项目 引入到页面
3. 使用get(), post() 请求

* html页面代码

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>使用axios发送异步请求</title>
    <script src="js/vuejs-2.5.16.js"></script>
    <script src="js/axios-0.18.0.js"></script>
</head>
<body>
    <div id="app">
        用户名:<span v-html="user.username"></span><br/>
        密码:<span v-html="user.password"></span><br/>
        昵称:<span v-html="user.nickname"></span><br/>
        <a href="javascript:;" @click="fn1()">使用axios发送异步的get请求</a>

        <br/>
        <a href="javascript:;" @click="fn2()">使用axios发送异步的post请求</a>
    </div>
    <script>
        var vue = new Vue({
           el:"#app",
           data:{
             user:{}
           },
           methods:{
                //使用axios发送异步的get请求
                fn1(){
                    /*axios.get("demo01?username=aobama&password=123&nickname=圣枪游侠").then(response=>{
                        //response就是http协议中的响应:包含行、头、体，我们要获取的是响应体的数据，其实就是response.data
                        console.log(response.data)
                        //处理响应数据，response就是服务器端的响应数据,将响应数据中的json赋值给user
                        this.user = response.data
                    }).catch(error=>{
                        //处理请求失败,error就是服务器的异常信息
                        console.log(error)
                    })*/

                    //另外一种get请求携带参数的方式
                    axios.get("demo01",{
                        params:{
                            username:"奥巴马",
                            password:"1234567",
                            nickname:"圣枪游侠"
                        }
                    }).then(response=>{
                        //response就是http协议中的响应:包含行、头、体，我们要获取的是响应体的数据，其实就是response.data
                        console.log(response.data)
                        //处理响应数据，response就是服务器端的响应数据,将响应数据中的json赋值给user
                        this.user = response.data
                    }).catch(error=>{
                        //处理请求失败,error就是服务器的异常信息
                        console.log(error)
                    })
                },
                fn2(){
                    //使用axios发送异步的post请求
                    //post请求也可以在?后面携带参数
                    /*axios.post("demo01?username=周杰棍&password=666666&nickname=周杰伦").then(response=>{
                        this.user = response.data
                    })*/

                    //使用axios发送异步的post请求，并且携带json类型的参数(在请求体中)
                    axios.post("demo01",{username:"周杰伦",password:"123456",nickname:"周杰棍"}).then(response=>{
                        this.user = response.data
                    })
                }
           }
        });
    </script>
</body>
</html>
```

* ServletDemo01的代码

get

```java
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");

        //向客户端响应数据
        User user = new User(username, password, nickname);

//        User user = JSON.parseObject(request, User.class);
//
        System.out.println(user);
        //int num = 10/0;

        //将user转换成json字符串，响应给客户端
        String jsonStr = JSON.toJSONString(user);
        response.getWriter().write(jsonStr);

//        JsonUtils.printResult(response,user);
    }
```

post

```java
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求参数
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        String nickname = request.getParameter("nickname");
//
//        //向客户端响应数据
//        User user = new User(username, password, nickname);

        User user = JSON.parseObject(request.getInputStream(), User.class);
//
        System.out.println(user);
        //int num = 10/0;

//        //将user转换成json字符串，响应给客户端
//        String jsonStr = JSON.toJSONString(user);
//        response.getWriter().write(jsonStr);

        response.setContentType("application/json;charset=utf-8");
        JSON.writeJSONString(response.getWriter(),user);
    }
```

## 5.3 小结

1. vue的get方式携带请求参数，以及vue的post方式通过url携带请求参数，实际上携带给服务器的参数的格式是"name=value&name=value&name=value", 服务器接收到这种格式的请求参数的时候，可以使用request的getParameter(name)或者getParameterValues(name)或者getParameterMap()方法获取请求参数
2. vue的post方式通过json格式携带请求参数，那么提交给服务器的参数的格式是{name:value,name:value},服务器就无法通过以前的getParameter(name)等方法获取请求参数。那么服务器要通过解析json获取，我们直接使用JsonUtils工具类中的parseJson2Object(request,Class)方法就行了

# 6. 自定义组件

- 学完了 Element 组件后，我们会发现组件其实就是自定义的标签。例如 就是对的封装。 

- 本质上，组件是带有一个名字且可复用的 Vue 实例，我们完全可以自己定义。

- 定义格式

  ```tex
  Vue.component(组件名称, {
   props:组件的属性,
   data: 组件的数据函数,
   template: 组件解析的标签模板
  })
  ```

- **代码实现**

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>自定义组件</title>
      <script src="vue/vue.js"></script>
  </head>
  <body>
      <div id="div">
          <my-button>我的按钮</my-button>
      </div>
  </body>
  <script>
      Vue.component("my-button",{
          // 属性
          props:["style"],
          // 数据函数
          data: function(){
              return{
                  msg:"我的按钮"
              }
          },
          //解析标签模板
          template:"<button style='color:red'>{{msg}}</button>"
      });
  
      new Vue({
          el:"#div"
      });
  </script>
  </html>
  ```

# 7. Element 基本使用

## 7.1 Element介绍

- Element：网站快速成型工具。是饿了么公司前端开发团队提供的一套基于Vue的网站组件库。

- 使用Element前提必须要有Vue。

- 组件：组成网页的部件，例如超链接、按钮、图片、表格等等~

- Element官网：https://element.eleme.cn/#/zh-CN

- Element 提供的按钮

  ![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/7.png)

## 7.2 Element快速入门

- **开发步骤**

  1. 下载 Element 核心库。
  2. 引入 Element 样式文件。
  3. 引入 Vue 核心 js 文件。
  4. 引入 Element 核心 js 文件。
  5. 编写按钮标签。
  6. 通过 Vue 核心对象加载元素。

- **代码实现**

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>快速入门</title>
      <link rel="stylesheet" href="element-ui/lib/theme-chalk/index.css">
      <script src="js/vue.js"></script>
      <script src="element-ui/lib/index.js"></script>
  </head>
  <body>
      <button>我是按钮</button>
      <br>
      <div id="div">
          <el-row>
              <el-button>默认按钮</el-button>
              <el-button type="primary">主要按钮</el-button>
              <el-button type="success">成功按钮</el-button>
              <el-button type="info">信息按钮</el-button>
              <el-button type="warning">警告按钮</el-button>
              <el-button type="danger">危险按钮</el-button>
            </el-row>
            <br>
            <el-row>
              <el-button plain>朴素按钮</el-button>
              <el-button type="primary" plain>主要按钮</el-button>
              <el-button type="success" plain>成功按钮</el-button>
              <el-button type="info" plain>信息按钮</el-button>
              <el-button type="warning" plain>警告按钮</el-button>
              <el-button type="danger" plain>危险按钮</el-button>
            </el-row>
            <br>
            <el-row>
              <el-button round>圆角按钮</el-button>
              <el-button type="primary" round>主要按钮</el-button>
              <el-button type="success" round>成功按钮</el-button>
              <el-button type="info" round>信息按钮</el-button>
              <el-button type="warning" round>警告按钮</el-button>
              <el-button type="danger" round>危险按钮</el-button>
            </el-row>
            <br>
            <el-row>
              <el-button icon="el-icon-search" circle></el-button>
              <el-button type="primary" icon="el-icon-edit" circle></el-button>
              <el-button type="success" icon="el-icon-check" circle></el-button>
              <el-button type="info" icon="el-icon-message" circle></el-button>
              <el-button type="warning" icon="el-icon-star-off" circle></el-button>
              <el-button type="danger" icon="el-icon-delete" circle></el-button>
            </el-row>
      </div>
  </body>
  <script>
      new Vue({
          el:"#div"
      });
  </script>
  </html>
  ```

## 7.3 基础布局

将页面分成最多 24 个部分，自由切分。

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/8.png)

- **代码实现**

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>基础布局</title>
      <link rel="stylesheet" href="element-ui/lib/theme-chalk/index.css">
      <script src="js/vue.js"></script>
      <script src="element-ui/lib/index.js"></script>
      <style>
          .el-row {
              /* 行距为20px */
              margin-bottom: 20px;
          }
          .bg-purple-dark {
              background: red;
          }
          .bg-purple {
              background: blue;
          }
          .bg-purple-light {
              background: green;
          }
          .grid-content {
              /* 边框圆润度 */
              border-radius: 4px;
              /* 行高为36px */
              min-height: 36px;
          }
        </style>
  </head>
  <body>
      <div id="div">
          <el-row>
              <el-col :span="24"><div class="grid-content bg-purple-dark"></div></el-col>
            </el-row>
            <el-row>
              <el-col :span="12"><div class="grid-content bg-purple"></div></el-col>
              <el-col :span="12"><div class="grid-content bg-purple-light"></div></el-col>
            </el-row>
            <el-row>
              <el-col :span="8"><div class="grid-content bg-purple"></div></el-col>
              <el-col :span="8"><div class="grid-content bg-purple-light"></div></el-col>
              <el-col :span="8"><div class="grid-content bg-purple"></div></el-col>
            </el-row>
            <el-row>
              <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
              <el-col :span="6"><div class="grid-content bg-purple-light"></div></el-col>
              <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
              <el-col :span="6"><div class="grid-content bg-purple-light"></div></el-col>
            </el-row>
            <el-row>
              <el-col :span="4"><div class="grid-content bg-purple"></div></el-col>
              <el-col :span="4"><div class="grid-content bg-purple-light"></div></el-col>
              <el-col :span="4"><div class="grid-content bg-purple"></div></el-col>
              <el-col :span="4"><div class="grid-content bg-purple-light"></div></el-col>
              <el-col :span="4"><div class="grid-content bg-purple"></div></el-col>
              <el-col :span="4"><div class="grid-content bg-purple-light"></div></el-col>
            </el-row>
      </div>
  </body>
  <script>
      new Vue({
          el:"#div"
      });
  </script>
  </html>
  ```

## 7.4 容器布局

将页面分成头部区域、侧边栏区域、主区域、底部区域。

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/9.png)

- **代码实现**

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>容器布局</title>
      <link rel="stylesheet" href="element-ui/lib/theme-chalk/index.css">
      <script src="js/vue.js"></script>
      <script src="element-ui/lib/index.js"></script>
      <style>
          .el-header, .el-footer {
              background-color: #d18e66;
              color: #333;
              text-align: center;
              height: 100px;
          }
          .el-aside {
              background-color: #55e658;
              color: #333;
              text-align: center;
              height: 580px;
          }
          .el-main {
              background-color: #5fb1f3;
              color: #333;
              text-align: center;
              height: 520px;
          }
      </style>
  </head>
  <body>
      <div id="div">
          <el-container>
              <el-header>头部区域</el-header>
              <el-container>
                <el-aside width="200px">侧边栏区域</el-aside>
                <el-container>
                  <el-main>主区域</el-main>
                  <el-footer>底部区域</el-footer>
                </el-container>
              </el-container>
            </el-container>
      </div>
  </body>
  <script>
      new Vue({
          el:"#div"
      });
  </script>
  </html>
  ```

## 7.5 表单组件

由输入框、下拉列表、单选框、多选框等控件组成，用以收集、校验、提交数据。

- **代码实现**

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>表单组件</title>
      <link rel="stylesheet" href="element-ui/lib/theme-chalk/index.css">
      <script src="js/vue.js"></script>
      <script src="element-ui/lib/index.js"></script>
  </head>
  <body>
      <div id="div">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
              <el-form-item label="活动名称" prop="name">
                <el-input v-model="ruleForm.name"></el-input>
              </el-form-item>
              <el-form-item label="活动区域" prop="region">
                <el-select v-model="ruleForm.region" placeholder="请选择活动区域">
                  <el-option label="区域一" value="shanghai"></el-option>
                  <el-option label="区域二" value="beijing"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="活动时间" required>
                <el-col :span="11">
                  <el-form-item prop="date1">
                    <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.date1" style="width: 100%;"></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col class="line" :span="2">-</el-col>
                <el-col :span="11">
                  <el-form-item prop="date2">
                    <el-time-picker placeholder="选择时间" v-model="ruleForm.date2" style="width: 100%;"></el-time-picker>
                  </el-form-item>
                </el-col>
              </el-form-item>
              <el-form-item label="即时配送" prop="delivery">
                <el-switch v-model="ruleForm.delivery"></el-switch>
              </el-form-item>
              <el-form-item label="活动性质" prop="type">
                <el-checkbox-group v-model="ruleForm.type">
                  <el-checkbox label="美食/餐厅线上活动" name="type"></el-checkbox>
                  <el-checkbox label="地推活动" name="type"></el-checkbox>
                  <el-checkbox label="线下主题活动" name="type"></el-checkbox>
                  <el-checkbox label="单纯品牌曝光" name="type"></el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item label="特殊资源" prop="resource">
                <el-radio-group v-model="ruleForm.resource">
                  <el-radio label="线上品牌商赞助"></el-radio>
                  <el-radio label="线下场地免费"></el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="活动形式" prop="desc">
                <el-input type="textarea" v-model="ruleForm.desc"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
              </el-form-item>
            </el-form>
      </div>
  </body>
  <script>
      new Vue({
          el:"#div",
          data:{
              ruleForm: {
                  name: '',
                  region: '',
                  date1: '',
                  date2: '',
                  delivery: false,
                  type: [],
                  resource: '',
                  desc: ''
                  },
          rules: {
            name: [
              { required: true, message: '请输入活动名称', trigger: 'blur' },
              { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
            ],
            region: [
              { required: true, message: '请选择活动区域', trigger: 'change' }
            ],
            date1: [
              { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
            ],
            date2: [
              { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
            ],
            type: [
              { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
            ],
            resource: [
              { required: true, message: '请选择活动资源', trigger: 'change' }
            ],
            desc: [
              { required: true, message: '请填写活动形式', trigger: 'blur' }
            ]
          }
          },
          methods:{
              submitForm(formName) {
                  this.$refs[formName].validate((valid) => {
                  if (valid) {
                      alert('submit!');
                  } else {
                      console.log('error submit!!');
                      return false;
                  }
                  });
              },
              resetForm(formName) {
                  this.$refs[formName].resetFields();
              }
          }
      });
  </script>
  </html>
  ```

## 7.6 表格组件

用于展示多条结构类似的数据，可对数据进行编辑、删除或其他自定义操作。

- **代码实现**

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>表格组件</title>
      <link rel="stylesheet" href="element-ui/lib/theme-chalk/index.css">
      <script src="js/vue.js"></script>
      <script src="element-ui/lib/index.js"></script>
  </head>
  <body>
      <div id="div">
          <template>
              <el-table
                :data="tableData"
                style="width: 100%">
                <el-table-column
                  prop="date"
                  label="日期"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="name"
                  label="姓名"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="address"
                  label="地址">
                </el-table-column>
  
                <el-table-column
                  label="操作"
                  width="180">
                  <el-button type="warning">编辑</el-button>
                  <el-button type="danger">删除</el-button>
                </el-table-column>
              </el-table>
            </template>
      </div>
  </body>
  <script>
      new Vue({
          el:"#div",
          data:{
              tableData: [{
                  date: '2016-05-02',
                  name: '王小虎',
                  address: '上海市普陀区金沙江路 1518 弄'
              }, {
                  date: '2016-05-04',
                  name: '王小虎',
                  address: '上海市普陀区金沙江路 1517 弄'
              }, {
                  date: '2016-05-01',
                  name: '王小虎',
                  address: '上海市普陀区金沙江路 1519 弄'
              }, {
                  date: '2016-05-03',
                  name: '王小虎',
                  address: '上海市普陀区金沙江路 1516 弄'
              }]
          }
      });
  </script>
  </html>
  ```

## 7.7 顶部导航栏组件

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/10.png)

- **代码实现**

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>顶部导航栏</title>
      <link rel="stylesheet" href="element-ui/lib/theme-chalk/index.css">
      <script src="js/vue.js"></script>
      <script src="element-ui/lib/index.js"></script>
  </head>
  <body>
      <div id="div">
        <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">
        <el-menu-item index="1">处理中心</el-menu-item>
        <el-submenu index="2">
          <template slot="title">我的工作台</template>
          <el-menu-item index="2-1">选项1</el-menu-item>
          <el-menu-item index="2-2">选项2</el-menu-item>
          <el-menu-item index="2-3">选项3</el-menu-item>
          <el-submenu index="2-4">
            <template slot="title">选项4</template>
            <el-menu-item index="2-4-1">选项1</el-menu-item>
            <el-menu-item index="2-4-2">选项2</el-menu-item>
            <el-menu-item index="2-4-3">选项3</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-menu-item index="3" disabled>消息中心</el-menu-item>
        <el-menu-item index="4"><a href="https://www.ele.me" target="_blank">订单管理</a></el-menu-item>
      </el-menu>
      </div>
  </body>
  <script>
      new Vue({
          el:"#div"
      });
  </script>
  </html>
  ```

## 7.8 侧边导航栏组件

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.11/pics/11.png)

- **代码实现**

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>侧边导航栏</title>
      <link rel="stylesheet" href="element-ui/lib/theme-chalk/index.css">
      <script src="js/vue.js"></script>
      <script src="element-ui/lib/index.js"></script>
  </head>
  <body>
      <div id="div">
        <el-col :span="6">
          <el-menu
            default-active="2"
            class="el-menu-vertical-demo"
            @open="handleOpen"
            @close="handleClose"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b">
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>导航一</span>
              </template>
              <el-menu-item-group>
                <template slot="title">分组一</template>
                <el-menu-item index="1-1">选项1</el-menu-item>
                <el-menu-item index="1-2">选项2</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="分组2">
                <el-menu-item index="1-3">选项3</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="1-4">
                <template slot="title">选项4</template>
                <el-menu-item index="1-4-1">选项1</el-menu-item>
              </el-submenu>
            </el-submenu>
            <el-menu-item index="2">
              <i class="el-icon-menu"></i>
              <span slot="title">导航二</span>
            </el-menu-item>
            <el-menu-item index="3" disabled>
              <i class="el-icon-document"></i>
              <span slot="title">导航三</span>
            </el-menu-item>
            <el-menu-item index="4">
              <i class="el-icon-setting"></i>
              <span slot="title">导航四</span>
            </el-menu-item>
          </el-menu>
        </el-col>
      </div>
  </body>
  <script>
      new Vue({
          el:"#div"
      });
  </script>
  </html>
  ```

## 7.9 小结

- Element：网站快速成型工具。是一套为开发者、设计师、产品经理准备的基于Vue的桌面端组件库。
- 使用Element前提必须要有Vue。
- 使用步骤
  1.下载Element核心库。
  2.引入Element样式文件。
  3.引入Vue核心js文件。
  4.引入Element核心js文件。
  5.借助常用组件编写网页。
- 常用组件
  网页基本组成部分，布局、按钮、表格、表单等等~~~
  常用组件不需要记住，只需要在Element官网中复制使用即可。

# 8. 综合案例

## 案例一:显示所有联系人案例

### 一，案例需求

![img](img/tu_1.png)

+ 查询数据库里面所有的联系人, 展示在页面

### 二, 思路分析

1. 在list.html导入vue和axios,创建vue实例
2. 在钩子函数created里面, 使用axios请求LinkManServlet

```
axios.get('linkMan?method=findAll').then(response=>{
   //进行数据的赋值和绑定,展示
})
```

3. 在LinkManServlet的findAll()方法里面, 封装Result 响应json



### 三, 代码实现

#### 1.准备工作

+ 数据库的创建

```mysql
CREATE TABLE linkman (
  id int primary key auto_increment,
  name varchar(50),
  sex varchar(50),
  age int,
  address varchar(50),
  qq varchar(50),
  email varchar(50)
);

INSERT INTO `linkman`  (`id`, `name`, `sex`, `age`, `address`, `qq`, `email`) VALUES
(null, '张三', '男', 11, '广东', '766335435', '766335435@qq.com'),
(null, '李四', '男', 12, '广东', '243424242', '243424242@qq.com'),
(null, '王五', '女', 13, '广东', '474574574', '474574574@qq.com'),
(null, '赵六', '女', 18, '广东', '77777777', '77777777@qq.com'),
(null, '钱七', '女', 15, '湖南', '412132145', '412132145@qq.com'),
(null, '王八', '男', 25, '广西', '412132775', '412132995@qq.com');
```

+ JavaBean的创建

```java
public class LinkMan implements Serializable{
    /**
     *   id int primary key auto_increment,
     name varchar(50),
     sex varchar(50),
     age int,
     address varchar(50),
     qq varchar(50),
     email varchar(50)
     */
    private int id;
    private String name;
    private String sex;
    private int age;
    private String address;
    private String qq;
    private String email;
    //自己补充get和set方法
}
```

+ jar包
+ 工具类
+ 配置文件
+ 页面

#### 2.代码

+ list.html

```html
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN" xmlns:v-bind="http://www.w3.org/1999/xhtml">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap模板</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <style type="text/css">
        td, th {
            text-align: center;
        }
    </style>

    <!--引入vue和axios-->
    <script src="js/vuejs-2.5.16.js"></script>
    <script src="js/axios-0.18.0.js"></script>
</head>
<body>
<div class="container" id="app">
    <h3 style="text-align: center">显示所有用户</h3>
    <table border="1" class="table table-bordered table-hover">
        <tr class="success">
            <th>编号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>籍贯</th>
            <th>QQ</th>
            <th>邮箱</th>
            <th>操作</th>
        </tr>

        <!--v-for绑定linkmanList-->
        <tr v-for="(linkman,index) in linkmanList">
            <td v-text="index+1"></td>
            <td v-text="linkman.name"></td>
            <td v-text="linkman.sex"></td>
            <td v-text="linkman.age"></td>
            <td v-text="linkman.address"></td>
            <td v-text="linkman.qq"></td>
            <td v-text="linkman.email"></td>
            <!--绑定属性-->
            <td><a class="btn btn-default btn-sm" :href="'/update.html?id='+linkman.id">修改</a>&nbsp;
                <!--给a标签绑定点击事件-->
                <a class="btn btn-default btn-sm" href="javascript:;">删除</a></td>
        </tr>
        <tr>
            <td colspan="8" align="center">
                <a class="btn btn-primary" href="/add.html">添加用户</a>
            </td>
        </tr>
    </table>
</div>

<script>
    var vue = new Vue({
       el:"#app",
       data:{
            linkmanList:[]
       },
       methods:{
            findAll(){
                //发送异步请求
                axios.get("linkman?action=findAll").then(response=>{
                    //response.data就是响应体的内容(json)
                    if (response.data.flag) {
                        //获取成功
                        this.linkmanList = response.data.data
                    }else {
                        //获取失败
                        alert("获取联系人列表失败")
                    }
                })
            }
       },
        //钩子函数
       created(){
            this.findAll()
       }
    });
</script>
</body>
</html>
```

+ LinkManServlet

```java
private void findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 调用业务层的方法，获取所有联系人信息
        List<LinkMan> linkManList = linkManService.findAll();
        //2. 将查询到的数据封装到ResultBean对象
        resultBean.setData(linkManList);
    } catch (Exception e) {
        e.printStackTrace();
        //服务器异常
        resultBean.setFlag(false);
        resultBean.setErrorMsg("获取联系人列表失败");
    }

    //使用JsonUtil将resultBean对象转换成json字符串输出到客户端
    JsonUtils.printResult(response,resultBean);
}
```

* LinkManService

```java
public List<LinkMan> findAll() throws SQLException {
    return linkManDao.findAll();
}
```

* LinkManDao

```java
public List<LinkMan> findAll() throws SQLException {
    String sql = "select * from linkman";
    List<LinkMan> linkManList = queryRunner.query(sql, new BeanListHandler<>(LinkMan.class));
    return linkManList;
}
```



### 四,小结

1. 在list.html里面, 导入vue和axios 创建vue实例
2. 在钩子函数里面 created()里面,使用axios请求LinkManServlet获得数据 进行赋值,绑定
3. 在LinkManServlet的findAll()方法里面, 封装Result 响应





## 案例二:添加联系人

### 一,案例需求

1. 点击添加联系人跳转添加联系人页面

   ![1571568952279](img/1571568952279.png)

2. 在添加联系人页面，点击提交按钮,把数据提交到服务器,保存到数据库

   ![1571568994445](img/1571568994445.png)

3. 在添加完成，可以查看到新建的联系人信息

   ![1571569023876](img/1571569023876.png) 



### 二,思路分析

1. 在add.html 导入vue,axios ,创建vue实例

2. 创建linkMan:{} ,和表单进行绑定 

3. 给提交设置一个点击事件,创建函数

4. 在这个函数里面

   ```
   //1.发送ajax请求服务器 携带linkMan
   //2.获得响应的结果,  判断是否增加成功
   ```

   

   

### 三,代码实现

+ add.html

```html
<!-- HTML5文档-->
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>添加用户</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <script src="js/axios-0.18.0.js"></script>
    <script src="js/vuejs-2.5.16.js"></script>
</head>
<body>
<div class="container" id="app">
    <center><h3>添加用户页面</h3></center>
    <!--屏蔽同步提交-->
    <form action="#" method="post" onsubmit="return false">
        <div class="form-group">
            <label for="name">姓名：</label>
            <input type="text" v-model="linkman.name" class="form-control" id="name" name="name" placeholder="请输入姓名">
        </div>

        <div class="form-group">
            <label>性别：</label>
            <input type="radio" v-model="linkman.sex" name="sex" value="男" checked="checked"/>男
            <input type="radio" v-model="linkman.sex" name="sex" value="女"/>女
        </div>

        <div class="form-group">
            <label for="age">年龄：</label>
            <input type="text" v-model="linkman.age" class="form-control" id="age" name="age" placeholder="请输入年龄">
        </div>

        <div class="form-group">
            <label for="address">籍贯：</label>
            <select name="address" v-model="linkman.address" id="address" class="form-control">
                <option value="广东">广东</option>
                <option value="广西">广西</option>
                <option value="湖南">湖南</option>
            </select>
        </div>

        <div class="form-group">
            <label for="qq">QQ：</label>
            <input type="text" v-model="linkman.qq" id="qq" class="form-control" name="qq" placeholder="请输入QQ号码"/>
        </div>

        <div class="form-group">
            <label for="email">Email：</label>
            <input type="text" v-model="linkman.email" id="email" class="form-control" name="email" placeholder="请输入邮箱地址"/>
        </div>

        <div class="form-group" style="text-align: center">
            <!--绑定点击事件-->
            <input class="btn btn-primary" type="submit" value="提交" @click="addLinkMan()"/>
            <input class="btn btn-default" type="reset" value="重置" />
            <input class="btn btn-default" type="button" value="返回" />
        </div>
    </form>
</div>
<script>
    var vue = new Vue({
       el:"#app",
       data:{
           linkman:{}
       },
       methods:{
           addLinkMan(){
               //向服务器发送异步请求，携带联系人的信息
               axios.post("linkman?action=add",this.linkman).then(response=>{
                   //获取是否添加成功
                   if (response.data.flag) {
                       //添加成功,跳转到list.html
                       location.href = "list.html"
                   }else {
                       //添加失败
                       alert("添加失败")
                   }
               })
           }
       }
    });
</script>
</body>
</html>
```

+ LinkManServlet

```java
/**
     * 添加联系人
     * @param request
     * @param response
     * @throws IOException
     */
private void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取客户端提交的联系人的信息,使用JsonUtils获取
        LinkMan linkMan = JsonUtils.parseJSON2Object(request, LinkMan.class);
        //2. 调用业务层的方法添加联系人信息
        linkManService.add(linkMan);
    } catch (Exception e) {
        e.printStackTrace();

        resultBean.setFlag(false);
        resultBean.setErrorMsg("添加联系人失败");
    }

    //将resultBean转换成json字符串输出到客户端
    JsonUtils.printResult(response,resultBean);
}
```

* LinkManService

```java
public void add(LinkMan linkMan) throws SQLException {
    linkManDao.add(linkMan);
}
```

* LinkManDao

```java
public void add(LinkMan linkMan) throws SQLException {
    String sql = "insert into linkman values (null,?,?,?,?,?,?)";
    queryRunner.update(sql,linkMan.getName(),linkMan.getSex(),linkMan.getAge(),linkMan.getAddress(),linkMan.getQq(),linkMan.getEmail());
}
```



### 四,小结

1. 在add.html里面 创建linkMan:{}, 和表单进行绑定
2. 点击提交按钮, 把linkMan提交到LinkManServlet



## 案例三:删除联系人

### 一,案例需求

![1571569201344](img/1571569201344.png)

​	点击确定删除之后, 再重新查询所有全部展示,



### 二,思路分析

1. 点击了确定, 发送axios请求LinkManServlet 携带id
2. 获得响应的结果 判断

### 三,代码实现

+ list.html

```js
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap模板</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <style type="text/css">
        td, th {
            text-align: center;
        }
    </style>
    <script src="js/vuejs-2.5.16.js"></script>
    <script src="js/axios-0.18.0.js"></script>
</head>
<body>
<div class="container" id="app">
    <h3 style="text-align: center">显示所有用户</h3>
    <table border="1" class="table table-bordered table-hover">
        <tr class="success">
            <th>编号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>籍贯</th>
            <th>QQ</th>
            <th>邮箱</th>
            <th>操作</th>
        </tr>

        <tr v-for="(linkman,index) in linkmanList">
            <td v-html="index+1"></td>
            <td v-html="linkman.name"></td>
            <td v-html="linkman.sex"></td>
            <td v-html="linkman.age"></td>
            <td v-html="linkman.address"></td>
            <td v-html="linkman.qq"></td>
            <td v-html="linkman.email"></td>
            <td>
                <a class="btn btn-default btn-sm" :href="'update.html?id='+linkman.id">修改</a>&nbsp;
                <a class="btn btn-default btn-sm" href="javascript:;" @click="deleteLinkMan(linkman.name,linkman.id)">删除</a>
            </td>
        </tr>
        <tr>
            <td colspan="8" align="center"><a class="btn btn-primary" href="add.html">添加用户</a></td>
        </tr>
    </table>
</div>
<script>
    var vue = new Vue({
       el:"#app",
       data:{
           linkmanList:[]
       },
       methods:{
           findAll(){
               //发送异步请求给LinkManServlet，进行查询所有
               axios.post("linkman?action=findAll").then(response=>{
                    //拿到服务器的响应数据，赋值给linkmanList
                   this.linkmanList = response.data.data
               })
           },
           deleteLinkMan(name,id){
               var b = confirm("你确定要删除"+name+"吗?");
               if (b) {
                   //发送异步请求给LinkManServlet进行根据id删除
                  axios.get("linkman?action=delete&id="+id).then(response=>{
                      if (response.data.flag) {
                          location.href = "list.html"
                      }else {
                          alert("删除失败")
                      }
                  })
               }
           }
       },
       created(){
            //在这个钩子函数中调用findAll()方法
           this.findAll()
       }
    });
</script>
</body>
</html>
```



+ LinkManServlet

```java
/**
     * 删除联系人
     * @param request
     * @param response
     */
private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取要删除的联系人的id
        Integer id = Integer.valueOf(request.getParameter("id"));
        //2. 调用业务层的方法删除联系人
        linkManService.deleteById(id);
    } catch (Exception e) {
        e.printStackTrace();
        resultBean.setFlag(false);
        resultBean.setErrorMsg("删除失败");
    }

    //将resultBean对象转换成json输出给客户端
    JsonUtils.printResult(response,resultBean);
}
```

* LinkManService

```java
public void deleteById(Integer id) throws SQLException {
	linkManDao.deleteById(id);
}
```

* LinkManDao

```java
public void deleteById(Integer id) throws SQLException {
    String sql = "delete from linkman where id=?";
    queryRunner.update(sql,id);
}
```



### 四,小结

1. 点击`删除` 弹出确定框

2. 在确定框里面点击了确定 请求LinkManServlet 携带method=delete&id=xxx

3. 在LinkManServlet的dlete方法里面

   ```
   //1.获得id
   //2.调用业务删除
   //3.响应
   ```

   

## 案例四:更新联系人

### 一,案例需求

![1571569357806](img/1571569357806.png) 

![1571569369845](img/1571569369845.png) 

### 二,思路分析

### 三,代码实现

+ list.html

![1571207832129](img/1571207832129.png) 

+ update.html

```html
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
    <head>
    	<base href="<%=basePath%>"/>
        <!-- 指定字符集 -->
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>修改用户</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery-2.1.0.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/vuejs-2.5.16.js"></script>
        <script src="js/axios-0.18.0.js"></script>
        <script src="js/getParameter.js"></script>
    </head>
    <body>
        <div class="container" style="width: 400px;" id="app">
        <h3 style="text-align: center;">修改用户</h3>
        <form action="/day05/update" method="post" onsubmit="return false">
              <input type="hidden" name="id" v-model="linkman.id">
              <div class="form-group">
                <label for="name">姓名：</label>
                <input type="text" class="form-control" v-model="linkman.name" id="name" name="name"  placeholder="请输入姓名" />
              </div>

              <div class="form-group">
                <label>性别：</label>
                  <input type="radio" v-model="linkman.sex" name="sex" value="男"  />男
                    <input type="radio" v-model="linkman.sex" name="sex" value="女"  />女
              </div>

              <div class="form-group">
                <label for="age">年龄：</label>
                <input type="text" v-model="linkman.age" class="form-control" id="age"  name="age" placeholder="请输入年龄" />
              </div>

              <div class="form-group">
                <label for="address">籍贯：</label>
                 <select name="address" v-model="linkman.address" id="address" class="form-control" >
                    <option value="广东">广东</option>
                    <option value="广西">广西</option>
                    <option value="湖南">湖南</option>
                </select>
              </div>

              <div class="form-group">
                <label for="qq">QQ：</label>
                <input type="text" id="qq" v-model="linkman.qq" class="form-control" name="qq" placeholder="请输入QQ号码"/>
              </div>

              <div class="form-group">
                <label for="email">Email：</label>
                <input type="text" id="email" v-model="linkman.email" class="form-control" name="email" placeholder="请输入邮箱地址"/>
              </div>

                 <div class="form-group" style="text-align: center">
                    <input class="btn btn-primary" type="submit" value="提交" @click="updateLinkMan()"/>
                    <input class="btn btn-default" type="reset" value="重置" />
                    <input class="btn btn-default" type="button" value="返回"/>
                 </div>
        </form>
        </div>

    <script>
        var vue = new Vue({
           el:"#app",
           data:{
                linkman:{}
           },
           methods:{
                findById(){
                    //1. 获取到地址上携带过来的id
                    var id = parseInt(getParameter("id"))
                    //2. 发送异步请求给LinkManServlet，根据id查询联系人的信息
                    axios.get("linkman?action=findOne&id="+id).then(response=>{
                        this.linkman = response.data.data
                    })
                },
               updateLinkMan(){
                   var params = this.linkman
                   //发送异步请求给LinkManServlet，提交要修改的联系人的信息
                   axios.post("linkman?action=update",params).then(response=>{
                       if (response.data.flag) {
                           //修改成功
                           location.href = "list.html"
                       }else {
                           alert("修改失败")
                       }
                   })
               }
           },
           created(){
                this.findById()
           }
        });
    </script>
    </body>
</html>
```

+ LinkManServlet

```java
/**
     * 数据回显
     * @param request
     * @param response
     * @throws IOException
     */
private void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取联系人的id
        Integer id = Integer.valueOf(request.getParameter("id"));
        //2. 调用业务层的方法，根据id获取联系人信息
        LinkMan linkMan = linkManService.findById(id);
        //3. 将响应数据封装到resultBean对象
        resultBean.setData(linkMan);
    } catch (Exception e) {
        e.printStackTrace();

        resultBean.setFlag(false);
        resultBean.setErrorMsg("获取联系人失败");
    }

    //将resultBean对象转换成json输出给客户端
    JsonUtils.printResult(response,resultBean);
}

/**
     * 修改联系人
     * @param request
     * @param response
     */
public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 使用JsonUtils获取请求参数封装到LinkMan对象
        LinkMan linkMan = JsonUtils.parseJSON2Object(request, LinkMan.class);
        //2. 调用业务层的方法修改联系人
        linkManService.update(linkMan);
    } catch (Exception e) {
        e.printStackTrace();

        resultBean.setFlag(false);
        resultBean.setErrorMsg("修改失败");
    }

    //将resultBean对象转换成json输出给客户端
    JsonUtils.printResult(response,resultBean);
}
```

+ linkManService

```java
    public LinkMan findByUid(int uid) throws Exception {
        return linkManDao.findByUid(uid);
    }

    public void update(LinkMan linkMan) throws Exception {
        linkManDao.update(linkMan);
    }
```

+ linkManDao

````java
    public LinkMan findByUid(int uid) throws Exception {
        String sql = "SELECT * FROM linkman where id = ?";
        return queryRunner.query(sql,new BeanHandler<LinkMan>(LinkMan.class),uid);
    }

    public void update(LinkMan linkMan) throws Exception {
        String sql  ="UPDATE linkman SET name = ?,sex =?,age = ?,address = ?,qq =?,email = ? WHERE id = ?";
        Object[] params= {
                linkMan.getName(),
                linkMan.getSex(), linkMan.getAge(),
                linkMan.getAddress(), linkMan.getQq(),
                linkMan.getEmail(),linkMan.getId()
        };
        queryRunner.update(sql,params);

    }
````



### 四,小结



## 案例五:分页查看联系人

#### 代码

* index.html代码

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
        <a href="/list.html">用户列表</a><br/>
        <a href="/list_page.html?currentPage=1&pageSize=3">分页查看联系人</a>
    </body>
</html>
```

* list_page.html代码

```html
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap模板</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <style type="text/css">
        td, th {
            text-align: center;
        }
    </style>
    <script src="js/vuejs-2.5.16.js"></script>
    <script src="js/axios-0.18.0.js"></script>
    <script src="js/getParameter.js"></script>
</head>
<body>
<div class="container" id="app">
    <h3 style="text-align: center">显示所有用户</h3>
    <table border="1" class="table table-bordered table-hover">
        <tr class="success">
            <th>编号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>籍贯</th>
            <th>QQ</th>
            <th>邮箱</th>
            <th>操作</th>
        </tr>
        <tr v-for="(linkman,index) in pagination.list">
            <td v-html="index+1"></td>
            <td v-html="linkman.name"></td>
            <td v-html="linkman.sex"></td>
            <td v-html="linkman.age"></td>
            <td v-html="linkman.address"></td>
            <td v-html="linkman.qq"></td>
            <td v-html="linkman.email"></td>
            <td><a class="btn btn-default btn-sm" href="修改联系人.html">修改</a>&nbsp;<a class="btn btn-default btn-sm" href="修改联系人.html">删除</a></td>
        </tr>
        <tr>
            <td colspan="8" align="center">
				<ul class="pagination success">
					<li v-if="prev">
                        <a :href="'list_page.html?currentPage='+(pagination.currentPage-1)+'&pageSize='+pagination.pageSize" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>
                    </li>

					<li v-for="(element,index) in pagination.totalPage" :class="{active:pagination.currentPage == index+1}"><a :href="'list_page.html?currentPage='+(index+1)+'&pageSize='+pagination.pageSize" v-html="index+1"></a></li>
					<li v-if="next">
                        <a :href="'list_page.html?currentPage='+(pagination.currentPage+1)+'&pageSize='+pagination.pageSize" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>
                    </li>
				</ul>
            </td>
        </tr>
    </table>
</div>

<script>
    var vue = new Vue({
       el:"#app",
       data:{
            pagination:{},
            prev:true,
            next:true
       },
       methods:{
            findByPage(){
                //获取当前页数currentPage以及每页条数pageSize
                var currentPage = parseInt(getParameter("currentPage"));
                var pageSize = parseInt(getParameter("pageSize"));

                //发送异步请求给LinkManServlet进行分页查询
                axios.get("linkman?action=findByPage&currentPage="+currentPage+"&pageSize="+pageSize).then(response=>{
                    this.pagination = response.data.data

                    //怎么控制上一页和下一页是否显示
                    if(currentPage <= 1){
                        this.prev = false
                    }else {
                        this.prev = true
                    }

                    if(currentPage >= this.pagination.totalPage){
                        this.next = false
                    }else {
                        this.next = true
                    }
                })
            }
       },
       created(){
           this.findByPage()
       }
    });
</script>
</body>
</html>
```

* LinkManServlet代码

```java
/**
     * 分页查看联系人
     * @param request
     * @param response
     */
private void findByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取请求参数currentPage以及pageSize
        Long currentPage = Long.valueOf(request.getParameter("currentPage"));
        Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));
        //2. 调用业务层的方法，查询分页信息
        PageBean<LinkMan> pageBean = linkManService.findByPage(currentPage,pageSize);

        resultBean.setData(pageBean);
    } catch (Exception e) {
        e.printStackTrace();

        resultBean.setFlag(false);
        resultBean.setErrorMsg("分页查询失败");
    }

    //将resultBean对象转换成json输出给客户端
    JsonUtils.printResult(response,resultBean);
}
```

* LinkManService代码

```java
public PageBean<LinkMan> findByPage(Long currentPage, Integer pageSize) throws SQLException {
    //1. 创建PageBean对象
    PageBean<LinkMan> pageBean = new PageBean<>();
    //2. 设置pageBean的属性
    pageBean.setCurrentPage(currentPage);
    pageBean.setPageSize(pageSize);

    // 调用dao层的方法，获取总数据条数
    Long totalSize = linkManDao.findTotalSize();
    pageBean.setTotalSize(totalSize);

    //计算出总页数
    Long totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
    pageBean.setTotalPage(totalPage);

    //调用dao层的方法获取当前页的联系人集合
    List<LinkMan> linkManList = linkManDao.findPageList(currentPage,pageSize);
    pageBean.setList(linkManList);
    return pageBean;
}
```

* LinkManDao代码

```java
public Long findTotalSize() throws SQLException {
    String sql = "select count(*) from linkman";
    Long totalSize = (Long) queryRunner.query(sql, new ScalarHandler());
    return totalSize;
}

public List<LinkMan> findPageList(Long currentPage, Integer pageSize) throws SQLException {
    String sql = "select * from linkman limit ?,?";
    List<LinkMan> linkManList = queryRunner.query(sql, new BeanListHandler<>(LinkMan.class), (currentPage - 1) * pageSize, pageSize);
    return linkManList;
}
```



# 附录

## 1.键盘ascii码

[http://tool.oschina.net/commons?type=4](http://tool.oschina.net/commons?type=4)

![1552547539189](img/keycode.png)

