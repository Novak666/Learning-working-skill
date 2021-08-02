# day01

# 1. Vue是什么？

- **Vue (读音 /vjuː/，类似于 **view**) 是一套用于构建用户界面的渐进式框架**
- vue 的核心库只关注视图层，不仅易于上手，还便于与第三方库或既有项目整合

官网教程：https://cn.vuejs.org/v2/guide/syntax.html

# 2. 简单案例

使用Vue将helloworld渲染到页面上

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.01/img1/1.png)

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.01/img1/2.png)

# 3. 模板语法

+ 插值表达式
+ 指令
+ 事件绑定
+ 属性绑定
+ 样式绑定
+ 分支循环结构

# 4. 指令-数据绑定相关

- 本质就是自定义属性
- Vue中指定都是以v-开头

## 4.1 v-cloak

```
/*
  v-cloak指令的用法
  1、提供样式
    [v-cloak]{
      display: none;
    }
  2、在插值表达式所在的标签中添加v-cloak指令

  背后的原理：先通过样式隐藏内容，然后在内存中进行值的替换，替换好之后再显示最终的结果
*/
```

- 防止页面加载时出现闪烁问题

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
  <style type="text/css">
  [v-cloak]{
    display: none;
  }
  </style>
</head>
<body>
  <div id="app">
    <div v-cloak>{{msg}}</div>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      v-cloak指令的用法
      1、提供样式
        [v-cloak]{
          display: none;
        }
      2、在插值表达式所在的标签中添加v-cloak指令

      背后的原理：先通过样式隐藏内容，然后在内存中进行值的替换，替换好之后再显示最终的结果
    */
    var vm = new Vue({
      el: '#app',
      data: {
        msg: 'Hello Vue'
      }
    });
  </script>
</body>
</html>
```

## 4.2 v-text

- v-text指令用于将数据填充到标签中，作用于插值表达式类似，但是**没有闪动问题**
- 如果数据中有HTML标签会将html标签一并输出，不会解析
- 注意：此处为单向绑定，数据对象上的值改变，插值会发生变化；但是当插值发生变化并不会影响数据对象的值

## 4.3 v-html

- 用法和v-text 相似  但是他可以将HTML片段填充到标签中

- 可能有安全问题, 一般只在内部可信任内容上使用 `v-html`，**永不**用在用户提交的内容上

- 它与v-text区别在于v-text输出的是纯文本，浏览器不会对其再进行html解析，但v-html会将其当html标签解析后输出。


## 4.4 v-pre

- 显示原始信息跳过编译过程
- 跳过这个元素和它的子元素的编译过程。
- **一些静态的内容不需要编译加这个指令可以加快渲染**

4.2到4.4代码：

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
</head>
<body>
  <div id="app">
    <div>{{msg}}</div>
    <div v-text='msg'></div>
    <div v-html='msg1'></div>
    <div v-pre>{{msg}}</div>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      1、v-text指令用于将数据填充到标签中，作用于插值表达式类似，但是没有闪动问题
      2、v-html指令用于将HTML片段填充到标签中，但是可能有安全问题
      3、v-pre用于显示原始信息
    */
    var vm = new Vue({
      el: '#app',
      data: {
        msg: 'Hello Vue',
        msg1: '<h1>HTML</h1>'
      }
    });
  </script>
</body>
</html>
```

## 4.5 v-once

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.01/img1/3.png)

- 执行一次性的插值【当数据改变时，插值处的内容不会继续更新】节省性能

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
</head>
<body>
  <div id="app">
    <div>{{msg}}</div>
    <div v-once>{{info}}</div>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      v-once的应用场景：如果显示的信息后续不需要再修改，你们可以使用v-once，这样可以提高性能。
    */
    var vm = new Vue({
      el: '#app',
      data: {
        msg: 'Hello Vue',
        info: 'nihao'
      }
    });
  </script>
</body>
</html>
```

## 4.6 v-model

### 4.6.1 双向数据绑定

- 当数据发生变化的时候，视图也就发生变化(数据的响应式)
- 当视图发生变化的时候，数据也会跟着同步变化(比如用户改变表单)

- **v-model**是一个指令，<font color='red'>限制在 `<input>、<select>、<textarea>、components`中使用！！！！！！</font>

```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Document</title>
  </head>
  <body>
    <div id="app">
      <div>{{msg}}</div>
      <div>
        <input type="text" v-model='msg'>
      </div>
    </div>
    <script type="text/javascript" src="js/vue.js"></script>
    <script type="text/javascript">
      /*
        双向数据绑定
        1、从页面到数据
        2、从数据到页面
      */
      var vm = new Vue({
        el: '#app',
        data: {
          msg: 'Hello Vue'
        }
      });
    </script>
  </body>
  </html>
