package com.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.mapper.TbItemParamMapper;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbItemParamDubboServiceImpl implements TbItemParamDubboService {
	@Resource
	private TbItemParamMapper tbItemParamMapper;

	@Override
	public EasyUIDataGrid showPage(int page, int rows) {
		// 先设置分页条件
		PageHelper.startPage(page, rows);
		
		// 设置查询的SQL语句
		// XXXXExample()设置了什么,相当于在sql中where从句中添加了什么条件
		// 如果表中有一个或一个以上的列是text类型.逆向工程中生成的方法是xxxxxxxxWithBlobs()
		// 如果使用xxxxWithBlobs()，查询结果中包含带有text类的列；如果没有使用withblobs()方法，则查询结果中不带有text类型.
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
		
		// 根据程序员自己编写的SQL语句结合分页插件产生最终结果,封装到PageInfo
		PageInfo<TbItemParam> pageInfo = new PageInfo<>(list);
		
		// 设置方法返回结果
		EasyUIDataGrid easyUIDataGrid = new EasyUIDataGrid();
		easyUIDataGrid.setRows(pageInfo.getList());
		easyUIDataGrid.setTotal(pageInfo.getTotal());
		
		return easyUIDataGrid;
	}

	@Override
	public int delByIds(String ids) throws Exception {
		// 分割获取包含所有id的数组
		String[] idStrs = ids.split(",");
		
		// 遍历，依次删除(带事务回滚)
		int index = 0;
		try {
			for (String id : idStrs) {
				index += tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(id));
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		if (index == idStrs.length) {
			return 1;
		}else {
			throw new Exception("删除失败。可能原因是数据不存在~~~");
		}
	}

	@Override
	public TbItemParam selByItemCatId(long itemcatid) {
		TbItemParamExample example = new TbItemParamExample();
		// 设置查询条件
		example.createCriteria().andItemCatIdEqualTo(itemcatid);
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
		
		// 这里不太懂???
		if (list!=null && list.size()>0) {
			// 要求每个类目只能有一个模板
			return list.get(0);
			
		}
		return null;
	}

	@Override
	public int insItemParam(TbItemParam tbItemParam) {
		return tbItemParamMapper.insertSelective(tbItemParam);
	}

}
