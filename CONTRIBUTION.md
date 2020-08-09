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

### 关于 feature 分支

正常情况下，我们只需要把 feature 放在本地就好，如果需要多人在周一个feature下协作的时候，需要先 git flow feature publish 一下本地的 feature 分支，然后对方 git flow feature pull 对应的分支

然后一个需求开发完成之后我们就会删除对应的 feature 分支，这样才不至于在项目发展的后期有大量的不活跃的分支存在

在 finish feature分支之前，记得用 git flow feature rebase 把 develop 分支最新的内容更新到 feature 分支上，解决完冲突之后再继续提交 

### 关于 master 分支

master 分支的最低线要求是可以在任一个时间点无人工干预自动更新（因为我们需要解放更多的人力专注在开发，而不是运维之上）

### 关于 git commit 规范

一定要写清楚 commit 的 message，标准只有一条：
* 除了只更新文档或者注释的情况外，把 message 给提交者看，提交者要能大致说出来代码改动了什么，解决了什么问题，或者 fix 了哪个 issue

相关的改动请尽可能放在同一个 commit 里，不相关的改动请分成不同的 feature 和 commit，灵活使用 git commit --amend

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

## 关于文档

我们一定要写足文档和注释，因为我们很有可能面临着某个功能开发的小伙伴长期离开的情况。所以这一块儿的标准只有一条，要求也只有一条：

* 标准：一个新加入的小伙伴可以完全不通过和代码开发者交流的情况下，弄懂代码逻辑
* 要求：如果有发生任何一次的代码口头交流的情景，在口头交流之后请把交流的内容补充到注释或者文档里

复杂的注释可以用文档的方式直接放到对应代码的目录下，以.md 结尾，当然也可以放在代码头部采用块注释的方式

## 关于包结构

* com.er706.backend 是我们的根目录，在根目录下不存放和业务相关的东西
* 第一级目录为对应的业务，比方说 com.er706.backend.user 或者 com.er705.backend.activity
* 第二级目录为 services / controllers / daos / models 等，注意复数

这样做的原因主要也是为了更好地让后来的小伙伴更容易理解业务逻辑，试想一下如果第一级目录为 controllers，然后一个新人想看看代码，点开一看，50来个 controller，当时就崩溃了。如果我们第一级是业务目录，然后里面再按功能分，就会好很多。

## 关于URL接口

我们采用 restful 的接口设计，具体原则请看这里 [RESTful API 最佳实践](https://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html)
