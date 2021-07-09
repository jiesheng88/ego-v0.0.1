package com.ego.item.pojo;

import java.util.List;
/*
 * [{"group":"主体","params":[{"k":"品牌","v":"苹果（Apple）"},{"k":"型号","v":"iPhone 6 A1586"}]},
 	{"group":"网络","params":[{"k":"4G网络制式","v":"移动4G(TD-LTE)/联通4G(FDD-LTE)/电信4G(FDD-LTE)"}]}]
 */

public class ParamItem {
	private String group;
	private List<ParamItemNode> params;
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<ParamItemNode> getParams() {
		return params;
	}
	public void setParams(List<ParamItemNode> params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return "ParamItem [group=" + group + ", params=" + params + "]";
	}
	
}
