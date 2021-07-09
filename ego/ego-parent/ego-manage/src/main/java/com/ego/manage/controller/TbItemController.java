package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;

@Controller
public class TbItemController {
	@Resource
	private TbItemService tbItemServiceImpl;
	
	/**
	 * 分页显示商品
	 * @param page 请求传过来的page，第几页
	 * @param rows 请求传过来的rows，总数据个数
	 * @return 传递给EasyUI的数据必须是EasyUIDataGrid对象
	 */
	@RequestMapping("item/list")
	@ResponseBody
	public EasyUIDataGrid show(int page,int rows){
		return tbItemServiceImpl.show(page, rows);
	}
	
	/**
	 * 修改商品
	 * @return
	 */
	@RequestMapping("rest/page/item-edit")
	public String edit() {
		return "item-edit";
	}
	
	/**
	 * 商品上架
	 * @param ids 接收到的数据
	 * @return
	 */
	@RequestMapping("rest/item/reshelf")
	@ResponseBody
	public EgoResult reshelf(String ids){
		EgoResult egoResult = new EgoResult();
		int index = tbItemServiceImpl.update(ids, (byte)1);
		if (index ==1) {
			egoResult.setStatus(200);
		}
		return egoResult;
	}
	
	/**
	 * 商品下架
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/instock")
	@ResponseBody
	public EgoResult instock(String ids){
		EgoResult egoResult = new EgoResult();
		int index = tbItemServiceImpl.update(ids, (byte)2);
		if (index ==1) {
			egoResult.setStatus(200);
		}
		return egoResult;
	}
	
	/**
	 * 商品删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/delete")
	@ResponseBody
	public EgoResult delete(String ids){
		EgoResult egoResult = new EgoResult();
		int index = tbItemServiceImpl.update(ids, (byte)3);
		if (index ==1) {
			egoResult.setStatus(200);
		}
		return egoResult;
	}
	
	/**
	 * 商品新增
	 * @param tbItem	接收商品其余信息
	 * @param desc	接收商品描述信息
	 * @param itemParams	接收商品规格参数信息
	 * @return
	 */
	@RequestMapping("item/save")
	@ResponseBody
	public EgoResult save(TbItem tbItem,String desc, String itemParams) {
		/* 未加事务回滚 */
//		EgoResult egoResult = new EgoResult();
//		int index = tbItemServiceImpl.insert(tbItem, desc);
//		if (index == 1) {
//			egoResult.setStatus(200);
//		}
//		return egoResult;
		
		/* 添加事务回滚 */
		EgoResult egoResult = new EgoResult();
		int index = 0;
		try {
			index = tbItemServiceImpl.insert(tbItem, desc, itemParams);
			if (index == 1) {
				egoResult.setStatus(200);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			egoResult.setData(e.getMessage());
			
		}
		return egoResult;
	}

}
