# 1. CSS入门

## 1.1 概述

**CSS** (层叠样式表——Cascading Style Sheets，缩写为 **CSS**），简单的说，它是用于设置和布局网页的计算机语言。会告知浏览器如何渲染页面元素。例如，调整内容的字体，颜色，大小等样式，设置边框的样式，调整模块的间距等。

## 1.2 CSS的组成

CSS是一门基于规则的语言 — 你能定义用于你的网页中**特定元素**的一组**样式规则**。这里面提到了两个概念，一是特定元素，二是样式规则。对应CSS的语法，也就是**选择器（*selects*）**和**声明（*eclarations*）**。

- **选择器**：选择 HTML元素的方式。可以使用标签名，class值，id值等多种方式。
- **声明**：形式为**属性(property):值(value)**，用于设置特定元素的属性信息，即具体样式。
  - 属性：指示文体特征，例如`font-size`，`width`，`background-color`。
  - 值：每个指定的属性都有一个值，该值指示您如何更改这些样式。

格式：

```css
选择器 {
    属性名:属性值;
    属性名:属性值;
    属性名:属性值;
}
```

举例：

```css
h1 {
    color: red;
    font-size: 5px;
}
```

## 1.3 入门案例

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>入门案例</title>
    <style>
        <!--CSS-->
        h1{
            color: red;
            font-size: 100px;
        }
    </style>
</head>
<body>
    <h1>今天开始变漂亮！</h1>
</body>
</html>
```

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/1.png)

## 1.4 小结

**CSS**是对**HTML**的补充，指定页面如何展示的语言。

CSS的主要部分有：

1. 选择器：用来选择页面元素的方式。
2. 声明：用来设置样式，格式`属性名：值`。

在学习CSS时，要抓住两个方面：

1. 掌握多种选择器，能够灵活的选择页面元素。
2. 掌握样式的声明，能够使用多种属性来设置多样的效果。

# 2. 基本语法

## 2.1 引入方式

### 2.1.1 内联样式

> 了解,几乎不用,维护艰难

内联样式是CSS声明在元素的`style`属性中，仅影响一个元素：

- **格式**：

```html
<标签 style="属性名:属性值; 属性名:属性值;">内容</标签>
```

- **举例**：

```html
<h1 style="color: blue;background-color: yellow;border: 1px solid black;">
    Hello World!
</h1>
```

### 2.1.2 内部样式

内部样式表是将CSS样式放在[`style`](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/style)标签中，通常`style标签`编写在HTML 的[`head`](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/head)标签内部。（本文件有效）

- **格式**：

```html
<head>
    <style>
        选择器 {
            属性名: 属性值;
            属性名: 属性值;
        }
    </style>
</head>
```

- **举例**：

```html
<head>
    <meta charset="UTF-8">
    <title>引入方式2</title>
    <!--内部样式-->
    <style>
        div{
            color: red;
            font-size: 20px;
        }
    </style>
</head>
```

效果：

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/2.png)

代码见03-引入方式2.html

### 2.1.3 外部样式

外部样式表是CSS附加到文档中的最常见和最有用的方法，因为您可以将CSS文件链接到多个页面，从而允许您使用相同的样式表设置所有页面的样式。

外部样式表是指将CSS编写在扩展名为`.css` 的单独文件中，并从HTML`<link>` 元素引用它，通常`link标签`编写在HTML 的[`head`](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/head)标签内部。：

- **格式**：

```html
<link rel="stylesheet" href="css文件">

rel：表示“关系 (relationship) ”，属性值指链接方式与包含它的文档之间的关系，引入css文件固定值为stylesheet。

href：属性需要引用某文件系统中的一个文件。
```

示例：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>引入方式3</title>
    <!--外部样式-->
    <link rel="stylesheet" href="css/01.css"/>
</head>
<body>
    <div>div1</div>
    <div>div2</div>
</body>
</html>
```

## 2.2 注释

和Java一样

## 2.3 选择器

### 2.3.1 基本选择器

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/3.png)

#### 1）元素选择器

