package com.ego.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.portal.service.TbContentService;
import com.ego.redis.dao.JedisDao;

@Service
public class TbContentServiceImpl implements TbContentService {
	@Reference
	private TbContentDubboService tbContentDubboService;
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.bigpic.key}")
	private String key;

	@Override
	public String showBigPic() {
		// 1、先在缓存Redis中判断是否存在
		if (jedisDaoImpl.exists(key)) {
			// 非空
			if (jedisDaoImpl.get(key)!=null && !jedisDaoImpl.get(key).equals("")) {
				// 存在，直接返回
				return jedisDaoImpl.get(key);
			}
		}
		
		// 2、缓存中不存在，先从数据库中取
		// 排序后，查询前六个数据(排序的目的是每次都是查询最新的六个数据)
		List<TbContent> listContent = tbContentDubboService.selByCount(6, true);
		
		// 因为传输的数据data是一个数组，所以使用List
		List<Map<String, Object>> listJson = new ArrayList<>();
		for (TbContent tbContent : listContent) {
			// 因为只使用一次，所以用map来存储，更高效
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("srcB", tbContent.getPic2());
			map.put("height", 240);
			map.put("alt", "");
			map.put("width", 670);
			map.put("src", tbContent.getPic());
			map.put("widthB", 550);
			map.put("href", tbContent.getUrl());
			map.put("heightB", 240);
			listJson.add(map);
		}
		// 转换成json格式
		String listResult = JsonUtils.objectToJson(listJson);
		
		// 3、存到缓存Redis中
		jedisDaoImpl.set(key, listResult);
		
		return listResult;
	}

}
