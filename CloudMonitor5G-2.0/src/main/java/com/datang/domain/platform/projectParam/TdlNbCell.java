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
 * TDL邻区表
 * 
 * @author yinzhipeng
 * @date:2015年10月19日 上午11:46:17
 * @version
 */
@Entity
@Table(name = "IADS_TDL_NBCELL")
public class TdlNbCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -205409504898964053L;
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
	 * 邻区MME Group ID选填:邻区MME组标识
	 */
	private String nbMmeGroupId;
	/**
	 * 邻区MME ID选填:邻区MME编号
	 */
	private String nbMmeId;
	/**
	 * 邻区eNB ID 选填:邻区eNB编号
	 */
	private String nbENBId;
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
	 * @return the nbMmeGroupIdnbMmeGroupId
	 */
	@Column(name = "NB_MME_GROUP_ID")
	public String getNbMmeGroupId() {
		return nbMmeGroupId;
	}

	/**
	 * @param nbMmeGroupId
	 *            the nbMmeGroupId to set
	 */
	public void setNbMmeGroupId(String nbMmeGroupId) {
		this.nbMmeGroupId = nbMmeGroupId;
	}

	/**
	 * @return the nbMmeIdnbMmeId
	 */
	@Column(name = "NB_MME_ID")
	public String getNbMmeId() {
		return nbMmeId;
	}

	/**
	 * @param nbMmeId
	 *            the nbMmeId to set
	 */
	public void setNbMmeId(String nbMmeId) {
		this.nbMmeId = nbMmeId;
	}

	/**
	 * @return the nbENBIdnbENBId
	 */
	@Column(name = "NB_ENB_ID")
	public String getNbENBId() {
		return nbENBId;
	}

	/**
	 * @param nbENBId
	 *            the nbENBId to set
	 */
	public void setNbENBId(String nbENBId) {
		this.nbENBId = nbENBId;
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
