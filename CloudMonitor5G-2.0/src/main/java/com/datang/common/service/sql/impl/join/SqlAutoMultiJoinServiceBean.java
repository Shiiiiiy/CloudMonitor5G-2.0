package com.datang.common.service.sql.impl.join;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.datang.common.dao.sql.PageOrTopService;
import com.datang.common.service.sql.GroupByService;
import com.datang.common.service.sql.JoinService;
import com.datang.common.service.sql.OrderByService;
import com.datang.common.service.sql.SelectService;
import com.datang.common.service.sql.TableService;
import com.datang.common.service.sql.WhereService;
import com.datang.common.tools.sql.SqlConstant;
import com.datang.common.util.BeanLoader;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.ReflectUtil;

/**
 * 多表连接服务:这多个表服务可以使用不同的Serivce体系(SelectService, WhereService GroupBy1Service,
 * TableService).
 * 
 * @author zhangchongfeng
 * 
 */
@Service
public class SqlAutoMultiJoinServiceBean extends SqlJoinServiceBean {

	/**
	 * 存储模块和Serivce信息的Map,格式为<MODEL,{SERVICETYPE:SERVICENAME;}>
	 */
	protected Map<String, String> modelServiceMap = new LinkedHashMap<String, String>();

	/**
	 * 存储模块和参数信息的Map,格式为<MODEL,PARAM>
	 */
	protected Map<String, Object> paramMap = new LinkedHashMap<String, Object>();

	/**
	 * 
	 */
	public SqlAutoMultiJoinServiceBean() {
		super();
	}

	/**
	 * 初始化参数
	 * 
	 * @param param
	 */
	protected void initParam(Object param) {
		this.param = param;
		joinService.setParam(this.param);
		orderBy.setParam(this.param);
		pageService.setParam(this.param);

		// 如果子类没有设置参数MAP,则默认为每个模块都对应相同的参数param;否则,根据子类中的参数MAP为每个模块设置不同的参数
		if (paramMap.isEmpty()) {
			for (String model : modelServiceMap.keySet()) {
				paramMap.put(model, this.param);
			}
		}

	}

