# 1. jQuery介绍

## 1.1 jQuery的概述

​	jQuery是一个优秀的javascript库(类似Java里面的jar包)，兼容css3和各大浏览器，提供了dom、events、animate、ajax等简易的操作。  并且jquery的插件非常丰富，大多数功能都有相应的插件解决方案。jquery的宗旨是 write less, do more

​	说白了: JQ就是js库, 封装了JS常见的操作,我们使用JS起来更加的简单  (特别是dom这块)

## 1.2 jQuery的作用

​	jQuery最主要的作用是简化js的Dom树的操作 

## 1.3 jQuery框架的下载

​	jQuery的官方下载地址：http://www.jquery.com

## 1.4 jQuery的版本

+ 1.x：兼容IE678，使用最为广泛的，官方只做BUG维护，功能不再新增。因此一般项目来说，使用1.x版本就可以了，最终版本：1.12.4 (2016年5月20日)

+ 2.x：不兼容IE678，很少有人使用，官方只做BUG维护，功能不再新增。如果不考虑兼容低版本的浏览器可以使用2.x，最终版本：2.2.4 (2016年5月20日)

+ 3.x：不兼容IE678，很多老的jQuery插件不支持这个版本。目前该版本是官方主要更新维护的版本.

+ 注:开发版本与生产版本，命名为jQuery-x.x.x.js为开发版本，命名为jQuery-x.x.x.min.js为生产版本，开发版本源码格式良好，有代码缩进和代码注释，方便开发人员查看源码，但体积稍大。而生产版本没有代码缩进和注释，且去掉了换行和空行，不方便发人员查看源码，但体积很小 

小结

1. JQ: 是JS库, 能让我们写js代码更加的简单和快捷, 本质还是JS
2. JQ版本: 选择xxx.min.js使用

# 2. 快速入门

## 2.1 需求

​	等页面加载完成之后 获取输入框的value

## 2.2 步骤

1. 拷贝jq库到项目
2. 把jq引入页面
3. $(匿名函数)，该匿名函数中的代码会在文档加载完毕之后执行

## 2.3 实现

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>jQuery的入门</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <script>
        //给当前的window绑定一个加载完毕事件
        /*window.onload = function () {
            //窗体加载完毕，就会执行这个匿名函数
            //目标: 获取输入框的value值
            var ipt = document.getElementById("ipt");
            var value = ipt.value;
            console.log(value)
        }*/

        //jQuery也能够绑定页面加载完毕事件
        $(function () {
            //页面加载完毕之后要执行的代码，就写在这个匿名函数中
            //目标: 获取输入框的value值
            var ipt = document.getElementById("ipt");
            var value = ipt.value;
            console.log(value)
        })
    </script>
    <input type="text" id="ipt" value="张三">
</body>
</html>
```

注意事项

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.08/pics/1.png) 

100%几率是JQ库没有导入或者JQ库路径写错了

# 3. JQ和JS对象转换(重点)

## 3.1 对象说明

​	JS对象:  document.getElemxxx()  获得的都是JS对象  大部分都是属性

​	JQ对象: $()   大部分都是方法

​	jQuery本质上虽然也是js，但如果使用jQuery的属性和方法那么必须保证对象是jQuery对象而不是js方式获得的DOM对象。使用js方式获取的对象是js的DOM对象，使用jQuery方式获取的对象是jQuery对象。两者的转换关系如下

## 3.2 转换语法

​	js的DOM对象转换成jQuery对象，**语法：$(js对象)**

​	jQuery对象转换成js对象，**语法：jquery对象[索引]** 或 jquery对象.get(索引); 一般索引写0

## 3.3 讲解

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>jQuery对象和js对象</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <input type="text" id="ipt" value="张三">
    <script>
        //目标: 获取输入框的值
        //1. 什么是jQuery对象: 使用jQuery的选择器获取的对象就是jQuery对象
        //2. 什么是js对象: 除了jQuery对象，其它的都是js对象
        //3. js对象只能操作js对象自己的属性和方法
        //4. jQuery对象只能操作jQuery对象自己的属性和方法
        var ipt = document.getElementById("ipt");//js对象
        //var value = ipt.value;
        //console.log(value)

        var $ipt = $("#ipt");//jQuery对象，jQuery对象的命名通常是以$开头作为标示
        //var val = $ipt.val();
        //console.log(val)

        //5. 将js对象转换成jQuery对象: $(js对象)
        //console.log($(ipt).val())

        //6. 将jQuery对象转成js对象: 遍历出数组中的元素(对象)
        //jQuery对象的本质是一个数组，该数组中的每一个对象都是js对象
        console.log($ipt[0].value)
    </script>
</body>
</html>
```

小结

1. JS对象转成jQ对象  

```
$(js对象)
```

2. JQ对象转成JS对象

```
JQ对象[下标] 或者 JQ对象.get(下标) 下标一般写0
```

# 4. JQ选择器(重要)

## 4.1 基本选择器(重点)

+ 语法

| 选择器名称               | 语法                | 解释                              |
| ------------------------ | ------------------- | --------------------------------- |
| 标签选择器（元素选择器） | $("html标签名")     | 获得所有匹配标签名称的元素        |
| id选择器                 | $("#id的属性值")    | 获得与指定id属性值匹配的元素      |
| 类选择器                 | $(".class的属性值") | 获得与指定的class属性值匹配的元素 |

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>基本选择器</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <div id="d1">div1</div>
    <div class="c1">div2</div>
    <span class="c1">span1</span>
    <span class="c1">span2</span>

    <script>
        //目标1: 获取id为d1的标签
        //console.log($("#d1"))

        //目标2: 获取类名为c1的所有标签
        //console.log($(".c1"))

        //目标3: 获取标签名为span的所有标签
        console.log($("span"))
    </script>
