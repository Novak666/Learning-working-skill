# 1. NoSQL简介

## 1.1 概念

NoSQL：即 Not-Only SQL（ 泛指非关系型的数据库），作为关系型数据库的补充。 作用：应对基于海量用户和海量数据前提下的数据处理问题。

我们存储数据，可以不光使用SQL，我们还可以使用非SQL的这种存储方案，这就是所谓的NoSQL。

## 1.2 特征

1. 大数据量下高性能，存储在内存中
2. 可扩容，可伸缩，因为关系不复杂
3. 灵活的数据模型、高可用。他设计了自己的一些数据存储格式，这样能保证效率上来说是比较高的，最后一个高可用，用在集群中

## 1.3 应用场景

以电商为例

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/1.png)

我们的基础数据都存MySQL,在它的基础之上，我们把它连在一块儿，同时对外提供服务。向上走，有一些信息加载完以后,要放到我们的MongoDB中。还有一类信息，我们放到我们专用的文件系统中（比如图片），就放到我们的这个搜索专用的，如Lucene、solr及集群里边，或者用ES的这种技术里边。那么剩下来的热点信息，放到我们的redis里面。

热点信息。访问频度比较高的信息，这种东西的第二特征就是它具有波段性。换句话说他不是稳定的，它具有一个时效性的。那么这类信息放哪儿了，放到我们的redis这个解决方案中来进行存储。

# 2. Redis简介

## 2.1 概念

Redis (REmote DIctionary Server) 是用 C 语言开发的一个开源的高性能键值对（key-value）数据库。

## 2.2 特征

1. 数据间没有必然的关联关系

2. 内部采用单线程机制进行工作

3. 高性能。官方提供测试数据，50个并发执行100000 个请求，读的速度是110000 次/s，写的速度是81000次/s。

4. 多数据类型支持

   + 字符串类型，string  list

   + 列表类型，hash  set

   + 散列类型，zset/sorted_set

   + 集合类型

   + 有序集合类型

5. 支持持久化，可以进行数据灾难恢复

## 2.3 应用场景

1. 为热点数据加速查询（主要场景）。如热点商品、热点新闻、热点资讯、推广类等高访问量信息等。
2. 即时信息查询。如各位排行榜、各类网站访问统计、公交到站信息、在线人数信息（聊天室、网站）、设备信号等。
3. 时效性信息控制。如验证码控制、投票控制等。
4. 分布式数据共享。如分布式集群架构中的 session 分离消息队列。

# 3. Redis下载与安装

## 3.1 下载与安装

1. 下载安装包

```bash
wget http://download.redis.io/releases/redis-5.0.0.tar.gz
```

或者本地下载好，CRT上传，上传命令：Alt + p

2. 将安装包移动到自己想解压的地方进行解压

```bash
tar -xvf redis-5.0.0.tar.gz
```

3. 安装gcc

Redis由C开发，运行需要C环境

```bash
yum install gcc-c++
```

4. 编译（进入redis-5.0.0文件夹）

```bash
make
```

5. 安装

```bash
make install
```

文件介绍

redis-server 服务器启动命令

redis-cli 客户端启动命令

redis.conf redis核心配置文件

redis-check-dump RDB文件检查工具（快照持久化文件）

redis-check-aof AOF文件修复工具

## 3.2 修改配置

修改配置文件redis.conf vim编辑器 /搜索 n下一个 N上一个

daemonize改为yes

bind ip注释掉

protected-mode改为no

dir设置服务器文件保存路径

其他设置：

 服务器允许客户端连接最大数量，默认0，表示无限制。当客户端连接到达上限后，Redis会拒绝新的连接

```bash
maxclients count
```

客户端闲置等待最大时长，达到最大值后关闭对应连接。如需关闭该功能，设置为 0

```bash
timeout seconds
```

设置服务器以指定日志记录级别

```bash
loglevel debug|verbose|notice|warning
```

日志记录文件名

```bash
logfile filename
```

