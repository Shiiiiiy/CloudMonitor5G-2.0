/**
 * 
 */
package com.datang.domain.VoLTEDissertation.handoffDropping;

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

import com.datang.domain.testLogItem.TestLogItem;

/**
 * VoLTE专题----切换失败父类
 * 
 * @author shenyanwei
 * @date 2016年4月20日上午11:12:19
 */

@Entity
@Table(name = "IADS_DISS_VOLTE_DROPPING")
@Inheritance(strategy = InheritanceType.JOINED)
public class VolteDropping implements Serializable {

	/**
	 * 序列化编号
	 */
	private static final long serialVersionUID = -3943621592840457239L;
	/**
	 * id
	 */
	private Long id;
	/**
	 * 测试日志
	 */
	private TestLogItem testLogItem;
	/**
	 * 切换失败开始时间戳
	 */
	private Long startTime;
	/**
	 * 切换失败结束时间戳
	 */
	private Long endTime;
	/**
	 * 问题路段名称,根据以下3对经纬度信息获取
	 */
	private String m_stRoadName;
	private Float beginLatitude;
	private Float courseLatitude;
	private Float endLatitude;
	private Float beginLongitude;
	private Float courseLongitude;
	private Float endLongitude;

	/**
	 * 涉及小区占比:切换失败所在的服务小区的小区个数/所选测试日志作为服务小区的小区个数*100%<br>
	 * cellid用于区分 :切换失败服务小区的小区个数
	 */
	private Long cellId;
	/**
	 * 目标小区的区分标识
	 */
	private Long failId;
	/**
	 * 切换失败时间
	 */
	private Long failTime;

	/**
	 * 源服务小区友好名
	 */
	private String cellName;
	/**
	 * 目标小区友好名
	 */
	private String failName;

	/**
	 * 日志类型,0原始日志,2对比日志
	 */
	private Integer testLogType;

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
	 * @return the testLogItemtestLogItem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO", nullable = false, referencedColumnName = "RECSEQNO")
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
	 * @return the m_stRoadNamem_stRoadName
	 */
	@Column(name = "ROAD_NAME")
	public String getM_stRoadName() {
		return m_stRoadName;
	}

	/**
	 * @param m_stRoadName
	 *            the m_stRoadName to set
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
	 * @return the failName
	 */
	@Column(name = "FAIL_NAME")
	public String getFailName() {
		return failName;
	}

	/**
	 * @param failName
	 *            the failName to set
	 */
	public void setFailName(String failName) {
		this.failName = failName;
	}

	/**
	 * @return the failTimefailTime
	 */
	// @Column(name = "FAIL_TIME")
	@Transient
	public Long getFailTime() {
		return failTime;
	}

	@Transient
	public String getFailDateTime() {
		if (null != endTime) {
			return new SimpleDateFormat("MM-dd HH:mm:ss.SSS").format(new Date(
					endTime));
		}
		return null;
	}

	/**
	 * @param failTime
	 *            the failTime to set
	 */
	public void setFailTime(Long failTime) {
		this.failTime = failTime;
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

	@Column(name = "FAILID")
	public Long getFailId() {
		return failId;
	}

	public void setFailId(Long failId) {
		this.failId = failId;
	}

	/**
	 * @return the startTimestartTime
	 */
	@Column(name = "START_TIME")
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTimeendTime
	 */
	@Column(name = "END_TIME")
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the testLogTypetestLogType
	 */
	@Transient
	public Integer getTestLogType() {
		return testLogType;
	}

	/**
	 * @param testLogType
	 *            the testLogType to set
	 */
	public void setTestLogType(Integer testLogType) {
		this.testLogType = testLogType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VolteDropping [id=" + id + ", testLogItem=" + testLogItem
				+ ", m_stRoadName=" + m_stRoadName + ", beginLatitude="
				+ beginLatitude + ", courseLatitude=" + courseLatitude
				+ ", endLatitude=" + endLatitude + ", beginLongitude="
				+ beginLongitude + ", courseLongitude=" + courseLongitude
				+ ", endLongitude=" + endLongitude + ", cellId=" + cellId
				+ ", failId=" + failId + ", failTime=" + failTime
				+ ", cellName=" + cellName + ", failName=" + failName + "]";
	}

}
