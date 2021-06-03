package com.datang.domain.stationTest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 单验日志指标详表
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_5G_EXCEPTION_CELL_LOG")
public class EceptionCellLogPojo {

	private Long id;
	
	private String nrCellname;

	private String nrRegion;

	private String nrTestdate;
	
	private String nrWirelessstation;
	
	/**
	 * 工单号
	 */
	private String nrDy;
	/**
	 * 测试业务
	 */
	private String nrTestevent;
	
	private String nrRelatelogname;
	/**
	 * 采样点数
	 */
	private String nrSamplenumber;
	
	private String nrPlancellid;
	
	private String nrPlanpci;
	
	private String nrPlanlocalcellid;
	
	/**
	 * 频率
	 */
	private String nrFrequery;
	
	private String nrCellid1;
	
	private String nrCellid2;
	
	private String nrCellid3;
	
	private String nrPci1;
	
	private String nrPci2;
	
	private String nrPci3;
	/**
	 * lte频率
	 */
	private String lteFrequery;
	
	private String lteCellid1;
	
	private String lteCellid2;
	
	private String lteCellid3;
	
	private String ltePci1;
	
	private String ltePci2;
	
	private String ltePci3;
	
	/**
	 * 频点
	 */
	private String nrPoint;
	/**
	 * 带宽
	 */
	private String nrBandwidth;
	/**
	 * 频点
	 */
	private String ltePoint;
	/**
	 * 带宽
	 */
	private String lteBandwidth;
	/**
	 * 特殊子帧配比
	 */
	private String nrSspratio;
	
	/**
	 * 上下行比例
	 */
	private String nrSaratio;
	/**
	 * SSB子载波间隔
	 */
	private String nrSubcarrierspacing;
	
	private String nrTac;
	
	private String lteTac;
	/**
	 * SSB发送功率
	 */
	private String nrPuschtxpower;

	/**
	 * 4G锚点连接建立成功次数
	 */
	private String lteEpsattachsuccess;
	
	/**
	 * 4G锚点连接建立失败次数
	 */
	private String lteEpsattachfailure;
	/**
	 * 5G连接建立成功次数
	 */
	private String nrEdsaddsuccess;
	
	/**
	 * 5G连接建立失败次数
	 */
	private String nrEdsaddfailure;
	
	/**
	 * 4G锚点切换成功次数
	 */
	private String lteInterfreqhandoversuccess;
	
	/**
	 * 4G锚点切换失败次数
	 */
	private String lteInterfreqhandoverfailure;
	
	/**
	 * 5G连接切换成功次数
	 */
	private String nrEdschangesuccess;
	
	/**
	 * 5G连接切换失败次数
	 */
	private String nrEdschangefailure;
	
	/**
	 * 4G锚点RRC重建成功次数
	 */
	private String rrcConnectionsuccess;
	
	/**
	 * 4G锚点RRC重建失败次数
	 */
	private String rrcConnectionfailure;
	
	/**
	 * 4G锚点成功次数
	 */
	private String lteEarfcnsuccess;
	
	/**
	 * 4G锚点掉线次数
	 */
	private String lteEarfcndrop;
	
	/**
	 *5G成功次数
	 */
	private String nrEarfcnsuccess;
	
	/**
	 * 5G掉线次数
	 */
	private String nrEarfcndrop;
	
	/**
	 *ping时延
	 */
	private String nrPingresqtime;
	
	/**
	 * ping成功次数
	 */
	private String nrPingrespose;
	
	/**
	 * ping失败次数
	 */
	private String nrPingfailure;
	
	/**
	 *NR RSRP
	 */
	private String nrRsrpavg;
	
	/**
	 * NR SINR
	 */
	private String nrSinr;
	
	/**
	 * NR下载吞吐率
	 */
	private String nrThrputdl;
	
	/**
	 * NR上传吞吐率
	 */
	private String nrThrputul;
	
	/**
	 * LTE特殊子帧配置
	 */
	private String lteSspratio;
	
	/**
	 * LTE子帧配置
	 */
	private String lteSpratio;
	
	/**
	 * rsEpre
	 */
	private String rsErpe;
	
	/**
	 * p_a
	 */
	private String pA;
	
	/**
	 * p_b
	 */
	private String pB;
	
	/**
	 * 往本站1小区切换成功次数
	 */
	private String toStation1Change;
	
	/**
	 * 往本站2小区切换成功次数
	 */
	private String toStation2Change;
	
	/**
	 * 往本站3小区切换成功次数
	 */
	private String toStation3Change;

	/**
	 * lte rsrp
	 */
	private String lteRsrp;

	/**
	 * lte sinr
	 */
	private String lteSinr;
	/**
	 * lte下载吞吐率
	 */
	private String lteThrputdl;
	
	/**
	 * lte上传吞吐率
	 */
	private String lteThrputul;
	
	/**
	 * 根序列码
	 */
	private String nrPreambleFormat;
	
	/**
	 * rs power
	 */
	private String rsPower;

	/**
	 * pdcch符号数
	 */
	private String pdcch;

	/**
	 * RRC建立成功次数
	 */
	private String rrcConnectionSetupComplete;
	
	/**
	 * ERAB建立成功次数
	 */
	private String erabConnectionSetupSuccess;
	
	/**
	 * Volte成功次数
	 */
	private String volteSuccess;
	
	/**
	 * Volte掉话次数
	 */
	private String volteDrop;
	
	/**
	 * CSFB成功次数
	 */
	private String csfbSuccess;
	
	/**
	 * CSFB接通时延
	 */
	private String csfbRequesttime;
	
	/**
	 * CSFB未接通次数
	 */
	private String csfbNotRequest;
	
	/**
	 * CSFB成功回落次数
	 */
	private String csfbCallbackSuccess;
	
	/**
	 * CSFB回落时延
	 */
	private String csfbCallbackTime;
	
	/**
	 * CSFB回落失败次数
	 */
	private String csfbCallbackDrop;
	
	/**
	 * TRx
	 */
	private String nrTrx;
	
	/**
	 * NR帧结构
	 */
	private String nrFrameStructure;
	
	/**
	 * NR下载吞吐率max
	 */
	private Float nrThrputDLMax;
	
	/**
	 * NR上传吞吐率max
	 */
	private Float nrThrputULMax;
	
	/**
	 * LTE下载吞吐率max
	 */
	private Float lteThrputDLMax;
	
	/**
	 * LTE上传吞吐率max
	 */
	private Float lteThrputULMax;
	
	/**
	 * 主叫VONR成功次数
	 */
	private String dialVonrSuccess;
	
	/**
	 * 主叫VONR掉话次数
	 */
	private String dialVonrDrop;
	
	/**
	 * 弱覆盖率
	 */
	private String weakCoverRate;
	
	
	/**
	 * NR切换成功次数
	 */
	private String nrSwitchSuccess;
	
	/**
	 * NR切换失败次数
	 */
	private String nrSwitchDrop;
	
	
	/**
	 * NR连接建立成功次数
	 */
	private String nrConnectSuccess;
	
	/**
	 * NR连接建立成功次数2
	 */
	private Long nrEdsSuccessSecond;
	
	/**
	 * NR连接建立失败次数
	 */
	private String nrConnectDrop;
	
	/**
	 * NR连接建立失败次数2
	 */
	private Long nrEdsFailureSecond;
	
	/**
	 * NR连接建立时延
	 */
	private String nrConnectTimeDelay;
	
	/**
	 * SA制式NR频点
	 */
	private Integer nrArfcnssbSA;
	
	/**
	 * SA制式小区带宽
	 */
	private Integer nrBandwidthSA;
	
	/**
	 * NR PDSCH DMRS RSRP
	 */
	private Float nrPdschDmrsRsrpAvg;
	
	/**
	 * NR PDCCH DMRS SINR
	 */
	private Float nrPdschDmrsSinrAvg;
	
	/**
	 * PDCPNR下载吞吐率
	 */
	private Float nrPdcpThrputDLAvg;
	
	/**
	 * PDCPNR上传吞吐率
	 */
	private Float nrPdcpThrputULAvg;
	
	/**
	 * 上行低速率占比
	 */
	private Float nrMacThrputULvg;
	
