/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad;

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
 * VoLTE质量专题----语音质差路段分析,语音质差路段公共指标
 * 
 * @author yinzhipeng
 * @date:2015年11月4日 上午9:49:18
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_QBR")
@Inheritance(strategy = InheritanceType.JOINED)
public class VolteQualityBadRoad implements Serializable {

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
	private Float m_dbDistance;
	/**
	 * 问题路段持续测试时间:ms
	 */
	private Long m_dbContinueTime;
	/**
	 * 初次mos时间
	 */
	private Long initMosTime;
	/**
	 * mos均值
	 */
	private Float mosAvg;
	/**
	 * mos类型
	 */
	private String mosType;

	// 以下为整体分析下的参数,也为所有问题路段的公共参数
	/**
	 * 问题路段LTE制式下上报的MOS采样点个数
	 */
	private Long m_ulLTE_MosPointNum;
	/**
	 * 问题路段LTE制式下上报的MOS采样点值累计和
	 */
	private Float m_dbLTE_MosValueSum;

	// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
	// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
	/**
	 * 初始BLER,残留BLER:初传次数<br>
	 * 
	 * @date 20160324 废弃
	 */
	private Long m_ulBlerFirstRequestNum;
	/**
	 * 初始BLER:初传成功次数<br>
	 * 
	 * @date 20160324 废弃
	 */
	private Long m_ulBlerFirstSuccessNum;
	/**
	 * 残留BLER:多次重传后成功次数<br>
	 * 
	 * @date 20160324 废弃
	 */
	private Long m_ulBlerAgainSuccessNum;

	/**
	 * 初始BLER<br>
	 * 
	 * @date 20160324 新增
	 */
	private Float lBler;
	/**
	 * 残留BLER<br>
	 * 
	 * @date 20160324 新增
	 */
	private Float rBler;

	// RTP丢包率=(发送的VoIP数据包数量—接收的数据包数量)/发送的VoIP数据包数量
	/**
	 * RTP丢包率:发送的VoIP数据包数量
	 */
	private Float m_dbRTPSendVoIPDataPackageNum;
	/**
	 * RTP丢包率:接收的数据包数量
	 */
	private Float m_dbRTPReceiveDataPackageNum;
	/**
	 * RTP抖动(ms)=RTP取值之和/RTP数量
	 */
	// private Float m_dbRTPShake;
	private Float m_dbRTPSum;
	private Long m_ulRTPNum;

	/**
	 * TDL驻网时长
	 */
	private Float m_dbTDLTestTime;
	/**
	 * TDS驻网时长
	 */
	private Float m_dbTDSTestTime;
	/**
	 * GSM驻网时长
	 */
	private Float m_dbGSMTestTime;
	/**
	 * 问题路段MOS采样点个数
	 */
	private Long m_ulMosPointNum;
	/**
	 * 问题路段MOS>=3.0的个数
	 */
	private Long m_ulMosOver30PointNum;
	/**
	 * 问题路段MOS>=3.5的个数
	 */
	private Long m_ulMosOver35PointNum;
	/**
	 * 问题路段sinr值之和
	 */
	private Float m_dbSinrValueSum;
	/**
	 * 问题路段rsrp采样点个数,问题路段sinr采样点个数,问题路段总的采样点个数
	 */
	private Long m_ulRsrpOrSinrPointNum;
	/**
	 * 问题路段rsrp值之和
	 */
	private Float m_dbRsrpValueSum;

	/**
	 * 问题路段lte同频切换成功次数
	 */
	private Long m_ulLteIntraHoSuccNum;
	/**
	 * 问题路段lte异频切换成功次数
	 */
	private Long m_ulLteInterHoSuccNum;
	/**
	 * 问题路段lte同频切换尝试次数
	 */
	private Long m_ulLteIntraHoReqNum;
	/**
	 * 问题路段lte异频切换尝试次数
	 */
	private Long m_ulLteInterHoReqNum;
	/**
	 * 问题路段满足覆盖条件(RSRP>=-110&SINR>=-3)的采样点
	 */
	private Long m_ulLteCoverNum;

	/**
	 * 语音质差问题路段中作为服务小区的小区个数
	 */
	private Long m_ulCellNum;
	/**
	 * 因语音质差原因，需要进行天馈调整的小区个数
	 */
	private Long m_ulTianKuiCellNum;
	/**
	 * 因语音质差原因，需要进行参数调整的小区个数
	 */
	private Long m_ulCanShuCellNum;
	/**
	 * 因语音质差原因，需要进行邻区关系调整的小区个数
	 */
	private Long m_ulLinQuCellNum;
	/**
	 * 因语音质差原因，需要进行PCI调整的小区个数
	 */
	private Long m_ulPCICellNum;

	/**
	 * 主被叫都占用LTE时,主叫MOS采样点数
	 */
	private Long m_ulLTE_LTE_CallingMosPointNum;
	/**
	 * 主被叫都占用LTE时,主叫MOS值累积和
	 */
	private Float m_dbLTE_LTE_CallingMosValueSum;
	/**
	 * 主被叫都占用LTE时,被叫MOS采样点数
	 */
	private Long m_ulLTE_LTE_CalledMosPointNum;
	/**
	 * 主被叫都占用LTE时,被叫MOS值累积和
	 */
	private Float m_dbLTE_LTE_CalledMosValueSum;

	/**
	 * 主叫占用LTE，被叫占用2/3G时,主叫MOS采样点数
	 */
	private Long m_ulLTE_CS_CallingMosPointNum;
	/**
	 * 主叫占用LTE，被叫占用2/3G时,主叫MOS值累积和
	 */
	private Float m_dbLTE_CS_CallingMosValueSum;
	/**
	 * 主叫占用LTE，被叫占用2/3G时,被叫MOS采样点数
	 */
	private Long m_ulLTE_CS_CalledMosPointNum;
	/**
	 * 主叫占用LTE，被叫占用2/3G时,被叫MOS值累积和
	 */
	private Float m_dbLTE_CS_CalledMosValueSum;
	/**
	 * 主叫占用2/3G，被叫占用LTE时,主叫MOS采样点数
	 */
	private Long m_ulCS_LTE_CallingMosPointNum;
	/**
	 * 主叫占用2/3G，被叫占用LTE时,主叫MOS值累积和
	 */
	private Float m_dbCS_LTE_CallingMosValueSum;
	/**
	 * 主叫占用2/3G，被叫占用LTE时,被叫MOS采样点数
	 */
	private Long m_ulCS_LTE_CalledMosPointNum;
	/**
	 * 主叫占用2/3G，被叫占用LTE时,被叫MOS值累积和
	 */
	private Float m_dbCS_LTE_CalledMosValueSum;
	/**
	 * 主被叫都在2/3G时,主叫MOS采样点数
	 */
	private Long m_ulCS_CS_CallingMosPointNum;
	/**
	 * 主被叫都在2/3G时,主叫MOS值累积和
	 */
	private Float m_dbCS_CS_CallingMosValueSum;
	/**
	 * 主被叫都在2/3G时,被叫MOS采样点数
	 */
	private Long m_ulCS_CS_CalledMosPointNum;
	/**
	 * 主被叫都在2/3G时,被叫MOS值累积和
	 */
	private Float m_dbCS_CS_CalledMosValueSum;

