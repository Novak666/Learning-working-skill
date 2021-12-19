# 第1部分 数据结构与对象

# 第2章 简单动态字符串

## 2.1 SDS定义

![2.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/2.1.png)

SDS遵循C字符串以空字符结尾的惯例，保存空字符的1字节空间
不计算在SDS的len属性里面。

## 2.2 SDS与C字符串的区别

### 2.2.1 获取字符串长度的时间复杂度是O(N)

### 2.2.2 自动扩容，杜绝缓冲区溢出

### 2.2.3 减少修改时产生的内存重分配

Redis作为内存型数据库，经常被用于对速度要求严格、数据被频繁修改的场景。通过未使用空间free，SDS实现<font color='red'>空间预分配和惰性空间释放</font>2种优化策略。

### 2.2.4 二进制安全

Redis不仅可以保存文本数据，还可以保存任意格式的二进制数据。

# 第3章 链表

## 3.1 链表节点

![3.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/3.1.png)

## 3.2 链表

![3.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/3.2.png)

![3.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/3.3.png)

# 第4章 字典

在Redis中，<font color='red'>数据库的底层结构就是字典</font>，对数据库的CRUD就是对字典的CRUD。

字典还是哈希键的底层实现之一。

## 4.1 字典的实现

Redis的字典又是使用哈希表实现的(注意和上面的区分)。

### 4.1.1 哈希表

![4.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/4.4.png)

### 4.1.2 哈希表节点

![4.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/4.2.png)

next指针可以将多个哈希值统统的键值对连接在一起，以此解决哈希冲突。

### 4.1.3 字典

![4.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/4.3.png)

## 4.4 rehash

## 4.5 渐进式rehash

大致了解即可。

# 第8章 对象

Redis<font color='red'>没有直接使用前面章节介绍的数据结构来实现数据库，而是基于这些数据结构创建了一个对象系统</font>。这个系统包含字符串对象、列表对象、哈希对象、集合对象和有序集合对象这5种类型的对象。每种对象至少用到了一种数据结构。

## 8.1 对象的类型与编码

<font color='red'>Redis的底层结构是字典，字典的键和值都是对象</font>。

![8.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/8.1.png)

### 8.1.1 type

对象的type属性记录了对象的类型，这个属性的值可以是下表中的任意一个：

![8.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/8.2.png)

<font color='red'>对于Redis数据库来说，键总是一个字符串，值可以是5种对象类型之一。因此，在Redis中，当称呼一个数据库键是”XX键“时，指的是这个数据库键所对应的值是XX对象</font>。

例如，“列表键”指的是某个数据库键所对应的值是列表对象。

### 8.1.2 encoding

对象的ptr指针指向对象的底层实现数据结构，而这些数据结构由对象的encoding属性决定。encoding的值可以是下表中的任意一个：

![8.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/8.3.png)

对象和编码的关系如下表：

![8.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/8.4.png)

通过encoding属性来设定对象所用的编码，可以极大地提升Redis的灵活性和效率。因为Redis可以根据不同的使用场景来为一个对象设置不同的编码，从而优化对象在某一场景下的效率。

## 8.2 5种对象

<font color='red'>注意区别hashtable编码的哈希对象和hashtable编码的集合对象。2者都使用字典作为底层实现，但是前者既有键又有值(都是字符串)，后者只有键(字符串)，值为null</font>。

## 8.10 对象的空转时长

![8.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/8.5.png)

# 第2部分 单机数据库的实现

# 第9章 数据库

## 9.1 数据库相关结构定义

![9.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/9.1.png)

![9.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/9.2.png)

## 9.3 数据库键空间

前面章节内容的复习：

![9.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/9.3.png)

## 9.4 键的生存与过期时间

![9.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/9.4.png)

## 9.5 过期键的删除策略

![9.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/9.5.png)

定时删除：对内存最友好，对CPU最不友好。

惰性删除：对CPU最友好，对内存最不友好。

定期删除：是前2种的整合和折中，难确定的是删除操作的时长和频率，太频繁或时长太长会退化为定时删除，太少或时长太短会退化为惰性删除。

## 9.6 Redis过期键的删除策略

使用惰性删除和定期删除。

### 9.6.1 惰性删除

![9.6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/9.6.png)

### 9.6.2 定期删除

