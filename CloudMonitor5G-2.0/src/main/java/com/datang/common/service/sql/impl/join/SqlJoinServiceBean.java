package com.datang.common.service.sql.impl.join;

import org.springframework.stereotype.Service;

import com.datang.common.dao.entity.PageListArray;
import com.datang.common.dao.sql.PageOrTopService;
import com.datang.common.service.sql.GroupByService;
import com.datang.common.service.sql.JoinService;
import com.datang.common.service.sql.OrderByService;
import com.datang.common.service.sql.SelectService;
import com.datang.common.service.sql.SqlService;
import com.datang.common.service.sql.TableService;
import com.datang.common.service.sql.WhereService;
import com.datang.common.util.ParamConstant;

@Service
public class SqlJoinServiceBean implements SqlService {
	/**
	 * 参数1
	 */
	protected Object param;
	/**
	 * 参数2
	 */
	protected Object param2;
	/**
	 * SQL语句
	 */
	protected String sql;
	protected SelectService select1;
	protected TableService table1;
	protected WhereService where1;
	protected GroupByService groupBy1;

	protected SelectService select2;
	protected TableService table2;
	protected WhereService where2;
	protected GroupByService groupBy2;

	protected JoinService joinService;
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
		// this.param2 = ParseParamUtil.clone(this.param);

		select1.setParam(this.param);
		where1.setParam(this.param);
		groupBy1.setParam(this.param);
		table1.setParam(this.param);

		select2.setParam(this.param2);
		where2.setParam(this.param2);
		groupBy2.setParam(this.param2);
		table2.setParam(this.param2);