	/**
	 * 下行低速率占比
	 */
	private Float nrMacThrputDLvg;
	
	/**
	 * CQI
	 */
	private Float nrCwoWbCqiAbg;
	
	/**
	 * RANK
	 */
	private Float nrRankIndicatAvg;
	
	/**
	 * NR PHY Thr DL(M)
	 */
	private Float nrPhyThrDLAvg;
	
	/**
	 * NR PHY Thr UL(M)
	 */
	private Float nrPhyThrULAvg;
	
	/**
	 * NR RLC Thr DL(M)
	 */
	private Float nrRlcThrDLAvg;
	
	/**
	 * NR RLC Thr UL(M)
	 */
	private Float nrRlcThrULAvg;
	
	/**
	 * NR SDAP Thr DL(M)
	 */
	private Float nrSdapThrDLAvg;
	
	/**
	 * NR SDAP Thr UL(M)
	 */
	private Float nrSdapThrULAvg;
	
	/**
	 * LTE PHY Thr DL(M)
	 */
	private Float ltePhyThrDLAvg;
	
	/**
	 * LTE PHY Thr UL(M)
	 */
	private Float ltePhyThrULAvg;
	
	/**
	 * LTE PDCP Thr DL(M)
	 */
	private Float ltePdcpThrDLAvg;
	
	/**
	 * LTE PDCP Thr UL(M)
	 */
	private Float ltePdcpThrULAvg;
	
	/**
	 * LTE RLC Thr DL(M)
	 */
	private Float lteRlcThrDLAvg;
	
	/**
	 * LTE RLC Thr UL(M)
	 */
	private Float lteRlcThrULAvg;
	
	
	/**
	 * PDCPNR下载吞吐率max
	 */
	private Float nrPdcpThrDLMaxval;
	
	/**
	 * PDCPNR上传吞吐率max
	 */
	private Float nrPdcpThrULMaxval;
	
	/**
	 * NR PHY Thr DL max(M)
	 */
	private Float nrPhyThrDLMaxval;
	
	/**
	 * NR PHY Thr UL max(M)
	 */
	private Float nrPhyThrULMaxval;
	
	/**
	 * NR RLC Thr DL max(M)
	 */
	private Float nrRlcThrDLMaxval;
	
	/**
	 * NR RLC Thr UL max(M)
	 */
	private Float nrRlcThrULMaxval;
	
	/**
	 * NR SDAP Thr DL max(M)
	 */
	private Float nrSdapThrDLMaxval;
	
	/**
	 * NR SDAP Thr UL max（M)
	 */
	private Float nrSdapThrULMaxval;
	
	/**
	 * LTE PDCP Thr DL max(M)
	 */
	private Float ltePdcpThrDLMaxval;
	
	/**
	 * LTE PDCP Thr UL max(M)
	 */
	private Float ltePdcpThrULMaxval;
	
	/**
	 * LTE RLC Thr DL max(M)
	 */
	private Float lteRlcThrDLMaxval;
	
	/**
	 * LTE RLC Thr UL max(M)
	 */
	private Float lteRlcThrULMaxval;
	
	/**
	 * LTE PHY Thr DL max(M)
	 */
	private Float ltePhyThrDLMaxval;
	
	/**
	 * LTE PHY Thr UL max(M)
	 */
	private Float ltePhyThrULMaxval;
	
	
	/**
	 * 上行pdcp低速率占比
	 */
	private Float nrPdcpULlessTenRatio;
	
	/**
	 * 下行pdcp低速率占比
	 */
	private Float nrPdcpDLlessHundredRatio;
	
	/**
	 * 上行phy低速率占比
	 */
	private Float nrPhyULlessTenRatio;
	
	/**
	 * 下行phy低速率占比
	 */
	private Float nrPhyDLlessHundredRatio;
	
	/**
	 * 上行rlc低速率占比
	 */
	private Float nrRlcULlessTenRatio;
	
	/**
	 * 下行rlc低速率占比
	 */
	private Float nrRlcDLlessHundredRatio;
	
	/**
	 * 上行SDAP低速率占比
	 */
	private Float nrSdapULlessTenRatio;
	
	/**
	 * 下行SDAP低速率占比
	 */
	private Float nrSdapDLlessHundredRatio;
	
	/**
	 * 主叫Volte未接通次数
	 */
	private Integer volteCancelNum;
	
	/**
	 * 主叫Volte时延
	 */
	private Float volteDelayTimeAvg;
	
	/**
	 * 主叫VONR未接通次数
	 */
	private Integer vonrCancelNum;
	
	/**
	 * 主叫VoNR时延
	 */
	private Float vonrDelayTimeAvg;
	
	/**
	 * 弱覆盖率2
	 */
	private Float nrSSRsrpSinr100;
	
	/**
	 * 弱覆盖率3
	 */
	private Float nrSSRsrpSinr105;
	
	/**
	 * 弱覆盖率4
	 */
	private Float nrSSRsrpSinr110;
	
	/**
	 * NRGNBID
	 */
	private String nrGnbid;
	
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NR_CELLNAME")
	public String getNrCellname() {
		return nrCellname;
	}
	public void setNrCellname(String nrCellname) {
		this.nrCellname = nrCellname;
	}

	@Column(name = "NR_REGION")
	public String getNrRegion() {
		return nrRegion;
	}
	public void setNrRegion(String nrRegion) {
		this.nrRegion = nrRegion;
	}

	@Column(name = "NR_TESTDATE")
	public String getNrTestdate() {
		return nrTestdate;
	}
	public void setNrTestdate(String nrTestdate) {
		this.nrTestdate = nrTestdate;
	}
	
	@Column(name = "NR_DY")
	public String getNrDy() {
		return nrDy;
	}
	
	public void setNrDy(String nrDy) {
		this.nrDy = nrDy;
	}
	
	
	@Column(name = "NR_WIRELESSSTATION")
	public String getNrWirelessstation() {
		return nrWirelessstation;
	}
	public void setNrWirelessstation(String nrWirelessstation) {
		this.nrWirelessstation = nrWirelessstation;
	}
	
	@Column(name = "NR_TESTEVENT")
	public String getNrTestevent() {
		return nrTestevent;
	}
	public void setNrTestevent(String nrTestevent) {
		this.nrTestevent = nrTestevent;
	}
	
	@Column(name = "NR_RELATELOGNAME")
	public String getNrRelatelogname() {
		return nrRelatelogname;
	}
	public void setNrRelatelogname(String nrRelatelogname) {
		this.nrRelatelogname = nrRelatelogname;
	}
	
	@Column(name = "NR_SAMPLENUMBER")
	public String getNrSamplenumber() {
		return nrSamplenumber;
	}
	public void setNrSamplenumber(String nrSamplenumber) {
		this.nrSamplenumber = nrSamplenumber;
	}
	
	@Column(name = "NR_PLANCELLID")
	public String getNrPlancellid() {
		return nrPlancellid;
	}
	public void setNrPlancellid(String nrPlancellid) {
		this.nrPlancellid = nrPlancellid;
	}
	
	@Column(name = "NR_PLANPCI")
	public String getNrPlanpci() {
		return nrPlanpci;
	}
	public void setNrPlanpci(String nrPlanpci) {
		this.nrPlanpci = nrPlanpci;
	}
	
	@Column(name = "NR_PLANLOCALCELLID")
	public String getNrPlanlocalcellid() {
		return nrPlanlocalcellid;
	}
	public void setNrPlanlocalcellid(String nrPlanlocalcellid) {
		this.nrPlanlocalcellid = nrPlanlocalcellid;
	}
	
	@Column(name = "NR_FREQUERY")
	public String getNrFrequery() {
		return nrFrequery;
	}
	public void setNrFrequery(String nrFrequery) {
		this.nrFrequery = nrFrequery;
	}
	
	@Column(name = "NR_CELLID1")
	public String getNrCellid1() {
		return nrCellid1;
	}
	public void setNrCellid1(String nrCellid1) {
		this.nrCellid1 = nrCellid1;
	}
	
	@Column(name = "NR_CELLID2")
	public String getNrCellid2() {
		return nrCellid2;
	}
	
	public void setNrCellid2(String nrCellid2) {
		this.nrCellid2 = nrCellid2;
	}
	
