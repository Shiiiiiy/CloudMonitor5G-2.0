package com.datang.common.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 抽象工厂-生产原子语句等一些列产品
 * 
 * @author zhangchongfeng
 * 
 */
public class SqlAtomFactory {

	/**
	 * 系统默认工厂
	 */
	private static SqlAtomFactory sqlAtomFactory = null;

	/**
	 * 数据源
	 */
	protected DataSource dataSource;

	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public SqlAtomFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public SqlAtomFactory createFactory() {
		if (null == sqlAtomFactory) {
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				String sqlProductName = connection.getMetaData()
						.getDatabaseProductName();
				if (sqlProductName.equals("MySQL")) {
					sqlAtomFactory = new MySqlAtomFactory(dataSource);
				} else {
					sqlAtomFactory = new OracleAtomFactory(dataSource);
				}

			} catch (SQLException e) {
				sqlAtomFactory = new MySqlAtomFactory(dataSource);
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		return sqlAtomFactory;
	}

	/**
	 * 生产JDBCTemplate原子产品
	 * 
	 * @return
	 */
	public JdbcTemplateAtomProduct createJdbcTemplateAtomProduct() {
		return null;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
