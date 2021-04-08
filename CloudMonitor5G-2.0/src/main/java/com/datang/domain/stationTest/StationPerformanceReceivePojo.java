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
 * 单站性能验收测试单体类
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_STATION_PERFOR_RECEIVE")
public class StationPerformanceReceivePojo {

	private Long id;
	/**
	 * 规划工参id
	 */
	private Long cellParamId;
	
	private String cellName;
	
	private String siteName;
	
	private String localCellId;
	/**
	 * ftp下载/单用户平均下载速率/序号：1
	 */
	private String ftpDownload;
	/**
	 * FTP上传/单用户平均上传速率/序号：2
	 */
	private String ftpUpload;
	/**
	 * 4G锚点连接建立成功率/序号：3
	 */
	private String lteEpsattachSuccessRation;
	/**
	 * 5G连接建立成功率（gNB添加成功率）/Access Success Rate/序号：4
	 */
	private String nrEdsaddSuccessRation;
	/**
	 * 4G锚点切换成功率/序号：5
	 */
	private String lteInterfreqHandoverSuccessRation;
	/**
	 * 5G切换成功率（gNB变更成功率）/序号：6
	 */
	private String nrEdschangeSuccessRation;
	/**
	 * 4G锚点RRC重建比例/序号：7
	 */
	private String rrcConnectionSuccessRation;
	
	/**
	 * 4G锚点成功值/序号：8
	 */
	private String lteEarfcnSuccessRlt8;
	
	/**
	 * 5G锚点成功值/序号：9
	 */
	private String nrEarfcnSuccessRlt9;
	
	/**
	 * 4G锚点掉线率/ 序号：43
	 */
	private String lteEarfcnSuccessRation;
	/**
	 * 5G掉线率/ 序号：39
	 */
	private String nrEarfcnSuccessRation;
	/**
	 * Ping时延（32Bytes） 序号：10
	 */
	private String ping32SuccessRation;
	/**
	 * Ping时延（2000Bytes） 序号：11
	 */
	private String ping1500SuccessRation;
	/**
	 * PING时延 序号：34
	 */
	private String pingDelay;
	
	/**
	 * 切换验证：PS业务4G锚点切换成功率结果/
	 */
	private String lteSwitchSuccessRation37;
	/**
	 * 切换验证PS业务：5G切换成功率结果（gNB变更成功率）/绕点DT测试指标：5G切换成功率结果（gNB变更成功率）
	 */
	private String nrSwitchSuccessRation38;
	/**
	 * 绕点DT测试指标：5G掉线率结果
	 */
	private String nrEarfcnSuccessRation42;
	/**
	 * DT测试指标：4g掉线率结果
	 */
	private String lteEarfcnSuccessRation46;
	
	/**
	 * NR切换成功率平均值结果
	 */
	private String nrSwitchSuccessAvg50;
	
	/**
	 * 语音业务呼叫建立成功率/网优性能“76”加“77”大于等于10。该指标为“是”，不然，该指标为“否” 
	 */
	private String dialVoSunccess51;
	
	/**
	 * 无线覆盖率/ 1减去网优性能验收表的“75”的结果，大于“0.92”，该指标为“是”，不然，该指标为“否”。
	 */
	private String wireCoverRate54;
	
	/**
	 * NR连接建立成功结果/ “81”大于等于10。该指标为“是”，不然，该指标为“否”。
	 */
	private String nrConnectSuccess55;
	
	/**
	 * NR连接建立时延/ “82”小于等于120。该指标为“是”，不然，该指标为“否”。
	 */
	private String nrConnectTimeDelay58;
	
	/**
	 * “37”大于-85，该指标为“是”，不然，该指标为“否”/rsrp好点是否通过
	 */
	private String rsrpGoodPointRlt61;
	
	/**
	 * 网优验收测试表格里面，“38”大于15，该指标为“是”，不然，该指标为“否”/sinr好点是否通过
	 */
	private String sinrGoodPointRlt62;
	
	/**
	 * “35”小于15（ping32时延门限）该指标为“是”，不然，该指标为“否”/Ping时延（32Byte）是否通过
	 */
	private String ping32TimeRlt67;
	
