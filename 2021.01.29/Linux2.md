# 1. Linux文件管理

## 1.1 touch命令

touch命令用于创建文件、修改文件或者目录的时间属性，包括存取时间和更改时间。若文件不存在，系统会建立一个新的文件。

ls -l 可以显示档案的时间记录

**使用 touch 创建一个空文件**

在 Linux 系统上使用 `touch` 命令创建空文件，键入 `touch`，然后输入文件名。如下所示

```shell
touch czbk-devops.txt
```

**使用 touch 创建批量空文件**

在实际的开发过程中可能会出现一些情况，我们必须为某些测试创建大量空文件，这可以使用 `touch` 命令轻松实现

```shell
touch czbk-{1..10}.txt
```

 **关于stat命令：**

**stat命令用于显示inode内容。**

stat以文字的格式来显示inode的内容。

**语法**

```
stat [文件或目录]
```

## 1.2 vi与vim命令

### 1.2.1 vi/vim模式

vi/vim模式主要分为以下三种：

**命令模式**：在Linux终端中输入“vim 文件名”就进入了命令模式,但不能输入文字。
**编辑模式：**在命令模式下按i就会进入编辑模式，此时就可以写入程式，按Esc可回到命令模式。
**末行模式：**在命令模式下按：进入末行模式，左下角会有一个冒号出现，此时可以敲入命令并执行。

### 1.2.2 三种模式切换

**1、进入命令模式**

上接上面的例子，我们执行下面的命令其实就是进入了命令模式

```shell
vim txtfile.txt
```

- 如果文件已经存在, 会直接打开该文件
- 如果文件不存在, 保存且退出时 就会新建一个文件

**2、进入编辑模式**

上接上面的例子，按i进入插入模式

- 在 vi 中除了常用 `i` 进入**编辑模式** 外, 还提供了以下命令同样可以进入编辑模式

| 命令 | 英文   | 功能                   | 常用   |
| ---- | ------ | ---------------------- | ------ |
| i    | insert | 在当前字符前插入文本   | 常用   |
| I    | insert | 在行首插入文本         | 较常用 |
| a    | append | 在当前字符后添加文本   |        |
| A    | append | 在行末添加文本         | 较常用 |
| o    |        | 在当前行后面插入一空行 | 常用   |
| O    |        | 在当前行前面插入一空行 | 常用   |

**3、进入末行模式**

编辑模式不能保存文件
必须先退到命令模式
先按Esc键退出到命令模式

:q            当vim进入文件没有对文件内容做任何操作可以按"q"退出

:q!           当vim进入文件对文件内容有操作但不想保存退出

:wq          正常保存退出

:wq!         强行保存退出，只针对与root用户或文件所有人生

### 1.2.3 文件查看

以下5个为文件查看命令，**我们只讲4个常用的命令，head不在赘述**

| 序号 | 命令               | 对应英文    | 作用                             |
| :--- | ------------------ | ----------- | -------------------------------- |
| 01   | cat 文件名         | concatenate | 查看小文件内容                   |
| 02   | less -N 文件名     | less        | **分频** 显示大文件内容          |
| 03   | head -n 文件名     |             | 查看文件的**前一**部分           |
| 04   | tail -n 文件名     |             | 查看文件的**最后**部分           |
| 05   | grep 关键字 文件名 | grep        | 根据**关键词**, 搜索文本文件内容 |

#### 4) tail命令

**1、要显示 txtfile.txt  文件的最后 3 行，请输入以下命令：**

```shell
tail -3 txtfile.txt 
```

**2、动态显示文档的最后内容,一般用来查看日志，请输入以下命令：**

```shell
tail -f txtfile.txt
```

此命令显示 txtfile.txt 文件的最后 10 行。当将某些行添加至 txtfile.txt 文件时，tail 命令会继续显示这些行。 显示一直继续，直到您按下（Ctrl-C）组合键停止显示。

#### 5) grep命令

1、搜索 **存在关键字【eeee】** 的行的文件

```shell
grep eeee txtfile.txt 
```

2、搜索 **存在关键字【eeee】** 的行 且 **显示行号**

```shell
grep -n eeee txtfile.txt 
```

3、**忽略大小写** 搜索 **存在关键字** 的行

```shell
grep -i EEEE txtfile.txt 
```

