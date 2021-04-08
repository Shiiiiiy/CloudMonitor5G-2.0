package com.datang.domain.oppositeOpen3d;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.domain.platform.projectParam.Plan4GParam;

import lombok.Data;

/**
 * 反开3d小区无线参数表实体类
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_OPPOSITE_OPEN3D_WIRELESS")
@Data
public class OppositeOpen3dWirelessPojo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	/**
	 * SiteName必填:基站名称
	 */
	@Column(name = "SITE_NAME")
	private String siteName;
	/**
	 * CellName必填:小区友好名
	 */
	@Column(name = "CELL_NAME1")
	private String cellName1;
	/**
	 * CELL ID必填:小区标识
	 */
	@Column(name = "CELLID1")
	private Long cellId1;
	/**
	 * PCI 必填:物理小区ID
	 */
	@Column(name = "PCI1")
	private Long pci1;
	
	/**
	 *Frequency_DL,选填  频率
	 */
	@Column(name = "FREQUENCY_DL1")
	private String frequencyDl1;
	
	/**
	 * earfcn 必填 频点
	 */
	@Column(name = "EARFCN1")
	private String earfcn1;
	
	/**
	 * 带宽,选填
	 */
	@Column(name = "BROAD_BAND1")
	private Float broadBand1;
	
	/**
	 * subFrameConfigsubFrameConfig  选填：子帧配置
	 */
	@Column(name = "SUBFRAME_CONFIG1")
	private String subFrameConfig1;
	
	/**
	 * specialSubFrameConfig  选填:特殊子帧配置
	 */
	@Column(name = "SPECIAL_CONFIG1")
	private String specialSubFrameConfig1;
	/**
	 * rs_epre  选填:RS EPRE
	 */
	@Column(name = "RS_EPRE1")
	private String rs_epre1;
	/**
	 * p_a  选填:p-a
	 */
	@Column(name = "P_A1")
	private String p_a1;
	/**
	 * p_b  选填:p-b
	 */
	@Column(name = "P_B1")
	private String p_b1;
	/**
	 * pdcch符号数  
	 */
	@Column(name = "PDCCH_SYMBOL")
	private String pdcchSymbol;
	/**
	 * 根序列
	 */
	@Column(name = "ROOTSEQ")
	private String  rootSeq;

	
	
	/**
	 * PCI 必填:物理小区ID   实测值
	 */
	@Column(name = "PCI1_R")
	private Long pciReality1;
	
	/**
	 *Frequency_DL,选填   实测值
	 */
	@Column(name = "FREQUENCY_DL1_R")
	private String frequencyDlReality1;
	
	/**
	 * earfcn 必填   实测值
	 */
	@Column(name = "EARFCN1_R")
	private String earfcnReality1;
	
	/**
	 * 带宽,选填  实测值
	 */
	@Column(name = "BROAD_BAND1_R")
	private Float broadBandReality1;
	
	/**
	 * subFrameConfigsubFrameConfig  选填：子帧配置
	 */
	@Column(name = "SUBFRAME_CONFIG1_R")
	private String subFrameConfigReality1;
	
	/**
	 * specialSubFrameConfig  选填:特殊子帧配置 实测值
	 */
	@Column(name = "SPECIAL_CONFIG1_R")
	private String specialSubFrameConfigReality1;
	/**
	 * rs_epre  选填:RS EPRE 实测值
	 */
	@Column(name = "RS_EPRE1_R")
	private String rs_epreReality1;
	/**
	 * p_a  选填:p-a 实测值
	 */
	@Column(name = "P_A1_R")
	private String p_aReality1;
	/**
	 * p_b  选填:p-b 实测值
	 */
	@Column(name = "P_B1_R")
	private String p_bReality1;
	
	/**
	 * p_b  实测值
	 */
	@Column(name = "CELL_ID1_R")
	private Long cellIdReality1;
	
	/**
	 * pdcch符号数  
	 */
	@Column(name = "PDCCH_SYMBOL_R")
	private String pdcchSymbolReality1;
	/**
	 * 根序列
	 */
	@Column(name = "ROOTSEQ_R")
	private String  rootSeqReality1;
	
	/**
	 * eNB ID
	 */
	@Column(name = "ENB_ID")
	private Integer enbId;
	/**
	 * 归属tac
	 */
	@Column(name = "TAC")
	private Integer tac;
	/**
	 * eNB ID 实测值
	 */
	@Column(name = "ENB_ID_R")
	private Integer enbIdReality;
	/**
	 * 归属tac 实测值
	 */
	@Column(name = "TAC_R")
	private Integer tacReality;
	
	//结论
	/**
	 * PCI 必填:物理小区ID 结论  0:不一致,1:一致
	 */
	@Column(name = "PCI_CONTRAST")
	private Integer pciContrast;
	
	@Transient 
	private Integer pciContrastOpposite;
	
	@Transient 
	private String pciContrastStr;
	
	/**
	 *Frequency_DL,选填 结论  0:不一致,1:一致
	 */
	@Column(name = "FREQUENCY_DL_CONTRAST")
	private Integer frequencyDlContrast;
	
	@Transient 
	private Integer frequencyDlContrastOpposite;
	
	@Transient 
	private String frequencyDlContrastStr;
	
	/**
	 * earfcn 必填 结论	 0:不一致,1:一致
	 */
	@Column(name = "EARFCN_CONTRAST")
	private Integer earfcnContrast;
	
	@Transient 
	private Integer earfcnContrastOpposite;
	
	@Transient 
	private String earfcnContrastStr;
	
	/**
	 * 带宽,选填 结论	 0:不一致,1:一致
	 */
	@Column(name = "BROAD_BAND_CONTRAST")
	private Integer broadBandContrast;
	
	@Transient 
	private Integer broadBandContrastOpposite;
	
	@Transient 
	private String broadBandContrastStr;
	
	/**
	 * subFrameConfigsubFrameConfig  选填：子帧配置
	 */
	@Column(name = "SUBFRAME_CONFIG_CONTRAST")
	private Integer subFrameConfigContrast;
	
	@Transient 
	private Integer subFrameConfigContrastOpposite;
	
	@Transient 
	private String subFrameConfigContrastStr;
	
	/**
	 * specialSubFrameConfig  选填:特殊子帧配置 结论   0:不一致,1:一致
	 */
	@Column(name = "SPECIAL_CONFIG_CONTRAST")
	private Integer specialSubFrameConfigContrast;
	
	@Transient 
	private Integer specialSubFrameConfigContrastOpposite;
	
	@Transient 
	private String specialSubFrameConfigContrastStr;
	
	/**
	 * rs_epre  选填:RS EPRE 结论   0:不一致,1:一致
	 */
	@Column(name = "RS_EPRE_CONTRAST")
	private Integer rs_epreContrast;
	
	@Transient 
	private Integer rs_epreContrasttOpposite;
	
	@Transient 
	private String rs_epreContrastStr;
	/**
	 * p_a  选填:p-a 结论   0:不一致,1:一致
	 */
	@Column(name = "P_A_CONTRAST")
	private Integer p_aContrast;
	
	@Transient 
	private Integer p_aContrastOpposite;
	
	@Transient 
	private String p_aContrastStr;
	
	
	/**
	 * p_b  选填:p-b 结论   0:不一致,1:一致
	 */
	@Column(name = "P_B_CONTRAST")
	private Integer p_bContrast;
	
	@Transient 
	private Integer p_bContrastOpposite;
	
	@Transient 
	private String p_bContrastStr;
	
	/**
	 * cellId  结论   0:不一致,1:一致
	 */
	@Column(name = "CELL_ID_CONTRAST")
	private Integer cellIdContrast;
	
	@Transient 
	private Integer cellIdContrastOpposite;
	
	@Transient 
	private String cellIdContrastStr;
	
	/**
	 * eNB ID 结论   0:不一致,1:一致
	 */
	@Column(name = "ENB_ID_CONTRAST")
	private Integer enbIdContrast;
	
	@Transient 
	private Integer enbIdContrastOpposite;
	
	@Transient 
	private String enbIdContrastStr;
	
	/**
	 * 归属tac 结论   0:不一致,1:一致
	 */
	@Column(name = "TAC_CONTRAST")
	private Integer tacContrast;
	
	@Transient 
	private Integer tacContrastOpposite;
	
	@Transient 
	private String tacContrastStr;
	
	/** 
	 * pdcch符号数   结论   0:不一致,1:一致
	 */
	@Column(name = "PDCCH_SYMBOL_CONTRAST")
	private Integer pdcchSymContrast;
	
	@Transient 
	private Integer pdccSymContrastOpposite;
	
	@Transient 
	private String pdcchSymContrastStr;
	
	/**
	 * 根序列 结论   0:不一致,1:一致
	 */
	@Column(name = "ROOTSEQ_CONTRAST")
	private Integer  rootSeqContrast;
	
	@Transient 
	private Integer rootSeqContrastOpposite;
	
	@Transient 
	private String rootSeqContrastStr;
	
	/**
	 * 工参
	 */
	@OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "plan4GParam")
	private Plan4GParam plan4GParam;

	public Integer getPciContrastOpposite() {
		if(pciContrast==null){
			return null;
		}else if(pciContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getFrequencyDlContrastOpposite() {
		if(frequencyDlContrast==null){
			return null;
		}else if(frequencyDlContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getEarfcnContrastOpposite() {
		if(earfcnContrast==null){
			return null;
		}else if(earfcnContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getBroadBandContrastOpposite() {
		if(broadBandContrast==null){
			return null;
		}else if(broadBandContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getSubFrameConfigContrastOpposite() {
		if(subFrameConfigContrast==null){
			return null;
		}else if(subFrameConfigContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getSpecialSubFrameConfigContrastOpposite() {
		if(specialSubFrameConfigContrast==null){
			return null;
		}else if(specialSubFrameConfigContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getRs_epreContrasttOpposite() {
		if(rs_epreContrast==null){
			return null;
		}else if(rs_epreContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getP_aContrastOpposite() {
		if(p_aContrast==null){
			return null;
		}else if(p_aContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getP_bContrastOpposite() {
		if(p_bContrast==null){
			return null;
		}else if(p_bContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getCellIdContrastOpposite() {
		if(cellIdContrast==null){
			return null;
		}else if(cellIdContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getEnbIdContrastOpposite() {
		if(enbIdContrast==null){
			return null;
		}else if(enbIdContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getTacContrastOpposite() {
		if(tacContrast==null){
			return null;
		}else if(tacContrast==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getPdccSymContrastOpposite() {
		if(pdccSymContrastOpposite==null){
			return null;
		}else if(pdccSymContrastOpposite==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getRootSeqContrastOpposite() {
		if(rootSeqContrastOpposite==null){
			return null;
		}else if(rootSeqContrastOpposite==1){
			return 0;
		}else{
			return 1;
		}
	}

	public String getPciContrastStr() {
		if(pciContrast==null){
			return null;
		}else if(pciContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getFrequencyDlContrastStr() {
		if(frequencyDlContrast==null){
			return null;
		}else if(frequencyDlContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getEarfcnContrastStr() {
		if(earfcnContrast==null){
			return null;
		}else if(earfcnContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getBroadBandContrastStr() {
		if(broadBandContrast==null){
			return null;
		}else if(broadBandContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getSubFrameConfigContrastStr() {
		if(subFrameConfigContrast==null){
			return null;
		}else if(subFrameConfigContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getSpecialSubFrameConfigContrastStr() {
		if(specialSubFrameConfigContrast==null){
			return null;
		}else if(specialSubFrameConfigContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getRs_epreContrastStr() {
		if(rs_epreContrast==null){
			return null;
		}else if(rs_epreContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getP_aContrastStr() {
		if(p_aContrast==null){
			return null;
		}else if(p_aContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getP_bContrastStr() {
		if(p_bContrast==null){
			return null;
		}else if(p_bContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getCellIdContrastStr() {
		if(cellIdContrast==null){
			return null;
		}else if(cellIdContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getEnbIdContrastStr() {
		if(enbIdContrast==null){
			return null;
		}else if(enbIdContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getTacContrastStr() {
		if(tacContrast==null){
			return null;
		}else if(tacContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getPdcchSymContrastStr() {
		if(pdcchSymContrast==null){
			return null;
		}else if(pdcchSymContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}

	public String getRootSeqContrastStr() {
		if(rootSeqContrast==null){
			return null;
		}else if(rootSeqContrast==1){
			return "一致";
		}else{
			return "不一致";
		}
	}
	
	
}
