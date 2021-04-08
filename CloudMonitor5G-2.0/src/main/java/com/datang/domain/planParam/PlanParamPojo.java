package com.datang.domain.planParam;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.stationTest.StationCellParamCensusPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;

/**
 * 规划工参实体类
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_PLAN_PARAM")
@SuppressWarnings("unused")
public class PlanParamPojo implements Cloneable{

	private Long id;
	
	private String siteName;
	
	private Long gnbId;
	
	private String cellId;
	
	private String cellName;
	
	private Float lon;
	
	private Float lat;
	
	private Integer localCellID;
	
	private String city;
	
	/**
	 * tac
	 */
	private Integer tac;
	
	private Integer pci;
	
	/**
	 * 频点
	 */
	private Integer frequency1;
	
	private Integer tiltM;
	
	/**
	 * 方向角
	 */
	private Integer azimuth;
	
	private Integer height;
	
	/**
	 * nr频率
	 */
	private String nrFrequency;
	/**
	 * 小区带宽
	 */
	private String cellBroadband;
	/**
	 * 特殊子帧配比
	 */
	private String specialRatio;
	/**
	 * 上下行比例
	 */
	private String upAndDownRatio;
	/**
	 * SSB子载波间隔
	 */
	private String ssbWaveInterval;
	/**
	 * SSB发送功率
	 */
	private String ssbSending;
	
	/**
	 * 日志生成时间
	 */
	private Long reportCreateDate;
	
	/**
	 * Tilt E
	 */
	private Integer tiltE;
	
	/**
	 * AAU型号
	 */
	private String  aauModel;
	
	/**
	 * 根序列
	 */
	private String  rootSeq;
	
	/**
	 * 天线厂家
	 */
	private String  antennaManufacturer;
	
	/**
	 * 小区TRx
	 */
	private String  cellTRx;
	
	/**
	 * 帧结构
	 */
	private String  frameStructure;
	
	/**
	 * P-a
	 */
	private String  p_a;
	/**
	 * P-b
	 */
	private String  p_b;
	
	/**
	 * pdcch符号数
	 */
	private String  pdcchSymbol;
	
	/**
	 * 区域
	 */
	private String  region;
	
	
	
	//单站日志表
	//private List<StationVerificationLogPojo> stationVerificationLogPojoList;
	//单站小区参数统计实体类
	private List<StationCellParamCensusPojo> StationCellParamCensusList;
	//单站网优验收测试实体类
	private List<StationNetoptReceiveTestPojo> StationNetoptReceiveTestList;
	//单站性能验收测试实体类
	private List<StationPerformanceReceivePojo> StationPerformanceReceiveList;
	//单站规划参数和基站工程参数合并的实体类
	private List<StationBasicParamPojo> stationBasicParamPojoList;
	
	/**
	 * 小区信息表
	 */
	private CellInfo cellInfo;
	

	/**
	 * 好点ftp上传
	 */
	private Integer goodFtpUpload = 0;//0：未完成  1：完成  2：有更新
	/**
	 * 中点ftp上传
	 */
	private Integer midFtpUpload = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * 差点ftp上传
	 */
	private Integer badFtpUpload = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * 好点ftp下载
	 */
	private Integer goodFtpdownload = 0;//0：未完成  1：完成  2：有更新
	/**
	 * 中点ftp下载
	 */
	private Integer midFtpdownload = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * 差点ftp下载
	 */
	private Integer badFtpdownload = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * 绕点测试
	 */
	private Integer raodianTest = 0;//0：未完成  1：完成  2：有更新
	/**
	 * ping32好点
	 */
	private Integer goodPing32 = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * ping32中点
	 */
	private Integer midPing32 = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * ping32差点
	 */
	private Integer badPing32 = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * ping1500好点
	 */
	private Integer goodPing1500 = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * ping1500中点
	 */
	private Integer midPing1500 = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * ping1500差点
	 */
	private Integer badPing1500 = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * 好点ENDC成功率测试
	 */
	private Integer goodEndcSuccessRatio = 0;//0：未完成  1：完成  2：有更新
	/**
	 * 中点ENDC成功率测试
	 */
	private Integer midEndcSuccessRatio = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * 差点ENDC成功率测试
	 */
	private Integer badEndcSuccessRatio = 0;//0：未完成  1：完成  2：有更新 
	/**
	 * 测试项完成情况 : 无效字段
	 */
	private String testEventAllStatus;
	/**
	 * 无线参数一致性
	 */
	private String wirelessParamStatus;
	/**
	 * 不通过的测试项
	 */
	private String noPassTestEvent;
	
	/**
	 * 基站勘察结果
	 */
	private String stationProspectRlt;
	
	/**
	 * 单站验证模板选择
	 */
	private Integer stationTemplateSelect;

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

	@Column(name = "GNB_ID")
	public Long getGnbId() {
		return gnbId;
	}

	public void setGnbId(Long gnbId) {
		this.gnbId = gnbId;
	}

	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	@Column(name = "LON")
	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	@Column(name = "LAT")
	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	@Column(name = "LOCAL_CELL_ID")
	public Integer getLocalCellID() {
		return localCellID;
	}

	public void setLocalCellID(Integer localCellID) {
		this.localCellID = localCellID;
	}

	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name = "CELL_ID")
	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	@Column(name = "TAC")
	public Integer getTac() {
		return tac;
	}

	public void setTac(Integer tac) {
		this.tac = tac;
	}

	@Column(name = "PCI")
	public Integer getPci() {
		return pci;
	}

	public void setPci(Integer pci) {
		this.pci = pci;
	}

	@Column(name = "FREQUENCY1")
	public Integer getFrequency1() {
		return frequency1;
	}

	public void setFrequency1(Integer frequency1) {
		this.frequency1 = frequency1;
	}

	@Column(name = "TILT_M")
	public Integer getTiltM() {
		return tiltM;
	}

	public void setTiltM(Integer tiltM) {
		this.tiltM = tiltM;
	}

	@Column(name = "AZIMUTH")
	public Integer getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}

	@Column(name = "HEIGHT")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "NR_FREQUENCY")
	public String getNrFrequency() {
		return nrFrequency;
	}

	public void setNrFrequency(String nrFrequency) {
		this.nrFrequency = nrFrequency;
	}

	@Column(name = "CELL_BROADBAND")
	public String getCellBroadband() {
		return cellBroadband;
	}

	public void setCellBroadband(String cellBroadband) {
		this.cellBroadband = cellBroadband;
	}

	@Column(name = "SPECIAL_RATIO")
	public String getSpecialRatio() {
		return specialRatio;
	}

	public void setSpecialRatio(String specialRatio) {
		this.specialRatio = specialRatio;
	}

	@Column(name = "UPANDDOWN_RATIO")
	public String getUpAndDownRatio() {
		return upAndDownRatio;
	}

	public void setUpAndDownRatio(String upAndDownRatio) {
		this.upAndDownRatio = upAndDownRatio;
	}

	@Column(name = "SSB_WAVE_INTERVAL")
	public String getSsbWaveInterval() {
		return ssbWaveInterval;
	}

	public void setSsbWaveInterval(String ssbWaveInterval) {
		this.ssbWaveInterval = ssbWaveInterval;
	}

	@Column(name = "SSB_SENDING")
	public String getSsbSending() {
		return ssbSending;
	}

	public void setSsbSending(String ssbSending) {
		this.ssbSending = ssbSending;
	}

	@Column(name = "REPORT_CREATE_DATE")
	public Long getReportCreateDate() {
		return reportCreateDate;
	}
	
	public void setReportCreateDate(Long reportCreateDate) {
		this.reportCreateDate = reportCreateDate;
	}

	@Column(name = "GOOD_FTP_UPLOAD")
	public Integer getGoodFtpUpload() {
		return goodFtpUpload;
	}

	public void setGoodFtpUpload(Integer goodFtpUpload) {
		this.goodFtpUpload = goodFtpUpload;
	}

	@Column(name = "MID_FTP_UPLOAD")
	public Integer getMidFtpUpload() {
		return midFtpUpload;
	}

	public void setMidFtpUpload(Integer midFtpUpload) {
		this.midFtpUpload = midFtpUpload;
	}

	@Column(name = "BAD_FTP_UPLOAD")
	public Integer getBadFtpUpload() {
		return badFtpUpload;
	}

	public void setBadFtpUpload(Integer badFtpUpload) {
		this.badFtpUpload = badFtpUpload;
	}

	@Column(name = "GOOD_FTP_DOWNLOAD")
	public Integer getGoodFtpdownload() {
		return goodFtpdownload;
	}

	public void setGoodFtpdownload(Integer goodFtpdownload) {
		this.goodFtpdownload = goodFtpdownload;
	}

	@Column(name = "MID_FTP_DOWNLOAD")
	public Integer getMidFtpdownload() {
		return midFtpdownload;
	}

	public void setMidFtpdownload(Integer midFtpdownload) {
		this.midFtpdownload = midFtpdownload;
	}

	@Column(name = "BAD_FTP_DOWNLOAD")
	public Integer getBadFtpdownload() {
		return badFtpdownload;
	}

	public void setBadFtpdownload(Integer badFtpdownload) {
		this.badFtpdownload = badFtpdownload;
	}

	@Column(name = "RAODIAN_TEST")
	public Integer getRaodianTest() {
		return raodianTest;
	}

	public void setRaodianTest(Integer raodianTest) {
		this.raodianTest = raodianTest;
	}

	@Column(name = "GOOD_PING32")
	public Integer getGoodPing32() {
		return goodPing32;
	}

	public void setGoodPing32(Integer goodPing32) {
		this.goodPing32 = goodPing32;
	}

	@Column(name = "GOOD_PING1500")
	public Integer getGoodPing1500() {
		return goodPing1500;
	}

	public void setGoodPing1500(Integer goodPing1500) {
		this.goodPing1500 = goodPing1500;
	}

	@Column(name = "GOOD_ENDC_SUCCESS_RATIO")
	public Integer getGoodEndcSuccessRatio() {
		return goodEndcSuccessRatio;
	}

	public void setGoodEndcSuccessRatio(Integer goodEndcSuccessRatio) {
		this.goodEndcSuccessRatio = goodEndcSuccessRatio;
	}

	@Column(name = "MID_ENDC_SUCCESS_RATIO")
	public Integer getMidEndcSuccessRatio() {
		return midEndcSuccessRatio;
	}

	public void setMidEndcSuccessRatio(Integer midEndcSuccessRatio) {
		this.midEndcSuccessRatio = midEndcSuccessRatio;
	}

	@Column(name = "BAD_ENDC_SUCCESS_RATIO")
	public Integer getBadEndcSuccessRatio() {
		return badEndcSuccessRatio;
	}

	public void setBadEndcSuccessRatio(Integer badEndcSuccessRatio) {
		this.badEndcSuccessRatio = badEndcSuccessRatio;
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

	@Column(name = "NOPASS_TEST_EVENT")
	public String getNoPassTestEvent() {
		return noPassTestEvent;
	}

	public void setNoPassTestEvent(String noPassTestEvent) {
		this.noPassTestEvent = noPassTestEvent;
	}
	
	@Column(name = "STATION_PROSPECT_RLT")
	public String getStationProspectRlt() {
		return stationProspectRlt;
	}

	public void setStationProspectRlt(String stationProspectRlt) {
		this.stationProspectRlt = stationProspectRlt;
	}
	
	@Column(name = "STATION_TEMPLAT_SELECT")
	public Integer getStationTemplateSelect() {
		return stationTemplateSelect;
	}

	public void setStationTemplateSelect(Integer stationTemplateSelect) {
		this.stationTemplateSelect = stationTemplateSelect;
	}

	/*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JSON(serialize = true)
	@JoinColumn(name = "STATION_ID")
	public List<StationVerificationLogPojo> getStationVerificationLogPojoList() {
		return stationVerificationLogPojoList;
	}

	public void setStationVerificationLogPojoList(List<StationVerificationLogPojo> stationVerificationLogPojoList) {
		this.stationVerificationLogPojoList = stationVerificationLogPojoList;
	}*/


	@Column(name = "MID_PING32")
	public Integer getMidPing32() {
		return midPing32;
	}

	public void setMidPing32(Integer midPing32) {
		this.midPing32 = midPing32;
	}

	@Column(name = "BAD_PING32")
	public Integer getBadPing32() {
		return badPing32;
	}

	public void setBadPing32(Integer badPing32) {
		this.badPing32 = badPing32;
	}

	@Column(name = "MID_PING1500")
	public Integer getMidPing1500() {
		return midPing1500;
	}

	public void setMidPing1500(Integer midPing1500) {
		this.midPing1500 = midPing1500;
	}

	@Column(name = "BAD_PING1500")
	public Integer getBadPing1500() {
		return badPing1500;
	}

	public void setBadPing1500(Integer badPing1500) {
		this.badPing1500 = badPing1500;
	}

	@Column(name = "TILT_E")
	public Integer getTiltE() {
		return tiltE;
	}

	public void setTiltE(Integer tiltE) {
		this.tiltE = tiltE;
	}

	@Column(name = "AAU_MODEL")
	public String getAauModel() {
		return aauModel;
	}

	public void setAauModel(String aauModel) {
		this.aauModel = aauModel;
	}

	@Column(name = "ROOTSEQ")
	public String getRootSeq() {
		return rootSeq;
	}

	public void setRootSeq(String rootSeq) {
		this.rootSeq = rootSeq;
	}

	@Column(name = "ANTENNA_MANUFACTURER")
	public String getAntennaManufacturer() {
		return antennaManufacturer;
	}

	public void setAntennaManufacturer(String antennaManufacturer) {
		this.antennaManufacturer = antennaManufacturer;
	}

	@Column(name = "CELL_TRX")
	public String getCellTRx() {
		return cellTRx;
	}

	public void setCellTRx(String cellTRx) {
		this.cellTRx = cellTRx;
	}

	@Column(name = "FRAME_STRUCTURE")
	public String getFrameStructure() {
		return frameStructure;
	}

	public void setFrameStructure(String frameStructure) {
		this.frameStructure = frameStructure;
	}

	@Column(name = "P_A")
	public String getP_a() {
		return p_a;
	}

	public void setP_a(String p_a) {
		this.p_a = p_a;
	}

	@Column(name = "P_B")
	public String getP_b() {
		return p_b;
	}

	public void setP_b(String p_b) {
		this.p_b = p_b;
	}

	@Column(name = "PDCCH_SYMBOL")
	public String getPdcchSymbol() {
		return pdcchSymbol;
	}

	public void setPdcchSymbol(String pdcchSymbol) {
		this.pdcchSymbol = pdcchSymbol;
	}

	@Column(name = "REGION")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planParamPojo", cascade = CascadeType.ALL)
	@JSON(serialize = false)
	public List<StationCellParamCensusPojo> getStationCellParamCensusList() {
		return StationCellParamCensusList;
	}

	public void setStationCellParamCensusList(List<StationCellParamCensusPojo> stationCellParamCensusList) {
		StationCellParamCensusList = stationCellParamCensusList;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planParamPojo", cascade = CascadeType.ALL)
	@JSON(serialize = false)
	public List<StationNetoptReceiveTestPojo> getStationNetoptReceiveTestList() {
		return StationNetoptReceiveTestList;
	}

	public void setStationNetoptReceiveTestList(List<StationNetoptReceiveTestPojo> stationNetoptReceiveTestList) {
		StationNetoptReceiveTestList = stationNetoptReceiveTestList;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planParamPojo", cascade = CascadeType.ALL)
	@JSON(serialize = false)
	public List<StationPerformanceReceivePojo> getStationPerformanceReceiveList() {
		return StationPerformanceReceiveList;
	}

	public void setStationPerformanceReceiveList(List<StationPerformanceReceivePojo> stationPerformanceReceiveList) {
		StationPerformanceReceiveList = stationPerformanceReceiveList;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planParamPojo", cascade = CascadeType.ALL)
	@JSON(serialize = false)
	public List<StationBasicParamPojo> getStationBasicParamPojoList() {
		return stationBasicParamPojoList;
	}

	public void setStationBasicParamPojoList(List<StationBasicParamPojo> stationBasicParamPojoList) {
		this.stationBasicParamPojoList = stationBasicParamPojoList;
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
	
	@Override  
    public Object clone() {  
	 PlanParamPojo planParamPojo = null;  
        try{  
        	planParamPojo = (PlanParamPojo)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return planParamPojo;  
    }  
}
