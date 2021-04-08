/**
 * 
 */
package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.dao.VoLTEDissertation.exceptionEvent.CsfbFailDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.DropCallDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.ExceptionEventDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.ImsRegistFailDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.NotConnectDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.VideoDropCallDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.VideoNotConnectDao;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.dao.gis.TestLogItemGpsPointDetailDao;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VideoExceptionEventDropCall;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VideoExceptionEventNotConnect;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEvent;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventCsfbFail;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventDropCall;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventImsRegistFail;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventNotConnect;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.service.gis.IEEGpsPointService;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 异常事件轨迹点service接口实现
 * 
 * @author yinzhipeng
 * @date:2016年5月3日 下午2:42:55
 * @modify:yinzhipeng 2017年7月24日
 * @version 1.5.2
 */
@Service
@Transactional
@SuppressWarnings("all")
public class EEGpsPointServiceImpl implements IEEGpsPointService {
	@Value("${exceptionEvent.oppositeSignalling.timeDelay}")
	private String oppositeSignallingTimeDelay;
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private NotConnectDao notConnectDao;
	@Autowired
	private DropCallDao dropCallDao;
	@Autowired
	private ImsRegistFailDao imsRegistFailDao;
	@Autowired
	private CsfbFailDao csfbFailDao;
	@Autowired
	private ExceptionEventDao exceptionEventDao;
	@Autowired
	private VideoNotConnectDao videoNotConnectDao;
	@Autowired
	private VideoDropCallDao videoDropCallDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.IEEGpsPointService#getPointsByEEIdAndIndexType
	 * (java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByEEIdAndIndexType(
			Long EEId, Integer indexType, Integer callType) {
		List<TestLogItemIndexGpsPoint> pointsByEventGpsPoints = new ArrayList<>();
		if (null != EEId || null != indexType) {
			VolteExceptionEvent find = exceptionEventDao.find(EEId);
			if (null != find) {
				if (null == callType) {
					pointsByEventGpsPoints = testLogItemGpsPointDetailDao
							.getTestLogItemGpsIndexPoint(find.getTestLogItem()
									.getRecSeqNo(), indexType, find
									.getStartTime(), find.getEndTime());
				} else {
					Integer eventcallType = null;
					if (null != find) {
						eventcallType = find.getTestLogItem().getCallType();
					}
					if (null == eventcallType) {
						pointsByEventGpsPoints = testLogItemGpsPointDetailDao
								.getTestLogItemGpsIndexPoint(find
										.getTestLogItem().getRecSeqNo(),
										indexType, find.getStartTime(), find
												.getEndTime());
					} else {
						switch (callType) {
						case 0:// 界面查询主叫
							switch (eventcallType) {
							case 0:// 异常事件为主叫
								pointsByEventGpsPoints = testLogItemGpsPointDetailDao
										.getTestLogItemGpsIndexPoint(
												find.getTestLogItem()
														.getRecSeqNo(),
												indexType, find.getStartTime(),
												find.getEndTime());
								break;
							case 1:// 异常事件为被叫,查询对端指标
								pointsByEventGpsPoints = testLogItemGpsPointDetailDao
										.getTestLogItemGpsIndexPoint(
												find.getTestLogItem()
														.getPeerRecSeqNo(),
												indexType,
												null == find.getStartTime() ? null
														: find.getStartTime()
																- Long.valueOf(oppositeSignallingTimeDelay),
												null == find.getEndTime() ? null
														: find.getEndTime()
																+ Long.valueOf(oppositeSignallingTimeDelay));
								break;
							default:
								break;
							}
							break;
						case 1:// 界面查询被叫
							switch (eventcallType) {
							case 0:// 异常事件为主叫,查询对端指标
								pointsByEventGpsPoints = testLogItemGpsPointDetailDao
										.getTestLogItemGpsIndexPoint(
												find.getTestLogItem()
														.getPeerRecSeqNo(),
												indexType,
												null == find.getStartTime() ? null
														: find.getStartTime()
																- Long.valueOf(oppositeSignallingTimeDelay),
												null == find.getEndTime() ? null
														: find.getEndTime()
																+ Long.valueOf(oppositeSignallingTimeDelay));
								break;
							case 1:// 异常事件为被叫
								pointsByEventGpsPoints = testLogItemGpsPointDetailDao
										.getTestLogItemGpsIndexPoint(
												find.getTestLogItem()
														.getRecSeqNo(),
												indexType, find.getStartTime(),
												find.getEndTime());
								break;
							default:
								break;
							}
							break;
						default:
							break;
						}
					}
				}
			}
		}
		return pointsByEventGpsPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.gis.IEEGpsPointService#
	 * getPointDirectionsByEEIdAndIndexType(java.lang.Long, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public List<TestLogItemGpsPoint> getPointDirectionsByEEIdAndIndexType(
			Long EEId, Integer indexType, Integer callType) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != EEId || null != indexType) {
			VolteExceptionEvent find = exceptionEventDao.find(EEId);
			if (null != find) {
				if (null == callType) {
					gpsPoints = testLogItemGpsPointDao
							.getTestLogItemGpsIndexPointDirection(find
									.getTestLogItem().getRecSeqNo(), indexType,
									find.getStartTime(), find.getEndTime());
				} else {
					Integer eventcallType = null;
					if (null != find) {
						eventcallType = find.getTestLogItem().getCallType();
					}
					if (null == eventcallType) {
						gpsPoints = testLogItemGpsPointDao
								.getTestLogItemGpsIndexPointDirection(find
										.getTestLogItem().getRecSeqNo(),
										indexType, find.getStartTime(), find
												.getEndTime());
					} else {
						switch (callType) {
						case 0:// 界面查询主叫
							switch (eventcallType) {
							case 0:// 异常事件为主叫
								gpsPoints = testLogItemGpsPointDao
										.getTestLogItemGpsIndexPointDirection(
												find.getTestLogItem()
														.getRecSeqNo(),
												indexType, find.getStartTime(),
												find.getEndTime());
								break;
							case 1:// 异常事件为被叫,查询对端指标
								gpsPoints = testLogItemGpsPointDao
										.getTestLogItemGpsIndexPointDirection(
												find.getTestLogItem()
														.getPeerRecSeqNo(),
												indexType,
												null == find.getStartTime() ? null
														: find.getStartTime()
																- Long.valueOf(oppositeSignallingTimeDelay),
												null == find.getEndTime() ? null
														: find.getEndTime()
																+ Long.valueOf(oppositeSignallingTimeDelay));
								break;
							default:
								break;
							}
							break;
						case 1:// 界面查询被叫
							switch (eventcallType) {
							case 0:// 异常事件为主叫,查询对端指标
								gpsPoints = testLogItemGpsPointDao
										.getTestLogItemGpsIndexPointDirection(
												find.getTestLogItem()
														.getPeerRecSeqNo(),
												indexType,
												null == find.getStartTime() ? null
														: find.getStartTime()
																- Long.valueOf(oppositeSignallingTimeDelay),
												null == find.getEndTime() ? null
														: find.getEndTime()
																+ Long.valueOf(oppositeSignallingTimeDelay));
								break;
							case 1:// 异常事件为被叫
								gpsPoints = testLogItemGpsPointDao
										.getTestLogItemGpsIndexPointDirection(
												find.getTestLogItem()
														.getRecSeqNo(),
												indexType, find.getStartTime(),
												find.getEndTime());
								break;
							default:
								break;
							}
							break;
						default:
							break;
						}
					}
				}
			}
		}
		return gpsPoints;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.IEEGpsPointService#getPointsByEEId(java.lang.Long)
	 */

	@Override
	public List<TestLogItemGpsPoint> getPointsByEEId(Long EEId) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != EEId) {
			VolteExceptionEvent find = exceptionEventDao.find(EEId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDao
						.getTestLogItemGpsIndexPointDirection(find
								.getTestLogItem().getRecSeqNo(), null, find
								.getStartTime(), find.getEndTime());
			}
		}
		return gpsPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.gis.IEEGpsPointService#
	 * getEventPointsByTestlogIdsAndIconType(java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	public List<TestLogItemIndexGpsPoint> getEventPointsByTestlogIdsAndIconType(
			String testLogItemIds, String iconType, Integer eeType) {
		List<VolteExceptionEvent> queryEEs = queryEEs(testLogItemIds, eeType);
		List<TestLogItemIndexGpsPoint> eventEventPoints = new ArrayList<>();
		if (StringUtils.hasText(iconType)) {
			List<Integer> iconTypeList = new ArrayList<>();
			String[] split = iconType.split(",");
			for (String string : split) {
				if (StringUtils.hasText(string)) {
					iconTypeList.add(Integer.valueOf(string.trim()));
				}
			}

			for (VolteExceptionEvent volteExceptionEvent : queryEEs) {
				List<TestLogItemIndexGpsPoint> testLogItemGpsIndexPoint = testLogItemGpsPointDetailDao
						.getTestLogItemGpsEventPoint(volteExceptionEvent
								.getTestLogItem().getRecSeqNo(), iconTypeList,
								volteExceptionEvent.getStartTime(),
								volteExceptionEvent.getEndTime());
				if (testLogItemGpsIndexPoint != null
						&& testLogItemGpsIndexPoint.size() != 0) {
					eventEventPoints.addAll(testLogItemGpsIndexPoint);
				}
			}

		}
		return eventEventPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.IEEGpsPointService#getEventPointsByEEIdAndIndexType
	 * (java.lang.Long)
	 */
	@Override
	public List<TestLogItemIndexGpsPoint> getEventPointsByEEIdAndIndexType(
			Long EEId, Integer callType) {
		List<TestLogItemIndexGpsPoint> gpsPoints = new ArrayList<>();
		if (null != EEId) {
			VolteExceptionEvent find = exceptionEventDao.find(EEId);
			if (null != find) {

				if (null == callType) {
					gpsPoints = testLogItemGpsPointDetailDao
							.getTestLogItemGpsEventPoint(find.getTestLogItem()
									.getRecSeqNo(), null, find.getStartTime(),
									find.getEndTime());
				} else {
					Integer eventcallType = null;
					if (null != find) {
						eventcallType = find.getTestLogItem().getCallType();
					}
					if (null == eventcallType) {
						gpsPoints = testLogItemGpsPointDetailDao
								.getTestLogItemGpsEventPoint(find
										.getTestLogItem().getRecSeqNo(), null,
										find.getStartTime(), find.getEndTime());
					} else {
						switch (callType) {
						case 0:// 界面查询主叫
							switch (eventcallType) {
							case 0:// 异常事件为主叫
								gpsPoints = testLogItemGpsPointDetailDao
										.getTestLogItemGpsEventPoint(
												find.getTestLogItem()
														.getRecSeqNo(), null,
												find.getStartTime(), find
														.getEndTime());
								break;
							case 1:// 异常事件为被叫,查询对端指标
								gpsPoints = testLogItemGpsPointDetailDao
										.getTestLogItemGpsEventPoint(
												find.getTestLogItem()
														.getPeerRecSeqNo(),
												null,
												null == find.getStartTime() ? null
														: find.getStartTime()
																- Long.valueOf(oppositeSignallingTimeDelay),
												null == find.getEndTime() ? null
														: find.getEndTime()
																+ Long.valueOf(oppositeSignallingTimeDelay));
								break;
							default:
								break;
							}
							break;
						case 1:// 界面查询被叫
							switch (eventcallType) {
							case 0:// 异常事件为主叫,查询对端指标
								gpsPoints = testLogItemGpsPointDetailDao
										.getTestLogItemGpsEventPoint(
												find.getTestLogItem()
														.getPeerRecSeqNo(),
												null,
												null == find.getStartTime() ? null
														: find.getStartTime()
																- Long.valueOf(oppositeSignallingTimeDelay),
												null == find.getEndTime() ? null
														: find.getEndTime()
																+ Long.valueOf(oppositeSignallingTimeDelay));
								break;
							case 1:// 异常事件为被叫
								gpsPoints = testLogItemGpsPointDetailDao
										.getTestLogItemGpsEventPoint(
												find.getTestLogItem()
														.getRecSeqNo(), null,
												find.getStartTime(), find
														.getEndTime());
								break;
							default:
								break;
							}
							break;
						default:
							break;
						}
					}
				}
			}
		}
		return gpsPoints;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.IEEGpsPointService#getEEGpsPointsByTestlogIdsAndEEType
	 * (java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<List<TestLogItemGpsPoint>> getEEGpsPointsByTestlogIdsAndEEType(
			String testLogItemIds, Integer eeType) {
		List<List<TestLogItemGpsPoint>> lists = new ArrayList<>();
		// 添加所有eetype的事件轨迹数据
		List<Long> queryEEIds = queryEEIds(testLogItemIds, eeType);
		for (Long eeId : queryEEIds) {
			List<TestLogItemGpsPoint> pointsByEEId = getPointsByEEId(eeId);
			if (null != pointsByEEId && 0 != pointsByEEId.size()) {
				lists.add(pointsByEEId);
			}
		}
		return lists;
	}

	/**
	 * 获取某些测试日志下的某种异常事件id集合
	 * 
	 * @param testLogItemIds
	 *            测试日志id按','分隔字符串
	 * @param eeType
	 *            异常事件类型
	 * @return
	 */
	private List<Long> queryEEIds(String testLogItemIds, Integer eeType) {
		List<Long> longs = new ArrayList<>();
		if (null != eeType && StringUtils.hasText(testLogItemIds)) {
			String[] logIds = testLogItemIds.trim().split(",");
			// 存储TestLogItem的id集合
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < logIds.length; i++) {
				if (StringUtils.hasText(logIds[i])) {
					try {
						ids.add(Long.parseLong(logIds[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
			// 异常事件类型(0语音未接通,1语音掉话,2IMS注册失败,3CSFB失败,4视频未接通,5视频掉话)
			switch (eeType) {
			case 0:
				List<Long> queryNotConnectIdsByLogIds = notConnectDao
						.queryNotConnectIdsByLogIds(ids);
				if (null != queryNotConnectIdsByLogIds) {
					longs.addAll(queryNotConnectIdsByLogIds);
				}
				break;
			case 1:
				List<Long> queryDropCallIdsByLogIds = dropCallDao
						.queryDropCallIdsByLogIds(ids);
				if (null != queryDropCallIdsByLogIds) {
					longs.addAll(queryDropCallIdsByLogIds);
				}
				break;
			case 2:
				List<Long> queryImsRegistFailIdsByLogIds = imsRegistFailDao
						.queryImsRegistFailIdsByLogIds(ids);
				if (null != queryImsRegistFailIdsByLogIds) {
					longs.addAll(queryImsRegistFailIdsByLogIds);
				}
				break;
			case 3:
				List<Long> queryCsfbFailIdsByLogIds = csfbFailDao
						.queryCsfbFailIdsByLogIds(ids);
				if (null != queryCsfbFailIdsByLogIds) {
					longs.addAll(queryCsfbFailIdsByLogIds);
				}
				break;
			case 4:
				List<Long> queryVideoNotConnectIdsByLogIds = videoNotConnectDao
						.queryVideoNotConnectIdsByLogIds(ids);
				if (null != queryVideoNotConnectIdsByLogIds) {
					longs.addAll(queryVideoNotConnectIdsByLogIds);
				}
				break;
			case 5:
				List<Long> queryVideoDropCallIdsByLogIds = videoDropCallDao
						.queryVideoDropCallIdsByLogIds(ids);
				if (null != queryVideoDropCallIdsByLogIds) {
					longs.addAll(queryVideoDropCallIdsByLogIds);
				}
				break;
			default:
				break;
			}
		}
		return longs;
	}

	/**
	 * 获取某些测试日志下的某种异常事件id集合
	 * 
	 * @param testLogItemIds
	 *            测试日志id按','分隔字符串
	 * @param eeType
	 *            异常事件类型
	 * @return
	 */
	private List<VolteExceptionEvent> queryEEs(String testLogItemIds,
			Integer eeType) {
		List<VolteExceptionEvent> longs = new ArrayList<>();
		if (null != eeType && StringUtils.hasText(testLogItemIds)) {
			String[] logIds = testLogItemIds.trim().split(",");
			// 存储TestLogItem的id集合
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < logIds.length; i++) {
				if (StringUtils.hasText(logIds[i])) {
					try {
						ids.add(Long.parseLong(logIds[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
			// 异常事件类型(0语音未接通,1语音掉话,2IMS注册失败,3CSFB失败,4视频未接通,5视频掉话)
			switch (eeType) {
			case 0:
				List<VolteExceptionEventNotConnect> list = notConnectDao
						.queryVolteExceptionEventNotConnectsByLogIds(ids);
				if (null != list) {
					longs.addAll(list);
				}
				break;
			case 1:
				List<VolteExceptionEventDropCall> list2 = dropCallDao
						.queryVolteExceptionEventDropCallsByLogIds(ids);
				if (null != list2) {
					longs.addAll(list2);
				}
				break;
			case 2:
				List<VolteExceptionEventImsRegistFail> list3 = imsRegistFailDao
						.queryVolteExceptionEventImsRegistFailsByLogIds(ids);
				if (null != list3) {
					longs.addAll(list3);
				}
				break;
			case 3:
				List<VolteExceptionEventCsfbFail> list4 = csfbFailDao
						.queryVolteExceptionEventCsfbFailsByLogIds(ids);
				if (null != list4) {
					longs.addAll(list4);
				}
				break;
			case 4:
				List<VideoExceptionEventNotConnect> list5 = videoNotConnectDao
						.queryVideoExceptionEventNotConnectsByLogIds(ids);
				if (null != list5) {
					longs.addAll(list5);
				}
				break;
			case 5:
				List<VideoExceptionEventDropCall> list6 = videoDropCallDao
						.queryVideoExceptionEventDropCallsByLogIds(ids);
				if (null != list6) {
					longs.addAll(list6);
				}
				break;
			default:
				break;
			}
		}
		return longs;
	}

}
