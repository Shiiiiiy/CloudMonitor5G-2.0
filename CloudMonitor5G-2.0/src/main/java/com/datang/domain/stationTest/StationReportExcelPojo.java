package com.datang.domain.stationTest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import lombok.Data;

/**
 * 生成的单验报告保存列表
 * @author LUCHENG
 *
 */
@Entity
@Table(name = "IADS_STATION_REPORT_EXCEL")
public class StationReportExcelPojo {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 基站名称
	 */
	@Column(name = "SITE_NAME")
	private String siteName; 
	
	/**
	 * 区域
	 */
	@Column(name = "region")
	private String region; 
	
	/**
	 * 单站报告名称
	 */
	@Column(name = "REPORT_NAME")
	private String reportName; 
	
	/**
	 * 单站报告保存路径
	 */
	@Column(name = "REPORT_PATH")
	private String reportPath; 
	
	/**
	 * 报告生成时间
	 */
	@Column(name = "REPORT_CREAT_DATE")
	private String reportCreatDate; 
	
	
	/**
	 * 制式 "NR"或"LTE"
	 */
	@Column(name = "MODEL")
	private String model;
	
	/**
	 * 备注
	 */
	@Column(name = "COMMENTS")
	private String comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@JSON(serialize = false)
	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getReportCreatDate() {
		return reportCreatDate;
	}

	public void setReportCreatDate(String reportCreatDate) {
		this.reportCreatDate = reportCreatDate;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
