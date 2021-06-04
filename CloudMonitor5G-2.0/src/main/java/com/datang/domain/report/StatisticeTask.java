package com.datang.domain.report;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 统计任务实体
 * 
 * @explain
 * @name ReportTask
 * @author shenyanwei
 * @date 2016年9月8日下午5:08:36
 */
@Entity
@Table(name = "IADS_STATISTICE_TASK")
public class StatisticeTask implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 274653069108830834L;
	/**
	 * 主键
	 */
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
	private Long beginDateLong;
	/**
	 * 结束时间
	 */
	private Date endDate;
	private Long endDateLong;
	/**
	 * 创建时间
	 */
	private Date creatDate;
	private Long creatDateLong;
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
	 * 任务状态
	 */
	private String taskStatus;
	
	/**
	 * 测试分组，地级域
	 */
	private String terminalGroup;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param
	 *            id to set
	 */

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the createrName
	 */
	@Column(name = "CREATERNAME")
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
	@Column(name = "NAME")
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
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public String getBeginDate() {
		if (beginDateLong != null) {
			this.beginDate = new Date(beginDateLong);
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String format = dateFormat.format(this.beginDate);
			return format;
		}
		return null;
	}

	/**
	 * @param
	 *            beginDate to set
	 */

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
		if (null != beginDate) {
			this.beginDateLong = beginDate.getTime();
		}
	}

	/**
	 * @return the beginDateLong
	 */
	@Column(name = "BEGINDATELONG")
	public Long getBeginDateLong() {
		if (beginDateLong != null) {
			this.beginDate = new Date(beginDateLong);
		}
		return beginDateLong;
	}

	/**
	 * @param
	 *            beginDateLong to set
	 */

	public void setBeginDateLong(Long beginDateLong) {
		this.beginDateLong = beginDateLong;
	}

	/**
	 * @return the endDate
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public String getEndDate() {
		if (endDateLong != null) {
			this.endDate = new Date(endDateLong);
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String format = dateFormat.format(this.endDate);
			return format;
		}
		return null;
	}

	/**
	 * @param
	 *            endDate to set
	 */

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		if (null != endDate) {
			this.endDateLong = endDate.getTime();
		}
	}

	/**
	 * @return the endDateLong
	 */
	@Column(name = "ENDDATELONG")
	public Long getEndDateLong() {
		if (endDateLong != null) {
			this.endDate = new Date(endDateLong);
		}
		return endDateLong;
	}

	/**
	 * @param
	 *            endDateLong to set
	 */

	public void setEndDateLong(Long endDateLong) {
		this.endDateLong = endDateLong;
	}

	/**
	 * @return the creatDate
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getCreatDate() {
		if (creatDateLong != null) {
			this.creatDate = new Date(creatDateLong);
		}
		return creatDate;
	}

	/**
	 * @param
	 *            creatDate to set
	 */

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
		if (null != creatDate) {
			this.creatDateLong = creatDate.getTime();
		}
	}

	/**
	 * @return the creatDateLong
	 */
	@Column(name = "CREATDATELONG")
	public Long getCreatDateLong() {
		return creatDateLong;
	}

	/**
	 * @param
	 *            creatDateLong to set
	 */

	public void setCreatDateLong(Long creatDateLong) {
		this.creatDateLong = creatDateLong;
	}

	/**
	 * @return the testRank
	 */
	@Column(name = "TESTRANK")
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
	@Column(name = "COLLECTTYPE")
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
	// @ElementCollection(fetch = FetchType.LAZY, // 加载策略,延迟加载
	// targetClass = String.class)
	// // 指定集合中元素的类型
	// @CollectionTable(name = "CITYIDS_INFO")
	// // 指定集合生成的表
	// @OrderColumn(name = "C_ID")
	// // 指定排序列的名称
	//
	@Column(name = "CITYIDS")
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
	@Column(name = "BOXIDS")
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
	@Column(name = "LOGIDS")
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

	@Column(name = "TEMPLATEIDS")
	public String getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	}

	@Column(name = "TASKSTATUS")
	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @return the terminalGroup
	 */
	@Column(name = "TERMINALGROUP")
	public String getTerminalGroup() {
		return terminalGroup;
	}

	/**
	 * @param
	 *            terminalGroup to set
	 */

	public void setTerminalGroup(String terminalGroup) {
		this.terminalGroup = terminalGroup;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatisticeTask [id=" + id + ", createrName=" + createrName
				+ ", name=" + name + ", beginDate=" + beginDate
				+ ", beginDateLong=" + beginDateLong + ", endDate=" + endDate
				+ ", endDateLong=" + endDateLong + ", creatDate=" + creatDate
				+ ", creatDateLong=" + creatDateLong + ", testRank=" + testRank
				+ ", collectType=" + collectType + ", cityIds=" + cityIds
				+ ", boxIds=" + boxIds + ", logIds=" + logIds
				+ ", terminalGroup=" + terminalGroup + "]";
	}

}
