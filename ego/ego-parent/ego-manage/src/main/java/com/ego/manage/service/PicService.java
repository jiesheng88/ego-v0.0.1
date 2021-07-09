package com.ego.manage.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface PicService {
	/**
	 * 文件上传
	 * @param file	请求上传的文件
	 * @return
	 * @throws IOException 
	 */
	Map<String, Object> upload(MultipartFile file) throws IOException;

}

