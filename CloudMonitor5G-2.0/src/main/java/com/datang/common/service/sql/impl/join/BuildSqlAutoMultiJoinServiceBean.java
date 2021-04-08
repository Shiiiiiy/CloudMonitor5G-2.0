package com.datang.common.service.sql.impl.join;

/**
 * 组织SQL语句
 * 
 * @author shengyingchao
 * 
 */
public class BuildSqlAutoMultiJoinServiceBean extends
		SqlAutoMultiJoinServiceBean {

	/**
	 * @return the sql
	 */
	public String getSql() {

		initParam(param);

		buildSql();

		return sql;
	}

}
