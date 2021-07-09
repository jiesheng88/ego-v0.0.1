package com.ego.search.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.search.service.TbItemService;

@Controller
public class TbItemController {
	@Resource
	private TbItemService tbItemServiceImpl;

	/**
	 * Solr数据初始化 
	 * produces="text/html;charset=utf-8"	使结果可以展示中文
	 * @return
	 */
	@RequestMapping(value="solr/init",produces="text/html;charset=utf-8")
	@ResponseBody
	public String init(){
		long start=System.currentTimeMillis();
		try {
			tbItemServiceImpl.init();
			long end=System.currentTimeMillis();
			return "总花费时间："+(end-start)/1000+"秒";
		} catch (Exception e) {
			e.printStackTrace();
			return "Solr数据初始化失败~~~";
		}
	}
	
	/**
	 * 搜索功能
	 * @param model
	 * @param q
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("search.html")
	public String search(Model model, String q,@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="12") int rows){
		try {
			q = new String(q.getBytes("iso-8859-1"), "utf-8");
			Map<String, Object> map = tbItemServiceImpl.selByQuery(q, page, rows);
			model.addAttribute("query", q);
			model.addAttribute("itemList", map.get("itemList"));
			model.addAttribute("totalPages", map.get("totalPages"));
			model.addAttribute("page", page);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "search";
	}
	
	/**
	 * 新增
	 * @RequestBody 因为请求发过来的是json数据格式,把请求体中流数据转换为指定类型
	 * @param tbItem
	 * @return
	 */
	@RequestMapping("solr/add")
	@ResponseBody
	public int add(@RequestBody Map<String, Object> map){
		System.out.println(map);
		System.out.println(map.get("item"));
		try {
			// 因为map.get("tbItem")的格式是Object，需要传入的参数格式是Map<String, Object>，强转成map格式
			return tbItemServiceImpl.add((LinkedHashMap<String, Object>)map.get("tbItem"), map.get("desc").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
