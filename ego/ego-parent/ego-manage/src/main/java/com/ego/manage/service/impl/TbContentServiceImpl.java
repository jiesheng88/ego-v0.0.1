package com.ego.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.I0Itec.zkclient.IDefaultNameSpace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.manage.service.TbContentService;
import com.ego.pojo.TbContent;
import com.ego.redis.dao.JedisDao;

@Service
public class TbContentServiceImpl implements TbContentService {
	@Reference
	private TbContentDubboService tbContentDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.bigpic.key}")
	private String key;

	@Override
	public EasyUIDataGrid showPage(long categoryId, int page, int rows) {
		return tbContentDubboServiceImpl.selContentByPage(categoryId, page, rows);
	}

	@Override
	public EgoResult save(TbContent tbContent) {
		EgoResult egoResult = new EgoResult();
		
		Date date = new Date();
		tbContent.setCreated(date);
		tbContent.setUpdated(date);
		
		int index = tbContentDubboServiceImpl.insByContent(tbContent);
		if (index>0) {
			egoResult.setStatus(200);
		}
		
		// 将新添加的大广告添加到缓存中
		// 1、判断缓存Redis中是否有数据
		if (jedisDaoImpl.exists(key)) {
			String value = jedisDaoImpl.get(key);
			if (value!=null && !value.equals("")) { // 缓存中有数据
				// 2、设置一个map存储新添加大广告的数据
				HashMap<String, Object> map = new HashMap<>();
				map.put("srcB",tbContent.getPic2());
				map.put("height",240);
				map.put("alt","");
				map.put("width",670);
				map.put("src",tbContent.getPic());
				map.put("widthB",550);
				map.put("href",tbContent.getUrl());
				map.put("heightB",240);
				
				// 3、将缓存数据转换为List列表
				List<HashMap> list = JsonUtils.jsonToList(value, HashMap.class);
				
				// 4、确保缓存中的数据都是最新的六个
				if (list.size()==6) {
					// 列表中新添加的index为0，所以删除index为5的，即最先添加的
					list.remove(5);
				}
				list.add(0,map); // 添加最新的数据到index为0处
				
				// 5、将列表list转换成json格式，保存到缓存Redis中
				jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
			}
		}
		
		return egoResult;
	}

	@Override
	public EgoResult edit(TbContent tbContent) {
		EgoResult egoResult = new EgoResult();
		
		Date date = new Date();
		tbContent.setCreated(date);
		tbContent.setUpdated(date);
		
		int index = tbContentDubboServiceImpl.updByContent(tbContent);
		if (index>0) {
			egoResult.setStatus(200);
		}
		return egoResult;
	}

	@Override
	public EgoResult delete(String ids) {
		EgoResult egoResult = new EgoResult();
		
		// 拆分传过来的ids
		String[] idStr = ids.split(",");
		
		int index = 0;
		
		// 遍历，批量删除
		for (String id : idStr) {
			index += tbContentDubboServiceImpl.delByid(Long.parseLong(id));
		}
		
		if (index == idStr.length) {
			egoResult.setStatus(200);
		}
		
		return egoResult;
	}

}
