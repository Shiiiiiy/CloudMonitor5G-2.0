package com.datang.common.service.sql.util;

import java.util.List;
import java.util.Map;

public interface KpiUtilService {
	
	/**
	 * init
	 */
	public void init();

	/**
	 * 根据KPI指标,获取这些KPI所属的Table,返回MAP<表名,KPIS(以逗号分隔)>,KEY为表名,Value为该表名下的KPIS.
	 * 
	 * @param kpisValue
	 * @return
	 */
	public Map<String, String> getKpiBelongToTables(Object kpisValue);
	
	/**
	 * 获取指标的公式信息
	 * 
	 * @param kpiNameEn
	 * @return
	 */
	public String buildFormula(String kpiNameEn);

	/**
	 * 获取Select中的<指标公式 指标名>序偶信息
	 * 
	 * @param kpisValue
	 * @param indicationKpiValue
	 * @param mergeKpisValue
	 * @return
	 */
	public String getSelectKpis(Object kpisValue, Object indicationKpiValue,
			Object mergeKpisValue);

	/**
	 * 合并分段KPIS指标
	 * 
	 * @param selectKpisInfo
	 *            指标总集
	 * @param mergeKpisValue
	 *            需要被合并的指标
	 * @return
	 */
	public String mergeSeqKpis(Object selectKpisInfo, Object mergeKpisValue);
	
	/**
	 * 根据KPI指标,获取其指标中文名
	 * 
	 * @param kpisValue
	 * @return
	 */
	public abstract String getNameChByKpis(Object kpisValue);
	
	/**
	 * 根据KPI指标,获取分段指标
	 * 
	 * @param kpisValue
	 * @return
	 */
	public abstract String getAliasSeqKpisByKpis(Object kpisValue);
	
	/**
	 * 根据分段指标,获取kpiType列表
	 * 
	 * @param seqKpis
	 * @return
	 */
	public List<String> getKpiTypesBySeqKpis(Object seqKpisValue);
	
	/**
	 * 根据指标,获取kpiType列表
	 * 
	 * @param seqKpis
	 * @return
	 */
	public List<String> getKpiTypesByKpis(Object kpisValue);
	
}
