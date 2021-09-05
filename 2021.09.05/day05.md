# 1. 路由的概念

路由的本质就是一种对应关系，比如说我们在url地址中输入我们要访问的url地址之后，浏览器要去请求这个url地址对应的资源。那么url地址和真实的资源之间就有一种对应的关系，就是路由

路由分为前端路由和后端路由：

1. 后端路由是由服务器端进行实现，并完成资源的分发
2. 前端路由是依靠hash值(锚链接)的变化进行实现 

后端路由性能相对前端路由来说较低，所以，我们接下来主要学习的是前端路由

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/1.png)

前端路由的基本概念：根据不同的事件来显示不同的页面内容，即事件与事件处理函数之间的对应关系，前端路由主要做的事情就是监听事件并分发执行事件处理函数

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/2.png)

# 2. 前端路由的初体验

前端路由是基于hash值的变化进行实现的（比如点击页面中的菜单或者按钮改变URL的hash值，根据hash值的变化来控制组件的切换）
核心实现依靠一个事件，即监听hash值变化的事件

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/3.png)

前端路由实现tab栏切换：
```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Document</title>
    <!-- 导入 vue 文件 -->
    <script src="./lib/vue_2.5.22.js"></script>
  </head>
  <body>
    <!-- 被 vue 实例控制的 div 区域 -->
    <div id="app">
      <!-- 切换组件的超链接 -->
      <a href="#/zhuye">主页</a> 
      <a href="#/keji">科技</a> 
      <a href="#/caijing">财经</a>
      <a href="#/yule">娱乐</a>

      <!-- 根据 :is 属性指定的组件名称，把对应的组件渲染到 component 标签所在的位置 -->
      <!-- 可以把 component 标签当做是【组件的占位符】 -->
      <component :is="comName"></component>
    </div>

    <script>
      // #region 定义需要被切换的 4 个组件
      // 主页组件
      const zhuye = {
        template: '<h1>主页信息</h1>'
      }

      // 科技组件
      const keji = {
        template: '<h1>科技信息</h1>'
      }

      // 财经组件
      const caijing = {
        template: '<h1>财经信息</h1>'
      }

      // 娱乐组件
      const yule = {
        template: '<h1>娱乐信息</h1>'
      }
      // #endregion

      // #region vue 实例对象
      const vm = new Vue({
        el: '#app',
        data: {
          comName: 'zhuye'
        },
        // 注册私有组件
        components: {
          zhuye,
          keji,
          caijing,
          yule
        }
      })
      // #endregion

      // 监听 window 的 onhashchange 事件，根据获取到的最新的 hash 值，切换要显示的组件的名称
      window.onhashchange = function() {
        // 通过 location.hash 获取到最新的 hash 值
        console.log(location.hash);
        switch(location.hash.slice(1)){
          case '/zhuye':
            vm.comName = 'zhuye'
          break
          case '/keji':
            vm.comName = 'keji'
          break
          case '/caijing':
            vm.comName = 'caijing'
          break
          case '/yule':
            vm.comName = 'yule'
          break
        }
      }
    </script>
  </body>
</html>
```

# 3. Vue Router

## 3.1 简介

它是一个Vue.js官方提供的路由管理器。是一个功能更加强大的前端路由器，推荐使用

Vue Router和Vue.js非常契合，可以一起方便的实现SPA(single page web application,单页应用程序)应用程序的开发

Vue Router依赖于Vue，所以需要先引入Vue，再引入Vue Router

Vue Router的特性：

+ 支持H5历史模式或者hash模式
+ 支持嵌套路由
+ 支持路由参数
+ 支持编程式路由
+ 支持命名路由
+ 支持路由导航守卫
+ 支持路由过渡动画特效
+ 支持路由懒加载
+ 支持路由滚动行为

## 3.2 Vue Router的基本使用步骤

1. 导入js文件

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/4.png)

2. 添加路由链接

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/5.png)

3. 添加路由填充位（路由占位符）

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/6.png)

4. 定义路由组件

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/7.png)

5. 配置路由规则并创建路由实例

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/8.png)

6. 将路由挂载到Vue实例中

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/9.png)

## 3.3 路由重定向

可以通过路由重定向为页面设置默认展示的组件，在路由规则中添加一条路由规则即可，如下：

```html
// 创建路由实例对象
const router = new VueRouter({
  // 所有的路由规则
  routes: [
    { path: '/', redirect: '/user'},
    { path: '/user', component: User },
    { path: '/register', component: Register }
  ]
})
```

## 3.4 嵌套路由
当我们进行路由的时候显示的组件中还有新的子级路由链接以及内容

