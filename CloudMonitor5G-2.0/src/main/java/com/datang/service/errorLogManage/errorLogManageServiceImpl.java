package com.datang.service.errorLogManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.errorLogManage.ErrorLogManageDao;
import com.datang.domain.errorLogManage.ErrorLogManagePojo;
import com.datang.service.errorLogManage.impl.ErrorLogManageService;

/**
 * 错误日志Service层
 * @author maxuancheng
 * @date 2019年5月30日
 */
@Service
@Transactional
@SuppressWarnings("all")
public class errorLogManageServiceImpl implements ErrorLogManageService {

	@Autowired
	private ErrorLogManageDao errorLogManageDao;
	
	@Override
	public EasyuiPageList getPageDataOfFactor(PageList pageList) {
		return errorLogManageDao.getPageDataOfFactor(pageList);
	}

	@Override
	public Boolean errorLogManageService(ErrorLogManagePojo errorLogManagePojo) {
		
		return errorLogManageDao.errorLogManageService(errorLogManagePojo);
	}

	@Override
	public Map<String, Object> getVersionAndUserNumber(String cityName) {
		Map<String, Object> map = errorLogManageDao.getVersionAndUserNumber(cityName);
		return map;
	}

	@Override
	public Map<String, Object> getOnLineVersionAndUserNumber(List<Object> list) {
		
		return errorLogManageDao.getOnLineVersionAndUserNumber(list);
	}

	@Override
	public List<Object> getTerminalNumber(String cityName) {
		
		return errorLogManageDao.getTerminalNumber(cityName);
	}

	@Override
	public List<List<Object>> getUserPlaceShow(List<String> colorList) {
		
		return errorLogManageDao.getUserPlaceShow(colorList);
	}

}
