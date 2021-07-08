# 1. HTML入门

## 1.1 概述

**网页的构成**

- [HTML](https://developer.mozilla.org/zh-CN/docs/Web/HTML)：通常用来定义网页内容的含义和基本结构。
- [CSS](https://developer.mozilla.org/zh-CN/docs/Web/CSS)：通常用来描述网页的表现与展示效果。
- [JavaScript](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript)：通常用来执行网页的功能与行为。

**HTML**（超文本标记语言——HyperText Markup Language）是构成 Web 世界的一砖一瓦。它是一种用来告知浏览器如何组织页面的**标记语言**。

所谓`超文本Hypertext`，是指连接单个或者多个网站间的网页的链接。我们通过链接，就能访问互联网中的内容。

所谓`标记Markup` ，是用来注明文本，图片等内容，以便于在浏览器中显示，例如`<head>`,`<body>`等。

## 1.2 HTML组成

HTML页面由一系列的**元素（[elements](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element)）** 组成，而元素是使用**标签**创建的

### 1）标签

一对标签（ [tags](https://developer.mozilla.org/en-US/docs/Glossary/Tag)）可以设置一段文字样式，添加一张图片或者添加超链接等等。 例如：

```html
<h1>今天是个好日子</h1>
```

在HTML中，`<h1>`标签表示**标题**，那么，我们可以使用**开始标签**和**结束标签**包围文本内容，这样其中的内容就以标题的形式显示了。

### 2）属性

HTML标签可以拥有[属性](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Attributes)。**属性**提供了有关 HTML 元素的**更多的信息**。我们只能在开始标签中，加入属性。通常以`名称=值 `成对的形式出现，**比如：name='value'**。例如：

```html
<h1 align="center">今天是个好日子!!!</h1>
```

在HTML标签中，`align`  属性表示**水平对齐方式**，我们可以赋值为 `center`  表示 **居中** 。

## 1.3 入门案例

### 页面说明

1. '<!DOCTYPE html>'**声明文档类型**。规定了HTML页面必须遵从的良好规则，从HTML5后，`<!DOCTYPE html>`是最短的有效的文档声明。
2. '<html>'这个标签包裹了整个完整的页面，是一个**根元素（顶级元素）**。其他所有元素必须是此元素的后代，每篇HTML文档只有一个根元素。
3. '<head>'这个标签是一个容器，它包含了所有你想包含在HTML页面中但不想在HTML页面中显示的内容。这些内容包括你想在搜索结果中出现的关键字和页面描述，CSS样式，字符集声明等等。以后的章节能学到更多关于`<head>` 元素的内容。
4. '<body>'包含了文档内容，你访问页面时所有显示在页面上的文本，图片，音频，游戏等等。

## 1.4 小结

- HTML是一种**标记语言**，用来组织页面，使用元素和属性。
- **这个元素的主要部分有：**

1. **元素**（Element）：开始标签、结束标签与内容相结合，便是一个完整的元素。
2. **开始标签**（Opening tag）：包含元素的名称（本例为 p），被左、右角括号所包围。表示元素从这里开始或者开始起作用 —— 在本例中即段落由此开始。
3. **结束标签**（Closing tag）：与开始标签相似，只是其在元素名之前包含了一个斜杠。这表示着元素的结尾 —— 在本例中即段落在此结束。初学者常常会犯忘记包含结束标签的错误，这可能会产生一些奇怪的结果。
4. **内容**（Content）：元素的内容，本例中就是所输入的文本本身。
5. **属性**（Attribute）：标签的附加信息。

- **在学习HTML时，要抓住两个方面：**

1. 掌握标签所代表的含义。
2. 掌握在标签中加入的属性的含义。

# 2. HTML基本语法

## 2.1 注释

类似 <!--和-->

## 2.2 标签

### 2.2.1 空元素（自闭合）

不是所有元素都拥有开始标签，内容和结束标记。一些元素只有一个标签，叫做空元素。它是在开始标签中进行关闭的。例子如下：

```html
<br/> 换行
<hr/> 水平分隔线
```

### 2.2.2 嵌套元素

```html
<p>这是<b>第一个</b>页面</p>
```

### 2.2.3 块级和行内

#### 1）概念

在HTML中有两种重要元素类别，块级元素和内联元素。

- **块级元素**：

  **独占一行**。块级元素（block）在页面中以块的形式展现。相对于其前面的内容它会出现在新的一行，其后的内容也会被挤到下一行展现。比如`<p>` ，`<hr>`，`<li>` ，`<div>`等。

- **行内元素**

  **行内显示**。行内元素不会导致换行。通常出现在块级元素中并环绕文档内容的一小部分，而不是一整个段落或者一组内容。比如`<b>`，`<a>`，`<i>`，`<span>` 等。

  > 注意：一个块级元素不会被嵌套进内联元素中，但可以嵌套在其它块级元素中。

#### 2）div和span

- `<div>` 是一个通用的内容容器，并没有任何特殊语义。它可以被用来对其它元素进行分组，一般用于样式化相关的需求。它是一个**块级元素**。

- ` <span>` 是短语内容的通用行内容器，并没有任何特殊语义。它可以被用来编组元素以达到某种样式。它是一个**行内元素**。

  > 注意：div和span在页面布局中有重要作用。

## 2.3 关于属性

标签属性，主要用于拓展标签。属性包含元素的额外信息，这些信息不会出现在实际的内容中。但是可以改变标签的一些行为或者提供数据，属性总是以`name = value`的格式展现。

- 属性名：同一个标签中，属性名不得重复。

- 大小写：属性和属性值对大小写不敏感。不过W3C标准中，推荐使用小写的属性/属性值。

- 引号：双引号是最常用的，不过使用单引号也没有问题。

- 常用属性：

  | 属性名 | 作用                                                         |
  | ------ | ------------------------------------------------------------ |
  | class  | 定义元素类名，用来选择和访问特定的元素                       |
  | id     | 定义元素唯一标识符，在整个文档中必须是唯一的                 |
  | name   | 定义元素名称，可以用于提交服务器的表单字段                   |
  | value  | 定义在元素内显示的默认值                                     |
  | style  | 定义CSS样式，这些样式会覆盖之前设置的样式（第一天简单了解，第二天主要内容） |

## 2.4 特殊字符

在HTML中，字符 `<`, `>`,`"`,`'` 和 `&` 是特殊字符. 它们是HTML语法自身的一部分, 那么你如何将这些字符包含进你的文本中呢

| 原义字符 | 等价字符引用 |
| -------- | ------------ |
| <        | `&lt;`       |
| >        | `&gt;`       |
| "        | `&quot;`     |
| '        | `&apos;`     |
| &        | `&amp;`      |
| 空格     | `&nbsp;`     |

# 3. HTML案例-新闻文本

1. div布局的基本方式
2. 文本标签的基本使用

## 3.1 效果

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/1.png)

## 3.2 分析

### 3.2.1 div样式布局

在head标签中，通过style标签加入样式

基本格式:

```html
<style>
    标签名{
        属性名1:属性值1 属性值2 属性值3; 
        属性名2:属性值2;
        属性名3:属性值3;
    }
</style>
```

多样式格式：

一个属性名可以含有多个值，同时设置多样式

```html
<style>
    标签名{
        属性名:属性值1 属性值2 属性值3; 
    }
</style>
```

代码见案例一：01-样式演示.html

效果：

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/2.png)

### 3.2.2 文本标签

使用文本内容标签设置文字基本样式。

| 标签名 | 作用                                                         |
| ------ | ------------------------------------------------------------ |
| p      | 表示文本的一个段落                                           |
| h      | 表示文档标题，`<h1>–<h6>` ，呈现了六个不同的级别的标题，`<h1>` 级别最高，而 `<h6>` 级别最低 |
| hr     | 表示段落级元素之间的主题转换，一般显示为水平线               |
| li     | 表示列表里的条目。                                           |
| ul     | 表示一个无序列表，可含多个元素，无编号显示。                 |
| ol     | 表示一个有序列表，通常渲染为有带编号的列表                   |
| em     | 表示文本着重，一般用斜体显示                                 |
| strong | 表示文本重要，一般用粗体显示                                 |
| font   | 表示字体，可以设置样式（已过时）                             |
| i      | 表示斜体                                                     |
| b      | 表示加粗文本                                                 |

代码见案例一：02-文本标签演示.html

## 3.3 新闻文本案例

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/3.png)

