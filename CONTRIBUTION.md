# 如何贡献代码
欢迎新加入的小伙伴，因为706的技术团队大家很多时候都是靠自己的热情加入，希望给706的线上平台奉献一些自己的力量，
所以我们为了让后来的小伙伴能更好、更快地融入，所以以开源的方式来做这个后端项目，也因此我们在代码的规范上会对大家有更严格的要求。

## Git 工作流程

详细流程请参考 [Git flow](https://danielkummer.github.io/git-flow-cheatsheet/index.zh_CN.html)

我们将完全按照这个规范走，初始化git flow的时候记得 release 的 prefix 需要设置为 v 

### 分支名规范
我们开的新 feature 名字格式是：[issue-id]-xxxxxx

其中 issue-id 是对应这个项目issue页面里的id，如果一些特殊情况，我们可以使用 0 为 issue-id。这样做的好处是我们之后可以更快地找到对应的需求文档

### 版本号规范
在新建 release 分支的时候，分支名的格式是 x.y.z，这代表了我们会上线的版本号，其中 x y z 都是数字格式。

x 代表了大版本的更新，这一位如果升位的话基本上意味着大部分的代码都无法兼容，包括但不限于更换开发语言、更换开发框架、升级不兼容的开发框架版本等，但是和业务的大版本迭代无关

y 代表了业务需求的更新，我们针对每个需求集合（可能会由若干个 feature组成）都需要在这个数字上加一。

z 代表了技术bug的更新，我们修复了一些纯技术方面的bug之后需要发布新版本就在这位数字上加一，然后新的业务版本更新之后（y增加后）z 归零

## 代码格式规范

### 代码格式
这里建议大家使用 intellij idea 进行开发，用 vscode 和 eclipse 的小伙伴可能需要考虑该如何应用代码格式的插件

在 Preferences... > Editor > Code Style > Java > Scheme 里使用 GoogleStyle，为了让大家都更方便地加入项目，在这个scheme之上不做任何修改

然后推荐大家安装插件 [Save Actions](https://github.com/dubreuia/intellij-plugin-save-actions#installation) ，可以直接在 plugin 的 marketplace 里搜索 Save Actions，安装之后勾上

Activate save actions on save、Optimize imports、Reformat file、Rearrange fields and methods

### 数据库字段格式
数据库表使用复数形式，如 users，accounts

数据库字段使用下划线方式连接，如 created_at

数据库索引名为 [类型]_[名字]_[涉及字段]

* 比方说一个普通索引 idx_xx_school
* 一个唯一索引 uniq_xx_email
* 一个复合索引 idx_xx_name_age_gender