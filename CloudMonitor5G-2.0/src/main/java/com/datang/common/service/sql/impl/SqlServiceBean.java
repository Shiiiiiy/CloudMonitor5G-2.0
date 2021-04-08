package com.datang.common.service.sql.impl;

import com.datang.common.dao.entity.PageListArray;
import com.datang.common.dao.sql.PageOrTopService;
import com.datang.common.service.sql.GroupByService;
import com.datang.common.service.sql.OrderByService;
import com.datang.common.service.sql.SelectService;
import com.datang.common.service.sql.SqlService;
import com.datang.common.service.sql.TableService;
import com.datang.common.service.sql.WhereService;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;

/**
 * 组织SQL语句,查询数据集
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 上午9:51:45
 * @version
 */
public abstract class SqlServiceBean implements SqlService {
	/**
	 * 参数
	 */
	protected Object param;
	/**
	 * SQL语句
	 */
	protected String sql;

	protected SelectService select;
	protected WhereService where;
	protected TableService table;
	protected GroupByService groupBy;
	protected OrderByService orderBy;

	protected PageOrTopService pageService;

	/**
	 * 根据参数 解析SQL语句
	 * 
	 * @return
	 */
	public PageListArray<Object[]> parse(Object param) {
		initParam(param);
		buildSql();
		return pageService.getPageTop(sql);
	}

	/**
	 * 初始化参数
	 * 
	 * @param param
	 */
	protected void initParam(Object param) {
		this.param = param;
		select.setParam(param);
		where.setParam(param);
		groupBy.setParam(param);
		orderBy.setParam(param);
		table.setParam(param);
		pageService.setParam(param);
	}