过期键的定期删除由redis.c/activeExpireCycle函数实现，每当Redis的服务器周期性操作redis.c/serverCron函数执行时，activeExpireCycle函数就会被调用。它在规定的时间内，分多次遍历服务器中的各个数据库，从数据库的expires字典中随机检查一部分键的过期时间，并删除其中的过期键。

函数伪代码见课本，总结如下：

![9.7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/9.7.png)

## 9.7 AOF、RDB和复制功能对过期键的影响

### 9.7.1 RDB生成

用SAVE或BGSAVE命令创建一个新的RDB文件时，过期键不会被保存到新的RDB文件中。

### 9.7.2 RDB载入

Redis服务器启动时，若开启了RDB功能，那么服务器会载入RDB文件。

如果服务器以主服务器模式运行，过期键会被忽略，因此不影响。

如果服务器以从服务器模式运行，所有键都会被载入数据库中，但是和主服务器进行数据同步时，从服务器会清空数据库，所以也不会有影响。

### 9.7.3 AOF写入

服务器以AOF持久化运行时，过期键没被删除时，没影响；删除后，程序会向AOF追加一条DEL指令来显式记录已删除。

### 9.7.4 AOF重写

过期键不会被保存到重写后的AOF文件中，因此不影响。

### 9.7.5 复制

服务器运行在复制模式时：

+ 主服务器在删除一个过期键后，会显式地向所有从服务器发送一个DEL命令，告诉从服务器删除这个过期键。
+ 从服务器在执行客户端发来的读命令时，即使碰到过期键也不会将过期键删除，而是继续像处理未过期的键一样返回给客户端。
+ 从服务器只有在接收到主服务器发来的DEL命令后，才会删除过期键。

<font color='red'>通过主服务器控制从服务器进行统一删除过期键，保证主从服务器的数据一致性。因此，当一个过期键仍然存在于主服务器的数据库中时，这个过期键在从服务器中也会存在</font>。

# 第10章 RDB持久化

<font color='red'>RDB文件用于保存和还原Redis服务器中所有数据库的所有键值对数据</font>。

## 10.1 RDB文件的创建与载入

创建RDB文件：SAVE指令和BGSAVE指令。

SAVE命令会阻塞Redis服务器进程，直到RDB文件创建完成。阻塞期间，服务器不能处理任何命令请求。

BGSAVE会派生出一个子进程，子进程负责创建RDB文件，服务器进程继续处理命令请求。

载入RDB文件时服务器启动时<font color='red'>自动</font>执行的，没有专门的命令，只要启动时Redis服务器监测到RDB文件的存在，它就会自动载入，<font color='red'>在此期间是阻塞状态</font>。

另外，AOF文件比RDB文件的更新频率要高，因此：

+ 服务器开启了AOF持久化功能，服务器优先使用AOF文件来还原数据库状态。
+ AOF持久化功能未开启，服务器才使用RDB文件来还原数据库状态。

一些注意点：

<font color='red'>服务器执行BGSAVE时，若客户端发来SAVE、BGSAVE或BGREWRITEAOF</font>：

+ SAVE会被拒绝。
+ BGSAVE会被拒绝。
+ BGREWRITEAOF会延迟到BGSAVE完毕之后执行(若正在执行的是BGREWRITEAOF，发来的BGSAVE会被拒绝)。

## 10.2 自动间歇性保存

例如配置save 900 1表示：只要服务器在900秒之内，对数据库进行了至少1次修改，BGSAVE命令就会被执行。

## 10.3 RDB文件结构

![10.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/10.1.png)

db_version长度为4字节，它的值是一个字符串表示的整数，这个整数记录了RDB

![10.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/10.1.png)

### 10.3.1 databases部分

![10.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/10.3.png)

![10.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/10.4.png)

### 10.3.2 key_value_pairs部分

![10.6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/10.6.png)

value根据类型的不同，长度和结构也会不同，具体介绍略。

# 第11章 AOF持久化

![11.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/11.1.png)

## 11.1 AOF持久化的实现

AOF持久化功能的实现有3步：

+ 命令追加(append)
+ 文件写入
+ 文件同步(sync)

### 11.1.1 命令追加

![11.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/11.2.png)

### 11.1.2 写入与同步

