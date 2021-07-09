package com.ego.item.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.item.pojo.ParamItem;
import com.ego.item.service.TbItemParamItemService;
import com.ego.pojo.TbItemParamItem;

@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService{
	@Reference
	private TbItemParamItemDubboService tbItemParamItemDubboServiceImpl;

	@Override
	public String showParam(long itemId) {
		TbItemParamItem tbItemParamItem = tbItemParamItemDubboServiceImpl.selByItemId(itemId);
		// 先空指针判断
		if (tbItemParamItem==null||tbItemParamItem.equals("")) {
			return null;
		}
		
		// 将paramData字符串(字符串相当于是json数据)转换成list数组
		String paramData = tbItemParamItem.getParamData();
		List<ParamItem> list = JsonUtils.jsonToList(paramData, ParamItem.class);
		System.out.println(list);
		
		// 利用StringBuffer画出表格来显示paramData内容
		StringBuffer sb = new StringBuffer();
		// 循环画表格：每个paramItem内容就是一个表格内容
		for (ParamItem paramItem : list) {
			sb.append("<table width='500' style='color:gray;'>"); // 画表格
			// 画每行数据:分为三行
			for (int i = 0; i < paramItem.getParams().size(); i++) {
				if (i==0) {
					sb.append("<tr>");
					// 居右显示
					sb.append("<td align='right' width='30%'>"+paramItem.getGroup()+"</td>");
					// 居右显示
					sb.append("<td align='right' width='30%'>"+paramItem.getParams().get(i).getK()+"</td>");
					// 居左显示(默认)
					sb.append("<td>"+paramItem.getParams().get(i).getV()+"</td>");
					sb.append("</tr>");
				}else {
					sb.append("<tr>");
					// 其余行第一列不显示内容
					sb.append("<td></td>");
					// 居右显示
					sb.append("<td align='right' width='30%'>"+paramItem.getParams().get(i).getK()+"</td>");
					// 居左显示(默认)
					sb.append("<td>"+paramItem.getParams().get(i).getV()+"</td>");
					sb.append("</tr>");
				}
			}
			sb.append("</table>");
			sb.append("<hr style='color:gray;'/>");
		}
		
		return sb.toString();
	}

}
