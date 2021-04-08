package com.datang.service.VoLTEDissertation.continueWirelessBadRoad.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.DisturbCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.DisturbCellInfoCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.DisturbPCIAdjustCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.DisturbSanChaoCellCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.DisturbTianKuiAdjustCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.NbCellDeficiencyCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.NbCellDeficiencyCoPerfCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.NbCellDeficiencyGSMAddAdviceCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.NbCellDeficiencyLTEAddAdviceCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.OtherCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.OtherCellInfoCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoadCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.WeakCoverAdviceAddStationCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.WeakCoverCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.WeakCoverCellInfoCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.WeakCoverTianKuiAdjustCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.WeakCoverTianKuiConnectReverseCWBRDao;
import com.datang.dao.platform.analysisThreshold.VolteAnalysisAboutThresholdDao;
import com.datang.dao.testLogItem.TestLogItemDao;
import com.datang.dao.testLogItem.TestLogItemSignallingDao;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoad;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.VolteContinueWirelessBadRoadDisturbProblem;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.VolteContinueWirelessBadRoadNbDeficiency;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.otherProblem.VolteContinueWirelessBadRoadOtherProblem;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.weakCover.VolteContinueWirelessBadRoadWeakCover;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisAboutThreshold;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.domain.security.User;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.continueWirelessBadRoad.IVolteContinueWirelessBadRoadService;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService;
import com.datang.service.platform.security.IUserService;
import com.datang.util.NumberFormatUtils;
import com.datang.web.beans.VoLTEDissertation.continueWirelessBadRoad.WholeIndexResponseBean;
import com.datang.web.beans.VoLTEDissertation.continueWirelessBadRoad.WholeIndexResponseBean1;
import com.datang.web.beans.VoLTEDissertation.continueWirelessBadRoad.WholeIndexResponseBean2;
import com.datang.web.beans.VoLTEDissertation.continueWirelessBadRoad.WholeIndexResponseBean3;

/**
 * Volte质量专题--连续无线差Service实现
 * 
 * @explain
 * @name VolteContinueWirelessBadRoadServiceImpl
 * @author shenyanwei
 * @date 2016年5月31日下午4:48:04
 */