	@Column(name = "NR_CELLID3")
	public String getNrCellid3() {
		return nrCellid3;
	}
	
	public void setNrCellid3(String nrCellid3) {
		this.nrCellid3 = nrCellid3;
	}
	
	@Column(name = "NR_PCI1")
	public String getNrPci1() {
		return nrPci1;
	}
	public void setNrPci1(String nrPci1) {
		this.nrPci1 = nrPci1;
	}
	
	@Column(name = "NR_PCI2")
	public String getNrPci2() {
		return nrPci2;
	}
	
	public void setNrPci2(String nrPci2) {
		this.nrPci2 = nrPci2;
	}
	@Column(name = "NR_PCI3")
	public String getNrPci3() {
		return nrPci3;
	}
	public void setNrPci3(String nrPci3) {
		this.nrPci3 = nrPci3;
	}
	@Column(name = "NR_POINT")
	public String getNrPoint() {
		return nrPoint;
	}
	public void setNrPoint(String nrPoint) {
		this.nrPoint = nrPoint;
	}
	
	@Column(name = "NR_BANDWIDTH")
	public String getNrBandwidth() {
		return nrBandwidth;
	}
	public void setNrBandwidth(String nrBandwidth) {
		this.nrBandwidth = nrBandwidth;
	}
	
	@Column(name = "NR_SSPRATIO")
	public String getNrSspratio() {
		return nrSspratio;
	}
	public void setNrSspratio(String nrSspratio) {
		this.nrSspratio = nrSspratio;
	}
	
	@Column(name = "NR_SARATIO")
	public String getNrSaratio() {
		return nrSaratio;
	}
	public void setNrSaratio(String nrSaratio) {
		this.nrSaratio = nrSaratio;
	}
	
	@Column(name = "NR_SUBCARRIERSPACING")
	public String getNrSubcarrierspacing() {
		return nrSubcarrierspacing;
	}
	public void setNrSubcarrierspacing(String nrSubcarrierspacing) {
		this.nrSubcarrierspacing = nrSubcarrierspacing;
	}
	
	@Column(name = "NR_TAC")
	public String getNrTac() {
		return nrTac;
	}
	public void setNrTac(String nrTac) {
		this.nrTac = nrTac;
	}
	
	@Column(name = "NR_PUSCHTXPOWER")
	public String getNrPuschtxpower() {
		return nrPuschtxpower;
	}
	public void setNrPuschtxpower(String nrPuschtxpower) {
		this.nrPuschtxpower = nrPuschtxpower;
	}
	
	@Column(name = "LTE_EPSATTACHSUCCESS")
	public String getLteEpsattachsuccess() {
		return lteEpsattachsuccess;
	}
	public void setLteEpsattachsuccess(String lteEpsattachsuccess) {
		this.lteEpsattachsuccess = lteEpsattachsuccess;
	}
	
	@Column(name = "LTE_EPSATTACHFAILURE")
	public String getLteEpsattachfailure() {
		return lteEpsattachfailure;
	}
	public void setLteEpsattachfailure(String lteEpsattachfailure) {
		this.lteEpsattachfailure = lteEpsattachfailure;
	}
	
	@Column(name = "NR_EDSADDSUCCESS")
	public String getNrEdsaddsuccess() {
		return nrEdsaddsuccess;
	}
	public void setNrEdsaddsuccess(String nrEdsaddsuccess) {
		this.nrEdsaddsuccess = nrEdsaddsuccess;
	}
	
	@Column(name = "NR_EDSADDFAILURE")
	public String getNrEdsaddfailure() {
		return nrEdsaddfailure;
	}
	public void setNrEdsaddfailure(String nrEdsaddfailure) {
		this.nrEdsaddfailure = nrEdsaddfailure;
	}
	
	@Column(name = "LTE_INTERFREQHANDOVERSUCCESS")
	public String getLteInterfreqhandoversuccess() {
		return lteInterfreqhandoversuccess;
	}
	public void setLteInterfreqhandoversuccess(String lteInterfreqhandoversuccess) {
		this.lteInterfreqhandoversuccess = lteInterfreqhandoversuccess;
	}
	
	@Column(name = "LTE_INTERFREQHANDOVERFAILURE")
	public String getLteInterfreqhandoverfailure() {
		return lteInterfreqhandoverfailure;
	}
	public void setLteInterfreqhandoverfailure(String lteInterfreqhandoverfailure) {
		this.lteInterfreqhandoverfailure = lteInterfreqhandoverfailure;
	}
	
	@Column(name = "NR_EDSCHANGESUCCESS")
	public String getNrEdschangesuccess() {
		return nrEdschangesuccess;
	}
	public void setNrEdschangesuccess(String nrEdschangesuccess) {
		this.nrEdschangesuccess = nrEdschangesuccess;
	}
	
	@Column(name = "NR_EDSCHANGEFAILURE")
	public String getNrEdschangefailure() {
		return nrEdschangefailure;
	}
	public void setNrEdschangefailure(String nrEdschangefailure) {
		this.nrEdschangefailure = nrEdschangefailure;
	}
	
	@Column(name = "RRC_CONNECTIONSUCCESS")
	public String getRrcConnectionsuccess() {
		return rrcConnectionsuccess;
	}
	public void setRrcConnectionsuccess(String rrcConnectionsuccess) {
		this.rrcConnectionsuccess = rrcConnectionsuccess;
	}
	
	@Column(name = "RRC_CONNECTIONFAILURE")
	public String getRrcConnectionfailure() {
		return rrcConnectionfailure;
	}
	public void setRrcConnectionfailure(String rrcConnectionfailure) {
		this.rrcConnectionfailure = rrcConnectionfailure;
	}
	
	@Column(name = "LTE_EARFCNSUCCESS")
	public String getLteEarfcnsuccess() {
		return lteEarfcnsuccess;
	}
	public void setLteEarfcnsuccess(String lteEarfcnsuccess) {
		this.lteEarfcnsuccess = lteEarfcnsuccess;
	}
	
	@Column(name = "LTE_EARFCNDROP")
	public String getLteEarfcndrop() {
		return lteEarfcndrop;
	}
	public void setLteEarfcndrop(String lteEarfcndrop) {
		this.lteEarfcndrop = lteEarfcndrop;
	}
	
	@Column(name = "NR_EARFCNSUCCESS")
	public String getNrEarfcnsuccess() {
		return nrEarfcnsuccess;
	}
	public void setNrEarfcnsuccess(String nrEarfcnsuccess) {
		this.nrEarfcnsuccess = nrEarfcnsuccess;
	}
	
	@Column(name = "NR_EARFCNDROP")
	public String getNrEarfcndrop() {
		return nrEarfcndrop;
	}
	public void setNrEarfcndrop(String nrEarfcndrop) {
		this.nrEarfcndrop = nrEarfcndrop;
	}
	
	@Column(name = "NR_PINGRESQTIME")
	public String getNrPingresqtime() {
		return nrPingresqtime;
	}
	public void setNrPingresqtime(String nrPingresqtime) {
		this.nrPingresqtime = nrPingresqtime;
	}
	
	@Column(name = "NR_PINGRESPOSE")
	public String getNrPingrespose() {
		return nrPingrespose;
	}
	public void setNrPingrespose(String nrPingrespose) {
		this.nrPingrespose = nrPingrespose;
	}
	
	@Column(name = "NR_PINGFAILURE")
	public String getNrPingfailure() {
		return nrPingfailure;
	}
	public void setNrPingfailure(String nrPingfailure) {
		this.nrPingfailure = nrPingfailure;
	}
	
	@Column(name = "NR_RSRPAVG")
	public String getNrRsrpavg() {
		return nrRsrpavg;
	}
	
	public void setNrRsrpavg(String nrRsrpavg) {
		this.nrRsrpavg = nrRsrpavg;
	}
	
	@Column(name = "NR_SINR")
	public String getNrSinr() {
		return nrSinr;
	}
	
	public void setNrSinr(String nrSinr) {
		this.nrSinr = nrSinr;
	}
	
	@Column(name = "NR_THRPUTDL")
	public String getNrThrputdl() {
		return nrThrputdl;
	}
	
