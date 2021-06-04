package com.datang.dao.testPlan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testPlan.TestPlan;
import com.datang.web.beans.testPlan.TestPlanQuery;

/**
 * @author songzhigang
 * @version 1.0.0, 2010-12-27
 */
@SuppressWarnings("unchecked")
@Repository
public class TestPlanDao extends GenericHibernateDao<TestPlan, Integer> {

	/**
	 * 分页查询测试计划
	 * 
	 * @param planQuery
	 *            查询条件
	 * @return 查询出的结果
	 */
	public Collection<TestPlan> queryList(TestPlanQuery planQuery) {

		Criteria criteria = this.getHibernateSession().createCriteria(
				TestPlan.class);
		if (null != planQuery.getName() && !"".equals(planQuery.getName())) {
			criteria.add(Expression.like("name", planQuery.getName(),
					MatchMode.ANYWHERE));
		}
		if (null != planQuery.getVersion() && planQuery.getVersion() != 0) {
			Criteria autoTestUnitCriteria = criteria
					.createCriteria("autoTestUnit");
			autoTestUnitCriteria.add(Restrictions.eq("version",
					planQuery.getVersion()));
		}
		if (planQuery.getSended() != null) {
			criteria.add(Expression.eq("sended", planQuery.getSended()));
		}
		if (planQuery.getTestPlanIds() != null) {
			if (planQuery.getTestPlanIds().isEmpty()) {
				planQuery.setPagerCount(0);
				return new ArrayList<TestPlan>();
			}
			criteria.add(Expression.in("id", planQuery.getTestPlanIds()));
		}

		criteria.addOrder(Order.desc("createDate"));
		criteria.setFirstResult(planQuery.getPagerOffset());
		criteria.setMaxResults(planQuery.getPagerPerPage());
		Collection<TestPlan> result = criteria.list();
		planQuery.setQueryResults(result);
		if (planQuery.getPagerCount() == -1) {
			int count = (Integer) criteria
					.setProjection(Projections.rowCount()).uniqueResult();
			planQuery.setPagerCount(count);
		}
		return result;
	}

