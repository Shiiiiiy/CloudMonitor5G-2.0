package com.datang.domain.embbCover;

/**
 * 覆盖分析所有字段
 * @author maxuancheng
 *
 */
public class CoverAnalyseAllFiledPojo extends LteWeakCoverAnalysePojo{

	/**
	 * @author maxuancheng
	 * date:2020年5月11日 下午2:18:39
	 */
	private static final long serialVersionUID = 1L;
	
	private String cellId;
	
	private String tac;
	
	/**
	 * 天馈下倾角
	 */
	private Integer downtilt;

	/**
	 * 天馈方位角
	 */
	private Integer azimuth;
	
	/**
	 * 采样点占比
	 */
	private Float pointRatio;
	
	/**
	 * 采样点总数
	 */
	private Integer pointNumberSum;
	
	/**
	 * 总里程
	 */
	private Integer distanceSum;
	
	/**
	 * 站间距
	 */
	private Integer stationDistance;
	
	/**
	 * 测试时长
	 */
	private Integer durationSum;
	
	/**
	 * 覆盖采样点到站点距离
	 */
	private Integer pointToStationSpace;
	
	/**
	 * 覆盖采样点RSRP均值（dBm）
	 */
	private Integer rsrpAverage;
	
	/**
	 * 覆盖电平/最强beam rsrp均值（dBm）
	 */
	private Integer rrsrpAverage;
	
	/**
	 * 覆盖采样点最强邻区RSRP均值（dBm）
	 */
	private Integer rsrpBestAverage;
	
	/**
	 * 覆盖采样点最强邻区RSRP均值（dBm）
	 */
	private Float sinrAverage;
	
	/**
	 * 覆盖采样点最强邻区ibler均值（dBm）
	 */
	private Float iblerAverage;
	

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getTac() {
		return tac;
	}

	public void setTac(String tac) {
		this.tac = tac;
	}

	public Integer getDowntilt() {
		return downtilt;
	}

	public void setDowntilt(Integer downtilt) {
		this.downtilt = downtilt;
	}

	public Integer getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}

	public Float getPointRatio() {
		return pointRatio;
	}

	public void setPointRatio(Float pointRatio) {
		this.pointRatio = pointRatio;
	}

	public Integer getDistanceSum() {
		return distanceSum;
	}

	public void setDistanceSum(Integer distanceSum) {
		this.distanceSum = distanceSum;
	}

	public Integer getDurationSum() {
		return durationSum;
	}

	public void setDurationSum(Integer durationSum) {
		this.durationSum = durationSum;
	}

	public Integer getPointToStationSpace() {
		return pointToStationSpace;
	}

	public void setPointToStationSpace(Integer pointToStationSpace) {
		this.pointToStationSpace = pointToStationSpace;
	}

	public Integer getRsrpAverage() {
		return rsrpAverage;
	}

	public void setRsrpAverage(Integer rsrpAverage) {
		this.rsrpAverage = rsrpAverage;
	}

	public Integer getRrsrpAverage() {
		return rrsrpAverage;
	}

	public void setRrsrpAverage(Integer rrsrpAverage) {
		this.rrsrpAverage = rrsrpAverage;
	}

	public Integer getRsrpBestAverage() {
		return rsrpBestAverage;
	}

	public void setRsrpBestAverage(Integer rsrpBestAverage) {
		this.rsrpBestAverage = rsrpBestAverage;
	}

	public Float getSinrAverage() {
		return sinrAverage;
	}

	public void setSinrAverage(Float sinrAverage) {
		this.sinrAverage = sinrAverage;
	}

	public Float getIblerAverage() {
		return iblerAverage;
	}

	public void setIblerAverage(Float iblerAverage) {
		this.iblerAverage = iblerAverage;
	}

	public Integer getPointNumberSum() {
		return pointNumberSum;
	}

	public void setPointNumberSum(Integer pointNumberSum) {
		this.pointNumberSum = pointNumberSum;
	}

	public Integer getStationDistance() {
		return stationDistance;
	}

	public void setStationDistance(Integer stationDistance) {
		this.stationDistance = stationDistance;
	}
}
