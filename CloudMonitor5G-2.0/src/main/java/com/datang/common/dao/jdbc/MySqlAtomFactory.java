package com.datang.common.dao.jdbc;

import javax.sql.DataSource;

/**
 * 具体工厂A-MySql工厂,生产MySql原子语句等一些列产品
 * 
 * @author zhangchongfeng
 * 
 */
public class MySqlAtomFactory extends SqlAtomFactory {


	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public MySqlAtomFactory(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 生产JDBCTemplate原子产品
	 * 
	 * @return
	 */
	public JdbcTemplateAtomProduct createJdbcTemplateAtomProduct() {
		return new MySqlJdbcTemplateAtomProduct(dataSource);
	}

	

}
