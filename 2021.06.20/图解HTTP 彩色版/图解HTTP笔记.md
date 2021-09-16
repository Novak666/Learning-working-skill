# 第1章 了解Web及网络基础

## 1.1 使用HTTP协议访问Web

可以说，Web是建立在HTTP协议上通信的。

## 1.3 网络基础TCP/IP

### 1.3.3 TCP/IP通信传输流

![1.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/1.1.png)

利用 TCP/IP 协议族进行网络通信时，会通过分层顺序与对方进行通信。发送端从应用层往下走，接收端则往应用层往上走。

![1.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/1.2.png)

发送端在层与层之间传输数据时，每经过一层时必定会被打上一个该层所属的首部信息。反之，接收端在层与层传输数据时，每经过一层时会把对应的首部消去。
这种把数据信息包装起来的做法称为封装。

## 1.4 与HTTP关系密切的协议：IP、TCP和DNS
### 1.4.1 负责传输的IP协议

IP 协议的作用是把各种数据包传送给对方。而要保证确实传送到对方那里，则需要满足各类条件。其中两个重要的条件是 IP 地址和 MAC地址（Media Access Control Address）。

IP 地址指明了节点被分配到的地址，MAC 地址是指网卡所属的固定地址。

ARP 是一种用以解析地址的协议，根据通信方的 IP 地址就可以反查出对应的 MAC 地址。

没有人能够全面掌握互联网中的传输状况，在到达通信目标前的中转过程中，那些计算机和路由器等网络设备只
能获悉很粗略的传输路线。这种机制称为路由选择（routing）。

![1.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/1.3.png)

### 1.4.2 确保可靠性的TCP协议

TCP 协议为了更容易传送大数据才把数据分割，而且 TCP 协议能够确认数据最终是否送达到对方。

确保数据能到达目标。

![1.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/1.4.png)

### 1.4.3 负责域名解析的DNS服务

DNS（Domain Name System）服务是和 HTTP 协议一样位于应用层的协议。它提供域名到 IP 地址之间的解析服务。

DNS 协议提供通过域名查找 IP 地址，或逆向从 IP 地址反查域名的服务。

![1.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/1.5.png)

## 1.6 各种协议与HTTP协议的关系

IP 协议、TCP 协议和 DNS 服务在使用 HTTP 协议的通信过程中各自发挥了哪些作用。

![1.6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/1.6.png)

## 1.7 URI和URL

URI 用字符串标识某一互联网资源，而 URL 表示资源的地点（互联网上所处的位置，用定位的方式）。可见 URL 是 URI 的子集。

URI例子：

![1.7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/1.7.png)

绝对URI格式如下：

![1.8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/1.8.png)

# 第2章 简单的HTTP协议

## 2.2 通过请求和响应的交换达成通信

请求报文是由请求方法、请求 URI、协议版本、可选的请求首部字段和内容实体构成的。

![2.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.1.png)

响应报文基本上由协议版本、状态码（表示请求成功或失败的数字代码）、用以解释状态码的原因短语、可选的响应首部字段以及实体主体构成。

![2.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.2.png)

## 2.4 请求URI定位资源

以 http://hackr.jp/index.htm 作为请求的例子：

![2.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.3.png)

## 2.5 告知服务器意图的HTTP方法

### 2.5.1 GET

GET ：获取资源

例子：

![2.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.4.png)

### 2.5.2 POST

POST：传输实体主体

虽然用 GET 方法也可以传输数据，但一般不用 GET 方法进行传输，而是用 POST 方法。

例子：

![2.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.5.png)

### 2.5.3 PUT

PUT：传输文件

PUT 方法用来传输文件。就像 FTP 协议的文件上传一样，要求在请求报文的主体中包含文件内容，然后保存到请求 URI 指定的位置。但是，鉴于 HTTP/1.1 的 PUT 方法自身不带验证机制，任何人都可以上传文件 , 存在安全性问题，因此一般的 Web 网站不使用该方法。

例子：

![2.6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.6.png)

### 2.5.4 HEAD

