package com.ego.passport.service.impl;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbUserDubboService;
import com.ego.passport.service.TbUserService;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;

@Service
public class TbUserServiceImpl implements TbUserService{
	@Reference
	private TbUserDubboService tbUserDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Override
	public EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response) {
		TbUser userSelected = tbUserDubboServiceImpl.selByUser(user);
		EgoResult er = new EgoResult();
		if (userSelected!=null) {
			// 当用户登录成功后把用户信息放入到redis中
			String key = UUID.randomUUID().toString();
			jedisDaoImpl.set(key, JsonUtils.objectToJson(userSelected));
			jedisDaoImpl.expire(key, 60*60*24*7);  // 七天有效期
			
			// 设置Cookie（token为key，Redis的key为value）
			CookieUtils.setCookie(request, response, "TT_TOKEN", key, 60*60*24*7);
			
			er.setStatus(200);
			er.setMsg("OK");
			er.setData(key); // 返回token的值UUID
		}else {
			er.setMsg("用户名或密码错误");
		}
		return er;
	}

	@Override
	public EgoResult getUserInfoByToken(String token) {
		// 通过token这个key从Redis中获取用户信息
		String value = jedisDaoImpl.get(token);
		EgoResult er = new EgoResult();
		if (value!=null && !value.equals("")) {
			TbUser user = JsonUtils.jsonToPojo(value, TbUser.class);
			// 为了保证安全性，将查出来的用户密码设为空
			user.setPassword("");
			
			er.setStatus(200);
			er.setMsg("OK");
			er.setData(user);
		}else {
			er.setMsg("获取用户信息失败");
		}
		return er;
	}

	@Override
	public EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response) {
		// 删除Redis中的key
		jedisDaoImpl.del(token);
		// 删除Cookie
		CookieUtils.deleteCookie(request, response, "TT_TOKEN");
		
		EgoResult er = new EgoResult();
		er.setStatus(200);
		er.setMsg("OK");
		return er;
	}
}