</body>
</html>
```

## 4.2 层级选择器 

+ 语法

| 选择器名称     | 语法       | 解释                         |
| -------------- | ---------- | ---------------------------- |
| **后代选择器** | $("A  B ") | 选择A元素内部的所有B元素     |
| 子选择器       | $("A > B") | 选择A元素内部的所有B子元素   |
| 兄弟选择器     | $("A + B") | 获得A元素同级的下一个B元素   |
| 兄弟选择器     | $("A ~ B") | 获得A元素同级的后面所有B元素 |

```html
<!DOCTYPE html>
<html>
<head>
    <title>层次选择器</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <style type="text/css">
        div,span{
            width: 180px;
            height: 180px;
            margin: 20px;
            background: #9999CC;
            border: #000 1px solid;
            float:left;
            font-size: 17px;
            font-family:Roman;
        }

        div .mini{
            width: 50px;
            height: 50px;
            background: #CC66FF;
            border: #000 1px solid;
            font-size: 12px;
            font-family:Roman;
        }

        div .mini01{
            width: 50px;
            height: 50px;
            background: #CC66FF;
            border: #000 1px solid;
            font-size: 12px;
            font-family:Roman;
        }

    </style>
    <script src="../js/jquery-3.3.1.js"></script>
    <script>
        //改变 <body> 内所有 <div> 的背景色为红色
        function fn1() {
            //1. 获取到body内的所有div: 层级选择器 A B 表示获取A里面的所有B
            //获取子孙后代
            //2. 将这些div的背景色设置为红色
            $("body div").css("background-color","red")
        }

        //改变 <body> 内子 <div> 的背景色为 红色: 层级选择器 A>B 表示获取A里面的子元素B
        //获取儿子们
        function fn2() {
            $("body>div").css("background-color","red")
        }

        //改变 id 为 one 的下一个 <div> 的背景色为 红色: 层级选择器 A+B 表示获取和A同级的后面的第一个B
        //获取第一个弟弟
        function fn3() {
            $("#one+div").css("background-color","red")
        }

        //改变 id 为 two 的元素后面的所有兄弟<div>的元素的背景色为 红色: 层级选择器 A~B 表示获取和A同级的后面的所有B‘
        //获取弟弟们
        function fn4() {
            $("#two~div").css("background-color","red")
        }
    </script>
</head>

<body>
<input onclick="fn1()" type="button" value=" 改变 <body> 内所有 <div> 的背景色为红色"  id="b1"/>
<input onclick="fn2()" type="button" value=" 改变 <body> 内子 <div> 的背景色为 红色"  id="b2"/>
<input onclick="fn3()" type="button" value=" 改变 id 为 one 的下一个 <div> 的背景色为 红色"  id="b3"/>
<input onclick="fn4()" type="button" value=" 改变 id 为 two 的元素后面的所有兄弟<div>的元素的背景色为 红色"  id="b4"/>

<h1>有一种奇迹叫坚持</h1>
<h2>自信源于努力</h2>

<div id="one">
    id为one

</div>

<div id="two" class="mini" >
    id为two   class是 mini
    <div  class="mini" >class是 mini</div>
</div>

<div class="one" >
    class是 one
    <div  class="mini" >class是 mini</div>
    <div  class="mini" >class是 mini</div>
</div>
<div class="one">
    class是 one
    <div  class="mini01" >class是 mini01</div>
    <div  class="mini" >class是 mini</div>
</div>

<div id="mover" >
    动画
</div>

<span class="spanone">span</span>

</body>
</html>
```

## 4.3 属性选择器

+ 语法

| 选择器名称 | 语法              | 解释                           |
| ---------- | ----------------- | ------------------------------ |
| 属性选择器 | $("A[属性名]")    | 包含指定属性的选择器           |
| 属性选择器 | $("A[属性名=值]") | 包含指定属性等于指定值的选择器 |

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>属性选择器</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <input id="ipt1" type="text" value="张三">
    <input type="password" value="123456">
    <script>
        //目标1: 设置有id属性的input标签的背景色为红色
        //属性选择器: [属性名]包含某个属性
        //$("input[id]").css("background-color","red")

        //目标2: 设置type属性为password的input标签的背景色为红色
        //属性选择器: [属性名='属性值']某个属性为啥值的标签
        $("input[type='password']").css("background-color","red")
    </script>
</body>
</html>
```

## 4.4 基本过滤选择器

+ 语法

| 选择器名称     | 语法           | 解释                           |
| -------------- | -------------- | ------------------------------ |
| 首元素选择器   | :first         | 获得选择的元素中的第一个元素   |
| 尾元素选择器   | :last          | 获得选择的元素中的最后一个元素 |
| 非元素选择器   | :not(selecter) | 不包括指定内容的元素           |
| 偶数选择器     | :even          | 偶数，从 0 开始计数            |
| 奇数选择器     | :odd           | 奇数，从 0 开始计数            |
| 等于索引选择器 | :eq(index)     | 指定索引元素                   |
| 大于索引选择器 | :gt(index)     | 大于指定索引元素               |
| 小于索引选择器 | :lt(index)     | 小于指定索引元素               |

