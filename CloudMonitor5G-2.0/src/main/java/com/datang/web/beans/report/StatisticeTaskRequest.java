package com.datang.web.beans.report;

import java.io.Serializable;
import java.util.Date;

public class StatisticeTaskRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 274653069108830834L;

	private String prov;
	private String city;
	/**
	 * 0 通用模板 调用ZMQ 后台维护任务状态
	 * 1 分析模板 自己维护
	 * */
	private String reportType;
	private Long id;
	/**
	 * 创建人
	 */
	private String createrName;
	/**
	 * 任务名称
	 */
	private String name;
	/**
	 * 开始时间
	 */
	private Date beginDate;
	/**
	 * 结束时间
	 */
	private Date endDate;
	/**
	 * 测试级别 1组织巡检、2日常优化、3设备调试
	 */
	private String testRank;
	/**
	 * 汇总方式 1全国，2省级、3地市、4BOXID、5测试计划、6Log文件
	 */
	private String collectType;
	/**
	 * 关联的atu域
	 */
	private String cityIds;
	private String cityNames;
	/**
	 * 设备Id集合
	 */
	private String boxIds;
	/**
	 * Log文件名称集合
	 */
	private String logIds;
	
	/**
	 * 统计任务模板集合
	 */
	private String templateIds;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id to set
	 */

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the createrName
	 */
	public String getCreaterName() {
		return createrName;
	}

	/**
	 * @param
	 *            createrName to set
	 */

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param
	 *            name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param
	 *            beginDate to set
	 */

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param
	 *            endDate to set
	 */

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the testRank
	 */
	public String getTestRank() {
		return testRank;
	}

	/**
	 * @param
	 *            testRank to set
	 */

	public void setTestRank(String testRank) {
		this.testRank = testRank;
	}

	/**
	 * @return the collectType
	 */
	public String getCollectType() {
		return collectType;
	}

	/**
	 * @param
	 *            collectType to set
	 */

	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	/**
	 * @return the cityIds
	 */
	public String getCityIds() {
		return cityIds;
	}

	/**
	 * @param
	 *            cityIds to set
	 */

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	/**
	 * @return the boxIds
	 */
	public String getBoxIds() {
		return boxIds;
	}

	/**
	 * @param
	 *            boxIds to set
	 */

	public void setBoxIds(String boxIds) {
		this.boxIds = boxIds;
	}

	/**
	 * @return the logIds
	 */
	public String getLogIds() {
		return logIds;
	}

	/**
	 * @param
	 *            logIds to set
	 */

	public void setLogIds(String logIds) {
		this.logIds = logIds;
	}

	public String getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	}

	/**
	 * @return the cityNames
	 */
	public String getCityNames() {
		return cityNames;
	}

	/**
	 * @param
	 *            cityNames to set
	 */

	public void setCityNames(String cityNames) {
		this.cityNames = cityNames;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatisticeTaskRequest [id=" + id + ", createrName="
				+ createrName + ", name=" + name + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", testRank=" + testRank
				+ ", collectType=" + collectType + ", cityIds=" + cityIds
				+ ", cityNames=" + cityNames + ", boxIds=" + boxIds
				+ ", logIds=" + logIds + "]";
	}

	/* *
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	public static boolean typeIsAnylyFileReport(String reportType){
		// 分析日志
		if(reportType!=null && reportType.equals("1")) {
			return true;
		}
		return false;
	}

	public static String typeIsAnylyFileReport(boolean bool){
		// 分析日志
		if(bool) {
			return "1";
		}
		return "";
	}

}
