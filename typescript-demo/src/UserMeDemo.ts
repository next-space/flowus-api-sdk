/**
 * FlowUs API TypeScript Demo - 获取机器人创建者信息演示
 * 演示如何使用 /v1/users/me 接口获取当前机器人的创建者用户信息
 */
import { FlowUsClient } from './utils/flowusClient';
import { config, validateConfig } from './config/apiConfig';

async function userMeDemo() {
  console.log('👤 FlowUs API TypeScript Demo - 获取机器人创建者信息演示');
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
    
    console.log('📡 正在获取机器人创建者信息...');
    
    // 调用 /v1/users/me 接口获取用户信息
    const userInfo = await client.getUserMe();
    
    console.log('✅ 成功获取机器人创建者信息:');
    console.log('================================================');
    console.log(`🆔 用户ID: ${userInfo.id}`);
    console.log(`👤 用户类型: ${userInfo.type}`);
    console.log(`📛 显示名称: ${userInfo.name || '未设置'}`);
    
    if (userInfo.person && userInfo.person.email) {
      console.log(`📧 邮箱地址: ${userInfo.person.email}`);
    }
    
    if (userInfo.avatar_url) {
      console.log(`🖼️ 头像URL: ${userInfo.avatar_url}`);
    }
    
    console.log('\n📄 完整响应数据:');
    console.log(JSON.stringify(userInfo, null, 2));
    
    console.log('\n🎉 获取机器人创建者信息演示完成！');
    
    return userInfo;
    
  } catch (error: any) {
    console.error('❌ 获取机器人创建者信息演示失败:', error.message);
    if (error.response?.data) {
      console.error('详细错误信息:', JSON.stringify(error.response.data, null, 2));
    }
    
    // 根据错误类型提供具体的解决建议
    if (error.message.includes('401')) {
      console.error('💡 解决建议: 请检查您的API Token是否正确，或者Token是否已过期');
    } else if (error.message.includes('403')) {
      console.error('💡 解决建议: 请检查您的API Token权限，确保有访问用户信息的权限');
    } else if (error.message.includes('404')) {
      console.error('💡 解决建议: 机器人创建者信息不存在，请联系管理员');
    } else if (error.message.includes('网络连接失败')) {
      console.error('💡 解决建议: 请检查网络连接和API服务器地址配置');
    }
    
    process.exit(1);
  }
}

// 如果直接运行此文件，则执行演示
if (require.main === module) {
  userMeDemo();
}

export default userMeDemo;
