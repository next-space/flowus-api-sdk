# FlowUs API Java Demo

这个项目演示了如何使用FlowUs API的Java SDK进行各种操作。

## 项目结构

```
demo/
├── FlowUsApiDemo.java      # 完整的API演示程序
├── SimpleBlockDemo.java    # 简化的块操作演示
├── pom.xml                # Maven项目配置
└── README.md              # 项目说明
```

## 功能演示

### FlowUsApiDemo.java
完整的API功能演示，包括：
- 创建数据库
- 在数据库中创建页面
- 向页面添加各种类型的块（段落、待办事项、标题等）
- 查询数据库
- 获取页面信息
- 获取块的子块

### SimpleBlockDemo.java
基于用户提供示例的简化演示，专注于：
- 向指定块添加段落子块
- 设置富文本格式和颜色

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

### 2. 配置Token

在演示代码中替换`your-token-here`为您的实际FlowUs API Token：

```java
bearerAuth.setBearerToken("您的实际token");
```

### 3. 配置ID

根据您的实际情况，替换代码中的占位符ID：
- `your-page-id-here` - 页面ID
- `your-database-id-here` - 数据库ID  
- `your-block-id-here` - 块ID

### 4. 编译和运行

```bash
# 编译项目
mvn clean compile

# 运行简化演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.SimpleBlockDemo"

# 运行完整演示
mvn exec:java -Dexec.mainClass="cn.flowus.demo.FlowUsApiDemo"

# 或者构建可执行JAR后运行
mvn clean package
java -jar target/flowus-api-demo-1.0.0-shaded.jar
```

## API 使用示例

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

## 许可证

本项目仅用于演示目的，请遵循FlowUs的服务条款。 