package com.datang.dao.stationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong.PingPongCutEvent;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
/**
 * 单站日志轨迹指标Dao
 * @author maxuancheng
 *
 */
@Repository
public class StationEtgTralDao extends GenericHibernateDao<StationEtgTralPojo, Long> {

	public List<StationEtgTralPojo> findTralById(String fileName,String pciStr) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationEtgTralPojo.class);
		if(StringUtils.hasText(fileName)){
			List<String> fileList = new ArrayList<String>();
			String[] split = fileName.split(",");
			for (String name : split) {
				fileList.add(name);
			}
			criteria.add(Restrictions.in("nrLogname", fileList));
		}
		
		List<String> pcilist = Arrays.asList(pciStr.replace(" ", "").split(","));
		
		criteria.add(Restrictions.in("nrPci", pcilist));
		criteria.add(Restrictions.ne("latitude", "0.000000"));
		criteria.add(Restrictions.isNotNull("latitude"));
		criteria.add(Restrictions.ne("longtitude", "0.000000"));
		criteria.add(Restrictions.isNotNull("longtitude"));
		return criteria.list();
	}
	
	/**
	 * 报告生成通过日志名称和pci值和事件的名称获取详细轨迹指标数据
	 * @author lucheng
	 * @date 2020年10月15日 上午11:04:03
	 * @param fileName
	 * @param pci
	 * @param event 事件名称
	 * @return
	 */
	public List<StationEtgTralPojo> findEtgTralByReportId(String fileName,String pciStr,String event){
		Criteria criteria = this.getHibernateSession().createCriteria(StationEtgTralPojo.class);
		if(StringUtils.hasText(fileName)){
			List<String> fileList = new ArrayList<String>();
			String[] split = fileName.split(",");
			for (String name : split) {
				fileList.add(name);
			}
			criteria.add(Restrictions.in("nrLogname", fileList));
		}
		
		List<String> pcilist = Arrays.asList(pciStr.replace(" ", "").split(","));
		
		criteria.add(Restrictions.in("nrPci", pcilist));
		criteria.add(Restrictions.ne("latitude", "0.000000"));
		criteria.add(Restrictions.isNotNull("latitude"));
		criteria.add(Restrictions.ne("longtitude", "0.000000"));
		criteria.add(Restrictions.isNotNull("longtitude"));
		
		String kpiName = getEventName(event);
		
		criteria.add(Restrictions.isNotNull(kpiName));
		
		if(kpiName !=null ){
			criteria.add(Restrictions.isNotNull(kpiName));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("longtitude"), "longtitude");
			projectionList.add(Projections.groupProperty("latitude"), "latitude");
			projectionList.add(Projections.groupProperty(kpiName), kpiName);
			// projectionList.add(Projections.);
			criteria.setProjection(projectionList);
			ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
					StationEtgTralPojo.class);
			criteria.setResultTransformer(resultTransformer);
		}
		return criteria.list();
	}
	
	/**
	 * 报告生成通过日志名称和pci值和事件的名称获取抽样轨迹指标数据
	 * @author lucheng
	 * @date 2020年10月15日 上午11:04:03
	 * @param fileName
	 * @param pci
	 * @param event 事件名称
	 * @return
	 */
	public List<StationSAMTralPojo> findSamTralByReportId(String fileName,String pciStr,String event){
		Criteria criteria = this.getHibernateSession().createCriteria(StationSAMTralPojo.class);
		if(StringUtils.hasText(fileName)){
			List<String> fileList = new ArrayList<String>();
			String[] split = fileName.split(",");
			for (String name : split) {
				fileList.add(name);
			}
			criteria.add(Restrictions.in("nrLogname", fileList));
		}
		
		List<String> pcilist = Arrays.asList(pciStr.replace(" ", "").split(","));
		
		criteria.add(Restrictions.in("nrPci", pcilist));
		criteria.add(Restrictions.ne("latitude", "0.000000"));
		criteria.add(Restrictions.isNotNull("latitude"));
		criteria.add(Restrictions.ne("longtitude", "0.000000"));
		criteria.add(Restrictions.isNotNull("longtitude"));
		
		String kpiName = getEventName(event);
		
		criteria.add(Restrictions.isNotNull(kpiName));
		
		if(kpiName !=null ){
			criteria.add(Restrictions.isNotNull(kpiName));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("longtitude"), "longtitude");
			projectionList.add(Projections.groupProperty("latitude"), "latitude");
			projectionList.add(Projections.groupProperty(kpiName), kpiName);
			// projectionList.add(Projections.);
			criteria.setProjection(projectionList);
			ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
					StationSAMTralPojo.class);
			criteria.setResultTransformer(resultTransformer);
		}
		return criteria.list();
	}
	
	/**
	 * 报告生成通过日志名称和pci值和事件的名称获取最大的事件轨迹指标数据
	 * @author lucheng
	 * @date 2020年12月17日 下午2:46:01
	 * @param fileName
	 * @param pci
	 * @param event
	 * @return
	 */
	public StationEtgTralPojo findMaxTralByReportId(String fileName,String event){
		Session session = this.getHibernateSession();
		String kpiName = getEventName(event);
		
		StringBuffer hql = new StringBuffer();
		hql.append("select timeLong,"+kpiName+" from StationEtgTralPojo where "+kpiName+" is not null ");
		hql.append("and nrLogname in (:alist) ");
		hql.append(" group by timeLong ,"+kpiName+" order by to_number("+kpiName+") DESC");
		
		List<String> fileList = new ArrayList<String>();
		String[] split = fileName.split(",");
		for (String name : split) {
			fileList.add(name);
		}
		Query createQuery = session.createQuery(new String(hql));
		createQuery.setParameterList("alist", fileList);
//		List<String> pcilist = Arrays.asList(pciStr.replace(" ", "").split(","));
//		createQuery.setParameterList("alistPCi", pcilist);
		
		List<Object[]> list = createQuery.list();
		if(list!=null && list.size()>0){
			StationEtgTralPojo stationEtgTralPojo = new StationEtgTralPojo();
			stationEtgTralPojo.setTimeLong(list.get(0)[0].toString());
			return stationEtgTralPojo;
		}else{
			return null;
		}
	}
	
	public List<StationEtgTralPojo> findTralByMaxTime(String fileName, String event, String maxTime, Long timeLength) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationEtgTralPojo.class);
		if(StringUtils.hasText(fileName)){
			List<String> fileList = new ArrayList<String>();
			String[] split = fileName.split(",");
			for (String name : split) {
				fileList.add(name);
			}
			criteria.add(Restrictions.in("nrLogname", fileList));
		}
		