<font color='red'>Redis的服务器进程就是一个事件循环，这个循环中的文件事件负责接收客户端的命令请求，以及想客户端发送命令回复，而时间事件则负责执行像serverCron函数这样需要定时运行的函数</font>。

![11.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/11.3.png)

![11.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/11.4.png)

+ always会丢失一个事件循环中所产生的命令数据。
+ everysec会丢失一秒钟的命令数据。
+ no会丢失上次AOF同步后的所有命令数据。

## 11.2 AOF文件的载入与数据还原

![11.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/11.5.png)

## 11.3 AOF重写

Redis可以创建一个新的AOF文件来代替现有的AOF文件，新旧2个AOF文件所保存的数据库状态相同，但新AOF文件不包含任何浪费空间的冗余命令，所以，新AOF文件的体积通常会比旧AOF文件的体积小得多。

### 11.3.1 AOF文件重写的实现

<font color='red'>实际上，AOF文件重写并不需要对现有的AOF文件进行任何的读取、分析或者写入操作，它是通过读取服务器当前的数据库状态来实现的</font>。

整个重写过程如函数aof_rewrite(new_aof_file_name)所示。

### 11.3.2 AOF后台重写

因为Redis服务器是使用单个线程来处理命令请求，所以如果由服务器直接调用aof_rewrite函数的话，会造成阻塞。

所以Redis将AOF重写放到子进程中。

但是子进程在重写AOF期间，服务器还会继续处理客户端的命令请求，因此有可能会进一步改变数据库的状态，造成数据库最新的状态和重写后的AOF文件保存的数据库状态不一致。

![11.6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/11.6.png)

![11.7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/11.7.png)

<font color='red'>注意：信号处理函数执行时，服务器是阻塞状态</font>。

以上就是<font color='red'>BGREWRITEAOF</font>命令的实现过程。

# 第12章 事件

![12.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/12.1.png)

## 12.1 文件事件

![12.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/12.2.png)

## 12.2 时间事件

![12.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/12.3.png)

目前版本的Redis只使用周期性事件，而没有使用定时事件。

![12.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/12.4.png)

### 12.2.1 时间事件应用实例：serverCron函数

![12.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/12.5.png)

# 第13章 客户端

![13.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/13.1.png)

# 第14章 服务器

## 14.2 serverCron函数

Redis服务器中的serverCron函数默认每隔100毫秒执行一次，这个函数负责管理服务器的资源，并保持服务器自身的良好运转。

1. 更新服务器时间缓存
2. 更新LRU时钟
3. 更新服务器每秒执行命令次数
4. 更新服务器内存峰值记录
5. 处理SIGTERM信号
6. 管理客户端资源
7. 管理数据库资源
8. 执行被延迟的BGREWRITEAOF
9. 检查持久化操作的运行状态
10. 将AOF缓冲区中的内容写入AOF文件
11. 关闭异步客户端
12. 增加cronloops计数器的值

# 第15章 复制

![15.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.1.png)

## 15.1 旧版复制功能的实现

![15.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.2.png)

### 15.1.1 同步

![15.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.3.png)

![15.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.4.png)

### 15.1.2 命令传播

在同步操作执行完之后，主从服务器的数据库将达到一致状态。但这种一致状态并不是一成不变的，每当主服务器执行完客户端发送的写命令后，主服务器的数据库状态就有可能被修改，从而导致主从服务器状态不再一致。为了主从状态再次一致，主服务器需要对从服务器执行命令传播，将造成状态不一致的写命令发送给从服务器。

## 15.2 旧版复制功能的缺陷

![15.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.5.png)

总的来说，主从服务器断开，为了让从服务器补足一小部分缺失的数据，却要让主服务器重新执行一次SYNC命令，这种做法无疑是非常低效的。

![15.6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.6.png)

## 15.3 新版复制功能的实现

![15.7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.7.png)

![15.8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.8.png)

## 15.4 部分重同步的实现

### 15.4.1 复制偏移量

执行复制的双方—主服务器和从服务器会分别维护一个复制偏移量：

+ 主服务器每次向从服务器传播N个字节的数据时，就会将自己复制偏移量的值加上N。
+ 从服务器每次收到主服务器传播来的N个字节的数据时，就将自己复制偏移量的值加上N。

通过对比主从服务器的复制偏移量，程序可以很容易知道主从服务器状态是否处于一致：

