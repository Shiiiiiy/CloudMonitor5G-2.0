package com.datang.service.monitor.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.bean.monitor.ATULoginLogItemBean;
import com.datang.bean.monitor.EventParamBean;
import com.datang.bean.monitor.GPSBean;
import com.datang.bean.monitor.MosValueBean;
import com.datang.bean.monitor.RealtimeAlarmBean;
import com.datang.bean.monitor.RealtimeEventBean;
import com.datang.bean.monitor.StatusReportBean;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.dao.monitor.ATULogDao;
import com.datang.dao.monitor.AlarmDao;
import com.datang.dao.monitor.EventDao;
import com.datang.dao.monitor.GPSDao;
import com.datang.dao.monitor.MosValueDao;
import com.datang.dao.monitor.StatusDao;
import com.datang.domain.monitor.ATULoginInfo;
import com.datang.domain.monitor.ATULoginLogItem;
import com.datang.domain.monitor.GPS;
import com.datang.domain.monitor.MosValue;
import com.datang.domain.monitor.RealtimeAlarm;
import com.datang.domain.monitor.RealtimeEvent;
import com.datang.domain.monitor.StatusReport;
import com.datang.service.monitor.MapMonitorService;
import com.datang.util.monitor.AlarmCode;
import com.datang.util.monitor.CodeUtils;
import com.datang.web.beans.monitor.GPSRequestBean;

/**
 * @explain
 * @name MapMonitorServiceImpl
 * @author shenyanwei
 * @date 2016年7月12日下午1:50:58
 */
@Service
@Transactional
@SuppressWarnings("all")
public class MapMonitorServiceImpl implements MapMonitorService {
	@Autowired
	private GPSDao gpsDao;
	@Autowired
	private AlarmDao alarmDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private MosValueDao mosValueDao;
	@Autowired
	private StatusDao statusDao;
	@Autowired
	private ATULogDao atuLogDao;

