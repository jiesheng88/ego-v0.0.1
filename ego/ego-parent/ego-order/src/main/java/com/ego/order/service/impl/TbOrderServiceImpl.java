package com.ego.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.dubbo.service.TbOrderDubboService;
import com.ego.order.pojo.MyOrderParam;
import com.ego.order.service.TbOrderService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;
import com.ego.redis.dao.JedisDao;

@Service
public class TbOrderServiceImpl implements TbOrderService{
	@Resource
	private JedisDao jedisDaoImpl;
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Reference
	private TbOrderDubboService tbOrderDubboServiceImpl;
	
	@Value("${passport.url}")
	private String passportUrl;
	@Value("${cart.key}")
	private String cartKey;

	@Override
	public List<TbItemChild> showOrderCart(List<Long> ids, HttpServletRequest request) {
		// 获取Redis的key
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String jsonKey = HttpClientUtil.doPost(passportUrl+token);
		EgoResult egoResult = JsonUtils.jsonToPojo(jsonKey, EgoResult.class);
		String key = cartKey+((LinkedHashMap)egoResult.getData()).get("username");
		
		// 获取购物车中的商品信息
		String jsonCart = jedisDaoImpl.get(key);
		List<TbItemChild> listCart = JsonUtils.jsonToList(jsonCart, TbItemChild.class);
		
		// 定义存储订单中商品的list
		List<TbItemChild> listOrder = new ArrayList<TbItemChild>();
		
		// 遍历找到订单中的商品
		for (TbItemChild itemCart : listCart) {
			for (Long id : ids) {
				if (itemCart.getId().equals(id)) {
					// 判断库存是否满足购买量
					TbItem tbItem = tbItemDubboServiceImpl.selById(id);
					if (tbItem.getNum() >= itemCart.getNum()) { // 库存大于购买量
						itemCart.setEnough(true);
					}else {
						itemCart.setEnough(false);
					}
					listOrder.add(itemCart);
				}
			}
		}
		return listOrder;
	}

	@Override
	public EgoResult create(MyOrderParam param, HttpServletRequest request) {
		// 1、订单表数据
		TbOrder order = new TbOrder();
		order.setPayment(param.getPayment());
		order.setPaymentType(param.getPaymentType());
		long orderId = IDUtils.genItemId();
		order.setOrderId(orderId+"");
		Date date = new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		// 通过Cookie+Redis获取用户信息
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 通过token获取用户信息
		String json = HttpClientUtil.doPost(passportUrl+token);
		EgoResult egoResult = JsonUtils.jsonToPojo(json, EgoResult.class);
		// 因为EgoResult中的Date是Object类型，egoResult.getData()之后，Java默认将其转换成LinkedHashMap类型
		Map map = (LinkedHashMap)egoResult.getData();
		long userId = Long.parseLong(map.get("id").toString());
		String buyerNick = map.get("username").toString();
		order.setUserId(userId);	// 买家用户ID
		order.setBuyerNick(buyerNick);	// 买家昵称
		
		// 2、订单---商品表数据
		for (TbOrderItem orderItem : param.getOrderItems()) {
			orderItem.setId(IDUtils.genItemId()+"");
			orderItem.setOrderId(orderId+"");
		}
		
		// 3、收货人信息
		TbOrderShipping shipping = param.getOrderShipping();
		shipping.setOrderId(orderId+"");
		shipping.setCreated(date);
		shipping.setUpdated(date);
		
		// 4、设置返回值
		EgoResult er = new EgoResult();
		try {
			int index = tbOrderDubboServiceImpl.insOrder(order, param.getOrderItems(), shipping);
			if (index>0) {
				er.setStatus(200);
				// 5、删除购物车中的订单物品
				// 获取购物车商品列表
				String jsonCart = jedisDaoImpl.get(cartKey+map.get("username"));
				List<TbItemChild> listCart = JsonUtils.jsonToList(jsonCart, TbItemChild.class);
				
				// 定义暂存待删除物品的列表
				List<TbItemChild> listRemove = new ArrayList<>();
				
				// 遍历，将待删除物品暂存到待删除物品列表
				for (TbItemChild cartItem : listCart) {
					for (TbOrderItem orderItem : param.getOrderItems()) {
						if (Long.parseLong(orderItem.getItemId()) == cartItem.getId()) {
//							System.out.println("1"+orderItem.getItemId());
//							System.out.println("2"+cartItem.getId());
							listRemove.add(cartItem);
						}
					}
				}
				
				// 遍历删除待删除物品
				for (TbItemChild item : listRemove) {
					listCart.remove(item);
				}
				
 				// 重新保存到Redis中
				jedisDaoImpl.set(cartKey+map.get("username"), JsonUtils.objectToJson(listCart));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return er;
	}


}
