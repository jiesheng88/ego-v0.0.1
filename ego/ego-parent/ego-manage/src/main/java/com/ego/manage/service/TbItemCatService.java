package com.ego.manage.service;

import java.util.List;

import com.ego.commons.pojo.EasyUITree;

public interface TbItemCatService {
	/**
	 * 根据父菜单id显示所有子菜单
	 * @param pid
	 * @return  返回值为EasyUITree对象，因为EasyUI树控件的数据格式化中，需要传入id,text,state
	 */
	List<EasyUITree> show(long pid);

}
