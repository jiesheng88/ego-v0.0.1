package com.ego.item.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.item.service.TbItemDescService;

@Controller
public class TbItemDescController {
	@Resource
	private TbItemDescService tbItemDescServiceImpl;
	
	/**
	 * 显示商品描述详情
	 * produces="text/html;charset=utf-8" 是为了将service中返回的结果以HTML的样式直接显示在页面上
	 * @param id
	 * @return
	 */
	@RequestMapping(value="item/desc/{id}.html",produces="text/html;charset=utf-8")
	@ResponseBody
	public String showItemDesc(@PathVariable long id){
		return tbItemDescServiceImpl.showItemDesc(id);	
	}
}