注意：日志级别开发期设置为verbose即可，生产环境中配置为notice，简化日志输出量，降低写日志IO的频度。

## 3.3 启动

以配置文件方式启动Redis服务器

```bash
redis-server /home/redis-5.0.0/redis.conf
```

启动Redis客户端

## 3.4 基本操作

```
help [command]
set key value
get key
exit
```

# 4. Redis数据类型

http://redisdoc.com/index.html

## 4.1 String

### 4.1.1 简介

表示value是一个字符串，若字符串是整数形式，可以作为数字使用

### 4.1.2 基本操作

添加/修改数据

```
set key value
```

获取数据

```
get key
```

删除数据

```
del key
```

判定性添加数据

```
setnx key value
```

添加/修改多个数据

```
mset key1 value1 key2 value2 …
```

获取多个数据

```
mget key1 key2 …
```

获取数据字符个数（字符串长度）

```
strlen key
```

追加信息到原始信息后部（如果原始信息存在就追加，否则新建）

```
append key value
```

set与mset的选择

在影响的数据量比较大时，尽量用mset

### 4.1.3 扩展操作

设置数值数据增加指定范围的值

```bash
incr key
incrby key increment
incrbyfloat key increment
```

设置数值数据减少指定范围的值

```bash
decr key
decrby key increment
```

设置数据具有指定的生命周期

```bash
setex key seconds value
psetex key milliseconds value
```

### 4.1.4 注意事项

1. 数据操作不成功的反馈与数据正常操作之间的差异

   表示运行结果是否成功

   (integer) 0  → false                 失败

   (integer) 1  → true                  成功

   表示运行结果值

   (integer) 3  → 3                        3个

   (integer) 1  → 1                         1个

2. 数据未获取到时，对应的数据为（nil），等同于null

3. 数据最大存储量：512MB

4. string在redis内部存储默认就是一个字符串，当遇到增减类操作incr，decr时会转成数值型进行计算

5. 按数值进行操作的数据，如果原始数据不能转成数值，或超越了redis数值上限范围，将报错
   9223372036854775807（java中Long型数据最大值，Long.MAX_VALUE）

6. redis所有的操作都是原子性的，采用单线程处理所有业务，命令是一个一个执行的，因此无需考虑并发带来的数据影响

### 4.1.5 应用举例

比如B站up主的粉丝数和视频数，这是高频访问的信息，因此要存在redis中

解决方案

1. 在redis中为大V用户设定用户信息，以用户主键和属性值作为key，后台设定定时刷新策略即可。

```
user:id:3506728370:fans		→	12210947
user:id:3506728370:blogs	→	6164
user:id:3506728370:focuses	→	83
```

2. 也可以使用json格式保存数据

```
user:id:3506728370    →	{“fans”：12210947，“blogs”：6164，“ focuses ”：83 }
```

3. key 的设置约定

数据库中的热点数据key命名惯例

| **表名** | **主键名** | 主键值    | **字段名** |
| -------- | ---------- | --------- | ---------- |
| order    | id         | 29437595  | name       |
| equip    | id         | 390472345 | type       |
| news     | id         | 202004150 | title      |

## 4.2 Hash

### 4.2.1 简介

为了对数据更方便的进行管理，衍生出了Hash类型

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/2.png)

值得注意的是：

如果field数量较少，存储结构优化为类数组结构

如果field数量较多，存储结构使用HashMap结构

### 4.2.2 基本操作

添加/修改数据

```bash
hset key field value
```

获取数据

```bash
hget key field
hgetall key
```

删除数据

```bash
hdel key field1 [field2]
```

设置field的值，如果该field存在则不做任何操作

```bash
hsetnx key field value
```

添加/修改多个数据

```bash
hmset key field1 value1 field2 value2 …
```

获取多个数据

```bash
hmget key field1 field2 …
```

获取哈希表中字段的数量

```bash
hlen key
```

获取哈希表中是否存在指定的字段

```bash
hexists key field
```

### 4.2.3 扩展操作

获取哈希表中所有的字段名或字段值

