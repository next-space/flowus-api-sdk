# FlowUs API Java Demo

这个项目演示了如何使用FlowUs API的Java SDK进行各种操作。

## 项目结构

```
demo/
├── BlockAddChildrenDemo.java    # 向块添加子块演示
├── CreateDatabaseDemo.java      # 创建数据库演示
├── CreateDatabaseRecordDemo.java # 创建数据库记录演示
├── DeleteBlockDemo.java         # 删除块演示
├── SimpleBlockDemo.java         # 简化的块操作演示
├── UserMeDemo.java              # 获取用户信息演示
├── V1SearchDemo.java            # 搜索页面演示
├── pom.xml                     # Maven项目配置
└── README.md                   # 项目说明
```

## 功能演示

### 数据库操作
- **CreateDatabaseDemo.java**: 创建数据库，包含各种属性类型（标题、选择、多选、日期、数字等）
- **CreateDatabaseRecordDemo.java**: 在数据库中创建页面记录

### 块操作
- **BlockAddChildrenDemo.java**: 向指定块添加子块（段落、待办事项、标题等）
- **DeleteBlockDemo.java**: 删除指定的块
- **SimpleBlockDemo.java**: 简化的块操作演示，专注于向指定块添加段落子块

### 用户和搜索
- **UserMeDemo.java**: 获取机器人创建者的用户信息
- **V1SearchDemo.java**: 在授权范围内搜索页面，支持关键词搜索和分页

## 环境要求

- Java 8 或更高版本
- Maven 3.6 或更高版本
- FlowUs API Token

## 快速开始

### 1. 构建FlowUs SDK

首先需要构建FlowUs API SDK：

```bash
cd ../sdk/java-compatible
mvn clean install -DskipTests
```

### 2. 配置环境变量

复制环境变量模板文件并配置您的API信息：

```bash
# 复制配置模板
cp .env.example .env

# 编辑 .env 文件，填入您的实际配置
# FLOWUS_BASE_PATH=https://api.flowus.cn
# FLOWUS_BEARER_TOKEN=your-actual-api-token-here
```

配置文件说明：
- `FLOWUS_BASE_PATH`: FlowUs API的基础路径（默认: https://api.flowus.cn）
- `FLOWUS_BEARER_TOKEN`: 您的FlowUs API Token（必填）
- `FLOWUS_CONNECT_TIMEOUT`: 连接超时时间，毫秒（默认: 30000）
- `FLOWUS_READ_TIMEOUT`: 读取超时时间，毫秒（默认: 30000）
- `FLOWUS_WRITE_TIMEOUT`: 写入超时时间，毫秒（默认: 30000）

### 3. 配置ID

根据您的实际情况，替换代码中的占位符ID：
- `your-page-id-here` - 页面ID
- `your-database-id-here` - 数据库ID  
- `your-block-id-here` - 块ID

### 4. 编译和运行

```bash
# 编译项目
mvn clean compile

# 运行用户信息演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.UserMeDemo"

# 运行搜索演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.V1SearchDemo"

# 运行数据库创建演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.CreateDatabaseDemo"

# 运行数据库记录创建演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.CreateDatabaseRecordDemo"

# 运行块操作演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.BlockAddChildrenDemo"

# 运行删除块演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.DeleteBlockDemo"

# 运行简化块演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.SimpleBlockDemo"

# 或者构建可执行JAR后运行
mvn clean package
java -jar target/flowus-api-demo-1.0.0-shaded.jar
```

## API 使用示例

### 获取用户信息

```java
// 使用配置类初始化API客户端
ApiConfig config = ApiConfig.getInstance();
ApiClient apiClient = config.getApiClient();
DefaultApi apiInstance = new DefaultApi(apiClient);

// 调用获取用户信息API
UserMe result = apiInstance.getMe();
System.out.println("用户ID: " + result.getId());
System.out.println("用户名: " + result.getName());
System.out.println("邮箱: " + result.getPerson().getEmail());
```

### 搜索页面