4、搜索 **不存在关键字** 的行

```shell
grep -v 中国 txtfile.txt 
```

**5、查找指定的进程信息（包含grep进程）**

```shell
ps -ef | grep  sshd
```

**6、查找指定的进程信息（不包含grep进程）**

```shell
ps aux | grep sshd | grep -v "grep"
```

**7、查找进程个数**

```shell
 ps -ef|grep -c sshd
```

### 1.2.4 vim定位行

我们打开文件定位到第6行，如下：

```shell
vim txtfile.txt +6
```

### 1.2.5 vim异常处理

如果 vim异常退出, 在磁盘上可能会保存有 交换文件

解决方案：

将后缀名为.swp的文件删除即可恢复

## 1.3 echo命令

**使用者权限：所有用户**

- `echo string` 将字符串输出到控制台 ,  通常和 **重定向** 联合使用

```
# 如果字符串有空格, 为了避免歧义 请增加 双引号 或者 单引号
echo "hello world"
```

- **第一步: 将命令的成功结果 覆盖 指定文件内容**

```
 echo  传智博客 >czbk-txt.txt
```

- ##### 第二步: 将**命令的成功结果** **追加**  指定文件的后面

```
echo  黑马程序员 >> czbk-txt.txt
```

- ##### 第三步: 将**命令的失败结果** **追加** 指定文件的后面

```
cat 不存在的目录  &>>  error.log
```

## 1.4 awk命令

AWK是一种处理文本文件的语言，是一个强大的文本分析工具。

**1、数据准备：czbk-txt.txt文本内容如下：**

```shell
zhangsan 68 99 26
lisi 98 66 96
wangwu 38 33 86
zhaoliu 78 44 36
maq 88 22 66
zhouba 98 44 46
```

 **2、搜索含有 zhang  和 li 的学生成绩：**

```shell
cat czbk-txt.txt | awk '/zhang|li/'
```

**3、指定分割符, 根据下标显示内容**

| 命令                                        | 含义                                                         |
| ------------------------------------------- | ------------------------------------------------------------ |
| awk   -F  ','    '{print $1, $2, $3}'  文件 | 操作1.txt文件,  根据 逗号 分割, 打印 第一段 第二段 第三段 内容 |

选项

| 选项       | 英文            | 含义                     |
| ---------- | --------------- | ------------------------ |
| `-F ','`   | field-separator | 使用 **指定字符** 分割   |
| `$ + 数字` |                 | 获取**第几段**内容       |
| `$0`       |                 | 获取 **当前行** 内容     |
| `NF`       | field           | 表示当前行共有多少个字段 |
| `$NF`      |                 | 代表 最后一个字段        |
| `$(NF-1)`  |                 | 代表 倒数第二个字段      |
| `NR`       |                 | 代表 处理的是第几行      |

```
# 查看文档内容
cat czbk-txt.txt 
#直接输出
cat score.txt | awk -F ' ' '{print $1,$2,$3}'
```

**4、指定分割符, 根据下标显示内容**

| 命令                                                    | 含义                                                         |
| ------------------------------------------------------- | ------------------------------------------------------------ |
| awk   -F  ' '    '{OFS="==="}{print $1, $2, $3}'  1.txt | 操作1.txt文件,  根据 逗号 分割, 打印 第一段 第二段 第三段 内容 |

选项

| 选项         | 英文                   | 含义                     |
| ------------ | ---------------------- | ------------------------ |
| `OFS="字符"` | output field separator | 向外输出时的段分割字符串 |

| 转义序列 | 含义   |
| -------- | ------ |
| \b       | 退格   |
| \f       | 换页   |
| \n       | 换行   |
| \r       | 回车   |
| \t       | 制表符 |

```
# 按照 === 进行分割, 打印 第一段 第二段 第三段
cat  czbk-txt.txt | awk -F ' ' '{OFS="==="}{print $1,$2,$3}'
# 按照 制表符tab 进行分割, 打印 第一段 第二段 第三段
cat czbk-txt.txt| awk -F ' ' '{OFS="\t"}{print $1,$2,$3}'
```

**5、调用 awk 提供的函数**

| 命令                                           | 含义                                                         |
| ---------------------------------------------- | ------------------------------------------------------------ |
| awk   -F  ','    '{print  toupper($2)}'  1.txt | 操作1.txt文件,  根据 逗号 分割, 打印 第一段 第二段 第三段 内容 |

