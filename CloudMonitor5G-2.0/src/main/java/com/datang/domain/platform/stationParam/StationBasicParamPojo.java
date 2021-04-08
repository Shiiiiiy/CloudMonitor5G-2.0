package com.datang.domain.platform.stationParam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.domain.oppositeOpen3d.OppositeOpen3dPerformanceReceivePojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;

import lombok.Data;

/**
 * 站点规划参数和基站工程参数合并的实体类
 * @author lucheng
 *
 */
@Entity
@Table(name = "IADS_STATION_BASIC_PARAM")
public class StationBasicParamPojo {

	private Long id;
	
	private String region;

	/**
	 * 规划工参id
	 */
	private Long cellParamId;
	
	private String cellName;
	
	private String siteName;
	
	private String localCellId;
	
	//站点规划参数
	private String gnbId;
	
	private Float lon;
	
	private Float lat;
	
	/**
	 * high 高度/海拔
	 */
	private Integer height;
	
	/**
	 * Azimuth 方向角/调整前方位角
	 */
	private Integer azimuth;
	
	/**
	 * tilt_m  机械下倾角（度）
	 */
	private Integer tiltM;
	/**
	 * tilt_e  电子下倾角（度）
	 */
	private Integer tiltE;
	
	/**
	 * tilt_e + tilt_m  
	 */
	private Integer tiltTotal;
	
	/**
	 * AAU型号
	 */
	private String  aauModel;
	
	/**
	 * tac
	 */
	private Integer tac;
	
	/**
	 * 天线厂家
	 */
	private String  antennaManufacturer;
	
	//下面是：基站工程参数
	/**
	 * 基站勘察数据表的海拔/站高
	 */
	private Integer  adjustHeight;
	
	/**
	 * 基站勘察数据表的小区天馈方位角/调整后方位角
	 */
	private Integer  adjustAzimuth;

	/**
	 * 基站勘察数据表的Tilt E/调整后电子下倾角/电子俯仰角
	 */
	private Integer  adjustTiltE;
	
	/**
	 * 基站勘察数据表的小区天馈俯仰角/调整后机械下倾角/机械俯仰角
	 */
	private Integer  adjustTiltM;
	
	/**
	 * adjustTiltE + adjustTiltM / 调整后总下倾角（度）
	 */
	private Integer  adjustTiltToatal;
	
	/**
	 *  基站勘察数据表的AAU型号
	 */
	private String  adjustAauModel;
	
	/**
	 *  基站勘察数据表的地址
	 */
	private String  adjustAddress;
	
	/**
	 *  基站勘察数据表的天线类型
	 */
	private String  adjustAntennaType;
	
	/**
	 *  基站勘察数据表的经度
	 */
	private Float  adjustLon;
	
	/**
	 *  基站勘察数据表的纬度
	 */
	private Float  adjustLat;
	
	/**
	 *  保存某个小区的gnodebId，用来与其他同站小区的gnodebId对比
	 */
	private String  adjustGnbId;
	
	/**
	 * 基站勘察数据表的站高对比结果
	 */
	private String  adjustHeightContrast;
	
	/**
	 *方位角对比结果
	 */
	private String  adjustAzimuthContrast;

	/**
	 * Tilt E电子下倾角对比结果
	 */
	private String  adjustTiltEContrast;
	
	/**
	 * 小区天馈俯仰角和机械下倾角对比结果
	 */
	private String  adjustTiltMContrast;
	
	/**
	 * 总下倾角对比结果
	 */
	private String  adjustTiltToatalContrast;

	/**
	 * AAU型号对比结果
	 */
	private String  adjustAauModelContrast;
	
	/**
	 * 经度对比结果
	 */
	private String  adjustLonContrast;
	

	private Integer adjustLonContrastNum;
	

	private Integer adjustLonContrastOpposite;
	
	/**
	 * 纬度对比结果
	 */
	private String  adjustLatContrast;
	

	private Integer adjustLatContrastNum;
	

	private Integer adjustLatContrastOpposite;
	