```

### 4.6.2 MVVM

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.01/img1/4.png)

- MVC 是后端的分层开发概念； MVVM是前端视图层的概念，主要关注于 视图层分离，也就是说：MVVM把前端的视图层，分为了 三部分 Model, View , VM ViewModel
- m   model
  - 数据层   Vue  中 数据层 都放在 data 里面
- v   view     视图
  - Vue  中  view      即 我们的HTML页面
- vm   （view-model）     控制器     将数据和视图层建立联系
  - vm 即  Vue 的实例  就是 vm

### 4.6.3 原理分析

与属性绑定和事件绑定密切相关

属性绑定：即数据能改变视图 v-bind

事件绑定：即视图(监听事件)能改变数据 v-on

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
</head>
<body>
  <div id="app">
    <div>{{msg}}</div>
    <input type="text" v-bind:value="msg" v-on:input='handle'>
    <input type="text" v-bind:value="msg" v-on:input='msg=$event.target.value'>
    <input type="text" v-model='msg'>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      v-model指令的本质
    */
    var vm = new Vue({
      el: '#app',
      data: {
        msg: 'hello'
      },
      methods: {
        handle: function(event){
          // 使用输入域中的最新的数据覆盖原来的数据
          this.msg = event.target.value;
        }
      }
    });
  </script>
</body>
</html>
```

# 5. 指令-事件绑定相关

## 5.1 v-on

- 用来绑定事件的
- 形式如：v-on:click  缩写为@click

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>

<body>
    <div id="app">
        <div>{{num}}</div>
        <div>
            <button v-on:click='num++'>点击</button>
            <button @click='num++'>点击1</button>
            <button @click='handle'>点击2</button>
            <button @click='handle()'>点击3</button>
        </div>
    </div>
    <script type="text/javascript" src="js/vue.js"></script>
    <script type="text/javascript">
        var vm = new Vue({
            el: '#app',
            data: {

                num: 0

            }, // 注意点： 这里不要忘记加逗号 
            // methods  中 主要是定义一些函数
            methods: {

                handle: function() {
                    // 这里的this是Vue的实例对象+
                    console.log(this === vm)
                        //   在函数中 想要使用data里面的数据 一定要加this 
                    this.num++;
                }

            }

        });
    </script>
    
</body>

</html>
```

## 5.2 v-on事件函数中传入参数

```html

<body>
    <div id="app">
        <div>{{num}}</div>
        <div>
            <!-- 如果事件直接绑定函数名称，那么默认会传递事件对象作为事件函数的第一个参数 -->
            <button v-on:click='handle1'>点击1</button>
            <!-- 2、如果事件绑定函数调用，那么事件对象必须作为最后一个参数显示传递，
                 并且事件对象的名称必须是$event 
            -->
            <button v-on:click='handle2(123, 456, $event)'>点击2</button>
        </div>
    </div>
    <script type="text/javascript" src="js/vue.js"></script>
    <script type="text/javascript">
        var vm = new Vue({
            el: '#app',
            data: {
                num: 0
            },
            methods: {
                handle1: function(event) {
                    console.log(event.target.innerHTML)
                },
                handle2: function(p, p1, event) {
                    console.log(p, p1)
                    console.log(event.target.innerHTML)
                    this.num++;
                }
            }
        });
    </script>
```

## 5.3 事件修饰符

- 在事件处理程序中调用 `event.preventDefault()` 或 `event.stopPropagation()` 是非常常见的需求。
- Vue 不推荐我们操作DOM    为了解决这个问题，Vue.js 为 `v-on` 提供了**事件修饰符**
- 修饰符是由点开头的指令后缀来表示的

```html
<!DOCTYPE html>
<html lang="en" xmlns:v-on="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
</head>
<body>
  <div id="app">
    <div>{{num}}</div>
    <div v-on:click='handle0'>
      <button v-on:click.stop>点击1</button>
    </div>
    <div>
      <a href="http://www.baidu.com" v-on:click.prevent>百度</a>
    </div>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      事件绑定-事件修饰符
    */
    var vm = new Vue({
      el: '#app',
      data: {
        num: 0
      },
      methods: {
        handle0: function(){
          this.num++;
        },
        handle1: function(event){
          // 阻止冒泡
          // event.stopPropagation();
        },
        handle2: function(event){
          // 阻止默认行为
          // event.preventDefault();
        }
      }
    });
  </script>