代码见案例一：03-新闻文本案例.html

# 4. HTML案例-头条页面

## 4.1 分析

### 4.1.1 div的class值

使用class的值，格式：

```html
.class值{
    属性名:属性值;
}

<标签名 class="class值">  
 提示: class是自定义的值
```

所以，使用class属性值，可以帮助我们区分div，更加精确的设置标签的样式。

### 4.1.2 浮动布局和清除

1. 加入三部分div

```html
<div class="left">left</div>
<div class="center">center</div>
<div class="right">right</div>
```

2. 浮动布局

```html
/*左侧图片的div样式*/
.left{
    width: 20%;
    float: left;
    height: 500px;
}

/*中间正文的div样式*/
.center{
    width: 59%;
    float: left;
    height: 500px;
}

/*右侧广告图片的div样式*/
.right{
    width: 20%;
    float: left;
    height: 500px;
}
```

```html
/*底部超链接的div样式*/
.footer{
    /*清除浮动效果*/
    clear: both;
    /*文本对齐方式*/
    text-align: center;
    /*背景颜色*/
    background: blue;
}
```

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/4.png)

代码见01案例二：样式演示.html

### 4.1.3 图片标签

| 标签名  | 作用                         | 备注                                              |
| ------- | ---------------------------- | ------------------------------------------------- |
| **img** | 可以显示一张图片(本地或网络) | **src属性**，这是一个必需的属性，表示图片的地址。 |

