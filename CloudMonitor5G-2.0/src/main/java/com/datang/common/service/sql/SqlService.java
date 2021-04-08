package com.datang.common.service.sql;

import com.datang.common.dao.entity.PageListArray;

/**
 * 封装SQL的公共service
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 上午9:33:02
 * @version
 */
public interface SqlService {

	/**
	 * 根据参数 解析SQL语句
	 * 
	 * @return
	 */
	public PageListArray<Object[]> parse(Object param);

	/**
	 * 获取参数
	 * 
	 * @return the param
	 */
	public Object getParam();

	/**
	 * 设置参数
	 * 
	 * @param param
	 *            the param to set
	 */
	public void setParam(Object param);

	/**
	 * 获取SQL
	 * 
	 * @return the sql
	 */
	public String getSql();

}