	/**
	 * 动态注入Service
	 * 
	 * @param param
	 * @param model
	 */
	protected boolean initDynamicServiceBean(Object param, String model) {

		boolean isSucc = false;// 初始化是否成功

		if (modelServiceMap.containsKey(model)) {
			String serviceEexpression = modelServiceMap.get(model);
			String[] serviceUnits = serviceEexpression
					.split(ParamConstant.SIMICOLON);
			for (String serviceUnit : serviceUnits) {
				String[] service = serviceUnit.split(ParamConstant.COLON);
				if (service.length > 1) {
					// 动态注入
					if (service[0].equals(SqlConstant.SELECT)) {
						select1 = (SelectService) BeanLoader
								.getBean(service[1]);
						// 挑选KPI
						select1.setParam(param);
						isSucc = chooseKpi(param, model);
					} else if (service[0].equals(SqlConstant.TABLE)) {
						table1 = (TableService) BeanLoader.getBean(service[1]);
						table1.setParam(param);
					} else if (service[0].equals(SqlConstant.WHERE)) {
						where1 = (WhereService) BeanLoader.getBean(service[1]);
						where1.setParam(param);
					} else if (service[0].equals(SqlConstant.GROUPBY)) {
						groupBy1 = (GroupByService) BeanLoader
								.getBean(service[1]);
						groupBy1.setParam(param);
					}

				}
			}
		}

		return isSucc;
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
		String sql = "";
		String selectStatement = "";
		int i = 0;
		for (String model : modelServiceMap.keySet()) {

			i++;// 第i套Service体系

			// 获取该模块的参数
			Object param = paramMap.get(model);
			// 动态初始化该模块的Service体系
			boolean isSucc = initDynamicServiceBean(param, model);
			if (!isSucc)
				continue;

			// 构造Part Sql语句
			StringBuffer firstSqlOther = new StringBuffer();

			String selectStatementOhter = select1.getSelect();
			if (selectStatementOhter.isEmpty()) {
				selectStatementOhter = "*";
			}
			firstSqlOther.append(ParamConstant.SELECT);
			firstSqlOther.append(ParamConstant.SPACE);
			firstSqlOther.append(selectStatementOhter);
			firstSqlOther.append(ParamConstant.SPACE);
			firstSqlOther.append(ParamConstant.FROM);
			firstSqlOther.append(ParamConstant.SPACE);
			firstSqlOther.append(table1.getTableName());
			firstSqlOther.append(ParamConstant.SPACE);
			firstSqlOther.append(ParamConstant.WHERE);
			firstSqlOther.append(ParamConstant.SPACE);
			firstSqlOther.append(where1.getWhere());
			firstSqlOther.append(ParamConstant.SPACE);
			String groupByStatementOhter = groupBy1.getGroupBy();
			if (!groupByStatementOhter.isEmpty()) {
				firstSqlOther.append(ParamConstant.GROUP_BY);
				firstSqlOther.append(ParamConstant.SPACE);
				firstSqlOther.append(groupByStatementOhter);
			}

			StringBuffer secondSqlOhter = new StringBuffer();
			secondSqlOhter.append(ParamConstant.SELECT_ALL);
			secondSqlOhter.append(ParamConstant.SPACE);
			secondSqlOhter.append(ParamConstant.FROM);
			secondSqlOhter.append(ParamConstant.SPACE);
			secondSqlOhter.append(ParamConstant.LEFT_BRACKET);
			secondSqlOhter.append(firstSqlOther);
			secondSqlOhter.append(ParamConstant.RIGHT_BRACKET);
			secondSqlOhter.append(ParamConstant.SPACE);
			secondSqlOhter.append("firstSql");
			secondSqlOhter.append(i);
			secondSqlOhter.append(ParamConstant.SPACE);
			secondSqlOhter.append(ParamConstant.WHERE);
			secondSqlOhter.append(ParamConstant.SPACE);
			secondSqlOhter.append(where1.getWhereByIndicationCondition());
			secondSqlOhter.append(ParamConstant.SPACE);
			secondSqlOhter.append(ParamConstant.AND);
			secondSqlOhter.append(ParamConstant.SPACE);
			secondSqlOhter.append(where1.getWhereByFilterEmptyKpi());
			secondSqlOhter.append(ParamConstant.SPACE);

			selectStatementOhter = select1.getSelectResult();
			if (selectStatementOhter.isEmpty()) {
				selectStatementOhter = "*";
			}
			StringBuffer thirdSqlOhter = new StringBuffer();
			thirdSqlOhter.append(ParamConstant.SELECT);
			thirdSqlOhter.append(ParamConstant.SPACE);
			thirdSqlOhter.append(selectStatementOhter);
			thirdSqlOhter.append(ParamConstant.SPACE);
			thirdSqlOhter.append(ParamConstant.FROM);
			thirdSqlOhter.append(ParamConstant.SPACE);
			thirdSqlOhter.append(ParamConstant.LEFT_BRACKET);
			thirdSqlOhter.append(secondSqlOhter);
			thirdSqlOhter.append(ParamConstant.RIGHT_BRACKET);
			thirdSqlOhter.append(ParamConstant.SPACE);
			thirdSqlOhter.append("secondSql" + i);
			thirdSqlOhter.append(ParamConstant.SPACE);
			groupByStatementOhter = groupBy1.getGroupByResult();
			if (!groupByStatementOhter.isEmpty()) {
				thirdSqlOhter.append(ParamConstant.GROUP_BY);
				thirdSqlOhter.append(ParamConstant.SPACE);
				thirdSqlOhter.append(groupByStatementOhter);
				if (!where1.getHaving().isEmpty()) {
					thirdSqlOhter.append(ParamConstant.SPACE);
					thirdSqlOhter.append(ParamConstant.HAVING);
					thirdSqlOhter.append(ParamConstant.SPACE);
					thirdSqlOhter.append(where1.getHaving());
				}
			}

			sql = joinService.getJoin(sql, selectStatement, "a" + i,
					thirdSqlOhter.toString(), selectStatementOhter, "b" + i,
					JoinService.LEFT_JOIN);

			selectStatement = mergeAndFilterEmptyKpis(new Object[] {
					selectStatement, selectStatementOhter });

		}

		sql += ParamConstant.SPACE;
		String orderByStatement = orderBy.getOrderBy();
		if (!orderByStatement.isEmpty()) {
			sql += ParamConstant.ORDER_BY;
			sql += ParamConstant.SPACE;
			sql += orderByStatement;
		}

		setSql(sql);

	}

