# CineStream — JavaFX Online Movie Booking System

这是一个以马来西亚线上电影院订票体验为灵感的 JavaFX Final Assignment。原本的注册、登录、电影与场次选择、选座、价格计算、付款、电子票及订票记录功能均保留，并重新设计为统一的黑紫色响应式界面。

## 设计主题

- **Primary:** `#7630e7`
- **Button:** `#420e96`
- **Background:** 黑色与 `#7630e7` 渐变
- **Text:** 白色
- 启动时窗口会自动最大化，也可自由调整窗口大小。
- 按钮、电影卡片、座位和功能卡片均有平滑 hover 缩放及高亮反馈。

## 图片替换位置

所有展示图片均放在：

```text
Final_Assignment/Final_Assignment/src/CineStream/assets/
├── banner/
│   ├── cinema-hero.png       # 首页横幅，建议 1600 × 560
│   └── auth-cinema.png       # 登录/注册页图片，建议 700 × 820
└── posters/
    ├── wandering-earth-3.png
    ├── zootopia-2.png
    ├── avengers-final-chapter.png
    └── placeholder-poster.png
```

替换时请保留相同文件名；电影海报建议使用 **3:4 直式比例（例如 600 × 800）**。如果要增加新电影，请在 `src/CineStream/Model/MovieCatalog.java` 添加电影资料、海报 classpath 路径和场次。

> 目前仓库内已经附上可直接运行的原创 placeholder 横幅与海报，因此没有放自己的图片时界面也不会留白。

## 主要代码结构

```text
src/CineStream/
├── MainApp.java              # 最大化窗口、页面切换淡入动画
├── UIComponents.java         # 共用 header、按钮、卡片、hover 动画、图片加载
├── style.css                 # 全站黑紫色设计系统
├── Model/MovieCatalog.java   # 共用电影、影厅与场次资料
└── Page/                     # 所有应用页面
```

## 运行说明

项目使用 JavaFX。请在原本能够运行此 Assignment 的 Eclipse/IntelliJ JavaFX 配置中，重新 build `src` 后运行：

```text
CineStream.MainApp
```

建议使用支持 text blocks 和 `Stream.toList()` 的 **JDK 17 或以上版本**。

## Demo 提示

- 账号必须使用 `@gmail.com` 注册后才能登录。
- 付款页面是 assignment 用的模拟流程，不会进行真实交易或储存付款资料。
- 选择信用卡模式时，可输入任意 16 位 demo 卡号、有效的 `MM/YY` 和 3–4 位 CVV 进行测试。
- 用户与订票记录目前按照原本设计只保存在程序运行期间。
