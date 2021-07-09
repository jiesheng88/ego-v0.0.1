package com.ego.dubbo.service;

import java.util.List;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbContent;

public interface TbContentDubboService {
	/**
	 * 分页查询--内容
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGrid selContentByPage(long categoryId, int page, int rows);
	
	/**
	 * 新增内容
	 * @param tbContent
	 * @return
	 */
	int insByContent(TbContent tbContent);
	
	/**
	 * 修改内容
	 * @param tbContent
	 * @return
	 */
	int updByContent(TbContent tbContent);
	
	/**
	 * 根据id删除内容
	 * @param id
	 * @return
	 */
	int delByid(long id);
	
	/**
	 * 根据是否排序，查询前n个内容
	 * @param count
	 * @param isSort
	 * @return
	 */
	List<TbContent> selByCount(int count, boolean isSort);
}
