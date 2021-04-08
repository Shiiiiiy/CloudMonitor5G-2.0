package com.datang.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.datang.web.beans.testPlan.DateBean;

public class DateBeanUtils {

	/**
	 * 通过dateString生成List<DateBean>
	 * 
	 * @param dateString
	 *            String
	 * @return 生成List<DateBean>
	 */
	public static List<DateBean> parse(String dateString) {

		List<DateBean> dateBeans = new ArrayList<DateBean>();
		if (dateString == null || "".equals(dateString)) {
			return null;
		}
		String[] dateStrs = dateString.split(",");
		Arrays.sort(dateStrs);
		if (dateStrs.length == 1) {
			DateBean dateBean = new DateBean();
			dateBean.setStartDate(dateStrs[0]);
			dateBean.setEndDate(dateStrs[0]);
			dateBeans.add(dateBean);
		} else {
			String start = dateStrs[0];
			String end = dateStrs[0];
			for (int i = 0; i < dateStrs.length - 1; i++) {
				try {
					long days = DateUtil.getDistinceDay(dateStrs[i],
							dateStrs[i + 1]);
					if (days != 1) {
						DateBean dateBean = new DateBean();
						dateBean.setStartDate(start);
						dateBean.setEndDate(dateStrs[i]);
						end = dateStrs[i];
						start = dateStrs[i + 1];
						dateBeans.add(dateBean);
					}
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
			if (!end.equals(dateStrs[dateStrs.length - 1])) {
				DateBean dateBean = new DateBean();
				dateBean.setStartDate(start);
				dateBean.setEndDate(dateStrs[dateStrs.length - 1]);
				dateBeans.add(dateBean);
			}
		}

		return dateBeans;

	}

	/**
	 * 通过List<DateBean>生成日期的串
	 * 
	 * @param dateBeans
	 * @return
	 */
	public static String parse(List<DateBean> dateBeans) {
		if (dateBeans == null) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (DateBean dateBean : dateBeans) {
			String currentDay = dateBean.getStartDate();
			while (!dateBean.getEndDate().equals(currentDay)) {
				stringBuilder.append(currentDay);
				stringBuilder.append(",");
				try {
					currentDay = DateUtil.nextDayString(currentDay);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
			stringBuilder.append(currentDay);
			stringBuilder.append(",");
		}
		String result = stringBuilder.toString();
		return result.substring(0, result.length() - 1);
	}

}
