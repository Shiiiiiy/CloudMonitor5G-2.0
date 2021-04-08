/**
 * 
 */
package com.datang.dao.platform.projectParam;

import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.Cell5GNbCell;
import com.datang.domain.platform.projectParam.Cell5GtdlNbCell;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.platform.projectParam.GsmCell;
import com.datang.domain.platform.projectParam.Lte5GCell;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.projectParam.TdlGsmNbCell;
import com.datang.domain.platform.projectParam.TdlNbCell;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;

/**
 * 小区信息dao
 * 
 * @author yinzhipeng
 * @date:2015年10月20日 上午9:38:38
 * @version
 */
@Repository
public class CellInfoDao extends GenericHibernateDao<CellInfo, Long> {

	/**
	 * 删除cityid城市下的infotype运营商下的operatortype制式的小区
	 * 
	 * @param cityId
	 * @param infoType
	 * @param operatorType
	 */
	public void delCells(Long cityId, String infoType, String operatorType) {
		if (ProjectParamInfoType.FG.equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession()
					.createCriteria(Cell5G.class);
			Criteria createCriteria2 = createCriteria
					.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<Cell5G> list = (List<Cell5G>) createCriteria.list();
			for (Cell5G cell : list) {
				cell.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		} else if (ProjectParamInfoType.LTE.equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession()
					.createCriteria(LteCell.class);
			Criteria createCriteria2 = createCriteria
					.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<LteCell> list = (List<LteCell>) createCriteria.list();
			for (LteCell lteCell : list) {
				lteCell.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		} else if (ProjectParamInfoType.GSM.equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession()
					.createCriteria(GsmCell.class);
			Criteria createCriteria2 = createCriteria
					.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<GsmCell> list = (List<GsmCell>) createCriteria.list();
			for (GsmCell gsmCell : list) {
				gsmCell.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		} else if (ProjectParamInfoType.FG_FG.equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession()
					.createCriteria(Cell5GNbCell.class);
			Criteria createCriteria2 = createCriteria
					.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<Cell5GNbCell> list = (List<Cell5GNbCell>) createCriteria
					.list();
			for (Cell5GNbCell cell5gNbCell : list) {
				cell5gNbCell.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		} else if (ProjectParamInfoType.FG_LTE.equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession()
					.createCriteria(Cell5GtdlNbCell.class);
			Criteria createCriteria2 = createCriteria
					.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<Cell5GtdlNbCell> list = (List<Cell5GtdlNbCell>) createCriteria
					.list();
			for (Cell5GtdlNbCell cell5GtdlNbCell : list) {
				cell5GtdlNbCell.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		} else if (ProjectParamInfoType.TDL_NB.equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession()
					.createCriteria(TdlNbCell.class);
			Criteria createCriteria2 = createCriteria
					.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<TdlNbCell> list = (List<TdlNbCell>) createCriteria.list();
			for (TdlNbCell tdlNbCell : list) {
				tdlNbCell.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		} else if (ProjectParamInfoType.TDL_GSM_NB.equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession()
					.createCriteria(TdlGsmNbCell.class);
			Criteria createCriteria2 = createCriteria
					.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<TdlGsmNbCell> list = (List<TdlGsmNbCell>) createCriteria
					.list();
			for (TdlGsmNbCell tdlGsmNbCell : list) {
				tdlGsmNbCell.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		} else if (ProjectParamInfoType.LTE_5G.equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession().createCriteria(Lte5GCell.class);
			Criteria createCriteria2 = createCriteria.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<Lte5GCell> list = (List<Lte5GCell>) createCriteria.list();
			for (Lte5GCell lte5GCell : list) {
				lte5GCell.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		}else if ("5GPARAM".equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
			Criteria createCriteria2 = createCriteria.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<PlanParamPojo> list = (List<PlanParamPojo>) createCriteria.list();
			for (PlanParamPojo planParamPojo : list) {
				planParamPojo.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		}else if ("4GPARAM".equals(operatorType)) {
			Criteria createCriteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
			Criteria createCriteria2 = createCriteria.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("operatorType", infoType));
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
			List<Plan4GParam> list = (List<Plan4GParam>) createCriteria.list();
			for (Plan4GParam plan4GParam : list) {
				plan4GParam.setCellInfo(null);
			}
			this.getHibernateTemplate().deleteAll(list);
		}
		
	}
	
	/**
	 * 查询规划参数表信息
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQuery(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(CellInfo.class);
		Criteria createCriteria2 = criteria.createCriteria("group");
		Object startTime = pageList.getParam("beginImportDate");
		Object endTime = pageList.getParam("endImportDate");
		Object cityStr = pageList.getParam("citNames");
		
		criteria.add(Restrictions.or(Restrictions.eq("operatorType", "5G"), Restrictions.eq("operatorType", "4G")));
		if(startTime != null){
			criteria.add(Restrictions.ge("importDate", startTime));
		}
		if(endTime != null){
			criteria.add(Restrictions.le("importDate", endTime));
		}
		if(cityStr != null ){
			String[] cityNames = cityStr.toString().split(",");
			createCriteria2.add(Restrictions.in("name",cityNames));
		}
		
		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.addOrder(Order.desc("id"));
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List<CellInfo> list = (List<CellInfo>)criteria.list();
//		for (CellInfo cellInfo : list) {
//			cellInfo.setPlan4GParams(null);
//			cellInfo.setPlanParams(null);
//		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}
	
	/**
	 * 查询5G规划参数表详细信息
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQueryParam5G(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
		Object idsStr = pageList.getParam("idsStr");
		if(idsStr != null && StringUtils.hasText(idsStr.toString())){
			Criteria createCriteria2 = criteria.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("id", (Long.valueOf(String.valueOf(idsStr)))));
		}
		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.addOrder(Order.desc("id"));
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List<PlanParamPojo> list = (List<PlanParamPojo>)criteria.list();
//		for (CellInfo cellInfo : list) {
//			cellInfo.setPlan4GParams(null);
//			cellInfo.setPlanParams(null);
//		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}
	
	/**
	 * 查询4G规划参数表详细信息
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQueryParam4G(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		Object idsStr = pageList.getParam("idsStr");
		if(idsStr != null && StringUtils.hasText(idsStr.toString())){
			Criteria createCriteria2 = criteria.createCriteria("cellInfo");
			createCriteria2.add(Restrictions.eq("id", (Long.valueOf(String.valueOf(idsStr)))));
		}
		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.addOrder(Order.desc("id"));
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List<Plan4GParam> list = (List<Plan4GParam>)criteria.list();
//		for (CellInfo cellInfo : list) {
//			cellInfo.setPlan4GParams(null);
//			cellInfo.setPlanParams(null);
//		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}
}