* 使用基本过滤选择器完成隔行换色案例

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>基本过滤选择器完成隔行换色案例</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
<table id="tab1" border="1" width="800" align="center" >
    <tr>
        <td colspan="5"><input type="button" value="删除"></td>
    </tr>
    <tr style="background-color: #999999;">
        <th><input type="checkbox"></th>
        <th>分类ID</th>
        <th>分类名称</th>
        <th>分类描述</th>
        <th>操作</th>
    </tr>
    <tr>
        <td><input type="checkbox"></td>
        <td>1</td>
        <td>手机数码</td>
        <td>手机数码类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox"></td>
        <td>2</td>
        <td>电脑办公</td>
        <td>电脑办公类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox"></td>
        <td>3</td>
        <td>鞋靴箱包</td>
        <td>鞋靴箱包类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox"></td>
        <td>4</td>
        <td>家居饰品</td>
        <td>家居饰品类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>

    <script>
        //基本过滤选择器:
        //目标1: 设置第一行的背景色为红色, :first
        $("tr:first").css("background-color","red")

        //目标2: 设置最后一行的背景色为蓝色，:last
        $("tr:last").css("background-color","blue")

        //目标3: 设置所有奇数(下标)行的背景色为绿色, :odd
        $("tr:odd").css("background-color","green")

        //目标4: 设置所有偶数(下标)行的背景色为黄色, :even
        $("tr:even").css("background-color","yellow")

        //目标5: 设置下标为3的那一行的背景色为粉红色, :eq(index)
        $("tr:eq(3)").css("background-color","pink")

        //目标6: 设置所有下标小于3的所有行的背景色为灰色, :lt(index)
        $("tr:lt(3)").css("background-color","gray")

        //目标7: 设置所有下标大于3的所有行的背景色为#ffc900, :gt(index)
        $("tr:gt(3)").css("background-color","#ffc900")
    </script>
</table>
</body>
</html>
```

## 4.5 表单属性过滤选择器

+ 语法

| 选择器名称       | 语法      | 解释                      |
| ---------------- | --------- | ------------------------- |
| 可用元素选择器   | :enabled  | 获得可用元素              |
| 不可用元素选择器 | :disabled | 获得不可用元素            |
| 选中选择器       | :checked  | 获得单选/复选框选中的元素 |
| 选中选择器       | :selected | 获得下拉框选中的元素      |

# 5. JQ操作标签样式

| API方法            | 解释           |
| ------------------ | :------------- |
| css(name) 使用很少 | 获取CSS样式    |
| css(name,value)    | 设置CSS样式    |
| addClass(类名)     | 给标签添加类名 |
| removeClass(类名)  | 删除标签的类名 |

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>jQuery操作标签的样式</title>
    <script src="../../js/jquery-3.3.1.js"></script>
    <style>
        /*css的类选择器*/
        .redStyle{
            background-color: red;
            width: 600px;
            height: 60px;
        }

        .blueStyle{
            background-color: blue;
            width: 800px;
            height: 80px;
        }
    </style>
</head>
<body>
<input id="ipt" type="text" onmouseover="fn1()" onmouseout="fn2()">
<script>
    //目标: 鼠标移入输入框的时候，设置输入框的背景色为红色，移出输入框的时候设置输入框的背景色为蓝色
    function fn1() {
        //鼠标移入
        //使用css方法设置标签的样式，其实是设置的标签的行内样式,缺点:1. 样式无法复用  2. 编写太麻烦
        //$("#ipt").css("background-color","red")

        //给输入框添加一个类名为"redStyle"
        $("#ipt").addClass("redStyle")
        //删除输入框的"blueStyle"类名

        $("#ipt").removeClass("blueStyle")
    }


    function fn2() {
        //鼠标移出
        //$("#ipt").css("background-color","blue")

        //给输入框添加一个类名为"blueStyle"
        $("#ipt").addClass("blueStyle")

        //删除输入框的"redStyle"类名
        $("#ipt").removeClass("redStyle")

    }
</script>
</body>
</html>
```

# 6. JQ操作属性

| API方法            | 解释                                 |
| ------------------ | ------------------------------------ |
| attr(name,[value]) | 获得/设置属性的值                    |
| prop(name,[value]) | 获得/设置属性的值(checked，selected) |

+ attr与prop的注意问题

  ​	boolean类型的属性例如checked 和 selected 建议 使用prop操作

  ​	其他使用attr获取 

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>jquery操作标签的属性</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <input type="text" name="username" aa="bb"><br>

    <script>
        //设置输入框的name属性为nickname
        //$("input").attr("name","nickname")

        //$("input").prop("name","nickname")

        //设置输入框的aa属性为cc
        $("input").attr("aa","cc")
        //$("input").prop("aa","cc")

        //1. attr方法可以操作标签的一切属性,而prop方法只能操作标签内置有的属性
        //2. 如果属性是boolean类型的，那么我们使用prop操作
    </script>
</body>
</html>
```

小结

1. JQ对象.attr/prop(标签的属性);  获得标签的属性值
2. JQ对象.attr/prop(标签的属性,值);  设置标签的属性

# 7. 使用JQ操作DOM(重点)

## 7.1 jQuery对DOM树中的文本和值进行操作 

+ API

| API方法       | 解释                                     |
| ------------- | ---------------------------------------- |
| val([value])  | 获得/设置表单项标签里面value属性相应的值 |
| text([value]) | 获得/设置标签体的文本内容                |
| html([value]) | 获得/设置标签体的内容                    |

+ 解释

```
val()         获得表单项标签里面value属性的值   value属性的封装  
val("参数")    给表单项标签value属性设置值