HEAD：获得报文首部

HEAD 方法和 GET 方法一样，只是不返回报文主体部分。用于确认 URI 的有效性及资源更新的日期时间等。

例子：

![2.7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.7.png)

### 2.5.5 DELETE

DELETE：删除文件

DELETE 方法用来删除文件，是与 PUT 相反的方法。DELETE 方法按请求 URI 删除指定的资源。但是，HTTP/1.1 的 DELETE 方法本身和 PUT 方法一样不带验证机制，所以一般的 Web 网站也不使用 DELETE 方法。

例子：

![2.8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.8.png)

### 2.5.6 OPTIONS

OPTIONS：询问支持的方法

OPTIONS 方法用来查询针对请求 URI 指定的资源支持的方法。

例子：

![2.9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.9.png)

## 2.6 GET和POST比较

### 2.6.1 作用

GET 用于获取资源，而 POST 用于传输实体主体。

### 2.6.2 参数

GET 和 POST 的请求都能使用额外的参数，但是 GET 的参数是以查询字符串出现在 URL 中，而 POST 的参数存储在实体主体中。不能因为 POST 参数存储在实体主体中就认为它的安全性更高，因为照样可以通过一些抓包工具
（Fiddler）查看。

因为 URL 只支持 ASCII 码，因此 GET 的参数中如果存在中文等字符就需要先进行编码。例如 中文 会转换为
%E4%B8%AD%E6%96%87 ，而空格会转换为 %20 。POST 参数支持标准字符集。

```
GET /test/demo_form.asp?name1=value1&name2=value2 HTTP/1.1
```

```
POST /test/demo_form.asp HTTP/1.1
Host: w3schools.com
name1=value1&name2=value2
```

### 2.6.3 安全

安全的 HTTP 方法不会改变服务器状态，也就是说它只是可读的。
GET 方法是安全的，而 POST 却不是，因为 POST 的目的是传送实体主体内容，这个内容可能是用户上传的表单数据，上传成功之后，服务器可能把这个数据存储到数据库中，因此状态也就发生了改变。
安全的方法除了 GET 之外还有：HEAD、OPTIONS。
不安全的方法除了 POST 之外还有 PUT、DELETE。

### 2.6.4 幂等性

幂等的 HTTP 方法，同样的请求被执行一次与连续执行多次的效果是一样的，服务器的状态也是一样的。换句话说就是，幂等方法不应该具有副作用（统计用途除外）。

所有的安全方法也都是幂等的。

在正确实现的条件下，GET，HEAD，PUT 和 DELETE 等方法都是幂等的，而 POST 方法不是。

GET /pageX HTTP/1.1 是幂等的，连续调用多次，客户端接收到的结果都是一样的：

```
GET /pageX HTTP/1.1
GET /pageX HTTP/1.1
GET /pageX HTTP/1.1
```

POST /add_row HTTP/1.1 不是幂等的，如果调用多次，就会增加多行记录：

```
POST /add_row HTTP/1.1 -> Adds a 1nd row
POST /add_row HTTP/1.1 -> Adds a 2nd row
POST /add_row HTTP/1.1 -> Adds a 3rd row
```

DELETE /idX/delete HTTP/1.1 是幂等的，即使不同的请求接收到的状态码不一样：

```
DELETE /idX/delete HTTP/1.1 -> Returns 200 if idX exists
DELETE /idX/delete HTTP/1.1 -> Returns 404 as it just got deleted
DELETE /idX/delete HTTP/1.1 -> Returns 404
```

### 2.6.5 可缓存

如果要对响应进行缓存，需要满足以下条件：

+ 请求报文的 HTTP 方法本身是可缓存的，包括 GET 和 HEAD，但是 PUT 和 DELETE 不可缓存，POST 在多数情况下不可缓存的。
+ 响应报文的状态码是可缓存的，包括：200, 203, 204, 206, 300, 301, 404, 405, 410, 414, and 501。
+ 响应报文的 Cache-Control 首部字段没有指定不进行缓存。