嵌套路由最关键的代码在于理解子级路由的概念
![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/10.png)

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/11.png)

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/12.png)

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Document</title>
    <!-- 导入 vue 文件 -->
    <script src="./lib/vue_2.5.22.js"></script>
    <script src="./lib/vue-router_3.0.2.js"></script>
  </head>
  <body>
    <!-- 被 vm 实例所控制的区域 -->
    <div id="app">
      <router-link to="/user">User</router-link>
      <router-link to="/register">Register</router-link>

      <!-- 路由占位符 -->
      <router-view></router-view>
    </div>

    <script>
      const User = {
        template: '<h1>User 组件</h1>'
      }

      const Register = {
        template: `<div>
          <h1>Register 组件</h1>
          <hr/>

          <router-link to="/register/tab1">tab1</router-link>
          <router-link to="/register/tab2">tab2</router-link>

          <router-view/>
        <div>`
      }

      const Tab1 = {
        template: '<h3>tab1 子组件</h3>'
      }

      const Tab2 = {
        template: '<h3>tab2 子组件</h3>'
      }

      // 创建路由实例对象
      const router = new VueRouter({
        // 所有的路由规则
        routes: [
          { path: '/', redirect: '/user'},
          { path: '/user', component: User },
          // children 数组表示子路由规则
          { path: '/register', component: Register, children: [
            { path: '/register/tab1', component: Tab1 },
            { path: '/register/tab2', component: Tab2 }
          ] }
        ]
      })

      // 创建 vm 实例对象
      const vm = new Vue({
        // 指定控制的区域
        el: '#app',
        data: {},
        // 挂载路由实例对象
        // router: router
        router
      })
    </script>
  </body>
</html>
```

## 3.5 动态路由匹配

### 3.5.1 基本用法

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/13.png)

### 3.5.2 props为布尔

如果使用$route.params.id来获取路径传参的数据不够灵活，我们可以使用props来接收参数，将组件和路由解耦

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/14.png)

### 3.5.3 props是对象

![15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/15.png)

### 3.5.4 props是函数

![16](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/16.png)

## 3.6 命名路由
命名路由：给路由取别名
![17](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/17.png)

## 3.7 编程式导航

页面导航有两种方式

![18](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/18.png)

![19](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/19.png)

![20](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/20.png)

# 4. 路由案例-实现后台管理案例

案例效果：

![21](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.09.05/pics/21.png)

点击左侧的"用户管理","权限管理","商品管理","订单管理","系统设置"都会出现对应的组件并展示内容

其中"用户管理"组件展示的效果如上图所示，在用户管理区域中的详情链接也是可以点击的，点击之后将会显示用户详情信息

案例思路：

1. 先将素材文件夹中的11.基于vue-router的案例.html复制到我们自己的文件夹中。看一下这个文件中的代码编写了一些什么内容，这个页面已经把后台管理页面的基本布局实现了
2. 在页面中引入vue，vue-router
3. 创建Vue实例对象，准备开始编写代码实现功能
4. 希望是通过组件的形式展示页面的主体内容，而不是写死页面结构，所以我们可以定义一个根组件：

```html
//只需要把原本页面中的html代码设置为组件中的模板内容即可
const app = {
    template:`<div>
        <!-- 头部区域 -->
        <header class="header">传智后台管理系统</header>
        <!-- 中间主体区域 -->
        <div class="main">
          <!-- 左侧菜单栏 -->
          <div class="content left">
            <ul>
              <li>用户管理</li>
              <li>权限管理</li>
              <li>商品管理</li>
              <li>订单管理</li>
              <li>系统设置</li>
            </ul>
          </div>
          <!-- 右侧内容区域 -->
          <div class="content right">
            <div class="main-content">添加用户表单</div>
          </div>
        </div>
        <!-- 尾部区域 -->
        <footer class="footer">版权信息</footer>
      </div>`
  }
```
5. 当我们访问页面的时候，默认需要展示刚刚创建的app根组件，我们可以创建一个路由对象来完成这个事情，然后将路由挂载到Vue实例对象中即可

```html
const myRouter = new VueRouter({
    routes:[
        {path:"/",component:app}
    ]
})

const vm = new Vue({
    el:"#app",
    data:{},
    methods:{},
    router:myRouter
})
```
补充：到此为止，基本的js代码都处理完毕了，我们还需要设置一个路由占位符
```html
<body>
  <div id="app">
    <router-view></router-view>
  </div>
