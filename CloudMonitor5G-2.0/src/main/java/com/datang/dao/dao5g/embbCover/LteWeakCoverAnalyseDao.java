package com.datang.dao.dao5g.embbCover;

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
import com.datang.domain.embbCover.LteContrarilyCoverAnalysePojo;
import com.datang.domain.embbCover.LteDistantCoverAnalysePojo;
import com.datang.domain.embbCover.LteOverCoverAnalysePojo;
import com.datang.domain.embbCover.LteOverlapCoverAnalysePojo;
import com.datang.domain.embbCover.LteWeakCoverAnalysePojo;
import com.datang.domain.embbCover.NrContrarilyCoverAnalysePojo;
import com.datang.domain.embbCover.NrDistantCoverAnalysePojo;
import com.datang.domain.embbCover.NrOverCoverAnalysePojo;
import com.datang.domain.embbCover.NrOverlapCoverAnalysePojo;
import com.datang.domain.embbCover.NrWeakCoverAnalysePojo;
import com.datang.domain.embbCover.StationDistanceLTEPojo;
import com.datang.domain.embbCover.StationDistanceNRPojo;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.stationTest.StationEtgTralPojo;

/**
 * lte弱覆盖分析Dao
 * @author maxuancheng
 *
 */
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class LteWeakCoverAnalyseDao extends GenericHibernateDao<LteWeakCoverAnalysePojo, Long> {

	public AbstractPageList doPageQuery(PageList pageList) {
		Integer netType = (Integer) pageList.getParam("netType");
		Integer coverType = (Integer) pageList.getParam("coverType");
		Object cellName = pageList.getParam("cellName");
		List<String> logNames = (List<String>) pageList.getParam("logNames");
		
		Criteria criteria = null;
		if(netType == 1 && coverType == 1){			
			criteria = this.getHibernateSession().createCriteria(LteWeakCoverAnalysePojo.class);
		}else if(netType == 1 && coverType == 2){			
			criteria = this.getHibernateSession().createCriteria(LteOverCoverAnalysePojo.class);
		}else if(netType == 1 && coverType == 3){			
			criteria = this.getHibernateSession().createCriteria(LteOverlapCoverAnalysePojo.class);
		}else if(netType == 1 && coverType == 4){			
			criteria = this.getHibernateSession().createCriteria(LteDistantCoverAnalysePojo.class);
		}else if(netType == 1 && coverType == 5){			
			criteria = this.getHibernateSession().createCriteria(LteContrarilyCoverAnalysePojo.class);
		}else if(netType == 2 && coverType == 1){			
			criteria = this.getHibernateSession().createCriteria(NrWeakCoverAnalysePojo.class);
		}else if(netType == 2 && coverType == 2){			
			criteria = this.getHibernateSession().createCriteria(NrOverCoverAnalysePojo.class);
		}else if(netType == 2 && coverType == 3){			
			criteria = this.getHibernateSession().createCriteria(NrOverlapCoverAnalysePojo.class);
		}else if(netType == 2 && coverType == 4){			
			criteria = this.getHibernateSession().createCriteria(NrDistantCoverAnalysePojo.class);
		}else if(netType == 2 && coverType == 5){			
			criteria = this.getHibernateSession().createCriteria(NrContrarilyCoverAnalysePojo.class);
		}
		
		if(logNames!=null && logNames.size()>0){
			criteria.add(Restrictions.in("logName",logNames));
		}
		
		if(cellName!= null && StringUtils.hasText(cellName.toString())){
			criteria.add(Restrictions.like("cellName",cellName.toString(),MatchMode.ANYWHERE));
		}
		
//		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.addOrder(Order.desc("id"));
//		criteria.setProjection(null);
//		int rowsCount = pageList.getRowsCount();// 每页记录数
//		int pageNum = pageList.getPageNum();// 页码
//		criteria.setFirstResult((pageNum - 1) * rowsCount);
//		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
//		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}

	public Object getCellData(String cellName, Integer netType) {
		Criteria criteria = null;
		if(netType == 1){			
			criteria = this.getHibernateSession().createCriteria(LteCell.class);
		}else if(netType == 2){			
			criteria = this.getHibernateSession().createCriteria(Cell5G.class);
		}
		
		criteria.add(Restrictions.eq("cellName",cellName));
		criteria.setMaxResults(1);
		List list = criteria.list();
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据5G的小区名和城市获取站间距
	 * @author maxuancheng
	 * date:2020年5月12日 上午9:57:05
	 * @param cellName
	 * @param regin
	 * @return
	 */
	public Integer getNRDistance(String cellName, String regin) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationDistanceNRPojo.class);
		criteria.add(Restrictions.eq("cellName", cellName));
		criteria.add(Restrictions.eq("regin", regin));
		criteria.setMaxResults(1);
		List<StationDistanceNRPojo> list = criteria.list();
		if(list != null && list.size() > 0){
			return list.get(0).getDistance();
		}
		return null;
	}
	
	/**
	 * 根据4G的小区名和城市获取站间距
	 * @author lucheng
	 * date:2020年5月12日 上午9:57:05
	 * @param cellName
	 * @param regin
	 * @return
	 */
	public Integer getLTEDistance(String cellName, String regin) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationDistanceLTEPojo.class);
		criteria.add(Restrictions.eq("cellName", cellName));
		criteria.add(Restrictions.eq("regin", regin));
		criteria.setMaxResults(1);
		List<StationDistanceLTEPojo> list = criteria.list();
		if(list != null && list.size() > 0){
			return list.get(0).getDistance();
		}
		return null;
	}
	
	/**
	 * 根据日志名和pci查询轨迹点
	 * @param logName
	 * @param pci
	 * @param netType
	 * @return
	 */
	public List<StationEtgTralPojo> getGpsPointData(List<String> logName,Long pci,Long frequency1,Integer netType){
		Criteria criteria = this.getHibernateSession().createCriteria(StationEtgTralPojo.class);
		if(netType == 1){			
			criteria.add(Restrictions.eq("ltePci", String.valueOf(pci)));
//			criteria.add(Restrictions.eq("ltePoint", String.valueOf(frequency1)));
		}else if(netType == 2){			
			criteria.add(Restrictions.eq("nrPci", String.valueOf(pci)));
//			criteria.add(Restrictions.eq("nrPoint", String.valueOf(frequency1)));
		}
		criteria.add(Restrictions.in("nrLogname", logName));
		List<StationEtgTralPojo> list = (List<StationEtgTralPojo>)criteria.list();
		return list;
	}

}
