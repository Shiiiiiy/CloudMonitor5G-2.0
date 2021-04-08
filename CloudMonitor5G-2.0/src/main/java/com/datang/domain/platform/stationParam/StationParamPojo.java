package com.datang.domain.platform.stationParam;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.datang.domain.security.menu.Menu;

/**
 * 单站参数实体类
 * @author maxuancheng
 */
@Entity
@Table(name = "IADS_STATION_PARAM")
public class StationParamPojo implements Serializable{
	
	private static final long serialVersionUID = 2646423832182549992L;

	private Long id;
	
	/**
	 * 下载
	 */
	private Float upgradeGood;
	
	private Float upgradeMid;
	
	private Float upgradeBad;
	
	/**
	 * 上传
	 */
	private Float uploadGood;
	
	private Float uploadMid;
	
	private Float uploadBad;
	
	/**
	 * 峰值
	 */
	private Float downloadGoodApex;
	
	private Float uploadGoodApex;
	
	
	/**
	 * 4g连接成功率
	 */
	private Float connectSuccessRatio4G;
	
	private Float connectNumber4g;
	
	private Float changeSuccessRatio4G;
	
	private Float disconectRatio4G;
	
	private Float rrcRatio4G;
	
	private Float connectSuccess5G;
	
	private Float connectNumber5G;
	
	private Float changeSuccessRatio5G;
	
	private Float disconectRatio5G;
	
	private Float ping32DelayTime;
	
	private Float ping32SuccessRatio;
	
	private Float ping1500DelayTime;
	
	private Float ping1500SuccessRatio;
	
	private Float ping32Number;
	
	private Float ping1500Nember;
	
	private Float upgradeVeryGood3D4G;

	private Float uploadVeryGood3D4G;

	private Float ping32DelayTime3D;

	private Float ping32SuccessRatio3D;

	private Float ping32Number3D;
	
	private Float nrAccessDelayTime; 
	
	private Float voiceAccessRatio; 
	
	private Float voiceDropRatio; 
	
	private Float voiceAccesDelayTime; 
	
	private Float dtDownloadRate; 
	
	private Float dtUploadRate; 
	
	private Float dtTestRsrp; 
	
	private Float dtTestSinr; 
	
	private Float epsFallbackTestNum; 
	
	private Float epsFallbackTimeDelay; 
	
	private Float vonrTestNumber; 
	
	/**
	 * 是否路测轨迹抽样显示  1:是   0:否
	 */
	private String trailSamDisplay;
	
	/**
	 *  1：山西单验报告模板，2：兰州单验报告模板，3：贵阳单验报告模板，4：云南（室外）单验报告模板 ,5: 云南（室分）单验报告模板
	 */
	private String singleStationMOdelSelect;
	
	/**
	 *  1：山西反开3d模板，2：兰州反开3d模板，3：贵阳单验报告模板，4：云南（室外）单验报告模板 ,5: 云南（室分）单验报告模板
	 */
	private String contrastOpen3DMOdelSelect;

	
	private Menu menu;
	
