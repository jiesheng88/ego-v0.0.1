package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbContentCategory;

public interface TbContentCategoryDubboService {
	/**
	 * 根据父id查询所有子类目
	 * @param pid
	 * @return
	 */
	List<TbContentCategory> selByPid(long pid);
	
	/**
	 * 根据传输来的parentId和name两个参数，新增
	 * @param tbContentCategory
	 * @return
	 */
	int insByCategory(TbContentCategory tbContentCategory);
	
	/**
	 * 通过id来修改Category中的isParent属性
	 * @param tbContentCategory
	 * @return
	 */
	int updById(TbContentCategory tbContentCategory);
	
	/**
	 * 通过id查询内容类目详细信息
	 * @param id
	 * @return
	 */
	TbContentCategory selById(long id);

}
