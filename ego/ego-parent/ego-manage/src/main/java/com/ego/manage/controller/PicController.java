package com.ego.manage.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ego.manage.service.PicService;

@Controller
public class PicController {
	@Resource
	private PicService picServiceImpl;
	
	/**
	 * 图片上传
	 * @param uploadFile	该参数名必须与jsp中参数名一致
	 * @return
	 */
	@RequestMapping("pic/upload")
	@ResponseBody
	public Map<String, Object> upload(MultipartFile uploadFile){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = picServiceImpl.upload(uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
			map.put("error", 1);
			map.put("message", "上传图片时服务器异常~~~");
		}
		return map;
	}

}
