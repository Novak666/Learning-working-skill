# 1. MySQL存储引擎

## 1.1 MySQL体系结构

+ 体系结构详解
  - 客户端连接
    - 支持接口：支持的客户端连接，例如C、Java、PHP等语言来连接MySQL数据库
  - 第一层：网络连接层
    - 连接池：管理、缓冲用户的连接，线程处理等需要缓存的需求。
    - 例如：当客户端发送一个请求连接，会从连接池中获取一个连接进行使用。
  - 第二层：核心服务层
    - 管理服务和工具：系统的管理和控制工具，例如备份恢复、复制、集群等。 
    - SQL接口：接受SQL命令，并且返回查询结果。
    - 查询解析器：验证和解析SQL命令，例如过滤条件、语法结构等。 
    - 查询优化器：在执行查询之前，使用默认的一套优化机制进行优化sql语句
    - 缓存：如果缓存当中有想查询的数据，则直接将缓存中的数据返回。没有的话再重新查询！
  - 第三层：存储引擎层
    - 插件式存储引擎：管理和操作数据的一种机制，包括(存储数据、如何更新、查询数据等)
  - 第四层：系统文件层
    - 文件系统：配置文件、数据文件、日志文件、错误文件、二进制文件等等的保存

## 1.2 存储引擎简介

- MySQL存储引擎的概念
  - MySQL数据库使用不同的机制存取表文件 , 机制的差别在于不同的存储方式、索引技巧、锁定水平以及广泛的不同的功能和能力，在MySQL中 , 将这些不同的技术及配套的功能称为**存储引擎**
  - 在关系型数据库中数据的存储是以表的形式存进行储的，所以存储引擎也可以称为**表类型**（即存储和操作此表的类型）。
  - Oracle , SqlServer等数据库只有一种存储引擎 , 而MySQL针对不同的需求, 配置MySQL的不同的存储引擎 , 就会让数据库采取了不同的处理数据的方式和扩展功能。
  - 通过选择不同的引擎 ,能够获取最佳的方案 ,  也能够获得额外的速度或者功能，提高程序的整体效果。所以了解引擎的特性 , 才能贴合我们的需求 , 更好的发挥数据库的性能。
- MySQL支持的存储引擎
  - MySQL5.7支持的引擎包括：InnoDB、MyISAM、MEMORY、Archive、Federate、CSV、BLACKHOLE等
  - 其中较为常用的有三种：InnoDB、MyISAM、MEMORY

## 1.3 存储引擎对比

- 常用的存储引擎
  - MyISAM存储引擎
    - 访问快,不支持事务和外键。表结构保存在.frm文件中，表数据保存在.MYD文件中，索引保存在.MYI文件中。
  - InnoDB存储引擎(MySQL5.5版本后默认的存储引擎)
    - 支持事务 ,占用磁盘空间大 ,支持并发控制。表结构保存在.frm文件中，如果是共享表空间，数据和索引保存在 innodb_data_home_dir 和 innodb_data_file_path定义的表空间中，可以是多个文件。如果是多表空间存储，每个表的数据和索引单独保存在 .ibd 中。
  - MEMORY存储引擎
    - 内存存储 , 速度快 ,不安全 ,适合小量快速访问的数据。表结构保存在.frm中。
- 特性对比

| 特性         | MyISAM                       | InnoDB        | MEMORY             |
| ------------ | ---------------------------- | ------------- | ------------------ |
| 存储限制     | 有(平台对文件系统大小的限制) | 64TB          | 有(平台的内存限制) |
| **事务安全** | **不支持**                   | **支持**      | **不支持**         |
| **锁机制**   | **表锁**                     | **表锁/行锁** | **表锁**           |
| B+Tree索引   | 支持                         | 支持          | 支持               |
| 哈希索引     | 不支持                       | 不支持        | 支持               |
| 全文索引     | 支持                         | 支持          | 不支持             |
| **集群索引** | **不支持**                   | **支持**      | **不支持**         |
| 数据索引     | 不支持                       | 支持          | 支持               |
| 数据缓存     | 不支持                       | 支持          | N/A                |
| 索引缓存     | 支持                         | 支持          | N/A                |
| 数据可压缩   | 支持                         | 不支持        | 不支持             |
| 空间使用     | 低                           | 高            | N/A                |
| 内存使用     | 低                           | 高            | 中等               |
| 批量插入速度 | 高                           | 低            | 高                 |
| **外键**     | **不支持**                   | **支持**      | **不支持**         |

