package com.datang.common.dao.entity;

import java.util.List;

public interface ArrayResult<T> {
	/**
	 * @return the fields
	 */
	public List<String> getFields();

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<String> fields);

	/**
	 * @return the titles
	 */
	public List<String> getTitles();

	/**
	 * @param titles
	 *            the titles to set
	 */
	public void setTitles(List<String> titles);

	/**
	 * @return the arrayIndexs
	 */
	public List<Integer> getArrayIndexs();

	/**
	 * @param arrayIndexs
	 *            the arrayIndexs to set
	 */
	public void setArrayIndexs(List<Integer> arrayIndexs);

	/**
	 * @return the datas
	 */
	public List<T> getDatas();

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(List<T> datas);
	
	
	/**
	 * @return the originalFields
	 */
	public List<String> getOriginalFields();

	/**
	 * @param originalFields the originalFields to set
	 */
	public void setOriginalFields(List<String> originalFields);
}
