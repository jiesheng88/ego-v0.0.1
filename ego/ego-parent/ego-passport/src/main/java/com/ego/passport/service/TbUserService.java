package com.ego.passport.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbUser;

public interface TbUserService {
	/**
	 * 登录
	 * @param user
	 * @return
	 */
	EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 通过token查询用户信息
	 * @param token
	 * @return
	 */
	EgoResult getUserInfoByToken(String token);
	
	/**
	 * 安全退出
	 * @param token
	 * @return
	 */
	EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response);

}
