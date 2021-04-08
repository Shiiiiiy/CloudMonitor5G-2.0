/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.dao.convert;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import oracle.sql.CLOB;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 缺省实体转换类
 * 
 * @author wangshuzhen
 * @version 1.0.0
 * 
 * @param T
 *            实体
 */
public class DefaultResultSetConvert<T> implements ResultSetConvert<T> {
	/**
	 * 实体类
	 */
	private Class<T> entityClass;

	/**
	 * 列名，不能直接使用，需从{@link #getColumnNames(ResultSet)}获取
	 */
	protected String[] columnNames;

	/**
	 * 数据库字段与实体属性名的映射关系
	 */
	protected Map<String, String> propertyMap = new HashMap<String, String>();

	/**
	 * 构造函数
	 * 
	 * @param entityClass
	 *            实体类
	 */
	public DefaultResultSetConvert(Class<T> entityClass) {
		this.entityClass = entityClass;

		setAllProperties(entityClass);
	}

	/**
	 * 获取列名
	 * 
	 * @param rs
	 *            ResultSet
	 * @return 列名　
	 * @throws SQLException
	 */
	private String[] getColumnNames(ResultSet rs) throws SQLException {
		if (columnNames == null) {
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();// 列数
			columnNames = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				columnNames[i] = metaData.getColumnName(i + 1).toUpperCase();
			}
		}
		return columnNames;
	}

	/**
	 * 将ResultSet转换成实体对象，根据数据库列名与实体字段名的对应关系赋值
	 * 
	 * @param rs
	 *            ResultSet
	 * @return T
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @see com.datang.common.midware.dao.convert.ResultSetConvert#convert(java.sql.ResultSet)
	 */
	public T convert(ResultSet rs) throws SQLException, IllegalAccessException,
			InvocationTargetException {
		T object = null;
		try {
			object = (T) entityClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid class: " + entityClass,
					e);
		}

		String[] columns = getColumnNames(rs);
		for (String name : columns) {
			Object value = rs.getObject(name);

			if (((object.getClass().toString().endsWith("LteNeGroup") || object
					.getClass().toString().endsWith("IndicationKpi4g")) && name
					.equals("NES"))
					|| (object.getClass().toString().endsWith("Signaling4g") && name
							.equals("PAYLOAD"))
					|| (object.getClass().toString().endsWith("CdlEventItem") && name
							.equals("MSG"))) {
				String str = "";
				StringBuffer buf = new StringBuffer();
				try {
					BufferedReader in = new BufferedReader(
							((CLOB) value).getCharacterStream());
					while (str != null) {
						str = in.readLine();
						if (str != null) {
							if (object.getClass().toString()
									.endsWith("LteNeGroup")) {
								buf.append(str);
							} else {
								buf.append(str + "\n");
							}
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				value = buf.toString();
			}

			String propName = propertyMap.get(name);
			if (value != null && propName != null) {
				BeanUtils.setProperty(object, propName, value);
			}
		}
		return object;
	}

	/**
	 * 设置类与父类的属性
	 * 
	 * @param clazz
	 *            类
	 */
	private void setAllProperties(Class<?> clazz) {
		Class<?> parentClazz = clazz.getSuperclass();
		if (parentClazz != null) {
			setAllProperties(parentClazz);
		}
		setProperties(clazz);
	}

	/**
	 * 缓存类属性
	 * 
	 * @param clazz
	 *            class
	 */
	private void setProperties(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String propName = field.getName();
			propertyMap.put(propName.toUpperCase(), propName);
		}
	}
}
