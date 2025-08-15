# FlowUs API TypeScript SDK 演示程序

这是 FlowUs API 的 TypeScript SDK 演示程序，展示了如何使用 TypeScript 调用 FlowUs API 进行各种操作。

## 准备工作

1. 确保已安装 Node.js (v14+) 和 npm
2. 克隆本仓库
3. 安装依赖：

```bash
cd typescript-demo
npm install
```

4. 复制 `.env.example` 为 `.env` 并填入您的 API Token 和其他配置：

```bash
cp .env.example .env
```

## 配置说明

在 `.env` 文件中设置以下配置：

```
# FlowUs API 配置
FLOWUS_API_BASE_URL=https://api.flowus.cn
FLOWUS_API_TOKEN=您的实际token
```

## 运行演示

首先构建项目：

```bash
npm run build
```

然后运行各个演示：

```bash
# 创建数据库演示
npm run demo:create-database

# 添加块子元素演示
npm run demo:add-children

# 创建数据库记录演示
npm run demo:create-record

# 删除块演示
npm run demo:delete-block

# 运行所有演示
npm run demo:all
```

## 演示内容

本演示程序包含以下示例：

1. **创建数据库**：演示如何创建一个包含多种属性类型的数据库
2. **添加块子元素**：演示如何向页面添加各种类型的块元素
3. **创建数据库记录**：演示如何在数据库中创建记录并设置各种类型的属性值
4. **删除块**：演示如何删除指定的块

## 项目结构

```
typescript-demo/
├── dist/               # 编译后的JavaScript文件
├── src/                # 源代码
│   ├── config/         # 配置文件
│   ├── utils/          # 工具类和辅助函数
│   ├── CreateDatabaseDemo.ts        # 创建数据库演示
│   ├── BlockAddChildrenDemo.ts      # 添加块子元素演示
│   ├── CreateDatabaseRecordDemo.ts  # 创建数据库记录演示
│   └── DeleteBlockDemo.ts           # 删除块演示
├── .env                # 环境变量配置文件（需自行创建）
├── .env.example        # 环境变量示例文件
├── package.json        # 项目依赖配置
└── tsconfig.json       # TypeScript配置
```

## 更多资源

- [FlowUs API 文档](https://flowus.cn/api-docs)
- [FlowUs 官网](https://flowus.cn)

## 许可证

MIT 