## 1.4 存储引擎的操作

- 查询数据库支持的引擎

```mysql
-- 标准语法
SHOW ENGINES;

-- 查询数据库支持的存储引擎
SHOW ENGINES;
```

```mysql
-- 表含义:
  - support : 指服务器是否支持该存储引擎
  - transactions : 指存储引擎是否支持事务
  - XA : 指存储引擎是否支持分布式事务处理
  - Savepoints : 指存储引擎是否支持保存点
```

- 查询某个数据库中所有数据表的引擎

```mysql
-- 标准语法
SHOW TABLE STATUS FROM 数据库名称;

-- 查看db9数据库所有表的存储引擎
SHOW TABLE STATUS FROM db9;
```

- 查询某个数据库中某个数据表的引擎

```mysql
-- 标准语法
SHOW TABLE STATUS FROM 数据库名称 WHERE NAME = '数据表名称';

-- 查看db9数据库中stu_score表的存储引擎
SHOW TABLE STATUS FROM db9 WHERE NAME = 'stu_score';
```

- 创建数据表，指定存储引擎

```mysql
-- 标准语法
CREATE TABLE 表名(
	列名,数据类型,
    ...
)ENGINE = 引擎名称;

-- 创建db11数据库
CREATE DATABASE db11;

-- 使用db11数据库
USE db11;

-- 创建engine_test表，指定存储引擎为MyISAM
CREATE TABLE engine_test(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(10)
)ENGINE = MYISAM;

-- 查询engine_test表的引擎
SHOW TABLE STATUS FROM db11 WHERE NAME = 'engine_test';
```

- 修改表的存储引擎

```mysql
-- 标准语法
ALTER TABLE 表名 ENGINE = 引擎名称;

-- 修改engine_test表的引擎为InnoDB
ALTER TABLE engine_test ENGINE = INNODB;

-- 查询engine_test表的引擎
SHOW TABLE STATUS FROM db11 WHERE NAME = 'engine_test';
```

## 1.5 总结

- MyISAM ：由于MyISAM不支持事务、不支持外键、支持全文检索和表级锁定，读写相互阻塞，读取速度快，节约资源，所以如果应用是以**查询操作**和**插入操作**为主，只有很少的**更新和删除**操作，并且对事务的完整性、并发性要求不是很高，那么选择这个存储引擎是非常合适的。
- InnoDB : 是MySQL的默认存储引擎， 由于InnoDB支持事务、支持外键、行级锁定 ，支持所有辅助索引(5.5.5后不支持全文检索)，高缓存，所以用于对事务的完整性有比较高的要求，在并发条件下要求数据的一致性，读写频繁的操作，那么InnoDB存储引擎是比较合适的选择，比如BBS、计费系统、充值转账等
- MEMORY：将所有数据保存在RAM中，在需要快速定位记录和其他类似数据环境下，可以提供更快的访问。MEMORY的缺陷就是对表的大小有限制，太大的表无法缓存在内存中，其次是要确保表的数据可以恢复，数据库异常终止后表中的数据是可以恢复的。MEMORY表通常用于更新不太频繁的小表，用以快速得到访问结果。
- 总结：针对不同的需求场景，来选择最适合的存储引擎即可！如果不确定、则使用数据库默认的存储引擎！

# 2. MySQL索引

## 2.1 简介