	public void setNrThrputdl(String nrThrputdl) {
		this.nrThrputdl = nrThrputdl;
	}
	
	@Column(name = "NR_THRPUTUL")
	public String getNrThrputul() {
		return nrThrputul;
	}
	
	public void setNrThrputul(String nrThrputul) {
		this.nrThrputul = nrThrputul;
	}
	
	@Column(name = "LTE_FREQUERY")
	public String getLteFrequery() {
		return lteFrequery;
	}
	
	public void setLteFrequery(String lteFrequery) {
		this.lteFrequery = lteFrequery;
	}

	@Column(name = "LTE_CELLID1")
	public String getLteCellid1() {
		return lteCellid1;
	}
	
	public void setLteCellid1(String lteCellid1) {
		this.lteCellid1 = lteCellid1;
	}

	@Column(name = "LTE_CELLID2")
	public String getLteCellid2() {
		return lteCellid2;
	}
	
	public void setLteCellid2(String lteCellid2) {
		this.lteCellid2 = lteCellid2;
	}

	@Column(name = "LTE_CELLID3")
	public String getLteCellid3() {
		return lteCellid3;
	}
	
	public void setLteCellid3(String lteCellid3) {
		this.lteCellid3 = lteCellid3;
	}

	@Column(name = "LTE_PCI1")
	public String getLtePci1() {
		return ltePci1;
	}
	
	public void setLtePci1(String ltePci1) {
		this.ltePci1 = ltePci1;
	}

	@Column(name = "LTE_PCI2")
	public String getLtePci2() {
		return ltePci2;
	}
	
	public void setLtePci2(String ltePci2) {
		this.ltePci2 = ltePci2;
	}

	@Column(name = "LTE_PCI3")
	public String getLtePci3() {
		return ltePci3;
	}
	
	public void setLtePci3(String ltePci3) {
		this.ltePci3 = ltePci3;
	}

	@Column(name = "LTE_POINT")
	public String getLtePoint() {
		return ltePoint;
	}
	
	public void setLtePoint(String ltePoint) {
		this.ltePoint = ltePoint;
	}

	@Column(name = "LTE_BANDWIDTH")
	public String getLteBandwidth() {
		return lteBandwidth;
	}
	
	public void setLteBandwidth(String lteBandwidth) {
		this.lteBandwidth = lteBandwidth;
	}

	@Column(name = "LTE_SPECIALSUBFRAMEPATTERNS")
	public String getLteSspratio() {
		return lteSspratio;
	}
	
	public void setLteSspratio(String lteSspratio) {
		this.lteSspratio = lteSspratio;
	}

	@Column(name = "LTE_SUBFRAMEASSIGN")
	public String getLteSpratio() {
		return lteSpratio;
	}
	
	public void setLteSpratio(String lteSpratio) {
		this.lteSpratio = lteSpratio;
	}

	@Column(name = "LTE_RS_EPRE")
	public String getRsErpe() {
		return rsErpe;
	}
	
	public void setRsErpe(String rsErpe) {
		this.rsErpe = rsErpe;
	}

	@Column(name = "LTE_P_A")
	public String getpA() {
		return pA;
	}
	
	public void setpA(String pA) {
		this.pA = pA;
	}

	@Column(name = "LTE_P_B")
	public String getpB() {
		return pB;
	}
	
	public void setpB(String pB) {
		this.pB = pB;
	}

	@Column(name = "TOCHANGELOCALCELL1SUCCESS")
	public String getToStation1Change() {
		return toStation1Change;
	}
	
	public void setToStation1Change(String toStation1Change) {
		this.toStation1Change = toStation1Change;
	}

	@Column(name = "TOCHANGELOCALCELL2SUCCESS")
	public String getToStation2Change() {
		return toStation2Change;
	}
	
	public void setToStation2Change(String toStation2Change) {
		this.toStation2Change = toStation2Change;
	}

	@Column(name = "TOCHANGELOCALCELL3SUCCESS")
	public String getToStation3Change() {
		return toStation3Change;
	}
	
	public void setToStation3Change(String toStation3Change) {
		this.toStation3Change = toStation3Change;
	}

	@Column(name = "LTE_RSRPAVG")
	public String getLteRsrp() {
		return lteRsrp;
	}
	
	public void setLteRsrp(String lteRsrp) {
		this.lteRsrp = lteRsrp;
	}

	@Column(name = "LTE_SINR")
	public String getLteSinr() {
		return lteSinr;
	}
	
	public void setLteSinr(String lteSinr) {
		this.lteSinr = lteSinr;
	}
	
	@Column(name = "LTE_THRPUTDL")
	public String getLteThrputdl() {
		return lteThrputdl;
	}
	
	public void setLteThrputdl(String lteThrputdl) {
		this.lteThrputdl = lteThrputdl;
	}
	
	@Column(name = "LTE_THRPUTUL")
	public String getLteThrputul() {
		return lteThrputul;
	}
	
	public void setLteThrputul(String lteThrputul) {
		this.lteThrputul = lteThrputul;
	}
	
	@Column(name = "LTE_TAC")
	public String getLteTac() {
		return lteTac;
	}
	
	public void setLteTac(String lteTac) {
		this.lteTac = lteTac;
	}

	@Column(name = "NR_PREAMBLEFORMAT")
	public String getNrPreambleFormat() {
		return nrPreambleFormat;
	}
	public void setNrPreambleFormat(String nrPreambleFormat) {
		this.nrPreambleFormat = nrPreambleFormat;
	}

	@Column(name = "NR_RSPOWER")
	public String getRsPower() {
		return rsPower;
	}
	public void setRsPower(String rsPower) {
		this.rsPower = rsPower;
	}

	@Column(name = "PDCCH_SIGNAL")
	public String getPdcch() {
		return pdcch;
	}
	public void setPdcch(String pdcch) {
		this.pdcch = pdcch;
	}

	@Column(name = "RRC_CONNECTIONSETUPCOMPLETE")
	public String getRrcConnectionSetupComplete() {
		return rrcConnectionSetupComplete;
	}
	public void setRrcConnectionSetupComplete(String rrcConnectionSetupComplete) {
		this.rrcConnectionSetupComplete = rrcConnectionSetupComplete;
	}

	@Column(name = "ERAB_CONNECTIONSETUPSUCCESS")
	public String getErabConnectionSetupSuccess() {
		return erabConnectionSetupSuccess;
	}
	public void setErabConnectionSetupSuccess(String erabConnectionSetupSuccess) {
		this.erabConnectionSetupSuccess = erabConnectionSetupSuccess;
	}

	@Column(name = "VOLTE_SUCCESS")
	public String getVolteSuccess() {
		return volteSuccess;
	}
	public void setVolteSuccess(String volteSuccess) {
		this.volteSuccess = volteSuccess;
	}

	@Column(name = "VOLTE_DROP")
	public String getVolteDrop() {
		return volteDrop;
	}
	public void setVolteDrop(String volteDrop) {
		this.volteDrop = volteDrop;
	}

	@Column(name = "CSFB_SUCCESS")
	public String getCsfbSuccess() {
		return csfbSuccess;
	}
	public void setCsfbSuccess(String csfbSuccess) {
		this.csfbSuccess = csfbSuccess;
	}

	@Column(name = "CSFB_REQUESTTIME")
	public String getCsfbRequesttime() {
		return csfbRequesttime;
	}
	public void setCsfbRequesttime(String csfbRequesttime) {
		this.csfbRequesttime = csfbRequesttime;
	}

	@Column(name = "NR_TRX")
	public String getNrTrx() {
		return nrTrx;
	}
	
	public void setNrTrx(String nrTrx) {
		this.nrTrx = nrTrx;
	}
	
	@Column(name = "NR_FRAMESTRUCTURE")
	public String getNrFrameStructure() {
		return nrFrameStructure;
	}
	
	public void setNrFrameStructure(String nrFrameStructure) {
		this.nrFrameStructure = nrFrameStructure;
	}
	
	@Column(name = "NR_THRPUTDLMAX")
	public Float getNrThrputDLMax() {
		return nrThrputDLMax;
	}
	
	public void setNrThrputDLMax(Float nrThrputDLMax) {
		this.nrThrputDLMax = nrThrputDLMax;
	}
	
