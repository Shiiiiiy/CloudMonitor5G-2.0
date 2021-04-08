package com.datang.domain.stationTest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单验日志路测轨迹指标采样表
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_5G_EXCEPTION_SAM_TRAL")
public class StationSAMTralPojo {
	
private Long id;
	
	private String nrLogname; 
	
	private String beamSum; 
	
	private String latitude; 
	
	private String longtitude; 
	
	private Float lteIbler;
	
	private String ltePci; 
	
	private String ltePoint; 
	
	private String lteRsrp; 
	
	private String lteSinr; 
	
	private String nrIbler;
	
	private String nrPci;
	
	private String nrPoint;
	
	private String nrRsrp;
	
	private String nrRsrq;
	
	private String nrSinr;
	
	private String nrNcellnumber1;
	
	private String nrNcellnumber2; 
	
	private String nrNcellnumber3; 
	
	private String nrNcellnumber4; 
	
	private String nrNcellnumber5; 
	
	private String nrNcellnumber6; 
	
	private String nrNcellnumber7; 
	
	private String nrNcellnumber8; 
	
	private String recseqno; 
	
	private String timeLong; 
	
	private String lteCellid; 
	
	private String nrCellid; 
	
	private String nrMacthrputdl; 
	
	private String nrMacthrputul; 
	
	private String lteMacthrputdl; 
	
	private String lteMacthrputul;
	
	private Float nrSsRsrp0;
	
	private Float nrSsRsrp1;
	
	private Float nrSsRsrp2;
	
	private Float nrSsRsrp3;
	
	private Float nrSsRsrp4;
	
	private Float nrSsRsrp5;
	
	private Float nrSsRsrp6;
	
	private Float nrSsRsrp7;
	
	//NR_PHY_THR_UL
	private String nrPhyThrUL;
	
	//NR_PHY_THR_DL
	private String nrPhyThrDL;
	
	//NR_RLC_THR_UL
	private String nrRlcThrUL;
	
	//NR_RLC_THR_DL
	private String nrRlcThrDL;
	
	//NR_PDCP_THR_UL
	private String nrPdcpThrUL;
	
	//NR_PDCP_THR_DL
	private String nrPdcpThrDL;
	
	//NR_SDAP_THR_UL
	private String nrSdcpThrUL;
	
	//NR_SDAP_THR_DL
	private String nrSdcpThrDL;
	
	//LTE_PDCP_THR_UL
	private String ltePdcpThrUL;
	
	//LTE_PDCP_THR_DL
	private String ltePdcpThrDL;
	
	//LTE_RLC_THR_UL
	private String lteRlcThrUL;
	
	//LTE_RLC_THR_DL
	private String lteRlcThrDL;
	
	//LTE_PHY_THR_UL
	private String ltePhyThrUl;
	
	//LTE_PHY_THR_DL
	private String ltePhyThrDl;


	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NR_LOGNAME")
	public String getNrLogname() {
		return nrLogname;
	}

	public void setNrLogname(String nrLogname) {
		this.nrLogname = nrLogname;
	}
	
	@Column(name = "BEAM_SUM")
	public String getBeamSum() {
		return beamSum;
	}

	public void setBeamSum(String beamSum) {
		this.beamSum = beamSum;
	}
	