+ 如果两者的偏移量应该是相同，主从状态一致。
+ 相反，如果偏移量不相同，主从状态不一致。

### 15.4.2 复制积压缓冲区

复制积压缓冲区是由主服务器维护的一个固定长度的先进先出(FIFO)队列，默认大小是1MB。

当主服务器进行命令传播时，它不仅会将写命令发送给所有从服务器，还会将命令写入到复制缓冲区，如下图所示：

![15.9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.9.png)

因此，<font color='red'>主服务器的复制积压缓冲区里面保存着一部分最近传播的写命令，每个字节都有对应的复制偏移量</font>。

当从服务器重新连上主服务器时，主服务器根据从服务器发来PSYNC命令中的复制偏移量offset来决定策略：

+ 如果offset偏移量之后的数据(offset+1开始)仍然处于复制积压缓冲区内，那么主服务器将对从服务器执行部分重同步操作。
+ 相反如果offset偏移量之后的数据(offset+1开始)已经不存在于复制积压缓冲区，那么主服务器将对从服务器执行完整重同步操作。

### 15.4.3 服务器运行ID

![15.10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.10.png)

## 15.5 PSYNC命令的实现

![15.11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.11.png)

## 15.6 复制的实现步骤

1. 设置主服务器的地址和端口
2. 建立套接字连接
3. 发送PING命令
4. 身份验证
5. 发送端口信息
6. 同步
7. 命令传播

## 15.7 心跳检测

![15.12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/15.12.png)

### 15.7.3 检测命令丢失

检测命令丢失和部分重同步十分相似，区别是前者是主从没有断线时执行，后者是主从断线重连后执行。

# 第16章 Sentinel

Sentinel是Redis的高可用性解决方案：由一个或多个Sentinel实例组成的Sentinel系统可以监视任意多个主从服务器，并且在被监视的主服务器下线时，自动将下线主服务器所属的某个从服务器升级为新的主服务器，然后由新的主服务器代替已下线的主服务器继续处理命令请求。

## 16.1 启动并初始化Sentinel

启动Sentinel命令：

```
redis-sentinel /path/to/your/sentinel.conf
```

或者：

```
redis-server /path/to/your/sentinel.conf --sentinel
```

![16.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.1.png)

<font color='red'>Sentinel使用了和普通模式下不同的指令表，所以能使用的指令和普通Redis服务器有所不同，因此Sentinel只是一个运行在特殊模式下的Redis服务器</font>。

## 16.2 获取主服务器信息

Sentinel默认会以每10秒1次的频率，通过命令连接向被监视的主服务器发送INFO命令，并通过分析INFO命令的回复来获取主服务器的当前信息。

![16.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.2.png)

## 16.3 获取从服务器信息

当Sentinel发现主服务器有新的从服务器出现时，Sentinel除了会为这个新的从服务器创建相应的实例结构之外，Sentinel还会创建连接到从服务器的命令连接和订阅连接。Sentinel默认会以每10秒1次的频率，通过命令连接向被监视的从服务器发送INFO命令。

![16.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.3.png)

## 16.4 向主服务器和从服务器发送消息

![16.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.4.png)

## 16.5 接收来自主服务器或者从服务器的频道信息

![16.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.5.png)

这表明：对于每个与Sentinel连接的服务器，Sentinel既通过命令连接向服务器的_sentinel__:hello频道发送信息，又通过订阅连接从服务器的该频道接收信息。

![16.6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.6.png)

### 16.5.1 更新sentinels字典

### 16.5.2 创建连向其他Sentinel的命令连接

![16.7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.7.png)

## 16.6 检测主观下线状态

在默认情况下，Sentinel会以每秒一次的频率向所有与它创建了命令连接的实例(包括主服务器、从服务器、其他Sentinel在内)发送PING命令，并通过实例返回的PING命令回复来判断实例是否在线。

![16.8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.8.png)

## 16.7 检测客观下线状态

### 16.7.1 发送SENTINEL is-master-down-by-addr命令

![16.9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.9.png)

### 16.7.2 接收SENTINEL is-master-down-by-addr命令

![16.10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.10.png)

### 16.7.3 判断是否客观下线

![16.11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.11.png)

![16.12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.12.png)

根据配置的不同，不同Sentinel判断客观下线的条件可能不同。

