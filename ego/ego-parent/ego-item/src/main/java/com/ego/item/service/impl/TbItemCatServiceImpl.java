package com.ego.item.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.item.pojo.PortalMenu;
import com.ego.item.pojo.PortalMenuNode;
import com.ego.item.service.TbItemCatService;
import com.ego.pojo.TbItemCat;

@Service
public class TbItemCatServiceImpl implements TbItemCatService{
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;

	@Override
	public PortalMenu showCatMenu() {
		// 查询出所有一级菜单
		List<TbItemCat> listFirst = tbItemCatDubboServiceImpl.show(0);
		
		// 通过递归获取所有子菜单列表
		List<Object> list = selAllMenu(listFirst);
		
		// 设置返回结果
		PortalMenu pm = new PortalMenu();
		pm.setData(list);
		
		return pm;
	}
	
	/**
	 * 最终返回结果：所有查询到的结果
	 * @param list 只返回这一级菜单的u、n、i，所以使用List<Object>
	 * @return
	 */
	public List<Object> selAllMenu(List<TbItemCat> list) {
		// 因为有可能存PortalMenuNode，也可能存字符串，所以是Object
		List<Object> listNode = new ArrayList<>();
		
		// 遍历当前级别菜单
		for (TbItemCat tbItemCat : list) {
			// 如果是父菜单，其中设置的i为List
			if (tbItemCat.getIsParent()) {
				PortalMenuNode pmn = new PortalMenuNode();
				pmn.setU("/products/"+tbItemCat.getId()+".html");
				pmn.setN("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
				// 通过递归获取所有子菜单
				pmn.setI(selAllMenu(tbItemCatDubboServiceImpl.show(tbItemCat.getId())));
				listNode.add(pmn);
			}else {
				// "i":["/products/3.html|电子书","/products/4.html|网络原创","/products/5.html|数字杂志","/products/6.html|多媒体图书"]
				// 当是最后一级菜单时，i中存储的直接是字符串
				listNode.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
			}
		}
		return listNode;
	
	}

}
