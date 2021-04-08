package com.datang.domain.monitor;

/**
 * 模式
 * 
 * @explain
 * @name PowerMode
 * @author shenyanwei
 * @date 2016年7月11日下午1:47:59
 */
public enum PowerMode {
	I, // 使用内置电池供电
	E, // 使用外电供电
	Unknown; // 未知供电模式

	/**
	 * 解析字符串为供电模式
	 * 
	 * @param powerMode
	 *            供电模式，为I或E或其他字符串
	 * @return 供电模式
	 */
	public static PowerMode parseString(String powerMode) {
		if (powerMode.equals("I")) {
			return I;
		} else if (powerMode.equals("E")) {
			return E;
		} else {
			return Unknown;
		}
	}

	/**
	 * 
	 */
	public String toString() {
		switch (this) {
		case I:
			return "电池供电";
		case E:
			return "外部供电";
		case Unknown:
			return "未知";
		default:
			return "未知";
		}
	}
}
