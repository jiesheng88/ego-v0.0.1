package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;

@Controller
public class TbItemParamController {
	@Resource
	private TbItemParamService tbItemParamServiceImpl;
	
	/**
	 * 分页显示--商品规格参数
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("item/param/list")
	@ResponseBody
	public EasyUIDataGrid show(int page, int rows){
		return tbItemParamServiceImpl.show(page, rows);
	}
	
	/**
	 * 批量删除规格参数
	 * @param ids
	 * @return
	 */
	@RequestMapping("item/param/delete")
	@ResponseBody
	public EgoResult delete(String ids){
		int index = 0;
		EgoResult egoResult = new EgoResult();
		try {
			index = tbItemParamServiceImpl.delete(ids);
			if (index == 1) {
				egoResult.setStatus(200);
			}
		} catch (Exception e) {
			egoResult.setData(e.getMessage());
		}
		return egoResult;
	}
	
	/**
	 * 点击商品类目按钮显示添加分组按钮
	 * 判断类目是否已经添加到模板
	 * @param itemcatid
	 * @return
	 */
	@RequestMapping("item/param/query/itemcatid/{itemcatid}")
	@ResponseBody
	public EgoResult showParam(@PathVariable long itemcatid){
		return tbItemParamServiceImpl.showParam(itemcatid);
	}
	
	/**
	 * 商品类目新增
	 * @param tbItemParam	接收规格参数具体信息
	 * @param itemcatid		接收商品类目id
	 * @return
	 */
	@RequestMapping("item/param/save/{itemcatid}")
	@ResponseBody
	public EgoResult save(TbItemParam tbItemParam ,@PathVariable long itemcatid){
		tbItemParam.setItemCatId(itemcatid);
		return tbItemParamServiceImpl.insert(tbItemParam);
	}

}
