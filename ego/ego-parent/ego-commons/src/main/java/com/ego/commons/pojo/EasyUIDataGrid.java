package com.ego.commons.pojo;

import java.io.Serializable;
import java.util.List;

public class EasyUIDataGrid implements Serializable {
	// 当前显示页的数据
	private List<?> rows;
	// 总的数据个数
	private long total;
	
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
}
