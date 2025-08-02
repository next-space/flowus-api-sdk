package cn.flowus.demo;

import org.openapitools.client.ApiClient;
import org.openapitools.client.Configuration;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.auth.HttpBearerAuth;
import org.openapitools.client.model.DeleteBlockResponse;

import java.util.UUID;

public class DeleteBlockDemo {
    public static void main(String[] args) throws Exception {
        // 配置API客户端
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:7001");

        // 配置Bearer认证
        HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
        bearerAuth.setBearerToken("your-api-token-here"); // 请替换为您的实际API token

        DefaultApi apiInstance = new DefaultApi(defaultClient);
        DeleteBlockResponse result = apiInstance.deleteBlock(UUID.fromString("6e9c8635-9ea0-4293-8f8c-dc5fbaa14b0a"));
        System.out.println(result);
    }
}
