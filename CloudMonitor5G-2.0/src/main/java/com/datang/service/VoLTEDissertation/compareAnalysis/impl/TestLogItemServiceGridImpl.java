/**
 * 
 */
package com.datang.service.VoLTEDissertation.compareAnalysis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.dao.gis.TestLogItemGrid100Dao;
import com.datang.dao.gis.TestLogItemGrid500Dao;
import com.datang.dao.gis.TestLogItemGrid50Dao;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisAboutThreshold;
import com.datang.domain.security.User;
import com.datang.service.VoLTEDissertation.compareAnalysis.ITestLogItemGridService;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisAboutThresholdService;
import com.datang.service.platform.security.IUserService;
import com.datang.util.NumberFormatUtils;
import com.datang.web.beans.VoLTEDissertation.compareAnalysis.TestLogItemGridBean;

/**
 * volte专题---对比分析栅格对比Service接口实现
 * 
 * @author yinzhipeng
 * @date:2016年6月30日 上午9:19:57
 * @version
 */
@Service
@Transactional
public class TestLogItemServiceGridImpl implements ITestLogItemGridService {
	@Autowired
	private IUserService userService;
	@Autowired
	private IVolteAnalysisAboutThresholdService volteAnalysisAboutThresholdService;
	@Autowired
	private TestLogItemGrid50Dao testLogItemGrid50Dao;
	@Autowired
	private TestLogItemGrid100Dao testLogItemGrid100Dao;
	@Autowired
	private TestLogItemGrid500Dao testLogItemGrid500Dao;

	@Value("${gis.compareGridBaddest.color}")
	private String compareGridBaddestColor;
	@Value("${gis.compareGridBad.color}")
	private String compareGridBadColor;
	@Value("${gis.compareGridOther.color}")
	private String compareGridOtherColor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.compareAnalysis.ITestLogItemGridService
	 * #doTestLogItemGridAnalysis(java.util.List, java.util.List)
	 */
	@Override
	public Map<String, EasyuiPageList> doTestLogItemGridAnalysis(
			List<Long> ids, List<Long> compareIds) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("testLogItemGrid0", new EasyuiPageList());
		if (null == ids || 0 == ids.size() || null == compareIds
				|| 0 == compareIds.size()) {
			return map;
		}
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User findByUsername = userService.findByUsername(username);

