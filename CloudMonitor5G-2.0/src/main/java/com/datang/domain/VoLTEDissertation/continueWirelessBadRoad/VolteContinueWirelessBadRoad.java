/**
 * 
 */
package com.datang.domain.VoLTEDissertation.continueWirelessBadRoad;

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
import com.datang.util.NumberFormatUtils;

/**
 * VoLTE质量专题----连续无线差路段分析,连续无线差路段公共指标
 * 
 * @author yinzhipeng
 * @date:2016年5月19日 下午3:40:14
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_CWBR")
@Inheritance(strategy = InheritanceType.JOINED)
public class VolteContinueWirelessBadRoad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -345837204261672853L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 测试日志
	 */
	private TestLogItem testLogItem;

	/**
	 * 连续无线差路段名称,根据以下3对经纬度信息获取
	 */
	private String m_stRoadName;
	private Float beginLatitude;
	private Float courseLatitude;
	private Float endLatitude;
	private Float beginLongitude;
	private Float courseLongitude;
	private Float endLongitude;

	/**
	 * 连续无线差路段开始时间戳
	 */
	private Long startTime;
	/**
	 * 连续无线差路段结束时间戳
	 */
	private Long endTime;
	/**
	 * 连续无线差路段里程:单位m
	 */
	private Float m_dbDistance;
	/**
	 * 连续无线差路段持续测试时间:ms
	 */
	private Long m_dbContinueTime;

	/**
	 * 连续无线差路段LTE制式下上报的MOS采样点个数
	 */
	private Long m_ulLTE_MosPointNum;
	/**
	 * 连续无线差路段LTE制式下上报的MOS采样点值累计和
	 */
	private Float m_dbLTE_MosValueSum;

	/**
	 * 连续无线差路段sinr值之和
	 */
	private Float m_dbSinrValueSum;
	/**
	 * 连续无线差路段rsrp采样点个数,问题路段sinr采样点个数,问题路段总的采样点个数
	 */
	private Long m_ulRsrpOrSinrPointNum;
	/**
	 * 连续无线差路段rsrp值之和
	 */
	private Float m_dbRsrpValueSum;
	// RTP丢包率=丢失的VoIP数据包数量/发送的VoIP数据包数量
	/**
	 * RTP丢包率:丢失的VoIP数据包数量
	 */
	private Float m_dbRTPLoseVoIPDataPackageNum;
	/**
	 * RTP丢包率:接收的数据包数量
	 */
	private Float m_dbRTPReceiveDataPackageNum;

	/**
	 * 连续无线差路段中作为服务小区的小区个数
	 */
	private Long m_ulCellNum;
	/**
	 * 因连续无线差原因，需要进行天馈调整的小区个数
	 */
	private Long m_ulTianKuiCellNum;
	/**
	 * 因连续无线差原因，需要进行邻区关系调整的小区个数
	 */
	private Long m_ulLinQuCellNum;
	/**
	 * 因连续无线差原因，需要进行PCI调整的小区个数
	 */
	private Long m_ulPCICellNum;

	/**
	 * RSRP均值,根据RSRP值之和和RSRP数量和计算
	 */
	private Float rsrpAvg;
	/**
	 * SINR均值,根据SINR值之和和SINR数量和计算
	 */
	private Float sinrAvg;
	/**
	 * MOS均值,根据MOS值之和和MOS数量和计算
	 */
	private Float mosAvg;

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
	 * @return the m_dbDistancem_dbDistance
	 */
	@Column(name = "DISTANCE")
	public Float getM_dbDistance() {
		return m_dbDistance;
	}

	/**
	 * @param m_dbDistance
	 *            the m_dbDistance to set
	 */
	public void setM_dbDistance(Float m_dbDistance) {
		this.m_dbDistance = m_dbDistance;
	}

	/**
	 * @return the m_dbContinueTimem_dbContinueTime
	 */
	@Column(name = "CONTINUE_TIME")
	public Long getM_dbContinueTime() {
		return m_dbContinueTime;
	}

	/**
	 * @param m_dbContinueTime
	 *            the m_dbContinueTime to set
	 */
	public void setM_dbContinueTime(Long m_dbContinueTime) {
		this.m_dbContinueTime = m_dbContinueTime;
	}

	@Transient
	public String getStartTime2String() {
		if (null != startTime) {
			return new SimpleDateFormat("MM-dd HH:mm:ss.SSS").format(new Date(
					startTime));
		}
		return null;
	}

	/**
	 * @return the m_ulLTE_MosPointNumm_ulLTE_MosPointNum
	 */
	@Column(name = "LTE_MOSPOINT_NUM")
	public Long getM_ulLTE_MosPointNum() {
		return m_ulLTE_MosPointNum;
	}

	/**
	 * @param m_ulLTE_MosPointNum
	 *            the m_ulLTE_MosPointNum to set
	 */
	public void setM_ulLTE_MosPointNum(Long m_ulLTE_MosPointNum) {
		this.m_ulLTE_MosPointNum = m_ulLTE_MosPointNum;
	}

	/**
	 * @return the m_dbLTE_MosValueSumm_dbLTE_MosValueSum
	 */
	@Column(name = "LTE_MOSVALUE_SUM")
	public Float getM_dbLTE_MosValueSum() {
		return m_dbLTE_MosValueSum;
	}

	/**
	 * @param m_dbLTE_MosValueSum
	 *            the m_dbLTE_MosValueSum to set
	 */
	public void setM_dbLTE_MosValueSum(Float m_dbLTE_MosValueSum) {
		this.m_dbLTE_MosValueSum = m_dbLTE_MosValueSum;
	}

	/**
	 * @return the m_dbRTPLoseVoIPDataPackageNumm_dbRTPLoseVoIPDataPackageNum
	 */
	@Column(name = "RTP_LOSEVOIP_DATAPACKAGE_NUM")
	public Float getM_dbRTPLoseVoIPDataPackageNum() {
		return m_dbRTPLoseVoIPDataPackageNum;
	}

	/**
	 * @param m_dbRTPLoseVoIPDataPackageNum
	 *            the m_dbRTPLoseVoIPDataPackageNum to set
	 */
	public void setM_dbRTPLoseVoIPDataPackageNum(
			Float m_dbRTPLoseVoIPDataPackageNum) {
		this.m_dbRTPLoseVoIPDataPackageNum = m_dbRTPLoseVoIPDataPackageNum;
	}

	/**
	 * @return the m_dbRTPReceiveDataPackageNumm_dbRTPReceiveDataPackageNum
	 */
	@Column(name = "RTP_RECEIVE_DATAPACKAGE_NUM")
	public Float getM_dbRTPReceiveDataPackageNum() {
		return m_dbRTPReceiveDataPackageNum;
	}

	/**
	 * @param m_dbRTPReceiveDataPackageNum
	 *            the m_dbRTPReceiveDataPackageNum to set
	 */
	public void setM_dbRTPReceiveDataPackageNum(
			Float m_dbRTPReceiveDataPackageNum) {
		this.m_dbRTPReceiveDataPackageNum = m_dbRTPReceiveDataPackageNum;
	}

	/**
	 * @return the m_ulCellNumm_ulCellNum
	 */
	@Column(name = "CELL_NUM")
	public Long getM_ulCellNum() {
		return m_ulCellNum;
	}

	/**
	 * @param m_ulCellNum
	 *            the m_ulCellNum to set
	 */
	public void setM_ulCellNum(Long m_ulCellNum) {
		this.m_ulCellNum = m_ulCellNum;
	}

	/**
	 * @return the m_ulTianKuiCellNumm_ulTianKuiCellNum
	 */
	@Column(name = "TIANKUI_CELLNUM")
	public Long getM_ulTianKuiCellNum() {
		return m_ulTianKuiCellNum;
	}

	/**
	 * @param m_ulTianKuiCellNum
	 *            the m_ulTianKuiCellNum to set
	 */
	public void setM_ulTianKuiCellNum(Long m_ulTianKuiCellNum) {
		this.m_ulTianKuiCellNum = m_ulTianKuiCellNum;
	}

	/**
	 * @return the m_ulLinQuCellNumm_ulLinQuCellNum
	 */
	@Column(name = "LINQU_CELLNUM")
	public Long getM_ulLinQuCellNum() {
		return m_ulLinQuCellNum;
	}

	/**
	 * @param m_ulLinQuCellNum
	 *            the m_ulLinQuCellNum to set
	 */
	public void setM_ulLinQuCellNum(Long m_ulLinQuCellNum) {
		this.m_ulLinQuCellNum = m_ulLinQuCellNum;
	}

	/**
	 * @return the m_ulPCICellNumm_ulPCICellNum
	 */
	@Column(name = "PCI_CELLNUM")
	public Long getM_ulPCICellNum() {
		return m_ulPCICellNum;
	}

	/**
	 * @param m_ulPCICellNum
	 *            the m_ulPCICellNum to set
	 */
	public void setM_ulPCICellNum(Long m_ulPCICellNum) {
		this.m_ulPCICellNum = m_ulPCICellNum;
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
	 * @return the m_dbSinrValueSumm_dbSinrValueSum
	 */
	@Column(name = "SINR_VALUE_SUM")
	public Float getM_dbSinrValueSum() {
		return m_dbSinrValueSum;
	}

	/**
	 * @param m_dbSinrValueSum
	 *            the m_dbSinrValueSum to set
	 */
	public void setM_dbSinrValueSum(Float m_dbSinrValueSum) {
		this.m_dbSinrValueSum = m_dbSinrValueSum;
	}

	/**
	 * @return the m_ulRsrpOrSinrPointNumm_ulRsrpOrSinrPointNum
	 */
	@Column(name = "RSRPORSINR_POINT_NUM")
	public Long getM_ulRsrpOrSinrPointNum() {
		return m_ulRsrpOrSinrPointNum;
	}

	/**
	 * @param m_ulRsrpOrSinrPointNum
	 *            the m_ulRsrpOrSinrPointNum to set
	 */
	public void setM_ulRsrpOrSinrPointNum(Long m_ulRsrpOrSinrPointNum) {
		this.m_ulRsrpOrSinrPointNum = m_ulRsrpOrSinrPointNum;
	}

	/**
	 * @return the m_dbRsrpValueSumm_dbRsrpValueSum
	 */
	@Column(name = "RSRP_VALUE_SUM")
	public Float getM_dbRsrpValueSum() {
		return m_dbRsrpValueSum;
	}

	/**
	 * @param m_dbRsrpValueSum
	 *            the m_dbRsrpValueSum to set
	 */
	public void setM_dbRsrpValueSum(Float m_dbRsrpValueSum) {
		this.m_dbRsrpValueSum = m_dbRsrpValueSum;
	}

	/**
	 * @return the startTimestartTime
	 */
	@Column(name = "START_TIME")
	public Long getStartTime() {
		return startTime;
	}

	@Transient
	public String getStartTimeValue() {
		if (null != startTime) {
			return new SimpleDateFormat("MM-dd HH:mm:ss.SSS").format(new Date(
					startTime));
		}
		return null;
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
	 * @return the rsrpAvgrsrpAvg
	 */
	@Transient
	public Float getRsrpAvg() {
		Float rsrpValueSum = this.getM_dbRsrpValueSum();
		Long rsrpOrSinrPointNum = this.getM_ulRsrpOrSinrPointNum();
		if (null != rsrpOrSinrPointNum && null != rsrpValueSum
				&& 0 != rsrpOrSinrPointNum) {
			rsrpAvg = NumberFormatUtils.format(rsrpValueSum
					/ rsrpOrSinrPointNum, 2);
		}
		return rsrpAvg;
	}

	/**
	 * @param rsrpAvg
	 *            the rsrpAvg to set
	 */
	public void setRsrpAvg(Float rsrpAvg) {
		this.rsrpAvg = rsrpAvg;
	}

	/**
	 * @return the sinrAvgsinrAvg
	 */
	@Transient
	public Float getSinrAvg() {
		Float sinrValueSum = this.getM_dbSinrValueSum();
		Long rsrpOrSinrPointNum = this.getM_ulRsrpOrSinrPointNum();
		if (null != rsrpOrSinrPointNum && null != sinrValueSum
				&& 0 != rsrpOrSinrPointNum) {
			sinrAvg = NumberFormatUtils.format(sinrValueSum
					/ rsrpOrSinrPointNum, 2);
		}
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
	 * @return the mosAvgmosAvg
	 */
	@Transient
	public Float getMosAvg() {
		Float mosValueSum = this.getM_dbLTE_MosValueSum();
		Long mosPointNum = this.getM_ulLTE_MosPointNum();
		if (null != mosPointNum && null != mosValueSum && 0 != mosPointNum) {
			mosAvg = NumberFormatUtils.format(mosValueSum / mosPointNum, 2);
		}
		return mosAvg;
	}

	/**
	 * @param mosAvg
	 *            the mosAvg to set
	 */
	public void setMosAvg(Float mosAvg) {
		this.mosAvg = mosAvg;
	}

}
