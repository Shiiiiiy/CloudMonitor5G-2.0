/**
 * 
 */
package com.datang.service.mos.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.mos.AppTestInfoDAO;
import com.datang.domain.mos.AppTestInfo;
import com.datang.service.mos.IAppService;



/**
 * @author yinzhipeng
 * @date:2015年8月20日 下午5:28:14
 * @version
 */
@Service
@Transactional
public class AppServiceImpl implements IAppService {
	@Autowired
	private AppTestInfoDAO appTestInfoDAO;



	@Override
	public void addAppTestInfo(AppTestInfo appTestInfo) {
		appTestInfoDAO.create(appTestInfo);
	}


	@Override
	public AbstractPageList appTestInfoPageList(PageList pageList) {
		return appTestInfoDAO.pageList(pageList);
	}
	
	@Override
	public List<AppTestInfo> findAppTestInfo(PageList pageList){
		return appTestInfoDAO.findAppTestInfo(pageList);
	}
	
	@Override
	public Map<String, Object> findRankData(PageList pageList , String rankType){
		return appTestInfoDAO.findRankData(pageList,rankType);
	}
	
	@Override
	public List<AppTestInfo> getCityAnalysisInfo(PageList pageList) {
		return appTestInfoDAO.getCityAnalysisInfo(pageList);
	}
}
