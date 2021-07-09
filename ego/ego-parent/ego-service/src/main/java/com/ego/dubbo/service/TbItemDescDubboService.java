package com.ego.dubbo.service;

import com.ego.pojo.TbItemDesc;

public interface TbItemDescDubboService {
	/**
	 * 新增---商品描述信息
	 * @param tbItemDesc
	 * @return
	 */
	int insDesc(TbItemDesc tbItemDesc);
	
	/**
	 * 根据id查询信息
	 * @param itemId
	 * @return
	 */
	TbItemDesc selById(long itemId);

}
