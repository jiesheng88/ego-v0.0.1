package com.ego.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUITree;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.manage.service.TbItemCatService;
import com.ego.pojo.TbItemCat;

@Service
public class TbItemCatServiceImpl implements TbItemCatService{
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;

	@Override
	public List<EasyUITree> show(long pid) {
		List<EasyUITree> easyUITreeList = new ArrayList<>();
		/* EasyUITree 中的id对应于TbItemCat中的id
		 * EasyUITree 中的text对应于TbItemCat中的name
		 * EasyUITree 中的state根据TbItemCat中的isParent来决定，如果是父菜单，则设置为closed；不是父菜单，设置为open
		 */
		List<TbItemCat> tbItemCatList = tbItemCatDubboServiceImpl.show(pid);
		for (TbItemCat tbItemCat : tbItemCatList) {
			EasyUITree easyUITree = new EasyUITree();
			easyUITree.setId(tbItemCat.getId());
			easyUITree.setText(tbItemCat.getName());
			easyUITree.setState(tbItemCat.getIsParent()?"closed":"open");
			easyUITreeList.add(easyUITree);
		}
		return easyUITreeList;
	}

}