</body>
</html>
```

更多见官网

https://cn.vuejs.org/v2/api/#v-on

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.01/img1/5.png)

## 5.4 按键修饰符

- 在做项目中有时会用到键盘事件，在监听键盘事件时，我们经常需要检查详细的按键。Vue 允许为 `v-on` 在监听键盘事件时添加按键修饰符

```html
<!-- 只有在 `keyCode` 是 13 时调用 `vm.submit()` -->
<input v-on:keyup.13="submit">

<!-- -当点击enter时调用 `vm.submit()` -->
<input v-on:keyup.enter="submit">

<!--当点击enter或者space时  时调用 `vm.alertMe()`   -->
<input type="text" v-on:keyup.enter.space="alertMe" >

常用的按键修饰符
.enter => enter键
.tab => tab键
.delete (捕获“删除”和“退格”按键) =>  删除键
.esc => 取消键
.space =>  空格键
.up =>  上
.down =>  下
.left =>  左
.right =>  右

<script>
	var vm = new Vue({
        el:"#app",
        methods: {
              submit:function(){},
              alertMe:function(){},
        }
    })

</script>
```

## 5.5 自定义按键修饰符别名

- 在Vue中可以通过`config.keyCodes`自定义按键修饰符别名

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
</head>
<body>
  <div id="app">
    <form action="">
      <div>
        用户名：
        <input type="text" v-on:keyup.delete='clearContent' v-model='uname'>
      </div>
      <div>
        密码：
        <input type="text" v-on:keyup.f1='handleSubmit' v-model='pwd'>
      </div>
      <div>
        <input type="button" v-on:click='handleSubmit' value="提交">
      </div>
    </form>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      事件绑定-按键修饰符
    */
    Vue.config.keyCodes.f1 = 113
    var vm = new Vue({
      el: '#app',
      data: {
        uname: '',
        pwd: '',
        age: 0
      },
      methods: {
        clearContent:function(){
          // 按delete键的时候，清空用户名
          this.uname = '';
        },
        handleSubmit: function(){
          console.log(this.uname,this.pwd)
        }
      }
    });
  </script>
</body>
</html>

```

## 5.6 简单计算器案例

1. 通过v-model指令实现数值a和数值b的绑定
2. 给计算按钮绑定事件，实现计算逻辑
3. 将计算结果绑定到对应位置

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
</head>
<body>
  <div id="app">
    <h1>简单计算器</h1>
    <div>
      <span>数值A:</span>
      <span>
        <input type="text" v-model='a'>
      </span>
    </div>
    <div>
      <span>数值B:</span>
      <span>
        <input type="text" v-model='b'>
      </span>
    </div>
    <div>
      <button v-on:click='handle'>计算</button>
    </div>
    <div>
      <span>计算结果:</span>
      <span v-text='result'></span>
    </div>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      简单计算器案例 
    */
    var vm = new Vue({
      el: '#app',
      data: {
        a: '',
        b: '',
        result: ''
      },
      methods: {
        handle: function(){
          // 实现计算逻辑，注意和字符串拼接的差异
          this.result = parseInt(this.a) + parseInt(this.b);
        }
      }
    });
  </script>
</body>
</html>
```

# 6. 指令-属性绑定相关

## 6.1 v-bind

- v-bind 指令被用来动态响应地更新改变 HTML 属性
- v-bind:href 可以缩写为:href;

```html
<!-- 绑定一个属性 -->
<img v-bind:src="imageSrc">

<!-- 缩写 -->
<img :src="imageSrc">
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
</head>
<body>
  <div id="app">
    <a v-bind:href="url">百度</a>
    <a :href="url">百度1</a>
    <button v-on:click='handle'>切换</button>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      属性绑定
    */
    var vm = new Vue({
      el: '#app',
      data: {
        url: 'http://www.baidu.com'
      },
      methods: {
        handle: function(){
          // 修改URL地址
          this.url = 'http://itcast.cn';
        }
      }
    });
  </script>