@Service
@Transactional
public class VolteContinueWirelessBadRoadServiceImpl implements
		IVolteContinueWirelessBadRoadService {

	@Autowired
	private VolteContinueWirelessBadRoadCWBRDao volteContinueWirelessBadRoadDao;
	@Autowired
	private WeakCoverCWBRDao weakCoverDao;
	@Autowired
	private WeakCoverCellInfoCWBRDao weakCoverCellInfoDao;
	@Autowired
	private WeakCoverAdviceAddStationCWBRDao weakCoverAdviceAddStationDao;
	@Autowired
	private WeakCoverTianKuiAdjustCWBRDao weakCoverTianKuiAdjustDao;
	@Autowired
	private WeakCoverTianKuiConnectReverseCWBRDao weakCoverTianKuiConnectReverseDao;

	@Autowired
	private DisturbCWBRDao disturbDao;
	@Autowired
	private DisturbCellInfoCWBRDao disturbCellInfoDao;
	@Autowired
	private DisturbPCIAdjustCWBRDao disturbPCIAdjustDao;
	@Autowired
	private DisturbSanChaoCellCWBRDao disturbSanChaoCellDao;
	@Autowired
	private DisturbTianKuiAdjustCWBRDao disturbTianKuiAdjustDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private IVolteAnalysisThresholdService volteAnalysisThresholdService;
	@Autowired
	private VolteAnalysisAboutThresholdDao volteAnalysisAboutThresholdDao;
	@Autowired
	private NbCellDeficiencyCWBRDao nbCellDeficiencyDao;
	@Autowired
	private NbCellDeficiencyGSMAddAdviceCWBRDao nbCellDeficiencyGSMAddAdviceDao;
	@Autowired
	private NbCellDeficiencyLTEAddAdviceCWBRDao nbCellDeficiencyLTEAddAdviceDao;
	@Autowired
	private NbCellDeficiencyCoPerfCWBRDao nbCellDeficiencyCoPerfDao;
	@Autowired
	private OtherCWBRDao otherDao;
	@Autowired
	private OtherCellInfoCWBRDao otherCellInfoDao;
	@Autowired
	private TestLogItemSignallingDao testLogItemSignallingDao;
	@Autowired
	private TestLogItemDao testLogItemDao;
	private VolteContinueWirelessBadRoad volteContinueWirelessBadRoad;

	@Override
	public VolteContinueWirelessBadRoad getVolteContinueWirelessBadRoad(
			Long cwbrId) {

		return volteContinueWirelessBadRoadDao.find(cwbrId);
	}

	@Override
	public List<VolteContinueWirelessBadRoad> getVolteContinueWirelessBadRoadsByLogIds(
			List<Long> testLogItemIds) {

		return volteContinueWirelessBadRoadDao
				.queryVolteContinueWirelessBadRoadsByLogIds(testLogItemIds);
	}

	@Override
	public Float sumCWBRDistance(List<Long> testLogItemIds) {
		Float distance = null;
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return distance;
		}
		List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds = getVolteContinueWirelessBadRoadsByLogIds(testLogItemIds);
		if (null == volteContinueWirelessBadRoadsByLogIds
				|| 0 == volteContinueWirelessBadRoadsByLogIds.size()) {
			return distance;
		}
		for (VolteContinueWirelessBadRoad volteContinueWirelessBadRoad : volteContinueWirelessBadRoadsByLogIds) {
			/**
			 * 计算所有问题路段:总里程
			 */
			Float m_dbDistance = volteContinueWirelessBadRoad.getM_dbDistance();
			if (null != m_dbDistance) {
				distance = (null == distance ? m_dbDistance : distance
						+ m_dbDistance);
			}
		}
		return null == distance ? distance : NumberFormatUtils.format(distance,
				2);
	}

	@Override
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		// map.put("wholeRoadIndex0",doWholeIndex0Analysis(volteQualityBadRoadsByLogIds));
		map.put("wholeRoadIndex0",
				doWholeIndex00Analysis(volteContinueWirelessBadRoadsByLogIds,
						ids));
		map.put("wholeRoadIndex1",
				doWholeIndex1Analysis(volteContinueWirelessBadRoadsByLogIds,
						queryTestLogItems));
		map.put("wholeRoadIndex2", doWholeIndex2Analysis(ids));
		map.put("wholeRoadIndex3",
				doWholeIndex3Analysis(volteContinueWirelessBadRoadsByLogIds));
		return map;
	}

	@Override
	public EasyuiPageList doWholeIndex00Analysis(
			List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds,
			List<Long> ids) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean> rows = new ArrayList<>();
		WholeIndexResponseBean wholeIndexResponseBean = new WholeIndexResponseBean();
		rows.add(wholeIndexResponseBean);
		easyuiPageList.setRows(rows);
		VolteContinueWirelessBadRoad volteContinueWirelessBadRoad0 = new VolteContinueWirelessBadRoad();// 主叫bean

		for (VolteContinueWirelessBadRoad volteContinueWirelessBadRoad : volteContinueWirelessBadRoadsByLogIds) {
			/**
			 * 问题路段LTE制式下上报的MOS采样点个数
			 */
			Long m_ulLTE_MosPointNum = volteContinueWirelessBadRoad0
					.getM_ulLTE_MosPointNum();
			Long m_ulLTE_MosPointNum2 = volteContinueWirelessBadRoad
					.getM_ulLTE_MosPointNum();
			if (null != m_ulLTE_MosPointNum2) {
				volteContinueWirelessBadRoad0
						.setM_ulLTE_MosPointNum(m_ulLTE_MosPointNum == null ? m_ulLTE_MosPointNum2
								: m_ulLTE_MosPointNum + m_ulLTE_MosPointNum2);
			}
			/**
			 * 问题路段LTE制式下上报的MOS采样点值累计和
			 */
			Float m_dbLTE_MosValueSum = volteContinueWirelessBadRoad0
					.getM_dbLTE_MosValueSum();
			Float m_dbLTE_MosValueSum2 = volteContinueWirelessBadRoad
					.getM_dbLTE_MosValueSum();
			if (null != m_dbLTE_MosValueSum2) {
				volteContinueWirelessBadRoad0
						.setM_dbLTE_MosValueSum(m_dbLTE_MosValueSum == null ? m_dbLTE_MosValueSum2
								: m_dbLTE_MosValueSum + m_dbLTE_MosValueSum2);
			}

			/**
			 * RTP丢包率:丢失的VoIP数据包数量
			 */
			Float m_dbRTPSendVoIPDataPackageNum = volteContinueWirelessBadRoad0
					.getM_dbRTPLoseVoIPDataPackageNum();
			Float m_dbRTPSendVoIPDataPackageNum2 = volteContinueWirelessBadRoad
					.getM_dbRTPLoseVoIPDataPackageNum();
			if (null != m_dbRTPSendVoIPDataPackageNum2) {
				volteContinueWirelessBadRoad0
						.setM_dbRTPLoseVoIPDataPackageNum(m_dbRTPSendVoIPDataPackageNum == null ? m_dbRTPSendVoIPDataPackageNum2
								: m_dbRTPSendVoIPDataPackageNum
										+ m_dbRTPSendVoIPDataPackageNum2);
			}
			/**
			 * RTP丢包率:接收的数据包数量
			 */
			Float m_dbRTPReceiveDataPackageNum = volteContinueWirelessBadRoad0
					.getM_dbRTPReceiveDataPackageNum();
			Float m_dbRTPReceiveDataPackageNum2 = volteContinueWirelessBadRoad
					.getM_dbRTPReceiveDataPackageNum();
			if (null != m_dbRTPReceiveDataPackageNum2) {
				volteContinueWirelessBadRoad0
						.setM_dbRTPReceiveDataPackageNum(m_dbRTPReceiveDataPackageNum == null ? m_dbRTPReceiveDataPackageNum2
								: m_dbRTPReceiveDataPackageNum
										+ m_dbRTPReceiveDataPackageNum2);
			}

			/**
			 * 问题路段sinr值之和
			 */
			Float m_dbSinrValueSum = volteContinueWirelessBadRoad0
					.getM_dbSinrValueSum();
			Float m_dbSinrValueSum2 = volteContinueWirelessBadRoad
					.getM_dbSinrValueSum();
			if (null != m_dbSinrValueSum2) {
				volteContinueWirelessBadRoad0
						.setM_dbSinrValueSum(m_dbSinrValueSum == null ? m_dbSinrValueSum2
								: m_dbSinrValueSum + m_dbSinrValueSum2);
			}

			/**
			 * 问题路段rsrp值之和
			 */
			Float m_dbRsrpValueSum = volteContinueWirelessBadRoad0
					.getM_dbRsrpValueSum();
			Float m_dbRsrpValueSum2 = volteContinueWirelessBadRoad
					.getM_dbRsrpValueSum();
			if (null != m_dbRsrpValueSum2) {
				volteContinueWirelessBadRoad0
						.setM_dbRsrpValueSum(m_dbRsrpValueSum == null ? m_dbRsrpValueSum2
								: m_dbRsrpValueSum + m_dbRsrpValueSum2);
			}

			/**
			 * 问题路段rsrp采样点个数,问题路段sinr采样点个数,问题路段总的采样点个数
			 */
			Long m_ulRsrpOrSinrPointNum = volteContinueWirelessBadRoad0
					.getM_ulRsrpOrSinrPointNum();
			Long m_ulRsrpOrSinrPointNum2 = volteContinueWirelessBadRoad
					.getM_ulRsrpOrSinrPointNum();
			if (null != m_ulRsrpOrSinrPointNum2) {
				volteContinueWirelessBadRoad0
						.setM_ulRsrpOrSinrPointNum(m_ulRsrpOrSinrPointNum == null ? m_ulRsrpOrSinrPointNum2
								: m_ulRsrpOrSinrPointNum
										+ m_ulRsrpOrSinrPointNum2);
			}
			/**
			 * 连续无线差路段里程总和
			 */
			Float m_dbDistance = volteContinueWirelessBadRoad0
					.getM_dbDistance();
			Float m_dbDistance2 = volteContinueWirelessBadRoad
					.getM_dbDistance();
			if (null != m_dbDistance2) {
				volteContinueWirelessBadRoad0
						.setM_dbDistance(m_dbDistance == null ? m_dbDistance2
								: m_dbDistance + m_dbDistance2);
			}

		}

		/**
		 * 计算所选日志总里程
		 */
		Float LogSum = null;// 所选日志总里程
		for (Long id : ids) {
			TestLogItem find = testLogItemDao.find(id);
			Float distance = find.getDistance();
			if (null != distance) {
				LogSum = (null == LogSum ? distance : LogSum + distance);
			}
		}
		/**
		 * 查询连续无线差距离门限
		 */
		String userName = (String) SecurityUtils.getSubject().getPrincipal();
		User user = null;
		if (userName != null) {
			user = userService.findByUsername(userName);
		}
		String threshold = null;
		VolteAnalysisThreshold byNameEn = volteAnalysisThresholdService
				.getVolteAnalysisThresholdByNameEn("ContinueWBMileage");

		List<VolteAnalysisAboutThreshold> selectByUser = volteAnalysisAboutThresholdDao
				.selectByUser(user);
		for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : selectByUser) {
			if (volteAnalysisAboutThreshold.getThresholdType().equals(
					"ContinueWirelessBad")) {
				if (volteAnalysisAboutThreshold.getValue().equals("1")) {
					threshold = byNameEn.getThreshold1();
				} else if (volteAnalysisAboutThreshold.getValue().equals("2")) {
					threshold = byNameEn.getThreshold2();
				} else if (volteAnalysisAboutThreshold.getValue().equals("3")) {
					threshold = byNameEn.getThreshold3();
				} else {
					threshold = byNameEn.getThreshold1();
				}
			} else {
				threshold = byNameEn.getThreshold1();
			}
		}
		if (StringUtils.hasText(threshold)) {

			wholeIndexResponseBean.setDistanceThreshold(Float
					.valueOf(threshold));
		}

		/**
		 * 计算连续无线差里程占比(%)
		 */
		Float distance = volteContinueWirelessBadRoad0.getM_dbDistance();
		if (null != LogSum && LogSum != 0 && null != distance) {
			wholeIndexResponseBean.setMileageProportion(NumberFormatUtils
					.format(distance * 100 / LogSum, 2));
		}
		/**
		 * 计算volte平均mos
		 */
		Long m_ulLTE_MosPointNum = volteContinueWirelessBadRoad0
				.getM_ulLTE_MosPointNum();
		Float m_dbLTE_MosValueSum = volteContinueWirelessBadRoad0
				.getM_dbLTE_MosValueSum();
		if (null != m_ulLTE_MosPointNum && m_ulLTE_MosPointNum != 0
				&& null != m_dbLTE_MosValueSum) {
			wholeIndexResponseBean.setVolteMosAvg(NumberFormatUtils.format(
					m_dbLTE_MosValueSum / m_ulLTE_MosPointNum, 2));
		}
		/**
		 * 计算RTP丢包率=丢失的VoIP数据包数量/接收的数据包数量
		 * 
		 */
		float m_dbRTPSendVoIPDataPackageNum = NumberFormatUtils
				.isEmpty(volteContinueWirelessBadRoad0
						.getM_dbRTPLoseVoIPDataPackageNum());
		float m_dbRTPReceiveDataPackageNum = NumberFormatUtils
				.isEmpty(volteContinueWirelessBadRoad0
						.getM_dbRTPReceiveDataPackageNum());
		if (0 != m_dbRTPReceiveDataPackageNum) {
			wholeIndexResponseBean.setRtpDropRatio(NumberFormatUtils.format(
					m_dbRTPSendVoIPDataPackageNum
							/ m_dbRTPReceiveDataPackageNum, 2));
		}
		/**
		 * 计算sinr均值
		 */
		Float m_dbSinrValueSum = volteContinueWirelessBadRoad0
				.getM_dbSinrValueSum();
		Long m_ulRsrpOrSinrPointNum = volteContinueWirelessBadRoad0
				.getM_ulRsrpOrSinrPointNum();
		if (null != m_ulRsrpOrSinrPointNum && m_ulRsrpOrSinrPointNum != 0
				&& null != m_dbSinrValueSum) {
			wholeIndexResponseBean.setSinrAvg(NumberFormatUtils.format(
					m_dbSinrValueSum / m_ulRsrpOrSinrPointNum, 2));
		}

		/**
		 * 计算rsrp均值
		 */
		Float m_dbRsrpValueSum = volteContinueWirelessBadRoad0
				.getM_dbRsrpValueSum();
		if (null != m_ulRsrpOrSinrPointNum && m_ulRsrpOrSinrPointNum != 0
				&& null != m_dbRsrpValueSum) {
			wholeIndexResponseBean.setRsrpAvg(NumberFormatUtils.format(
					m_dbRsrpValueSum / m_ulRsrpOrSinrPointNum, 2));
		}

		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex1Analysis(
			List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean1> rows = new ArrayList<>();
		WholeIndexResponseBean1 wholeIndexResponseBean1 = new WholeIndexResponseBean1();
		rows.add(wholeIndexResponseBean1);
		easyuiPageList.setRows(rows);
		if (null == volteContinueWirelessBadRoadsByLogIds
				|| 0 == volteContinueWirelessBadRoadsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置连续无线差路段数量
		 */
		wholeIndexResponseBean1
				.setRoadNum(volteContinueWirelessBadRoadsByLogIds.size());
		Float cellNum = null;// 所有连续无线差路段:小区数量
		Set<String> boxIdSet = new HashSet<>();// 所有连续无线差路段:设备数量,以设备boxid为准
		Float cellTotalNum = null;// 所有测试日志:小区数量
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VolteContinueWirelessBadRoad volteContinueWirelessBadRoad : volteContinueWirelessBadRoadsByLogIds) {
			/**
			 * 计算所有连续无线差路段:总里程和总测试时长
			 */
			Float distance = wholeIndexResponseBean1.getDistance();
			Float m_dbDistance = volteContinueWirelessBadRoad.getM_dbDistance();
			Long continueTime = wholeIndexResponseBean1.getContinueTime();
			Long m_dbContinueTime = volteContinueWirelessBadRoad
					.getM_dbContinueTime();
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
			 * 计算所有连续无线差路段:小区数
			 * 
			 */

			Long m_ulCellNum = volteContinueWirelessBadRoad.getM_ulCellNum();
			if (null != m_ulCellNum) {
				cellNum = (cellNum == null ? m_ulCellNum : cellNum
						+ m_ulCellNum);
			}
			/**
			 * 计算所有连续无线差路段:设备数量,以设备boxid为准
			 */
			String boxId = volteContinueWirelessBadRoad.getTestLogItem()
					.getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
		}
		for (TestLogItem testLog : queryTestLogItems) {
			/**
			 * 计算所有测试日志:小区数
			 * 
			 */

			Long testLogCellNum = testLog.getCellSumNum();

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
		// 汇总计算小区占比,终端数量占比
		if (null != cellTotalNum && cellTotalNum != 0 && null != cellNum) {
			wholeIndexResponseBean1.setCellNumRatio(NumberFormatUtils.format(
					cellNum / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size()) {
			wholeIndexResponseBean1.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		System.out.println(wholeIndexResponseBean1);
		return easyuiPageList;

	}

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
		wholeIndexResponseBean2.setOtherProblem(otherDao
				.queryOtherRoadNumByLogIds(ids));
		System.out.println(wholeIndexResponseBean2);
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex3Analysis(
			List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeIndexResponseBean3> rows = new ArrayList<>();
		WholeIndexResponseBean3 wholeIndexResponseBean3 = new WholeIndexResponseBean3();
		rows.add(wholeIndexResponseBean3);
		easyuiPageList.setRows(rows);
		if (null == volteContinueWirelessBadRoadsByLogIds
				|| 0 == volteContinueWirelessBadRoadsByLogIds.size()) {
			return easyuiPageList;
		}
		for (VolteContinueWirelessBadRoad volteContinueWirelessBadRoad : volteContinueWirelessBadRoadsByLogIds) {
			Long m_ulTianKuiCellNum = volteContinueWirelessBadRoad
					.getM_ulTianKuiCellNum();
			Integer tianKuiCellNum = wholeIndexResponseBean3
					.getTianKuiCellNum();
			if (null != m_ulTianKuiCellNum) {
				wholeIndexResponseBean3
						.setTianKuiCellNum((int) (null == tianKuiCellNum ? m_ulTianKuiCellNum
								: tianKuiCellNum + m_ulTianKuiCellNum));
			}
			Long m_ulLinQuCellNum = volteContinueWirelessBadRoad
					.getM_ulLinQuCellNum();
			Integer nbCellNum = wholeIndexResponseBean3.getNbCellNum();
			if (null != m_ulLinQuCellNum) {
				wholeIndexResponseBean3
						.setNbCellNum((int) (null == nbCellNum ? m_ulLinQuCellNum
								: nbCellNum + m_ulLinQuCellNum));
			}
			Long m_ulPCICellNum = volteContinueWirelessBadRoad
					.getM_ulPCICellNum();
			Integer pciCellNum = wholeIndexResponseBean3.getPciCellNum();
			if (null != m_ulPCICellNum) {
				wholeIndexResponseBean3
						.setPciCellNum((int) (null == pciCellNum ? m_ulPCICellNum
								: pciCellNum + m_ulPCICellNum));
			}
			wholeIndexResponseBean3.setTotalCellNum(wholeIndexResponseBean3
					.getNbCellNum()
					+ wholeIndexResponseBean3.getPciCellNum()
					+ wholeIndexResponseBean3.getTianKuiCellNum());
		}
		return easyuiPageList;
	}

	@Override
	public List<VolteContinueWirelessBadRoadWeakCover> getWeakCoverRoadsByLogIds(
			List<Long> testLogItemIds) {

		return weakCoverDao.queryWeakCoverRoadsByLogIds(testLogItemIds);
	}

	@Override
	public VolteContinueWirelessBadRoadWeakCover getWeakCoverRoadById(Long id) {

		return weakCoverDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doWeakCoverAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("weakCoverRoadCellInfo",
				doWeakCoverCellInfoAnalysis(volteContinueWirelessBadRoadWeakCover));
		map.put("weakCoverRoadTianKuiAdjustCellInfo",
				doWeakCoverTianKuiAdjustAnalysis(volteContinueWirelessBadRoadWeakCover));
		map.put("weakCoverRoadTiankuiConnectReverseCellInfo",
				doWeakCoverTianKuiConnectReverseAnalysis(volteContinueWirelessBadRoadWeakCover));
		map.put("weakCoverRoadAdviceAddStationCellInfo",
				doWeakCoverAdviceAddStationAnalysis(volteContinueWirelessBadRoadWeakCover));
		return map;
	}

	@Override
	public EasyuiPageList doWeakCoverCellInfoAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover) {
		if (null == volteContinueWirelessBadRoadWeakCover
				|| null == volteContinueWirelessBadRoadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverCellInfoDao
				.queryCellInfoByRoad(volteContinueWirelessBadRoadWeakCover));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWeakCoverTianKuiAdjustAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover) {
		if (null == volteContinueWirelessBadRoadWeakCover
				|| null == volteContinueWirelessBadRoadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverTianKuiAdjustDao
				.queryCellInfoByRoad(volteContinueWirelessBadRoadWeakCover));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWeakCoverTianKuiConnectReverseAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover) {
		if (null == volteContinueWirelessBadRoadWeakCover
				|| null == volteContinueWirelessBadRoadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverTianKuiConnectReverseDao
				.queryCellInfoByRoad(volteContinueWirelessBadRoadWeakCover));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWeakCoverAdviceAddStationAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover) {
		if (null == volteContinueWirelessBadRoadWeakCover
				|| null == volteContinueWirelessBadRoadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverAdviceAddStationDao
				.queryCellInfoByRoad(volteContinueWirelessBadRoadWeakCover));
		return easyuiPageList;
	}

	@Override
	public List<VolteContinueWirelessBadRoadDisturbProblem> getDisturbRoadsByLogIds(
			List<Long> testLogItemIds) {
		return disturbDao.queryDisturbRoadsByLogIds(testLogItemIds);
	}

	@Override
	public VolteContinueWirelessBadRoadDisturbProblem getDisturbRoadById(
			Long roadId) {

		return disturbDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doDisturbAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById) {
		Map<String, EasyuiPageList> map = new HashMap<>();
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

	@Override
	public EasyuiPageList doDisturbCellInfoAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById || null == disturbRoadById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbCellInfoDao
				.queryCellInfoByRoad(disturbRoadById));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doDisturbPCIAdjustAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById || null == disturbRoadById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbPCIAdjustDao
				.queryCellInfoByRoad(disturbRoadById));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doDisturbTianKuiAdjustAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById || null == disturbRoadById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbTianKuiAdjustDao
				.queryCellInfoByRoad(disturbRoadById));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doDisturbSanChaoCellAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById) {
		if (null == disturbRoadById || null == disturbRoadById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbSanChaoCellDao
				.queryCellInfoByRoad(disturbRoadById));
		return easyuiPageList;
	}

	@Override
	public List<VolteContinueWirelessBadRoadNbDeficiency> getNbDeficiencyRoadsByLogIds(
			List<Long> testLogItemIds) {

		return nbCellDeficiencyDao
				.queryNbDeficiencyRoadsByLogIds(testLogItemIds);
	}

	@Override
	public VolteContinueWirelessBadRoadNbDeficiency getNbDeficiencyRoadById(
			Long roadId) {

		return nbCellDeficiencyDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doNbDeficiencyAnalysis(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("nbCellLTEAddAdvice",
				doNbDeficiencyLTEAddAdviceAnalysis(volteContinueWirelessBadRoadNbDeficiency));
		map.put("nbCellGSMAddAdvice",
				doNbDeficiencyGSMAddAdviceAnalysis(volteContinueWirelessBadRoadNbDeficiency));
		map.put("nbCellCoPerf",
				doNbDeficiencyCoPerfAnalysis(volteContinueWirelessBadRoadNbDeficiency));
		return map;
	}

	@Override
	public EasyuiPageList doNbDeficiencyLTEAddAdviceAnalysis(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency) {
		if (null == volteContinueWirelessBadRoadNbDeficiency
				|| null == volteContinueWirelessBadRoadNbDeficiency.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(nbCellDeficiencyLTEAddAdviceDao
				.queryCellInfoByRoad(volteContinueWirelessBadRoadNbDeficiency));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doNbDeficiencyGSMAddAdviceAnalysis(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency) {
		if (null == volteContinueWirelessBadRoadNbDeficiency
				|| null == volteContinueWirelessBadRoadNbDeficiency.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(nbCellDeficiencyGSMAddAdviceDao
				.queryCellInfoByRoad(volteContinueWirelessBadRoadNbDeficiency));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doNbDeficiencyCoPerfAnalysis(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency) {
		if (null == volteContinueWirelessBadRoadNbDeficiency
				|| null == volteContinueWirelessBadRoadNbDeficiency.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(nbCellDeficiencyCoPerfDao
				.queryCellInfoByRoad(volteContinueWirelessBadRoadNbDeficiency));
		return easyuiPageList;
	}

	@Override
	public List<VolteContinueWirelessBadRoadOtherProblem> getOtherRoadsByLogIds(
			List<Long> testLogItemIds) {

		return otherDao.queryOtherRoadsByLogIds(testLogItemIds);
	}

	@Override
	public VolteContinueWirelessBadRoadOtherProblem getOtherRoadById(Long roadId) {

		return otherDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doOtherAnalysis(
			VolteContinueWirelessBadRoadOtherProblem volteContinueWirelessBadRoadOtherProblem) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("otherRoadCellInfo",
				doOtherCellInfoAnalysis(volteContinueWirelessBadRoadOtherProblem));
		return map;
	}

	@Override
	public EasyuiPageList doOtherCellInfoAnalysis(
			VolteContinueWirelessBadRoadOtherProblem volteContinueWirelessBadRoadOtherProblem) {
		if (null == volteContinueWirelessBadRoadOtherProblem
				|| null == volteContinueWirelessBadRoadOtherProblem.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(otherCellInfoDao
				.queryCellInfoByRoad(volteContinueWirelessBadRoadOtherProblem));
		return easyuiPageList;
	}

	@Override
	public AbstractPageList doOtherRoadSignllingPageList(PageList pageList) {
		return testLogItemSignallingDao.getPageSignalling(pageList);
	}

	@Override
	public List<VolteContinueWirelessBadRoad> getNullRoadNameCWBR() {

		return volteContinueWirelessBadRoadDao.queryNullRoadName();
	}

	@Override
	public void addCWBRoadName(String roadName, Long cwbrId) {
		VolteContinueWirelessBadRoad find = volteContinueWirelessBadRoadDao
				.find(cwbrId);
		if (null != find && !StringUtils.hasText(find.getM_stRoadName())) {
			volteContinueWirelessBadRoadDao.addCWBRRoadName(roadName, cwbrId);
		}

	}

}
