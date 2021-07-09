package com.ego.dubbo.service;

import java.util.List;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;

public interface TbItemDubboService {
	/**
	 * 商品分页查询
	 * @param page	第几页
	 * @param rows	总页数
	 * @return
	 */
	EasyUIDataGrid show(int page, int rows);
	
	/**
	 * 根据id值修改商品的状态status：1-正常(上架)，2-下架，3-删除
	 * TbItem类中设置id和status值
	 * @param tbItem
	 * @return
	 */
	int updItemStatus(TbItem tbItem);
	
	/**
	 * 新增---商品前部分信息(未考虑回滚)
	 * @param tbItem
	 * @return
	 */
	int insTbItem(TbItem tbItem);
	
	/**
	 * 新增----包含商品表、商品描述表和商品规格参数(考虑事务回滚)
	 * 虽然插入信息要存入三个表中，但是要在一个接口中实现全部功能
	 * @param tbItem
	 * @param tbItemDesc
	 * @param tbItemParamItem
	 * @return
	 * @throws Exception
	 */
	int insTbItemAndDesc(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem tbItemParamItem) throws Exception;
	
	/**
	 * 根据状态查询全部可用数据
	 * @param status
	 * @return
	 */
	List<TbItem> selAllByStatus(byte status);
	
	/**
	 * 根据id值查询商品信息
	 * @param id
	 * @return
	 */
	TbItem selById(long id);
}