	private Long menuId;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "UPGRADE_GOOD")
	public Float getUpgradeGood() {
		return upgradeGood;
	}

	public void setUpgradeGood(Float upgradeGood) {
		this.upgradeGood = upgradeGood;
	}

	@Column(name = "UPGRADE_MID")
	public Float getUpgradeMid() {
		return upgradeMid;
	}

	public void setUpgradeMid(Float upgradeMid) {
		this.upgradeMid = upgradeMid;
	}

	@Column(name = "UPGRADE_BAD")
	public Float getUpgradeBad() {
		return upgradeBad;
	}

	public void setUpgradeBad(Float upgradeBad) {
		this.upgradeBad = upgradeBad;
	}

	@Column(name = "UPLOAD_GOOD")
	public Float getUploadGood() {
		return uploadGood;
	}

	public void setUploadGood(Float uploadGood) {
		this.uploadGood = uploadGood;
	}

	@Column(name = "UPLOAD_MID")
	public Float getUploadMid() {
		return uploadMid;
	}

	public void setUploadMid(Float uploadMid) {
		this.uploadMid = uploadMid;
	}

	@Column(name = "UPLOAD_BAD")
	public Float getUploadBad() {
		return uploadBad;
	}

	public void setUploadBad(Float uploadBad) {
		this.uploadBad = uploadBad;
	}

	@Column(name = "CONNECT_SUCCESS_RATIO4G")
	public Float getConnectSuccessRatio4G() {
		return connectSuccessRatio4G;
	}

	public void setConnectSuccessRatio4G(Float connectSuccessRatio4G) {
		this.connectSuccessRatio4G = connectSuccessRatio4G;
	}

	@Column(name = "CONNECT_NUMBER4G")
	public Float getConnectNumber4g() {
		return connectNumber4g;
	}

	public void setConnectNumber4g(Float connectNumber4g) {
		this.connectNumber4g = connectNumber4g;
	}

	@Column(name = "CHANGE_SUCCESS_RATIO4G")
	public Float getChangeSuccessRatio4G() {
		return changeSuccessRatio4G;
	}

	public void setChangeSuccessRatio4G(Float changeSuccessRatio4G) {
		this.changeSuccessRatio4G = changeSuccessRatio4G;
	}

	@Column(name = "DISCONECT_RATIO4G")
	public Float getDisconectRatio4G() {
		return disconectRatio4G;
	}

	public void setDisconectRatio4G(Float disconectRatio4G) {
		this.disconectRatio4G = disconectRatio4G;
	}

	@Column(name = "RRCRATIO_4G")
	public Float getRrcRatio4G() {
		return rrcRatio4G;
	}

	public void setRrcRatio4G(Float rrcRatio4G) {
		this.rrcRatio4G = rrcRatio4G;
	}

	@Column(name = "CONNECT_SUCCESS5G")
	public Float getConnectSuccess5G() {
		return connectSuccess5G;
	}

	public void setConnectSuccess5G(Float connectSuccess5G) {
		this.connectSuccess5G = connectSuccess5G;
	}

	@Column(name = "CONNECT_NUMBER5G")
	public Float getConnectNumber5G() {
		return connectNumber5G;
	}

	public void setConnectNumber5G(Float connectNumber5G) {
		this.connectNumber5G = connectNumber5G;
	}

	@Column(name = "CHANGE_SUCCESS_RATIO5G")
	public Float getChangeSuccessRatio5G() {
		return changeSuccessRatio5G;
	}

	public void setChangeSuccessRatio5G(Float changeSuccessRatio5G) {
		this.changeSuccessRatio5G = changeSuccessRatio5G;
	}

	@Column(name = "DISCONECT_RATIO5G")
	public Float getDisconectRatio5G() {
		return disconectRatio5G;
	}

	public void setDisconectRatio5G(Float disconectRatio5G) {
		this.disconectRatio5G = disconectRatio5G;
	}

	@Column(name = "PING32_DELAY_TIME")
	public Float getPing32DelayTime() {
		return ping32DelayTime;
	}

	public void setPing32DelayTime(Float ping32DelayTime) {
		this.ping32DelayTime = ping32DelayTime;
	}

	@Column(name = "PING32_SUCCESS_RATIO")
	public Float getPing32SuccessRatio() {
		return ping32SuccessRatio;
	}

	public void setPing32SuccessRatio(Float ping32SuccessRatio) {
		this.ping32SuccessRatio = ping32SuccessRatio;
	}

	@Column(name = "PING1500_DELAY_TIME")
	public Float getPing1500DelayTime() {
		return ping1500DelayTime;
	}

	public void setPing1500DelayTime(Float ping1500DelayTime) {
		this.ping1500DelayTime = ping1500DelayTime;
	}

	@Column(name = "PING1500_SUCCESS_RATIO")
	public Float getPing1500SuccessRatio() {
		return ping1500SuccessRatio;
	}

	public void setPing1500SuccessRatio(Float ping1500SuccessRatio) {
		this.ping1500SuccessRatio = ping1500SuccessRatio;
	}

	@Column(name = "PING32_NUMBER")
	public Float getPing32Number() {
		return ping32Number;
	}

	public void setPing32Number(Float ping32Number) {
		this.ping32Number = ping32Number;
	}

	@Column(name = "PING1500_NEMBER")
	public Float getPing1500Nember() {
		return ping1500Nember;
	}

	public void setPing1500Nember(Float ping1500Nember) {
		this.ping1500Nember = ping1500Nember;
	}

	/**
	 * @return the upgradeVeryGood3D4G
	 */
	@Column(name = "UPGRADE_VERYGOOD_3D4G")
	public Float getUpgradeVeryGood3D4G() {
		return upgradeVeryGood3D4G;
	}

	/**
	 * @param upgradeVeryGood3D4G the upgradeVeryGood3D4G to set
	 */
	public void setUpgradeVeryGood3D4G(Float upgradeVeryGood3D4G) {
		this.upgradeVeryGood3D4G = upgradeVeryGood3D4G;
	}

	/**
	 * @return the uploadVeryGood3D4G
	 */
	@Column(name = "UPLOAD_VERYGOOD_3D4G")
	public Float getUploadVeryGood3D4G() {
		return uploadVeryGood3D4G;
	}

	/**
	 * @param uploadVeryGood3D4G the uploadVeryGood3D4G to set
	 */
	public void setUploadVeryGood3D4G(Float uploadVeryGood3D4G) {
		this.uploadVeryGood3D4G = uploadVeryGood3D4G;
	}

	/**
	 * @return the ping32DelayTime3D
	 */
	@Column(name = "PING32_DELAY_TIME3D")
	public Float getPing32DelayTime3D() {
		return ping32DelayTime3D;
	}

	/**
	 * @param ping32DelayTime3D the ping32DelayTime3D to set
	 */
	public void setPing32DelayTime3D(Float ping32DelayTime3D) {
		this.ping32DelayTime3D = ping32DelayTime3D;
	}

	/**
	 * @return the ping32SuccessRatio3D
	 */
	@Column(name = "PING32_SUCCESS_RATIO3D")
	public Float getPing32SuccessRatio3D() {
		return ping32SuccessRatio3D;
	}

	/**
	 * @param ping32SuccessRatio3D the ping32SuccessRatio3D to set
	 */
	public void setPing32SuccessRatio3D(Float ping32SuccessRatio3D) {
		this.ping32SuccessRatio3D = ping32SuccessRatio3D;
	}

	/**
	 * @return the ping32Number3D
	 */
	@Column(name = "PING32_NUMBER3D")
	public Float getPing32Number3D() {
		return ping32Number3D;
	}

	/**
	 * @param ping32Number3D the ping32Number3D to set
	 */
	public void setPing32Number3D(Float ping32Number3D) {
		this.ping32Number3D = ping32Number3D;
	}

	/**
	 * @return the nrAccessDelayTime
	 */
	@Column(name = "NR_ACCESS_DELAY_TIME")
	public Float getNrAccessDelayTime() {
		return nrAccessDelayTime;
	}

	/**
	 * @param nrAccessDelayTime the nrAccessDelayTime to set
	 */
	public void setNrAccessDelayTime(Float nrAccessDelayTime) {
		this.nrAccessDelayTime = nrAccessDelayTime;
	}

	/**
	 * @return the voiceAccessRatio
	 */
	@Column(name = "VOICE_ACCESS_RATIO")
	public Float getVoiceAccessRatio() {
		return voiceAccessRatio;
	}

	/**
	 * @param voiceAccessRatio the voiceAccessRatio to set
	 */
	public void setVoiceAccessRatio(Float voiceAccessRatio) {
		this.voiceAccessRatio = voiceAccessRatio;
	}

	/**
	 * @return the voiceDropRatio
	 */
	@Column(name = "VOICE_DROP_RATIO")
	public Float getVoiceDropRatio() {
		return voiceDropRatio;
	}

	/**
	 * @param voiceDropRatio the voiceDropRatio to set
	 */
	public void setVoiceDropRatio(Float voiceDropRatio) {
		this.voiceDropRatio = voiceDropRatio;
	}

	/**
	 * @return the voiceAccesDelayTime
	 */
	@Column(name = "VOICE_ACCESS_DELAY_TIME")
	public Float getVoiceAccesDelayTime() {
		return voiceAccesDelayTime;
	}

	/**
	 * @param voiceAccesDelayTime the voiceAccesDelayTime to set
	 */
	public void setVoiceAccesDelayTime(Float voiceAccesDelayTime) {
		this.voiceAccesDelayTime = voiceAccesDelayTime;
	}

	/**
	 * @return the dtDownloadRate
	 */
	@Column(name = "DT_DOWNLAOD_RATE")
	public Float getDtDownloadRate() {
		return dtDownloadRate;
	}

	/**
	 * @param dtDownloadRate the dtDownloadRate to set
	 */
	public void setDtDownloadRate(Float dtDownloadRate) {
		this.dtDownloadRate = dtDownloadRate;
	}

	/**
	 * @return the dtUploadRate
	 */
	@Column(name = "DT_UPLOAD_RATE")
	public Float getDtUploadRate() {
		return dtUploadRate;
	}

	/**
	 * @param dtUploadRate the dtUploadRate to set
	 */
	public void setDtUploadRate(Float dtUploadRate) {
		this.dtUploadRate = dtUploadRate;
	}

	/**
	 * @return the dtTestRsrp
	 */
	@Column(name = "DT_TEST_RSRP")
	public Float getDtTestRsrp() {
		return dtTestRsrp;
	}

	/**
	 * @param dtTestRsrp the dtTestRsrp to set
	 */
	public void setDtTestRsrp(Float dtTestRsrp) {
		this.dtTestRsrp = dtTestRsrp;
	}

	/**
	 * @return the dtTestSinr
	 */
	@Column(name = "DT_TEST_SINR")
	public Float getDtTestSinr() {
		return dtTestSinr;
	}

	/**
	 * @param dtTestSinr the dtTestSinr to set
	 */
	public void setDtTestSinr(Float dtTestSinr) {
		this.dtTestSinr = dtTestSinr;
	}

	
	/**
	 * @return the epsFallbackTestNum
	 */
	@Column(name = "EPS_FALLBACK_TEST_NUM")
	public Float getEpsFallbackTestNum() {
		return epsFallbackTestNum;
	}

	/**
	 * @param epsFallbackTestNum the epsFallbackTestNum to set
	 */
	public void setEpsFallbackTestNum(Float epsFallbackTestNum) {
		this.epsFallbackTestNum = epsFallbackTestNum;
	}

	/**
	 * @return the epsFallbackTimeDelay
	 */
	@Column(name = "EPS_FALLBACK_TIME_DELAY")
	public Float getEpsFallbackTimeDelay() {
		return epsFallbackTimeDelay;
	}

	/**
	 * @param epsFallbackTimeDelay the epsFallbackTimeDelay to set
	 */
	public void setEpsFallbackTimeDelay(Float epsFallbackTimeDelay) {
		this.epsFallbackTimeDelay = epsFallbackTimeDelay;
	}

	/**
	 * @return the vonrTestNumber
	 */
	@Column(name = "VONR_TEST_NUMBER")
	public Float getVonrTestNumber() {
		return vonrTestNumber;
	}

	/**
	 * @param vonrTestNumber the vonrTestNumber to set
	 */
	public void setVonrTestNumber(Float vonrTestNumber) {
		this.vonrTestNumber = vonrTestNumber;
	}
	

	/**
	 * @return the downloadGoodApex
	 */
	@Column(name = "DOWNLOAD_GOOD_APEX")
	public Float getDownloadGoodApex() {
		return downloadGoodApex;
	}

	/**
	 * @param downloadGoodApex the downloadGoodApex to set
	 */
	public void setDownloadGoodApex(Float downloadGoodApex) {
		this.downloadGoodApex = downloadGoodApex;
	}

	/**
	 * @return the uploadGoodApex
	 */
	@Column(name = "UPLOAD_GOOD_APEX")
	public Float getUploadGoodApex() {
		return uploadGoodApex;
	}

	/**
	 * @param uploadGoodApex the uploadGoodApex to set
	 */
	public void setUploadGoodApex(Float uploadGoodApex) {
		this.uploadGoodApex = uploadGoodApex;
	}

	@Column(name = "TRAIL_SAM_DISPLAY")
	public String getTrailSamDisplay() {
		return trailSamDisplay;
	}

	public void setTrailSamDisplay(String trailSamDisplay) {
		this.trailSamDisplay = trailSamDisplay;
	}

	/**
	 * @return the singleStationMOdelSelect
	 */
	@Column(name = "SINGLE_STATION_MODEL_SELECT")
	public String getSingleStationMOdelSelect() {
		return singleStationMOdelSelect;
	}

	/**
	 * @param singleStationMOdelSelect the singleStationMOdelSelect to set
	 */
	public void setSingleStationMOdelSelect(String singleStationMOdelSelect) {
		this.singleStationMOdelSelect = singleStationMOdelSelect;
	}

	/**
	 * @return the contrastOpen3DMOdelSelect
	 */
	@Column(name = "CONTRAST_OPEN3D_MODEL_SELECT")
	public String getContrastOpen3DMOdelSelect() {
		return contrastOpen3DMOdelSelect;
	}

	/**
	 * @param contrastOpen3DMOdelSelect the contrastOpen3DMOdelSelect to set
	 */
	public void setContrastOpen3DMOdelSelect(String contrastOpen3DMOdelSelect) {
		this.contrastOpen3DMOdelSelect = contrastOpen3DMOdelSelect;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "stationParamPojo",fetch = FetchType.LAZY)
	@JoinColumn(name="MENU_ID")
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Transactional
	public Long getMenuId() {
		if(this.menu!=null){
			return this.menu.getId();
		}
		return null;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
	
}
