/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.dao.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.datang.common.dao.convert.ArrayResultSetConvert;
import com.datang.common.dao.convert.DefaultResultSetConvert;
import com.datang.common.dao.convert.ResultSetConvert;
import com.datang.common.dao.entity.BeanResult;
import com.datang.common.dao.entity.Page;
import com.datang.common.dao.entity.PageListArray;
import com.datang.common.dao.entity.TableInfo;
import com.datang.common.dao.exception.DaoException;
import com.datang.common.util.Assert;
import com.datang.common.util.IOUtils;

/**
 * JDBC模板类，提供通用JDBC操作
 * 
 * @author wangshuzhen
 * @version 1.0.0
 */
public class JdbcTemplate {

	/**
	 * 数据源
	 */
	private DataSource dataSource;

	/**
	 * jdbc 原子
	 */
	private static JdbcTemplateAtomProduct jdbcAtom;

	/**
	 * 从数据库每次取得的最大记录数,默认不限制
	 */
	private int fetchSize = -1;

	/**
	 * 构造函数1
	 * 
	 * @param ds
	 *            数据源
	 */
	public JdbcTemplate(DataSource ds) {
		setDataSource(ds);
		if (null == jdbcAtom)
			jdbcAtom = new SqlAtomFactory(dataSource).createFactory()
					.createJdbcTemplateAtomProduct();
	}

	/**
	 * 构造函数2
	 * 
	 * @param dataSource
	 *            数据源
	 */
	public JdbcTemplate() {
	}