	/**
	 * 存储MOS差黑点的中心点
	 */
	private Float mosBadLatitude;
	private Float mosBadLongitude;

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

	@Column(name = "FIRST_MOS_TIME")
	public Long getInitMosTime() {
		return initMosTime;
	}

	@Transient
	public String getFirstMosTime() {
		if (null != initMosTime) {
			return new SimpleDateFormat("MM-dd HH:mm:ss.SSS").format(new Date(
					initMosTime));
		}
		return null;
	}

	public void setInitMosTime(Long initMosTime) {
		this.initMosTime = initMosTime;
	}

	/**
	 * @return the m_ulMosPointNumm_ulMosPointNum
	 */
	@Column(name = "MOSPOINT_NUM")
	public Long getM_ulMosPointNum() {
		return m_ulMosPointNum;
	}

	/**
	 * @param m_ulMosPointNum
	 *            the m_ulMosPointNum to set
	 */
	public void setM_ulMosPointNum(Long m_ulMosPointNum) {
		this.m_ulMosPointNum = m_ulMosPointNum;
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
	 * @return the m_ulBlerFirstRequestNumm_ulBlerFirstRequestNum
	 */
	@Column(name = "BLER_FIRSTREQUEST_NUM")
	public Long getM_ulBlerFirstRequestNum() {
		return m_ulBlerFirstRequestNum;
	}

	/**
	 * @param m_ulBlerFirstRequestNum
	 *            the m_ulBlerFirstRequestNum to set
	 */
	public void setM_ulBlerFirstRequestNum(Long m_ulBlerFirstRequestNum) {
		this.m_ulBlerFirstRequestNum = m_ulBlerFirstRequestNum;
	}

	/**
	 * @return the m_ulBlerFirstSuccessNumm_ulBlerFirstSuccessNum
	 */
	@Column(name = "BLER_FIRSTSUCCESS_NUM")
	public Long getM_ulBlerFirstSuccessNum() {
		return m_ulBlerFirstSuccessNum;
	}

	/**
	 * @param m_ulBlerFirstSuccessNum
	 *            the m_ulBlerFirstSuccessNum to set
	 */
	public void setM_ulBlerFirstSuccessNum(Long m_ulBlerFirstSuccessNum) {
		this.m_ulBlerFirstSuccessNum = m_ulBlerFirstSuccessNum;
	}

	/**
	 * @return the m_ulBlerAgainSuccessNumm_ulBlerAgainSuccessNum
	 */
	@Column(name = "BLER_AGAINSUCCESS_NUM")
	public Long getM_ulBlerAgainSuccessNum() {
		return m_ulBlerAgainSuccessNum;
	}

	/**
	 * @param m_ulBlerAgainSuccessNum
	 *            the m_ulBlerAgainSuccessNum to set
	 */
	public void setM_ulBlerAgainSuccessNum(Long m_ulBlerAgainSuccessNum) {
		this.m_ulBlerAgainSuccessNum = m_ulBlerAgainSuccessNum;
	}

	/**
	 * @return the m_dbRTPSendVoIPDataPackageNumm_dbRTPSendVoIPDataPackageNum
	 */
	@Column(name = "RTP_SENDVOIP_DATAPACKAGE_NUM")
	public Float getM_dbRTPSendVoIPDataPackageNum() {
		return m_dbRTPSendVoIPDataPackageNum;
	}

	/**
	 * @param m_dbRTPSendVoIPDataPackageNum
	 *            the m_dbRTPSendVoIPDataPackageNum to set
	 */
	public void setM_dbRTPSendVoIPDataPackageNum(
			Float m_dbRTPSendVoIPDataPackageNum) {
		this.m_dbRTPSendVoIPDataPackageNum = m_dbRTPSendVoIPDataPackageNum;
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
	 * @return the m_dbRTPShakem_dbRTPShake
	 */
	// @Column(name = "RTP_SHAKE")
	// public Float getM_dbRTPShake() {
	// return m_dbRTPShake;
	// }

	/**
	 * @param m_dbRTPShake
	 *            the m_dbRTPShake to set
	 */
	// public void setM_dbRTPShake(Float m_dbRTPShake) {
	// this.m_dbRTPShake = m_dbRTPShake;
	// }

	/**
	 * @return the m_dbTDLTestTimem_dbTDLTestTime
	 */
	@Column(name = "TDL_TESTTIME")
	public Float getM_dbTDLTestTime() {
		return m_dbTDLTestTime;
	}

	/**
	 * @param m_dbTDLTestTime
	 *            the m_dbTDLTestTime to set
	 */
	public void setM_dbTDLTestTime(Float m_dbTDLTestTime) {
		this.m_dbTDLTestTime = m_dbTDLTestTime;
	}

	/**
	 * @return the m_dbTDSTestTimem_dbTDSTestTime
	 */
	@Column(name = "TDS_TESTTIME")
	public Float getM_dbTDSTestTime() {
		return m_dbTDSTestTime;
	}

	/**
	 * @param m_dbTDSTestTime
	 *            the m_dbTDSTestTime to set
	 */
	public void setM_dbTDSTestTime(Float m_dbTDSTestTime) {
		this.m_dbTDSTestTime = m_dbTDSTestTime;
	}

	/**
	 * @return the m_dbGSMTestTimem_dbGSMTestTime
	 */
	@Column(name = "GSM_TESTTIME")
	public Float getM_dbGSMTestTime() {
		return m_dbGSMTestTime;
	}

	/**
	 * @param m_dbGSMTestTime
	 *            the m_dbGSMTestTime to set
	 */
	public void setM_dbGSMTestTime(Float m_dbGSMTestTime) {
		this.m_dbGSMTestTime = m_dbGSMTestTime;
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
	 * @return the m_ulCanShuCellNumm_ulCanShuCellNum
	 */
	@Column(name = "CANSHU_CELLNUM")
	public Long getM_ulCanShuCellNum() {
		return m_ulCanShuCellNum;
	}

	/**
	 * @param m_ulCanShuCellNum
	 *            the m_ulCanShuCellNum to set
	 */
	public void setM_ulCanShuCellNum(Long m_ulCanShuCellNum) {
		this.m_ulCanShuCellNum = m_ulCanShuCellNum;
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
	 * @return the m_ulLTE_LTE_CallingMosPointNumm_ulLTE_LTE_CallingMosPointNum
	 */
	@Column(name = "LTE_LTE_CALLING_MOSPOINTNUM")
	public Long getM_ulLTE_LTE_CallingMosPointNum() {
		return m_ulLTE_LTE_CallingMosPointNum;
	}

	/**
	 * @param m_ulLTE_LTE_CallingMosPointNum
	 *            the m_ulLTE_LTE_CallingMosPointNum to set
	 */
	public void setM_ulLTE_LTE_CallingMosPointNum(
			Long m_ulLTE_LTE_CallingMosPointNum) {
		this.m_ulLTE_LTE_CallingMosPointNum = m_ulLTE_LTE_CallingMosPointNum;
	}

	/**
	 * @return the m_dbLTE_LTE_CallingMosValueSumm_dbLTE_LTE_CallingMosValueSum
	 */
	@Column(name = "LTE_LTE_CALLING_MOSVALUESUM")
	public Float getM_dbLTE_LTE_CallingMosValueSum() {
		return m_dbLTE_LTE_CallingMosValueSum;
	}

	/**
	 * @param m_dbLTE_LTE_CallingMosValueSum
	 *            the m_dbLTE_LTE_CallingMosValueSum to set
	 */
	public void setM_dbLTE_LTE_CallingMosValueSum(
			Float m_dbLTE_LTE_CallingMosValueSum) {
		this.m_dbLTE_LTE_CallingMosValueSum = m_dbLTE_LTE_CallingMosValueSum;
	}

	/**
	 * @return the m_ulLTE_LTE_CalledMosPointNumm_ulLTE_LTE_CalledMosPointNum
	 */
	@Column(name = "LTE_LTE_CALLED_MOSPOINTNUM")
	public Long getM_ulLTE_LTE_CalledMosPointNum() {
		return m_ulLTE_LTE_CalledMosPointNum;
	}

	/**
	 * @param m_ulLTE_LTE_CalledMosPointNum
	 *            the m_ulLTE_LTE_CalledMosPointNum to set
	 */
	public void setM_ulLTE_LTE_CalledMosPointNum(
			Long m_ulLTE_LTE_CalledMosPointNum) {
		this.m_ulLTE_LTE_CalledMosPointNum = m_ulLTE_LTE_CalledMosPointNum;
	}

	/**
	 * @return the m_dbLTE_LTE_CalledMosValueSumm_dbLTE_LTE_CalledMosValueSum
	 */
	@Column(name = "LTE_LTE_CALLED_MOSVALUESUM")
	public Float getM_dbLTE_LTE_CalledMosValueSum() {
		return m_dbLTE_LTE_CalledMosValueSum;
	}

	/**
	 * @param m_dbLTE_LTE_CalledMosValueSum
	 *            the m_dbLTE_LTE_CalledMosValueSum to set
	 */
	public void setM_dbLTE_LTE_CalledMosValueSum(
			Float m_dbLTE_LTE_CalledMosValueSum) {
		this.m_dbLTE_LTE_CalledMosValueSum = m_dbLTE_LTE_CalledMosValueSum;
	}

	/**
	 * @return the m_ulLTE_CS_CallingMosPointNumm_ulLTE_CS_CallingMosPointNum
	 */
	@Column(name = "LTE_CS_CALLING_MOSPOINTNUM")
	public Long getM_ulLTE_CS_CallingMosPointNum() {
		return m_ulLTE_CS_CallingMosPointNum;
	}

	/**
	 * @param m_ulLTE_CS_CallingMosPointNum
	 *            the m_ulLTE_CS_CallingMosPointNum to set
	 */
	public void setM_ulLTE_CS_CallingMosPointNum(
			Long m_ulLTE_CS_CallingMosPointNum) {
		this.m_ulLTE_CS_CallingMosPointNum = m_ulLTE_CS_CallingMosPointNum;
	}

	/**
	 * @return the m_dbLTE_CS_CallingMosValueSumm_dbLTE_CS_CallingMosValueSum
	 */
	@Column(name = "LTE_CS_CALLING_MOSVALUESUM")
	public Float getM_dbLTE_CS_CallingMosValueSum() {
		return m_dbLTE_CS_CallingMosValueSum;
	}

	/**
	 * @param m_dbLTE_CS_CallingMosValueSum
	 *            the m_dbLTE_CS_CallingMosValueSum to set
	 */
	public void setM_dbLTE_CS_CallingMosValueSum(
			Float m_dbLTE_CS_CallingMosValueSum) {
		this.m_dbLTE_CS_CallingMosValueSum = m_dbLTE_CS_CallingMosValueSum;
	}

	/**
	 * @return the m_ulLTE_CS_CalledMosPointNumm_ulLTE_CS_CalledMosPointNum
	 */
	@Column(name = "LTE_CS_CALLED_MOSPOINTNUM")
	public Long getM_ulLTE_CS_CalledMosPointNum() {
		return m_ulLTE_CS_CalledMosPointNum;
	}

	/**
	 * @param m_ulLTE_CS_CalledMosPointNum
	 *            the m_ulLTE_CS_CalledMosPointNum to set
	 */
	public void setM_ulLTE_CS_CalledMosPointNum(
			Long m_ulLTE_CS_CalledMosPointNum) {
		this.m_ulLTE_CS_CalledMosPointNum = m_ulLTE_CS_CalledMosPointNum;
	}

	/**
	 * @return the m_dbLTE_CS_CalledMosValueSumm_dbLTE_CS_CalledMosValueSum
	 */
	@Column(name = "LTE_CS_CALLED_MOSVALUESUM")
	public Float getM_dbLTE_CS_CalledMosValueSum() {
		return m_dbLTE_CS_CalledMosValueSum;
	}

	/**
	 * @param m_dbLTE_CS_CalledMosValueSum
	 *            the m_dbLTE_CS_CalledMosValueSum to set
	 */
	public void setM_dbLTE_CS_CalledMosValueSum(
			Float m_dbLTE_CS_CalledMosValueSum) {
		this.m_dbLTE_CS_CalledMosValueSum = m_dbLTE_CS_CalledMosValueSum;
	}

	/**
	 * @return the m_ulCS_LTE_CallingMosPointNumm_ulCS_LTE_CallingMosPointNum
	 */
	@Column(name = "CS_LTE_CALLING_MOSPOINTNUM")
	public Long getM_ulCS_LTE_CallingMosPointNum() {
		return m_ulCS_LTE_CallingMosPointNum;
	}

	/**
	 * @param m_ulCS_LTE_CallingMosPointNum
	 *            the m_ulCS_LTE_CallingMosPointNum to set
	 */
	public void setM_ulCS_LTE_CallingMosPointNum(
			Long m_ulCS_LTE_CallingMosPointNum) {
		this.m_ulCS_LTE_CallingMosPointNum = m_ulCS_LTE_CallingMosPointNum;
	}

	/**
	 * @return the m_dbCS_LTE_CallingMosValueSumm_dbCS_LTE_CallingMosValueSum
	 */
	@Column(name = "CS_LTE_CALLING_MOSVALUESUM")
	public Float getM_dbCS_LTE_CallingMosValueSum() {
		return m_dbCS_LTE_CallingMosValueSum;
	}

	/**
	 * @param m_dbCS_LTE_CallingMosValueSum
	 *            the m_dbCS_LTE_CallingMosValueSum to set
	 */
	public void setM_dbCS_LTE_CallingMosValueSum(
			Float m_dbCS_LTE_CallingMosValueSum) {
		this.m_dbCS_LTE_CallingMosValueSum = m_dbCS_LTE_CallingMosValueSum;
	}

	/**
	 * @return the m_ulCS_LTE_CalledMosPointNumm_ulCS_LTE_CalledMosPointNum
	 */
	@Column(name = "CS_LTE_CALLED_MOSPOINTNUM")
	public Long getM_ulCS_LTE_CalledMosPointNum() {
		return m_ulCS_LTE_CalledMosPointNum;
	}

	/**
	 * @param m_ulCS_LTE_CalledMosPointNum
	 *            the m_ulCS_LTE_CalledMosPointNum to set
	 */
	public void setM_ulCS_LTE_CalledMosPointNum(
			Long m_ulCS_LTE_CalledMosPointNum) {
		this.m_ulCS_LTE_CalledMosPointNum = m_ulCS_LTE_CalledMosPointNum;
	}

	/**
	 * @return the m_dbCS_LTE_CalledMosValueSumm_dbCS_LTE_CalledMosValueSum
	 */
	@Column(name = "CS_LTE_CALLED_MOSVALUESUM")
	public Float getM_dbCS_LTE_CalledMosValueSum() {
		return m_dbCS_LTE_CalledMosValueSum;
	}

	/**
	 * @param m_dbCS_LTE_CalledMosValueSum
	 *            the m_dbCS_LTE_CalledMosValueSum to set
	 */
	public void setM_dbCS_LTE_CalledMosValueSum(
			Float m_dbCS_LTE_CalledMosValueSum) {
		this.m_dbCS_LTE_CalledMosValueSum = m_dbCS_LTE_CalledMosValueSum;
	}

	/**
	 * @return the m_ulCS_CS_CallingMosPointNumm_ulCS_CS_CallingMosPointNum
	 */
	@Column(name = "CS_CS_CALLING_MOSPOINTNUM")
	public Long getM_ulCS_CS_CallingMosPointNum() {
		return m_ulCS_CS_CallingMosPointNum;
	}

	/**
	 * @param m_ulCS_CS_CallingMosPointNum
	 *            the m_ulCS_CS_CallingMosPointNum to set
	 */
	public void setM_ulCS_CS_CallingMosPointNum(
			Long m_ulCS_CS_CallingMosPointNum) {
		this.m_ulCS_CS_CallingMosPointNum = m_ulCS_CS_CallingMosPointNum;
	}

	/**
	 * @return the m_dbCS_CS_CallingMosValueSumm_dbCS_CS_CallingMosValueSum
	 */
	@Column(name = "CS_CS_CALLING_MOSVALUESUM")
	public Float getM_dbCS_CS_CallingMosValueSum() {
		return m_dbCS_CS_CallingMosValueSum;
	}

	/**
	 * @param m_dbCS_CS_CallingMosValueSum
	 *            the m_dbCS_CS_CallingMosValueSum to set
	 */
	public void setM_dbCS_CS_CallingMosValueSum(
			Float m_dbCS_CS_CallingMosValueSum) {
		this.m_dbCS_CS_CallingMosValueSum = m_dbCS_CS_CallingMosValueSum;
	}

	/**
	 * @return the m_ulCS_CS_CalledMosPointNumm_ulCS_CS_CalledMosPointNum
	 */
	@Column(name = "CS_CS_CALLED_MOSPOINTNUM")
	public Long getM_ulCS_CS_CalledMosPointNum() {
		return m_ulCS_CS_CalledMosPointNum;
	}

	/**
	 * @param m_ulCS_CS_CalledMosPointNum
	 *            the m_ulCS_CS_CalledMosPointNum to set
	 */
	public void setM_ulCS_CS_CalledMosPointNum(Long m_ulCS_CS_CalledMosPointNum) {
		this.m_ulCS_CS_CalledMosPointNum = m_ulCS_CS_CalledMosPointNum;
	}

	/**
	 * @return the m_dbCS_CS_CalledMosValueSumm_dbCS_CS_CalledMosValueSum
	 */
	@Column(name = "CS_CS_CALLED_MOSVALUESUM")
	public Float getM_dbCS_CS_CalledMosValueSum() {
		return m_dbCS_CS_CalledMosValueSum;
	}

	/**
	 * @param m_dbCS_CS_CalledMosValueSum
	 *            the m_dbCS_CS_CalledMosValueSum to set
	 */
	public void setM_dbCS_CS_CalledMosValueSum(Float m_dbCS_CS_CalledMosValueSum) {
		this.m_dbCS_CS_CalledMosValueSum = m_dbCS_CS_CalledMosValueSum;
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
	 * @return the mosAvgmosAvg
	 */
	@Column(name = "MOS_AVG")
	public Float getMosAvg() {
		return mosAvg;
	}

	/**
	 * @param mosAvg
	 *            the mosAvg to set
	 */
	public void setMosAvg(Float mosAvg) {
		this.mosAvg = mosAvg;
	}

	/**
	 * @return the mosTypemosType
	 */
	@Column(name = "MOS_TYPE")
	public String getMosType() {
		return mosType;
	}

	/**
	 * @param mosType
	 *            the mosType to set
	 */
	public void setMosType(String mosType) {
		this.mosType = mosType;
	}

	/**
	 * @return the m_dbRTPSumm_dbRTPSum
	 */
	@Column(name = "RTP_SUM")
	public Float getM_dbRTPSum() {
		return m_dbRTPSum;
	}

	/**
	 * @param m_dbRTPSum
	 *            the m_dbRTPSum to set
	 */
	public void setM_dbRTPSum(Float m_dbRTPSum) {
		this.m_dbRTPSum = m_dbRTPSum;
	}

	/**
	 * @return the m_ulRTPNumm_ulRTPNum
	 */
	@Column(name = "RTP_NUM")
	public Long getM_ulRTPNum() {
		return m_ulRTPNum;
	}

	/**
	 * @param m_ulRTPNum
	 *            the m_ulRTPNum to set
	 */
	public void setM_ulRTPNum(Long m_ulRTPNum) {
		this.m_ulRTPNum = m_ulRTPNum;
	}

	/**
	 * 
	 * @return the lBler
	 */
	@Column(name = "IBLER")
	public Float getlBler() {
		return lBler;
	}

	/**
	 * 
	 * @param lBler
	 *            the lBler to set
	 */
	public void setlBler(Float lBler) {
		this.lBler = lBler;
	}

	/**
	 * 
	 * @return the rBler
	 */
	@Column(name = "RBLER")
	public Float getrBler() {
		return rBler;
	}

	/**
	 * 
	 * @param rBler
	 *            the rBler to set
	 */
	public void setrBler(Float rBler) {
		this.rBler = rBler;
	}

	/**
	 * @return the m_ulMosOver30PointNumm_ulMosOver30PointNum
	 */
	@Column(name = "MOS_30POINT_NUM")
	public Long getM_ulMosOver30PointNum() {
		return m_ulMosOver30PointNum;
	}

	/**
	 * @param m_ulMosOver30PointNum
	 *            the m_ulMosOver30PointNum to set
	 */
	public void setM_ulMosOver30PointNum(Long m_ulMosOver30PointNum) {
		this.m_ulMosOver30PointNum = m_ulMosOver30PointNum;
	}

	/**
	 * @return the m_ulMosOver35PointNumm_ulMosOver35PointNum
	 */
	@Column(name = "MOS_35POINT_NUM")
	public Long getM_ulMosOver35PointNum() {
		return m_ulMosOver35PointNum;
	}

	/**
	 * @param m_ulMosOver35PointNum
	 *            the m_ulMosOver35PointNum to set
	 */
	public void setM_ulMosOver35PointNum(Long m_ulMosOver35PointNum) {
		this.m_ulMosOver35PointNum = m_ulMosOver35PointNum;
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
	 * @return the m_ulLteIntraHoSuccNumm_ulLteIntraHoSuccNum
	 */
	@Column(name = "LTE_INTRAHO_SUCC_NUM")
	public Long getM_ulLteIntraHoSuccNum() {
		return m_ulLteIntraHoSuccNum;
	}

	/**
	 * @param m_ulLteIntraHoSuccNum
	 *            the m_ulLteIntraHoSuccNum to set
	 */
	public void setM_ulLteIntraHoSuccNum(Long m_ulLteIntraHoSuccNum) {
		this.m_ulLteIntraHoSuccNum = m_ulLteIntraHoSuccNum;
	}

	/**
	 * @return the m_ulLteInterHoSuccNumm_ulLteInterHoSuccNum
	 */
	@Column(name = "LTE_INTERHO_SUCC_NUM")
	public Long getM_ulLteInterHoSuccNum() {
		return m_ulLteInterHoSuccNum;
	}

	/**
	 * @param m_ulLteInterHoSuccNum
	 *            the m_ulLteInterHoSuccNum to set
	 */
	public void setM_ulLteInterHoSuccNum(Long m_ulLteInterHoSuccNum) {
		this.m_ulLteInterHoSuccNum = m_ulLteInterHoSuccNum;
	}

	/**
	 * @return the m_ulLteIntraHoReqNumm_ulLteIntraHoReqNum
	 */
	@Column(name = "LTE_INTRAHO_REQ_NUM")
	public Long getM_ulLteIntraHoReqNum() {
		return m_ulLteIntraHoReqNum;
	}

	/**
	 * @param m_ulLteIntraHoReqNum
	 *            the m_ulLteIntraHoReqNum to set
	 */
	public void setM_ulLteIntraHoReqNum(Long m_ulLteIntraHoReqNum) {
		this.m_ulLteIntraHoReqNum = m_ulLteIntraHoReqNum;
	}

	/**
	 * @return the m_ulLteInterHoReqNumm_ulLteInterHoReqNum
	 */
	@Column(name = "LTE_INTERHO_REQ_NUM")
	public Long getM_ulLteInterHoReqNum() {
		return m_ulLteInterHoReqNum;
	}

	/**
	 * @param m_ulLteInterHoReqNum
	 *            the m_ulLteInterHoReqNum to set
	 */
	public void setM_ulLteInterHoReqNum(Long m_ulLteInterHoReqNum) {
		this.m_ulLteInterHoReqNum = m_ulLteInterHoReqNum;
	}

	/**
	 * @return the m_ulLteCoverNumm_ulLteCoverNum
	 */
	@Column(name = "LTE_COVER_NUM")
	public Long getM_ulLteCoverNum() {
		return m_ulLteCoverNum;
	}

	/**
	 * @param m_ulLteCoverNum
	 *            the m_ulLteCoverNum to set
	 */
	public void setM_ulLteCoverNum(Long m_ulLteCoverNum) {
		this.m_ulLteCoverNum = m_ulLteCoverNum;
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
	 * @return the mosBadLatitudemosBadLatitude
	 */
	@Transient
	public Float getMosBadLatitude() {
		return mosBadLatitude;
	}

	/**
	 * @param mosBadLatitude
	 *            the mosBadLatitude to set
	 */
	public void setMosBadLatitude(Float mosBadLatitude) {
		this.mosBadLatitude = mosBadLatitude;
	}

	/**
	 * @return the mosBadLongitudemosBadLongitude
	 */
	@Transient
	public Float getMosBadLongitude() {
		return mosBadLongitude;
	}

	/**
	 * @param mosBadLongitude
	 *            the mosBadLongitude to set
	 */
	public void setMosBadLongitude(Float mosBadLongitude) {
		this.mosBadLongitude = mosBadLongitude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((beginLatitude == null) ? 0 : beginLatitude.hashCode());
		result = prime * result
				+ ((beginLongitude == null) ? 0 : beginLongitude.hashCode());
		result = prime * result
				+ ((courseLatitude == null) ? 0 : courseLatitude.hashCode());
		result = prime * result
				+ ((courseLongitude == null) ? 0 : courseLongitude.hashCode());
		result = prime * result
				+ ((endLatitude == null) ? 0 : endLatitude.hashCode());
		result = prime * result
				+ ((endLongitude == null) ? 0 : endLongitude.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((initMosTime == null) ? 0 : initMosTime.hashCode());
		result = prime * result + ((lBler == null) ? 0 : lBler.hashCode());
		result = prime
				* result
				+ ((m_dbCS_CS_CalledMosValueSum == null) ? 0
						: m_dbCS_CS_CalledMosValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbCS_CS_CallingMosValueSum == null) ? 0
						: m_dbCS_CS_CallingMosValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbCS_LTE_CalledMosValueSum == null) ? 0
						: m_dbCS_LTE_CalledMosValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbCS_LTE_CallingMosValueSum == null) ? 0
						: m_dbCS_LTE_CallingMosValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbContinueTime == null) ? 0 : m_dbContinueTime.hashCode());
		result = prime * result
				+ ((m_dbDistance == null) ? 0 : m_dbDistance.hashCode());
		result = prime * result
				+ ((m_dbGSMTestTime == null) ? 0 : m_dbGSMTestTime.hashCode());
		result = prime
				* result
				+ ((m_dbLTE_CS_CalledMosValueSum == null) ? 0
						: m_dbLTE_CS_CalledMosValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbLTE_CS_CallingMosValueSum == null) ? 0
						: m_dbLTE_CS_CallingMosValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbLTE_LTE_CalledMosValueSum == null) ? 0
						: m_dbLTE_LTE_CalledMosValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbLTE_LTE_CallingMosValueSum == null) ? 0
						: m_dbLTE_LTE_CallingMosValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbLTE_MosValueSum == null) ? 0 : m_dbLTE_MosValueSum
						.hashCode());
		result = prime
				* result
				+ ((m_dbRTPReceiveDataPackageNum == null) ? 0
						: m_dbRTPReceiveDataPackageNum.hashCode());
		result = prime
				* result
				+ ((m_dbRTPSendVoIPDataPackageNum == null) ? 0
						: m_dbRTPSendVoIPDataPackageNum.hashCode());
		result = prime * result
				+ ((m_dbRTPSum == null) ? 0 : m_dbRTPSum.hashCode());
		result = prime
				* result
				+ ((m_dbRsrpValueSum == null) ? 0 : m_dbRsrpValueSum.hashCode());
		result = prime
				* result
				+ ((m_dbSinrValueSum == null) ? 0 : m_dbSinrValueSum.hashCode());
		result = prime * result
				+ ((m_dbTDLTestTime == null) ? 0 : m_dbTDLTestTime.hashCode());
		result = prime * result
				+ ((m_dbTDSTestTime == null) ? 0 : m_dbTDSTestTime.hashCode());
		result = prime * result
				+ ((m_stRoadName == null) ? 0 : m_stRoadName.hashCode());
		result = prime
				* result
				+ ((m_ulBlerAgainSuccessNum == null) ? 0
						: m_ulBlerAgainSuccessNum.hashCode());
		result = prime
				* result
				+ ((m_ulBlerFirstRequestNum == null) ? 0
						: m_ulBlerFirstRequestNum.hashCode());
		result = prime
				* result
				+ ((m_ulBlerFirstSuccessNum == null) ? 0
						: m_ulBlerFirstSuccessNum.hashCode());
		result = prime
				* result
				+ ((m_ulCS_CS_CalledMosPointNum == null) ? 0
						: m_ulCS_CS_CalledMosPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulCS_CS_CallingMosPointNum == null) ? 0
						: m_ulCS_CS_CallingMosPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulCS_LTE_CalledMosPointNum == null) ? 0
						: m_ulCS_LTE_CalledMosPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulCS_LTE_CallingMosPointNum == null) ? 0
						: m_ulCS_LTE_CallingMosPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulCanShuCellNum == null) ? 0 : m_ulCanShuCellNum
						.hashCode());
		result = prime * result
				+ ((m_ulCellNum == null) ? 0 : m_ulCellNum.hashCode());
		result = prime
				* result
				+ ((m_ulLTE_CS_CalledMosPointNum == null) ? 0
						: m_ulLTE_CS_CalledMosPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulLTE_CS_CallingMosPointNum == null) ? 0
						: m_ulLTE_CS_CallingMosPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulLTE_LTE_CalledMosPointNum == null) ? 0
						: m_ulLTE_LTE_CalledMosPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulLTE_LTE_CallingMosPointNum == null) ? 0
						: m_ulLTE_LTE_CallingMosPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulLTE_MosPointNum == null) ? 0 : m_ulLTE_MosPointNum
						.hashCode());
		result = prime
				* result
				+ ((m_ulLinQuCellNum == null) ? 0 : m_ulLinQuCellNum.hashCode());
		result = prime * result
				+ ((m_ulLteCoverNum == null) ? 0 : m_ulLteCoverNum.hashCode());
		result = prime
				* result
				+ ((m_ulLteInterHoReqNum == null) ? 0 : m_ulLteInterHoReqNum
						.hashCode());
		result = prime
				* result
				+ ((m_ulLteInterHoSuccNum == null) ? 0 : m_ulLteInterHoSuccNum
						.hashCode());
		result = prime
				* result
				+ ((m_ulLteIntraHoReqNum == null) ? 0 : m_ulLteIntraHoReqNum
						.hashCode());
		result = prime
				* result
				+ ((m_ulLteIntraHoSuccNum == null) ? 0 : m_ulLteIntraHoSuccNum
						.hashCode());
		result = prime
				* result
				+ ((m_ulMosOver30PointNum == null) ? 0 : m_ulMosOver30PointNum
						.hashCode());
		result = prime
				* result
				+ ((m_ulMosOver35PointNum == null) ? 0 : m_ulMosOver35PointNum
						.hashCode());
		result = prime * result
				+ ((m_ulMosPointNum == null) ? 0 : m_ulMosPointNum.hashCode());
		result = prime * result
				+ ((m_ulPCICellNum == null) ? 0 : m_ulPCICellNum.hashCode());
		result = prime * result
				+ ((m_ulRTPNum == null) ? 0 : m_ulRTPNum.hashCode());
		result = prime
				* result
				+ ((m_ulRsrpOrSinrPointNum == null) ? 0
						: m_ulRsrpOrSinrPointNum.hashCode());
		result = prime
				* result
				+ ((m_ulTianKuiCellNum == null) ? 0 : m_ulTianKuiCellNum
						.hashCode());
		result = prime * result + ((mosAvg == null) ? 0 : mosAvg.hashCode());
		result = prime * result + ((mosType == null) ? 0 : mosType.hashCode());
		result = prime * result + ((rBler == null) ? 0 : rBler.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VolteQualityBadRoad other = (VolteQualityBadRoad) obj;
		if (beginLatitude == null) {
			if (other.beginLatitude != null)
				return false;
		} else if (!beginLatitude.equals(other.beginLatitude))
			return false;
		if (beginLongitude == null) {
			if (other.beginLongitude != null)
				return false;
		} else if (!beginLongitude.equals(other.beginLongitude))
			return false;
		if (courseLatitude == null) {
			if (other.courseLatitude != null)
				return false;
		} else if (!courseLatitude.equals(other.courseLatitude))
			return false;
		if (courseLongitude == null) {
			if (other.courseLongitude != null)
				return false;
		} else if (!courseLongitude.equals(other.courseLongitude))
			return false;
		if (endLatitude == null) {
			if (other.endLatitude != null)
				return false;
		} else if (!endLatitude.equals(other.endLatitude))
			return false;
		if (endLongitude == null) {
			if (other.endLongitude != null)
				return false;
		} else if (!endLongitude.equals(other.endLongitude))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (initMosTime == null) {
			if (other.initMosTime != null)
				return false;
		} else if (!initMosTime.equals(other.initMosTime))
			return false;
		if (lBler == null) {
			if (other.lBler != null)
				return false;
		} else if (!lBler.equals(other.lBler))
			return false;
		if (m_dbCS_CS_CalledMosValueSum == null) {
			if (other.m_dbCS_CS_CalledMosValueSum != null)
				return false;
		} else if (!m_dbCS_CS_CalledMosValueSum
				.equals(other.m_dbCS_CS_CalledMosValueSum))
			return false;
		if (m_dbCS_CS_CallingMosValueSum == null) {
			if (other.m_dbCS_CS_CallingMosValueSum != null)
				return false;
		} else if (!m_dbCS_CS_CallingMosValueSum
				.equals(other.m_dbCS_CS_CallingMosValueSum))
			return false;
		if (m_dbCS_LTE_CalledMosValueSum == null) {
			if (other.m_dbCS_LTE_CalledMosValueSum != null)
				return false;
		} else if (!m_dbCS_LTE_CalledMosValueSum
				.equals(other.m_dbCS_LTE_CalledMosValueSum))
			return false;
		if (m_dbCS_LTE_CallingMosValueSum == null) {
			if (other.m_dbCS_LTE_CallingMosValueSum != null)
				return false;
		} else if (!m_dbCS_LTE_CallingMosValueSum
				.equals(other.m_dbCS_LTE_CallingMosValueSum))
			return false;
		if (m_dbContinueTime == null) {
			if (other.m_dbContinueTime != null)
				return false;
		} else if (!m_dbContinueTime.equals(other.m_dbContinueTime))
			return false;
		if (m_dbDistance == null) {
			if (other.m_dbDistance != null)
				return false;
		} else if (!m_dbDistance.equals(other.m_dbDistance))
			return false;
		if (m_dbGSMTestTime == null) {
			if (other.m_dbGSMTestTime != null)
				return false;
		} else if (!m_dbGSMTestTime.equals(other.m_dbGSMTestTime))
			return false;
		if (m_dbLTE_CS_CalledMosValueSum == null) {
			if (other.m_dbLTE_CS_CalledMosValueSum != null)
				return false;
		} else if (!m_dbLTE_CS_CalledMosValueSum
				.equals(other.m_dbLTE_CS_CalledMosValueSum))
			return false;
		if (m_dbLTE_CS_CallingMosValueSum == null) {
			if (other.m_dbLTE_CS_CallingMosValueSum != null)
				return false;
		} else if (!m_dbLTE_CS_CallingMosValueSum
				.equals(other.m_dbLTE_CS_CallingMosValueSum))
			return false;
		if (m_dbLTE_LTE_CalledMosValueSum == null) {
			if (other.m_dbLTE_LTE_CalledMosValueSum != null)
				return false;
		} else if (!m_dbLTE_LTE_CalledMosValueSum
				.equals(other.m_dbLTE_LTE_CalledMosValueSum))
			return false;
		if (m_dbLTE_LTE_CallingMosValueSum == null) {
			if (other.m_dbLTE_LTE_CallingMosValueSum != null)
				return false;
		} else if (!m_dbLTE_LTE_CallingMosValueSum
				.equals(other.m_dbLTE_LTE_CallingMosValueSum))
			return false;
		if (m_dbLTE_MosValueSum == null) {
			if (other.m_dbLTE_MosValueSum != null)
				return false;
		} else if (!m_dbLTE_MosValueSum.equals(other.m_dbLTE_MosValueSum))
			return false;
		if (m_dbRTPReceiveDataPackageNum == null) {
			if (other.m_dbRTPReceiveDataPackageNum != null)
				return false;
		} else if (!m_dbRTPReceiveDataPackageNum
				.equals(other.m_dbRTPReceiveDataPackageNum))
			return false;
		if (m_dbRTPSendVoIPDataPackageNum == null) {
			if (other.m_dbRTPSendVoIPDataPackageNum != null)
				return false;
		} else if (!m_dbRTPSendVoIPDataPackageNum
				.equals(other.m_dbRTPSendVoIPDataPackageNum))
			return false;
		if (m_dbRTPSum == null) {
			if (other.m_dbRTPSum != null)
				return false;
		} else if (!m_dbRTPSum.equals(other.m_dbRTPSum))
			return false;
		if (m_dbRsrpValueSum == null) {
			if (other.m_dbRsrpValueSum != null)
				return false;
		} else if (!m_dbRsrpValueSum.equals(other.m_dbRsrpValueSum))
			return false;
		if (m_dbSinrValueSum == null) {
			if (other.m_dbSinrValueSum != null)
				return false;
		} else if (!m_dbSinrValueSum.equals(other.m_dbSinrValueSum))
			return false;
		if (m_dbTDLTestTime == null) {
			if (other.m_dbTDLTestTime != null)
				return false;
		} else if (!m_dbTDLTestTime.equals(other.m_dbTDLTestTime))
			return false;
		if (m_dbTDSTestTime == null) {
			if (other.m_dbTDSTestTime != null)
				return false;
		} else if (!m_dbTDSTestTime.equals(other.m_dbTDSTestTime))
			return false;
		if (m_stRoadName == null) {
			if (other.m_stRoadName != null)
				return false;
		} else if (!m_stRoadName.equals(other.m_stRoadName))
			return false;
		if (m_ulBlerAgainSuccessNum == null) {
			if (other.m_ulBlerAgainSuccessNum != null)
				return false;
		} else if (!m_ulBlerAgainSuccessNum
				.equals(other.m_ulBlerAgainSuccessNum))
			return false;
		if (m_ulBlerFirstRequestNum == null) {
			if (other.m_ulBlerFirstRequestNum != null)
				return false;
		} else if (!m_ulBlerFirstRequestNum
				.equals(other.m_ulBlerFirstRequestNum))
			return false;
		if (m_ulBlerFirstSuccessNum == null) {
			if (other.m_ulBlerFirstSuccessNum != null)
				return false;
		} else if (!m_ulBlerFirstSuccessNum
				.equals(other.m_ulBlerFirstSuccessNum))
			return false;
		if (m_ulCS_CS_CalledMosPointNum == null) {
			if (other.m_ulCS_CS_CalledMosPointNum != null)
				return false;
		} else if (!m_ulCS_CS_CalledMosPointNum
				.equals(other.m_ulCS_CS_CalledMosPointNum))
			return false;
		if (m_ulCS_CS_CallingMosPointNum == null) {
			if (other.m_ulCS_CS_CallingMosPointNum != null)
				return false;
		} else if (!m_ulCS_CS_CallingMosPointNum
				.equals(other.m_ulCS_CS_CallingMosPointNum))
			return false;
		if (m_ulCS_LTE_CalledMosPointNum == null) {
			if (other.m_ulCS_LTE_CalledMosPointNum != null)
				return false;
		} else if (!m_ulCS_LTE_CalledMosPointNum
				.equals(other.m_ulCS_LTE_CalledMosPointNum))
			return false;
		if (m_ulCS_LTE_CallingMosPointNum == null) {
			if (other.m_ulCS_LTE_CallingMosPointNum != null)
				return false;
		} else if (!m_ulCS_LTE_CallingMosPointNum
				.equals(other.m_ulCS_LTE_CallingMosPointNum))
			return false;
		if (m_ulCanShuCellNum == null) {
			if (other.m_ulCanShuCellNum != null)
				return false;
		} else if (!m_ulCanShuCellNum.equals(other.m_ulCanShuCellNum))
			return false;
		if (m_ulCellNum == null) {
			if (other.m_ulCellNum != null)
				return false;
		} else if (!m_ulCellNum.equals(other.m_ulCellNum))
			return false;
		if (m_ulLTE_CS_CalledMosPointNum == null) {
			if (other.m_ulLTE_CS_CalledMosPointNum != null)
				return false;
		} else if (!m_ulLTE_CS_CalledMosPointNum
				.equals(other.m_ulLTE_CS_CalledMosPointNum))
			return false;
		if (m_ulLTE_CS_CallingMosPointNum == null) {
			if (other.m_ulLTE_CS_CallingMosPointNum != null)
				return false;
		} else if (!m_ulLTE_CS_CallingMosPointNum
				.equals(other.m_ulLTE_CS_CallingMosPointNum))
			return false;
		if (m_ulLTE_LTE_CalledMosPointNum == null) {
			if (other.m_ulLTE_LTE_CalledMosPointNum != null)
				return false;
		} else if (!m_ulLTE_LTE_CalledMosPointNum
				.equals(other.m_ulLTE_LTE_CalledMosPointNum))
			return false;
		if (m_ulLTE_LTE_CallingMosPointNum == null) {
			if (other.m_ulLTE_LTE_CallingMosPointNum != null)
				return false;
		} else if (!m_ulLTE_LTE_CallingMosPointNum
				.equals(other.m_ulLTE_LTE_CallingMosPointNum))
			return false;
		if (m_ulLTE_MosPointNum == null) {
			if (other.m_ulLTE_MosPointNum != null)
				return false;
		} else if (!m_ulLTE_MosPointNum.equals(other.m_ulLTE_MosPointNum))
			return false;
		if (m_ulLinQuCellNum == null) {
			if (other.m_ulLinQuCellNum != null)
				return false;
		} else if (!m_ulLinQuCellNum.equals(other.m_ulLinQuCellNum))
			return false;
		if (m_ulLteCoverNum == null) {
			if (other.m_ulLteCoverNum != null)
				return false;
		} else if (!m_ulLteCoverNum.equals(other.m_ulLteCoverNum))
			return false;
		if (m_ulLteInterHoReqNum == null) {
			if (other.m_ulLteInterHoReqNum != null)
				return false;
		} else if (!m_ulLteInterHoReqNum.equals(other.m_ulLteInterHoReqNum))
			return false;
		if (m_ulLteInterHoSuccNum == null) {
			if (other.m_ulLteInterHoSuccNum != null)
				return false;
		} else if (!m_ulLteInterHoSuccNum.equals(other.m_ulLteInterHoSuccNum))
			return false;
		if (m_ulLteIntraHoReqNum == null) {
			if (other.m_ulLteIntraHoReqNum != null)
				return false;
		} else if (!m_ulLteIntraHoReqNum.equals(other.m_ulLteIntraHoReqNum))
			return false;
		if (m_ulLteIntraHoSuccNum == null) {
			if (other.m_ulLteIntraHoSuccNum != null)
				return false;
		} else if (!m_ulLteIntraHoSuccNum.equals(other.m_ulLteIntraHoSuccNum))
			return false;
		if (m_ulMosOver30PointNum == null) {
			if (other.m_ulMosOver30PointNum != null)
				return false;
		} else if (!m_ulMosOver30PointNum.equals(other.m_ulMosOver30PointNum))
			return false;
		if (m_ulMosOver35PointNum == null) {
			if (other.m_ulMosOver35PointNum != null)
				return false;
		} else if (!m_ulMosOver35PointNum.equals(other.m_ulMosOver35PointNum))
			return false;
		if (m_ulMosPointNum == null) {
			if (other.m_ulMosPointNum != null)
				return false;
		} else if (!m_ulMosPointNum.equals(other.m_ulMosPointNum))
			return false;
		if (m_ulPCICellNum == null) {
			if (other.m_ulPCICellNum != null)
				return false;
		} else if (!m_ulPCICellNum.equals(other.m_ulPCICellNum))
			return false;
		if (m_ulRTPNum == null) {
			if (other.m_ulRTPNum != null)
				return false;
		} else if (!m_ulRTPNum.equals(other.m_ulRTPNum))
			return false;
		if (m_ulRsrpOrSinrPointNum == null) {
			if (other.m_ulRsrpOrSinrPointNum != null)
				return false;
		} else if (!m_ulRsrpOrSinrPointNum.equals(other.m_ulRsrpOrSinrPointNum))
			return false;
		if (m_ulTianKuiCellNum == null) {
			if (other.m_ulTianKuiCellNum != null)
				return false;
		} else if (!m_ulTianKuiCellNum.equals(other.m_ulTianKuiCellNum))
			return false;
		if (mosAvg == null) {
			if (other.mosAvg != null)
				return false;
		} else if (!mosAvg.equals(other.mosAvg))
			return false;
		if (mosType == null) {
			if (other.mosType != null)
				return false;
		} else if (!mosType.equals(other.mosType))
			return false;
		if (rBler == null) {
			if (other.rBler != null)
				return false;
		} else if (!rBler.equals(other.rBler))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;

		return true;
	}

}
