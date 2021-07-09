package com.ego.manage.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ego.commons.utils.FtpUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.manage.service.PicService;

@Service
public class PicServiceImpl implements PicService{
	// 从properties文件动态获取参数信息
	@Value("${ftpclient.host}")
	private String host;
	@Value("${ftpclient.port}")
	private int port;
	@Value("${ftpclient.username}")
	private String username;
	@Value("${ftpclient.password}")
	private String password;
	@Value("${ftpclient.basepath}")
	private String basePath;
	@Value("${ftpclient.filepath}")
	private String filePath;


	@Override
	public Map<String, Object> upload(MultipartFile file) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 拼接成新的文件名
		String oldName = file.getOriginalFilename();
		String filename = IDUtils.genImageName()+oldName.substring(oldName.lastIndexOf("."));
		
		
		boolean result = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename,
				file.getInputStream());

		if (result) {
			map.put("error", 0);
			map.put("url", "http://" + host + ":80" + filePath + filename);
		} else {
			map.put("error", 1);
			map.put("message", "图片上传失败~~~");
		}
		return map;
	}

}
