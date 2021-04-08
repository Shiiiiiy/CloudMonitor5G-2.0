/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.dao.convert;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link ResultSet}到<code>T<code>的转换
 * 
 * @author wangshuzhen
 * @version 1.0.0
 * 
 * @param <T>
 *            实体对象
 */
public interface ResultSetConvert<T> {
	/**
	 * 将ResultSet转换成实体T
	 * 
	 * @param rs
	 *            ResultSet
	 * @return T
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	T convert(ResultSet rs) throws SQLException, IllegalAccessException,
			InvocationTargetException;
}
