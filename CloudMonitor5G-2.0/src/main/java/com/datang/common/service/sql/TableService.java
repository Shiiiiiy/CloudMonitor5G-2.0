package com.datang.common.service.sql;


public interface TableService {

	/**
	 * 1.first 获取TABLE NAME语句
	 * 
	 * @param param
	 * @return
	 */
	public String getTableName();

	/**
	 * @return the param
	 */
	public Object getParam();

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(Object param);

}
