/**
 * FlowUs API TypeScript Demo - V1搜索页面演示
 * 演示如何使用 /v1/search 接口在机器人授权的页面范围内搜索相关内容
 */
import { FlowUsClient } from './utils/flowusClient';
import { config, validateConfig } from './config/apiConfig';

async function v1SearchDemo() {
  console.log('🔍 FlowUs API TypeScript Demo - V1搜索页面演示');
  console.log('================================================');
  
  try {
    // 验证配置
    validateConfig();
    
    // 创建API客户端
    const client = new FlowUsClient({
      baseURL: config.baseURL,
      apiToken: config.apiToken,
      timeout: config.timeout
    });
    
    // 演示1: 基本关键词搜索
    console.log('📡 演示1: 基本关键词搜索');
    console.log('搜索关键词: "项目计划"');
    console.log('================================================');
    
    const searchResult1 = await client.v1Search({
      query: "项目计划",
      page_size: 5
    });
    
    console.log('✅ 搜索结果:');
    console.log(`找到 ${searchResult1.results.length} 个结果`);
    console.log(`是否有更多结果: ${searchResult1.has_more}`);
    
    if (searchResult1.results.length > 0) {
      searchResult1.results.forEach((result: any, index: number) => {
        console.log(`\n📄 结果 ${index + 1}:`);
        console.log(`  🆔 页面ID: ${result.id}`);
        console.log(`  📅 创建时间: ${result.created_time}`);
        console.log(`  📅 最后编辑时间: ${result.last_edited_time}`);
        console.log(`  📚 是否归档: ${result.archived}`);
        
        if (result.parent) {
          console.log(`  📁 父级类型: ${result.parent.type}`);
          if (result.parent.database_id) {
            console.log(`  📊 数据库ID: ${result.parent.database_id}`);
          }
          if (result.parent.page_id) {
            console.log(`  📄 父页面ID: ${result.parent.page_id}`);
          }
        }
        
        if (result.properties?.title?.title?.[0]?.text?.content) {
          console.log(`  📝 标题: ${result.properties.title.title[0].text.content}`);
        }
      });
    }
    
    // 演示2: 分页搜索
    console.log('\n\n📡 演示2: 分页搜索');
    console.log('搜索关键词: "会议"，页面大小: 3');
    console.log('================================================');
    
    const searchResult2 = await client.v1Search({
      query: "会议",
      page_size: 3
    });
    
    console.log('✅ 第一页搜索结果:');
    console.log(`找到 ${searchResult2.results.length} 个结果`);
    console.log(`下一页游标: ${searchResult2.next_cursor || '无'}`);
    console.log(`是否有更多结果: ${searchResult2.has_more}`);
    
    // 如果有下一页，获取第二页
    if (searchResult2.has_more && searchResult2.next_cursor) {
      console.log('\n📡 正在获取第二页结果...');
      
      const searchResult3 = await client.v1Search({
        query: "会议",
        page_size: 3,
        start_cursor: searchResult2.next_cursor
      });
      
      console.log('✅ 第二页搜索结果:');
      console.log(`找到 ${searchResult3.results.length} 个结果`);
      console.log(`是否有更多结果: ${searchResult3.has_more}`);
    }
    
    // 演示3: 列出所有授权页面
    console.log('\n\n📡 演示3: 列出所有授权页面');
    console.log('使用空查询参数列出所有可访问的页面');
    console.log('================================================');
    
    const searchResult4 = await client.v1Search({
      query: "",
      page_size: 10
    });
    
    console.log('✅ 所有授权页面:');
    console.log(`总共找到 ${searchResult4.results.length} 个页面`);
    console.log(`是否有更多结果: ${searchResult4.has_more}`);
    
    if (searchResult4.results.length > 0) {
      console.log('\n📝 页面列表:');
      searchResult4.results.forEach((result: any, index: number) => {
        const title = result.properties?.title?.title?.[0]?.text?.content || '无标题';
        console.log(`  ${index + 1}. ${title} (ID: ${result.id})`);
      });
    }
    
    console.log('\n📄 完整响应数据（最后一次搜索）:');
    console.log(JSON.stringify(searchResult4, null, 2));
    
    console.log('\n🎉 V1搜索页面演示完成！');
    
    return {
      keywordSearch: searchResult1,
      paginatedSearch: searchResult2,
      allPages: searchResult4
    };
    
  } catch (error: any) {
    console.error('❌ V1搜索页面演示失败:', error.message);
    if (error.response?.data) {
      console.error('详细错误信息:', JSON.stringify(error.response.data, null, 2));
    }
    
    // 根据错误类型提供具体的解决建议
    if (error.message.includes('400')) {
      console.error('💡 解决建议: 请检查搜索参数是否正确，page_size应在1-100之间');
    } else if (error.message.includes('401')) {
      console.error('💡 解决建议: 请检查您的API Token是否正确，或者Token是否已过期');
    } else if (error.message.includes('403')) {
      console.error('💡 解决建议: 请检查您的API Token权限，确保有搜索页面的权限');
    } else if (error.message.includes('429')) {
      console.error('💡 解决建议: 请求频率过高，请稍后重试');
    } else if (error.message.includes('网络连接失败')) {
      console.error('💡 解决建议: 请检查网络连接和API服务器地址配置');
    }
    
    process.exit(1);
  }
}

// 如果直接运行此文件，则执行演示
if (require.main === module) {
  v1SearchDemo();
}

export default v1SearchDemo;