常用函数如下:

| 函数名    | 含义   | 作用           |
| --------- | ------ | -------------- |
| toupper() | upper  | 字符 转成 大写 |
| tolower() | lower  | 字符 转成小写  |
| length()  | length | 返回 字符长度  |

```
# 打印第一段内容
 cat czbk-txt.txt | awk -F ' ' '{print $1}'
# 将第一段内容转成大写 且 显示 
 cat czbk-txt.txt | awk -F ' ' '{print toupper($1)}'
```

**6、求指定学科平均分**

| 命令                                                         | 含义                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| awk 'BEGIN{初始化操作}{每行都执行} END{结束时操作}'   文件名 | BEGIN{ 这里面放的是执行前的语句 }<br />{这里面放的是处理每一行时要执行的语句}<br />END {这里面放的是处理完所有的行后要执行的语句 } |

**查看总分**

注意：这里计算的是第4列的总分

```shell
cat czbk-txt.txt| awk -F ' ' 'BEGIN{}{total=total+$4} END{print total}'
```

**查看总分, 总人数, 平均分**

注意：这里计算的是第4列的

```shell
 cat czbk-txt.txt | awk -F ' ' 'BEGIN{}{total=total+$4} END{print total, NR, (total/NR)}'
```

总结

awk在使用过程中主要用作分析

简单来说awk就是把文件逐行的读入，以空格为默认分隔符将每行切片，切开的部分再进行各种分析处理

## 1.5 软连接

**语法如下:**

| 命令                                      | 英文 | 作用                                         |
| ----------------------------------------- | ---- | -------------------------------------------- |
| ln **-s**  目标文件绝对路径  快捷方式路径 | link | 给目标文件增加一个软链接, 通俗讲就是快捷方式 |

**给home/itcast/txtfile.txt文件增加软连接**

```shell
 ln  -s /home/itcast/txtfile.txt    czbk-txt
```

上面；我们将/home/itcast/路径下的txtfile.txt文件增加软连接到

czbk-txt，然后通过cat 访问czbk-txt也是可以正常访问的

## 1.6 find查找

**1、将目前目录及其子目录下所有延伸档名是 gz 的文件查询出来** 

```shell
find . -name "*.gz"
```

**2、将目前目录及其子目录下所有最近 1天内更新过的文件查询出来**

```shell
find . -ctime -1
```

**3、全局搜索czbk**

/代表是全盘搜索,也可以指定目录搜索 

```shell
 find / -name  'czbk'
```

# 2. Linux备份压缩

## 2.1 gzip命令

gzip命令用于压缩文件。

gzip是个使用广泛的压缩程序，文件经它压缩过后，其名称后面会多出".gz"的扩展名

**1、压缩目录下的所有文件**

```shell
gzip * 
```

**2、解压文件列出详细的信息**

```shell
gzip -dv *
```

## 2.2 gunzip命令

gunzip命令用于解压文件。

**语法**

```
gunzip[参数][文件或者目录]
```

```shell
gunzip 001.gz 
```

## 2.3 tar命令

tar的主要功能是打包、压缩和解压文件。

tar本身不具有压缩功能。他是调用压缩功能实现的 。

**1、将 txtfile.txt文件打包（仅打包，不压缩）**

txtfile.txt文件为上面章节的例子

```shell
tar -cvf txt.tar txtfile.txt 
```

 **2、将 txtfile.txt文件打包压缩（打包压缩（gzip））**

```shell
tar -zcvf txt.tar.gz txtfile.txt 
```

参数 f 之后的文件档名是自己取的，我们习惯上都用 .tar 来作为辨识。 如果加 z 参数，则以 .tar.gz 或 .tgz 来代表 gzip 压缩过的 tar包

**3、查看tar中有哪些文件**

```shell
tar -ztvf txt.tar.gz
```

**4、将tar 包解压缩**

```shell
1.新建目录
 mkdir ysFiles
2.复制
 cp txt.tar.gz ./ysFiles/
3.解压缩
 tar -zxvf /home/itcast/ysFiles/txt.tar.gz
```

## 2.4 zip命令

开始压缩

```shell
2.压缩
zip -q -r zFiles.zip *
```

## 2.5 unzip命令

