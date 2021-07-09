package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContent;

public interface TbContentService {
	/**
	 * 分页显示内容信息
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGrid showPage(long categoryId, int page, int rows);
	
	/**
	 * 新增
	 * @param tbContent
	 * @return
	 */
	EgoResult save(TbContent tbContent);
	
	/**
	 * 编辑
	 * @param tbContent
	 * @return
	 */
	EgoResult edit(TbContent tbContent);
	
	/**
	 * 根据传过来的ids，批量删除
	 * @param ids
	 * @return
	 */
	EgoResult delete(String ids);

}
