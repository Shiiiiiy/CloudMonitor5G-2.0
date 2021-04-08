package com.datang.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期计算/格式化工具类
 * @author maxuancheng
 * @date 2019年6月19日
 */
public class DateComputeUtils {
	
	private static GregorianCalendar gc;
	
	/**
	 * 年计算
	 * @param date 时间
	 * @param i 加减年数
	 * @return date
	 */
	public static Date DateComputeOfYear(Date date,Integer i){
		if(gc == null){
			gc = getGregorianCalendar();
		}
		gc.setTime(date);
		gc.add(1, i);
		return gc.getTime();
	}

	/**
	 * 月份计算
	 * @param date 时间
	 * @param i 加减月数
	 * @return date
	 */
	public static Date DateComputeOfMonth(Date date,Integer i){
		if(gc == null){
			gc = getGregorianCalendar();
		}
		gc.setTime(date);
		gc.add(2, i);
		return gc.getTime();
	}
	
	
	/**
	 * 周份计算
	 * @param date 时间
	 * @param i 加减周数
	 * @return date
	 */
	public static Date DateComputeOfWeek(Date date,Integer i){
		if(gc == null){
			gc = getGregorianCalendar();
		}
		gc.setTime(date);
		gc.add(3, i);
		return gc.getTime();
	}
	
	/**
	 * 天份计算
	 * @param date 时间
	 * @param i 加减天数
	 * @return date
	 */
	public static Date DateComputeOfDay(Date date,Integer i){
		if(gc == null){
			gc = getGregorianCalendar();
		}
		gc.setTime(date);
		gc.add(5, i);
		return gc.getTime();
	}
	
	/**
	 * 小时计算
	 * @param date 时间
	 * @param i 加减小时数
	 * @return date
	 */
	public static Date DateComputeOfHour(Date date,Integer i){
		if(gc == null){
			gc = getGregorianCalendar();
		}
		gc.setTime(date);
		gc.add(Calendar.HOUR, i);
		return gc.getTime();
	}
	
	private static GregorianCalendar getGregorianCalendar(){
		if(gc == null){
			return new GregorianCalendar();
		}
		return gc;
	}
	
}
