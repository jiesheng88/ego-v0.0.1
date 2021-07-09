package com.ego.cart.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;

@Controller
public class CartController {
	@Resource
	private CartService cartServiceImpl;
	
	/**
	 * 添加到购物车
	 * @param id	商品id
	 * @param num	购物数量
	 * @param request
	 * @return
	 */
	@RequestMapping("cart/add/{id}.html")
	public String addCart(@PathVariable long id, int num, HttpServletRequest request){
		cartServiceImpl.addCart(id, num, request);
		return "cartSuccess";
	}
	
	/**
	 * 显示购物车，即结算页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("cart/cart.html")
	public String showCart(HttpServletRequest request, Model model){
		List<TbItemChild> list = cartServiceImpl.showCart(request);
		// 放入到请求域中
		model.addAttribute("cartList",list);
		return "cart";
	}
	
	/**
	 * 修改商品数量
	 * @param id
	 * @param num
	 * @param request
	 * @return
	 */
	@RequestMapping("cart/update/num/{id}/{num}.action")
	@ResponseBody
	public EgoResult update(@PathVariable long id, @PathVariable int num, HttpServletRequest request){
		return cartServiceImpl.update(id, num, request);
	}
	
	/**
	 * 删除购物车商品
	 * @param id
	 * @param num
	 * @param request
	 * @return
	 */
	@RequestMapping("cart/delete/{id}.action")
	@ResponseBody
	public EgoResult delete(@PathVariable long id, HttpServletRequest request){
		return cartServiceImpl.delete(id, request);
	}

}