	/**
	 * “17”除以“8”大于等于1（ping32成功率门限）.且，“17”大于等于20（ping32次数门限），该指标为“是”，不然，该指标为“否”/Ping成功率（32Byte）是否通过
	 */
	private String ping32SucessRlt68;
	
	/**
	 * “36”小于17（ping1500时延门限），该指标为“是”，不然，该指标为“否”/PING包成功率（2000Byte）是否通过
	 */
	private String ping1500TimeRlt69;
	
	/**
	 * “18”除以“9”大于等于1（ping1500成功率门限）.，且，“18”大于等于20（ping1500次数门限），该指标为“是”，不然，该指标为“否”/PING包平均时延（2000Byte）是否通过
	 */
	private String ping1500SucessRlt70;
	
	/**
	 * “39”大于300(下载好点门限)该指标为“是”，不然，该指标为“否”/下行峰值速率是否通过
	 */
	private String downLoadRateRlt79;
	
	/**
	 * “48”大于30(上传好点门限，该指标为“是”，不然，该指标为“否”/上行峰值速率是否通过
	 */
	private String upLoadRateRlt80;
	
	/**
	 * “78”大于“0.98”（5G切换成功率”门限），该指标为“是”，不然，该指标为“否”/切换功能是否通过
	 */
	private String nrSwitchSuccessRlt85;
	
	/**
	 * “99”大于等于10，该指标为“是”，不然，该指标为“否”。
	 */
	private String csfbFallbackSuccessRlt88;
	
	/**
	 * “100”小于等于200，该指标为“是”，不然，该指标为“否”。
	 */
	private String csfbFallbackTimeRlt91;
	
	/**
	 * “22”等于0，且，“13”大于等于6。该指标为“是”，不然，该指标为“否”。
	 */
	private String nrEdschangeGt6Rlt94;
	
	/**
	 * “77”或“98”大于等于10，该指标为“是”，不然，该指标为“否
	 */
	private String voiceBuildSuccessRlt97;
	
	/**
	 * “105”大于等于10(attach尝试次数门限)，该指标为“是”，不然，该指标为“否
	 */
	private String endcGoodNrConnectRlt100;
	
	/**
	 * “118”大于220(上传好点峰值门限)，该指标为“是”，不然，该指标为“否”。
	 */
	private String goodFtpPdcpnrULRlt103;
	
	/**
	 * “117”大于880(下载好点峰值门限)该指标为“是”，不然，该指标为“否”。
	 */
	private String goodFtpPdcpnrDLRlt104;
	
	/**
	 * 1，减去，网优性能验收表的,LocalCellid=1,“75”的结果，大于“0.92”，该指标为“是”，不然，该指标为“否”。
	 */
	private String cellWireCoverRate109;
	
	/**
	 * 序号：112/“119”的结果，大于“0.9”，该指标为“是”，不然，该指标为“否”。
	 */
	private String ssRsrpSinr100Rlt112;
	
	/**
	 * 序号：115/“120”的结果，大于“0.9”，该指标为“是”，不然，该指标为“否”。
	 */
	private String ssRsrpSinr105Rlt115;
	
	/**
	 * 序号：118/“121”的结果，大于“0.95”，该指标为“是”，不然，该指标为“否”。
	 */
	private String ssRsrpSinr110Rlt118;
	
	/**
	 * “37”大于-80，该指标为“是”，不然，该指标为“否”。
	 */
	private String rsrpGoodPointRlt121;
	
	/**
	 * 序号：124/“87”大于“(下载好点门限)-500”，该指标为“是”，不然，该指标为“否”。
	 */
	private String raoGoodPdcpnrDL124;
	
	/**
	 * 序号：127/“88”大于“(上传好点门限)-55”，该指标为“是”，不然，该指标为“否”。
	 */
	private String raoGoodPdcpnrUL127;
	
	/**
	 * 序号：130/ “122”（等于“95”，取网优“95”的值）大于等于1，该指标为“是”，不然，该指标为“否”。
	 */
	private String raoNrConnectBuilRlt130;
	
	/**
	 * 序号：133/ “69”大于-80，该指标为“是”，不然，该指标为“否”。
	 */
	private String raoGoodLteRsrpRlt133;
	
