/**
 * 
 */
package com.datang.service.service5g.embbCover.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.util.CollectionUtils;
import com.datang.dao.dao5g.embbCover.EmabbCoverCellInfoDao;
import com.datang.dao.dao5g.embbCover.EmbbCoverBadRoadDao;
import com.datang.dao.dao5g.embbCover.EmbbCoverCellBeamInfoDao;
import com.datang.dao.dao5g.embbCover.EmbbCoverOptimizeAdviceCommonDao;
import com.datang.domain.domain5g.embbCover.EmbbCoverAddNbCell;
import com.datang.domain.domain5g.embbCover.EmbbCoverAddStation;
import com.datang.domain.domain5g.embbCover.EmbbCoverAntennaWeightError;
import com.datang.domain.domain5g.embbCover.EmbbCoverAntennaWeightOptimize;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;
import com.datang.domain.domain5g.embbCover.EmbbCoverCellInfo;
import com.datang.domain.domain5g.embbCover.EmbbCoverDowntiltAdjust;
import com.datang.domain.domain5g.embbCover.EmbbCoverHeightErrorCell;
import com.datang.domain.domain5g.embbCover.EmbbCoverNbCellAzimuthAdjust;
import com.datang.domain.domain5g.embbCover.EmbbCoverPowerAdjust;
import com.datang.domain.domain5g.embbCover.EmbbCoverSceneRetest;
import com.datang.domain.domain5g.embbCover.EmbbCoverTianKuiAdjust;
import com.datang.domain.domain5g.embbCover.EmbbCoverTianKuiConnectReverse;
import com.datang.domain.domain5g.embbCover.EmbbCoverWaitCheckCell;
import com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService;
import com.datang.util.NumberFormatUtils;

/**
 * 5G专题----EMBB覆盖专题service接口实现
 * 
 * @author _YZP
 * 
 */
