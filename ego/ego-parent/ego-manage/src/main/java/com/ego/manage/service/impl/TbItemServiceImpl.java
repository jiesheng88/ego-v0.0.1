package com.ego.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;
import com.ego.redis.dao.JedisDao;

@Service
public class TbItemServiceImpl implements TbItemService {
	// Dubbo的注解，类似于Spring的@Resource,注入的是分布式中的远程服务对象
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${search.url}")
	private String url;
	@Value("${redis.item.key}")
	private String itemKey;

	@Override
	public EasyUIDataGrid show(int page, int rows) {
		return tbItemDubboServiceImpl.show(page, rows);
	}

	@Override
	public int update(String ids, byte status) {
		int index = 0;
		TbItem tbItem = new TbItem();
		
		// 对接受到的数据进行拆分，获得id数组
		String[] idsStr = ids.split(",");
		
		// 遍历所有的id，依次进行状态更新
		for (String id : idsStr) {
			tbItem.setId(Long.parseLong(id));
			tbItem.setStatus(status);
			index += tbItemDubboServiceImpl.updItemStatus(tbItem);
			// 当status==2||status==3(即商品下架或删除)时，从Redis缓存中删除key
			String key = itemKey+id;
			if (status==2||status==3) {
				jedisDaoImpl.del(key);
			}
		}
		
		// 判断是否全部修改完成：全部修改完，返回1
		if (index == idsStr.length) { 
			return 1;
		}
		return 0;
	}

	@Override
	public int insert(TbItem tbItem, String desc, String itemParams) throws Exception {
		/* 没有事务回滚 */
//		// 待插入商品数据的全部信息，在数据库中有两个表来存储
//		// 补全待插入商品数据
//		long id = IDUtils.genItemId();
//		tbItem.setId(id);	// 设置id
//		tbItem.setStatus((byte) 1);	// 设置商品状态--正常
//		Date date = new Date();
//		tbItem.setCreated(date);	// 设置商品上架时间
//		tbItem.setUpdated(date);	// 设置商品更新时间
//		// 插入商品前部分信息
//		int index = tbItemDubboServiceImpl.insTbItem(tbItem);
//		
//		if (index==1) { // 插入商品前部分信息---成功
//			// 补全待插入商品数据的描述信息
//			TbItemDesc tbItemDesc = new TbItemDesc();
//			tbItemDesc.setItemId(id);
//			tbItemDesc.setItemDesc(desc);
//			tbItemDesc.setCreated(date);
//			tbItemDesc.setUpdated(date);
//			// 插入商品描述信息
//			index += tbItemDescDubboServiceImpl.insDesc(tbItemDesc);
//		}
//		
//		if (index == 2) {  // 全部插入成功
//			return 1;
//		}
//		return 0;
		
		/* 有事务回滚 */
		// 补全待插入商品数据
		long id = IDUtils.genItemId();
		tbItem.setId(id);	// 设置id
		tbItem.setStatus((byte) 1);	// 设置商品状态--正常
		Date date = new Date();
		tbItem.setCreated(date);	// 设置商品上架时间
		tbItem.setUpdated(date);	// 设置商品更新时间
		
		// 补全待插入商品数据的描述信息
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(id);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		
		// 补全待插入商品的规格参数信息
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setCreated(date);
		tbItemParamItem.setUpdated(date);
		tbItemParamItem.setParamData(itemParams);
		tbItemParamItem.setItemId(id);
		
		// 插入商品信息
		int index = tbItemDubboServiceImpl.insTbItemAndDesc(tbItem, tbItemDesc, tbItemParamItem);
		
		// final修饰的参数
		final TbItem tbItemFinal = tbItem;
		final String descFinal = desc;
		// 使用线程优化Solr相关代码
		new Thread(){
			public void run() {
				//使用java代码调用其他项目的控制器
				// 传入的参数的格式是 Map<String, String>
				// 传入的参数是TbItem tbItem, String desc
				Map<String, Object> param = new HashMap<>();
				// 设置Solr字段数据可以使用map.put("字段名","值")的方式一个一个设置+doPost的方式
				// 这里使用另一种方式，传输的数据使用json格式+doPostJson
				// 线程中使用的变量必须是全局变量或是final修饰的！！！！
				param.put("tbItem", tbItemFinal);		// 将整个对象直接放入map中
				param.put("desc", descFinal);
				HttpClientUtil.doPostJson(url, JsonUtils.objectToJson(param)); // URL请求的是ego-search中的控制器
			};
		}.start();
		
		return index;

	}

}