- MySQL数据库中的索引：是帮助MySQL高效获取数据的一种数据结构！所以，索引的本质就是数据结构。
- 在表数据之外，数据库系统还维护着满足特定查找算法的数据结构，这些数据结构以某种方式指向数据， 这样就可以在这些数据结构上实现高级查找算法，这种数据结构就是索引。
- 一张数据表，用于保存数据。   一个索引配置文件，用于保存索引，每个索引都去指向了某一个数据(表格演示)
- 举例，无索引和有索引的查找原理

![1](C:\Users\HASEE\Desktop\pics\1.png)

## 2.2 索引的分类

- 功能分类 
  - 普通索引： 最基本的索引，它没有任何限制。
  - 唯一索引：索引列的值必须唯一，但允许有空值。如果是组合索引，则列值组合必须唯一。
  - 主键索引：一种特殊的唯一索引，不允许有空值。一般在建表时同时创建主键索引。
  - 组合索引：顾名思义，就是将单列索引进行组合。
  - 外键索引：只有InnoDB引擎支持外键索引，用来保证数据的一致性、完整性和实现级联操作。
  - 全文索引：快速匹配全部文档的方式。InnoDB引擎5.6版本后才支持全文索引。MEMORY引擎不支持。
- 结构分类
  - B+Tree索引 ：MySQL使用最频繁的一个索引数据结构，是InnoDB和MyISAM存储引擎默认的索引类型。
  - Hash索引 : MySQL中Memory存储引擎默认支持的索引类型。

## 2.3 索引的操作

- 数据准备

```mysql
-- 创建db12数据库
CREATE DATABASE db12;

-- 使用db12数据库
USE db12;

-- 创建student表
CREATE TABLE student(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(10),
	age INT,
	score INT
);
-- 添加数据
INSERT INTO student VALUES (NULL,'张三',23,98),(NULL,'李四',24,95),
(NULL,'王五',25,96),(NULL,'赵六',26,94),(NULL,'周七',27,99);
```

- 创建索引
  - 注意：如果一个表中有一列是主键，那么就会默认为其创建主键索引！(主键列不需要单独创建索引)

```mysql
-- 标准语法
CREATE [UNIQUE|FULLTEXT] INDEX 索引名称
[USING 索引类型]  -- 默认是B+TREE
ON 表名(列名...);

-- 为student表中姓名列创建一个普通索引
CREATE INDEX idx_name ON student(NAME);

-- 为student表中年龄列创建一个唯一索引
CREATE UNIQUE INDEX idx_age ON student(age);
```

- 查看索引

```mysql
-- 标准语法
SHOW INDEX FROM 表名;

-- 查看student表中的索引
SHOW INDEX FROM student;
```

- alter语句添加索引

```mysql
-- 普通索引
ALTER TABLE 表名 ADD INDEX 索引名称(列名);

-- 组合索引
ALTER TABLE 表名 ADD INDEX 索引名称(列名1,列名2,...);

-- 主键索引
ALTER TABLE 表名 ADD PRIMARY KEY(主键列名); 

-- 外键索引(添加外键约束，就是外键索引)
ALTER TABLE 表名 ADD CONSTRAINT 外键名 FOREIGN KEY (本表外键列名) REFERENCES 主表名(主键列名);

-- 唯一索引
ALTER TABLE 表名 ADD UNIQUE 索引名称(列名);

-- 全文索引(mysql只支持文本类型)
ALTER TABLE 表名 ADD FULLTEXT 索引名称(列名);


-- 为student表中name列添加全文索引
ALTER TABLE student ADD FULLTEXT idx_fulltext_name(name);

-- 查看student表中的索引
SHOW INDEX FROM student;
```

- 删除索引

```mysql
-- 标准语法
DROP INDEX 索引名称 ON 表名;

-- 删除student表中的idx_score索引
DROP INDEX idx_score ON student;

-- 查看student表中的索引
SHOW INDEX FROM student;
```

## 2.4 索引的原理

