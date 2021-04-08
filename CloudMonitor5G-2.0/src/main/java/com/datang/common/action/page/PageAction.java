/**
 * 
 */
package com.datang.common.action.page;

import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 分页公用action 子类继承该类是需在页面设置rows--每页显示的记录条数,page--查询的当前页码,orderDirection--
 * 正序或者倒序,orderField--按那个字段排序,且复写doPageQuery方法解析自定义参数和根据需要返回AbstractPageList的子类型
 * 
 * @author yinzhipeng
 * @date:2015年6月12日 下午3:58:34
 * @version
 */
@SuppressWarnings("all")
public abstract class PageAction extends ActionSupport {
	private int rows;// 每页显示的记录条
	private int page;// 查询的当前页码
	private String orderDirection;// 哪种顺序排序,正序或者倒序
	private String orderField;// 按那个字段排序

	/**
	 * 公用分页Action,返回Json类型的分页数据
	 * 页面需设置rows--每页显示的记录条数,page--查询的当前页码,orderDirection--
	 * 正序或者倒序,orderField--按那个字段排序 ,请在你的Struts2的配置文件中配置类型为json,名称为json的result结果集
	 * 
	 * 
	 * @return json
	 */
	public String doPageListJson() {
		PageList pageList = new PageList();
		pageList.setRowsCount(rows);
		pageList.setPageNum(page);
		if (orderDirection != null && orderDirection.length() != 0) {
			pageList.setOrderDirection(orderDirection);
		}
		if (orderField != null && orderField.length() != 0) {
			pageList.setOrderField(orderField);
		}
		AbstractPageList doPageQuery = doPageQuery(pageList);
		ActionContext.getContext().getValueStack().push(doPageQuery);
		return ReturnType.JSON;
	}

	/**
	 * 公用分页Action,非json类型返回,返回子类自定义的list对应的结果集,分页结果集类型已经压入值栈
	 * 请在你的Struts2的配置文件中配置名称为list的result结果集
	 * 
	 * @return 'list'
	 */
	public String doPageList() {
		doPageListJson();
		return ReturnType.LIST;
	}

	/**
	 * 该方法传入PageList对象,PageList中封装了页面公用的查询参数(rows:每页显示的记录条,page:查询的当前页码,
	 * orderDirection:正序或者倒序,orderField:按那个字段排序),根据需要返回AbstractPageList的子类型
	 * 子类必须覆盖此方法,子类需自己实现自定义的页面查询条件的封装
	 * 
	 * @param pageList
	 * @return AbstractPageList 已经包含有查询结果的PagedQuery对象
	 */
	public abstract AbstractPageList doPageQuery(PageList pageList);

	/**
	 * @return the rowsrows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the pagepage
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(int page) {
		this.page = page;
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

}
