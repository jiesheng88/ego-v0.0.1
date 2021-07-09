package com.ego.dubbo.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItemParam;

public interface TbItemParamDubboService {
	/**
	 * 分页查询数据---规格参数
	 * @param page
	 * @param rows
	 * @return	包含：当前页显示数据list，和总的数据个数total
	 */
	EasyUIDataGrid showPage(int page, int rows);
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	int delByIds(String ids) throws Exception;
	
	/**
	 * 根据itemCatId查询
	 * @param itemcatid
	 * @return
	 */
	TbItemParam selByItemCatId(long itemcatid);
	
	/**
	 * 新增规格参数
	 * @param tbItemParam
	 * @return
	 */
	int insItemParam(TbItemParam tbItemParam);

}
