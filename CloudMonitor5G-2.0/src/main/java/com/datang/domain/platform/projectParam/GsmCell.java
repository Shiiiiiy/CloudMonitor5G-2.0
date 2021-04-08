/**
 * 
 */
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
 * GSM小区表
 * 
 * @author yinzhipeng
 * @date:2015年10月19日 上午10:45:51
 * @version
 */
@Entity
@Table(name = "IADS_GSM_CELL")
public class GsmCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5760676853831056027L;
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
	 * RAC选填:路由区
	 */
	private String rac;
	/**
	 * LAC 必填:所在位置区的位置区域码
	 */
	private Long lac;
	/**
	 * CI 必填:小区标识。LAC内唯一
	 */
	private Long ci;
	/**
	 * SiteName选填:基站名称
	 */
	private String siteName;
	/**
	 * CellName必填:小区友好名
	 */
	private String cellName;
	/**
	 * BCCH必填:BCCH频点
	 */
	private Long bcch;
	/**
	 * BSIC必填:小区扰码。由NCC与BCC组成，每个3位，实际上就是一个8进制,
	 * 最小0，最大7。直接用十进制表示，NCC在前，BCC在后，比如67，其中6表示NCC，7表示BCC。
	 */
	private Long bsic;
	/**
	 * LocalCellId选填:0,1,2表示第一、二、三扇区，依次类推，不限制扇区数，全向小区也开始于0
	 */
	private Integer localCellId;
	/**
	 * NCC 选填 NCC
	 */
	private Integer ncc;
	/**
	 * BCC 选填 BCC
	 */
	private Integer bcc;
	/**
	 * DCS 选填:是否是DCS1800。 0：不是；1：是
	 */
	private Integer dcs;
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
	private String sectorType;
	/**
	 * AHeight 选填:天线高度(m)
	 */
	private Float antHigh;
	/**
	 * Azimuth必填:方向角
	 */
	private Float azimuth;
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
	 * Beamwidth必填:天线波束宽度（度）
	 */
	private Float beamWidth;
	/**
	 * VBeamWidth 选填:天线垂直波束宽度(度)
	 */
	private Float vBeamWidth;
	/**
	 * DoorType 选填:室内外类型1:室外，2:室内
	 */
	private String doorType;
	/**
	 * Region 程序填:所属二级域，如徐汇区、黄浦区
	 */
	private String region;

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
	 * @return the beamWidthbeamWidth
	 */
	@Column(name = "BEAM_WIDTH")
	public Float getBeamWidth() {
		return beamWidth;
	}

	/**
	 * @param beamWidth
	 *            the beamWidth to set
	 */
	public void setBeamWidth(Float beamWidth) {
		this.beamWidth = beamWidth;
	}

	/**
	 * @return the vBeamWidthvBeamWidth
	 */
	@Column(name = "V_BEAM_WIDTH")
	public Float getvBeamWidth() {
		return vBeamWidth;
	}

	/**
	 * @param vBeamWidth
	 *            the vBeamWidth to set
	 */
	public void setvBeamWidth(Float vBeamWidth) {
		this.vBeamWidth = vBeamWidth;
	}

	/**
	 * @return the antHighantHigh
	 */
	@Column(name = "ANT_HIGH")
	public Float getAntHigh() {
		return antHigh;
	}

	/**
	 * @param antHigh
	 *            the antHigh to set
	 */
	public void setAntHigh(Float antHigh) {
		this.antHigh = antHigh;
	}

	/**
	 * @return the racrac
	 */
	@Column(name = "RAC")
	public String getRac() {
		return rac;
	}

	/**
	 * @param rac
	 *            the rac to set
	 */
	public void setRac(String rac) {
		this.rac = rac;
	}

	/**
	 * @return the laclac
	 */
	@Column(name = "LAC")
	public Long getLac() {
		return lac;
	}

	/**
	 * @param lac
	 *            the lac to set
	 */
	public void setLac(Long lac) {
		this.lac = lac;
	}

	/**
	 * @return the cici
	 */
	@Column(name = "CI")
	public Long getCi() {
		return ci;
	}

	/**
	 * @param ci
	 *            the ci to set
	 */
	public void setCi(Long ci) {
		this.ci = ci;
	}

	/**
	 * @return the bcchbcch
	 */
	@Column(name = "BCCH")
	public Long getBcch() {
		return bcch;
	}

	/**
	 * @param bcch
	 *            the bcch to set
	 */
	public void setBcch(Long bcch) {
		this.bcch = bcch;
	}

	/**
	 * @return the bsicbsic
	 */
	@Column(name = "BSIC")
	public Long getBsic() {
		return bsic;
	}

	/**
	 * @param bsic
	 *            the bsic to set
	 */
	public void setBsic(Long bsic) {
		this.bsic = bsic;
	}

	/**
	 * @return the nccncc
	 */
	@Column(name = "NCC")
	public Integer getNcc() {
		return ncc;
	}

	/**
	 * @param ncc
	 *            the ncc to set
	 */
	public void setNcc(Integer ncc) {
		this.ncc = ncc;
	}

	/**
	 * @return the bccbcc
	 */
	@Column(name = "BCC")
	public Integer getBcc() {
		return bcc;
	}

	/**
	 * @param bcc
	 *            the bcc to set
	 */
	public void setBcc(Integer bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return the dcsdcs
	 */
	@Column(name = "DCS")
	public Integer getDcs() {
		return dcs;
	}

	/**
	 * @param dcs
	 *            the dcs to set
	 */
	public void setDcs(Integer dcs) {
		this.dcs = dcs;
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

}
