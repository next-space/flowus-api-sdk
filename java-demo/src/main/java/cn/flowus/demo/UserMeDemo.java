package cn.flowus.demo;

import cn.flowus.demo.config.ApiConfig;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.UserMe;

/**
 * 获取机器人创建者信息演示
 * 
 * 此演示展示如何使用 /v1/users/me API 获取当前机器人的创建者用户信息
 */
public class UserMeDemo {
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

        try {
            System.out.println("正在获取机器人创建者信息...");
            
            // 调用获取用户信息API
            UserMe result = apiInstance.getMe();
            
            // 输出用户信息
            System.out.println("✅ 成功获取用户信息:");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("对象类型: " + result.getObject());
            System.out.println("用户ID: " + result.getId());
            System.out.println("用户类型: " + result.getType());
            
            if (result.getName() != null) {
                System.out.println("显示名称: " + result.getName());
            } else {
                System.out.println("显示名称: 未设置");
            }
            
            if (result.getAvatarUrl() != null) {
                System.out.println("头像URL: " + result.getAvatarUrl());
            } else {
                System.out.println("头像URL: 未设置");
            }
            
            // 显示个人信息
            if (result.getPerson() != null) {
                System.out.println("个人信息:");
                if (result.getPerson().getEmail() != null) {
                    System.out.println("  邮箱: " + result.getPerson().getEmail());
                } else {
                    System.out.println("  邮箱: 未设置");
                }
            }
            
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            // 输出完整的JSON响应（可选）
            System.out.println("\n完整响应对象:");
            System.out.println(result.toString());
            
        } catch (ApiException e) {
            System.err.println("❌ 调用 /v1/users/me API 时发生错误");
            System.err.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.err.println("状态码: " + e.getCode());
            System.err.println("错误原因: " + e.getResponseBody());
            System.err.println("响应头: " + e.getResponseHeaders());
            
            // 根据状态码提供更具体的错误信息
            switch (e.getCode()) {
                case 401:
                    System.err.println("\n💡 解决建议: 请检查您的API Token是否正确配置");
                    break;
                case 404:
                    System.err.println("\n💡 解决建议: 机器人创建者信息不存在，请联系管理员");
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
}
