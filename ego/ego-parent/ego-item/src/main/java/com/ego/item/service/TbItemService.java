package com.ego.item.service;

import com.ego.commons.pojo.TbItemChild;

public interface TbItemService {
	/**
	 * 显示商品详情
	 * @return
	 */
	TbItemChild showItemDetail(long id);
}