## 16.8 选举领头Sentinel

当一个主服务器被判断为客观下线时，监视这个下线主服务器的各个Sentinel会进行协商，选举出一个领头Sentinel，并由领头Sentinel对下线主服务器执行故障转移操作。

以下是Redis选举领头Sentinel的规则和方法：

+ 所有在线且监视同一个主服务器的Sentinel都有被选举为领头Sentinel的资格。
+ 每次进行领头Sentinel选举之后，无论选举是否成功，所有Sentinel的配置纪元(configuration epoch)的值都会自增加一。
+ 在一次配置纪元里，所有Sentinel都有一次将某个Sentinel设为局部领头Sentinel的机会，一旦设置，这个配置纪元就不能再修改。
+ 每个发现主服务器客观下线的Sentinel都会要求其他Sentinel将自己设置为局部领头Sentinel。
+ 当一个Sentinel向另一个Sentinel发送SENTINEL is-master-down-by-addr命令，且命令的runid参数不是*而是源Sentinel的运行ID时，这表示源Sentinel要求目标Sentinel将源Sentinel设置为局部Sentinel。
+ Sentinel设置局部Sentinel的规则是先到先得，目标Sentinel最先收到的SENTINEL is-master-down-by-addr请求命令会执行，后面的请求都会被目标Sentinel拒绝。
+ 目标Sentinel在接收SENTINEL is-master-down-by-addr命令后，向源Sentinel返回一个回复，包括leader_runid(目标Sentinel的局部领头Sentinel的运行ID)和leader_epoch(配置纪元)。
+ 源Sentinel收到目标Sentinel的回复后，比较leader_epoch是否相同，若相同，继续比较leader_runid，还是相同，则设置局部领头Sentinel成功。
+ 如果有某个Sentinel被半数以上的Sentinel设置成局部领头Sentinel，那么这个Sentinel就会成为领头Sentinel。
+ 如果给定时间内没选举出领头Sentinel，隔一段时间再次选举，直到选举出为止。

## 16.9 故障转移

### 16.9.1 选出新的主服务器

故障转移第一步是在已下线主服务器的所有从服务器中，选出一个状态良好、数据完整的从服务器，然后向这个从服务器发送SLAVEOF no one命令，将这个从服务器转换成主服务器。

![16.13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.13.png)

### 16.9.2 修改从服务器的复制目标

![16.14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.14.png)

### 16.9.3 将旧的主服务器变为从服务器

![16.15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/16.15.png)

# 第17章 集群

Redis集群是Redis提供的分布式数据库方案，集群通过<font color='red'>分片(sharding)</font>来进行数据共享、并提供复制和故障转移功能。

## 17.1 节点

一个Redis集群通常由多个<font color='red'>节点(node)</font>组成，在刚开始的时候，<font color='red'>每个节点都是相互独立的</font>，它们都处于一个只包含自己的集群中。要组建一个真正可工作的集群，我们必须将各个独立的节点连接起来，构成一个包含多个节点的集群。

向一个node发送CLUSTER MEET命令时，可以让节点与指定ip和port的节点进行握手(handshake)。如果握手成功，指定节点会加入到当前节点所在的集群。

CLUSTER NODE会显示当前节点所在集群的一些信息。

### 17.1.1 启动节点

![17.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.1.png)

### 17.1.2 集群特有的数据结构

clusterNode结构用来记录节点自身的状态，并为集群中的所有其他节点创建clusterNode结构(clusterState结构中)。

![17.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.2.png)

clusterNode结构中的link属性是clusterLink结构，该结构保存了连接节点的有关信息。

![17.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.3.png)

注意：redisClient结构中的套接字和缓冲区用于连接客户端，clusterLink结构汇总的套接字和缓冲区用于连接节点。

最后，每个节点中都保存着一个clusterState结构，这个结构记录了在当前节点视角下的集群状态。

![17.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.4.png)

例子见课本。

### 17.1.3 CLUSTER MEET过程(握手过程)

客户端向节点A发送一个CLUSTER MEET命令，节点A和指定的目标节点B尝试握手，成功就将B加入到A所在的集群。

整个握手流程如下：

![17.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.5.png)

## 17.2 槽指派