其他属性：

| 属性名     | 作用                          | 备注 |
| ---------- | ----------------------------- | ---- |
| **title**  | 鼠标悬停（hover）时显示文本。 |      |
| **alt**    | 图形不显示时的替换文本。      |      |
| **height** | 图像的高度。                  |      |
| **width**  | 图像的宽度。                  |      |

代码见02案例二：图片标签演示.html

```html
<img src="../img/ad1.jpg" title="广告" alt="图片找不到啦" width="150px" height="150px"/>
```

### 4.1.4 超链接

| 标签名 | 作用         | 备注                                    |
| ------ | ------------ | --------------------------------------- |
| **a**  | 表示超链接。 | **href属性**，表示超链接指向的URL地址。 |

| 属性名 | 作用                                           |
| ------ | ---------------------------------------------- |
| target | 页面的打开方式(_self当前页   _blank新标签页)。 |

**去掉下划线**

根据某些样式的布局需求，去除下划线更为美观。

```css
a { 
    text-decoration:none;  // none 表示不显示
}
```

代码见03案例二：超链接标签演示.html

## 4.2 头条页面

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/5.png)

代码见04案例二：头条页面.html

# 5. HTML案例-登录页面

效果：

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/6.png)

## 5.1 表单标签

| 标签名   | 作用                                                         | 备注 |
| -------- | ------------------------------------------------------------ | ---- |
| **form** | 表示表单，是用来收集用户输入信息并向 Web 服务器提交的一个容器 |      |

举例：

```html
<form >
    //表单元素
</form>
```

**表单的属性**

| 属性名           | 作用                                                         | 备注                           |
| ---------------- | ------------------------------------------------------------ | ------------------------------ |
| **action**       | 处理此表单信息的Web服务器的URL地址                           |                                |
| **method**       | 提交此表单信息到Web服务器的方式                              | 可能的值有get和post，默认为get |
| **autocomplete** | 自动补全，指示表单元素是否能够拥有一个默认值，配合input标签使用 | HTML5                          |

简单示例：

```html
<!--get方式的表单-->
<form action="#" method="get" autocomplete="off">
    用户名：<input type="text" name="username"/>
    <button type="submit">提交</button>
</form>
```

代码见02案例三：表单标签演示.html

## 5.2 表单内元素

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/5=7.png)

代码见03案例三：表单项标签演示.html

```html
<form action="#" method="get" autocomplete="off">
    <label for="username">用户名：</label>
    <input type="text" id="username" name="username" value="" placeholder=" 请在此处输入用户名" required/>
    <button type="submit">提交</button>
    <button type="reset">重置</button>
    <button type="button">按钮</button>
</form>
```

## 5.3 type属性

| 属性值   | 作用                                                         | 备注  |
| -------- | ------------------------------------------------------------ | ----- |
| text     | 单行文本字段                                                 |       |
| password | 单行文本字段，值被遮盖                                       |       |
| email    | 用于编辑 e-mail 的字段，可以对e-mail地址进行简单校验         | HTML5 |
| radio    | 单选按钮。 1. 在同一个”单选按钮组“中，所有单选按钮的 name 属性使用同一个值；一个单选按钮组中是，同一时间只有一个单选按钮可以被选择。 2. 必须使用 value 属性定义此控件被提交时的值。 3. 使用checked 必须指示控件是否缺省被选择。 |       |
| checkbox | 复选框。 1. 必须使用 value 属性定义此控件被提交时的值。 2. 使用 checked 属性指示控件是否被选择。 3. 选中多个值时，所有的值会构成一个数组而提交到Web服务器 |       |

HTML5新增的type值

