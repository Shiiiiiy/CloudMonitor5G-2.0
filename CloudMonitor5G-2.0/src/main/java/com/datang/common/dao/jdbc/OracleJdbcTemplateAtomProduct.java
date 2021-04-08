package com.datang.common.dao.jdbc;

import javax.sql.DataSource;

public class OracleJdbcTemplateAtomProduct extends JdbcTemplateAtomProduct {

	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public OracleJdbcTemplateAtomProduct(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 构建分页查询语句，根据数据库类型不同会有所区别
	 * 
	 * @param sql
	 *            SQL
	 * @param startPosition
	 *            开始位置,oracle的ROWNUM从1开始
	 * @param needRowNum
	 *            需要查询的行数
	 * @return 查询语句
	 */
	public String buildPageSql(String sql, int startPosition, int needRowNum) {
		String pageSql = sql;
		// 如果二者有一个不大于0, 就当作全部查询操作
		if (startPosition > 0 && needRowNum > 0) {
			// Oracle
			pageSql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (" + sql
					+ ") A WHERE ROWNUM <= " + (startPosition + needRowNum - 1)
					+ ") WHERE RN >= " + startPosition;
		}
		return pageSql;
	}

}
