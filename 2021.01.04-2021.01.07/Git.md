---
typora-root-url: ..\00_阶段一-Java基础课件\5.Git\day10_Git\笔记\img
---

# 1. Git作用

1. 代码备份 (云端)
2. 版本控制
3. 协同工作 (团队)
4. 责任追溯



# 2. Git特点

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/1.png)



# 3. Git操作

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/2.png)

先将代码添加到暂存区，再提交到本地仓库

git常用命令

| 命令                     | 作用                                           |
| ------------------------ | ---------------------------------------------- |
| git init                 | 初始化，创建 git 仓库                          |
| git status               | 查看 git 状态 （文件是否进行了添加、提交操作） |
| git add 文件名           | 添加，将指定文件添加到暂存区                   |
| git commit -m '提交信息' | 提交，将暂存区文件提交到历史仓库               |
| git log                  | 查看日志（ git 提交的历史日志）                |

<font color='red'>git --version    查看版本</font>

<font color='red'>git config --global init.defaultBranch main   git在2.28.0上，重新设置git默认分支为main</font>

例子

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/3.png)



# 4. Git版本管理

## 4.1 历史版本切换

+ 准备动作

1. 查看 my_project 的 log 日志
   git reflog ：可以查看所有分支的所有操作记录（包括已经被删除的 commit 记录的操作）
2. 增加一次新的修改记录

- 需求: 将代码切换到第二次修改的版本

  指令：git reset --hard 版本唯一索引值

## 4.2 分支管理介绍

分支

- 由每次提交的代码，串成的一条时间线
- <font color = 'red'>使用分支意味着你可以把你的工作从开发主线上分离开来,以免影响开发主线</font>

分支的使用场景

1. 周期较长的模块开发
2. 尝试性的模块开发

分支工作流程

- Master: 指向提交的代码版本
- Header: 指向当前所使用的的分支

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/4.png)

## 4.3 分支操作

+ <font color = 'red'>创建分支</font>

  创建命令：git branch 分支名

+ <font color = 'red'>切换分支</font>

  切换命令：git checkout 分支名

+ <font color = 'red'>合并分支</font>

  合并命令：git merge 分支名

+ <font color = 'red'>删除分支</font>

  删除命令：git branch -d 分支名

+ <font color = 'red'>查看分支</font>

  查看命令：git branch

附加：不同分支之间的关系是平行的关系，不会相互影响



# 5. 远程仓库

## 5.1 工作流程

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/5.png)

## 5.2 远程仓库介绍

- GitHub

  域名：https://github.com
  介绍：GitHub是全球最大的开源项目托管平台，俗称大型程序员社区化交友网站

  ​	    各类好玩有趣的开源项目，只有想不到，没有找不到。

- 码云

  域名：https://gitee.com
  介绍：码云是全国最大的开源项目托管平台，良心平台，速度快，提供免费私有库

## 5.3 远程仓库操作

### 5.3.1 先有本地项目，远程仓库为空

+ 步骤

1. 创建本地仓库
2. 创建或修改文件，添加（add）文件到暂存区，提交（commit）到本地仓库
3. 创建远程仓库
4. 推送到远程仓库

+ <font color = 'red'>**创建远程仓库的说明**</font>

  + 现在码云上创建

  + 生成SSH公钥

    1. 设置Git账户

    - git config user.name（查看git账户）
    - git config user.email（查看git邮箱）
    - git config --global user.name “账户名”（设置全局账户名）
    - git config --global user.email “邮箱”（设置全局邮箱）
    - cd ~/.ssh（查看是否生成过SSH公钥）

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/6.png)

​		2. 生成SSH公钥

​		生成命令: ssh-keygen –t rsa –C “邮箱” ( 注意：这里需要敲3次回车)

​		查看命令: cat ~/.ssh/id_rsa.pub

​		3. 设置账户公钥

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/7.png)

​		4. 公钥测试

​		命令: ssh -T git@gitee.com

+ <font color = 'red'>**推送到远程仓库的说明**</font>

推送到远程仓库

- 步骤
  1. 为远程仓库的URL（网址），自定义远程仓库名称（别名）
  2. 推送
- 命令
  git remote add 远程仓库别名 远程仓库URL
  git push -u 远程仓库别名 分支名

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/8.png)

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/9.png)

### 5.3.2 先有远程仓库，本地项目为空

1. 将远程仓库的代码，克隆到本地仓库
   克隆命令：git clone 仓库地址
2. 创建新文件，添加并提交到本地仓库
3. 推送至远程仓库
4. 项目拉取更新
   拉取命令：git pull 远程仓库别名 分支名

## 5.4 代码冲突

+ 如何解决冲突

  <<<<<<<和>>>>>>>中间的内容,就是冲突部分

  1. 修改冲突行，保存，即可解决冲突。
  2. 重新add冲突文件并commit到本地仓库，重新push到远程



# 6. IDEA集成Git

创建仓库

1. 选择工程所在的目录（模块上一级）,这样就创建好本地仓库了
2. 点击git后边的对勾,将当前项目代码提交到本地仓库

![](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.01.04-2021.01.07/pics/10.png)

其他操作包括版本切换 分支管理（创建 切换 合并 删除）向远程仓库推送 克隆到本地