	@Column(name = "NR_THRPUTULMAX")
	public Float getNrThrputULMax() {
		return nrThrputULMax;
	}
	
	public void setNrThrputULMax(Float nrThrputULMax) {
		this.nrThrputULMax = nrThrputULMax;
	}
	
	@Column(name = "LTE_THRPUTDLMAX")
	public Float getLteThrputDLMax() {
		return lteThrputDLMax;
	}
	
	public void setLteThrputDLMax(Float lteThrputDLMax) {
		this.lteThrputDLMax = lteThrputDLMax;
	}
	
	@Column(name = "LTE_THRPUTULMAX")
	public Float getLteThrputULMax() {
		return lteThrputULMax;
	}
	
	public void setLteThrputULMax(Float lteThrputULMax) {
		this.lteThrputULMax = lteThrputULMax;
	}
	
	/**
	 * @return the dialVonrSuccess
	 */
	@Column(name = "DIAL_VONR_SUCCESS")
	public String getDialVonrSuccess() {
		return dialVonrSuccess;
	}
	
	/**
	 * @param dialVonrSuccess the dialVonrSuccess to set
	 */
	public void setDialVonrSuccess(String dialVonrSuccess) {
		this.dialVonrSuccess = dialVonrSuccess;
	}
	
	/**
	 * @return the dialVonrDrop
	 */
	@Column(name = "DIAL_VONR_DROP")
	public String getDialVonrDrop() {
		return dialVonrDrop;
	}
	
	/**
	 * @param dialVonrDrop the dialVonrDrop to set
	 */
	public void setDialVonrDrop(String dialVonrDrop) {
		this.dialVonrDrop = dialVonrDrop;
	}
	
	/**
	 * @return the weakCoverRate
	 */
	@Column(name = "WEAK_COVER_RATE")
	public String getWeakCoverRate() {
		return weakCoverRate;
	}
	
	/**
	 * @param weakCoverRate the weakCoverRate to set
	 */
	public void setWeakCoverRate(String weakCoverRate) {
		this.weakCoverRate = weakCoverRate;
	}
	
	/**
	 * @return the nrSwitchSuccess
	 */
	@Column(name = "NR_SWITCH_SUCCESS")
	public String getNrSwitchSuccess() {
		return nrSwitchSuccess;
	}
	
	/**
	 * @param nrSwitchSuccess the nrSwitchSuccess to set
	 */
	public void setNrSwitchSuccess(String nrSwitchSuccess) {
		this.nrSwitchSuccess = nrSwitchSuccess;
	}
	
	/**
	 * @return the nrSwitchDrop
	 */
	@Column(name = "NR_SWITCH_DROP")
	public String getNrSwitchDrop() {
		return nrSwitchDrop;
	}
	
	/**
	 * @param nrSwitchDrop the nrSwitchDrop to set
	 */
	public void setNrSwitchDrop(String nrSwitchDrop) {
		this.nrSwitchDrop = nrSwitchDrop;
	}
	
	/**
	 * @return the nrConnectSuccess
	 */
	@Column(name = "NR_CONNECT_SUCCESS")
	public String getNrConnectSuccess() {
		return nrConnectSuccess;
	}
	
	/**
	 * @param nrConnectSuccess the nrConnectSuccess to set
	 */
	public void setNrConnectSuccess(String nrConnectSuccess) {
		this.nrConnectSuccess = nrConnectSuccess;
	}
	
	/**
	 * @return the nrConnectDrop
	 */
	@Column(name = "NR_CONNECT_DROP")
	public String getNrConnectDrop() {
		return nrConnectDrop;
	}
	
	/**
	 * @param nrConnectDrop the nrConnectDrop to set
	 */
	public void setNrConnectDrop(String nrConnectDrop) {
		this.nrConnectDrop = nrConnectDrop;
	}
	
	/**
	 * @return the nrConnectTimeDelay
	 */
	@Column(name = "NR_CONNECT_TIMEDELAY")
	public String getNrConnectTimeDelay() {
		return nrConnectTimeDelay;
	}
	
	/**
	 * @param nrConnectTimeDelay the nrConnectTimeDelay to set
	 */
	public void setNrConnectTimeDelay(String nrConnectTimeDelay) {
		this.nrConnectTimeDelay = nrConnectTimeDelay;
	}
	
	/**
	 * @return the nrArfcnssbSA
	 */
	@Column(name = "NR_ARFCNSSB_SA")
	public Integer getNrArfcnssbSA() {
		return nrArfcnssbSA;
	}
	
	/**
	 * @param nrArfcnssbSA the nrArfcnssbSA to set
	 */
	public void setNrArfcnssbSA(Integer nrArfcnssbSA) {
		this.nrArfcnssbSA = nrArfcnssbSA;
	}
	
	/**
	 * @return the nrBandwidthSA
	 */
	@Column(name = "NR_BANDWIDTH_SA")
	public Integer getNrBandwidthSA() {
		return nrBandwidthSA;
	}
	
	/**
	 * @param nrBandwidthSA the nrBandwidthSA to set
	 */
	public void setNrBandwidthSA(Integer nrBandwidthSA) {
		this.nrBandwidthSA = nrBandwidthSA;
	}
	
	/**
	 * @return the nrPdschDmrsRsrp
	 */
	@Column(name = "NR_PDSCH_DMRS_RSRPAVG")
	public Float getNrPdschDmrsRsrpAvg() {
		return nrPdschDmrsRsrpAvg;
	}
	
	/**
	 * @param nrPdschDmrsRsrp the nrPdschDmrsRsrp to set
	 */
	public void setNrPdschDmrsRsrpAvg(Float nrPdschDmrsRsrpAvg) {
		this.nrPdschDmrsRsrpAvg = nrPdschDmrsRsrpAvg;
	}
	
	/**
	 * @return the nrPdschDmrsSinr
	 */
	@Column(name = "NR_PDCCH_DMRS_SINRAVG")
	public Float getNrPdschDmrsSinrAvg() {
		return nrPdschDmrsSinrAvg;
	}
	
	/**
	 * @param nrPdschDmrsSinr the nrPdschDmrsSinr to set
	 */
	public void setNrPdschDmrsSinrAvg(Float nrPdschDmrsSinrAvg) {
		this.nrPdschDmrsSinrAvg = nrPdschDmrsSinrAvg;
	}
	
	/**
	 * @return the nrPdcpThrputDL
	 */
	@Column(name = "NR_PDCP_THRPUT_DLAVG")
	public Float getNrPdcpThrputDLAvg() {
		return nrPdcpThrputDLAvg;
	}
	
	/**
	 * @param nrPdcpThrputDL the nrPdcpThrputDL to set
	 */
	public void setNrPdcpThrputDLAvg(Float nrPdcpThrputDLAvg) {
		this.nrPdcpThrputDLAvg = nrPdcpThrputDLAvg;
	}
	
	/**
	 * @return the nrPdcpThrputUL
	 */
	@Column(name = "NR_PDCP_THRPUT_ULAVG")
	public Float getNrPdcpThrputULAvg() {
		return nrPdcpThrputULAvg;
	}
	
	/**
	 * @param nrPdcpThrputUL the nrPdcpThrputUL to set
	 */
	public void setNrPdcpThrputULAvg(Float nrPdcpThrputULAvg) {
		this.nrPdcpThrputULAvg = nrPdcpThrputULAvg;
	}
	
	/**
	 * @return the nrEdsSuccessSecond
	 */
	@Column(name = "NR_EDS_SUCCESS_SECOND")
	public Long getNrEdsSuccessSecond() {
		return nrEdsSuccessSecond;
	}
	
	/**
	 * @param nrEdsSuccessSecond the nrEdsSuccessSecond to set
	 */
	public void setNrEdsSuccessSecond(Long nrEdsSuccessSecond) {
		this.nrEdsSuccessSecond = nrEdsSuccessSecond;
	}
	
	/**
	 * @return the nrEdsFailureSecond
	 */
	@Column(name = "NR_EDS_FAILURE_SECOND")
	public Long getNrEdsFailureSecond() {
		return nrEdsFailureSecond;
	}
	