```
hkeys key
hvals key
```

设置指定字段的数值数据增加指定范围的值

```
hincrby key field increment
hincrbyfloat key field increment
```

### 4.2.4 注意事项

1. hash类型中value只能存储字符串，不允许存储其他数据类型，不存在嵌套现象。如果数据未获取到，对应的值为（nil）
2. 每个hash可以存储 2^32 - 1 个键值对
   hash类型十分贴近对象的数据存储形式，并且可以灵活添加删除对象属性。但hash设计初衷不是为了存储大量对象而设计 的，切记不可滥用，更不可以将hash作为对象列表使用
3. hgetall操作可以获取全部属性，如果内部field过多，遍历整体数据效率就很会低，有可能成为数据访问瓶颈

## 4.3 List

### 4.3.1 简介

如果要维护数据的顺序，根据key存储一个list，list底层是双向链表

### 4.3.2 基本操作

添加/修改数据

```bash
lpush key value1 [value2] ……
rpush key value1 [value2] ……
```

获取数据

```bash
lrange key start stop
lindex key index
llen key
```

获取并移除数据

```bash
lpop key
rpop key
```

### 4.3.3 扩展操作

移除指定数据

```
lrem key count value
```

规定时间内获取并移除数据

```
blpop key1 [key2] timeout
brpop key1 [key2] timeout
brpoplpush source destination timeout
```

### 4.3.4 注意事项

1. list中保存的数据都是string类型的，数据总容量是有限的，最多232 - 1 个元素
2. list具有索引的概念，但是操作数据时通常以队列的形式进行入队出队操作，或以栈的形式进行入栈出栈操作
3. 获取全部数据操作结束索引设置为-1
4. list可以对数据进行分页操作，通常第一页的信息来自于list，第2页及更多的信息通过数据库的形式加载

### 4.3.5 应用

依赖list的数据具有顺序的特征对信息进行管理

使用队列模型解决多路信息汇总合并的问题

使用栈模型解决最新消息的问题

## 4.4 Set

### 4.4.1 简介

set类型：与hash存储结构完全相同，仅存储键，不存储值（nil），并且值是不允许重复的

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/3.png)

### 4.4.2 基本操作

添加数据

```bash
sadd key member1 [member2]
```

获取全部数据

```bash
smembers key
```

删除数据

```bash
srem key member1 [member2]
```

获取集合数据总量

```bash
scard key
```

判断集合中是否包含指定数据

```bash
sismember key member
```

随机获取集合中指定数量的数据

```bash
srandmember key [count]
```

随机获取集中的某个数据并将该数据移除集合

```bash
spop key [count]
```

### 4.4.3 扩展操作

求两个集合的交、并、差集

```
sinter key1 [key2 …]  
sunion key1 [key2 …]  
sdiff key1 [key2 …]
```

求两个集合的交、并、差集并存储到指定集合中

```
sinterstore destination key1 [key2 …]  
sunionstore destination key1 [key2 …]  
sdiffstore destination key1 [key2 …]
```

将指定数据从原始集合中移动到目标集合中

```
smove source destination member
```

### 4.4.4 注意事项

set 类型不允许数据重复，如果添加的数据在 set 中已经存在，将只保留一份

set 虽然与hash的存储结构相同，但是无法启用hash中存储值的空间

## 4.5 综合案例

结合微信接收消息分为普通用户和置顶用户

解决方案

依赖list的数据具有顺序的特征对消息进行管理，将list结构作为栈使用

置顶与普通会话分别创建独立的list分别管理

当某个list中接收到用户消息后，将消息发送方的id从list的一侧加入list（此处设定左侧）

多个相同id发出的消息反复入栈会出现问题，在入栈之前无论是否具有当前id对应的消息，先删除对应id

推送消息时先推送置顶会话list，再推送普通会话list，推送完成的list清除所有数据
消息的数量，也就是微信用户对话数量采用计数器的思想另行记录，伴随list操作同步更新

# 5. 常用指令

## 5.1 key的指令