	/**
	 * gNBid对比结果
	 */
	private String  adjustGnBIdContrast;
	
	/**
	 * 经度和纬度计算值 sqrt(abs("71"-"78")*110000+abs("72"-"79")*110000*....)
	 */
	private String  LonLatCompute;
	
	/**
	 * 同一siteName下的不同loacleCellid的站高对比结果再对比判断是否一致
	 */
	private String  heightContrastBySitname81;
	

	private Integer heightContrastBySitnameNum;
	

	private Integer heightBySitnameOpposite;
	
	/**
	 * 同一siteName下的不同loacleCellid的AAU型号对比结果再对比判断是否一致
	 */
	private String  aauModelContrastBySitname;
	

	private Integer aauModelContrastBySitnameNum;
	

	private Integer aauBySitnameOpposite;
	
	/**
	 * 同一siteName下的不同loacleCellid的方位角对比结果再对比判断是否一致
	 */
	private String  azimuthContrastBySitname;
	

	private Integer azimuthContrastBySitnameNum;
	

	private Integer azimuthBySitnameOpposite;
	
	/**
	 * 同一siteName下的不同loacleCellid的Title E对比结果再对比判断是否一致
	 */
	private String  tiltEContrastBySitname;
	

	private Integer tiltEContrastBySitnameNum;
	

	private Integer tiltEContrastBySitnameOpposite;
	
	/**
	 * 同一siteName下的不同loacleCellid的Title M(天馈俯仰角)对比结果再对比判断是否一致
	 */
	private String  tiltMContrastBySitname;
	

	private Integer tiltMContrastBySitnameNum;
	

	private Integer tiltMContrastBySitnameOpposite;
	
	/**
	 * 同一siteName下的不同loacleCellid的站高和工参的站高对比结果
	 */
	private String  heightContrastByParam77;
	
	private Integer heightContrastByParam77Num;
	
	private Integer heightByParam77Opposite;
	
	//建筑物功能
	private String  buildingFunction86;
	
	/**
	 * 单验日志规划工参
	 */
	private PlanParamPojo planParamPojo;
	
