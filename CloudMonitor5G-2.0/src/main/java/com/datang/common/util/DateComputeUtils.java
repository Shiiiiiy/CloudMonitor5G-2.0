package com.datang.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

	/**
	 *
	 * <p>Description: 本地时间转化为UTC时间</p>
	 * @param localTime
	 * @return
	 * @author wgs
	 * @date  2018年10月19日 下午2:23:43
	 *
	 */
	public static String localToUTC(Date localTime) {
		/** long时间转换成Calendar */
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(localTime.getTime());
		/** 取得时间偏移量 */
		int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
		/** 取得夏令时差 */
		int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
		/** 从本地时间里扣除这些差量，即可以取得UTC时间*/
		calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		/** 取得的时间就是UTC标准时间 */
		Date utcDate=new Date(calendar.getTimeInMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return sdf.format(utcDate);
	}


	public static Calendar toUtcDateSub2s(String date) {
		Calendar instance = toUtcDate(date);
		instance.add(Calendar.SECOND,-2);
		return instance;
	}
	public static Calendar toUtcDate(String date){
		for(int i=0;i<utcformats.length;i++){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(utcformats[i]);
				Date d = sdf.parse(date);
				Calendar instance = Calendar.getInstance();
				instance.setTime(d);
				return instance;
			} catch (ParseException e) {
				continue;
			}
		}
		throw  new RuntimeException("时间格式化异常!");
	}

	public static List<Map<String,Object>> getPre2sDatas(List<Map<String,Object>> result, String startTime) {
		if(startTime==null||result==null||result.isEmpty()){
			return null;
		}
		Calendar end = toUtcDate(startTime);
		Calendar start = toUtcDateSub2s(startTime);
		return result.stream().filter(item->{
			Calendar time = toUtcDate(item.get("time").toString());
				return time.before(end)&&time.after(start);
		}).collect(Collectors.toList());
	}

	public static List<Map<String,Object>> getPreDatas(List<Map<String,Object>> result, String startTime) {
		if(startTime==null||result.isEmpty()){
			return null;
		}
		Calendar end = toUtcDate(startTime);
		return result.stream().filter(item->{
			Calendar time = toUtcDate(item.get("time").toString());
			return time.before(end);
		}).collect(Collectors.toList());
	}

	/**
	 * 获取从开始时间往前到指定事件发生的记录之间的数据集
	 * @param result
	 * @param startTime
	 * @return
	 */
	public static List<Map<String,Object>> getPreDatasToEndEvt(List<Map<String,Object>> result, String startTime,String evt) {
		if(startTime==null||result.isEmpty()){
			return null;
		}
		List<Map<String,Object>> r=new ArrayList<>();
		Calendar end = toUtcDate(startTime);
		for(int i=result.size()-1;i>=0;i--){
			Calendar time = toUtcDate(result.get(i).get("time").toString());
			if(time.before(end)&&evt.equalsIgnoreCase(result.get(i).get("evtName").toString())){
				r.add(result.get(i));
			}
		}
		return r;
	}
	public static List<Map<String,Object>> getAfterDatas(List<Map<String,Object>> result, String startTime) {
		if(startTime==null||result.isEmpty()){
			return null;
		}
		Calendar end = toUtcDate(startTime);
		return result.stream().filter(item->{
			Calendar time = toUtcDate(item.get("time").toString());
			return time.after(end);
		}).collect(Collectors.toList());
	}

	public static List<List<Map<String,Object>>> getDatasBySplitEvt(List<Map<String,Object>> result, String evtName) {
		if(result.isEmpty()){
			return Collections.emptyList();
		}
		long evtName1 = result.stream().filter(o -> o.get("evtName").toString().equalsIgnoreCase(evtName)).count();
		if(evtName1<=0){
			return Collections.emptyList();
		}
		List<List<Map<String,Object>>> re=new ArrayList<>();
		int i=0;
		List<Integer> indexes=new ArrayList<>();
		
		for(Map<String,Object> o:result){
			if(o.get("evtName").toString().equalsIgnoreCase(evtName)){
				indexes.add(i);
			}
			i++;
		}
		if(indexes.size()>1){
			for(int m=0;m<indexes.size()-1;m++){
				re.add(result.subList(indexes.get(m),indexes.get(m+1)));
			}
		}else{
			re.add(result);
		}
		return re;
	}



	public static String formatTime(String date){
		Date d = null;
		try {
			String tempTime = date.replace("Z", " UTC");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
			d = sdf.parse(tempTime);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str= sdf1.format(d);
			return str;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Date formatDate(String time){
		try {
			return DateUtils.parseDate(time,formats2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	static String[] formats=new String[]{ "yyyy-MM-dd'T'HH:mm:ss Z","yyyy-MM-dd'T'HH:mm:ss.S Z","yyyy-MM-dd'T'HH:mm:ss.SS Z","yyyy-MM-dd'T'HH:mm:ss.SSS Z"};
	static String[] formats2=new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.S","yyyy-MM-dd HH:mm:ss.SS","yyyy-MM-dd HH:mm:ss.SSS"};
	static String[] utcformats=new String[]{ "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","yyyy-MM-dd'T'HH:mm:ss.SS'Z'","yyyy-MM-dd'T'HH:mm:ss.S'Z'","yyyy-MM-dd'T'HH:mm:ss'Z'"};

	public static synchronized String formatMicroTime(String date){
		SimpleDateFormat sdf;
		int index=0;
		if(date.contains(".")){
			String substring = date.substring(date.indexOf(".")+1, date.indexOf("Z"));
			index=substring.length();
		}
		sdf = new SimpleDateFormat(formats[index]);
		String tempTime = date.replace("Z", " UTC");
		try {
			Date d = sdf.parse(tempTime);
			SimpleDateFormat sdf1 = new SimpleDateFormat(formats2[index]);
			String str= sdf1.format(d);
			return str;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Double getDelay(String startTime,String endTime){
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			try {
				return (DateUtils.parseDate(endTime,formats2).getTime()-DateUtils.parseDate(startTime,formats2).getTime())*1.0/1000;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	public static String getDelay(Double startTime,Double endTime){
		if(null!=startTime&&null!=endTime){
				return (endTime-startTime)/1000+"";
		}
		return "";
	}
	
}