| 属性值         | 作用                                                         | 备注                                                         |
| -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| date           | HTML5 用于输入日期的控件                                     | 年，月，日，不包括时间                                       |
| time           | HTML5 用于输入时间的控件                                     | 不含时区                                                     |
| datetime-local | HTML5 用于输入日期时间的控件                                 | 不包含时区                                                   |
| number         | HTML5 用于输入浮点数的控件                                   |                                                              |
| range          | HTML5 用于输入不精确值控件                                   | max-规定最大值<br/>min-规定最小值 <br/>step-规定步进值 <br/>value-规定默认值 |
| search         | HTML5 用于输入搜索字符串的单行文本字段                       | 可以点击`x`清除内容                                          |
| tel            | HTML5 用于输入电话号码的控件                                 |                                                              |
| url            | HTML5 用于编辑URL的字段                                      | 可以校验URL地址格式                                          |
| file           | 此控件可以让用户选择文件，用于文件上传。                     | 使用 accept 属性可以定义控件可以选择的文件类型               |
| hidden         | 此控件用户在页面上不可见，但它的值会被提交到服务器，用于传递隐藏值 |                                                              |

```html
<form action="#" method="get" autocomplete="off">
    <label for="username">用户名：</label>
    <input type="text" id="username" name="username"/>  <br/>

    <label for="password">密码：</label>
    <input type="password" id="password" name="password"/> <br/>

    <label for="email">邮箱：</label>
    <input type="email" id="email" name="email"/> <br/>

    <label for="gender">性别：</label>
    <input type="radio" id="gender" name="gender" value="men"/>男
    <input type="radio" name="gender" value="women"/>女
    <input type="radio" name="gender" value="other"/>其他<br/>

    <label for="hobby">爱好：</label>
    <input type="checkbox" id="hobby" name="hobby" value="music" checked/>音乐
    <input type="checkbox" name="hobby" value="game"/>游戏 <br/>

    <label for="birthday">生日：</label>
    <input type="date" id="birthday" name="birthday"/> <br/>

    <label for="time">当前时间：</label>
    <input type="time" id="time" name="time"/> <br/>

    <label for="insert">注册时间：</label>
    <input type="datetime-local" id="insert" name="insert"/> <br/>

    <label for="age">年龄：</label>
    <input type="number" id="age" name="age"/> <br/>

    <label for="range">心情值(1~10)：</label>
    <input type="range" id="range" name="range" min="1" max="10" step="1"/> <br/>

    <label for="search">可全部清除文本：</label>
    <input type="search" id="search" name="search"/> <br/>

    <label for="tel">电话：</label>
    <input type="tel" id="tel" name="tel"/> <br/>

    <label for="url">个人网站：</label>
    <input type="url" id="url" name="url"/> <br/>

    <label for="file">文件上传：</label>
    <input type="file" id="file" name="file"/> <br/>

    <label for="hidden">隐藏信息：</label>
    <input type="hidden" id="hidden" name="hidden" value="itheima"/> <br/>

    <button type="submit">提交</button>
    <button type="reset">重置</button>
</form>
```

效果：

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/5=8.png)

代码见04案例三：表单项标签type属性演示.html

## 5.4 其他表单属性

| 标签名       | 作用                                              | 备注                            |
| ------------ | ------------------------------------------------- | ------------------------------- |
| **select**   | 表单的控件，下拉选项菜单                          | 与option配合实用                |
| **optgroup** | option的分组标签                                  | 与option配合实用                |
| **option**   | select的子标签，表示一个选项                      |                                 |
| **textarea** | 表示多行纯文本编辑控件                            | rows表示行高度， cols表示列宽度 |
| **fieldset** | 用来对表单中的控制元素进行分组(也包括 label 元素) |                                 |
| **legend**   | 用于表示它的**fieldset**内容的标题。              | **fieldset** 的子元素           |

```html
<form action="#" method="get" autocomplete="off">
    所在城市：<select name="city">
        <option>---请选择城市---</option>
        <optgroup label="直辖市">
            <option>北京</option>
            <option>上海</option>
        </optgroup>
    <optgroup label="省会市">
        <option>杭州</option>
        <option>武汉</option>
    </optgroup>
</select>
    <br/>

    个人介绍：<textarea name="desc" rows="5" cols="20"></textarea>

    <button type="submit">提交</button>
    <button type="reset">重置</button>
</form>
```

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.30/pics/5=9.png)

代码见05案例三：其他常用表单项标签演示.html

登录页面见代码06案例三：注册页面.html