### 5.1.1 基本操作

删除指定key

```bash
del key
```

获取key是否存在

```bash
exists key
```

获取key代表的类型

```bash
type key
```

排序

```bash
sort
```

改名

```bash
rename key newkey
renamenx key newkey
```

### 5.1.2 时效性操作

为指定key设置有效期

```bash
expire key seconds
pexpire key milliseconds
expireat key timestamp
pexpireat key milliseconds-timestamp
```

获取key的有效时间

```bash
ttl key
pttl key
```

切换key从时效性转换为永久性

```bash
persist key
```

注意

（Integer）-2代表失效

### 5.1.3 查询操作

查询key

```bash
keys pattern
```

查询模式规则

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/4.png)

## 5.2 数据库指令

### 5.2.1 存在的问题

key会存在大量重复

解决：Redis为每个服务提供有16个数据库，编号从0到15

### 5.2.2 基本操作

切换数据库

```
select index
```

Redis服务器是否启动

```
ping
```

数据移动

```
move key db
```

数据总量

```
dbsize
```

数据清除

```
flushdb  flushall
```

# 6. Jedis

## 6.1 简介

Jedis用于连接Java和Redis，提供对应的API

## 6.2 简单案例

导入jar包，下载地址：https://mvnrepository.com/artifact/redis.clients/jedis

Maven坐标

```xml
<dependency>
	<groupId>redis.clients</groupId>
	<artifactId>jedis</artifactId>
	<version>2.9.0</version>
</dependency>
```

示例代码

```java
public class JedisTest {
    public static void main(String[] args) {
        //1.获取连接对象
        Jedis jedis = new Jedis("192.168.40.130",6379);
        //2.执行操作
        jedis.set("age","39");
        String hello = jedis.get("hello");
        System.out.println(hello);
        jedis.lpush("list1","a","b","c","d");
        List<String> list1 = jedis.lrange("list1", 0, -1);
        for (String s:list1 ) {
            System.out.println(s);
        }
        jedis.sadd("set1","abc","abc","def","poi","cba");
        Long len = jedis.scard("set1");
        System.out.println(len);
        //3.关闭连接
        jedis.close();
    }
}
```

## 6.3 Jedis简单工具类

src下创建jedis.properties

```properties
jedis.host=192.168.40.130
jedis.port=6379
jedis.maxTotal=50
jedis.maxIdle=10
```

JedisUtils类

```java
public class JedisUtils {
    private static int maxTotal;
    private static int maxIdel;
    private static String host;
    private static int port;
    private static JedisPoolConfig jpc;
    private static JedisPool jp;
	//只加载一次资源
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        maxTotal = Integer.parseInt(bundle.getString("redis.maxTotal"));
        maxIdel = Integer.parseInt(bundle.getString("redis.maxIdel"));
        host = bundle.getString("redis.host");
        port = Integer.parseInt(bundle.getString("redis.port"));
        //设置连接池配置
        jpc = new JedisPoolConfig();
        jpc.setMaxTotal(maxTotal);
        jpc.setMaxIdle(maxIdel);
        jp = new JedisPool(jpc,host,port);
    }
}
//获取连接对象
public static Jedis getJedis(){
	Jedis jedis = jedisPool.getResource();
	return jedis;
}
```

## 6.4 可视化客户端

Redis Desktop Manager

开始连接不上

Windows上测试远程端口是否可用

```bash
telnet ip 6379
```

显示连接失败，说明Linux没有开放6379端口

Linux查看6379端口

```bash
firewall-cmd --query-port=6379/tcp
```

打开6379端口

```bash
firewall-cmd --add-port=6379/tcp
```

再测试端口6379就打开了

客户端连接成功

# 7. 持久化

## 7.1 简介

持久化利用永久性存储介质将数据进行保存，在特定的时间将保存的数据进行恢复的工作机制称为持久化

持久化用于防止数据的意外丢失，确保数据安全性

2种保存形式

第一种：将当前数据状态进行保存，快照形式，存储数据结果，存储格式简单，关注点在数据

