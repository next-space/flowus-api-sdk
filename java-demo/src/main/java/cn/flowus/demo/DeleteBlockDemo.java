package cn.flowus.demo;

import cn.flowus.demo.config.ApiConfig;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.DeleteBlockResponse;

import java.util.UUID;

public class DeleteBlockDemo {
    public static void main(String[] args) throws Exception {
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
        DeleteBlockResponse result = apiInstance.deleteBlock(UUID.fromString("6e9c8635-9ea0-4293-8f8c-dc5fbaa14b0a"));
        System.out.println(result);
    }
}