	/**
	 * 执行insert,update,delete等语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 执行完影响的记录数
	 * @throws DaoException
	 */
	public int execute(String sql) throws DaoException {
		Assert.notNull(sql, "argument sql is null");

		Connection con = null;
		Statement stmt = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			int ret = stmt.executeUpdate(sql);
			return ret;
		} catch (SQLException e) {
			throw new DaoException("excute failed: " + sql, e);
		} finally {
			IOUtils.close(con, stmt, null);
		}
	}

	/**
	 * 执行不返回ResultSet的SQL语句,如insert,update,delete
	 * 
	 * @param sql
	 *            用于PreparedStatement的执行的SQL语句，含参数
	 * @param params
	 *            参数数组,不能为空
	 * @return 执行完影响的记录数
	 * @throws DaoException
	 */
	public int execute(String sql, Object[] params) throws DaoException {
		Assert.notNull(sql, "argument sql is null");
		Assert.notNull(params, "argument params is null");

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			for (int i = 0, c = params.length; i < c; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			int ret = pstmt.executeUpdate();
			return ret;
		} catch (SQLException e) {
			throw new DaoException("excute failed: " + sql, e);
		} finally {
			IOUtils.close(con, pstmt, null);
		}
	}

	/**
	 * 批量执行更新修改操作,默认一次提交
	 * 
	 * @param sql
	 *            用于PreparedStatement的更新修改SQL语句
	 * @param params
	 *            二维数组,不能为空,每行是一条SQL语句的参数
	 * @throws DaoException
	 */
	public void batchExecute(String sql, Object[][] params) throws DaoException {
		batchExecute(sql, params, params.length);
	}

	/**
	 * 批量执行更新修改操作，指定一次提交的数量
	 * 
	 * @param sql
	 *            用于PreparedStatement的更新修改SQL语句
	 * @param params
	 *            二维数组,不能为空,每行是一条SQL语句的参数
	 * @param max
	 *            指定一次批量提交的数量
	 * @throws DaoException
	 */
	public void batchExecute(String sql, Object[][] params, int max)
			throws DaoException {
		Assert.notNull(sql, "argument sql is null");
		Assert.notNull(params, "argument params is null");
		Assert.isTrue(max > 0, "max must bigger than zero");

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);

			for (int i = 0, l = params.length; i < l; i++) {
				for (int j = 0, n = params[i].length; j < n; j++) {
					pstmt.setObject(j + 1, params[i][j]);
				}

				pstmt.addBatch();

				if ((i + 1) % max == 0) {
					pstmt.executeBatch();
				}
			}
			pstmt.executeBatch();
		} catch (SQLException e) {
			throw new DaoException("excute failed: " + sql, e);
		} finally {
			IOUtils.close(con, pstmt, null);
		}
	}

	/**
	 * 批量执行添加操作，指定一次提交的数量
	 * 
	 * @param sql
	 *            用于PreparedStatement的更新修改SQL语句
	 * @param params
	 *            二维数组,不能为空,每行是一条SQL语句的参数
	 * @param max
	 *            指定一次批量提交的数量
	 * @throws DaoException
	 */
	public void batchExecute(StringBuffer sql, int max) throws DaoException {
		Assert.notNull(sql, "argument sql is null");

		Assert.isTrue(max > 0, "max must bigger than zero");

		Connection con = null;
		Statement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.createStatement();
			String sqlinsert[] = sql.toString().split(";");
			if (!sqlinsert[0].equals("")) {
				for (int i = 0, l = sqlinsert.length; i < l; i++) {
					pstmt.addBatch(sqlinsert[i]);
					if ((i + 1) % 100 == 0) {
						pstmt.executeBatch();
					}
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			throw new DaoException("excute failed: " + sql, e);
		} finally {
			IOUtils.close(con, pstmt, null);
		}
	}

	/**
	 * 查询表的列信息
	 * 
	 * @param tableName
	 * @return
	 * @throws DaoException
	 */
	public List<TableInfo> queryTableInfo(String tableName) throws DaoException {
		Assert.notNull(tableName, "argument tableName is null");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TableInfo> tableInfos = new ArrayList<TableInfo>();
		con = getConnection();
		try {

			String sql = buildPageSql("select * from " + tableName, 1, 1);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i).toLowerCase();
				if (!columnName.equals("rn")) {
					String fieldType = String
							.valueOf(metaData.getColumnType(i));
					tableInfos.add(new TableInfo(columnName, fieldType));
				}
			}
		} catch (SQLException e) {
			throw new DaoException("excute query columns information failed:"
					+ tableName, e);
		} finally {
			IOUtils.close(con, pstmt, rs);
		}
		return tableInfos;
	}

	/**
	 * 执行SQL语句查询
	 * 
	 * @param sql
	 *            SQL
	 * @param sql
	 *            如：select 部分字段 from 表1，表2 where ....
	 * @return 返回List<Object[]>
	 */
	public List<Object[]> query(String sql) throws DaoException {
		return query(sql, 0, 0);
	}

	/**
	 * 指定范围查询
	 * 
	 * @param sql
	 *            SQL
	 * @param startPosition
	 *            记录起始位置
	 * @param maxResult
	 *            查询最大记录数
	 * @param sql
	 *            如：select 部分字段 from 表1，表2 where ....
	 * @return 返回List<T>
	 */
	public List<Object[]> query(String sql, int startPosition, int maxResult)
			throws DaoException {
		Assert.notNull(sql, "argument sql is null");

		return query(new ArrayResultSetConvert(), sql, startPosition, maxResult);
	}

	/**
	 * 执行SQL语句查询
	 * 
	 * @param <T>
	 *            实体
	 * @param entityClass
	 *            实体类
	 * @param sql
	 *            如：select 部分字段 from 表1，表2 where ....
	 * @return 返回List<T>
	 */
	public <T> List<T> query(Class<T> entityClass, String sql)
			throws DaoException {
		return query(entityClass, sql, 0, 0);
	}

	/**
	 * 指定范围查询
	 * 
	 * @param <T>
	 *            实体
	 * @param entityClass
	 *            实体类
	 * @param sql
	 *            SQL
	 * @param startPosition
	 *            记录起始位置
	 * @param maxResult
	 *            查询最大记录数
	 * @return 返回List<T>
	 */
	public <T> List<T> query(Class<T> entityClass, String sql,
			int startPosition, int maxResult) throws DaoException {
		Assert.notNull(entityClass, "argument entityClass is null");
		Assert.notNull(sql, "argument sql is null");

		ResultSetConvert<T> convert = new DefaultResultSetConvert<T>(
				entityClass);
		return query(convert, sql, startPosition, maxResult);
	}

	/**
	 * 执行SQL语句查询
	 * 
	 * @param <T>
	 *            实体
	 * @param convert
	 *            ResultSet到T的转换
	 * @param sql
	 *            如：select 部分字段 from 表1，表2 where ....
	 * @return 返回List<T>
	 */
	public <T> List<T> query(ResultSetConvert<T> convert, String sql)
			throws DaoException {
		return query(convert, sql, 0, 0);
	}

	/**
	 * 指定记录范围查询
	 * 
	 * @param <T>
	 *            实体
	 * @param convert
	 *            ResultSet到T的转换
	 * @param sql
	 *            SQL
	 * @param startPosition
	 *            记录起始位置
	 * @param maxResult
	 *            查询最大记录数
	 * @return 返回List<T>
	 */
	public <T> List<T> query(ResultSetConvert<T> convert, String sql,
			int startPosition, int maxResult) throws DaoException {
		Assert.notNull(convert, "argument convert is null");
		Assert.notNull(sql, "argument sql is null");

		String pageSql = buildPageSql(sql, startPosition, maxResult);
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			if (getFetchSize() > 0) {
				stmt.setFetchSize(getFetchSize());
			}
			rs = stmt.executeQuery(pageSql);
			List<T> resultList = convertResultSet(convert, rs);
			return resultList;
		} catch (SQLException e) {
			throw new DaoException("query failed: " + sql, e);
		} catch (IllegalAccessException e) {
			throw new DaoException("query failed: " + sql, e);
		} catch (InvocationTargetException e) {
			throw new DaoException("query failed: " + sql, e);
		} finally {
			IOUtils.close(con, stmt, rs);
		}
	}

	/**
	 * 执行SQL语句查询
	 * 
	 * @param sql
	 *            SQL
	 * @param params
	 *            参数条件，无参数可为null
	 * @return 返回List<Object[]>
	 */
	public List<Object[]> query(String sql, Object[] params)
			throws DaoException {
		return query(sql, params, 0, 0);
	}

	/**
	 * 指定记录范围查询
	 * 
	 * @param sql
	 *            SQL
	 * @param params
	 *            参数条件，无参数可为null
	 * @param startPosition
	 *            记录起始位置
	 * @param maxResult
	 *            查询最大记录数
	 * @param sql
	 *            如：select 部分字段 from 表1，表2 where ....
	 * @return 返回List<T>
	 */
	public List<Object[]> query(String sql, Object[] params, int startPosition,
			int maxResult) throws DaoException {
		Assert.notNull(sql, "argument sql is null");

		return query(new ArrayResultSetConvert(), sql, params, startPosition,
				maxResult);
	}

	/**
	 * 执行SQL语句查询
	 * 
	 * @param <T>
	 *            实体
	 * @param entityClass
	 *            实体类
	 * @param sql
	 *            如：select 部分字段 from 表1，表2 where ....
	 * @param params
	 *            参数条件，无参数可为null
	 * @return 返回List<T>
	 */
	public <T> List<T> query(Class<T> entityClass, String sql, Object[] params)
			throws DaoException {
		return query(entityClass, sql, params, 0, 0);
	}

	/**
	 * 分页查询
	 * 
	 * @param <T>
	 *            实体
	 * @param entityClass
	 *            实体类
	 * @param sql
	 *            查询条件
	 * @param params
	 *            参数，无参数可为null
	 * @param curPage
	 *            当前页数，从1开始
	 * @param pageSize
	 *            页大小
	 * @return
	 * @throws DaoException
	 */
	public <T> Page<T> pageQuery(Class<T> entityClass, String sql,
			Object[] params, int curPage, int pageSize) throws DaoException {
		Page<T> page = new Page<T>();
		int recordCount = count(sql);
		int pageCount = (recordCount - 1) / pageSize + 1; // 总页数
		int rCurPage = (curPage > pageCount) ? pageCount : curPage;// 实际当前页
		page.setCurPage(rCurPage);
		page.setPageSize(pageSize);
		page.setPageCount(pageCount);
		page.setRecordCount(recordCount);
		// 计算记录开始索引
		int first = (int) ((rCurPage - 1) * pageSize) + 1;
		List<T> list = query(entityClass, sql, params, first, pageSize);
		page.setResultList(list);
		return page;
	}

	// -----------------------------------PageListArray<Object[]>的分页和TOP处理------------------------------------------------------

	/**
	 * 1. 返回Object[],不分页也不TopN
	 * 
	 * @param convert
	 * @param curPage
	 * @param pageSize
	 * @param sql
	 * @return
	 */
	public PageListArray<Object[]> objectQuery(String sql) {
		if (null == sql || sql.isEmpty())
			return new PageListArray<Object[]>();

		PageListArray<Object[]> page = new PageListArray<Object[]>();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			if (getFetchSize() > 0) {
				stmt.setFetchSize(getFetchSize());
			}
			rs = stmt.executeQuery(sql);
			ResultSetConvert<Object[]> convert = new ArrayResultSetConvert();
			List<Object[]> resultList = convertResultSet(convert, rs);
			page.setDatas(resultList);

			// 获取结果中的字段名
			List<String> fields = new ArrayList<String>();
			// 结果中的数据的索引
			List<Integer> arrayIndexs = new ArrayList<Integer>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i).toLowerCase();
				if (!columnName.equals("rn")) {
					fields.add(columnName);
					arrayIndexs.add(i - 1);
				}
			}
			page.setFields(fields);
			page.getOriginalFields().addAll(fields);
			page.setArrayIndexs(arrayIndexs);

			return page;
		} catch (Exception e) {
			throw new DaoException("queryPage failed : " + e.getMessage(), e);
		} finally {
			IOUtils.close(con, stmt, rs);
		}
	}

	/**
	 * 2. 返回Object[]的分页查询
	 * 
	 * @param clazz
	 *            实体类
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * 
	 * @param params
	 *            查询参数
	 * @return 分页结果
	 */
	public PageListArray<Object[]> pageObjectQuery(Integer curPage,
			Integer pageSize, String sql) throws DaoException {
		ResultSetConvert<Object[]> convert = new ArrayResultSetConvert();
		return pageObjectQuery(convert, curPage, pageSize, sql);
	}

	/**
	 * 3. 返回Object[]的TopN查询
	 * 
	 * @param topN
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	public PageListArray<Object[]> topObjectQuery(Integer topN, String sql)
			throws DaoException {
		ResultSetConvert<Object[]> convert = new ArrayResultSetConvert();
		return pageObjectQuery(convert, 1, topN, sql);
	}

	/**
	 * 4. 返回Object[]的分页和TopN查询
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param topN
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	public PageListArray<Object[]> pageAndTopObjectQuery(Integer curPage,
			Integer pageSize, Integer topN, String sql) throws DaoException {
		PageListArray<Object[]> page = new PageListArray<Object[]>();
		if (null == curPage || null == pageSize || null == topN || null == sql
				|| 0 == curPage || 0 == pageSize || 0 == topN || sql.isEmpty())
			return page;

		ResultSetConvert<Object[]> convert = new ArrayResultSetConvert();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			int recordCount = count(sql);
			if (topN < recordCount)
				recordCount = topN;
			if (topN < pageSize) {
				pageSize = topN;
			} else {

			}
			int pageCount = (recordCount - 1) / pageSize + 1; // 总页数
			int rCurPage = (curPage > pageCount) ? pageCount : curPage;// 实际当前页
			page.setCurPage(rCurPage);
			page.setPageSize(pageSize);
			page.setPageCount(pageCount);
			page.setRecordCount(recordCount);

			int startPosition = (int) ((rCurPage - 1) * pageSize) + 1;
			int needRowNum = 0;
			if (recordCount % pageSize != 0 && pageCount == rCurPage) {
				needRowNum = recordCount % pageSize;
			} else {
				needRowNum = pageSize;
			}
			String pageSql = buildPageSql(sql, startPosition, needRowNum);
			con = getConnection();
			stmt = con.createStatement();
			if (getFetchSize() > 0) {
				stmt.setFetchSize(getFetchSize());
			}
			rs = stmt.executeQuery(pageSql);

			List<Object[]> resultList = convertResultSet(convert, rs);
			page.setDatas(resultList);

			// 获取结果中的字段名
			List<String> fields = new ArrayList<String>();
			// 结果中的数据的索引
			List<Integer> arrayIndexs = new ArrayList<Integer>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i).toLowerCase();
				if (!columnName.equals("rn")) {
					fields.add(columnName);
					arrayIndexs.add(i - 1);
				}
			}
			page.setFields(fields);
			page.getOriginalFields().addAll(fields);
			page.setArrayIndexs(arrayIndexs);

			return page;
		} catch (Exception e) {
			throw new DaoException("queryPage failed", e);
		} finally {
			IOUtils.close(con, stmt, rs);
		}

	}

	/**
	 * 内部方法:返回Object[]的分页或TopN查询
	 * 
	 * @param convert
	 * @param curPage
	 * @param pageSize
	 * @param sql
	 * @return
	 */
	private PageListArray<Object[]> pageObjectQuery(
			ResultSetConvert<Object[]> convert, Integer curPage,
			Integer pageSize, String sql) {

		if (null == curPage || null == pageSize || null == sql || 0 == curPage
				|| 0 == pageSize || sql.isEmpty())
			return new PageListArray<Object[]>();

		PageListArray<Object[]> page = new PageListArray<Object[]>();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			int recordCount = count(sql);
			int pageCount = (recordCount - 1) / pageSize + 1; // 总页数
			int rCurPage = (curPage > pageCount) ? pageCount : curPage;// 实际当前页
			page.setCurPage(rCurPage);
			page.setPageSize(pageSize);
			page.setPageCount(pageCount);
			page.setRecordCount(recordCount);

			int startPosition = (int) ((rCurPage - 1) * pageSize) + 1;
			String pageSql = buildPageSql(sql, startPosition, pageSize);

			con = getConnection();
			stmt = con.createStatement();
			if (getFetchSize() > 0) {
				stmt.setFetchSize(getFetchSize());
			}
			rs = stmt.executeQuery(pageSql);

			List<Object[]> resultList = convertResultSet(convert, rs);
			page.setDatas(resultList);

			// 获取结果中的字段名
			List<String> fields = new ArrayList<String>();
			// 结果中的数据的索引
			List<Integer> arrayIndexs = new ArrayList<Integer>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i).toLowerCase();
				if (!columnName.equals("rn")) {
					fields.add(columnName);
					arrayIndexs.add(i - 1);
				}
			}
			page.setFields(fields);
			page.getOriginalFields().addAll(fields);
			page.setArrayIndexs(arrayIndexs);

			return page;
		} catch (Exception e) {
			throw new DaoException("queryPage failed", e);
		} finally {
			IOUtils.close(con, stmt, rs);
		}
	}

	// -----------------------------------BeanResult<T>的分页和TOP处理------------------------------------------------------

	/**
	 * 1. 返回BeanResult<T>,不分页也不TopN
	 * 
	 * @param convert
	 * @param curPage
	 * @param pageSize
	 * @param sql
	 * @return
	 */
	public <T> BeanResult<T> beanQuery(Class<T> entityClass, String sql) {

		BeanResult<T> beanResult = new BeanResult<T>();

		if (null == sql || sql.isEmpty())
			return beanResult;

		List<T> beans = query(entityClass, sql, null);
		beanResult.setDatas(beans);

		return beanResult;
	}

	/**
	 * 2. 返回BeanResult<T>的分页查询
	 * 
	 * @param clazz
	 *            实体类
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * 
	 * @param params
	 *            查询参数
	 * @return 分页结果
	 */
	public <T> BeanResult<T> pageBeanQuery(Class<T> entityClass,
			Integer curPage, Integer pageSize, String sql) throws DaoException {
		return pageBeanQuery(entityClass, sql, curPage, pageSize);
	}

	/**
	 * 3. 返回BeanResult<T>的TopN查询
	 * 
	 * @param topN
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	public <T> BeanResult<T> topBeanQuery(Class<T> entityClass, Integer topN,
			String sql) throws DaoException {
		return pageBeanQuery(entityClass, sql, 1, topN);
	}

	/**
	 * 4. 返回BeanResult<T>的分页和TopN查询
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param topN
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	public <T> BeanResult<T> pageAndTopBeanQuery(Class<T> entityClass,
			Integer curPage, Integer pageSize, Integer topN, String sql)
			throws DaoException {

		BeanResult<T> beanResult = new BeanResult<T>();
		if (null == curPage || null == pageSize || null == topN || null == sql
				|| 0 == curPage || 0 == pageSize || 0 == topN || sql.isEmpty())
			return beanResult;

		ResultSetConvert<T> convert = new DefaultResultSetConvert<T>(
				entityClass);

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			int recordCount = count(sql);
			if (topN < recordCount)
				recordCount = topN;
			if (topN < pageSize)
				pageSize = topN;
			int pageCount = (recordCount - 1) / pageSize + 1; // 总页数
			int rCurPage = (curPage > pageCount) ? pageCount : curPage;// 实际当前页
			beanResult.setCurPage(rCurPage);
			beanResult.setPageSize(pageSize);
			beanResult.setPageCount(pageCount);
			beanResult.setRecordCount(recordCount);

			int startPosition = (int) ((rCurPage - 1) * pageSize) + 1;
			String pageSql = buildPageSql(sql, startPosition, pageSize);

			con = getConnection();
			stmt = con.createStatement();
			if (getFetchSize() > 0) {
				stmt.setFetchSize(getFetchSize());
			}
			rs = stmt.executeQuery(pageSql);

			List<T> resultList = convertResultSet(convert, rs);
			beanResult.setDatas(resultList);

			return beanResult;
		} catch (Exception e) {
			throw new DaoException("queryPage failed", e);
		} finally {
			IOUtils.close(con, stmt, rs);
		}

	}

	/**
	 * 内部方法:返回BeanResult<T>的分页或TopN查询
	 * 
	 * @param <T>
	 *            实体
	 * @param entityClass
	 *            实体类
	 * @param sql
	 *            查询条件
	 * @param params
	 *            参数，无参数可为null
	 * @param curPage
	 *            当前页数，从1开始
	 * @param pageSize
	 *            页大小
	 * @return
	 * @throws DaoException
	 */
	private <T> BeanResult<T> pageBeanQuery(Class<T> entityClass, String sql,
			int curPage, int pageSize) throws DaoException {
		BeanResult<T> beanResult = new BeanResult<T>();
		int recordCount = count(sql);
		int pageCount = (recordCount - 1) / pageSize + 1; // 总页数
		int rCurPage = (curPage > pageCount) ? pageCount : curPage;// 实际当前页
		beanResult.setCurPage(rCurPage);
		beanResult.setPageSize(pageSize);
		beanResult.setPageCount(pageCount);
		beanResult.setRecordCount(recordCount);
		// 计算记录开始索引
		int first = (int) ((rCurPage - 1) * pageSize) + 1;
		List<T> list = query(entityClass, sql, null, first, pageSize);
		beanResult.setDatas(list);
		return beanResult;
	}

	/**
	 * 指定范围查询
	 * 
	 * @param <T>
	 *            实体
	 * @param entityClass
	 *            实体类
	 * @param sql
	 *            SQL
	 * @param params
	 *            参数条件，无参数可为null
	 * @param startPosition
	 *            记录起始位置
	 * @param maxResult
	 *            查询最大记录数
	 * @return 返回List<T>
	 */
	public <T> List<T> query(Class<T> entityClass, String sql, Object[] params,
			int startPosition, int maxResult) throws DaoException {
		Assert.notNull(entityClass, "argument entityClass is null");
		Assert.notNull(sql, "argument sql is null");

		ResultSetConvert<T> convert = new DefaultResultSetConvert<T>(
				entityClass);
		return query(convert, sql, params, startPosition, maxResult);
	}

	/**
	 * 执行SQL语句查询
	 * 
	 * @param <T>
	 *            实体
	 * @param convert
	 *            ResultSet到实体的转换
	 * @param sql
	 *            如：select 部分字段 from 表1，表2 where ....
	 * @param params
	 *            参数数组，无参数可为null
	 * @return List<T>
	 */
	public <T> List<T> query(ResultSetConvert<T> convert, String sql,
			Object[] params) throws DaoException {
		return query(convert, sql, params, 0, 0);
	}

	/**
	 * 执行SQL语句查询
	 * 
	 * @param <T>
	 *            实体
	 * @param sql
	 *            如：select 部分字段 from 表1，表2 where ....
	 * @param params
	 *            参数数组，无参数可为null
	 * @param startPosition
	 *            返回数据在查询结果中的起始位置,从1开始
	 * @param maxResult
	 *            返回数据的最大记录数
	 * @param convert
	 *            ResultSet与值转换
	 * @return 返回从startPosition开始，最多maxResult个记录数(List<T>)
	 * @throws DaoException
	 */
	public <T> List<T> query(ResultSetConvert<T> convert, String sql,
			Object[] params, int startPosition, int maxResult)
			throws DaoException {
		String pageSql = buildPageSql(sql, startPosition, maxResult);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(pageSql);

			int fetchSize = getFetchSize();
			if (fetchSize > 0) {
				pstmt.setFetchSize(fetchSize);
			}

			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}

			rs = pstmt.executeQuery();
			List<T> resultList = convertResultSet(convert, rs);
			return resultList;
		} catch (SQLException e) {
			throw new DaoException("query failed: " + sql, e);
		} catch (IllegalAccessException e) {
			throw new DaoException("query failed: " + sql, e);
		} catch (InvocationTargetException e) {
			throw new DaoException("query failed: " + sql, e);
		} finally {
			IOUtils.close(con, pstmt, rs);
		}
	}

	/**
	 * Count
	 * 
	 * @param sql
	 *            原始SQL
	 * @return
	 * @throws DaoException
	 */
	public int count(String sql) throws DaoException {
		String countSql = "SELECT count(1) FROM ( " + sql + " ) cnt";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(countSql);
			int count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
				break;
			}
			return count;
		} catch (Exception e) {
			throw new DaoException("query count failed: " + sql, e);
		} finally {
			IOUtils.close(con, stmt, rs);
		}
	}

	/**
	 * *******************************************************辅助方法**************
	 * *******************************************
	 */

	/**
	 * 获取数据库联接
	 * 
	 * @return 数据库联接
	 * @throws DaoException
	 */
	private Connection getConnection() throws DaoException {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new DaoException("get connection failed", e);
		}
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
	private String buildPageSql(String sql, int startPosition, int needRowNum) {
		return jdbcAtom.buildPageSql(sql, startPosition, needRowNum);
	}

	/**
	 * 转换结果
	 * 
	 * @param <T>
	 *            实体
	 * @param convert
	 *            转换器
	 * @param rs
	 *            结果列表
	 * @return 转换后的实体列表
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private <T> List<T> convertResultSet(ResultSetConvert<T> convert,
			ResultSet rs) throws SQLException, IllegalAccessException,
			InvocationTargetException {
		List<T> list = new LinkedList<T>();
		while (rs.next()) {
			T obj = convert.convert(rs);
			list.add(obj);
		}
		return list;
	}
	
	/**
	 * 1. 返回Map<String, Object>,不分页也不TopN
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, Object> objectQueryNoPage(String sql) {
		if (null == sql || sql.isEmpty())
			return new HashMap<String, Object>();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			if (getFetchSize() > 0) {
				stmt.setFetchSize(getFetchSize());
			}
	        Map<String, Object> values = new HashMap<String, Object>();
			rs = stmt.executeQuery(sql);
	        while(rs.next()){
	        	ResultSetMetaData rsmd = rs.getMetaData();
	            for(int i = 0; i < rsmd.getColumnCount(); i++){
	                String columnLabel = rsmd.getColumnLabel(i + 1);
	                Object columnValue = rs.getObject(columnLabel);
	                values.put(columnLabel, columnValue);
	            }
	        }
			return values;
		} catch (Exception e) {
			throw new DaoException("query failed : " + e.getMessage(), e);
		} finally {
			IOUtils.close(con, stmt, rs);
		}
	}
	
	/**
	 * 1. 返回List<Map<String, Object>>,不分页也不TopN
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> objectQueryAll(String sql) {
		if (null == sql || sql.isEmpty())
			return new ArrayList<Map<String,Object>>();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			if (getFetchSize() > 0) {
				stmt.setFetchSize(getFetchSize());
			}
			List<Map<String, Object>> values = new ArrayList<Map<String,Object>>();
			rs = stmt.executeQuery(sql);
	        while(rs.next()){
	        	Map<String, Object> vmap = new HashMap<String, Object>();
	        	ResultSetMetaData rsmd = rs.getMetaData();
	            for(int i = 0; i < rsmd.getColumnCount(); i++){
	                String columnLabel = rsmd.getColumnLabel(i + 1);
	                Object columnValue = rs.getObject(columnLabel);
	                vmap.put(columnLabel, columnValue);
	            }
	            values.add(vmap);
	        }
			return values;
		} catch (Exception e) {
			throw new DaoException("query failed : " + e.getMessage(), e);
		} finally {
			IOUtils.close(con, stmt, rs);
		}
	}

	

	/**
	 * @return
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		if (null == jdbcAtom)
			jdbcAtom = new SqlAtomFactory(dataSource).createFactory()
					.createJdbcTemplateAtomProduct();
	}

	/**
	 * @return the fetchSize
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * @param fetchSize
	 *            the fetchSize to set
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

}
