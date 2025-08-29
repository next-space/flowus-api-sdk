package cn.flowus.demo;

import cn.flowus.demo.config.ApiConfig;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;

import java.util.List;

/**
 * 搜索页面演示
 * 
 * 此演示展示如何使用 /v1/search API 在机器人授权的页面范围内搜索相关内容
 */
public class V1SearchDemo {
    public static void main(String[] args) {
        // 加载配置并初始化API客户端
        ApiConfig config = ApiConfig.getInstance();
        config.printConfig();
        
        // 检查配置是否有效
        if (!config.isConfigValid()) {
            System.err.println("❌ 配置无效，请检查 .env 文件中的 FLOWUS_BEARER_TOKEN 设置");
            System.err.println("💡 请复制 .env.example 为 .env 并填入您的实际配置");
            return;
        }
        
        ApiClient apiClient = config.getApiClient();
        DefaultApi apiInstance = new DefaultApi(apiClient);

        // 演示不同的搜索场景
        demonstrateSearch(apiInstance, "项目计划", "搜索包含'项目计划'的页面");
        demonstrateSearch(apiInstance, "会议", "搜索包含'会议'的页面");
        demonstrateSearch(apiInstance, "", "列出所有授权页面（空查询）");
        
        // 演示分页搜索
        demonstratePaginatedSearch(apiInstance);
    }
    
    /**
     * 演示基本搜索功能
     */
    private static void demonstrateSearch(DefaultApi apiInstance, String searchQuery, String description) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🔍 " + description);
        System.out.println("=".repeat(80));
        
