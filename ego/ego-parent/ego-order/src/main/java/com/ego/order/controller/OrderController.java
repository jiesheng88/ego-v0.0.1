package com.ego.order.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.order.pojo.MyOrderParam;
import com.ego.order.service.TbOrderService;

@Controller
public class OrderController {
	@Resource
	private TbOrderService tbOrderServiceImpl;
	
	/**
	 * 显示核对订单页面
	 * @param ids
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("order/order-cart.html")
	public String showCartOrder(@RequestParam("id") List<Long> ids, HttpServletRequest request, Model model){
		List<TbItemChild> list = tbOrderServiceImpl.showOrderCart(ids, request);
		model.addAttribute("cartList", list);
		return "order-cart";
	}
	
	/**
	 * 创建订单
	 * @param param
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("order/create.html")
	public String createOrder(MyOrderParam param, HttpServletRequest request, Model model){
		EgoResult er = tbOrderServiceImpl.create(param, request);
		if (er.getStatus()==200) {
			return "my-orders";
		}else {
			model.addAttribute("message","订单创建失败");
			return "error/exception";
		}
	}
	
	
}