### 2.6.6 XMLHttpRequest

为了阐述 POST 和 GET 的另一个区别，需要先了解 XMLHttpRequest：

XMLHttpRequest 是一个 API，它为客户端提供了在客户端和服务器之间传输数据的功能。它提供了一个通过
URL 来获取数据的简单方式，并且不会使整个页面刷新。这使得网页只更新一部分页面而不会打扰到用户。
XMLHttpRequest 在 AJAX 中被大量使用。

+ 在使用 XMLHttpRequest 的 POST 方法时，浏览器会先发送 Header 再发送 Data。但并不是所有浏览器会这么做，例如火狐就不会。
+ 而 GET 方法 Header 和 Data 会一起发送。

## 2.7 持久连接和管线化



# 第3章 HTTP报文内的HTTP信息

## 3.2 请求报文及响应报文的结构

### 3.2.1 请求报文

![2.10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.10.png)

![2.11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.11.png)

请求行：包含用于请求的方法，请求 URI 和 HTTP 版本。

### 3.2.2 响应报文

![2.12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.12.png)

![2.13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.13.png)

状态行：包含表明响应结果的状态码，原因短语和 HTTP 版本。

# 第4章 返回结果的HTTP状态码
## 4.1 状态码类别

![2.14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.14.png)

## 4.2 1XX 信息

100 Continue ：表明到目前为止都很正常，客户端可以继续发送请求或者忽略这个响应。

## 4.3 2XX 成功

200 OK：正常处理

204 No Content ：请求已经成功处理，但是返回的响应报文不包含实体的主体部分。一般在只需要从客户端
往服务器发送信息，而不需要返回数据时使用。

206 Partial Content ：表示客户端进行了范围请求，响应报文包含由 Content-Range 指定范围的实体内容。

## 4.4 3XX 重定向

301 Moved Permanently ：永久性重定向

302 Found ：临时性重定向

303 See Other ：和 302 有着相同的功能，但是 303 明确要求客户端应该采用 GET 方法获取资源。

304 Not Modified ：如果请求报文首部包含一些条件，例如：If-Match，If-Modified-Since，If-None-
Match，If-Range，If-Unmodified-Since，如果不满足条件，则服务器会返回 304 状态码。304 虽然被划分在 3XX 类别中，但是和重定向没有关系。

307 Temporary Redirect ：临时重定向，与 302 的含义类似，但是 307 要求浏览器不会把重定向请求的
POST 方法改成 GET 方法。

## 4.5 4XX 客户端错误

400 Bad Request ：请求报文中存在语法错误。

401 Unauthorized ：该状态码表示发送的请求需要有认证信息（BASIC 认证、DIGEST 认证）。如果之前已进
行过一次请求，则表示用户认证失败。

403 Forbidden ：请求被拒绝。

404 Not Found：没有请求的资源。

## 4.6 5XX 服务器错误

500 Internal Server Error ：服务器正在执行请求时发生错误。

503 Service Unavailable ：服务器暂时处于超负载或正在进行停机维护，现在无法处理请求。

# 第5章 会话

## 5.1 概述

这里的会话，指的是web开发中的一次通话过程，当打开浏览器，访问网站地址后，会话开始，当关闭浏览器（或者到了过期时间），会话结束。这期间会产生多次请求和响应。

会话管理是为我们共享数据用的，并且是在不同请求间实现数据共享。也就是说，如果我们需要在多次请求间实现数据共享，就可以考虑使用会话管理技术了。

## 5.2 分类

在JavaEE的项目中，会话管理分为两类。分别是：客户端会话管理技术和服务端会话管理技术。

**客户端会话管理技术**

它是把要共享的数据保存到了客户端（也就是浏览器端）。每次请求时，把会话信息带到服务器，从而实现多次请求的数据共享。这样可以保证每次访问时先从本地缓存中获取数据，提高效率。

**服务端会话管理技术**

