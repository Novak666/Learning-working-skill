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
    <style>
      h1 {
        color: blue;
        background-color: yellow;
        border: 1px solid black;
      }
    </style>
  </head>
```

### 2.1.3 外部样式

外部样式表是CSS附加到文档中的最常见和最有用的方法，因为您可以将CSS文件链接到多个页面，从而允许您使用相同的样式表设置所有页面的样式。

外部样式表是指将CSS编写在扩展名为`.css` 的单独文件中，并从HTML`<link>` 元素引用它，通常`link标签`编写在HTML 的[`head`](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/head)标签内部。：

- **格式**：

```html
<link rel="stylesheet" href="css文件">

rel：表示“关系 (relationship) ”，属性值指链接方式与包含它的文档之间的关系，引入css文件固定值为stylesheet。

href：属性需要引用某文件系统中的一个文件。
```

## 2.2 注释

和Java一样

## 2.3 选择器

### 2.3.1 基本选择器

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

### 2.3.3 伪类选择器

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

### 2.3.4 组合选择器

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