- 索引是在MySQL的存储引擎中实现的，所以每种存储引擎的索引不一定完全相同，也不是所有的引擎支持所有的索引类型。这里我们主要介绍InnoDB引擎的实现的**B+Tree索引**。
- B+Tree是一种树型数据结构，是B-Tree的变种。通常使用在数据库和操作系统中的文件系统，特点是能够保持数据稳定有序。我们逐步的来了解一下。

### 2.4.1 磁盘存储

- 系统从磁盘读取数据到内存时是以磁盘块（block）为基本单位的
- 位于同一个磁盘块中的数据会被一次性读取出来，而不是需要什么取什么。
- InnoDB存储引擎中有页（Page）的概念，页是其磁盘管理的最小单位。InnoDB存储引擎中默认每个页的大小为16KB。
- InnoDB引擎将若干个地址连接磁盘块，以此来达到页的大小16KB，在查询数据时如果一个页中的每条数据都能有助于定位数据记录的位置，这将会减少磁盘I/O次数，提高查询效率。

### 2.4.2 B-Tree

+ BTree结构的数据可以让系统高效的找到数据所在的磁盘块。为了描述BTree，首先定义一条记录为一个二元组[key, data] ，key为记录的键值，对应表中的主键值，data为一行记录中除主键外的数据。对于不同的记录，key值互不相同。BTree中的每个节点根据实际情况可以包含大量的关键字信息和分支，如下图所示为一个3阶的BTree： 

![2](C:\Users\HASEE\Desktop\pics\2.png)

- 根据图中结构显示，每个节点占用一个盘块的磁盘空间，一个节点上有两个升序排序的关键字和三个指向子树根节点的指针，指针存储的是子节点所在磁盘块的地址。两个关键词划分成的三个范围域对应三个指针指向的子树的数据的范围域。以根节点为例，关键字为17和35，P1指针指向的子树的数据范围为小于17，P2指针指向的子树的数据范围为17~35，P3指针指向的子树的数据范围为大于35。

查找顺序：

```mysql
模拟查找15的过程 : 

1.根节点找到磁盘块1，读入内存。【磁盘I/O操作第1次】
	比较关键字15在区间（<17），找到磁盘块1的指针P1。
2.P1指针找到磁盘块2，读入内存。【磁盘I/O操作第2次】
	比较关键字15在区间（>12），找到磁盘块2的指针P3。
3.P3指针找到磁盘块7，读入内存。【磁盘I/O操作第3次】
	在磁盘块7中找到关键字15。
	
-- 分析上面过程，发现需要3次磁盘I/O操作，和3次内存查找操作。
-- 由于内存中的关键字是一个有序表结构，可以利用二分法查找提高效率。而3次磁盘I/O操作是影响整个BTree查找效率的决定因素。BTree使用较少的节点个数，使每次磁盘I/O取到内存的数据都发挥了作用，从而提高了查询效率。
```

### 2.4.3 B+Tree

- B+Tree是在BTree基础上的一种优化，使其更适合实现外存储索引结构，InnoDB存储引擎就是用B+Tree实现其索引结构。
- 从上一节中的BTree结构图中可以看到每个节点中不仅包含数据的key值，还有data值。而每一个页的存储空间是有限的，如果data数据较大时将会导致每个节点（即一个页）能存储的key的数量很小，当存储的数据量很大时同样会导致B-Tree的深度较大，增大查询时的磁盘I/O次数，进而影响查询效率。在B+Tree中，所有数据记录节点都是按照键值大小顺序存放在同一层的叶子节点上，而非叶子节点上只存储key值信息，这样可以大大加大每个节点存储的key值数量，降低B+Tree的高度。
- B+Tree相对于BTree区别：
  - 非叶子节点只存储键值信息。
  - 所有叶子节点之间都有一个连接指针。
  - 数据记录都存放在叶子节点中。
- 将上一节中的BTree优化，由于B+Tree的非叶子节点只存储键值信息，假设每个磁盘块能存储4个键值及指针信息，则变成B+Tree后其结构如下图所示：

![3](C:\Users\HASEE\Desktop\pics\3.png)

