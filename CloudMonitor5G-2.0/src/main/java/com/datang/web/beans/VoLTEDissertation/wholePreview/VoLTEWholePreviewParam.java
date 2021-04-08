/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.wholePreview;

import java.io.Serializable;

/**
 * VoLte专题分析-----volte整体概览参数
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 上午9:16:01
 * @version
 */
public class VoLTEWholePreviewParam implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9007066526750021980L;

	/**
	 * 报表类型
	 */
	private String reportType;

	/**
	 * 一级分类
	 */
	private String stairClassify;

	/**
	 * 测试日志id集合
	 */
	private String testLogItemIds;

	/**
	 * 字段分组
	 */
	private String groupByField;

	/**
	 * 查询表名
	 */
	private String queryTable;

	/**
	 * @return the groupByFieldgroupByField
	 */
	public String getGroupByField() {
		return groupByField;
	}

	/**
	 * @param groupByField
	 *            the groupByField to set
	 */
	public void setGroupByField(String groupByField) {
		this.groupByField = groupByField;
	}

	/**
	 * @return the queryTablequeryTable
	 */
	public String getQueryTable() {
		return queryTable;
	}

	/**
	 * @param queryTable
	 *            the queryTable to set
	 */
	public void setQueryTable(String queryTable) {
		this.queryTable = queryTable;
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
	 * @return the testLogItemIdstestLogItemIds
	 */
	public String getTestLogItemIds() {
		return testLogItemIds;
	}

	/**
	 * @param testLogItemIds
	 *            the testLogItemIds to set
	 */
	public void setTestLogItemIds(String testLogItemIds) {
		this.testLogItemIds = testLogItemIds;
	}

	public VoLTEWholePreviewParam() {
		super();
	}

	/**
	 * clone method
	 */
	public Object clone() {

		VoLTEWholePreviewParam o = null;
		try {
			o = (VoLTEWholePreviewParam) super.clone();
		} catch (CloneNotSupportedException e) {
			o = new VoLTEWholePreviewParam();
		}
		return o;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VoLTEWholePreviewParam [reportType=" + reportType
				+ ", stairClassify=" + stairClassify + ", testLogItemIds="
				+ testLogItemIds + ", groupByField=" + groupByField
				+ ", queryTable=" + queryTable + "]";
	}

}