	/**
	 * 序号：134/ “70”大于15，该指标为“是”，不然，该指标为“否”。
	 */
	private String raoGoodLteSinrRlt134;
	
	

	
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

	@Column(name = "SITE_NAME")
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	
	@Column(name = "LOCAL_CELL_ID")
	public String getLocalCellId() {
		return localCellId;
	}
	public void setLocalCellId(String localCellId) {
		this.localCellId = localCellId;
	}
	
	@Column(name = "FTP_DOWNLOAD")
	public String getFtpDownload() {
		return ftpDownload;
	}
	public void setFtpDownload(String ftpDownload) {
		this.ftpDownload = ftpDownload;
	}
	
	@Column(name = "FTP_UPLOAD")
	public String getFtpUpload() {
		return ftpUpload;
	}
	public void setFtpUpload(String ftpUpload) {
		this.ftpUpload = ftpUpload;
	}
	
	@Column(name = "LTEEPSATTACH_SUCCESS_RATION")
	public String getLteEpsattachSuccessRation() {
		return lteEpsattachSuccessRation;
	}
	public void setLteEpsattachSuccessRation(String lteEpsattachSuccessRation) {
		this.lteEpsattachSuccessRation = lteEpsattachSuccessRation;
	}
	
	@Column(name = "NREDSADD_SUCCESS_RATION")
	public String getNrEdsaddSuccessRation() {
		return nrEdsaddSuccessRation;
	}
	public void setNrEdsaddSuccessRation(String nrEdsaddSuccessRation) {
		this.nrEdsaddSuccessRation = nrEdsaddSuccessRation;
	}
	
	@Column(name = "LTEINTERFREQ_SUCCESS_RATION")
	public String getLteInterfreqHandoverSuccessRation() {
		return lteInterfreqHandoverSuccessRation;
	}
	public void setLteInterfreqHandoverSuccessRation(String lteInterfreqHandoverSuccessRation) {
		this.lteInterfreqHandoverSuccessRation = lteInterfreqHandoverSuccessRation;
	}
	
	@Column(name = "NREDSCHANGE_SUCCESS_RATION")
	public String getNrEdschangeSuccessRation() {
		return nrEdschangeSuccessRation;
	}
	public void setNrEdschangeSuccessRation(String nrEdschangeSuccessRation) {
		this.nrEdschangeSuccessRation = nrEdschangeSuccessRation;
	}
	
	@Column(name = "RRCCONNECTION_SUCCESS_RATION")
	public String getRrcConnectionSuccessRation() {
		return rrcConnectionSuccessRation;
	}
	public void setRrcConnectionSuccessRation(String rrcConnectionSuccessRation) {
		this.rrcConnectionSuccessRation = rrcConnectionSuccessRation;
	}
	
	@Column(name = "LTEEARFCN_SUCCESS_RLT8")
	public String getLteEarfcnSuccessRlt8() {
		return lteEarfcnSuccessRlt8;
	}

	public void setLteEarfcnSuccessRlt8(String lteEarfcnSuccessRlt8) {
		this.lteEarfcnSuccessRlt8 = lteEarfcnSuccessRlt8;
	}

	@Column(name = "NREARFCN_SUCCESS_RLT9")
	public String getNrEarfcnSuccessRlt9() {
		return nrEarfcnSuccessRlt9;
	}

	public void setNrEarfcnSuccessRlt9(String nrEarfcnSuccessRlt9) {
		this.nrEarfcnSuccessRlt9 = nrEarfcnSuccessRlt9;
	}

	@Column(name = "LTEEARFCN_SUCCESS_RATION")
	public String getLteEarfcnSuccessRation() {
		return lteEarfcnSuccessRation;
	}
	public void setLteEarfcnSuccessRation(String lteEarfcnSuccessRation) {
		this.lteEarfcnSuccessRation = lteEarfcnSuccessRation;
	}
	
	@Column(name = "NREARFCN_SUCCESS_RATION")
	public String getNrEarfcnSuccessRation() {
		return nrEarfcnSuccessRation;
	}
	public void setNrEarfcnSuccessRation(String nrEarfcnSuccessRation) {
		this.nrEarfcnSuccessRation = nrEarfcnSuccessRation;
	}
	