@Service
@Transactional
public class EmbbCoverBadRoadServiceImpl implements IEmbbCoverBadRoadService {
	@Autowired
	private EmbbCoverBadRoadDao embbCoverBadRoadDao;
	@Autowired
	private EmabbCoverCellInfoDao embbCoverCellInfoDao;
	@Autowired
	private EmbbCoverOptimizeAdviceCommonDao adviceCommonDao;
	@Autowired
	private EmbbCoverCellBeamInfoDao embbCoverCellBeamInfoDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * getEmbbCoverBadRoad(java.lang.Long)
	 */
	@Override
	public EmbbCoverBadRoad getEmbbCoverBadRoad(Long ecbrId) {
		return embbCoverBadRoadDao.find(ecbrId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * getEmbbCoverBadRoadByLogIds(java.util.List)
	 */
	@Override
	public List<EmbbCoverBadRoad> getEmbbCoverBadRoadByLogIds(
			List<Long> testLogItemIds) {
		List<EmbbCoverBadRoad> queryAllEmbbCoverRoad = embbCoverBadRoadDao
				.queryAllEmbbCoverRoad(testLogItemIds);
		return queryAllEmbbCoverRoad;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * getEmbbCoverBadRoadByLogIds(java.util.List, int)
	 */
	@Override
	public List<EmbbCoverBadRoad> getEmbbCoverBadRoadByLogIds(
			List<Long> testLogItemIds, int coverTypeNum) {
		List<EmbbCoverBadRoad> queryAllEmbbCoverRoad = embbCoverBadRoadDao
				.queryAllEmbbCoverRoad(testLogItemIds, coverTypeNum);
		return queryAllEmbbCoverRoad;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * doWholeAnalysis(java.util.List)
	 */
	@Override
	public Map<String, Map<String, Object>> doWholeAnalysis(List<Long> ids) {
		Map<String, Map<String, Object>> resultMap = new HashMap<>();

		Map<String, Object> roadNumMap = new HashMap<>();
		resultMap.put("roadNumMap", roadNumMap);

		Map<String, Object> sinrAvgMap = new HashMap<>();
		resultMap.put("sinrAvgMap", sinrAvgMap);

		Map<String, Object> ftpUpSpeedMap = new HashMap<>();
		resultMap.put("ftpUpSpeedMap", ftpUpSpeedMap);

		Map<String, Object> ftpDlSpeedMap = new HashMap<>();
		resultMap.put("ftpDlSpeedMap", ftpDlSpeedMap);

		Map<String, Object> distanceMap = new HashMap<>();
		resultMap.put("distanceMap", distanceMap);

		Map<String, Object> distanceMaxMap = new HashMap<>();
		resultMap.put("distanceMaxMap", distanceMaxMap);

		Map<String, Object> distanceMinMap = new HashMap<>();
		resultMap.put("distanceMinMap", distanceMinMap);

		Map<String, Object> distanceAvgMap = new HashMap<>();
		resultMap.put("distanceAvgMap", distanceAvgMap);

		Map<String, Object> timeMap = new HashMap<>();
		resultMap.put("timeMap", timeMap);

		if (!CollectionUtils.isEmpty(ids)) {

			// 路段总数
			int weakCoverNum = 0;
			int overCoverNum = 0;
			int overlappingCoverNum = 0;
			int weakAndOverCoverNum = 0;
			int weakAndOverlappingCoverNum = 0;
			int overAndOverlappingCoverNum = 0;
			int weakAndOverAndOverlappingCoverNum = 0;

			// sinr均值
			float weakCoverSinrAvg = 0.0f;
			float overCoverSinrAvg = 0.0f;
			float overlappingCoverAvg = 0.0f;

			// ftp上传速度
			float weakCoverFtpUpSpeed = 0.0f;
			float overCoverFtpUpSpeed = 0.0f;
			float overlappingCoverFtpUpSpeed = 0.0f;

			// ftp下载速度
			float weakCoverFtpDlSpeed = 0.0f;
			float overCoverFtpDlSpeed = 0.0f;
			float overlappingCoverFtpDlSpeed = 0.0f;

			// 持续距离(距离总和)
			float weakCoverDistance = 0.0f;
			float overCoverDistance = 0.0f;
			float overlappingCoverDistance = 0.0f;
			float weakAndOverCoverDistance = 0.0f;
			float weakAndOverlappingCoverDistance = 0.0f;
			float overAndOverlappingCoverDistance = 0.0f;
			float weakAndOverAndOverlappingCoverDistance = 0.0f;

			// 最长持续距离
			float weakCoverMaxDistance = 0.0f;
			float overCoverMaxDistance = 0.0f;
			float overlappingCoverMaxDistance = 0.0f;
			float weakAndOverCoverMaxDistance = 0.0f;
			float weakAndOverlappingCoverMaxDistance = 0.0f;
			float overAndOverlappingCoverMaxDistance = 0.0f;
			float weakAndOverAndOverlappingCoverMaxDistance = 0.0f;

			// 最短持续距离
			float weakCoverMinDistance = 0.0f;
			float overCoverMinDistance = 0.0f;
			float overlappingCoverMinDistance = 0.0f;
			float weakAndOverCoverMinDistance = 0.0f;
			float weakAndOverlappingCoverMinDistance = 0.0f;
			float overAndOverlappingCoverMinDistance = 0.0f;
			float weakAndOverAndOverlappingCoverMinDistance = 0.0f;

			// 平均距离
			float weakCoverAvgDistance = 0.0f;
			float overCoverAvgDistance = 0.0f;
			float overlappingCoverAvgDistance = 0.0f;
			float weakAndOverCoverAvgDistance = 0.0f;
			float weakAndOverlappingCoverAvgDistance = 0.0f;
			float overAndOverlappingCoverAvgDistance = 0.0f;
			float weakAndOverAndOverlappingCoverAvgDistance = 0.0f;

			long weakCoverTime = 0l;
			long overCoverTime = 0l;
			long overlappingCoverTime = 0l;
			long weakAndOverCoverTime = 0l;
			long weakAndOverlappingCoverTime = 0l;
			long overAndOverlappingCoverTime = 0l;
			long weakAndOverAndOverlappingCoverTime = 0l;

			List<EmbbCoverBadRoad> embbCoverBadRoadByLogIds = getEmbbCoverBadRoadByLogIds(ids);
			if (!CollectionUtils.isEmpty(embbCoverBadRoadByLogIds)) {

				Float weakCoverSinrValueSum = 0.0f;
				Long weakCoverRsrpOrSinrPointNum = 0l;
				Float overCoverSinrValueSum = 0.0f;
				Long overCoverRsrpOrSinrPointNum = 0l;
				Float overlappingCoverSinrValueSum = 0.0f;
				Long overlappingRsrpOrSinrPointNum = 0l;
				Float weakCoverFtpDlSpeedSum = 0.0f;
				Float overCoverFtpDlSpeedSum = 0.0f;
				Float overlappingCoverFtpDlSpeedSum = 0.0f;
				Float weakCoverFtpUpSpeedSum = 0.0f;
				Float overCoverFtpUpSpeedSum = 0.0f;
				Float overlappingCoverFtpUpSpeedSum = 0.0f;
				for (EmbbCoverBadRoad embbCoverBadRoad : embbCoverBadRoadByLogIds) {
					Integer coverTypeNum = embbCoverBadRoad.getCoverTypeNum();
					Float sinrValueSum = embbCoverBadRoad.getSinrValueSum();
					Long rsrpOrSinrPointNum = embbCoverBadRoad
							.getRsrpOrSinrPointNum();
					Float ftpDlSpeed = embbCoverBadRoad.getFtpDlSpeed();
					Float ftpUpSpeed = embbCoverBadRoad.getFtpUpSpeed();
					Float distance = embbCoverBadRoad.getDistance();
					Long continueTime = embbCoverBadRoad.getContinueTime();
					if (null != coverTypeNum) {
						switch (coverTypeNum) {
						case 0:// 弱覆盖
								// 计算路段总数
							weakCoverNum++;
							// 计算SINR均值
							if (null != sinrValueSum) {
								weakCoverSinrValueSum += sinrValueSum;
							}
							if (null != rsrpOrSinrPointNum) {
								weakCoverRsrpOrSinrPointNum += rsrpOrSinrPointNum;
							}
							// 计算FTP速率
							if (null != ftpDlSpeed) {
								weakCoverFtpDlSpeedSum += ftpDlSpeed;
							}
							if (null != ftpUpSpeed) {
								weakCoverFtpUpSpeedSum += ftpUpSpeed;
							}
							// 计算持续距离
							if (null != distance) {
								weakCoverDistance += distance;
								// 计算最长持续距离
								weakCoverMaxDistance = (weakCoverMaxDistance
										- distance > 0) ? weakCoverMaxDistance
										: distance;
								// 计算最短持续距离
								weakCoverMinDistance = (weakCoverMinDistance == 0.0f ? distance
										: ((weakCoverMinDistance - distance < 0) ? weakCoverMinDistance
												: distance));
							}
							// 计算持续时间
							if (null != continueTime) {
								weakCoverTime += continueTime;
							}
							break;
						case 1:// 过覆盖
								// 计算路段总数
							overCoverNum++;
							// 计算SINR均值
							if (null != sinrValueSum) {
								overCoverSinrValueSum += sinrValueSum;
							}
							if (null != rsrpOrSinrPointNum) {
								overCoverRsrpOrSinrPointNum += rsrpOrSinrPointNum;
							}
							// 计算FTP速率
							if (null != ftpDlSpeed) {
								overCoverFtpDlSpeedSum += ftpDlSpeed;
							}
							if (null != ftpUpSpeed) {
								overCoverFtpUpSpeedSum += ftpUpSpeed;
							}
							// 计算持续距离
							if (null != distance) {
								overCoverDistance += distance;
								// 计算最长持续距离
								overCoverMaxDistance = (overCoverMaxDistance
										- distance > 0) ? overCoverMaxDistance
										: distance;
								// 计算最短持续距离
								overCoverMinDistance = (overCoverMinDistance == 0.0f ? distance
										: ((overCoverMinDistance - distance < 0) ? overCoverMinDistance
												: distance));
							}
							// 计算持续时间
							if (null != continueTime) {
								overCoverTime += continueTime;
							}
							break;
						case 2:// 重叠覆盖
								// 计算路段总数
							overlappingCoverNum++;
							// 计算SINR均值
							if (null != sinrValueSum) {
								overlappingCoverSinrValueSum += sinrValueSum;
							}
							if (null != rsrpOrSinrPointNum) {
								overlappingRsrpOrSinrPointNum += rsrpOrSinrPointNum;
							}
							// 计算FTP速率
							if (null != ftpDlSpeed) {
								overlappingCoverFtpDlSpeedSum += ftpDlSpeed;
							}
							if (null != ftpUpSpeed) {
								overlappingCoverFtpUpSpeedSum += ftpUpSpeed;
							}
							// 计算持续距离
							if (null != distance) {
								overlappingCoverDistance += distance;
								// 计算最长持续距离
								overlappingCoverMaxDistance = (overlappingCoverMaxDistance
										- distance > 0) ? overlappingCoverMaxDistance
										: distance;
								// 计算最短持续距离
								overlappingCoverMinDistance = (overlappingCoverMinDistance == 0.0f ? distance
										: ((overlappingCoverMinDistance
												- distance < 0) ? overlappingCoverMinDistance
												: distance));
							}
							// 计算持续时间
							if (null != continueTime) {
								overlappingCoverTime += continueTime;
							}
							break;
						case 3:// 弱覆盖过覆盖
								// 计算路段总数
							weakAndOverCoverNum++;
							// 计算持续距离
							if (null != distance) {
								weakAndOverCoverDistance += distance;
								// 计算最长持续距离
								weakAndOverCoverMaxDistance = (weakAndOverCoverMaxDistance
										- distance > 0) ? weakAndOverCoverMaxDistance
										: distance;
								// 计算最短持续距离
								weakAndOverCoverMinDistance = (weakAndOverCoverMinDistance == 0.0f ? distance
										: ((weakAndOverCoverMinDistance
												- distance < 0) ? weakAndOverCoverMinDistance
												: distance));
							}
							// 计算持续时间
							if (null != continueTime) {
								weakAndOverCoverTime += continueTime;
							}
							break;
						case 4:// 弱覆盖重叠覆盖
								// 计算路段总数
							weakAndOverlappingCoverNum++;
							// 计算持续距离
							if (null != distance) {
								weakAndOverlappingCoverDistance += distance;
								// 计算最长持续距离
								weakAndOverlappingCoverMaxDistance = (weakAndOverlappingCoverMaxDistance
										- distance > 0) ? weakAndOverlappingCoverMaxDistance
										: distance;
								// 计算最短持续距离
								weakAndOverlappingCoverMinDistance = (weakAndOverlappingCoverMinDistance == 0.0f ? distance
										: ((weakAndOverlappingCoverMinDistance
												- distance < 0) ? weakAndOverlappingCoverMinDistance
												: distance));
							}
							// 计算持续时间
							if (null != continueTime) {
								weakAndOverlappingCoverTime += continueTime;
							}
							break;
						case 5:// 过覆盖重叠覆盖
								// 计算路段总数
							overAndOverlappingCoverNum++;
							// 计算持续距离
							if (null != distance) {
								overAndOverlappingCoverDistance += distance;
								// 计算最长持续距离
								overAndOverlappingCoverMaxDistance = (overAndOverlappingCoverMaxDistance
										- distance > 0) ? overAndOverlappingCoverMaxDistance
										: distance;
								// 计算最短持续距离
								overAndOverlappingCoverMinDistance = (overAndOverlappingCoverMinDistance == 0.0f ? distance
										: ((overAndOverlappingCoverMinDistance
												- distance < 0) ? overAndOverlappingCoverMinDistance
												: distance));
							}
							// 计算持续时间
							if (null != continueTime) {
								overAndOverlappingCoverTime += continueTime;
							}
							break;
						case 6: // 弱覆盖过覆盖重叠覆盖
							// 计算路段总数
							weakAndOverAndOverlappingCoverNum++;
							// 计算持续距离
							if (null != distance) {
								weakAndOverAndOverlappingCoverDistance += distance;
								// 计算最长持续距离
								weakAndOverAndOverlappingCoverMaxDistance = (weakAndOverAndOverlappingCoverMaxDistance
										- distance > 0) ? weakAndOverAndOverlappingCoverMaxDistance
										: distance;
								// 计算最短持续距离
								weakAndOverAndOverlappingCoverMinDistance = (weakAndOverAndOverlappingCoverMinDistance == 0.0f ? distance
										: ((weakAndOverAndOverlappingCoverMinDistance
												- distance < 0) ? weakAndOverAndOverlappingCoverMinDistance
												: distance));
							}
							// 计算持续时间
							if (null != continueTime) {
								weakAndOverAndOverlappingCoverTime += continueTime;
							}
							break;
						default:
							break;
						}
					}
				}

				// 计算SINR均值
				weakCoverSinrAvg = (weakCoverRsrpOrSinrPointNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(weakCoverSinrValueSum
								/ weakCoverRsrpOrSinrPointNum, 2));
				overCoverSinrAvg = (overCoverRsrpOrSinrPointNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(overCoverSinrValueSum
								/ overCoverRsrpOrSinrPointNum, 2));
				overlappingCoverAvg = (overlappingRsrpOrSinrPointNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(overlappingCoverSinrValueSum
								/ overlappingRsrpOrSinrPointNum, 2));

				// 计算FTP速率
				weakCoverFtpUpSpeed = (weakCoverNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(weakCoverFtpUpSpeedSum
								/ weakCoverNum, 2));
				overCoverFtpUpSpeed = (overCoverNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(overCoverFtpUpSpeedSum
								/ overCoverNum, 2));
				overlappingCoverFtpUpSpeed = (overlappingCoverNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(
								overlappingCoverFtpUpSpeedSum
										/ overlappingCoverNum, 2));
				weakCoverFtpDlSpeed = (weakCoverNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(weakCoverFtpDlSpeedSum
								/ weakCoverNum, 2));
				overCoverFtpDlSpeed = (overCoverNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(overCoverFtpDlSpeedSum
								/ overCoverNum, 2));
				overlappingCoverFtpDlSpeed = (overlappingCoverNum == 0.0f ? 0.0f
						: NumberFormatUtils.format(
								overlappingCoverFtpDlSpeedSum
										/ overlappingCoverNum, 2));

				// 计算平均持续距离
				weakCoverAvgDistance = (weakCoverNum == 0 ? 0.0f
						: NumberFormatUtils.format(weakCoverDistance
								/ weakCoverNum, 2));
				overCoverAvgDistance = (overCoverNum == 0 ? 0.0f
						: NumberFormatUtils.format(overCoverDistance
								/ overCoverNum, 2));
				overlappingCoverAvgDistance = (overlappingCoverNum == 0 ? 0.0f
						: NumberFormatUtils.format(overlappingCoverDistance
								/ overlappingCoverNum, 2));
				weakAndOverCoverAvgDistance = (weakAndOverCoverNum == 0 ? 0.0f
						: NumberFormatUtils.format(weakAndOverCoverDistance
								/ weakAndOverCoverNum, 2));
				weakAndOverlappingCoverAvgDistance = (weakAndOverlappingCoverNum == 0 ? 0.0f
						: NumberFormatUtils.format(
								weakAndOverlappingCoverDistance
										/ weakAndOverlappingCoverNum, 2));
				overAndOverlappingCoverAvgDistance = (overAndOverlappingCoverNum == 0 ? 0.0f
						: NumberFormatUtils.format(
								overAndOverlappingCoverDistance
										/ overAndOverlappingCoverNum, 2));
				weakAndOverAndOverlappingCoverAvgDistance = (weakAndOverAndOverlappingCoverNum == 0 ? 0.0f
						: NumberFormatUtils.format(
								weakAndOverAndOverlappingCoverDistance
										/ weakAndOverAndOverlappingCoverNum, 2));
			}

			roadNumMap.put("weakCoverNum", weakCoverNum);
			roadNumMap.put("overCoverNum", overCoverNum);
			roadNumMap.put("overlappingCoverNum", overlappingCoverNum);
			roadNumMap.put("weakAndOverCoverNum", weakAndOverCoverNum);
			roadNumMap.put("weakAndOverlappingCoverNum",
					weakAndOverlappingCoverNum);
			roadNumMap.put("overAndOverlappingCoverNum",
					overAndOverlappingCoverNum);
			roadNumMap.put("weakAndOverAndOverlappingCoverNum",
					weakAndOverAndOverlappingCoverNum);

			sinrAvgMap.put("weakCoverSinrAvg", weakCoverSinrAvg);
			sinrAvgMap.put("overCoverSinrAvg", overCoverSinrAvg);
			sinrAvgMap.put("overlappingCoverAvg", overlappingCoverAvg);

			ftpUpSpeedMap.put("weakCoverFtpUpSpeed", weakCoverFtpUpSpeed);
			ftpUpSpeedMap.put("overCoverFtpUpSpeed", overCoverFtpUpSpeed);
			ftpUpSpeedMap.put("overlappingCoverFtpUpSpeed",
					overlappingCoverFtpUpSpeed);

			ftpDlSpeedMap.put("weakCoverFtpDlSpeed", weakCoverFtpDlSpeed);
			ftpDlSpeedMap.put("overCoverFtpDlSpeed", overCoverFtpDlSpeed);
			ftpDlSpeedMap.put("overlappingCoverFtpDlSpeed",
					overlappingCoverFtpDlSpeed);

			distanceMap.put("weakCoverDistance", weakCoverDistance);
			distanceMap.put("overCoverDistance", overCoverDistance);
			distanceMap.put("overlappingCoverDistance",
					overlappingCoverDistance);
			distanceMap.put("weakAndOverCoverDistance",
					weakAndOverCoverDistance);
			distanceMap.put("weakAndOverlappingCoverDistance",
					weakAndOverlappingCoverDistance);
			distanceMap.put("overAndOverlappingCoverDistance",
					overAndOverlappingCoverDistance);
			distanceMap.put("weakAndOverAndOverlappingCoverDistance",
					weakAndOverAndOverlappingCoverDistance);

			distanceMaxMap.put("weakCoverMaxDistance", weakCoverMaxDistance);
			distanceMaxMap.put("overCoverMaxDistance", overCoverMaxDistance);
			distanceMaxMap.put("overlappingCoverMaxDistance",
					overlappingCoverMaxDistance);
			distanceMaxMap.put("weakAndOverCoverMaxDistance",
					weakAndOverCoverMaxDistance);
			distanceMaxMap.put("weakAndOverlappingCoverMaxDistance",
					weakAndOverlappingCoverMaxDistance);
			distanceMaxMap.put("overAndOverlappingCoverMaxDistance",
					overAndOverlappingCoverMaxDistance);
			distanceMaxMap.put("weakAndOverAndOverlappingCoverMaxDistance",
					weakAndOverAndOverlappingCoverMaxDistance);

			distanceMinMap.put("weakCoverMinDistance", weakCoverMinDistance);
			distanceMinMap.put("overCoverMinDistance", overCoverMinDistance);
			distanceMinMap.put("overlappingCoverMinDistance",
					overlappingCoverMinDistance);
			distanceMinMap.put("weakAndOverCoverMinDistance",
					weakAndOverCoverMinDistance);
			distanceMinMap.put("weakAndOverlappingCoverMinDistance",
					weakAndOverlappingCoverMinDistance);
			distanceMinMap.put("overAndOverlappingCoverMinDistance",
					overAndOverlappingCoverMinDistance);
			distanceMinMap.put("weakAndOverAndOverlappingCoverMinDistance",
					weakAndOverAndOverlappingCoverMinDistance);

			distanceAvgMap.put("weakCoverAvgDistance", weakCoverAvgDistance);
			distanceAvgMap.put("overCoverAvgDistance", overCoverAvgDistance);
			distanceAvgMap.put("overlappingCoverAvgDistance",
					overlappingCoverAvgDistance);
			distanceAvgMap.put("weakAndOverCoverAvgDistance",
					weakAndOverCoverAvgDistance);
			distanceAvgMap.put("weakAndOverlappingCoverAvgDistance",
					weakAndOverlappingCoverAvgDistance);
			distanceAvgMap.put("overAndOverlappingCoverAvgDistance",
					overAndOverlappingCoverAvgDistance);
			distanceAvgMap.put("weakAndOverAndOverlappingCoverAvgDistance",
					weakAndOverAndOverlappingCoverAvgDistance);

			timeMap.put("weakCoverTime",
					NumberFormatUtils.format(weakCoverTime / 60000.0f, 2));
			timeMap.put("overCoverTime",
					NumberFormatUtils.format(overCoverTime / 60000.0f, 2));
			timeMap.put("overlappingCoverTime", NumberFormatUtils.format(
					overlappingCoverTime / 60000.0f, 2));
			timeMap.put("weakAndOverCoverTime", NumberFormatUtils.format(
					weakAndOverCoverTime / 60000.0f, 2));
			timeMap.put("weakAndOverlappingCoverTime", NumberFormatUtils
					.format(weakAndOverlappingCoverTime / 60000.0f, 2));
			timeMap.put("overAndOverlappingCoverTime", NumberFormatUtils
					.format(overAndOverlappingCoverTime / 60000.0f, 2));
			timeMap.put("weakAndOverAndOverlappingCoverTime", NumberFormatUtils
					.format(weakAndOverAndOverlappingCoverTime / 60000.0f, 2));

		}
		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * doEmbbCoverTotalIndexAnalysis(java.util.List)
	 */
	@Override
	public Map<String, Object> doEmbbCoverTotalIndexAnalysis(
			List<EmbbCoverBadRoad> embbCoverBadRoadByLogIds) {
		Map<String, Object> resultMap = new HashMap<>();
		// 原因占比结果
		List<Map<String, Integer>> reasonRateNumList = new ArrayList<>();
		Map<String, Integer> reasonRate0 = new HashMap<>();
		Map<String, Integer> reasonRate1 = new HashMap<>();
		Map<String, Integer> reasonRate2 = new HashMap<>();
		Map<String, Integer> reasonRate3 = new HashMap<>();
		Map<String, Integer> reasonRate4 = new HashMap<>();
		Map<String, Integer> reasonRate5 = new HashMap<>();
		Map<String, Integer> reasonRate6 = new HashMap<>();
		Map<String, Integer> reasonRate7 = new HashMap<>();
		Map<String, Integer> reasonRate8 = new HashMap<>();
		Map<String, Integer> reasonRate9 = new HashMap<>();
		Map<String, Integer> reasonRate10 = new HashMap<>();
		Map<String, Integer> reasonRate11 = new HashMap<>();
		reasonRate0.put("rate0t6", 0);
		reasonRate0.put("rate6t8", 0);
		reasonRate0.put("rate8t100", 0);
		reasonRate0.put("total", 0);
		reasonRate1.put("rate0t6", 0);
		reasonRate1.put("rate6t8", 0);
		reasonRate1.put("rate8t100", 0);
		reasonRate1.put("total", 0);
		reasonRate2.put("rate0t6", 0);
		reasonRate2.put("rate6t8", 0);
		reasonRate2.put("rate8t100", 0);
		reasonRate2.put("total", 0);
		reasonRate3.put("rate0t6", 0);
		reasonRate3.put("rate6t8", 0);
		reasonRate3.put("rate8t100", 0);
		reasonRate3.put("total", 0);
		reasonRate4.put("rate0t6", 0);
		reasonRate4.put("rate6t8", 0);
		reasonRate4.put("rate8t100", 0);
		reasonRate4.put("total", 0);
		reasonRate5.put("rate0t6", 0);
		reasonRate5.put("rate6t8", 0);
		reasonRate5.put("rate8t100", 0);
		reasonRate5.put("total", 0);
		reasonRate6.put("rate0t6", 0);
		reasonRate6.put("rate6t8", 0);
		reasonRate6.put("rate8t100", 0);
		reasonRate6.put("total", 0);
		reasonRate7.put("rate0t6", 0);
		reasonRate7.put("rate6t8", 0);
		reasonRate7.put("rate8t100", 0);
		reasonRate7.put("total", 0);
		reasonRate8.put("rate0t6", 0);
		reasonRate8.put("rate6t8", 0);
		reasonRate8.put("rate8t100", 0);
		reasonRate8.put("total", 0);
		reasonRate9.put("rate0t6", 0);
		reasonRate9.put("rate6t8", 0);
		reasonRate9.put("rate8t100", 0);
		reasonRate9.put("total", 0);
		reasonRate10.put("rate0t6", 0);
		reasonRate10.put("rate6t8", 0);
		reasonRate10.put("rate8t100", 0);
		reasonRate10.put("total", 0);
		reasonRate11.put("rate0t6", 0);
		reasonRate11.put("rate6t8", 0);
		reasonRate11.put("rate8t100", 0);
		reasonRate11.put("total", 0);
		reasonRateNumList.add(0, reasonRate0);
		reasonRateNumList.add(1, reasonRate1);
		reasonRateNumList.add(2, reasonRate2);
		reasonRateNumList.add(3, reasonRate3);
		reasonRateNumList.add(4, reasonRate4);
		reasonRateNumList.add(5, reasonRate5);
		reasonRateNumList.add(6, reasonRate6);
		reasonRateNumList.add(7, reasonRate7);
		reasonRateNumList.add(8, reasonRate8);
		reasonRateNumList.add(9, reasonRate9);
		reasonRateNumList.add(10, reasonRate10);
		reasonRateNumList.add(11, reasonRate11);

		// 路段总数
		int coverNum = 0;
		// 采样点占比
		float pointRate = 0.0f;

		// 持续距离(距离总和)
		float distance = 0.0f;
		// 最长持续距离
		float maxDistance = 0.0f;
		// 最短持续距离
		float minDistance = 0.0f;
		// 平均持续距离
		float avgDistance = 0.0f;
		// 持续距离占比
		float distanceRate = 0.0f;

		// 总测试时长
		long time = 0l;
		// 最长测试时长
		long maxTime = 0l;
		// 最短测试时长
		long minTime = 0l;
		// 平均测试时长
		float avgTime = 0.0f;
		// 测试时长占比
		float timeRate = 0.0f;

		// sinr
		float sinr = 0.0f;
		// rsrp
		float rsrp = 0.0f;
		// ftp上传速度
		float ftpUpSpeed = 0.0f;
		// ftp下载速度
		float ftpDlSpeed = 0.0f;

		if (!CollectionUtils.isEmpty(embbCoverBadRoadByLogIds)) {

			Float coverPointRateSum = 0.0f;
			Float coverSinrValueSum = 0.0f;
			Float coverRsrpValueSum = 0.0f;
			Long coverRsrpOrSinrPointNum = 0l;
			Float coverFtpDlSpeedSum = 0.0f;
			Float coverFtpUpSpeedSum = 0.0f;

			for (EmbbCoverBadRoad embbCoverBadRoad : embbCoverBadRoadByLogIds) {
				Float pointRate_ = embbCoverBadRoad.getPointRate();
				Float sinrValueSum = embbCoverBadRoad.getSinrValueSum();
				Float rsrpValueSum = embbCoverBadRoad.getRsrpValueSum();
				Long rsrpOrSinrPointNum = embbCoverBadRoad
						.getRsrpOrSinrPointNum();
				Float ftpDlSpeed_ = embbCoverBadRoad.getFtpDlSpeed();
				Float ftpUpSpeed_ = embbCoverBadRoad.getFtpUpSpeed();
				Float distance_ = embbCoverBadRoad.getDistance();
				Long continueTime = embbCoverBadRoad.getContinueTime();

				// 计算覆盖占比的分区间分原因的路段个数
				Float coverRate = embbCoverBadRoad.getCoverRate();
				Integer reasonTypeNum = embbCoverBadRoad.getReasonTypeNum();
				if (null != reasonTypeNum && 11 >= reasonTypeNum) {
					Map<String, Integer> coverRateMap = reasonRateNumList
							.get(reasonTypeNum);
					coverRateMap.put("total", (coverRateMap.get("total") + 1));
					if (coverRate < 60) {
						coverRateMap.put("rate0t6",
								(coverRateMap.get("rate0t6") + 1));
					} else if (coverRate >= 60 && coverRate < 80) {
						coverRateMap.put("rate6t8",
								(coverRateMap.get("rate6t8") + 1));
					} else {
						coverRateMap.put("rate8t100",
								(coverRateMap.get("rate8t100") + 1));
					}
				}

				// 计算路段总数
				coverNum++;
				// 计算采样点占比
				if (null != pointRate_) {
					coverPointRateSum += pointRate_;
				}
				// 计算持续距离
				if (null != distance_) {
					distance += distance_;
					// 计算最长持续距离
					maxDistance = (maxDistance - distance_ > 0) ? maxDistance
							: distance_;
					// 计算最短持续距离
					minDistance = (minDistance == 0.0f ? distance_
							: ((minDistance - distance_ < 0) ? minDistance
									: distance_));
				}
				// 计算测试时长
				if (null != continueTime) {
					time += continueTime;
					// 计算最长测试时长
					maxTime = (maxTime - time > 0) ? maxTime : time;
					// 计算最短测试时长
					minTime = (minTime == 0.0f ? time
							: ((minTime - time < 0) ? minTime : time));
				}

				// 计算SINR均值/RSRP均值
				if (null != sinrValueSum) {
					coverSinrValueSum += sinrValueSum;
				}
				if (null != rsrpValueSum) {
					coverRsrpValueSum += rsrpValueSum;
				}
				if (null != rsrpOrSinrPointNum) {
					coverRsrpOrSinrPointNum += rsrpOrSinrPointNum;
				}
				// 计算FTP速率
				if (null != ftpDlSpeed_) {
					coverFtpDlSpeedSum += ftpDlSpeed_;
				}
				if (null != ftpUpSpeed_) {
					coverFtpUpSpeedSum += ftpUpSpeed_;
				}

			}

			// 计算采样点占比
			pointRate = (coverNum == 0 ? 0.0f : (NumberFormatUtils.format(
					coverPointRateSum / coverNum, 2)));
			// 计算平均持续距离
			avgDistance = (coverNum == 0 ? 0.0f : (NumberFormatUtils.format(
					distance / coverNum, 2)));
			// 计算平均测试时长
			avgTime = (coverNum == 0 ? 0.0f : (NumberFormatUtils.format(time
					* 1.0f / coverNum, 2)));

			// 计算SINR均值/RSRP均值
			sinr = (coverRsrpOrSinrPointNum == 0l ? 0.0f : NumberFormatUtils
					.format(coverSinrValueSum / coverRsrpOrSinrPointNum, 2));
			rsrp = (coverRsrpOrSinrPointNum == 0l ? 0.0f : NumberFormatUtils
					.format(coverRsrpValueSum / coverRsrpOrSinrPointNum, 2));
			// 计算FTP速率
			ftpUpSpeed = (coverNum == 0 ? 0.0f : NumberFormatUtils.format(
					coverFtpUpSpeedSum / coverNum, 2));
			ftpDlSpeed = (coverNum == 0 ? 0.0f : NumberFormatUtils.format(
					coverFtpDlSpeedSum / coverNum, 2));

		}
		resultMap.put("coverNum", coverNum);
		resultMap.put("pointRate", pointRate);
		resultMap.put("distance", distance);
		resultMap.put("maxDistance", maxDistance);
		resultMap.put("minDistance", minDistance);
		resultMap.put("avgDistance", avgDistance);
		resultMap.put("distanceRate", distanceRate);
		resultMap.put("time", NumberFormatUtils.format(time / 1000, 2));
		resultMap.put("maxTime", NumberFormatUtils.format(maxTime / 1000, 2));
		resultMap.put("minTime", NumberFormatUtils.format(minTime / 1000, 2));
		resultMap.put("avgTime", NumberFormatUtils.format(avgTime / 1000, 2));
		resultMap.put("timeRate", timeRate);
		resultMap.put("sinr", sinr);
		resultMap.put("rsrp", rsrp);
		resultMap.put("ftpUpSpeed", ftpUpSpeed);
		resultMap.put("ftpDlSpeed", ftpDlSpeed);

		resultMap.put("reasonRateNumList", reasonRateNumList);

		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * doEmbbCoverAnalysis
	 * (com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad)
	 */
	@Override
	public Map<String, EasyuiPageList> doEmbbCoverAnalysis(
			EmbbCoverBadRoad embbCoverBadRoad) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("emmCoverCell", doEmbbCoverCellInfoAnalysis(embbCoverBadRoad));
		map.put("emmCoverOptimizeAdvice",
				doEmbbCoverOptimizeAdviceAnalysis(embbCoverBadRoad));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * doEmbbCoverCellInfoAnalysis
	 * (com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad)
	 */
	@Override
	public EasyuiPageList doEmbbCoverCellInfoAnalysis(
			EmbbCoverBadRoad embbCoverBadRoad) {
		if (null == embbCoverBadRoad || null == embbCoverBadRoad.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(embbCoverCellInfoDao
				.queryCellInfoByRoad(embbCoverBadRoad));
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * doEmbbCoverOptimizeAdviceAnalysis
	 * (com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad)
	 */
	@Override
	public EasyuiPageList doEmbbCoverOptimizeAdviceAnalysis(
			EmbbCoverBadRoad embbCoverBadRoad) {
		if (null == embbCoverBadRoad || null == embbCoverBadRoad.getId()) {
			return new EasyuiPageList();
		}
		Integer reasonTypeNum = embbCoverBadRoad.getReasonTypeNum();
		if (null == reasonTypeNum) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		if (0 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverAddStation.class));
		} else if (1 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverAddNbCell.class));
		} else if (2 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverTianKuiConnectReverse.class));
		} else if (3 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverAntennaWeightOptimize.class));
		} else if (4 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverTianKuiAdjust.class));
		} else if (5 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverWaitCheckCell.class));
		} else if (6 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverHeightErrorCell.class));
		} else if (7 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverDowntiltAdjust.class));
		} else if (8 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverPowerAdjust.class));
		} else if (9 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverSceneRetest.class));
		} else if (10 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverNbCellAzimuthAdjust.class));
		} else if (11 == reasonTypeNum) {
			easyuiPageList.setRows(adviceCommonDao.queryOptimizeAdviceByRoad(
					embbCoverBadRoad, EmbbCoverAntennaWeightError.class));
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * doEmbbCoverCellBeamInfoAnalysis
	 * (com.datang.domain.domain5g.embbCover.EmbbCoverCellInfo)
	 */
	@Override
	public EasyuiPageList doEmbbCoverCellBeamInfoAnalysis(
			EmbbCoverCellInfo embbCoverCellInfo) {
		if (null == embbCoverCellInfo || null == embbCoverCellInfo.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(embbCoverCellBeamInfoDao
				.queryCellBeamInfoByCell(embbCoverCellInfo));
		return easyuiPageList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService#
	 * getEmbbCoverCellInfo(java.lang.Long)
	 */
	@Override
	public EmbbCoverCellInfo getEmbbCoverCellInfo(Long ecciId) {
		return embbCoverCellInfoDao.find(ecciId);
	}
}