它本质仍是采用客户端会话管理技术，只不过保存到客户端的是一个特殊的标识，并且把要共享的数据保存到了服务端的内存对象中。每次请求时，把这个标识带到服务器端，然后使用这个标识，找到对应的内存空间，从而实现数据共享。

## 5.3 Cookie

Cookie 是服务器发送到用户浏览器并保存在本地的一小块数据，它会在浏览器之后向同一服务器再次发起请求时被携带上，用于告知服务端两个请求是否来自同一浏览器。由于之后每次请求都会需要携带 Cookie 数据，因此会带来额外的性能开销（尤其是在移动环境下）。

### 5.3.1 用途

+ 会话状态管理（如用户登录状态、购物车、游戏分数或其它需要记录的信息）
+ 个性化设置（如用户自定义设置、主题等）
+ 浏览器行为跟踪（如跟踪分析用户行为等）

### 5.3.2 创建过程

服务器发送的响应报文包含 Set-Cookie 首部字段，客户端得到响应报文后把 Cookie 内容保存到浏览器中。

```
HTTP/1.0 200 OK
Content-type: text/html
Set-Cookie: yummy_cookie=choco
Set-Cookie: tasty_cookie=strawberry
[page content]
```

客户端之后对同一个服务器发送请求时，会从浏览器中取出 Cookie 信息并通过 Cookie 请求首部字段发送给服务
器。

```
GET /sample_page.html HTTP/1.1
Host: www.example.org
Cookie: yummy_cookie=choco; tasty_cookie=strawberry
```

### 5.3.3 有效期

+ 会话期 Cookie：浏览器关闭之后它会被自动删除，也就是说它仅在会话期内有效。
+ 持久性 Cookie：指定过期时间（Expires）或有效期（max-age）之后就成为了持久性的 Cookie。

## 5.4 Session

Session 可以存储在服务器上的文件、数据库或者内存中。也可以将 Session 存储在 Redis 这种内存型数据库中，效率会更高。

使用 Session 维护用户登录状态的过程如下：

+ 用户进行登录时，用户提交包含用户名和密码的表单，放入 HTTP 请求报文中。
+ 服务器验证该用户名和密码，如果正确则把用户信息存储到 Redis 中，它在 Redis 中的 Key 称为 Session ID，用来区分不同用户。
+ 服务器返回的响应报文的 Set-Cookie 首部字段包含了这个 Session ID，客户端收到响应报文之后将该 Cookie
  值存入浏览器中。
+ 客户端之后对同一个服务器进行请求时会包含该 Cookie 值，服务器收到之后提取出 Session ID，从 Redis 中取出用户信息，继续之前的业务操作。

应该注意 Session ID 的安全性问题，不能让它被恶意攻击者轻易获取，那么就不能产生一个容易被猜到的 Session ID 值。此外，还需要经常重新生成 Session ID。在对安全性要求极高的场景下，例如转账等操作，除了使用 Session 管理用户状态之外，还需要对用户进行重新验证，比如重新输入密码，或者使用短信验证码等方式。

## 5.5 Cookie和Session选择

+ Cookie 只能存储 ASCII 码字符串，而 Session 则可以存储任何类型的数据，因此在考虑数据复杂性时首选
  Session。
+ Cookie 存储在浏览器中，容易被恶意查看。如果非要将一些隐私数据存在 Cookie 中，可以将 Cookie 值进行加密，然后在服务器进行解密。
+ 对于大型网站，如果用户所有的信息都存储在 Session 中，那么开销是非常大的，因此不建议将所有的用户信
  息都存储到 Session 中。

区别：

https://mp.weixin.qq.com/s?__biz=MzA4MjA0MTc4NQ==&mid=504090000&idx=3&sn=f57d4f194c902daadd80296d5b8ed001#rd

# 第6章 HTTP首部

## 6.1 HTTP报文首部

### 6.1.1 HTTP请求报文

![2.15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.15.png)

### 6.1.2 HTTP响应报文

![2.16](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.06.20/%E5%9B%BE%E8%A7%A3HTTP%20%E5%BD%A9%E8%89%B2%E7%89%88/pics/2.16.png)

