package com.ego.item.controller;

import javax.annotation.Resource;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.item.pojo.PortalMenu;
import com.ego.item.service.TbItemCatService;

@Controller
public class TbItemCatController {
	@Resource
	private TbItemCatService tbItemCatServiceImpl;
	
	/**
	 * 返回jsonp数据格式，包含所有菜单信息
	 * @param callback
	 * @return
	 */
	@RequestMapping("rest/itemcat/all")
	@ResponseBody
	public MappingJacksonValue showMenu(String callback){
		// 获取返回结果对象
		PortalMenu pm = tbItemCatServiceImpl.showCatMenu();
		// 将结果对象转换成js格式
		MappingJacksonValue mjv = new MappingJacksonValue(pm);
		mjv.setJsonpFunction(callback);
		return mjv;
	}
}