	/**
	 * 挑选参数中属于本模块中的KPI,返回是否获取到KPI
	 * 
	 * @param param
	 * @param model
	 * @return TRUE:获取到KPI;FALSE:没有获取到该模块的KPI
	 */
	private boolean chooseKpi(Object param, String model) {
		Object kpisValue = ReflectUtil.getField(param, ParamConstant.KPIS);
		Map<String, String> tableToKpis = select1
				.getKpiBelongToTables(kpisValue);
		String kpis = "";
		if (!model.contains(ParamConstant.COMMA)) {
			// 单模块KPI
			kpis = tableToKpis.get(model);
			if (null == kpis) {
				kpis = "";
				ReflectUtil.setField(param, ParamConstant.KPIS, kpis);
				return false;
			} else {
				ReflectUtil.setField(param, ParamConstant.KPIS, kpis);
				return true;
			}
		} else {
			// 多模块组合KPI
			String[] models = model.split(ParamConstant.COMMA);
			for (String m : models) {
				String kpi = tableToKpis.get(m);
				if (null != kpi && !kpi.isEmpty()) {
					kpis += kpi;
					kpis += ParamConstant.COMMA;
				}
			}

			if (!kpis.isEmpty()) {
				kpis = kpis.substring(0, kpis.lastIndexOf(","));
				ReflectUtil.setField(param, ParamConstant.KPIS, kpis);
				return true;
			} else {
				ReflectUtil.setField(param, ParamConstant.KPIS, kpis);
			}
			return false;
		}

	}

	/**
	 * 获取Select中的指标名信息,合并并过滤指标中的空信息
	 * 
	 * @param kpisValue
	 * @return
	 */
	protected String mergeAndFilterEmptyKpis(Object... kpisValues) {

		StringBuffer selectOfkpis = new StringBuffer();
		if (null == kpisValues)
			return selectOfkpis.toString();
		for (Object kpisValue : kpisValues) {

			if (null == kpisValue || !(kpisValue instanceof String)) {
				continue;
			}

			String kpis = (String) kpisValue;
			String[] kpiArray = kpis.split(ParamConstant.COMMA_OR_SIMICOLON);
			for (String kpi : kpiArray) {
				if (null != kpi
						&& !kpi.trim().isEmpty()
						&& !Arrays.asList(selectOfkpis.toString().split(","))
								.contains(kpi)) {
					selectOfkpis.append(kpi);
					selectOfkpis.append(ParamConstant.COMMA);
				}
			}
		}

		if (selectOfkpis.length() > 0) {
			return selectOfkpis.substring(0,
					selectOfkpis.lastIndexOf(ParamConstant.COMMA));
		} else {
			return selectOfkpis.toString();
		}
	}

	/**
	 * @param joinService
	 *            the joinService to set
	 */
	// @Resource(name = "joinServiceBean")
	public void setJoinService(JoinService joinService) {
		this.joinService = joinService;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	// @Resource(name = "orderByServiceBean")
	public void setOrderBy(OrderByService orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @param pageTop
	 *            the pageTop to set
	 */
	// @Resource(name = "pageOrTopServiceBean")
	public void setPageTop(PageOrTopService pageService) {
		this.pageService = pageService;
	}

}