	/**
	 * 反开3d日志规划工参
	 */
	private Plan4GParam plan4GParam;
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "REGION")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
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
	
	@Column(name = "GNB_ID")
	public String getGnbId() {
		return gnbId;
	}

	public void setGnbId(String gnbId) {
		this.gnbId = gnbId;
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
	
	@Column(name = "HEIGHT")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
	@Column(name = "AZIMUTH")
	public Integer getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}
	
	@Column(name = "TILT_M")
	public Integer getTiltM() {
		return tiltM;
	}

	public void setTiltM(Integer tiltM) {
		this.tiltM = tiltM;
	}
	
	@Column(name = "TILT_E")
	public Integer getTiltE() {
		return tiltE;
	}

	public void setTiltE(Integer tiltE) {
		this.tiltE = tiltE;
	}
	
	@Column(name = "TILT_TOTAL")
	public Integer getTiltTotal() {
		return tiltTotal;
	}

	public void setTiltTotal(Integer tiltTotal) {
		this.tiltTotal = tiltTotal;
	}
	
	@Column(name = "AAU_MODEL")
	public String getAauModel() {
		return aauModel;
	}

	public void setAauModel(String aauModel) {
		this.aauModel = aauModel;
	}
	
	@Column(name = "TAC")
	public Integer getTac() {
		return tac;
	}

	public void setTac(Integer tac) {
		this.tac = tac;
	}
	
	@Column(name = "ANTENNA_MANUFACTURER")
	public String getAntennaManufacturer() {
		return antennaManufacturer;
	}

	public void setAntennaManufacturer(String antennaManufacturer) {
		this.antennaManufacturer = antennaManufacturer;
	}

	@Column(name = "ADJUST_HEIGHT")
	public Integer getAdjustHeight() {
		return adjustHeight;
	}

	public void setAdjustHeight(Integer adjustHeight) {
		this.adjustHeight = adjustHeight;
	}

	@Column(name = "ADJUST_AZIMUTH")
	public Integer getAdjustAzimuth() {
		return adjustAzimuth;
	}

	public void setAdjustAzimuth(Integer adjustAzimuth) {
		this.adjustAzimuth = adjustAzimuth;
	}

	@Column(name = "ADJUST_TILTE")
	public Integer getAdjustTiltE() {
		return adjustTiltE;
	}

	public void setAdjustTiltE(Integer adjustTiltE) {
		this.adjustTiltE = adjustTiltE;
	}

	@Column(name = "ADJUST_TILTM")
	public Integer getAdjustTiltM() {
		return adjustTiltM;
	}

	public void setAdjustTiltM(Integer adjustTiltM) {
		this.adjustTiltM = adjustTiltM;
	}

	@Column(name = "ADJUST_TILT_TOTAL")
	public Integer getAdjustTiltToatal() {
		return adjustTiltToatal;
	}

	public void setAdjustTiltToatal(Integer adjustTiltToatal) {
		this.adjustTiltToatal = adjustTiltToatal;
	}

	@Column(name = "ADJUST_AAUMODEL")
	public String getAdjustAauModel() {
		return adjustAauModel;
	}

	public void setAdjustAauModel(String adjustAauModel) {
		this.adjustAauModel = adjustAauModel;
	}

	@Column(name = "ADJUST_ADDRESS")
	public String getAdjustAddress() {
		return adjustAddress;
	}

	public void setAdjustAddress(String adjustAddress) {
		this.adjustAddress = adjustAddress;
	}

	@Column(name = "ADJUST_ANTENNA_TYPE")
	public String getAdjustAntennaType() {
		return adjustAntennaType;
	}

	public void setAdjustAntennaType(String adjustAntennaType) {
		this.adjustAntennaType = adjustAntennaType;
	}

	@Column(name = "ADJUST_LON")
	public Float getAdjustLon() {
		return adjustLon;
	}

	public void setAdjustLon(Float adjustLon) {
		this.adjustLon = adjustLon;
	}

	@Column(name = "ADJUST_LAT")
	public Float getAdjustLat() {
		return adjustLat;
	}

	public void setAdjustLat(Float adjustLat) {
		this.adjustLat = adjustLat;
	}

	@Column(name = "ADJUST_GNBID")
	public String getAdjustGnbId() {
		return adjustGnbId;
	}

	public void setAdjustGnbId(String adjustGnbId) {
		this.adjustGnbId = adjustGnbId;
	}

	@Column(name = "ADJUST_HEIGHT_CONTRAST")
	public String getAdjustHeightContrast() {
		return adjustHeightContrast;
	}

	public void setAdjustHeightContrast(String adjustHeightContrast) {
		this.adjustHeightContrast = adjustHeightContrast;
	}

	@Column(name = "ADJUST_AZIMUTH_CONTRAST")
	public String getAdjustAzimuthContrast() {
		return adjustAzimuthContrast;
	}

	public void setAdjustAzimuthContrast(String adjustAzimuthContrast) {
		this.adjustAzimuthContrast = adjustAzimuthContrast;
	}

	@Column(name = "ADJUST_TILTE_CONTRAST")
	public String getAdjustTiltEContrast() {
		return adjustTiltEContrast;
	}

	public void setAdjustTiltEContrast(String adjustTiltEContrast) {
		this.adjustTiltEContrast = adjustTiltEContrast;
	}

	@Column(name = "ADJUST_TILTM_CONTRAST")
	public String getAdjustTiltMContrast() {
		return adjustTiltMContrast;
	}

	public void setAdjustTiltMContrast(String adjustTiltMContrast) {
		this.adjustTiltMContrast = adjustTiltMContrast;
	}

	@Column(name = "ADJUST_TILT_TOTAL_CONTRAST")
	public String getAdjustTiltToatalContrast() {
		return adjustTiltToatalContrast;
	}

	public void setAdjustTiltToatalContrast(String adjustTiltToatalContrast) {
		this.adjustTiltToatalContrast = adjustTiltToatalContrast;
	}

	@Column(name = "ADJUST_AAUMODEL_CONTRAST")
	public String getAdjustAauModelContrast() {
		return adjustAauModelContrast;
	}

	public void setAdjustAauModelContrast(String adjustAauModelContrast) {
		this.adjustAauModelContrast = adjustAauModelContrast;
	}

	@Column(name = "ADJUST_LON_CONTRAST")
	public String getAdjustLonContrast() {
		return adjustLonContrast;
	}

	public void setAdjustLonContrast(String adjustLonContrast) {
		this.adjustLonContrast = adjustLonContrast;
	}

	@Column(name = "ADJUST_LAT_CONTRAST")
	public String getAdjustLatContrast() {
		return adjustLatContrast;
	}

	public void setAdjustLatContrast(String adjustLatContrast) {
		this.adjustLatContrast = adjustLatContrast;
	}

	@Column(name = "ADJUST_GNODEBID_CONTRAST")
	public String getAdjustGnBIdContrast() {
		return adjustGnBIdContrast;
	}

	public void setAdjustGnBIdContrast(String adjustGnBIdContrast) {
		this.adjustGnBIdContrast = adjustGnBIdContrast;
	}

	@Column(name = "LON_LAT_COMPUTE")
	public String getLonLatCompute() {
		return LonLatCompute;
	}

	public void setLonLatCompute(String lonLatCompute) {
		LonLatCompute = lonLatCompute;
	}

	@Column(name = "HEIGHT_CONTRAST_BYSITE")
	public String getHeightContrastBySitname81() {
		return heightContrastBySitname81;
	}

	public void setHeightContrastBySitname81(String heightContrastBySitname81) {
		this.heightContrastBySitname81 = heightContrastBySitname81;
	}

	@Column(name = "HEIGHT_CONTRAST_BYPARAM77")
	public String getHeightContrastByParam77() {
		return heightContrastByParam77;
	}

	public void setHeightContrastByParam77(String heightContrastByParam77) {
		this.heightContrastByParam77 = heightContrastByParam77;
	}

	@Column(name = "AAU_CONTRAST_BYSITE")
	public String getAauModelContrastBySitname() {
		return aauModelContrastBySitname;
	}

	public void setAauModelContrastBySitname(String aauModelContrastBySitname) {
		this.aauModelContrastBySitname = aauModelContrastBySitname;
	}

	@Column(name = "AZIMUTH_CONTRAST_BYSITE")
	public String getAzimuthContrastBySitname() {
		return azimuthContrastBySitname;
	}

	public void setAzimuthContrastBySitname(String azimuthContrastBySitname) {
		this.azimuthContrastBySitname = azimuthContrastBySitname;
	}

	@Column(name = "TILTE_CONTRAST_BYSITE")
	public String getTiltEContrastBySitname() {
		return tiltEContrastBySitname;
	}

	public void setTiltEContrastBySitname(String tiltEContrastBySitname) {
		this.tiltEContrastBySitname = tiltEContrastBySitname;
	}

	@Column(name = "TILTM_CONTRAST_BYSITE")
	public String getTiltMContrastBySitname() {
		return tiltMContrastBySitname;
	}

	public void setTiltMContrastBySitname(String tiltMContrastBySitname) {
		this.tiltMContrastBySitname = tiltMContrastBySitname;
	}
	
	/**
	 * @return the buildingFunction86
	 */
	@Column(name = "BUILDING_FUNCTION86")
	public String getBuildingFunction86() {
		return buildingFunction86;
	}

	/**
	 * @param buildingFunction86 the buildingFunction86 to set
	 */
	public void setBuildingFunction86(String buildingFunction86) {
		this.buildingFunction86 = buildingFunction86;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PLAN_PARAM_ID")
	public PlanParamPojo getPlanParamPojo() {
		return planParamPojo;
	}
	
	public void setPlanParamPojo(PlanParamPojo planParamPojo) {
		this.planParamPojo = planParamPojo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARAM_ID_OP3D")
	public Plan4GParam getPlan4GParam() {
		return plan4GParam;
	}

	public void setPlan4GParam(Plan4GParam plan4gParam) {
		plan4GParam = plan4gParam;
	}

	
	
	public void setAdjustLonContrastNum(Integer adjustLonContrastNum) {
		this.adjustLonContrastNum = adjustLonContrastNum;
	}

	public void setAdjustLonContrastOpposite(Integer adjustLonContrastOpposite) {
		this.adjustLonContrastOpposite = adjustLonContrastOpposite;
	}

	public void setAdjustLatContrastNum(Integer adjustLatContrastNum) {
		this.adjustLatContrastNum = adjustLatContrastNum;
	}

	public void setAdjustLatContrastOpposite(Integer adjustLatContrastOpposite) {
		this.adjustLatContrastOpposite = adjustLatContrastOpposite;
	}

	public void setHeightContrastBySitnameNum(Integer heightContrastBySitnameNum) {
		this.heightContrastBySitnameNum = heightContrastBySitnameNum;
	}

	public void setAauModelContrastBySitnameNum(Integer aauModelContrastBySitnameNum) {
		this.aauModelContrastBySitnameNum = aauModelContrastBySitnameNum;
	}

	public void setAzimuthContrastBySitnameNum(Integer azimuthContrastBySitnameNum) {
		this.azimuthContrastBySitnameNum = azimuthContrastBySitnameNum;
	}

	public void setTiltEContrastBySitnameNum(Integer tiltEContrastBySitnameNum) {
		this.tiltEContrastBySitnameNum = tiltEContrastBySitnameNum;
	}

	public void setTiltEContrastBySitnameOpposite(Integer tiltEContrastBySitnameOpposite) {
		this.tiltEContrastBySitnameOpposite = tiltEContrastBySitnameOpposite;
	}

	public void setTiltMContrastBySitnameNum(Integer tiltMContrastBySitnameNum) {
		this.tiltMContrastBySitnameNum = tiltMContrastBySitnameNum;
	}

	public void setTiltMContrastBySitnameOpposite(Integer tiltMContrastBySitnameOpposite) {
		this.tiltMContrastBySitnameOpposite = tiltMContrastBySitnameOpposite;
	}

	public void setHeightContrastByParam77Num(Integer heightContrastByParam77Num) {
		this.heightContrastByParam77Num = heightContrastByParam77Num;
	}

	public void setHeightBySitnameOpposite(Integer heightBySitnameOpposite) {
		this.heightBySitnameOpposite = heightBySitnameOpposite;
	}

	public void setAauBySitnameOpposite(Integer aauBySitnameOpposite) {
		this.aauBySitnameOpposite = aauBySitnameOpposite;
	}

	public void setAzimuthBySitnameOpposite(Integer azimuthBySitnameOpposite) {
		this.azimuthBySitnameOpposite = azimuthBySitnameOpposite;
	}

	public void setHeightByParam77Opposite(Integer heightByParam77Opposite) {
		this.heightByParam77Opposite = heightByParam77Opposite;
	}

	@Transient 
	public Integer getAdjustLonContrastNum() {
		if(adjustLonContrast==null){
			return null;
		}else if(adjustLonContrast.equals("一致")){
			return 1;
		}else if(adjustLonContrast.equals("不一致")){
			return 0;
		}
		return null;
	}

	@Transient 
	public Integer getAdjustLonContrastOpposite() {
		if(adjustLonContrast==null){
			return null;
		}else if(adjustLonContrast.equals("一致")){
			return 0;
		}else if(adjustLonContrast.equals("不一致")){
			return 1;
		}
		return null;
	}

	@Transient 
	public Integer getAdjustLatContrastNum() {
		if(adjustLatContrast==null){
			return null;
		}else if(adjustLatContrast.equals("一致")){
			return 1;
		}else if(adjustLatContrast.equals("不一致")){
			return 0;
		}
		return null;
	}

	@Transient 
	public Integer getAdjustLatContrastOpposite() {
		if(adjustLatContrast==null){
			return null;
		}else if(adjustLatContrast.equals("一致")){
			return 0;
		}else if(adjustLatContrast.equals("不一致")){
			return 1;
		}
		return null;
	}

	@Transient 
	public Integer getHeightContrastBySitnameNum() {
		if(heightContrastBySitname81==null){
			return null;
		}else if(heightContrastBySitname81.equals("一致")){
			return 1;
		}else if(heightContrastBySitname81.equals("不一致")){
			return 0;
		}
		return null;
	}

	@Transient 
	public Integer getHeightBySitnameOpposite() {
		if(heightContrastBySitname81==null){
			return null;
		}else if(heightContrastBySitname81.equals("一致")){
			return 0;
		}else if(heightContrastBySitname81.equals("不一致")){
			return 1;
		}
		return null;
	}

	@Transient 
	public Integer getAauModelContrastBySitnameNum() {
		if(aauModelContrastBySitname==null){
			return null;
		}else if(aauModelContrastBySitname.equals("一致")){
			return 1;
		}else if(aauModelContrastBySitname.equals("不一致")){
			return 0;
		}
		return null;
	}

	@Transient 
	public Integer getAauBySitnameOpposite() {
		if(aauModelContrastBySitname==null){
			return null;
		}else if(aauModelContrastBySitname.equals("一致")){
			return 0;
		}else if(aauModelContrastBySitname.equals("不一致")){
			return 1;
		}
		return null;
	}

	@Transient 
	public Integer getAzimuthContrastBySitnameNum() {
		if(azimuthContrastBySitname==null){
			return null;
		}else if(azimuthContrastBySitname.equals("一致")){
			return 1;
		}else if(azimuthContrastBySitname.equals("不一致")){
			return 0;
		}
		return null;
	}

	@Transient 
	public Integer getAzimuthBySitnameOpposite() {
		if(azimuthContrastBySitname==null){
			return null;
		}else if(azimuthContrastBySitname.equals("一致")){
			return 0;
		}else if(azimuthContrastBySitname.equals("不一致")){
			return 1;
		}
		return null;
	}

	@Transient 
	public Integer getTiltEContrastBySitnameNum() {
		if(tiltEContrastBySitname==null){
			return null;
		}else if(tiltEContrastBySitname.equals("一致")){
			return 1;
		}else if(tiltEContrastBySitname.equals("不一致")){
			return 0;
		}
		return null;
	}

	@Transient 
	public Integer getTiltEContrastBySitnameOpposite() {
		if(tiltEContrastBySitname==null){
			return null;
		}else if(tiltEContrastBySitname.equals("一致")){
			return 0;
		}else if(tiltEContrastBySitname.equals("不一致")){
			return 1;
		}
		return null;
	}

	@Transient 
	public Integer getTiltMContrastBySitnameNum() {
		if(tiltMContrastBySitname==null){
			return null;
		}else if(tiltMContrastBySitname.equals("一致")){
			return 1;
		}else if(tiltMContrastBySitname.equals("不一致")){
			return 0;
		}
		return null;
	}

	@Transient 
	public Integer getTiltMContrastBySitnameOpposite() {
		if(tiltMContrastBySitname==null){
			return null;
		}else if(tiltMContrastBySitname.equals("一致")){
			return 0;
		}else if(tiltMContrastBySitname.equals("不一致")){
			return 1;
		}
		return null;
	}

	@Transient 
	public Integer getHeightContrastByParam77Num() {
		if(heightContrastByParam77==null){
			return null;
		}else if(heightContrastByParam77.equals("一致")){
			return 1;
		}else if(heightContrastByParam77.equals("不一致")){
			return 0;
		}
		return null;
	}

	@Transient 
	public Integer getHeightByParam77Opposite() {
		if(heightContrastByParam77==null){
			return null;
		}else if(heightContrastByParam77.equals("一致")){
			return 0;
		}else if(heightContrastByParam77.equals("不一致")){
			return 1;
		}
		return null;
	}

}
