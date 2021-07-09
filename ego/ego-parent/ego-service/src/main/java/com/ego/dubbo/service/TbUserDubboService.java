package com.ego.dubbo.service;

import com.ego.pojo.TbUser;

public interface TbUserDubboService {
	/**
	 * 根据用户名和密码查询用户
	 * @param user
	 * @return
	 */
	TbUser selByUser(TbUser user);

}