	/**
	 * @param nrEdsFailureSecond the nrEdsFailureSecond to set
	 */
	public void setNrEdsFailureSecond(Long nrEdsFailureSecond) {
		this.nrEdsFailureSecond = nrEdsFailureSecond;
	}
	
	/**
	 * @return the nrMacThrputULvg
	 */
	@Column(name = "NR_MAC_THRPUT_ULAVG")
	public Float getNrMacThrputULvg() {
		return nrMacThrputULvg;
	}
	
	/**
	 * @param nrMacThrputULvg the nrMacThrputULvg to set
	 */
	public void setNrMacThrputULvg(Float nrMacThrputULvg) {
		this.nrMacThrputULvg = nrMacThrputULvg;
	}
	
	/**
	 * @return the nrMacThrputDLvg
	 */
	@Column(name = "NR_MAC_THRPUT_DLAVG")
	public Float getNrMacThrputDLvg() {
		return nrMacThrputDLvg;
	}
	
	/**
	 * @param nrMacThrputDLvg the nrMacThrputDLvg to set
	 */
	public void setNrMacThrputDLvg(Float nrMacThrputDLvg) {
		this.nrMacThrputDLvg = nrMacThrputDLvg;
	}
	
	/**
	 * @return the nrCwoWbCqiAbg
	 */
	@Column(name = "NR_CWO_WB_CQIAVG")
	public Float getNrCwoWbCqiAbg() {
		return nrCwoWbCqiAbg;
	}
	
	/**
	 * @param nrCwoWbCqiAbg the nrCwoWbCqiAbg to set
	 */
	public void setNrCwoWbCqiAbg(Float nrCwoWbCqiAbg) {
		this.nrCwoWbCqiAbg = nrCwoWbCqiAbg;
	}
	
	/**
	 * @return the nrRankIndicatAvg
	 */
	@Column(name = "NR_RANK_INDICATAVG")
	public Float getNrRankIndicatAvg() {
		return nrRankIndicatAvg;
	}
	
	/**
	 * @param nrRankIndicatAvg the nrRankIndicatAvg to set
	 */
	public void setNrRankIndicatAvg(Float nrRankIndicatAvg) {
		this.nrRankIndicatAvg = nrRankIndicatAvg;
	}
	
	/**
	 * @return the nrPhyThrDLAvg
	 */
	@Column(name = "NR_PHY_THR_DL_AVG")
	public Float getNrPhyThrDLAvg() {
		return nrPhyThrDLAvg;
	}
	
	/**
	 * @param nrPhyThrDLAvg the nrPhyThrDLAvg to set
	 */
	public void setNrPhyThrDLAvg(Float nrPhyThrDLAvg) {
		this.nrPhyThrDLAvg = nrPhyThrDLAvg;
	}
	
	/**
	 * @return the nrPhyThrULAvg
	 */
	@Column(name = "NR_PHY_THR_UL_AVG")
	public Float getNrPhyThrULAvg() {
		return nrPhyThrULAvg;
	}
	
	/**
	 * @param nrPhyThrULAvg the nrPhyThrULAvg to set
	 */
	public void setNrPhyThrULAvg(Float nrPhyThrULAvg) {
		this.nrPhyThrULAvg = nrPhyThrULAvg;
	}
	
	/**
	 * @return the nrRlcThrDLAvg
	 */
	@Column(name = "NR_RLC_THR_DL_AVG")
	public Float getNrRlcThrDLAvg() {
		return nrRlcThrDLAvg;
	}
	
	/**
	 * @param nrRlcThrDLAvg the nrRlcThrDLAvg to set
	 */
	public void setNrRlcThrDLAvg(Float nrRlcThrDLAvg) {
		this.nrRlcThrDLAvg = nrRlcThrDLAvg;
	}
	
	/**
	 * @return the nrRlcThrULAvg
	 */
	@Column(name = "NR_RLC_THR_UL_AVG")
	public Float getNrRlcThrULAvg() {
		return nrRlcThrULAvg;
	}
	
	/**
	 * @param nrRlcThrULAvg the nrRlcThrULAvg to set
	 */
	public void setNrRlcThrULAvg(Float nrRlcThrULAvg) {
		this.nrRlcThrULAvg = nrRlcThrULAvg;
	}
	
	/**
	 * @return the nrSdapThrDLAvg
	 */
	@Column(name = "NR_SDAP_THR_DL_AVG")
	public Float getNrSdapThrDLAvg() {
		return nrSdapThrDLAvg;
	}
	
	/**
	 * @param nrSdapThrDLAvg the nrSdapThrDLAvg to set
	 */
	public void setNrSdapThrDLAvg(Float nrSdapThrDLAvg) {
		this.nrSdapThrDLAvg = nrSdapThrDLAvg;
	}
	
	/**
	 * @return the nrSdcThrULAvg
	 */
	@Column(name = "NR_SDAP_THR_UL_AVG")
	public Float getNrSdapThrULAvg() {
		return nrSdapThrULAvg;
	}
	
	/**
	 * @param nrSdcThrULAvg the nrSdcThrULAvg to set
	 */
	public void setNrSdapThrULAvg(Float nrSdapThrULAvg) {
		this.nrSdapThrULAvg = nrSdapThrULAvg;
	}
	
	/**
	 * @return the ltePhyThrDLAvg
	 */
	@Column(name = "LTE_PHY_THR_DL_AVG")
	public Float getLtePhyThrDLAvg() {
		return ltePhyThrDLAvg;
	}
	
	/**
	 * @param ltePhyThrDLAvg the ltePhyThrDLAvg to set
	 */
	public void setLtePhyThrDLAvg(Float ltePhyThrDLAvg) {
		this.ltePhyThrDLAvg = ltePhyThrDLAvg;
	}
	
	/**
	 * @return the ltePhyThrULAvg
	 */
	@Column(name = "LTE_PHY_THR_UL_AVG")
	public Float getLtePhyThrULAvg() {
		return ltePhyThrULAvg;
	}
	
	/**
	 * @param ltePhyThrULAvg the ltePhyThrULAvg to set
	 */
	public void setLtePhyThrULAvg(Float ltePhyThrULAvg) {
		this.ltePhyThrULAvg = ltePhyThrULAvg;
	}
	
	/**
	 * @return the ltePdcpThrDLAvg
	 */
	@Column(name = "LTE_PDCP_THR_DL_AVG")
	public Float getLtePdcpThrDLAvg() {
		return ltePdcpThrDLAvg;
	}
	
	/**
	 * @param ltePdcpThrDLAvg the ltePdcpThrDLAvg to set
	 */
	public void setLtePdcpThrDLAvg(Float ltePdcpThrDLAvg) {
		this.ltePdcpThrDLAvg = ltePdcpThrDLAvg;
	}
	
	/**
	 * @return the ltePdcpThrULAvg
	 */
	@Column(name = "LTE_PDCP_THR_UL_AVG")
	public Float getLtePdcpThrULAvg() {
		return ltePdcpThrULAvg;
	}
	
	/**
	 * @param ltePdcpThrULAvg the ltePdcpThrULAvg to set
	 */
	public void setLtePdcpThrULAvg(Float ltePdcpThrULAvg) {
		this.ltePdcpThrULAvg = ltePdcpThrULAvg;
	}
	
	/**
	 * @return the lteRlcThrDLAvg
	 */
	@Column(name = "LTE_RLC_THR_DL_AVG")
	public Float getLteRlcThrDLAvg() {
		return lteRlcThrDLAvg;
	}
	
	/**
	 * @param lteRlcThrDLAvg the lteRlcThrDLAvg to set
	 */
	public void setLteRlcThrDLAvg(Float lteRlcThrDLAvg) {
		this.lteRlcThrDLAvg = lteRlcThrDLAvg;
	}
	
	/**
	 * @return the lteRlcThrULAvg
	 */
	@Column(name = "LTE_RLC_THR_UL_AVG")
	public Float getLteRlcThrULAvg() {
		return lteRlcThrULAvg;
	}
	
	/**
	 * @param lteRlcThrULAvg the lteRlcThrULAvg to set
	 */
	public void setLteRlcThrULAvg(Float lteRlcThrULAvg) {
		this.lteRlcThrULAvg = lteRlcThrULAvg;
	}
	
