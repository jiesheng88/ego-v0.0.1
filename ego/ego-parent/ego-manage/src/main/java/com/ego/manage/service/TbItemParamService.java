package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItemParam;

public interface TbItemParamService {
	/**
	 * 分页显示--规格参数
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGrid show(int page, int rows);

	/**
	 * 批量删除--规格参数
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	int delete(String ids) throws Exception;
	
	/**
	 * 根据itemcatid显示模板信息
	 * @param itemcatid
	 * @return
	 */
	EgoResult showParam(long itemcatid);
	
	/**
	 * 新增规格参数---模板信息
	 * @param tbItemParam
	 * @return
	 */
	EgoResult insert(TbItemParam tbItemParam);
	
	
	
}