	@Column(name = "PING32_SUCCESS_RATION")
	public String getPing32SuccessRation() {
		return ping32SuccessRation;
	}
	
	public void setPing32SuccessRation(String ping32SuccessRation) {
		this.ping32SuccessRation = ping32SuccessRation;
	}

	@Column(name = "PING1500_SUCCESS_RATION")
	public String getPing1500SuccessRation() {
		return ping1500SuccessRation;
	}
	
	public void setPing1500SuccessRation(String ping1500SuccessRation) {
		this.ping1500SuccessRation = ping1500SuccessRation;
	}
	
	@Column(name = "PING_DELAY")
	public String getPingDelay() {
		return pingDelay;
	}

	public void setPingDelay(String pingDelay) {
		this.pingDelay = pingDelay;
	}

	@Column(name = "LTESWITCH_SUCCESS_RATION37")
	public String getLteSwitchSuccessRation37() {
		return lteSwitchSuccessRation37;
	}

	public void setLteSwitchSuccessRation37(String lteSwitchSuccessRation37) {
		this.lteSwitchSuccessRation37 = lteSwitchSuccessRation37;
	}

	@Column(name = "NRSWITCH_SUCCESS_RATION38")
	public String getNrSwitchSuccessRation38() {
		return nrSwitchSuccessRation38;
	}

	public void setNrSwitchSuccessRation38(String nrSwitchSuccessRation38) {
		this.nrSwitchSuccessRation38 = nrSwitchSuccessRation38;
	}

	@Column(name = "NREARFCN_SUCCESS_RATION42")
	public String getNrEarfcnSuccessRation42() {
		return nrEarfcnSuccessRation42;
	}

	public void setNrEarfcnSuccessRation42(String nrEarfcnSuccessRation42) {
		this.nrEarfcnSuccessRation42 = nrEarfcnSuccessRation42;
	}

	@Column(name = "LTEEARFCN_SUCCESS_RATION46")
	public String getLteEarfcnSuccessRation46() {
		return lteEarfcnSuccessRation46;
	}

	public void setLteEarfcnSuccessRation46(String lteEarfcnSuccessRation46) {
		this.lteEarfcnSuccessRation46 = lteEarfcnSuccessRation46;
	}

	/**
	 * @return the nrSwitchSuccessAvg50
	 */
	@Column(name = "NR_SWITCH_SUCCESS_AVG50")
	public String getNrSwitchSuccessAvg50() {
		return nrSwitchSuccessAvg50;
	}

	/**
	 * @param nrSwitchSuccessAvg50 the nrSwitchSuccessAvg50 to set
	 */
	public void setNrSwitchSuccessAvg50(String nrSwitchSuccessAvg50) {
		this.nrSwitchSuccessAvg50 = nrSwitchSuccessAvg50;
	}

	/**
	 * @return the dialVoSunccess51
	 */
	@Column(name = "DIAL_VO_SUCCESS51")
	public String getDialVoSunccess51() {
		return dialVoSunccess51;
	}

	/**
	 * @param dialVoSunccess51 the dialVoSunccess51 to set
	 */
	public void setDialVoSunccess51(String dialVoSunccess51) {
		this.dialVoSunccess51 = dialVoSunccess51;
	}

	/**
	 * @return the wireCoverRate54
	 */
	@Column(name = "WIRE_COVER_RATE54")
	public String getWireCoverRate54() {
		return wireCoverRate54;
	}

	/**
	 * @param wireCoverRate54 the wireCoverRate54 to set
	 */
	public void setWireCoverRate54(String wireCoverRate54) {
		this.wireCoverRate54 = wireCoverRate54;
	}
	
	/**
	 * @return the nrConnectSuccess55
	 */
	@Column(name = "NR_CONNECT_SUCCESS55")
	public String getNrConnectSuccess55() {
		return nrConnectSuccess55;
	}

	/**
	 * @param nrConnectSuccess55 the nrConnectSuccess55 to set
	 */
	public void setNrConnectSuccess55(String nrConnectSuccess55) {
		this.nrConnectSuccess55 = nrConnectSuccess55;
	}

