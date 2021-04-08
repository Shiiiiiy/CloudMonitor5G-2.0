package com.datang.util.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import com.datang.domain.testManage.terminal.ChannelType;

public class CodeUtils {

	public static Map<Integer, AlarmCode> getAlarmCode() {
		int a;
		int b = 5;
		List<AlarmEntity> alarmList = new ArrayList<AlarmEntity>();
		List<Sheet> list = readExcel("config/副本附录3：告警(20150617).xls");
		Sheet sheet = list.get(0);
		a = sheet.getRows();
		for (int i = 1; i < a; i++) {
			AlarmEntity entity = new AlarmEntity();
			for (int j = 0; j < b; j++) {
				if (j == 0) {
					entity.setStr1(sheet.getCell(j, i).getContents());
				} else if (j == 1) {
					entity.setStr2(sheet.getCell(j, i).getContents());
				} else if (j == 2) {
					entity.setStr3(sheet.getCell(j, i).getContents());
				} else if (j == 3) {
					entity.setStr4(sheet.getCell(j, i).getContents());
				} else if (j == 4) {
					entity.setStr5(sheet.getCell(j, i).getContents());
				} else {

				}
			}
			alarmList.add(entity);
		}
		Map<Integer, AlarmCode> map = new HashMap<Integer, AlarmCode>();
		for (AlarmEntity alarm : alarmList) {
			if (null != alarm.getStr1() && !alarm.getStr1().equals("")) {
				String[] split = alarm.getStr1().trim().split("x");
				AlarmCode alarmCode = new AlarmCode();
				alarmCode.setName(alarm.getStr3().trim());
				alarmCode.setType(alarm.getStr5().trim());
				alarmCode.setReson(alarm.getStr4().trim());
				map.put(Integer.parseInt(split[1], 16), alarmCode);
			}

		}
		return map;
	}

	/**
	 * 获取告警名称和类型信息
	 * 
	 * @param code
	 * @return
	 */
	public static Map<Integer, AlarmCode> getEventCode() {
		int a;
		int b = 5;
		List<AlarmEntity> alarmList = new ArrayList<AlarmEntity>();
		List<Sheet> list = readExcel("config/ATU上报事件列表.xls");
		Sheet sheet = list.get(0);
		a = sheet.getRows();
		for (int i = 1; i < a; i++) {
			AlarmEntity entity = new AlarmEntity();
			for (int j = 0; j < b; j++) {
				if (j == 0) {
					entity.setStr1(sheet.getCell(j, i).getContents());
				} else if (j == 1) {
					entity.setStr2(sheet.getCell(j, i).getContents());
				} else if (j == 2) {
					entity.setStr3(sheet.getCell(j, i).getContents());
				} else if (j == 3) {
					entity.setStr4(sheet.getCell(j, i).getContents());
				} else if (j == 4) {
					entity.setStr5(sheet.getCell(j, i).getContents());
				} else {

				}
			}
			alarmList.add(entity);
		}
		Map<Integer, AlarmCode> map = new HashMap<Integer, AlarmCode>();
		for (AlarmEntity alarm : alarmList) {
			if (null != alarm.getStr2() && !alarm.getStr2().equals("")) {
				String[] split = alarm.getStr2().trim().split("x");
				AlarmCode alarmCode = new AlarmCode();
				alarmCode.setName(alarm.getStr3().trim());
				alarmCode.setType(alarm.getStr1().trim());
				map.put(Integer.parseInt(split[1], 16), alarmCode);
			}

		}
		return map;
	}

	/**
	 * 获取事件图标号
	 * 
	 * @param code
	 * @return
	 */
	public static Map<Integer, String> getEventNumber() {
		int a;
		int b = 5;
		List<AlarmEntity> alarmList = new ArrayList<AlarmEntity>();
		List<Sheet> list = readExcel("config/ATU上报事件列表(图标).xls");
		Sheet sheet = list.get(0);
		a = sheet.getRows();
		for (int i = 1; i < a; i++) {
			AlarmEntity entity = new AlarmEntity();
			for (int j = 0; j < b; j++) {
				if (j == 0) {
					entity.setStr1(sheet.getCell(j, i).getContents());
				} else if (j == 1) {
					entity.setStr2(sheet.getCell(j, i).getContents());
				} else if (j == 2) {
					entity.setStr3(sheet.getCell(j, i).getContents());
				} else if (j == 3) {
					entity.setStr4(sheet.getCell(j, i).getContents());
				} else if (j == 4) {
					entity.setStr5(sheet.getCell(j, i).getContents());
				} else {

				}
			}
			alarmList.add(entity);
		}
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (AlarmEntity alarm : alarmList) {
			if (null != alarm.getStr2() && !alarm.getStr2().equals("")) {
				String[] split = alarm.getStr2().trim().split("x");
				map.put(Integer.parseInt(split[1], 16), alarm.getStr5());
			}

		}
		return map;
	}

	/**
	 * 根据代号获取通道类型
	 * 
	 * @param number
	 * @return
	 */
	public static String getChanneType(Integer number) {
		Map<Integer, String> map = new HashMap<>();
		for (ChannelType s : ChannelType.values()) {
			map.put(s.ordinal(), s.name());
		}
		map.put(-1, "未知类型");
		return map.get(number);
	}

	/**
	 * 从本地读取一个excel文件
	 * 
	 * @param sourceFilePath
	 *            excel文件路径
	 */
	public static List<Sheet> readExcel(String sourceFilePath) {
		List<Sheet> sheets = new ArrayList<Sheet>(0);
		InputStream is = null;
		try {
			is = ClassUtil.getResourceAsStream(sourceFilePath);
			Workbook wb = Workbook.getWorkbook(is);
			Sheet[] wbSheets = wb.getSheets();
			if (wbSheets != null && wbSheets.length != 0) {
				for (Sheet sheet : wbSheets) {
					if (sheet.getRows() != 0) {
						sheets.add(sheet);
					}
				}
			}
			return sheets;
		} catch (Exception e) {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e1) {
				// do nothing
			}
			e.printStackTrace();
			return sheets;
		}
	}

}
