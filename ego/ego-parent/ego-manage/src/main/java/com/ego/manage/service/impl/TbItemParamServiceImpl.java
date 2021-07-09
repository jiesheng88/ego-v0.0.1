package com.ego.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.omg.CORBA.DATA_CONVERSION;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.manage.pojo.TbItemParamChild;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;

@Service
public class TbItemParamServiceImpl implements TbItemParamService{
	@Reference
	private TbItemParamDubboService tbItemParamDubboServiceImpl;
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;
	
	@Override
	public EasyUIDataGrid show(int page, int rows) {
		// 这里返回的easyUIDataGrid里每页显示的数据中只有TbItemParam该类中的数据，
		// 而该类中没有包含item_cat，只有item_cat_id，所以还需要通过item_cat_id查询到item_cat
		// 所以需要设置一个继承自TbItemParam的子类TbItemParamChild，该子类包含item_cat(itemCatName)
		EasyUIDataGrid easyUIDataGrid = tbItemParamDubboServiceImpl.showPage(page, rows);
		
		// 获取easyUIDataGrid中的List<TbItemParam>
		List<TbItemParam> tbItemParamList = (List<TbItemParam>) easyUIDataGrid.getRows();
		
		// 设置TbItemParam的子类TbItemParamChild
		List<TbItemParamChild> tbItemParamChildList = new ArrayList<TbItemParamChild>();
		
		// 通过遍历设置
		for (TbItemParam tbItemParam : tbItemParamList) {
			TbItemParamChild tbItemParamChild = new TbItemParamChild();
			
			// 通过item_cat_id查询到item_cat
			String itemCatName = tbItemCatDubboServiceImpl.selById(tbItemParam.getItemCatId()).getName();
			
			tbItemParamChild.setItemCatName(itemCatName);
			tbItemParamChild.setId(tbItemParam.getId());
			tbItemParamChild.setItemCatId(tbItemParam.getItemCatId());
			tbItemParamChild.setParamData(tbItemParam.getParamData());
			tbItemParamChild.setCreated(tbItemParam.getCreated());
			tbItemParamChild.setUpdated(tbItemParam.getUpdated());
			// 添加到列表中
			tbItemParamChildList.add(tbItemParamChild);
		}
		
		// 修改easyUIDataGrid中每页显示的内容list
		easyUIDataGrid.setRows(tbItemParamChildList);

		return easyUIDataGrid;
	}

	@Override
	public int delete(String ids) throws Exception {
		return tbItemParamDubboServiceImpl.delByIds(ids);
	}

	@Override
	public EgoResult showParam(long itemcatid) {
		EgoResult egoResult = new EgoResult();
		TbItemParam tbItemParam = tbItemParamDubboServiceImpl.selByItemCatId(itemcatid);
		
		if (tbItemParam!=null) {
			egoResult.setStatus(200);
			egoResult.setData(tbItemParam);
		}
		return egoResult;
	}

	@Override
	public EgoResult insert(TbItemParam tbItemParam) {
		EgoResult egoResult = new EgoResult();
		// 接收过来的tbItemParam，只有paramData，主键是自增的，其余信息需要自己补全
		Date date = new Date();
		tbItemParam.setCreated(date);
		tbItemParam.setUpdated(date);
		int index = tbItemParamDubboServiceImpl.insItemParam(tbItemParam);
		if (index>0) {
			egoResult.setStatus(200);
		}
		return egoResult;
	}

}
