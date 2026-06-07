# 学成在线（xuecheng-plus）

基于 Spring Cloud 微服务架构的在线教育平台，涵盖课程管理、媒资管理、在线学习、订单支付等核心业务。

## 技术栈

| 分类 | 技术 |
|------|------|
| 基础框架 | Spring Boot 2.3.7、Spring Cloud Hoxton.SR9、Spring Cloud Alibaba 2.2.6 |
| 注册/配置中心 | Nacos |
| 网关 | Spring Cloud Gateway |
| 认证授权 | Spring Security + OAuth2 |
| 数据库 | MySQL 8.0、MyBatis-Plus 3.4.1、Druid |
| 缓存 | Redis、Redisson |
| 消息队列 | RabbitMQ |
| 搜索引擎 | Elasticsearch 7.12.1 |
| 对象存储 | MinIO |
| 视频处理 | FFmpeg |
| 任务调度 | XXL-JOB 2.3.1 |
| 远程调用 | OpenFeign |
| 支付集成 | 支付宝 SDK |
| API 文档 | Swagger |
| 工具库 | Lombok、MapStruct、Fastjson、OkHttp、Guava |

## 项目结构

```
xuecheng-plus-project
├── xuecheng-plus-parent/          # 父工程，统一依赖管理
├── xuecheng-plus-base/            # 公共基础模块（工具类、MinIO 配置等）
├── xuecheng-plus-gateway/         # 网关服务（路由、统一鉴权）
├── xuecheng-plus-auth/            # 认证服务（OAuth2 授权服务器）
├── xuecheng-plus-content/         # 课程内容管理
│   ├── xuecheng-plus-content-model/
│   ├── xuecheng-plus-content-api/
│   └── xuecheng-plus-content-service/
├── xuecheng-plus-media/           # 媒资管理（视频上传、转码）
│   ├── xuecheng-plus-media-model/
│   ├── xuecheng-plus-media-api/
│   └── xuecheng-plus-media-service/
├── xuecheng-plus-search/          # 搜索服务（Elasticsearch）
├── xuecheng-plus-learning/        # 在线学习服务
│   ├── xuecheng-plus-learning-model/
│   ├── xuecheng-plus-learning-api/
│   └── xuecheng-plus-learning-service/
├── xuecheng-plus-orders/          # 订单支付服务
│   ├── xuecheng-plus-orders-model/
│   ├── xuecheng-plus-orders-api/
│   └── xuecheng-plus-orders-service/
├── xuecheng-plus-system/          # 系统管理服务
│   ├── xuecheng-plus-system-model/
│   ├── xuecheng-plus-system-api/
│   └── xuecheng-plus-system-service/
├── xuecheng-plus-checkcode/       # 验证码服务
├── xuecheng-plus-message-sdk/     # 消息通知 SDK
└── xuecheng-plus-generator/       # 代码生成器
```

每个业务模块按 **model / api / service** 三层划分：
- **model**：实体类、DTO
- **api**：Controller、对外接口
- **service**：业务逻辑实现

## 核心功能

- **课程管理**：课程 CRUD、审核发布、静态页面生成、ES 索引更新
- **媒资管理**：视频上传（分片）、FFmpeg 转码、MinIO 存储
- **在线学习**：课程学习、学习记录、视频播放凭证
- **订单支付**：支付宝支付对接、支付结果异步通知（RabbitMQ）
- **认证授权**：OAuth2 授权码模式、JWT Token、网关统一鉴权
- **搜索服务**：Elasticsearch 全文检索课程信息
- **分布式锁**：Redisson 解决课程详情缓存击穿问题

## 环境要求

| 环境 | 版本 |
|------|------|
| JDK | 1.8+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |
| Redis | 5.0+ |
| Nacos | 2.x |
| RabbitMQ | 3.8+ |
| Elasticsearch | 7.12+ |
| MinIO | 最新版 |
| XXL-JOB | 2.3.1 |

## 快速启动

```bash
# 1. 克隆项目
git clone https://github.com/wangyue377/xuecheng-plus-project.git
cd xuecheng-plus-project

# 2. 编译打包
mvn clean install -DskipTests

# 3. 启动服务（按顺序）
# 3.1 启动 Nacos、MySQL、Redis、RabbitMQ、Elasticsearch、MinIO
# 3.2 启动网关
# 3.3 启动认证服务
# 3.4 启动各业务服务
```
