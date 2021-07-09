package com.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.mapper.TbContentCategoryMapper;
import com.ego.pojo.TbContentCategory;
import com.ego.pojo.TbContentCategoryExample;

public class TbContentCategoryDubboServiceImpl implements TbContentCategoryDubboService{
	@Resource
	private TbContentCategoryMapper tbContentCategoryMapper;

	@Override
	public List<TbContentCategory> selByPid(long pid) {
		// 设置查询条件
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(pid).andStatusEqualTo(1);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		return list;
	}

	@Override
	public int insByCategory(TbContentCategory tbContentCategory) {
		return tbContentCategoryMapper.insertSelective(tbContentCategory);
	}

	@Override
	public int updById(TbContentCategory tbContentCategory) {
		return tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
	}

	@Override
	public TbContentCategory selById(long id) {
		return tbContentCategoryMapper.selectByPrimaryKey(id);
	}

}