html()      获得标签的内容，如果有标签，一并获得。    相当于JavaScript里面的innerHTML
html("....)  设置html代码，如果有标签，将进行解析。


text()		获得标签的内容，如果有标签，忽略标签。    相当于JavaScript中的innerText
text("..."")	设置文本，如果含有标签，把标签当做字符串.不支持标
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>jquery操作标签的文本和值</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <input type="text" id="ipt" value="张三"/>
    <div id="city"><h1>北京</h1></div>
    <script>
        //获取输入框的value的值
        //val()方法如果不传入参数，表示获取value值
        //console.log($("#ipt").val())

        //设置输入框的value为李四
        //val()传入参数表示设置value
        //$("#ipt").val("李四")


        //获取id为city的div中的文本内容
        //text()方法不传入参数，表示获取标签体的文本
        //console.log($("#city").text())

        //获取id为city的div中的所有内容
        //html()方法不传入参数，表示获取标签体的所有内容
        //console.log($("#city").html())

        //text()方法传入参数，表示设置标签体的文本
        //$("#city").text("<h1>深圳</h1>")

        //html()方法传入参数，表示设置标签体的内容
        $("#city").html("<h1>深圳</h1>")
    </script>
</body>
</html>
```

## 7.2 jQuery创建,插入

+ API   

| API方法             | 解释                                     |
| ------------------- | ---------------------------------------- |
| $("A")              | 创建A元素对象                            |
| **append(element)** | 添加成最后一个子元素，两者之间是父子关系 |
| prepend(element)    | 添加成第一个子元素，两者之间是父子关系   |
| appendTo(element)   | 添加到父元素的内部最后面                 |
| prependTo(element)  | 添加到父元素的内部最前面                 |
| before(element)     | 添加到当前元素的前面，两者之间是兄弟关系 |
| after(element)      | 添加到当前元素的后面，两者之间是兄弟关系 |

+ 内部插入:父节点/子字点之间操作

```
	append		a.append(c), 将c插入到a的内部的后面;   添加最小的孩子
				<a>
					...
					<c></c>
				</a>
	prepend		a.prepend(c),将c插入到a的内部的前面;   添加最大的孩子
			
				<a>
					<c></c>
					...
				</a>
	
	---------------------------------------------------------------------------
	appendTo	a.appendTo(c), 将a插入到c的内部的后面;   最小的孩子认干爹 (沙悟净拜师)
	
				<c>
					...
					<a></a>
				</c>
	
	prependTo	a.prependTo(c),将a插入到c的内部的前面 ;  最大孩子认干爹 (孙悟空拜师)
	
				<c>
					<a></a>
					...
				</c>
```

+ 外部插入(了解): 兄弟节点之间操作

```
after   a.after(c);     哥哥找弟弟 八戒找沙师弟
		<a></a><c></c>  
before  a.before(c);    弟弟找哥哥 八戒找孙悟空
		<c></c><a></a>
```

## 7.3 jQuery移除节点(对象) 

+ API

| API方法  | 解释                       |
| -------- | -------------------------- |
| remove() | 删除指定元素(自己移除自己) |
| empty()  | 清空指定元素的所有子元素   |

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>jQuery创建、删除、添加标签</title>
    <script src="../../js/jquery-3.3.1.js"></script>
</head>
<body>
    <ul id="city">
        <li id="bj">北京</li>
        <li id="sh">上海</li>
        <li id="sz">深圳</li>
        <li id="gz">广州</li>
    </ul>
    <input type="button" value="添加" onclick="addCity()"><br>
    <input type="button" value="删除" onclick="removeCity()"><br>
    <input type="button" value="清空城市列表" onclick="removeAllCity()">
    <script>
        //目标1: 点击添加按钮，往城市列表中添加一个"长沙"
        function addCity() {
            //1. 创建一个长沙标签，并且指定id为"cs"
            //2. 将创建出来的那个长沙标签，添加到id为city的ul中
            //append()方法是往父标签的后面添加子标签
            //$("#city").append("<li id='cs'>长沙</li>")

            //appendTo()方法：子标签.appendTo(父标签)
            //$("<li id='cs'>长沙</li>").appendTo($("#city"))

            //prepend()方法是往父标签的前面添加子标签
            //$("#city").prepend("<li id='cs'>长沙</li>")

            //prependTo(): 子标签.prependTo(父标签)

            //兄弟标签.after(新标签)，将新标签添加到某个兄弟标签之后
            $("#sh").after("<li id='cs'>长沙</li>")

            //兄弟标签.before(新标签),将新标签添加到兄弟标签之前
        }

        function removeCity() {
            //目标: 删除上海
            $("#sh").remove()
        }

        //清空城市列表，但是要保留ul标签
        function removeAllCity() {
            //就是删除ul标签中的所有子标签
            //empty()清除某个标签的所有子标签
            $("#city").empty()
        }
    </script>
</body>
</html>
```

## 7.4 小结

### 7.4.1 操作值和内容

+ 操作值

```
val()  	 获得value
val(...) 设置value	
```

+ 操作内容

```
text()   获得标签里面的文本, 如果有标签, 忽略标签	
html()	 获得标签里面的标签字符串, 如果有标签 一并获得标签

text(...) 设置标签里面的文本 	  1.如果设置标签, 忽略标签 当做文本 2.新插入的会把前面的覆盖
html(...) 设置标签里面的htmt字符串 1.如果设置标签, 解析标签 2.新插入的会把前面的覆盖

```

### 7.4.2 创建和添加节点

+ 创建节点

```
$("A")
```

+ 添加节点

```html
1.父子之间的
a.append(c);   把c添加到a的内部的最后面
a.prepend(c);  把c添加到a的内部的最前面

a.appendTo(c);  把a添加到c的内部的最后面
a.prependTo(c); 把a添加到c的内部的最前面


2.兄弟之间的
a.after(c);    把c添加到a的后面
a.before(c);  把c添加到a的前面


```

### 7.4.3 移除节点

```
remove()  自己移除自己
empty()  清空当前标签里面的内容
```

# 8. JQ中事件的使用

## 8.1 基本事件的使用(重点)

+ 事件在jq里面都封装成了方法. 去掉了JS里面on语法:


```js
jq对象.事件方法名(function(){});

eg:点击事件
btn.click(function(){});
```

+ 点击事件


```html
<body>
    <input id="btnId" type="button" value="点我吧" />
</body>

<script>
    //jq对象.click(function(){})
    $("#btnId").click(function () {
        alert("hello...");
    });

</script>
```

+ 获得焦点和失去焦点

```html
<body>
    <input id="inputId"  type="text" value="hello.."/>
</body>

<script>
    //jq对象.focus(function(){})  获得焦点事件
    //this就是当前对象(是JS对象), 谁获得了焦点this就是谁
    $("#inputId").focus(function () {
        //this ----> document.getElementById("inputId");
        console.log("获得了焦点..."+this.value);
    });

    //jq对象.blur(function(){})  失去焦点事件
    $("#inputId").blur(function () {
        console.log("失去了焦点...");
    });

</script>
```


+ 内容改变事件 


```html
<body>
    <select id="starSelectId">
        <option value="Jordan">乔丹</option>
        <option value="James">詹姆斯</option>
        <option value="Kobe">科比</option>
        <option value="Iverson">艾弗森</option>
    </select>
</body>

<script>
    //jq对象.change(function(){}) 内容改变事件
    $("#starSelectId").change(function () {
        console.log("内容改变了..."+this.value);
    });
</script>
```


+ 鼠标相关的事件


```html
<body>
    <div  id="divId"></div>
</body>

<script>
    // jq对象.mouseover()  鼠标进入
    $("#divId").mouseover(function () {
        //this.style.backgroundColor = "red";
        $(this).css("backgroundColor","red");   //把js对象转成了jq对象
    });

    // jq对象.mouseout()  鼠标离开
    $("#divId").mouseout(function () {
        //this.style.backgroundColor = "blue";
        $(this).css("backgroundColor","blue");
    });

</script>
```


+ 键盘相关事件

```html
<body>
    <input id="inputId" type="text"/>
</body>

<script>

    //  jq对象.keydown()  键盘按下
    $("#inputId").keydown(function () {
        console.log("键盘按下...");
    });

    //  jq对象.keyup()  键盘抬起
    $("#inputId").keyup(function () {
        console.log("键盘抬起...");
    });
</script>
```

## 8.2 jQuery的事件绑定与解绑 

+ 事件的绑定

```JavaScript
jQuery元素对象.on(事件的类型,function(){} );
其中：事件名称是jQuery的事件方法的方法名称，例如：click、mouseover、mouseout、focus、blur等

eg:点击事件
-- 基本使用  jq对象.click(function(){})
-- 绑定发送  jq对象.on("click",function(){});
```

+ 事件的解绑

```JavaScript
jQuery元素对象.off(事件名称);

其中：参数事件名称如果省略不写，可以解绑该jQuery对象上的所有事件
```

+ 实例代码

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>jquery操作事件</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <input type="button" value="按钮" id="btn">

    <script>
        //目标: 给按钮绑定点击事件
        //绑定事件的方式: 1. 普通函数方式  2. js的匿名函数方式  3. jquery的匿名函数方式   4. jquery的on方法绑定事件

        //jQuery的匿名函数方式绑定事件
        $("#btn").click(function () {
            alert("点击了...")
        })

        //jQuery的on方法绑定事件
        /*$("#btn").on("click",function () {
            //事件触发后要调用的函数
            console.log("点击了....")
        })*/


        $("#btn").on("mouseover",function () {
            //事件触发后要调用的函数
            console.log("鼠标移入了....")
        })

        //jquery的off()方法可以解绑事件，但是它只能解绑jQuery绑定事件
        //off()方法，如果传入参数，表示解绑某种事件；off()方法没如果不传入参数，表示解绑所有事件
        $("#btn").off()
    </script>
</body>
</html>
```

## 8.3 事件切换(了解)  

+ 普通写法

```js
<script type="text/javascript">
	$(function() {
		$("#myDiv").mouseover(function() {
			console.log("鼠标移入了...")
		});
		$("#myDiv").mouseout(function() {
			console.log("鼠标移出了...")
		});
	});
</script>
```

+ 链式写法 

```js
<script type="text/javascript">
	$(function() {
		$("#myDiv").mouseover(function() {
			console.log("鼠标移入了...")
		}).mouseout(function() {
			 console.log("鼠标移出了...")
		});
	});
</script>
```

* hover方法

```js
<script>
    //jQuery的hover方法可以同时给标签绑定鼠标移入和移出事件
    $("#ipt").hover(function () {
          console.log("鼠标移入了...")
    },function () {
          console.log("鼠标移出了...")
    })
</script>
```

小结

1. JQ里面的事件, 去掉on, 封装成了方法. 语法:【重点】

```
jq对象.事件方法名(function(){})

掌握事件:
click(), focus(),blur(),change(),submit(), keydown(), keyup(),mousexxx()
```

2. 绑定和解绑

+ 绑定

```
jq对象.on(事件名,function(){})  注意:事件名不带(),eg:click
```

+ 解绑

```
jq对象.off(事件名)
```

# 9. JQ遍历(重点)

## 9.1 复习JS方式遍历

+ 语法

```js
for(var i=0;i<元素数组.length;i++){
  	元素数组[i];
}
```

+ eg

```
//定义了一个数组
var array = [1,2,3,4,"aa"];
//a. 使用原来的方式遍历
for(var i = 0; i < array.length;i++){
  console.log("array["+i+"]="+array[i]);
}
```

## 9.2 jquery对象方法遍历

+ 语法

```js
jquery对象.each(function(index,element){});

其中:(参数名字随便取的)
index:就是元素在集合中的索引
element：就是集合中的每一个元素对象
```

+ eg

```
//b 使用JQ来遍历 jquery对象.each(function(index,element){}); 第一个参数index 索引, 第二个参数element每个索引对应的值(参数的名字随意)
$(array).each(function (a,n) {
  console.log("array["+a+"]="+n);
});
```

## 9.3 jquery的全局方法遍历

+ 语法

```js
$.each(jquery对象,function(index,element){});

其中，
index:就是元素在集合中的索引
element：就是集合中的每一个元素对象
```

+ eg

```
        //c. 全局方式遍历 $.each(jquery对象,function(index,element){});  第一个参数index 索引, 第二个参数element每个索引对应的值(参数的名字随意)
        $.each($(array),function (a,n) {
            console.log("array["+a+"]="+n);
        });
```

## 9.4 for of语句遍历

+ 语法

```js
for(变量 of jquery对象){
  	变量；
}

其中，
变量:定义变量依次接受jquery数组中的每一个元素
jquery对象：要被遍历的jquery对象
```

+ eg

```
for(n of $(array)){
  console.log("n="+n);
}
```

小结

1. jq对象方法遍历【重点】

```
jq对象.each(function(i,ele){

});

参数1: 下标
参数2:下标对应的值
参数名字随便取
```

2. 全局方法遍历

```
$.each(jq对象,function(i,ele){

});

参数1: 下标
参数2:下标对应的值
参数名字随便取
```

3. jq3.0新特性

```
for(ele of jq对象){

}
```

# 10. JQ动画(了解)

## 10.1 基本效果

+ 方法

| 方法名称             | 解释                                         |
| -------------------- | -------------------------------------------- |
| show([speed],[fn]])  | 显示元素方法                                 |
| hide([speed,[fn]])   | 隐藏元素方法                                 |
| toggle([speed],[fn]) | 切换元素方法，显示的使之隐藏，隐藏的使之显示 |

+ 参数

| 参数名称 | 解释                                                         |
| -------- | ------------------------------------------------------------ |
| speed    | 三种预定速度之一的字符串("slow","normal", or "fast")或表示动画时长的毫秒数值(如：1000) |
| fn       | 在动画执行完毕的时候会调用的函数，每个元素执行一次           |

+ 实例

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>jQuery的动画</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <img id="mm" src="../img/mm.jpg" width="500px" height="500px">
    <br>
    <input type="button" onclick="showImg()" value="显示">
    <input type="button" onclick="hideImg()" value="隐藏">
    <input type="button" onclick="toggleImg()" value="切换">

    <script>
        function showImg() {
            //让图片显示: 方法1 show(动画执行的速度一般用毫秒值表示,动画执行完毕的时候要调用的函数)
            $("#mm").show(2000,function () {
                console.log("动画执行完毕了...")
            })

            //方法2：slideDown(),向下滑动
            //$("#mm").slideDown(2000)

            //方法3: fadeIn(), 淡入
            //$("#mm").fadeIn(2000)
        }

        function hideImg() {
            //让图片隐藏: 方法1 hide()
            $("#mm").hide(2000)

            //方法2: slideUp()向上滑动
            //$("#mm").slideUp(2000)

            //方法3: fadeOut(),淡出
            //$("#mm").fadeOut(2000)
        }

        function toggleImg() {
            //切换图片显示和隐藏: 方法1 toggle()
            $("#mm").toggle(2000)

            //方法2:slideToggle()
            //$("#mm").slideToggle(2000)

            // 方法3: fadeToggle()
            //$("#mm").fadeToggle(2000)
        }
    </script>
</body>
</html>
```

## 10.2 滑动效果

+ 方法

| 方法名称                  | 解释                                         |
| ------------------------- | -------------------------------------------- |
| slideDown([speed,[fn]])   | 向下滑动方法                                 |
| slideUp([speed,[fn]])     | 向上滑动方法                                 |
| slideToggle([speed],[fn]) | 切换元素方法，显示的使之隐藏，隐藏的使之显示 |

+ 参数

| 参数名称 | 解释                                                         |
| -------- | ------------------------------------------------------------ |
| speed    | 三种预定速度之一的字符串("slow","normal", or "fast")或表示动画时长的毫秒数值(如：1000) |
| fn       | 在动画完成时执行的函数，每个元素执行一次                     |

## 10.3 淡入淡出效果

+ 方法

| 方法名称                          | 解释                                         |
| --------------------------------- | -------------------------------------------- |
| fadeIn([speed,[easing],[fn]])     | 淡入显示方法                                 |
| fadeOut([speed,[easing],[fn]])    | 淡出隐藏方法                                 |
| fadeToggle([speed],[easing],[fn]) | 切换元素方法，显示的使之隐藏，隐藏的使之显示 |

+ 参数

| 参数名称 | 解释                                                         |
| -------- | ------------------------------------------------------------ |
| speed    | 三种预定速度之一的字符串("slow","normal", or "fast")或表示动画时长的毫秒数值(如：1000) |
| easing   | 用来指定切换效果，默认是"swing"，可用参数"linear"            |
| fn       | 在动画完成时执行的函数，每个元素执行一次                     |

# 第二章-JQ案例

## 案例:使用JQuery完成页面定时弹出广告 ##

### 一,需求分析 ###

![img](img/tu_18-1574242791512.png)

- 进入页面3s后弹出广告,3s后广告隐藏 

### 二,思路分析 ###

1. 在index.html的顶部定义一个广告区域, 设置隐藏
2. 创建定时任务,3s显示广告

```
setTimeout("showAd()",3000);
```

3. 创建showAd()

```
function showAd(){
	//1.展示广告
	//2.setTimeOut("hideAd()",3000);

}
```

4. 创建hideAd()

```
function hideAd(){
	//1.隐藏广告
}
```



###三,代码实现 ###

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>定时弹广告</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <div id="ad" style="display: none">
        <img src="../img/ad.jpg" width="100%" height="200">
    </div>
    <h1>主题页面的内容</h1>

    <script>
        //声明一个函数，用于显示广告
        function showAd(){
            //在这个方法中，使用jquery的动画，显示广告
            $("#ad").slideDown(3000,function () {
                //当广告显示出来之后，再隔三秒就隐藏广告
                setTimeout(hideAd,3000)
            })
        }

        //声明一个函数，用于隐藏广告
        function hideAd(){
            $("#ad").slideUp(3000)
        }

        //当页面加载3秒后，开始显示广告: setTimeout()
        setTimeout("showAd()",3000)
    </script>
</body>
</html>
```

### 四,小结

1. 创建定时任务, 3s展示广告

```
setTimeout("showAd()",3000);
```

2. 创建showAd()

```
function showAd(){
	//a.展示广告
	//b.setTimeout("hideAd()",3000);
}
```

3. 创建hideAd()

```
function hideAd(){
	//a.隐藏广告

}
```





## 案例:使用JQuery完成表格的隔行换色 ##

### 一,需求分析 ###

![img](img/tu_15.png)

### 二,思路分析

1. 使用筛选选择器, 匹配出奇数(odd)行, 设置背景色
2. 使用筛选选择器, 匹配出偶数(even)行, 设置背景色
3. 鼠标进入tr的时候, 当前tr的背景色改成red
4. 鼠标离开tr的时候, 当前tr的背景色还原



### 三,代码实现 ###



```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <script src="../js/jquery-3.3.1.js"></script>

    <style>
        .greenStyle{
            background-color: lawngreen;
        }

        .blueStyle{
            background-color: lightskyblue;
        }

        .redStyle{
            background-color: red;
        }
    </style>
</head>
<body>
<table id="tab1" border="1" width="800" align="center" >
    <tr>
        <td colspan="5"><input type="button" value="删除"></td>
    </tr>

    <tr>
        <th><input type="checkbox"></th>
        <th>分类ID</th>
        <th>分类名称</th>
        <th>分类描述</th>
        <th>操作</th>
    </tr>

    <tr>
        <td><input type="checkbox"></td>
        <td>1</td>
        <td>手机数码</td>
        <td>手机数码类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox"></td>
        <td>2</td>
        <td>电脑办公</td>
        <td>电脑办公类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox"></td>
        <td>3</td>
        <td>鞋靴箱包</td>
        <td>鞋靴箱包类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox"></td>
        <td>4</td>
        <td>家居饰品</td>
        <td>家居饰品类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
</table>
</body>
<script>
    //隔行换色: 其实就是设置奇数行为绿色，偶数行为蓝色
    $("tr:odd").addClass("greenStyle")
    $("tr:even").addClass("blueStyle")

    //给每行添加鼠标移入和鼠标移出事件，当鼠标移入的时候，将背景色变为红色，鼠标移出的时候，还原成原来的颜色
    //要变红色，其实就是给当前鼠标移入的那一行添加类名为"redStyle",要还原成原来的颜色，就是移除当前这一行的类名"redStyle"
    $("tr").mouseover(function () {
        //怎么拿到当前行: this
        $(this).addClass("redStyle")
    })

    $("tr").mouseout(function () {
        $(this).removeClass("redStyle")
    })
</script>
</html>
```

### 四,小结

## 案例:使用JQuery完成复选框的全选效果 ##

### 一,需求分析 ###

![img](img/tu_14.png)

1. 全选效果
2. 反选效果
3. 点击子选框控制全选框的选中效果

### 二.思路分析 ###

1. 全选效果: 设置子选框与全选框的checked属性一致

2. 反选效果: 将所有子选框点击一遍

3. 点击子选框控制全选框的选中效果: 如果所有子选框都选中那么全选框选中，否则全选框不选中


### 三,代码实现 ###

- js代码


```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
<table id="tab1" border="1" width="800" align="center" >
    <tr>
        <td colspan="5">
            <input type="button" value="反选" onclick="reverseSelect()">
        </td>
    </tr>
    <tr>
        <th>
            <input type="checkbox" id="all" onclick="selectAll(this)" >
        </th>
        <th>分类ID</th>
        <th>分类名称</th>
        <th>分类描述</th>
        <th>操作</th>
    </tr>
    <tr>
        <td><input type="checkbox" class="itemSelect"></td>
        <td>1</td>
        <td>手机数码</td>
        <td>手机数码类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox" class="itemSelect"></td>
        <td>2</td>
        <td>电脑办公</td>
        <td>电脑办公类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox" class="itemSelect"></td>
        <td>3</td>
        <td>鞋靴箱包</td>
        <td>鞋靴箱包类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
    <tr>
        <td><input type="checkbox" class="itemSelect"></td>
        <td>4</td>
        <td>家居饰品</td>
        <td>家居饰品类商品</td>
        <td><a href="">修改</a>|<a href="">删除</a></td>
    </tr>
</table>
</body>
<script>
    //全选: 给全选框绑定点击事件，设置所有子选框的选中状态和全选框一致
    function selectAll(obj) {
        //设置所有子选框的选中状态(checked属性)和全选框的checked一样
        $(".itemSelect").prop("checked",obj.checked)
    }

    //反选: 给反选按钮绑定点击事件: 设置所有子选框的checked属性和原来相反
    function reverseSelect() {
        //设置所有子选框的选中状态和原来的相反
        /*$(".itemSelect").each(function (index,element) {
            //element就是遍历出来的每一个子选框(js对象)
            element.checked = !element.checked
        })*/

        //将所有子选框点击一遍也能实现反选
        $(".itemSelect").click()
    }

    //点击子选框控制全选框的选中状态
    //当所有子选框都选中，那么全选框也要选中；但凡有一个子选框没有选中，全选框也不选中
    //关键是:怎么判断所有子选框都选中
    $(".itemSelect").click(function () {
        //最关键的一步: 判断是否所有子选框都选中
        //思路一: 只要有没选中的，那么就是false
        /*var flag = true
        $(".itemSelect").each(function (index,element) {
            if (!element.checked) {
                //就说明，有子选框未选中
                flag = false
            }
        })
        //设置全选框的选中状态为flag
        $("#all").prop("checked",flag)*/


        //思路二: 判断子选框的总个数和选中的子选框的总个数是否相同
        $("#all").prop("checked",$(".itemSelect").length == $(".itemSelect:checked").length)
    })
</script>
</html>
```

### 四,小结

1. 触发点: 最上面的复选框的点击事件
2. 把下面的复选框状态设置成和最上面的一样



## 案例:使用JQuery完成省市联动效果

### 一,需求分析

![img](img/tu_2.png)



### 二,思路分析

1. 创建页面, 初始化数据
2. 给省份的select设置内容改变事件

```
function(){
	//1.获得省份的数据
	//2.根据省份的数据 获得对应的城市的数据  eg:["深圳", "广州", "东莞", "惠州"]
	//3.遍历城市的数据, 拼接成<option>城市名字</option>
	//4.添加到右边的城市select里面
};
```

### 三,代码实现

- js代码


```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>省市二级联动</title>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
    <!--
        给省份的下拉框绑定change事件
    -->
    <select id="provinceSelect" onchange="changeProvince(this.value)">
        <option value="0">请选择省份</option>
        <option value="1">广东省</option>
        <option value="2">湖南省</option>
        <option value="3">湖北省</option>
    </select>
    <select id="citySelect">
        <option>请选择城市</option>
    </select>

    <script>
        var cities = [
            [],
            ["广州","深圳","惠州","东莞"],
            ["长沙","岳阳","常德","衡阳"],
            ["武汉","黄冈","宜昌","荆州"]
        ]

        function changeProvince(value){
            //当省份发生改变的时候，要获取当前省份的城市
            //value就是当前省份的城市列表的下标
            var items = cities[value];

            //遍历添加之前清除所有城市
            $("#citySelect").html("<option>请选择城市</option>")
            //遍历出每一个城市名，添加到城市下拉框中
            $.each(items,function (index,element) {
                //每一个element就是一个城市名
                $("#citySelect").append("<option>"+element+"</option>")
            })
        }
    </script>
</body>
</html>
```

### 四.小结

1. 遍历 jq对象.each(function(i,ele){})
2. 操作dom a.append(c)
3. 思路
   + 给省份的select设置内容改变事件
   + 获得省份的值
   + 根据省份的值 获得对应的城市的数据
   + 遍历城市的数据, 拼接成option
   + 添加到右边的select里面

## 扩展案例_电子时钟

### 1.需求

![image-20191217160708414](img/1.png) 

1. 点击开始按钮，那么在id为time的div中显示当前时间,并且让时间动起来
2. 点击暂停按钮，让时钟停止跳动

### 2.实现

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>电子时钟案例</title>
    <style>
        div{
            font-size: 30px;
            color: green;
        }
    </style>
    <script src="../js/jquery-3.3.1.js"></script>
</head>
<body>
<div id="time">

</div>

<input type="button" value="开始" onclick="startTime()">
<input type="button" value="暂停" onclick="pauseTime()">
<script>
    var id = null
    //1. 给开始按钮绑定点击事件，点击之后，在id为time的div中显示当前时间，并且要让时钟动起来
    function startTime() {
        var time = new Date().toLocaleString();
        $("#time").html(time)

        //每次开启一个定时器之前，先清除原来的定时器
        if (id != null) {
            clearInterval(id)
        }

        id = setInterval(function () {
            var time = new Date().toLocaleString();
            $("#time").html(time)
        },1000);
    }

    //2. 点击暂停按钮的时候，时钟停止
    function pauseTime() {
        //要让电子时钟停止，则。需要清除那个定时器
        clearInterval(id)
    }
</script>
</body>
</html>
```







