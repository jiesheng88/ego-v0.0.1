package com.ego.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.TbItemChild;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemCat;
import com.ego.pojo.TbItemDesc;
import com.ego.search.service.TbItemService;

@Service
public class TbItemServiceImpl implements TbItemService{
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;
	@Reference
	private TbItemDescDubboService tbItemDescDubboService;
	
	@Resource
	private CloudSolrClient solrClient;

	@Override
	public void init() throws Exception {
		// 1、查询出所有正常的商品信息
		List<TbItem> listItem = tbItemDubboServiceImpl.selAllByStatus((byte)1);
		
		// 2、遍历，查询出商品的类别和描述
		for (TbItem item : listItem) {
			// 商品类别信息
			TbItemCat tbItemCat = tbItemCatDubboServiceImpl.selById(item.getCid());
			// 商品描述信息
			TbItemDesc desc = tbItemDescDubboService.selById(item.getId());
			
			// 3、添加到Solr中
			// 因为要频繁创建CloudSolrClient对象，所以使用Spring配置
			SolrInputDocument docs = new SolrInputDocument();
			
			// 给Solr中的自定义字段赋值
			docs.setField("id", item.getId());
			docs.setField("item_title", item.getTitle());
			docs.setField("item_sell_point", item.getSellPoint());
			docs.setField("item_price", item.getPrice());
			docs.setField("item_image", item.getImage());
			docs.setField("item_category_name", tbItemCat.getName());
			docs.setField("item_desc", desc.getItemDesc());
			docs.setField("item_updated", item.getUpdated());
			
			solrClient.add(docs);
		}
		solrClient.commit();
	}

	@Override
	public Map<String, Object> selByQuery(String query, int page, int rows) throws Exception{
		
		/* 1、Solr查询 */
		// 可视化界面左侧条件
		SolrQuery param = new SolrQuery();
		
		// 设置查询条件q
		param.setQuery("item_keywords:"+query);	// 通过复合属性查询
		// 设置分页条件
		param.setStart((rows*(page-1)));
		param.setRows(rows);
		// 设置排序方式
		param.setSort("item_updated", ORDER.asc);
		// 设置高亮显示
		param.setHighlight(true);
		param.addHighlightField("item_title"); // 设置高亮字段
		param.setHighlightSimplePre("<span style='color:red'>");
		param.setHighlightSimplePost("</span>");
		
		// 查询
		QueryResponse response = solrClient.query(param);
		// 取出docs{}中的内容(未高亮显示内容)
		SolrDocumentList solrList = response.getResults();
		
		// 获得高亮显示对象
		Map<String, Map<String, List<String>>> hh = response.getHighlighting();
		
		/* 2、存储itemList内容 */
		List<TbItemChild> listChild = new ArrayList<TbItemChild>();
		// 遍历获取数据
		for (SolrDocument doc : solrList) {
			TbItemChild itemChild = new TbItemChild();
			
			// 设置id
			itemChild.setId(Long.parseLong(doc.getFieldValue("id").toString()));
			// 设置title，分为高亮显示和非高亮显示
			List<String> list = hh.get(doc.getFieldValue("id")).get("item_title");
			if (list!=null && list.size()>0) {
				// 高亮显示title
				itemChild.setTitle(list.get(0));
			}else {
				// 未高亮显示的title
				itemChild.setTitle(doc.getFieldValue("item_title").toString());
			}
			// 设置卖点
			itemChild.setSellPoint(doc.getFieldValue("item_sell_point").toString());
			// 设置价格
			itemChild.setPrice(Long.parseLong(doc.getFieldValue("item_price").toString()));
			// 设置图片
			String imsges = doc.getFieldValue("item_image").toString();
			// 当为空时，传入的数组
			itemChild.setImages(imsges==null||imsges.equals("")?new String[1]:imsges.split(","));
			
			listChild.add(itemChild);
		}
		
		/* 3、需要传输的三个参数存储到map中 */
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("itemList", listChild);
		resultMap.put("totalPages", solrList.getNumFound()%rows==0?solrList.getNumFound()/rows:solrList.getNumFound()/rows+1);
		
		return resultMap;
	}

	@Override
	public int add(Map<String, Object> map, String desc) throws Exception {
		// 给Solr中的自定义字段赋值
		SolrInputDocument docs = new SolrInputDocument();
		// 虽然直接存入的是对象，但是仍可以直接通过对象的属性直接取值
		docs.setField("id", map.get("id"));
		docs.setField("item_title", map.get("title"));
		docs.setField("item_sell_point", map.get("sellPoint"));
		docs.setField("item_price", map.get("price"));
		docs.setField("item_image", map.get("image"));
		docs.setField("item_category_name", tbItemCatDubboServiceImpl.selById((Integer)map.get("cid")).getName());
		docs.setField("item_desc", desc);
		
		UpdateResponse result = solrClient.add(docs);
		solrClient.commit();
		// 判断成功与否
		if (result.getStatus()==0) { // Solr成功添加的话，其Status为0
			return 1;
		}
		return 0;
	}

}
