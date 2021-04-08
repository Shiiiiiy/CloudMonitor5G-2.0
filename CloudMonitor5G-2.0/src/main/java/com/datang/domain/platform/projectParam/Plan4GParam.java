package com.datang.domain.platform.projectParam;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.oppositeOpen3d.OppositeOpen3dPerformanceReceivePojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dResultPojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dWirelessPojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.stationTest.StationCellParamCensusPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;

/**
 * 规划工参4G小区实体类
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_PROJECT_PLAN_4GPARAM")
public class Plan4GParam implements Cloneable{


	/**
	 * 主键
	 */
	private Long id; 
	/**
	 * MCC选填:所在PLMN网络的移动国家码
	 */
	private String mcc;
	/**
	 * MNC选填:所在PLMN网络的移动网络码
	 */
	private String mnc;
	/**
	 * 选填	eNB编号
	 */
	private String enbId;
	/**
	 * SiteName必填:基站名称
	 */
	private String siteName;
	/**
	 * CellName必填:小区友好名
	 */
	private String cellName;
	/**
	 * LocalCellId
	 * 必填:eNB内的小区ID，唯一标识在一个eNB内的小区。在eNB内统一分配，同一个eNB中的LocalCellId配置值要求不能重复
	 */
	private Integer localCellId;
	/**
	 * CELL ID必填:小区标识
	 */
	private Long cellId;
	/**
	 * reverseOpen3d选填:是否反开3d
	 */
	private String reverseOpen3d;
	/**
	 * ECI选填:ECI
	 */
