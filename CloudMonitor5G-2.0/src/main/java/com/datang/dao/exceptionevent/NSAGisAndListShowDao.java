package com.datang.dao.exceptionevent;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.exceptionevent.Iads5gExceptionEventTable;
import com.datang.domain.exceptionevent.iads5gExceptionEventRsrp;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.web.action.action5g.exceptionevent.dto.GisAndListShowDto;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-20 16:00
 */
@Repository
public class NSAGisAndListShowDao  extends GenericHibernateDao<Iads5gExceptionEventTable, Long> {




    public EasyuiPageList getPageListOfNsa(PageList pageList) {
        Criteria criteria = this.getHibernateSession().createCriteria(
                Iads5gExceptionEventTable.class);

        String nrFriendlyName =  (String) pageList.getParam("nrFriendlyName");
        // 筛选道路名称
        if(StringUtils.isNotBlank(nrFriendlyName)){
            criteria.add(Restrictions.like("nrFriendlyName","%"+nrFriendlyName+"%"));
        }
        List<String> logNameList =  (List<String>) pageList.getParam("logNameList");
        criteria.add(Restrictions.in("fileName", logNameList));
        
        criteria.add(Restrictions.eq("eType", "1"));

        long total = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();
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

    public AbstractPageList getPageListOfSaSwitch(PageList pageList) {
        Criteria criteria = this.getHibernateSession().createCriteria(
                Iads5gExceptionEventTable.class);

        String nrFriendlyName = (String) pageList.getParam("nrFriendlyName");
        // 筛选道路名称
        if(StringUtils.isNotBlank(nrFriendlyName)){
            criteria.add(Restrictions.like("nrFriendlyName","%"+nrFriendlyName+"%"));
        }
        List<String> logNameList =  (List<String>) pageList.getParam("logNameList");
        criteria.add(Restrictions.in("fileName", logNameList));
        
        criteria.add(Restrictions.eq("eType", "2"));
        long total = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();
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


    public AbstractPageList getPageListOfSa45G(PageList pageList) {
        Criteria criteria = this.getHibernateSession().createCriteria(
                Iads5gExceptionEventTable.class);

        String nrFriendlyName = (String) pageList.getParam("pageQueryBean");
        // 筛选道路名称
        if(StringUtils.isNotBlank(nrFriendlyName)){
        	criteria.add(Restrictions.or(Restrictions.like("nrFriendlyName","%"+nrFriendlyName+"%"), Restrictions.like("lteFriendlyName","%"+nrFriendlyName+"%")));
        }
        List<String> logNameList =  (List<String>) pageList.getParam("logNameList");
        criteria.add(Restrictions.in("fileName", logNameList));
        
        criteria.add(Restrictions.like("eType", "4"));
        long total = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();
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


    public AbstractPageList getPageListOfSaAccess(PageList pageList) {
        Criteria criteria = this.getHibernateSession().createCriteria(
                Iads5gExceptionEventTable.class);

        String nrFriendlyName =  (String) pageList.getParam("nrFriendlyName");
        List<String> logNameList =  (List<String>) pageList.getParam("logNameList");
        criteria.add(Restrictions.in("fileName", logNameList));
        // 筛选小区名称
        if(StringUtils.isNotBlank(nrFriendlyName)){
            criteria.add(Restrictions.or(Restrictions.like("nrFriendlyName","%"+nrFriendlyName+"%"), Restrictions.like("lteFriendlyName","%"+nrFriendlyName+"%")));
        }

        criteria.add(Restrictions.eq("eType", "3"));

        long total = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();
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
    
    public AbstractPageList getPageListOfBussiness(PageList pageList) {
        Criteria criteria = this.getHibernateSession().createCriteria(
                Iads5gExceptionEventTable.class);

        String nrFriendlyName =  (String) pageList.getParam("nrFriendlyName");
        List<String> logNameList =  (List<String>) pageList.getParam("logNameList");
        criteria.add(Restrictions.in("fileName", logNameList));
        // 筛选小区名称
        if(StringUtils.isNotBlank(nrFriendlyName)){
            criteria.add(Restrictions.or(Restrictions.like("nrFriendlyName","%"+nrFriendlyName+"%"), Restrictions.like("lteFriendlyName","%"+nrFriendlyName+"%")));
        }

        criteria.add(Restrictions.eq("eType", "5"));

        long total = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();
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

    public Iads5gExceptionEventTable queryRecordById(String fileId) {
        return this.find(Long.parseLong(fileId));
    }

	public List<Object> getGpsPointData(Long recSeqNo, Long timeLong,Integer timeHigh, Integer timeLow,String fileName) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationEtgTralPojo.class);
		criteria.add(Restrictions.gt("timeLong",String.valueOf(timeLong - timeLow*1000)));
		criteria.add(Restrictions.le("timeLong",String.valueOf(timeLong + timeHigh*1000)));
		criteria.addOrder(Order.desc("timeLong"));
		criteria.add(Restrictions.eq("nrLogname", fileName));
		List list = criteria.list();
		return list;
	}

	/**
	 * 根据pci和频点获取小区数据
	 * @author maxuancheng
	 * date:2019年12月19日 下午5:26:05
	 * @param nrPci
	 * @param point
	 * @return
	 */
	public List<Cell5G> getCellIdAndCellName(String nrPci, String point) {
		Criteria criteria = this.getHibernateSession().createCriteria(Cell5G.class);
		criteria.add(Restrictions.eq("pci", Long.valueOf(nrPci)));
		criteria.add(Restrictions.eq("frequency1", Long.valueOf(point)));
		List list = criteria.list();
		return list;
	}
	
	/**
	 * 根据lte小区友好名获取小区数据
	 * @param lteFriendlyName
	 * @return
	 */
	public List<LteCell> getLteCell(String lteFriendlyName) {
		Criteria criteria = this.getHibernateSession().createCriteria(LteCell.class);
		criteria.add(Restrictions.eq("cellName", lteFriendlyName));
		List list = criteria.list();
		return list;
	}
	
	/**
	 * 根据nr小区友好名获取小区数据
	 * @param nrFriendlyName
	 * @return
	 */
	public List<Cell5G> getNrCell(String nrFriendlyName) {
		Criteria criteria = this.getHibernateSession().createCriteria(Cell5G.class);
		criteria.add(Restrictions.eq("cellName", nrFriendlyName));
		List list = criteria.list();
		return list;
	}
}
