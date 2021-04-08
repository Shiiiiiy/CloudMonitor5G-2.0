/**
 * 
 */
package com.datang.tools;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.platform.projectParam.StationDistanceLTEDao;
import com.datang.dao.platform.projectParam.StationDistanceNRDao;
import com.datang.domain.embbCover.StationDistanceLTEPojo;
import com.datang.domain.embbCover.StationDistanceNRPojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.service.platform.projectParam.IProjectParamService;
import com.datang.util.GPSUtils;
import com.datang.util.SqlCreateUtils;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;

/**
 * 调取计算站间距线程
 * 
 * @author lucheng
 * @date:2020年5月27日 下午4:46:08
 * @version
 * @param <T>
 */
@Controller
public class ComputeStationDistance<T> implements Runnable {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ComputeStationDistance.class);
	/**
	 * 工参管理服务
	 */
	@Autowired
	private IProjectParamService projectParamService;
	private List<T> stationDistancePojoList ;// 传递对象
	private String region;// 制式
	private String operatorType;// 制式
	
	public ComputeStationDistance() {
	
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		//站间距计算：该小区所属基站的经纬度，周边3km范围内，所有室外站点和其距离的平均值
		try {
			// 线程休眠,规避事物
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
			e1.printStackTrace();
		}
		try {
			if(operatorType.equals(ProjectParamInfoType.FG)){
				//查询保存的区域的站间距并全部删除
				List<StationDistanceNRPojo> list = projectParamService.queryStationNRDistance(region);
				projectParamService.deleteNrDistance(list);
			}else if(operatorType.equals(ProjectParamInfoType.LTE)){
				//查询保存的区域的站间距并全部删除
				List<StationDistanceLTEPojo> list = projectParamService.queryStationLteDistance(region);
				projectParamService.deleteLteDistance(list);
			}
			for (T pojo1 : stationDistancePojoList) {
				Class<? extends Object> clazz = pojo1.getClass();
				Method ori_method1 = clazz.getMethod("getCellName");
				String cellName = (String)ori_method1.invoke(pojo1);
				
				Method ori_method2 = clazz.getMethod("getSiteName");
				String ori_siteName = (String)ori_method2.invoke(pojo1);
				Float ori_longitude = null;
				Float ori_latitude = null;		
				
				Method ori_method3 = clazz.getMethod("getLongitude");
				ori_longitude = (Float)ori_method3.invoke(pojo1);
				
				Method ori_method4 = clazz.getMethod("getLatitude");
				ori_latitude = (Float)ori_method4.invoke(pojo1);
				
				double distanceTotal = 0;
				Integer k = 0;
				List<Double> distanceList = new ArrayList<Double>();
				Map<String,Double> map = new HashMap<String,Double>();
				for (T pojo2 : stationDistancePojoList) {
					Class<? extends Object> clazz2 = pojo2.getClass();
					Method obj_method1 = clazz2.getMethod("getCellName");
					String obj_cellName = (String)obj_method1.invoke(pojo2);
					
					Method obj_method2 = clazz2.getMethod("getSiteName");
					String obj_siteName = (String)obj_method2.invoke(pojo2);
					
					if(!ori_siteName.equals(obj_siteName)){
						Float obj_longitude = null;
						Float obj_latitude = null;
						Method obj_method3 = clazz2.getMethod("getLongitude");
						obj_longitude = (Float)obj_method3.invoke(pojo2);
						
						Method obj_method4 = clazz2.getMethod("getLatitude");
						obj_latitude = (Float)obj_method4.invoke(pojo2);
						
						double distance = GPSUtils.distance(ori_latitude, ori_longitude, obj_latitude, obj_longitude);
						if(distance<3000){
//							k++;
//							distanceTotal = distanceTotal + distance;
							if(map.get(obj_cellName)==null){
								Double val = (double)Math.round(distance*100)/100;
								if(!distanceList.contains(val)){
									map.put(obj_cellName, val);
									distanceList.add(val);
								}
							}
						}
					}
				 }
				List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(map.entrySet());
		        Collections.sort(list,new Comparator<Map.Entry<String,Double>>() {
		            //升序排序
		            public int compare(Entry<String, Double> o1,
		                    Entry<String, Double> o2) {
		                return o1.getValue().compareTo(o2.getValue());
		            }
		        });
		        String nCellNames = "";
		        for (int i = 0; i < list.size(); i++) {
		        	if(i<8){
		        		nCellNames = nCellNames + String.valueOf(list.get(i).getKey())+",";
		        		distanceTotal = distanceTotal + list.get(i).getValue();
		        		k=i+1;
		        	}
				}
					 
				 if(k>0){
						Integer distanceAvg = (int) distanceTotal / k;
						StationDistanceNRPojo stationDistanceNRPojo = null;
						StationDistanceLTEPojo stationDistanceLTEPojo = null;
						if(operatorType.equals(ProjectParamInfoType.FG)){
							stationDistanceNRPojo = new StationDistanceNRPojo();
							stationDistanceNRPojo.setCellName(cellName);
							stationDistanceNRPojo.setRegin(region);
							stationDistanceNRPojo.setDistance(distanceAvg);
							stationDistanceNRPojo.setnCellNames(nCellNames.substring(0, nCellNames.lastIndexOf(",")));
							projectParamService.createNrDistance(stationDistanceNRPojo);
						}else if(operatorType.equals(ProjectParamInfoType.LTE)){
							stationDistanceLTEPojo = new StationDistanceLTEPojo();
							stationDistanceLTEPojo.setCellName(cellName);
							stationDistanceLTEPojo.setRegin(region);
							stationDistanceLTEPojo.setDistance(distanceAvg);
							stationDistanceLTEPojo.setnCellNames(nCellNames.substring(0, nCellNames.lastIndexOf(",")));
							projectParamService.createLteDistance(stationDistanceLTEPojo);
						}
				 }
			}
			LOGGER.info(region+"工参导入：站间距计算导入成功");
			System.out.println(region+"工参导入：站间距计算导入成功");
		} catch (Exception e) {
			LOGGER.info(region+"工参导入：站间距计算失败");
			e.printStackTrace();
		}

	}

	

	public List<T> getStationDistancePojoList() {
		return stationDistancePojoList;
	}


	public void setStationDistancePojoList(List<T> stationDistancePojoList) {
		this.stationDistancePojoList = stationDistancePojoList;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getOperatorType() {
		return operatorType;
	}


	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}







}
