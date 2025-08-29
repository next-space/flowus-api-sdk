package cn.flowus.demo.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.openapitools.client.ApiClient;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.HttpBearerAuth;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * FlowUs API 配置工具类
 * 
 * 负责从 .env 文件加载配置并初始化 API 客户端
 */
public class ApiConfig {
    
    private static final String DEFAULT_BASE_PATH = "https://api.flowus.cn";
    private static final int DEFAULT_TIMEOUT = 30000; // 30秒
    
    private final Dotenv dotenv;
    private ApiClient apiClient;
    
    // 单例模式
    private static ApiConfig instance;
    
    private ApiConfig() {
        // 尝试加载 .env 文件
        this.dotenv = loadDotenv();
        this.apiClient = initializeApiClient();
    }
    
    /**
     * 获取配置实例
     */
    public static ApiConfig getInstance() {
        if (instance == null) {
            synchronized (ApiConfig.class) {
                if (instance == null) {
                    instance = new ApiConfig();
                }
            }
        }
        return instance;
    }
    
    /**
     * 加载 .env 文件
     */
    private Dotenv loadDotenv() {
        try {
            // 首先尝试从当前目录加载 .env
            File envFile = new File(".env");
            if (envFile.exists()) {
                System.out.println("✅ 找到 .env 文件: " + envFile.getAbsolutePath());
                return Dotenv.configure()
                    .directory(".")
                    .ignoreIfMissing()
                    .load();
            }
            
            // 如果当前目录没有，尝试从项目根目录加载
            envFile = new File("java-demo/.env");
            if (envFile.exists()) {
                System.out.println("✅ 找到 .env 文件: " + envFile.getAbsolutePath());
                return Dotenv.configure()
                    .directory("java-demo")
                    .ignoreIfMissing()
                    .load();
            }
            
            // 如果都没有找到，尝试从系统环境变量加载
            System.out.println("⚠️  未找到 .env 文件，将使用系统环境变量");
            return Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();
                
        } catch (Exception e) {
            System.err.println("⚠️  加载 .env 文件时发生错误: " + e.getMessage());
            System.err.println("将使用默认配置");
            return Dotenv.configure()
                .ignoreIfMissing()
                .load();
        }
    }
    
    /**
     * 初始化 API 客户端
     */
    private ApiClient initializeApiClient() {
        ApiClient client = Configuration.getDefaultApiClient();
        
        // 设置基础路径
        String basePath = getBasePath();
        client.setBasePath(basePath);
        System.out.println("🌐 API 基础路径: " + basePath);
        
        // 设置认证
        String bearerToken = getBearerToken();
        if (bearerToken != null && !bearerToken.isEmpty() && !bearerToken.equals("your-api-token-here")) {
            HttpBearerAuth bearerAuth = (HttpBearerAuth) client.getAuthentication("bearerAuth");
            bearerAuth.setBearerToken(bearerToken);
            System.out.println("🔐 Bearer Token 已配置 (长度: " + bearerToken.length() + ")");
        } else {
            System.err.println("⚠️  警告: Bearer Token 未配置或使用默认值，API 调用可能失败");
            System.err.println("请在 .env 文件中设置 FLOWUS_BEARER_TOKEN");
        }
        
        // 设置超时时间
        client.setConnectTimeout(getConnectTimeout());
        client.setReadTimeout(getReadTimeout());
        client.setWriteTimeout(getWriteTimeout());
        
        System.out.println("⏱️  超时配置 - 连接: " + getConnectTimeout() + "ms, 读取: " + 
            getReadTimeout() + "ms, 写入: " + getWriteTimeout() + "ms");
        
        return client;
    }
    
    /**
     * 获取配置的 API 客户端
     */
    public ApiClient getApiClient() {
        return apiClient;
    }
    
    /**
     * 获取基础路径
     */
    public String getBasePath() {
        return dotenv.get("FLOWUS_BASE_PATH", DEFAULT_BASE_PATH);
    }
    
    /**
     * 获取 Bearer Token
     */
    public String getBearerToken() {
        return dotenv.get("FLOWUS_BEARER_TOKEN", "your-api-token-here");
    }
    
    /**
     * 获取连接超时时间
     */
    public int getConnectTimeout() {
        String timeout = dotenv.get("FLOWUS_CONNECT_TIMEOUT", String.valueOf(DEFAULT_TIMEOUT));
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            System.err.println("⚠️  连接超时配置无效，使用默认值: " + DEFAULT_TIMEOUT + "ms");
            return DEFAULT_TIMEOUT;
        }
    }
    
    /**
     * 获取读取超时时间
     */
    public int getReadTimeout() {
        String timeout = dotenv.get("FLOWUS_READ_TIMEOUT", String.valueOf(DEFAULT_TIMEOUT));
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            System.err.println("⚠️  读取超时配置无效，使用默认值: " + DEFAULT_TIMEOUT + "ms");
            return DEFAULT_TIMEOUT;
        }
    }
    
    /**
     * 获取写入超时时间
     */
    public int getWriteTimeout() {
        String timeout = dotenv.get("FLOWUS_WRITE_TIMEOUT", String.valueOf(DEFAULT_TIMEOUT));
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            System.err.println("⚠️  写入超时配置无效，使用默认值: " + DEFAULT_TIMEOUT + "ms");
            return DEFAULT_TIMEOUT;
        }
    }
    
    /**
     * 验证配置是否有效
     */
    public boolean isConfigValid() {
        String token = getBearerToken();
        return token != null && !token.isEmpty() && !token.equals("your-api-token-here");
    }
    
    /**
     * 打印配置信息
     */
    public void printConfig() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 FlowUs API 配置信息");
        System.out.println("=".repeat(60));
        System.out.println("基础路径: " + getBasePath());
        System.out.println("Token配置: " + (isConfigValid() ? "✅ 已配置" : "❌ 未配置"));
        System.out.println("连接超时: " + getConnectTimeout() + "ms");
        System.out.println("读取超时: " + getReadTimeout() + "ms");
        System.out.println("写入超时: " + getWriteTimeout() + "ms");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * 重新加载配置
     */
    public void reload() {
        System.out.println("🔄 重新加载配置...");
        this.apiClient = initializeApiClient();
    }
}