//	private String eci;
	/**
	 * TAC选填:跟踪号
	 */
	private Integer tac;
	/**
	 * 带宽,选填
	 */
	private Float broadBand;
	/**
	 *Frequency_DL,选填 频率
	 */
	private String frequencyDl;
	/**
	 * PCI 必填:物理小区ID
	 */
	private Long pci;
	/**
	 * Longitude  必填:经度
	 */
	private Float longitude;
	
	/**
	 * latitude 必填:纬度
	 */
	private Float latitude;
	/**
	 * high 选填:高度
	 */
	private Float high;
	/**
	 * beamWidth 选填:波束宽度
	 */
	private Float beamWidth;
	/**
	 * Azimuth 必填:方向角
	 */
	private Float azimuth;
	/**
	 * Tilt Total选填:下倾角（度）
	 */
	private Float totalTilt;
	/**
	 * tilt_m选填:机械下倾角（度）
	 */
	private Float tilt_m;
	/**
	 * tilt_e选填:电子下倾角（度）
	 */
	private Float tilt_e;
	
	/**
	 * Type 选填:类型
	 */
	private String type;
	/**
	 * earfcn 必填
	 */
	private String earfcn;
	/**
	 * subFrameConfigsubFrameConfig  选填：子帧配置
	 */
	private String subFrameConfig;
	/**
	 * specialSubFrameConfig  选填:特殊子帧配置
	 */
	private String specialSubFrameConfig;
	/**
	 * rs_epre  选填:RS EPRE
	 */
	private String rs_epre;
	/**
	 * p_a  选填:p-a
	 */
	private String p_a;
	/**
	 * p_b  选填:p-b
	 */
	private String p_b;
	/**
	 * 根序列
	 */
	private String  rootSeq;
	/**
	 * AAU型号
	 */
	private String  aauModel;
	
	/**
	 * 天线厂家
	 */
	private String  antennaManufacturer;
	/**
	 * 帧结构
	 */
	private String  frameStructure;
	/**
	 * pdcch符号数
	 */
	private String  pdcchSymbol;
	
	
	/**
	 * 区域
	 */
	private String region;
	
	/**
	 * 小区信息表
	 */
	private CellInfo cellInfo;
	
	/**
	 * 生成报告日期
	 */
	private Long createReportDate;
	
	/**
	 * 极好点FTP下载
	 */
	private Integer ftpDownloadGood = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 中点FTP下载
	 */
	private Integer ftpdownloadMid = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 差点FTP下载
	 */
	private Integer ftpdownloadBad = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 极好点FTP上传
	 */
	private Integer ftpUploadGood = 0;//0：未完成  1：完成  2：有更新
	/**
	 * 中点FTP上传
	 */
	private Integer ftpUploadMid = 0;//0：未完成  1：完成  2：有更新
	/**
	 * 差点FTP上传
	 */
	private Integer ftpUploadBad = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 极好点ping（32bytes）
	 */
	private Integer ping32Good = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 中好点ping（32bytes）
	 */
	private Integer ping32Mid = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 差点ping（32bytes）
	 */
	private Integer ping32Bad = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 极好点ping（1500bytes）
	 */
	private Integer ping1500Good = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 中好点ping（1500bytes）
	 */
	private Integer ping1500Mid = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 差点ping（1500bytes）
	 */
	private Integer ping1500Bad = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * ENDC成功率测试测试
	 */
	private Integer goodEndcSuccessRatio = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * 绕点
	 */
	private Integer raodianTest = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * CSFB测试
	 */
	private Integer csfTest = 0;//0：未完成  1：完成  2：有更新
	
	/**
	 * Volte测试
	 */
	private Integer volteTest = 0;//0：未完成  1：完成  2：有更新
	
	
	/**
	 * 不通过的测试项
	 */
	private String noPassTestEvent;
	
	/**
	 * 测试项完成情况
	 */
	private String testEventAllStatus;
	/**
	 * 无线参数一致性
	 */
	private String wirelessParamStatus;
	
	/**
	 * 基站勘察结果
	 */
	private String stationProspectRlt;
	
	/**
	 * 反开3d验证模板选择
	 */
	private Integer opposOpenTemplateSelect;
	
	//反开3d无线参数表
	private OppositeOpen3dWirelessPojo OppositeOpen3dWirelessPojo;
	
	//性能验收实体类
	private OppositeOpen3dPerformanceReceivePojo oppositeOpen3dPerformanceReceivePojo;
	
	private OppositeOpen3dResultPojo oppositeOpen3dResultPojo;
	
	//单站规划参数和基站工程参数合并的实体类
	private List<StationBasicParamPojo> stationBasicParamPojoList;
	

	/**
	 * @return the id
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

	/**
	 * @return the mcc
	 */
	@Column(name = "MCC")
	public String getMcc() {
		return mcc;
	}

	/**
	 * @param mcc the mcc to set
	 */
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the mnc
	 */
	@Column(name = "MNC")
	public String getMnc() {
		return mnc;
	}

	/**
	 * @param mnc the mnc to set
	 */
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	/**
	 * @return the enbId
	 */
	@Column(name = "ENB_ID")
	public String getEnbId() {
		return enbId;
	}

	/**
	 * @param enbId the enbId to set
	 */
	public void setEnbId(String enbId) {
		this.enbId = enbId;
	}

	/**
	 * @return the siteName
	 */
	@Column(name = "SITE_NAME")
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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

	/**
	 * @return the localCellId
	 */
	@Column(name = "LOCAL_CELL_ID")
	public Integer getLocalCellId() {
		return localCellId;
	}

	/**
	 * @param localCellId the localCellId to set
	 */
	public void setLocalCellId(Integer localCellId) {
		this.localCellId = localCellId;
	}

	/**
	 * @return the cellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the reverseOpen3d
	 */
	@Column(name = "REVERSE_OPEN_3D")
	public String getReverseOpen3d() {
		return reverseOpen3d;
	}

	/**
	 * @param reverseOpen3d the reverseOpen3d to set
	 */
	public void setReverseOpen3d(String reverseOpen3d) {
		this.reverseOpen3d = reverseOpen3d;
	}

	/**
	 * @return the eci
	 */
//	@Column(name = "ECI")
//	public String getEci() {
//		return eci;
//	}

	/**
	 * @param eci the eci to set
	 */
