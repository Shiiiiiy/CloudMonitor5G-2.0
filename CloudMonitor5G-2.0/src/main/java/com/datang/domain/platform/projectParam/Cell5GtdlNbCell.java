/**
 * 
 */
package com.datang.domain.platform.projectParam;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 5G-LTE邻区
 * @author _YZP
 *
 */
@Entity
@Table(name = "IADS_5G_TDL_NBCELL")
public class Cell5GtdlNbCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4573144455756000634L;
	
	private Long id;
	/**
	 * 小区信息表
	 */
	private CellInfo cellInfo;

	/**
	 * AMF Group ID	选填	:AMF组标识（暂定）
	 */
	private String amfGroupId;
	/**
	 * AMF ID选填:AMF编号（暂定）
	 */
	private String amfId;
	/**
	 * gNB ID选填:基站设备唯一标识，用于标识一个PLMN所管辖区域范围内的gNB
	 */
	private String gNBId;
	/**
	 * CellName选填:小区友好名
	 */
	private String cellName;
	/**
	 * CELL ID必填:小区标识
	 */
	private Long cellId;
	
	/**
	 * MME Group ID选填:MME组标识
	 */
	private String mmeGroupId;
	/**
	 * MME ID 选填:MME编号
	 */
	private String mmeId;
	/**
	 * eNB ID选填: eNB编号
	 */
	private String eNBId;
	
	/**
	 * 邻区CellName 选填:邻区小区友好名
	 */
	private String nbCellName;
	/**
	 * 邻区CELL ID 必填:邻小区标识
	 */
	private Long nbCellId;
	/**
	 * Region 程序填:所属二级域，如徐汇区、黄浦区
	 */
	private String region;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "AMF_GROUP_ID")
	public String getAmfGroupId() {
		return amfGroupId;
	}

	public void setAmfGroupId(String amfGroupId) {
		this.amfGroupId = amfGroupId;
	}

	@Column(name = "AMF_ID")
	public String getAmfId() {
		return amfId;
	}

	public void setAmfId(String amfId) {
		this.amfId = amfId;
	}

	@Column(name = "GNB_ID")
	public String getgNBId() {
		return gNBId;
	}

	public void setgNBId(String gNBId) {
		this.gNBId = gNBId;
	}

	/**
	 * @return the cellNamecellName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName
	 *            the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * @return the cellIdcellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}
	

	/**
	 * @return the mmeGroupIdmmeGroupId
	 */
	@Column(name = "MME_GROUP_ID")
	public String getMmeGroupId() {
		return mmeGroupId;
	}

	/**
	 * @param mmeGroupId
	 *            the mmeGroupId to set
	 */
	public void setMmeGroupId(String mmeGroupId) {
		this.mmeGroupId = mmeGroupId;
	}

	/**
	 * @return the mmeIdmmeId
	 */
	@Column(name = "MME_ID")
	public String getMmeId() {
		return mmeId;
	}

	/**
	 * @param mmeId
	 *            the mmeId to set
	 */
	public void setMmeId(String mmeId) {
		this.mmeId = mmeId;
	}

	/**
	 * @return the eNBIdeNBId
	 */
	@Column(name = "ENB_ID")
	public String geteNBId() {
		return eNBId;
	}

	/**
	 * @param eNBId
	 *            the eNBId to set
	 */
	public void seteNBId(String eNBId) {
		this.eNBId = eNBId;
	}
	/**
	 * @return the nbCellNamenbCellName
	 */
	@Column(name = "NB_CELL_NAME")
	public String getNbCellName() {
		return nbCellName;
	}

	/**
	 * @param nbCellName
	 *            the nbCellName to set
	 */
	public void setNbCellName(String nbCellName) {
		this.nbCellName = nbCellName;
	}

	/**
	 * @return the nbCellIdnbCellId
	 */
	@Column(name = "NB_CELL_ID")
	public Long getNbCellId() {
		return nbCellId;
	}

	/**
	 * @param nbCellId
	 *            the nbCellId to set
	 */
	public void setNbCellId(Long nbCellId) {
		this.nbCellId = nbCellId;
	}

	/**
	 * @return the cellInfocellInfo
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CI_ID")
	public CellInfo getCellInfo() {
		return cellInfo;
	}

	/**
	 * @param cellInfo
	 *            the cellInfo to set
	 */
	public void setCellInfo(CellInfo cellInfo) {
		this.cellInfo = cellInfo;
	}

	/**
	 * @return the regionregion
	 */
	@Column(name = "REGION")
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}


}
