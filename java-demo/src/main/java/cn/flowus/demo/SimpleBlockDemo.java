package cn.flowus.demo;

import cn.flowus.demo.config.ApiConfig;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.auth.*;
import org.openapitools.client.model.*;
import org.openapitools.client.api.DefaultApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 简化的FlowUs API演示 - 添加块内容
 * 基于用户提供的示例代码
 */
public class SimpleBlockDemo {
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
        
        // 替换为实际的块ID
        UUID blockId = UUID.fromString("101fe0c3-cbbd-4a66-91d0-26fc63d2229e");
        
        AppendBlockChildrenRequest appendBlockChildrenRequest = new AppendBlockChildrenRequest();
        
        try {
            List<AppendBlockChildrenRequestChildrenInner> children = new ArrayList<>();
            
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
            textContent.setContent("这是一个新添加的段落块from Java API");
            textContent.setLink(null);
            richTextItem.setText(textContent);
            
            // 设置文本注释（格式）
            RichTextItemAnnotations annotations = new RichTextItemAnnotations();
            annotations.setBold(false);
            annotations.setItalic(false);
            annotations.setStrikethrough(false);
            annotations.setUnderline(false);
            annotations.setCode(false);
            annotations.setColor(RichTextItemAnnotations.ColorEnum.DEFAULT);
            richTextItem.setAnnotations(annotations);
            
            richTextItems.add(richTextItem);
            
            // 设置块数据
            blockData.setRichText(richTextItems);
            blockData.setTextColor(BlockData.TextColorEnum.BLUE);
            blockData.setBackgroundColor(BlockData.BackgroundColorEnum.DEFAULT);
            
            child.setData(blockData);
            children.add(child);
            
            appendBlockChildrenRequest.setChildren(children);
            
            // 调用API添加子块
            AppendBlockChildrenResponse result = apiInstance.appendBlockChildren(blockId, appendBlockChildrenRequest);
            
            System.out.println("✅ 成功添加子块！");
            System.out.println("结果: " + result);
            System.out.println("添加的块数量: " + result.getResults().size());
            
            // 打印新添加的块信息
            for (Block block : result.getResults()) {
                System.out.println("新块ID: " + block.getId());
                System.out.println("块类型: " + block.getType());
                System.out.println("创建时间: " + block.getCreatedTime());
            }
            
        } catch (ApiException e) {
            System.err.println("❌ 调用API失败");
            System.err.println("状态码: " + e.getCode());
            System.err.println("错误信息: " + e.getResponseBody());
            System.err.println("响应头: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
} 