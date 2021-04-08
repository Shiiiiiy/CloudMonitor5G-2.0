package com.datang.service.testPlan;

import java.util.Collection;
import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.testPlan.TestPlan;
import com.datang.web.beans.testPlan.TestPlanQuery;

/**
 * @author songzhigang
 * @version 1.0.0, 2010-12-27
 */
public interface TestPlanService {
	/**
	 * 添加测试计划
	 * 
	 * @param testPlan
	 *            TestPlan
	 */
	public void addTestPlan(TestPlan testPlan);

	/**
	 * 通过id查找测试计划
	 * 
	 * @param id
	 *            Integer
	 */
	public TestPlan getTestPlan(Integer id);

	/**
	 * 更新测试计划
	 * 
	 * @param testPlan
	 *            TestPlan
	 */
	public void updateTestPlan(TestPlan testPlan);

	/**
	 * 通过计划名称查找计划
	 * 
	 * @param planName
	 *            String
	 * @return TestPlan对象
	 */
	public TestPlan getTestPlanByName(String planName);

	/**
	 * 通过版本号获取测试计划
	 * 
	 * @param version
	 *            版本号
	 * @return 测试计划
	 */
	public TestPlan getTestPlanByVersion(Integer version);

	/**
	 * 根据查询条件查询测试计划
	 * 
	 * @param testPlanQuery
	 *            TestPlanQuery
	 * @return 查询到的结果
	 */
	public Collection<TestPlan> queryTestPlan(TestPlanQuery testPlanQuery);

	/**
	 * 通过id删除测试计划
	 * 
	 * @param id
	 *            Integer
	 */
	public void deleteTestPlan(Integer id);

	/**
	 * 下发测试计划
	 * 
	 * @param testPlanId
	 *            测试计划id
	 * @param terminalIds
	 *            下发的终端id的集合
	 */
	// yzp
	// public BatchResult sendTestPlan(Integer testPlanId,
	// Collection<Long> terminalIds);

	/**
	 * 下发测试计划
	 * 
	 * @modify yinzhipeng
	 * @param testPlanId
	 *            测试计划id
	 * @param terminalIds
	 *            下发的终端id
	 */
	public void sendTestPlan(Integer testPlanId, Long terminalId);

	/**
	 * 更新或保存测试计划
	 * 
	 * @param testPlan
	 *            TestPlan
	 */
	public void saveOrUpadate(TestPlan testPlan);

	/**
	 * 得到所有的测试计划
	 * 
	 * @return Collection<TestPlan>
	 */
	public Collection<TestPlan> queryAll();

	/**
	 * 多条件组合分页
	 * 
	 * @author yinzhipeng
	 * @param pageList
	 * @return
	 */
	public AbstractPageList queryPageTestPlan(PageList pageList);

	/**
	 * 获取某个终端下的测试计划最大的版本号
	 * 
	 * @param terminalId
	 * @return
	 */
	public Long queryTestPlanLastVersion(Long terminalId);
	
	/**
	 * 根据条件查找测试计划
	 * @author lucheng
	 * @date 2020年8月27日 下午4:29:23
	 * @param pageList
	 * @return
	 */
	public List<TestPlan> queryTestPlanByBoxid(PageList pageList);

}