通常在B+Tree上有两个头指针，一个指向根节点，另一个指向关键字最小的叶子节点，而且所有叶子节点（即数据节点）之间是一种链式环结构。因此可以对B+Tree进行两种查找运算：

- 【有范围】对于主键的范围查找和分页查找
- 【有顺序】从根节点开始，进行随机查找

实际情况中每个节点可能不能填充满，因此在数据库中，B+Tree的高度一般都在2~4层。MySQL的InnoDB存储引擎在设计时是将根节点常驻内存的，也就是说查找某一键值的行记录时最多只需要1~3次磁盘I/O操作。

# 3. MySQL锁机制

## 3.1 简介

+ 锁机制 : 数据库为了保证数据的一致性，而使用各种共享的资源在被并发访问时变得有序所设计的一种规则。

+ 在数据库中，数据是一种供许多用户共享访问的资源，如何保证数据并发访问的一致性、有效性，是所有数据库必须解决的一个问题，MySQL由于自身架构的特点，在不同的存储引擎中，都设计了面对特定场景的锁定机制，所以引擎的差别，导致锁机制也是有很大差别的。

## 3.2 分类

- 按操作分类：
  - 共享锁：也叫读锁。针对同一份数据，多个事务读取操作可以同时加锁而不互相影响 ，但是不能修改数据记录。
  - 排他锁：也叫写锁。当前的操作没有完成前,会阻断其他操作的读取和写入
- 按粒度分类：
  - 表级锁：操作时，会锁定整个表。开销小，加锁快；不会出现死锁；锁定力度大，发生锁冲突概率高，并发度最低。偏向于MyISAM存储引擎！
  - 行级锁：操作时，会锁定当前操作行。开销大，加锁慢；会出现死锁；锁定粒度小，发生锁冲突的概率低，并发度高。偏向于InnoDB存储引擎！
  - 页级锁：锁的粒度、发生冲突的概率和加锁的开销介于表锁和行锁之间，会出现死锁，并发性能一般。
- 按使用方式分类：
  - 悲观锁：每次查询数据时都认为别人会修改，很悲观，所以查询时加锁。
  - 乐观锁：每次查询数据时都认为别人不会修改，很乐观，但是更新时会判断一下在此期间别人有没有去更新这个数据
- 不同存储引擎支持的锁

| 存储引擎 | 表级锁 | 行级锁 | 页级锁 |
| -------- | ------ | ------ | ------ |
| MyISAM   | 支持   | 不支持 | 不支持 |
| InnoDB   | 支持   | 支持   | 不支持 |
| MEMORY   | 支持   | 不支持 | 不支持 |
| BDB      | 支持   | 不支持 | 支持   |

## 3.3 InnoDB共享锁

- 数据准备

```mysql
-- 创建db13数据库
CREATE DATABASE db13;

-- 使用db13数据库
USE db13;

-- 创建student表
CREATE TABLE student(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(10),
	age INT,
	score INT
);
-- 添加数据
INSERT INTO student VALUES (NULL,'张三',23,99),(NULL,'李四',24,95),
(NULL,'王五',25,98),(NULL,'赵六',26,97);
```

- 共享锁

```mysql
-- 标准语法
SELECT语句 LOCK IN SHARE MODE;
```

```mysql
-- 窗口1
/*
	共享锁：数据可以被多个事务查询，但是不能修改
*/
-- 开启事务
START TRANSACTION;

-- 查询id为1的数据记录。加入共享锁
SELECT * FROM student WHERE id=1 LOCK IN SHARE MODE;

-- 查询分数为99分的数据记录。加入共享锁
SELECT * FROM student WHERE score=99 LOCK IN SHARE MODE;

-- 提交事务
COMMIT;
```