		joinService.setParam(this.param);
		orderBy.setParam(this.param);
		pageService.setParam(this.param);
	}

	/**
	 * 解析SQL:对于BSC和LAC级别的SQL语句中,PART1指标名变换过程:前台传递KPI别名->经过参数解析工具类解析为指标英文名->
	 * 最后分别在getSelect()和getSelectResult()把指标做别名处理.
	 * PART2内部添加的指标的指标名变换过程:内部添加指标英文名
	 * ->最后分别在getSelect()和getSelectResult()把指标做别名处理.
	 * 故前台传递的指标必须为指标别名,而内部添加的指标必须为指标英文名
	 */
	protected void buildSql() {

		// 组织SQL语句
		StringBuffer sql = new StringBuffer();

		// part1
		// 是否启动第一套Service体系
		StringBuffer thirdSql1 = new StringBuffer();
		String selectStatement1 = "";
		if (null != select1.getParam()) {
			StringBuffer firstSql1 = new StringBuffer();

			selectStatement1 = select1.getSelect();
			if (selectStatement1.trim().isEmpty())
				selectStatement1 = "*";
			firstSql1.append(ParamConstant.SELECT);
			firstSql1.append(ParamConstant.SPACE);
			firstSql1.append(selectStatement1);
			firstSql1.append(ParamConstant.SPACE);
			firstSql1.append(ParamConstant.FROM);
			firstSql1.append(ParamConstant.SPACE);
			firstSql1.append(table1.getTableName());
			firstSql1.append(ParamConstant.SPACE);
			firstSql1.append(ParamConstant.WHERE);
			firstSql1.append(ParamConstant.SPACE);
			firstSql1.append(where1.getWhere());
			firstSql1.append(ParamConstant.SPACE);
			String groupByStatement1 = groupBy1.getGroupBy();
			if (!groupByStatement1.trim().isEmpty()) {
				firstSql1.append(ParamConstant.GROUP_BY);
				firstSql1.append(ParamConstant.SPACE);
				firstSql1.append(groupByStatement1);
			}

			StringBuffer secondSql1 = new StringBuffer();
			secondSql1.append(ParamConstant.SELECT_ALL);
			secondSql1.append(ParamConstant.SPACE);
			secondSql1.append(ParamConstant.FROM);
			secondSql1.append(ParamConstant.SPACE);
			secondSql1.append(ParamConstant.LEFT_BRACKET);
			secondSql1.append(firstSql1);
			secondSql1.append(ParamConstant.RIGHT_BRACKET);
			secondSql1.append(ParamConstant.SPACE);
			secondSql1.append("firstSql1");
			secondSql1.append(ParamConstant.SPACE);
			secondSql1.append(ParamConstant.WHERE);
			secondSql1.append(ParamConstant.SPACE);
			secondSql1.append(where1.getWhereByIndicationCondition());
			secondSql1.append(ParamConstant.SPACE);
			secondSql1.append(ParamConstant.AND);
			secondSql1.append(ParamConstant.SPACE);
			secondSql1.append(where1.getWhereByFilterEmptyKpi());
			secondSql1.append(ParamConstant.SPACE);

			selectStatement1 = select1.getSelectResult();
			if (selectStatement1.trim().isEmpty())
				selectStatement1 = "*";

			thirdSql1.append(ParamConstant.SELECT);
			thirdSql1.append(ParamConstant.SPACE);
			thirdSql1.append(selectStatement1);
			thirdSql1.append(ParamConstant.SPACE);
			thirdSql1.append(ParamConstant.FROM);
			thirdSql1.append(ParamConstant.SPACE);
			thirdSql1.append(ParamConstant.LEFT_BRACKET);
			thirdSql1.append(secondSql1);
			thirdSql1.append(ParamConstant.RIGHT_BRACKET);
			thirdSql1.append(ParamConstant.SPACE);
			thirdSql1.append("secondSql1");
		}

		// part2
		// 是否启动第二套Service体系
		String selectStatement2 = "";
		StringBuffer thirdSql2 = new StringBuffer();
		if (null != select2.getParam()) {
			StringBuffer firstSql2 = new StringBuffer();

			selectStatement2 = select2.getSelect();
			if (selectStatement2.trim().isEmpty()) {
				selectStatement2 = "*";
			}
			firstSql2.append(ParamConstant.SELECT);
			firstSql2.append(ParamConstant.SPACE);
			firstSql2.append(selectStatement2);
			firstSql2.append(ParamConstant.SPACE);
			firstSql2.append(ParamConstant.FROM);
			firstSql2.append(ParamConstant.SPACE);
			firstSql2.append(table2.getTableName());
			firstSql2.append(ParamConstant.SPACE);
			firstSql2.append(ParamConstant.WHERE);
			firstSql2.append(ParamConstant.SPACE);
			firstSql2.append(where2.getWhere());
			firstSql2.append(ParamConstant.SPACE);
			String groupByStatement2 = groupBy2.getGroupBy();
			if (!groupByStatement2.trim().isEmpty()) {
				firstSql2.append(ParamConstant.GROUP_BY);
				firstSql2.append(ParamConstant.SPACE);
				firstSql2.append(groupByStatement2);
			}

			StringBuffer secondSql2 = new StringBuffer();
			secondSql2.append(ParamConstant.SELECT_ALL);
			secondSql2.append(ParamConstant.SPACE);
			secondSql2.append(ParamConstant.FROM);
			secondSql2.append(ParamConstant.SPACE);
			secondSql2.append(ParamConstant.LEFT_BRACKET);
			secondSql2.append(firstSql2);
			secondSql2.append(ParamConstant.RIGHT_BRACKET);
			secondSql2.append(ParamConstant.SPACE);
			secondSql2.append("firstSql2");
			secondSql2.append(ParamConstant.SPACE);
			secondSql2.append(ParamConstant.WHERE);
			secondSql2.append(ParamConstant.SPACE);
			secondSql2.append(where2.getWhereByIndicationCondition());
			secondSql2.append(ParamConstant.SPACE);
			secondSql2.append(ParamConstant.AND);
			secondSql2.append(ParamConstant.SPACE);
			secondSql2.append(where2.getWhereByFilterEmptyKpi());
			secondSql2.append(ParamConstant.SPACE);

			selectStatement2 = select2.getSelectResult();
			if (selectStatement2.trim().isEmpty()) {
				selectStatement2 = "*";
			}

			thirdSql2.append(ParamConstant.SELECT);
			thirdSql2.append(ParamConstant.SPACE);
			thirdSql2.append(selectStatement2);
			thirdSql2.append(ParamConstant.SPACE);
			thirdSql2.append(ParamConstant.FROM);
			thirdSql2.append(ParamConstant.SPACE);
			thirdSql2.append(ParamConstant.LEFT_BRACKET);
			thirdSql2.append(secondSql2);
			thirdSql2.append(ParamConstant.RIGHT_BRACKET);
			thirdSql2.append(ParamConstant.SPACE);
			thirdSql2.append("secondSql2");
			thirdSql2.append(ParamConstant.SPACE);
			groupByStatement2 = groupBy2.getGroupByResult();
			if (!groupByStatement2.trim().isEmpty()) {
				thirdSql2.append(ParamConstant.GROUP_BY);
				thirdSql2.append(ParamConstant.SPACE);
				thirdSql2.append(groupByStatement2);
			}
		}
		if (null != selectStatement1 && !selectStatement1.isEmpty()
				&& null != selectStatement2 && !selectStatement2.isEmpty()) {
			sql.append(joinService.getJoin(thirdSql1.toString(),
					selectStatement1, "a", thirdSql2.toString(),
					selectStatement2, "b", JoinService.LEFT_JOIN));
		} else if (null != selectStatement1 && !selectStatement1.isEmpty()) {
			sql.append(thirdSql1);
		} else {
			sql.append(thirdSql2);
		}
		sql.append(ParamConstant.SPACE);
		String orderByStatement = orderBy.getOrderBy();
		if (!orderByStatement.trim().isEmpty()) {
			sql.append(ParamConstant.ORDER_BY);
			sql.append(ParamConstant.SPACE);
			sql.append(orderByStatement);
		}
		setSql(sql.toString());
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
	 * @return the select1
	 */
	public SelectService getSelect1() {
		return select1;
	}

	/**
	 * @param select1
	 *            the select1 to set
	 */
	public void setSelect1(SelectService select1) {
		this.select1 = select1;
	}

	/**
	 * @return the where1
	 */
	public WhereService getWhere1() {
		return where1;
	}

	/**
	 * @param where1
	 *            the where1 to set
	 */
	public void setWhere1(WhereService where1) {
		this.where1 = where1;
	}

	/**
	 * @return the groupBy1
	 */
	public GroupByService getGroupBy1() {
		return groupBy1;
	}

	/**
	 * @param groupBy1
	 *            the groupBy1 to set
	 */
	public void setGroupBy1(GroupByService groupBy1) {
		this.groupBy1 = groupBy1;
	}

	/**
	 * @return the table1
	 */
	public TableService getTable1() {
		return table1;
	}

	/**
	 * @param table1
	 *            the table1 to set
	 */
	public void setTable1(TableService table1) {
		this.table1 = table1;
	}

	/**
	 * @return the select2
	 */
	public SelectService getSelect2() {
		return select2;
	}

	/**
	 * @param select2
	 *            the select2 to set
	 */
	public void setSelect2(SelectService select2) {
		this.select2 = select2;
	}

	/**
	 * @return the where2
	 */
	public WhereService getWhere2() {
		return where2;
	}

	/**
	 * @param where2
	 *            the where2 to set
	 */
	public void setWhere2(WhereService where2) {
		this.where2 = where2;
	}

	/**
	 * @return the groupBy2
	 */
	public GroupByService getGroupBy2() {
		return groupBy2;
	}

	/**
	 * @param groupBy2
	 *            the groupBy2 to set
	 */
	public void setGroupBy2(GroupByService groupBy2) {
		this.groupBy2 = groupBy2;
	}

	/**
	 * @return the table2
	 */
	public TableService getTable2() {
		return table2;
	}

	/**
	 * @param table2
	 *            the table2 to set
	 */
	public void setTable2(TableService table2) {
		this.table2 = table2;
	}

	/**
	 * @return the joinService
	 */
	public JoinService getJoinService() {
		return joinService;
	}

	/**
	 * @param joinService
	 *            the joinService to set
	 */
	public void setJoinService(JoinService joinService) {
		this.joinService = joinService;
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
