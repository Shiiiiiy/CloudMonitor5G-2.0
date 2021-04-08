/**
 * 
 */
package com.datang.common.action.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页返回的公用bean
 * 
 * @author yinzhipeng
 * @date:2015年6月12日 下午4:20:24
 * @version
 */
public abstract class AbstractPageList {
	private String total = "0";// 总记录的条数
	private List rows = new ArrayList();// 查询的结果集数据

	/**
	 * @return the totaltotal
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * @return the rowsrows
	 */
	public List getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List rows) {
		this.rows = rows;
	}

}