```mysql
-- 窗口2
-- 开启事务
START TRANSACTION;

-- 查询id为1的数据记录(普通查询，可以查询)
SELECT * FROM student WHERE id=1;

-- 查询id为1的数据记录，并加入共享锁(可以查询。共享锁和共享锁兼容)
SELECT * FROM student WHERE id=1 LOCK IN SHARE MODE;

-- 修改id为1的姓名为张三三(不能修改，会出现锁的情况。只有窗口1提交事务后，才能修改成功)
UPDATE student SET NAME='张三三' WHERE id = 1;

-- 修改id为2的姓名为李四四(修改成功，InnoDB引擎默认是行锁)
UPDATE student SET NAME='李四四' WHERE id = 2;

-- 修改id为3的姓名为王五五(注意：InnoDB引擎如果不采用带索引的列。则会提升为表锁)
UPDATE student SET NAME='王五五' WHERE id = 3;

-- 提交事务
COMMIT;
```

## 3.4 InnoDB排他锁

- 排他锁

```mysql
-- 标准语法
SELECT语句 FOR UPDATE;
```

```mysql
-- 窗口1
/*
	排他锁：加锁的数据，不能被其他事务加锁查询或修改
*/
-- 开启事务
START TRANSACTION;

-- 查询id为1的数据记录，并加入排他锁
SELECT * FROM student WHERE id=1 FOR UPDATE;

-- 提交事务
COMMIT;
```

```mysql
-- 窗口2
-- 开启事务
START TRANSACTION;

-- 查询id为1的数据记录(普通查询没问题)
SELECT * FROM student WHERE id=1;

-- 查询id为1的数据记录，并加入共享锁(不能查询。因为排他锁不能和其他锁共存)
SELECT * FROM student WHERE id=1 LOCK IN SHARE MODE;

-- 查询id为1的数据记录，并加入排他锁(不能查询。因为排他锁不能和其他锁共存)
SELECT * FROM student WHERE id=1 FOR UPDATE;

-- 修改id为1的姓名为张三(不能修改，会出现锁的情况。只有窗口1提交事务后，才能修改成功)
UPDATE student SET NAME='张三' WHERE id=1;

-- 提交事务
COMMIT;
```

- 注意：锁的兼容性
  - 共享锁和共享锁     兼容
  - 共享锁和排他锁     冲突
  - 排他锁和排他锁     冲突
  - 排他锁和共享锁     冲突

## 3.5 MyISAM读锁

- 数据准备

```mysql
-- 创建product表
CREATE TABLE product(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20),
	price INT
)ENGINE = MYISAM;  -- 指定存储引擎为MyISAM

-- 添加数据
INSERT INTO product VALUES (NULL,'华为手机',4999),(NULL,'小米手机',2999),
(NULL,'苹果',8999),(NULL,'中兴',1999);
```

- 读锁

```mysql
-- 标准语法
-- 加锁
LOCK TABLE 表名 READ;

-- 解锁(将当前会话所有的表进行解锁)
UNLOCK TABLES;
```

```mysql
-- 窗口1
/*
	读锁：所有连接只能读取数据，不能修改
*/
-- 为product表加入读锁
LOCK TABLE product READ;

-- 查询product表(查询成功)
SELECT * FROM product;

-- 修改华为手机的价格为5999(修改失败)
UPDATE product SET price=5999 WHERE id=1;

-- 解锁
UNLOCK TABLES;
```

```mysql
-- 窗口2
-- 查询product表(查询成功)
SELECT * FROM product;

-- 修改华为手机的价格为5999(不能修改，窗口1解锁后才能修改成功)
UPDATE product SET price=5999 WHERE id=1;
```

## 3.6 MyISAM写锁

- 写锁

```mysql
-- 标准语法
-- 加锁
LOCK TABLE 表名 WRITE;

-- 解锁(将当前会话所有的表进行解锁)
UNLOCK TABLES;
```

```mysql
-- 窗口1
/*
	写锁：其他连接不能查询和修改数据
*/
-- 为product表添加写锁
LOCK TABLE product WRITE;

-- 查询product表(查询成功)
SELECT * FROM product;

-- 修改小米手机的金额为3999(修改成功)
UPDATE product SET price=3999 WHERE id=2;

-- 解锁
UNLOCK TABLES;
```

