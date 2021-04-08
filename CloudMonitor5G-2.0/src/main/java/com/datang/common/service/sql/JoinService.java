package com.datang.common.service.sql;

public interface JoinService {

	public final static String LEFT_JOIN = "left join";
	public final static String RIGHT_JOIN = "right join";

	/**
	 * Join两个表的sql语句
	 * 
	 * @param sql1
	 * @param selectResult1
	 * @param alias1
	 * @param sql2
	 * @param selectResult2
	 * @param alias2
	 * @param joinMode
	 * @return
	 */
	public String getJoin(String sql1, String selectResult1, String alias1,
			String sql2, String selectResult2, String alias2, String joinMode);

	/**
	 * @return the param
	 */
	public Object getParam();

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(Object param);

}
