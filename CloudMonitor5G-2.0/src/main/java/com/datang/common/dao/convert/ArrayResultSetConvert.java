/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.dao.convert;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Object[]与ResultSet的转换
 * 
 * @author wangshuzhen
 * @version 1.0.0
 */
public class ArrayResultSetConvert implements ResultSetConvert<Object[]> {
	/**
	 * 数组长度
	 */
	private int length = -1;

	/**
	 * 获取数组长度
	 * 
	 * @param rs ResultSet
	 * @return 数组长度
	 * @throws SQLException
	 */
	protected int getLength(ResultSet rs) throws SQLException {
		if (length <= 0) {
			ResultSetMetaData metaData = rs.getMetaData();
			length = metaData.getColumnCount();// 列数
		}

		return length;
	}

	/**
	 * ResultSet到Object[]的转换
	 * 
	 * @param rs ResultSet
	 * @return Object[]
	 * @throws SQLException
	 * @see com.datang.common.midware.dao.convert.ResultSetConvert#convert(java.sql.ResultSet)
	 */
	public Object[] convert(ResultSet rs) throws SQLException {
		int len = getLength(rs);
		Object[] objs = new Object[len];
		for (int i = 0; i < len; i++) {
			objs[i] = rs.getObject(i+1);
		}
		return objs;
	}

}
