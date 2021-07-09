package com.ego.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService{
	@Reference
	private TbContentCategoryDubboService tbContentCategoryDubboServiceImpl;

	@Override
	public List<EasyUITree> show(long id) {
		List<EasyUITree> listTree = new ArrayList<EasyUITree>();
		// 点击一次，发生一次请求，把id传过去查询该层所有子类菜单
		List<TbContentCategory> listCategory = tbContentCategoryDubboServiceImpl.selByPid(id);
		
		// 遍历,将每个对象保存到EasyUITree中
		for (TbContentCategory category : listCategory) {
			EasyUITree easyUITree = new EasyUITree();
			easyUITree.setId(category.getId());
			easyUITree.setText(category.getName());
			// 根据是否是父节点判断，是父节点的话，设置为closed
			easyUITree.setState(category.getIsParent()?"closed":"open");
			listTree.add(easyUITree);
		}
		return listTree;
	}

	@Override
	public EgoResult create(TbContentCategory tbContentCategory) {
		EgoResult egoResult = new EgoResult();
		
		// 1、判断待插入节点的父节点中的子节点是否有与待插入节点相同名字的
		List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByPid(tbContentCategory.getParentId());
		for (TbContentCategory category : list) {
			if (tbContentCategory.getName().equals(category.getName())) {
				egoResult.setData("有重名子节点~~~");
				return egoResult;
			}
		}
		
		// 补齐新增对象tbContentCategory的全部信息
		Date date = new Date();
		tbContentCategory.setCreated(date);
		tbContentCategory.setUpdated(date);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setStatus(1);
		long id = IDUtils.genItemId();
		tbContentCategory.setId(id);
		
		// 2、新增
		int index = tbContentCategoryDubboServiceImpl.insByCategory(tbContentCategory);
		
		// 3、修改父节点的isParent
		if (index>0) {
			TbContentCategory parent = new TbContentCategory();
			parent.setId(tbContentCategory.getParentId());
			parent.setIsParent(true);
			tbContentCategoryDubboServiceImpl.updById(parent);
		}
		
		// 设置往jsp文件中传输的内容
		egoResult.setStatus(200);
		// 因为data中需要传入新插节点的id值，并且只有这一个值，所以使用map更快
		Map<String, Long> data = new HashMap<String, Long>();
		data.put("id", id);
		egoResult.setData(data);
		
		return egoResult;
	}

	@Override
	public EgoResult update(TbContentCategory tbContentCategory) {
		EgoResult egoResult = new EgoResult();
		
		// 1、根据传过来的id查询详细信息
		TbContentCategory cateSelect = tbContentCategoryDubboServiceImpl.selById(tbContentCategory.getId());
		
		// 2、遍历该节点的父节点的所有子节点有没有与修改后名字name相同的
		List<TbContentCategory> children = tbContentCategoryDubboServiceImpl.selByPid(cateSelect.getParentId());
		for (TbContentCategory child : children) {
			if (tbContentCategory.getName().equals(child.getName())) {
				egoResult.setData("与已有子节点重名~~~");
				// 如果重名，这里就要return，不再执行更新操作
				return egoResult;
			}
		}
		
		// 3、更新
		int index = tbContentCategoryDubboServiceImpl.updById(tbContentCategory);
		if (index>0) {
			egoResult.setStatus(200);
		}
		
		return egoResult;
	}

	@Override
	public EgoResult delete(TbContentCategory tbContentCategory) {
		EgoResult egoResult = new EgoResult();
		
		// 1、根据传过来的id查询详细信息
		TbContentCategory cateSelect = tbContentCategoryDubboServiceImpl.selById(tbContentCategory.getId());
		
		// 2、删除:不是真正删除，只是将其状态status设置为了2
		tbContentCategory.setStatus(2); // 
		int index = tbContentCategoryDubboServiceImpl.updById(tbContentCategory);
		
		// 3、如果删除节点的父节点没有子节点的话，将父节点的isParent设置为false
		if (index>0) {
			List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByPid(cateSelect.getParentId());
			// 判断子列表是否为空
			if (list==null || list.size()==0) { // 为空，修改父节点的IsParent
				TbContentCategory parent = new TbContentCategory();
				parent.setId(cateSelect.getParentId());
				parent.setIsParent(false);
				int result = tbContentCategoryDubboServiceImpl.updById(parent);
				if (result>0) {
					egoResult.setStatus(200);
				}
			}else {
				egoResult.setStatus(200);
			}
		}else {
			egoResult.setData("删除失败~~~");
		}
		
		return egoResult;
	}
}
