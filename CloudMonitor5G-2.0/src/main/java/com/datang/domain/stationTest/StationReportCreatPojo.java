package com.datang.domain.stationTest;

import java.util.Date;

import com.datang.domain.planParam.PlanParamPojo;

public class StationReportCreatPojo {
	
	private Long id;
	
	/**
	 * 报告是否生成 
	 */
	private Integer reportStatus;//0：未生成 ；1：生成；
	/**
	 * 报告生成 日期
	 */
	private Date reportCreatDate;
	/**
	 * 好点ftp上传
	 */
	private Integer goodFtpUpload;//0：未完成  1：完成  2：有更新
	/**
	 * 中点ftp上传
	 */
	private Integer midFtpUpload;//0：未完成  1：完成  2：有更新 
	/**
	 * 差点ftp上传
	 */
	private Integer badFtpUpload;//0：未完成  1：完成  2：有更新 
	/**
	 * 好点ftp下载
	 */
	private Integer goodFtpdownload;//0：未完成  1：完成  2：有更新
	/**
	 * 中点ftp下载
	 */
	private Integer midFtpdownload;//0：未完成  1：完成  2：有更新 
	/**
	 * 差点ftp下载
	 */
	private Integer badFtpdownload;//0：未完成  1：完成  2：有更新 
	/**
	 * 绕点测试
	 */
	private Integer raodianTest;//0：未完成  1：完成  2：有更新
	/**
	 * ping32好点
	 */
	private Integer goodPing32;//0：未完成  1：完成  2：有更新 
	/**
	 * ping1500好点
	 */
	private Integer goodPing1500;//0：未完成  1：完成  2：有更新 
	/**
	 * 好点ENDC成功率测试
	 */
	private Integer goodEndcSuccessRatio;//0：未完成  1：完成  2：有更新
	/**
	 * 中点ENDC成功率测试
	 */
	private Integer midEndcSuccessRatio;//0：未完成  1：完成  2：有更新 
	/**
	 * 差点ENDC成功率测试
	 */
	private Integer badEndcSuccessRatio;//0：未完成  1：完成  2：有更新 

	private PlanParamPojo planParamPojo;

	public PlanParamPojo getPlanParamPojo() {
		return planParamPojo;
	}

	public void setPlanParamPojo(PlanParamPojo planParamPojo) {
		this.planParamPojo = planParamPojo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}

	public Date getReportCreatDate() {
		return reportCreatDate;
	}

	public void setReportCreatDate(Date reportCreatDate) {
		this.reportCreatDate = reportCreatDate;
	}

	public Integer getGoodFtpUpload() {
		return goodFtpUpload;
	}

	public void setGoodFtpUpload(Integer goodFtpUpload) {
		this.goodFtpUpload = goodFtpUpload;
	}

	public Integer getMidFtpUpload() {
		return midFtpUpload;
	}

	public void setMidFtpUpload(Integer midFtpUpload) {
		this.midFtpUpload = midFtpUpload;
	}

	public Integer getBadFtpUpload() {
		return badFtpUpload;
	}

	public void setBadFtpUpload(Integer badFtpUpload) {
		this.badFtpUpload = badFtpUpload;
	}

	public Integer getGoodFtpdownload() {
		return goodFtpdownload;
	}

	public void setGoodFtpdownload(Integer goodFtpdownload) {
		this.goodFtpdownload = goodFtpdownload;
	}

	public Integer getMidFtpdownload() {
		return midFtpdownload;
	}

	public void setMidFtpdownload(Integer midFtpdownload) {
		this.midFtpdownload = midFtpdownload;
	}

	public Integer getBadFtpdownload() {
		return badFtpdownload;
	}

	public void setBadFtpdownload(Integer badFtpdownload) {
		this.badFtpdownload = badFtpdownload;
	}

	public Integer getRaodianTest() {
		return raodianTest;
	}

	public void setRaodianTest(Integer raodianTest) {
		this.raodianTest = raodianTest;
	}

	public Integer getGoodPing32() {
		return goodPing32;
	}

	public void setGoodPing32(Integer goodPing32) {
		this.goodPing32 = goodPing32;
	}

	public Integer getGoodPing1500() {
		return goodPing1500;
	}

	public void setGoodPing1500(Integer goodPing1500) {
		this.goodPing1500 = goodPing1500;
	}

	public Integer getGoodEndcSuccessRatio() {
		return goodEndcSuccessRatio;
	}

	public void setGoodEndcSuccessRatio(Integer goodEndcSuccessRatio) {
		this.goodEndcSuccessRatio = goodEndcSuccessRatio;
	}

	public Integer getMidEndcSuccessRatio() {
		return midEndcSuccessRatio;
	}

	public void setMidEndcSuccessRatio(Integer midEndcSuccessRatio) {
		this.midEndcSuccessRatio = midEndcSuccessRatio;
	}

	public Integer getBadEndcSuccessRatio() {
		return badEndcSuccessRatio;
	}

	public void setBadEndcSuccessRatio(Integer badEndcSuccessRatio) {
		this.badEndcSuccessRatio = badEndcSuccessRatio;
	}
	
}
