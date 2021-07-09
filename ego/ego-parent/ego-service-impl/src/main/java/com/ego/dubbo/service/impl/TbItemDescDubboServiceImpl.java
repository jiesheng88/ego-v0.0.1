package com.ego.dubbo.service.impl;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.pojo.TbItemDesc;

public class TbItemDescDubboServiceImpl implements TbItemDescDubboService{
	@Resource
	private TbItemDescMapper tbItemDescMapper;

	@Override
	public int insDesc(TbItemDesc tbItemDesc) {
		return tbItemDescMapper.insert(tbItemDesc);
	}

	@Override
	public TbItemDesc selById(long itemId) {
		// 可以查询出text文本的字段内容
		return tbItemDescMapper.selectByPrimaryKey(itemId);
	}

}
