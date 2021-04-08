package com.datang.common.service.sql.util;

import java.util.List;

public interface MrKpiUtilService {

	/**
	 * 根据KPI指标,获取分段指标语句
	 * 
	 * @param kpisValue
	 * @return
	 */
	public abstract String mergeSeqKpi(Object kpisValue);
	
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
	public abstract List<String> getKpiTypesBySeqKpis(Object seqKpisValue);

	/**
	 * 根据指标,获取kpiType列表
	 * 
	 * @param seqKpis
	 * @return
	 */
	public abstract List<String> getKpiTypesByKpis(Object kpisValue);
	
	
}
