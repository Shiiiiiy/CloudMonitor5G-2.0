/**
 * 
 */
package com.datang.domain.domain5g.embbCover;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.domain.testLogItem.TestLogItem;
import com.datang.util.NumberFormatUtils;

/**
 * eMBB覆盖专题----弱过重叠覆盖路段bean
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_ROAD")
public class EmbbCoverBadRoad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1163437908478880274L;
	/**
	 * id
	 */
	private Long id;
	/**
	 * 测试日志
	 */
	private TestLogItem testLogItem;

	/**
	 * 问题路段名称,根据以下3对经纬度信息获取
	 */
	private String roadName;
	private Float beginLatitude;
	private Float courseLatitude;
	private Float endLatitude;
	private Float beginLongitude;
	private Float courseLongitude;
	private Float endLongitude;

	/**
	 * 质差路段开始时间戳
	 */
	private Long startTime;
	/**
	 * 质差路段结束时间戳
	 */
	private Long endTime;
	/**
	 * 问题路段里程:单位m
	 */
	private Float distance;
	/**
	 * 问题路段持续测试时间:ms
	 */
	private Long continueTime;

	/**
	 * 问题路段sinr值之和
	 */
	private Float sinrValueSum;
	/**
	 * 问题路段rsrp采样点个数,问题路段sinr采样点个数,问题路段总的采样点个数
	 */
	private Long rsrpOrSinrPointNum;
	/**
	 * 问题路段rsrp值之和
	 */
	private Float rsrpValueSum;

	/**
	 * 采样点占比
	 */
	private Float pointRate;
	/**
	 * FTP上传速率（kbps）
	 */
	private Float ftpUpSpeed;

	/**
	 * FTP下载速率（kbps）
	 */
	private Float ftpDlSpeed;

	/**
	 * rsrp最低值
	 */
	private Float rsrpMin;
	/**
	 * sinr最低值
	 */
	private Float sinrMin;
	/**
	 * 弱过重叠覆盖占比
	 */
	private Float coverRate;
	/**
	 * 主邻小区平均距离
	 */
	private Float cellToNbCellDistanceAvg;
	/**
	 * 天馈反接距离
	 */
	private Float tiankuiRevDistance;
	/**
	 * 原因分析：<br>
	 * 0距离脱离5G覆盖区域，建议加站<br>
	 * 1邻区缺失，建议添加邻区<br>
	 * 2小区反向覆盖导致，反向覆盖建议调整<br>
	 * 3小区波束异常，天线权值优化<br>
	 * 4其他原因，天馈调整<br>
	 * 5小区无信号，核查影响无线性能的告警<br>
	 * 6超高站，降低天线的高度<br>
	 * 7天线倾角不合适，增大天线下倾角<br>
	 * 8下行功率分配不合理，减小小区功率<br>
	 * 9其他原因，现场复测分析<br>
	 * 10小区方位角不合理，调整邻区方位角<br>
	 * 11主小区波束异常，优化主小区天线权值
	 */
	private Integer reasonTypeNum;
	/**
	 * 路段类型： 0弱覆盖，1过覆盖，2重叠覆盖，3弱覆盖过覆盖，4弱覆盖重叠覆盖，5过覆盖重叠覆盖，6弱覆盖过覆盖重叠覆盖
	 */
	private Integer coverTypeNum;

	@Transient
	public String getStartTimeString() {
		if (null != startTime) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
					startTime));
		}
		return null;
	}

	@Transient
	public String getContinueTimeString() {
		if (null != continueTime) {
			return String.valueOf(NumberFormatUtils.format(
					continueTime / 1000.0f, 2));
		}
		return null;
	}

	@Transient
	public String getRsrpAvg() {
		if (null != rsrpValueSum && 0 != rsrpValueSum
				&& rsrpOrSinrPointNum != null && 0 != rsrpOrSinrPointNum) {
			return String.valueOf(NumberFormatUtils.format(rsrpValueSum
					/ rsrpOrSinrPointNum, 2));
		}
		return null;
	}

	@Transient
	public String getSinrAvg() {
		if (null != sinrValueSum && 0 != sinrValueSum
				&& rsrpOrSinrPointNum != null && 0 != rsrpOrSinrPointNum) {
			return String.valueOf(NumberFormatUtils.format(sinrValueSum
					/ rsrpOrSinrPointNum, 2));
		}
		return null;
	}

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

	@Column(name = "BEGIN_LATITUDE")
	public Float getBeginLatitude() {
		return beginLatitude;
	}

	public void setBeginLatitude(Float beginLatitude) {
		this.beginLatitude = beginLatitude;
	}

	@Column(name = "COURSE_LATITUDE")
	public Float getCourseLatitude() {
		return courseLatitude;
	}

	public void setCourseLatitude(Float courseLatitude) {
		this.courseLatitude = courseLatitude;
	}

	@Column(name = "END_LATITUDE")
	public Float getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(Float endLatitude) {
		this.endLatitude = endLatitude;
	}

	@Column(name = "BEGIN_LONGITUDE")
	public Float getBeginLongitude() {
		return beginLongitude;
	}

	public void setBeginLongitude(Float beginLongitude) {
		this.beginLongitude = beginLongitude;
	}

	@Column(name = "COURSE_LONGITUDE")
	public Float getCourseLongitude() {
		return courseLongitude;
	}

	public void setCourseLongitude(Float courseLongitude) {
		this.courseLongitude = courseLongitude;
	}

	@Column(name = "END_LONGITUDE")
	public Float getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(Float endLongitude) {
		this.endLongitude = endLongitude;
	}

	@Column(name = "START_TIME")
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Column(name = "DISTANCE")
	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	@Column(name = "CONTINUE_TIME")
	public Long getContinueTime() {
		return continueTime;
	}

	public void setContinueTime(Long continueTime) {
		this.continueTime = continueTime;
	}

	@Column(name = "SINR_VALUE_SUM")
	public Float getSinrValueSum() {
		return sinrValueSum;
	}

	public void setSinrValueSum(Float sinrValueSum) {
		this.sinrValueSum = sinrValueSum;
	}

	@Column(name = "RSRP_OR_SINR_POINT_NUM")
	public Long getRsrpOrSinrPointNum() {
		return rsrpOrSinrPointNum;
	}

	public void setRsrpOrSinrPointNum(Long rsrpOrSinrPointNum) {
		this.rsrpOrSinrPointNum = rsrpOrSinrPointNum;
	}

	@Column(name = "RSRP_VALUE_SUM")
	public Float getRsrpValueSum() {
		return rsrpValueSum;
	}

	public void setRsrpValueSum(Float rsrpValueSum) {
		this.rsrpValueSum = rsrpValueSum;
	}

	@Column(name = "POINT_RATE")
	public Float getPointRate() {
		return pointRate;
	}

	public void setPointRate(Float pointRate) {
		this.pointRate = pointRate;
	}

	@Column(name = "FTP_UP_SPEED")
	public Float getFtpUpSpeed() {
		return ftpUpSpeed;
	}

	public void setFtpUpSpeed(Float ftpUpSpeed) {
		this.ftpUpSpeed = ftpUpSpeed;
	}

	@Column(name = "FTP_DL_SPEED")
	public Float getFtpDlSpeed() {
		return ftpDlSpeed;
	}

	public void setFtpDlSpeed(Float ftpDlSpeed) {
		this.ftpDlSpeed = ftpDlSpeed;
	}

	@Column(name = "RSRP_MIN")
	public Float getRsrpMin() {
		return rsrpMin;
	}

	public void setRsrpMin(Float rsrpMin) {
		this.rsrpMin = rsrpMin;
	}

	@Column(name = "SINR_MIN")
	public Float getSinrMin() {
		return sinrMin;
	}

	public void setSinrMin(Float sinrMin) {
		this.sinrMin = sinrMin;
	}

	@Column(name = "COVER_RATE")
	public Float getCoverRate() {
		return coverRate;
	}

	public void setCoverRate(Float coverRate) {
		this.coverRate = coverRate;
	}

	@Column(name = "CELL_NBCELL_DISTANCE")
	public Float getCellToNbCellDistanceAvg() {
		return cellToNbCellDistanceAvg;
	}

	public void setCellToNbCellDistanceAvg(Float cellToNbCellDistanceAvg) {
		this.cellToNbCellDistanceAvg = cellToNbCellDistanceAvg;
	}

	@Column(name = "TIANKUI_REV_DISTANCE")
	public Float getTiankuiRevDistance() {
		return tiankuiRevDistance;
	}

	public void setTiankuiRevDistance(Float tiankuiRevDistance) {
		this.tiankuiRevDistance = tiankuiRevDistance;
	}

	@Column(name = "REASON_TYPE_NUM")
	public Integer getReasonTypeNum() {
		return reasonTypeNum;
	}

	public void setReasonTypeNum(Integer reasonTypeNum) {
		this.reasonTypeNum = reasonTypeNum;
	}

	@Column(name = "COVER_TYPE_NUM")
	public Integer getCoverTypeNum() {
		return coverTypeNum;
	}

	public void setCoverTypeNum(Integer coverTypeNum) {
		this.coverTypeNum = coverTypeNum;
	}

}