Redis集群通过<font color='red'>分片</font>的方式来保存数据库中的键值对。集群的整个数据库被分为16384个槽(slot)，数据库中的每个键都属于这16384个槽中的一个，集群中的每个节点可以处理0个或最多16384个槽。

当数据库中的16384个槽都有节点在处理时，集群处于上线状态(ok)；相反，如果数据库中有任何一个槽没有得到处理，那么集群处于下线状态(fail)。

CLUSTER ADDSLOTS命令可以将一个或多个槽指派(assign)给节点负责。

### 17.2.1 记录节点的槽指派信息

![17.6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.6.png)

### 17.2.2 传播节点的槽指派信息

集群中的每个节点都会将自己的slots数组通过消息发送给集群中的其他节点，并且每个收到slots数组的节点都会将数组保存到相应节点clusterNode结构里面，因此，集群中的每个节点都会知道数据库中的16384个槽分别被指派给了集群中的哪些节点。

### 17.2.3 记录集群所有槽的指派信息

![17.7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.7.png)

clusterState.slots记录了集群中所有槽的指派信息，而clusterNode.slots记录了clusterNode结构所代表节点的槽指派信息，这2个数据结构都是必要的。

### 17.2.4 CLUSTER ADDSLOTS命令

该命令会将clusterState.slots和clusterNode.slots的槽信息更新。

## 17.3 在集群中执行命令

![17.8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.8.png)

### 17.3.1 计算键属于哪个槽

![17.9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.9.png)

### 17.3.2 判断槽是否由当前节点处理

![17.10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.10.png)

### 17.3.3 MOVED错误

当节点发现键所在的槽不是自己负责的时候，节点会向客户端返回一个MOVED错误。客户端会根据MOVED提供的ip和端口。转向新节点，并重新发送之前处理键的命令。

### 17.3.4 节点数据库的实现

集群节点和单机服务器在数据库方面的一个区别是节点只能使用0号数据库，而单机没有这一限制。

![17.11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.11.png)

![17.12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.12.png)

## 17.4 重新分片

Redis集群的重新分片操作可以将任意数量已经指派给某个节点(源节点)的槽改为指派给另一个节点(目标节点)，并且相关槽所属的键值对也会从源节点被移动到目标节点。

重新分片操作可以在线(online)进行，在重新分片的过程中，集群不需要下线，并且源节点和目标节点都可以继续处理命令请求。

![17.13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.13.png)

## 17.5 ASK错误

ASK错误只是2个节点在迁移槽的过程中使用的一种临时措施，客户端只会在接下去的一次命令请求中进行转向。

## 17.6 复制与故障转移

Redis集群中的节点分为主节点和从节点，其中主节点用于处理槽，而从节点用于复制某个主节点，并在被复制的主节点下线时，代替下线主节点继续处理命令请求。

### 17.6.1 设置从节点

![17.14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.14.png)

![17.15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.15.png)

### 17.6.2 故障检测

集群中的每个节点都会定期地向集群中的其他节点发送PING消息，以此来检测对方是否在线，如果接收PING消息的节点没有在规定时间内，向发送PING消息的节点返回PONG消息，那么发送PING消息的节点就会将接收PING消息的节点标记为疑似下线(probable fail，PFAIL)。

集群中的各个节点会通过互相发送消息的方式来交换集群中各个节点的状态信息。

当一个主节点A通过消息得知主节点B认为主节点C是PFAIL时，主节点A会在自己的clusterState.nodes字典中找到主节点C所对应的clusterNode结构，并将主节点B的下线报告(failure report)添加到clusterNode结构的fail_reports链表里面。

![17.16](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.16.png)

如果在一个集群里，<font color='red'>半数以上负责处理槽的主节点都将某个主节点x报告为疑似下线</font>，那么这个主节点x将被标记为下线(FAIL)，将主节点x标记为下线的节点会向集群广播一条关于主节点x的FAIL消息，所有收到这条FAIL消息的节点都会立即将主节点x标记为下线。

### 17.6.3 故障转移

![17.17](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.17.png)

### 17.6.4 选举新的主节点

和16章选举领头Sentinel十分相似，基于Raft算法领头选举方法实现。

## 17.7 消息

![17.18](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.18.png)

![17.19](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/17.19.png)

5种消息具体实现略。

# 第18章 操作

## 18.1 主从复制

### 18.1.1 集群规划

