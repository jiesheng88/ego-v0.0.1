package com.ego.item.pojo;

import java.util.List;
/**
 * getDataService中的data（即门户导航页）最终要的数据格式
 * @author jie
 *
 */
public class PortalMenuNode {
	private String u;
	private String n;
	private List<Object> i;
	
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}
	public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	public List<Object> getI() {
		return i;
	}
	public void setI(List<Object> i) {
		this.i = i;
	}
}
