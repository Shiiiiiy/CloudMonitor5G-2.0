package com.datang.domain.stream.streamVideoQualitybad;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.testLogItem.TestLogItem;
import com.datang.util.DateUtil;
import com.datang.util.NumberFormatUtils;

/**
 * 流媒体专题----流媒体视频质差,视频质差公共指标
 * 
 * @explain
 * @name StreamVideoQualityBad
 * @author shenyanwei
 * @date 2017年10月20日下午2:00:18
 */
@Entity
@Table(name = "STREAM_BQ")
@Inheritance(strategy = InheritanceType.JOINED)
public class StreamQualityBad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5479171449571318877L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 测试日志
	 */
	private TestLogItem testLogItem;
	/**
	 * 路段名称
	 */
	private String m_stRoadName;
	/**
	 * 问题路段名称,根据以下3对经纬度信息获取
	 */
	private Float beginLatitude;
	private Float courseLatitude;
	private Float endLatitude;
	private Float beginLongitude;
	private Float courseLongitude;
	private Float endLongitude;
	/**
	 * 持续距离
	 */
	private Float distance;
	/**
	 * 关键参数原因
	 */
	private Integer critialCause;
	/**
	 * 涉及小区个数
	 */
	private Integer cellNum;
	/**
	 * VMOS采样点个数
	 */
	private Integer vmosNumber;
	/**
	 * VMOS总值
	 */
	private Float vmosSum;
	/**
	 * 卡顿比例采样点个数
	 */
	private Integer stallingratioNumber;
	/**
	 * 卡顿比例总值
	 */
	private Float stallingratioSum;
	/**
	 * 初始缓冲时延采样点个数
	 */
	private Integer initialbuffertimeNumber;
	/**
	 * 初始缓冲时延总值
	 */
	private Float initialbuffertimeSum;
	/**
	 * 分辨率采样点个数
	 */
	private Integer videoresolutionNumber;
	/**
	 * 分辨率总值
	 */
	private Float videoresolutionSum;
	/**
	 * 
	 * 视频全程感知速率采样点个数
	 */
	private Integer videobitrateNumber;
	/**
	 * 视频全程感知速率总值
	 */
	private Float videobitrateSum;
	/**
	 * SINR总值
	 */
	private Float sinrSum;
	/**
	 * RSRP总值
	 */
	private Float rsrpSum;
	/**
	 * RSRP以及SINR采样点个数
	 */
	private Integer rsrporsinrNumber;
	/**
	 * SINR最小值
	 */
	private Float sinrMin;
	/**
	 * RSRP最小值
	 */
	private Float rsrpMin;
	/**
	 * 开始时间
	 */
	private Long startTime;
	/**
	 * 结束时间
	 */
	private Long endTime;
	/**
	 * 优化建议
	 */
	private String optimization;

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
	 * @return the optimization
	 */
	@Column(name = "OPTIMIZATION")
	public String getOptimization() {
		return optimization;
	}

	/**
	 * @param the
	 *            optimization to set
	 */

	public void setOptimization(String optimization) {
		this.optimization = optimization;
	}

	/**
	 * @return the testLogItemtestLogItem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO", nullable = false, referencedColumnName = "RECSEQNO")
	@JSON(serialize = false)
	public TestLogItem getTestLogItem() {
		return testLogItem;
	}

	/**
	 * @param testLogItem
	 *            the testLogItem to set
	 */
	public void setTestLogItem(TestLogItem testLogItem) {
		this.testLogItem = testLogItem;
	}

	/**
	 * @return the m_stRoadName
	 */
	@Column(name = "ROAD_NAME")
	public String getM_stRoadName() {
		return m_stRoadName;
	}

	/**
	 * @param the
	 *            m_stRoadName to set
	 */

	public void setM_stRoadName(String m_stRoadName) {
		this.m_stRoadName = m_stRoadName;
	}

	/**
	 * @return the beginLatitudebeginLatitude
	 */
	@Column(name = "BEGIN_LATITUDE")
	public Float getBeginLatitude() {
		return beginLatitude;
	}

	/**
	 * @param beginLatitude
	 *            the beginLatitude to set
	 */
	public void setBeginLatitude(Float beginLatitude) {
		this.beginLatitude = beginLatitude;
	}

	/**
	 * @return the courseLatitudecourseLatitude
	 */
	@Column(name = "COURSE_LATITUDE")
	public Float getCourseLatitude() {
		return courseLatitude;
	}

	/**
	 * @param courseLatitude
	 *            the courseLatitude to set
	 */
	public void setCourseLatitude(Float courseLatitude) {
		this.courseLatitude = courseLatitude;
	}

	/**
	 * @return the endLatitudeendLatitude
	 */
	@Column(name = "END_LATITUDE")
	public Float getEndLatitude() {
		return endLatitude;
	}

	/**
	 * @param endLatitude
	 *            the endLatitude to set
	 */
	public void setEndLatitude(Float endLatitude) {
		this.endLatitude = endLatitude;
	}

	/**
	 * @return the beginLongitudebeginLongitude
	 */
	@Column(name = "BEGIN_LONGITUDE")
	public Float getBeginLongitude() {
		return beginLongitude;
	}

	/**
	 * @param beginLongitude
	 *            the beginLongitude to set
	 */
	public void setBeginLongitude(Float beginLongitude) {
		this.beginLongitude = beginLongitude;
	}

	/**
	 * @return the courseLongitudecourseLongitude
	 */
	@Column(name = "COURSE_LONGITUDE")
	public Float getCourseLongitude() {
		return courseLongitude;
	}

	/**
	 * @param courseLongitude
	 *            the courseLongitude to set
	 */
	public void setCourseLongitude(Float courseLongitude) {
		this.courseLongitude = courseLongitude;
	}

	/**
	 * @return the endLongitudeendLongitude
	 */
	@Column(name = "END_LONGITUDE")
	public Float getEndLongitude() {
		return endLongitude;
	}

	/**
	 * @param endLongitude
	 *            the endLongitude to set
	 */
	public void setEndLongitude(Float endLongitude) {
		this.endLongitude = endLongitude;
	}

	/**
	 * @return the distance
	 */
	@Column(name = "DISTANCE")
	public Float getDistance() {
		return distance;
	}

	/**
	 * @param the
	 *            distance to set
	 */

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	/**
	 * @return the critialCause
	 */
	@Column(name = "CRITIAL_CAUSE")
	public Integer getCritialCause() {
		return critialCause;
	}

	/**
	 * @param the
	 *            critialCause to set
	 */

	public void setCritialCause(Integer critialCause) {
		this.critialCause = critialCause;
	}

	/**
	 * @return the vmosNumber
	 */
	@Column(name = "VMOS_NUM")
	public Integer getVmosNumber() {
		return vmosNumber;
	}

	/**
	 * @param the
	 *            vmosNumber to set
	 */

	public void setVmosNumber(Integer vmosNumber) {
		this.vmosNumber = vmosNumber;
	}

	/**
	 * @return the vmosSum
	 */
	@Column(name = "VMOS_SUM")
	public Float getVmosSum() {
		return vmosSum;
	}

	/**
	 * @param the
	 *            vmosSum to set
	 */

	public void setVmosSum(Float vmosSum) {
		this.vmosSum = vmosSum;
	}

	/**
	 * @return the stallingratioNumber
	 */
	@Column(name = "STALLINGRATIO_NUM")
	public Integer getStallingratioNumber() {
		return stallingratioNumber;
	}

	/**
	 * @param the
	 *            stallingratioNumber to set
	 */

	public void setStallingratioNumber(Integer stallingratioNumber) {
		this.stallingratioNumber = stallingratioNumber;
	}

	/**
	 * @return the stallingratioSum
	 */
	@Column(name = "STALLINGRATIO_SUM")
	public Float getStallingratioSum() {
		return stallingratioSum;
	}

	/**
	 * @param the
	 *            stallingratioSum to set
	 */

	public void setStallingratioSum(Float stallingratioSum) {
		this.stallingratioSum = stallingratioSum;
	}

	/**
	 * @return the initialbuffertimeNumber
	 */
	@Column(name = "INITIALBUFFERTIME_NUM")
	public Integer getInitialbuffertimeNumber() {
		return initialbuffertimeNumber;
	}

	/**
	 * @param the
	 *            initialbuffertimeNumber to set
	 */

	public void setInitialbuffertimeNumber(Integer initialbuffertimeNumber) {
		this.initialbuffertimeNumber = initialbuffertimeNumber;
	}

	/**
	 * @return the initialbuffertimeSum
	 */
	@Column(name = "INITIALBUFFERTIME_SUM")
	public Float getInitialbuffertimeSum() {
		return initialbuffertimeSum;
	}

	/**
	 * @param the
	 *            initialbuffertimeSum to set
	 */

	public void setInitialbuffertimeSum(Float initialbuffertimeSum) {
		this.initialbuffertimeSum = initialbuffertimeSum;
	}

	/**
	 * @return the videoresolutionNumber
	 */
	@Column(name = "VIDEORESOLUTION_NUM")
	public Integer getVideoresolutionNumber() {
		return videoresolutionNumber;
	}

	/**
	 * @param the
	 *            videoresolutionNumber to set
	 */

	public void setVideoresolutionNumber(Integer videoresolutionNumber) {
		this.videoresolutionNumber = videoresolutionNumber;
	}

	/**
	 * @return the videoresolutionSum
	 */
	@Column(name = "VIDEORESOLUTION_SUM")
	public Float getVideoresolutionSum() {
		return videoresolutionSum;
	}

	/**
	 * @param the
	 *            videoresolutionSum to set
	 */

	public void setVideoresolutionSum(Float videoresolutionSum) {
		this.videoresolutionSum = videoresolutionSum;
	}

	/**
	 * @return the videobitrateNumber
	 */
	@Column(name = "VIDEOBITRATE_NUM")
	public Integer getVideobitrateNumber() {
		return videobitrateNumber;
	}

	/**
	 * @param the
	 *            videobitrateNumber to set
	 */

	public void setVideobitrateNumber(Integer videobitrateNumber) {
		this.videobitrateNumber = videobitrateNumber;
	}

	/**
	 * @return the Sum
	 */
	@Column(name = "VIDEOBITRATE_SUM")
	public Float getVideobitrateSum() {
		return videobitrateSum;
	}

	/**
	 * @param the
	 *            Sum to set
	 */

	public void setVideobitrateSum(Float videobitratesum) {
		videobitrateSum = videobitratesum;
	}

	/**
	 * @return the sinrSum
	 */
	@Column(name = "SINR_SUM")
	public Float getSinrSum() {
		return sinrSum;
	}

	/**
	 * @param the
	 *            sinrSum to set
	 */

	public void setSinrSum(Float sinrSum) {
		this.sinrSum = sinrSum;
	}

	/**
	 * @return the rsrpSum
	 */
	@Column(name = "RSRP_SUM")
	public Float getRsrpSum() {
		return rsrpSum;
	}

	/**
	 * @param the
	 *            rsrpSum to set
	 */

	public void setRsrpSum(Float rsrpSum) {
		this.rsrpSum = rsrpSum;
	}

	/**
	 * @return the rsrporsinrNumber
	 */
	@Column(name = "RSRPORSINR_NUM")
	public Integer getRsrporsinrNumber() {
		return rsrporsinrNumber;
	}

	/**
	 * @param the
	 *            rsrporsinrNumber to set
	 */

	public void setRsrporsinrNumber(Integer rsrporsinrNumber) {
		this.rsrporsinrNumber = rsrporsinrNumber;
	}

	/**
	 * @return the sinrMin
	 */
	@Column(name = "SINR_MIN")
	public Float getSinrMin() {
		return sinrMin;
	}

	/**
	 * @param the
	 *            sinrMin to set
	 */

	public void setSinrMin(Float sinrMin) {
		this.sinrMin = sinrMin;
	}

	/**
	 * @return the rsrpMin
	 */
	@Column(name = "RSRP_MIN")
	public Float getRsrpMin() {
		return rsrpMin;
	}

	/**
	 * @param the
	 *            rsrpMin to set
	 */

	public void setRsrpMin(Float rsrpMin) {
		this.rsrpMin = rsrpMin;
	}

	/**
	 * @return the startTime
	 */
	@Column(name = "START_TIME")
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * @param the
	 *            startTime to set
	 */

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	@Column(name = "END_TIME")
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * @param the
	 *            endTime to set
	 */

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the cellNum
	 */
	@Column(name = "CELL_NUM")
	public Integer getCellNum() {
		return cellNum;
	}

	/**
	 * @param the
	 *            cellNum to set
	 */

	public void setCellNum(Integer cellNum) {
		this.cellNum = cellNum;
	}

	@Transient
	public String getTimeValue() {
		if (null != startTime) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date(startTime));
		}
		return null;
	}

	@Transient
	public String getEndTimeValue() {
		if (null != endTime) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date(endTime));
		}
		return null;
	}

	@Transient
	public String getTimeInterval() {
		if (null != startTime && null != endTime) {
			return DateUtil.getHHmmssStr(endTime - startTime);
			// 这是什么鬼?
			// return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(
			// startTime - endTime));
		}
		return null;
	}

	@Transient
	public Float getVmosAvg() {
		if (null != vmosSum && null != vmosNumber && 0 != vmosNumber) {
			return NumberFormatUtils.format(vmosSum / vmosNumber * 1f, 4);
		}
		return null;
	}

	@Transient
	public Float getVideobitrateAvg() {
		if (null != videobitrateSum && null != videobitrateNumber
				&& 0 != videobitrateNumber) {
			return NumberFormatUtils.format(videobitrateSum
					/ videobitrateNumber * 1f, 4);
		}
		return null;
	}

	@Transient
	public Float getStallingratioAvg() {
		if (null != stallingratioSum && null != stallingratioNumber
				&& 0 != stallingratioNumber) {
			return NumberFormatUtils.format(stallingratioSum
					/ stallingratioNumber * 1f, 4);
		}
		return null;
	}

	@Transient
	public Float getInitialbuffertimeAvg() {
		if (null != initialbuffertimeSum && null != initialbuffertimeNumber
				&& 0 != initialbuffertimeNumber) {
			return NumberFormatUtils.format(initialbuffertimeSum
					/ initialbuffertimeNumber * 1f, 4);
		}
		return null;
	}

	@Transient
	public Float getVideoresolutionAvg() {
		if (null != videoresolutionSum && null != videoresolutionNumber
				&& 0 != videoresolutionNumber) {
			return NumberFormatUtils.format(videoresolutionSum
					/ videoresolutionNumber * 1f, 4);
		}
		return null;
	}

	@Transient
	public Float getRsrpAvg() {
		if (null != rsrpSum && null != rsrporsinrNumber
				&& 0 != rsrporsinrNumber) {
			return NumberFormatUtils.format(rsrpSum / rsrporsinrNumber * 1f, 4);
		}
		return null;
	}

	@Transient
	public Float getSinrAvg() {
		if (null != sinrSum && null != rsrporsinrNumber
				&& 0 != rsrporsinrNumber) {
			return NumberFormatUtils.format(sinrSum / rsrporsinrNumber * 1f, 4);
		}
		return null;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StreamQualityBad [id=" + id + ", testLogItem=" + testLogItem
				+ ", m_stRoadName=" + m_stRoadName + ", beginLatitude="
				+ beginLatitude + ", courseLatitude=" + courseLatitude
				+ ", endLatitude=" + endLatitude + ", beginLongitude="
				+ beginLongitude + ", courseLongitude=" + courseLongitude
				+ ", endLongitude=" + endLongitude + ", distance=" + distance
				+ ", critialCause=" + critialCause + ", cellNum=" + cellNum
				+ ", vmosNumber=" + vmosNumber + ", vmosSum=" + vmosSum
				+ ", stallingratioNumber=" + stallingratioNumber
				+ ", stallingratioSum=" + stallingratioSum
				+ ", initialbuffertimeNumber=" + initialbuffertimeNumber
				+ ", initialbuffertimeSum=" + initialbuffertimeSum
				+ ", videoresolutionNumber=" + videoresolutionNumber
				+ ", videoresolutionSum=" + videoresolutionSum
				+ ", videobitrateNumber=" + videobitrateNumber
				+ ", videobitrateSum=" + videobitrateSum + ", sinrSum="
				+ sinrSum + ", rsrpSum=" + rsrpSum + ", rsrporsinrNumber="
				+ rsrporsinrNumber + ", sinrMin=" + sinrMin + ", rsrpMin="
				+ rsrpMin + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", optimization=" + optimization + "]";
	}

}