**页面元素：**

```html
<div>
  <ul>
    <li>List item 1</li>
    <li>List item 2</li>
    <li>List item 3</li>
  </ul>
  <ol>
    <li>List item 1</li>
    <li>List item 2</li>
    <li>List item 3</li>
  </ol>
</div>
```

**选择器：**

```css
选择所有li标签,背景变成蓝色
li{
    background-color: aqua;
}
```

#### 2）类选择器

**页面元素：**

```html
<div>
    <ul>
        <li class="ulist l1">List item 1</li>
        <li class="l2">List item 2</li>
        <li class="l3">List item 3</li>
    </ul>
    <ol>
        <!--class 为两个值-->
        <li class="olist l1">List item 1</li>
        <li class="olist l2">List item 2</li>
        <li class="olist l3">List item 3</li>
    </ol>
</div>
```

**选择器：**

```css
选择class属性值为l2的,背景变成蓝色
.l2{
    background-color: aqua;
}
选择class属性值为olist l2的,字体为白色
.olist.l2{
   color: wheat;
}
```

#### 3）ID选择器

**页面元素：**

```html
<div>
    <ul>
        <li class="l1" id="one">List item 1</li>
        <li class="l2" id="two">List item 2</li>
        <li class="l3" id="three">List item 3</li>
    </ul>
    <ol>
        <li class="l1" id="four">List item 1</li>
        <li class="l2" id="five">List item 2</li>
        <li class="l3" id="six">List item 3</li>
    </ol>
</div>
```

**选择器：**

```css
#two{
    background-color: aqua;
}
```

代码见06-基本选择器.html

### 2.3.2 属性选择器

**选择器和效果图，示例1**

```css
[属性名]{ }
```

**选择器和效果图，示例2**

```css
标签名[属性名]{ }
```

**选择器和效果图，示例3**

```css
标签名[属性名='属性值']{ }
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>属性选择器</title>
    <style>
        [type]{
            color: red;
        }

        [type=password]{
            color: blue;
        }
    </style>
</head>
<body>
    用户名：<input type="text"/> <br/>
    密码：<input type="password"/> <br/>
    邮箱：<input type="email"/> <br/>
</body>
</html>
```

代码见07-属性选择器.html

### 2.3.3 伪类选择器

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/4.png)

伪类选择器，用于选择处于特定状态的元素，例如，一些元素中的第一个元素，或者某个元素被鼠标指针悬停。

格式：

```css
标签名:伪类名{ }
```

常用伪类:

- 锚伪类

  在支持 CSS 的浏览器中，链接的不同状态都可以以不同的方式显示

  ```css
  a:link {color:#FF0000;} 	/* 未访问的链接 */
  a:visited {color:#00FF00;} 	/* 已访问的链接 */
  a:hover {color:#FF00FF;} 	/* 鼠标悬停链接 */
  a:active {color:#0000FF;} 	/* 已选中的链接 */
  ```

  > 注意：
  >
  > 伪类顺序 link ，visited，hover，active，否则有可能失效。

  代码示例：

  ```html
  HTML 代码 ： 
  <div>
      <a class="red" href="http://www.itheima.com">黑马</a> <br/>
      <a class="blue" href="http://www.itheima.com">传智</a>
  </div>
  
  
  CSS 代码 ： 
  /* 选择a标签,class值为red ,设置访问后为红色链接*/
  a.red:visited {
      color: red;
  }
  ```

代码见08-伪类选择器.html

```html
<style>
    a{
        text-decoration: none;
    }

    /*未访问的状态*/
    a:link{
        color: black;
    }

    /*已访问的状态*/
    a:visited{
        color: blue;
    }

    /*鼠标悬浮的状态*/
    a:hover{
        color: red;
    }

    /*已选中的状态*/
    a:active{
        color: yellow;
    }
</style>
```

### 2.3.4 组合选择器

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/5.png)

**页面元素：**

