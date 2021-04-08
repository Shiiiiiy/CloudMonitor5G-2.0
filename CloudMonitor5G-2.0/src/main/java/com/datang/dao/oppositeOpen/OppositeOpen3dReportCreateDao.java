package com.datang.dao.oppositeOpen;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.platform.projectParam.Plan4GParam;

@Repository
public class OppositeOpen3dReportCreateDao extends GenericHibernateDao<Plan4GParam,Long>{

	/**
	 * 分页查询
	 * @author maxuancheng
	 * date:2020年3月10日 下午4:23:31
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQuery(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		Plan4GParam plan = (Plan4GParam) pageList.getParam("plan4GParam");
		Object startTime = pageList.getParam("startTime");
		Object endTime = pageList.getParam("endTime");
		Object testStatus = pageList.getParam("testStatus");
		Object testService = pageList.getParam("testService");
		Object cityStr = pageList.getParam("cityStr");
		
		if(StringUtils.hasText(plan.getRegion()) && !"全部".equals(plan.getRegion())){
			criteria.add(Restrictions.eq("region", plan.getRegion()));
		}
		
		if(startTime != null){
			criteria.add(Restrictions.ge("createReportDate", Long.valueOf(startTime.toString())));
		}
		if(endTime != null){
			criteria.add(Restrictions.le("createReportDate", Long.valueOf(endTime.toString())));
		}
		if(plan.getEnbId() != null && StringUtils.hasText(plan.getEnbId())){
			criteria.add(Restrictions.eq("enbId",plan.getEnbId()));
		}
		if(StringUtils.hasText(plan.getSiteName())){
			criteria.add(Restrictions.like("siteName",plan.getSiteName(),MatchMode.ANYWHERE));
		}
		if(cityStr != null){
			String[] cityNames = cityStr.toString().split(",");
			criteria.add(Restrictions.in("region",cityNames));
		}
		if(plan.getWirelessParamStatus() != null && StringUtils.hasText(plan.getWirelessParamStatus())){
			criteria.add(Restrictions.eq("wirelessParamStatus",plan.getWirelessParamStatus()));
		}
		
		if(plan.getTestEventAllStatus() != null && StringUtils.hasText(plan.getTestEventAllStatus())){
			criteria.add(Restrictions.eq("testEventAllStatus",plan.getTestEventAllStatus()));
		}
		
		if(plan.getNoPassTestEvent() != null && StringUtils.hasText(plan.getNoPassTestEvent())){
			criteria.add(Restrictions.eq("noPassTestEvent",plan.getNoPassTestEvent()));			
		}
		
		if(testStatus != null && testService != null){
			String fieldName = "";
			switch (Integer.valueOf(testService.toString())) {
			case 0://ftp下载
				fieldName = "ftpDownloadGood";
				break;
			case 1://ftp上传
				fieldName = "ftpUploadGood";
				break;
			case 2://ENDC成功率测试
				fieldName = "goodEndcSuccessRatio";
				break;
			case 3://CSFB测试
				fieldName = "csfTest";
				break;
			case 4://Volte测试
				fieldName = "volteTest";		
				break;
			case 5://ping（32）测试
				fieldName = "ping32Good";
				break;
			case 6://绕点测试
				fieldName = "raodianTest";
				break;
			}
			criteria.add(Restrictions.eq(fieldName, Integer.valueOf(testStatus.toString())));
		}

		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		
		criteria.addOrder(Order.desc("id"));
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}
	
	/**
	 * 根据id查询数据
	 * @author maxuancheng
	 * date:2020年2月24日 下午2:12:09
	 * @param idList
	 * @return
	 */
	public List<Plan4GParam> findByIds(List<Long> idList) {
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		criteria.add(Restrictions.in("id", idList));
		return criteria.list();
	}
	
	/**
	 * 根据enbId查询对应的localCellId
	 * @author lucheng
	 * @param enbId
	 * @return
	 */
	public List<Plan4GParam> getAllLocalCellId(String enbId){
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		criteria.add(Restrictions.eq("enbId", enbId));
		criteria.addOrder(Order.asc("localCellId"));
		return criteria.list();
	}
	
	/**
	 * 根据站名查询对应的4g工参
	 * @author lucheng
	 * @date 2020年8月20日 下午8:58:21
	 * @param siteName
	 * @return
	 */
	public List<Plan4GParam> getAllBySitename(String siteName){
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		criteria.add(Restrictions.eq("siteName", siteName));
		criteria.addOrder(Order.asc("localCellId"));
		return criteria.list();
	}
	
	
}
