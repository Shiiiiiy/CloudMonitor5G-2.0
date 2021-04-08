package com.datang.common.dao.jdbc;

import javax.sql.DataSource;


/**
 * 具体工厂B-Oracle工厂,生产Oracle原子语句等一些列产品
 * 
 * @author zhangchongfeng
 * 
 */
public class OracleAtomFactory extends SqlAtomFactory {

	/**
	 * 构造函数
	 * @param dataSource
	 */
	public OracleAtomFactory(DataSource dataSource) {
		super(dataSource);
	}
	
	/**
	 * 生产JDBCTemplate原子产品
	 * 
	 * @return
	 */
	public JdbcTemplateAtomProduct createJdbcTemplateAtomProduct(){
		return new OracleJdbcTemplateAtomProduct(dataSource);
	}

}