```html
<div>
    <ul class="l1">
        <li>List item 1</li>
        <li>List item 2</li>
        <li>List item 3</li>
        <ul class="l2">
            <li id="four">List item 1</li>
            <li id="five">List item 2</li>
            <li id="six">List item 3</li>
        </ul>
    </ul>
</div>
```

#### 1）后代选择器

**选择器：**

```css
.l1 li{
    background-color: aqua;
}
```

#### 2）子级选择器

**选择器：**

```css
.l1 > li{
    background-color: aqua;
}
```

#### 3）同级选择器

**选择器：**

```css
.l1 ~ li{
    background-color: aqua;
}
```

#### 4）相邻选择器

**选择器：**

```css
.l1 + li{
    background-color: aqua;
}
```

代码见09-组合选择器.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>组合选择器</title>
    <style>
        /*后代选择器*/
        .center li{
            color: red;
        }

        /*分组选择器*/
        span,p{
            color: blue;
        }
    </style>
</head>
<body>
    <div class="top">
        <ol>
            <li>aa</li>
            <li>bb</li>
        </ol>
    </div>

    <div class="center">
        <ol>
            <li>cc</li>
            <li>dd</li>
        </ol>
    </div>

    <span>span</span> <br/>
    <p>段落</p>
</body>
</html>
```

## 2.4 总结

**选择器的分类**：

| 分类       | 名称       | 符号   | 作用                                                         | 示例         |
| ---------- | ---------- | ------ | ------------------------------------------------------------ | ------------ |
| 基本选择器 | 元素选择器 | 标签名 | 基于标签名匹配元素                                           | div{ }       |
|            | 类选择器   | `.`    | 基于class属性值匹配元素                                      | .center{ }   |
|            | ID选择器   | `#`    | 基于id属性值匹配元素                                         | #username{ } |
| 属性选择器 | 属性选择器 | `[]`   | 基于某属性匹配元素                                           | [type]{ }    |
| 伪类选择器 | 伪类选择器 | `:`    | 用于向某些选择器添加特殊的效果                               | a : hover{ } |
| 组合选择器 | 后代选择器 | `空格` | 使用`空格符号`结合两个选择器，基于第一个选择器，匹配第二个选择器的所有后代元素 | .top li{ }   |
|            | 子级选择器 | `>`    | 使用 `>` 结合两个选择器，基于第一个选择器，匹配第二个选择器的直接子级元素 | .top > li{ } |
|            | 同级选择器 | `~`    | 使用 `~` 结合两个选择器，基于第一个选择器，匹配第二个选择器的所有兄弟元素 | .l1 ~ li{ }  |
|            | 相邻选择器 | `+`    | 使用 `+` 结合两个选择器，基于第一个选择器，匹配第二个选择器的相邻兄弟元素 | .l1 + li{ }  |
|            | 通用选择器 | `*`    | 匹配文档中的所有内容                                         | *{ }         |

1. CSS的引入方式有三种，建议使用外部样式表。
2. 注释类似于java多行注释。
3. 选择器是CSS的重要部分：
   1. 基本选择器：可以通过元素，类，id来选择元素。
   2. 属性选择器：可以通过属性值选择元素
   3. 伪类选择器：可以指定元素的某种状态，比如链接
   4. 组合选择器：可以组合基本选择器，更加精细的划分如何选择

…………暂时了解为主

# 3 头条页面

## 3.1 边框

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/6.png)

代码见02案例一：文本样式演示.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文本样式演示</title>
    <style>
        div{
            /*文本颜色*/
            color: /*red*/ #ff0000;

            /*字体*/
            font-family: /*宋体*/ 微软雅黑;

            /*字体大小*/
            font-size: 25px;

            /*下划线  none：无  underline：下划线  overline：上划线  line-through：删除线*/
            text-decoration: none;

            /*水平对齐方式  left：居左  center：居中  right：居右*/
            text-align: center;

            /*行间距*/
            line-height: 60px;
        }


        span{
            /*文字垂直对齐  top：居上   bottom：居下  middle：居中   百分比*/
            vertical-align: 50%;
        }
    </style>