//	public void setEci(String eci) {
//		this.eci = eci;
//	}

	/**
	 * @return the tac
	 */
	@Column(name = "TAC")
	public Integer getTac() {
		return tac;
	}

	/**
	 * @param tac the tac to set
	 */
	public void setTac(Integer tac) {
		this.tac = tac;
	}

	/**
	 * @return the broadBand
	 */
	@Column(name = "BROAD_BAND")
	public Float getBroadBand() {
		return broadBand;
	}

	/**
	 * @param broadBand the broadBand to set
	 */
	public void setBroadBand(Float broadBand) {
		this.broadBand = broadBand;
	}

	/**
	 * @return the frequencyDl
	 */
	@Column(name = "FREQUENCY_DL")
	public String getFrequencyDl() {
		return frequencyDl;
	}

	/**
	 * @param frequencyDl the frequencyDl to set
	 */
	public void setFrequencyDl(String frequencyDl) {
		this.frequencyDl = frequencyDl;
	}

	/**
	 * @return the pci
	 */
	@Column(name = "PCI")
	public Long getPci() {
		return pci;
	}

	/**
	 * @param pci the pci to set
	 */
	public void setPci(Long pci) {
		this.pci = pci;
	}

	/**
	 * @return the longitude
	 */
	@Column(name = "LON")
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	@Column(name = "LAT")
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the high
	 */
	@Column(name = "HIGH")
	public Float getHigh() {
		return high;
	}

	/**
	 * @param high the high to set
	 */
	public void setHigh(Float high) {
		this.high = high;
	}

	/**
	 * @return the beamWidth
	 */
	@Column(name = "BEAM_WIDTH")
	public Float getBeamWidth() {
		return beamWidth;
	}

	/**
	 * @param beamWidth the beamWidth to set
	 */
	public void setBeamWidth(Float beamWidth) {
		this.beamWidth = beamWidth;
	}

	/**
	 * @return the azimuth
	 */
	@Column(name = "AZIMUTH")
	public Float getAzimuth() {
		return azimuth;
	}

	/**
	 * @param azimuth the azimuth to set
	 */
	public void setAzimuth(Float azimuth) {
		this.azimuth = azimuth;
	}

	/**
	 * @return the totalTilt
	 */
	@Column(name = "TOTAL_TILT")
	public Float getTotalTilt() {
		return totalTilt;
	}

	/**
	 * @param totalTilt the totalTilt to set
	 */
	public void setTotalTilt(Float totalTilt) {
		this.totalTilt = totalTilt;
	}

	@Column(name = "TILT_M")
	public Float getTilt_m() {
		return tilt_m;
	}

	public void setTilt_m(Float tilt_m) {
		this.tilt_m = tilt_m;
	}

	@Column(name = "TILT_E")
	public Float getTilt_e() {
		return tilt_e;
	}

	public void setTilt_e(Float tilt_e) {
		this.tilt_e = tilt_e;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the earfcn
	 */
	@Column(name = "EARFCN")
	public String getEarfcn() {
		return earfcn;
	}

	/**
	 * @param earfcn the earfcn to set
	 */
	public void setEarfcn(String earfcn) {
		this.earfcn = earfcn;
	}

	/**
	 * @return the subFrameConfig
	 */
	@Column(name = "SUBFRAME_CONFIG")
	public String getSubFrameConfig() {
		return subFrameConfig;
	}

	/**
	 * @param subFrameConfig the subFrameConfig to set
	 */
	public void setSubFrameConfig(String subFrameConfig) {
		this.subFrameConfig = subFrameConfig;
	}

	/**
	 * @return the specialSubFrameConfig
	 */
	@Column(name = "SPECIAL_SUBFRAME_CONFIG")
	public String getSpecialSubFrameConfig() {
		return specialSubFrameConfig;
	}

	/**
	 * @param specialSubFrameConfig the specialSubFrameConfig to set
	 */
	public void setSpecialSubFrameConfig(String specialSubFrameConfig) {
		this.specialSubFrameConfig = specialSubFrameConfig;
	}

	/**
	 * @return the rs_epre
	 */
	@Column(name = "RS_RPRE")
	public String getRs_epre() {
		return rs_epre;
	}

	/**
	 * @param rs_epre the rs_epre to set
	 */
	public void setRs_epre(String rs_epre) {
		this.rs_epre = rs_epre;
	}

	/**
	 * @return the p_a
	 */
	@Column(name = "P_A")
	public String getP_a() {
		return p_a;
	}

	/**
	 * @param p_a the p_a to set
	 */
	public void setP_a(String p_a) {
		this.p_a = p_a;
	}

	/**
	 * @return the p_b
	 */
	@Column(name = "P_B")
	public String getP_b() {
		return p_b;
	}

	/**
	 * @param p_b the p_b to set
	 */
	public void setP_b(String p_b) {
		this.p_b = p_b;
	}

	@Column(name = "ROOTSEQ")
	public String getRootSeq() {
		return rootSeq;
	}

	public void setRootSeq(String rootSeq) {
		this.rootSeq = rootSeq;
	}

	@Column(name = "AAU_MODEL")
	public String getAauModel() {
		return aauModel;
	}

	public void setAauModel(String aauModel) {
		this.aauModel = aauModel;
	}

	@Column(name = "ANTENNA_MANUFACTURER")
	public String getAntennaManufacturer() {
		return antennaManufacturer;
	}

	public void setAntennaManufacturer(String antennaManufacturer) {
		this.antennaManufacturer = antennaManufacturer;
	}

	@Column(name = "FRAME_STRUCTURE")
	public String getFrameStructure() {
		return frameStructure;
	}

	public void setFrameStructure(String frameStructure) {
		this.frameStructure = frameStructure;
	}

	@Column(name = "PDCCH_SYMBOL")
	public String getPdcchSymbol() {
		return pdcchSymbol;
	}

	public void setPdcchSymbol(String pdcchSymbol) {
		this.pdcchSymbol = pdcchSymbol;
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

	@Column(name="CREATEREPORT_DATE")
	public Long getCreateReportDate() {
		return createReportDate;
	}

	public void setCreateReportDate(Long createReportDate) {
		this.createReportDate = createReportDate;
	}

	@Column(name="FTPDOWNLOAD_GOOD")
	public Integer getFtpDownloadGood() {
		if(ftpDownloadGood==null){
			return 0;
		}else{
			return ftpDownloadGood;
		}
	}

	public void setFtpDownloadGood(Integer ftpDownloadGood) {
		this.ftpDownloadGood = ftpDownloadGood;
	}

	@Column(name="FTPUPLOAD_GOOD")
	public Integer getFtpUploadGood() {
		if(ftpUploadGood==null){
			return 0;
		}else{
			return ftpUploadGood;
		}
	}

	public void setFtpUploadGood(Integer ftpUploadGood) {
		this.ftpUploadGood = ftpUploadGood;
	}

	@Column(name="PING32_GOOD")
	public Integer getPing32Good() {
		if(ping32Good==null){
			return 0;
		}else{
			return ping32Good;
		}
	}

	public void setPing32Good(Integer ping32Good) {
		this.ping32Good = ping32Good;
	}

	@Column(name="GOODENDC_SUCCESS_RATIO")
	public Integer getGoodEndcSuccessRatio() {
		if(goodEndcSuccessRatio==null){
			return 0;
		}else{
			return goodEndcSuccessRatio;
		}
	}

	public void setGoodEndcSuccessRatio(Integer goodEndcSuccessRatio) {
		this.goodEndcSuccessRatio = goodEndcSuccessRatio;
	}

	@Column(name="RAODIAN_TEST")
	public Integer getRaodianTest() {
		if(raodianTest==null){
			return 0;
		}else{
			return raodianTest;
		}
	}

	public void setRaodianTest(Integer raodianTest) {
		this.raodianTest = raodianTest;
	}

	@Column(name="CSF_TEST")
	public Integer getCsfTest() {
		if(csfTest==null){
			return 0;
		}else{
			return csfTest;
		}
	}

	public void setCsfTest(Integer csfTest) {
		this.csfTest = csfTest;
	}

	@Column(name="VOLTE_TEST")
	public Integer getVolteTest() {
		if(volteTest==null){
			return 0;
		}else{
			return volteTest;
		}
	}

	public void setVolteTest(Integer volteTest) {
		this.volteTest = volteTest;
	}

	@Column(name = "NOPASS_TEST_EVENT")
	public String getNoPassTestEvent() {
		return noPassTestEvent;
	}

	public void setNoPassTestEvent(String noPassTestEvent) {
		this.noPassTestEvent = noPassTestEvent;
	}

	@Column(name = "TEST_EVENT_ALLSTATUS")
	public String getTestEventAllStatus() {
		return testEventAllStatus;
	}

	public void setTestEventAllStatus(String testEventAllStatus) {
		this.testEventAllStatus = testEventAllStatus;
	}

	@Column(name = "WIRELESS_PARAM_STATUS")
	public String getWirelessParamStatus() {
		return wirelessParamStatus;
	}

	public void setWirelessParamStatus(String wirelessParamStatus) {
		this.wirelessParamStatus = wirelessParamStatus;
	}
	
	@Column(name = "STATION_PROSPECT_RLT")
	public String getStationProspectRlt() {
		return stationProspectRlt;
	}

	public void setStationProspectRlt(String stationProspectRlt) {
		this.stationProspectRlt = stationProspectRlt;
	}
	
	@Column(name = "OPPOSOPEN_TEMPLAT_SELECT")
	public Integer getOpposOpenTemplateSelect() {
		return opposOpenTemplateSelect;
	}

	public void setOpposOpenTemplateSelect(Integer opposOpenTemplateSelect) {
		this.opposOpenTemplateSelect = opposOpenTemplateSelect;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "plan4GParam")
	public OppositeOpen3dPerformanceReceivePojo getOppositeOpen3dPerformanceReceivePojo() {
		return oppositeOpen3dPerformanceReceivePojo;
	}

	public void setOppositeOpen3dPerformanceReceivePojo(
			OppositeOpen3dPerformanceReceivePojo oppositeOpen3dPerformanceReceivePojo) {
		this.oppositeOpen3dPerformanceReceivePojo = oppositeOpen3dPerformanceReceivePojo;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "plan4GParam")
	public OppositeOpen3dResultPojo getOppositeOpen3dResultPojo() {
		return oppositeOpen3dResultPojo;
	}

	public void setOppositeOpen3dResultPojo(OppositeOpen3dResultPojo oppositeOpen3dResultPojo) {
		this.oppositeOpen3dResultPojo = oppositeOpen3dResultPojo;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "plan4GParam")
	public OppositeOpen3dWirelessPojo getOppositeOpen3dWirelessPojo() {
		return OppositeOpen3dWirelessPojo;
	}

	public void setOppositeOpen3dWirelessPojo(OppositeOpen3dWirelessPojo oppositeOpen3dWirelessPojo) {
		OppositeOpen3dWirelessPojo = oppositeOpen3dWirelessPojo;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plan4GParam", cascade = CascadeType.ALL)
	@JSON(serialize = false)
	public List<StationBasicParamPojo> getStationBasicParamPojoList() {
		return stationBasicParamPojoList;
	}

	public void setStationBasicParamPojoList(List<StationBasicParamPojo> stationBasicParamPojoList) {
		this.stationBasicParamPojoList = stationBasicParamPojoList;
	}

	@Column(name = "FTPDOWNLOAD_MID")
	public Integer getFtpdownloadMid() {
		if(ftpdownloadMid==null){
			return 0;
		}else{
			return ftpdownloadMid;
		}
	}

	public void setFtpdownloadMid(Integer ftpdownloadMid) {
		this.ftpdownloadMid = ftpdownloadMid;
	}

	@Column(name = "FTPDOWNLOAD_BAD")
	public Integer getFtpdownloadBad() {
		if(ftpdownloadBad==null){
			return 0;
		}else{
			return ftpdownloadBad;
		}
	}

	public void setFtpdownloadBad(Integer ftpdownloadBad) {
		this.ftpdownloadBad = ftpdownloadBad;
	}

	@Column(name = "FTPUPLOAD_MID")
	public Integer getFtpUploadMid() {
		if(ftpUploadMid==null){
			return 0;
		}else{
			return ftpUploadMid;
		}
	}

	public void setFtpUploadMid(Integer ftpUploadMid) {
		this.ftpUploadMid = ftpUploadMid;
	}

	@Column(name = "FTPUPLOAD_BAD")
	public Integer getFtpUploadBad() {
		if(ftpUploadBad==null){
			return 0;
		}else{
			return ftpUploadBad;
		}
	}

	public void setFtpUploadBad(Integer ftpUploadBad) {
		this.ftpUploadBad = ftpUploadBad;
	}

	@Column(name = "PING32_MID")
	public Integer getPing32Mid() {
		if(ping32Mid==null){
			return 0;
		}else{
			return ping32Mid;
		}
	}

	public void setPing32Mid(Integer ping32Mid) {
		this.ping32Mid = ping32Mid;
	}

	@Column(name = "PING32_BAD")
	public Integer getPing32Bad() {
		if(ping32Bad==null){
			return 0;
		}else{
			return ping32Bad;
		}
	}

	public void setPing32Bad(Integer ping32Bad) {
		this.ping32Bad = ping32Bad;
	}

	@Column(name = "PING1500_GOOD")
	public Integer getPing1500Good() {
		if(ping1500Good==null){
			return 0;
		}else{
			return ping1500Good;
		}
	}

	public void setPing1500Good(Integer ping1500Good) {
		this.ping1500Good = ping1500Good;
	}

	@Column(name = "PING1500_MID")
	public Integer getPing1500Mid() {
		if(ping1500Mid==null){
			return 0;
		}else{
			return ping1500Mid;
		}
	}

	public void setPing1500Mid(Integer ping1500Mid) {
		this.ping1500Mid = ping1500Mid;
	}

	@Column(name = "PING1500_BAD")
	public Integer getPing1500Bad() {
		if(ping1500Bad==null){
			return 0;
		}else{
			return ping1500Bad;
		}
	}

	public void setPing1500Bad(Integer ping1500Bad) {
		this.ping1500Bad = ping1500Bad;
	}
	
	@Override  
    public Object clone() {  
		Plan4GParam plan4GParam = null;  
        try{  
        	plan4GParam = (Plan4GParam)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return plan4GParam;  
    }
	
}
