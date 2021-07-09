package com.ego.manage.service;

import java.util.List;

import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

public interface TbContentCategoryService {
	/**
	 * 查询所有类目并转换为Easyui tree的属性要求
	 * @param pid
	 * @return  返回值为EasyUITree对象，因为EasyUI树控件的数据格式化中，需要传入id,text,state
	 */
	List<EasyUITree> show(long id);
	
	/**
	 * 根据传输来的parentId和name两个参数，新增节点
	 * 如果jsp文件中需要传输status和data值的话，返回值一般都是EgoResult
	 * @param tbContentCategory
	 * @return 
	 */
	EgoResult create(TbContentCategory tbContentCategory);
	
	/**
	 * 根据传输来的id和name两个参数，修改节点(重命名)
	 * @param tbContentCategory
	 * @return
	 */
	EgoResult update(TbContentCategory tbContentCategory);
	
	/**
	 * 根据传输来的id，删除节点
	 * @param tbContentCategory
	 * @return
	 */
	EgoResult delete(TbContentCategory tbContentCategory);

}
