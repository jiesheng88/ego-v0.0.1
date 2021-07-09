package com.ego.cart.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.redis.dao.JedisDao;

@Service
public class CartServiceImpl implements CartService{
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${passport.url}")
	private String passportUrl;
	@Value("${cart.key}")
	private String cartKey;

	@Override
	public void addCart(long id, int num, HttpServletRequest request) {
		// 列表中存放所有购物车商品
		List<TbItemChild> list = new ArrayList<TbItemChild>();
		
		/* 1、获取Redis中的key */
		// 获取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 通过调用ego-passport的控制器获取用户信息，返回结果是Egresult
		String json = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(json, EgoResult.class);
		// 使用 cart:用户名 来组成Redis中的key
		String key = cartKey + ((LinkedHashMap)er.getData()).get("username");
		
		/* 2、 商品已在购物车中 */
		// 先判断Redis中是否已经有key
		if (jedisDaoImpl.exists(key)) {
			String value = jedisDaoImpl.get(key);
			if (value!=null && !value.equals("")) {
				// 证明商品已经在购物车中了
				// 获取购物车中商品列表
				list = JsonUtils.jsonToList(value, TbItemChild.class);
				// 遍历找到指定商品
				for (TbItemChild itemChild : list) {
					if ((long)itemChild.getId() == id) {
						// 修改购物车中商品数量
						itemChild.setNum(itemChild.getNum()+num);
						// 重新加入Redis中
						jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
						return ;
					}
				}
				
			}
		}
		
		/* 3、商品未在购物车中 */
		// 根据id查询商品item信息
		TbItem item = tbItemDubboServiceImpl.selById(id);
		// 设置往列表中添加的商品信息：id、title、images、num、price（其中num即传进来的参数num值）
		TbItemChild child = new TbItemChild();
		child.setId(item.getId());
		child.setTitle(item.getTitle());
		String images = item.getImage();
		child.setImages(images==null || images.equals("")?new String[1]:images.split(","));
		child.setNum(num);
		child.setPrice(item.getPrice());
		// 加入到list中
		list.add(child);
		// list加入到Redis中
		jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
	}

	@Override
	public List<TbItemChild> showCart(HttpServletRequest request) {
		/* 1、获取Redis中的key */
		// 获取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 通过调用ego-passport的控制器获取用户信息，返回结果是Egresult
		String json = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(json, EgoResult.class);
		// 使用 cart:商品名 来组成Redis中的key
		String key = cartKey + ((LinkedHashMap)er.getData()).get("username");
		
		// 获取Redis中key对应的值
		String result = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(result, TbItemChild.class);
		return list;
	}

	@Override
	public EgoResult update(long id, int num, HttpServletRequest request) {
		/* 1、获取Redis中的key */
		// 获取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 通过调用ego-passport的控制器获取用户信息，返回结果是Egresult
		String json = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(json, EgoResult.class);
		// 使用 cart:商品名 来组成Redis中的key
		String key = cartKey + ((LinkedHashMap)er.getData()).get("username");
		
		// 获取Redis中的值
		String result = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(result, TbItemChild.class);
		// 更改商品数量
		for (TbItemChild itemChild : list) {
			if ((long)itemChild.getId() == id) {
				itemChild.setNum(num);
			}
		}
		// 重新设置Redis值
		String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
		
		// 设置返回值
		EgoResult egoResult = new EgoResult();
		if (ok.equals("OK")) {
			egoResult.setStatus(200);
		}
		
		return egoResult;
	}

	@Override
	public EgoResult delete(long id, HttpServletRequest request) {
		/* 1、获取Redis中的key */
		// 获取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 通过调用ego-passport的控制器获取用户信息，返回结果是Egresult
		String json = HttpClientUtil.doPost(passportUrl + token);
		EgoResult er = JsonUtils.jsonToPojo(json, EgoResult.class);
		// 使用 cart:商品名 来组成Redis中的key
		String key = cartKey + ((LinkedHashMap) er.getData()).get("username");

		// 获取Redis中的值
		String result = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(result, TbItemChild.class);
		// 移除商品
		TbItemChild tbItemChild = null;
		for (TbItemChild itemChild : list) {
			if ((long) itemChild.getId() == id) {
				// 先取出待删除商品
				tbItemChild = itemChild;
			}
		}
		// 然后通过商品对象从list中删除
		list.remove(tbItemChild);
		
		// 重新设置Redis值
		String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));

		// 设置返回值
		EgoResult egoResult = new EgoResult();
		if (ok.equals("OK")) {
			egoResult.setStatus(200);
		}

		return egoResult;
	}

}