</body>
</html>
```

## 6.2 绑定对象

- 我们可以给v-bind:class 一个对象，以动态地切换class。
- 注意：v-bind:class指令可以与普通的class特性共存

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
  <style type="text/css">
    .active {
      border: 1px solid red;
      width: 100px;
      height: 100px;
    }
    .error {
      background-color: orange;
    }
  </style>
</head>
<body>
  <div id="app">
    <div v-bind:class="{active: isActive,error: isError}">
      测试样式
    </div>
    <button v-on:click='handle'>切换</button>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      样式绑定

    */
    var vm = new Vue({
      el: '#app',
      data: {
        isActive: true,
        isError: true
      },
      methods: {
        handle: function(){
          // 控制isActive的值在true和false之间进行切换
          this.isActive = !this.isActive;
          this.isError = !this.isError;
        }
      }
    });
  </script>
</body>
</html>
```

## 6.3 绑定数组

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
  <style type="text/css">
    .active {
      border: 1px solid red;
      width: 100px;
      height: 100px;
    }
    .error {
      background-color: orange;
    }
  </style>
</head>
<body>
  <div id="app">
    <div v-bind:class='[activeClass, errorClass]'>测试样式</div>
    <button v-on:click='handle'>切换</button>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      样式绑定

    */
    var vm = new Vue({
      el: '#app',
      data: {
        activeClass: 'active',
        errorClass: 'error'
      },
      methods: {
        handle: function(){
          this.activeClass = '';
          this.errorClass = '';
        }
      }
    });
  </script>
</body>
</html>
```

## 6.4 绑定对象和绑定数组的区别

- 绑定对象的时候 对象的属性 即要渲染的类名对象的属性值对应的是data中的数据
- 绑定数组的时候数组里面存的是data中的数据

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
  <style type="text/css">
    .active {
      border: 1px solid red;
      width: 100px;
      height: 100px;
    }
    .error {
      background-color: orange;
    }
    .test {
      color: blue;
    }
    .base {
      font-size: 28px;
    }
  </style>
</head>
<body>
  <div id="app">
    <div v-bind:class='[activeClass, errorClass, {test: isTest}]'>测试样式</div>
    <div v-bind:class='arrClasses'></div>
    <div v-bind:class='objClasses'></div>
    <div class="base" v-bind:class='objClasses'></div>

    <button v-on:click='handle'>切换</button>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      样式绑定相关语法细节：
      1、对象绑定和数组绑定可以结合使用
      2、class绑定的值可以简化操作
      3、默认的class如何处理？默认的class会保留
      
    */
    var vm = new Vue({
      el: '#app',
      data: {
        activeClass: 'active',
        errorClass: 'error',
        isTest: true,
        arrClasses: ['active','error'],
        objClasses: {
          active: true,
          error: true
        }
      },
      methods: {
        handle: function(){
          // this.isTest = false;
          this.objClasses.error = false;
        }
      }
    });
  </script>
</body>
</html>
```

## 6.5 绑定style

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
  
</head>
<body>
  <div id="app">
    <div v-bind:style='{border: borderStyle, width: widthStyle, height: heightStyle}'></div>
    <div v-bind:style="objStyles"></div>
    <div v-bind:style='[objStyles, overrideStyles]'></div>
    <button v-on:click='handle'>切换</button>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      样式绑定之内联样式Style：
      
    */
    var vm = new Vue({
      el: '#app',
      data: {
        borderStyle: '1px solid blue',
        widthStyle: '100px',
        heightStyle: '200px',
        objStyles: {
          border: '1px solid green',
          width: '200px',
          height: '100px'
        },
        overrideStyles: {
          border: '5px solid orange',
          backgroundColor: 'blue'
        }
      },
      methods: {
        handle: function(){
          this.heightStyle = '100px';
          this.objStyles.width = '100px';
        }
      }
    });
  </script>
</body>
</html>
```

# 7. 分支结构

## 7.1 v-if v-show

- 1- 多个元素 通过条件判断展示或者隐藏某个元素。或者多个元素

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
  
</head>
<body>
  <div id="app">
    <div v-if='score>=90'>优秀</div>
    <div v-else-if='score<90&&score>=80'>良好</div>
    <div v-else-if='score<80&&score>60'>一般</div>
    <div v-else>比较差</div>
    <div v-show='flag'>测试v-show</div>
    <button v-on:click='handle'>点击</button>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      分支结构

      v-show的原理：控制元素样式是否显示 display:none
    */
    var vm = new Vue({
      el: '#app',
      data: {
        score: 10,
        flag: false
      },
      methods: {
        handle: function(){
          this.flag = !this.flag;
        }
      }
    });
  </script>
</body>
</html>
```

