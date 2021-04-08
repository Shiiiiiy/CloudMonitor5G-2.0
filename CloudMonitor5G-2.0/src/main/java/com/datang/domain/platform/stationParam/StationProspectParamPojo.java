package com.datang.domain.platform.stationParam;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.planParam.PlanParamPojo;

import lombok.Data;

/**
 * 基站勘察表
 * @author lucheng
 *
 */
@Entity
@Table(name = "IADS_STATION_PROSPECT_PARAM")
public class StationProspectParamPojo {

	private Long id;

	/**
	 * 基站名称
	 */
	private  String siteName;
	
	/**
	 * 经度
	 */
	private  String longitude;
	
	
	/** 
	 * 纬度
	 */
	private  String latitude;
	
	/**
	 * 建筑物功能
	 */
	private String buildingFunction;
	
	
	/**
	 * 地址
	 */
	private String address;
	
	
	/**
	 * 1小区天馈方位角
	 */
	private Integer cell1Azimuth;
	
	
	/**
	 * 2小区天馈方位角
	 */
	private Integer cell2Azimuth;
	
	
	/**
	 * 3小区天馈方位角
	 */
	private Integer cell3Azimuth;
	
	
	/**
	 * 1小区机械俯仰角
	 */
	private Integer cell1TiltM;
	
	
	/**
	 * 2小区机械俯仰角
	 */
	private Integer cell2TiltM;
	
	
	/**
	 * 3小区机械俯仰角
	 */
	private Integer cell3TiltM;
	
	/**
	 * 1小区电子俯仰角
	 */
	private Integer cell1TilE;
	
	
	/**
	 * 2小区天馈电子角
	 */
	private Integer cell2TiltE;
	
	
	/**
	 * 3小区电子俯仰角
	 */
	private Integer cell3TiltE;
	
	
	/**
	 * 站高
	 */
	private Integer height;
	
	
	/**
	 * 天线厂家
	 */
	private String  antennaManufacturer;
	
	
	/**
	 * AAU型号
	 */
	private String  aauModel;
	
	
	/**
	 * 天线类型
	 */
	private String  antennaType;
	
	
	
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SITE_NAME")
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Column(name = "LONGITUDE")
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE")
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}


	@Column(name = "BUILDING_FUNCTION")
	public String getBuildingFunction() {
		return buildingFunction;
	}

	public void setBuildingFunction(String buildingFunction) {
		this.buildingFunction = buildingFunction;
	}


	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	@Column(name = "CEELL1_AZIMUTH")
	public Integer getCell1Azimuth() {
		return cell1Azimuth;
	}

	public void setCell1Azimuth(Integer cell1Azimuth) {
		this.cell1Azimuth = cell1Azimuth;
	}


	@Column(name = "CEELL2_AZIMUTH")
	public Integer getCell2Azimuth() {
		return cell2Azimuth;
	}

	public void setCell2Azimuth(Integer cell2Azimuth) {
		this.cell2Azimuth = cell2Azimuth;
	}

	@Column(name = "CEELL3_AZIMUTH")
	public Integer getCell3Azimuth() {
		return cell3Azimuth;
	}

	public void setCell3Azimuth(Integer cell3Azimuth) {
		this.cell3Azimuth = cell3Azimuth;
	}


	@Column(name = "CEELL1_TILTM")
	public Integer getCell1TiltM() {
		return cell1TiltM;
	}

	public void setCell1TiltM(Integer cell1TiltM) {
		this.cell1TiltM = cell1TiltM;
	}


	@Column(name = "CEELL2_TILTM")
	public Integer getCell2TiltM() {
		return cell2TiltM;
	}

	public void setCell2TiltM(Integer cell2TiltM) {
		this.cell2TiltM = cell2TiltM;
	}

	@Column(name = "CEELL3_TILTM")
	public Integer getCell3TiltM() {
		return cell3TiltM;
	}

	public void setCell3TiltM(Integer cell3TiltM) {
		this.cell3TiltM = cell3TiltM;
	}

	/**
	 * @return the cell1TilE
	 */
	@Column(name = "CEELL1_TILTE")
	public Integer getCell1TilE() {
		return cell1TilE;
	}

	/**
	 * @param cell1TilE the cell1TilE to set
	 */
	public void setCell1TilE(Integer cell1TilE) {
		this.cell1TilE = cell1TilE;
	}

	/**
	 * @return the cell2TiltE
	 */
	@Column(name = "CEELL2_TILTE")
	public Integer getCell2TiltE() {
		return cell2TiltE;
	}

	/**
	 * @param cell2TiltE the cell2TiltE to set
	 */
	public void setCell2TiltE(Integer cell2TiltE) {
		this.cell2TiltE = cell2TiltE;
	}

	/**
	 * @return the cell3TiltE
	 */
	@Column(name = "CEELL3_TILTE")
	public Integer getCell3TiltE() {
		return cell3TiltE;
	}

	/**
	 * @param cell3TiltE the cell3TiltE to set
	 */
	public void setCell3TiltE(Integer cell3TiltE) {
		this.cell3TiltE = cell3TiltE;
	}

	@Column(name = "HEIGHT")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "ANTENNA_MANUFACTURER")
	public String getAntennaManufacturer() {
		return antennaManufacturer;
	}

	public void setAntennaManufacturer(String antennaManufacturer) {
		this.antennaManufacturer = antennaManufacturer;
	}

	@Column(name = "AAU_MODEL")
	public String getAauModel() {
		return aauModel;
	}

	public void setAauModel(String aauModel) {
		this.aauModel = aauModel;
	}


	@Column(name = "ANTENNA_TYPE")
	public String getAntennaType() {
		return antennaType;
	}

	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}

	

}