Linux unzip命令用于解压缩zip文件

unzip为.zip压缩文件的解压缩程序

**1、查看压缩文件中包含的文件：**

```shell
 unzip -l zFiles.zip
```

**2、如果要把文件解压到指定的目录下，需要用到-d参数**

```shell
1.新建目录
unFiles
2.解压缩
unzip -d ./unFiles zFiles.zip
```

## 2.6 bzip2命令

bzip2命令是.bz2文件的压缩程序。

bzip2采用新的压缩演算法，压缩效果比传统的LZ77/LZ78压缩演算法来得好。若没有加上任何参数，bzip2压缩完文件后会产生.bz2的压缩文件，并删除原始的文件。

## 2.7 bunzip2命令

**解压.bz2文件**

```shell
bunzip2 -v 001.bz2 
```

-v显示详细信息

# 3. Linux网络与磁盘管理

## 3.1 网络命令

### 3.1.1 ifconfig命令

**1、显示激活的网卡信息**

```
ifconfig
```

**2、关闭网卡（需要切换到管理员账户）**

```shell
 ifconfig ens37 down
```

**3、启用网卡（需要切换到管理员账户）**

```shell
ifconfig ens37 up
```

**4、配置ip信息**

```shell
// 配置ip地址
ifconfig ens37 192.168.23.199
// 配置ip地址和子网掩码
ifconfig ens37 192.168.23.133 netmask 255.255.255.0
```

### 3.1.2 ping命令

**1、检测是否与主机连通**

```shell
 ping www.baidu.com
```

**2、指定接收包的次数**

和上面不同的是：收到两次包后，自动退出

```shell
ping -c 2 www.baidu.com
```

### 3.1.3 netstat

netstat命令用于显示网络状态。

**1、显示详细的连接状况**

```shell
netstat -a
```

**2、显示网卡列表**

```shell
netstat -i
```

## 3.2 磁盘管理命令

### 3.2.1 lsblk命令

lsblk命令的英文是“list block”，即用于列出所有可用块设备的信息，而且还能显示他们之间的依赖关系，但是它不会列出RAM盘的信息。

**1、lsblk命令默认情况下将以树状列出所有块设备：**

```shell
lsblk
```

**2、默认选项不会列出所有空设备：**

```shell
lsblk -f
```

### 3.2.2 df命令

**1、显示磁盘使用情况统计情况**

```
df  
```

**2、df命令也可以显示磁盘使用的文件系统信息**

比如我们df下之前创建过的目录gzipTest的使用情况

```
df  gzipTest/
```

**3、df显示所有的信息**

```
df --total 
```

 **4、df换算后显示**

```shell
df -h 
```

### 3.2.3 mount命令

mount命令是经常会使用到的命令，它用于挂载Linux系统外的文件。

**挂载概念**

在安装linux系统时设立的各个分区，如根分区、/boot分区等都是自动挂载的，也就是说不需要我们人为操作，开机就会自动挂载。但是光盘、u盘等存储设备如果需要使用，就必须人为的进行挂载。

其实我们在windows下插入U盘也是需要挂载(分配盘符)的，只不过windows下分配盘符是自动的

Linux中的根目录以外的文件要想被访问，需要将其“关联”到根目录下的某个目录来实现，这种关联操作就是“挂载”，这个目录就是“挂载点”，解除次关联关系的过程称之为“卸载”。

**注意：“挂载点”的目录需要以下几个要求：**

（1）目录事先存在，可以用mkdir命令新建目录；

（2）挂载点目录不可被其他进程使用到；

（3）挂载点下原有文件将被隐藏。

**2、创建挂载点**

**3、开始挂载**

通过挂载点的方式查看上面的【ISO文件内容】

```shell
mount -t auto /dev/cdrom /mnt/cdrom
```

**4、查看挂载点内容**

```shell
ls -l -a ./mnt/cdrom/
```

**5、卸载cdrom**

在前面我们将CD/DVD挂载到了文件系统，如果我们不用了，就可以将其卸载掉

```
umount ./mnt/cdrom/
```

# 4. yum

## 4.1 安装

```shell
yum -y install tree   //y当安装过程提示选择全部为"yes"
```

注意：第一次在普通用户执行的时候

提示我们【需要管理员权限】

## 4.2 卸载

