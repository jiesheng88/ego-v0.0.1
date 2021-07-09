package com.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.mapper.TbContentMapper;
import com.ego.pojo.TbContent;
import com.ego.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbContentDubboServiceImpl implements TbContentDubboService{
	@Resource
	private TbContentMapper tbContentMapper;

	@Override
	public EasyUIDataGrid selContentByPage(long categoryId, int page, int rows) {
		// 1、设置分页条件
		PageHelper.startPage(page, rows);
		
		// 2、设置查询条件
		TbContentExample example = new TbContentExample();
		// 判断categoryId是否为0，为零的话，不需要传参
		if (categoryId!=0) {
			example.createCriteria().andCategoryIdEqualTo(categoryId);
		}
		
		// 3、查询全部
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		
		// 4、分页查询
		PageInfo<TbContent> pi = new PageInfo<>(list);
		
		// 5、设置返回结果
		EasyUIDataGrid easyUIDataGrid = new EasyUIDataGrid();
		easyUIDataGrid.setRows(pi.getList());
		easyUIDataGrid.setTotal(pi.getTotal());
		
		return easyUIDataGrid;
	}

	@Override
	public int insByContent(TbContent tbContent) {
		return tbContentMapper.insertSelective(tbContent);
	}

	@Override
	public int updByContent(TbContent tbContent) {
		return tbContentMapper.updateByPrimaryKeyWithBLOBs(tbContent);
	}

	@Override
	public int delByid(long id) {
		return tbContentMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<TbContent> selByCount(int count, boolean isSort) {
		// 设置查询条件
		TbContentExample example = new TbContentExample();
		if (isSort) { // 排序
			// .createCriteria() 只相当于是设置查询条件的where内容，不包含排序查询
			example.setOrderByClause("updated desc");
		}
		
		if (count!=0) {
			// 利用分页查询来查询前n个信息
			PageHelper.startPage(1, count);
			List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
			PageInfo<TbContent> pi = new PageInfo<>(list);
			
			// 取出第一页内容，即可
			List<TbContent> listResult = pi.getList();
			
			return listResult;
		}else {
			return tbContentMapper.selectByExampleWithBLOBs(example);
		}
	}

}
