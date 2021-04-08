package com.datang.domain.testLogItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.testManage.terminal.Terminal;

/**
 * 单站日志表
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_DZ_TESTLOG_ITEM")
//@Table(name = "IADS_STATION_LOG")
public class StationVerificationLogPojo {

	private Long id;
	
	/**
	 * 日志id
	 */
	private Long recseqno;
	
	/**
	 * 文件名
	 */
	private String fileName;
	
	/**
	 * 日志来源
	 */
	private String logSource;
	
	private String boxId;
	
	private String cellName;
	
	/**
	 * 运营商
	 */
	private String operatorName;

	/**
	 * 文件大小
	 */
	private Float ftpByteSize;
	
	/**
	 * 是否解析
	 * 0：未解析
	 * 1：已解析
	 */
	private Integer testFileStatus;
	
	/**
	 * 是否生成报告
	 * 0：未生成
	 * 1：已生成
	 */
	private Integer reportStatus;
	

	/**
	 * 上传时间
	 */
	private Long uploadTime;
	
	
	private Long startDate;

	/**
	 * 测试时间
	 */
	private Long testTime;

	/**
	 * 相关小区
	 */
	private String correlativeCell;

	/**
	 * 测试业务
	 */
	private String testService;

	/**
	 * 无线状态
	 */
	private String wirelessStatus;
	
	/**
	 * 设备
	 */
	private Terminal terminal;
	
	/**
	 * 文件路径
	 */
	private String filelink;
	/**
	 * 区域
	 */
	private String region;
	
	/**
	 * 上传大小
	 */
	private String uploadedSize;
	
	/**
	 * 密钥
	 */
	private String tridesKey;
	
	// 版本号
	private String logVersion;
	
	// 测试级别 组织巡检、日常优化、设备调试(单站验证)
	private String testLevel;
	
	// 测试目标 DT、FP
	private String testTarget;
	
	// 是否达标 1：达标 0：不达标
	private Integer satifyTarget;
	
	
	private Long stationId;
	
	//private PlanParamPojo planParamPojo;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "LOG_SOURCE")
	public String getLogSource() {
		return logSource;
	}

	public void setLogSource(String logSource) {
		this.logSource = logSource;
	}

	@Column(name = "BOX_ID")
	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @return the cellName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	@Column(name = "OPERATOR")
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Column(name = "FTP_BYTESIZE")
	public Float getFtpByteSize() {
		return ftpByteSize;
	}

	public void setFtpByteSize(Float ftpByteSize) {
		this.ftpByteSize = ftpByteSize;
	}

	@Column(name = "TESTFILE_STATUS")
	public Integer getTestFileStatus() {
		return testFileStatus;
	}

	public void setTestFileStatus(Integer testFileStatus) {
		this.testFileStatus = testFileStatus;
	}

	@Column(name = "REPORT_STATUS")
	public Integer getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}
	
	@Column(name = "UPLOAD_TIME")
	public Long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Long uploadTime) {
		this.uploadTime = uploadTime;
	}

	@Column(name = "TEST_DATE")
	public Long getTestTime() {
		return testTime;
	}

	public void setTestTime(Long testTime) {
		this.testTime = testTime;
	}

	@Column(name = "CORRELATIVE_CELL")
	public String getCorrelativeCell() {
		return correlativeCell;
	}

	public void setCorrelativeCell(String correlativeCell) {
		this.correlativeCell = correlativeCell;
	}

	@Column(name = "NR_TESTEVENT")
	public String getTestService() {
		return testService;
	}

	public void setTestService(String testService) {
		this.testService = testService;
	}

	@Column(name = "WIRELESS_STATUS")
	public String getWirelessStatus() {
		return wirelessStatus;
	}

	public void setWirelessStatus(String wirelessStatus) {
		this.wirelessStatus = wirelessStatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOX_ID", referencedColumnName = "BOX_ID", insertable = false, updatable = false)
	@JSON(serialize = false)
	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	@Column(name = "FILE_LINK")
	public String getFilelink() {
		return filelink;
	}

	public void setFilelink(String filelink) {
		this.filelink = filelink;
	}

	@Column(name = "RECSEQNO")
	public Long getRecseqno() {
		return recseqno;
	}

	public void setRecseqno(Long recseqno) {
		this.recseqno = recseqno;
	}

	@Column(name = "REGION")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "UPLOADED_SIZE")
	public String getUploadedSize() {
		return uploadedSize;
	}

	public void setUploadedSize(String uploadedSize) {
		this.uploadedSize = uploadedSize;
	}

	@Column(name = "TRIDES_KEY")
	public String getTridesKey() {
		return tridesKey;
	}

	public void setTridesKey(String tridesKey) {
		this.tridesKey = tridesKey;
	}
	
	/**
	 * @return the logVersion
	 */
	@Column(name = "LOG_VERSION")
	public String getLogVersion() {
		return logVersion;
	}

	/**
	 * @param logVersion the logVersion to set
	 */
	public void setLogVersion(String logVersion) {
		this.logVersion = logVersion;
	}
	
	@Column(name = "STATION_ID")
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	
	/**
	 * @return the startDate
	 */
	@Column(name = "START_DATE")
	public Long getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the testLevel
	 */
	@Transient
	public String getTestLevel() {
		return testLevel;
	}

	/**
	 * @param testLevel the testLevel to set
	 */
	public void setTestLevel(String testLevel) {
		this.testLevel = testLevel;
	}

	/**
	 * @return the testTarget
	 */
	@Transient
	public String getTestTarget() {
		return testTarget;
	}

	/**
	 * @param testTarget the testTarget to set
	 */
	public void setTestTarget(String testTarget) {
		this.testTarget = testTarget;
	}

	/**
	 * @return the satifyTarget
	 */
	@Transient
	public Integer getSatifyTarget() {
		return satifyTarget;
	}

	/**
	 * @param satifyTarget the satifyTarget to set
	 */
	public void setSatifyTarget(Integer satifyTarget) {
		this.satifyTarget = satifyTarget;
	}


	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_ID")
	@JSON(serialize = false)
	public PlanParamPojo getPlanParamPojo(){
		return planParamPojo;
	}

	public void setPlanParamPojo(PlanParamPojo planParamPojo) {
		this.planParamPojo = planParamPojo;
	}*/
	
	
	
}
