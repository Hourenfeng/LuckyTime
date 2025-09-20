
# 🚀 LuckyTime

> 这是一款短视频平台，拥有热度排行榜，热门视频，兴趣推送，关注推送，内容审核等功能

## 🌟 功能模块

![功能模块.png](https://tc-cdn.processon.com/po/624aef811e085307894a8e29-68cec286a1d77e5ff37c0355)

## 🗜 架构设计

![架构设计.jpg](https://tc-cdn.processon.com/po/624aef811e085307894a8e29-68cec30c5802951f40b23c7e)

## 🎨 截图
### 用户平台
登录注册
![注册.png](https://tc-cdn.processon.com/po/624aef811e085307894a8e29-68cec358ddf6b15eb1a582f4)
首页
![首页.png](https://tc-cdn.processon.com/po/624aef811e085307894a8e29-68cec3879642253faa291654)
视频播放
![播放视频.png](https://img.remit.ee/api/file/BQACAgUAAyEGAASHRsPbAAECRMhozsRZiCioVP7XF3AaNfLpwDUrLgACFSQAAtUHeVYNjMzV0m6REDYE.png)
个人中心
![创作中心.png](https://img.remit.ee/api/file/BQACAgUAAyEGAASHRsPbAAECRNdozsSdUgGfm_QEfcOSidEOJYjOKQACJSQAAtUHeVYNMxT2fXrykDYE.png)
### 后台管理
可管理首页的分类
![后台分类.png](https://img.remit.ee/api/file/BQACAgUAAyEGAASHRsPbAAECRNtozsS8PlHtVdqyzxffOWg61HNL-QACKiQAAtUHeVYBIQ_6tFgS1jYE.png)
使用RBAC实现权限模块,超级管理员可自行分配角色
![后台权限界面.png](https://img.remit.ee/api/file/BQACAgUAAyEGAASHRsPbAAECRNxozsS8TZoDVtei7SXSqL57dVxnIQACKyQAAtUHeVbwZlGWuChdYTYE.png)
系统配置中配置了审核力度、审核开关、热门视频热度限制、白名单
![后台审核界面.png](https://img.remit.ee/api/file/BQACAgUAAyEGAASHRsPbAAECROlozsUQoAjnwRat1qAgYcJfEoxQtwACOCQAAtUHeVYG2UKyQatmMTYE.png)
可对视频进行下架审核处理
![后台视频.png](https://img.remit.ee/api/file/BQACAgUAAyEGAASHRsPbAAECROpozsUUrE1P-OxdEaRbxP639VIomQACOSQAAtUHeVbkXNOLomhMYjYE.png)
## 📦 核心亮点
### - **兴趣推送**
群体：用户——兴趣推送；游客——热门推送
**设计**：
> 1个分类对应 n 标签，n 个标签对应 n 个视频
用户模型：
每个用户拥有用户模型，存储 redis 中，用户模型是 Map 结构， key：标签id，value：对应的概率

用户订阅分类后，实际是订阅了分类后的标签，将标签概率分布，后续兴趣推送则获取用户的标签，进行随机概率获取推送视频。浏览停留时长,点赞,收藏行为将对对应的标签概率进行增减
![用户模型.png](https://img.remit.ee/api/file/BQACAgUAAyEGAASHRsPbAAECRlJoztEeGnbYmmCaRVtXt4DDHkKoWgACqSUAAtUHeVatJGdNUc3WXzYE.png)
### - **热门排行榜**
**热度 = 权重 和 时差的计算**
权重 = 点赞,浏览,分享,收藏对应的占比例
时差：当前时间 - 视频发布时间的差值
> A视频24小时内点赞到了1W，B视频1小时内点赞到了1W，则说明B视频热度更高

这里可以采用**半衰期公式**计算热度

总结可以理解为  **当前时间 - 视频发布时间 差值为x ，x越小y越大，x越大y越小 后 对应的权重 得到热度** 
定时切片快速分页扫描全表，每个视频计算热度值后放入有界的小根堆,遍历完成再放入Redis -> TopK问题
### - **关注推送——Feed流**
用户拥有发件箱和收件箱
1. 用户所发布的视频存储在发件箱
数据结构：
```
Zset ttl:-1

key: out:follow:feed: + 用户id

value: 视频id

Score: 视频发布时间 
```
2. 收件箱存储用户关注人的视频
数据结构：
```
Zset ttl:7天

key：in:follow:feed: + 用户id

value: 视频id

score: 视频发布时间 
```
设计：推拉模式结合
推模式：只需要给在线用户推送；
拉模式：上线的时候去收件箱拉取。

```
用户发布一条视频后，放入自己的发件箱内，并主动推送到在线的用户的收件箱中，并且 ttl 为7天。
用户上线后，主动拉取收件箱中最新一条视频至当前时间之内的关注的人发件箱内的视频。
1. 如果收件箱无数据，则拉取关注人发件箱中的 7 天之内的视频
2. 如果收件箱有数据...
在点击关注视频模块的时候，从收件箱中获取视频显示给用户。
更新收件箱时机： 用户登录、刷新、首次或离线后访问关注页 
```