	/**
	 * @return the nrConnectTimeDelay58
	 */
	@Column(name = "NR_CONNECT_TIEMDELAY58")
	public String getNrConnectTimeDelay58() {
		return nrConnectTimeDelay58;
	}

	/**
	 * @param nrConnectTimeDelay58 the nrConnectTimeDelay58 to set
	 */
	public void setNrConnectTimeDelay58(String nrConnectTimeDelay58) {
		this.nrConnectTimeDelay58 = nrConnectTimeDelay58;
	}

	/**
	 * @return the rsrpGoodPointRlt61
	 */
	@Column(name = "RSRP_GOOD_POINT_RLT61")
	public String getRsrpGoodPointRlt61() {
		return rsrpGoodPointRlt61;
	}

	/**
	 * @param rsrpGoodPointRlt61 the rsrpGoodPointRlt61 to set
	 */
	public void setRsrpGoodPointRlt61(String rsrpGoodPointRlt61) {
		this.rsrpGoodPointRlt61 = rsrpGoodPointRlt61;
	}

	/**
	 * @return the sinrGoodPointRlt62
	 */
	@Column(name = "SINR_GOOD_POINT_RLT62")
	public String getSinrGoodPointRlt62() {
		return sinrGoodPointRlt62;
	}

	/**
	 * @param sinrGoodPointRlt62 the sinrGoodPointRlt62 to set
	 */
	public void setSinrGoodPointRlt62(String sinrGoodPointRlt62) {
		this.sinrGoodPointRlt62 = sinrGoodPointRlt62;
	}

	/**
	 * @return the ping32TimeRlt67
	 */
	@Column(name = "PING32_TIME_RLT67")
	public String getPing32TimeRlt67() {
		return ping32TimeRlt67;
	}

	/**
	 * @param ping32TimeRlt67 the ping32TimeRlt67 to set
	 */
	public void setPing32TimeRlt67(String ping32TimeRlt67) {
		this.ping32TimeRlt67 = ping32TimeRlt67;
	}

	/**
	 * @return the ping32SucessRlt68
	 */
	@Column(name = "PING32_SUCESS_RLT68")
	public String getPing32SucessRlt68() {
		return ping32SucessRlt68;
	}

	/**
	 * @param ping32SucessRlt68 the ping32SucessRlt68 to set
	 */
	public void setPing32SucessRlt68(String ping32SucessRlt68) {
		this.ping32SucessRlt68 = ping32SucessRlt68;
	}

	/**
	 * @return the ping1500TimeRlt69
	 */
	@Column(name = "PING1500_TIME_RLT69")
	public String getPing1500TimeRlt69() {
		return ping1500TimeRlt69;
	}

	/**
	 * @param ping1500TimeRlt69 the ping1500TimeRlt69 to set
	 */
	public void setPing1500TimeRlt69(String ping1500TimeRlt69) {
		this.ping1500TimeRlt69 = ping1500TimeRlt69;
	}

	/**
	 * @return the ping1500SucessRlt70
	 */
	@Column(name = "PING1500_SUCESS_RLT70")
	public String getPing1500SucessRlt70() {
		return ping1500SucessRlt70;
	}

	/**
	 * @param ping1500SucessRlt70 the ping1500SucessRlt70 to set
	 */
	public void setPing1500SucessRlt70(String ping1500SucessRlt70) {
		this.ping1500SucessRlt70 = ping1500SucessRlt70;
	}

	/**
	 * @return the downLoadRateRlt79
	 */
	@Column(name = "DOWNLOAD_RATE_RLT79")
	public String getDownLoadRateRlt79() {
		return downLoadRateRlt79;
	}

	/**
	 * @param downLoadRateRlt79 the downLoadRateRlt79 to set
	 */
	public void setDownLoadRateRlt79(String downLoadRateRlt79) {
		this.downLoadRateRlt79 = downLoadRateRlt79;
	}

	/**
	 * @return the upLoadRateRlt80
	 */
	@Column(name = "UPLOAD_RATE_RLT80")
	public String getUpLoadRateRlt80() {
		return upLoadRateRlt80;
	}

