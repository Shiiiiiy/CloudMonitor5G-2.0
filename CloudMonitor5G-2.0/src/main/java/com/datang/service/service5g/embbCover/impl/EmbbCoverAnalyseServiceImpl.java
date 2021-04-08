package com.datang.service.service5g.embbCover.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.dao5g.embbCover.LteWeakCoverAnalyseDao;
import com.datang.domain.embbCover.CoverAnalyseAllFiledPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.service.service5g.embbCover.EmbbCoverAnalyseService;

@Service
@Transactional
@SuppressWarnings("rawtypes")
public class EmbbCoverAnalyseServiceImpl implements EmbbCoverAnalyseService {

	@Autowired
	private LteWeakCoverAnalyseDao LteWeakCoverAnalyseDao;
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		AbstractPageList doPageQuery = LteWeakCoverAnalyseDao.doPageQuery(pageList);
		List rows = doPageQuery.getRows();
		//处理数据
		Integer netType = (Integer) pageList.getParam("netType");
		Integer coverType = (Integer) pageList.getParam("coverType");
		List<CoverAnalyseAllFiledPojo> handleData = handleData(rows,netType,coverType);
		
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		int firstResult = (pageNum - 1) * rowsCount;
		
		List<CoverAnalyseAllFiledPojo> rltData  = new ArrayList<CoverAnalyseAllFiledPojo>();
		for (int i = firstResult; i < (firstResult+rowsCount); i++) {
			if(handleData.size()>i){
				rltData.add(handleData.get(i));
			}else{
				break;
			}
		}
		doPageQuery.setRows(rltData);
		doPageQuery.setTotal(handleData.size()+"");
		return doPageQuery;
	}
	
	/**
	 * 数据处理
	 * @author maxuancheng
	 * date:2020年5月8日 下午4:24:26
	 * @param list
	 * @param netType
	 * @return
	 */
	private List<CoverAnalyseAllFiledPojo> handleData(List<Object> dataList,Integer netType,Integer coverType){
		try {
			List<CoverAnalyseAllFiledPojo> list = new ArrayList<CoverAnalyseAllFiledPojo>();
			Map<String, List<Object>> dataMap = new HashMap<String,List<Object>>();
			for (Object data : dataList) {
				Class<? extends Object> clazz = data.getClass();
				Method method = clazz.getMethod("getCellName");
				String cellName = (String)method.invoke(data);
				List<Object> cellList = null;
				if(dataMap.get(cellName) == null){
					cellList = new ArrayList<Object>();
				}else{
					cellList = dataMap.get(cellName);
				}
				cellList.add(data);
				dataMap.put(cellName, cellList);
			}
			
			Iterator<Entry<String, List<Object>>> iterator = dataMap.entrySet().iterator();
			while (iterator.hasNext()) {
				CoverAnalyseAllFiledPojo caaf = new CoverAnalyseAllFiledPojo();
				Entry<String, List<Object>> entry = iterator.next();
				List<Object> valueList = entry.getValue();
				if(valueList != null && valueList.size() > 0){
					Object oneObject = valueList.get(0);
					Class<? extends Object> clazz = oneObject.getClass();
					
					String cellName = (String) clazz.getMethod("getCellName").invoke(oneObject);
					caaf.setCellName(cellName);
					//根据cellName获取小区信息
					Object cellData = LteWeakCoverAnalyseDao.getCellData(cellName,netType);
					if(cellData != null){
						Class<? extends Object> cellDataClass = cellData.getClass();
						Long cellId = (Long)cellDataClass.getMethod("getCellId").invoke(cellData);
						caaf.setCellId(cellId.toString());
						Float azimuth = (Float)cellDataClass.getMethod("getAzimuth").invoke(cellData);
						caaf.setAzimuth(Math.round(azimuth));
						Float downtilt = (Float)cellDataClass.getMethod("getTotalTilt").invoke(cellData);
						caaf.setDowntilt(Math.round(downtilt));
						String tac = (String)cellDataClass.getMethod("getTac").invoke(cellData);
						caaf.setTac(tac);
					}
					String regin = (String) clazz.getMethod("getRegin").invoke(oneObject);
					caaf.setRegin(regin);
					Integer stationDistance = null;
					if(netType == 1){
						stationDistance = LteWeakCoverAnalyseDao.getLTEDistance(cellName,regin);
					}else if(netType == 2){
						stationDistance = LteWeakCoverAnalyseDao.getNRDistance(cellName,regin);
					}
					caaf.setStationDistance(stationDistance);
					//计算数据里面的平均或总和部分
					computeMeanData(caaf, valueList,netType,coverType);
					
					list.add(caaf);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 计算数据里面的平均或总和部分
	 * @author maxuancheng
	 * date:2020年5月11日 下午5:50:21
	 * @param caaf
	 * @param valueList
	 * @return 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private CoverAnalyseAllFiledPojo computeMeanData(CoverAnalyseAllFiledPojo caaf, List<Object> valueList,Integer netType,Integer coverType)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Integer pointNumber = null;
		Integer distanceSum = null;
		Integer duration = null;
		Integer rNumberSum = null;
		Integer spaceSum = null;
		Float rsrpSum = null;
		Float rrsrpSum = null;
		Float bestRsrpSum = null;
		Float sinrSum = null;
		Float iblerSum = null;
		
		for (Object object : valueList) {
			Class<? extends Object> clazz2 = object.getClass();
			if(pointNumber == null && clazz2.getMethod("getPointNumber").invoke(object) != null){
				pointNumber = Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}else if(pointNumber != null){
				pointNumber = pointNumber + Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}
			
			if(distanceSum == null && clazz2.getMethod("getDistance").invoke(object) != null){
				distanceSum = Integer.valueOf((String) clazz2.getMethod("getDistance").invoke(object));
			}else if(distanceSum != null){
				distanceSum = distanceSum + Integer.valueOf((String) clazz2.getMethod("getDistance").invoke(object));
			}
			
			if(duration == null && clazz2.getMethod("getDuration").invoke(object) != null){
				duration = Integer.valueOf((String) clazz2.getMethod("getDuration").invoke(object));
			}else if(duration != null){
				duration = duration + Integer.valueOf((String) clazz2.getMethod("getDuration").invoke(object));
			}
			
			if(rNumberSum == null && clazz2.getMethod("getCoverNumber").invoke(object) != null){
				rNumberSum = Integer.valueOf((String) clazz2.getMethod("getCoverNumber").invoke(object));
			}else if(rNumberSum != null){
				rNumberSum = rNumberSum + Integer.valueOf((String) clazz2.getMethod("getCoverNumber").invoke(object));
			}
			
			if(spaceSum == null && clazz2.getMethod("getCoverSpace").invoke(object) != null){
				spaceSum = Integer.valueOf((String) clazz2.getMethod("getCoverSpace").invoke(object)) 
						* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}else if(spaceSum != null){
				spaceSum = spaceSum + Integer.valueOf((String) clazz2.getMethod("getCoverSpace").invoke(object)) 
						* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}
			
			if(rsrpSum == null && clazz2.getMethod("getRsrp").invoke(object) != null){
				rsrpSum = Float.valueOf((String) clazz2.getMethod("getRsrp").invoke(object)) 
						* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}else if(rsrpSum != null){
				rsrpSum = rsrpSum + Float.valueOf((String) clazz2.getMethod("getRsrp").invoke(object)) 
				* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}
			
			if(coverType==1){
				if(rrsrpSum == null && clazz2.getMethod("getCoverRsrp").invoke(object) != null){
					rrsrpSum = Float.valueOf((String) clazz2.getMethod("getCoverRsrp").invoke(object)) 
							* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
				}else if(rrsrpSum != null){
					rrsrpSum = rrsrpSum + Float.valueOf((String) clazz2.getMethod("getCoverRsrp").invoke(object)) 
					* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
				}
			}
			
			if(bestRsrpSum == null && clazz2.getMethod("getBestNcellRsrp").invoke(object) != null){
				bestRsrpSum = Float.valueOf((String) clazz2.getMethod("getBestNcellRsrp").invoke(object)) 
						* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}else if(bestRsrpSum != null){
				bestRsrpSum = bestRsrpSum + Float.valueOf((String) clazz2.getMethod("getBestNcellRsrp").invoke(object)) 
				* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}
			
			if(sinrSum == null && clazz2.getMethod("getSinr").invoke(object) != null){
				sinrSum = Float.valueOf((String) clazz2.getMethod("getSinr").invoke(object)) 
						* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}else if(sinrSum != null){
				sinrSum = sinrSum + Float.valueOf((String) clazz2.getMethod("getSinr").invoke(object)) 
				* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}
			
			if(iblerSum == null && clazz2.getMethod("getIbler").invoke(object) != null){
				iblerSum = Float.valueOf((String) clazz2.getMethod("getIbler").invoke(object)) 
						* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}else if(iblerSum != null){
				iblerSum = iblerSum + Float.valueOf((String) clazz2.getMethod("getIbler").invoke(object)) 
				* Integer.valueOf((String) clazz2.getMethod("getPointNumber").invoke(object));
			}
			
		}
		
		if(pointNumber != null){
			caaf.setPointNumberSum(pointNumber);
		}
		if(distanceSum != null){
			caaf.setDistanceSum(distanceSum);
		}
		if(duration != null){
			caaf.setDurationSum(duration);
		}
		if(rNumberSum != null && pointNumber != null){
			caaf.setPointRatio(Float.valueOf(rNumberSum.toString()) / pointNumber*100);	
		}
		if(spaceSum != null && pointNumber != null){
			caaf.setPointToStationSpace((spaceSum / pointNumber));
		}
		if(rsrpSum != null && pointNumber != null){
			caaf.setRsrpAverage(Math.round(rsrpSum / pointNumber));
		}
		if(rrsrpSum != null && pointNumber != null){
			caaf.setRrsrpAverage(Math.round(rrsrpSum / pointNumber));
		}
		if(bestRsrpSum != null && pointNumber != null){
			caaf.setRsrpBestAverage(Math.round(bestRsrpSum / pointNumber));
		}
		if(sinrSum != null && pointNumber != null){
			caaf.setSinrAverage((float)Math.round(sinrSum / pointNumber * 100) / 100);
		}
		if(iblerSum != null && pointNumber != null){
			caaf.setIblerAverage((float)Math.round(iblerSum / pointNumber* 100) / 100);
		}
		return caaf;
	}
	
	@Override
	public Map<String,Object> getParam(String cellName,Integer netType){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			Object cellData =  LteWeakCoverAnalyseDao.getCellData(cellName, netType);
			Class<? extends Object> cellDataClass = cellData.getClass();
			Long pci = (Long)cellDataClass.getMethod("getPci").invoke(cellData);
			map.put("pci", pci);
			Long frequency1 = (Long)cellDataClass.getMethod("getFrequency1").invoke(cellData);
			map.put("frequency1", frequency1);
			Float longitude = (Float)cellDataClass.getMethod("getLongitude").invoke(cellData);
			map.put("longitude", longitude);
			Float latitude = (Float)cellDataClass.getMethod("getLatitude").invoke(cellData);
			map.put("latitude", latitude);
			Long cellid = (Long)cellDataClass.getMethod("getCellId").invoke(cellData);
			map.put("cellid", cellid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public List<StationEtgTralPojo> getGpsPointData(List<String> logName,Long pci,Long frequency1,Integer netType){
		return LteWeakCoverAnalyseDao.getGpsPointData(logName,pci,frequency1,netType);
	}

}