	/**
	 * @return the nrPdcpThrDLMaxval
	 */
	@Column(name = "NR_PDCP_THR_DL_MAXVAL")
	public Float getNrPdcpThrDLMaxval() {
		return nrPdcpThrDLMaxval;
	}
	
	/**
	 * @param nrPdcpThrDLMaxval the nrPdcpThrDLMaxval to set
	 */
	public void setNrPdcpThrDLMaxval(Float nrPdcpThrDLMaxval) {
		this.nrPdcpThrDLMaxval = nrPdcpThrDLMaxval;
	}
	
	/**
	 * @return the nrPdcpThrULMaxval
	 */
	@Column(name = "NR_PDCP_THR_UL_MAXVAL")
	public Float getNrPdcpThrULMaxval() {
		return nrPdcpThrULMaxval;
	}
	
	/**
	 * @param nrPdcpThrULMaxval the nrPdcpThrULMaxval to set
	 */
	public void setNrPdcpThrULMaxval(Float nrPdcpThrULMaxval) {
		this.nrPdcpThrULMaxval = nrPdcpThrULMaxval;
	}
	
	/**
	 * @return the nrPhyThrDLMaxval
	 */
	@Column(name = "NR_PHY_THR_DL_MAXVAL")
	public Float getNrPhyThrDLMaxval() {
		return nrPhyThrDLMaxval;
	}
	
	/**
	 * @param nrPhyThrDLMaxval the nrPhyThrDLMaxval to set
	 */
	public void setNrPhyThrDLMaxval(Float nrPhyThrDLMaxval) {
		this.nrPhyThrDLMaxval = nrPhyThrDLMaxval;
	}
	
	/**
	 * @return the nrPhyThrULMaxval
	 */
	@Column(name = "NR_PHY_THR_UL_MAXVAL")
	public Float getNrPhyThrULMaxval() {
		return nrPhyThrULMaxval;
	}
	
	/**
	 * @param nrPhyThrULMaxval the nrPhyThrULMaxval to set
	 */
	public void setNrPhyThrULMaxval(Float nrPhyThrULMaxval) {
		this.nrPhyThrULMaxval = nrPhyThrULMaxval;
	}
	
	/**
	 * @return the nrRlcThrDLMaxval
	 */
	@Column(name = "NR_RLC_THR_DL_MAXVAL")
	public Float getNrRlcThrDLMaxval() {
		return nrRlcThrDLMaxval;
	}
	
	/**
	 * @param nrRlcThrDLMaxval the nrRlcThrDLMaxval to set
	 */
	public void setNrRlcThrDLMaxval(Float nrRlcThrDLMaxval) {
		this.nrRlcThrDLMaxval = nrRlcThrDLMaxval;
	}
	
	/**
	 * @return the nrRlcThrULMaxval
	 */
	@Column(name = "NR_RLC_THR_UL_MAXVAL")
	public Float getNrRlcThrULMaxval() {
		return nrRlcThrULMaxval;
	}
	
	/**
	 * @param nrRlcThrULMaxval the nrRlcThrULMaxval to set
	 */
	public void setNrRlcThrULMaxval(Float nrRlcThrULMaxval) {
		this.nrRlcThrULMaxval = nrRlcThrULMaxval;
	}
	
	/**
	 * @return the nrSdapThrDLMaxval
	 */
	@Column(name = "NR_SDAP_THR_DL_MAXVAL")
	public Float getNrSdapThrDLMaxval() {
		return nrSdapThrDLMaxval;
	}
	
	/**
	 * @param nrSdapThrDLMaxval the nrSdapThrDLMaxval to set
	 */
	public void setNrSdapThrDLMaxval(Float nrSdapThrDLMaxval) {
		this.nrSdapThrDLMaxval = nrSdapThrDLMaxval;
	}
	
	/**
	 * @return the nrSdapThrULMaxval
	 */
	@Column(name = "NR_SDAP_THR_UL_MAXVAL")
	public Float getNrSdapThrULMaxval() {
		return nrSdapThrULMaxval;
	}
	
	/**
	 * @param nrSdapThrULMaxval the nrSdapThrULMaxval to set
	 */
	public void setNrSdapThrULMaxval(Float nrSdapThrULMaxval) {
		this.nrSdapThrULMaxval = nrSdapThrULMaxval;
	}
	
	/**
	 * @return the ltePdcpThrDLMaxval
	 */
	@Column(name = "LTE_PDCP_THR_DL_MAXVAL")
	public Float getLtePdcpThrDLMaxval() {
		return ltePdcpThrDLMaxval;
	}
	
	/**
	 * @param ltePdcpThrDLMaxval the ltePdcpThrDLMaxval to set
	 */
	public void setLtePdcpThrDLMaxval(Float ltePdcpThrDLMaxval) {
		this.ltePdcpThrDLMaxval = ltePdcpThrDLMaxval;
	}
	
	/**
	 * @return the ltePdcpThrULMaxval
	 */
	@Column(name = "LTE_PDCP_THR_UL_MAXVAL")
	public Float getLtePdcpThrULMaxval() {
		return ltePdcpThrULMaxval;
	}
	
	/**
	 * @param ltePdcpThrULMaxval the ltePdcpThrULMaxval to set
	 */
	public void setLtePdcpThrULMaxval(Float ltePdcpThrULMaxval) {
		this.ltePdcpThrULMaxval = ltePdcpThrULMaxval;
	}
	
	/**
	 * @return the lteRlcThrDLMaxval
	 */
	@Column(name = "LTE_RLC_THR_DL_MAXVAL")
	public Float getLteRlcThrDLMaxval() {
		return lteRlcThrDLMaxval;
	}
	
	/**
	 * @param lteRlcThrDLMaxval the lteRlcThrDLMaxval to set
	 */
	public void setLteRlcThrDLMaxval(Float lteRlcThrDLMaxval) {
		this.lteRlcThrDLMaxval = lteRlcThrDLMaxval;
	}
	
	/**
	 * @return the lteRlcThrULMaxval
	 */
	@Column(name = "LTE_RLC_THR_UL_MAXVAL")
	public Float getLteRlcThrULMaxval() {
		return lteRlcThrULMaxval;
	}
	
	/**
	 * @param lteRlcThrULMaxval the lteRlcThrULMaxval to set
	 */
	public void setLteRlcThrULMaxval(Float lteRlcThrULMaxval) {
		this.lteRlcThrULMaxval = lteRlcThrULMaxval;
	}
	
	/**
	 * @return the ltePhyThrDLMaxval
	 */
	@Column(name = "LTE_PHY_THR_DL_MAXVAL")
	public Float getLtePhyThrDLMaxval() {
		return ltePhyThrDLMaxval;
	}
	
	/**
	 * @param ltePhyThrDLMaxval the ltePhyThrDLMaxval to set
	 */
	public void setLtePhyThrDLMaxval(Float ltePhyThrDLMaxval) {
		this.ltePhyThrDLMaxval = ltePhyThrDLMaxval;
	}
	
	/**
	 * @return the ltePhyThrULMaxval
	 */
	@Column(name = "LTE_PHY_THR_UL_MAXVAL")
	public Float getLtePhyThrULMaxval() {
		return ltePhyThrULMaxval;
	}
	
	/**
	 * @param ltePhyThrULMaxval the ltePhyThrULMaxval to set
	 */
	public void setLtePhyThrULMaxval(Float ltePhyThrULMaxval) {
		this.ltePhyThrULMaxval = ltePhyThrULMaxval;
	}
	
	/**
	 * @return the nrPdcpULlessTenRatio
	 */
	@Column(name = "NR_PDCP_UL_LESS_TEN_RATIO")
	public Float getNrPdcpULlessTenRatio() {
		return nrPdcpULlessTenRatio;
	}
	
	/**
	 * @param nrPdcpULlessTenRatio the nrPdcpULlessTenRatio to set
	 */
	public void setNrPdcpULlessTenRatio(Float nrPdcpULlessTenRatio) {
		this.nrPdcpULlessTenRatio = nrPdcpULlessTenRatio;
	}
	
	/**
	 * @return the nrPdcpDLlessHundredRatio
	 */
	@Column(name = "NR_PDCP_DL_LESS_HUNDRED_RATIO")
	public Float getNrPdcpDLlessHundredRatio() {
		return nrPdcpDLlessHundredRatio;
	}
	