第二种：将数据的操作过程进行保存，日志形式，存储操作过程，存储格式复杂，关注点在数据的操作过程

## 7.2 RDB

### 7.2.1 save指令

```
save
```

**save指令相关配置**

设置本地数据库文件名，默认值为 dump.rdb，通常设置为dump-端口号.rdb

```
dbfilename filename
```

设置存储.rdb文件的路径，通常设置成存储空间较大的目录中，目录名称data

```
dir path
```

设置存储至本地数据库时是否压缩数据，默认yes，设置为no，节省 CPU 运行时间，但存储文件变大

```
rdbcompression yes|no
```

设置读写文件过程是否进行RDB格式校验，默认yes，设置为no，节约读写10%时间消耗，但存在数据损坏的风险

```
rdbchecksum yes|no
```

**save指令工作原理**

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/5.png)

### 7.2.2 bgsave指令

bg其实是background的意思，后台执行的意思

手动启动后台保存操作，但不是立即执行

```
bgsave
```

**bgsave指令相关配置**

后台存储过程中如果出现错误现象，是否停止保存操作，默认yes

```
stop-writes-on-bgsave-error yes|no
```

其他

```
dbfilename filename  
dir path  
rdbcompression yes|no  
rdbchecksum yes|no
```

**bgsave指令工作原理**

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/6.png)

### 7.2.3 自动执行

设置自动持久化的条件，满足限定时间范围内key的变化数量达到指定数量即进行持久化

```
save second changes
```

参数

second：监控时间范围

changes：监控key的变化量

范例

```markdown
save 900 1
save 300 10
save 60 10000
```

其他相关配置

```markdown
dbfilename filename
dir path
rdbcompression yes|no
rdbchecksum yes|no
stop-writes-on-bgsave-error yes|no
```

**save配置工作原理**

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/7.png)

### 7.2.4 3种方案对比

| 方式           | save指令 | bgsave指令 |
| -------------- | -------- | ---------- |
| 读写           | 同步     | 异步       |
| 阻塞客户端指令 | 是       | 否         |
| 额外内存消耗   | 否       | 是         |
| 启动新进程     | 否       | 是         |

**RDB特殊启动形式**

服务器运行过程中重启

```bash
debug reload
```

关闭服务器时指定保存数据

```bash
shutdown save
```

全量复制（在主从复制中详细讲解）

**RDB优点：**

- RDB是一个紧凑压缩的二进制文件，存储效率较高
- RDB内部存储的是redis在某个时间点的数据快照，非常适合用于数据备份，全量复制等场景
- RDB恢复数据的速度要比AOF快很多
- 应用：服务器中每X小时执行bgsave备份，并将RDB文件拷贝到远程机器中，用于灾难恢复。

**RDB缺点**

- RDB方式无论是执行指令还是利用配置，无法做到实时持久化，具有较大的可能性丢失数据
- bgsave指令每次运行要执行fork操作创建子进程，要牺牲掉一些性能
- Redis的众多版本中未进行RDB文件格式的版本统一，有可能出现各版本服务之间数据格式无法兼容现象
- 存储数据量较大，效率较低，基于快照思想，每次读写都是全部数据，当数据量巨大时，效率非常低
- 大数据量下的IO性能较低

## 7.3 AOF

### 7.3.1 简介

AOF(append only file)持久化：以独立日志的方式记录每次写命令，重启时再重新执行AOF文件中命令达到恢复数据的目的。**与RDB相比可以简单理解为由记录数据改为记录数据产生的变化**

AOF的主要作用是解决了数据持久化的实时性，目前已经是Redis持久化的主流方式

### 7.3.2 执行过程

**AOF写数据过程**

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/8.png)

**启动AOF相关配置**

开启AOF持久化功能，默认no，即不开启状态

```
appendonly yes|no
```

AOF持久化文件名，默认文件名为appendonly.aof，建议配置为appendonly-端口号.aof

```
appendfilename filename
```

