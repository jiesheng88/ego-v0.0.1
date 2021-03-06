package com.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.pojo.TbItemParamItem;
import com.ego.pojo.TbItemParamItemExample;

public class TbItemParamItemDubboServiceImpl implements TbItemParamItemDubboService{
	@Resource
	private TbItemParamItemMapper tbItemParamItemMapper;

	@Override
	public TbItemParamItem selByItemId(long itemId) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		example.createCriteria().andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
		// 因为是通用方法，返回结果都是list，如果明确返回结果只有一个，就用get(0)来获取
		if (list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
