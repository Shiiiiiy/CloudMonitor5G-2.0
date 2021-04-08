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

@Entity
@Table(name = "IADS_LTE_5GCELL")
public class Lte5GCell implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id; 
	
	/**
	 * 必填	eNB编号
	 */
	private String enbId;

	/**
	 * 必填	小区标识
	 */
	private int celld; 

	/**
	 * 选填	小区友好名
	 */
	private String cellName;

	/**
	 * 选填	配对小区AMF组标识
	 */
	private String pairmfGroupId;

	/**
	 * 选填	配对小区AMF编号
	 */
	private int pairAmfId;

	/**
	 * 选填	配对小区gNB编号
	 */
	private int pairGnbId;

	/**
	 * 选填	配对小区友好名
	 */
	private String pairCellName;

	/**
	 * 必填	配对小小区标识
	 */
	private int pairCellId;

	/**
	 * 小区信息表
	 */
	private CellInfo cellInfo;
	
	/**
	 * 区域
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

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ENB_ID")
	public String getEnbId() {
		return enbId;
	}

	public void setEnbId(String enbId) {
		this.enbId = enbId;
	}

	@Column(name = "CELL_ID")
	public int getCelld() {
		return celld;
	}

	public void setCelld(int celld) {
		this.celld = celld;
	}

	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	@Column(name = "PAIR_AMF_GROUP_ID")
	public String getPairmfGroupId() {
		return pairmfGroupId;
	}

	public void setPairmfGroupId(String pairmfGroupId) {
		this.pairmfGroupId = pairmfGroupId;
	}

	@Column(name = "PAIR_AMF_ID")
	public int getPairAmfId() {
		return pairAmfId;
	}

	public void setPairAmfId(int pairAmfId) {
		this.pairAmfId = pairAmfId;
	}

	@Column(name = "PAIR_GNB_ID")
	public int getPairGnbId() {
		return pairGnbId;
	}

	public void setPairGnbId(int pairGnbId) {
		this.pairGnbId = pairGnbId;
	}

	@Column(name = "PAIR_CELL_NAME")
	public String getPairCellName() {
		return pairCellName;
	}

	public void setPairCellName(String pairCellName) {
		this.pairCellName = pairCellName;
	}

	@Column(name = "PAIR_CELL_ID")
	public int getPairCellId() {
		return pairCellId;
	}

	public void setPairCellId(int pairCellId) {
		this.pairCellId = pairCellId;
	}

	/**
	 * @return the cellInfocellInfo
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CI_ID")
	public CellInfo getCellInfo() {
		return cellInfo;
	}

	public void setCellInfo(CellInfo cellInfo) {
		this.cellInfo = cellInfo;
	}

	@Column(name="REGION")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}	

	
}