</head>
<body>

    <div>
        我是文字
    </div>
    <div>
        我是文字
    </div>

    <img src="../img/wx.png" width="38px" height="38px"/>
    <span>微信</span>
</body>
</html>
```

## 3.2 文本样式

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/7.png)

代码见02案例一：文本样式演示.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文本样式演示</title>
    <style>
        div{
            /*文本颜色*/
            color: /*red*/ #ff0000;

            /*字体*/
            font-family: /*宋体*/ 微软雅黑;

            /*字体大小*/
            font-size: 25px;

            /*下划线  none：无  underline：下划线  overline：上划线  line-through：删除线*/
            text-decoration: none;

            /*水平对齐方式  left：居左  center：居中  right：居右*/
            text-align: center;

            /*行间距*/
            line-height: 60px;
        }


        span{
            /*文字垂直对齐  top：居上   bottom：居下  middle：居中   百分比*/
            vertical-align: 50%;
        }
    </style>
</head>
<body>

    <div>
        我是文字
    </div>
    <div>
        我是文字
    </div>

    <img src="../img/wx.png" width="38px" height="38px"/>
    <span>微信</span>
</body>
</html>
```

## 3.3 头条

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/8.png)

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/9.png)

news.css

```css
/*顶部样式*/
.top{
    background: black;  /*背景色*/
    line-height: 40px;  /*行高*/
    text-align: right;  /*文字水平右对齐*/
    font-size: 20px;    /*字体大小*/
}
/*顶部超链接样式*/
.top a{
    color: white;   /*超链接颜色*/
}

/*导航条样式*/
.nav{
    line-height: 40px;  /*行高*/
}

/*左侧分享样式*/
.left{
    width: 20%;     /*宽度*/
    text-align: center; /*文字水平居中对齐*/
    float: left;    /*浮动*/
}
/*左侧分享图片样式*/
.left img{
    width: 38px;
    height: 38px;
}
/*左侧文字*/
.left span{
    vertical-align: 50%;    /*文字垂直居中对齐*/
}

/*中间正文样式*/
.center{
    width: 60%; /*宽度*/
    float: left;    /*浮动*/
}

/*右侧广告图片样式*/
.right{
    width: 20%; /*宽度*/
    float: left;    /*浮动*/
}

/*底部页脚超链接样式*/
.footer{
    clear: both;    /*清除浮动*/
    background: blue;   /*背景色*/
    text-align: center; /*文字水平居中对齐*/
    line-height: 25px;
}

/*底部页脚超链接文字颜色*/
.footer a{
    color: white;
}

/*超链接样式*/
a{
    text-decoration: none;  /*去除下划线*/
    color: black;           /*超链接颜色*/
}
/*超链接悬浮样式*/
a:hover{
    color: red; /*悬浮颜色*/
}
```

代码见03案例一：头条页面.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>头条页面</title>
    <link rel="stylesheet" href="../css/news.css"/>
