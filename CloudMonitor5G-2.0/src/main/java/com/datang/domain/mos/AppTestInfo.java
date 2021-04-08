/**
 * 
 */
package com.datang.domain.mos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * APP测试上传的测试信息
 * 
 * @author yinzhipeng
 * @date:2015年11月4日 下午5:52:33
 * @version
 */
@Entity
@Table(name = "IADS_APP_TEST_INFO")
public class AppTestInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3260657399574230646L;
	private Long id;// id
	private String pingDelay;// ”ping时延”,
	private String upSpeedAvg;// ”上传平均速度，数据业务上传速率”,
	private String downSpeedAvg;// ”下载平均速度，数据业务下载速率”,
	private String mosScore;// ”mos值”,

	// @author yinzhipeng 2017-01-18 增加4个vmos字段
	private String vmos; //视频业务VMOS
	private String sQuality; //视频质量分
	private String sLoading;  //视频业务开始缓冲分
	private String sStalling; //视频卡顿分

	private String networkType;// ”网络类型（LTE /GSM/NR）”,
	private String lac_ci;// ”LAC/CI”,
	private String tac_eci;// ”TAC/ECI”,
	private String network;// ”信号强度”,
	private String lteNetwork;// LTE”信号强度”,
	private String sinr;// ”SINR”,
	private String pci;// ”PCI”,
	private String mcc;// ”MCC”,
	private String mnc;// ”MNC”,
	private String imei;// ”IMEI”,
	private String imsi;// ”IMSI”,
	private String mkr;// ”手机厂家”,
	private String model;// ”手机型号”,
	private String latitude;// ”纬度”,
	private String longitude;// ”经度”
	private String timestamp;// ”时间戳,测试时间”
	private String uniqueId;// ”唯一标识”
	
	// @author lucheng 2020-09-17 增加14个字段
	private String province;// "省"
	private String city;// "市"
	private String district;// "区"
	private String nrTac;// "NR TAC"
	private String nrArfcn;// "NR ARFCN"
	private String nrPci;// "NR PCI"
	private String nrRsrp;// "NR RSRP"
	private String nrSinr;// "NR SINR"
	private String earfcn;// "EARFCN"
	private Long phoneNUmber;// "手机号"
	private Long friendRate;// "朋友圈打开速率"
	private Long httpDlRate;// "HTTP下载速率"
	private Long httpTimeDelay;// "HTTP浏览时延"

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
	 * @return the pingDelaypingDelay
	 */
	@Column(name = "PING_DELAY")
	public String getPingDelay() {
		return pingDelay;
	}

	/**
	 * @param pingDelay
	 *            the pingDelay to set
	 */
	public void setPingDelay(String pingDelay) {
		this.pingDelay = pingDelay;
	}

	/**
	 * @return the upSpeedAvgupSpeedAvg
	 */
	@Column(name = "UP_SPEED_AVG")
	public String getUpSpeedAvg() {
		return upSpeedAvg;
	}

	/**
	 * @param upSpeedAvg
	 *            the upSpeedAvg to set
	 */
	public void setUpSpeedAvg(String upSpeedAvg) {
		this.upSpeedAvg = upSpeedAvg;
	}

	/**
	 * @return the downSpeedAvgdownSpeedAvg
	 */
	@Column(name = "DOWN_SPEED_AVG")
	public String getDownSpeedAvg() {
		return downSpeedAvg;
	}

	/**
	 * @param downSpeedAvg
	 *            the downSpeedAvg to set
	 */
	public void setDownSpeedAvg(String downSpeedAvg) {
		this.downSpeedAvg = downSpeedAvg;
	}

	/**
	 * @return the mosScoremosScore
	 */
	@Column(name = "MOS_SCORE")
	public String getMosScore() {
		return mosScore;
	}

	/**
	 * @param mosScore
	 *            the mosScore to set
	 */
	public void setMosScore(String mosScore) {
		this.mosScore = mosScore;
	}

	/**
	 * @return the networkTypenetworkType
	 */
	@Column(name = "NETWORK_TYPE")
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType
	 *            the networkType to set
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the lac_cilac_ci
	 */
	@Column(name = "LAC_CI")
	public String getLac_ci() {
		return lac_ci;
	}

	/**
	 * @param lac_ci
	 *            the lac_ci to set
	 */
	public void setLac_ci(String lac_ci) {
		this.lac_ci = lac_ci;
	}

	/**
	 * @return the tac_ecitac_eci
	 */
	@Column(name = "TAC_ECI")
	public String getTac_eci() {
		return tac_eci;
	}

	/**
	 * @param tac_eci
	 *            the tac_eci to set
	 */
	public void setTac_eci(String tac_eci) {
		this.tac_eci = tac_eci;
	}

	/**
	 * @return the networknetwork
	 */
	@Column(name = "NETWORK")
	public String getNetwork() {
		return network;
	}

	/**
	 * @param network
	 *            the network to set
	 */
	public void setNetwork(String network) {
		this.network = network;
	}

	/**
	 * @return the sinrsinr
	 */
	@Column(name = "SINR")
	public String getSinr() {
		return sinr;
	}

	/**
	 * @param sinr
	 *            the sinr to set
	 */
	public void setSinr(String sinr) {
		this.sinr = sinr;
	}

	/**
	 * @return the pcipci
	 */
	@Column(name = "PCI")
	public String getPci() {
		return pci;
	}

	/**
	 * @param pci
	 *            the pci to set
	 */
	public void setPci(String pci) {
		this.pci = pci;
	}

	/**
	 * @return the mccmcc
	 */
	@Column(name = "MCC")
	public String getMcc() {
		return mcc;
	}

	/**
	 * @param mcc
	 *            the mcc to set
	 */
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the mncmnc
	 */
	@Column(name = "MNC")
	public String getMnc() {
		return mnc;
	}

	/**
	 * @param mnc
	 *            the mnc to set
	 */
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	/**
	 * @return the imeiimei
	 */
	@Column(name = "IMEI")
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei
	 *            the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @return the imsiimsi
	 */
	@Column(name = "IMSI")
	public String getImsi() {
		return imsi;
	}

	/**
	 * @param imsi
	 *            the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * @return the mkrmkr
	 */
	@Column(name = "MKR")
	public String getMkr() {
		return mkr;
	}

	/**
	 * @param mkr
	 *            the mkr to set
	 */
	public void setMkr(String mkr) {
		this.mkr = mkr;
	}

	/**
	 * @return the modelmodel
	 */
	@Column(name = "MODEL")
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the latitudelatitude
	 */
	@Column(name = "LATITUDE")
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitudelongitude
	 */
	@Column(name = "LONGITUDE")
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the timestamptimestamp
	 */
	@Column(name = "TIME_STAMP")
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the uniqueIduniqueId
	 */
	@Column(name = "UNIQUE_ID")
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * @param uniqueId
	 *            the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * @return the province
	 */
	@Column(name = "PROVINCE")
	public String getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the district
	 */
	@Column(name = "DISTRICT")
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the lteNetworklteNetwork
	 */
	@Column(name = "LTE_NETWORK")
	public String getLteNetwork() {
		return lteNetwork;
	}

	/**
	 * @param lteNetwork
	 *            the lteNetwork to set
	 */
	public void setLteNetwork(String lteNetwork) {
		this.lteNetwork = lteNetwork;
	}

	/**
	 * @return the vmosvmos
	 */
	@Column(name = "VMOS")
	public String getVmos() {
		return vmos;
	}

	/**
	 * @param vmos
	 *            the vmos to set
	 */
	public void setVmos(String vmos) {
		this.vmos = vmos;
	}

	/**
	 * @return the sQualitysQuality
	 */
	@Column(name = "S_QUALITY")
	public String getsQuality() {
		return sQuality;
	}

	/**
	 * @param sQuality
	 *            the sQuality to set
	 */
	public void setsQuality(String sQuality) {
		this.sQuality = sQuality;
	}

	/**
	 * @return the sLoadingsLoading
	 */
	@Column(name = "S_LOADING")
	public String getsLoading() {
		return sLoading;
	}

	/**
	 * @param sLoading
	 *            the sLoading to set
	 */
	public void setsLoading(String sLoading) {
		this.sLoading = sLoading;
	}

	/**
	 * @return the sStallingsStalling
	 */
	@Column(name = "S_STALLING")
	public String getsStalling() {
		return sStalling;
	}

	/**
	 * @param sStalling
	 *            the sStalling to set
	 */
	public void setsStalling(String sStalling) {
		this.sStalling = sStalling;
	}

	/**
	 * @return the nrTac
	 */
	@Column(name = "NR_TAC")
	public String getNrTac() {
		return nrTac;
	}

	/**
	 * @param nrTac the nrTac to set
	 */
	public void setNrTac(String nrTac) {
		this.nrTac = nrTac;
	}

	/**
	 * @return the nrArfcn
	 */
	@Column(name = "NR_ARFCN")
	public String getNrArfcn() {
		return nrArfcn;
	}

	/**
	 * @param nrArfcn the nrArfcn to set
	 */
	public void setNrArfcn(String nrArfcn) {
		this.nrArfcn = nrArfcn;
	}

	/**
	 * @return the nrPci
	 */
	@Column(name = "NR_PCI")
	public String getNrPci() {
		return nrPci;
	}

	/**
	 * @param nrPci the nrPci to set
	 */
	public void setNrPci(String nrPci) {
		this.nrPci = nrPci;
	}

	/**
	 * @return the nrRsrp
	 */
	@Column(name = "NR_RSRP")
	public String getNrRsrp() {
		return nrRsrp;
	}

	/**
	 * @param nrRsrp the nrRsrp to set
	 */
	public void setNrRsrp(String nrRsrp) {
		this.nrRsrp = nrRsrp;
	}

	/**
	 * @return the nrSinr
	 */
	@Column(name = "NR_SINR")
	public String getNrSinr() {
		return nrSinr;
	}

	/**
	 * @param nrSinr the nrSinr to set
	 */
	public void setNrSinr(String nrSinr) {
		this.nrSinr = nrSinr;
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
	 * @return the phoneNUmber
	 */
	@Column(name = "PHONE_NUMBER")
	public Long getPhoneNUmber() {
		return phoneNUmber;
	}

	/**
	 * @param phoneNUmber the phoneNUmber to set
	 */
	public void setPhoneNUmber(Long phoneNUmber) {
		this.phoneNUmber = phoneNUmber;
	}

	/**
	 * @return the friendRate
	 */
	@Column(name = "FRIEND_CIRCLE_RATE")
	public Long getFriendRate() {
		return friendRate;
	}

	/**
	 * @param friendRate the friendRate to set
	 */
	public void setFriendRate(Long friendRate) {
		this.friendRate = friendRate;
	}

	/**
	 * @return the httpDlRate
	 */
	@Column(name = "HTTP_DL_RATE")
	public Long getHttpDlRate() {
		return httpDlRate;
	}

	/**
	 * @param httpDlRate the httpDlRate to set
	 */
	public void setHttpDlRate(Long httpDlRate) {
		this.httpDlRate = httpDlRate;
	}

	/**
	 * @return the httpTimeDelay
	 */
	@Column(name = "HTTP_TIME_DELAY")
	public Long getHttpTimeDelay() {
		return httpTimeDelay;
	}

	/**
	 * @param httpTimeDelay the httpTimeDelay to set
	 */
	public void setHttpTimeDelay(Long httpTimeDelay) {
		this.httpTimeDelay = httpTimeDelay;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppTestInfo [id=" + id + ", pingDelay=" + pingDelay + ", upSpeedAvg=" + upSpeedAvg + ", downSpeedAvg="
				+ downSpeedAvg + ", mosScore=" + mosScore + ", vmos=" + vmos + ", sQuality=" + sQuality + ", sLoading="
				+ sLoading + ", sStalling=" + sStalling + ", networkType=" + networkType + ", lac_ci=" + lac_ci
				+ ", tac_eci=" + tac_eci + ", network=" + network + ", lteNetwork=" + lteNetwork + ", sinr=" + sinr
				+ ", pci=" + pci + ", mcc=" + mcc + ", mnc=" + mnc + ", imei=" + imei + ", imsi=" + imsi + ", mkr="
				+ mkr + ", model=" + model + ", latitude=" + latitude + ", longitude=" + longitude + ", timestamp="
				+ timestamp + ", uniqueId=" + uniqueId + ", nrTac=" + nrTac + ", nrArfcn=" + nrArfcn + ", nrPci="
				+ nrPci + ", nrRsrp=" + nrRsrp + ", nrSinr=" + nrSinr + ", earfcn=" + earfcn
				+ ", phoneNUmber=" + phoneNUmber + ", friendRate=" + friendRate + ", httpDlRate=" + httpDlRate
				+ ", httpTimeDelay=" + httpTimeDelay + "]";
	}

}
