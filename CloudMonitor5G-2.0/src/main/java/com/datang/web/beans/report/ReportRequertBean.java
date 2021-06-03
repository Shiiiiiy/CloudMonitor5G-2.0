package com.datang.web.beans.report;

import java.util.Date;

/**
 * 
 * 
 * @explain
 * @name AlarmRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:36:04
 */
public class ReportRequertBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176078388182457911L;
	/**
	 * 开始时间
	 */
	public Date beginDate;
	/**
	 * 结束时间
	 */
	public Date endDate;
	/** 创建人 */
	private String createrName;
	/** 任务名称 */
	private String name;


	private String taskType;

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

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportRequertBean [beginDate=" + beginDate + ", endDate="
				+ endDate + ", createrName=" + createrName + ", name=" + name
				+ "]";
	}

}
