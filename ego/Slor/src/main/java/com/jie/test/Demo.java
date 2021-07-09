package com.jie.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * 增加删除修改都需要提交事务
 * @author jie
 *
 */
public class Demo {
	/**
	 * 新增，修改同理
	 * 
	 * @throws IOException
	 * @throws SolrServerException
	 */
	@Test
	public void testInsert() throws SolrServerException, IOException {
		// 1、针对单个Solr，传入的参数是Solr的访问地址
//		SolrClient client = new HttpSolrClient("http://192.168.195.131:8080/solr/");
		// 1、针对集群版SolrCloud，传入的参数是zkHosts(zookeeper地址)
		// tomcat/bin/catalina.sh的最上面添加的-DzkHost=192.168.213.130:2181,192.168.213.130:2182,192.168.213.130:2183
		CloudSolrClient client = new CloudSolrClient("192.168.195.131:2181,192.168.195.131:2182,192.168.195.131:2183");
		client.setDefaultCollection("collection1");
		
		// 2、设置添加内容
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "003"); // id必须添加，最好使用字符串格式
		doc.addField("Chinese", "尚学堂Java机构");
		doc.addField("Chinese1", "尚硅谷Java机构");

		// 3、添加
		client.add(doc);
		// 4、事务提交
		client.commit();
		System.out.println("新增成功~~~");
	}

	/**
	 * 删除
	 * @throws SolrServerException
	 * @throws IOException
	 */

	public void testDelete() throws SolrServerException, IOException {
		// 1、针对单个Solr，传入的参数是Solr的访问地址
//		SolrClient client = new HttpSolrClient("http://192.168.195.131:8080/solr/");
		// 1、针对集群版SolrCloud
		CloudSolrClient client = new CloudSolrClient("192.168.195.131:2181,192.168.195.131:2182,192.168.195.131:2183");
		client.setDefaultCollection("collection1");
		
		// 2、删除
		// client.deleteById("001"); // 通过id删除
		client.deleteByQuery("*:*"); // 全部删除
		// 3、事务提交
		client.commit();
		System.out.println("删除成功~~~");
	}

	/**
	 * 查询
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	
	public void testQuery() throws SolrServerException, IOException {
		// 1、针对单个Solr，传入的参数是Solr的访问地址
//		SolrClient client = new HttpSolrClient("http://192.168.195.131:8080/solr/");
		// 1、针对集群版SolrCloud
		CloudSolrClient client = new CloudSolrClient("192.168.195.131:2181,192.168.195.131:2182,192.168.195.131:2183");
		client.setDefaultCollection("collection1");
		
		// 2、可视化界面左侧条件
		SolrQuery params = new SolrQuery();
		// 设置q
		params.setQuery("Chineseall:*");
		// 设置分页
		params.setStart(0);	// 从第几页开始查询，从0开始
		params.setRows(10);	// 查询显示几个
		
		// 设置高亮
		params.setHighlight(true);	// 启动高亮显示
		params.addHighlightField("Chinese");	// 设置高亮显示的字段
		params.setHighlightSimplePre("<span style='color:red'>");	// 设置高亮显示字段前置内容
		params.setHighlightSimplePost("</span>");	// 设置高亮显示字段后置内容

		// 3、查询
		// 相当于是点击查询按钮，本质上是，向Solr web服务器发生请求，并接受响应，query对象里面包含了返回的json数据
		QueryResponse response = client.query(params);
		
		// 获得高亮显示对象
		Map<String, Map<String, List<String>>> hh = response.getHighlighting();
		
		// 4、取出docs{}中的内容
		SolrDocumentList solrList = response.getResults();
		
		// 5、输出显示
		for (SolrDocument doc : solrList) {
			System.out.println("id:"+doc.getFieldValue("id"));
			System.out.println("Chinese(未高亮显示):"+doc.getFieldValue("Chinese"));
			// 读取高亮显示内容
			Map<String, List<String>> map = hh.get(doc.getFieldValue("id"));	// 获取的是第二层id包括的内容
			List<String> list = map.get("Chinese");		// 获取高亮字段的列表内容
			// list中内容有可能为空
			if (list!=null && list.size()>0) {
				System.out.println("Chinese(高亮显示):"+list.get(0));
			}else {
				System.out.println("没有高亮内容");
			}
			System.out.println("Chinese1:"+doc.getFieldValue("Chinese1"));
		}
	}

}
