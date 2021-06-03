package com.datang.domain.customTemplate;

import lombok.Data;
import org.apache.struts2.json.annotations.JSON;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义日志统计任务实体
 * 
 * @explain
 * @name CustomLogReportTask
 */
@Entity
@Table(name = "IADS_ANALY_FILE_REPORT")
public class AnalyFileReport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 204653069102830834L;
	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 文件路径
	 * */
	private String filePath;

	/**
	 * 任务id
	 * */
	private Long taskId;

	/**
	 * 报表id
	 * */
	private String reportId;


	private String description;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