```shell
yum remove  tree
```

## 4.3 查找

利用 yum 的功能，找出以 tom 为开头的软件名称有哪些

```shell
yum list tom*
```

## 4.4 yum源

**安装阿里yum源**

因为默认的yum源服务器在国外，我们在安装软件的时候会受到速度的影响，所以安装国内yum源在下载的时候速度、稳定性会比国外的好很多。

**1) 安装wget**

```shell
yum -y install wget
```

**2) 备份/etc/yum.repos.d/CentOS-Base.repo文件**

```shell
cd /etc/yum.repos.d/
mv CentOS-Base.repo CentOS-Base.repo.back
```

**3) 下载阿里云的Centos-7.repo文件**

```shell
wget -O CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
```

查看下载的阿里云的Centos-6.repo文件

```shell
cat CentOS-Base.repo
```

**4) 重新加载yum**

```shell
yum clean all
```

清理之前（CentOS）的缓存

```shell
yum makecache
```

就是把服务器的包信息下载到本地电脑缓存起来，makecache建立一个缓存，以后用install时就在缓存中搜索，提高了速度

**5) 验证yum源使用**

```shell
yum search tomcat
```

# 5. shell

## 5.1 shell简介

编写第一个shell

现在，我们打开文本编辑器(我们也可以使用 vi/vim 命令来创建文件)，新建一个文件 czbk.sh，扩展名为 sh（sh代表shell）：

```shell
#!/bin/bash  --- 指定脚本解释器
echo "你好，传智播客 !"
```

**1、创建sh文件**

```shell
vim czbk.sh
```

**2、编写并保存**

**3、查看czbk.sh文件**

```shell
ls -l
```

**通过chmod设置执行权限**

```shell
chmod +x ./czbk.sh
```

**4、执行czbk.sh文件**

```shell
 ./czbk.sh  或者 bash czbk.sh 
```

## 5.2 shell注释

**1、单行注释**

以 **#** 开头的行就是注释，会被解释器忽略。

**2、多行注释**

```shell
:<<符号
注释内容...
注释内容...
注释内容...
符号
```

符号一般可以用 ! 来代替

## 5.3 shell变量

**1、定义变量:**

```
variable_name="czbk"
```

变量名和等号之间不能有空格

**2、使用变量**

使用一个定义过的变量，只要在变量名前面加美元符号即可，如：

```shell
variable_name="czbk"
echo $variable_name
echo ${variable_name}
```

变量名外面的花括号是可选的，加不加都行，加花括号是为了帮助解释器识别变量的边界，比如下面这种情况：

```shell
 echo "I am good at ${shell-t}Script"
```

**3、只读变量**

使用 readonly 命令可以将变量定义为只读变量，只读变量的值不能被改变。

下面的例子尝试更改只读变量，结果报错：

```shell
#!/bin/bash
myUrl="https://www.baidu.com"
readonly myUrl
myUrl="https://cn.bing.com/"
```

**4、删除变量**

使用 unset 命令可以删除变量。语法：

```
unset variable_name
```

变量被删除后不能再次使用。unset 命令不能删除只读变量。

**Shell 字符串**

**单引号**

```shell
str='this is a string variable'
```

单引号字符串的限制：

- 单引号里的任何字符都会原样输出，单引号字符串中的变量是无效的；
- 单引号字串中不能出现单独一个的单引号（对单引号使用转义符后也不行），但可成对出现，作为字符串拼接使用。

**双引号**

```shell
your_name='frank'
str="Hello,  \"$your_name\"! \n"
echo -e $str
```

双引号的优点：

- 双引号里可以有变量
- 双引号里可以出现转义字符

## 5.4 shell数组

**定义数组**

在 Shell 中，用括号来表示数组，数组元素用"空格"符号分割开。如下：

```shell
数组名=(值1 值2 ... 值n)
```

**读取数组**

读取数组元素值的一般格式是：

```shell
${数组名[下标]}
```

例如：

```shell
valuen=${array_name[n]}
```

使用 **@** 符号可以获取数组中的所有元素，例如：

```shell
echo ${array_name[@]}
```

**获取数组的长度**

获取数组长度的方法与获取字符串长度的方法相同，例如：

```shell
# 取得数组元素的个数
length=${#array_name[@]}
# 或者
length=${#array_name[*]}
```

