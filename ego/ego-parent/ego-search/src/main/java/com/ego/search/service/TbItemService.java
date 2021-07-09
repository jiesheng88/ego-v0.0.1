package com.ego.search.service;

import java.util.Map;

import com.ego.pojo.TbItem;

public interface TbItemService {
	/**
	 *Solr数据初始化
	 * @throws Exception 
	 */
	void init() throws Exception;
	
	/**
	 * 分页显示
	 * @param query
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception 
	 */
	Map<String, Object> selByQuery(String query, int page, int rows) throws Exception;
	
	/**
	 * 新增的同时添加数据到Solr中
	 * @param map	httpclient中dopost中参数类型是map格式
	 * @param desc
	 * @return
	 * @throws Exception
	 */
	int add(Map<String, Object> map, String desc) throws Exception;

}