	/*
	 * *
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.monitor.MapMonitorService#queryTerminalGpsPoint(java
	 * .util.List)
	 */
	@Override
	public List<Object> queryTerminalGpsPoint(List<String> boxIDs) {
		ArrayList<Object> returnList = new ArrayList<Object>();
		List<List<Object>> pointLists = new ArrayList<>();
		HashMap<String, List<GPS>> hashMapForTime = new HashMap<>();
		HashMap<String, List<List<GPSBean>>> hashMap2 = new HashMap<String, List<List<GPSBean>>>();
		List<String> timeList = new ArrayList<String>();
		// 初始化起始时间为一小时前
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2);// 1小时前
		Date begain = cal.getTime();
		List<GPS> gpsAll = gpsDao.queryTerminalGpsPoint(begain, null, boxIDs);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		// 先收集每个终端最新数据的时间
		if (null != gpsAll) {
			for (GPS gps : gpsAll) {
				if (null != hashMapForTime.get(gps.getBoxId())) {
					hashMapForTime.get(gps.getBoxId()).add(gps);
				} else {
					ArrayList<GPS> arrayList = new ArrayList<>();
					arrayList.add(gps);
					// arrayList2.add(arrayList);
					hashMapForTime.put(gps.getBoxId(), arrayList);
					// 每个boxID都对应一个时间 字符串用，隔开 查询数据已排序，可确保第一个数为最新数据
					timeList.add(gps.getBoxId() + ","
							+ dateFormat.format(gps.getGpsTime()));
				}
			}
		}
		// 查询每个终端登录日志以便分段
		List<ATULoginLogItem> atuLoginLogItems = atuLogDao.queryByTimeAndBoxId(
				begain, null, boxIDs);
		List<List<GPS>> gpsLists = new ArrayList<>();
		if (null != atuLoginLogItems && 0 != atuLoginLogItems.size()
				&& null != gpsAll) {
			// 根据登陆日志时间分段查询GPS
			for (ATULoginLogItem atuLoginLogItem : atuLoginLogItems) {
				List<String> boxStrs2 = new ArrayList<>();
				boxStrs2.add(atuLoginLogItem.getBoxId());
				if (0 != atuLoginLogItem.getOfflineTimeLong()) {
					List<RealtimeAlarm> alarmList = alarmDao
							.queryGPSByTimeAndBoxId(
									atuLoginLogItem.getLoginTime(),
									atuLoginLogItem.getOfflineTime(),
									atuLoginLogItem.getBoxId());

					if (null != alarmList && 0 != alarmList.size()) {
						if (alarmList.size() == 1) {
							List<GPS> querygps = gpsDao.queryTerminalGpsPoint(
									atuLoginLogItem.getLoginTime(), alarmList
											.get(0).getAlarmTime(), boxStrs2);

							if (null != querygps) {
								querygps.remove(querygps.size() - 1);
								gpsLists.add(querygps);
							}
							List<GPS> querygps2 = gpsDao.queryTerminalGpsPoint(
									alarmList.get(0).getAlarmTime(),
									atuLoginLogItem.getOfflineTime(), boxStrs2);
							if (null != querygps2) {
								querygps2.remove(querygps2.size() - 1);
								gpsLists.add(querygps2);
							}
						} else {
							for (int i = 0; i < alarmList.size(); i++) {
								if (i == 0) {
									List<GPS> querygps = gpsDao
											.queryTerminalGpsPoint(
													atuLoginLogItem
															.getLoginTime(),
													alarmList.get(i)
															.getAlarmTime(),
													boxStrs2);
									if (null != querygps) {
										querygps.remove(querygps.size() - 1);
										gpsLists.add(querygps);
									}
								} else {
									if (i == alarmList.size() - 1) {
										List<GPS> querygps = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i - 1)
																.getAlarmTime(),
														alarmList.get(i)
																.getAlarmTime(),
														boxStrs2);
										if (null != querygps) {
											querygps.remove(querygps.size() - 1);
											gpsLists.add(querygps);
										}
										List<GPS> querygps2 = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i)
																.getAlarmTime(),
														atuLoginLogItem
																.getOfflineTime(),
														boxStrs2);
										if (null != querygps2) {
											querygps2
													.remove(querygps2.size() - 1);
											gpsLists.add(querygps2);
										}
									} else {
										List<GPS> querygps = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i - 1)
																.getAlarmTime(),
														alarmList.get(i)
																.getAlarmTime(),
														boxStrs2);
										if (null != querygps) {
											querygps.remove(querygps.size() - 1);
											gpsLists.add(querygps);
										}
									}
								}
							}
						}

					} else {
						List<GPS> querygps = gpsDao.queryTerminalGpsPoint(
								atuLoginLogItem.getLoginTime(),
								atuLoginLogItem.getOfflineTime(), boxStrs2);
						if (null != querygps) {
							gpsLists.add(querygps);
						}
					}
				} else {
					List<RealtimeAlarm> alarmList = alarmDao
							.queryGPSByTimeAndBoxId(
									atuLoginLogItem.getLoginTime(), null,
									atuLoginLogItem.getBoxId());

					if (null != alarmList && 0 != alarmList.size()) {
						if (alarmList.size() == 1) {
							List<GPS> querygps = gpsDao.queryTerminalGpsPoint(
									atuLoginLogItem.getLoginTime(), alarmList
											.get(0).getAlarmTime(), boxStrs2);

							if (null != querygps) {
								querygps.remove(querygps.size() - 1);
								gpsLists.add(querygps);
							}
							List<GPS> querygps2 = gpsDao.queryTerminalGpsPoint(
									alarmList.get(0).getAlarmTime(), null,
									boxStrs2);
							if (null != querygps2) {
								querygps2.remove(querygps2.size() - 1);
								gpsLists.add(querygps2);
							}
						} else {
							for (int i = 0; i < alarmList.size(); i++) {
								if (i == 0) {
									List<GPS> querygps = gpsDao
											.queryTerminalGpsPoint(
													atuLoginLogItem
															.getLoginTime(),
													alarmList.get(i)
															.getAlarmTime(),
													boxStrs2);
									if (null != querygps) {
										querygps.remove(querygps.size() - 1);
										gpsLists.add(querygps);
									}
								} else {
									if (i == alarmList.size() - 1) {
										List<GPS> querygps = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i - 1)
																.getAlarmTime(),
														alarmList.get(i)
																.getAlarmTime(),
														boxStrs2);
										if (null != querygps) {
											querygps.remove(querygps.size() - 1);
											gpsLists.add(querygps);
										}
										List<GPS> querygps2 = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i)
																.getAlarmTime(),
														null, boxStrs2);
										if (null != querygps2) {
											querygps2
													.remove(querygps2.size() - 1);
											gpsLists.add(querygps2);
										}
									} else {
										List<GPS> querygps = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i - 1)
																.getAlarmTime(),
														alarmList.get(i)
																.getAlarmTime(),
														boxStrs2);
										if (null != querygps) {
											querygps.remove(querygps.size() - 1);
											gpsLists.add(querygps);
										}
									}
								}
							}
						}

					} else {
						List<GPS> querygps = gpsDao.queryTerminalGpsPoint(
								atuLoginLogItem.getLoginTime(), null, boxStrs2);
						if (null != querygps) {
							gpsLists.add(querygps);
						}
					}
				}
			}
		} else {
			gpsLists.add(gpsAll);
		}
		JSONObject jsonObject = new JSONObject();
		if (gpsLists != null) {
			for (List<GPS> list : gpsLists) {
				HashMap<String, List<GPSBean>> hashMap = new HashMap<String, List<GPSBean>>();
				if (null != list) {
					for (GPS gps2 : list) {
						GPSBean gpsBean = new GPSBean();
						gpsBean.setLatitude(gps2.getLatitude());
						gpsBean.setLongitude(gps2.getLongitude());
						if (null != hashMap.get(gps2.getBoxId())) {
							hashMap.get(gps2.getBoxId()).add(gpsBean);
						} else {
							List<List<GPSBean>> arrayList2 = new ArrayList<>();
							ArrayList<GPSBean> arrayList = new ArrayList<GPSBean>();
							arrayList.add(gpsBean);
							arrayList2.add(arrayList);
							hashMap.put(gps2.getBoxId(), arrayList);
							if (null != hashMap2.get(gps2.getBoxId())) {
								hashMap2.get(gps2.getBoxId()).add(arrayList);
							} else {
								hashMap2.put(gps2.getBoxId(), arrayList2);
								// 每个boxID都对应一个时间 字符串用，隔开 查询数据已排序，可确保第一个数为最新数据
								// timeList.add(gps2.getBoxId() + ","
								// + dateFormat.format(gps2.getGpsTime()));
							}
						}
					}
				}
			}
			// 包装成需要的格式
			for (Entry<String, List<List<GPSBean>>> entry : hashMap2.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		if (0 != timeList.size()) {
			jsonObject.put("time", timeList);
			returnList.add(0, jsonObject);
			returnList.add(1, pointLists);
			return returnList;
		} else {
			for (String str : boxIDs) {
				timeList.add(str + "," + dateFormat.format(new Date()));
			}
			jsonObject.put("time", timeList);
			returnList.add(0, jsonObject);
			returnList.add(1, pointLists);
			return returnList;
		}

	}

	@Override
	public Map<String, EasyuiPageList> queryOtherInformation(
			GPSRequestBean gpsRequestBean) {
		HashMap<String, EasyuiPageList> hashMap = new HashMap<String, EasyuiPageList>();
		List<String> idList = new ArrayList<>();
		idList.add(gpsRequestBean.getBoxID());
		List<ATULoginLogItem> ATULoginList = atuLogDao.queryByTimeAndBoxId(
				gpsRequestBean.getBeginDate(), gpsRequestBean.getEndDate(),
				idList);
		List<ATULoginLogItem> arrayList = new ArrayList<>();
		if (null != ATULoginList && 0 != ATULoginList.size()) {
			for (ATULoginLogItem atuLoginLogItem : ATULoginList) {
				ATULoginLogItem atuLoginLogItem2 = new ATULoginLogItem();
				atuLoginLogItem2.setBoxId(atuLoginLogItem.getBoxId());
				atuLoginLogItem2.setLoginTime(atuLoginLogItem.getLoginTime());
				atuLoginLogItem2.setLoginTimeLong(atuLoginLogItem
						.getLoginTimeLong());
				atuLoginLogItem2.setOfflineTime(atuLoginLogItem
						.getOfflineTime());
				atuLoginLogItem2.setOfflineTimeLong(atuLoginLogItem
						.getOfflineTimeLong());
				arrayList.add(atuLoginLogItem2);
			}
		}
		// 调用方法将离线时间不足一小时的登陆记录合并
		machListATUInfo(arrayList);
		EasyuiPageList pageListATUInfo = new EasyuiPageList();
		List<ATULoginInfo> listATUInfo = new ArrayList<>();
		// 若查询结果不为空则遍历包装成页面需要的数据格式并返回
		if (arrayList != null && 0 != arrayList.size()) {
			for (ATULoginLogItem atuLoginLogItem : arrayList) {
				if (null != atuLoginLogItem.getLoginTime()
						&& null != atuLoginLogItem.getOfflineTime()) {
					ATULoginInfo atuLoginInfo = new ATULoginInfo();
					atuLoginInfo.setBoxId(atuLoginLogItem.getBoxId());
					atuLoginInfo.setLoginTime(atuLoginLogItem.getLoginTime());
					atuLoginInfo.setOfflineTime(atuLoginLogItem
							.getOfflineTime());
					listATUInfo.add(atuLoginInfo);
				}
			}
			pageListATUInfo.setRows(listATUInfo);
		}
		hashMap.put("Info", pageListATUInfo);
		return hashMap;
	}

	@Override
	public List<Object> queryGPSRefreshRequestParam(List<String> boxIDs,
			Map<String, Date> timeMap) {
		ArrayList<Object> returnList = new ArrayList<Object>();
		List<List<Object>> pointLists = new ArrayList<>();
		List<String> timeList = new ArrayList<String>();
		HashMap<String, List<List<GPSBean>>> hashMap2 = new HashMap<String, List<List<GPSBean>>>();
		HashMap<String, List<GPSBean>> hashMap = new HashMap<String, List<GPSBean>>();
		JSONObject jsonObject = new JSONObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		// List<List<GPS>> gpsLists = new ArrayList<>();
		for (String boxId : boxIDs) {
			List<String> listStrings = new ArrayList<>();
			listStrings.add(boxId);
			List<GPS> gpsAll = gpsDao.queryTerminalGpsPoint(timeMap.get(boxId),
					null, listStrings);

			List<ATULoginLogItem> atuLoginLogItems = atuLogDao
					.queryByTimeAndBoxId(timeMap.get(boxId), null, boxIDs);
			List<RealtimeAlarm> queryGPSByTimeAndBoxId = alarmDao
					.queryGPSByTimeAndBoxId(timeMap.get(boxId), null, boxId);
			if (gpsAll != null) {
				if (null != atuLoginLogItems || null != queryGPSByTimeAndBoxId) {
					if (1 == gpsAll.size()) {
						timeList.add(gpsAll.get(0).getBoxId() + ","
								+ dateFormat.format(gpsAll.get(0).getGpsTime()));
						gpsAll.remove(gpsAll.size() - 1);
					} else {
						gpsAll.remove(gpsAll.size() - 1);
					}
				}
			}
			// for (List<GPS> list : gpsAll) {
			// HashMap<String, List<GPSBean>> hashMap = new HashMap<String,
			// List<GPSBean>>();
			if (gpsAll != null) {
				for (GPS gps2 : gpsAll) {
					GPSBean gpsBean = new GPSBean();
					gpsBean.setLatitude(gps2.getLatitude());
					gpsBean.setLongitude(gps2.getLongitude());
					if (null != hashMap.get(gps2.getBoxId())) {
						hashMap.get(gps2.getBoxId()).add(gpsBean);
					} else {
						// List<List<GPSBean>> arrayList2 = new ArrayList<>();
						ArrayList<GPSBean> arrayList = new ArrayList<GPSBean>();
						arrayList.add(gpsBean);
						// arrayList2.add(arrayList);
						hashMap.put(gps2.getBoxId(), arrayList);
						// 每个boxID都对应一个时间 字符串用，隔开 查询数据已排序，可确保第一个数为最新数据
						timeList.add(gps2.getBoxId() + ","
								+ dateFormat.format(gps2.getGpsTime()));
						// if (null != hashMap2.get(gps2.getBoxId())) {
						// hashMap2.get(gps2.getBoxId()).add(arrayList);
						// } else {
						// hashMap2.put(gps2.getBoxId(), arrayList2);
						// // 每个boxID都对应一个时间 字符串用，隔开 查询数据已排序，可确保第一个数为最新数据
						// timeList.add(gps2.getBoxId() + ","
						// + dateFormat.format(gps2.getGpsTime()));
						// }
					}
				}
			}
		}
		// 包装成需要的格式
		// for (Entry<String, List<List<GPSBean>>> entry : hashMap2
		// .entrySet()) {
		for (Entry<String, List<GPSBean>> entry : hashMap.entrySet()) {
			List<Object> list = new LinkedList<>();
			list.add(entry.getKey());
			list.add(entry.getValue());
			pointLists.add(list);
		}
		if (timeList != null && timeList.size() != 0) {
			jsonObject.put("time", timeList);
		} else {
			for (String string : timeMap.keySet()) {
				timeList.add(string + "," + dateFormat.format(new Date()));
				jsonObject.put("time", timeList);
			}
		}
		returnList.add(0, jsonObject);
		returnList.add(1, pointLists);

		return returnList;
	}

	@Override
	public List<Object> queryGPSEventRequestParam(Date begainDate,
			Date endDate, List<Integer> galleryNos, List<String> boxIds) {
		List<Object> pointLists = new ArrayList<>();
		HashMap<String, List<EventParamBean>> hashMap = new HashMap<String, List<EventParamBean>>();
		List<RealtimeEvent> queryByGalleryIDsAndBoxIds = eventDao
				.queryByGalleryIDsAndBoxIds(begainDate, endDate, galleryNos,
						boxIds);
		// 将结果根据ChannelNo分类包装
		if (queryByGalleryIDsAndBoxIds != null) {
			Map<Integer, String> eventNumber = CodeUtils.getEventNumber();
			for (RealtimeEvent event : queryByGalleryIDsAndBoxIds) {
				EventParamBean eventParamBean = new EventParamBean();
				if (null != event.getGps()) {
					eventParamBean.setLatitude(event.getGps().getLatitude());
					eventParamBean.setLongitude(event.getGps().getLongitude());
				}
				eventParamBean.setEventType(eventNumber.get(event
						.getEventCode()));
				if (null != hashMap.get(String.valueOf(event.getChannelNo()))) {
					hashMap.get(String.valueOf(event.getChannelNo())).add(
							eventParamBean);
				} else {
					ArrayList<EventParamBean> arrayList = new ArrayList<EventParamBean>();
					arrayList.add(eventParamBean);
					hashMap.put(String.valueOf(event.getChannelNo()), arrayList);
				}
			}
			for (Entry<String, List<EventParamBean>> entry : hashMap.entrySet()) {
				List<Object> list = new LinkedList<>();
				if (null != entry.getKey()) {
					list.add(entry.getKey());
					list.add(entry.getValue());
					pointLists.add(list);
				}
			}
		}

		return pointLists;
	}

	@Override
	public Map<String, EasyuiPageList> queryHistoryTerminalGpsPoint(
			Date begainTime, Date endTime, String boxID) {
		HashMap<String, EasyuiPageList> hashMap = new HashMap<String, EasyuiPageList>();
		ArrayList<Object> returnList = new ArrayList<Object>();
		List<List<Object>> pointLists = new ArrayList<>();
		HashMap<String, List<GPSBean>> map = new HashMap<String, List<GPSBean>>();
		// 收集历史轨迹信息(整个轨迹汇总用来回放)
		List<GPS> gps = gpsDao.queryHistoryTerminalGpsPoint(begainTime,
				endTime, boxID);

		if (gps != null && 0 != gps.size()) {
			Collections.reverse(gps);
			for (GPS gps2 : gps) {
				GPSBean gpsBean = new GPSBean();
				gpsBean.setLatitude(gps2.getLatitude());
				gpsBean.setLongitude(gps2.getLongitude());
				if (null != map.get(gps2.getBoxId())) {
					map.get(gps2.getBoxId()).add(gpsBean);
				} else {
					ArrayList<GPSBean> arrayList = new ArrayList<GPSBean>();
					arrayList.add(gpsBean);
					map.put(gps2.getBoxId(), arrayList);
				}
			}
			if (0 == map.size()) {
				List<Object> list = new LinkedList<>();
				list.add(boxID);
				list.add(new ArrayList());
				pointLists.add(list);
			} else {
				for (Entry<String, List<GPSBean>> entry : map.entrySet()) {
					List<Object> list = new LinkedList<>();
					list.add(entry.getKey());
					list.add(entry.getValue());
					pointLists.add(list);
				}
			}
		} else {
			List<Object> list = new LinkedList<>();
			list.add(boxID);
			list.add(new ArrayList());
			pointLists.add(list);
		}
		JSONObject jsonObject = new JSONObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		jsonObject.put(
				"time",
				dateFormat.format(begainTime) + ","
						+ dateFormat.format(endTime));
		returnList.add(0, jsonObject);
		returnList.add(1, pointLists);
		EasyuiPageList pageListGsp = new EasyuiPageList();
		pageListGsp.setRows(returnList);
		// 收集分段轨迹，用来绘制轨迹路线
		List<List<Object>> pointLists2 = new ArrayList<>();
		HashMap<String, List<List<GPSBean>>> hashMap2 = new HashMap<String, List<List<GPSBean>>>();
		List<String> boxStrs = new ArrayList<>();
		boxStrs.add(boxID);
		List<GPS> gpsAll = gpsDao.queryTerminalGpsPoint(begainTime, endTime,
				boxStrs);
		List<ATULoginLogItem> atuLoginLogItems = atuLogDao.queryByTimeAndBoxId(
				begainTime, endTime, boxStrs);
		List<List<GPS>> gpsLists = new ArrayList<>();
		if (null != atuLoginLogItems && 0 != atuLoginLogItems.size()
				&& null != gpsAll) {
			for (ATULoginLogItem atuLoginLogItem : atuLoginLogItems) {
				// List<String> boxStrs2 = new ArrayList<>();
				// boxStrs2.add(atuLoginLogItem.getBoxId());
				// List<GPS> queryTerminalGpsPoint =
				// gpsDao.queryTerminalGpsPoint(
				// atuLoginLogItem.getLoginTime(),
				// atuLoginLogItem.getOfflineTime(), boxStrs2);
				// if (null != queryTerminalGpsPoint) {
				// gpsLists.add(queryTerminalGpsPoint);
				// }
				List<String> boxStrs2 = new ArrayList<>();
				boxStrs2.add(atuLoginLogItem.getBoxId());
				if (0 != atuLoginLogItem.getOfflineTimeLong()) {
					List<RealtimeAlarm> alarmList = alarmDao
							.queryGPSByTimeAndBoxId(
									atuLoginLogItem.getLoginTime(),
									atuLoginLogItem.getOfflineTime(),
									atuLoginLogItem.getBoxId());

					if (null != alarmList && 0 != alarmList.size()) {
						if (alarmList.size() == 1) {
							List<GPS> querygps = gpsDao.queryTerminalGpsPoint(
									atuLoginLogItem.getLoginTime(), alarmList
											.get(0).getAlarmTime(), boxStrs2);

							if (null != querygps) {
								querygps.remove(querygps.size() - 1);
								gpsLists.add(querygps);
							}
							List<GPS> querygps2 = gpsDao.queryTerminalGpsPoint(
									alarmList.get(0).getAlarmTime(),
									atuLoginLogItem.getOfflineTime(), boxStrs2);
							if (null != querygps2) {
								querygps2.remove(querygps2.size() - 1);
								gpsLists.add(querygps2);
							}
						} else {
							for (int i = 0; i < alarmList.size(); i++) {
								if (i == 0) {
									List<GPS> querygps = gpsDao
											.queryTerminalGpsPoint(
													atuLoginLogItem
															.getLoginTime(),
													alarmList.get(i)
															.getAlarmTime(),
													boxStrs2);
									if (null != querygps) {
										querygps.remove(querygps.size() - 1);
										gpsLists.add(querygps);
									}
								} else {
									if (i == alarmList.size() - 1) {
										List<GPS> querygps = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i - 1)
																.getAlarmTime(),
														alarmList.get(i)
																.getAlarmTime(),
														boxStrs2);
										if (null != querygps) {
											querygps.remove(querygps.size() - 1);
											gpsLists.add(querygps);
										}
										List<GPS> querygps2 = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i)
																.getAlarmTime(),
														atuLoginLogItem
																.getOfflineTime(),
														boxStrs2);
										if (null != querygps2) {
											querygps2
													.remove(querygps2.size() - 1);
											gpsLists.add(querygps2);
										}
									} else {
										List<GPS> querygps = gpsDao
												.queryTerminalGpsPoint(
														alarmList.get(i - 1)
																.getAlarmTime(),
														alarmList.get(i)
																.getAlarmTime(),
														boxStrs2);
										if (null != querygps) {
											querygps.remove(querygps.size() - 1);
											gpsLists.add(querygps);
										}
									}
								}
							}
						}

					} else {
						List<GPS> querygps = gpsDao.queryTerminalGpsPoint(
								atuLoginLogItem.getLoginTime(),
								atuLoginLogItem.getOfflineTime(), boxStrs2);
						if (null != querygps) {
							querygps.remove(querygps.size() - 1);
							gpsLists.add(querygps);
						}
					}
				} else {
					// List<RealtimeAlarm> alarmList = alarmDao
					// .queryGPSByTimeAndBoxId(
					// atuLoginLogItem.getLoginTime(), null,
					// atuLoginLogItem.getBoxId());
					// if (null != alarmList && 0 != alarmList.size()) {
					// for (int i = 0; i < alarmList.size(); i++) {
					// if (i == 0) {
					// List<GPS> querygps = gpsDao
					// .queryTerminalGpsPoint(
					// atuLoginLogItem.getLoginTime(),
					// alarmList.get(i).getAlarmTime(),
					// boxStrs2);
					// if (null != querygps) {
					// querygps.remove(querygps.size() - 1);
					// gpsLists.add(querygps);
					// }
					// } else {
					// List<GPS> querygps = gpsDao
					// .queryTerminalGpsPoint(
					// alarmList.get(i - 1)
					// .getAlarmTime(),
					// alarmList.get(i).getAlarmTime(),
					// boxStrs2);
					// if (null != querygps) {
					// querygps.remove(querygps.size() - 1);
					// gpsLists.add(querygps);
					// }
					// }
					// }
					// } else {
					// List<GPS> querygps = gpsDao.queryTerminalGpsPoint(
					// atuLoginLogItem.getLoginTime(), null, boxStrs2);
					// if (null != querygps) {
					// querygps.remove(querygps.size() - 1);
					// gpsLists.add(querygps);
					// }
					// }
				}

			}
		} else {
			gpsLists.add(gpsAll);
		}
		if (null != gpsLists && 0 != gpsLists.size()) {
			for (List<GPS> list : gpsLists) {
				List<Object> list1 = new ArrayList<>();
				if (null != list && 0 != list.size()) {
					for (GPS gps2 : list) {
						GPSBean gpsBean = new GPSBean();
						gpsBean.setLatitude(gps2.getLatitude());
						gpsBean.setLongitude(gps2.getLongitude());
						list1.add(gpsBean);
					}
				}
				pointLists2.add(list1);
			}
		}
		EasyuiPageList pageListGsp2 = new EasyuiPageList();
		pageListGsp2.setRows(pointLists2);
		// 收集告警信息
		List<Object> alarmList = alarmDao.queryByTimeAndBoxId(begainTime,
				endTime, boxID);
		EasyuiPageList pageListAlarm = new EasyuiPageList();
		ArrayList<Object> listalarm = new ArrayList<Object>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"MM-dd HH:mm:ss.SSS");
		if (alarmList != null && 0 != alarmList.size()) {
			Map<Integer, AlarmCode> alarmCodeMap = CodeUtils.getAlarmCode();
			for (Object object : alarmList) {
				RealtimeAlarm alarm = (RealtimeAlarm) object;
				RealtimeAlarmBean realtimeAlarmBean = new RealtimeAlarmBean();
				if (0 != alarm.getAlarmClearTime().getTime()) {
					realtimeAlarmBean.setAlarmClearTime(alarm
							.getAlarmClearTime());
				}
				realtimeAlarmBean.setAlarmCode(String.valueOf(alarm
						.getAlarmCode()));
				realtimeAlarmBean.setAlarmReason(alarmCodeMap.get(
						alarm.getAlarmCode()).getReson());
				realtimeAlarmBean.setAlarmTime(alarm.getAlarmTime());
				realtimeAlarmBean.setAlarmType(alarmCodeMap.get(
						alarm.getAlarmCode()).getType());
				realtimeAlarmBean.setBoxId(alarm.getBoxId());
				realtimeAlarmBean.setChannelNo(String.valueOf(alarm
						.getChannelNo()));
				realtimeAlarmBean.setChannelType(CodeUtils.getChanneType(alarm
						.getChannelType()));
				listalarm.add(realtimeAlarmBean);
			}
			pageListAlarm.setRows(listalarm);
		}
		// 收集事件信息
		List<Object> eventList = eventDao.queryByTimeAndBoxId(begainTime,
				endTime, boxID);
		EasyuiPageList pageListEvent = new EasyuiPageList();
		ArrayList<Object> listEvent = new ArrayList<Object>();
		if (eventList != null && 0 != eventList.size()) {
			Map<Integer, AlarmCode> eventCodeMap = CodeUtils.getEventCode();
			for (Object object : eventList) {
				RealtimeEvent event = (RealtimeEvent) object;
				RealtimeEventBean eventBean = new RealtimeEventBean();
				eventBean.setBoxId(event.getBoxId());
				eventBean.setChannelNo(String.valueOf(event.getChannelNo()));
				eventBean.setChannelType(CodeUtils.getChanneType(event
						.getChannelType()));
				eventBean.setEventCode(String.valueOf(event.getEventCode()));
				eventBean.setEventName(eventCodeMap.get(event.getEventCode())
						.getName());
				eventBean.setEventTime(event.getEventTime());
				eventBean.setEventType(eventCodeMap.get(event.getEventCode())
						.getType());
				listEvent.add(eventBean);
			}
			pageListEvent.setRows(listEvent);
		}
		// 收集MOS信息
		List<Object> mosList = mosValueDao.queryByTimeAndBoxId(begainTime,
				endTime, boxID);
		EasyuiPageList pageListMos = new EasyuiPageList();
		ArrayList<Object> listMos = new ArrayList<Object>();
		if (mosList != null && 0 != mosList.size()) {
			for (Object object : mosList) {
				MosValue mosValue = (MosValue) object;
				MosValueBean mosValueBean = new MosValueBean();
				mosValueBean.setBoxId(mosValue.getBoxId());
				mosValueBean.setChannelNo(String.valueOf(mosValue
						.getChannelNo()));
				mosValueBean.setChannelType(CodeUtils.getChanneType(mosValue
						.getChannelType()));
				mosValueBean.setMosTime(mosValue.getMosTime());
				mosValueBean.setMosValue(mosValue.getMosValue());
				listMos.add(mosValueBean);
			}
			pageListMos.setRows(listMos);
		}
		// 收集状态报告信息
		List<Object> statusList = statusDao.queryByTimeAndBoxId(begainTime,
				endTime, boxID);
		EasyuiPageList pageListStatus = new EasyuiPageList();
		ArrayList<Object> listStatus = new ArrayList<Object>();
		if (statusList != null && 0 != statusList.size()) {
			for (Object object : statusList) {
				StatusReport statusReport = (StatusReport) object;
				StatusReportBean statusReportBean = new StatusReportBean();
				statusReportBean.setBoxId(statusReport.getBoxId());
				statusReportBean.setFilesLeft(statusReport.getFilesLeft());
				statusReportBean.setPowerMode(getPowerMode(statusReport
						.getPowerMode()));
				statusReportBean.setSpaceLeft(statusReport.getSpaceLeft());
				statusReportBean.setStatusReportTime(statusReport
						.getStatusReportTime());
				statusReportBean.setTemperature(statusReport.getTemperature());
				listStatus.add(statusReportBean);
			}
			pageListStatus.setRows(listStatus);
		}
		// 收集ATU登录日志信息
		List<ATULoginLogItem> ATULoginList = atuLogDao.queryByTimeAndBoxId(
				begainTime, endTime, boxStrs);
		EasyuiPageList pageListATU = new EasyuiPageList();
		ArrayList<Object> listATU = new ArrayList<Object>();
		if (ATULoginList != null && 0 != ATULoginList.size()) {
			for (ATULoginLogItem atuLoginLogItem : ATULoginList) {
				ATULoginLogItemBean atuLoginLogItemBean = new ATULoginLogItemBean();

				atuLoginLogItemBean.setBoxId(atuLoginLogItem.getBoxId());
				atuLoginLogItemBean.setFailReason(getFailReason(atuLoginLogItem
						.getFailReason()));
				atuLoginLogItemBean
						.setLoginTime(atuLoginLogItem.getLoginTime());
				atuLoginLogItemBean.setOfflineTime(atuLoginLogItem
						.getOfflineTime());
				atuLoginLogItemBean.setSessionId(String.valueOf(atuLoginLogItem
						.getSessionId()));
				atuLoginLogItemBean.setStatus(getStatus(atuLoginLogItem
						.getStatus()));
				atuLoginLogItemBean.setTestPlanVersion(String
						.valueOf(atuLoginLogItem.getTestPlanVersion()));
				listATU.add(atuLoginLogItemBean);
			}
			pageListATU.setRows(listATU);
		}
		// 将收集后的结果分类存进Map并返回
		hashMap.put("alarm", pageListAlarm);
		hashMap.put("event", pageListEvent);
		hashMap.put("mos", pageListMos);
		hashMap.put("status", pageListStatus);
		hashMap.put("ATULogin", pageListATU);
		hashMap.put("gsp", pageListGsp);
		hashMap.put("gsp2", pageListGsp2);
		return hashMap;
	}

	@Override
	public Set<String> queryGpsByTime(Date begainTime, Date endTime) {
		List<GPS> queryHistoryTerminalGpsPoint = gpsDao
				.queryHistoryTerminalGpsPoint(begainTime, endTime, null);
		Set<String> set = new HashSet<>();
		for (GPS gps : queryHistoryTerminalGpsPoint) {
			set.add(gps.getBoxId());
		}
		return set;
	}

	/**
	 * 将登出时间小于1小时的时间段合并
	 */
	public void machListATUInfo(List<ATULoginLogItem> list) {
		if (null != list && 0 != list.size()) {
			for (int i = 0; i < list.size(); i++) {
				if (0 == list.get(i).getOfflineTimeLong()) {
					list.remove(i);
				}
			}
			int count = 0;
			if (list.size() >= 2) {
				for (int i = 0; i < list.size() - 1; i += 2) {
					Long offlineTimeLong = list.get(i).getOfflineTimeLong();
					Long loginTimeLong = list.get(i + 1).getLoginTimeLong();
					if (3600 > (loginTimeLong - offlineTimeLong) / 1000) {
						list.get(i).setOfflineTime(
								list.get(i + 1).getOfflineTime());
						list.get(i).setOfflineTimeLong(
								list.get(i + 1).getOfflineTimeLong());
						list.remove(i + 1);
						count++;
					}
				}
			}
			if (0 != count) {
				machListATUInfo(list);
			}
			if (0 == count
					&& list.size() >= 3
					&& (list.get(list.size() - 1).getLoginTimeLong() - list
							.get(list.size() - 2).getOfflineTimeLong()) / 1000 < 3600) {
				list.get(list.size() - 2).setOfflineTime(
						list.get(list.size() - 1).getOfflineTime());
				list.get(list.size() - 2).setOfflineTimeLong(
						list.get(list.size() - 1).getOfflineTimeLong());
				list.remove(list.size() - 1);
			}
		}
	}

	/**
	 * 查询供电模式 0 内置电池供电 1 外电供电
	 * 
	 * @param number
	 * @return
	 */
	private String getPowerMode(Integer number) {
		if (number == 0) {
			return "内置电池供电";
		} else if (number == 1) {
			return "外电供电";
		} else {
			return "未知供电";
		}
	}

	/**
	 * 查询失败原因
	 * 
	 * @param number
	 * @return
	 */
	private String getFailReason(Integer number) {
		if (number == 0) {
			return "成功，没有错误";
		} else if (number == 1) {
			return "配置错误";
		} else if (number == 2) {
			return "终端ID非法";
		} else if (number == 3) {
			return "密码错误";
		} else if (number == 4) {
			return "已经登录";
		} else if (number == 5) {
			return "终端未登录";
		} else if (number == 6) {
			return "未知的指令";
		} else if (number == 7) {
			return "升级文件丢失";
		} else if (number == 8) {
			return "无效数据";
		} else if (number == 9) {
			return "无效数据包";
		} else if (number == 10) {
			return "打开文件失败";
		} else if (number == 11) {
			return "关闭文件失败";
		} else if (number == 12) {
			return "测试配置错误";
		} else if (number == 100) {
			return "超时离线";
		} else {
			return "未知错误";
		}
	}

	/**
	 * 查询登录状态
	 * 
	 * @param number
	 * @return
	 */
	private String getStatus(Integer number) {
		if (number == 0) {
			return "登录成功";
		} else if (number == 1) {
			return "登录失败";
		} else if (number == 2) {
			return "正常退出";
		} else if (number == 3) {
			return "退出异常";
		} else {
			return "未知状态";
		}
	}

	/**
	 * 时间减去一秒
	 * 
	 * @param number
	 * @return
	 */
	private Date getOtherDate(Date date) {
		// System.out.println(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, 1);
		// System.out.println(calendar.getTime());
		// return calendar.getTime();
		return null;
	}

}