## 5.5 shell运算符

**1、算数运算符**

```
val=`expr 2 + 2`
echo "相加之后的结果为：" $val
```

注意：

**表达式和运算符之间要有空格**，例如 2+2 是不对的，必须写成 2 + 2。

完整的表达式要被 **`** 包含，注意不是单引号。

| **运算符** | **说明**                                      | **举例**                      |
| ---------- | --------------------------------------------- | ----------------------------- |
| +          | 加法                                          | `expr $a + $b` 结果为 30。    |
| -          | 减法                                          | `expr $a - $b` 结果为 -10。   |
| *          | 乘法                                          | `expr $a \* $b` 结果为  200。 |
| /          | 除法                                          | `expr $b / $a` 结果为 2。     |
| %          | 取余                                          | `expr $b % $a` 结果为 0。     |
| =          | 赋值                                          | a=$b 将把变量 b 的值赋给 a。  |
| ==         | 相等。用于比较两个数字，相同则返回 true。     | [ $a == $b ] 返回 false。     |
| !=         | 不相等。用于比较两个数字，不相同则返回 true。 | [ $a != $b ] 返回 true。      |

**注意：**条件表达式要放在方括号之间，并且要有空格，例如: **[$a==$b]** 是错误的，必须写成 **[ $a == $b ]**。

**2、字符串运算符**

下表列出了常用的字符串运算符，假定变量 a 为 "abc"，变量 b 为 "efg"：

| 运算符 | 说明                                      | 举例                     |
| :----- | :---------------------------------------- | :----------------------- |
| =      | 检测两个字符串是否相等，相等返回 true。   | [ $a = $b ] 返回 false。 |
| !=     | 检测两个字符串是否相等，不相等返回 true。 | [ $a != $b ] 返回 true。 |
| -z     | 检测字符串长度是否为0，为0返回 true。     | [ -z $a ] 返回 false。   |
| -n     | 检测字符串长度是否为0，不为0返回 true。   | [ -n "$a" ] 返回 true。  |
| $      | 检测字符串是否为空，不为空返回 true。     | [ $a ] 返回 true。       |

**<font color='red'>注意：shell中0为真，1为假</font>**

**3、关系运算符**

关系运算符只支持数字，不支持字符串，除非字符串的值是数字。

下表列出了常用的关系运算符，假定变量 a 为 10，变量 b 为 20：

| 运算符 | 说明                                                  | 举例                       |
| :----- | :---------------------------------------------------- | :------------------------- |
| -eq    | 检测两个数是否相等，相等返回 true。                   | [ $a -eq $b ] 返回 false。 |
| -ne    | 检测两个数是否不相等，不相等返回 true。               | [ $a -ne $b ] 返回 true。  |
| -gt    | 检测左边的数是否大于右边的，如果是，则返回 true。     | [ $a -gt $b ] 返回 false。 |
| -lt    | 检测左边的数是否小于右边的，如果是，则返回 true。     | [ $a -lt $b ] 返回 true。  |
| -ge    | 检测左边的数是否大于等于右边的，如果是，则返回 true。 | [ $a -ge $b ] 返回 false。 |
| -le    | 检测左边的数是否小于等于右边的，如果是，则返回 true。 | [ $a -le $b ] 返回 true。  |

**4、布尔运算符**

下表列出了常用的布尔运算符，假定变量 a 为 10，变量 b 为 20：

| 运算符 | 说明                                                | 举例                                     |
| :----- | :-------------------------------------------------- | :--------------------------------------- |
| !      | 非运算，表达式为 true 则返回 false，否则返回 true。 | [ ! false ] 返回 true。                  |
| -o     | 或运算，有一个表达式为 true 则返回 true。           | [ $a -lt 20 -o $b -gt 100 ] 返回 true。  |
| -a     | 与运算，两个表达式都为 true 才返回 true。           | [ $a -lt 20 -a $b -gt 100 ] 返回 false。 |

**5、逻辑运算符**

假定变量 a 为 10，变量 b 为 20:

| 运算符 | 说明       | 举例                                       |
| :----- | :--------- | :----------------------------------------- |
| &&     | 逻辑的 AND | [[ $a -lt 100 && $b -gt 100 ]] 返回 false  |
| \|\|   | 逻辑的 OR  | [[ $a -lt 100 \|\| $b -gt 100 ]] 返回 true |

## 5.6 shell流程控制

**1、if 语句：**

**主要用于判断，相当于java se中的if，我们还是采用之前的例子test-shell.sh**

```shell
if condition
then
    command1 
    command2
    ...
    commandN 
