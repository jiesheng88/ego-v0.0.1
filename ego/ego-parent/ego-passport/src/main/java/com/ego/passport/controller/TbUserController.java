package com.ego.passport.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EgoResult;
import com.ego.passport.service.TbUserService;
import com.ego.pojo.TbUser;

@Controller
public class TbUserController {
	@Resource
	private TbUserService tbUserServiceImpl;
	
	/**
	 * 显示登录界面
	 * 请求头中的Referer中包含跳转的URL
	 * @return
	 */
	@RequestMapping("user/showLogin")
	public String showLogin(@RequestHeader(value="Referer", defaultValue="") String url, Model model, String interurl){
		// 根据interurl来判断是否是购物车拦截
		if (interurl!=null && !interurl.equals("")) {
			model.addAttribute("redirect", interurl);
		}else if (!url.equals("")) {
			model.addAttribute("redirect", url);
		}
		return "login";
	}
	
	/**
	 * 点击登录按钮
	 * @param user
	 * @return
	 */
	@RequestMapping("user/login")
	@ResponseBody
	public EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response){
		return tbUserServiceImpl.login(user, request, response);
	}
	
	/**
	 * 通过token获取用户信息
	 * @param token
	 * @param callback	可选参数callback：如果有此参数表示此方法为jsonp请求，需要支持jsonp。
	 * @return
	 */
	@RequestMapping("user/token/{token}")
	@ResponseBody
	public Object getUserInfo(@PathVariable String token, String callback){
		EgoResult er = tbUserServiceImpl.getUserInfoByToken(token);
		if (callback!=null && !callback.equals("")) {
			MappingJacksonValue mjv = new MappingJacksonValue(er);
			mjv.setJsonpFunction(callback);
			return mjv;
		}
		return er;
	}
	
	/**
	 * 成功退出
	 * @param token
	 * @param callback	可选参数callback：如果有此参数表示此方法为jsonp请求，需要支持jsonp。
	 * @return
	 */
	@RequestMapping("user/logout/{token}")
	@ResponseBody
	public Object logout(@PathVariable String token, String callback, HttpServletRequest request, HttpServletResponse response){
		EgoResult er = tbUserServiceImpl.logout(token, request, response);
		if (callback!=null && !callback.equals("")) {
			MappingJacksonValue mjv = new MappingJacksonValue(er);
			mjv.setJsonpFunction(callback);
			return mjv;
		}
		return er;
	}

}
