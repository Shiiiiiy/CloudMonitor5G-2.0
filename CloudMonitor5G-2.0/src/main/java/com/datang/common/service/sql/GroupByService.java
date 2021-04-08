package com.datang.common.service.sql;

public interface GroupByService {

	/**
	 * 1.first 获取group by语句
	 * 
	 * @param param
	 * @return
	 */
	public String getGroupBy();
	
	/**
	 * 3.third 获取group by语句
	 * 
	 * @param param
	 * @return
	 */
	public String getGroupByResult();

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