</head>
<body>
    <!--顶部登录注册更多-->
    <div class="top">
        <a href="../案例二/04案例二：登录页面.html" target="_self">登录&nbsp;&nbsp;</a>
        <a href="#">注册&nbsp;&nbsp;</a>
        <a href="#">更多&nbsp;&nbsp;</a>
    </div>

    <!--导航条-->
    <div class="nav">
        <img src="../img/logo.png"/>
        <a href="#">首页&nbsp;&nbsp;</a>/
        <a href="#">科技&nbsp;&nbsp;</a>/
        <font color="gray">正文</font>
        <hr/>
    </div>

    <!--左侧分享-->
    <div class="left">
        <a href="#"> <img src="../img/cc.png"/> <span>&nbsp;评论</span> </a>
        <hr/>
        <a href="#"> <img src="../img/repost.png"/> <span>&nbsp;转发</span> </a>  <br/>
        <a href="#"> <img src="../img/weibo.png"/> <span>&nbsp;微博</span> </a>  <br/>
        <a href="#"> <img src="../img/qq.png"/> <span>&nbsp;空间</span> </a>  <br/>
        <a href="#"> <img src="../img/wx.png"/> <span>&nbsp;微信</span> </a>  <br/>
    </div>

    <!--中间正文-->
    <div class="center">
        <div>
            <h1>支付宝特权福利！芝麻分600以上用户惊喜，网友：幸福来得突然？</h1>
        </div>

        <!--作者信息-->
        <div>
            <i><font size="2" color="gray">作者：itheima 2088-08-08</font></i>
            <hr/>
        </div>

        <!--副标题-->
        <div>
            <h3>支付宝特权福利！芝麻分600以上用户惊喜，网友：幸福来得突然？</h3>
        </div>

        <!--正文内容-->
        <div>
            <p>这些年，马云的风头正盛，但是上个月他毅然辞去了阿里巴巴的职务。而马云所做的很多事情也的确改变了这个世界，特别是在移动支付领域，更是走在了世界的前列。如今中国的移动支付已经成为老百姓的必备，支付宝对中国社会的变革都带来了深远的影响。不过马云依然没有满足，他认为移动互联网将会成为人类的基础设施，而且这里面的机会和各种挑战还非常多。</p>
            <p>支付宝的诞生就是为了解决淘宝网的客户们的买卖问题，而随着支付宝的用户的不断增加，支付宝也推出了一系列的附加功能。比如生活缴费、转账汇款、还信用卡、车主服务、公益理财等，往简单的说，支付宝既可以满足人们的日常生活，又可以利用芝麻信用进行资金周转服务。除了芝麻分能够进行周转以外，互联网信用体系下的产品多多，我们对比以下几个产品看看区别:
            <ol>
                <li>蚂蚁借呗，芝麻分600并且受到邀请开通福利，这个就是支付宝贷款，直接秒杀了银行贷款和线下金融公司，是现在支付宝用户使用最多的。</li>
                <li>微粒贷：于2015年上线，主要面向QQ和微信征信极好的用户而推出，受到邀请才能申请开通，额度最高有30万，难度较大</li>
                <li>蚂蚁巴士：这个在微信 蚂蚁巴士 公众平台申请,对于信用分要求530分以上才可以,额度1-30万不等，目前非常火爆</li>
            </ol>
            <img src="../img/1.jpg" width="100%"/>
            </p>
            <p>说起支付宝中的芝麻信用功能，相信更是受到了许多人的推崇，因为随着自己使用的不断增多，信用分会慢慢提高，而达到了一个阶段，就可以获得许多的福利。而当我们的芝麻信用分可以达到600分以上的时候，会有令我们想象不到的惊喜，接下来就让我们一起来看看，具体都有哪些惊喜吧。</p>
            <p><b>一、芝麻分600以上福利之信用购。</b>网购相信大家都不陌生，但是很多时候，网购都有一个通病，就是没办法试用，导致很多人买了很多自己不喜欢的东西。但是只要你的支付宝芝麻分在650及以上，就能立马享有0元下单，收到货使用满意了再进行付款。还能享用美食的专属优惠，是不是很耐斯</p>
            <p><b>二、芝麻分600以上福利之信用免押。</b>芝麻信用与木鸟短租联合推出信用住宿服务，芝麻分600及以上的用户可享受免押入住特权。木鸟短租拥有全国50万套房源，是国内领先的短租民宿预订平台。包括大家知道的飞猪信用住，大部分酒店可以免押金入住，离店再交钱。</p>
            <img src="../img/2.jpg" width="100%"/>
            <p><b>三、芝麻分600以上福利之国际驾照。</b>我们经常听说的可能只是中国驾照，但现在芝麻分已经应用到了国际领域，只要你的芝麻分够550就可以免费办理国际驾照，也有不少人非常佩服马云，一个简单的芝麻分居然有如此大的功能，也从侧面反应出来马云在国际上的地位，这个国际驾照是由新西兰、德国、澳大利亚联合认证，可以在全球200多个国家通行，相信大家一定都有一个自驾全球的梦想吧，而现在支付宝就给了你一把钥匙，剩下的就你自己搞定了！有没有想带着你的女神来一次浪漫之旅呢？</p>
            <p>随着互联网对我们生活的改变越来越大，信用这一词也被大家推上风口浪尖，不论是生活出行，还是其他的互联网服务，与信用体系已经密不可分了，马云当初说道，找老婆需要拼芝麻分，如今似乎也要成为现实，那么你们的芝麻分有多少了呢？</p>
        </div>
    </div>

    <!--右侧广告图片-->
    <div class="right">
        <img src="../img/ad1.jpg" width="100%"/>
        <img src="../img/ad2.jpg" width="100%"/>
        <img src="../img/ad3.jpg" width="100%"/>
        <img src="../img/ad1.jpg" width="100%"/>
        <img src="../img/ad2.jpg" width="100%"/>
        <img src="../img/ad3.jpg" width="100%"/>
    </div>

    <!--底部页脚超链接-->
    <div class="footer">
        <a href="#">关于黑马</a> &nbsp;
        <a href="#">帮助中心</a> &nbsp;
        <a href="#">开放平台</a> &nbsp;
        <a href="#">诚聘英才</a> &nbsp;
        <a href="#">联系我们</a> &nbsp;
        <a href="#">法律声明</a> &nbsp;
        <a href="#">隐私政策</a> &nbsp;
        <a href="#">知识产权</a> &nbsp;
        <a href="#">廉政举报</a> &nbsp;
    </div>

