package com.ego.item.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.item.service.TbItemDescService;
import com.ego.pojo.TbItemDesc;
import com.ego.redis.dao.JedisDao;

@Service
public class TbItemDescServiceImpl implements TbItemDescService{
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.desc.key}")
	private String descKey;

	@Override
	public String showItemDesc(long itemId) {
		// 判断Redis缓存中是否有
		String key = descKey+itemId;
		if (jedisDaoImpl.exists(key)) {
			String value = jedisDaoImpl.get(key);
			if (value!=null || !value.equals("")) {
				return value;
			}
		}
		
		// 从数据库中查询获得
		String result = tbItemDescDubboServiceImpl.selById(itemId).getItemDesc();
		
		// 保存到缓存中
		jedisDaoImpl.set(key, result);
		
		return result;
	}

}