	/**
	 * 解析SQL
	 */
	protected void buildSql() {

		// 组织SQL语句
		StringBuffer firstSql = new StringBuffer();

		String selectStatement = select.getSelect();
		if (selectStatement.isEmpty()) {
			selectStatement = "*";
		}
		firstSql.append(ParamConstant.SELECT);
		firstSql.append(ParamConstant.SPACE);
		firstSql.append(selectStatement);
		firstSql.append(ParamConstant.SPACE);
		firstSql.append(ParamConstant.FROM);
		firstSql.append(ParamConstant.SPACE);
		firstSql.append(table.getTableName());
		firstSql.append(ParamConstant.SPACE);
		firstSql.append(ParamConstant.WHERE);
		firstSql.append(ParamConstant.SPACE);
		firstSql.append(where.getWhere());
		firstSql.append(ParamConstant.SPACE);
		String groupByStatement = groupBy.getGroupBy();
		if (!groupByStatement.trim().isEmpty()) {
			firstSql.append(ParamConstant.GROUP_BY);
			firstSql.append(ParamConstant.SPACE);
			firstSql.append(groupBy.getGroupBy());
			firstSql.append(ParamConstant.SPACE);
			// 添加汇总
			String selectStatementTotal = select.getInfoTotal();
			if(!StringUtils.hasText(selectStatementTotal)){
				selectStatementTotal = selectStatement;
			}
			System.out.println("汇总："+selectStatementTotal);
			firstSql.append("UNION");
			firstSql.append(ParamConstant.SPACE);
			firstSql.append(ParamConstant.SELECT);
			firstSql.append(ParamConstant.SPACE);
			if (selectStatementTotal.equals("*")) {
				firstSql.append(selectStatementTotal);
			} else {
				if (groupByStatement.contains(",")) {
					String[] split = groupByStatement.split(",");
					for (int i = 0; i < split.length; i++) {
						firstSql.append("NULL AS ");
						if (i == (split.length - 1)) {
							firstSql.append(split[i] + ParamConstant.SPACE);
						} else {
							firstSql.append(split[i] + ParamConstant.COMMA);
						}

					}
					firstSql.append(selectStatementTotal.replace(
							selectStatementTotal.trim(), ""));
				} else {
					firstSql.append("NULL AS ");
					firstSql.append(selectStatementTotal);
				}
			}
			firstSql.append(ParamConstant.SPACE);
			firstSql.append(ParamConstant.FROM);
			firstSql.append(ParamConstant.SPACE);
			firstSql.append(table.getTableName());
			firstSql.append(ParamConstant.SPACE);
			firstSql.append(ParamConstant.WHERE);
			firstSql.append(ParamConstant.SPACE);
			firstSql.append(where.getWhere());
			firstSql.append(ParamConstant.SPACE);
		}
		StringBuffer secondSql = new StringBuffer();
		secondSql.append(ParamConstant.SELECT_ALL);
		secondSql.append(ParamConstant.SPACE);
		secondSql.append(ParamConstant.FROM);
		secondSql.append(ParamConstant.SPACE);
		secondSql.append(ParamConstant.LEFT_BRACKET);
		secondSql.append(firstSql);
		secondSql.append(ParamConstant.RIGHT_BRACKET);
		secondSql.append(ParamConstant.SPACE);
		secondSql.append("firstSql");
		secondSql.append(ParamConstant.SPACE);
		secondSql.append(ParamConstant.WHERE);
		secondSql.append(ParamConstant.SPACE);
		secondSql.append(where.getWhereByIndicationCondition());
		secondSql.append(ParamConstant.SPACE);
		// secondSql.append(ParamConstant.AND);
		// secondSql.append(ParamConstant.SPACE);
		// secondSql.append(where.getWhereByFilterEmptyKpi());
		secondSql.append(ParamConstant.SPACE);
		String orderByStatement = orderBy.getOrderBy();
		if (!orderByStatement.trim().isEmpty()) {
			secondSql.append(ParamConstant.ORDER_BY);
			secondSql.append(ParamConstant.SPACE);
			secondSql.append(orderByStatement);
		}
		selectStatement = select.getSelectResult();
		if (selectStatement.trim().isEmpty()) {
			selectStatement = "*";
		}
		StringBuffer thirdSql = new StringBuffer();
		thirdSql.append(ParamConstant.SELECT);
		thirdSql.append(ParamConstant.SPACE);
		thirdSql.append(selectStatement);
		thirdSql.append(ParamConstant.SPACE);
		thirdSql.append(ParamConstant.FROM);
		thirdSql.append(ParamConstant.SPACE);
		thirdSql.append(ParamConstant.LEFT_BRACKET);
		thirdSql.append(secondSql);
		thirdSql.append(ParamConstant.RIGHT_BRACKET);
		thirdSql.append(ParamConstant.SPACE);
		thirdSql.append("secondSql");
		setSql(thirdSql.toString());

	}

	/**
	 * @return the param
	 */
	public Object getParam() {
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(Object param) {
		this.param = param;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql
	 *            the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return the select
	 */
	public SelectService getSelect() {
		return select;
	}

	/**
	 * @param select
	 *            the select to set
	 */
	public void setSelect(SelectService select) {
		this.select = select;
	}

	/**
	 * @return the where
	 */
	public WhereService getWhere() {
		return where;
	}

	/**
	 * @param where
	 *            the where to set
	 */
	public void setWhere(WhereService where) {
		this.where = where;
	}

	/**
	 * @return the groupBy
	 */
	public GroupByService getGroupBy() {
		return groupBy;
	}

	/**
	 * @param groupBy
	 *            the groupBy to set
	 */
	public void setGroupBy(GroupByService groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * @return the orderBy
	 */
	public OrderByService getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(OrderByService orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the table
	 */
	public TableService getTable() {
		return table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(TableService table) {
		this.table = table;
	}

	/**
	 * @return the pageServicepageService
	 */
	public PageOrTopService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService
	 *            the pageService to set
	 */
	public void setPageService(PageOrTopService pageService) {
		this.pageService = pageService;
	}

}
