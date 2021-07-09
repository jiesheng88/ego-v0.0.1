package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbContentService;
import com.ego.pojo.TbContent;

@Controller
public class TbContentController {
	@Resource
	private TbContentService tbContentService;
	
	/**
	 * 分页显示内容信息
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("content/query/list")
	@ResponseBody
	public EasyUIDataGrid showPage(long categoryId, int page, int rows){
		return tbContentService.showPage(categoryId, page, rows);
	}
	
	/**
	 * 新增内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("content/save")
	@ResponseBody
	public EgoResult save(TbContent tbContent){
		return tbContentService.save(tbContent);
	}
	
	/**
	 * 编辑内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("rest/content/edit")
	@ResponseBody
	public EgoResult edit(TbContent tbContent){
		return tbContentService.edit(tbContent);
	}
	
	/**
	 * 根据传输的ids，批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("content/delete")
	@ResponseBody
	public EgoResult delete(String ids){
		return tbContentService.delete(ids);
	}

}
