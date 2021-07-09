package com.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.mapper.TbItemMapper;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemExample;
import com.ego.pojo.TbItemParamItem;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbItemDubboServiceImpl implements TbItemDubboService {
	@Resource
	private TbItemMapper tbItemMapper;
	@Resource
	private TbItemDescMapper tbItemDescMapper;
	@Resource
	private TbItemParamItemMapper tbItemParamItemMapper;

	@Override
	public EasyUIDataGrid show(int page, int rows) {
		// 1.必须先设置分页条件
		PageHelper.startPage(page, rows);
		
		// 2.查询全部数据
		List<TbItem> list = tbItemMapper.selectByExample(new TbItemExample());
		
		// 3.开始分页
		/* 利用PageInfo来获取rows和total，PageInfo必须传入查询的全部数据list
		 * PageInfo的生成参数中包含了总记录数total和每页显示数据的结果集list
		 * */
		PageInfo<TbItem> pi = new PageInfo<>(list);
		
		// 4.放入到EasyUIDataGrid实体类，并返回
		EasyUIDataGrid easyUIDataGrid = new EasyUIDataGrid();
		easyUIDataGrid.setRows(pi.getList());
		easyUIDataGrid.setTotal(pi.getTotal());
		
		return easyUIDataGrid;
	}

	@Override
	public int updItemStatus(TbItem tbItem) {
		return tbItemMapper.updateByPrimaryKeySelective(tbItem);
	}

	@Override
	public int insTbItem(TbItem tbItem) {
		return tbItemMapper.insert(tbItem);
	}

	@Override
	public int insTbItemAndDesc(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem tbItemParamItem) throws Exception {
		int index = 0;
		try {
			// 新增商品表中数据
			index = tbItemMapper.insertSelective(tbItem);
			// 新增商品描述中数据
			index += tbItemDescMapper.insertSelective(tbItemDesc);
			// 新增商品对应的商品规格参数信息
			index += tbItemParamItemMapper.insertSelective(tbItemParamItem);
		} catch (Exception e) {
			e.printStackTrace();  // 开发时，该行代码留着；上线时，该行代码注释掉
		}
		
		if (index==3) {
			return 1;
		}else {
			throw new Exception("新增失败，数据回滚还原~~~");
		}
	}

	@Override
	public List<TbItem> selAllByStatus(byte status) {
		TbItemExample example = new TbItemExample();
		example.createCriteria().andStatusEqualTo(status);
		return tbItemMapper.selectByExample(example);
	}

	@Override
	public TbItem selById(long id) {
		return tbItemMapper.selectByPrimaryKey(id);
	}

}
