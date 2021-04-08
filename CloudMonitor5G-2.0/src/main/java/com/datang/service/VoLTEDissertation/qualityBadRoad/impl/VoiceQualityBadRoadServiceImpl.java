/**
 * 
 */
package com.datang.service.VoLTEDissertation.qualityBadRoad.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.constant.TwoDimensionalChartType;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.CoreNetworkDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.DisturbCellInfoDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.DisturbDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.DisturbPCIAdjustDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.DisturbSanChaoCellDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.DisturbTianKuiAdjustDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.NbCellDeficiencyCoPerfDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.NbCellDeficiencyDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.NbCellDeficiencyGSMAddAdviceDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.NbCellDeficiencyLTEAddAdviceDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.OtherCellInfoDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.OtherDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.ParamErrorDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.ParamErrorOptimizeAdviceDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.QualityBadRoadDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.WeakCoverAdviceAddStationDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.WeakCoverCellInfoDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.WeakCoverDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.WeakCoverTianKuiAdjustDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.WeakCoverTianKuiConnectReverseDao;
import com.datang.dao.testLogItem.TestLogItemSignallingDao;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.coreNetwork.VolteQualityBadRoadCoreNetwork;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.VolteQualityBadRoadNbDeficiency;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.otherProblem.VolteQualityBadRoadOther;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.paramError.VolteQualityBadRoadParamError;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover;
import com.datang.domain.chart.OneDimensionalChartConfig;
import com.datang.domain.chart.TwoDimensionalChartValues;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.qualityBadRoad.IVoiceQualityBadRoadService;
import com.datang.service.gis.IQBRGpsPointService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.util.GPSUtils;
import com.datang.util.NumberFormatUtils;
import com.datang.web.beans.VoLTEDissertation.compareAnalysis.MosBadRoadResponseBean;
import com.datang.web.beans.VoLTEDissertation.qualityBadRoad.OtherIndexResponseBean;
import com.datang.web.beans.VoLTEDissertation.qualityBadRoad.WeakCoverIndexResponseBean;
import com.datang.web.beans.VoLTEDissertation.qualityBadRoad.WholeIndexResponseBean;
import com.datang.web.beans.VoLTEDissertation.qualityBadRoad.WholeIndexResponseBean1;
import com.datang.web.beans.VoLTEDissertation.qualityBadRoad.WholeIndexResponseBean2;
import com.datang.web.beans.VoLTEDissertation.qualityBadRoad.WholeIndexResponseBean3;
import com.datang.web.beans.VoLTEDissertation.qualityBadRoad.WholeIndexResponseBean4;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * volte质量专题---volte语音质差Service接口实现
 * 
 * @author yinzhipeng
 * @date:2015年11月9日 下午4:35:58
 * @version
 */