	/**
	 * 通过名称查找测试计划
	 * 
	 * @param name
	 * @param version
	 * @return
	 */
	public Collection<TestPlan> findTestPlan(String name) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestPlan.class);

		criteria.add(Expression.eq("name", name));

		// Criteria autoTestUnitCriteria =
		// criteria.createCriteria("autoTestUnit");
		// autoTestUnitCriteria.add(Restrictions.eq("version", version));

		return criteria.list();
	}

	/**
	 * 通过名称得到测试计划
	 * 
	 * @param testPlanName
	 *            测试计划名称
	 * @return 测试计划列表
	 */
	public TestPlan getTestPlanByName(String testPlanName) {
		String hql = "from TestPlan testPlan where testPlan.name =:testPlanName";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("testPlanName", testPlanName);
		Object reObject = query.uniqueResult();
		if (reObject != null) {
			return (TestPlan) reObject;
		} else {
			return null;
		}

	}

	public TestPlan getTestPlanByVersion(Integer version) {

		Criteria criteria = this.getHibernateSession().createCriteria(
				TestPlan.class);
		if (null != version) {
			Criteria autoTestUnitCriteria = criteria
					.createCriteria("autoTestUnit");
			autoTestUnitCriteria.add(Restrictions.eq("version", version));
		}
		return (TestPlan) criteria.uniqueResult();
	}

	/**
	 * 获取某个终端下的测试计划最大版本号
	 * 
	 * @author yinzhipeng
	 * @param terminalId
	 * @return
	 */
	public Long queryTestPlanVersion(Long terminalId) {
		if (null == terminalId) {
			return 0l;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestPlan.class);
		// 设置terminalId筛选条件
		criteria.add(Restrictions.eq("terminalId", terminalId));
		// 将版本号排序
		Criteria autoTestUnitCriteria = criteria.createCriteria("autoTestUnit",
				"autoTestUnit");
		autoTestUnitCriteria.addOrder(Order.desc("version"));
		criteria.setProjection(Projections.property("autoTestUnit.version"));
		criteria.setFirstResult(0);
		criteria.setMaxResults(1);
		Long version = (Long) criteria.uniqueResult();
		return version;
	}

	/**
	 * 多条件查询
	 * 
	 * @author yinzhipeng
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList getPageTestPlan(PageList pageList) {
		if (null == pageList || null == pageList.getParam("terminalIds") || "".equals(pageList.getParam("terminalIds").toString())) {
			return new EasyuiPageList();
		}

		Criteria criteria = this.getHibernateSession().createCriteria(
				TestPlan.class);
		Criteria autoTestUnitCriteria = criteria.createCriteria("autoTestUnit");
		// terminalId
		Object terminalIds = pageList.getParam("terminalIds");
		// name
		Object name = pageList.getParam("name");
		// version
		Object version = pageList.getParam("version");
		//startTime
		Object startTime = pageList.getParam("startTime");
		//endTime
		Object endTime = pageList.getParam("endTime");
		//boxid
		Map<Long, String> terBoxIdMap = (Map<Long, String>) pageList.getParam("terBoxIdMap");

		// 设置name筛选条件
		if (StringUtils.hasText((String) name)) {
			criteria.add(Restrictions.like("name", (String) name,
					MatchMode.ANYWHERE));
		}
		// 设置version筛选条件
		if (null != version) {
			autoTestUnitCriteria.add(Restrictions.eq("version", version));
		}
		// 设置terminalId筛选条件
		if (null != terminalIds) {
			String[] tIds = terminalIds.toString().split(",");
			List<Long> idls = new ArrayList<Long>();
			for (String id : tIds) {
				idls.add(Long.valueOf(id));
			}
			//criteria.add(Restrictions.eq("terminalId", terminalId));
			criteria.add(Restrictions.in("terminalId", idls));
		}
		
		//设置筛选时间
		if ( startTime != null && endTime != null) {
			criteria.add(Restrictions.between("planSendDate", startTime, endTime));
		} else if(startTime != null){
			criteria.add(Restrictions.gt("planSendDate", startTime));
		} else if(endTime != null){
			criteria.add(Restrictions.lt("planSendDate", endTime));
		}
		autoTestUnitCriteria.addOrder(Order.desc("version"));
		criteria.addOrder(Order.desc("sendDate"));

		long total = 0;
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List<TestPlan> list = (List<TestPlan>)criteria.list();
		if(list.size() > 0){
			total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		}
		
		for (TestPlan testPlan : list) {
			testPlan.setBoxId(terBoxIdMap.get(testPlan.getTerminalId()));
		}
		
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}
	
	/**
	 * 根据条件查询测试计划
	 * @author lucheng
	 * @date 2020年8月27日 下午4:31:57
	 * @param pageList
	 * @return
	 */
	public List<TestPlan> queryTestPlanByBoxid(PageList pageList){

		Criteria criteria = this.getHibernateSession().createCriteria(TestPlan.class);
		
		Criteria autoTestUnitCriteria = criteria.createCriteria("autoTestUnit");
		// terminalId
		Object terminalIds = pageList.getParam("terminalIds");
		// name
		Object name = pageList.getParam("name");
		// version
		Object version = pageList.getParam("version");

		// 设置name筛选条件
		if (StringUtils.hasText((String) name)) {
			criteria.add(Restrictions.like("name", (String) name,
					MatchMode.ANYWHERE));
		}
		// 设置version筛选条件
		if (null != version) {
			autoTestUnitCriteria.add(Restrictions.eq("version", version));
		}
		// 设置terminalId筛选条件
		if (null != terminalIds) {
			String[] tIds = terminalIds.toString().split(",");
			List<Long> idls = new ArrayList<Long>();
			for (String id : tIds) {
				idls.add(Long.valueOf(id));
			}
			//criteria.add(Restrictions.eq("terminalId", terminalId));
			criteria.add(Restrictions.in("terminalId", idls));
		}
		
		autoTestUnitCriteria.addOrder(Order.desc("version"));
		criteria.addOrder(Order.desc("sendDate"));

		List<TestPlan> list = (List<TestPlan>)criteria.list();
		
		return list;
	}
}
