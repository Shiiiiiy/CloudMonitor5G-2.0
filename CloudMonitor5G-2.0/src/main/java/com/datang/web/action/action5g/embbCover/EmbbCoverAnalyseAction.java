package com.datang.web.action.action5g.embbCover;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.embbCover.CoverAnalyseAllFiledPojo;
import com.datang.domain.embbCover.LteWeakCoverAnalysePojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.service.service5g.embbCover.EmbbCoverAnalyseService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.util.GPSUtils;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 5G EMBB覆盖分析Action
 * 
 * @author maxuancheng
 * 
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class EmbbCoverAnalyseAction extends PageAction implements ModelDriven<LteWeakCoverAnalysePojo> {
	
	private LteWeakCoverAnalysePojo lteWeakCoverAnalyse = new LteWeakCoverAnalysePojo();
	
	private Integer netType;
	
	private Integer coverType;
	
	private String testLogItemIds;
	
	private String cellNameParam;
	
	private String lon;
	
	private String lat;
	
	private String azimuth;
	@Autowired
	private EmbbCoverAnalyseService embbCoverAnalyseService;
	
	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	
	/**
	 * 日志服务
	 */
	@Autowired
	private ITestLogItemService itestLogItemService;
	
	/**
     * 主页面跳转
     * @return
     */
    public String nasexceptionviewlistui(){
        return ReturnType.LISTUI;
    }
    
    /**
	 * 下载数据
	 * 
	 * @return
	 */
	public String downloadData() {
		return "downloadData";
	}

	/**
	 * 下载数据
	 * 
	 * @return
	 */
	public InputStream getDownloadData() {
		PageList pageList = new PageList();
		pageList.setRowsCount(super.getRows());
		pageList.setPageNum(super.getPage());
		
		AbstractPageList doPageQuery = doPageQuery(pageList);
		HashMap<String, Object> hashMap = new HashMap<String,Object>();
		hashMap.put("dataList", doPageQuery.getRows());
		String str = "";
		if(coverType == 1){
    		str = "弱";
    	}else if(coverType == 2){
    		str = "过";
    	}else if(coverType == 3){
    		str = "重叠";
    	}else if(coverType == 4){
    		str = "超远";
    	}else if(coverType == 5){
    		str = "反向";
    	}
		hashMap.put("coverName", str);
		
		ByteArrayOutputStream byteOutputStream = null;
		try {
			byteOutputStream = new ByteArrayOutputStream();
			String xlsName = "";
			if(netType == 1){
				xlsName = "templates/覆盖分析数据报表4G.xls";
			}else if(netType == 2){
				xlsName = "templates/覆盖分析数据报表5G.xls";
			}
			Workbook transformToExcel = POIExcelUtil.transformToExcel(hashMap,xlsName);
			transformToExcel.write(byteOutputStream);
			ActionContext.getContext().put("fileName",new String((str + "覆盖分析数据报表.xls").getBytes(), "ISO8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	@Override
	public LteWeakCoverAnalysePojo getModel() {
		return lteWeakCoverAnalyse;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		if(!StringUtils.hasText(testLogItemIds)){
			EasyuiPageList easyuiPageList = new EasyuiPageList();
			easyuiPageList.setRows(null);
			easyuiPageList.setTotal("0");
			return easyuiPageList;
		}
		List<TestLogItem> queryCQTByFloorName = itestLogItemService.queryTestLogItems(testLogItemIds);
		List<String> logNames = new ArrayList<String>();
		for (TestLogItem tl: queryCQTByFloorName) {
			logNames.add(tl.getFileName());
		}
		pageList.putParam("netType", netType);
		pageList.putParam("coverType", coverType);
		pageList.putParam("logNames", logNames);
		pageList.putParam("cellName", lteWeakCoverAnalyse.getCellName());
		AbstractPageList doPageQuery = embbCoverAnalyseService.doPageQuery(pageList);
		return doPageQuery;
	}
	
	

	/**
	 * 根据日志id和小区名查询采样点数据
	 * @author lucheng
	 * date:2020年6月4日 下午4:16:50
	 * @return
	 */
	public String getGpsPointData(){
		List<TestLogItem> queryTestLogItems = testlogItemService
				.queryTestLogItems(testLogItemIds);
		Set<String> logNameSet = new HashSet<String>();
		for (TestLogItem testLogItem : queryTestLogItems) {
			logNameSet.add(testLogItem.getFileName());
		}
		List<String> logNameList = new ArrayList(logNameSet);
		Map<String,Object> map = embbCoverAnalyseService.getParam(cellNameParam,netType);
		
		List<StationEtgTralPojo> point = embbCoverAnalyseService.getGpsPointData(logNameList,(Long) map.get("pci"),(Long) map.get("frequency1"),netType);
		for (StationEtgTralPojo stationEtgTralPojo : point) {
			stationEtgTralPojo.setNrCellid(String.valueOf(map.get("cellid")));
		}
//		//allLogNames = "5000001620200416164615_1XW-松林坡ADHV_1_H_DT.l5g";
//		List<StationEtgTralPojo> point = itestLogItemService.getGpsPointData(allLogNames);
		ActionContext.getContext().getValueStack().set("pointList", point);
		ActionContext.getContext().getValueStack().set("longitude", map.get("longitude"));
		ActionContext.getContext().getValueStack().set("latitude", map.get("latitude"));
		return ReturnType.JSON;
	}
	
	/**
	 * 根据日志id和小区名查询采样点数据
	 * @author lucheng
	 * date:2020年6月4日 下午4:16:50
	 * @return
	 */
	public String getGpsPointDis(){
		List<TestLogItem> queryTestLogItems = testlogItemService
				.queryTestLogItems(testLogItemIds);
		Set<String> logNameSet = new HashSet<String>();
		for (TestLogItem testLogItem : queryTestLogItems) {
			logNameSet.add(testLogItem.getFileName());
		}
		List<String> logNameList = new ArrayList(logNameSet); 
		//cellNameParam = "A2_SQ西环高速口DLS_H-42";
		Map<String,Object> map = embbCoverAnalyseService.getParam(cellNameParam,netType);
		
		List<StationEtgTralPojo> point = embbCoverAnalyseService.getGpsPointData(logNameList,(Long) map.get("pci"),(Long) map.get("frequency1"),netType);
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		for (StationEtgTralPojo st : point) {
			Map<String, Object> cellMap = new HashMap<String, Object>();
			double angle = 0;
			if(Double.valueOf(st.getLongtitude()) > Double.valueOf(lon) && Double.valueOf(st.getLatitude()) > Double.valueOf(lat)){
				angle = Math.abs(Double.valueOf(azimuth) - ((90) - Math.atan((Math.abs(Double.valueOf(st.getLatitude()) - Double.valueOf(lat)) 
						/ (Math.abs(Double.valueOf(st.getLongtitude()) - Double.valueOf(lon)) * Math.cos(Double.valueOf(lat)))))*180/Math.PI
						));
			}else if(Double.valueOf(st.getLongtitude()) > Double.valueOf(lon) && Double.valueOf(st.getLatitude()) < Double.valueOf(lat)){
				angle = Math.abs(Double.valueOf(azimuth) - 180 + ((90) - Math.atan((Math.abs(Double.valueOf(st.getLatitude()) - Double.valueOf(lat)) 
						/ (Math.abs(Double.valueOf(st.getLongtitude()) - Double.valueOf(lon)) * Math.cos(Double.valueOf(lat)))))*180/Math.PI
						));
			}else if(Double.valueOf(st.getLongtitude()) < Double.valueOf(lon) && Double.valueOf(st.getLatitude()) < Double.valueOf(lat)){
				angle = Math.abs(Double.valueOf(azimuth) - 180 - ((90) - Math.atan((Math.abs(Double.valueOf(st.getLatitude()) - Double.valueOf(lat)) 
						/ (Math.abs(Double.valueOf(st.getLongtitude()) - Double.valueOf(lon)) * Math.cos(Double.valueOf(lat)))))*180/Math.PI
						));
			}else if(Double.valueOf(st.getLongtitude()) < Double.valueOf(lon) && Double.valueOf(st.getLatitude()) > Double.valueOf(lat)){
				angle = Math.abs(Double.valueOf(azimuth) - 360 + ((90) - Math.atan((Math.abs(Double.valueOf(st.getLatitude()) - Double.valueOf(lat)) 
						/ (Math.abs(Double.valueOf(st.getLongtitude()) - Double.valueOf(lon)) * Math.cos(Double.valueOf(lat)))))*180/Math.PI
						));
			}
			double distance = GPSUtils.distance( Double.valueOf(st.getLatitude()), Double.valueOf(st.getLongtitude()), Double.valueOf(lat), Double.valueOf(lon));
//			System.out.println("lon:"+lon+",lat:"+lat+",lon_m:"+st.getLongtitude()+",lat_m:"+st.getLatitude()+",beamId:"+st.getRecseqno()+",azimuth:"+azimuth+",angle:"+angle+",distance:"+distance);
			cellMap.put("angle", angle);
			cellMap.put("beamId", st.getRecseqno());
			cellMap.put("value", distance);
			if(data.size() == 0){
				data.add(cellMap);
			}else if(data.size() > 0){
				for(int i = 0;i< data.size() ;i++){
					Map<String, Object> angMap = data.get(i);
					if((Double)angMap.get("angle") <= angle){
						data.add(i, cellMap);
						break;
					}
				}
			}
		}
		
		ActionContext.getContext().getValueStack().set("data", data);
		return ReturnType.JSON;
	}
	
	
	/**
     * 获取所有的cellid
     * @return
     */
    public String getCellIds(){
		if(testLogItemIds != null){
			PageList pageList = new PageList();
			pageList.setRowsCount(20);
			pageList.setPageNum(1);
    		String cellids = "";
    		List<TestLogItem> queryCQTByFloorName = itestLogItemService.queryTestLogItems((String)testLogItemIds);
    		List<String> logNames = new ArrayList<String>();
    		for (TestLogItem tl: queryCQTByFloorName) {
    			logNames.add(tl.getFileName());
    		}
    		pageList.putParam("netType", netType);
    		pageList.putParam("coverType", coverType);
    		pageList.putParam("logNames", logNames);
    		AbstractPageList doPageQuery = embbCoverAnalyseService.doPageQuery(pageList);
    		List<CoverAnalyseAllFiledPojo> list = (List<CoverAnalyseAllFiledPojo>)doPageQuery.getRows();
    		for (CoverAnalyseAllFiledPojo coverAnalyseAllFiledPojo : list) {
    			if(coverAnalyseAllFiledPojo.getCellName() != null){
    				cellids = cellids + coverAnalyseAllFiledPojo.getCellId()+",";
    			}
    		}
    		if(!cellids.equals("")){
    			cellids = cellids.substring(0,cellids.length()-1);
    		}
    		ActionContext.getContext().getValueStack().set("cellids", cellids);
    	}
		return ReturnType.JSON;
    }

	public Integer getNetType() {
		return netType;
	}

	public void setNetType(Integer netType) {
		this.netType = netType;
	}

	public Integer getCoverType() {
		return coverType;
	}

	public void setCoverType(Integer coverType) {
		this.coverType = coverType;
	}

	public String getTestLogItemIds() {
		return testLogItemIds;
	}

	public void setTestLogItemIds(String testLogItemIds) {
		this.testLogItemIds = testLogItemIds;
	}

	public String getCellNameParam() {
		return cellNameParam;
	}

	public void setCellNameParam(String cellNameParam) {
		this.cellNameParam = cellNameParam;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}
	
}
