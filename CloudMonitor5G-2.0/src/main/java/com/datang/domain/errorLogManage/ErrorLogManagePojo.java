package com.datang.domain.errorLogManage;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 错误日志管理表
 * @author maxuancheng
 * @date 2019年5月30日
 */
@Entity
@Table(name = "IADS_5G_ERROR_LOG_MANAGE")
public class ErrorLogManagePojo implements Serializable{

	/**
	 * @author maxuancheng
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String city;
	
	private String boxid;
	
	private Date uploadTime;
	
	private String macAdress;
	
	private float lon;
	
	private float lat;
	
	private String softwareV;
	
	private String osV;
	
	private String terminalV;
	
	private int terminalType;//终端类型，1 APP，2 Outum
	
	private Date softwareStartTime;
	
	private Date softwareEndTime;
	
	private String fileName;
	
	private String terminalNumber; //终端型号

	@Column(name = "ID")
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 所属省市
	 */
	@Column(name = "CITY")
	public String getCity() {
		return city;
	}
	
	/**
	 * 所属省市
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * 设备id
	 */
	@Column(name = "BOXID")
	public String getBoxid() {
		return boxid;
	}
	
	/**
	 * 设备id
	 */
	public void setBoxid(String boxid) {
		this.boxid = boxid;
	}
	
	/**
	 * 上报时间
	 * @return
	 */
	@Column(name = "UPLOAD_TIME")
	public Date getUploadTime() {
		return uploadTime;
	}
	
	/**
	 * 上报时间
	 * @return
	 */
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * MAC地址
	 */
	@Column(name = "MAC_ADRESS")
	public String getMacAdress() {
		return macAdress;
	}
	
	/**
	 * MAC地址
	 */
	public void setMacAdress(String macAdress) {
		this.macAdress = macAdress;
	}
	
	/**
	 * 经度
	 */
	@Column(name = "LON")
	public float getLon() {
		return lon;
	}
	
	/**
	 * 经度
	 */
	public void setLon(float lon) {
		this.lon = lon;
	}
	
	/**
	 *  纬度
	 */
	@Column(name = "LAT")
	public float getLat() {
		return lat;
	}
	
	/**
	 * 纬度
	 */
	public void setLat(float lat) {
		this.lat = lat;
	}
	
	/**
	 * 软件版本号
	 */
	@Column(name = "SOFTWARE_V")
	public String getSoftwareV() {
		return softwareV;
	}
	
	/**
	 * 软件版本号
	 */
	public void setSoftwareV(String softwareV) {
		this.softwareV = softwareV;
	}
	
	/**
	 * 操作系统版本号
	 */
	@Column(name = "OS_V")
	public String getOsV() {
		return osV;
	}
	
	/**
	 * 操作系统版本号
	 */
	public void setOsV(String osV) {
		this.osV = osV;
	}
	
	/**
	 * 终端版本号
	 */
	@Column(name = "TERMINAL_V")
	public String getTerminalV() {
		return terminalV;
	}
	
	/**
	 * 终端版本号
	 */
	public void setTerminalV(String terminalV) {
		this.terminalV = terminalV;
	}
	
	/**
	 * 终端类型 1 APP,2 Outum
	 */
	@Column(name = "TERMINAL_TYPE")
	public int getTerminalType() {
		return terminalType;
	}
	
	/**
	 * 终端类型 1 APP,2 Outum
	 */
	public void setTerminalType(int terminalType) {
		this.terminalType = terminalType;
	}
	
	/**
	 * 软件开始时间
	 */
	@Column(name = "SOFTWARE_START_TIME")
	public Date getSoftwareStartTime() {
		return softwareStartTime;
	}
	
	/**
	 * 软件开始时间
	 */
	public void setSoftwareStartTime(Date softwareStartTime) {
		this.softwareStartTime = softwareStartTime;
	}
	
	/**
	 * 软件结束时间
	 */
	@Column(name = "SOFTWARE_END_TIME")
	public Date getSoftwareEndTime() {
		return softwareEndTime;
	}
	
	/**
	 * 软件结束时间
	 */
	public void setSoftwareEndTime(Date softwareEndTime) {
		this.softwareEndTime = softwareEndTime;
	}

	/**
	 * 文件名
	 */
	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	/**
	 * 文件名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 终端型号
	 */
	@Column(name = "TERMINAL_NUMBER")
	public String getTerminalNumber() {
		return terminalNumber;
	}
	/**
	 * 终端型号
	 */
	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
	}
	
	
}
