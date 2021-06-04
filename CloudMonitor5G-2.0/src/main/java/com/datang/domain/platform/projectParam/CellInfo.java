/**
 * 
 */
package com.datang.domain.platform.projectParam;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.testManage.terminal.TerminalGroup;

/**
 * 小区信息表
 * 
 * @author yinzhipeng
 * @date:2015年10月19日 上午8:58:15
 * @version
 */
@Entity
@Table(name = "IADS_CELL_INFO")
public class CellInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8777802328244490530L;

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 运营商类型
	 */
	private String operatorType;
	/**
	 * 二级域
	 */
	private TerminalGroup group;
	/**
	 * 创建人
	 */
	private String userName;
	
	/**
	 * 规划表名称
	 */
	private String planSheetName;
	
	
	/**
	 * 总小区数
	 */
	private Integer totalCellNum;
	
	/**
	 * 导入时间
	 */
	private Date importDate;
	
	/**
	 * 区域
	 */
	private String region;

	private String lteCellGisFolderName;
	//4g图层名，包含中国移动4g工参和规划工参45g图层名
	private String lteCellGisFileName;
	private Integer lteCellDrawSucc;
	
	//5g图层名  中国移动5g工参图层名
	private String nrCellGisFileName;
	
	/**
	 * 5G小区表
	 */
	private Set<Cell5G> cells5g = new HashSet<>();
	private Boolean cells5gImport = Boolean.FALSE;

	/**
	 * LTE小区表
	 */
	private Set<LteCell> lteCells = new HashSet<LteCell>();
	private Boolean lteCellsImport = Boolean.FALSE;

	/**
	 * GSM小区表
	 */
	private Set<GsmCell> gsmCells = new HashSet<GsmCell>();
	private Boolean gsmCellsImport = Boolean.FALSE;
	/**
	 * 5G邻区
	 */
	private Set<Cell5GNbCell> cells5gNb = new HashSet<Cell5GNbCell>();
	private Boolean cells5gNbImport = Boolean.FALSE;
	
	/**
	 * 5G-TDL邻区
	 */
	private Set<Cell5GtdlNbCell> cells5gTdlNb = new HashSet<Cell5GtdlNbCell>();
	private Boolean cells5gTdlNbImport = Boolean.FALSE;

	/**
	 * TDL邻区
	 */
	private Set<TdlNbCell> tdlNbCells = new HashSet<TdlNbCell>();
	private Boolean tdlNbCellsImport = Boolean.FALSE;

	/**
	 * TDL-GSM邻区
	 */
	private Set<TdlGsmNbCell> tdlGsmNbCells = new HashSet<TdlGsmNbCell>();
	private Boolean tdlGsmNbCellsImport = Boolean.FALSE;
	
	/**
	 * TDL-GSM邻区
	 */
	private Set<Lte5GCell> lte5GCells = new HashSet<Lte5GCell>();
	private Boolean lte5GCellsImport = Boolean.FALSE;
	
	/**
	 * 5G规划工参小区
	 */
	private Set<PlanParamPojo> planParams = new HashSet<PlanParamPojo>();
	private Boolean planParamsImport = Boolean.FALSE;
	
	/**
	 * 4G规划工参小区
	 */
	private Set<Plan4GParam> plan4GParams = new HashSet<Plan4GParam>();
	private Boolean plan4GParamsImport = Boolean.FALSE;

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
	 * @return the groupgroup
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TID")
	public TerminalGroup getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(TerminalGroup group) {
		this.group = group;
	}

	/**
	 * @return the userNameuserName
	 */
	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the planSheetName
	 */
	@Column(name = "PLAN_SHEET_NAME")
	public String getPlanSheetName() {
		return planSheetName;
	}

	/**
	 * @param planSheetName the planSheetName to set
	 */
	public void setPlanSheetName(String planSheetName) {
		this.planSheetName = planSheetName;
	}


	/**
	 * @return the totalCellNum
	 */
	@Column(name = "TOTAL_CELL_NUM")
	public Integer getTotalCellNum() {
		return totalCellNum;
	}

	/**
	 * @param totalCellNum the totalCellNum to set
	 */
	public void setTotalCellNum(Integer totalCellNum) {
		this.totalCellNum = totalCellNum;
	}

	/**
	 * @return the importDateimportDate
	 */
	@Column(name = "IMPORT_DATE")
	public Date getImportDate() {
		return importDate;
	}

	/**
	 * @param importDate
	 *            the importDate to set
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	/**
	 * @return the lteCellslteCells
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	public Set<LteCell> getLteCells() {
		return lteCells;
	}

	/**
	 * @param lteCells
	 *            the lteCells to set
	 */
	public void setLteCells(Set<LteCell> lteCells) {
		this.lteCells = lteCells;
	}

	/**
	 * @return the gsmCellsgsmCells
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	public Set<GsmCell> getGsmCells() {
		return gsmCells;
	}

	/**
	 * @param gsmCells
	 *            the gsmCells to set
	 */
	public void setGsmCells(Set<GsmCell> gsmCells) {
		this.gsmCells = gsmCells;
	}

	/**
	 * @return the tdlNbCellstdlNbCells
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	public Set<TdlNbCell> getTdlNbCells() {
		return tdlNbCells;
	}

	/**
	 * @param tdlNbCells
	 *            the tdlNbCells to set
	 */
	public void setTdlNbCells(Set<TdlNbCell> tdlNbCells) {
		this.tdlNbCells = tdlNbCells;
	}

	/**
	 * @return the tdlGsmNbCellstdlGsmNbCells
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	public Set<TdlGsmNbCell> getTdlGsmNbCells() {
		return tdlGsmNbCells;
	}

	/**
	 * @param tdlGsmNbCells
	 *            the tdlGsmNbCells to set
	 */
	public void setTdlGsmNbCells(Set<TdlGsmNbCell> tdlGsmNbCells) {
		this.tdlGsmNbCells = tdlGsmNbCells;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	public Set<Lte5GCell> getLte5GCells() {
		return lte5GCells;
	}

	public void setLte5GCells(Set<Lte5GCell> lte5gCells) {
		lte5GCells = lte5gCells;
	}
	
	/**
	 * @return the operatorTypeoperatorType
	 */
	@Column(name = "OPERATOR_TYPE")
	public String getOperatorType() {
		return operatorType;
	}

	/**
	 * @param operatorType
	 *            the operatorType to set
	 */
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	/**
	 * @return the lteCellGisFolderNamelteCellGisFolderName
	 */
	@Column(name = "GIS_FOLDER_NAME")
	public String getLteCellGisFolderName() {
		return lteCellGisFolderName;
	}

	/**
	 * @param lteCellGisFolderName
	 *            the lteCellGisFolderName to set
	 */
	public void setLteCellGisFolderName(String lteCellGisFolderName) {
		this.lteCellGisFolderName = lteCellGisFolderName;
	}

	/**
	 * @return the lteCellGisFileNamelteCellGisFileName
	 */
	@Column(name = "GIS_FILE_NAME")
	public String getLteCellGisFileName() {
		return lteCellGisFileName;
	}

	/**
	 * @param lteCellGisFileName
	 *            the lteCellGisFileName to set
	 */
	public void setLteCellGisFileName(String lteCellGisFileName) {
		this.lteCellGisFileName = lteCellGisFileName;
	}

	/**
	 * @return
	 */
	@Column(name = "GIS_FILE_NAME_NR")
	public String getNrCellGisFileName() {
		return nrCellGisFileName;
	}

	public void setNrCellGisFileName(String nrCellGisFileName) {
		this.nrCellGisFileName = nrCellGisFileName;
	}

	/**
	 * @return the lteCellDrawSucclteCellDrawSucc
	 */
	@Column(name = "DRAW_SUCC")
	public Integer getLteCellDrawSucc() {
		return lteCellDrawSucc;
	}

	/**
	 * @param lteCellDrawSucc
	 *            the lteCellDrawSucc to set
	 */
	public void setLteCellDrawSucc(Integer lteCellDrawSucc) {
		this.lteCellDrawSucc = lteCellDrawSucc;
	}

	/**
	 * @return the lteCellsImportlteCellsImport
	 */
	@Column(name = "LTECELLS_IMPORT", columnDefinition = "boolean")
	public Boolean getLteCellsImport() {
		return lteCellsImport;
	}

	/**
	 * @param lteCellsImport
	 *            the lteCellsImport to set
	 */
	public void setLteCellsImport(Boolean lteCellsImport) {
		this.lteCellsImport = lteCellsImport;
	}

	/**
	 * @return the gsmCellsImportgsmCellsImport
	 */
	@Column(name = "GSMCELLS_IMPORT", columnDefinition = "boolean")
	public Boolean getGsmCellsImport() {
		return gsmCellsImport;
	}

	/**
	 * @param gsmCellsImport
	 *            the gsmCellsImport to set
	 */
	public void setGsmCellsImport(Boolean gsmCellsImport) {
		this.gsmCellsImport = gsmCellsImport;
	}

	/**
	 * @return the tdlNbCellsImporttdlNbCellsImport
	 */
	@Column(name = "TDLNBCELLS_IMPORT", columnDefinition = "boolean")
	public Boolean getTdlNbCellsImport() {
		return tdlNbCellsImport;
	}

	/**
	 * @param tdlNbCellsImport
	 *            the tdlNbCellsImport to set
	 */
	public void setTdlNbCellsImport(Boolean tdlNbCellsImport) {
		this.tdlNbCellsImport = tdlNbCellsImport;
	}

	/**
	 * @return the tdlGsmNbCellsImporttdlGsmNbCellsImport
	 */
	@Column(name = "TDLGSMNBCELLS_IMPORT", columnDefinition = "boolean")
	public Boolean getTdlGsmNbCellsImport() {
		return tdlGsmNbCellsImport;
	}
	
	/**
	 * @param tdlGsmNbCellsImport
	 *            the tdlGsmNbCellsImport to set
	 */
	public void setTdlGsmNbCellsImport(Boolean tdlGsmNbCellsImport) {
		this.tdlGsmNbCellsImport = tdlGsmNbCellsImport;
	}

	@Column(name = "LTE5GCELLS_IMPORT", columnDefinition = "boolean")
	public Boolean getLte5GCellsImport() {
		return lte5GCellsImport;
	}

	public void setLte5GCellsImport(Boolean lte5gCellsImport) {
		lte5GCellsImport = lte5gCellsImport;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	public Set<Cell5G> getCells5g() {
		return cells5g;
	}

	public void setCells5g(Set<Cell5G> cells5g) {
		this.cells5g = cells5g;
	}

	@Column(name = "CELLS5G_IMPORT", columnDefinition = "boolean")
	public Boolean getCells5gImport() {
		return cells5gImport;
	}

	public void setCells5gImport(Boolean cells5gImport) {
		this.cells5gImport = cells5gImport;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	public Set<Cell5GNbCell> getCells5gNb() {
		return cells5gNb;
	}

	public void setCells5gNb(Set<Cell5GNbCell> cells5gNb) {
		this.cells5gNb = cells5gNb;
	}

	@Column(name = "CELLS5GNB_IMPORT", columnDefinition = "boolean")
	public Boolean getCells5gNbImport() {
		return cells5gNbImport;
	}

	public void setCells5gNbImport(Boolean cells5gNbImport) {
		this.cells5gNbImport = cells5gNbImport;
	}
	

	/**
	 * @return the region
	 */
	@Column(name="REGION")
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	public Set<Cell5GtdlNbCell> getCells5gTdlNb() {
		return cells5gTdlNb;
	}

	public void setCells5gTdlNb(Set<Cell5GtdlNbCell> cells5gTdlNb) {
		this.cells5gTdlNb = cells5gTdlNb;
	}

	@Column(name = "CELLS5GTDLNB_IMPORT", columnDefinition = "boolean")
	public Boolean getCells5gTdlNbImport() {
		return cells5gTdlNbImport;
	}

	public void setCells5gTdlNbImport(Boolean cells5gTdlNbImport) {
		this.cells5gTdlNbImport = cells5gTdlNbImport;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	@JSON(serialize = false)
	public Set<PlanParamPojo> getPlanParams() {
		return planParams;
	}

	public void setPlanParams(Set<PlanParamPojo> planParams) {
		this.planParams = planParams;
	}

	@Column(name = "PLANPARAMS_IMPORT", columnDefinition = "boolean")
	public Boolean getPlanParamsImport() {
		return planParamsImport;
	}

	public void setPlanParamsImport(Boolean planParamsImport) {
		this.planParamsImport = planParamsImport;
	}

	/**
	 * @return the plan4GParams
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cellInfo", cascade = CascadeType.ALL)
	@JSON(serialize = false)
	public Set<Plan4GParam> getPlan4GParams() {
		return plan4GParams;
	}

	/**
	 * @param plan4gParams the plan4GParams to set
	 */
	public void setPlan4GParams(Set<Plan4GParam> plan4gParams) {
		plan4GParams = plan4gParams;
	}

	/**
	 * @return the plan4GParamsImport
	 */
	@Column(name = "PLAN4GPARAMS_IMPORT", columnDefinition = "boolean")
	public Boolean getPlan4GParamsImport() {
		return plan4GParamsImport;
	}

	/**
	 * @param plan4gParamsImport the plan4GParamsImport to set
	 */
	public void setPlan4GParamsImport(Boolean plan4gParamsImport) {
		plan4GParamsImport = plan4gParamsImport;
	}
	
	

}
