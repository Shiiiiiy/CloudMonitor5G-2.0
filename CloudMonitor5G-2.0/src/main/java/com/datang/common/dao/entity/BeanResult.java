package com.datang.common.dao.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体Bean的数据结果集.数据形式是实体Bean
 * 
 * @author zhangchongfeng
 * 
 * @param <T>
 */
public class BeanResult<T> extends Page<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6343177151528521271L;

	/**
	 * 结果中的数据集
	 */
	protected List<T> datas = new ArrayList<T>();

	/**
	 * 数据的显示属性的英文名
	 */
	protected List<String> fields = new ArrayList<String>();

	/**
	 * 数据的隐藏属性名的英文名
	 */
	protected List<String> hiddenFields = new ArrayList<String>();

	/**
	 * 数据的显示属性的中文列名
	 */
	protected List<String> titles = new ArrayList<String>();

	/**
	 * @return the datas
	 */
	public List<T> getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	/**
	 * @return the fields
	 */
	public List<String> getFields() {
		return fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	/**
	 * @return the hiddenFields
	 */
	public List<String> getHiddenFields() {
		return hiddenFields;
	}

	/**
	 * @param hiddenFields
	 *            the hiddenFields to set
	 */
	public void setHiddenFields(List<String> hiddenFields) {
		this.hiddenFields = hiddenFields;
	}

	/**
	 * @return the titles
	 */
	public List<String> getTitles() {
		return titles;
	}

	/**
	 * @param titles
	 *            the titles to set
	 */
	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

}