AOF持久化文件保存路径，与RDB持久化文件保持一致即可

```
dir
```

AOF写数据策略，默认为everysec

```
appendfsync always|everysec|no
```

- **always**(每次）：每次写入操作均同步到AOF文件中数据零误差，性能较低，不建议使用。

- **everysec**（每秒）：每秒将缓冲区中的指令同步到AOF文件中，在系统突然宕机的情况下丢失1秒内的数据 数据准确性较高，性能较高，建议使用，也是默认配置

- **no**（系统控制）：由操作系统控制每次同步到AOF文件的周期，整体过程不可控

### 7.3.3 AOF重写

问题

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/9.png)

Redis引入了AOF重写机制压缩文件体积。AOF文件重写是将Redis进程内的数据转化为写命令同步到新AOF文件的过程。简单说就是将对同一个数据的若干个条命令执行结 果转化成最终结果数据对应的指令进行记录。

**AOF重写作用**

- 降低磁盘占用量，提高磁盘利用率
- 提高持久化效率，降低持久化写时间，提高IO性能
- 降低数据恢复用时，提高数据恢复效率

**AOF重写规则**

- 进程内具有时效性的数据，并且数据已超时将不再写入文件

- 非写入类的无效指令将被忽略，只保留最终数据的写入命令

  如del key1、 hdel key2、srem key3、set key4 111、set key4 222等

  如select指令虽然不更改数据，但是更改了数据的存储位置，此类命令同样需要记录

- 对同一数据的多条写命令合并为一条命令

如lpushlist1 a、lpush list1 b、lpush list1 c可以转化为：lpush list1 a b c。

为防止数据量过大造成客户端缓冲区溢出，对list、set、hash、zset等类型，每条指令最多写入64个元素

**AOF重写方式**

手动重写

```
bgrewriteaof
```

手动重写原理

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/10.png)

自动重写

```
auto-aof-rewrite-min-size size
auto-aof-rewrite-percentage percentage
```

自动重写触发条件设置

```
auto-aof-rewrite-min-size size
auto-aof-rewrite-percentage percent
```

自动重写触发比对参数（ 运行指令info Persistence获取具体信息 ）

```
aof_current_size  
aof_base_size
```

 自动重写触发条件公式

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/11.png)

**工作流程**

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.28/pics/12.png)

## 7.4 RDB和AOF对比

### 7.4.1 RDB与AOF对比（优缺点）

| 持久化方式   | RDB                | AOF                |
| ------------ | ------------------ | ------------------ |
| 占用存储空间 | 小（数据级：压缩） | 大（指令级：重写） |
| 存储速度     | 慢                 | 快                 |
| 恢复速度     | 快                 | 慢                 |
| 数据安全性   | 会丢失数据         | 依据策略决定       |
| 资源消耗     | 高/重量级          | 低/轻量级          |
| 启动优先级   | 低                 | 高                 |

### 7.4.2 RDB与AOF应用场景

RDB与AOF的选择之惑

- 对数据非常敏感，建议使用默认的AOF持久化方案

AOF持久化策略使用everysecond，每秒钟fsync一次。该策略redis仍可以保持很好的处理性能，当出 现问题时，最多丢失0-1秒内的数据。

注意：由于AOF文件存储体积较大，且恢复速度较慢

- 数据呈现阶段有效性，建议使用RDB持久化方案

数据可以良好的做到阶段内无丢失（该阶段是开发者或运维人员手工维护的），且恢复速度较快，阶段的数据恢复通常采用RDB方案

注意：利用RDB实现紧凑的数据持久化会使Redis降的很低

- RDB与AOF的选择实际上是在做一种权衡，每种都有利有弊
- 如不能承受数分钟以内的数据丢失，对业务数据非常敏感，选用AOF
- 如能承受数分钟以内的数据丢失，且追求大数据集的恢复速度，选用RDB
- 灾难恢复选用RDB
- 双保险策略，同时开启 RDB和 AOF，重启后，Redis优先使用 AOF 来恢复数据，降低丢失数据的量