</body>
</html>
```

# 4. 登录页面

## 4.1 表格

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/10.png)

代码见01案例二：表格标签演示.html：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>表格标签演示</title>
</head>
<body>
    <!--
        表格标签：<table>
        属性：
            width-宽度
            height-高度
            border-边框
            align-对齐方式

        行标签：<tr>
        属性：
            align-对齐方式

        单元格标签：<td>
        属性：
            rowspan-合并行
            colspan-和并列

        表头标签：<th> 自带居中和加粗效果

        表头语义标签：<thead>
        表体语义标签：<tbody>
        表脚语义标签：<tfoot>
    -->
    <table width="400px" border="1px" align="center">
        <thead>
            <tr>
                <th>姓名</th>
                <th>性别</th>
                <th>年龄</th>
                <th>数学</th>
                <th>语文</th>
            </tr>
        </thead>

        <tbody>
            <tr align="center">
                <td>张三</td>
                <td rowspan="2">男</td>
                <td>23</td>
                <td colspan="2">90</td>
                <!--<td>90</td>-->
            </tr>

            <tr align="center">
                <td>李四</td>
                <!--<td>男</td>-->
                <td>24</td>
                <td>95</td>
                <td>98</td>
            </tr>
        </tbody>

        <tfoot>
            <tr>
                <td colspan="4">总分数：</td>
                <!--<td></td>
                <td></td>
                <td></td>-->
                <td>373</td>
            </tr>
        </tfoot>
    </table>
</body>
</html>
```

效果：

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/11.png)

## 4.2 样式控制

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/12.png)

代码见02案例二：样式演示.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>样式演示</title>
    <style>
        /*背景图片重复  no-repeat：不重复  repeat-x：水平重复  repeat-y：垂直重复   repeat：水平+垂直重复*/
        body{
            background: url("../img/bg.jpg");

            background-repeat: repeat;
        }

        /*轮廓控制 double：双实线   dotted：圆点   dashed：虚线   none：无*/
        input{
            outline: none;
        }

        /*元素显示  inline：内联元素(无换行、无长宽)   block：块级元素(有换行)  inline-block：内联元素(有长宽)  none：隐藏元素*/
        div{
            display: inline-block;
            width: 100px;
        }
    </style>
</head>
<body>
    用户名：<input type="text"/> <br/>

    <div>春季</div>
    <div>夏季</div>
    <div>秋季</div>
    <div>冬季</div>
</body>
</html>
```

效果：

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/13.png)

## 4.3 盒子模型

万物皆"盒子"。盒子模型是通过设置**元素框**与**元素内容**和**外部元素**的边距，而进行布局的方式

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.31/pics/14.png)

