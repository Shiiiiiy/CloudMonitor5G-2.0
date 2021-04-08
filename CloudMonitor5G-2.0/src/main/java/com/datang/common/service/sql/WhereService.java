package com.datang.common.service.sql;

/**
 * @author zhangchongfeng
 */
public interface WhereService {

	/**
	 * 1.first 获取where语句
	 * 
	 * @param param
	 * @return
	 */
	public String getWhere();

	/**
	 * 2.second 获取门限指标条件的where条件
	 * 
	 * @param param
	 * @return
	 */
	public String getWhereByIndicationCondition();

	/**
	 * 3.second 获取过滤空指标的where条件
	 * 
	 * @param param
	 * @return
	 */
	public String getWhereByFilterEmptyKpi();
	/**
	 * 3.third 获取having语句
	 * 
	 * @param param
	 * @return
	 */
	public String getHaving();

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
