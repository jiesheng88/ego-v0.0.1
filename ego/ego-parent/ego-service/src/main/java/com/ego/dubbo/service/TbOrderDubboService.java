package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;

public interface TbOrderDubboService {
	/**
	 * 创建订单
	 * 利用事务同时新增tb_order、tb_order_item和tb_order_shipping三张表的数据
	 * @param order
	 * @param list
	 * @param shipping
	 * @return
	 * @throws Exception 
	 */
	int insOrder(TbOrder order, List<TbOrderItem> list, TbOrderShipping shipping) throws Exception;

}
