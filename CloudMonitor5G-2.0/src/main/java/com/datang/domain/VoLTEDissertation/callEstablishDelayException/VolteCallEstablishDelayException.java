/**
 * 
 */
package com.datang.domain.VoLTEDissertation.callEstablishDelayException;

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
 * VoLTE专题----呼叫建立时延异常分析,呼叫建立时延异常公共指标
 * 
 * @author yinzhipeng
 * @date:2016年5月20日 上午9:41:46
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_CEDE")
@Inheritance(strategy = InheritanceType.JOINED)
public class VolteCallEstablishDelayException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3348960381960358680L;
	/**
	 * id
	 */
	private Long id;
	/**
	 * 测试日志
	 */
	private TestLogItem testLogItem;

	/**
	 * 呼叫建立时延异常路段名称,根据以下3对经纬度信息获取
	 */
	private String m_stRoadName;
	private Float beginLatitude;
	private Float courseLatitude;
	private Float endLatitude;
	private Float beginLongitude;
	private Float courseLongitude;
	private Float endLongitude;

	/**
	 * 呼叫建立时延异常路段开始时间戳
	 */
	private Long startTime;
	/**
	 * 呼叫建立时延异常路段结束时间戳
	 */
	private Long endTime;
	/**
	 * 呼叫建立时延异常路段里程:单位m
	 */
	private Float m_dbDistance;
	/**
	 * 呼叫建立时延异常路段持续测试时间:ms
	 */
	private Long m_dbContinueTime;

	/**
	 * 时延异常节点: <br>
	 * 0'VoLTE呼叫接入时延',1'VoLTE呼叫RAB建立时延',<br>
	 * 2'VOLTE呼叫被叫寻呼时延',3'VoLTE呼叫被叫媒体类型上报时延'<br>
	 * 4'VoLTE呼叫主叫振铃时延',5'其他节点'<br>
	 * 若一次VoLTE呼叫过程中，多个节点时延大于门限,则以","分隔的形式入库,如:"0,3,5"
	 */
	private String exceptionSignallingNode;
	/**
	 * 涉及小区占比:
	 * VoLTE呼叫建立时延异常问题数据中，从VoLTE起呼到VoLTE振铃之间中作为服务小区的小区个数/所选测试日志作为服务小区的小区个数*100%<br>
	 * cellid用于区分 :服务小区的小区个数
	 */
	private Long cellId;

	/**
	 * 服务小区友好名
	 */
	private String cellName;

	/**
	 * 呼叫建立时延
	 */
	private Float callEstablishDelay;
	/**
	 * INVITE->RRC连接建立完成时延
	 */
	private Float invite2rrcConnectionSeutpCompleteDelay;
	/**
	 * RRC连接建立完成->RRC连接重配置时延
	 */
	private Float rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay;
	/**
	 * INVITE 100->被叫paging时延
	 */
	private Float invite1002calledPagingDelay;
	/**
	 * 被叫INVITE->被叫INVITE 183时延
	 */
	private Float calledInvite2calledInvite183Delay;
	/**
	 * 主叫INVITE 183->主叫INVITE 180Ringing时延
	 */
	private Float callingInvite2callingInvite180RingingDelay;

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
	 * @return the exceptionSignallingNodeexceptionSignallingNode
	 */
	@Column(name = "EXCEPTION_SIGN_NODE")
	public String getExceptionSignallingNode() {
		return exceptionSignallingNode;
	}

	/**
	 * @param exceptionSignallingNode
	 *            the exceptionSignallingNode to set
	 */
	public void setExceptionSignallingNode(String exceptionSignallingNode) {
		this.exceptionSignallingNode = exceptionSignallingNode;
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
	 * @return the callEstablishDelaycallEstablishDelay
	 */
	@Column(name = "CALL_ESTABLISH")
	public Float getCallEstablishDelay() {
		return callEstablishDelay;
	}

	/**
	 * @param callEstablishDelay
	 *            the callEstablishDelay to set
	 */
	public void setCallEstablishDelay(Float callEstablishDelay) {
		this.callEstablishDelay = callEstablishDelay;
	}

	/**
	 * @return the
	 *         invite2rrcConnectionSeutpCompleteDelayinvite2rrcConnectionSeutpCompleteDelay
	 */
	@Column(name = "INV_RRC_COMP")
	public Float getInvite2rrcConnectionSeutpCompleteDelay() {
		return invite2rrcConnectionSeutpCompleteDelay;
	}

	/**
	 * @param invite2rrcConnectionSeutpCompleteDelay
	 *            the invite2rrcConnectionSeutpCompleteDelay to set
	 */
	public void setInvite2rrcConnectionSeutpCompleteDelay(
			Float invite2rrcConnectionSeutpCompleteDelay) {
		this.invite2rrcConnectionSeutpCompleteDelay = invite2rrcConnectionSeutpCompleteDelay;
	}

	/**
	 * @return the
	 *         rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelayrrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay
	 */
	@Column(name = "RRC_COMP_RRC_RECO")
	public Float getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay() {
		return rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay;
	}

	/**
	 * @param rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay
	 *            the
	 *            rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay
	 *            to set
	 */
	public void setRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay(
			Float rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay) {
		this.rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay = rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay;
	}

	/**
	 * @return the invite1002calledPagingDelayinvite1002calledPagingDelay
	 */
	@Column(name = "INV100_CALLED_PAGING")
	public Float getInvite1002calledPagingDelay() {
		return invite1002calledPagingDelay;
	}

	/**
	 * @param invite1002calledPagingDelay
	 *            the invite1002calledPagingDelay to set
	 */
	public void setInvite1002calledPagingDelay(Float invite1002calledPagingDelay) {
		this.invite1002calledPagingDelay = invite1002calledPagingDelay;
	}

	/**
	 * @return the
	 *         calledInvite2calledInvite183DelaycalledInvite2calledInvite183Delay
	 */
	@Column(name = "CALLED_INV_CALLED_INV183")
	public Float getCalledInvite2calledInvite183Delay() {
		return calledInvite2calledInvite183Delay;
	}

	/**
	 * @param calledInvite2calledInvite183Delay
	 *            the calledInvite2calledInvite183Delay to set
	 */
	public void setCalledInvite2calledInvite183Delay(
			Float calledInvite2calledInvite183Delay) {
		this.calledInvite2calledInvite183Delay = calledInvite2calledInvite183Delay;
	}

	/**
	 * @return the
	 *         callingInvite2callingInvite180RingingDelaycallingInvite2callingInvite180RingingDelay
	 */
	@Column(name = "CALL_INV_CALL_INV180_RING")
	public Float getCallingInvite2callingInvite180RingingDelay() {
		return callingInvite2callingInvite180RingingDelay;
	}

	/**
	 * @param callingInvite2callingInvite180RingingDelay
	 *            the callingInvite2callingInvite180RingingDelay to set
	 */
	public void setCallingInvite2callingInvite180RingingDelay(
			Float callingInvite2callingInvite180RingingDelay) {
		this.callingInvite2callingInvite180RingingDelay = callingInvite2callingInvite180RingingDelay;
	}

	@Override
	public String toString() {
		return "VolteCallEstablishDelayException [id="
				+ id
				+ ", testLogItem="
				+ testLogItem
				+ ", m_stRoadName="
				+ m_stRoadName
				+ ", beginLatitude="
				+ beginLatitude
				+ ", courseLatitude="
				+ courseLatitude
				+ ", endLatitude="
				+ endLatitude
				+ ", beginLongitude="
				+ beginLongitude
				+ ", courseLongitude="
				+ courseLongitude
				+ ", endLongitude="
				+ endLongitude
				+ ", startTime="
				+ startTime
				+ ", endTime="
				+ endTime
				+ ", m_dbDistance="
				+ m_dbDistance
				+ ", m_dbContinueTime="
				+ m_dbContinueTime
				+ ", exceptionSignallingNode="
				+ exceptionSignallingNode
				+ ", cellId="
				+ cellId
				+ ", cellName="
				+ cellName
				+ ", callEstablishDelay="
				+ callEstablishDelay
				+ ", invite2rrcConnectionSeutpCompleteDelay="
				+ invite2rrcConnectionSeutpCompleteDelay
				+ ", rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay="
				+ rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay
				+ ", invite1002calledPagingDelay="
				+ invite1002calledPagingDelay
				+ ", calledInvite2calledInvite183Delay="
				+ calledInvite2calledInvite183Delay
				+ ", callingInvite2callingInvite180RingingDelay="
				+ callingInvite2callingInvite180RingingDelay + "]";
	}

}