fi
```

 比如，我们现在通过前面学习的知识查找一个进程，如果进程存在就打印true

```shell
if [ $(ps -ef | grep -c "ssh") -gt 1 ]
then 
	echo "true"
fi
```

**2、if else 语句：**

**主要用于判断，相当于java se中的if else，我们还是采用之前的例子test-shell.sh。**

```shell
if condition
then
    command1 
    command2
    ...
    commandN
else
    command
fi
```

上接上面的例子，如果找不到sshAAA**（此处可以随便输入一个）**进程，我们就打印false

```shell
if [ $(ps -ef | grep -c "sshAAA") -gt 1 ]
then
	echo "true"
	else echo "false"
fi
```

**3、if else-if else 语句：**

**主要用于判断，相当于java se中的if else-if else**

```shell
if condition1
then
    command1
elif condition2 
then 
    command2
else
    commandN
fi
```

以下实例判断两个变量是否相等

我们继续使用上面的例子（test-shell.sh ）

```shell
a=10
b=20
if [ $a == $b ]
then
   echo "a 等于 b"
elif [ $a -gt $b ]
then
   echo "a 大于 b"
elif [ $a -lt $b ]
then
   echo "a 小于 b"
else
   echo "没有符合的条件"
fi
```

**4、case ... esac语句**

**主要用于分支条件选择，相当于java se中的switch case循环**

**case ... esac** 与其他语言中的 switch ... case 语句类似，是一种多分枝选择结构，每个 case 分支用右圆括号开始，用两个分号 **;;** 表示 break，即执行结束，跳出整个 case ... esac 语句，esac（就是 case 反过来）作为结束标记。

还是采用之前的例子test-shell.sh

case ... esac 语法格式如下：

```shell
case 值 in
模式1)
    command1
    command2
    command3
    ;;
模式2）
    command1
    command2
    command3
    ;;
*)
    command1
    command2
    command3
    ;;
esac
```

case 后为取值，值可以为变量或常数。

值后为关键字 in，接下来是匹配的各种模式，每一模式最后必须以右括号结束，模式支持正则表达式。

下面通过v的值进行case--esac

```shell
v="czbk"

case "$v" in
   "czbk") 
   		echo "传智播客"
   ;;
   "baidu") 
   		echo "baidu 搜索"
   ;;
   "google") 
   		echo "google 搜索"
   ;;
esac
```

**5、for 循环**

**主要用于循环，相当于java se中的for循环，我们还是采用之前的例子test-shell.sh**

for循环格式为

```shell
for var in item1 item2 ... itemN
do
    command1
    command2
    ...
    commandN
done
```

顺序输出当前列表中的字母：

```shell
for loop in A B C D E F G 
do
    echo "顺序输出字母为: $loop"
done
```

**6、while循环**

主要用于循环，相当于java se中的while循环

while循环用于不断执行一系列命令，也用于从输入文件中读取数据 

语法格式为

```
while condition
do
    command
done
```

以下是一个基本的while循环，测试条件是：如果int小于等于10，那么条件返回真。int从0开始，每次循环处理时，int加1。 

还是采用之前的例子test-shell.sh

```shell
#!/bin/bash
int=1
while [ $int -le 10 ]
do
    echo "${int}"
    ((int++))
done
```

## 5.7 shell函数

```shell
#!/bin/bash

czbk(){
    echo "这是第一个函数!"
}
echo "-----这里是函数开始执行-----"
czbk
echo "-----这里是函数执行完毕-----"
```

下面，我们定义一个带有return语句的函数：

```shell
function czbk(){
    echo "对输入的两个数字进行相加运算..."
    echo "输入第一个数字: "
    read aNum
    echo "输入第二个数字: "
    read anotherNum
    echo "两个数字分别为 $aNum 和 $anotherNum !"
    return $(($aNum+$anotherNum))
}
czbk
echo "输入的两个数字之和为 $? !"
```

> 注意：
>
> 函数返回值在调用该函数后通过 $? 来获得。

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.29/pics/1.jpg)