</body>
```
6. 此时我们打开页面应该就可以得到一个VueRouter路由出来的根组件了。我们需要在这个根组件中继续路由实现其他的功能子组件，先让我们更改根组件中的模板：更改左侧li为子级路由链接，并在右侧内容区域添加子级组件占位符

```html
const app = {
    template:`<div>
        ........
        <div class="main">
          <!-- 左侧菜单栏 -->
          <div class="content left">
            <ul>
              <!-- 注意：我们把所有li都修改为了路由链接 -->
              <li><router-link to="/users">用户管理</router-link></li>
              <li><router-link to="/accesses">权限管理</router-link></li>
              <li><router-link to="/goods">商品管理</router-link></li>
              <li><router-link to="/orders">订单管理</router-link></li>
              <li><router-link to="/systems">系统设置</router-link></li>
            </ul>
          </div>
          <!-- 右侧内容区域 -->
          <div class="content right">
            <div class="main-content">
                <!-- 在 -->
                <router-view></router-view> 
            </div>
          </div>
        </div>
        .......
      </div>`
  }
```
然后，我们要为子级路由创建并设置需要显示的子级组件
```html
//建议创建的组件首字母大写，和其他内容区分
const Users = {template:`<div>
    <h3>用户管理</h3>
</div>`}
const Access = {template:`<div>
    <h3>权限管理</h3>
</div>`}
const Goods = {template:`<div>
    <h3>商品管理</h3>
</div>`}
const Orders = {template:`<div>
    <h3>订单管理</h3>
</div>`}
const Systems = {template:`<div>
    <h3>系统管理</h3>
</div>`}

//添加子组件的路由规则
const myRouter = new VueRouter({
    routes:[
        {path:"/",component:app , children:[
            { path:"/users",component:Users },
            { path:"/accesses",component:Access },
            { path:"/goods",component:Goods },
            { path:"/orders",component:Orders },
            { path:"/systems",component:Systems },
        ]}
    ]
})

const vm = new Vue({
    el:"#app",
    data:{},
    methods:{},
    router:myRouter
})
```

7. 展示用户信息列表：

   Users组件添加私有数据,并在模板中循环展示私有数据

```html
const Users = {
  data() {
    return {
      userlist: [
        { id: 1, name: '张三', age: 10 },
        { id: 2, name: '李四', age: 20 },
        { id: 3, name: '王五', age: 30 },
        { id: 4, name: '赵六', age: 40 }
      ]
    }
  },
  methods: {
    goDetail(id) {
      console.log(id)
      this.$router.push('/userinfo/' + id)
    }
  },
  template: `<div>
  <h3>用户管理区域</h3>
  <table>
    <thead>
      <tr><th>编号</th><th>姓名</th><th>年龄</th><th>操作</th></tr>
    </thead>
    <tbody>
      <tr v-for="item in userlist" :key="item.id">
        <td>{{item.id}}</td>
        <td>{{item.name}}</td>
        <td>{{item.age}}</td>
        <td>
          <a href="javascript:;" @click="goDetail(item.id)">详情</a>
        </td>
      </tr>
    </tbody>
  </table>
</div>`
}
```

8. 当用户列表展示完毕之后，我们可以点击列表中的详情来显示用户详情信息，首先我们需要创建一个组件，用来展示详情信息

```html
const UserInfo = {
    props:["id"],
    template:`<div>
      <h5>用户详情</h5>
      <p>查看 {{id}} 号用户信息</p>
      <button @click="goBack">返回用户详情页</button>
    </div> `,
    methods:{
      goBack(){
        //当用户点击按钮，后退一页
        this.$router.go(-1);
      }
    }
  }
```
然后我们需要设置这个组件的路由规则
```html
const myRouter = new VueRouter({
    routes:[
        {path:"/",component:app , children:[
            { path:"/users",component:Users },
            //添加一个/userinfo的路由规则
            { path:"/userinfo/:id",component:UserInfo,props:true},
            { path:"/accesses",component:Access },
            { path:"/goods",component:Goods },
            { path:"/orders",component:Orders },
            { path:"/systems",component:Systems },
        ]}
    ]
})

const vm = new Vue({
    el:"#app",
    data:{},
    methods:{},
    router:myRouter
})
```
再接着给用户列表中的详情a连接添加事件
```html
const Users = {
    data(){
        return {
            userList:[
                {id:1,name:"zs",age:18},
                {id:2,name:"ls",age:19},
                {id:3,name:"wang",age:20},
                {id:4,name:"jack",age:21},
            ]
        }
    },
    template:`<div>
        <h3>用户管理</h3>
        <table>
            <thead>
                <tr>
                    <th>编号</th>
                    <th>姓名</th>
                    <th>年龄</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr :key="item.id" v-for="item in userList">
                    <td>{{item.id}}</td>
                    <td>{{item.name}}</td>
                    <td>{{item.age}}</td>
                    <td><a href="javascript:;" @click="goDetail(item.id)">详情</a></td>
                </tr>
            </tbody>
        </table>
    </div>`,
    methods:{
        goDetail(id){
            this.$router.push("/userinfo/"+id);
        }
    }
}
```