//		List<String> pcilist = Arrays.asList(pciStr.replace(" ", "").split(","));
		
		//criteria.add(Restrictions.in("nrPci", pcilist));
//		criteria.add(Restrictions.ne("latitude", "0.000000"));
//		criteria.add(Restrictions.isNotNull("latitude"));
//		criteria.add(Restrictions.ne("longtitude", "0.000000"));
//		criteria.add(Restrictions.isNotNull("longtitude"));
		
		if(timeLength!=-1){
			if(maxTime != null){
				criteria.add(Restrictions.ge("timeLong", String.valueOf(Long.valueOf(maxTime)-(timeLength/2*1000))));
				criteria.add(Restrictions.le("timeLong", String.valueOf(Long.valueOf(maxTime)+(timeLength/2*1000))));
			}
		}
		
		String kpiName = getEventName(event);
		
		criteria.add(Restrictions.isNotNull(kpiName));
		
		criteria.addOrder(Order.asc("timeLong"));
		
		if(kpiName !=null ){
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("timeLong"), "timeLong");
			projectionList.add(Projections.groupProperty(kpiName), kpiName);
			// projectionList.add(Projections.);
			criteria.setProjection(projectionList);
			ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
					StationEtgTralPojo.class);
			criteria.setResultTransformer(resultTransformer);
		}
		return criteria.list();
	}
	

	/**
	 *报告生成通过日志名称获取最大的时间或最小的时间
	 * @author lucheng
	 * @date 2020年12月29日 下午2:46:01
	 * @param fileName
	 * @param type 0：求最大时间 1：求最小时间
	 * @return
	 */
	public StationEtgTralPojo findMaxTralTimeByReportName(String fileName, String type) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationEtgTralPojo.class);
		if(StringUtils.hasText(fileName)){
			List<String> fileList = new ArrayList<String>();
			String[] split = fileName.split(",");
			for (String name : split) {
				fileList.add(name);
			}
			criteria.add(Restrictions.in("nrLogname", fileList));
		}
		criteria.add(Restrictions.isNotNull("timeLong"));
		
		ProjectionList projectionList = Projections.projectionList();
		if(type.equals("0")){
			projectionList.add(Projections.max("timeLong"),"timeLong");
		}else if(type.equals("1")){
			projectionList.add(Projections.min("timeLong"),"timeLong");
		}
		// projectionList.add(Projections.);
		criteria.setProjection(projectionList);
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				StationEtgTralPojo.class);
		criteria.setResultTransformer(resultTransformer);
		return (StationEtgTralPojo) criteria.uniqueResult();
	}
	
	/**
	 *  减少查询的数据量
	 * @author lucheng
	 * @date 2020年12月9日 上午11:31:05
	 * @param event 事件的名称
	 * @return kpiName 事件在StationEtgTralPojo类里对应渲染的点的指标名称，和layerControl.js的stationEtgTralFunc2一样
	 */
	public String getEventName(String event){
		String kpiName = "";
		if(event.indexOf("NR RSRP")!=-1){
			kpiName = "nrRsrp";
		} else if(event.indexOf("LTE RSRP")!=-1){
			kpiName = "lteRsrp";
		} else if(event.indexOf("NR SINR")!=-1){
			kpiName = "nrSinr";
		} else if(event.indexOf("LTE SINR")!=-1){
			kpiName = "lteSinr";
		} else if(event.equals("NR PHY Thr UL(M)")){
			kpiName = "nrPhyThrUL";
		} else if(event.equals("NR PHY Thr DL(M)")){
			kpiName = "nrPhyThrDL";
		} else if(event.equals("NR MAC Thr UL(M)")){
			kpiName = "nrMacthrputul";
		} else if(event.equals("NR MAC Thr DL(M)")){
			kpiName = "nrMacthrputdl";
		} else if(event.equals("NR RLC Thr UL(M)")){
			kpiName = "nrRlcThrUL";
		} else if(event.equals("NR RLC Thr DL(M)")){
			kpiName = "nrRlcThrDL";
		} else if(event.equals("NR PDCP Thr UL(M)")){
			kpiName = "nrPdcpThrUL";
		} else if(event.equals("NR PDCP Thr DL(M)")){
			kpiName = "nrPdcpThrDL";
		} else if(event.equals("NR SDAP Thr UL(M)")){
			kpiName = "nrSdcpThrUL";
		} else if(event.equals("NR SDAP Thr DL(M)")){
			kpiName = "nrSdcpThrDL";
		} else if(event.equals("LTE PDCP Thr UL(M)")){
			kpiName = "ltePdcpThrUL";
		} else if(event.equals("LTE PDCP Thr DL(M)")){
			kpiName = "ltePdcpThrDL";
		} else if(event.equals("LTE RLC Thr UL(M)")){
			kpiName = "lteRlcThrUL";
		} else if(event.equals("LTE RLC Thr DL(M)")){
			kpiName = "lteRlcThrDL";
		} else if(event.equals("LTE MAC Thr UL(M)")){
			kpiName = "lteMacthrputul";
		} else if(event.equals("LTE MAC Thr DL(M)")){
			kpiName = "lteMacthrputdl";
		} else if(event.equals("LTE PHY Thr DL(M)")){
			kpiName = "ltePhyThrUl";
		} else if(event.equals("LTE PHY Thr DL(M)")){
			kpiName = "ltePhyThrDl";
		} else if(event.equals("NR PCI")){
			kpiName = "nrPci";
		} else if(event.equals("LTE PCI")){
			kpiName = "ltePci";
		}else{
			kpiName = null;
		}
		return kpiName;
	}


}
