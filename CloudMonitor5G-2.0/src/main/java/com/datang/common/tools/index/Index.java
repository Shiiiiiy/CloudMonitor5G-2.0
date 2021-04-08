package com.datang.common.tools.index;

import java.io.Serializable;

import com.datang.common.tools.annotation.Necessary;

public class Index implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7027507424381838038L;
	// kpiTitles.put("二级分类", "stair2Classify");
	// kpiTitles.put("三级分类", "stair3Classify");

	// kpiTitles.put("计算公式", "formula");
	// kpiTitles.put("KPI定义", "kpiDefinition");

	// kpiTitles.put("英文汇总公式(开发使用)", "totalSummaryFormula");
	// kpiTitles.put("公式备注", "formulaRemark");
	/**
	 * 指标序号
	 */
	@Necessary(id = 0)
	private String id;

	/**
	 * KPI编号
	 */
	private String kpiCode;
	/**
	 * 指标名称
	 */
	@Necessary(id = 1)
	private String nameCh;
	/**
	 * 英文名称
	 */
	@Necessary(id = 2)
	private String nameEn;

	/**
	 * 报表类型
	 */
	private String reportType;

	/**
	 * 一级分类
	 */
	private String stairClassify;
	/**
	 * 数据单位
	 */
	private String unit;

	/**
	 * 英文名称(开发使用)
	 */
	@Necessary(id = 3)
	private String alias;

	/**
	 * 英文公式(开发使用)
	 */
	private String summaryFormula;

	/**
	 * visible
	 */
	@Necessary(id = 4)
	private String visible;
	/**
	 * 英文汇总公式(开发使用)
	 */
	private String totalSummaryFormula;

	/**
	 * @return the idid
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the kpiCodekpiCode
	 */
	public String getKpiCode() {
		return kpiCode;
	}

	/**
	 * @param kpiCode
	 *            the kpiCode to set
	 */
	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
	}

	/**
	 * @return the nameChnameCh
	 */
	public String getNameCh() {
		return nameCh;
	}

	/**
	 * @param nameCh
	 *            the nameCh to set
	 */
	public void setNameCh(String nameCh) {
		this.nameCh = nameCh;
	}

	/**
	 * @return the nameEnnameEn
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn
	 *            the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * @return the reportTypereportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType
	 *            the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * @return the stairClassifystairClassify
	 */
	public String getStairClassify() {
		return stairClassify;
	}

	/**
	 * @param stairClassify
	 *            the stairClassify to set
	 */
	public void setStairClassify(String stairClassify) {
		this.stairClassify = stairClassify;
	}

	/**
	 * @return the unitunit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the aliasalias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the summaryFormulasummaryFormula
	 */
	public String getSummaryFormula() {
		return summaryFormula;
	}

	/**
	 * @param summaryFormula
	 *            the summaryFormula to set
	 */
	public void setSummaryFormula(String summaryFormula) {
		this.summaryFormula = summaryFormula;
	}

	/**
	 * @return the visiblevisible
	 */
	public String getVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}

	/**
	 * @return the totalSummaryFormulatotalSummaryFormula
	 */
	public String getTotalSummaryFormula() {
		return totalSummaryFormula;
	}

	/**
	 * @param totalSummaryFormula
	 *            the totalSummaryFormula to set
	 */
	public void setTotalSummaryFormula(String totalSummaryFormula) {
		this.totalSummaryFormula = totalSummaryFormula;
	}

	/**
	 * @return the serialversionuidserialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
