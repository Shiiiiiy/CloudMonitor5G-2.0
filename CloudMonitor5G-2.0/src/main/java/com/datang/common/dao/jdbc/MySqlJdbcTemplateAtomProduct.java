package com.datang.common.dao.jdbc;

import javax.sql.DataSource;

public class MySqlJdbcTemplateAtomProduct extends JdbcTemplateAtomProduct {

	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public MySqlJdbcTemplateAtomProduct(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 构建分页查询语句，根据数据库类型不同会有所区别
	 * 
	 * @param sql
	 *            SQL
	 * @param startPosition
	 *            开始位置,mysql的limit起始行从0开始,故需要减1
	 * @param needRowNum
	 *            需要查询的行数
	 * @return 查询语句
	 */
	public String buildPageSql(String sql, int startPosition, int needRowNum) {
		String pageSql = sql;
		// 如果二者有一个不大于0, 就当作全部查询操作
		if (startPosition > 0 && needRowNum > 0) {
			pageSql = sql + " limit " + (startPosition-1) + " , " + needRowNum;
		}
		return pageSql;
	}

}
