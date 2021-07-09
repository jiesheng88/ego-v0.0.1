package com.ego.item.service;

import com.ego.item.pojo.PortalMenu;

public interface TbItemCatService {
	/**
	 * 查询出所有分类类目并转换需要的特定类型
	 * @return	自定义的PortalMenu类
	 */
	PortalMenu showCatMenu();

}
