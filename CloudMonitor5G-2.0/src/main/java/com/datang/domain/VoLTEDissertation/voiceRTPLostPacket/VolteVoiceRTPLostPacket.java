/**
 * 
 */
package com.datang.domain.VoLTEDissertation.voiceRTPLostPacket;

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

/**
 * VoLTE质量专题----语音RTP连续丢包,语音RTP连续丢包公共指标
 * 
 * @author yinzhipeng
 * @date:2017年2月9日 上午9:30:10
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOICE_RTP_LOST")
@Inheritance(strategy = InheritanceType.JOINED)
public class VolteVoiceRTPLostPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2813728130489836857L;
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
	private String m_stRoadName;
	private Float beginLatitude;
	private Float courseLatitude;
	private Float endLatitude;
	private Float beginLongitude;
	private Float courseLongitude;
	private Float endLongitude;

	/**
	 * 问题开始时间戳
	 */
	private Long startTime;
	/**
	 * 问题结束时间戳
	 */
	private Long endTime;
	/**
	 * 发生时间
	 */
	// private Long failTime;

	/**
	 * 问题路段里程:单位m
	 */
	// private Float m_dbDistance;
	/**
	 * 问题路段持续测试时间:ms
	 */
	// private Long m_dbContinueTime;

	/**
	 * 问题路段MOS采样点值之和
	 */
	// private Float m_ulMosPointSum;

	/**
	 * 问题路段MOS采样点个数
	 */
	// private Long m_ulMosPointNum;

	/**
	 * 是否是主叫,0主叫,1被叫
	 */
	// private boolean isCalling;

	/**
	 * 是否有关联网络侧日志,0有,1没有,用于汇总计算'成功关联网络侧问题数'指标
	 */
	private boolean hasNetworkTestLogItem;

	/**
	 * 丢包数
	 */
	private Integer lostPacketNum;

	/**
	 * 问题节点:0'节点1,发送端手机上行丢包',1'节点2,发送端S1口丢包',2'节点3,发送端SGi口丢包'<br>
	 * 3'节点4,接收端SGi口丢包',4'节点5,接收端S1口丢包',5'节点6,接收端Uu口丢包'
	 * 
	 */
	private Integer problemNode;

	/**
	 * 
	 * cellid
	 */
	private Long cellId;

	/**
	 * 服务小区友好名
	 */
	private String cellName;

	/**
	 * 问题路段sinr值之和
	 */
	// private Float m_dbSinrValueSum;
	private Float sinr;
	/**
	 * 问题路段rsrp采样点个数,问题路段sinr采样点个数
	 */
	// private Long m_ulRsrpOrSinrPointNum;
	/**
	 * 问题路段rsrp值之和
	 */
	// private Float m_dbRsrpValueSum;
	private Float rsrp;

	/**
	 * 节点丢包情况
	 */
	private Long sendUE;
	private Long sendS1;
	private Long sendSGi;
	private Long receUE;
	private Long receS1;
	private Long receSGi;
	private Long beginSequence;
	private Long endSequence;

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

	// /**
	// * @return the m_dbDistancem_dbDistance
	// */
	// @Column(name = "DISTANCE")
	// public Float getM_dbDistance() {
	// return m_dbDistance;
	// }
	//
	// /**
	// * @param m_dbDistance
	// * the m_dbDistance to set
	// */
	// public void setM_dbDistance(Float m_dbDistance) {
	// this.m_dbDistance = m_dbDistance;
	// }
	//
	// /**
	// * @return the m_dbContinueTimem_dbContinueTime
	// */
	// @Column(name = "CONTINUE_TIME")
	// public Long getM_dbContinueTime() {
	// return m_dbContinueTime;
	// }
	//
	// /**
	// * @param m_dbContinueTime
	// * the m_dbContinueTime to set
	// */
	// public void setM_dbContinueTime(Long m_dbContinueTime) {
	// this.m_dbContinueTime = m_dbContinueTime;
	// }
	//
	// /**
	// * @return the m_ulMosPointNumm_ulMosPointNum
	// */
	// @Column(name = "MOS_POINT_NUM")
	// public Long getM_ulMosPointNum() {
	// return m_ulMosPointNum;
	// }
	//
	// /**
	// * @param m_ulMosPointNum
	// * the m_ulMosPointNum to set
	// */
	// public void setM_ulMosPointNum(Long m_ulMosPointNum) {
	// this.m_ulMosPointNum = m_ulMosPointNum;
	// }

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
	// @Column(name = "COURSE_LATITUDE")
	@Transient
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
	// @Column(name = "COURSE_LONGITUDE")
	@Transient
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

	// /**
	// * @return the m_dbSinrValueSumm_dbSinrValueSum
	// */
	// @Column(name = "SINR_VALUE_SUM")
	// public Float getM_dbSinrValueSum() {
	// return m_dbSinrValueSum;
	// }
	//
	// /**
	// * @param m_dbSinrValueSum
	// * the m_dbSinrValueSum to set
	// */
	// public void setM_dbSinrValueSum(Float m_dbSinrValueSum) {
	// this.m_dbSinrValueSum = m_dbSinrValueSum;
	// }
	//
	// /**
	// * @return the m_ulRsrpOrSinrPointNumm_ulRsrpOrSinrPointNum
	// */
	// @Column(name = "RSRPORSINR_POINT_NUM")
	// public Long getM_ulRsrpOrSinrPointNum() {
	// return m_ulRsrpOrSinrPointNum;
	// }
	//
	// /**
	// * @param m_ulRsrpOrSinrPointNum
	// * the m_ulRsrpOrSinrPointNum to set
	// */
	// public void setM_ulRsrpOrSinrPointNum(Long m_ulRsrpOrSinrPointNum) {
	// this.m_ulRsrpOrSinrPointNum = m_ulRsrpOrSinrPointNum;
	// }
	//
	// /**
	// * @return the m_dbRsrpValueSumm_dbRsrpValueSum
	// */
	// @Column(name = "RSRP_VALUE_SUM")
	// public Float getM_dbRsrpValueSum() {
	// return m_dbRsrpValueSum;
	// }
	//
	// /**
	// * @param m_dbRsrpValueSum
	// * the m_dbRsrpValueSum to set
	// */
	// public void setM_dbRsrpValueSum(Float m_dbRsrpValueSum) {
	// this.m_dbRsrpValueSum = m_dbRsrpValueSum;
	// }

	/**
	 * @return the startTimestartTime
	 */
	@Column(name = "BEGIN_TIME")
	public Long getStartTime() {
		return startTime;
	}

	@Transient
	public String getFailTimeString() {
		if (null != startTime) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date(startTime));
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

	// /**
	// * @return the failTimefailTime
	// */
	// @Column(name = "FAIL_TIME")
	// public Long getFailTime() {
	// return failTime;
	// }
	//
	// /**
	// * @param failTime
	// * the failTime to set
	// */
	// public void setFailTime(Long failTime) {
	// this.failTime = failTime;
	// }

	// /**
	// * @return the m_ulMosPointSumm_ulMosPointSum
	// */
	// @Column(name = "MOS_POINT_SUM")
	// public Float getM_ulMosPointSum() {
	// return m_ulMosPointSum;
	// }
	//
	// /**
	// * @param m_ulMosPointSum
	// * the m_ulMosPointSum to set
	// */
	// public void setM_ulMosPointSum(Float m_ulMosPointSum) {
	// this.m_ulMosPointSum = m_ulMosPointSum;
	// }
	//
	// /**
	// * @return the isCallingisCalling
	// */
	// @Column(name = "IS_CALLING", columnDefinition = "boolean")
	// public boolean getIsCalling() {
	// return isCalling;
	// }
	//
	// /**
	// * @param isCalling
	// * the isCalling to set
	// */
	// public void setIsCalling(boolean isCalling) {
	// this.isCalling = isCalling;
	// }
	//
	/**
	 * @return the hasNetworkTestLogItemhasNetworkTestLogItem
	 */
	@Column(name = "HAS_NETWORK_LOG", columnDefinition = "boolean")
	@JSON(serialize = false)
	public boolean getHasNetworkTestLogItem() {
		return hasNetworkTestLogItem;
	}

	/**
	 * @param hasNetworkTestLogItem
	 *            the hasNetworkTestLogItem to set
	 */
	public void setHasNetworkTestLogItem(boolean hasNetworkTestLogItem) {
		this.hasNetworkTestLogItem = hasNetworkTestLogItem;
	}

	/**
	 * @return the lostPacketNumlostPacketNum
	 */
	@Column(name = "LOST_PACKET_NUM")
	public Integer getLostPacketNum() {
		return lostPacketNum;
	}

	/**
	 * @param lostPacketNum
	 *            the lostPacketNum to set
	 */
	public void setLostPacketNum(Integer lostPacketNum) {
		this.lostPacketNum = lostPacketNum;
	}

	/**
	 * @return the problemNodeproblemNode
	 */
	@Column(name = "PROBLEM_NODE")
	public Integer getProblemNode() {
		return problemNode;
	}

	/**
	 * @param problemNode
	 *            the problemNode to set
	 */
	public void setProblemNode(Integer problemNode) {
		this.problemNode = problemNode;
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
	 * @return the sendUEsendUE
	 */
	@Column(name = "SEND_UE")
	public Long getSendUE() {
		return sendUE;
	}

	/**
	 * @param sendUE
	 *            the sendUE to set
	 */
	public void setSendUE(Long sendUE) {
		this.sendUE = sendUE;
	}

	/**
	 * @return the sendS1sendS1
	 */
	@Column(name = "SEND_S1")
	public Long getSendS1() {
		return sendS1;
	}

	/**
	 * @param sendS1
	 *            the sendS1 to set
	 */
	public void setSendS1(Long sendS1) {
		this.sendS1 = sendS1;
	}

	/**
	 * @return the sendSGisendSGi
	 */
	@Column(name = "SEND_SGI")
	public Long getSendSGi() {
		return sendSGi;
	}

	/**
	 * @param sendSGi
	 *            the sendSGi to set
	 */
	public void setSendSGi(Long sendSGi) {
		this.sendSGi = sendSGi;
	}

	/**
	 * @return the receUEreceUE
	 */
	@Column(name = "RECE_UE")
	public Long getReceUE() {
		return receUE;
	}

	/**
	 * @param receUE
	 *            the receUE to set
	 */
	public void setReceUE(Long receUE) {
		this.receUE = receUE;
	}

	/**
	 * @return the receS1receS1
	 */
	@Column(name = "RECE_S1")
	public Long getReceS1() {
		return receS1;
	}

	/**
	 * @param receS1
	 *            the receS1 to set
	 */
	public void setReceS1(Long receS1) {
		this.receS1 = receS1;
	}

	/**
	 * @return the receSGireceSGi
	 */
	@Column(name = "RECE_SGI")
	public Long getReceSGi() {
		return receSGi;
	}

	/**
	 * @param receSGi
	 *            the receSGi to set
	 */
	public void setReceSGi(Long receSGi) {
		this.receSGi = receSGi;
	}

	/**
	 * @return the beginSequencebeginSequence
	 */
	@Column(name = "BEGIN_SEQUENCE")
	public Long getBeginSequence() {
		return beginSequence;
	}

	/**
	 * @param beginSequence
	 *            the beginSequence to set
	 */
	public void setBeginSequence(Long beginSequence) {
		this.beginSequence = beginSequence;
	}

	/**
	 * @return the endSequenceendSequence
	 */
	@Column(name = "END_SEQUENCE")
	public Long getEndSequence() {
		return endSequence;
	}

	/**
	 * @param endSequence
	 *            the endSequence to set
	 */
	public void setEndSequence(Long endSequence) {
		this.endSequence = endSequence;
	}

	/**
	 * @return the sinr
	 */
	@Column(name = "SINR")
	public Float getSinr() {
		return sinr;
	}

	/**
	 * @param the
	 *            sinr to set
	 */

	public void setSinr(Float sinr) {
		this.sinr = sinr;
	}

	/**
	 * @return the rsrp
	 */
	@Column(name = "RSRP")
	public Float getRsrp() {
		return rsrp;
	}

	/**
	 * @param the
	 *            rsrp to set
	 */

	public void setRsrp(Float rsrp) {
		this.rsrp = rsrp;
	}

}
