package com.datang.common.dao.jdbc;

import javax.sql.DataSource;

public abstract class JdbcTemplateAtomProduct {

	/**
	 * 数据源
	 */
	protected DataSource dataSource;

	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public JdbcTemplateAtomProduct(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 构建分页查询语句，根据数据库类型不同会有所区别
	 * 
	 * @param sql
	 *            SQL
	 * @param startPosition
	 *            开始位置,从1开始
	 * @param needRowNum
	 *            需要查询的行数
	 * @return 查询语句
	 */
	public abstract String buildPageSql(String sql, int startPosition,
			int needRowNum);

}
