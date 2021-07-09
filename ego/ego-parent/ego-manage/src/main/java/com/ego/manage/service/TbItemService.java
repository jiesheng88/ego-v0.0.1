package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItem;

public interface TbItemService {
	/**
	 * 业务逻辑层	分页显示商品
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGrid show(int page, int rows);
	
	/**
	 * 批量修改商品状态
	 * @param ids	选多个商品时，接收到的数据是 ids: 917770,919669,925237
	 * @param status	商品的状态
	 * @return
	 */
	int update(String ids, byte status);
	
	/**
	 * 商品新增
	 * @param tbItem
	 * @param desc
	 * @param itemParams
	 * @return
	 * @throws Exception
	 */
	int insert(TbItem tbItem,String desc, String itemParams) throws Exception;

}