```java
// 使用配置类初始化API客户端
ApiConfig config = ApiConfig.getInstance();
ApiClient apiClient = config.getApiClient();
DefaultApi apiInstance = new DefaultApi(apiClient);

// 创建搜索请求
V1SearchRequest searchRequest = new V1SearchRequest();
searchRequest.setQuery("项目计划");    // 搜索关键词
searchRequest.setPageSize(10);        // 每页结果数
searchRequest.setStartCursor(null);   // 分页游标（首页为null）

// 调用搜索API
V1SearchResponse result = apiInstance.v1Search(searchRequest);

// 处理搜索结果
for (V1SearchPageResult page : result.getResults()) {
    System.out.println("页面ID: " + page.getId());
    System.out.println("创建时间: " + page.getCreatedTime());
    
    // 获取页面标题
    if (page.getProperties() != null && page.getProperties().getTitle() != null) {
        for (RichTextItem titleItem : page.getProperties().getTitle().getTitle()) {
            System.out.println("标题: " + titleItem.getText().getContent());
        }
    }
}

// 检查是否有更多结果
if (result.getHasMore()) {
    String nextCursor = result.getNextCursor();
    // 使用 nextCursor 获取下一页
}
```

### 创建段落块

```java
// 创建段落块
AppendBlockChildrenRequestChildrenInner child = new AppendBlockChildrenRequestChildrenInner();
child.setType(AppendBlockChildrenRequestChildrenInner.TypeEnum.PARAGRAPH);

// 创建块数据
BlockData blockData = new BlockData();

// 创建富文本内容
List<RichTextItem> richTextItems = new ArrayList<>();
RichTextItem richTextItem = new RichTextItem();
richTextItem.setType(RichTextItem.TypeEnum.TEXT);

// 设置文本内容
RichTextItemText textContent = new RichTextItemText();
textContent.setContent("这是一个新添加的段落块");
richTextItem.setText(textContent);

// 设置文本格式
RichTextItemAnnotations annotations = new RichTextItemAnnotations();
annotations.setBold(true);
annotations.setColor(RichTextItemAnnotations.ColorEnum.BLUE);
richTextItem.setAnnotations(annotations);

richTextItems.add(richTextItem);
blockData.setRichText(richTextItems);
child.setData(blockData);
```

### 调用API

```java
// 初始化API客户端
ApiClient defaultClient = Configuration.getDefaultApiClient();
defaultClient.setBasePath("https://api.flowus.cn");

// 配置认证
HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
bearerAuth.setBearerToken("your-token");

DefaultApi apiInstance = new DefaultApi(defaultClient);

// 调用API
GetBlockChildren200Response result = apiInstance.appendBlockChildren(blockId, request);
```

## 常见问题

### 1. 编译错误

如果遇到编译错误，请确保：
- 已正确构建FlowUs SDK
- Maven版本兼容
- Java版本为8或更高

### 2. 运行时错误

如果遇到运行时错误，请检查：
- Token是否正确配置
- ID是否为有效的UUID格式
- 网络连接是否正常

### 3. API调用失败

常见的API调用失败原因：
- Token无效或过期
- 权限不足
- 请求参数错误
- 目标资源不存在

### 4. 配置文件问题

如果遇到配置相关的问题：

**未找到 .env 文件：**
```
⚠️ 未找到 .env 文件，将使用系统环境变量
```
解决方案：
```bash
cp .env.example .env
# 然后编辑 .env 文件填入您的配置
```

**Token 未配置：**
```
⚠️ 警告: Bearer Token 未配置或使用默认值，API 调用可能失败
```
解决方案：在 .env 文件中设置正确的 `FLOWUS_BEARER_TOKEN`

**配置验证失败：**
```
❌ 配置无效，请检查 .env 文件中的 FLOWUS_BEARER_TOKEN 设置
```
解决方案：检查 Token 是否正确，不应为 `your-api-token-here`

## 许可证

本项目仅用于演示目的，请遵循FlowUs的服务条款。 