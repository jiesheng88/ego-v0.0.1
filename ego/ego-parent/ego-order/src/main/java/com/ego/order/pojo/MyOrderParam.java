package com.ego.order.pojo;

import java.util.List;

import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;

public class MyOrderParam {
	// 接收请求中的两个单独参数
	private int paymentType;
	private String payment;
	// 使用列表接收orderItems参数
	private List<TbOrderItem> orderItems;
	// 使用对象接收orderShipping参数
	private TbOrderShipping orderShipping;
	
	public int getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	@Override
	public String toString() {
		return "MyOrderParam [paymentType=" + paymentType + ", payment=" + payment + ", orderItems=" + orderItems
				+ ", orderShipping=" + orderShipping + "]";
	}
	
}
