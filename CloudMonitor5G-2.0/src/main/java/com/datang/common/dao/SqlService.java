package com.datang.common.dao;

import com.datang.common.dao.entity.PageListArray;


public interface SqlService {

	/**
	 * 根据参数 解析SQL语句
	 * 
	 * @return
	 */
	public PageListArray<Object[]> parse(Object param);

	/**
	 * @return the param
	 */
	public Object getParam();
	
	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(Object param);

	/**
	 * @return the sql
	 */
	public String getSql();

}
