package com.datang.domain.domain5g.qualityBad5g;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.domain.testLogItem.TestLogItem;

/**
 * 干扰路段列表实体类
 * @author maxuancheng
 * @date 2019年3月14日
 */
@Entity
@Table(name = "IADS_QUALITY_5G_INTERFERE_ROAD")
public class InterfereRoadPojo  implements Serializable{
	/**
	 * @author maxuancheng
	 */
	private static final long serialVersionUID = 1L;
	
	private Long rid;
	
	@Id
	@Column(name = "RID")
	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	/**
	 * 测试日志
	 */
	private TestLogItem testLogItem;
	/**
	 * 道路名称
	 */
	private String  roadName;
	/**
	 * 开始时间
	 */
	private Long startTime	;
	/**
	 * 测试时长(S)
	 */
	private Long testDuration	;
	/**
	 * 持续距离(M)
	 */
	private Double continuedDistance ;
	/**
	 * RSRP均值(DBM)
	 */
	private Double rsrpMean;
	/**
	 * RSRP最低值(DBM)
	 */
	private Double rsrpLow;
	/**
	 * SINR均值
	 */
	private Double  sinrMean	;
	/**
	 * SINR最低值
	 */
	private Double sinrLow;
	/**
	 * 过覆盖占比(%)
	 */
	private Double highCoverRatio;
	/**
	 * 重叠覆盖占比(%)
	 */
	private Double overlayCoverRatio ;
	/**
	 * 优化建议
	 */
	private String optimizeSuggest;
	
	/**
	 * @return the testLogItemtestLogItem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO", nullable = false, referencedColumnName = "RECSEQNO")
	public TestLogItem getTestLogItem() {
		return testLogItem;
	}
	
	public void setTestLogItem(TestLogItem testLogItem) {
		this.testLogItem = testLogItem;
	}
	
	@Column(name = "ROAD_NAME")
	public String getRoadName() {
		return roadName;
	}
	
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	
	@Transient
	public String getStartTimeString() {
		if (null != startTime) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
					startTime));
		}
		return null;
	}
	
	@Column(name = "START_TIME")
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	@Column(name = "TEST_DURATION")
	public Long getTestDuration() {
		return testDuration;
	}
	
	public void setTestDuration(Long testDuration) {
		this.testDuration = testDuration;
	}

	@Column(name = "CONTINUED_DISTANCE")
	public Double getContinuedDistance() {
		return continuedDistance;
	}
	
	public void setContinuedDistance(Double continuedDistance) {
		this.continuedDistance = continuedDistance;
	}

	@Column(name = "RSRP_MEAN")
	public Double getRsrpMean() {
		return rsrpMean;
	}
	
	public void setRsrpMean(Double rsrpMean) {
		this.rsrpMean = rsrpMean;
	}

	@Column(name = "RSRP_LOW")
	public Double getRsrpLow() {
		return rsrpLow;
	}
	
	public void setRsrpLow(Double rsrpLow) {
		this.rsrpLow = rsrpLow;
	}

	@Column(name = "SINR_MEAN")
	public Double getSinrMean() {
		return sinrMean;
	}
	
	public void setSinrMean(Double sinrMean) {
		this.sinrMean = sinrMean;
	}

	@Column(name = "SINR_LOW")
	public Double getSinrLow() {
		return sinrLow;
	}
	
	public void setSinrLow(Double sinrLow) {
		this.sinrLow = sinrLow;
	}

	@Column(name = "HIGH_COVER_RATIO")
	public Double getHighCoverRatio() {
		return highCoverRatio;
	}
	
	public void setHighCoverRatio(Double highCoverRatio) {
		this.highCoverRatio = highCoverRatio;
	}

	@Column(name = "OVERLAY_COVER_RATIO")
	public Double getOverlayCoverRatio() {
		return overlayCoverRatio;
	}
	
	public void setOverlayCoverRatio(Double overlayCoverRatio) {
		this.overlayCoverRatio = overlayCoverRatio;
	}

	@Column(name = "OPTIMIZE_SUGGEST")
	public String getOptimizeSuggest() {
		return optimizeSuggest;
	}
	
	public void setOptimizeSuggest(String optimizeSuggest) {
		this.optimizeSuggest = optimizeSuggest;
	}
}