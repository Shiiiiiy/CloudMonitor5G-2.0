package com.datang.common.service.sql.impl;

/**
 * 组织SQL语句
 * 
 * @author zhangchongfeng
 * 
 */
public abstract class BuildSqlServiceBean extends SqlServiceBean {

	/**
	 * 
	 */
	public BuildSqlServiceBean() {
		super();
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		if (null == sql || 0 == sql.trim().length()) {
			initParam(param);
			buildSql();
		}
		return sql;
	}

}
