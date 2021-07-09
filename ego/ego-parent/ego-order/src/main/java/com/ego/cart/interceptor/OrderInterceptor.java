package com.ego.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;

public class OrderInterceptor implements HandlerInterceptor{
	/**
	 * 只有当Redis中token对应的对象的status为200时，才放行；其余情况，全部拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 获取Cookie中的值
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		if (token!=null && !token.equals("")) {
			// 通过ego-passport中控制器，通过token获取用户信息
			String json = HttpClientUtil.doPost("http://localhost:8084/user/token/"+token);
			EgoResult er = JsonUtils.jsonToPojo(json, EgoResult.class);
			// 仅status为200时才放行
			if (er.getStatus()==200) {
				return true;
			}
		}
		
		// 拦截后，要跳转到指定页面 
		// 传过去的参数interurl是要跳转到的URL
		// 点击加入购物车时的URL：http://localhost:8085/cart/add/1063013.html(即request.getRequestURL()获得的URL)
		// 加入购物车成功时的URL：http://localhost:8085/cart/add/1063013.html?num=5
		String num = request.getParameter("num");
		if (num!=num && !num.equals("")) {
			// 购物车要跳转的页面
			StringBuffer interurl = request.getRequestURL().append("?num=").append(num);
			response.sendRedirect("http://localhost:8084/user/showLogin?interurl="+interurl);
		}else {
			// 提交订单要跳转的页面
			StringBuffer interurl = request.getRequestURL();
			response.sendRedirect("http://localhost:8084/user/showLogin?interurl="+interurl);
		}
//		System.out.println("interurl:"+interurl);
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
