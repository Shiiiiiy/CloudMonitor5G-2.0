/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * 干扰原因问题路段----问题路段小区详情表
 * 
 * @author yinzhipeng
 * @date:2015年9月15日 上午10:17:25
 * @version
 */
@Entity
@Table(name = "IADS_QBR_DIST_PRO_ROAD_CELL")
public class DisturbProblemRoadCellInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8991864245248904795L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(干扰原因问题路段)
	 */
	private VolteQualityBadRoadDisturbProblem volteQualityBadRoadDisturbProblem;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * SINR均值,问题路段中，该服务小区的SINR均值
	 */
	private Float sinrAvg;
	/**
	 * SINR最低值,问题路段中，该服务小区的SINR最低值
	 */
	private Float sinrMin;
	/**
	 * 问题路段该小区为服务小区的区域中，满足重叠覆盖的采样点
	 * 重叠覆盖占比(%),问题路段该小区为服务小区的区域中，满足重叠覆盖的采样点/总覆盖采样点*100%
	 */
	// private Integer overlapCoverNum;
	/**
	 * 重叠覆盖占比(%)
	 */
	private Float overlapCoverRatio;
	/**
	 * 持续距离(m),该服务小区与各采样点问题路段的平均距离
	 */
	private Float distanceAvg;
	/**
	 * 测试时间(s),以该小区为服务小区的时长
	 */
	private Float testTime;

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
	 * @return the sinrAvgsinrAvg
	 */
	@Column(name = "SINR_AVG")
	public Float getSinrAvg() {
		return sinrAvg;
	}

	/**
	 * @param sinrAvg
	 *            the sinrAvg to set
	 */
	public void setSinrAvg(Float sinrAvg) {
		this.sinrAvg = sinrAvg;
	}

	/**
	 * @return the sinrMinsinrMin
	 */
	@Column(name = "SINR_MIN")
	public Float getSinrMin() {
		return sinrMin;
	}

	/**
	 * @param sinrMin
	 *            the sinrMin to set
	 */
	public void setSinrMin(Float sinrMin) {
		this.sinrMin = sinrMin;
	}

	/**
	 * @return the overlapCoverNumoverlapCoverNum
	 */
	// public Integer getOverlapCoverNum() {
	// return overlapCoverNum;
	// }

	/**
	 * @param overlapCoverNum
	 *            the overlapCoverNum to set
	 */
	// public void setOverlapCoverNum(Integer overlapCoverNum) {
	// this.overlapCoverNum = overlapCoverNum;
	// }

	/**
	 * @return the distanceAvgdistanceAvg
	 */
	@Column(name = "DISTANCE_AVG")
	public Float getDistanceAvg() {
		return distanceAvg;
	}

	/**
	 * @return the overCoverRatiooverCoverRatio
	 */
	@Column(name = "OVERLAP_COVER_RATIO")
	public Float getOverlapCoverRatio() {
		return overlapCoverRatio;
	}

	/**
	 * @param overCoverRatio
	 *            the overCoverRatio to set
	 */
	public void setOverlapCoverRatio(Float overlapCoverRatio) {
		this.overlapCoverRatio = overlapCoverRatio;
	}

	/**
	 * @param distanceAvg
	 *            the distanceAvg to set
	 */
	public void setDistanceAvg(Float distanceAvg) {
		this.distanceAvg = distanceAvg;
	}

	/**
	 * @return the testTimetestTime
	 */
	@Column(name = "TEST_TIME")
	public Float getTestTime() {
		return testTime;
	}

	/**
	 * @param testTime
	 *            the testTime to set
	 */
	public void setTestTime(Float testTime) {
		this.testTime = testTime;
	}

	/**
	 * @return the
	 *         volteQualityBadRoadDisturbProblemvolteQualityBadRoadDisturbProblem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteQualityBadRoadDisturbProblem getVolteQualityBadRoadDisturbProblem() {
		return volteQualityBadRoadDisturbProblem;
	}

	/**
	 * @param volteQualityBadRoadDisturbProblem
	 *            the volteQualityBadRoadDisturbProblem to set
	 */
	public void setVolteQualityBadRoadDisturbProblem(
			VolteQualityBadRoadDisturbProblem volteQualityBadRoadDisturbProblem) {
		this.volteQualityBadRoadDisturbProblem = volteQualityBadRoadDisturbProblem;
	}

}
