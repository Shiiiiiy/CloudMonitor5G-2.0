package com.datang.service.monitor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.bean.monitor.RealtimeEventBean;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.monitor.EventDao;
import com.datang.domain.monitor.RealtimeEvent;
import com.datang.service.monitor.EventService;
import com.datang.util.monitor.AlarmCode;
import com.datang.util.monitor.CodeUtils;

/**
 * @explain
 * @name EventServiceImpl
 * @author shenyanwei
 * @date 2016年7月12日下午1:50:04
 */
@Service
@Transactional
@SuppressWarnings("all")
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;

	@Override
	public AbstractPageList pageList(PageList pageList) {
		EasyuiPageList pageItem = eventDao.getPageItem(pageList);
		ArrayList<RealtimeEventBean> list = new ArrayList<RealtimeEventBean>();
		List rows = pageItem.getRows();
		Map<Integer, AlarmCode> eventCodeMap = CodeUtils.getEventCode();
		for (Object object : rows) {
			RealtimeEvent event = (RealtimeEvent) object;
			RealtimeEventBean eventBean = new RealtimeEventBean();
			eventBean.setBoxId(event.getBoxId());
			eventBean.setChannelNo(String.valueOf(event.getChannelNo()));
			eventBean.setChannelType(CodeUtils.getChanneType(event
					.getChannelType()));
			eventBean.setEventCode(String.valueOf(event.getEventCode()));
			eventBean
					.setEventName(eventCodeMap.get(event.getEventCode()) != null ? eventCodeMap
							.get(event.getEventCode()).getName() : String
							.valueOf(event.getEventCode()));
			eventBean.setEventTime(event.getEventTime());
			eventBean
					.setEventType(eventCodeMap.get(event.getEventCode()) != null ? eventCodeMap
							.get(event.getEventCode()).getType() : String
							.valueOf(event.getEventCode()));
			list.add(eventBean);
		}

		pageItem.setRows(list);
		return pageItem;

	}

}