## 7.2 v-show和v-if的区别

- v-show本质就是标签display设置为none，控制隐藏
  - v-show只编译一次，后面其实就是控制css，而v-if不停的销毁和创建，故v-show性能更好一点。
- v-if是动态的向DOM树内添加或者删除DOM元素
  - v-if切换有一个局部编译/卸载的过程，切换过程中合适地销毁和重建内部的事件监听和子组件

# 8. 循环结构

## 8.1 v-for遍历数组

- 用于循环的数组里面的值可以是对象，也可以是普通元素  

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
  
</head>
<body>
  <div id="app">
    <div>水果列表</div>
    <ul>
      <li v-for='item in fruits'>{{item}}</li>
      <li v-for='(item, index) in fruits'>{{item + '---' + index}}</li>
      <li :key='item.id' v-for='(item, index) in myFruits'>
        <span>{{item.ename}}</span>
        <span>-----</span>
        <span>{{item.cname}}</span>
      </li>

    </ul>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    /*
      循环结构-遍历数组
    */
    var vm = new Vue({
      el: '#app',
      data: {
        fruits: ['apple', 'orange', 'banana'],
        myFruits: [{
          id: 1,
          ename: 'apple',
          cname: '苹果'
        },{
          id: 2,
          ename: 'orange',
          cname: '橘子'
        },{
          id: 3,
          ename: 'banana',
          cname: '香蕉'
        }]
      }
    });
  </script>
</body>
</html>
```

## 8.2 v-for遍历对象

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document</title>
  
</head>
<body>
  <div id="app">
    <div v-if='v==13' v-for='(v,k,i) in obj'>{{v + '---' + k + '---' + i}}</div>
  </div>
  <script type="text/javascript" src="js/vue.js"></script>
  <script type="text/javascript">
    // 使用原生js遍历对象
    var obj = {
      uname: 'lisi',
      age: 12,
      gender: 'male'
    }
    for(var key in obj) {
      console.log(key, obj[key])
    }
    /*
      循环结构
    */
    var vm = new Vue({
      el: '#app',
      data: {
        obj: {
          uname: 'zhangsan',
          age: 13,
          gender: 'female'
        }
      }
    });
  </script>
</body>
</html>
```

- 不推荐同时使用 `v-if` 和 `v-for`
- 当 `v-if` 与 `v-for` 一起使用时，`v-for` 具有比 `v-if` 更高的优先级。

```html
   <!--  循环结构-遍历对象
		v 代表   对象的value
		k  代表对象的 键 
		i  代表索引	
	---> 
     <div v-if='v==13' v-for='(v,k,i) in obj'>{{v + '---' + k + '---' + i}}</div>

<script>
 new Vue({
  el: '#example-1',
  data: {
    items: [
      { message: 'Foo' },
      { message: 'Bar' }
    ]，
    obj: {
        uname: 'zhangsan',
        age: 13,
        gender: 'female'
    }
  }
})
</script>
```

- key 的作用
  - **key来给每个节点做一个唯一标识**
  - **key的作用主要是为了高效得更新虚拟DOM**

```html
<ul>
  <li v-for="item in items" :key="item.id">...</li>
</ul>
```

# 9. 案例选项卡

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.08.01/img1/6.png)

## 9.1 HTML结构

```html
    <div id="app">
        <div class="tab">
            <!--  tab栏  -->
            <ul>
                <li class="active">apple</li>
                <li class="">orange</li>
                <li class="">lemon</li>
            </ul>
              <!--  对应显示的图片 -->
            <div class="current"><img src="img/apple.png"></div>
            <div class=""><img src="img/orange.png"></div>
            <div class=""><img src="img/lemon.png"></div>
        </div>
    </div>
```

## 9.2 提供的数据

```js
list: [{
                    id: 1,
                    title: 'apple',
                    path: 'img/apple.png'
                }, {
                    id: 2,
                    title: 'orange',
                    path: 'img/orange.png'
                }, {
                    id: 3,
                    title: 'lemon',
                    path: 'img/lemon.png'
                }]
```

## 9.3 把数据渲染到页面

