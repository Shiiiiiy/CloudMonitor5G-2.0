package com.datang.service.taskOrderManage;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.taskOrderManage.FixedPointTaskLogNamePojo;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;

/**
 * 定点测试任务service
 * @author lucheng
 *
 */
public interface CQTTaskOrderService {

	public AbstractPageList doPageQuery(PageList pageList);

	/**
	 * 通过id查询数据
	 * @author lucheng
	 * date:2020年2月18日 下午1:38:07
	 * @param id
	 * @return
	 */
	public FixedPointTaskOrderPojo find(Long id);

	/**
	 * 修改数据
	 * @author lucheng
	 * date:2020年2月18日 下午1:44:50
	 * @param svl
	 */
	public void update(FixedPointTaskOrderPojo svl);

	/**
	 * 根据id删除数据
	 * @author lucheng
	 * date:2020年2月18日 下午2:08:22
	 * @param idStr
	 */
	public void delete(String idStr);

	/**
	 * 根据城市查找对应的5G规划工参数据
	 * date:2020年8月15日 上午10:29:27
	 * @author lucheng
	 * @param cityId 
	 * @return
	 */
	public List<PlanParamPojo> getNrCell(Long cityId);
	List<PlanParamPojo> getNrCell(String cityId);
	

	/**
	 * Description:根据城市查找对应的4G规划工参数据
	 * @author lucheng
	 * @date 下午6:02:39 
	 * @param cityId
	 * @return
	 */
	public List<Plan4GParam> getLteCell(Long cityId);
	public List<Plan4GParam> getLteCell(String cityName);
	
	/**
	 * 根据条件查找已知的定点测试任务 
	 * @author lucheng
	 * @date 2020年8月18日 上午10:13:50
	 * @param pageList
	 * @return
	 */
	public List<FixedPointTaskOrderPojo> findFixedPointTask(PageList pageList);
	
	/**
	 * 根据工单编号查询相关的日志名称
	 * @author lucheng
	 * @date 2020年8月19日 上午11:24:22
	 * @param workOrder
	 * @return
	 */
	public List<FixedPointTaskLogNamePojo> getLogNamesByOrder(String workOrder);
	
	/**
	 * 保存CQT的新工单编号和日志名称
	 * @author lucheng
	 * @date 2020年8月20日 下午1:26:25
	 * @param fixedPointTaskLogNamePojo
	 */
	public void addCQTLogName(FixedPointTaskLogNamePojo fixedPointTaskLogNamePojo);


}
