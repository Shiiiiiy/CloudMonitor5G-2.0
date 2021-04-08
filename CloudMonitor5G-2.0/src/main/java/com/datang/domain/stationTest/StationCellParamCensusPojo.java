package com.datang.domain.stationTest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;

/**
 * 单站小区参数统计实体类
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_STATION_CELLPARAM_CENSUS")
public class StationCellParamCensusPojo {

	private Long id;
	
	/**
	 * 规划工参id
	 */
	private Long cellParamId;
	
	/**
	 * localCellId
	 */
	private String localCellId;
	/**
	 * cellName;
	 */
	private String cellName;
	
	private String siteName;
	/**
	 * 第1列 小区id
	 */
	private String cellId1;
	/**
	 * 第1列 pci
	 */
	private Integer pci2;
	/**
	 * 第1列 nr频率
	 */
	private String nrFrequency3;
	/**
	 * 第1列 frequency1频点
	 */
	private Integer frequency14;
	/**
	 * 第1列 小区带宽
	 */
	private String cellBroadband5;
	/**
	 * 第1列 特殊子帧配比
	 */
	private String specialRatio6;
	/**
	 * 第1列 上下行比例
	 */
	private String upAndDownRatio7;
	/**
	 * 第1列 SSb子载波间隔
	 */
	private String ssbWaveInterval8;
	/**
	 * 第1列 tac
	 */
	private Integer tac9;
	/**
	 * 第1列 SSB发送功率(dBm)
	 */
	private String ssbSending10;
	/**
	 * 第1列 p-a规划值
	 */
	private String p_a_p;
	/**
	 * 第1列 p-b规划值
	 */
	private String p_b_p;
	/**
	 * 第1列 根序列码规划值
	 */
	private String rootSeq_p;
	/**
	 * 第1列 pdcch符号数规划值
	 */
	private String pdcchSymbol_p;
	/**
	 * 第1列 小区TRX规划值
	 */
	private String cellTRx_p;
	/**
	 * 第1列 NR帧结构规划值
	 */
	private String frameStructure_p;
	/**
	 * 第1列 gNB ID
	 */
	private Long gnbid_p;
	
	
	/**
	 * 第2列 CellID
	 */
	private String cellId11;
	/**
	 * 第2列 pci
	 */
	private Integer pci12;
	/**
	 * 第2列 NR频率
	 */
	private String nrFrequency13;
	/**
	 * 第2列 Frequency1
	 */
	private Integer frequency114;
	/**
	 * 第2列 小区带宽
	 */
	private String cellBroadband15;
	/**
	 * 第2列 特殊子帧配比
	 */
	private String specialRatio16;
	/**
	 * 第2列 上下行比例
	 */
	private String upAndDownRatio17;
	/**
	 * 第2列 SSb子载波间隔
	 */
	private String ssbWaveInterval18;
	/**
	 * 第2列 tac
	 */
	private Integer tac19;
	/**
	 * 第2列 SSB发送功率(dBm)
	 */
	private String ssbSending20;
	/**
	 * 第2列 p-a实测值
	 */
	private String p_a_r;
	/**
	 * 第2列 p-b实测值
	 */
	private String p_b_r;
	/**
	 * 第2列 根序列码实测值
	 */
	private String rootSeq_r;
	/**
	 * 第2列 pdcch符号数实测值
	 */
	private String pdcchSymbol_r;
	/**
	 * 第2列 小区TRX实测值
	 */
	private String cellTRx_r;
	/**
	 * 第2列 NR帧结构实测值
	 */
	private String frameStructure_r;
	/**
	 * 第2列 gNB ID实测值
	 */
	private Long gnbid_r;
	
	
	/**
	 * 第3列  cellid结果
	 */
	private String cellIdStatus21;
	/**
	 * 第3列  pci结果
	 */
	private String PciStatus22;
	/**
	 * 第3列  nr频率结果
	 */
	private String nrFrequencyStatus23;
	/**
	 * 第3列 频点结果
	 */
	private String frequencyStatus24;
	/**
	 * 第3列  小区带宽结果	
	 */
	private String cellBroadbandStatus25;
	/**
	 * 第3列特殊子帧配比结果
	 */
	private String specialRatioStatus26;
	/**
	 * 第3列  上下行比例结果
	 */
	private String upAndDownRatioStatus27;
	/**
	 * 第3列 SSB子载波间隔结果
	 */
	private String ssbWaveInterval28;
	/**
	 * 第3列 tac结果
	 */
	private String tacStatus29;
	/**
	 * 第3列 SSB发送功率(dBm)结果
	 */
	private String ssbSendingStatus30;
	/**
	 * 第3列 p-a结果
	 */
	private String p_a_rlt;
	/**
	 * 第3列 p-b结果
	 */
	private String p_b_rlt;
	/**
	 * 第3列 根序列码结果
	 */
	private String rootSeq_rlt;
	/**
	 * 第3列 pdcch符号数结果
	 */
	private String pdcchSymbol_rlt;
	/**
	 * 第3列 小区TRX结果
	 */
	private String cellTRx_rlt;
	/**
	 * 第3列 NR帧结构结果
	 */
	private String frameStructure_rlt;
	/**
	 * 第3列 gNB ID比较结果
	 */
	private String gnbid_rlt;
	
	/**
	 * 单验日志规划工参
	 */
	private PlanParamPojo planParamPojo;
	
	

	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "CELL_PARAM_ID")
	public Long getCellParamId() {
		return cellParamId;
	}

	public void setCellParamId(Long cellParamId) {
		this.cellParamId = cellParamId;
	}

	@Column(name = "LOCAL_CELL_ID")
	public String getLocalCellId() {
		return localCellId;
	}

	public void setLocalCellId(String localCellId) {
		this.localCellId = localCellId;
	}

	@Column(name = "CELL_ID1")
	public String getCellId1() {
		return cellId1;
	}

	public void setCellId1(String cellId1) {
		this.cellId1 = cellId1;
	}
	
	@Column(name = "PCI2")
	public Integer getPci2() {
		return pci2;
	}

	public void setPci2(Integer pci2) {
		this.pci2 = pci2;
	}
	
	@Column(name = "NR_FREQUENCY3")
	public String getNrFrequency3() {
		return nrFrequency3;
	}

	public void setNrFrequency3(String nrFrequency3) {
		this.nrFrequency3 = nrFrequency3;
	}
	
	@Column(name = "FREQUENCY14")
	public Integer getFrequency14() {
		return frequency14;
	}

	public void setFrequency14(Integer frequency14) {
		this.frequency14 = frequency14;
	}
	
	@Column(name = "CELL_BROADBAND5")
	public String getCellBroadband5() {
		return cellBroadband5;
	}

	public void setCellBroadband5(String cellBroadband5) {
		this.cellBroadband5 = cellBroadband5;
	}
	
	@Column(name = "SPECIAL_RATIO6")
	public String getSpecialRatio6() {
		return specialRatio6;
	}

	public void setSpecialRatio6(String specialRatio6) {
		this.specialRatio6 = specialRatio6;
	}
	
	@Column(name = "UPANDDOWN_RATIO7")
	public String getUpAndDownRatio7() {
		return upAndDownRatio7;
	}

	public void setUpAndDownRatio7(String upAndDownRatio7) {
		this.upAndDownRatio7 = upAndDownRatio7;
	}
	
	@Column(name = "SSB_WAVE_INTERVAL8")
	public String getSsbWaveInterval8() {
		return ssbWaveInterval8;
	}

	public void setSsbWaveInterval8(String ssbWaveInterval8) {
		this.ssbWaveInterval8 = ssbWaveInterval8;
	}
	
	@Column(name = "TAC9")
	public Integer getTac9() {
		return tac9;
	}

	public void setTac9(Integer tac9) {
		this.tac9 = tac9;
	}
	
	@Column(name = "SSB_SENDING10")
	public String getSsbSending10() {
		return ssbSending10;
	}

	public void setSsbSending10(String ssbSending10) {
		this.ssbSending10 = ssbSending10;
	}
	
	@Column(name = "P_A_P")
	public String getP_a_p() {
		return p_a_p;
	}
	
	public void setP_a_p(String p_a_p) {
		this.p_a_p = p_a_p;
	}

	@Column(name = "P_B_P")
	public String getP_b_p() {
		return p_b_p;
	}
	
	public void setP_b_p(String p_b_p) {
		this.p_b_p = p_b_p;
	}

	@Column(name = "ROOTSEQ_P")
	public String getRootSeq_p() {
		return rootSeq_p;
	}
	
	public void setRootSeq_p(String rootSeq_p) {
		this.rootSeq_p = rootSeq_p;
	}

	@Column(name = "PDCCH_SYMBOL_P")
	public String getPdcchSymbol_p() {
		return pdcchSymbol_p;
	}
	
	public void setPdcchSymbol_p(String pdcchSymbol_p) {
		this.pdcchSymbol_p = pdcchSymbol_p;
	}

	@Column(name = "CELL_TRX_P")
	public String getCellTRx_p() {
		return cellTRx_p;
	}
	
	public void setCellTRx_p(String cellTRx_p) {
		this.cellTRx_p = cellTRx_p;
	}

	@Column(name = "FRAME_STRUCTURE_P")
	public String getFrameStructure_p() {
		return frameStructure_p;
	}

	public void setFrameStructure_p(String frameStructure_p) {
		this.frameStructure_p = frameStructure_p;
	}

	@Column(name = "GNOBID_P")
	public Long getGnbid_p() {
		return gnbid_p;
	}

	public void setGnbid_p(Long gnbid_p) {
		this.gnbid_p = gnbid_p;
	}

	@Column(name = "CELL_ID11")
	public String getCellId11() {
		return cellId11;
	}

	public void setCellId11(String cellId11) {
		this.cellId11 = cellId11;
	}
	
	@Column(name = "PCI12")
	public Integer getPci12() {
		return pci12;
	}

	public void setPci12(Integer pci12) {
		this.pci12 = pci12;
	}
	
	@Column(name = "NR_FREQUENCY13")
	public String getNrFrequency13() {
		return nrFrequency13;
	}

	public void setNrFrequency13(String nrFrequency13) {
		this.nrFrequency13 = nrFrequency13;
	}
	
	@Column(name = "FREQUENCY114")
	public Integer getFrequency114() {
		return frequency114;
	}

	public void setFrequency114(Integer frequency114) {
		this.frequency114 = frequency114;
	}
	
	@Column(name = "CELL_BROADBAND15")
	public String getCellBroadband15() {
		return cellBroadband15;
	}

	public void setCellBroadband15(String cellBroadband15) {
		this.cellBroadband15 = cellBroadband15;
	}
	
	@Column(name = "SPECIAL_RATIO16")
	public String getSpecialRatio16() {
		return specialRatio16;
	}

	public void setSpecialRatio16(String specialRatio16) {
		this.specialRatio16 = specialRatio16;
	}
	
	@Column(name = "UPANDDOWN_RATIO17")
	public String getUpAndDownRatio17() {
		return upAndDownRatio17;
	}

	public void setUpAndDownRatio17(String upAndDownRatio17) {
		this.upAndDownRatio17 = upAndDownRatio17;
	}
	
	@Column(name = "SSB_WAVE_INTERVAL18")
	public String getSsbWaveInterval18() {
		return ssbWaveInterval18;
	}

	public void setSsbWaveInterval18(String ssbWaveInterval18) {
		this.ssbWaveInterval18 = ssbWaveInterval18;
	}
	
	@Column(name = "TAC19")
	public Integer getTac19() {
		return tac19;
	}

	public void setTac19(Integer tac19) {
		this.tac19 = tac19;
	}
	
	@Column(name = "SSB_SENDING20")
	public String getSsbSending20() {
		return ssbSending20;
	}

	public void setSsbSending20(String ssbSending20) {
		this.ssbSending20 = ssbSending20;
	}
	
	@Column(name = "P_A_R")
	public String getP_a_r() {
		return p_a_r;
	}
	
	public void setP_a_r(String p_a_r) {
		this.p_a_r = p_a_r;
	}

	@Column(name = "P_B_R")
	public String getP_b_r() {
		return p_b_r;
	}
	
	public void setP_b_r(String p_b_r) {
		this.p_b_r = p_b_r;
	}

	@Column(name = "ROOTSEQ_R")
	public String getRootSeq_r() {
		return rootSeq_r;
	}
	
	public void setRootSeq_r(String rootSeq_r) {
		this.rootSeq_r = rootSeq_r;
	}

	@Column(name = "PDCCH_SYMBOL_R")
	public String getPdcchSymbol_r() {
		return pdcchSymbol_r;
	}
	
	public void setPdcchSymbol_r(String pdcchSymbol_r) {
		this.pdcchSymbol_r = pdcchSymbol_r;
	}

	@Column(name = "CELL_TRX_R")
	public String getCellTRx_r() {
		return cellTRx_r;
	}
	
	public void setCellTRx_r(String cellTRx_r) {
		this.cellTRx_r = cellTRx_r;
	}

	@Column(name = "FRAME_STRUCTURE_R")
	public String getFrameStructure_r() {
		return frameStructure_r;
	}

	public void setFrameStructure_r(String frameStructure_r) {
		this.frameStructure_r = frameStructure_r;
	}
	
	@Column(name = "GNOBID_R")
	public Long getGnbid_r() {
		return gnbid_r;
	}

	public void setGnbid_r(Long gnbid_r) {
		this.gnbid_r = gnbid_r;
	}

	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	
	@Column(name = "SITE_NAME")
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Column(name = "CELL_ID_STATUS21")
	public String getCellIdStatus21() {
		return cellIdStatus21;
	}

	public void setCellIdStatus21(String cellIdStatus21) {
		this.cellIdStatus21 = cellIdStatus21;
	}

	@Column(name = "PCI_STATUS22")
	public String getPciStatus22() {
		return PciStatus22;
	}

	public void setPciStatus22(String pciStatus22) {
		PciStatus22 = pciStatus22;
	}

	@Column(name = "NR_FREQUENCY_STATUS23")
	public String getNrFrequencyStatus23() {
		return nrFrequencyStatus23;
	}

	public void setNrFrequencyStatus23(String nrFrequencyStatus23) {
		this.nrFrequencyStatus23 = nrFrequencyStatus23;
	}

	@Column(name = "FREQUENCY_STATUS24")
	public String getFrequencyStatus24() {
		return frequencyStatus24;
	}

	public void setFrequencyStatus24(String frequencyStatus24) {
		this.frequencyStatus24 = frequencyStatus24;
	}

	@Column(name = "CELL_BROADBAND_STATUS25")
	public String getCellBroadbandStatus25() {
		return cellBroadbandStatus25;
	}

	public void setCellBroadbandStatus25(String cellBroadbandStatus25) {
		this.cellBroadbandStatus25 = cellBroadbandStatus25;
	}

	@Column(name = "SPECIAL_RATIO_STATUS26")
	public String getSpecialRatioStatus26() {
		return specialRatioStatus26;
	}

	public void setSpecialRatioStatus26(String specialRatioStatus26) {
		this.specialRatioStatus26 = specialRatioStatus26;
	}

	@Column(name = "UPANDDOWN_RATIOSTATUS27")
	public String getUpAndDownRatioStatus27() {
		return upAndDownRatioStatus27;
	}

	public void setUpAndDownRatioStatus27(String upAndDownRatioStatus27) {
		this.upAndDownRatioStatus27 = upAndDownRatioStatus27;
	}

	@Column(name = "SSB_WAVEINTERVAL28")
	public String getSsbWaveInterval28() {
		return ssbWaveInterval28;
	}

	public void setSsbWaveInterval28(String ssbWaveInterval28) {
		this.ssbWaveInterval28 = ssbWaveInterval28;
	}

	@Column(name = "TAC_STATUS29")
	public String getTacStatus29() {
		return tacStatus29;
	}

	public void setTacStatus29(String tacStatus29) {
		this.tacStatus29 = tacStatus29;
	}

	@Column(name = "SSB_SENDING_STATUS30")
	public String getSsbSendingStatus30() {
		return ssbSendingStatus30;
	}

	public void setSsbSendingStatus30(String ssbSendingStatus30) {
		this.ssbSendingStatus30 = ssbSendingStatus30;
	}
	
	@Column(name = "P_A_RLT")
	public String getP_a_rlt() {
		return p_a_rlt;
	}
	
	public void setP_a_rlt(String p_a_rlt) {
		this.p_a_rlt = p_a_rlt;
	}

	@Column(name = "P_B_RLT")
	public String getP_b_rlt() {
		return p_b_rlt;
	}
	
	public void setP_b_rlt(String p_b_rlt) {
		this.p_b_rlt = p_b_rlt;
	}

	@Column(name = "ROOTSEQ_RLT")
	public String getRootSeq_rlt() {
		return rootSeq_rlt;
	}
	
	public void setRootSeq_rlt(String rootSeq_rlt) {
		this.rootSeq_rlt = rootSeq_rlt;
	}

	@Column(name = "PDCCH_SYMBOL_RLT")
	public String getPdcchSymbol_rlt() {
		return pdcchSymbol_rlt;
	}
	
	public void setPdcchSymbol_rlt(String pdcchSymbol_rlt) {
		this.pdcchSymbol_rlt = pdcchSymbol_rlt;
	}

	@Column(name = "CELL_TRX_RLT")
	public String getCellTRx_rlt() {
		return cellTRx_rlt;
	}
	
	public void setCellTRx_rlt(String cellTRx_rlt) {
		this.cellTRx_rlt = cellTRx_rlt;
	}

	@Column(name = "FRAME_STRUCTURE_RLT")
	public String getFrameStructure_rlt() {
		return frameStructure_rlt;
	}

	public void setFrameStructure_rlt(String frameStructure_rlt) {
		this.frameStructure_rlt = frameStructure_rlt;
	}
	
	@Column(name = "GNOBID_RLT")
	public String getGnbid_rlt() {
		return gnbid_rlt;
	}

	public void setGnbid_rlt(String gnbid_rlt) {
		this.gnbid_rlt = gnbid_rlt;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARAM_ID")
	//@JSON(serialize = false)
	public PlanParamPojo getPlanParamPojo() {
		return planParamPojo;
	}

	public void setPlanParamPojo(PlanParamPojo planParamPojo) {
		this.planParamPojo = planParamPojo;
	}
	
	
}