一主二从

| 节点   | 配置文件       | 端口 |
| ------ | -------------- | ---- |
| master | redis6379.conf | 6379 |
| slave1 | redis6379.conf | 6380 |
| slave2 | redis6379.conf | 6381 |

### 18.1.2 配置

```shell
# redis6379.conf    master
# 包含命令，有点复用的意思
include /opt/redis-5.0.5/redis.conf
pidfile /var/run/redis_6379.pid
port    6379
dbfilename dump6379.rdb
logfile "my-redis-6379.log"

# redis6380.conf    slave1
include /opt/redis-5.0.5/redis.conf
pidfile /var/run/redis_6380.pid
port    6380
dbfilename dump6380.rdb
logfile "my-redis-6380.log"
# 最后一行设置了主节点的 ip 端口
replicaof 127.0.0.1 6379

# redis6381.conf    slave2
include /opt/redis-5.0.5/redis.conf
pidfile /var/run/redis_6381.pid
port    6381
dbfilename dump6381.rdb
logfile "my-redis-6381.log"
# 最后一行设置了主节点的 ip 端口
replicaof 127.0.0.1 6379

## 注意 redis.conf 要调整一项，设置后台运行，对咱们操作比较友好
daemonize yes
```

### 18.1.3 启动

```shell
# 顺序启动节点
$ redis-server redis6379.conf
$ redis-server redis6380.conf
$ redis-server redis6381.conf
```

![18.1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/18.1.png)

```shell
# 进入redis 客户端，开多个窗口查看方便些
$ redis-cli -p 6379
# info replication 命令可以查看连接该数据库的其它库的信息(主从信息显示不同)
$ info replication
```

![18.2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/18.2.png)

### 18.1.4 数据验证

```shell
# master
$ redis-cli -p 6379
127.0.0.1:6379> set k1 v1
OK

# slave1
$ redis-cli -p 6380
127.0.0.1:6380> get k1
"v1"
```

## 18.2 Sentinel模式

### 18.2.1 集群规划

除了有主从复制外，加几个哨兵进行监控。

一主二从三哨兵

| 节点      | 配置文件       | 端口  |
| --------- | -------------- | ----- |
| master    | redis6379.conf | 6379  |
| slave1    | redis6379.conf | 6380  |
| slave2    | redis6379.conf | 6381  |
| sentinel1 | sentinel1.conf | 26379 |
| sentinel2 | sentinel2.conf | 26380 |
| sentinel3 | sentinel3.conf | 26381 |

### 18.2.2 配置

创建三个哨兵文件。

```shell
# 文件内容
# sentinel1.conf
port 26379
sentinel monitor mymaster 127.0.0.1 6379 2
# sentinel2.conf
port 26380
sentinel monitor mymaster 127.0.0.1 6379 2
# sentinel3.conf
port 26381
sentinel monitor mymaster 127.0.0.1 6379 2
```

### 18.2.3 启动

先启动主从，在启动哨兵。

```shell
$ redis-sentinel sentinel1.conf
$ redis-sentinel sentinel2.conf
$ redis-sentinel sentinel3.conf
```

![18.3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/18.3.png)

可以看到主从服务器和3个哨兵都启动起来了。

### 18.2.4 实验模拟

这时将主服务器下线

```shell
shutdown
```

![18.4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/18.4.png)

可以看到哨兵监测到6379服务器下线，标记为主观下线。然后2个哨兵标记为主观下线后，标记为客观下线。

随后出现了选举领头sentinel的过程，最终有一个哨兵被选举为领头哨兵。6380从服务器升级为主服务器，重新启动6379服务器，6379变成了6380的从服务器。

![18.5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.12.19/pics/18.5.png)

## 18.3 集群模式

### 18.3.1 集群规划

根据官方推荐，集群部署至少要3台以上的master节点，最好使用3主3从六个节点的模式。

| 节点            | 配置文件       | 端口 |
| --------------- | -------------- | ---- |
| cluster-master1 | redis7001.conf | 7001 |
| cluster-master2 | redis7002.conf | 7002 |
| cluster-master3 | redis7003.conf | 7003 |
| cluster-slave1  | redis7004.conf | 7004 |
| cluster-slave2  | redis7005.conf | 7005 |
| cluster-slave3  | redis7006.conf | 7006 |