```mysql
-- 窗口2
-- 查询product表(不能查询。只有窗口1解锁后才能查询成功)
SELECT * FROM product;

-- 修改小米手机的金额为2999(不能修改。只有窗口1解锁后才能修改成功)
UPDATE product SET price=2999 WHERE id=2;
```

## 3.7 悲观锁和乐观锁

- 悲观锁的概念

  - 就是很悲观，它对于数据被外界修改的操作持保守态度，认为数据随时会修改。
  - 整个数据处理中需要将数据加锁。悲观锁一般都是依靠关系型数据库提供的锁机制。
  - 我们之前所学的行锁，表锁不论是读写锁都是悲观锁。

- 乐观锁的概念

  - 就是很乐观，每次自己操作数据的时候认为没有人会来修改它，所以不去加锁。
  - 但是在更新的时候会去判断在此期间数据有没有被修改。
  - 需要用户自己去实现，不会发生并发抢占资源，只有在提交操作的时候检查是否违反数据完整性。

- 悲观锁和乐观锁使用前提

  - 对于读的操作远多于写的操作的时候，这时候一个更新操作加锁会阻塞所有的读取操作，降低了吞吐量。最后还要释放锁，锁是需要一些开销的，这时候可以选择乐观锁。
  - 如果是读写比例差距不是非常大或者系统没有响应不及时，吞吐量瓶颈的问题，那就不要去使用乐观锁，它增加了复杂度，也带来了业务额外的风险。这时候可以选择悲观锁。

- 乐观锁的实现方式

  - 版本号

    - 给数据表中添加一个version列，每次更新后都将这个列的值加1。
    - 读取数据时，将版本号读取出来，在执行更新的时候，比较版本号。
    - 如果相同则执行更新，如果不相同，说明此条数据已经发生了变化。
    - 用户自行根据这个通知来决定怎么处理，比如重新开始一遍，或者放弃本次更新。

    ```mysql
    -- 创建city表
    CREATE TABLE city(
    	id INT PRIMARY KEY AUTO_INCREMENT,  -- 城市id
    	NAME VARCHAR(20),                   -- 城市名称
    	VERSION INT                         -- 版本号
    );
    
    -- 添加数据
    INSERT INTO city VALUES (NULL,'北京',1),(NULL,'上海',1),(NULL,'广州',1),(NULL,'深圳',1);
    
    -- 修改北京为北京市
    -- 1.查询北京的version
    SELECT VERSION FROM city WHERE NAME='北京';
    -- 2.修改北京为北京市，版本号+1。并对比版本号
    UPDATE city SET NAME='北京市',VERSION=VERSION+1 WHERE NAME='北京' AND VERSION=1;
    ```

  - 时间戳

    - 和版本号方式基本一样，给数据表中添加一个列，名称无所谓，数据类型需要是timestamp
    - 每次更新后都将最新时间插入到此列。
    - 读取数据时，将时间读取出来，在执行更新的时候，比较时间。
    - 如果相同则执行更新，如果不相同，说明此条数据已经发生了变化。

## 3.8 总结

- 表锁和行锁
  - 行锁：锁的粒度更细，加行锁的性能损耗较大。并发处理能力较高。InnoDB引擎默认支持！
  - 表锁：锁的粒度较粗，加表锁的性能损耗较小。并发处理能力较低。InnoDB、MyISAM引擎支持！
- InnoDB锁优化建议
  - 尽量通过带索引的列来完成数据查询，从而避免InnoDB无法加行锁而升级为表锁。
  - 合理设计索引，索引要尽可能准确，尽可能的缩小锁定范围，避免造成不必要的锁定。
  - 尽可能减少基于范围的数据检索过滤条件。
  - 尽量控制事务的大小，减少锁定的资源量和锁定时间长度。
  - 在同一个事务中，尽可能做到一次锁定所需要的所有资源，减少死锁产生概率。
  - 对于非常容易产生死锁的业务部分，可以尝试使用升级锁定颗粒度，通过表级锁定来减少死锁的产生。

