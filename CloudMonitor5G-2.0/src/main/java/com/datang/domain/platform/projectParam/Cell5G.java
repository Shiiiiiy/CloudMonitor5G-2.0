package com.datang.domain.platform.projectParam;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 5G工参表
 * @author _YZP
 *
 */
@Entity
@Table(name = "IADS_5G_CELL")
public class Cell5G implements Serializable,Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3797471680772783061L;
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 小区表基本信息
	 */
	private CellInfo cellInfo;
	/**
	 * MCC选填:所在PLMN网络的移动国家码
	 */
	private String mcc = "460";
	/**
	 * MNC选填:所在PLMN网络的移动网络码
	 */
	private String mnc = "0";
	/**
	 * AMF Group ID	选填	:AMF组标识（暂定）
	 */
	private String amfGroupId;
	/**
	 * AMF ID选填:AMF编号（暂定）
	 */
	private String amfId;
	/**
	 * gNB ID选填:基站设备唯一标识，用于标识一个PLMN所管辖区域范围内的gNB
	 */
	private String gNBId;
	/**
	 * SiteName选填:基站名称
	 */
	private String siteName;
	/**
	 * CellName必填:小区友好名
	 */
	private String cellName;
	
	/**
	 * LocalCellId
	 * 选填:eNB内的小区ID，唯一标识在一个eNB内的小区。在eNB内统一分配，同一个eNB中的LocalCellId配置值要求不能重复
	 */
	private Integer localCellId;
	/**
	 * CELL ID必填:小区标识
	 */
	private Long cellId;
	/**
	 * TAC必填:跟踪号
	 */
	private String tac;
	/**
	 * PCI必填:物理小区ID
	 */
	private Long pci;
	/**
	 * Frequency1 必填:中心频点1
	 */
	private Long frequency1;
	/**
	 * Frequency2 选填:中心频点2, 某些小区配置双载波，目前还没有三载波及以上的多载波的配置需求，工程参数字段应预留、可扩展。
	 */
	private Long frequency2;
	/**
	 * Frequency3预留字段
	 */
	private Long frequency3;
	/**
	 * Frequency4预留字段
	 */
	private Long frequency4;
	/**
	 * Frequency5预留字段
	 */
	private Long frequency5;

	/**
	 * Frequency6预留字段
	 */
	private Long frequency6;
	/**
	 * Frequency7预留字段
	 */
	private Long frequency7;
	/**
	 * Frequency8预留字段
	 */
	private Long frequency8;
	/**
	 * Frequency9预留字段
	 */
	private Long frequency9;
	/**
	 * Frequency10预留字段
	 */
	private Long frequency10;
	/**
	 * Frequency11预留字段
	 */
	private Long frequency11;

	/**
	 * Bandwidth1 选填:中心频点1对于的带宽
	 */
	private String bandwidth1;
	/**
	 * Bandwidth2 选填:中心频点2对于的带宽
	 */
	private String bandwidth2;
	/**
	 * FreqCount选填:频点个数
	 */
	private Integer freqCount;
	/**
	 * Longitude必填:经度
	 */
	private Float longitude;
	/**
	 * latitude必填:纬度
	 */
	private Float latitude;
	/**
	 * SectorType选填:扇区类型1:全向, 2:定向
	 */
	private String sectorType="2";
	/**
	 * DoorType 选填:室内外类型1:室外，2:室内
	 */
	private String doorType;
	/**
	 * Tilt Total必填:下倾角（度）
	 */
	private Float totalTilt;
	/**
	 * Tilt M 选填:机械下倾角（度）
	 */
	private Float mechTilt;
	/**
	 * Tilt E 选填:电子下倾角（度）
	 */
	private Float elecTilt;
	/**
	 * Azimuth必填:方向角
	 */
	private Float azimuth;
	/**
	 * BeamCount选填:波束个数
	 */
	private Float beanCount;
	
	/**
	 * Height必填:天线高度(m)
	 */
	private Float height;
	/**
	 * Region必填:所属区域，如徐汇区、黄浦区
	 */
	private String region;
	/**
	 *选填:厂家
	 */
	private String vender;
	/**
	 * 选填:覆盖场景
	 */
	private String coverScene;
	/**
	 * 选填:是否属于网络
	 */
	private String isBelongtoNetwork;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CI_ID")
	public CellInfo getCellInfo() {
		return cellInfo;
	}

	public void setCellInfo(CellInfo cellInfo) {
		this.cellInfo = cellInfo;
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

	@Column(name = "AMF_GROUP_ID")
	public String getAmfGroupId() {
		return amfGroupId;
	}

	public void setAmfGroupId(String amfGroupId) {
		this.amfGroupId = amfGroupId;
	}
	
	@Column(name = "AMF_ID")
	public String getAmfId() {
		return amfId;
	}

	public void setAmfId(String amfId) {
		this.amfId = amfId;
	}

	@Column(name = "GNB_ID")
	public String getgNBId() {
		return gNBId;
	}
	

	public void setgNBId(String gNBId) {
		this.gNBId = gNBId;
	}

	/**
	 * @return the siteNamesiteName
	 */
	@Column(name = "SITE_NAME")
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @param siteName
	 *            the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * @return the cellNamecellName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName
	 *            the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * @return the localCellIdlocalCellId
	 */
	@Column(name = "LOCAL_CELL_ID")
	public Integer getLocalCellId() {
		return localCellId;
	}

	/**
	 * @param localCellId
	 *            the localCellId to set
	 */
	public void setLocalCellId(Integer localCellId) {
		this.localCellId = localCellId;
	}

	/**
	 * @return the cellIdcellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the tactac
	 */
	@Column(name = "TAC")
	public String getTac() {
		return tac;
	}

	/**
	 * @param tac
	 *            the tac to set
	 */
	public void setTac(String tac) {
		this.tac = tac;
	}

	/**
	 * @return the pcipci
	 */
	@Column(name = "PCI")
	public Long getPci() {
		return pci;
	}

	/**
	 * @param pci
	 *            the pci to set
	 */
	public void setPci(Long pci) {
		this.pci = pci;
	}

	/**
	 * @return the frequency1frequency1
	 */
	@Column(name = "FREQUENCY1")
	public Long getFrequency1() {
		return frequency1;
	}

	/**
	 * @param frequency1
	 *            the frequency1 to set
	 */
	public void setFrequency1(Long frequency1) {
		this.frequency1 = frequency1;
	}

	/**
	 * @return the frequency2frequency2
	 */
	@Column(name = "FREQUENCY2")
	public Long getFrequency2() {
		return frequency2;
	}

	/**
	 * @param frequency2
	 *            the frequency2 to set
	 */
	public void setFrequency2(Long frequency2) {
		this.frequency2 = frequency2;
	}

	/**
	 * @return the frequency3frequency3
	 */
	@Column(name = "FREQUENCY3")
	public Long getFrequency3() {
		return frequency3;
	}

	/**
	 * @param frequency3
	 *            the frequency3 to set
	 */
	public void setFrequency3(Long frequency3) {
		this.frequency3 = frequency3;
	}

	/**
	 * @return the frequency4frequency4
	 */
	@Column(name = "FREQUENCY4")
	public Long getFrequency4() {
		return frequency4;
	}

	/**
	 * @param frequency4
	 *            the frequency4 to set
	 */
	public void setFrequency4(Long frequency4) {
		this.frequency4 = frequency4;
	}

	/**
	 * @return the frequency5frequency5
	 */
	@Column(name = "FREQUENCY5")
	public Long getFrequency5() {
		return frequency5;
	}

	/**
	 * @param frequency5
	 *            the frequency5 to set
	 */
	public void setFrequency5(Long frequency5) {
		this.frequency5 = frequency5;
	}

	/**
	 * @return the frequency6frequency6
	 */
	@Column(name = "FREQUENCY6")
	public Long getFrequency6() {
		return frequency6;
	}

	/**
	 * @param frequency6
	 *            the frequency6 to set
	 */
	public void setFrequency6(Long frequency6) {
		this.frequency6 = frequency6;
	}

	/**
	 * @return the frequency7frequency7
	 */
	@Column(name = "FREQUENCY7")
	public Long getFrequency7() {
		return frequency7;
	}

	/**
	 * @param frequency7
	 *            the frequency7 to set
	 */
	public void setFrequency7(Long frequency7) {
		this.frequency7 = frequency7;
	}

	/**
	 * @return the frequency8frequency8
	 */
	@Column(name = "FREQUENCY8")
	public Long getFrequency8() {
		return frequency8;
	}

	/**
	 * @param frequency8
	 *            the frequency8 to set
	 */
	public void setFrequency8(Long frequency8) {
		this.frequency8 = frequency8;
	}

	/**
	 * @return the frequency9frequency9
	 */
	@Column(name = "FREQUENCY9")
	public Long getFrequency9() {
		return frequency9;
	}

	/**
	 * @param frequency9
	 *            the frequency9 to set
	 */
	public void setFrequency9(Long frequency9) {
		this.frequency9 = frequency9;
	}

	/**
	 * @return the frequency10frequency10
	 */
	@Column(name = "FREQUENCY10")
	public Long getFrequency10() {
		return frequency10;
	}

	/**
	 * @param frequency10
	 *            the frequency10 to set
	 */
	public void setFrequency10(Long frequency10) {
		this.frequency10 = frequency10;
	}

	/**
	 * @return the frequency11frequency11
	 */
	@Column(name = "FREQUENCY11")
	public Long getFrequency11() {
		return frequency11;
	}

	/**
	 * @param frequency11
	 *            the frequency11 to set
	 */
	public void setFrequency11(Long frequency11) {
		this.frequency11 = frequency11;
	}

	/**
	 * @return the bandwidth1bandwidth1
	 */
	@Column(name = "BAND_WIDTH1")
	public String getBandwidth1() {
		return bandwidth1;
	}

	/**
	 * @param bandwidth1
	 *            the bandwidth1 to set
	 */
	public void setBandwidth1(String bandwidth1) {
		this.bandwidth1 = bandwidth1;
	}

	/**
	 * @return the bandwidth2bandwidth2
	 */
	@Column(name = "BAND_WIDTH2")
	public String getBandwidth2() {
		return bandwidth2;
	}

	/**
	 * @param bandwidth2
	 *            the bandwidth2 to set
	 */
	public void setBandwidth2(String bandwidth2) {
		this.bandwidth2 = bandwidth2;
	}

	/**
	 * @return the freqCountfreqCount
	 */
	@Column(name = "FREQ_COUNT")
	public Integer getFreqCount() {
		return freqCount;
	}

	/**
	 * @param freqCount
	 *            the freqCount to set
	 */
	public void setFreqCount(Integer freqCount) {
		this.freqCount = freqCount;
	}

	/**
	 * @return the longitudelongitude
	 */
	@Column(name = "LONGITUDE")
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitudelatitude
	 */
	@Column(name = "LATITUDE")
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the sectorTypesectorType
	 */
	@Column(name = "SECTOR_TYPE")
	public String getSectorType() {
		return sectorType;
	}

	/**
	 * @param sectorType
	 *            the sectorType to set
	 */
	public void setSectorType(String sectorType) {
		this.sectorType = sectorType;
	}

	/**
	 * @return the doorTypedoorType
	 */
	@Column(name = "DOOR_TYPE")
	public String getDoorType() {
		return doorType;
	}

	/**
	 * @param doorType
	 *            the doorType to set
	 */
	public void setDoorType(String doorType) {
		this.doorType = doorType;
	}

	/**
	 * @return the totalTilttotalTilt
	 */
	@Column(name = "TOTAL_TILT")
	public Float getTotalTilt() {
		return totalTilt;
	}

	/**
	 * @param totalTilt
	 *            the totalTilt to set
	 */
	public void setTotalTilt(Float totalTilt) {
		this.totalTilt = totalTilt;
	}

	/**
	 * @return the mechTiltmechTilt
	 */
	@Column(name = "MECH_TILT")
	public Float getMechTilt() {
		return mechTilt;
	}

	/**
	 * @param mechTilt
	 *            the mechTilt to set
	 */
	public void setMechTilt(Float mechTilt) {
		this.mechTilt = mechTilt;
	}

	/**
	 * @return the elecTiltelecTilt
	 */
	@Column(name = "ELEC_TILT")
	public Float getElecTilt() {
		return elecTilt;
	}

	/**
	 * @param elecTilt
	 *            the elecTilt to set
	 */
	public void setElecTilt(Float elecTilt) {
		this.elecTilt = elecTilt;
	}

	/**
	 * @return the azimuthazimuth
	 */
	@Column(name = "AZIMUTH")
	public Float getAzimuth() {
		return azimuth;
	}

	/**
	 * @param azimuth
	 *            the azimuth to set
	 */
	public void setAzimuth(Float azimuth) {
		this.azimuth = azimuth;
	}

	/**
	 * @return the beanCountbeanCount
	 */
	@Column(name = "BEAM_COUNT")
	public Float getBeanCount() {
		return beanCount;
	}

	/**
	 * @param beanCount
	 *            the beanCount to set
	 */
	public void setBeanCount(Float beanCount) {
		this.beanCount = beanCount;
	}


	/**
	 * @return the heightheight
	 */
	@Column(name = "HEIGHT")
	public Float getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(Float height) {
		this.height = height;
	}

	/**
	 * @return the regionregion
	 */
	@Column(name = "REGION")
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the vender
	 */
	@Column(name = "VENDER")
	public String getVender() {
		return vender;
	}

	/**
	 * @param the
	 *            vender to set
	 */

	public void setVender(String vender) {
		this.vender = vender;
	}

	/**
	 * @return the coverScene
	 */
	@Column(name = "COVER_SCENE")
	public String getCoverScene() {
		return coverScene;
	}

	/**
	 * @param the
	 *            coverScene to set
	 */

	public void setCoverScene(String coverScene) {
		this.coverScene = coverScene;
	}

	/**
	 * @return the isBelongtoNetwork
	 */
	@Column(name = "ISBELONGTO_NETWORK")
	public String getIsBelongtoNetwork() {
		return isBelongtoNetwork;
	}

	/**
	 * @param the
	 *            isBelongtoNetwork to set
	 */

	public void setIsBelongtoNetwork(String isBelongtoNetwork) {
		this.isBelongtoNetwork = isBelongtoNetwork;
	}
	
	@Override  
    public Object clone() {  
		Cell5G cell5G = null;  
        try{  
        	cell5G = (Cell5G)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return cell5G;  
    }
	
}