- 把tab栏 中的数替换到页面上

  - 把 data 中 title  利用 v-for 循环渲染到页面上 
  - 把 data 中 path利用 v-for 循环渲染到页面上 

  ```html
      <div id="app">
          <div class="tab">  
              <ul>
                    <!--  
                      1、绑定key的作用 提高Vue的性能 
                      2、 key 需要是唯一的标识 所以需要使用id， 也可以使用index ，
  						index 也是唯一的 
                      3、 item 是 数组中对应的每一项  
                      4、 index 是 每一项的 索引
                  -->
                     <li :key='item.id' v-for='(item,index) in list'>{{item.title}}</li>
                </ul>
                <div  :key='item.id' v-for='(item, index) in list'>
                      <!-- :  是 v-bind 的简写   绑定属性使用 v-bind -->
                      <img :src="item.path">
                </div>
          </div>
      </div>
  <script>
      new  Vue({
          //  指定 操作元素 是 id 为app 的 
          el: '#app',
              data: {
                  list: [{
                      id: 1,
                      title: 'apple',
                      path: 'img/apple.png'
                  }, {
                      id: 2,
                      title: 'orange',
                      path: 'img/orange.png'
                  }, {
                      id: 3,
                      title: 'lemon',
                      path: 'img/lemon.png'
                  }]
              }
      })
  
  </script>
  ```


## 9.4 给每一个tab栏添加事件,并让选中的高亮

- 4.1 、让默认的第一项tab栏高亮

  - tab栏高亮 通过添加类名active 来实现   （CSS  active 的样式已经提前写好）
    - 在data 中定义一个 默认的  索引 currentIndex  为  0 
    - 给第一个li 添加 active 的类名  
      - 通过动态绑定class 来实现   第一个li 的索引为 0     和 currentIndex   的值刚好相等
      -  currentIndex     ===  index  如果相等  则添加类名 active  否则 添加 空类名

- 4.2 、让默认的第一项tab栏对应的div 显示 

  - 实现思路 和 第一个 tab 实现思路一样  只不过 这里控制第一个div 显示的类名是 current

  ```html
    <ul>
  	   <!-- 动态绑定class   有 active   类名高亮  无 active   不高亮-->
         <li  :class='currentIndex==index?"active":""'
             :key='item.id' v-for='(item,index) in list'
             >{{item.title}}</li>
    </ul>
  	<!-- 动态绑定class   有 current  类名显示  无 current  隐藏-->
    <div :class='currentIndex==index?"current":""' 
         
         :key='item.id' v-for='(item, index) in list'>
          <!-- :  是 v-bind 的简写   绑定属性使用 v-bind -->
          <img :src="item.path">
    </div>
  
  <script>
      new  Vue({
          el: '#app',
              data: {
                  currentIndex: 0, // 选项卡当前的索引  默认为 0  
                  list: [{
                      id: 1,
                      title: 'apple',
                      path: 'img/apple.png'
                  }, {
                      id: 2,
                      title: 'orange',
                      path: 'img/orange.png'
                  }, {
                      id: 3,
                      title: 'lemon',
                      path: 'img/lemon.png'
                  }]
              }
      })
  
  </script>
  ```

- 4.3 、点击每一个tab栏 当前的高亮 其他的取消高亮 

  - 给每一个li添加点击事件    

  - 让当前的索引 index  和  当前 currentIndex 的  值 进项比较 

  - 如果相等 则当前li  添加active 类名 当前的 li 高亮  当前对应索引的 div 添加 current 当前div 显示 其他隐藏

    ```html
        <div id="app">
            <div class="tab">
                <ul>
                    <!--  通过v-on 添加点击事件   需要把当前li 的索引传过去 
    				-->
                    <li v-on:click='change(index)'		           			
                        :class='currentIndex==index?"active":""'                   
                        :key='item.id' 
                        v-for='(item,index) in list'>{{item.title}}</li>
                </ul>
                <div :class='currentIndex==index?"current":""' 
                     :key='item.id' v-for='(item, index) in list'>
                    <img :src="item.path">
                </div>
            </div>
        </div>
    
    <script>
        new  Vue({
            el: '#app',
                data: {
                    currentIndex: 0, // 选项卡当前的索引  默认为 0  
                    list: [{
                        id: 1,
                        title: 'apple',
                        path: 'img/apple.png'
                    }, {
                        id: 2,
                        title: 'orange',
                        path: 'img/orange.png'
                    }, {
                        id: 3,
                        title: 'lemon',
                        path: 'img/lemon.png'
                    }]
                },
                methods: {
                    change: function(index) {
                        // 通过传入过来的索引来让当前的  currentIndex  和点击的index 值 相等 
                        //  从而实现 控制类名    
                        this.currentIndex = index;
                    }
                }
        
        })
    
    </script>
    ```

    