	/**
	 * @param nrPdcpDLlessHundredRatio the nrPdcpDLlessHundredRatio to set
	 */
	public void setNrPdcpDLlessHundredRatio(Float nrPdcpDLlessHundredRatio) {
		this.nrPdcpDLlessHundredRatio = nrPdcpDLlessHundredRatio;
	}
	
	/**
	 * @return the nrPhyULlessTenRatio
	 */
	@Column(name = "NR_PHY_UL_LESS_TEN_RATIO")
	public Float getNrPhyULlessTenRatio() {
		return nrPhyULlessTenRatio;
	}
	
	/**
	 * @param nrPhyULlessTenRatio the nrPhyULlessTenRatio to set
	 */
	public void setNrPhyULlessTenRatio(Float nrPhyULlessTenRatio) {
		this.nrPhyULlessTenRatio = nrPhyULlessTenRatio;
	}
	
	/**
	 * @return the nrPhyDLlessHundredRatio
	 */
	@Column(name = "NR_PHY_DL_LESS_HUNDRED_RATIO")
	public Float getNrPhyDLlessHundredRatio() {
		return nrPhyDLlessHundredRatio;
	}
	
	/**
	 * @param nrPhyDLlessHundredRatio the nrPhyDLlessHundredRatio to set
	 */
	public void setNrPhyDLlessHundredRatio(Float nrPhyDLlessHundredRatio) {
		this.nrPhyDLlessHundredRatio = nrPhyDLlessHundredRatio;
	}
	
	/**
	 * @return the nrRlcULlessTenRatio
	 */
	@Column(name = "NR_RLC_UL_LESS_TEN_RATIO")
	public Float getNrRlcULlessTenRatio() {
		return nrRlcULlessTenRatio;
	}
	
	/**
	 * @param nrRlcULlessTenRatio the nrRlcULlessTenRatio to set
	 */
	public void setNrRlcULlessTenRatio(Float nrRlcULlessTenRatio) {
		this.nrRlcULlessTenRatio = nrRlcULlessTenRatio;
	}
	
	/**
	 * @return the nrRlcDLlessHundredRatio
	 */
	@Column(name = "NR_RLC_DL_LESS_HUNDRED_RATIO")
	public Float getNrRlcDLlessHundredRatio() {
		return nrRlcDLlessHundredRatio;
	}
	
	/**
	 * @param nrRlcDLlessHundredRatio the nrRlcDLlessHundredRatio to set
	 */
	public void setNrRlcDLlessHundredRatio(Float nrRlcDLlessHundredRatio) {
		this.nrRlcDLlessHundredRatio = nrRlcDLlessHundredRatio;
	}
	
	/**
	 * @return the nrSdapULlessTenRatio
	 */
	@Column(name = "NR_SDAP_UL_LESS_TEN_RATIO")
	public Float getNrSdapULlessTenRatio() {
		return nrSdapULlessTenRatio;
	}
	
	/**
	 * @param nrSdapULlessTenRatio the nrSdapULlessTenRatio to set
	 */
	public void setNrSdapULlessTenRatio(Float nrSdapULlessTenRatio) {
		this.nrSdapULlessTenRatio = nrSdapULlessTenRatio;
	}
	
	/**
	 * @return the nrSdapDLlessHundredRatio
	 */
	@Column(name = "NR_SDAP_DL_LESS_HUNDRED_RATIO")
	public Float getNrSdapDLlessHundredRatio() {
		return nrSdapDLlessHundredRatio;
	}
	
	/**
	 * @param nrSdapDLlessHundredRatio the nrSdapDLlessHundredRatio to set
	 */
	public void setNrSdapDLlessHundredRatio(Float nrSdapDLlessHundredRatio) {
		this.nrSdapDLlessHundredRatio = nrSdapDLlessHundredRatio;
	}
	
	/**
	 * @return the volteCancelNum
	 */
	@Column(name = "VOLTE_CANCEL_NUM")
	public Integer getVolteCancelNum() {
		return volteCancelNum;
	}
	
	/**
	 * @param volteCancelNum the volteCancelNum to set
	 */
	public void setVolteCancelNum(Integer volteCancelNum) {
		this.volteCancelNum = volteCancelNum;
	}
	
	/**
	 * @return the volteDelayTimeAvg
	 */
	@Column(name = "VOLTE_DELAYTIME_AVG")
	public Float getVolteDelayTimeAvg() {
		return volteDelayTimeAvg;
	}
	
	/**
	 * @param volteDelayTimeAvg the volteDelayTimeAvg to set
	 */
	public void setVolteDelayTimeAvg(Float volteDelayTimeAvg) {
		this.volteDelayTimeAvg = volteDelayTimeAvg;
	}
	
	/**
	 * @return the vonrCancelNum
	 */
	@Column(name = "VONR_CANCEL_NUM")
	public Integer getVonrCancelNum() {
		return vonrCancelNum;
	}
	
	/**
	 * @param vonrCancelNum the vonrCancelNum to set
	 */
	public void setVonrCancelNum(Integer vonrCancelNum) {
		this.vonrCancelNum = vonrCancelNum;
	}
	
	/**
	 * @return the vonrDelayTimeAvg
	 */
	@Column(name = "VONR_DELAYTIME_AVG")
	public Float getVonrDelayTimeAvg() {
		return vonrDelayTimeAvg;
	}
	
	/**
	 * @param vonrDelayTimeAvg the vonrDelayTimeAvg to set
	 */
	public void setVonrDelayTimeAvg(Float vonrDelayTimeAvg) {
		this.vonrDelayTimeAvg = vonrDelayTimeAvg;
	}
	
	@Column(name = "CSFB_NOT_REQUEST_NUM")
	public String getCsfbNotRequest() {
		return csfbNotRequest;
	}
	
	public void setCsfbNotRequest(String csfbNotRequest) {
		this.csfbNotRequest = csfbNotRequest;
	}
	
	@Column(name = "CSFB_CALLBACK_SUCCESS_NUM")
	public String getCsfbCallbackSuccess() {
		return csfbCallbackSuccess;
	}
	
	public void setCsfbCallbackSuccess(String csfbCallbackSuccess) {
		this.csfbCallbackSuccess = csfbCallbackSuccess;
	}
	
	@Column(name = "CSFB_CALLBACK_TIME_AVG")
	public String getCsfbCallbackTime() {
		return csfbCallbackTime;
	}
	
	public void setCsfbCallbackTime(String csfbCallbackTime) {
		this.csfbCallbackTime = csfbCallbackTime;
	}
	
	@Column(name = "CSFB_CALLBACK_DROP_NUM")
	public String getCsfbCallbackDrop() {
		return csfbCallbackDrop;
	}
	
	public void setCsfbCallbackDrop(String csfbCallbackDrop) {
		this.csfbCallbackDrop = csfbCallbackDrop;
	}
	
	@Column(name = "NR_SSRSRP_SINR_100")
	public Float getNrSSRsrpSinr100() {
		return nrSSRsrpSinr100;
	}
	
	public void setNrSSRsrpSinr100(Float nrSSRsrpSinr100) {
		this.nrSSRsrpSinr100 = nrSSRsrpSinr100;
	}
	
	@Column(name = "NR_SSRSRP_SINR_105")
	public Float getNrSSRsrpSinr105() {
		return nrSSRsrpSinr105;
	}
	
	public void setNrSSRsrpSinr105(Float nrSSRsrpSinr105) {
		this.nrSSRsrpSinr105 = nrSSRsrpSinr105;
	}
	
	@Column(name = "NR_SSRSRP_SINR_110")
	public Float getNrSSRsrpSinr110() {
		return nrSSRsrpSinr110;
	}
	
	public void setNrSSRsrpSinr110(Float nrSSRsrpSinr110) {
		this.nrSSRsrpSinr110 = nrSSRsrpSinr110;
	}
	
	@Column(name = "NR_GNB_ID")
	public String getNrGnbid() {
		return nrGnbid;
	}
	
	public void setNrGnbid(String nrGnbid) {
		this.nrGnbid = nrGnbid;
	}
	
	

}
