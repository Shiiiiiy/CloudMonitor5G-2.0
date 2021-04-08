package com.datang.service.sql.impl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datang.common.service.sql.SelectService;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.ReflectUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.wholePreview.VoLteKpi;
import com.datang.service.VoLTEDissertation.voLTEKpi.IVoLTEKpiService;

/**
 * 获取查询参数的抽象服务类
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 下午4:52:27
 * @version
 */
@Service
public abstract class SelectServiceBean implements SelectService {

	/**
	 * 指标<别名,公式>列表
	 */
	protected Map<String, String> kpiAliasSummaryFormula = new HashMap<String, String>();
	
	/**
	 * 汇总指标<别名,公式>列表
	 */
	protected Map<String, String> kpiAliasTotalSummaryFormula = new HashMap<String, String>();

	/**
	 * 指标别名
	 */
	@Autowired
	protected IVoLTEKpiService voLTEKpiService;

	/**
	 * 
	 */
	protected Object param;
	
	/**
	 * 
	 */
	protected String infoTotal ="";

	/**
	 * init
	 */
	public void init() {
		// 初始化KPI
		kpiAliasSummaryFormula.clear();
		kpiAliasTotalSummaryFormula.clear();
		if (kpiAliasSummaryFormula.isEmpty() && kpiAliasTotalSummaryFormula.isEmpty()) {
			PropertyDescriptor[] fields = BeanUtils
					.getPropertyDescriptors(param.getClass());
			if (null == fields || 0 == fields.length) {
				return;
			}
			try {
				Object reportTypeValue = null;
				Object stairClassifyValue = null;

				for (PropertyDescriptor field : fields) {
					String fieldName = field.getName();
					if (fieldName.equals(ParamConstant.REPOR_TTYPE)) {
						reportTypeValue = ReflectUtil
								.getField(param, fieldName);
					}
					if (fieldName.equals(ParamConstant.STAIR_CLASSIFY)) {
						stairClassifyValue = ReflectUtil.getField(param,
								fieldName);
					}
				}
				if ((null != reportTypeValue && reportTypeValue instanceof String)
						&& (null != stairClassifyValue && stairClassifyValue instanceof String)) {
					List<VoLteKpi> findVolteKpiByParam = voLTEKpiService
							.findVolteKpiByParam((String) reportTypeValue,
									(String) stairClassifyValue);
					for (VoLteKpi voLteKpi : findVolteKpiByParam) {
						kpiAliasSummaryFormula.put(voLteKpi.getAlias(),
								voLteKpi.getSummaryFormula());
						if(voLteKpi.getTotalSummaryFormula()!=null && !voLteKpi.getTotalSummaryFormula().equals("")){
							kpiAliasTotalSummaryFormula.put(voLteKpi.getAlias(), voLteKpi.getTotalSummaryFormula());
						}else{
							kpiAliasTotalSummaryFormula.put(voLteKpi.getAlias(), voLteKpi.getSummaryFormula());
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 拼接公式和别名处理
	 * 
	 * @param nameEns
	 *            多个以逗号分隔
	 * @return
	 */
	protected String alias(String alias) {
		StringBuffer buffer = new StringBuffer();
		if (null != alias && !alias.isEmpty()) {
			if (kpiAliasSummaryFormula.containsKey(alias)) {
				buffer.append(kpiAliasSummaryFormula.get(alias));
				buffer.append(ParamConstant.SPACE);
				buffer.append(alias);
			} else {
				buffer.append(alias);
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 拼接汇总公式和别名处理
	 * 
	 * @param nameEns
	 *            多个以逗号分隔
	 * @return
	 */
	protected String aliasTotal(String alias) {
		StringBuffer buffer = new StringBuffer();
		if (null != alias && !alias.isEmpty()) {
			if (kpiAliasTotalSummaryFormula.containsKey(alias)) {
				buffer.append(kpiAliasTotalSummaryFormula.get(alias));
				buffer.append(ParamConstant.SPACE);
				buffer.append(alias);
			} else {
				buffer.append(alias);
			}
		}
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.common.service.sql.SelectService#getSelectResult()
	 */
	@Override
	public String getSelectResult() {
		return new StringBuffer().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.common.service.sql.SelectService#getSelect()
	 */
	@Override
	public String getSelect() {
		init();
		StringBuffer selectInfo = new StringBuffer();
		StringBuffer selectInfoTotal = new StringBuffer();
		try {
			PropertyDescriptor[] fields = BeanUtils
					.getPropertyDescriptors(param.getClass());
			if (null != fields || 0 != fields.length) {
				Object groupByValue = null;
				for (PropertyDescriptor field : fields) {
					String fieldName = field.getName();
					if (fieldName.equals(ParamConstant.GROUPBY_FIELD)) {
						groupByValue = ReflectUtil.getField(param, fieldName);
					}
				}
				if (null != groupByValue && groupByValue instanceof String) {
					selectInfo.append(groupByValue);
					selectInfo.append(ParamConstant.COMMA);
					selectInfoTotal.append(groupByValue);
					selectInfoTotal.append(ParamConstant.COMMA);
				}
			}
			for (Entry<String, String> entry : kpiAliasSummaryFormula
					.entrySet()) {
				String alias = alias(entry.getKey());
				String aliasTotal = aliasTotal(entry.getKey());
				if (StringUtils.hasText(alias)) {
					selectInfo.append(alias);
					selectInfo.append(ParamConstant.COMMA);
				}
				if (StringUtils.hasText(aliasTotal)) {
					selectInfoTotal.append(aliasTotal);
					selectInfoTotal.append(ParamConstant.COMMA);
				}
			}
			infoTotal = (-1 == selectInfoTotal.lastIndexOf(",")) ? selectInfoTotal.toString()
					: selectInfoTotal.substring(0, selectInfoTotal.lastIndexOf(","));
			System.out.println(selectInfo);
			System.out.println(infoTotal);
		} catch (Exception e) {
		}
		return -1 == selectInfo.lastIndexOf(",") ? selectInfo.toString()
				: selectInfo.substring(0, selectInfo.lastIndexOf(","));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.util.KpiUtilService#getKpiBelongToTables
	 * (java.lang.Object)
	 */
	@Override
	public Map<String, String> getKpiBelongToTables(Object kpisValue) {
		return new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.util.KpiUtilService#buildFormula(java.lang
	 * .String)
	 */
	@Override
	public String buildFormula(String kpiNameEn) {
		return new StringBuffer().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.util.KpiUtilService#getSelectKpis(java.
	 * lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getSelectKpis(Object kpisValue, Object indicationKpiValue,
			Object mergeKpisValue) {
		return new StringBuffer().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.util.KpiUtilService#mergeSeqKpis(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public String mergeSeqKpis(Object selectKpisInfo, Object mergeKpisValue) {
		return new StringBuffer().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.util.KpiUtilService#getNameChByKpis(java
	 * .lang.Object)
	 */
	@Override
	public String getNameChByKpis(Object kpisValue) {
		return new StringBuffer().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.util.KpiUtilService#getAliasSeqKpisByKpis
	 * (java.lang.Object)
	 */
	@Override
	public String getAliasSeqKpisByKpis(Object kpisValue) {
		return new StringBuffer().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.util.KpiUtilService#getKpiTypesBySeqKpis
	 * (java.lang.Object)
	 */
	@Override
	public List<String> getKpiTypesBySeqKpis(Object seqKpisValue) {
		return new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.util.KpiUtilService#getKpiTypesByKpis(java
	 * .lang.Object)
	 */
	@Override
	public List<String> getKpiTypesByKpis(Object kpisValue) {
		return new ArrayList<>();
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

	public String getInfoTotal() {
		return infoTotal;
	}

	public void setInfoTotal(String infoTotal) {
		this.infoTotal = infoTotal;
	}

}
