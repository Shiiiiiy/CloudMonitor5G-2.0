package com.datang.common.dao.sql;

import com.datang.common.dao.entity.BeanResult;
import com.datang.common.dao.entity.PageListArray;

public interface PageOrTopService {

	// -----------------------------------PageListArray<Object[]>的分页和TOP处理------------------------------------------------------

	/**
	 * 1.third 获取分页或TOPN信息
	 * 
	 * @param param
	 * @return
	 */
	public PageListArray<Object[]> getPageTop(String sql);

	/**
	 * 不分页,不topN
	 * 
	 * @author chenzhiyong
	 * @return Dec 19, 2012
	 */
	public PageListArray<Object[]> getAllInfo(String sql);

	// -----------------------------------BeanResult<T>的分页和TOP处理------------------------------------------------------

	/**
	 * 1.third 获取分页或TOPN信息
	 * 
	 * @param param
	 * @return
	 */
	public <T> BeanResult<T> getPageTop(Class<T> entityClass, String sql);

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
