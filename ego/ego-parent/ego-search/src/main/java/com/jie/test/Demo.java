package com.jie.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Demo {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// 由这个类，负责发送请求及解析响应
		CloseableHttpClient client = HttpClients.createDefault();
		
		// 设置访问的URL
		String url = "http://localhost:9082/demo";
		HttpPost post = new HttpPost(url);
		
		// 设置请求参数
		// NameValuePair	参数实体类，所有参数的内容全都放到这个类中，一个参数就是一个对象
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair e = new BasicNameValuePair("name","张三");
		params.add(e);
		
		// 把请求参数设置到HttpPost中
		HttpEntity entity = new UrlEncodedFormEntity(params,"utf-8");
		post.setEntity(entity);
		
		// 获取浏览器响应
		CloseableHttpResponse response = client.execute(post);
		
		// 获取响应体
//		response.getEntity();
		
		// 使用工具类把响应体中的内容转换为字符串，方便输出
		String str = EntityUtils.toString(response.getEntity());
		System.out.println(str);
	}

}
