package com.datang.domain.customTemplate;

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
 * 自定义日志统计任务实体
 * 
 * @explain
 * @name CustomLogReportTask
 */
@Entity
@Table(name = "IADS_CUSTOM_REPORT_TASK")
public class CustomLogReportTask implements Serializable {
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
	private String createrName = "admin";
	/**
	 * 任务名称
	 */
	private String name;
	
	/**
	 * 创建时间
	 */
	private Date creatDate;
	private Long creatDateLong;
	/**
	 * Log文件名称集合
	 */
	private String logIds;
	
	/**
	 * 任务状态
	 */
	private String taskStatus;

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
	 * @param the
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
	public Long getCreatDateLong() {
		return creatDateLong;
	}

	/**
	 * @param the
	 *            creatDateLong to set
	 */

	public void setCreatDateLong(Long creatDateLong) {
		this.creatDateLong = creatDateLong;
	}

	/**
	 * @return the logIds
	 */
	@Column(name = "LOGIDS", nullable = false, length=4000)
	@JSON(serialize = false)
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


	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Override
	public String toString() {
		return "CustomLogReportTask [id=" + id + ", createrName=" + createrName + ", name=" + name + ", creatDate="
				+ creatDate + ", creatDateLong=" + creatDateLong + ", logIds=" + logIds + ", taskStatus=" + taskStatus
				+ "]";
	}
	

}