### 18.3.2 配置

需要6份，以redis7001.conf为例：

```shell
# 端口
port 7001  
# 启用集群模式
cluster-enabled yes 
# 根据你启用的节点来命名，最好和端口保持一致，这个是用来保存其他节点的名称，状态等信息的
cluster-config-file nodes_7001.conf 
# 超时时间
cluster-node-timeout 5000
appendonly yes
# 后台运行
daemonize yes
# 非保护模式
protected-mode no 
pidfile  /var/run/redis_7001.pid
```

### 18.3.3 启动

```shell
redis-server redis7001.conf
...
redis-server redis7006.conf
```

```shell
# 执行命令
# --cluster-replicas 1 命令的意思是创建master的时候同时创建一个slave

$ redis-cli --cluster create 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 127.0.0.1:7006 --cluster-replicas 1
# 执行成功结果如下
# 我们可以看到 7001，7002，7003 成为了 master 节点，
# 分别占用了 slot [0-5460]，[5461-10922]，[10923-16383]
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 127.0.0.1:7005 to 127.0.0.1:7001
Adding replica 127.0.0.1:7006 to 127.0.0.1:7002
Adding replica 127.0.0.1:7004 to 127.0.0.1:7003
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: 0313641a28e42014a48cdaee47352ce88a2ae083 127.0.0.1:7001
   slots:[0-5460] (5461 slots) master
M: 4ada3ff1b6dbbe57e7ba94fe2a1ab4a22451998e 127.0.0.1:7002
   slots:[5461-10922] (5462 slots) master
M: 719b2f9daefb888f637c5dc4afa2768736241f74 127.0.0.1:7003
   slots:[10923-16383] (5461 slots) master
S: 987b3b816d3d1bb07e6c801c5048b0ed626766d4 127.0.0.1:7004
   replicates 4ada3ff1b6dbbe57e7ba94fe2a1ab4a22451998e
S: a876e977fc2ff9f18765a89c12fbd2c5b5b1f3bf 127.0.0.1:7005
   replicates 719b2f9daefb888f637c5dc4afa2768736241f74
S: ac8d6c4067dec795168ca705bf16efaa5f04095a 127.0.0.1:7006
   replicates 0313641a28e42014a48cdaee47352ce88a2ae083
Can I set the above configuration? (type 'yes' to accept): yes 
# 这里有个要手动输入 yes 确认的过程
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
...
>>> Performing Cluster Check (using node 127.0.0.1:7001)
M: 0313641a28e42014a48cdaee47352ce88a2ae083 127.0.0.1:7001
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
M: 4ada3ff1b6dbbe57e7ba94fe2a1ab4a22451998e 127.0.0.1:7002
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
S: ac8d6c4067dec795168ca705bf16efaa5f04095a 127.0.0.1:7006
   slots: (0 slots) slave
   replicates 0313641a28e42014a48cdaee47352ce88a2ae083
S: a876e977fc2ff9f18765a89c12fbd2c5b5b1f3bf 127.0.0.1:7005
   slots: (0 slots) slave
   replicates 719b2f9daefb888f637c5dc4afa2768736241f74
M: 719b2f9daefb888f637c5dc4afa2768736241f74 127.0.0.1:7003
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
S: 987b3b816d3d1bb07e6c801c5048b0ed626766d4 127.0.0.1:7004
   slots: (0 slots) slave
   replicates 4ada3ff1b6dbbe57e7ba94fe2a1ab4a22451998e
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```

### 18.3.4 数据验证

```shell
# 注意 集群模式下要带参数 -c，表示集群，否则不能正常存取数据！！！
[root@localhost redis-5.0.5]# redis-cli -p 7100 -c
# 设置 k1 v1
127.0.0.1:7001> set k1 v1
-> Redirected to slot [12706] located at 127.0.0.1:7003
OK
# 这可以看到集群的特点:把数据存到计算得出的 slot，这里还自动跳到了 7003
127.0.0.1:7003> get k1
"v1"

# 我们还回到 7001 获取 k1 试试
[root@localhost redis-5.0.5]# redis-cli -p 7001 -c
127.0.0.1:7001> get k1
-> Redirected to slot [12706] located at 127.0.0.1:7003
"v1"
# 我们可以看到重定向的过程
127.0.0.1:7003> 
```