@Service
@Transactional
public class VoiceQualityBadRoadServiceImpl implements
		IVoiceQualityBadRoadService {

	@Value("${compareAnalysis.mosBadRoad.distance}")
	private String mosBadRoadDistance;
	@Value("${compareAnalysis.mosBadRoad.superpositionRate}")
	private String mosBadRoadSuperpositionRate;

	@Autowired
	private QualityBadRoadDao qualityBadRoadDao;

	@Autowired
	private WeakCoverDao weakCoverDao;
	@Autowired
	private WeakCoverCellInfoDao weakCoverCellInfoDao;
	@Autowired
	private WeakCoverAdviceAddStationDao weakCoverAdviceAddStationDao;
	@Autowired
	private WeakCoverTianKuiAdjustDao weakCoverTianKuiAdjustDao;
	@Autowired
	private WeakCoverTianKuiConnectReverseDao weakCoverTianKuiConnectReverseDao;

	@Autowired
	private DisturbDao disturbDao;
	@Autowired
	private DisturbCellInfoDao disturbCellInfoDao;
	@Autowired
	private DisturbPCIAdjustDao disturbPCIAdjustDao;
	@Autowired
	private DisturbSanChaoCellDao disturbSanChaoCellDao;
	@Autowired
	private DisturbTianKuiAdjustDao disturbTianKuiAdjustDao;

	@Autowired
	private NbCellDeficiencyDao nbCellDeficiencyDao;
	@Autowired
	private NbCellDeficiencyGSMAddAdviceDao nbCellDeficiencyGSMAddAdviceDao;
	@Autowired
	private NbCellDeficiencyLTEAddAdviceDao nbCellDeficiencyLTEAddAdviceDao;
	@Autowired
	private NbCellDeficiencyCoPerfDao nbCellDeficiencyCoPerfDao;

	@Autowired
	private ParamErrorDao paramErrorDao;
	@Autowired
	private ParamErrorOptimizeAdviceDao paramErrorOptimizeAdviceDao;

	@Autowired
	private CoreNetworkDao coreNetworkDao;

	@Autowired
	private OtherDao otherDao;
	@Autowired
	private OtherCellInfoDao otherCellInfoDao;
	@Autowired
	private TestLogItemSignallingDao testLogItemSignallingDao;
	@Autowired
	private IQBRGpsPointService qbrGpsPointService;
	@Autowired
	private ITestLogItemService testLogItemService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getVolteQualityBadRoad(java.lang.Long)
	 */
	@Override
	public VolteQualityBadRoad getVolteQualityBadRoad(Long qbrId) {
		return qualityBadRoadDao.find(qbrId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #getVolteQualityBadRoadsByLogIds(java.util.List)
	 */
	@Override
	public List<VolteQualityBadRoad> getVolteQualityBadRoadsByLogIds(
			List<Long> testLogItemIds) {
		return qualityBadRoadDao
				.queryVolteQualityBadRoadsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#sumQBRDistance(java.util.List)
	 */
	@Override
	public Float sumQBRDistance(List<Long> testLogItemIds) {
		Float distance = null;
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return distance;
		}
		List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds = getVolteQualityBadRoadsByLogIds(testLogItemIds);
		if (null == volteQualityBadRoadsByLogIds
				|| 0 == volteQualityBadRoadsByLogIds.size()) {
			return distance;
		}
		for (VolteQualityBadRoad volteQualityBadRoad : volteQualityBadRoadsByLogIds) {
			/**
			 * 计算所有问题路段:总里程
			 */
			Float m_dbDistance = volteQualityBadRoad.getM_dbDistance();
			if (null != m_dbDistance) {
				distance = (null == distance ? m_dbDistance : distance
						+ m_dbDistance);
			}
		}
		return null == distance ? distance : NumberFormatUtils.format(distance,
				2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#doWholeAnalysis(java.util.List)
	 */
	@Override
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		// map.put("wholeRoadIndex0",doWholeIndex0Analysis(volteQualityBadRoadsByLogIds));
		map.put("wholeRoadIndex0",
				doWholeIndex00Analysis(volteQualityBadRoadsByLogIds));
		map.put("wholeRoadIndex1",
				doWholeIndex1Analysis(volteQualityBadRoadsByLogIds,
						queryTestLogItems));
		map.put("wholeRoadIndex2", doWholeIndex2Analysis(ids));
		map.put("wholeRoadIndex3",
				doWholeIndex3Analysis(volteQualityBadRoadsByLogIds));
		map.put("wholeRoadIndex4",
				doWholeIndex4Analysis(volteQualityBadRoadsByLogIds));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#doWholeIndexAnalysis(java.util.List)
	 */
	@Override
	@Deprecated
	public EasyuiPageList doWholeIndex0Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean> rows = new ArrayList<>();
		WholeIndexResponseBean wholeIndexResponseBean = new WholeIndexResponseBean();
		wholeIndexResponseBean.setCallType("主叫");
		WholeIndexResponseBean wholeIndexResponseBean1 = new WholeIndexResponseBean();
		wholeIndexResponseBean1.setCallType("被叫");
		rows.add(wholeIndexResponseBean);
		rows.add(wholeIndexResponseBean1);
		easyuiPageList.setRows(rows);
		if (null == volteQualityBadRoadsByLogIds
				|| 0 == volteQualityBadRoadsByLogIds.size()) {
			return easyuiPageList;
		}
		VolteQualityBadRoad volteQualityBadRoad0 = new VolteQualityBadRoad();// 主叫bean
		int lblerNum0 = 0;// 主叫lbler数量
		int rblerNum0 = 0;// 主叫rbler数量
		VolteQualityBadRoad volteQualityBadRoad1 = new VolteQualityBadRoad();// 被叫bean
		int lblerNum1 = 0;// 被叫lbler数量
		int rblerNum1 = 0;// 被叫rbler数量
		for (VolteQualityBadRoad volteQualityBadRoad : volteQualityBadRoadsByLogIds) {
			// 计算主被叫
			Integer callType = volteQualityBadRoad.getTestLogItem()
					.getCallType();
			if (null != callType) {
				if (0 == callType) {
					/**
					 * 问题路段LTE制式下上报的MOS采样点个数
					 */
					Long m_ulLTE_MosPointNum = volteQualityBadRoad0
							.getM_ulLTE_MosPointNum();
					Long m_ulLTE_MosPointNum2 = volteQualityBadRoad
							.getM_ulLTE_MosPointNum();
					if (null != m_ulLTE_MosPointNum2) {
						volteQualityBadRoad0
								.setM_ulLTE_MosPointNum(m_ulLTE_MosPointNum == null ? m_ulLTE_MosPointNum2
										: m_ulLTE_MosPointNum
												+ m_ulLTE_MosPointNum2);
					}
					/**
					 * 问题路段LTE制式下上报的MOS采样点值累计和
					 */
					Float m_dbLTE_MosValueSum = volteQualityBadRoad0
							.getM_dbLTE_MosValueSum();
					Float m_dbLTE_MosValueSum2 = volteQualityBadRoad
							.getM_dbLTE_MosValueSum();
					if (null != m_dbLTE_MosValueSum2) {
						volteQualityBadRoad0
								.setM_dbLTE_MosValueSum(m_dbLTE_MosValueSum == null ? m_dbLTE_MosValueSum2
										: m_dbLTE_MosValueSum
												+ m_dbLTE_MosValueSum2);
					}
					// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
					// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
					/**
					 * 初始BLER,残留BLER:初传次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerFirstRequestNum = volteQualityBadRoad0
							.getM_ulBlerFirstRequestNum();
					Long m_ulBlerFirstRequestNum2 = volteQualityBadRoad
							.getM_ulBlerFirstRequestNum();
					if (null != m_ulBlerFirstRequestNum2) {
						volteQualityBadRoad0
								.setM_ulBlerFirstRequestNum(m_ulBlerFirstRequestNum == null ? m_ulBlerFirstRequestNum2
										: m_ulBlerFirstRequestNum
												+ m_ulBlerFirstRequestNum2);
					}
					/**
					 * 初始BLER:初传成功次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerFirstSuccessNum = volteQualityBadRoad0
							.getM_ulBlerFirstSuccessNum();
					Long m_ulBlerFirstSuccessNum2 = volteQualityBadRoad
							.getM_ulBlerFirstSuccessNum();
					if (null != m_ulBlerFirstSuccessNum2) {
						volteQualityBadRoad0
								.setM_ulBlerFirstSuccessNum(m_ulBlerFirstSuccessNum == null ? m_ulBlerFirstSuccessNum2
										: m_ulBlerFirstSuccessNum
												+ m_ulBlerFirstSuccessNum2);
					}
					/**
					 * 残留BLER:多次重传后成功次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerAgainSuccessNum = volteQualityBadRoad0
							.getM_ulBlerAgainSuccessNum();
					Long m_ulBlerAgainSuccessNum2 = volteQualityBadRoad
							.getM_ulBlerAgainSuccessNum();
					if (null != m_ulBlerAgainSuccessNum2) {
						volteQualityBadRoad0
								.setM_ulBlerAgainSuccessNum(m_ulBlerAgainSuccessNum == null ? m_ulBlerAgainSuccessNum2
										: m_ulBlerAgainSuccessNum
												+ m_ulBlerAgainSuccessNum2);
					}

					/**
					 * 初始BLER
					 * 
					 * @date 20160324 新增
					 */
					Float lBler = volteQualityBadRoad0.getlBler();
					Float lBler2 = volteQualityBadRoad.getlBler();
					if (null != lBler2) {
						lblerNum0++;
						volteQualityBadRoad0.setlBler(lBler == null ? lBler2
								: lBler + lBler2);
					}

					/**
					 * 残留BLER
					 * 
					 * @date 20160324 新增
					 */
					Float rBler = volteQualityBadRoad0.getrBler();
					Float rBler2 = volteQualityBadRoad.getrBler();
					if (null != rBler2) {
						rblerNum0++;
						volteQualityBadRoad0.setrBler(rBler == null ? rBler2
								: rBler + rBler2);
					}

					// RTP丢包率=(发送的VoIP数据包数量—接收的数据包数量)/发送的VoIP数据包数量
					/**
					 * RTP丢包率:发送的VoIP数据包数量
					 */
					Float m_dbRTPSendVoIPDataPackageNum = volteQualityBadRoad0
							.getM_dbRTPSendVoIPDataPackageNum();
					Float m_dbRTPSendVoIPDataPackageNum2 = volteQualityBadRoad
							.getM_dbRTPSendVoIPDataPackageNum();
					if (null != m_dbRTPSendVoIPDataPackageNum2) {
						volteQualityBadRoad0
								.setM_dbRTPSendVoIPDataPackageNum(m_dbRTPSendVoIPDataPackageNum == null ? m_dbRTPSendVoIPDataPackageNum2
										: m_dbRTPSendVoIPDataPackageNum
												+ m_dbRTPSendVoIPDataPackageNum2);
					}
					/**
					 * RTP丢包率:接收的数据包数量
					 */
					Float m_dbRTPReceiveDataPackageNum = volteQualityBadRoad0
							.getM_dbRTPReceiveDataPackageNum();
					Float m_dbRTPReceiveDataPackageNum2 = volteQualityBadRoad
							.getM_dbRTPReceiveDataPackageNum();
					if (null != m_dbRTPReceiveDataPackageNum2) {
						volteQualityBadRoad0
								.setM_dbRTPReceiveDataPackageNum(m_dbRTPReceiveDataPackageNum == null ? m_dbRTPReceiveDataPackageNum2
										: m_dbRTPReceiveDataPackageNum
												+ m_dbRTPReceiveDataPackageNum2);
					}
					/**
					 * RTP抖动(ms)=RTP取值之和/RTP数量
					 */
					Float m_dbRTPSum = volteQualityBadRoad0.getM_dbRTPSum();
					Float m_dbRTPSum2 = volteQualityBadRoad.getM_dbRTPSum();
					if (null != m_dbRTPSum2) {
						volteQualityBadRoad0
								.setM_dbRTPSum(m_dbRTPSum == null ? m_dbRTPSum2
										: m_dbRTPSum + m_dbRTPSum2);
					}
					Long m_ulRTPNum = volteQualityBadRoad0.getM_ulRTPNum();
					Long m_ulRTPNum2 = volteQualityBadRoad.getM_ulRTPNum();
					if (null != m_ulRTPNum2) {
						volteQualityBadRoad0
								.setM_ulRTPNum(m_ulRTPNum == null ? m_ulRTPNum2
										: m_ulRTPNum + m_ulRTPNum2);
					}
					/**
					 * TDL驻网时长
					 */
					Float m_dbTDLTestTime = volteQualityBadRoad0
							.getM_dbTDLTestTime();
					Float m_dbTDLTestTime2 = volteQualityBadRoad
							.getM_dbTDLTestTime();
					if (null != m_dbTDLTestTime2) {
						volteQualityBadRoad0
								.setM_dbTDLTestTime(m_dbTDLTestTime == null ? m_dbTDLTestTime2
										: m_dbTDLTestTime + m_dbTDLTestTime2);
					}
					/**
					 * TDS驻网时长
					 */
					Float m_dbTDSTestTime = volteQualityBadRoad0
							.getM_dbTDSTestTime();
					Float m_dbTDSTestTime2 = volteQualityBadRoad
							.getM_dbTDSTestTime();
					if (null != m_dbTDSTestTime2) {
						volteQualityBadRoad0
								.setM_dbTDSTestTime(m_dbTDSTestTime == null ? m_dbTDSTestTime2
										: m_dbTDSTestTime + m_dbTDSTestTime2);
					}
					/**
					 * GSM驻网时长
					 */
					Float m_dbGSMTestTime = volteQualityBadRoad0
							.getM_dbGSMTestTime();
					Float m_dbGSMTestTime2 = volteQualityBadRoad
							.getM_dbGSMTestTime();
					if (null != m_dbGSMTestTime2) {
						volteQualityBadRoad0
								.setM_dbGSMTestTime(m_dbGSMTestTime == null ? m_dbGSMTestTime2
										: m_dbGSMTestTime + m_dbGSMTestTime2);
					}
				} else if (1 == callType) {
					/**
					 * 问题路段LTE制式下上报的MOS采样点个数
					 */
					Long m_ulLTE_MosPointNum = volteQualityBadRoad1
							.getM_ulLTE_MosPointNum();
					Long m_ulLTE_MosPointNum2 = volteQualityBadRoad
							.getM_ulLTE_MosPointNum();
					if (null != m_ulLTE_MosPointNum2) {
						volteQualityBadRoad1
								.setM_ulLTE_MosPointNum(m_ulLTE_MosPointNum == null ? m_ulLTE_MosPointNum2
										: m_ulLTE_MosPointNum
												+ m_ulLTE_MosPointNum2);
					}
					/**
					 * 问题路段LTE制式下上报的MOS采样点值累计和
					 */
					Float m_dbLTE_MosValueSum = volteQualityBadRoad1
							.getM_dbLTE_MosValueSum();
					Float m_dbLTE_MosValueSum2 = volteQualityBadRoad
							.getM_dbLTE_MosValueSum();
					if (null != m_dbLTE_MosValueSum2) {
						volteQualityBadRoad1
								.setM_dbLTE_MosValueSum(m_dbLTE_MosValueSum == null ? m_dbLTE_MosValueSum2
										: m_dbLTE_MosValueSum
												+ m_dbLTE_MosValueSum2);
					}
					// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
					// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
					/**
					 * 初始BLER,残留BLER:初传次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerFirstRequestNum = volteQualityBadRoad1
							.getM_ulBlerFirstRequestNum();
					Long m_ulBlerFirstRequestNum2 = volteQualityBadRoad
							.getM_ulBlerFirstRequestNum();
					if (null != m_ulBlerFirstRequestNum2) {
						volteQualityBadRoad1
								.setM_ulBlerFirstRequestNum(m_ulBlerFirstRequestNum == null ? m_ulBlerFirstRequestNum2
										: m_ulBlerFirstRequestNum
												+ m_ulBlerFirstRequestNum2);
					}
					/**
					 * 初始BLER:初传成功次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerFirstSuccessNum = volteQualityBadRoad1
							.getM_ulBlerFirstSuccessNum();
					Long m_ulBlerFirstSuccessNum2 = volteQualityBadRoad
							.getM_ulBlerFirstSuccessNum();
					if (null != m_ulBlerFirstSuccessNum2) {
						volteQualityBadRoad1
								.setM_ulBlerFirstSuccessNum(m_ulBlerFirstSuccessNum == null ? m_ulBlerFirstSuccessNum2
										: m_ulBlerFirstSuccessNum
												+ m_ulBlerFirstSuccessNum2);
					}
					/**
					 * 残留BLER:多次重传后成功次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerAgainSuccessNum = volteQualityBadRoad1
							.getM_ulBlerAgainSuccessNum();
					Long m_ulBlerAgainSuccessNum2 = volteQualityBadRoad
							.getM_ulBlerAgainSuccessNum();
					if (null != m_ulBlerAgainSuccessNum2) {
						volteQualityBadRoad1
								.setM_ulBlerAgainSuccessNum(m_ulBlerAgainSuccessNum == null ? m_ulBlerAgainSuccessNum2
										: m_ulBlerAgainSuccessNum
												+ m_ulBlerAgainSuccessNum2);
					}

					/**
					 * 初始BLER
					 * 
					 * @date 20160324 新增
					 */
					Float lBler = volteQualityBadRoad1.getlBler();
					Float lBler2 = volteQualityBadRoad.getlBler();
					if (null != lBler2) {
						lblerNum1++;
						volteQualityBadRoad1.setlBler(lBler == null ? lBler2
								: lBler + lBler2);
					}

					/**
					 * 残留BLER
					 * 
					 * @date 20160324 新增
					 */
					Float rBler = volteQualityBadRoad1.getrBler();
					Float rBler2 = volteQualityBadRoad.getrBler();
					if (null != rBler2) {
						rblerNum1++;
						volteQualityBadRoad1.setrBler(rBler == null ? rBler2
								: rBler + rBler2);
					}

					// RTP丢包率=(发送的VoIP数据包数量—接收的数据包数量)/发送的VoIP数据包数量
					/**
					 * RTP丢包率:发送的VoIP数据包数量
					 */
					Float m_dbRTPSendVoIPDataPackageNum = volteQualityBadRoad1
							.getM_dbRTPSendVoIPDataPackageNum();
					Float m_dbRTPSendVoIPDataPackageNum2 = volteQualityBadRoad
							.getM_dbRTPSendVoIPDataPackageNum();
					if (null != m_dbRTPSendVoIPDataPackageNum2) {
						volteQualityBadRoad1
								.setM_dbRTPSendVoIPDataPackageNum(m_dbRTPSendVoIPDataPackageNum == null ? m_dbRTPSendVoIPDataPackageNum2
										: m_dbRTPSendVoIPDataPackageNum
												+ m_dbRTPSendVoIPDataPackageNum2);
					}
					/**
					 * RTP丢包率:接收的数据包数量
					 */
					Float m_dbRTPReceiveDataPackageNum = volteQualityBadRoad1
							.getM_dbRTPReceiveDataPackageNum();
					Float m_dbRTPReceiveDataPackageNum2 = volteQualityBadRoad
							.getM_dbRTPReceiveDataPackageNum();
					if (null != m_dbRTPReceiveDataPackageNum2) {
						volteQualityBadRoad1
								.setM_dbRTPReceiveDataPackageNum(m_dbRTPReceiveDataPackageNum == null ? m_dbRTPReceiveDataPackageNum2
										: m_dbRTPReceiveDataPackageNum
												+ m_dbRTPReceiveDataPackageNum2);
					}
					/**
					 * RTP抖动(ms)=RTP取值之和/RTP数量
					 */
					Float m_dbRTPSum = volteQualityBadRoad1.getM_dbRTPSum();
					Float m_dbRTPSum2 = volteQualityBadRoad.getM_dbRTPSum();
					if (null != m_dbRTPSum2) {
						volteQualityBadRoad1
								.setM_dbRTPSum(m_dbRTPSum == null ? m_dbRTPSum2
										: m_dbRTPSum + m_dbRTPSum2);
					}
					Long m_ulRTPNum = volteQualityBadRoad1.getM_ulRTPNum();
					Long m_ulRTPNum2 = volteQualityBadRoad.getM_ulRTPNum();
					if (null != m_ulRTPNum2) {
						volteQualityBadRoad1
								.setM_ulRTPNum(m_ulRTPNum == null ? m_ulRTPNum2
										: m_ulRTPNum + m_ulRTPNum2);
					}
					/**
					 * TDL驻网时长
					 */
					Float m_dbTDLTestTime = volteQualityBadRoad1
							.getM_dbTDLTestTime();
					Float m_dbTDLTestTime2 = volteQualityBadRoad
							.getM_dbTDLTestTime();
					if (null != m_dbTDLTestTime2) {
						volteQualityBadRoad1
								.setM_dbTDLTestTime(m_dbTDLTestTime == null ? m_dbTDLTestTime2
										: m_dbTDLTestTime + m_dbTDLTestTime2);
					}
					/**
					 * TDS驻网时长
					 */
					Float m_dbTDSTestTime = volteQualityBadRoad1
							.getM_dbTDSTestTime();
					Float m_dbTDSTestTime2 = volteQualityBadRoad
							.getM_dbTDSTestTime();
					if (null != m_dbTDSTestTime2) {
						volteQualityBadRoad1
								.setM_dbTDSTestTime(m_dbTDSTestTime == null ? m_dbTDSTestTime2
										: m_dbTDSTestTime + m_dbTDSTestTime2);
					}
					/**
					 * GSM驻网时长
					 */
					Float m_dbGSMTestTime = volteQualityBadRoad1
							.getM_dbGSMTestTime();
					Float m_dbGSMTestTime2 = volteQualityBadRoad
							.getM_dbGSMTestTime();
					if (null != m_dbGSMTestTime2) {
						volteQualityBadRoad1
								.setM_dbGSMTestTime(m_dbGSMTestTime == null ? m_dbGSMTestTime2
										: m_dbGSMTestTime + m_dbGSMTestTime2);
					}
				} else if (2 == callType) {
					/**
					 * 问题路段LTE制式下上报的MOS采样点个数
					 */
					Long m_ulLTE_MosPointNum = volteQualityBadRoad0
							.getM_ulLTE_MosPointNum();
					Long m_ulLTE_MosPointNum1 = volteQualityBadRoad1
							.getM_ulLTE_MosPointNum();
					Long m_ulLTE_MosPointNum2 = volteQualityBadRoad
							.getM_ulLTE_MosPointNum();
					if (null != m_ulLTE_MosPointNum2) {
						volteQualityBadRoad0
								.setM_ulLTE_MosPointNum(m_ulLTE_MosPointNum == null ? m_ulLTE_MosPointNum2
										: m_ulLTE_MosPointNum
												+ m_ulLTE_MosPointNum2);
						volteQualityBadRoad1
								.setM_ulLTE_MosPointNum(m_ulLTE_MosPointNum1 == null ? m_ulLTE_MosPointNum2
										: m_ulLTE_MosPointNum1
												+ m_ulLTE_MosPointNum2);
					}
					/**
					 * 问题路段LTE制式下上报的MOS采样点值累计和
					 */
					Float m_dbLTE_MosValueSum = volteQualityBadRoad0
							.getM_dbLTE_MosValueSum();
					Float m_dbLTE_MosValueSum1 = volteQualityBadRoad1
							.getM_dbLTE_MosValueSum();
					Float m_dbLTE_MosValueSum2 = volteQualityBadRoad
							.getM_dbLTE_MosValueSum();
					if (null != m_dbLTE_MosValueSum2) {
						volteQualityBadRoad0
								.setM_dbLTE_MosValueSum(m_dbLTE_MosValueSum == null ? m_dbLTE_MosValueSum2
										: m_dbLTE_MosValueSum
												+ m_dbLTE_MosValueSum2);
						volteQualityBadRoad1
								.setM_dbLTE_MosValueSum(m_dbLTE_MosValueSum1 == null ? m_dbLTE_MosValueSum2
										: m_dbLTE_MosValueSum1
												+ m_dbLTE_MosValueSum2);
					}
					// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
					// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
					/**
					 * 初始BLER,残留BLER:初传次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerFirstRequestNum = volteQualityBadRoad0
							.getM_ulBlerFirstRequestNum();
					Long m_ulBlerFirstRequestNum1 = volteQualityBadRoad1
							.getM_ulBlerFirstRequestNum();
					Long m_ulBlerFirstRequestNum2 = volteQualityBadRoad
							.getM_ulBlerFirstRequestNum();
					if (null != m_ulBlerFirstRequestNum2) {
						volteQualityBadRoad0
								.setM_ulBlerFirstRequestNum(m_ulBlerFirstRequestNum == null ? m_ulBlerFirstRequestNum2
										: m_ulBlerFirstRequestNum
												+ m_ulBlerFirstRequestNum2);
						volteQualityBadRoad1
								.setM_ulBlerFirstRequestNum(m_ulBlerFirstRequestNum1 == null ? m_ulBlerFirstRequestNum2
										: m_ulBlerFirstRequestNum1
												+ m_ulBlerFirstRequestNum2);
					}
					/**
					 * 初始BLER:初传成功次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerFirstSuccessNum = volteQualityBadRoad0
							.getM_ulBlerFirstSuccessNum();
					Long m_ulBlerFirstSuccessNum1 = volteQualityBadRoad1
							.getM_ulBlerFirstSuccessNum();
					Long m_ulBlerFirstSuccessNum2 = volteQualityBadRoad
							.getM_ulBlerFirstSuccessNum();
					if (null != m_ulBlerFirstSuccessNum2) {
						volteQualityBadRoad0
								.setM_ulBlerFirstSuccessNum(m_ulBlerFirstSuccessNum == null ? m_ulBlerFirstSuccessNum2
										: m_ulBlerFirstSuccessNum
												+ m_ulBlerFirstSuccessNum2);
						volteQualityBadRoad1
								.setM_ulBlerFirstSuccessNum(m_ulBlerFirstSuccessNum1 == null ? m_ulBlerFirstSuccessNum2
										: m_ulBlerFirstSuccessNum1
												+ m_ulBlerFirstSuccessNum2);
					}
					/**
					 * 残留BLER:多次重传后成功次数
					 * 
					 * @date 20160324 废弃
					 */
					Long m_ulBlerAgainSuccessNum = volteQualityBadRoad0
							.getM_ulBlerAgainSuccessNum();
					Long m_ulBlerAgainSuccessNum1 = volteQualityBadRoad1
							.getM_ulBlerAgainSuccessNum();
					Long m_ulBlerAgainSuccessNum2 = volteQualityBadRoad
							.getM_ulBlerAgainSuccessNum();
					if (null != m_ulBlerAgainSuccessNum2) {
						volteQualityBadRoad0
								.setM_ulBlerAgainSuccessNum(m_ulBlerAgainSuccessNum == null ? m_ulBlerAgainSuccessNum2
										: m_ulBlerAgainSuccessNum
												+ m_ulBlerAgainSuccessNum2);
						volteQualityBadRoad1
								.setM_ulBlerAgainSuccessNum(m_ulBlerAgainSuccessNum1 == null ? m_ulBlerAgainSuccessNum2
										: m_ulBlerAgainSuccessNum1
												+ m_ulBlerAgainSuccessNum2);
					}

					/**
					 * 初始BLER
					 * 
					 * @date 20160324 新增
					 */
					Float lBler = volteQualityBadRoad0.getlBler();
					Float lBler1 = volteQualityBadRoad1.getlBler();
					Float lBler2 = volteQualityBadRoad.getlBler();
					if (null != lBler2) {
						lblerNum0++;
						lblerNum1++;
						volteQualityBadRoad0.setlBler(lBler == null ? lBler2
								: lBler + lBler2);
						volteQualityBadRoad1.setlBler(lBler1 == null ? lBler2
								: lBler1 + lBler2);
					}

					/**
					 * 残留BLER
					 * 
					 * @date 20160324 新增
					 */
					Float rBler = volteQualityBadRoad0.getrBler();
					Float rBler1 = volteQualityBadRoad1.getrBler();
					Float rBler2 = volteQualityBadRoad.getrBler();
					if (null != rBler2) {
						rblerNum0++;
						rblerNum1++;
						volteQualityBadRoad0.setrBler(rBler == null ? rBler2
								: rBler + rBler2);
						volteQualityBadRoad1.setrBler(rBler1 == null ? rBler2
								: rBler1 + rBler2);
					}

					// RTP丢包率=(发送的VoIP数据包数量—接收的数据包数量)/发送的VoIP数据包数量
					/**
					 * RTP丢包率:发送的VoIP数据包数量
					 */
					Float m_dbRTPSendVoIPDataPackageNum = volteQualityBadRoad0
							.getM_dbRTPSendVoIPDataPackageNum();
					Float m_dbRTPSendVoIPDataPackageNum1 = volteQualityBadRoad1
							.getM_dbRTPSendVoIPDataPackageNum();
					Float m_dbRTPSendVoIPDataPackageNum2 = volteQualityBadRoad
							.getM_dbRTPSendVoIPDataPackageNum();
					if (null != m_dbRTPSendVoIPDataPackageNum2) {
						volteQualityBadRoad0
								.setM_dbRTPSendVoIPDataPackageNum(m_dbRTPSendVoIPDataPackageNum == null ? m_dbRTPSendVoIPDataPackageNum2
										: m_dbRTPSendVoIPDataPackageNum
												+ m_dbRTPSendVoIPDataPackageNum2);
						volteQualityBadRoad1
								.setM_dbRTPSendVoIPDataPackageNum(m_dbRTPSendVoIPDataPackageNum1 == null ? m_dbRTPSendVoIPDataPackageNum2
										: m_dbRTPSendVoIPDataPackageNum1
												+ m_dbRTPSendVoIPDataPackageNum2);
					}
					/**
					 * RTP丢包率:接收的数据包数量
					 */
					Float m_dbRTPReceiveDataPackageNum = volteQualityBadRoad0
							.getM_dbRTPReceiveDataPackageNum();
					Float m_dbRTPReceiveDataPackageNum1 = volteQualityBadRoad1
							.getM_dbRTPReceiveDataPackageNum();
					Float m_dbRTPReceiveDataPackageNum2 = volteQualityBadRoad
							.getM_dbRTPReceiveDataPackageNum();
					if (null != m_dbRTPReceiveDataPackageNum2) {
						volteQualityBadRoad0
								.setM_dbRTPReceiveDataPackageNum(m_dbRTPReceiveDataPackageNum == null ? m_dbRTPReceiveDataPackageNum2
										: m_dbRTPReceiveDataPackageNum
												+ m_dbRTPReceiveDataPackageNum2);
						volteQualityBadRoad1
								.setM_dbRTPReceiveDataPackageNum(m_dbRTPReceiveDataPackageNum1 == null ? m_dbRTPReceiveDataPackageNum2
										: m_dbRTPReceiveDataPackageNum1
												+ m_dbRTPReceiveDataPackageNum2);
					}
					/**
					 * RTP抖动(ms)=RTP取值之和/RTP数量
					 */
					Float m_dbRTPSum = volteQualityBadRoad0.getM_dbRTPSum();
					Float m_dbRTPSum1 = volteQualityBadRoad1.getM_dbRTPSum();
					Float m_dbRTPSum2 = volteQualityBadRoad.getM_dbRTPSum();
					if (null != m_dbRTPSum2) {
						volteQualityBadRoad0
								.setM_dbRTPSum(m_dbRTPSum == null ? m_dbRTPSum2
										: m_dbRTPSum + m_dbRTPSum2);
						volteQualityBadRoad1
								.setM_dbRTPSum(m_dbRTPSum1 == null ? m_dbRTPSum2
										: m_dbRTPSum1 + m_dbRTPSum2);
					}
					Long m_ulRTPNum = volteQualityBadRoad0.getM_ulRTPNum();
					Long m_ulRTPNum1 = volteQualityBadRoad1.getM_ulRTPNum();
					Long m_ulRTPNum2 = volteQualityBadRoad.getM_ulRTPNum();
					if (null != m_ulRTPNum2) {
						volteQualityBadRoad0
								.setM_ulRTPNum(m_ulRTPNum == null ? m_ulRTPNum2
										: m_ulRTPNum + m_ulRTPNum2);
						volteQualityBadRoad1
								.setM_ulRTPNum(m_ulRTPNum1 == null ? m_ulRTPNum2
										: m_ulRTPNum1 + m_ulRTPNum2);
					}
					/**
					 * TDL驻网时长
					 */
					Float m_dbTDLTestTime = volteQualityBadRoad0
							.getM_dbTDLTestTime();
					Float m_dbTDLTestTime1 = volteQualityBadRoad1
							.getM_dbTDLTestTime();
					Float m_dbTDLTestTime2 = volteQualityBadRoad
							.getM_dbTDLTestTime();
					if (null != m_dbTDLTestTime2) {
						volteQualityBadRoad0
								.setM_dbTDLTestTime(m_dbTDLTestTime == null ? m_dbTDLTestTime2
										: m_dbTDLTestTime + m_dbTDLTestTime2);
						volteQualityBadRoad1
								.setM_dbTDLTestTime(m_dbTDLTestTime1 == null ? m_dbTDLTestTime2
										: m_dbTDLTestTime1 + m_dbTDLTestTime2);
					}
					/**
					 * TDS驻网时长
					 */
					Float m_dbTDSTestTime = volteQualityBadRoad0
							.getM_dbTDSTestTime();
					Float m_dbTDSTestTime1 = volteQualityBadRoad1
							.getM_dbTDSTestTime();
					Float m_dbTDSTestTime2 = volteQualityBadRoad
							.getM_dbTDSTestTime();
					if (null != m_dbTDSTestTime2) {
						volteQualityBadRoad0
								.setM_dbTDSTestTime(m_dbTDSTestTime == null ? m_dbTDSTestTime2
										: m_dbTDSTestTime + m_dbTDSTestTime2);
						volteQualityBadRoad1
								.setM_dbTDSTestTime(m_dbTDSTestTime1 == null ? m_dbTDSTestTime2
										: m_dbTDSTestTime1 + m_dbTDSTestTime2);
					}
					/**
					 * GSM驻网时长
					 */
					Float m_dbGSMTestTime = volteQualityBadRoad0
							.getM_dbGSMTestTime();
					Float m_dbGSMTestTime1 = volteQualityBadRoad1
							.getM_dbGSMTestTime();
					Float m_dbGSMTestTime2 = volteQualityBadRoad
							.getM_dbGSMTestTime();
					if (null != m_dbGSMTestTime2) {
						volteQualityBadRoad0
								.setM_dbGSMTestTime(m_dbGSMTestTime == null ? m_dbGSMTestTime2
										: m_dbGSMTestTime + m_dbGSMTestTime2);
						volteQualityBadRoad1
								.setM_dbGSMTestTime(m_dbGSMTestTime1 == null ? m_dbGSMTestTime2
										: m_dbGSMTestTime1 + m_dbGSMTestTime2);
					}
				}
			}
		}
		/**
		 * 计算volte平均mos
		 */
		Long m_ulLTE_MosPointNum = volteQualityBadRoad0
				.getM_ulLTE_MosPointNum();
		Float m_dbLTE_MosValueSum = volteQualityBadRoad0
				.getM_dbLTE_MosValueSum();
		if (null != m_ulLTE_MosPointNum && m_ulLTE_MosPointNum != 0
				&& null != m_dbLTE_MosValueSum) {
			wholeIndexResponseBean.setVolteMosAvg(NumberFormatUtils.format(
					m_dbLTE_MosValueSum / m_ulLTE_MosPointNum, 2));
		}
		Long m_ulLTE_MosPointNum1 = volteQualityBadRoad1
				.getM_ulLTE_MosPointNum();
		Float m_dbLTE_MosValueSum1 = volteQualityBadRoad1
				.getM_dbLTE_MosValueSum();
		if (null != m_ulLTE_MosPointNum1 && m_ulLTE_MosPointNum1 != 0
				&& null != m_dbLTE_MosValueSum1) {
			wholeIndexResponseBean1.setVolteMosAvg(NumberFormatUtils.format(
					m_dbLTE_MosValueSum1 / m_ulLTE_MosPointNum1, 2));
		}
		// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
		// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
		/**
		 * 计算初始BLER,残留BLER
		 * 
		 * @date 20160324 废弃
		 * 
		 */
		float m_ulBlerFirstRequestNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getM_ulBlerFirstRequestNum());
		float m_ulBlerFirstSuccessNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getM_ulBlerFirstSuccessNum());
		float m_ulBlerAgainSuccessNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getM_ulBlerAgainSuccessNum());
		if (0 != m_ulBlerFirstRequestNum) {
			wholeIndexResponseBean.setFirstBler(NumberFormatUtils.format(
					(m_ulBlerFirstRequestNum - m_ulBlerFirstSuccessNum)
							/ m_ulBlerFirstRequestNum * 100, 2));
			wholeIndexResponseBean.setResidualBler(NumberFormatUtils.format(
					(m_ulBlerFirstRequestNum - m_ulBlerAgainSuccessNum)
							/ m_ulBlerFirstRequestNum * 100, 2));
		}
		float m_ulBlerFirstRequestNum1 = NumberFormatUtils
				.isEmpty(volteQualityBadRoad1.getM_ulBlerFirstRequestNum());
		float m_ulBlerFirstSuccessNum1 = NumberFormatUtils
				.isEmpty(volteQualityBadRoad1.getM_ulBlerFirstSuccessNum());
		float m_ulBlerAgainSuccessNum1 = NumberFormatUtils
				.isEmpty(volteQualityBadRoad1.getM_ulBlerAgainSuccessNum());
		if (0 != m_ulBlerFirstRequestNum1) {
			wholeIndexResponseBean1.setFirstBler(NumberFormatUtils.format(
					(m_ulBlerFirstRequestNum1 - m_ulBlerFirstSuccessNum1)
							/ m_ulBlerFirstRequestNum1 * 100, 2));
			wholeIndexResponseBean1.setResidualBler(NumberFormatUtils.format(
					(m_ulBlerFirstRequestNum1 - m_ulBlerAgainSuccessNum1)
							/ m_ulBlerFirstRequestNum1 * 100, 2));
		}

		/**
		 * 计算初始BLER,残留BLER
		 * 
		 * @date 20160324 新增
		 * 
		 */
		// 主叫
		float lbler = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getlBler());
		float rbler = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getrBler());
		if (0 != lblerNum0) {
			wholeIndexResponseBean.setFirstBler(NumberFormatUtils.format(lbler
					/ lblerNum0, 2));
		}
		if (0 != rblerNum0) {
			wholeIndexResponseBean.setResidualBler(NumberFormatUtils.format(
					rbler / rblerNum0, 2));
		}

		// 被叫
		float lbler1 = NumberFormatUtils.isEmpty(volteQualityBadRoad1
				.getlBler());
		float rbler1 = NumberFormatUtils.isEmpty(volteQualityBadRoad1
				.getrBler());
		if (0 != lblerNum1) {
			wholeIndexResponseBean1.setFirstBler(NumberFormatUtils.format(
					lbler1 / lblerNum1, 2));
		}
		if (0 != rblerNum1) {
			wholeIndexResponseBean1.setResidualBler(NumberFormatUtils.format(
					rbler1 / rblerNum1, 2));
		}

		/**
		 * 计算RTP丢包率=(发送的VoIP数据包数量—接收的数据包数量)/发送的VoIP数据包数量 @date 20160324废弃
		 * 计算RTP丢包率=发送的VoIP数据包数量/接收的数据包数量 @date 20160324更新
		 * 
		 */
		float m_dbRTPSendVoIPDataPackageNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0
						.getM_dbRTPSendVoIPDataPackageNum());
		float m_dbRTPReceiveDataPackageNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getM_dbRTPReceiveDataPackageNum());
		// @date 20160324废弃
		if (0 != m_dbRTPSendVoIPDataPackageNum) {
			wholeIndexResponseBean
					.setRtpDropRatio(NumberFormatUtils
							.format((m_dbRTPSendVoIPDataPackageNum - m_dbRTPReceiveDataPackageNum)
									/ m_dbRTPSendVoIPDataPackageNum * 100, 2));
		}
		// @date 20160324更新
		if (0 != m_dbRTPReceiveDataPackageNum) {
			wholeIndexResponseBean.setRtpDropRatio(NumberFormatUtils.format(
					m_dbRTPSendVoIPDataPackageNum
							/ m_dbRTPReceiveDataPackageNum, 2));
		}

		float m_dbRTPSendVoIPDataPackageNum1 = NumberFormatUtils
				.isEmpty(volteQualityBadRoad1
						.getM_dbRTPSendVoIPDataPackageNum());
		float m_dbRTPReceiveDataPackageNum1 = NumberFormatUtils
				.isEmpty(volteQualityBadRoad1.getM_dbRTPReceiveDataPackageNum());
		// @date 20160324废弃
		if (0 != m_dbRTPSendVoIPDataPackageNum1) {
			wholeIndexResponseBean1
					.setRtpDropRatio(NumberFormatUtils
							.format((m_dbRTPSendVoIPDataPackageNum1 - m_dbRTPReceiveDataPackageNum1)
									/ m_dbRTPSendVoIPDataPackageNum1 * 100, 2));
		}
		// @date 20160324更新
		if (0 != m_dbRTPReceiveDataPackageNum1) {
			wholeIndexResponseBean1.setRtpDropRatio(NumberFormatUtils.format(
					m_dbRTPSendVoIPDataPackageNum1
							/ m_dbRTPReceiveDataPackageNum1, 2));
		}
		/**
		 * 计算RTP抖动(ms)=RTP取值之和/RTP数量
		 */
		Float m_dbRTPSum = volteQualityBadRoad0.getM_dbRTPSum();
		Long m_ulRTPNum = volteQualityBadRoad0.getM_ulRTPNum();
		if (null != m_ulRTPNum && m_ulRTPNum != 0 && null != m_dbRTPSum) {
			wholeIndexResponseBean.setRtpShake(NumberFormatUtils.format(
					m_dbRTPSum / m_ulRTPNum, 2));
		}
		Float m_dbRTPSum1 = volteQualityBadRoad1.getM_dbRTPSum();
		Long m_ulRTPNum1 = volteQualityBadRoad1.getM_ulRTPNum();
		if (null != m_ulRTPNum1 && m_ulRTPNum1 != 0 && null != m_dbRTPSum1) {
			wholeIndexResponseBean1.setRtpShake(NumberFormatUtils.format(
					m_dbRTPSum1 / m_ulRTPNum1, 2));
		}

		/**
		 * 计算tdl,tds,gsm驻网时长
		 */
		if (null != volteQualityBadRoad0.getM_dbTDLTestTime()) {
			wholeIndexResponseBean.setTdlTestTime(volteQualityBadRoad0
					.getM_dbTDLTestTime() / 3600000);
		}
		if (null != volteQualityBadRoad0.getM_dbTDSTestTime()) {
			wholeIndexResponseBean.setTdsTestTime(volteQualityBadRoad0
					.getM_dbTDSTestTime() / 3600000);
		}
		if (null != volteQualityBadRoad0.getM_dbGSMTestTime()) {
			wholeIndexResponseBean.setGsmTestTime(volteQualityBadRoad0
					.getM_dbGSMTestTime() / 3600000);
		}
		if (null != volteQualityBadRoad1.getM_dbTDLTestTime()) {
			wholeIndexResponseBean1.setTdlTestTime(volteQualityBadRoad1
					.getM_dbTDLTestTime() / 3600000);
		}
		if (null != volteQualityBadRoad1.getM_dbTDSTestTime()) {
			wholeIndexResponseBean1.setTdsTestTime(volteQualityBadRoad1
					.getM_dbTDSTestTime() / 3600000);
		}
		if (null != volteQualityBadRoad1.getM_dbGSMTestTime()) {
			wholeIndexResponseBean1.setGsmTestTime(volteQualityBadRoad1
					.getM_dbGSMTestTime() / 3600000);
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#doWholeIndexAnalysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doWholeIndex00Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean> rows = new ArrayList<>();
		WholeIndexResponseBean wholeIndexResponseBean = new WholeIndexResponseBean();
		rows.add(wholeIndexResponseBean);

		easyuiPageList.setRows(rows);
		if (null == volteQualityBadRoadsByLogIds
				|| 0 == volteQualityBadRoadsByLogIds.size()) {
			return easyuiPageList;
		}
		VolteQualityBadRoad volteQualityBadRoad0 = new VolteQualityBadRoad();// 主叫bean
		int lblerNum0 = 0;// lbler数量
		int rblerNum0 = 0;// rbler数量

		for (VolteQualityBadRoad volteQualityBadRoad : volteQualityBadRoadsByLogIds) {

			/**
			 * 问题路段LTE制式下上报的MOS采样点个数
			 */
			Long m_ulLTE_MosPointNum = volteQualityBadRoad0
					.getM_ulLTE_MosPointNum();
			Long m_ulLTE_MosPointNum2 = volteQualityBadRoad
					.getM_ulLTE_MosPointNum();
			if (null != m_ulLTE_MosPointNum2) {
				volteQualityBadRoad0
						.setM_ulLTE_MosPointNum(m_ulLTE_MosPointNum == null ? m_ulLTE_MosPointNum2
								: m_ulLTE_MosPointNum + m_ulLTE_MosPointNum2);
			}
			/**
			 * 问题路段LTE制式下上报的MOS采样点值累计和
			 */
			Float m_dbLTE_MosValueSum = volteQualityBadRoad0
					.getM_dbLTE_MosValueSum();
			Float m_dbLTE_MosValueSum2 = volteQualityBadRoad
					.getM_dbLTE_MosValueSum();
			if (null != m_dbLTE_MosValueSum2) {
				volteQualityBadRoad0
						.setM_dbLTE_MosValueSum(m_dbLTE_MosValueSum == null ? m_dbLTE_MosValueSum2
								: m_dbLTE_MosValueSum + m_dbLTE_MosValueSum2);
			}
			// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
			// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
			/**
			 * 初始BLER,残留BLER:初传次数
			 * 
			 * @date 20160324 废弃
			 */
			Long m_ulBlerFirstRequestNum = volteQualityBadRoad0
					.getM_ulBlerFirstRequestNum();
			Long m_ulBlerFirstRequestNum2 = volteQualityBadRoad
					.getM_ulBlerFirstRequestNum();
			if (null != m_ulBlerFirstRequestNum2) {
				volteQualityBadRoad0
						.setM_ulBlerFirstRequestNum(m_ulBlerFirstRequestNum == null ? m_ulBlerFirstRequestNum2
								: m_ulBlerFirstRequestNum
										+ m_ulBlerFirstRequestNum2);
			}
			/**
			 * 初始BLER:初传成功次数
			 * 
			 * @date 20160324 废弃
			 */
			Long m_ulBlerFirstSuccessNum = volteQualityBadRoad0
					.getM_ulBlerFirstSuccessNum();
			Long m_ulBlerFirstSuccessNum2 = volteQualityBadRoad
					.getM_ulBlerFirstSuccessNum();
			if (null != m_ulBlerFirstSuccessNum2) {
				volteQualityBadRoad0
						.setM_ulBlerFirstSuccessNum(m_ulBlerFirstSuccessNum == null ? m_ulBlerFirstSuccessNum2
								: m_ulBlerFirstSuccessNum
										+ m_ulBlerFirstSuccessNum2);
			}
			/**
			 * 残留BLER:多次重传后成功次数
			 * 
			 * @date 20160324 废弃
			 */
			Long m_ulBlerAgainSuccessNum = volteQualityBadRoad0
					.getM_ulBlerAgainSuccessNum();
			Long m_ulBlerAgainSuccessNum2 = volteQualityBadRoad
					.getM_ulBlerAgainSuccessNum();
			if (null != m_ulBlerAgainSuccessNum2) {
				volteQualityBadRoad0
						.setM_ulBlerAgainSuccessNum(m_ulBlerAgainSuccessNum == null ? m_ulBlerAgainSuccessNum2
								: m_ulBlerAgainSuccessNum
										+ m_ulBlerAgainSuccessNum2);
			}

			/**
			 * 初始BLER
			 * 
			 * @date 20160324 新增
			 */
			Float lBler = volteQualityBadRoad0.getlBler();
			Float lBler2 = volteQualityBadRoad.getlBler();
			if (null != lBler2) {
				lblerNum0++;
				volteQualityBadRoad0.setlBler(lBler == null ? lBler2 : lBler
						+ lBler2);
			}

			/**
			 * 残留BLER
			 * 
			 * @date 20160324 新增
			 */
			Float rBler = volteQualityBadRoad0.getrBler();
			Float rBler2 = volteQualityBadRoad.getrBler();
			if (null != rBler2) {
				rblerNum0++;
				volteQualityBadRoad0.setrBler(rBler == null ? rBler2 : rBler
						+ rBler2);
			}

			// RTP丢包率=(发送的VoIP数据包数量—接收的数据包数量)/发送的VoIP数据包数量
			/**
			 * RTP丢包率:发送的VoIP数据包数量
			 */
			Float m_dbRTPSendVoIPDataPackageNum = volteQualityBadRoad0
					.getM_dbRTPSendVoIPDataPackageNum();
			Float m_dbRTPSendVoIPDataPackageNum2 = volteQualityBadRoad
					.getM_dbRTPSendVoIPDataPackageNum();
			if (null != m_dbRTPSendVoIPDataPackageNum2) {
				volteQualityBadRoad0
						.setM_dbRTPSendVoIPDataPackageNum(m_dbRTPSendVoIPDataPackageNum == null ? m_dbRTPSendVoIPDataPackageNum2
								: m_dbRTPSendVoIPDataPackageNum
										+ m_dbRTPSendVoIPDataPackageNum2);
			}
			/**
			 * RTP丢包率:接收的数据包数量
			 */
			Float m_dbRTPReceiveDataPackageNum = volteQualityBadRoad0
					.getM_dbRTPReceiveDataPackageNum();
			Float m_dbRTPReceiveDataPackageNum2 = volteQualityBadRoad
					.getM_dbRTPReceiveDataPackageNum();
			if (null != m_dbRTPReceiveDataPackageNum2) {
				volteQualityBadRoad0
						.setM_dbRTPReceiveDataPackageNum(m_dbRTPReceiveDataPackageNum == null ? m_dbRTPReceiveDataPackageNum2
								: m_dbRTPReceiveDataPackageNum
										+ m_dbRTPReceiveDataPackageNum2);
			}
			/**
			 * RTP抖动(ms)=RTP取值之和/RTP数量
			 */
			Float m_dbRTPSum = volteQualityBadRoad0.getM_dbRTPSum();
			Float m_dbRTPSum2 = volteQualityBadRoad.getM_dbRTPSum();
			if (null != m_dbRTPSum2) {
				volteQualityBadRoad0
						.setM_dbRTPSum(m_dbRTPSum == null ? m_dbRTPSum2
								: m_dbRTPSum + m_dbRTPSum2);
			}
			Long m_ulRTPNum = volteQualityBadRoad0.getM_ulRTPNum();
			Long m_ulRTPNum2 = volteQualityBadRoad.getM_ulRTPNum();
			if (null != m_ulRTPNum2) {
				volteQualityBadRoad0
						.setM_ulRTPNum(m_ulRTPNum == null ? m_ulRTPNum2
								: m_ulRTPNum + m_ulRTPNum2);
			}
			/**
			 * TDL驻网时长
			 */
			Float m_dbTDLTestTime = volteQualityBadRoad0.getM_dbTDLTestTime();
			Float m_dbTDLTestTime2 = volteQualityBadRoad.getM_dbTDLTestTime();
			if (null != m_dbTDLTestTime2) {
				volteQualityBadRoad0
						.setM_dbTDLTestTime(m_dbTDLTestTime == null ? m_dbTDLTestTime2
								: m_dbTDLTestTime + m_dbTDLTestTime2);
			}
			/**
			 * TDS驻网时长
			 */
			Float m_dbTDSTestTime = volteQualityBadRoad0.getM_dbTDSTestTime();
			Float m_dbTDSTestTime2 = volteQualityBadRoad.getM_dbTDSTestTime();
			if (null != m_dbTDSTestTime2) {
				volteQualityBadRoad0
						.setM_dbTDSTestTime(m_dbTDSTestTime == null ? m_dbTDSTestTime2
								: m_dbTDSTestTime + m_dbTDSTestTime2);
			}
			/**
			 * GSM驻网时长
			 */
			Float m_dbGSMTestTime = volteQualityBadRoad0.getM_dbGSMTestTime();
			Float m_dbGSMTestTime2 = volteQualityBadRoad.getM_dbGSMTestTime();
			if (null != m_dbGSMTestTime2) {
				volteQualityBadRoad0
						.setM_dbGSMTestTime(m_dbGSMTestTime == null ? m_dbGSMTestTime2
								: m_dbGSMTestTime + m_dbGSMTestTime2);
			}

			/**
			 * 问题路段MOS采样点个数
			 */
			Long m_ulMosPointNum = volteQualityBadRoad0.getM_ulMosPointNum();
			Long m_ulMosPointNum2 = volteQualityBadRoad.getM_ulMosPointNum();
			if (null != m_ulMosPointNum2) {
				volteQualityBadRoad0
						.setM_ulMosPointNum(m_ulMosPointNum == null ? m_ulMosPointNum2
								: m_ulMosPointNum + m_ulMosPointNum2);
			}

			/**
			 * 问题路段MOS>=3.0采样点个数
			 */
			Long m_ulMosOver30PointNum = volteQualityBadRoad0
					.getM_ulMosOver30PointNum();
			Long m_ulMosOver30PointNum2 = volteQualityBadRoad
					.getM_ulMosOver30PointNum();
			if (null != m_ulMosOver30PointNum2) {
				volteQualityBadRoad0
						.setM_ulMosOver30PointNum(m_ulMosOver30PointNum == null ? m_ulMosOver30PointNum2
								: m_ulMosOver30PointNum
										+ m_ulMosOver30PointNum2);
			}

			/**
			 * 问题路段MOS>=3.5采样点个数
			 */
			Long m_ulMosOver35PointNum = volteQualityBadRoad0
					.getM_ulMosOver35PointNum();
			Long m_ulMosOver35PointNum2 = volteQualityBadRoad
					.getM_ulMosOver35PointNum();
			if (null != m_ulMosOver35PointNum2) {
				volteQualityBadRoad0
						.setM_ulMosOver35PointNum(m_ulMosOver35PointNum == null ? m_ulMosOver35PointNum2
								: m_ulMosOver35PointNum
										+ m_ulMosOver35PointNum2);
			}

			/**
			 * 问题路段sinr值之和
			 */
			Float m_dbSinrValueSum = volteQualityBadRoad0.getM_dbSinrValueSum();
			Float m_dbSinrValueSum2 = volteQualityBadRoad.getM_dbSinrValueSum();
			if (null != m_dbSinrValueSum2) {
				volteQualityBadRoad0
						.setM_dbSinrValueSum(m_dbSinrValueSum == null ? m_dbSinrValueSum2
								: m_dbSinrValueSum + m_dbSinrValueSum2);
			}

			/**
			 * 问题路段rsrp值之和
			 */
			Float m_dbRsrpValueSum = volteQualityBadRoad0.getM_dbRsrpValueSum();
			Float m_dbRsrpValueSum2 = volteQualityBadRoad.getM_dbRsrpValueSum();
			if (null != m_dbRsrpValueSum2) {
				volteQualityBadRoad0
						.setM_dbRsrpValueSum(m_dbRsrpValueSum == null ? m_dbRsrpValueSum2
								: m_dbRsrpValueSum + m_dbRsrpValueSum2);
			}

			/**
			 * 问题路段rsrp采样点个数,问题路段sinr采样点个数,问题路段总的采样点个数
			 */
			Long m_ulRsrpOrSinrPointNum = volteQualityBadRoad0
					.getM_ulRsrpOrSinrPointNum();
			Long m_ulRsrpOrSinrPointNum2 = volteQualityBadRoad
					.getM_ulRsrpOrSinrPointNum();
			if (null != m_ulRsrpOrSinrPointNum2) {
				volteQualityBadRoad0
						.setM_ulRsrpOrSinrPointNum(m_ulRsrpOrSinrPointNum == null ? m_ulRsrpOrSinrPointNum2
								: m_ulRsrpOrSinrPointNum
										+ m_ulRsrpOrSinrPointNum2);
			}

			/**
			 * 问题路段lte同频切换成功次数
			 */
			Long m_ulLteIntraHoSuccNum = volteQualityBadRoad0
					.getM_ulLteIntraHoSuccNum();
			Long m_ulLteIntraHoSuccNum2 = volteQualityBadRoad
					.getM_ulLteIntraHoSuccNum();
			if (null != m_ulLteIntraHoSuccNum2) {
				volteQualityBadRoad0
						.setM_ulLteIntraHoSuccNum(m_ulLteIntraHoSuccNum == null ? m_ulLteIntraHoSuccNum2
								: m_ulLteIntraHoSuccNum
										+ m_ulLteIntraHoSuccNum2);
			}

			/**
			 * 问题路段lte异频切换成功次数
			 */
			Long m_ulLteInterHoSuccNum = volteQualityBadRoad0
					.getM_ulLteInterHoSuccNum();
			Long m_ulLteInterHoSuccNum2 = volteQualityBadRoad
					.getM_ulLteInterHoSuccNum();
			if (null != m_ulLteInterHoSuccNum2) {
				volteQualityBadRoad0
						.setM_ulLteInterHoSuccNum(m_ulLteInterHoSuccNum == null ? m_ulLteInterHoSuccNum2
								: m_ulLteInterHoSuccNum
										+ m_ulLteInterHoSuccNum2);
			}

			/**
			 * 问题路段lte同频切换尝试次数
			 */
			Long m_ulLteIntraHoReqNum = volteQualityBadRoad0
					.getM_ulLteIntraHoReqNum();
			Long m_ulLteIntraHoReqNum2 = volteQualityBadRoad
					.getM_ulLteIntraHoReqNum();
			if (null != m_ulLteIntraHoReqNum2) {
				volteQualityBadRoad0
						.setM_ulLteIntraHoReqNum(m_ulLteIntraHoReqNum == null ? m_ulLteIntraHoReqNum2
								: m_ulLteIntraHoReqNum + m_ulLteIntraHoReqNum2);
			}

			/**
			 * 问题路段lte异频切换尝试次数
			 */
			Long m_ulLteInterHoReqNum = volteQualityBadRoad0
					.getM_ulLteInterHoReqNum();
			Long m_ulLteInterHoReqNum2 = volteQualityBadRoad
					.getM_ulLteInterHoReqNum();
			if (null != m_ulLteInterHoReqNum2) {
				volteQualityBadRoad0
						.setM_ulLteInterHoReqNum(m_ulLteInterHoReqNum == null ? m_ulLteInterHoReqNum2
								: m_ulLteInterHoReqNum + m_ulLteInterHoReqNum2);
			}

			/**
			 * 问题路段满足覆盖条件(RSRP>=-110&SINR>=-3)的采样点
			 */
			Long m_ulLteCoverNum = volteQualityBadRoad0.getM_ulLteCoverNum();
			Long m_ulLteCoverNum2 = volteQualityBadRoad.getM_ulLteCoverNum();
			if (null != m_ulLteCoverNum2) {
				volteQualityBadRoad0
						.setM_ulLteCoverNum(m_ulLteCoverNum == null ? m_ulLteCoverNum2
								: m_ulLteCoverNum + m_ulLteCoverNum2);
			}

		}
		/**
		 * 计算volte平均mos
		 */
		Long m_ulLTE_MosPointNum = volteQualityBadRoad0
				.getM_ulLTE_MosPointNum();
		Float m_dbLTE_MosValueSum = volteQualityBadRoad0
				.getM_dbLTE_MosValueSum();
		if (null != m_ulLTE_MosPointNum && m_ulLTE_MosPointNum != 0
				&& null != m_dbLTE_MosValueSum) {
			wholeIndexResponseBean.setVolteMosAvg(NumberFormatUtils.format(
					m_dbLTE_MosValueSum / m_ulLTE_MosPointNum, 2));
		}

		// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
		// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
		/**
		 * 计算初始BLER,残留BLER
		 * 
		 * @date 20160324 废弃
		 * 
		 */
		float m_ulBlerFirstRequestNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getM_ulBlerFirstRequestNum());
		float m_ulBlerFirstSuccessNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getM_ulBlerFirstSuccessNum());
		float m_ulBlerAgainSuccessNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getM_ulBlerAgainSuccessNum());
		if (0 != m_ulBlerFirstRequestNum) {
			wholeIndexResponseBean.setFirstBler(NumberFormatUtils.format(
					(m_ulBlerFirstRequestNum - m_ulBlerFirstSuccessNum)
							/ m_ulBlerFirstRequestNum * 100, 2));
			wholeIndexResponseBean.setResidualBler(NumberFormatUtils.format(
					(m_ulBlerFirstRequestNum - m_ulBlerAgainSuccessNum)
							/ m_ulBlerFirstRequestNum * 100, 2));
		}

		/**
		 * 计算初始BLER,残留BLER
		 * 
		 * @date 20160324 新增
		 * 
		 */
		float lbler = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getlBler());
		float rbler = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getrBler());
		if (0 != lblerNum0) {
			wholeIndexResponseBean.setFirstBler(NumberFormatUtils.format(lbler
					/ lblerNum0, 2));
		}
		if (0 != rblerNum0) {
			wholeIndexResponseBean.setResidualBler(NumberFormatUtils.format(
					rbler / rblerNum0, 2));
		}

		/**
		 * 计算RTP丢包率=(发送的VoIP数据包数量—接收的数据包数量)/发送的VoIP数据包数量 @date 20160324废弃
		 * 计算RTP丢包率=发送的VoIP数据包数量/接收的数据包数量 @date 20160324更新
		 * 
		 */
		float m_dbRTPSendVoIPDataPackageNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0
						.getM_dbRTPSendVoIPDataPackageNum());
		float m_dbRTPReceiveDataPackageNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoad0.getM_dbRTPReceiveDataPackageNum());
		// @date 20160324废弃
		if (0 != m_dbRTPSendVoIPDataPackageNum) {
			wholeIndexResponseBean
					.setRtpDropRatio(NumberFormatUtils
							.format((m_dbRTPSendVoIPDataPackageNum - m_dbRTPReceiveDataPackageNum)
									/ m_dbRTPSendVoIPDataPackageNum * 100, 2));
		}
		// @date 20160324更新
		if (0 != m_dbRTPReceiveDataPackageNum) {
			wholeIndexResponseBean.setRtpDropRatio(NumberFormatUtils.format(
					m_dbRTPSendVoIPDataPackageNum
							/ m_dbRTPReceiveDataPackageNum, 2));
		}

		/**
		 * 计算RTP抖动(ms)=RTP取值之和/RTP数量
		 */
		Float m_dbRTPSum = volteQualityBadRoad0.getM_dbRTPSum();
		Long m_ulRTPNum = volteQualityBadRoad0.getM_ulRTPNum();
		if (null != m_ulRTPNum && m_ulRTPNum != 0 && null != m_dbRTPSum) {
			wholeIndexResponseBean.setRtpShake(NumberFormatUtils.format(
					m_dbRTPSum / m_ulRTPNum, 2));
		}

		/**
		 * 计算tdl,tds,gsm驻网时长
		 */
		if (null != volteQualityBadRoad0.getM_dbTDLTestTime()) {
			wholeIndexResponseBean.setTdlTestTime(volteQualityBadRoad0
					.getM_dbTDLTestTime() / 3600000);
		}
		if (null != volteQualityBadRoad0.getM_dbTDSTestTime()) {
			wholeIndexResponseBean.setTdsTestTime(volteQualityBadRoad0
					.getM_dbTDSTestTime() / 3600000);
		}
		if (null != volteQualityBadRoad0.getM_dbGSMTestTime()) {
			wholeIndexResponseBean.setGsmTestTime(volteQualityBadRoad0
					.getM_dbGSMTestTime() / 3600000);
		}

		/**
		 * 计算MOS>=3.0占比
		 */
		Long m_ulMosOver30PointNum = volteQualityBadRoad0
				.getM_ulMosOver30PointNum();
		Long m_ulMosPointNum = volteQualityBadRoad0.getM_ulMosPointNum();
		if (null != m_ulMosPointNum && m_ulMosPointNum != 0
				&& null != m_ulMosOver30PointNum) {
			wholeIndexResponseBean.setPsMosOver30Rate(NumberFormatUtils.format(
					m_ulMosOver30PointNum * 1f / m_ulMosPointNum, 2));
		}

		/**
		 * 计算MOS>=3.5占比
		 */
		Long m_ulMosOver35PointNum = volteQualityBadRoad0
				.getM_ulMosOver35PointNum();
		if (null != m_ulMosPointNum && m_ulMosPointNum != 0
				&& null != m_ulMosOver35PointNum) {
			wholeIndexResponseBean.setPsMosOver30Rate(NumberFormatUtils.format(
					m_ulMosOver35PointNum * 1f / m_ulMosPointNum, 2));
		}

		/**
		 * 计算sinr均值
		 */
		Float m_dbSinrValueSum = volteQualityBadRoad0.getM_dbSinrValueSum();
		Long m_ulRsrpOrSinrPointNum = volteQualityBadRoad0
				.getM_ulRsrpOrSinrPointNum();
		if (null != m_ulRsrpOrSinrPointNum && m_ulRsrpOrSinrPointNum != 0
				&& null != m_dbSinrValueSum) {
			wholeIndexResponseBean.setSinrAvg(NumberFormatUtils.format(
					m_dbSinrValueSum / m_ulRsrpOrSinrPointNum, 2));
		}

		/**
		 * 计算rsrp均值
		 */
		Float m_dbRsrpValueSum = volteQualityBadRoad0.getM_dbRsrpValueSum();
		if (null != m_ulRsrpOrSinrPointNum && m_ulRsrpOrSinrPointNum != 0
				&& null != m_dbRsrpValueSum) {
			wholeIndexResponseBean.setRsrpAvg(NumberFormatUtils.format(
					m_dbRsrpValueSum / m_ulRsrpOrSinrPointNum, 2));
		}

		/**
		 * 计算LTE切换成功率
		 */
		Long m_ulLteIntraHoSuccNum = volteQualityBadRoad0
				.getM_ulLteIntraHoSuccNum();
		Long m_ulLteInterHoSuccNum = volteQualityBadRoad0
				.getM_ulLteInterHoSuccNum();
		Long m_ulLteIntraHoReqNum = volteQualityBadRoad0
				.getM_ulLteIntraHoReqNum();
		Long m_ulLteInterHoReqNum = volteQualityBadRoad0
				.getM_ulLteInterHoReqNum();
		if ((null != m_ulLteIntraHoReqNum && null != m_ulLteInterHoReqNum)
				&& (m_ulLteIntraHoReqNum + m_ulLteInterHoReqNum) != 0
				&& (null != m_ulLteIntraHoSuccNum && null != m_ulLteInterHoSuccNum)
				&& (m_ulLteIntraHoSuccNum + m_ulLteInterHoSuccNum) != 0) {
			wholeIndexResponseBean.setLteHoSuccRate(NumberFormatUtils.format(
					((float) (m_ulLteIntraHoSuccNum + m_ulLteInterHoSuccNum))
							/ (m_ulLteIntraHoReqNum + m_ulLteInterHoReqNum)
							* 100, 2));
		}

		/**
		 * 计算LTE覆盖率
		 */
		Long m_ulLteCoverNum = volteQualityBadRoad0.getM_ulLteCoverNum();
		if (null != m_ulRsrpOrSinrPointNum && m_ulRsrpOrSinrPointNum != 0
				&& null != m_ulLteCoverNum && m_ulLteCoverNum != 0) {
			wholeIndexResponseBean.setLteCoverRate(NumberFormatUtils
					.format(((float) m_ulLteCoverNum) / m_ulRsrpOrSinrPointNum
							* 100, 2));
		}

		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#doWholeIndex1Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doWholeIndex1Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean1> rows = new ArrayList<>();
		WholeIndexResponseBean1 wholeIndexResponseBean1 = new WholeIndexResponseBean1();
		rows.add(wholeIndexResponseBean1);
		easyuiPageList.setRows(rows);
		if (null == volteQualityBadRoadsByLogIds
				|| 0 == volteQualityBadRoadsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置语音质差路段数量
		 */
		wholeIndexResponseBean1.setRoadNum(volteQualityBadRoadsByLogIds.size());
		Float mosPointNum = null;// 所有问题路段:mos采样点数量
		Float cellNum = null;// 所有问题路段:小区数量
		Set<String> boxIdSet = new HashSet<>();// 所有问题路段:设备数量,以设备boxid为准
		Float mosPointTotalNum = null;// 所有测试日志:mos采样点数量
		Float cellTotalNum = null;// 所有测试日志:小区数量
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VolteQualityBadRoad volteQualityBadRoad : volteQualityBadRoadsByLogIds) {
			/**
			 * 计算所有问题路段:总里程和总测试时长
			 */
			Float distance = wholeIndexResponseBean1.getDistance();
			Float m_dbDistance = volteQualityBadRoad.getM_dbDistance();
			Long continueTime = wholeIndexResponseBean1.getContinueTime();
			Long m_dbContinueTime = volteQualityBadRoad.getM_dbContinueTime();
			if (null != m_dbDistance) {
				wholeIndexResponseBean1
						.setDistance(distance == null ? m_dbDistance : distance
								+ m_dbDistance);
			}
			if (null != m_dbContinueTime) {
				wholeIndexResponseBean1
						.setContinueTime(continueTime == null ? m_dbContinueTime
								: continueTime + m_dbContinueTime);
			}
			/**
			 * 计算所有问题路段:mos采样点数量和小区数
			 * 
			 */
			Long m_ulMosPointNum = volteQualityBadRoad.getM_ulMosPointNum();
			Long m_ulCellNum = volteQualityBadRoad.getM_ulCellNum();
			if (null != m_ulMosPointNum) {
				mosPointNum = (mosPointNum == null ? m_ulMosPointNum
						: mosPointNum + m_ulMosPointNum);
			}
			if (null != m_ulCellNum) {
				cellNum = (cellNum == null ? m_ulCellNum : cellNum
						+ m_ulCellNum);
			}
			/**
			 * 计算所有问题路段:设备数量,以设备boxid为准
			 */
			String boxId = volteQualityBadRoad.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
		}
		for (TestLogItem testLog : queryTestLogItems) {
			/**
			 * 计算所有测试日志:mos采样点数量和小区数
			 * 
			 */
			Long testLogMosPointNum = testLog.getMosPointNum();
			Long testLogCellNum = testLog.getCellSumNum();
			if (null != testLogMosPointNum) {
				mosPointTotalNum = (mosPointTotalNum == null ? testLogMosPointNum
						: mosPointTotalNum + testLogMosPointNum);
			}
			if (null != testLogCellNum) {
				cellTotalNum = (cellTotalNum == null ? testLogCellNum
						: cellTotalNum + testLogCellNum);
			}
			/**
			 * 计算所有测试日志:设备数量,以设备boxid为准
			 */
			String boxId = testLog.getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdTotalSet.add(boxId);
			}
		}
		// 汇总计算采样点占比,小区占比,终端数量占比
		if (null != mosPointTotalNum && mosPointTotalNum != 0
				&& null != mosPointNum) {
			wholeIndexResponseBean1.setMosPointNumRatio(NumberFormatUtils
					.format(mosPointNum / mosPointTotalNum * 100, 2));
		}
		if (null != cellTotalNum && cellTotalNum != 0 && null != cellNum) {
			wholeIndexResponseBean1.setCellNumRatio(NumberFormatUtils.format(
					cellNum / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size()) {
			wholeIndexResponseBean1.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#doWholeIndex2Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doWholeIndex2Analysis(List<Long> ids) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean2> rows = new ArrayList<>();
		WholeIndexResponseBean2 wholeIndexResponseBean2 = new WholeIndexResponseBean2();
		rows.add(wholeIndexResponseBean2);
		easyuiPageList.setRows(rows);
		if (null == ids || 0 == ids.size()) {
			return easyuiPageList;
		}
		wholeIndexResponseBean2.setWeakCover(weakCoverDao
				.queryWeakCoverRoadNumByLogIds(ids));
		wholeIndexResponseBean2.setDisturb(disturbDao
				.queryDisturbRoadNumByLogIds(ids));
		wholeIndexResponseBean2.setNbCell(nbCellDeficiencyDao
				.queryNbDeficiencyRoadNumByLogIds(ids));
		wholeIndexResponseBean2.setParamError(paramErrorDao
				.queryParamErrorRoadNumByLogIds(ids));
		wholeIndexResponseBean2.setCoreNetwork(coreNetworkDao
				.queryCoreNetworkRoadNumByLogIds(ids));
		wholeIndexResponseBean2.setOtherProblem(otherDao
				.queryOtherRoadNumByLogIds(ids));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#doWholeIndex3Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doWholeIndex3Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean3> rows = new ArrayList<>();
		WholeIndexResponseBean3 wholeIndexResponseBean3 = new WholeIndexResponseBean3();
		rows.add(wholeIndexResponseBean3);
		easyuiPageList.setRows(rows);
		if (null == volteQualityBadRoadsByLogIds
				|| 0 == volteQualityBadRoadsByLogIds.size()) {
			return easyuiPageList;
		}
		for (VolteQualityBadRoad volteQualityBadRoad : volteQualityBadRoadsByLogIds) {

			Long m_ulTianKuiCellNum = volteQualityBadRoad
					.getM_ulTianKuiCellNum();
			Integer tianKuiCellNum = wholeIndexResponseBean3
					.getTianKuiCellNum();
			if (null != m_ulTianKuiCellNum) {
				wholeIndexResponseBean3
						.setTianKuiCellNum((int) (null == tianKuiCellNum ? m_ulTianKuiCellNum
								: tianKuiCellNum + m_ulTianKuiCellNum));
			}
			Long m_ulCanShuCellNum = volteQualityBadRoad.getM_ulCanShuCellNum();
			Integer paramCellNum = wholeIndexResponseBean3.getParamCellNum();
			if (null != m_ulCanShuCellNum) {
				wholeIndexResponseBean3
						.setParamCellNum((int) (null == paramCellNum ? m_ulCanShuCellNum
								: paramCellNum + m_ulCanShuCellNum));
			}
			Long m_ulLinQuCellNum = volteQualityBadRoad.getM_ulLinQuCellNum();
			Integer nbCellNum = wholeIndexResponseBean3.getNbCellNum();
			if (null != m_ulLinQuCellNum) {
				wholeIndexResponseBean3
						.setNbCellNum((int) (null == nbCellNum ? m_ulLinQuCellNum
								: nbCellNum + m_ulLinQuCellNum));
			}
			Long m_ulPCICellNum = volteQualityBadRoad.getM_ulPCICellNum();
			Integer pciCellNum = wholeIndexResponseBean3.getPciCellNum();
			if (null != m_ulPCICellNum) {
				wholeIndexResponseBean3
						.setPciCellNum((int) (null == pciCellNum ? m_ulPCICellNum
								: pciCellNum + m_ulPCICellNum));
			}
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#doWholeIndex4Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doWholeIndex4Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean4> rows = new ArrayList<>();
		WholeIndexResponseBean4 wholeIndexResponseBean4 = new WholeIndexResponseBean4();
		rows.add(wholeIndexResponseBean4);
		easyuiPageList.setRows(rows);
		if (null == volteQualityBadRoadsByLogIds
				|| 0 == volteQualityBadRoadsByLogIds.size()) {
			return easyuiPageList;
		}
		VolteQualityBadRoad totalQBR = new VolteQualityBadRoad();
		for (VolteQualityBadRoad volteQualityBadRoad : volteQualityBadRoadsByLogIds) {
			Float m_dbCS_CS_CalledMosValueSum = volteQualityBadRoad
					.getM_dbCS_CS_CalledMosValueSum();
			Float m_dbCS_CS_CalledMosValueSum2 = totalQBR
					.getM_dbCS_CS_CalledMosValueSum();
			if (null != m_dbCS_CS_CalledMosValueSum) {
				totalQBR.setM_dbCS_CS_CalledMosValueSum(null == m_dbCS_CS_CalledMosValueSum2 ? m_dbCS_CS_CalledMosValueSum
						: m_dbCS_CS_CalledMosValueSum2
								+ m_dbCS_CS_CalledMosValueSum);
			}
			Float m_dbCS_CS_CallingMosValueSum = volteQualityBadRoad
					.getM_dbCS_CS_CallingMosValueSum();
			Float m_dbCS_CS_CallingMosValueSum2 = totalQBR
					.getM_dbCS_CS_CallingMosValueSum();
			if (null != m_dbCS_CS_CallingMosValueSum) {
				totalQBR.setM_dbCS_CS_CallingMosValueSum(null == m_dbCS_CS_CallingMosValueSum2 ? m_dbCS_CS_CallingMosValueSum
						: m_dbCS_CS_CallingMosValueSum2
								+ m_dbCS_CS_CallingMosValueSum);
			}
			Float m_dbCS_LTE_CalledMosValueSum = volteQualityBadRoad
					.getM_dbCS_LTE_CalledMosValueSum();
			Float m_dbCS_LTE_CalledMosValueSum2 = totalQBR
					.getM_dbCS_LTE_CalledMosValueSum();
			if (null != m_dbCS_LTE_CalledMosValueSum) {
				totalQBR.setM_dbCS_LTE_CalledMosValueSum(null == m_dbCS_LTE_CalledMosValueSum2 ? m_dbCS_LTE_CalledMosValueSum
						: m_dbCS_LTE_CalledMosValueSum2
								+ m_dbCS_LTE_CalledMosValueSum);
			}
			Float m_dbCS_LTE_CallingMosValueSum = volteQualityBadRoad
					.getM_dbCS_LTE_CallingMosValueSum();
			Float m_dbCS_LTE_CallingMosValueSum2 = totalQBR
					.getM_dbCS_LTE_CallingMosValueSum();
			if (null != m_dbCS_LTE_CallingMosValueSum) {
				totalQBR.setM_dbCS_LTE_CallingMosValueSum(null == m_dbCS_LTE_CallingMosValueSum2 ? m_dbCS_LTE_CallingMosValueSum
						: m_dbCS_LTE_CallingMosValueSum2
								+ m_dbCS_LTE_CallingMosValueSum);
			}

			Long m_ulCS_CS_CalledMosPointNum = volteQualityBadRoad
					.getM_ulCS_CS_CalledMosPointNum();
			Long m_ulCS_CS_CalledMosPointNum2 = totalQBR
					.getM_ulCS_CS_CalledMosPointNum();
			if (null != m_ulCS_CS_CalledMosPointNum) {
				totalQBR.setM_ulCS_CS_CalledMosPointNum(null == m_ulCS_CS_CalledMosPointNum2 ? m_ulCS_CS_CalledMosPointNum
						: m_ulCS_CS_CalledMosPointNum2
								+ m_ulCS_CS_CalledMosPointNum);
			}
			Long m_ulCS_CS_CallingPointNum = volteQualityBadRoad
					.getM_ulCS_CS_CallingMosPointNum();
			Long m_ulCS_CS_CallingPointNum2 = totalQBR
					.getM_ulCS_CS_CallingMosPointNum();
			if (null != m_ulCS_CS_CallingPointNum) {
				totalQBR.setM_ulCS_CS_CallingMosPointNum(null == m_ulCS_CS_CallingPointNum2 ? m_ulCS_CS_CallingPointNum
						: m_ulCS_CS_CallingPointNum2
								+ m_ulCS_CS_CallingPointNum);
			}
			Long m_ulCS_LTE_CalledPointNum = volteQualityBadRoad
					.getM_ulCS_LTE_CalledMosPointNum();
			Long m_ulCS_LTE_CalledPointNum2 = totalQBR
					.getM_ulCS_LTE_CalledMosPointNum();
			if (null != m_ulCS_LTE_CalledPointNum) {
				totalQBR.setM_ulCS_LTE_CalledMosPointNum(null == m_ulCS_LTE_CalledPointNum2 ? m_ulCS_LTE_CalledPointNum
						: m_ulCS_LTE_CalledPointNum2
								+ m_ulCS_LTE_CalledPointNum);
			}
			Long m_ulCS_LTE_CallingPointNum = volteQualityBadRoad
					.getM_ulCS_LTE_CallingMosPointNum();
			Long m_ulCS_LTE_CallingPointNum2 = totalQBR
					.getM_ulCS_LTE_CallingMosPointNum();
			if (null != m_ulCS_LTE_CallingPointNum) {
				totalQBR.setM_ulCS_LTE_CallingMosPointNum(null == m_ulCS_LTE_CallingPointNum2 ? m_ulCS_LTE_CallingPointNum
						: m_ulCS_LTE_CallingPointNum2
								+ m_ulCS_LTE_CallingPointNum);
			}

			Float m_dbLTE_CS_CalledMosValueSum = volteQualityBadRoad
					.getM_dbLTE_CS_CalledMosValueSum();
			Float m_dbLTE_CS_CalledMosValueSum2 = totalQBR
					.getM_dbLTE_CS_CalledMosValueSum();
			if (null != m_dbLTE_CS_CalledMosValueSum) {
				totalQBR.setM_dbLTE_CS_CalledMosValueSum(null == m_dbLTE_CS_CalledMosValueSum2 ? m_dbLTE_CS_CalledMosValueSum
						: m_dbLTE_CS_CalledMosValueSum2
								+ m_dbLTE_CS_CalledMosValueSum);
			}
			Float m_dbLTE_CS_CallingMosValueSum = volteQualityBadRoad
					.getM_dbLTE_CS_CallingMosValueSum();
			Float m_dbLTE_CS_CallingMosValueSum2 = totalQBR
					.getM_dbLTE_CS_CallingMosValueSum();
			if (null != m_dbLTE_CS_CallingMosValueSum) {
				totalQBR.setM_dbLTE_CS_CallingMosValueSum(null == m_dbLTE_CS_CallingMosValueSum2 ? m_dbLTE_CS_CallingMosValueSum
						: m_dbLTE_CS_CallingMosValueSum2
								+ m_dbLTE_CS_CallingMosValueSum);
			}
			Float m_dbLTE_LTE_CalledMosValueSum = volteQualityBadRoad
					.getM_dbLTE_LTE_CalledMosValueSum();
			Float m_dbLTE_LTE_CalledMosValueSum2 = totalQBR
					.getM_dbLTE_LTE_CalledMosValueSum();
			if (null != m_dbLTE_LTE_CalledMosValueSum) {
				totalQBR.setM_dbLTE_LTE_CalledMosValueSum(null == m_dbLTE_LTE_CalledMosValueSum2 ? m_dbLTE_LTE_CalledMosValueSum
						: m_dbLTE_LTE_CalledMosValueSum2
								+ m_dbLTE_LTE_CalledMosValueSum);
			}
			Float m_dbLTE_LTE_CallingMosValueSum = volteQualityBadRoad
					.getM_dbLTE_LTE_CallingMosValueSum();
			Float m_dbLTE_LTE_CallingMosValueSum2 = totalQBR
					.getM_dbLTE_LTE_CallingMosValueSum();
			if (null != m_dbLTE_LTE_CallingMosValueSum) {
				totalQBR.setM_dbLTE_LTE_CallingMosValueSum(null == m_dbLTE_LTE_CallingMosValueSum2 ? m_dbLTE_LTE_CallingMosValueSum
						: m_dbLTE_LTE_CallingMosValueSum2
								+ m_dbLTE_LTE_CallingMosValueSum);
			}

			Long m_ulLTE_CS_CalledMosPointNum = volteQualityBadRoad
					.getM_ulLTE_CS_CalledMosPointNum();
			Long m_ulLTE_CS_CalledMosPointNum2 = totalQBR
					.getM_ulLTE_CS_CalledMosPointNum();
			if (null != m_ulLTE_CS_CalledMosPointNum) {
				totalQBR.setM_ulLTE_CS_CalledMosPointNum(null == m_ulLTE_CS_CalledMosPointNum2 ? m_ulLTE_CS_CalledMosPointNum
						: m_ulLTE_CS_CalledMosPointNum2
								+ m_ulLTE_CS_CalledMosPointNum);
			}
			Long m_ulLTE_CS_CallingPointNum = volteQualityBadRoad
					.getM_ulLTE_CS_CallingMosPointNum();
			Long m_ulLTE_CS_CallingPointNum2 = totalQBR
					.getM_ulLTE_CS_CallingMosPointNum();
			if (null != m_ulLTE_CS_CallingPointNum) {
				totalQBR.setM_ulLTE_CS_CallingMosPointNum(null == m_ulLTE_CS_CallingPointNum2 ? m_ulLTE_CS_CallingPointNum
						: m_ulLTE_CS_CallingPointNum2
								+ m_ulLTE_CS_CallingPointNum);
			}
			Long m_ulLTE_LTE_CalledPointNum = volteQualityBadRoad
					.getM_ulLTE_LTE_CalledMosPointNum();
			Long m_ulLTE_LTE_CalledPointNum2 = totalQBR
					.getM_ulLTE_LTE_CalledMosPointNum();
			if (null != m_ulLTE_LTE_CalledPointNum) {
				totalQBR.setM_ulLTE_LTE_CalledMosPointNum(null == m_ulLTE_LTE_CalledPointNum2 ? m_ulLTE_LTE_CalledPointNum
						: m_ulLTE_LTE_CalledPointNum2
								+ m_ulLTE_LTE_CalledPointNum);
			}
			Long m_ulLTE_LTE_CallingPointNum = volteQualityBadRoad
					.getM_ulLTE_LTE_CallingMosPointNum();
			Long m_ulLTE_LTE_CallingPointNum2 = totalQBR
					.getM_ulLTE_LTE_CallingMosPointNum();
			if (null != m_ulLTE_LTE_CallingPointNum) {
				totalQBR.setM_ulLTE_LTE_CallingMosPointNum(null == m_ulLTE_LTE_CallingPointNum2 ? m_ulLTE_LTE_CallingPointNum
						: m_ulLTE_LTE_CallingPointNum2
								+ m_ulLTE_LTE_CallingPointNum);
			}
		}
		Float m_dbCS_CS_CalledMosValueSum = totalQBR
				.getM_dbCS_CS_CalledMosValueSum();
		Long m_ulCS_CS_CalledMosPointNum = totalQBR
				.getM_ulCS_CS_CalledMosPointNum();
		if (null != m_ulCS_CS_CalledMosPointNum
				&& m_ulCS_CS_CalledMosPointNum != 0
				&& null != m_dbCS_CS_CalledMosValueSum) {
			wholeIndexResponseBean4.setCsCsCalledMosValueAvg(NumberFormatUtils
					.format(m_dbCS_CS_CalledMosValueSum
							/ m_ulCS_CS_CalledMosPointNum, 2));
		}
		Float m_dbCS_LTE_CalledMosValueSum = totalQBR
				.getM_dbCS_LTE_CalledMosValueSum();
		Long m_ulCS_LTE_CalledMosPointNum = totalQBR
				.getM_ulCS_LTE_CalledMosPointNum();
		if (null != m_ulCS_LTE_CalledMosPointNum
				&& m_ulCS_LTE_CalledMosPointNum != 0
				&& null != m_dbCS_LTE_CalledMosValueSum) {
			wholeIndexResponseBean4.setCsLteCalledMosValueAvg(NumberFormatUtils
					.format(m_dbCS_LTE_CalledMosValueSum
							/ m_ulCS_LTE_CalledMosPointNum, 2));
		}
		Float m_dbLTE_LTE_CalledMosValueSum = totalQBR
				.getM_dbLTE_LTE_CalledMosValueSum();
		Long m_ulLTE_LTE_CalledMosPointNum = totalQBR
				.getM_ulLTE_LTE_CalledMosPointNum();
		if (null != m_ulLTE_LTE_CalledMosPointNum
				&& m_ulLTE_LTE_CalledMosPointNum != 0
				&& null != m_dbLTE_LTE_CalledMosValueSum) {
			wholeIndexResponseBean4
					.setLteLteCalledMosValueAvg(NumberFormatUtils.format(
							m_dbLTE_LTE_CalledMosValueSum
									/ m_ulLTE_LTE_CalledMosPointNum, 2));
		}
		Float m_dbLTE_CS_CalledMosValueSum = totalQBR
				.getM_dbLTE_CS_CalledMosValueSum();
		Long m_ulLTE_CS_CalledMosPointNum = totalQBR
				.getM_ulLTE_CS_CalledMosPointNum();
		if (null != m_ulLTE_CS_CalledMosPointNum
				&& m_ulLTE_CS_CalledMosPointNum != 0
				&& null != m_dbLTE_CS_CalledMosValueSum) {
			wholeIndexResponseBean4.setLteCsCalledMosValueAvg(NumberFormatUtils
					.format(m_dbLTE_CS_CalledMosValueSum
							/ m_ulLTE_CS_CalledMosPointNum, 2));
		}

		Float m_dbCS_CS_CallingMosValueSum = totalQBR
				.getM_dbCS_CS_CallingMosValueSum();
		Long m_ulCS_CS_CallingMosPointNum = totalQBR
				.getM_ulCS_CS_CallingMosPointNum();
		if (null != m_ulCS_CS_CallingMosPointNum
				&& m_ulCS_CS_CallingMosPointNum != 0
				&& null != m_dbCS_CS_CallingMosValueSum) {
			wholeIndexResponseBean4.setCsCsCallingMosValueAvg(NumberFormatUtils
					.format(m_dbCS_CS_CallingMosValueSum
							/ m_ulCS_CS_CallingMosPointNum, 2));
		}
		Float m_dbCS_LTE_CallingMosValueSum = totalQBR
				.getM_dbCS_LTE_CallingMosValueSum();
		Long m_ulCS_LTE_CallingMosPointNum = totalQBR
				.getM_ulCS_LTE_CallingMosPointNum();
		if (null != m_ulCS_LTE_CallingMosPointNum
				&& m_ulCS_LTE_CallingMosPointNum != 0
				&& null != m_dbCS_LTE_CallingMosValueSum) {
			wholeIndexResponseBean4
					.setCsLteCallingMosValueAvg(NumberFormatUtils.format(
							m_dbCS_LTE_CallingMosValueSum
									/ m_ulCS_LTE_CallingMosPointNum, 2));
		}
		Float m_dbLTE_LTE_CallingMosValueSum = totalQBR
				.getM_dbLTE_LTE_CallingMosValueSum();
		Long m_ulLTE_LTE_CallingMosPointNum = totalQBR
				.getM_ulLTE_LTE_CallingMosPointNum();
		if (null != m_ulLTE_LTE_CallingMosPointNum
				&& m_ulLTE_LTE_CallingMosPointNum != 0
				&& null != m_dbLTE_LTE_CallingMosValueSum) {
			wholeIndexResponseBean4
					.setLteLteCallingMosValueAvg(NumberFormatUtils.format(
							m_dbLTE_LTE_CallingMosValueSum
									/ m_ulLTE_LTE_CallingMosPointNum, 2));
		}
		Float m_dbLTE_CS_CallingMosValueSum = totalQBR
				.getM_dbLTE_CS_CallingMosValueSum();
		Long m_ulLTE_CS_CallingMosPointNum = totalQBR
				.getM_ulLTE_CS_CallingMosPointNum();
		if (null != m_ulLTE_CS_CallingMosPointNum
				&& m_ulLTE_CS_CallingMosPointNum != 0
				&& null != m_dbLTE_CS_CallingMosValueSum) {
			wholeIndexResponseBean4
					.setLteCsCallingMosValueAvg(NumberFormatUtils.format(
							m_dbLTE_CS_CallingMosValueSum
									/ m_ulLTE_CS_CallingMosPointNum, 2));
		}

		Long csCsMosPoint = wholeIndexResponseBean4.getCsCsMosPoint();
		if (null != m_ulCS_CS_CalledMosPointNum) {
			if (null != csCsMosPoint) {
				csCsMosPoint += m_ulCS_CS_CalledMosPointNum;
			} else {
				csCsMosPoint = m_ulCS_CS_CalledMosPointNum;
			}
			wholeIndexResponseBean4.setCsCsMosPoint(csCsMosPoint);
		}
		if (null != m_ulCS_CS_CallingMosPointNum) {
			if (null != csCsMosPoint) {
				csCsMosPoint += m_ulCS_CS_CallingMosPointNum;
			} else {
				csCsMosPoint = m_ulCS_CS_CallingMosPointNum;
			}
			wholeIndexResponseBean4.setCsCsMosPoint(csCsMosPoint);
		}

		Long csLteMosPoint = wholeIndexResponseBean4.getCsLteMosPoint();
		if (null != m_ulCS_LTE_CalledMosPointNum) {
			if (null != csLteMosPoint) {
				csLteMosPoint += m_ulCS_LTE_CalledMosPointNum;
			} else {
				csLteMosPoint = m_ulCS_LTE_CalledMosPointNum;
			}
			wholeIndexResponseBean4.setCsLteMosPoint(csLteMosPoint);
		}
		if (null != m_ulCS_LTE_CallingMosPointNum) {
			if (null != csLteMosPoint) {
				csLteMosPoint += m_ulCS_LTE_CallingMosPointNum;
			} else {
				csLteMosPoint = m_ulCS_LTE_CallingMosPointNum;
			}
			wholeIndexResponseBean4.setCsLteMosPoint(csLteMosPoint);
		}

		Long lteLteMosPoint = wholeIndexResponseBean4.getLteLteMosPoint();
		if (null != m_ulLTE_LTE_CalledMosPointNum) {
			if (null != lteLteMosPoint) {
				lteLteMosPoint += m_ulLTE_LTE_CalledMosPointNum;
			} else {
				lteLteMosPoint = m_ulLTE_LTE_CalledMosPointNum;
			}
			wholeIndexResponseBean4.setLteLteMosPoint(lteLteMosPoint);
		}
		if (null != m_ulLTE_LTE_CallingMosPointNum) {
			if (null != lteLteMosPoint) {
				lteLteMosPoint += m_ulLTE_LTE_CallingMosPointNum;
			} else {
				lteLteMosPoint = m_ulLTE_LTE_CallingMosPointNum;
			}
			wholeIndexResponseBean4.setLteLteMosPoint(lteLteMosPoint);
		}

		Long lteCsMosPoint = wholeIndexResponseBean4.getLteCsMosPoint();
		if (null != m_ulLTE_CS_CalledMosPointNum) {
			if (null != lteCsMosPoint) {
				lteCsMosPoint += m_ulLTE_CS_CalledMosPointNum;
			} else {
				lteCsMosPoint = m_ulLTE_CS_CalledMosPointNum;
			}
			wholeIndexResponseBean4.setLteCsMosPoint(lteCsMosPoint);
		}

		if (null != m_ulLTE_CS_CallingMosPointNum) {
			if (null != lteCsMosPoint) {
				lteCsMosPoint += m_ulLTE_CS_CallingMosPointNum;
			} else {
				lteCsMosPoint = m_ulLTE_CS_CallingMosPointNum;
			}
			wholeIndexResponseBean4.setLteCsMosPoint(lteCsMosPoint);
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getWeakCoverRoadsByLogIds(java.util.List)
	 */
	@Override
	public List<VolteQualityBadRoadWeakCover> getWeakCoverRoadsByLogIds(
			List<Long> testLogItemIds) {
		return weakCoverDao.queryWeakCoverRoadsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getWeakCoverRoadById(java.lang.Long)
	 */
	@Override
	public VolteQualityBadRoadWeakCover getWeakCoverRoadById(Long id) {
		return weakCoverDao.find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doWeakCoverAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover)
	 */
	@Override
	public Map<String, EasyuiPageList> doWeakCoverAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("weakCoverRoadIndex",
				doWeakCoverIndexAnalysis(volteQualityBadRoadWeakCover));
		map.put("weakCoverRoadCellInfo",
				doWeakCoverCellInfoAnalysis(volteQualityBadRoadWeakCover));
		map.put("weakCoverRoadTianKuiAdjustCellInfo",
				doWeakCoverTianKuiAdjustAnalysis(volteQualityBadRoadWeakCover));
		map.put("weakCoverRoadTiankuiConnectReverseCellInfo",
				doWeakCoverTianKuiConnectReverseAnalysis(volteQualityBadRoadWeakCover));
		map.put("weakCoverRoadAdviceAddStationCellInfo",
				doWeakCoverAdviceAddStationAnalysis(volteQualityBadRoadWeakCover));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doWeakCoverIndexAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover)
	 */
	@Override
	public EasyuiPageList doWeakCoverIndexAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		if (null == volteQualityBadRoadWeakCover) {
			return new EasyuiPageList();
		}
		WeakCoverIndexResponseBean weakCoverIndexResponseBean = new WeakCoverIndexResponseBean();
		// 计算主被叫
		Integer callType = volteQualityBadRoadWeakCover.getTestLogItem()
				.getCallType();
		if (null != callType) {
			switch (callType) {
			case 0:
				weakCoverIndexResponseBean.setCallType("主叫");
				break;
			case 1:
				weakCoverIndexResponseBean.setCallType("被叫");
				break;
			case 2:
				weakCoverIndexResponseBean.setCallType("都包含");
				break;
			default:
				break;
			}
		}
		// 计算MOS均值
		weakCoverIndexResponseBean.setMosAvg(volteQualityBadRoadWeakCover
				.getMosAvg());

		// 计算码字0:初始bler,残留bler @date 20160324 废弃
		// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
		// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
		float codon0BlerFirstRequestNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoadWeakCover
						.getCodon0BlerFirstRequestNum());
		float codon0BlerFirstSuccessNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoadWeakCover
						.getCodon0BlerFirstSuccessNum());
		float codon0BlerAgainSuccessNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoadWeakCover
						.getCodon0BlerAgainSuccessNum());
		if (codon0BlerFirstRequestNum != 0.0f) {
			weakCoverIndexResponseBean
					.setCodon0FirstBler(NumberFormatUtils
							.format((codon0BlerFirstRequestNum - codon0BlerFirstSuccessNum)
									/ codon0BlerFirstRequestNum * 100, 2));
			weakCoverIndexResponseBean
					.setCodon0ResidualBler(NumberFormatUtils
							.format((codon0BlerFirstRequestNum - codon0BlerAgainSuccessNum)
									/ codon0BlerFirstRequestNum * 100, 2));
		}

		// 计算码字0:初始bler,残留bler @date 20160324 新增
		if (null != volteQualityBadRoadWeakCover.getCode0lBler()) {
			weakCoverIndexResponseBean
					.setCodon0FirstBler(volteQualityBadRoadWeakCover
							.getCode0lBler());
		}
		if (null != volteQualityBadRoadWeakCover.getCode0rBler()) {
			weakCoverIndexResponseBean
					.setCodon0ResidualBler(volteQualityBadRoadWeakCover
							.getCode0rBler());
		}

		// 计算码字1:初始bler,残留bler @date 20160324 废弃
		// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
		// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
		float codon1BlerFirstRequestNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoadWeakCover
						.getCodon1BlerFirstRequestNum());
		float codon1BlerFirstSuccessNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoadWeakCover
						.getCodon1BlerFirstSuccessNum());
		float codon1BlerAgainSuccessNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoadWeakCover
						.getCodon1BlerAgainSuccessNum());
		if (codon1BlerFirstRequestNum != 0.0f) {
			weakCoverIndexResponseBean
					.setCodon1FirstBler(NumberFormatUtils
							.format((codon1BlerFirstRequestNum - codon1BlerFirstSuccessNum)
									/ codon1BlerFirstRequestNum * 100, 2));
			weakCoverIndexResponseBean
					.setCodon1ResidualBler(NumberFormatUtils
							.format((codon1BlerFirstRequestNum - codon1BlerAgainSuccessNum)
									/ codon1BlerFirstRequestNum * 100, 2));
		}

		// 计算码字1:初始bler,残留bler @date 20160324 新增
		if (null != volteQualityBadRoadWeakCover.getCode1lBler()) {
			weakCoverIndexResponseBean
					.setCodon1FirstBler(volteQualityBadRoadWeakCover
							.getCode1lBler());
		}
		if (null != volteQualityBadRoadWeakCover.getCode1rBler()) {
			weakCoverIndexResponseBean
					.setCodon1ResidualBler(volteQualityBadRoadWeakCover
							.getCode1rBler());
		}

		// 计算RTP丢包率:（发送的VoIP数据包数量—接收的数据包数量）/发送的VoIP数据包数量*100%
		Float m_dbRTPSendVoIPDataPackageNum = volteQualityBadRoadWeakCover
				.getM_dbRTPSendVoIPDataPackageNum();
		Float m_dbRTPReceiveDataPackageNum = NumberFormatUtils
				.isEmpty(volteQualityBadRoadWeakCover
						.getM_dbRTPReceiveDataPackageNum());
		// @date 20160324废弃
		if ((null != m_dbRTPSendVoIPDataPackageNum
				&& !m_dbRTPSendVoIPDataPackageNum.equals(0.0f) && null != m_dbRTPReceiveDataPackageNum)) {
			weakCoverIndexResponseBean
					.setRtpDropRatio(NumberFormatUtils
							.format((m_dbRTPSendVoIPDataPackageNum - m_dbRTPReceiveDataPackageNum)
									/ m_dbRTPSendVoIPDataPackageNum * 100, 2));
		}
		// @date 20160324更新
		if (0 != m_dbRTPReceiveDataPackageNum) {
			weakCoverIndexResponseBean.setRtpDropRatio(NumberFormatUtils
					.format(m_dbRTPSendVoIPDataPackageNum
							/ m_dbRTPReceiveDataPackageNum, 2));
		}

		// 计算RTP抖动:(RTP_SUM(浮点型):RTP取值之和)/(RTP_NUM(整型):RTP采样点数)
		Float m_dbRTPSum = volteQualityBadRoadWeakCover.getM_dbRTPSum();
		Long m_ulRTPNum = volteQualityBadRoadWeakCover.getM_ulRTPNum();
		if (null != m_ulRTPNum && !m_ulRTPNum.equals(0l) && null != m_dbRTPSum) {
			weakCoverIndexResponseBean.setRtpShake(NumberFormatUtils.format(
					m_dbRTPSum / m_ulRTPNum, 2));
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WeakCoverIndexResponseBean> rows = new ArrayList<>();
		rows.add(weakCoverIndexResponseBean);
		easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doWeakCoverCellInfoAnalysis(com.datang.domain.
	 * VoLTEDissertation.qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover)
	 */
	@Override
	public EasyuiPageList doWeakCoverCellInfoAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		if (null == volteQualityBadRoadWeakCover
				|| null == volteQualityBadRoadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverCellInfoDao
				.queryCellInfoByRoad(volteQualityBadRoadWeakCover));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doWeakCoverTianKuiAdjustAnalysis(com.datang.domain
	 * .VoLTEDissertation.qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover)
	 */
	@Override
	public EasyuiPageList doWeakCoverTianKuiAdjustAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		if (null == volteQualityBadRoadWeakCover
				|| null == volteQualityBadRoadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverTianKuiAdjustDao
				.queryCellInfoByRoad(volteQualityBadRoadWeakCover));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doWeakCoverTianKuiConnectReverseAnalysis(com.datang
	 * .domain.VoLTEDissertation
	 * .qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover)
	 */
	@Override
	public EasyuiPageList doWeakCoverTianKuiConnectReverseAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		if (null == volteQualityBadRoadWeakCover
				|| null == volteQualityBadRoadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverTianKuiConnectReverseDao
				.queryCellInfoByRoad(volteQualityBadRoadWeakCover));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doWeakCoverAdviceAddStationAnalysis(com.datang
	 * .domain.VoLTEDissertation.qualityBadRoad
	 * .weakCover.VolteQualityBadRoadWeakCover)
	 */
	@Override
	public EasyuiPageList doWeakCoverAdviceAddStationAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		if (null == volteQualityBadRoadWeakCover
				|| null == volteQualityBadRoadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverAdviceAddStationDao
				.queryCellInfoByRoad(volteQualityBadRoadWeakCover));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getDisturbRoadsByLogIds(java.util.List)
	 */
	@Override
	public List<VolteQualityBadRoadDisturbProblem> getDisturbRoadsByLogIds(
			List<Long> testLogItemIds) {
		return disturbDao.queryDisturbRoadsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getDisturbRoadById(java.lang.Long)
	 */
	@Override
	public VolteQualityBadRoadDisturbProblem getDisturbRoadById(Long roadId) {
		return disturbDao.find(roadId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doDisturbAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem)
	 */
	@Override
	public Map<String, EasyuiPageList> doDisturbAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("disturbRoadIndex", doDisturbIndexAnalysis(disturbRoadById));
		map.put("disturbRoadCellInfo",
				doDisturbCellInfoAnalysis(disturbRoadById));
		map.put("disturbRoadPCICellInfo",
				doDisturbPCIAdjustAnalysis(disturbRoadById));
		map.put("disturbRoadTiankuiAdjustCellInfo",
				doDisturbTianKuiAdjustAnalysis(disturbRoadById));
		map.put("disturbRoadSanChaoCellInfo",
				doDisturbSanChaoCellAnalysis(disturbRoadById));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doDisturbIndexAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem)
	 */
	@Override
	public EasyuiPageList doDisturbIndexAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById) {
			return new EasyuiPageList();
		}
		WeakCoverIndexResponseBean weakCoverIndexResponseBean = new WeakCoverIndexResponseBean();
		// 计算主被叫
		Integer callType = disturbRoadById.getTestLogItem().getCallType();
		if (null != callType) {
			switch (callType) {
			case 0:
				weakCoverIndexResponseBean.setCallType("主叫");
				break;
			case 1:
				weakCoverIndexResponseBean.setCallType("被叫");
				break;
			case 2:
				weakCoverIndexResponseBean.setCallType("都包含");
				break;
			default:
				break;
			}
		}
		// 计算MOS均值
		weakCoverIndexResponseBean.setMosAvg(disturbRoadById.getMosAvg());
		// 计算码字0:初始bler,残留bler @date 20160324 废弃
		// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
		// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
		float codon0BlerFirstRequestNum = NumberFormatUtils
				.isEmpty(disturbRoadById.getCodon0BlerFirstRequestNum());
		float codon0BlerFirstSuccessNum = NumberFormatUtils
				.isEmpty(disturbRoadById.getCodon0BlerFirstSuccessNum());
		float codon0BlerAgainSuccessNum = NumberFormatUtils
				.isEmpty(disturbRoadById.getCodon0BlerAgainSuccessNum());
		if (codon0BlerFirstRequestNum != 0.0f) {
			weakCoverIndexResponseBean
					.setCodon0FirstBler(NumberFormatUtils
							.format((codon0BlerFirstRequestNum - codon0BlerFirstSuccessNum)
									/ codon0BlerFirstRequestNum * 100, 2));
			weakCoverIndexResponseBean
					.setCodon0ResidualBler(NumberFormatUtils
							.format((codon0BlerFirstRequestNum - codon0BlerAgainSuccessNum)
									/ codon0BlerFirstRequestNum * 100, 2));
		}

		// 计算码字0:初始bler,残留bler @date 20160324 新增
		if (null != disturbRoadById.getCode0lBler()) {
			weakCoverIndexResponseBean.setCodon0FirstBler(disturbRoadById
					.getCode0lBler());
		}
		if (null != disturbRoadById.getCode0rBler()) {
			weakCoverIndexResponseBean.setCodon0ResidualBler(disturbRoadById
					.getCode0rBler());
		}

		// 计算码字1:初始bler,残留bler @date 20160324 废弃
		// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
		// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
		float codon1BlerFirstRequestNum = NumberFormatUtils
				.isEmpty(disturbRoadById.getCodon1BlerFirstRequestNum());
		float codon1BlerFirstSuccessNum = NumberFormatUtils
				.isEmpty(disturbRoadById.getCodon1BlerFirstSuccessNum());
		float codon1BlerAgainSuccessNum = NumberFormatUtils
				.isEmpty(disturbRoadById.getCodon1BlerAgainSuccessNum());
		if (codon1BlerFirstRequestNum != 0.0f) {
			weakCoverIndexResponseBean
					.setCodon1FirstBler(NumberFormatUtils
							.format((codon1BlerFirstRequestNum - codon1BlerFirstSuccessNum)
									/ codon1BlerFirstRequestNum * 100, 2));
			weakCoverIndexResponseBean
					.setCodon1ResidualBler(NumberFormatUtils
							.format((codon1BlerFirstRequestNum - codon1BlerAgainSuccessNum)
									/ codon1BlerFirstRequestNum * 100, 2));
		}

		// 计算码字1:初始bler,残留bler @date 20160324 新增
		if (null != disturbRoadById.getCode1lBler()) {
			weakCoverIndexResponseBean.setCodon1FirstBler(disturbRoadById
					.getCode1lBler());
		}
		if (null != disturbRoadById.getCode1rBler()) {
			weakCoverIndexResponseBean.setCodon1ResidualBler(disturbRoadById
					.getCode1rBler());
		}
		// 计算RTP丢包率:（发送的VoIP数据包数量—接收的数据包数量）/发送的VoIP数据包数量*100%
		Float m_dbRTPSendVoIPDataPackageNum = disturbRoadById
				.getM_dbRTPSendVoIPDataPackageNum();
		Float m_dbRTPReceiveDataPackageNum = NumberFormatUtils
				.isEmpty(disturbRoadById.getM_dbRTPReceiveDataPackageNum());
		// @date 20160324废弃
		if ((null != m_dbRTPSendVoIPDataPackageNum
				&& !m_dbRTPSendVoIPDataPackageNum.equals(0.0f) && null != m_dbRTPReceiveDataPackageNum)) {
			weakCoverIndexResponseBean
					.setRtpDropRatio(NumberFormatUtils
							.format((m_dbRTPSendVoIPDataPackageNum - m_dbRTPReceiveDataPackageNum)
									/ m_dbRTPSendVoIPDataPackageNum * 100, 2));
		}
		// @date 20160324更新
		if (0 != m_dbRTPReceiveDataPackageNum) {
			weakCoverIndexResponseBean.setRtpDropRatio(NumberFormatUtils
					.format(m_dbRTPSendVoIPDataPackageNum
							/ m_dbRTPReceiveDataPackageNum, 2));
		}
		// 计算RTP抖动:(RTP_SUM(浮点型):RTP取值之和)/(RTP_NUM(整型):RTP采样点数)
		Float m_dbRTPSum = disturbRoadById.getM_dbRTPSum();
		Long m_ulRTPNum = disturbRoadById.getM_ulRTPNum();
		if (null != m_ulRTPNum && !m_ulRTPNum.equals(0l) && null != m_dbRTPSum) {
			weakCoverIndexResponseBean.setRtpShake(NumberFormatUtils.format(
					m_dbRTPSum / m_ulRTPNum, 2));
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WeakCoverIndexResponseBean> rows = new ArrayList<>();
		rows.add(weakCoverIndexResponseBean);
		easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doDisturbCellInfoAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem)
	 */
	@Override
	public EasyuiPageList doDisturbCellInfoAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById || null == disturbRoadById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbCellInfoDao
				.queryCellInfoByRoad(disturbRoadById));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doDisturbPCIAdjustAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem)
	 */
	@Override
	public EasyuiPageList doDisturbPCIAdjustAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById || null == disturbRoadById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbPCIAdjustDao
				.queryCellInfoByRoad(disturbRoadById));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doDisturbTianKuiAdjustAnalysis(com.datang.domain
	 * .VoLTEDissertation.qualityBadRoad
	 * .disturbProblem.VolteQualityBadRoadDisturbProblem)
	 */
	@Override
	public EasyuiPageList doDisturbTianKuiAdjustAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById || null == disturbRoadById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbTianKuiAdjustDao
				.queryCellInfoByRoad(disturbRoadById));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doDisturbSanChaoCellAnalysis(com.datang.domain
	 * .VoLTEDissertation.qualityBadRoad
	 * .disturbProblem.VolteQualityBadRoadDisturbProblem)
	 */
	@Override
	public EasyuiPageList doDisturbSanChaoCellAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById || null == disturbRoadById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbSanChaoCellDao
				.queryCellInfoByRoad(disturbRoadById));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getNbDeficiencyRoadsByLogIds(java.util.List)
	 */
	@Override
	public List<VolteQualityBadRoadNbDeficiency> getNbDeficiencyRoadsByLogIds(
			List<Long> testLogItemIds) {
		return nbCellDeficiencyDao
				.queryNbDeficiencyRoadsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getNbDeficiencyRoadById(java.lang.Long)
	 */
	@Override
	public VolteQualityBadRoadNbDeficiency getNbDeficiencyRoadById(Long roadId) {
		return nbCellDeficiencyDao.find(roadId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doNbDeficiencyAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.nbDeficiency.VolteQualityBadRoadNbDeficiency)
	 */
	@Override
	public Map<String, EasyuiPageList> doNbDeficiencyAnalysis(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("nbCellLTEAddAdvice",
				doNbDeficiencyLTEAddAdviceAnalysis(volteQualityBadRoadNbDeficiency));
		map.put("nbCellGSMAddAdvice",
				doNbDeficiencyGSMAddAdviceAnalysis(volteQualityBadRoadNbDeficiency));
		map.put("nbCellCoPerf",
				doNbDeficiencyCoPerfAnalysis(volteQualityBadRoadNbDeficiency));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doNbDeficiencyLTEAddAdviceAnalysis(com.datang.
	 * domain.VoLTEDissertation.qualityBadRoad
	 * .nbDeficiency.VolteQualityBadRoadNbDeficiency)
	 */
	@Override
	public EasyuiPageList doNbDeficiencyLTEAddAdviceAnalysis(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency) {
		if (null == volteQualityBadRoadNbDeficiency
				|| null == volteQualityBadRoadNbDeficiency.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(nbCellDeficiencyLTEAddAdviceDao
				.queryCellInfoByRoad(volteQualityBadRoadNbDeficiency));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doNbDeficiencyGSMAddAdviceAnalysis(com.datang.
	 * domain.VoLTEDissertation.qualityBadRoad
	 * .nbDeficiency.VolteQualityBadRoadNbDeficiency)
	 */
	@Override
	public EasyuiPageList doNbDeficiencyGSMAddAdviceAnalysis(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency) {
		if (null == volteQualityBadRoadNbDeficiency
				|| null == volteQualityBadRoadNbDeficiency.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(nbCellDeficiencyGSMAddAdviceDao
				.queryCellInfoByRoad(volteQualityBadRoadNbDeficiency));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doNbDeficiencyCoPerfAnalysis(com.datang.domain
	 * .VoLTEDissertation.qualityBadRoad
	 * .nbDeficiency.VolteQualityBadRoadNbDeficiency)
	 */
	@Override
	public EasyuiPageList doNbDeficiencyCoPerfAnalysis(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency) {
		if (null == volteQualityBadRoadNbDeficiency
				|| null == volteQualityBadRoadNbDeficiency.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(nbCellDeficiencyCoPerfDao
				.queryCellInfoByRoad(volteQualityBadRoadNbDeficiency));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getParamErrorRoadsByLogIds(java.util.List)
	 */
	@Override
	public List<VolteQualityBadRoadParamError> getParamErrorRoadsByLogIds(
			List<Long> testLogItemIds) {
		return paramErrorDao.queryParamErrorRoadsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getParamErrorRoadById(java.lang.Long)
	 */
	@Override
	public VolteQualityBadRoadParamError getParamErrorRoadById(Long roadId) {
		return paramErrorDao.find(roadId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doParamErrorAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.paramError.VolteQualityBadRoadParamError)
	 */
	@Override
	public Map<String, EasyuiPageList> doParamErrorAnalysis(
			VolteQualityBadRoadParamError volteQualityBadRoadParamError) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("paramErrorOptimizeAdvice",
				doParamErrorOptimizeAdviceAnalysis(volteQualityBadRoadParamError));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doParamErrorOptimizeAdviceAnalysis(com.datang.
	 * domain.VoLTEDissertation.qualityBadRoad
	 * .paramError.VolteQualityBadRoadParamError)
	 */
	@Override
	public EasyuiPageList doParamErrorOptimizeAdviceAnalysis(
			VolteQualityBadRoadParamError volteQualityBadRoadParamError) {
		if (null == volteQualityBadRoadParamError
				|| null == volteQualityBadRoadParamError.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(paramErrorOptimizeAdviceDao
				.queryCellInfoByRoad(volteQualityBadRoadParamError));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getCoreNetworkRoadsByLogIds(java.util.List)
	 */
	@Override
	public List<VolteQualityBadRoadCoreNetwork> getCoreNetworkRoadsByLogIds(
			List<Long> testLogItemIds) {
		return coreNetworkDao.queryCoreNetworkRoadsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doCoreNetworkAnalysis(com.datang.domain.chart.OneDimensionalChartConfig,
	 * java.util.List)
	 */
	@Override
	public Map<String, List> doCoreNetworkAnalysis(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> testLogItemIds) {
		Map<String, List> map = new HashMap<>();
		map.put("rtpPacketLostRatioHourChart",
				doCoreNetworkRtpPacketLostRatioHourChartAnalysis(
						rtpPacketLostRatioHourChart, testLogItemIds));
		map.put("rtpPacketLostAndSinrChart",
				doCoreNetworkRtpPacketLostAndSinrChartAnalysis(testLogItemIds));
		map.put("rtpPacketLostAndRsrpChart",
				doCoreNetworkRtpPacketLostAndRsrpChartAnalysis(testLogItemIds));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doCoreNetworkRtpPacketLostRatioHourChartAnalysis
	 * (com.datang.domain.chart.OneDimensionalChartConfig, java.util.List)
	 */
	@Override
	public List<Object> doCoreNetworkRtpPacketLostRatioHourChartAnalysis(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> testLogItemIds) {
		if (null == testLogItemIds
				|| 0 == testLogItemIds.size()
				|| null == rtpPacketLostRatioHourChart
				|| !StringUtils.hasText(rtpPacketLostRatioHourChart
						.getAxisCustomer())) {
			return new ArrayList<>();
		}
		List<Long> queryCoreNetworkRoadIdsByLogIds = coreNetworkDao
				.queryCoreNetworkRoadIdsByLogIds(testLogItemIds);
		if (null != queryCoreNetworkRoadIdsByLogIds
				&& 0 != queryCoreNetworkRoadIdsByLogIds.size()) {
			List<Object> queryRtpPacketDropRatioHour = coreNetworkDao
					.queryOneDimensionalChartValues(
							rtpPacketLostRatioHourChart,
							queryCoreNetworkRoadIdsByLogIds);
			return queryRtpPacketDropRatioHour;
		}
		return new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doCoreNetworkRtpPacketLostAndSinrChartAnalysis(java.util.List)
	 */
	@Override
	public List<Object> doCoreNetworkRtpPacketLostAndSinrChartAnalysis(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		List<Long> queryCoreNetworkRoadIdsByLogIds = coreNetworkDao
				.queryCoreNetworkRoadIdsByLogIds(testLogItemIds);

		if (null != queryCoreNetworkRoadIdsByLogIds
				&& 0 != queryCoreNetworkRoadIdsByLogIds.size()) {
			List<TwoDimensionalChartValues> queryTwoDimenSionalChartValues = coreNetworkDao
					.queryTwoDimenSionalChartValues(
							TwoDimensionalChartType.RtpPacketLostRatioSinr,
							queryCoreNetworkRoadIdsByLogIds);
			if (null != queryTwoDimenSionalChartValues
					&& 0 != queryTwoDimenSionalChartValues.size()) {
				Long totalValue = null;
				for (TwoDimensionalChartValues twoDimensionalChartValues : queryTwoDimenSionalChartValues) {
					String xAxis = twoDimensionalChartValues.getxAxis();
					String yAxis = twoDimensionalChartValues.getyAxis();
					Long value = twoDimensionalChartValues.getValue();
					if (!StringUtils.hasText(xAxis)
							|| !StringUtils.hasText(yAxis) || null == value) {
						continue;
					}
					totalValue = totalValue == null ? value : value
							+ totalValue;
				}
				if (null != totalValue && !totalValue.equals(0l)) {
					List<Object> list = new ArrayList<Object>();
					for (TwoDimensionalChartValues twoDimensionalChartValues : queryTwoDimenSionalChartValues) {
						List<Object> vlues = new LinkedList<>();
						String xAxis = twoDimensionalChartValues.getxAxis();
						String yAxis = twoDimensionalChartValues.getyAxis();
						Long value = twoDimensionalChartValues.getValue();
						if (!StringUtils.hasText(xAxis)
								|| !StringUtils.hasText(yAxis) || null == value) {
							continue;
						}
						vlues.add("'" + xAxis + "'");
						vlues.add("'" + yAxis + "'");
						vlues.add(NumberFormatUtils.format((float) value
								/ totalValue * 100, 2));
						list.add(vlues);
					}
					return list;
				}
			}
		}
		return new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doCoreNetworkRtpPacketLostAndRsrpChartAnalysis(java.util.List)
	 */
	@Override
	public List<Object> doCoreNetworkRtpPacketLostAndRsrpChartAnalysis(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		List<Long> queryCoreNetworkRoadIdsByLogIds = coreNetworkDao
				.queryCoreNetworkRoadIdsByLogIds(testLogItemIds);

		if (null != queryCoreNetworkRoadIdsByLogIds
				&& 0 != queryCoreNetworkRoadIdsByLogIds.size()) {
			List<TwoDimensionalChartValues> queryTwoDimenSionalChartValues = coreNetworkDao
					.queryTwoDimenSionalChartValues(
							TwoDimensionalChartType.RtpPacketLostRatioRsrp,
							queryCoreNetworkRoadIdsByLogIds);
			if (null != queryTwoDimenSionalChartValues
					&& 0 != queryTwoDimenSionalChartValues.size()) {
				Long totalValue = null;
				for (TwoDimensionalChartValues twoDimensionalChartValues : queryTwoDimenSionalChartValues) {
					String xAxis = twoDimensionalChartValues.getxAxis();
					String yAxis = twoDimensionalChartValues.getyAxis();
					Long value = twoDimensionalChartValues.getValue();
					if (!StringUtils.hasText(xAxis)
							|| !StringUtils.hasText(yAxis) || null == value) {
						continue;
					}
					totalValue = totalValue == null ? value : value
							+ totalValue;
				}
				if (null != totalValue && !totalValue.equals(0l)) {
					List<Object> list = new ArrayList<Object>();
					for (TwoDimensionalChartValues twoDimensionalChartValues : queryTwoDimenSionalChartValues) {
						List<Object> vlues = new LinkedList<>();
						String xAxis = twoDimensionalChartValues.getxAxis();
						String yAxis = twoDimensionalChartValues.getyAxis();
						Long value = twoDimensionalChartValues.getValue();
						if (!StringUtils.hasText(xAxis)
								|| !StringUtils.hasText(yAxis) || null == value) {
							continue;
						}
						vlues.add("'" + xAxis + "'");
						vlues.add("'" + yAxis + "'");
						vlues.add(NumberFormatUtils.format((float) value
								/ totalValue * 100, 2));
						list.add(vlues);
					}
					return list;
				}
			}
		}
		return new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getOtherRoadsByLogIds(java.util.List)
	 */
	@Override
	public List<VolteQualityBadRoadOther> getOtherRoadsByLogIds(
			List<Long> testLogItemIds) {
		return otherDao.queryOtherRoadsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getOtherRoadById(java.lang.Long)
	 */
	@Override
	public VolteQualityBadRoadOther getOtherRoadById(Long roadId) {
		return otherDao.find(roadId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doOtherAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.otherProblem.VolteQualityBadRoadOther)
	 */
	@Override
	public Map<String, EasyuiPageList> doOtherAnalysis(
			VolteQualityBadRoadOther volteQualityBadRoadOther) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("otherRoadIndex",
				doOtherIndexAnalysis(volteQualityBadRoadOther));
		map.put("otherRoadCellInfo",
				doOtherCellInfoAnalysis(volteQualityBadRoadOther));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doOtherIndexAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.otherProblem.VolteQualityBadRoadOther)
	 */
	@Override
	public EasyuiPageList doOtherIndexAnalysis(
			VolteQualityBadRoadOther volteQualityBadRoadOther) {
		if (null == volteQualityBadRoadOther) {
			return new EasyuiPageList();
		}
		OtherIndexResponseBean otherIndexResponseBean = new OtherIndexResponseBean();
		otherIndexResponseBean.setMosAvg(volteQualityBadRoadOther.getMosAvg());
		otherIndexResponseBean.setDownMSCAvg(volteQualityBadRoadOther
				.getDownMSCAvg());
		otherIndexResponseBean.setDownPDCPSpeed(volteQualityBadRoadOther
				.getDownPDCPSpeed());
		otherIndexResponseBean.setDownPHYSpeed(volteQualityBadRoadOther
				.getDownPHYSpeed());
		otherIndexResponseBean.setDownRBNum(volteQualityBadRoadOther
				.getDownRBNum());
		otherIndexResponseBean.setUpMSCAvg(volteQualityBadRoadOther
				.getUpMSCAvg());
		otherIndexResponseBean.setUpPDCPSpeed(volteQualityBadRoadOther
				.getUpPDCPSpeed());
		otherIndexResponseBean.setUpPHYSpeed(volteQualityBadRoadOther
				.getUpPHYSpeed());
		otherIndexResponseBean
				.setUpRBNum(volteQualityBadRoadOther.getUpRBNum());
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<OtherIndexResponseBean> rows = new ArrayList<>();
		rows.add(otherIndexResponseBean);
		easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doOtherCellInfoAnalysis(com.datang.domain.VoLTEDissertation
	 * .qualityBadRoad.otherProblem.VolteQualityBadRoadOther)
	 */
	@Override
	public EasyuiPageList doOtherCellInfoAnalysis(
			VolteQualityBadRoadOther volteQualityBadRoadOther) {
		if (null == volteQualityBadRoadOther
				|| null == volteQualityBadRoadOther.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(otherCellInfoDao
				.queryCellInfoByRoad(volteQualityBadRoadOther));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doOtherRoadSignllingPageList(com.datang.common.action.page.PageList)
	 */
	@Override
	public AbstractPageList doOtherRoadSignllingPageList(PageList pageList) {
		return testLogItemSignallingDao.getPageSignalling(pageList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#getNullRoadNameQBR()
	 */
	@Override
	public List<VolteQualityBadRoad> getNullRoadNameQBR() {
		return qualityBadRoadDao.queryNullRoadName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#addQBRRoadName(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public void addQBRRoadName(String roadName, Long qbrId) {
		VolteQualityBadRoad find = qualityBadRoadDao.find(qbrId);
		if (null != find && !StringUtils.hasText(find.getM_stRoadName())) {
			qualityBadRoadDao.addQBRRoadName(roadName, qbrId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService#doMosBadAnalysis(java.util.List,
	 * java.util.List)
	 */
	@Override
	public Map<String, EasyuiPageList> doMosBadAnalysis(List<Long> ids,
			List<Long> compareIds) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("mosBadIndexTable0", new EasyuiPageList());
		map.put("mosBadIndexTable1", new EasyuiPageList());
		map.put("mosBadRoadTable", new EasyuiPageList());
		map.put("mosBadIndexTable2", new EasyuiPageList());
		map.put("mosBadIndexTable3", new EasyuiPageList());
		map.put("mosBadIndexTable4", new EasyuiPageList());
		if (null == ids || 0 == ids.size() || null == compareIds
				|| 0 == compareIds.size()) {
			return map;
		}
		List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds = getVolteQualityBadRoadsByLogIds(ids);
		List<VolteQualityBadRoad> compareVolteQualityBadRoadsByLogIds = getVolteQualityBadRoadsByLogIds(compareIds);
		if (null == volteQualityBadRoadsByLogIds
				|| 0 == volteQualityBadRoadsByLogIds.size()
				|| null == compareVolteQualityBadRoadsByLogIds
				|| 0 == compareVolteQualityBadRoadsByLogIds.size()) {
			return map;
		}
		// 存储计算后的MOS差黑点原始质差路段和对比质差路段用于计算汇总指标
		Set<VolteQualityBadRoad> mosBadRoadSet = new HashSet<>();
		Set<VolteQualityBadRoad> compareMosBadRoadSet = new HashSet<>();
		// 存储计算后的构成MOS差黑点原始和对比路段的里程总和和数量总和,按路段类型分
		Map<String, Float> distanceSum = new HashMap<>();
		Map<String, Float> compareDistanceSum = new HashMap<>();
		Map<String, Integer> numSum = new HashMap<>();
		// 存储汇总后的MOS差黑点路段对
		List<MosBadRoadResponseBean> mosBadRoadList = new ArrayList<>();

		// 将质差路段分类
		Map<String, List<VolteQualityBadRoad>> volteQualityBadRoadClassify = volteQualityBadRoadClassify(volteQualityBadRoadsByLogIds);
		Map<String, List<VolteQualityBadRoad>> compareVolteQualityBadRoadClassify = volteQualityBadRoadClassify(compareVolteQualityBadRoadsByLogIds);
		// 将分类好的质差路段计算MOS差黑点,并汇总
		classify: for (Entry<String, List<VolteQualityBadRoad>> entry : volteQualityBadRoadClassify
				.entrySet()) {
			String qbrType = entry.getKey();
			List<VolteQualityBadRoad> volteQualityBadRoads = entry.getValue();
			List<VolteQualityBadRoad> compareVolteQualityBadRoads = compareVolteQualityBadRoadClassify
					.get(qbrType);
			if (0 == volteQualityBadRoads.size()
					|| 0 == compareVolteQualityBadRoads.size()) {
				continue classify;
			}

			// 将分类好的质差路段计算MOS差黑点
			Map<VolteQualityBadRoad, List<VolteQualityBadRoad>> unMergeMosBad = analysisMosBadWithBadRoadAndCompareBadRoad0(
					volteQualityBadRoads, compareVolteQualityBadRoads);
			// 汇总MOS差黑点路段
			outer: for (Entry<VolteQualityBadRoad, List<VolteQualityBadRoad>> mosBadMapEntry : unMergeMosBad
					.entrySet()) {
				VolteQualityBadRoad mosBadRoad = mosBadMapEntry.getKey();
				List<VolteQualityBadRoad> compareMosBadRoads = mosBadMapEntry
						.getValue();
				if (null == compareMosBadRoads
						|| 0 == compareMosBadRoads.size()) {
					// 该原始质差路段不构成MOS差黑点
					continue outer;
				}

				// 该原始质差路段与对比质差路段构成MOS差黑点,汇总,并存储构成MOS差黑点的原始质差路段和对比质差路段用于计算汇总指标
				boolean add2 = mosBadRoadSet.add(mosBadRoad);
				if (add2) {
					if (null != mosBadRoad.getM_dbDistance()) {
						distanceSum.put(
								qbrType,
								null == distanceSum.get(qbrType) ? mosBadRoad
										.getM_dbDistance() : distanceSum
										.get(qbrType)
										+ mosBadRoad.getM_dbDistance());
					}
					numSum.put(qbrType, null == numSum.get(qbrType) ? 1
							: numSum.get(qbrType) + 1);
				}
				StringBuffer idsBuffer = new StringBuffer();
				// 汇总取里程最大的对比质差路段
				VolteQualityBadRoad maxDistanceVolteQualityBadRoad = null;
				inner: for (VolteQualityBadRoad volteQualityBadRoad : compareMosBadRoads) {
					boolean add = compareMosBadRoadSet.add(volteQualityBadRoad);
					if (add) {
						if (null != volteQualityBadRoad.getM_dbDistance()) {
							compareDistanceSum
									.put(qbrType,
											null == compareDistanceSum
													.get(qbrType) ? volteQualityBadRoad
													.getM_dbDistance()
													: compareDistanceSum
															.get(qbrType)
															+ volteQualityBadRoad
																	.getM_dbDistance());
						}
						numSum.put(qbrType, numSum.get(qbrType) + 1);
					}
					Float m_dbDistance = volteQualityBadRoad.getM_dbDistance();
					idsBuffer.append(volteQualityBadRoad.getId() + ",");
					if (null == maxDistanceVolteQualityBadRoad
							|| null == maxDistanceVolteQualityBadRoad
									.getM_dbDistance()) {
						maxDistanceVolteQualityBadRoad = volteQualityBadRoad;
					} else {
						if (null != m_dbDistance) {
							if (m_dbDistance
									- maxDistanceVolteQualityBadRoad
											.getM_dbDistance() > 0) {
								maxDistanceVolteQualityBadRoad = volteQualityBadRoad;
							}
						}
					}
				}
				// 存储汇总结果
				idsBuffer.append(mosBadRoad.getId());
				MosBadRoadResponseBean mosBadRoadResponseBean = new MosBadRoadResponseBean();
				mosBadRoadResponseBean.setRecSeqNo(mosBadRoad.getTestLogItem()
						.getRecSeqNo());
				mosBadRoadResponseBean.setFileName(mosBadRoad.getTestLogItem()
						.getFileName());
				mosBadRoadResponseBean.setBoxId(mosBadRoad.getTestLogItem()
						.getBoxId());
				mosBadRoadResponseBean.setId(mosBadRoad.getId());
				mosBadRoadResponseBean.setM_stRoadName(mosBadRoad
						.getM_stRoadName());
				mosBadRoadResponseBean.setBeginLatitude(mosBadRoad
						.getBeginLatitude());
				mosBadRoadResponseBean.setCourseLatitude(mosBadRoad
						.getCourseLatitude());
				mosBadRoadResponseBean.setEndLatitude(mosBadRoad
						.getEndLatitude());
				mosBadRoadResponseBean.setBeginLongitude(mosBadRoad
						.getBeginLongitude());
				mosBadRoadResponseBean.setCourseLongitude(mosBadRoad
						.getCourseLongitude());
				mosBadRoadResponseBean.setEndLongitude(mosBadRoad
						.getEndLongitude());
				mosBadRoadResponseBean.setM_dbDistance(mosBadRoad
						.getM_dbDistance());
				mosBadRoadResponseBean.setM_dbContinueTime(mosBadRoad
						.getM_dbContinueTime());
				mosBadRoadResponseBean
						.setCompareRecSeqNo(maxDistanceVolteQualityBadRoad
								.getTestLogItem().getRecSeqNo());
				mosBadRoadResponseBean
						.setCompareFileName(maxDistanceVolteQualityBadRoad
								.getTestLogItem().getFileName());
				mosBadRoadResponseBean
						.setCompareBoxId(maxDistanceVolteQualityBadRoad
								.getTestLogItem().getBoxId());
				mosBadRoadResponseBean
						.setCompareId(maxDistanceVolteQualityBadRoad.getId());
				mosBadRoadResponseBean
						.setCompareM_stRoadName(maxDistanceVolteQualityBadRoad
								.getM_stRoadName());
				mosBadRoadResponseBean
						.setCompareBeginLatitude(maxDistanceVolteQualityBadRoad
								.getBeginLatitude());
				mosBadRoadResponseBean
						.setCompareCourseLatitude(maxDistanceVolteQualityBadRoad
								.getCourseLatitude());
				mosBadRoadResponseBean
						.setCompareEndLatitude(maxDistanceVolteQualityBadRoad
								.getEndLatitude());
				mosBadRoadResponseBean
						.setCompareBeginLongitude(maxDistanceVolteQualityBadRoad
								.getBeginLongitude());
				mosBadRoadResponseBean
						.setCompareCourseLongitude(maxDistanceVolteQualityBadRoad
								.getCourseLongitude());
				mosBadRoadResponseBean
						.setCompareEndLongitude(maxDistanceVolteQualityBadRoad
								.getEndLongitude());
				mosBadRoadResponseBean
						.setCompareM_dbDistance(maxDistanceVolteQualityBadRoad
								.getM_dbDistance());
				mosBadRoadResponseBean
						.setCompareM_dbContinueTime(maxDistanceVolteQualityBadRoad
								.getM_dbContinueTime());
				mosBadRoadResponseBean.setCompareIds(idsBuffer.toString());
				mosBadRoadResponseBean.setMosBadLatitude(mosBadRoad
						.getMosBadLatitude());
				mosBadRoadResponseBean.setMosBadLongitude(mosBadRoad
						.getMosBadLongitude());
				if (qbrType.equals("WeakCover")) {
					mosBadRoadResponseBean.setQbrType(0);
				} else if (qbrType.equals("Disturb")) {
					mosBadRoadResponseBean.setQbrType(1);
				} else if (qbrType.equals("NbCell")) {
					mosBadRoadResponseBean.setQbrType(2);
				} else if (qbrType.equals("ParamError")) {
					mosBadRoadResponseBean.setQbrType(3);
				} else if (qbrType.equals("Other")) {
					mosBadRoadResponseBean.setQbrType(4);
				}
				mosBadRoadList.add(mosBadRoadResponseBean);
			}
		}
		// 计算汇总后的MOS差黑点路段列表
		EasyuiPageList mosRoadTableEasyuiPageList = new EasyuiPageList();
		mosRoadTableEasyuiPageList.setRows(mosBadRoadList);
		map.put("mosBadRoadTable", mosRoadTableEasyuiPageList);

		// 计算mos差黑点路段的整体指标呈现
		EasyuiPageList doWholeIndex00Analysis = doWholeIndex00Analysis(new ArrayList<VolteQualityBadRoad>(
				mosBadRoadSet));
		EasyuiPageList doWholeIndex00Analysis2 = doWholeIndex00Analysis(new ArrayList<VolteQualityBadRoad>(
				compareMosBadRoadSet));
		List<VolteQualityBadRoad> allMosBadRoads = new ArrayList<VolteQualityBadRoad>(
				mosBadRoadSet);
		allMosBadRoads.addAll(compareMosBadRoadSet);
		EasyuiPageList doWholeIndex00Analysis3 = doWholeIndex00Analysis(allMosBadRoads);
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		if (0 != doWholeIndex00Analysis.getRows().size()) {
			WholeIndexResponseBean wholeIndexResponseBean = (WholeIndexResponseBean) (doWholeIndex00Analysis
					.getRows().get(0));
			wholeIndexResponseBean.setTestLogType(0);
			easyuiPageList.getRows().add(wholeIndexResponseBean);
		}
		if (0 != doWholeIndex00Analysis2.getRows().size()) {
			WholeIndexResponseBean wholeIndexResponseBean = (WholeIndexResponseBean) (doWholeIndex00Analysis2
					.getRows().get(0));
			wholeIndexResponseBean.setTestLogType(1);
			easyuiPageList.getRows().add(wholeIndexResponseBean);
		}
		if (0 != doWholeIndex00Analysis3.getRows().size()) {
			WholeIndexResponseBean wholeIndexResponseBean = (WholeIndexResponseBean) (doWholeIndex00Analysis3
					.getRows().get(0));
			wholeIndexResponseBean.setTestLogType(2);
			easyuiPageList.getRows().add(wholeIndexResponseBean);
		}
		map.put("mosBadIndexTable0", easyuiPageList);

		// 计算MOS黑点路段统计
		ArrayList<Long> allIds = new ArrayList<Long>(ids);
		allIds.addAll(compareIds);
		List<TestLogItem> queryTestLogItems = testLogItemService
				.queryTestLogItems(allIds);
		map.put("mosBadIndexTable1",
				doWholeIndex1Analysis(allMosBadRoads, queryTestLogItems));

		// 计算MOS差黑点路段数量
		EasyuiPageList numSumEasyuiPageList = new EasyuiPageList();
		List rows0 = numSumEasyuiPageList.getRows();
		rows0.add(numSum);
		map.put("mosBadIndexTable2", numSumEasyuiPageList);

		// 计算MOS差黑点路段里程,原始日志
		EasyuiPageList distanceSumEasyuiPageList = new EasyuiPageList();
		List rows1 = distanceSumEasyuiPageList.getRows();
		rows1.add(distanceSum);
		map.put("mosBadIndexTable3", distanceSumEasyuiPageList);

		// 计算MOS差黑点路段里程,对比日志
		EasyuiPageList compareDistanceSumEasyuiPageList = new EasyuiPageList();
		List rows2 = compareDistanceSumEasyuiPageList.getRows();
		rows2.add(compareDistanceSum);
		map.put("mosBadIndexTable4", compareDistanceSumEasyuiPageList);
		return map;
	}

	/**
	 * 质差路段分类
	 * 
	 * @param qbrs
	 * @return
	 */
	private Map<String, List<VolteQualityBadRoad>> volteQualityBadRoadClassify(
			List<VolteQualityBadRoad> qbrs) {
		Map<String, List<VolteQualityBadRoad>> map = new HashMap<>();
		map.put("WeakCover", new ArrayList<VolteQualityBadRoad>());
		map.put("Disturb", new ArrayList<VolteQualityBadRoad>());
		map.put("NbCell", new ArrayList<VolteQualityBadRoad>());
		map.put("ParamError", new ArrayList<VolteQualityBadRoad>());
		map.put("Other", new ArrayList<VolteQualityBadRoad>());
		if (null != qbrs && 0 != qbrs.size()) {
			for (VolteQualityBadRoad volteQualityBadRoad : qbrs) {
				if (volteQualityBadRoad instanceof VolteQualityBadRoadWeakCover) {
					map.get("WeakCover").add(volteQualityBadRoad);
				} else if (volteQualityBadRoad instanceof VolteQualityBadRoadDisturbProblem) {
					map.get("Disturb").add(volteQualityBadRoad);
				} else if (volteQualityBadRoad instanceof VolteQualityBadRoadNbDeficiency) {
					map.get("NbCell").add(volteQualityBadRoad);
				} else if (volteQualityBadRoad instanceof VolteQualityBadRoadParamError) {
					map.get("ParamError").add(volteQualityBadRoad);
				} else if (volteQualityBadRoad instanceof VolteQualityBadRoadOther) {
					map.get("Other").add(volteQualityBadRoad);
				}
			}
		}
		return map;
	}

	/**
	 * 获取原始日志的质差路段和对比日志的质差路段构成的MOS差黑点,结果集未合并,返回结果集为"一条原始日志中的质差路段对应对比日志中的多条质差路段"
	 * 构成的集合
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @param compareVolteQualityBadRoadsByLogIds
	 * @return
	 */
	private Map<VolteQualityBadRoad, List<VolteQualityBadRoad>> analysisMosBadWithBadRoadAndCompareBadRoad0(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds,
			List<VolteQualityBadRoad> compareVolteQualityBadRoadsByLogIds) {
		Map<VolteQualityBadRoad, List<VolteQualityBadRoad>> resultMap = new HashMap<>();
		try {
			Float mosBadRoadDistanceFloat = Float.valueOf(mosBadRoadDistance);
			Float mosBadRoadSuperpositionRateFloat = Float
					.valueOf(mosBadRoadSuperpositionRate);
			// 存放对比日志的所有质差路段的MOS轨迹点信息
			Map<Long, List<TestLogItemIndexGpsPoint>> compareTestLogItemIndexGpsPointMap = new HashMap<>();

			outer: for (VolteQualityBadRoad volteQualityBadRoad : volteQualityBadRoadsByLogIds) {
				// 开始结束时间或者所属日志任意一个为空直接跳过
				Long startTime = volteQualityBadRoad.getStartTime();
				Long endTime = volteQualityBadRoad.getEndTime();
				TestLogItem testLogItem = volteQualityBadRoad.getTestLogItem();
				if (null == startTime || null == endTime || null == testLogItem) {
					continue outer;
				}
				// 原始日志质差路段MOS指标轨迹点为空直接跳过
				List<TestLogItemIndexGpsPoint> mosPoints = qbrGpsPointService
						.getPointsByQBRIdAndIndexType(
								volteQualityBadRoad.getId(), 0);
				if (0 == mosPoints.size()) {
					continue outer;
				}
				// 初始化结果集
				resultMap.put(volteQualityBadRoad,
						new ArrayList<VolteQualityBadRoad>());
				// 存放结果集中满足MOS差黑点的轨迹点
				Set<TestLogItemIndexGpsPoint> allMosBadGpsPoints = new HashSet<>();

				inner: for (VolteQualityBadRoad compareVolteQualityBadRoad : compareVolteQualityBadRoadsByLogIds) {
					// 开始结束时间或者所属日志任意一个为空直接跳过
					Long compareStartTime = compareVolteQualityBadRoad
							.getStartTime();
					Long compareEndTime = compareVolteQualityBadRoad
							.getEndTime();
					TestLogItem compareTestLogItem = compareVolteQualityBadRoad
							.getTestLogItem();
					if (null == compareStartTime || null == compareEndTime
							|| null == compareTestLogItem) {
						continue inner;
					}

					// 对比日志质差路段MOS指标轨迹点为空直接跳过
					List<TestLogItemIndexGpsPoint> compareMosPoints = compareTestLogItemIndexGpsPointMap
							.get(compareVolteQualityBadRoad.getId());
					if (null == compareMosPoints) {
						compareMosPoints = qbrGpsPointService
								.getPointsByQBRIdAndIndexType(
										compareVolteQualityBadRoad.getId(), 0);
						compareTestLogItemIndexGpsPointMap.put(
								compareVolteQualityBadRoad.getId(),
								compareMosPoints);
					}
					if (0 == compareMosPoints.size()) {
						continue inner;
					}

					// 原始质差路段轨迹点和对比质差路段轨迹点中个数较小的个数
					int minGpsSize = ((compareMosPoints.size() - mosPoints
							.size()) > 0 ? mosPoints.size() : compareMosPoints
							.size());
					// 存放满足MOS差黑点的轨迹点
					Set<TestLogItemIndexGpsPoint> mosBadGpsPoints = new HashSet<>();
					// 计算MOS差黑点
					pointOuter: for (TestLogItemIndexGpsPoint gpsPoint : mosPoints) {
						// 原始质差路段的轨迹点为空直接跳过
						Double latitude = gpsPoint.getLatitude();
						Double longitude = gpsPoint.getLongitude();
						if (null == latitude || 0 == latitude
								|| null == longitude || 0 == longitude) {
							continue pointOuter;
						}
						pointInner: for (TestLogItemIndexGpsPoint compareGpsPoint : compareMosPoints) {
							// 对比质差路段的轨迹点为空直接跳过
							Double compareLatitude = compareGpsPoint
									.getLatitude();
							Double compareLongitude = compareGpsPoint
									.getLongitude();
							if (null == compareLatitude || 0 == compareLatitude
									|| null == compareLongitude
									|| 0 == compareLongitude) {
								continue pointInner;
							}
							// 距离小于配置值,则认为这两个MOS采样点重合
							double distance = GPSUtils.distance(latitude,
									longitude, compareLatitude,
									compareLongitude);
							if (distance <= mosBadRoadDistanceFloat) {
								mosBadGpsPoints.add(gpsPoint);
								mosBadGpsPoints.add(compareGpsPoint);
							}
						}
					}
					// 判定原始质差路段和对比质差路段时候符合MOS差黑点路段,即是否满足((MOS差黑点重合的采样点个数/(原始质差路段轨迹点和对比质差路段轨迹点中个数较小的个数))>=配置重合率)
					if (mosBadRoadSuperpositionRateFloat <= (float) ((float) mosBadGpsPoints
							.size() * 100 / (float) minGpsSize)) {
						// 设置结果集
						resultMap.get(volteQualityBadRoad).add(
								compareVolteQualityBadRoad);
						allMosBadGpsPoints.addAll(mosBadGpsPoints);
					}
				}
				// 计算mos差黑点中心点
				float allLat = 0.0F;
				float allLon = 0.0F;
				int latLonNum = 0;
				totalLatLon: for (TestLogItemIndexGpsPoint testLogItemIndexGpsPoint : allMosBadGpsPoints) {
					if (null == testLogItemIndexGpsPoint.getLatitude()
							|| 0 == testLogItemIndexGpsPoint.getLatitude()
							|| null == testLogItemIndexGpsPoint.getLongitude()
							|| 0 == testLogItemIndexGpsPoint.getLongitude()) {
						continue totalLatLon;
					}
					allLat += testLogItemIndexGpsPoint.getLatitude();
					allLon += testLogItemIndexGpsPoint.getLongitude();
					latLonNum++;
				}
				if (0 != latLonNum) {
					volteQualityBadRoad.setMosBadLongitude(allLon / latLonNum);
					volteQualityBadRoad.setMosBadLatitude(allLat / latLonNum);
				}
			}
		} catch (NumberFormatException e) {
			// TODO 配置文件出错
		} catch (NullPointerException e) {
			// TODO 配置文件为空
		}
		return resultMap;
	}
}
