package com.datang.service.monitor.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.bean.monitor.MosValueBean;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.monitor.MosValueDao;
import com.datang.domain.monitor.MosValue;
import com.datang.service.monitor.MosValueService;
import com.datang.util.NumberFormatUtils;
import com.datang.util.monitor.CodeUtils;

/**
 * @explain
 * @name MosValueServiceImpl
 * @author shenyanwei
 * @date 2016年7月12日下午1:51:40
 */
@Service
@Transactional
@SuppressWarnings("all")
public class MosValueServiceImpl implements MosValueService {

	@Autowired
	private MosValueDao mosValueDao;

	@Override
	public AbstractPageList pageList(PageList pageList) {
		EasyuiPageList pageItem = mosValueDao.getPageItem(pageList);
		ArrayList<MosValueBean> list = new ArrayList<MosValueBean>();
		List rows = pageItem.getRows();
		for (Object object : rows) {
			MosValue mosValue = (MosValue) object;
			MosValueBean mosValueBean = new MosValueBean();
			mosValueBean.setBoxId(mosValue.getBoxId());
			mosValueBean.setChannelNo(String.valueOf(mosValue.getChannelNo()));
			mosValueBean.setChannelType(CodeUtils.getChanneType(mosValue
					.getChannelType()));
			mosValueBean.setMosTime(mosValue.getMosTime());
			mosValueBean.setMosValue(NumberFormatUtils.format(
					mosValue.getMosValue(), 3));
			list.add(mosValueBean);
		}
		pageItem.setRows(list);
		return pageItem;

	}

}