	@Column(name = "LATITUDE")
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@Column(name = "LONGTITUDE")
	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}
	
	@Column(name = "LTE_IBLER")
	public Float getLteIbler() {
		return lteIbler;
	}

	public void setLteIbler(Float lteIbler) {
		this.lteIbler = lteIbler;
	}

	@Column(name = "LTE_PCI")
	public String getLtePci() {
		return ltePci;
	}

	public void setLtePci(String ltePci) {
		this.ltePci = ltePci;
	}
	
	@Column(name = "LTE_POINT")
	public String getLtePoint() {
		return ltePoint;
	}

	public void setLtePoint(String ltePoint) {
		this.ltePoint = ltePoint;
	}
	
	@Column(name = "LTE_RSRP")
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
	
	@Column(name = "NR_IBLER")
	public String getNrIbler() {
		return nrIbler;
	}

	public void setNrIbler(String nrIbler) {
		this.nrIbler = nrIbler;
	}
	
	@Column(name = "NR_PCI")
	public String getNrPci() {
		return nrPci;
	}

	public void setNrPci(String nrPci) {
		this.nrPci = nrPci;
	}
	
	@Column(name = "NR_POINT")
	public String getNrPoint() {
		return nrPoint;
	}

	public void setNrPoint(String nrPoint) {
		this.nrPoint = nrPoint;
	}
	
	@Column(name = "NR_RSRP")
	public String getNrRsrp() {
		return nrRsrp;
	}

	public void setNrRsrp(String nrRsrp) {
		this.nrRsrp = nrRsrp;
	}
	
	@Column(name = "NR_RSRQ")
	public String getNrRsrq() {
		return nrRsrq;
	}

	public void setNrRsrq(String nrRsrq) {
		this.nrRsrq = nrRsrq;
	}
	
	@Column(name = "NR_NCELLNUMBER1")
	public String getNrNcellnumber1() {
		return nrNcellnumber1;
	}

	public void setNrNcellnumber1(String nrNcellnumber1) {
		this.nrNcellnumber1 = nrNcellnumber1;
	}
	
	@Column(name = "NR_NCELLNUMBER2")
	public String getNrNcellnumber2() {
		return nrNcellnumber2;
	}

	public void setNrNcellnumber2(String nrNcellnumber2) {
		this.nrNcellnumber2 = nrNcellnumber2;
	}
	
	@Column(name = "NR_NCELLNUMBER3")
	public String getNrNcellnumber3() {
		return nrNcellnumber3;
	}

	public void setNrNcellnumber3(String nrNcellnumber3) {
		this.nrNcellnumber3 = nrNcellnumber3;
	}
	
	@Column(name = "NR_NCELLNUMBER4")
	public String getNrNcellnumber4() {
		return nrNcellnumber4;
	}

	public void setNrNcellnumber4(String nrNcellnumber4) {
		this.nrNcellnumber4 = nrNcellnumber4;
	}
	
	@Column(name = "NR_NCELLNUMBER5")
	public String getNrNcellnumber5() {
		return nrNcellnumber5;
	}

	public void setNrNcellnumber5(String nrNcellnumber5) {
		this.nrNcellnumber5 = nrNcellnumber5;
	}
	
	@Column(name = "NR_NCELLNUMBER6")
	public String getNrNcellnumber6() {
		return nrNcellnumber6;
	}

	public void setNrNcellnumber6(String nrNcellnumber6) {
		this.nrNcellnumber6 = nrNcellnumber6;
	}
	
	@Column(name = "NR_NCELLNUMBER7")
	public String getNrNcellnumber7() {
		return nrNcellnumber7;
	}

	public void setNrNcellnumber7(String nrNcellnumber7) {
		this.nrNcellnumber7 = nrNcellnumber7;
	}
	
	@Column(name = "NR_NCELLNUMBER8")
	public String getNrNcellnumber8() {
		return nrNcellnumber8;
	}

	public void setNrNcellnumber8(String nrNcellnumber8) {
		this.nrNcellnumber8 = nrNcellnumber8;
	}
	
	@Column(name = "RECSEQNO")
	public String getRecseqno() {
		return recseqno;
	}

	public void setRecseqno(String recseqno) {
		this.recseqno = recseqno;
	}
	
	@Column(name = "TIME_LONG")
	public String getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(String timeLong) {
		this.timeLong = timeLong;
	}
	
	@Column(name = "LTE_CELLID")
	public String getLteCellid() {
		return lteCellid;
	}

	public void setLteCellid(String lteCellid) {
		this.lteCellid = lteCellid;
	}
	
	@Column(name = "NR_CELLID")
	public String getNrCellid() {
		return nrCellid;
	}

	public void setNrCellid(String nrCellid) {
		this.nrCellid = nrCellid;
	}
	
	@Column(name = "NR_MACTHRPUTDL")
	public String getNrMacthrputdl() {
		return nrMacthrputdl;
	}

	public void setNrMacthrputdl(String nrMacthrputdl) {
		this.nrMacthrputdl = nrMacthrputdl;
	}
	
	@Column(name = "NR_MACTHRPUTUL")
	public String getNrMacthrputul() {
		return nrMacthrputul;
	}

	public void setNrMacthrputul(String nrMacthrputul) {
		this.nrMacthrputul = nrMacthrputul;
	}
	
	@Column(name = "LTE_MACTHRPUTDL")
	public String getLteMacthrputdl() {
		return lteMacthrputdl;
	}

	public void setLteMacthrputdl(String lteMacthrputdl) {
		this.lteMacthrputdl = lteMacthrputdl;
	}
	
	@Column(name = "LTE_MACTHRPUTUL")
	public String getLteMacthrputul() {
		return lteMacthrputul;
	}

	public void setLteMacthrputul(String lteMacthrputul) {
		this.lteMacthrputul = lteMacthrputul;
	}
	
	@Column(name = "NR_SINR")
	public String getNrSinr() {
		return nrSinr;
	}

	public void setNrSinr(String nrSinr) {
		this.nrSinr = nrSinr;
	}

	@Column(name = "NR_SS_RSRP0")
	public Float getNrSsRsrp0() {
		return nrSsRsrp0;
	}

	public void setNrSsRsrp0(Float nrSsRsrp0) {
		this.nrSsRsrp0 = nrSsRsrp0;
	}

	@Column(name = "NR_SS_RSRP1")
	public Float getNrSsRsrp1() {
		return nrSsRsrp1;
	}

	public void setNrSsRsrp1(Float nrSsRsrp1) {
		this.nrSsRsrp1 = nrSsRsrp1;
	}

	@Column(name = "NR_SS_RSRP2")
	public Float getNrSsRsrp2() {
		return nrSsRsrp2;
	}

	public void setNrSsRsrp2(Float nrSsRsrp2) {
		this.nrSsRsrp2 = nrSsRsrp2;
	}

	@Column(name = "NR_SS_RSRP3")
	public Float getNrSsRsrp3() {
		return nrSsRsrp3;
	}

	public void setNrSsRsrp3(Float nrSsRsrp3) {
		this.nrSsRsrp3 = nrSsRsrp3;
	}

	@Column(name = "NR_SS_RSRP4")
	public Float getNrSsRsrp4() {
		return nrSsRsrp4;
	}

	public void setNrSsRsrp4(Float nrSsRsrp4) {
		this.nrSsRsrp4 = nrSsRsrp4;
	}

	@Column(name = "NR_SS_RSRP5")
	public Float getNrSsRsrp5() {
		return nrSsRsrp5;
	}

	public void setNrSsRsrp5(Float nrSsRsrp5) {
		this.nrSsRsrp5 = nrSsRsrp5;
	}

	@Column(name = "NR_SS_RSRP6")
	public Float getNrSsRsrp6() {
		return nrSsRsrp6;
	}

	public void setNrSsRsrp6(Float nrSsRsrp6) {
		this.nrSsRsrp6 = nrSsRsrp6;
	}

	@Column(name = "NR_SS_RSRP7")
	public Float getNrSsRsrp7() {
		return nrSsRsrp7;
	}

	public void setNrSsRsrp7(Float nrSsRsrp7) {
		this.nrSsRsrp7 = nrSsRsrp7;
	}
	
	/**
	 * @return the nrPhyThrUL
	 */
	@Column(name = "NR_PHY_THR_UL")
	public String getNrPhyThrUL() {
		return nrPhyThrUL;
	}

	/**
	 * @param nrPhyThrUL the nrPhyThrUL to set
	 */
	public void setNrPhyThrUL(String nrPhyThrUL) {
		this.nrPhyThrUL = nrPhyThrUL;
	}

	/**
	 * @return the nrPhyThrDL
	 */
	@Column(name = "NR_PHY_THR_DL")
	public String getNrPhyThrDL() {
		return nrPhyThrDL;
	}

	/**
	 * @param nrPhyThrDL the nrPhyThrDL to set
	 */
	public void setNrPhyThrDL(String nrPhyThrDL) {
		this.nrPhyThrDL = nrPhyThrDL;
	}

	/**
	 * @return the nrRlcThrUL
	 */
	@Column(name = "NR_RLC_THR_UL")
	public String getNrRlcThrUL() {
		return nrRlcThrUL;
	}

	/**
	 * @param nrRlcThrUL the nrRlcThrUL to set
	 */
	public void setNrRlcThrUL(String nrRlcThrUL) {
		this.nrRlcThrUL = nrRlcThrUL;
	}

	/**
	 * @return the nrRlcThrDL
	 */
	@Column(name = "NR_RLC_THR_DL")
	public String getNrRlcThrDL() {
		return nrRlcThrDL;
	}

	/**
	 * @param nrRlcThrDL the nrRlcThrDL to set
	 */
	public void setNrRlcThrDL(String nrRlcThrDL) {
		this.nrRlcThrDL = nrRlcThrDL;
	}

	/**
	 * @return the nrPdcpThrUL
	 */
	@Column(name = "NR_PDCP_THR_UL")
	public String getNrPdcpThrUL() {
		return nrPdcpThrUL;
	}

	/**
	 * @param nrPdcpThrUL the nrPdcpThrUL to set
	 */
	public void setNrPdcpThrUL(String nrPdcpThrUL) {
		this.nrPdcpThrUL = nrPdcpThrUL;
	}

	/**
	 * @return the nrPdcpThrDL
	 */
	@Column(name = "NR_PDCP_THR_DL")
	public String getNrPdcpThrDL() {
		return nrPdcpThrDL;
	}

	/**
	 * @param nrPdcpThrDL the nrPdcpThrDL to set
	 */
	public void setNrPdcpThrDL(String nrPdcpThrDL) {
		this.nrPdcpThrDL = nrPdcpThrDL;
	}

	/**
	 * @return the nrSdcpThrUL
	 */
	@Column(name = "NR_SDAP_THR_UL")
	public String getNrSdcpThrUL() {
		return nrSdcpThrUL;
	}

	/**
	 * @param nrSdcpThrUL the nrSdcpThrUL to set
	 */
	public void setNrSdcpThrUL(String nrSdcpThrUL) {
		this.nrSdcpThrUL = nrSdcpThrUL;
	}

	/**
	 * @return the nrSdcpThrDL
	 */
	@Column(name = "NR_SDAP_THR_DL")
	public String getNrSdcpThrDL() {
		return nrSdcpThrDL;
	}

	/**
	 * @param nrSdcpThrDL the nrSdcpThrDL to set
	 */
	public void setNrSdcpThrDL(String nrSdcpThrDL) {
		this.nrSdcpThrDL = nrSdcpThrDL;
	}

	/**
	 * @return the ltePdcpThrUL
	 */
	@Column(name = "LTE_PDCP_THR_UL")
	public String getLtePdcpThrUL() {
		return ltePdcpThrUL;
	}

	/**
	 * @param ltePdcpThrUL the ltePdcpThrUL to set
	 */
	public void setLtePdcpThrUL(String ltePdcpThrUL) {
		this.ltePdcpThrUL = ltePdcpThrUL;
	}

	/**
	 * @return the ltePdcpThrDL
	 */
	@Column(name = "LTE_PDCP_THR_DL")
	public String getLtePdcpThrDL() {
		return ltePdcpThrDL;
	}

	/**
	 * @param ltePdcpThrDL the ltePdcpThrDL to set
	 */
	public void setLtePdcpThrDL(String ltePdcpThrDL) {
		this.ltePdcpThrDL = ltePdcpThrDL;
	}

	/**
	 * @return the lteRlcThrUL
	 */
	@Column(name = "LTE_RLC_THR_UL")
	public String getLteRlcThrUL() {
		return lteRlcThrUL;
	}

	/**
	 * @param lteRlcThrUL the lteRlcThrUL to set
	 */
	public void setLteRlcThrUL(String lteRlcThrUL) {
		this.lteRlcThrUL = lteRlcThrUL;
	}

	/**
	 * @return the lteRlcThrDL
	 */
	@Column(name = "LTE_RLC_THR_DL")
	public String getLteRlcThrDL() {
		return lteRlcThrDL;
	}

	/**
	 * @param lteRlcThrDL the lteRlcThrDL to set
	 */
	public void setLteRlcThrDL(String lteRlcThrDL) {
		this.lteRlcThrDL = lteRlcThrDL;
	}

	/**
	 * @return the ltePhyThrUl
	 */
	@Column(name = "LTE_PHY_THR_UL")
	public String getLtePhyThrUl() {
		return ltePhyThrUl;
	}

	/**
	 * @param ltePhyThrUl the ltePhyThrUl to set
	 */
	public void setLtePhyThrUl(String ltePhyThrUl) {
		this.ltePhyThrUl = ltePhyThrUl;
	}

	/**
	 * @return the ltePhyThrDl
	 */
	@Column(name = "LTE_PHY_THR_DL")
	public String getLtePhyThrDl() {
		return ltePhyThrDl;
	}

	/**
	 * @param ltePhyThrDl the ltePhyThrDl to set
	 */
	public void setLtePhyThrDl(String ltePhyThrDl) {
		this.ltePhyThrDl = ltePhyThrDl;
	}
	
}
