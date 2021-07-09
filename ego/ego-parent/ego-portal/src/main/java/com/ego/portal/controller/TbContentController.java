package com.ego.portal.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ego.portal.service.TbContentService;

@Controller
public class TbContentController {
	@Resource
	private TbContentService tbContentServiceImpl;
	
	@RequestMapping("shwoBigPic")
	public String showBigPic(Model model){
		// 通过Model传输request作用域的数据
		model.addAttribute("ad1", tbContentServiceImpl.showBigPic());
		return "index";
	}

}
