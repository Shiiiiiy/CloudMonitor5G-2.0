package com.datang.service.monitor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.bean.monitor.RealtimeAlarmBean;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.monitor.AlarmDao;
import com.datang.domain.monitor.RealtimeAlarm;
import com.datang.service.monitor.AlarmService;
import com.datang.util.monitor.AlarmCode;
import com.datang.util.monitor.CodeUtils;

/**
 * 警告Service实现
 * 
 * @explain
 * @name AlarmServiceImpl
 * @author shenyanwei
 * @date 2016年7月12日下午1:47:58
 */
@Service
@Transactional
@SuppressWarnings("all")
public class AlarmServiceImpl implements AlarmService {

	@Autowired
	private AlarmDao alarmDao;

	@Override
	public AbstractPageList pageList(PageList pageList) {
		EasyuiPageList pageItem = alarmDao.getPageItem(pageList);
		ArrayList<RealtimeAlarmBean> list = new ArrayList<RealtimeAlarmBean>();
		List rows = pageItem.getRows();
		Map<Integer, AlarmCode> alarmCodeMap = CodeUtils.getAlarmCode();
		for (Object object : rows) {
			RealtimeAlarm alarm = (RealtimeAlarm) object;
			RealtimeAlarmBean realtimeAlarmBean = new RealtimeAlarmBean();
			if (0 != alarm.getAlarmClearTimeLong()) {
				realtimeAlarmBean.setAlarmClearTime(alarm.getAlarmClearTime());
			} else {
				realtimeAlarmBean.setAlarmClearTime(null);
			}

			realtimeAlarmBean
					.setAlarmCode(String.valueOf(alarm.getAlarmCode()));
			realtimeAlarmBean.setAlarmReason(alarmCodeMap.get(alarm
					.getAlarmCode()) != null ? alarmCodeMap.get(
					alarm.getAlarmCode()).getReson() : String.valueOf(alarm
					.getAlarmCode()));
			realtimeAlarmBean.setAlarmTime(alarm.getAlarmTime());
			realtimeAlarmBean.setAlarmType(alarmCodeMap.get(alarm
					.getAlarmCode()) != null ? alarmCodeMap.get(
					alarm.getAlarmCode()).getType() : String.valueOf(alarm
					.getAlarmCode()));
			realtimeAlarmBean.setBoxId(alarm.getBoxId());
			realtimeAlarmBean
					.setChannelNo(String.valueOf(alarm.getChannelNo()));
			realtimeAlarmBean.setChannelType(CodeUtils.getChanneType(alarm
					.getChannelType()));
			list.add(realtimeAlarmBean);
		}

		pageItem.setRows(list);
		return pageItem;

	}

}
