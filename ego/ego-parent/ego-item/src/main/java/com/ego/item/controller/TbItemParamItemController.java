package com.ego.item.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.item.service.TbItemParamItemService;

@Controller
public class TbItemParamItemController {
	@Resource
	private TbItemParamItemService tbItemParamItemServiceimpl;
	
	/**
	 * 显示商品规格参数详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value="item/param/{id}.html",produces="text/html;charset=utf-8")
	@ResponseBody
	public String showParam(@PathVariable long id){
		return tbItemParamItemServiceimpl.showParam(id);
	}

}
