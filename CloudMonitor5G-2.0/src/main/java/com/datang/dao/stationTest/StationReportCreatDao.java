package com.datang.dao.stationTest;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.dao.testLogItem.StationVerificationTestDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.stationTest.EceptionCellLogPojo;

/**
 * 单验报告生成dao
 * @author maxuancheng
 *
 */
@Repository
@SuppressWarnings("rawtypes")
public class StationReportCreatDao  extends GenericHibernateDao<PlanParamPojo, Long>{
	
	@Autowired
	private StationVerificationTestDao stationVerificationTestDao;
	
	public AbstractPageList doPageQuery(PageList pageList) {
		/*PlanParamPojo find = this.find(2L);
		StationVerificationLogPojo svl1 = stationVerificationTestDao.find(1L);
		StationVerificationLogPojo svl2 = stationVerificationTestDao.find(2L);
		ArrayList<StationVerificationLogPojo> arrayList = new ArrayList<StationVerificationLogPojo>();
		arrayList.add(svl1);
		arrayList.add(svl2);
		find.setStationVerificationLogPojoList(arrayList);
		this.update(find);*/
		
		Criteria criteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
		PlanParamPojo plan = (PlanParamPojo) pageList.getParam("planParamPojo");
		Object startTime = pageList.getParam("startTime");
		Object endTime = pageList.getParam("endTime");
		Object testStatus = pageList.getParam("testStatus");
		Object testService = pageList.getParam("testService");
		Object cityStr = pageList.getParam("cityStr");
		
		if(StringUtils.hasText(plan.getCity()) && !"全部".equals(plan.getCity())){
			criteria.add(Restrictions.eq("city", plan.getCity()));
		}
		
		if(startTime != null){
			criteria.add(Restrictions.ge("reportCreateDate", Long.valueOf(startTime.toString())));
		}
		if(endTime != null){
			criteria.add(Restrictions.le("reportCreateDate", Long.valueOf(endTime.toString())));
		}
		if(plan.getGnbId() != null){
			criteria.add(Restrictions.eq("gnbId",plan.getGnbId()));
		}
		if(StringUtils.hasText(plan.getSiteName())){
			criteria.add(Restrictions.like("siteName",plan.getSiteName(),MatchMode.ANYWHERE));
		}
		
		if(plan.getWirelessParamStatus() != null && StringUtils.hasText(plan.getWirelessParamStatus())){
			criteria.add(Restrictions.eq("wirelessParamStatus",plan.getWirelessParamStatus()));
		}
		
		if(plan.getTestEventAllStatus() != null && StringUtils.hasText(plan.getTestEventAllStatus())){
			criteria.add(Restrictions.eq("testEventAllStatus",plan.getTestEventAllStatus()));
		}
		
		if(cityStr != null){
			String[] cityNames = cityStr.toString().split(",");
			criteria.add(Restrictions.in("city",cityNames));
		}
		
		if(testStatus != null && testService != null){
			switch (Integer.valueOf(testService.toString())) {
			case 0://绕点
				switch (Integer.valueOf(testStatus.toString())) {
				case 0://未完成
					criteria.add(Restrictions.eq("raodianTest",0));
					break;
				case 1://已完成
					criteria.add(Restrictions.eq("raodianTest",1));
					break;
				case 2://更新
					criteria.add(Restrictions.eq("raodianTest",2));
					break;
				}
				break;
			case 1://FTP下载
				switch (Integer.valueOf(testStatus.toString())) {
				case 2://有更新
					criteria.add(
							Restrictions.or(
									Restrictions.eq("goodFtpdownload",2),
									Restrictions.eq("midFtpdownload",2),
									Restrictions.eq("badFtpdownload",2)
									));
					//criteria.add(Restrictions.eq("goodFtpdownload",2));
					//criteria.add(Restrictions.eq("midFtpdownload",2));
					//criteria.add(Restrictions.eq("badFtpdownload",2));
					break;
				case 1://已完成
					criteria.add(Restrictions.eq("goodFtpdownload",1));
					criteria.add(Restrictions.eq("midFtpdownload",1));
					criteria.add(Restrictions.eq("badFtpdownload",1));
					break;
				}
				break;
			case 2://FTP上传  
				switch (Integer.valueOf(testStatus.toString())) {
				case 2://有更新
					criteria.add(
							Restrictions.or(
									Restrictions.eq("goodFtpUpload",2),
									Restrictions.eq("midFtpUpload",2),
									Restrictions.eq("badFtpUpload",2)
									));
					break;
				case 1://已完成
					criteria.add(Restrictions.eq("goodFtpUpload",1));
					criteria.add(Restrictions.eq("midFtpUpload",1));
					criteria.add(Restrictions.eq("badFtpUpload",1));
					break;
				}
				break;
			case 3://ENDC成功率测试
				switch (Integer.valueOf(testStatus.toString())) {
				case 2://有更新
					criteria.add(
							Restrictions.or(
									Restrictions.eq("goodEndcSuccessRatio",2),
									Restrictions.eq("midEndcSuccessRatio",2),
									Restrictions.eq("badEndcSuccessRatio",2)
									));
					break;
				case 1://已完成
					criteria.add(Restrictions.eq("goodEndcSuccessRatio",1));
					criteria.add(Restrictions.eq("midEndcSuccessRatio",1));
					criteria.add(Restrictions.eq("badEndcSuccessRatio",1));
					break;
				}
				break;
			case 4://ping（32）测试
				switch (Integer.valueOf(testStatus.toString())) {
				case 0://未完成
					criteria.add(Restrictions.eq("goodPing32",0));
					break;
				case 1://已完成
					criteria.add(Restrictions.eq("goodPing32",1));
					break;
				case 2://更新
					criteria.add(Restrictions.eq("goodPing32",2));
					break;
				}
				break;
			case 5://
				switch (Integer.valueOf(testStatus.toString())) {
				case 0://未完成
					criteria.add(Restrictions.eq("goodPing1500",0));
					break;
				case 1://已完成
					criteria.add(Restrictions.eq("goodPing1500",1));
					break;
				case 2://已完成
					criteria.add(Restrictions.eq("goodPing32",2));
					break;
				}
				break;
			}
			
		}
		
		criteria.addOrder(Order.desc("id"));

		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
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
	 * 根据cellName获取单验日志详表数据
	 * @author maxuancheng
	 * date:2020年2月21日 下午2:37:53
	 * @param cellName
	 * @return
	 */
	public List<EceptionCellLogPojo> getExceptionCellLogOfCellName(String cellName) {
		Criteria criteria = this.getHibernateSession().createCriteria(EceptionCellLogPojo.class);
		criteria.add(Restrictions.eq("nrCellname", cellName));
		return criteria.list();	
	}


	/**
	 * 根据id查询数据
	 * @author maxuancheng
	 * date:2020年2月24日 下午2:12:09
	 * @param idList
	 * @return
	 */
	public List<PlanParamPojo> findByIds(List<Long> idList) {
		Criteria criteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
		criteria.add(Restrictions.in("id", idList));
		return criteria.list();
	}
	
	/**
	 * 根据enbId查询对应的localCellId
	 * @param enbId
	 * @return
	 */
	public List<PlanParamPojo> getAllLocalCellId(String siteName){
		Criteria criteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
		criteria.add(Restrictions.eq("siteName", siteName));
		criteria.addOrder(Order.asc("localCellID"));
		return criteria.list();
	}
	
	public List<EceptionCellLogPojo> getExceptionCellLogOfCellLog(String cellName,List<String> logNameList,String testService,String wireStatus){
		Criteria criteria = this.getHibernateSession().createCriteria(EceptionCellLogPojo.class);
		if(cellName!=null){
			criteria.add(Restrictions.eq("nrCellname", cellName));
		}
		if(logNameList!=null && logNameList.size()>0){
			criteria.add(Restrictions.in("nrRelatelogname", logNameList));
		}
		if(testService!=null){
			criteria.add(Restrictions.eq("nrTestevent", testService));
		}
		if(StringUtils.hasText(wireStatus)){
			String[] wireStatusArry = wireStatus.toString().split(",");
			criteria.add(Restrictions.in("nrWirelessstation", wireStatusArry));
		}
		return criteria.list();	
	}

}
