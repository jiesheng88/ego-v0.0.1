package com.ego.item.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.item.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.redis.dao.JedisDao;

import redis.clients.jedis.JedisCluster;

@Service
public class TbItemServiceImpl implements TbItemService{
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.item.key}")
	private String itemKey;

	@Override
	public TbItemChild showItemDetail(long id) {
		String key = itemKey+id;
		// 判断Redis缓存中是否有
		if (jedisDaoImpl.exists(key)) {
			String value = jedisDaoImpl.get(key);  // 字符串格式相当于是json格式
			if (value!=null && !value.equals("")) {
				return JsonUtils.jsonToPojo(value, TbItemChild.class);
			}
		}
		
		// Redis缓存中没有从数据库中查询
		TbItem tbItem = tbItemDubboServiceImpl.selById(id);
		
		TbItemChild child = new TbItemChild();
		child.setId(tbItem.getId());
		child.setTitle(tbItem.getTitle());
		child.setPrice(tbItem.getPrice());
		child.setSellPoint(tbItem.getSellPoint());
		String images = tbItem.getImage(); // 使用,隔开的图片地址
		child.setImages(images==null||images.equals("")?new String[1]:images.split(","));
		
		// 查询结果保存到Redis缓存中
		jedisDaoImpl.set(key, JsonUtils.objectToJson(child));
		
		return child;
	}

}
