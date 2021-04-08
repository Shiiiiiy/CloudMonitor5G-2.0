/**
 * 
 */
package com.datang.common.action.page;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页返回Bean
 * 
 * @author yinzhipeng
 * @date:2015年6月12日 下午4:26:31
 * @version
 */
@SuppressWarnings("all")
@Component
public class PageList extends AbstractPageList {
	private int rowsCount;// 每页显示的记录条
	private int pageNum;// 查询的当前页码
	private int totalPage;// 总页数
	private String orderDirection;// 哪种顺序排序,正序或者倒序
	private String orderField;// 按那个字段排序
	private Map paramsMap = new HashMap();// 返回的查询参数,请将查询参数放置于map中,分页回显时从该map中取

	public Object putParam(String key, Object value) {
		return paramsMap.put(key, value);
	}

	public Object getParam(String key) {
		return paramsMap.get(key);
	}

	/**
	 * @return the rowsCountrowsCount
	 */
	public int getRowsCount() {
		return rowsCount;
	}

	/**
	 * @param rowsCount
	 *            the rowsCount to set
	 */
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	/**
	 * @return the pageNumpageNum
	 */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * @param pageNum
	 *            the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @return the totalPagetotalPage
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @param totalPage
	 *            the totalPage to set
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * @return the orderDirectionorderDirection
	 */
	public String getOrderDirection() {
		return orderDirection;
	}

	/**
	 * @param orderDirection
	 *            the orderDirection to set
	 */
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	/**
	 * @return the orderFieldorderField
	 */
	public String getOrderField() {
		return orderField;
	}

	/**
	 * @param orderField
	 *            the orderField to set
	 */
	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	/**
	 * @return the paramsMapparamsMap
	 */
	public Map getParamsMap() {
		return paramsMap;
	}

	/**
	 * @param paramsMap
	 *            the paramsMap to set
	 */
	public void setParamsMap(Map paramsMap) {
		this.paramsMap = paramsMap;
	}

}
