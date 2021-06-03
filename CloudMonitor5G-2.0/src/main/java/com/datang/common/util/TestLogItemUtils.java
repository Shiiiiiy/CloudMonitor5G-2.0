/**
 * 
 */
package com.datang.common.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 测试日志工具类
 * 
 * @author yinzhipeng
 * @date:2015年11月23日 下午4:10:01
 * @version
 */
public class TestLogItemUtils {
	/**
	 * 汇总开始时间和结束时间，开始时间必须是所有日志开始时间的的最小时间， 结束时间必须是所有日志结束时间的最大时间
	 * 
	 * @param testLogs
	 * @return
	 */
	public static Map<String, Object> amountBeginEndDate(
			List<TestLogItem> testLogs) {
		Map<String, Object> map = new LinkedHashMap<>();
		if (!CollectionUtils.isEmpty(testLogs)) {
			List<Date> beginDateList = new ArrayList<Date>();
			List<Date> endDateList = new ArrayList<Date>();
			for (TestLogItem testLog : testLogs) {
				Date startDate = testLog.getStartDate();
				Date endDate = testLog.getEndDate();
				if (startDate != null) {
					beginDateList.add(startDate);
				}
				if (endDate != null) {
					endDateList.add(endDate);
				}
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			if (beginDateList.size() != 0) {
				Collections.sort(beginDateList);
				Date min = beginDateList.get(0);
				map.put("startDate", simpleDateFormat.format(min));
			}
			if (endDateList.size() != 0) {
				Collections.sort(endDateList);
				Date max = endDateList.get(endDateList.size() - 1);
				map.put("endDate", simpleDateFormat.format(max));
			}
		}
		return map;
	}

	/**
	 * 汇总测试总里程,测试总时长,平均车速,二级域名,测试占比最高的业务类型,测试终端数量
	 * 
	 * @param testLogs
	 * @param map
	 * @return
	 */
	public static Map<String, Object> amountTestLog(List<TestLogItem> testLogs,
			Map<String, Object> map) {
		if (!CollectionUtils.isEmpty(testLogs)) {
			// 记录汇总信息
			Float distanceValue = null;
			Long testTimeValue = null;
			String terminalGroupValue = null;
			String serviceTypeValue = null;
			Long timeValue = null;
			Set<String> terminalBoxids = new HashSet<>();
			// 计算汇总信息
			for (TestLogItem testLog : testLogs) {
				Float distance = testLog.getDistance();
				Date startDate = testLog.getStartDate();
				Date endDate = testLog.getEndDate();
				String terminalGroup = testLog.getTerminalGroup();
				String serviceType = testLog.getServiceType();
				Terminal terminal = testLog.getTerminal();

				if (distance != null) {
					distanceValue = ((null == distanceValue) ? distance
							: (distanceValue + distance));
				}
				if (null != startDate && null != endDate) {
					testTimeValue = ((null == testTimeValue) ? (endDate
							.getTime() - startDate.getTime()) : (testTimeValue
							+ endDate.getTime() - startDate.getTime()));
					long time = endDate.getTime() - startDate.getTime();
					if (null == timeValue) {
						terminalGroupValue = terminalGroup;
						serviceTypeValue = serviceType;
					} else {
						terminalGroupValue = (time - timeValue) >= 0 ? terminalGroup
								: terminalGroupValue;
						serviceTypeValue = (time - timeValue) >= 0 ? serviceType
								: serviceTypeValue;
					}
					timeValue = time;
				}
				if (null != terminal && null != terminal.getBoxId()) {
					terminalBoxids
							.add(terminal.getBoxId().trim().toUpperCase());
				}
			}
			NumberFormat instance = NumberFormat.getInstance();
			instance.setMaximumFractionDigits(2);
			// 处理汇总信息
			// 处理测试总里程
			if (null != distanceValue) {
				map.put("testTotalDistance",
						instance.format(distanceValue / 1000).replaceAll(",",
								""));
			}
			// 处理测试总时长
			if (null != testTimeValue) {
				map.put("testTotalTime", dateTime2String(testTimeValue));
			}
			// 处理平均车速
			if (null != testTimeValue && 0l != testTimeValue
					&& null != distanceValue) {
				map.put("testTotalSpeedAvg",
						instance.format(
								(double) distanceValue * 3600 / testTimeValue)
								.replaceAll(",", ""));
			}
			// 处理二级域名
			if (null != terminalGroupValue) {
				map.put("testTotalTerminalGroup", terminalGroupValue);
			}
			// 处理测试占比最高的业务类型
			if (null != serviceTypeValue) {
				map.put("testTotalServiceType", serviceTypeValue);
			}
			// 处理测试终端数量
			if (null != terminalBoxids && 0 != terminalBoxids.size()) {
				map.put("testTotalTerminalNum", terminalBoxids.size());
			}
		}
		return map;
	}

	/**
	 * 处理日志的类型,是原始日志还是对比日志
	 * 
	 * @param map
	 * @param valueType
	 * @return
	 */
	public static Map<String, Object> amountTestLogValueType(
			Map<String, Object> map, int valueType) {
		if (0 == valueType) {
			map.put("valueType", "原始");
		}
		if (1 == valueType) {
			map.put("valueType", "对比");
		}
		return map;
	}

	/**
	 * 处理测试日志文件名,测试日志二级域
	 * 
	 * @param testLogs
	 * @param rows
	 * @return
	 */
	public static List<Map<String, Object>> amountFileNameAndTerminalGroup(
			List<TestLogItem> testLogs, List<Map<String, Object>> rows) {
		if (null != testLogs && null != rows) {
			outter: for (Map<String, Object> map : rows) {
				Object recseqno = map.get(ParamConstant.RECSEQNO.toLowerCase());
				if (null != recseqno) {
					try {
						Long mapRecseqno = (Long) Long.parseLong(recseqno
								.toString());
						inner: for (TestLogItem testLog : testLogs) {
							Long recSeqNo = testLog.getRecSeqNo();
							if (!mapRecseqno.equals(recSeqNo)) {
								continue inner;
							}
							map.put("testLogFileName", testLog.getFileName());
							map.put("testLogItem", testLog);
							map.put("testLogTerminalGroup",
									testLog.getTerminalGroup());
						}
					} catch (NumberFormatException e) {
					}
				} else {
					map.put("testLogFileName", "汇总");
				}
			}
		}
		return rows;
	}

	/**
	 * 毫秒值换算成HH:mm:ss.SSS
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String dateTime2String(long dateTime) {
		if (dateTime == 0) {
			return "00:00:00.000";
		} else {
			// 毫秒值换算成hh:mm:ss.sss
			String hh = String
					.valueOf((long) ((dateTime / 1000 / 60 / 60) % 100));// 最多存100个小时
			String mm = String.valueOf((long) ((dateTime / 1000 / 60) % 60));
			String ss = String.valueOf((long) ((dateTime / 1000) % 60));
			String sss = String.valueOf(Math.round(dateTime % 1000));
			switch (hh.length()) {
			case 1:
				hh = "0" + hh;
				break;
			default:
				break;
			}
			switch (mm.length()) {
			case 1:
				mm = "0" + mm;
				break;

			default:
				break;
			}
			switch (ss.length()) {
			case 1:
				ss = "0" + ss;
				break;
			default:
				break;
			}
			switch (sss.length()) {
			case 1:
				sss = "00" + sss;
				break;
			case 2:
				sss = "0" + sss;
				break;
			default:
				break;
			}
			return hh + ":" + mm + ":" + ss + "." + sss;
		}

	}

	/**
	 * 根据汇总方式处理测试日志添加信息
	 * 
	 * @param testLogs
	 * @param rows
	 * @return
	 */
	public static List<Map<String, Object>> amountInfoByCollectTypes(
			List<TestLogItem> testLogs, List<Map<String, Object>> rows,
			String collectType, Integer index, Map<String, String> provinceName) {
		if (null != testLogs && null != rows) {
			outter: for (Map<String, Object> map : rows) {
				if (index == 1) {
					map.put("country", collectType);
				} else if (index == 2) {
					String[] collectTypes = collectType.trim().split(",");
					// 存储TestLogItem的id集合
					List<String> ids = new ArrayList<>();
					for (int i = 0; i < collectTypes.length; i++) {
						if (StringUtils.hasText(collectTypes[i])) {
							try {
								ids.add(collectTypes[i].trim());
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
					map.put("country", ids.get(0));
					map.put("province", ids.get(1));
				} else if (index == 3) {
					String[] collectTypes = collectType.trim().split(",");
					// 存储TestLogItem的id集合
					List<String> ids = new ArrayList<>();
					for (int i = 0; i < collectTypes.length; i++) {
						if (StringUtils.hasText(collectTypes[i])) {
							try {
								ids.add(collectTypes[i].trim());
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
					map.put("country", ids.get(0));
					map.put("province", ids.get(1));
					map.put("testLogTerminalGroup", ids.get(2));
				} else if (index == 4) {
					String[] collectTypes = collectType.trim().split(",");
					// 存储TestLogItem的id集合
					List<String> ids = new ArrayList<>();
					for (int i = 0; i < collectTypes.length; i++) {
						if (StringUtils.hasText(collectTypes[i])) {
							try {
								ids.add(collectTypes[i].trim());
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
					map.put("country", ids.get(0));
					map.put("province", ids.get(1));
					map.put("testLogTerminalGroup", ids.get(2));
					map.put("boxId", ids.get(3));
				} else if (index == 5) {
					String[] collectTypes = collectType.trim().split(",");
					// 存储TestLogItem的id集合
					List<String> ids = new ArrayList<>();
					for (int i = 0; i < collectTypes.length; i++) {
						if (StringUtils.hasText(collectTypes[i])) {
							try {
								ids.add(collectTypes[i].trim());
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
					map.put("country", ids.get(0));
					map.put("province", ids.get(1));
					map.put("testLogTerminalGroup", ids.get(2));
					map.put("boxId", ids.get(3));
					map.put("testPlay", ids.get(4));
				} else if (index == 6) {
					Object recseqno = map.get(ParamConstant.RECSEQNO
							.toLowerCase());
					if (null != recseqno) {
						try {
							Long mapRecseqno = (Long) Long.parseLong(recseqno
									.toString());
							inner: for (TestLogItem testLog : testLogs) {
								Long recSeqNo = testLog.getRecSeqNo();
								if (!mapRecseqno.equals(recSeqNo)) {
									continue inner;
								}
								map.put("country", "全国");

								map.put("province",
										provinceName.get(testLog.getBoxId()));
								map.put("testLogTerminalGroup",
										testLog.getTerminalGroup());
								map.put("boxId", testLog.getBoxId());
								map.put("testPlay", testLog.getTestPlanName());
								map.put("testLogFileName",
										testLog.getFileName());
							}
						} catch (NumberFormatException e) {
						}
					} else {
						map.put("testLogFileName", "日志汇总");
					}
				} else if (index == 7) {
					String[] collectTypes = collectType.trim().split(",");
					// 存储TestLogItem的id集合
					List<String> ids = new ArrayList<>();
					for (int i = 0; i < collectTypes.length; i++) {
						if (StringUtils.hasText(collectTypes[i])) {
							try {
								ids.add(collectTypes[i].trim());
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
					map.put("country", ids.get(0));
					map.put("province", ids.get(1));
					map.put("testLogTerminalGroup", ids.get(2));
					map.put("boxId", ids.get(3));
					map.put("testPlay", ids.get(4));
					map.put("testLogFileName", ids.get(5));
					map.put("floorName", ids.get(6));
				} else if (index == 8) {
					Object recseqno = map.get(ParamConstant.RECSEQNO
							.toLowerCase());
					if (null != recseqno) {
						try {
							Long mapRecseqno = (Long) Long.parseLong(recseqno
									.toString());
							inner: for (TestLogItem testLog : testLogs) {
								Long recSeqNo = testLog.getRecSeqNo();
								if (!mapRecseqno.equals(recSeqNo)) {
									continue inner;
								}
								map.put("country", "全国");
								map.put("province",
										provinceName.get(testLog.getBoxId()));
								map.put("testLogTerminalGroup",
										testLog.getTerminalGroup());
								map.put("boxId", testLog.getBoxId());
								map.put("testPlay", testLog.getTestPlanName());
								map.put("testLogFileName",
										testLog.getFileName());
								map.put("floorName", testLog.getFloorName());
							}
						} catch (NumberFormatException e) {
						}
					} else {
						map.put("keypointno", "场景汇总");
					}

				} else if (index == 9) {
					Object recseqno = map.get(ParamConstant.RECSEQNO
							.toLowerCase());
					if (null != recseqno) {
						try {
							Long mapRecseqno = (Long) Long.parseLong(recseqno
									.toString());
							inner: for (TestLogItem testLog : testLogs) {
								Long recSeqNo = testLog.getRecSeqNo();
								if (!mapRecseqno.equals(recSeqNo)) {
									continue inner;
								}
								map.put("country", "全国");
								map.put("province",
										provinceName.get(testLog.getBoxId()));
								map.put("testLogTerminalGroup",
										testLog.getTerminalGroup());
								map.put("boxId", testLog.getBoxId());
								map.put("testPlay", testLog.getTestPlanName());
								map.put("testLogFileName",
										testLog.getFileName());
								map.put("floorName", testLog.getFloorName());
							}
						} catch (NumberFormatException e) {
						}
					} else {
						map.put("floorno", "楼层汇总");
					}
				}

			}
		}
		return rows;
	}



	/**
	 * 加序号
	 * */
	public static void addId(List<Map<String,Object>> list){
		if(list==null) return;
		for(int i=0;i<list.size();i++){
			list.get(i).put("id",i);
		}
	}
	/**
	 * 转数字
	 * */
	public static void formatNumber(List<Map<String,Object>> list){
		for(Map<String,Object> map:list){
			for(Map.Entry<String,Object> entry:map.entrySet()){
				try{
					if(entry.getValue()==null){
						continue;
					}
					if(NumberUtils.isNumber(entry.getValue().toString())){
						entry.setValue(NumberUtils.createDouble(entry.getValue().toString()));
					}
				}catch (Exception e){

				}
			}
		}
	}

}