	/**
	 * @param upLoadRateRlt80 the upLoadRateRlt80 to set
	 */
	public void setUpLoadRateRlt80(String upLoadRateRlt80) {
		this.upLoadRateRlt80 = upLoadRateRlt80;
	}

	/**
	 * @return the nrSwitchSuccessRlt85
	 */
	@Column(name = "NR_SWITCH_SUCCESS_RLT85")
	public String getNrSwitchSuccessRlt85() {
		return nrSwitchSuccessRlt85;
	}

	/**
	 * @param nrSwitchSuccessRlt85 the nrSwitchSuccessRlt85 to set
	 */
	public void setNrSwitchSuccessRlt85(String nrSwitchSuccessRlt85) {
		this.nrSwitchSuccessRlt85 = nrSwitchSuccessRlt85;
	}

	/**
	 * @return the csfbFallbackSuccessRlt88
	 */
	@Column(name = "CSFB_FALLBACK_SUCESS_RLT88")
	public String getCsfbFallbackSuccessRlt88() {
		return csfbFallbackSuccessRlt88;
	}

	/**
	 * @param csfbFallbackSuccessRlt88 the csfbFallbackSuccessRlt88 to set
	 */
	public void setCsfbFallbackSuccessRlt88(String csfbFallbackSuccessRlt88) {
		this.csfbFallbackSuccessRlt88 = csfbFallbackSuccessRlt88;
	}

	/**
	 * @return the csfbFallbackTimeRlt91
	 */
	@Column(name = "CSFB_FALLBACK_TIME_RLT91")
	public String getCsfbFallbackTimeRlt91() {
		return csfbFallbackTimeRlt91;
	}

	/**
	 * @param csfbFallbackTimeRlt91 the csfbFallbackTimeRlt91 to set
	 */
	public void setCsfbFallbackTimeRlt91(String csfbFallbackTimeRlt91) {
		this.csfbFallbackTimeRlt91 = csfbFallbackTimeRlt91;
	}

	/**
	 * @return the nrEdschangeGt6Rlt94
	 */
	@Column(name = "NR_EDSC_GT6_RLT94")
	public String getNrEdschangeGt6Rlt94() {
		return nrEdschangeGt6Rlt94;
	}

	/**
	 * @param nrEdschangeGt6Rlt94 the nrEdschangeGt6Rlt94 to set
	 */
	public void setNrEdschangeGt6Rlt94(String nrEdschangeGt6Rlt94) {
		this.nrEdschangeGt6Rlt94 = nrEdschangeGt6Rlt94;
	}

	/**
	 * @return the voiceBuildSuccessRlt97
	 */
	@Column(name = "VOICE_BUILD_SUCCESS_RLT97")
	public String getVoiceBuildSuccessRlt97() {
		return voiceBuildSuccessRlt97;
	}

	/**
	 * @param voiceBuildSuccessRlt97 the voiceBuildSuccessRlt97 to set
	 */
	public void setVoiceBuildSuccessRlt97(String voiceBuildSuccessRlt97) {
		this.voiceBuildSuccessRlt97 = voiceBuildSuccessRlt97;
	}

	@Column(name = "ENDCGOOD_NRCONNECT_RLT100")
	public String getEndcGoodNrConnectRlt100() {
		return endcGoodNrConnectRlt100;
	}

	public void setEndcGoodNrConnectRlt100(String endcGoodNrConnectRlt100) {
		this.endcGoodNrConnectRlt100 = endcGoodNrConnectRlt100;
	}
	
	@Column(name = "GOOD_FTP_PDCPNRUL_RLT103")
	public String getGoodFtpPdcpnrULRlt103() {
		return goodFtpPdcpnrULRlt103;
	}

	public void setGoodFtpPdcpnrULRlt103(String goodFtpPdcpnrULRlt103) {
		this.goodFtpPdcpnrULRlt103 = goodFtpPdcpnrULRlt103;
	}

	@Column(name = "GOOD_FTP_PDCPNRDL_RLT104")
	public String getGoodFtpPdcpnrDLRlt104() {
		return goodFtpPdcpnrDLRlt104;
	}

