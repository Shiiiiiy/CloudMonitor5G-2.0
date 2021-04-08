/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.dao.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询的结果
 * 
 * @author wangshuzhen
 * @version 1.0.0
 * 
 * @param T 查询结果实体
 */
public class Page<T> implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3311904962439771435L;

	/**
	 * 当前页数
	 */
	private int curPage;

	/**
	 * 总页数
	 */
	private int pageCount;

	/**
	 * 每页记录数
	 */
	private int pageSize;

	/**
	 * 总记录数
	 */
	private int recordCount;

	/**
	 * 当前页查询结果
	 */
	private List<T> resultList = new ArrayList<T>();

	/**
	 * @return the curPage
	 */
	public int getCurPage() {
		return curPage;
	}

	/**
	 * @param curPage the curPage to set
	 */
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the recordCount
	 */
	public int getRecordCount() {
		return recordCount;
	}

	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	/**
	 * @return the resultList
	 */
	public List<T> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList the resultList to set
	 */
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
}
