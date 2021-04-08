/**
 * 
 */
package com.datang.util;

import java.text.NumberFormat;

/**
 * 数值工具类
 * 
 * @author yinzhipeng
 * @date:2015年11月13日 上午9:39:42
 * @version
 */
public class NumberFormatUtils {
	private static final NumberFormat numberFormat = NumberFormat.getInstance();
	static {
		numberFormat.setMaximumFractionDigits(2);
	}

	/**
	 * 格式化小数,保留几位小数
	 * 
	 * @param number
	 *            需要格式化的小数
	 * @param maximumFractionDigits
	 *            保留的小数位数
	 * @return
	 */
	public static float format(float number, int maximumFractionDigits) {
		numberFormat.setMaximumFractionDigits(maximumFractionDigits);
		String format = numberFormat.format(number).replaceAll(",", "");
		float parseFloat = Float.parseFloat(format);
		return parseFloat;
	}

	/*
	 * 添加处以1000后保留三位小数
	 */
	public static Float dfUtil(Float number) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.###");
		return Float.valueOf(df.format(number / 1000));
	}

	/**
	 * 判断 参数是否为空(Float)
	 */
	public static Float isEmpty(Float f) {
		return f == null || f == 0.0 ? 0 : f;
	}

	/**
	 * 判断 参数是否为空(Long)
	 */
	public static Long isEmpty(Long l) {
		return l == null || l == 0 ? 0 : l;
	}

	public static void main(String[] args) {
		System.out.println(format((float) 3l / 5l, 2));
	}
}
