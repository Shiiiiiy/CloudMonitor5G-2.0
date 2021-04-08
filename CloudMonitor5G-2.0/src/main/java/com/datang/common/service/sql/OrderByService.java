package com.datang.common.service.sql;

/**
 * 
 * @author zhangchongfeng
 * 
 */
public interface OrderByService {
	
	/**
	 * 1.second 获取Order By语句
	 * 
	 * @param param
	 * @return
	 */
	public String getOrderBy();

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