	public void setGoodFtpPdcpnrDLRlt104(String goodFtpPdcpnrDLRlt104) {
		this.goodFtpPdcpnrDLRlt104 = goodFtpPdcpnrDLRlt104;
	}

	@Column(name = "CELL_WIRE_COVER_RATE109")
	public String getCellWireCoverRate109() {
		return cellWireCoverRate109;
	}

	public void setCellWireCoverRate109(String cellWireCoverRate109) {
		this.cellWireCoverRate109 = cellWireCoverRate109;
	}

	@Column(name = "RSRP_GOOD_POINT_RLT121")
	public String getRsrpGoodPointRlt121() {
		return rsrpGoodPointRlt121;
	}

	public void setRsrpGoodPointRlt121(String rsrpGoodPointRlt121) {
		this.rsrpGoodPointRlt121 = rsrpGoodPointRlt121;
	}

	@Column(name = "RAO_GOOD_PDCPNRDL_RLT124")
	public String getRaoGoodPdcpnrDL124() {
		return raoGoodPdcpnrDL124;
	}

	public void setRaoGoodPdcpnrDL124(String raoGoodPdcpnrDL124) {
		this.raoGoodPdcpnrDL124 = raoGoodPdcpnrDL124;
	}

	@Column(name = "RAO_GOOD_PDCPNRUL_RLT127")
	public String getRaoGoodPdcpnrUL127() {
		return raoGoodPdcpnrUL127;
	}

	public void setRaoGoodPdcpnrUL127(String raoGoodPdcpnrUL127) {
		this.raoGoodPdcpnrUL127 = raoGoodPdcpnrUL127;
	}

	@Column(name = "RAO_NRCON_BUILD_RLT130")
	public String getRaoNrConnectBuilRlt130() {
		return raoNrConnectBuilRlt130;
	}

	public void setRaoNrConnectBuilRlt130(String raoNrConnectBuilRlt130) {
		this.raoNrConnectBuilRlt130 = raoNrConnectBuilRlt130;
	}

	@Column(name = "RAO_GOOD_LTERSRP_RLT133")
	public String getRaoGoodLteRsrpRlt133() {
		return raoGoodLteRsrpRlt133;
	}

	public void setRaoGoodLteRsrpRlt133(String raoGoodLteRsrpRlt133) {
		this.raoGoodLteRsrpRlt133 = raoGoodLteRsrpRlt133;
	}

	@Column(name = "RAO_GOOD_LTESINR_RLT134")
	public String getRaoGoodLteSinrRlt134() {
		return raoGoodLteSinrRlt134;
	}

	public void setRaoGoodLteSinrRlt134(String raoGoodLteSinrRlt134) {
		this.raoGoodLteSinrRlt134 = raoGoodLteSinrRlt134;
	}

	@Column(name = "SSRSRP_SINR100_RLT112")
	public String getSsRsrpSinr100Rlt112() {
		return ssRsrpSinr100Rlt112;
	}

	public void setSsRsrpSinr100Rlt112(String ssRsrpSinr100Rlt112) {
		this.ssRsrpSinr100Rlt112 = ssRsrpSinr100Rlt112;
	}

	@Column(name = "SSRSRP_SINR105_RLT115")
	public String getSsRsrpSinr105Rlt115() {
		return ssRsrpSinr105Rlt115;
	}

	public void setSsRsrpSinr105Rlt115(String ssRsrpSinr105Rlt115) {
		this.ssRsrpSinr105Rlt115 = ssRsrpSinr105Rlt115;
	}

	@Column(name = "SSRSRP_SINR110_RLT118")
	public String getSsRsrpSinr110Rlt118() {
		return ssRsrpSinr110Rlt118;
	}

	public void setSsRsrpSinr110Rlt118(String ssRsrpSinr110Rlt118) {
		this.ssRsrpSinr110Rlt118 = ssRsrpSinr110Rlt118;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PLAN_PARAM_ID")
	public PlanParamPojo getPlanParamPojo() {
		return planParamPojo;
	}
	
	public void setPlanParamPojo(PlanParamPojo planParamPojo) {
		this.planParamPojo = planParamPojo;
	}
	

	
}
