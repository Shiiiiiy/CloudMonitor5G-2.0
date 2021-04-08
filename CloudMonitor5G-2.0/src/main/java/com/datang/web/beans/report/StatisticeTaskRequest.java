package com.datang.web.beans.report;

import java.io.Serializable;
import java.util.Date;

public class StatisticeTaskRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 274653069108830834L;
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
	 * @param the
	 *            id to set
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
	 * @param the
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
	 * @param the
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
	 * @param the
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
	 * @param the
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
	 * @param the
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
	 * @param the
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
	 * @param the
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
	 * @param the
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
	 * @param the
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
	 * @param the
	 *            cityNames to set
	 */

	public void setCityNames(String cityNames) {
		this.cityNames = cityNames;
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

}
