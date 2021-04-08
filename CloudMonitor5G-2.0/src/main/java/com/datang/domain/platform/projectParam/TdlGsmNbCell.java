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
 * TDL-GSM邻区表
 * 
 * @author yinzhipeng
 * @date:2015年10月19日 上午11:46:48
 * @version
 */
@Entity
@Table(name = "IADS_TDL_GSM_NBCELL")
public class TdlGsmNbCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8592925605839248587L;
	private Long id;
	/**
	 * 小区信息表
	 */
	private CellInfo cellInfo;

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
	 * CellName选填:小区友好名
	 */
	private String cellName;
	/**
	 * CELL ID必填:小区标识
	 */
	private Long cellId;

	/**
	 * 邻区LAC必填:邻区所在位置区的位置区域码
	 */
	private String nbLac;
	/**
	 * 邻区CI必填:邻区小区标识。LAC内唯一
	 */
	private String nbCi;

	/**
	 * 邻区CellName 选填:邻区小区友好名
	 */
	private String nbCellName;
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
	 * @return the nbLacnbLac
	 */
	@Column(name = "NB_LAC")
	public String getnbLac() {
		return nbLac;
	}

	/**
	 * @param nbLac
	 *            the nbLac to set
	 */
	public void setnbLac(String nbLac) {
		this.nbLac = nbLac;
	}

	/**
	 * @return the nbCinbCi
	 */
	@Column(name = "NB_CI")
	public String getnbCi() {
		return nbCi;
	}

	/**
	 * @param nbCi
	 *            the nbCi to set
	 */
	public void setnbCi(String nbCi) {
		this.nbCi = nbCi;
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
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	@Column(name = "REGION")
	public void setRegion(String region) {
		this.region = region;
	}

}
