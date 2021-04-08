package com.datang.service.errorLogManage.impl;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.errorLogManage.ErrorLogManagePojo;

public interface ErrorLogManageService {

	/**
	 * 根据条件获取数据
	 * @author maxuancheng
	 * @param pageList
	 * @return
	 */
	public AbstractPageList getPageDataOfFactor(PageList pageList);

	/**
	 * 数据接收并存储
	 * @author maxuancheng
	 * @param errorLogManagePojo
	 */
	public Boolean errorLogManageService(ErrorLogManagePojo errorLogManagePojo);

	/**
	 * 根据城市名获取APP或Outum最高版本号和用户数(所有的)
	 * @author maxuancheng
	 * @param cityName
	 * @return
	 */
	public Map<String, Object> getVersionAndUserNumber(String cityName);

	/**
	 * 根据boxid集合获取APP或Outum最高版本号和用户数
	 * @author maxuancheng
	 * @param cityName
	 * @param list
	 * @return
	 */
	public Map<String, Object> getOnLineVersionAndUserNumber(List<Object> list);

	/**
	 * 根据城市名获取终端类型统计
	 * @author maxuancheng
	 * @param cityName 为null时获取所有城市
	 * @return
	 */
	public List<Object> getTerminalNumber(String cityName);

	/**
	 * 获取用户活跃度和经纬度
	 * @author maxuancheng
	 * @param colorList 
	 * @return
	 */
	public List<List<Object>> getUserPlaceShow(List<String> colorList);

}
