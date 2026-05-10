# 物业反馈与通知系统

一个前后端分离的物业服务管理系统，支持用户反馈、物业通知发布和管理员后台管理。

## 功能特性

### 用户端（无需登录）
- 📝 提交意见反馈
- 📋 查看反馈列表和处理状态
- 🔔 查看物业通知公告

### 管理员端（需登录）
- 📊 管理和处理用户反馈
- ✏️ 发布、编辑、删除通知
- 🔐 安全的登录认证

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 2 + Vue Router + Element UI + Axios |
| 后端 | Spring Boot 3 + MyBatis + Spring Security |
| 数据库 | MySQL 8.0 |
| 容器化 | Docker + Docker Compose |

## 项目结构

```
├── frontend/                # 前端项目
│   ├── public/             # 静态资源
│   ├── src/
│   │   ├── api/           # API接口封装
│   │   ├── router/        # 路由配置
│   │   ├── views/         # 页面组件
│   │   │   ├── admin/     # 管理员页面
│   │   │   ├── Home.vue
│   │   │   ├── Feedback.vue
│   │   │   └── Notices.vue
│   │   ├── App.vue
│   │   └── main.js
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
├── backend/                 # 后端项目
│   ├── src/main/java/com/property/
│   │   ├── config/        # 配置类
│   │   ├── controller/    # 控制器
│   │   ├── dto/           # 数据传输对象
│   │   ├── entity/        # 实体类
│   │   ├── mapper/        # MyBatis Mapper接口
│   │   ├── service/       # 业务逻辑层
│   │   └── PropertyApplication.java
│   ├── src/main/resources/
│   │   ├── mapper/        # MyBatis XML映射文件
│   │   └── application.yml
│   ├── Dockerfile
│   └── pom.xml
│
├── mysql/
│   └── init.sql            # 数据库初始化脚本
│
├── docker-compose.yml      # Docker编排配置
└── README.md
```

## 快速开始

### 环境要求

- Docker 20.0+
- Docker Compose 2.0+

### 一键启动

```bash
# 克隆项目后进入项目目录
cd taskId570

# 启动所有服务
docker compose up

# 查看服务状态
docker compose ps

# 查看日志
docker compose logs -f
```

### 访问地址

| 服务 | 地址 |
|------|------|
| 前端页面 | http://localhost:3000 |
| 后端API | http://localhost:8000 |
| MySQL | localhost:3306 |

### 默认账号

- **管理员账号**: admin
- **管理员密码**: admin123

## Docker 使用说明

### 常用命令

```bash
# 启动服务（后台运行）
docker compose up -d

# 启动服务（前台运行，可查看日志）
docker compose up

# 停止服务
docker compose down

# 停止并删除数据卷
docker compose down -v

# 重新构建镜像
docker compose build

# 重新构建并启动
docker compose up -d --build

# 查看服务状态
docker compose ps

# 查看服务日志
docker compose logs -f [service_name]

# 进入容器
docker compose exec backend sh
docker compose exec frontend sh
docker compose exec mysql mysql -uroot -p123456
```

### 服务说明

| 服务名 | 容器名 | 端口映射 | 说明 |
|--------|--------|----------|------|
| mysql | property-mysql | 3306:3306 | MySQL数据库 |
| backend | property-backend | 8000:8000 | Spring Boot后端 |
| frontend | property-frontend | 3000:80 | Vue前端(Nginx) |

## API 接口文档

### 反馈接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/feedbacks | 获取所有反馈 |
| GET | /api/feedbacks/{id} | 获取单个反馈 |
| POST | /api/feedbacks | 提交反馈 |
| PUT | /api/feedbacks/{id} | 处理反馈 |
| DELETE | /api/feedbacks/{id} | 删除反馈 |

### 通知接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/notices | 获取所有通知 |
| GET | /api/notices/{id} | 获取单个通知 |
| POST | /api/notices | 发布通知 |
| PUT | /api/notices/{id} | 更新通知 |
| DELETE | /api/notices/{id} | 删除通知 |

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/login | 管理员登录 |
| POST | /api/auth/logout | 退出登录 |
| GET | /api/auth/check | 检查登录状态 |

## 数据库配置

- **数据库名**: property_db
- **用户名**: root
- **密码**: 123456
- **字符集**: utf8mb4

### 数据表

- `admin` - 管理员表
- `feedback` - 反馈表
- `notice` - 通知表

## 开发说明

### 修改配置

- 后端配置: `backend/src/main/resources/application.yml`
- 前端配置: `frontend/vue.config.js`
- Docker配置: `docker-compose.yml`

## 注意事项

1. 首次启动时，MySQL需要初始化数据，可能需要等待30秒左右
2. 如遇到端口冲突，请修改 `docker-compose.yml` 中的端口映射
3. 数据持久化存储在Docker卷 `mysql_data` 中
4. 如需清空数据，请执行 `docker compose down -v`

## 许可证

MIT License
