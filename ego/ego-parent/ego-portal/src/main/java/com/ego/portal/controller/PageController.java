package com.ego.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	/**
	 * 门户首页
	 * @return
	 */
	@RequestMapping("/")
	public String welcome(){
		return "forward:/shwoBigPic";
	}
}