        try {
            // 创建搜索请求
            V1SearchRequest searchRequest = new V1SearchRequest();
            searchRequest.setQuery(searchQuery);
            searchRequest.setPageSize(10); // 每页显示10个结果
            
            System.out.println("搜索关键词: \"" + searchQuery + "\"");
            System.out.println("每页结果数: " + searchRequest.getPageSize());
            System.out.println("正在搜索...\n");
            
            // 调用搜索API
            V1SearchResponse result = apiInstance.v1Search(searchRequest);
            
            // 输出搜索结果
            System.out.println("✅ 搜索完成");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("对象类型: " + result.getObject());
            System.out.println("当前页结果数: " + result.getResults().size());
            System.out.println("是否有更多结果: " + result.getHasMore());
            
            if (result.getNextCursor() != null) {
                System.out.println("下一页游标: " + result.getNextCursor());
            } else {
                System.out.println("下一页游标: 无（已到最后一页）");
            }
            
            // 显示每个搜索结果
            List<V1SearchPageResult> results = result.getResults();
            if (results.isEmpty()) {
                System.out.println("\n📝 搜索结果: 没有找到匹配的页面");
            } else {
                System.out.println("\n📝 搜索结果:");
                for (int i = 0; i < results.size(); i++) {
                    V1SearchPageResult page = results.get(i);
                    System.out.println("\n" + (i + 1) + ". 页面信息:");
                    System.out.println("   ID: " + page.getId());
                    System.out.println("   对象类型: " + page.getObject());
                    System.out.println("   创建时间: " + page.getCreatedTime());
                    System.out.println("   最后编辑时间: " + page.getLastEditedTime());
                    System.out.println("   是否归档: " + page.getArchived());
                    
                    // 显示父级信息
                    if (page.getParent() != null) {
                        System.out.println("   父级对象: " + page.getParent().toString());
                    } else {
                        System.out.println("   父级对象: 无");
                    }
                    
                    // 显示页面属性
                    if (page.getProperties() != null && page.getProperties().getTitle() != null) {
                        V1SearchPageResultPropertiesTitle titleProp = page.getProperties().getTitle();
                        System.out.println("   标题类型: " + titleProp.getType());
                        
                        if (titleProp.getTitle() != null && !titleProp.getTitle().isEmpty()) {
                            System.out.print("   标题内容: ");
                            for (RichTextItem titleItem : titleProp.getTitle()) {
                                if (titleItem.getText() != null && titleItem.getText().getContent() != null) {
                                    System.out.print(titleItem.getText().getContent());
                                }
                            }
                            System.out.println();
                        } else {
                            System.out.println("   标题内容: 未设置");
                        }
                    } else {
                        System.out.println("   标题: 未设置");
                    }
                }
            }
            
        } catch (ApiException e) {
            System.err.println("❌ 调用 /v1/search API 时发生错误");
            System.err.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.err.println("状态码: " + e.getCode());
            System.err.println("错误原因: " + e.getResponseBody());
            System.err.println("响应头: " + e.getResponseHeaders());
            
            // 根据状态码提供更具体的错误信息
            switch (e.getCode()) {
                case 400:
                    System.err.println("\n💡 解决建议: 请检查搜索参数是否正确");
                    break;
                case 401:
                    System.err.println("\n💡 解决建议: 请检查您的API Token是否正确配置");
                    break;
                case 403:
                    System.err.println("\n💡 解决建议: 权限不足，请检查Token的权限范围");
                    break;
                case 429:
                    System.err.println("\n💡 解决建议: 请求频率过高，请稍后再试");
                    break;
                case 500:
                    System.err.println("\n💡 解决建议: 服务器内部错误，请稍后再试或联系技术支持");
                    break;
                default:
                    System.err.println("\n💡 解决建议: 请检查网络连接和API服务状态");
                    break;
            }
            
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ 发生未预期的错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 演示分页搜索功能
     */
    private static void demonstratePaginatedSearch(DefaultApi apiInstance) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📄 演示分页搜索功能");
        System.out.println("=".repeat(80));
        
        try {
            String currentCursor = null;
            int pageNumber = 1;
            final int maxPages = 3; // 最多演示3页
            
            while (pageNumber <= maxPages) {
                System.out.println("\n📖 第 " + pageNumber + " 页搜索:");
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                
                // 创建搜索请求
                V1SearchRequest searchRequest = new V1SearchRequest();
                searchRequest.setQuery(""); // 空查询，列出所有页面
                searchRequest.setPageSize(5); // 每页5个结果以便演示分页
                
                if (currentCursor != null) {
                    searchRequest.setStartCursor(currentCursor);
                    System.out.println("使用游标: " + currentCursor);
                }
                
                // 调用搜索API
                V1SearchResponse result = apiInstance.v1Search(searchRequest);
                
                System.out.println("当前页结果数: " + result.getResults().size());
                System.out.println("是否有更多结果: " + result.getHasMore());
                
                // 显示页面标题
                List<V1SearchPageResult> results = result.getResults();
                for (int i = 0; i < results.size(); i++) {
                    V1SearchPageResult page = results.get(i);
                    System.out.print("  " + (i + 1) + ". ");
                    
                    if (page.getProperties() != null && 
                        page.getProperties().getTitle() != null && 
                        page.getProperties().getTitle().getTitle() != null && 
                        !page.getProperties().getTitle().getTitle().isEmpty()) {
                        
                        for (RichTextItem titleItem : page.getProperties().getTitle().getTitle()) {
                            if (titleItem.getText() != null && titleItem.getText().getContent() != null) {
                                System.out.print(titleItem.getText().getContent());
                            }
                        }
                        System.out.println(" (ID: " + page.getId() + ")");
                    } else {
                        System.out.println("无标题 (ID: " + page.getId() + ")");
                    }
                }
                
                // 检查是否有下一页
                if (!result.getHasMore() || result.getNextCursor() == null) {
                    System.out.println("\n✅ 已到达最后一页");
                    break;
                }
                
                currentCursor = result.getNextCursor();
                pageNumber++;
                
                if (pageNumber <= maxPages) {
                    System.out.println("\n⏳ 准备获取下一页...");
                    Thread.sleep(1000); // 稍微延迟以避免请求过快
                }
            }
            
            if (pageNumber > maxPages) {
                System.out.println("\n📝 演示完成（仅显示前" + maxPages + "页）");
            }
            
        } catch (ApiException e) {
            System.err.println("❌ 分页搜索演示时发生错误: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ 分页搜索演示时发生未预期的错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
