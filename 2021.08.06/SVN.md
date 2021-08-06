# 1. SVN

## 1.1 SVN是什么

代码版本管理工具

记住每一次的修改

查看所有的修改记录

恢复到任何历史版本

恢复已经删除的文件

## 1.2 SVN与Git比

使用简单，上手快

目录级权限控制，企业安全必备

子目录Checkout，减少不必要的文件检出

## 1.3 主要应用

开发人员用来做代码的版本管理

用来存储一些重要的文件，比如合同

公司内部文件共享，并且能按目录划分权限

## 1.4 其他 

SVN仓库：

svnbucket

SVN客户端：

TortoiseSVN

Cornstone

# 2. SVN基本操作

前置条件：安装svnbucket(服务端)和TortoiseSVN(客户端)

## 2.1 SVN Checkout

建立本地文件夹

## 2.2 SVN Commit

提交

## 2.3 SVN Update

更新

## 2.3 本地查看日志信息

TortoiseSVN->show log

# 3. 撤销和恢复

## 3.1 撤销本地

TortoiseSVN->Revert或者点击SVN Commit，然后右键选中变化的文件，点击Revert

## 3.2 撤销已提交

TortoiseSVN->show log，右键最新错误版本，点击Revert changes from this revision(先update)

## 3.3 恢复到指定版本

TortoiseSVN->show log，右键历史版本恢复，点击Revert to this revision

# 4. 添加忽略

右键不想提交的文件，点击Unversion and add to ignore list，不想忽略反选就可以

# 5. 解决冲突

产生冲突时机：

1. 多个人修改了同个文件的同一行
2. 无法进行合并的二进制文件

避免冲突：

1. 经常update他人的代码
2. 二进制文件不要多个人同时操作

解决冲突：

1. 使用对方的
2. 使用自己的
3. 编辑冲突

# 6. 分支

SVN经典目录结构：

1. trunk
2. branches
3. tags

选择主干，右键创建分支，update

右键属性，在subversion中复制地址，可换一个地方checkout

分支TortoiseSVN->show log，选择相应的提交合并到主干上

还可以切换分支

查看服务器端的目录结构

# 7. 暂存

开发时修改了很多代码，但是代码没有写完，没有经过测试，不能提交，同时线上版本出现bug需要修复，就需要先把代码暂存起来。shelve会使暂存后代码变化，checkpoint暂存后代码不会改变

# 8. 复杂代码合并

问题情景：

主干由于开发了新功能，代码改变了许多

分支是线上版本，修复了很多bug

2个分支隔的时间久，无法直接合并或是指定提交记录合并

利用第三方工具Beyond Compare

就是逐一对比