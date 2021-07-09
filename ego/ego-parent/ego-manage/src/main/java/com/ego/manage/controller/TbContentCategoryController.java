package com.ego.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;

@Controller
public class TbContentCategoryController {
	@Resource
	private TbContentCategoryService tbContentCategoryServiceImpl;
	
	/**
	 * 显示商品类目
	 * @param id	全部显示的话，一级节点的父节点为0
	 * @return
	 */
	@RequestMapping("content/category/list")
	@ResponseBody
	public List<EasyUITree> show(@RequestParam(defaultValue="0") long id){
		return tbContentCategoryServiceImpl.show(id);
	}
	
	/**
	 * 根据传输来的parentId和name两个参数，新增内容类目
	 * @param tbContentCategory
	 * @return
	 */
	@RequestMapping("content/category/create")
	@ResponseBody
	public EgoResult create(TbContentCategory tbContentCategory){
		return tbContentCategoryServiceImpl.create(tbContentCategory);
	}
	
	/**
	 * 根据传输来的id和name两个参数，修改内容类目(重命名)
	 * @param tbContentCategory
	 * @return
	 */
	@RequestMapping("content/category/update")
	@ResponseBody
	public EgoResult update(TbContentCategory tbContentCategory){
		return tbContentCategoryServiceImpl.update(tbContentCategory);
	}
	
	/**
	 * 根据传输来的id，删除内容类目
	 * @param tbContentCategory
	 * @return
	 */
	@RequestMapping("content/category/delete")
	@ResponseBody
	public EgoResult delete(TbContentCategory tbContentCategory){
		return tbContentCategoryServiceImpl.delete(tbContentCategory);
	}
	

}
