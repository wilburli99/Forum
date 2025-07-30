# 论坛系统 (Forum System)

一个基于 Spring Boot 3.x 开发的现代化论坛系统，提供完整的用户管理、帖子发布、版块管理和站内消息功能。

## 📋 项目简介

本项目是一个功能完整的论坛系统，采用前后端分离架构，后端使用 Spring Boot + MyBatis，前端使用原生 HTML + JavaScript + Tabler UI 框架。系统支持用户注册登录、帖子发布与管理、版块分类、回复评论、站内消息等核心功能。

## ✨ 主要功能

### 用户管理
- ✅ 用户注册与登录
- ✅ 密码加密存储（MD5 + 盐值）
- ✅ 用户信息修改（昵称、邮箱、手机号、个人简介等）
- ✅ 密码修改
- ✅ 用户状态管理（正常/禁言）
- ✅ 管理员权限控制

### 版块管理
- ✅ 多版块分类（Java、C++、前端技术、MySQL、面试宝典等）
- ✅ 版块状态管理
- ✅ 版块帖子数量统计
- ✅ 版块排序功能

### 帖子系统
- ✅ 帖子发布与编辑
- ✅ 帖子详情查看
- ✅ 帖子点赞功能
- ✅ 访问量统计
- ✅ 帖子删除（软删除）
- ✅ 按版块/用户筛选帖子

### 回复系统
- ✅ 帖子回复
- ✅ 楼中楼回复
- ✅ 回复点赞
- ✅ 回复管理

### 站内消息
- ✅ 用户间私信
- ✅ 消息状态管理
- ✅ 消息列表查看

### 其他功能
- ✅ 登录状态拦截
- ✅ 全局异常处理
- ✅ Swagger API 文档
- ✅ 响应式前端界面
- ✅ 夜间模式切换

## 🛠 技术栈

### 后端技术
- **框架**: Spring Boot 3.4.6
- **数据库**: MySQL 8.x
- **ORM**: MyBatis 3.0.4
- **连接池**: Druid
- **文档**: SpringDoc OpenAPI 3 (Swagger)
- **工具库**: Lombok, Apache Commons Codec
- **构建工具**: Maven

### 前端技术
- **UI框架**: Tabler UI
- **脚本**: 原生 JavaScript
- **样式**: CSS3 + Bootstrap
- **编辑器**: Editor.md (Markdown编辑器)
- **图标**: Tabler Icons

### 开发工具
- **热部署**: Spring Boot DevTools
- **代码生成**: MyBatis Generator
- **日志**: SLF4J + Logback

## 📁 项目结构

```
Forum/
├── src/
│   ├── main/
│   │   ├── java/cn/iocoder/forum/
│   │   │   ├── common/           # 通用类
│   │   │   ├── config/           # 配置类
│   │   │   ├── controller/       # 控制器层
│   │   │   ├── dao/              # 数据访问层
│   │   │   ├── exception/        # 异常处理
│   │   │   ├── interceptor/      # 拦截器
│   │   │   ├── model/            # 实体类
│   │   │   ├── services/         # 业务逻辑层
│   │   │   └── utils/            # 工具类
│   │   └── resources/
│   │       ├── mapper/           # MyBatis映射文件
│   │       ├── static/           # 静态资源
│   │       └── application.yml   # 配置文件
│   ├── forum_db.sql             # 数据库脚本
│   └── test/                    # 测试代码
├── target/                      # 编译输出
├── pom.xml                      # Maven配置
└── README.md                    # 项目说明
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <项目地址>
   cd Forum
   ```

2. **创建数据库**
   ```sql
   # 执行 src/forum_db.sql 文件
   mysql -u root -p < src/forum_db.sql
   ```

3. **修改配置**
   编辑 `src/main/resources/application.yml`，修改数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/forum_db?allowPublicKeyRetrieval=true&useSSL=false
       username: your_username
       password: your_password
   ```

4. **启动项目**
   ```bash
   # 使用Maven启动
   mvn spring-boot:run
   
   # 或者编译后启动
   mvn clean package
   java -jar target/Forum-0.0.1-SNAPSHOT.jar
   ```

## 📊 数据库设计

系统包含5个核心数据表：

- **t_user**: 用户表，存储用户基本信息
- **t_board**: 版块表，存储论坛版块信息
- **t_article**: 帖子表，存储帖子内容
- **t_article_reply**: 帖子回复表，支持楼中楼
- **t_message**: 站内信表，用户私信功能

## 🔧 API 接口

### 用户相关
- `POST /user/register` - 用户注册
- `POST /user/login` - 用户登录
- `GET /user/logout` - 用户登出
- `GET /user/info` - 获取用户信息
- `POST /user/modifyInfo` - 修改用户信息
- `POST /user/modifyPwd` - 修改密码

### 版块相关
- `GET /board/topList` - 获取版块列表
- `GET /board/getById` - 根据ID获取版块信息

### 帖子相关
- `POST /article/create` - 创建帖子
- `GET /article/getAllByBoardId` - 按版块获取帖子列表
- `GET /article/getAllByUserId` - 按用户获取帖子列表
- `GET /article/details` - 获取帖子详情
- `POST /article/modify` - 修改帖子
- `POST /article/thumbsUp` - 帖子点赞
- `POST /article/delete` - 删除帖子

## 🎨 界面预览

系统提供了现代化的响应式界面：

- 🏠 **首页**: 展示最新帖子和热门版块
- 👤 **用户中心**: 个人信息管理和帖子管理
- 📝 **发帖页面**: 支持 Markdown 编辑
- 💬 **帖子详情**: 完整的帖子内容和回复系统
- 📱 **响应式设计**: 支持移动端访问

## 🔒 安全特性

- 密码加密存储（MD5 + 随机盐值）
- 登录状态拦截器
- XSS 防护
- SQL 注入防护
- 用户权限控制
- 禁言功能

## 📈 性能优化

- 数据库连接池 (Druid)
- 静态资源缓存
- 分页查询
- 索引优化
- 懒加载


⭐ 如果这个项目对您有帮助，请给我一个 Star！ 