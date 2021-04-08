package com.datang.common.service.sql;

import com.datang.common.service.sql.util.KpiUtilService;

public interface SelectService extends KpiUtilService{

	/**
	 * 1.third 获取select result 语句
	 * 
	 * @param param
	 * @return
	 */
	public String getSelectResult();

	/**
	 * 2.first 获取select语句
	 * 
	 * @param param
	 * @return
	 */
	public String getSelect();


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
	 * 获取汇总select语句
	 * @return the param
	 */
	public String getInfoTotal();

}
