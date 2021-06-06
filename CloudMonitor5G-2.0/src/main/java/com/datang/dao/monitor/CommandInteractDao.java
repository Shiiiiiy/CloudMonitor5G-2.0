package com.datang.dao.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.datang.domain.monitor.CommandInteract;
import com.datang.domain.monitor.MonitorEntity;
import com.datang.web.beans.monitor.CommandInteractRequestBean;

/**
 * 命令交互Dao
 * 
 * @explain
 * @name CommandInteractDao
 * @author shenyanwei
 * @date 2016年7月11日下午2:11:04
 */
@Repository
@SuppressWarnings("all")
public class CommandInteractDao extends
		GenericHibernateDao<CommandInteract, Long> {
	/**
	 * 多条件分页
	 * 
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList getPageItem(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				CommandInteract.class);
		CommandInteractRequestBean pageParams = (CommandInteractRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选命令执行开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("time", beginDate));
		}
		// 筛选命令执行结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("time", endDate));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			Criteria createCriteria = criteria.createCriteria("terminal");
			createCriteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		// 筛选命令名称
		String fileName = pageParams.getCommandName();
		if (StringUtils.hasText(fileName)) {
			criteria.add(Restrictions.like("commandName", fileName.trim(),
					MatchMode.ANYWHERE));
		}
		long total = 0;
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		total = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}

	/**
	 * 
	 * @param ciRecord
	 */
	public void addCIRecord(CommandInteract ciRecord) {
		create(ciRecord);
	}

	/**
	 * 
	 * @param ciRecord
	 */
	public void updateCIRecord(CommandInteract ciRecord) {
		update(ciRecord);
	}

	/**
	 * 
	 * @param ciRecord
	 */
	public void deleteCIRecord(CommandInteract ciRecord) {
		delete(ciRecord);
	}

	/**
	 * 
	 * @param id
	 */
	// public CommandInteract queryCIRecord(Integer id) {
	// return find(id);
	// }

	/**
	 * 删除terminalId表示的所有的交互命令记录
	 * 
	 * @param terminalId
	 */
	public void deleteCommandInteractByID(Long terminalId) {
		Query deleteQuery = getHibernateSession().createQuery(
				"delete CommandInteract ci where ci.terminal.id=:terminalID");
		deleteQuery.setLong("terminalID", terminalId);
		deleteQuery.executeUpdate();
	}

	/**
	 * 
	 * @param entityClass
	 * @param terminalName
	 * @param maxLogNum
	 * @return
	 */
	public List<MonitorEntity> refreshCommandInteract(String terminalName,
			String maxLogNum, List<Long> managedTerminalIDs) {
		if (managedTerminalIDs == null || managedTerminalIDs.size() == 0) {
			return new ArrayList<MonitorEntity>();
		}

		Criteria criteria = this.getHibernateSession().createCriteria(
				CommandInteract.class);
		Criteria terminalUnitCriteria = criteria.createCriteria("terminal");
		// name
		if (StringUtils.hasText(terminalName)) {
			terminalUnitCriteria.add(Restrictions.eq("name", terminalName));
		}

		terminalUnitCriteria.add(Expression.in("id", managedTerminalIDs));

		criteria.addOrder(Order.desc("time"));
		criteria.setMaxResults(Integer.parseInt(maxLogNum));

		List<MonitorEntity> result = criteria.list();
		// int logNum = Math.min(result.size(), Integer.parseInt(maxLogNum));
		// return result.subList(0, logNum);
		return result;
	}

	/**
	 * @param ids
	 */
	public void deleteCommandInteractByIDs(Long[] ids) {
		Query deleteQuery = getHibernateSession().createQuery(
				"delete CommandInteract ci where ci.terminal.id in (:ids)");
		deleteQuery.setParameterList("ids", ids);
		deleteQuery.executeUpdate();

	}
}
