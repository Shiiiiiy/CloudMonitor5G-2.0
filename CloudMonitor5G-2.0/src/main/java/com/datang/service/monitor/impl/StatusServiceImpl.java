package com.datang.service.monitor.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.bean.monitor.StatusReportBean;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.monitor.StatusDao;
import com.datang.domain.monitor.StatusReport;
import com.datang.service.monitor.StatusService;

/**
 * @explain
 * @name StatusServiceImpl
 * @author shenyanwei
 * @date 2016年7月12日下午1:56:40
 */
@Service
@Transactional
@SuppressWarnings("all")
public class StatusServiceImpl implements StatusService {
	/**
    *
    */
	@Autowired
	private StatusDao statusDao;

	@Override
	public AbstractPageList pageList(PageList pageList) {
		EasyuiPageList pageItem = statusDao.getPageItem(pageList);
		ArrayList<StatusReportBean> list = new ArrayList<StatusReportBean>();
		List rows = pageItem.getRows();
		for (Object object : rows) {
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
			list.add(statusReportBean);
		}

		pageItem.setRows(list);
		return pageItem;

	}

	private String getPowerMode(Integer number) {
		if (number == 0) {
			return "内置电池供电";
		} else if (number == 1) {
			return "外电供电";
		} else {
			return "未知供电";
		}
	}
}
