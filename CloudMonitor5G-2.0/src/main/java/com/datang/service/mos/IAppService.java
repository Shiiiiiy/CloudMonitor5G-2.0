/**
 * 
 */
package com.datang.service.mos;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.mos.AppTestInfo;


/**
 * app接口service接口
 * 
 * @author yinzhipeng
 * @date:2015年8月20日 下午5:24:32
 * @version
 */
public interface IAppService {

	/**
	 * 添加一条APP上传的测试信息
	 * 
	 * @param appTestInfo
	 */
	public void addAppTestInfo(AppTestInfo appTestInfo);

	/**
	 * 分页查询APP上传的测试信息
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList appTestInfoPageList(PageList pageList);
	
	/**
	 * 根据条件查询信息
	 * @author lucheng
	 * @date 2020年9月18日 下午5:20:14
	 * @param pageList
	 * @return
	 */
	public List<AppTestInfo> findAppTestInfo(PageList pageList);
	
	/**
	 * 查询城市排名
	 * @author lucheng
	 * @date 2020年9月21日 下午7:14:18
	 * @param pageList 参数值
	 * @param rankType 排名类型
	 * @return
	 */
	public Map<String, Object> findRankData(PageList pageList , String rankType);
	
	/**
	 * 分页查询城市维度感知分析的分析粒度平均值
	 * 
	 * @param pageList
	 * @return
	 */
	public List<AppTestInfo> getCityAnalysisInfo(PageList pageList);

	

}