		List<VolteAnalysisAboutThreshold> selectCompareByUser = volteAnalysisAboutThresholdService
				.selectCompareByUser(findByUsername);
		Map<String, String> volteAnalysisAboutThresholdMap = new HashMap<>();
		for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : selectCompareByUser) {
			volteAnalysisAboutThresholdMap.put(
					volteAnalysisAboutThreshold.getNameEn(),
					volteAnalysisAboutThreshold.getValue());
		}
		// 获取栅格大小
		String gridSizeString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.GRIDSIZE);
		Integer gridSize = 100;
		try {
			gridSize = Integer.valueOf(gridSizeString);
		} catch (NumberFormatException e) {
			// 用户级别栅格大小有错,采用默认栅格大小100米
			// TODO Auto-generated catch block
		}
		// 原始日志(mos,rsrp,sinr,rtp)栅格
		List<TestLogItemGridBean> mosGrid = new ArrayList<>();
		List<TestLogItemGridBean> rsrpGrid = new ArrayList<>();
		List<TestLogItemGridBean> sinrGrid = new ArrayList<>();
		List<TestLogItemGridBean> rtpGrid = new ArrayList<>();
		// 对比日志(mos,rsrp,sinr,rtp)栅格
		List<TestLogItemGridBean> compareMosGrid = new ArrayList<>();
		List<TestLogItemGridBean> compareRsrpGrid = new ArrayList<>();
		List<TestLogItemGridBean> compareSinrGrid = new ArrayList<>();
		List<TestLogItemGridBean> compareRtpGrid = new ArrayList<>();
		switch (gridSize) {
		case 50:
			mosGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids, 0);
			rsrpGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids, 1);
			sinrGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids, 2);
			rtpGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids, 6);
			compareMosGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, 0);
			compareRsrpGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, 1);
			compareSinrGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, 2);
			compareRtpGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, 6);
			break;
		case 100:
			mosGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 0);
			rsrpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 1);
			sinrGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 2);
			rtpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 6);
			compareMosGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 0);
			compareRsrpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 1);
			compareSinrGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 2);
			compareRtpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 6);
			break;
		case 500:
			mosGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids, 0);
			rsrpGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids, 1);
			sinrGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids, 2);
			rtpGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids, 6);
			compareMosGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, 0);
			compareRsrpGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, 1);
			compareSinrGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, 2);
			compareRtpGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, 6);
			break;

		default:
			mosGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 0);
			rsrpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 1);
			sinrGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 2);
			rtpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 6);
			compareMosGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 0);
			compareRsrpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 1);
			compareSinrGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 2);
			compareRtpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 6);
			break;
		}

		// 存储栅格指标变化情况
		EasyuiPageList testLogItemGrid0 = new EasyuiPageList();
		List testLogItemGrid0Rows = testLogItemGrid0.getRows();
		map.put("testLogItemGrid0", testLogItemGrid0);

		// 计算mos栅格指标变化情况
		Map<String, Object> mosResultMap = new HashMap<>();
		mosResultMap.put("indexType", "MOS");
		testLogItemGrid0Rows.add(0, mosResultMap);
		// 获取MOS值稍降门限
		String mosDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.MOS_DECLINE);
		Double mosDecline = 0.4d;
		try {
			mosDecline = Double.valueOf(mosDeclineString);
		} catch (NumberFormatException e) {
			// 用户级别MOS值稍降门限有错,采用默认MOS值稍降门限0.4
			// TODO Auto-generated catch block
		}
		Double mosValueSum = null;
		Long mosNumSum = null;
		Double compareMosValueSum = null;
		Long compareMosNumSum = null;
		Integer mosBetterGridNum = null;
		Integer mosBadGridNum = null;
		Integer mosGridNum = null;
		int mosIndex = 0;
		for (TestLogItemGridBean testLogItemGridBean : mosGrid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			mosValueSum = null == mosValueSum ? indexValueSumDouble
					: mosValueSum + indexValueSumDouble;
			mosNumSum = null == mosNumSum ? indexNumSumLong : mosNumSum
					+ indexNumSumLong;
			for (TestLogItemGridBean compareTestLogItemGridBean : compareMosGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (0 == mosIndex) {
					compareMosValueSum = null == compareMosValueSum ? compareIndexValueSumDouble
							: compareMosValueSum + compareIndexValueSumDouble;
					compareMosNumSum = null == compareMosNumSum ? compareIndexNumSumLong
							: compareMosNumSum + compareIndexNumSumLong;
				}
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					// 原始日志和对比日志都具备该栅格,则计算栅格指标变化趋势,并纳入计数
					mosGridNum = null == mosGridNum ? 1 : mosGridNum + 1;
					// 对比日志-原始日志>=MOS值稍降门限,指标提升
					if ((compareIndexValueSumDouble / compareIndexNumSumLong - indexValueSumDouble
							/ indexNumSumLong) >= mosDecline) {
						mosBetterGridNum = null == mosBetterGridNum ? 1
								: mosBetterGridNum + 1;
					}
					// 原始日志-对比日志>=MOS值稍降门限,指标恶化
					if ((indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
							/ compareIndexNumSumLong) >= mosDecline) {
						mosBadGridNum = null == mosBadGridNum ? 1
								: mosBadGridNum + 1;
					}
				}
			}
			mosIndex++;
		}
		if (null != mosValueSum && null != mosNumSum && 0 != mosNumSum) {
			mosResultMap.put("valueAvg", mosValueSum / mosNumSum);
		}
		if (null != compareMosValueSum && null != compareMosNumSum
				&& 0 != compareMosNumSum) {
			mosResultMap.put("compareValueAvg", compareMosValueSum
					/ compareMosNumSum);
		}
		if (null != mosGridNum) {
			mosResultMap.put("totalNum", mosGridNum);
		}
		if (null != mosBetterGridNum) {
			mosResultMap.put("betterNum", mosBetterGridNum);
		}
		if (null != mosGridNum && 0 != mosGridNum && null != mosBetterGridNum) {
			mosResultMap.put(
					"betterNumRatio",
					NumberFormatUtils.format((float) mosBetterGridNum * 100
							/ mosGridNum, 2));
		} else {
			mosResultMap.put("betterNumRatio", "-");
		}
		if (null != mosBadGridNum) {
			mosResultMap.put("badNum", mosBadGridNum);
		}
		if (null != mosGridNum && 0 != mosGridNum && null != mosBadGridNum) {
			mosResultMap.put(
					"badNumRatio",
					NumberFormatUtils.format((float) mosBadGridNum * 100
							/ mosGridNum, 2));
		} else {
			mosResultMap.put("badNumRatio", "-");
		}

		// 计算rsrp栅格指标变化情况
		Map<String, Object> rsrpResultMap = new HashMap<>();
		rsrpResultMap.put("indexType", "RSRP");
		testLogItemGrid0Rows.add(1, rsrpResultMap);
		// 获取rsrp均值稍降门限
		String rsrpAvgDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RSRP_AVAREGE_DECLINE);
		// 获取rsrp差值稍降门限
		String rsrpDiffDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RSRP_DIFFERENCE_DECLINE);
		Double rsrpAvgDecline = -95d;
		Double rsrpDiffDecline = 5d;
		try {
			rsrpAvgDecline = Double.valueOf(rsrpAvgDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			rsrpDiffDecline = Double.valueOf(rsrpDiffDeclineString);
		} catch (NumberFormatException e) {
		}
		Double rsrpValueSum = null;
		Long rsrpNumSum = null;
		Double compareRsrpValueSum = null;
		Long compareRsrpNumSum = null;
		Integer rsrpBetterGridNum = null;
		Integer rsrpBadGridNum = null;
		Integer rsrpGridNum = null;
		int rsrpIndex = 0;
		for (TestLogItemGridBean testLogItemGridBean : rsrpGrid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			rsrpValueSum = null == rsrpValueSum ? indexValueSumDouble
					: rsrpValueSum + indexValueSumDouble;
			rsrpNumSum = null == rsrpNumSum ? indexNumSumLong : rsrpNumSum
					+ indexNumSumLong;
			for (TestLogItemGridBean compareTestLogItemGridBean : compareRsrpGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (0 == rsrpIndex) {
					compareRsrpValueSum = null == compareRsrpValueSum ? compareIndexValueSumDouble
							: compareRsrpValueSum + compareIndexValueSumDouble;
					compareRsrpNumSum = null == compareRsrpNumSum ? compareIndexNumSumLong
							: compareRsrpNumSum + compareIndexNumSumLong;
				}
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					// 原始日志和对比日志都具备该栅格,则计算栅格指标变化趋势,并纳入计数
					rsrpGridNum = null == rsrpGridNum ? 1 : rsrpGridNum + 1;

					// 原始日志RSRP均值低于“RSRP均值稍降门限”,且对比日志RSRP均值-原始日志RSRP均值>=“RSRP差值稍降门限”,指标提升
					if ((indexValueSumDouble / indexNumSumLong <= rsrpAvgDecline)
							&& (compareIndexValueSumDouble
									/ compareIndexNumSumLong - indexValueSumDouble
									/ indexNumSumLong) >= rsrpDiffDecline) {
						rsrpBetterGridNum = null == rsrpBetterGridNum ? 1
								: rsrpBetterGridNum + 1;
					}
					// 对比日志RSRP均值低于“RSRP均值稍降门限”，且原始日志RSRP均值-对比日志RSRP均值>=“RSRP差值稍降门限”,指标恶化
					if ((compareIndexValueSumDouble / compareIndexNumSumLong) <= rsrpAvgDecline
							&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
									/ compareIndexNumSumLong) >= rsrpDiffDecline) {
						rsrpBadGridNum = null == rsrpBadGridNum ? 1
								: rsrpBadGridNum + 1;
					}
				}
			}
			rsrpIndex++;
		}
		if (null != rsrpValueSum && null != rsrpNumSum && 0 != rsrpNumSum) {
			rsrpResultMap.put("valueAvg", rsrpValueSum / rsrpNumSum);
		}
		if (null != compareRsrpValueSum && null != compareRsrpNumSum
				&& 0 != compareRsrpNumSum) {
			rsrpResultMap.put("compareValueAvg", compareRsrpValueSum
					/ compareRsrpNumSum);
		}
		if (null != rsrpGridNum) {
			rsrpResultMap.put("totalNum", rsrpGridNum);
		}
		if (null != rsrpBetterGridNum) {
			rsrpResultMap.put("betterNum", rsrpBetterGridNum);
		}
		if (null != rsrpGridNum && 0 != rsrpGridNum
				&& null != rsrpBetterGridNum) {
			rsrpResultMap.put(
					"betterNumRatio",
					NumberFormatUtils.format((float) rsrpBetterGridNum * 100
							/ rsrpGridNum, 2));
		} else {
			rsrpResultMap.put("betterNumRatio", "-");
		}
		if (null != rsrpBadGridNum) {
			rsrpResultMap.put("badNum", rsrpBadGridNum);
		}
		if (null != rsrpGridNum && 0 != rsrpGridNum && null != rsrpBadGridNum) {
			rsrpResultMap.put(
					"badNumRatio",
					NumberFormatUtils.format((float) rsrpBadGridNum * 100
							/ rsrpGridNum, 2));
		} else {
			rsrpResultMap.put("badNumRatio", "-");
		}

		// 计算sinr栅格指标变化情况
		Map<String, Object> sinrResultMap = new HashMap<>();
		sinrResultMap.put("indexType", "SINR");
		testLogItemGrid0Rows.add(2, sinrResultMap);
		// 获取sinr均值稍降门限
		String sinrAvgDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.SINR_AVAREGE_DECLINE);
		// 获取sinr差值稍降门限
		String sinrDiffDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.SINR_DIFFERENCE_DECLINE);
		Double sinrAvgDecline = 9d;
		Double sinrDiffDecline = 2d;
		try {
			sinrAvgDecline = Double.valueOf(sinrAvgDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			sinrDiffDecline = Double.valueOf(sinrDiffDeclineString);
		} catch (NumberFormatException e) {
		}
		Double sinrValueSum = null;
		Long sinrNumSum = null;
		Double compareSinrValueSum = null;
		Long compareSinrNumSum = null;
		Integer sinrBetterGridNum = null;
		Integer sinrBadGridNum = null;
		Integer sinrGridNum = null;
		int sinrIndex = 0;
		for (TestLogItemGridBean testLogItemGridBean : sinrGrid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			sinrValueSum = null == sinrValueSum ? indexValueSumDouble
					: sinrValueSum + indexValueSumDouble;
			sinrNumSum = null == sinrNumSum ? indexNumSumLong : sinrNumSum
					+ indexNumSumLong;
			for (TestLogItemGridBean compareTestLogItemGridBean : compareSinrGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (0 == sinrIndex) {
					compareSinrValueSum = null == compareSinrValueSum ? compareIndexValueSumDouble
							: compareSinrValueSum + compareIndexValueSumDouble;
					compareSinrNumSum = null == compareSinrNumSum ? compareIndexNumSumLong
							: compareSinrNumSum + compareIndexNumSumLong;
				}
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					// 原始日志和对比日志都具备该栅格,则计算栅格指标变化趋势,并纳入计数
					sinrGridNum = null == sinrGridNum ? 1 : sinrGridNum + 1;

					// 原始日志sinr均值低于“sinr均值稍降门限”,且对比日志sinr均值-原始日志sinr均值>=“sinr差值稍降门限”,指标提升
					if ((indexValueSumDouble / indexNumSumLong <= sinrAvgDecline)
							&& (compareIndexValueSumDouble
									/ compareIndexNumSumLong - indexValueSumDouble
									/ indexNumSumLong) >= sinrDiffDecline) {
						sinrBetterGridNum = null == sinrBetterGridNum ? 1
								: sinrBetterGridNum + 1;
					}
					// 对比日志sinr均值低于“sinr均值稍降门限”，且原始日志sinr均值-对比日志sinr均值>=“sinr差值稍降门限”,指标恶化
					if ((compareIndexValueSumDouble / compareIndexNumSumLong) <= sinrAvgDecline
							&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
									/ compareIndexNumSumLong) >= sinrDiffDecline) {
						sinrBadGridNum = null == sinrBadGridNum ? 1
								: sinrBadGridNum + 1;
					}
				}
			}
			sinrIndex++;
		}
		if (null != sinrValueSum && null != sinrNumSum && 0 != sinrNumSum) {
			sinrResultMap.put("valueAvg", sinrValueSum / sinrNumSum);
		}
		if (null != compareSinrValueSum && null != compareSinrNumSum
				&& 0 != compareSinrNumSum) {
			sinrResultMap.put("compareValueAvg", compareSinrValueSum
					/ compareSinrNumSum);
		}
		if (null != sinrGridNum) {
			sinrResultMap.put("totalNum", sinrGridNum);
		}
		if (null != sinrBetterGridNum) {
			sinrResultMap.put("betterNum", sinrBetterGridNum);
		}
		if (null != sinrGridNum && 0 != sinrGridNum
				&& null != sinrBetterGridNum) {
			sinrResultMap.put(
					"betterNumRatio",
					NumberFormatUtils.format((float) sinrBetterGridNum * 100
							/ sinrGridNum, 2));
		} else {
			sinrResultMap.put("betterNumRatio", "-");
		}
		if (null != sinrBadGridNum) {
			sinrResultMap.put("badNum", sinrBadGridNum);
		}
		if (null != sinrGridNum && 0 != sinrGridNum && null != sinrBadGridNum) {
			sinrResultMap.put(
					"badNumRatio",
					NumberFormatUtils.format((float) sinrBadGridNum * 100
							/ sinrGridNum, 2));
		} else {
			sinrResultMap.put("badNumRatio", "-");
		}

		// 计算rtp栅格指标变化情况
		Map<String, Object> rtpResultMap = new HashMap<>();
		rtpResultMap.put("indexType", "RTP丢包率");
		testLogItemGrid0Rows.add(3, rtpResultMap);
		// 获取rtp均值恶化门限
		String rtpAvgVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_AVAREGE_VORSEN);
		// 获取rtp差值恶化门限
		String rtpDiffVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_DIFFERENCE_VORSEN);
		Double rtpAvgVorsen = 30d;
		Double rtpDiffVorsen = 30d;
		try {
			rtpAvgVorsen = Double.valueOf(rtpAvgVorsenString);
		} catch (NumberFormatException e) {
		}
		try {
			rtpDiffVorsen = Double.valueOf(rtpDiffVorsenString);
		} catch (NumberFormatException e) {
		}
		Double rtpValueSum = null;
		Long rtpNumSum = null;
		Double comparertpValueSum = null;
		Long comparertpNumSum = null;
		Integer rtpBetterGridNum = null;
		Integer rtpBadGridNum = null;
		Integer rtpGridNum = null;
		int rtpIndex = 0;
		for (TestLogItemGridBean testLogItemGridBean : rtpGrid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			rtpValueSum = null == rtpValueSum ? indexValueSumDouble
					: rtpValueSum + indexValueSumDouble;
			rtpNumSum = null == rtpNumSum ? indexNumSumLong : rtpNumSum
					+ indexNumSumLong;
			for (TestLogItemGridBean compareTestLogItemGridBean : compareRtpGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (0 == rtpIndex) {
					comparertpValueSum = null == comparertpValueSum ? compareIndexValueSumDouble
							: comparertpValueSum + compareIndexValueSumDouble;
					comparertpNumSum = null == comparertpNumSum ? compareIndexNumSumLong
							: comparertpNumSum + compareIndexNumSumLong;
				}
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					// 原始日志和对比日志都具备该栅格,则计算栅格指标变化趋势,并纳入计数
					rtpGridNum = null == rtpGridNum ? 1 : rtpGridNum + 1;

					// 原始日志RTP丢包率均值高于“RTP均值恶化门限”，且对比日志RTP丢包率均值-原始日志RTP丢包率均值大于“RTP丢包率差值恶化门限”,指标提升
					if ((indexValueSumDouble / indexNumSumLong >= rtpAvgVorsen)
							&& (compareIndexValueSumDouble
									/ compareIndexNumSumLong - indexValueSumDouble
									/ indexNumSumLong) >= rtpDiffVorsen) {
						rtpBetterGridNum = null == rtpBetterGridNum ? 1
								: rtpBetterGridNum + 1;
					}
					// 对比日志RTP丢包率均值高于“RTP均值恶化门限”，且原始日志RTP丢包率均值-对比日志RTP丢包率均值大于“RTP丢包率差值恶化门限”,指标恶化
					if ((compareIndexValueSumDouble / compareIndexNumSumLong) >= rtpAvgVorsen
							&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
									/ compareIndexNumSumLong) >= rtpDiffVorsen) {
						rtpBadGridNum = null == rtpBadGridNum ? 1
								: rtpBadGridNum + 1;
					}
				}
			}
			rtpIndex++;
		}
		if (null != rtpValueSum && null != rtpNumSum && 0 != rtpNumSum) {
			rtpResultMap.put("valueAvg", rtpValueSum / rtpNumSum);
		}
		if (null != comparertpValueSum && null != comparertpNumSum
				&& 0 != comparertpNumSum) {
			rtpResultMap.put("compareValueAvg", comparertpValueSum
					/ comparertpNumSum);
		}
		if (null != rtpGridNum) {
			rtpResultMap.put("totalNum", rtpGridNum);
		}
		if (null != rtpBetterGridNum) {
			rtpResultMap.put("betterNum", rtpBetterGridNum);
		}
		if (null != rtpGridNum && 0 != rtpGridNum && null != rtpBetterGridNum) {
			rtpResultMap.put(
					"betterNumRatio",
					NumberFormatUtils.format((float) rtpBetterGridNum * 100
							/ rtpGridNum, 2));
		} else {
			rtpResultMap.put("betterNumRatio", "-");
		}
		if (null != rtpBadGridNum) {
			rtpResultMap.put("badNum", rtpBadGridNum);
		}
		if (null != rtpGridNum && 0 != rtpGridNum && null != rtpBadGridNum) {
			rtpResultMap.put(
					"badNumRatio",
					NumberFormatUtils.format((float) rtpBadGridNum * 100
							/ rtpGridNum, 2));
		} else {
			rtpResultMap.put("badNumRatio", "-");
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.compareAnalysis.ITestLogItemGridService
	 * #doTestLogItemGridAnalysis(java.util.List, java.util.List,
	 * java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> doTestLogItemGridAnalysis(List<Long> ids,
			List<Long> compareIds, Integer indexType) {
		List<Map<String, Object>> result = new ArrayList<>();
		if (null == ids || 0 == ids.size() || null == compareIds
				|| 0 == compareIds.size() || null == indexType) {
			return result;
		}
		if (0 != indexType && 1 != indexType && 2 != indexType
				&& 6 != indexType) {
			return result;
		}

		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User findByUsername = userService.findByUsername(username);

		List<VolteAnalysisAboutThreshold> selectCompareByUser = volteAnalysisAboutThresholdService
				.selectCompareByUser(findByUsername);
		Map<String, String> volteAnalysisAboutThresholdMap = new HashMap<>();
		for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : selectCompareByUser) {
			volteAnalysisAboutThresholdMap.put(
					volteAnalysisAboutThreshold.getNameEn(),
					volteAnalysisAboutThreshold.getValue());
		}
		// 获取栅格大小
		String gridSizeString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.GRIDSIZE);
		Integer gridSize = 100;
		try {
			gridSize = Integer.valueOf(gridSizeString);
		} catch (NumberFormatException e) {
		}

		// 原始日志(mos或者rsrp或者sinr或者rtp)栅格
		List<TestLogItemGridBean> grid = new ArrayList<>();
		// 对比日志(mos或者rsrp或者sinr或者rtp)栅格
		List<TestLogItemGridBean> compareGrid = new ArrayList<>();

		switch (gridSize) {
		case 50:
			grid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids,
							indexType);
			compareGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, indexType);
			break;
		case 100:
			grid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids,
							indexType);
			compareGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, indexType);
			break;
		case 500:
			grid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids,
							indexType);
			compareGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, indexType);
			break;
		default:
			grid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids,
							indexType);
			compareGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, indexType);
			break;
		}

		// 获取MOS值稍降门限
		String mosDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.MOS_DECLINE);
		// 获取MOS值恶化门限
		String mosVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.MOS_VORSEN);
		Double mosDecline = 0.4d;
		Double mosVorsen = 1d;
		try {
			mosDecline = Double.valueOf(mosDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			mosVorsen = Double.valueOf(mosVorsenString);
		} catch (NumberFormatException e) {
		}

		// 获取rsrp均值稍降门限
		String rsrpAvgDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RSRP_AVAREGE_DECLINE);
		// 获取rsrp差值稍降门限
		String rsrpDiffDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RSRP_DIFFERENCE_DECLINE);
		// 获取rsrp均值恶化门限
		String rsrpAvgVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RSRP_AVAREGE_VORSEN);
		// 获取rsrp差值恶化门限
		String rsrpDiffVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RSRP_DIFFERENCE_VORSEN);
		Double rsrpAvgDecline = -95d;
		Double rsrpDiffDecline = 5d;
		Double rsrpAvgVorsen = -100d;
		Double rsrpDiffVorsen = 10d;
		try {
			rsrpAvgDecline = Double.valueOf(rsrpAvgDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			rsrpDiffDecline = Double.valueOf(rsrpDiffDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			rsrpAvgVorsen = Double.valueOf(rsrpAvgVorsenString);
		} catch (NumberFormatException e) {
		}
		try {
			rsrpDiffVorsen = Double.valueOf(rsrpDiffVorsenString);
		} catch (NumberFormatException e) {
		}

		// 获取sinr均值稍降门限
		String sinrAvgDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.SINR_AVAREGE_DECLINE);
		// 获取sinr差值稍降门限
		String sinrDiffDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.SINR_DIFFERENCE_DECLINE);
		// 获取sinr均值恶化门限
		String sinrAvgVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.SINR_AVAREGE_VORSEN);
		// 获取sinr差值恶化门限
		String sinrDiffVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.SINR_DIFFERENCE_VORSEN);
		Double sinrAvgDecline = 9d;
		Double sinrDiffDecline = 2d;
		Double sinrAvgVorsen = 6d;
		Double sinrDiffVorsen = 4d;
		try {
			sinrAvgDecline = Double.valueOf(sinrAvgDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			sinrDiffDecline = Double.valueOf(sinrDiffDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			sinrAvgVorsen = Double.valueOf(sinrAvgVorsenString);
		} catch (NumberFormatException e) {
		}
		try {
			sinrDiffVorsen = Double.valueOf(sinrDiffVorsenString);
		} catch (NumberFormatException e) {
		}

		// 获取rtp均值稍降门限
		String rtpAvgDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_AVAREGE_DECLINE);
		// 获取rtp差值稍降门限
		String rtpDiffDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_DIFFERENCE_DECLINE);
		// 获取rtp均值恶化门限
		String rtpAvgVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_AVAREGE_VORSEN);
		// 获取rtp差值恶化门限
		String rtpDiffVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_DIFFERENCE_VORSEN);
		Double rtpAvgDecline = 20d;
		Double rtpDiffDecline = 20d;
		Double rtpAvgVorsen = 30d;
		Double rtpDiffVorsen = 30d;
		try {
			rtpAvgDecline = Double.valueOf(rtpAvgDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			rtpDiffDecline = Double.valueOf(rtpDiffDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			rtpAvgVorsen = Double.valueOf(rtpAvgVorsenString);
		} catch (NumberFormatException e) {
		}
		try {
			rtpDiffVorsen = Double.valueOf(rtpDiffVorsenString);
		} catch (NumberFormatException e) {
		}

		for (TestLogItemGridBean testLogItemGridBean : grid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			for (TestLogItemGridBean compareTestLogItemGridBean : compareGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					Map<String, Object> gridMap = new HashMap<>();
					result.add(gridMap);
					gridMap.put(
							"value",
							NumberFormatUtils.format(
									indexValueSumDouble.floatValue()
											/ indexNumSumLong, 2));
					gridMap.put(
							"compareValue",
							NumberFormatUtils.format(
									compareIndexValueSumDouble.floatValue()
											/ compareIndexNumSumLong, 2));
					gridMap.put("minx", compareTestLogItemGridBean.getMinx());
					gridMap.put("miny", compareTestLogItemGridBean.getMiny());
					gridMap.put("maxx", compareTestLogItemGridBean.getMaxx());
					gridMap.put("maxy", compareTestLogItemGridBean.getMaxy());

					switch (indexType) {
					case 0:
						// 对比日志MOS均值低于原始日志“MOS均值恶化门限”,栅格恶化
						if ((compareIndexValueSumDouble
								/ compareIndexNumSumLong - indexValueSumDouble
								/ indexNumSumLong) >= mosVorsen) {
							gridMap.put("color", compareGridBaddestColor);
						}
						// 对比日志MOS均值低于原始日志“MOS值稍降门限”,栅格稍降
						if (null == gridMap.get("color")
								&& (compareIndexValueSumDouble
										/ compareIndexNumSumLong - indexValueSumDouble
										/ indexNumSumLong) >= mosDecline) {
							gridMap.put("color", compareGridBadColor);
						}
						// 其他情况
						if (null == gridMap.get("color")) {
							gridMap.put("color", compareGridOtherColor);
						}
						break;
					case 1:
						// 对比日志RSRP均值低于“RSRP均值恶化门限”，且原始日志RSRP均值-对比日志RSRP均值大于“RSRP差值恶化门限”,栅格恶化
						if ((compareIndexValueSumDouble
								/ compareIndexNumSumLong <= rsrpAvgVorsen)
								&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
										/ compareIndexNumSumLong) >= rsrpDiffVorsen) {
							gridMap.put("color", compareGridBaddestColor);
						}
						// 对比日志RSRP均值低于“RSRP均值稍降门限”，且原始日志RSRP均值-对比日志RSRP均值大于“RSRP差值稍降门限”,栅格稍降
						if (null == gridMap.get("color")
								&& (compareIndexValueSumDouble / compareIndexNumSumLong) <= rsrpAvgDecline
								&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
										/ compareIndexNumSumLong) >= rsrpDiffDecline) {
							gridMap.put("color", compareGridBadColor);
						}
						// 其他情况
						if (null == gridMap.get("color")) {
							gridMap.put("color", compareGridOtherColor);
						}
						break;
					case 2:
						// 对比日志SINR均值低于“SINR均值恶化门限”，且原始日志SINR均值-对比日志SINR均值大于“SINR差值恶化门限”,栅格恶化
						if ((compareIndexValueSumDouble / compareIndexNumSumLong) <= sinrAvgVorsen
								&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
										/ compareIndexNumSumLong) >= sinrDiffVorsen) {
							gridMap.put("color", compareGridBaddestColor);
						}
						// 对比日志SINR均值低于“SINR均值稍降门限”，且原始日志SINR均值-对比日志SINR均值大于“SINR差值稍降门限”,栅格稍降
						if (null == gridMap.get("color")
								&& (compareIndexValueSumDouble / compareIndexNumSumLong) <= sinrAvgDecline
								&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
										/ compareIndexNumSumLong) >= sinrDiffDecline) {
							gridMap.put("color", compareGridBadColor);
						}
						// 其他情况
						if (null == gridMap.get("color")) {
							gridMap.put("color", compareGridOtherColor);
						}
						break;
					case 6:
						// 对比日志RTP丢包率均值高于“RTP均值恶化门限”，且原始日志RTP丢包率均值-对比日志RTP丢包率均值大于“RTP丢包率差值恶化门限”,栅格恶化
						if ((compareIndexValueSumDouble / compareIndexNumSumLong) >= rtpAvgVorsen
								&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
										/ compareIndexNumSumLong) >= rtpDiffVorsen) {
							gridMap.put("color", compareGridBaddestColor);
						}

						// 对比日志RTP丢包率均值高于“RTP均值稍降门限”，且原始日志RTP丢包率均值-对比日志RTP丢包率均值大于“RTP丢包率差值稍降门限”,栅格稍降
						if (null == gridMap.get("color")
								&& (compareIndexValueSumDouble / compareIndexNumSumLong) >= rtpAvgDecline
								&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
										/ compareIndexNumSumLong) >= rtpDiffDecline) {
							gridMap.put("color", compareGridBadColor);
						}
						// 其他情况
						if (null == gridMap.get("color")) {
							gridMap.put("color", compareGridOtherColor);
						}
						break;
					default:
						break;
					}

				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.compareAnalysis.ITestLogItemGridService
	 * #doTestLogItemGridAnalysisFG(java.util.List, java.util.List)
	 */
	@Override
	public Map<String, EasyuiPageList> doTestLogItemGridAnalysisFG(
			List<Long> ids, List<Long> compareIds) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("testLogItemGrid0", new EasyuiPageList());
		if (null == ids || 0 == ids.size() || null == compareIds
				|| 0 == compareIds.size()) {
			return map;
		}
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User findByUsername = userService.findByUsername(username);

		List<VolteAnalysisAboutThreshold> selectCompareByUser = volteAnalysisAboutThresholdService
				.selectCompareByUser(findByUsername);
		Map<String, String> volteAnalysisAboutThresholdMap = new HashMap<>();
		for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : selectCompareByUser) {
			volteAnalysisAboutThresholdMap.put(
					volteAnalysisAboutThreshold.getNameEn(),
					volteAnalysisAboutThreshold.getValue());
		}
		// 获取栅格大小
		String gridSizeString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.GRIDSIZE);
		Integer gridSize = 100;
		try {
			gridSize = Integer.valueOf(gridSizeString);
		} catch (NumberFormatException e) {
			// 用户级别栅格大小有错,采用默认栅格大小100米
			// TODO Auto-generated catch block
		}
		// 原始日志(mos,rsrp,sinr,rtp)栅格
		List<TestLogItemGridBean> mosGrid = new ArrayList<>();
		List<TestLogItemGridBean> rsrpGrid = new ArrayList<>();
		List<TestLogItemGridBean> sinrGrid = new ArrayList<>();
		List<TestLogItemGridBean> rtpGrid = new ArrayList<>();
		// 对比日志(mos,rsrp,sinr,rtp)栅格
		List<TestLogItemGridBean> compareMosGrid = new ArrayList<>();
		List<TestLogItemGridBean> compareRsrpGrid = new ArrayList<>();
		List<TestLogItemGridBean> compareSinrGrid = new ArrayList<>();
		List<TestLogItemGridBean> compareRtpGrid = new ArrayList<>();
		switch (gridSize) {
		case 50:
			mosGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids, 0);
			rsrpGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids, 1);
			sinrGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids, 2);
			rtpGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(ids, 6);
			compareMosGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, 0);
			compareRsrpGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, 1);
			compareSinrGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, 2);
			compareRtpGrid = testLogItemGrid50Dao
					.getTestLogItemGrid50ByLogIdsAndIndexTypeGroupby(
							compareIds, 6);
			break;
		case 100:
			mosGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 0);
			rsrpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 1);
			sinrGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 2);
			rtpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 6);
			compareMosGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 0);
			compareRsrpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 1);
			compareSinrGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 2);
			compareRtpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 6);
			break;
		case 500:
			mosGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids, 0);
			rsrpGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids, 1);
			sinrGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids, 2);
			rtpGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(ids, 6);
			compareMosGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, 0);
			compareRsrpGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, 1);
			compareSinrGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, 2);
			compareRtpGrid = testLogItemGrid500Dao
					.getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
							compareIds, 6);
			break;

		default:
			mosGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 0);
			rsrpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 1);
			sinrGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 2);
			rtpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(ids, 6);
			compareMosGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 0);
			compareRsrpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 1);
			compareSinrGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 2);
			compareRtpGrid = testLogItemGrid100Dao
					.getTestLogItemGrid100ByLogIdsAndIndexTypeGroupby(
							compareIds, 6);
			break;
		}

		// 存储栅格指标变化情况
		EasyuiPageList testLogItemGrid0 = new EasyuiPageList();
		List testLogItemGrid0Rows = testLogItemGrid0.getRows();
		map.put("testLogItemGrid0", testLogItemGrid0);

		// 计算mos栅格指标变化情况
		Map<String, Object> mosResultMap = new HashMap<>();
		mosResultMap.put("indexType", "MOS");
		// 去除MOS指标
		// testLogItemGrid0Rows.add(0, mosResultMap);
		// 获取MOS值稍降门限
		String mosDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.MOS_DECLINE);
		Double mosDecline = 0.4d;
		try {
			mosDecline = Double.valueOf(mosDeclineString);
		} catch (NumberFormatException e) {
			// 用户级别MOS值稍降门限有错,采用默认MOS值稍降门限0.4
			// TODO Auto-generated catch block
		}
		Double mosValueSum = null;
		Long mosNumSum = null;
		Double compareMosValueSum = null;
		Long compareMosNumSum = null;
		Integer mosBetterGridNum = null;
		Integer mosBadGridNum = null;
		Integer mosGridNum = null;
		int mosIndex = 0;
		for (TestLogItemGridBean testLogItemGridBean : mosGrid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			mosValueSum = null == mosValueSum ? indexValueSumDouble
					: mosValueSum + indexValueSumDouble;
			mosNumSum = null == mosNumSum ? indexNumSumLong : mosNumSum
					+ indexNumSumLong;
			for (TestLogItemGridBean compareTestLogItemGridBean : compareMosGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (0 == mosIndex) {
					compareMosValueSum = null == compareMosValueSum ? compareIndexValueSumDouble
							: compareMosValueSum + compareIndexValueSumDouble;
					compareMosNumSum = null == compareMosNumSum ? compareIndexNumSumLong
							: compareMosNumSum + compareIndexNumSumLong;
				}
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					// 原始日志和对比日志都具备该栅格,则计算栅格指标变化趋势,并纳入计数
					mosGridNum = null == mosGridNum ? 1 : mosGridNum + 1;
					// 对比日志-原始日志>=MOS值稍降门限,指标提升
					if ((compareIndexValueSumDouble / compareIndexNumSumLong - indexValueSumDouble
							/ indexNumSumLong) >= mosDecline) {
						mosBetterGridNum = null == mosBetterGridNum ? 1
								: mosBetterGridNum + 1;
					}
					// 原始日志-对比日志>=MOS值稍降门限,指标恶化
					if ((indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
							/ compareIndexNumSumLong) >= mosDecline) {
						mosBadGridNum = null == mosBadGridNum ? 1
								: mosBadGridNum + 1;
					}
				}
			}
			mosIndex++;
		}
		if (null != mosValueSum && null != mosNumSum && 0 != mosNumSum) {
			mosResultMap.put("valueAvg", mosValueSum / mosNumSum);
		}
		if (null != compareMosValueSum && null != compareMosNumSum
				&& 0 != compareMosNumSum) {
			mosResultMap.put("compareValueAvg", compareMosValueSum
					/ compareMosNumSum);
		}
		if (null != mosGridNum) {
			mosResultMap.put("totalNum", mosGridNum);
		}
		if (null != mosBetterGridNum) {
			mosResultMap.put("betterNum", mosBetterGridNum);
		}
		if (null != mosGridNum && 0 != mosGridNum && null != mosBetterGridNum) {
			mosResultMap.put(
					"betterNumRatio",
					NumberFormatUtils.format((float) mosBetterGridNum * 100
							/ mosGridNum, 2));
		} else {
			mosResultMap.put("betterNumRatio", "-");
		}
		if (null != mosBadGridNum) {
			mosResultMap.put("badNum", mosBadGridNum);
		}
		if (null != mosGridNum && 0 != mosGridNum && null != mosBadGridNum) {
			mosResultMap.put(
					"badNumRatio",
					NumberFormatUtils.format((float) mosBadGridNum * 100
							/ mosGridNum, 2));
		} else {
			mosResultMap.put("badNumRatio", "-");
		}

		// 计算rsrp栅格指标变化情况
		Map<String, Object> rsrpResultMap = new HashMap<>();
		rsrpResultMap.put("indexType", "RSRP");
		// 去除MOS指标
		// testLogItemGrid0Rows.add(1, rsrpResultMap);
		testLogItemGrid0Rows.add(0, rsrpResultMap);
		// 获取rsrp均值稍降门限
		String rsrpAvgDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RSRP_AVAREGE_DECLINE);
		// 获取rsrp差值稍降门限
		String rsrpDiffDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RSRP_DIFFERENCE_DECLINE);
		Double rsrpAvgDecline = -95d;
		Double rsrpDiffDecline = 5d;
		try {
			rsrpAvgDecline = Double.valueOf(rsrpAvgDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			rsrpDiffDecline = Double.valueOf(rsrpDiffDeclineString);
		} catch (NumberFormatException e) {
		}
		Double rsrpValueSum = null;
		Long rsrpNumSum = null;
		Double compareRsrpValueSum = null;
		Long compareRsrpNumSum = null;
		Integer rsrpBetterGridNum = null;
		Integer rsrpBadGridNum = null;
		Integer rsrpGridNum = null;
		int rsrpIndex = 0;
		for (TestLogItemGridBean testLogItemGridBean : rsrpGrid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			rsrpValueSum = null == rsrpValueSum ? indexValueSumDouble
					: rsrpValueSum + indexValueSumDouble;
			rsrpNumSum = null == rsrpNumSum ? indexNumSumLong : rsrpNumSum
					+ indexNumSumLong;
			for (TestLogItemGridBean compareTestLogItemGridBean : compareRsrpGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (0 == rsrpIndex) {
					compareRsrpValueSum = null == compareRsrpValueSum ? compareIndexValueSumDouble
							: compareRsrpValueSum + compareIndexValueSumDouble;
					compareRsrpNumSum = null == compareRsrpNumSum ? compareIndexNumSumLong
							: compareRsrpNumSum + compareIndexNumSumLong;
				}
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					// 原始日志和对比日志都具备该栅格,则计算栅格指标变化趋势,并纳入计数
					rsrpGridNum = null == rsrpGridNum ? 1 : rsrpGridNum + 1;

					// 原始日志RSRP均值低于“RSRP均值稍降门限”,且对比日志RSRP均值-原始日志RSRP均值>=“RSRP差值稍降门限”,指标提升
					if ((indexValueSumDouble / indexNumSumLong <= rsrpAvgDecline)
							&& (compareIndexValueSumDouble
									/ compareIndexNumSumLong - indexValueSumDouble
									/ indexNumSumLong) >= rsrpDiffDecline) {
						rsrpBetterGridNum = null == rsrpBetterGridNum ? 1
								: rsrpBetterGridNum + 1;
					}
					// 对比日志RSRP均值低于“RSRP均值稍降门限”，且原始日志RSRP均值-对比日志RSRP均值>=“RSRP差值稍降门限”,指标恶化
					if ((compareIndexValueSumDouble / compareIndexNumSumLong) <= rsrpAvgDecline
							&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
									/ compareIndexNumSumLong) >= rsrpDiffDecline) {
						rsrpBadGridNum = null == rsrpBadGridNum ? 1
								: rsrpBadGridNum + 1;
					}
				}
			}
			rsrpIndex++;
		}
		if (null != rsrpValueSum && null != rsrpNumSum && 0 != rsrpNumSum) {
			rsrpResultMap.put("valueAvg", rsrpValueSum / rsrpNumSum);
		}
		if (null != compareRsrpValueSum && null != compareRsrpNumSum
				&& 0 != compareRsrpNumSum) {
			rsrpResultMap.put("compareValueAvg", compareRsrpValueSum
					/ compareRsrpNumSum);
		}
		if (null != rsrpGridNum) {
			rsrpResultMap.put("totalNum", rsrpGridNum);
		}
		if (null != rsrpBetterGridNum) {
			rsrpResultMap.put("betterNum", rsrpBetterGridNum);
		}
		if (null != rsrpGridNum && 0 != rsrpGridNum
				&& null != rsrpBetterGridNum) {
			rsrpResultMap.put(
					"betterNumRatio",
					NumberFormatUtils.format((float) rsrpBetterGridNum * 100
							/ rsrpGridNum, 2));
		} else {
			rsrpResultMap.put("betterNumRatio", "-");
		}
		if (null != rsrpBadGridNum) {
			rsrpResultMap.put("badNum", rsrpBadGridNum);
		}
		if (null != rsrpGridNum && 0 != rsrpGridNum && null != rsrpBadGridNum) {
			rsrpResultMap.put(
					"badNumRatio",
					NumberFormatUtils.format((float) rsrpBadGridNum * 100
							/ rsrpGridNum, 2));
		} else {
			rsrpResultMap.put("badNumRatio", "-");
		}

		// 计算sinr栅格指标变化情况
		Map<String, Object> sinrResultMap = new HashMap<>();
		sinrResultMap.put("indexType", "SINR");
		// 去除MOS指标
		// testLogItemGrid0Rows.add(2, sinrResultMap);
		testLogItemGrid0Rows.add(1, sinrResultMap);
		// 获取sinr均值稍降门限
		String sinrAvgDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.SINR_AVAREGE_DECLINE);
		// 获取sinr差值稍降门限
		String sinrDiffDeclineString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.SINR_DIFFERENCE_DECLINE);
		Double sinrAvgDecline = 9d;
		Double sinrDiffDecline = 2d;
		try {
			sinrAvgDecline = Double.valueOf(sinrAvgDeclineString);
		} catch (NumberFormatException e) {
		}
		try {
			sinrDiffDecline = Double.valueOf(sinrDiffDeclineString);
		} catch (NumberFormatException e) {
		}
		Double sinrValueSum = null;
		Long sinrNumSum = null;
		Double compareSinrValueSum = null;
		Long compareSinrNumSum = null;
		Integer sinrBetterGridNum = null;
		Integer sinrBadGridNum = null;
		Integer sinrGridNum = null;
		int sinrIndex = 0;
		for (TestLogItemGridBean testLogItemGridBean : sinrGrid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			sinrValueSum = null == sinrValueSum ? indexValueSumDouble
					: sinrValueSum + indexValueSumDouble;
			sinrNumSum = null == sinrNumSum ? indexNumSumLong : sinrNumSum
					+ indexNumSumLong;
			for (TestLogItemGridBean compareTestLogItemGridBean : compareSinrGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (0 == sinrIndex) {
					compareSinrValueSum = null == compareSinrValueSum ? compareIndexValueSumDouble
							: compareSinrValueSum + compareIndexValueSumDouble;
					compareSinrNumSum = null == compareSinrNumSum ? compareIndexNumSumLong
							: compareSinrNumSum + compareIndexNumSumLong;
				}
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					// 原始日志和对比日志都具备该栅格,则计算栅格指标变化趋势,并纳入计数
					sinrGridNum = null == sinrGridNum ? 1 : sinrGridNum + 1;

					// 原始日志sinr均值低于“sinr均值稍降门限”,且对比日志sinr均值-原始日志sinr均值>=“sinr差值稍降门限”,指标提升
					if ((indexValueSumDouble / indexNumSumLong <= sinrAvgDecline)
							&& (compareIndexValueSumDouble
									/ compareIndexNumSumLong - indexValueSumDouble
									/ indexNumSumLong) >= sinrDiffDecline) {
						sinrBetterGridNum = null == sinrBetterGridNum ? 1
								: sinrBetterGridNum + 1;
					}
					// 对比日志sinr均值低于“sinr均值稍降门限”，且原始日志sinr均值-对比日志sinr均值>=“sinr差值稍降门限”,指标恶化
					if ((compareIndexValueSumDouble / compareIndexNumSumLong) <= sinrAvgDecline
							&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
									/ compareIndexNumSumLong) >= sinrDiffDecline) {
						sinrBadGridNum = null == sinrBadGridNum ? 1
								: sinrBadGridNum + 1;
					}
				}
			}
			sinrIndex++;
		}
		if (null != sinrValueSum && null != sinrNumSum && 0 != sinrNumSum) {
			sinrResultMap.put("valueAvg", sinrValueSum / sinrNumSum);
		}
		if (null != compareSinrValueSum && null != compareSinrNumSum
				&& 0 != compareSinrNumSum) {
			sinrResultMap.put("compareValueAvg", compareSinrValueSum
					/ compareSinrNumSum);
		}
		if (null != sinrGridNum) {
			sinrResultMap.put("totalNum", sinrGridNum);
		}
		if (null != sinrBetterGridNum) {
			sinrResultMap.put("betterNum", sinrBetterGridNum);
		}
		if (null != sinrGridNum && 0 != sinrGridNum
				&& null != sinrBetterGridNum) {
			sinrResultMap.put(
					"betterNumRatio",
					NumberFormatUtils.format((float) sinrBetterGridNum * 100
							/ sinrGridNum, 2));
		} else {
			sinrResultMap.put("betterNumRatio", "-");
		}
		if (null != sinrBadGridNum) {
			sinrResultMap.put("badNum", sinrBadGridNum);
		}
		if (null != sinrGridNum && 0 != sinrGridNum && null != sinrBadGridNum) {
			sinrResultMap.put(
					"badNumRatio",
					NumberFormatUtils.format((float) sinrBadGridNum * 100
							/ sinrGridNum, 2));
		} else {
			sinrResultMap.put("badNumRatio", "-");
		}

		// 计算rtp栅格指标变化情况
		Map<String, Object> rtpResultMap = new HashMap<>();
		// 改为PUSCCH发射功率
		// rtpResultMap.put("indexType", "RTP丢包率");
		rtpResultMap.put("indexType", "PUSCCH发射功率");
		// 去除MOS指标
		// testLogItemGrid0Rows.add(3, rtpResultMap);
		testLogItemGrid0Rows.add(2, rtpResultMap);
		// 获取rtp均值恶化门限
		String rtpAvgVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_AVAREGE_VORSEN);
		// 获取rtp差值恶化门限
		String rtpDiffVorsenString = volteAnalysisAboutThresholdMap
				.get(VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_DIFFERENCE_VORSEN);
		Double rtpAvgVorsen = 30d;
		Double rtpDiffVorsen = 30d;
		try {
			rtpAvgVorsen = Double.valueOf(rtpAvgVorsenString);
		} catch (NumberFormatException e) {
		}
		try {
			rtpDiffVorsen = Double.valueOf(rtpDiffVorsenString);
		} catch (NumberFormatException e) {
		}
		Double rtpValueSum = null;
		Long rtpNumSum = null;
		Double comparertpValueSum = null;
		Long comparertpNumSum = null;
		Integer rtpBetterGridNum = null;
		Integer rtpBadGridNum = null;
		Integer rtpGridNum = null;
		int rtpIndex = 0;
		for (TestLogItemGridBean testLogItemGridBean : rtpGrid) {
			Double indexValueSumDouble = testLogItemGridBean
					.getIndexValueSumDouble();
			Long indexNumSumLong = testLogItemGridBean.getIndexNumSumLong();
			rtpValueSum = null == rtpValueSum ? indexValueSumDouble
					: rtpValueSum + indexValueSumDouble;
			rtpNumSum = null == rtpNumSum ? indexNumSumLong : rtpNumSum
					+ indexNumSumLong;
			for (TestLogItemGridBean compareTestLogItemGridBean : compareRtpGrid) {
				Double compareIndexValueSumDouble = compareTestLogItemGridBean
						.getIndexValueSumDouble();
				Long compareIndexNumSumLong = compareTestLogItemGridBean
						.getIndexNumSumLong();
				if (0 == rtpIndex) {
					comparertpValueSum = null == comparertpValueSum ? compareIndexValueSumDouble
							: comparertpValueSum + compareIndexValueSumDouble;
					comparertpNumSum = null == comparertpNumSum ? compareIndexNumSumLong
							: comparertpNumSum + compareIndexNumSumLong;
				}
				if (compareTestLogItemGridBean.getObjectId().equals(
						testLogItemGridBean.getObjectId())
						&& compareTestLogItemGridBean.getMinx().equals(
								testLogItemGridBean.getMinx())
						&& compareTestLogItemGridBean.getMiny().equals(
								testLogItemGridBean.getMiny())
						&& compareTestLogItemGridBean.getMaxx().equals(
								testLogItemGridBean.getMaxx())
						&& compareTestLogItemGridBean.getMaxy().equals(
								testLogItemGridBean.getMaxy())) {
					// 原始日志和对比日志都具备该栅格,则计算栅格指标变化趋势,并纳入计数
					rtpGridNum = null == rtpGridNum ? 1 : rtpGridNum + 1;

					// 原始日志RTP丢包率均值高于“RTP均值恶化门限”，且对比日志RTP丢包率均值-原始日志RTP丢包率均值大于“RTP丢包率差值恶化门限”,指标提升
					if ((indexValueSumDouble / indexNumSumLong >= rtpAvgVorsen)
							&& (compareIndexValueSumDouble
									/ compareIndexNumSumLong - indexValueSumDouble
									/ indexNumSumLong) >= rtpDiffVorsen) {
						rtpBetterGridNum = null == rtpBetterGridNum ? 1
								: rtpBetterGridNum + 1;
					}
					// 对比日志RTP丢包率均值高于“RTP均值恶化门限”，且原始日志RTP丢包率均值-对比日志RTP丢包率均值大于“RTP丢包率差值恶化门限”,指标恶化
					if ((compareIndexValueSumDouble / compareIndexNumSumLong) >= rtpAvgVorsen
							&& (indexValueSumDouble / indexNumSumLong - compareIndexValueSumDouble
									/ compareIndexNumSumLong) >= rtpDiffVorsen) {
						rtpBadGridNum = null == rtpBadGridNum ? 1
								: rtpBadGridNum + 1;
					}
				}
			}
			rtpIndex++;
		}
		if (null != rtpValueSum && null != rtpNumSum && 0 != rtpNumSum) {
			rtpResultMap.put("valueAvg", rtpValueSum / rtpNumSum);
		}
		if (null != comparertpValueSum && null != comparertpNumSum
				&& 0 != comparertpNumSum) {
			rtpResultMap.put("compareValueAvg", comparertpValueSum
					/ comparertpNumSum);
		}
		if (null != rtpGridNum) {
			rtpResultMap.put("totalNum", rtpGridNum);
		}
		if (null != rtpBetterGridNum) {
			rtpResultMap.put("betterNum", rtpBetterGridNum);
		}
		if (null != rtpGridNum && 0 != rtpGridNum && null != rtpBetterGridNum) {
			rtpResultMap.put(
					"betterNumRatio",
					NumberFormatUtils.format((float) rtpBetterGridNum * 100
							/ rtpGridNum, 2));
		} else {
			rtpResultMap.put("betterNumRatio", "-");
		}
		if (null != rtpBadGridNum) {
			rtpResultMap.put("badNum", rtpBadGridNum);
		}
		if (null != rtpGridNum && 0 != rtpGridNum && null != rtpBadGridNum) {
			rtpResultMap.put(
					"badNumRatio",
					NumberFormatUtils.format((float) rtpBadGridNum * 100
							/ rtpGridNum, 2));
		} else {
			rtpResultMap.put("badNumRatio", "-");
		}

		return map;
	}
}
