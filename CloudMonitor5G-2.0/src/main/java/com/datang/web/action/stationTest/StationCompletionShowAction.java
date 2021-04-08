package com.datang.web.action.stationTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.errorLogManage.impl.ErrorLogManageService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.stationTest.StationCompletionShowService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;

/**
 * 单站进度查看Controller
 * @author lucheng
 *
 */
@Controller
@Scope("prototype")
public class StationCompletionShowAction extends PageAction implements
ModelDriven<PlanParamPojo>{
	
	@Autowired
	private StationCompletionShowService staionCompletionShowService;
	
	@Autowired
	private ErrorLogManageService errorLogManageService;
	
	/**
	 * 菜单管理服务
	 */
	@Autowired
	private IMenuManageService menuManageService;
	
	/**
	 * 设备组服务
	 */
	@Autowired
	private TerminalGroupService groupService;
	
	/**
	 * 区域管理服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	
	private static final long serialVersionUID = 1L;
	
	private String cityId;
	
	private String cityName;
	
	private String cityType;
	
	private String cellName;
	
	private String[] reportAnalysisname ={"全部通过","PING测试未通过","接入/切换测试未通过","FTP测试未通过"};
	
	private long onlineNum=0L; //在线数量
	private long offlineNum=0L;//离线数量
	private long activeNUm =0L;//活跃数量
	
	private long allPassNum=0L;//全部通过数量
	private long pingNoPassNum=0L;//PING测试未通过
	private long accessSwitchNoPassNUm =0L;//接入/切换测试未通过
	private long FtpTestNoPassNUm =0L;//FTP测试未通过

	public String goTOStationCompletionShowJsp(){
		
		HashMap<String, Long> dataMap = new HashMap<>();
		dataMap.put("allPassNum", 0L);
		dataMap.put("pingNoPassNum", 0L);
		dataMap.put("accessSwitchNoPassNUm", 0L);
		dataMap.put("FtpTestNoPassNUm", 0L);
		
		ActionContext.getContext().put("userNumber", JSONArray.fromObject(dataMap));
		return ReturnType.LISTUI;
	}
	
	/**
	 * 根据城市名获取shp文件名
	 * @author maxuancheng
	 * date:2020年3月4日 下午5:24:27
	 * @return
	 */
	public String getShpName(){
		List<PlanParamPojo> ps = staionCompletionShowService.getDataOfCityName(cityName);
		boolean flag = false;
		int testFinish = 0;
		ArrayList<String> testFinishList = new ArrayList<>();
		int testing = 0;
		ArrayList<String> testingList = new ArrayList<>();
		int allTestFinish = 0;
		ArrayList<String> allTestFinishList = new ArrayList<>();
		HashMap<String, Object> dataMap = new HashMap<>();
		
		for (PlanParamPojo planParamPojo : ps) {
			if(planParamPojo.getCellInfo() != null && flag == false){
				flag = true;
				dataMap.put("shpName", planParamPojo.getCellInfo().getLteCellGisFileName()+".shp");
				dataMap.put("lon", planParamPojo.getLon());
				dataMap.put("lat", planParamPojo.getLat());
			}
			if("全部通过".equals(planParamPojo.getNoPassTestEvent())){
				allTestFinish++;
				allTestFinishList.add(planParamPojo.getCellId());
			}else if((planParamPojo.getGoodFtpUpload() == 1 || planParamPojo.getGoodFtpUpload() == 2)
					&& (planParamPojo.getMidFtpUpload() == 1 || planParamPojo.getMidFtpUpload() == 2)
					&& (planParamPojo.getBadFtpUpload() == 1 || planParamPojo.getBadFtpUpload() == 2)
					&& (planParamPojo.getGoodFtpdownload() == 1 || planParamPojo.getGoodFtpdownload() == 2)
					&& (planParamPojo.getMidFtpdownload() == 1 || planParamPojo.getMidFtpdownload() == 2)
					&& (planParamPojo.getBadFtpdownload() == 1 || planParamPojo.getBadFtpdownload() == 2)
					&& (planParamPojo.getRaodianTest() == 1 || planParamPojo.getRaodianTest() == 2)
					&& (planParamPojo.getGoodPing32() == 1 || planParamPojo.getGoodPing32() == 2)
					&& (planParamPojo.getMidPing32() == 1 || planParamPojo.getMidPing32() == 2)
					&& (planParamPojo.getBadPing32() == 1 || planParamPojo.getBadPing32() == 2)
					&& (planParamPojo.getGoodPing1500() == 1 || planParamPojo.getGoodPing1500() == 2)
					&& (planParamPojo.getMidPing1500() == 1 || planParamPojo.getMidPing1500() == 2)
					&& (planParamPojo.getBadPing1500() == 1 || planParamPojo.getBadPing1500() == 2)
					&& (planParamPojo.getGoodEndcSuccessRatio() == 1 || planParamPojo.getGoodEndcSuccessRatio() == 2)
					&& (planParamPojo.getMidEndcSuccessRatio() == 1 || planParamPojo.getMidEndcSuccessRatio() == 2)
					&& (planParamPojo.getBadEndcSuccessRatio() == 1 || planParamPojo.getBadEndcSuccessRatio() == 2)){
				testFinish++;
				testFinishList.add(planParamPojo.getCellId());
			}else if((planParamPojo.getGoodFtpUpload() == 1 || planParamPojo.getGoodFtpUpload() == 2)
					|| (planParamPojo.getMidFtpUpload() == 1 || planParamPojo.getMidFtpUpload() == 2)
					|| (planParamPojo.getBadFtpUpload() == 1 || planParamPojo.getBadFtpUpload() == 2)
					|| (planParamPojo.getGoodFtpdownload() == 1 || planParamPojo.getGoodFtpdownload() == 2)
					|| (planParamPojo.getMidFtpdownload() == 1 || planParamPojo.getMidFtpdownload() == 2)
					|| (planParamPojo.getBadFtpdownload() == 1 || planParamPojo.getBadFtpdownload() == 2)
					|| (planParamPojo.getRaodianTest() == 1 || planParamPojo.getRaodianTest() == 2)
					|| (planParamPojo.getGoodPing32() == 1 || planParamPojo.getGoodPing32() == 2)
					|| (planParamPojo.getMidPing32() == 1 || planParamPojo.getMidPing32() == 2)
					|| (planParamPojo.getBadPing32() == 1 || planParamPojo.getBadPing32() == 2)
					|| (planParamPojo.getGoodPing1500() == 1 || planParamPojo.getGoodPing1500() == 2)
					|| (planParamPojo.getMidPing1500() == 1 || planParamPojo.getMidPing1500() == 2)
					|| (planParamPojo.getBadPing1500() == 1 || planParamPojo.getBadPing1500() == 2)
					|| (planParamPojo.getGoodEndcSuccessRatio() == 1 || planParamPojo.getGoodEndcSuccessRatio() == 2)
					|| (planParamPojo.getMidEndcSuccessRatio() == 1 || planParamPojo.getMidEndcSuccessRatio() == 2)
					|| (planParamPojo.getBadEndcSuccessRatio() == 1 || planParamPojo.getBadEndcSuccessRatio() == 2)){
				testing++;
				testingList.add(planParamPojo.getCellId());
			}
		}
		
		dataMap.put("allTestFinish", allTestFinish);
		dataMap.put("allTestFinishList", allTestFinishList);
		dataMap.put("testFinish", testFinish);
		dataMap.put("testFinishList", testFinishList);
		dataMap.put("testing", testing);
		dataMap.put("testingList", testingList);
		ActionContext.getContext().getValueStack().push(dataMap);
		return ReturnType.JSON;
	}
	
	/**
	 * 根据城市名获取相关数据(用户数,软禁版本统计,终端使用情况统计,终端类型统计)
	 * @author lucheng
	 * @return
	 */
	public String getCityData(){
		Map<String, Object> map = new HashMap<>();
		Map<String, Long> userNumber = getUserNumber(cityId);
		map.put("userNumber", JSONArray.fromObject(userNumber));
		
//		List<Object> terminalNumber = getTerminalNumber(cityName);
//		map.put("terminalNumber", terminalNumber);
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}
	
	/**
	 * 获取用户数量
	 * @author lucheng
	 * @return 
	 */
	public Map<String, Long> getUserNumber(String cityId){
		Map<String, Long> userNumber = new HashMap<>();
		ArrayList<String> boxIdS = new ArrayList<String>();
		List<TerminalGroup> citys = new ArrayList<TerminalGroup>();
		if(StringUtils.hasText(cityId)){
			if(cityType.equals("Province")){  //判断是省还是城市
				List<TerminalGroup> allCityGroup = terminalGroupService.getAllCityGroup();   //得到所有城市记录
				for (TerminalGroup terminalGroup : allCityGroup) {
					String name = terminalGroupService.getProvinceNameByCityGroup(terminalGroup);
					if(name.equals(cityName)){
						citys.add(terminalGroup);
					}
				}
				if(citys != null && citys.size() > 0){		
					for (TerminalGroup group : citys) {
						if (group.getTerminals() != null) {
							boxIdS = getTerminal(String.valueOf(group.getId()));
							getReportAnalysssResult(group.getName());
						}
					}
				}
				
			}else if(cityType.equals("City")){
				boxIdS = getTerminal(cityId);
				getReportAnalysssResult(cityName);
			}
		}else{   //全部或""
			// 获取用户权限范围内的二级域menu
			List<TerminalMenu> cities = menuManageService.getCities();
			// 将二级域menu转化成terminalGroup
			List<TerminalGroup> allCityGroup = groupService.getGroupsByMenus(cities);
			if(allCityGroup != null && allCityGroup.size() > 0){
				for (TerminalGroup terminalGroup : allCityGroup) {
					Set<Terminal> terminals = terminalGroup.getTerminals();
					getReportAnalysssResult(terminalGroup.getName());
					for (Terminal terminal : terminals) {
						if(terminal.isOnline()){
							onlineNum++;
						}else{
							offlineNum++;
						}
						if(terminal.getBoxId()!=null&&!terminal.getBoxId().equals("")){
							boxIdS.add(terminal.getBoxId());
						}
					}
				}
			}
		}
		/*if(boxIdS.size()>0){   //得到活跃数量
			String[] boxidArray = boxIdS.toArray(new String[boxIdS.size()]);
			Date currentDate = new Date();
			Calendar c= Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DAY_OF_MONTH, -6);
			Date pastDate = c.getTime();
			for (String id : boxidArray) {
				ATULoginLogItem active = atuLoginService.getActiveNUm(pastDate,id); 
				if(active!=null){
					activeNUm++;
				}
			}
			
		}*/
		userNumber.put("onlineNum", onlineNum);
		userNumber.put("offlineNum", offlineNum);
		userNumber.put("activeNUm", activeNUm);
		
		userNumber.put("allPassNum", allPassNum);
		userNumber.put("pingNoPassNum", pingNoPassNum);
		userNumber.put("accessSwitchNoPassNUm", accessSwitchNoPassNUm);
		userNumber.put("FtpTestNoPassNUm", FtpTestNoPassNUm);
		
		return userNumber;
	}
	
	/**
	 * 根据城市id得到搜有终端的boxid和在线终端数量和离线数量
	 * @param id
	 * @return
	 */
	public ArrayList<String> getTerminal(String id){
		ArrayList<String> ids = new ArrayList<String>();
		List<Terminal> cityIdTerminal = terminalGroupService.getAllTerminalByGroupId(Long.valueOf(id));
		if(cityIdTerminal != null && cityIdTerminal.size() > 0){
			for (Terminal terminal : cityIdTerminal) {
				if(terminal.isOnline()){
					onlineNum++;
				}else{
					offlineNum++;
				}
				if(terminal.getBoxId()!=null&&!terminal.getBoxId().equals("")){
					ids.add(terminal.getBoxId());
				}
			}
		}
		return ids;
	}
	
	/**
	 * 根据城市名查询单验报告质量分析，即不通过的测试项
	 * @param city
	 */
	public void getReportAnalysssResult(String city){
		for(int i=0;i<reportAnalysisname.length;i++){
			long num = staionCompletionShowService.getReportAnalysssResult(reportAnalysisname[i],city);
			if(num>0){
				switch (i) {
				case 0://全部通过数量
					allPassNum = allPassNum+num;
					break;
				case 1://PING测试未通过
					pingNoPassNum = pingNoPassNum+num;
					break;
				case 2://接入/切换测试未通过
					accessSwitchNoPassNUm = accessSwitchNoPassNUm+num;
					break;
				case 3://FTP测试未通过
					FtpTestNoPassNUm = FtpTestNoPassNUm+num;
					break;
				}
			}
		}
	}
	
	/**
	 * 根据城市名获取终端类型统计
	 * @author maxuancheng
	 * @param cityName 为null时获取所有城市
	 * @return
	 */
	private List<Object> getTerminalNumber(String cityName) {
		return errorLogManageService.getTerminalNumber(cityName);
	}
	
	/**
	 * 根据小区名查询小区经纬度
	 * @author maxuancheng
	 * date:2020年5月6日 上午10:13:47
	 * @return
	 */
	public String queryCellLonAndLat(){
		List<PlanParamPojo> queryCellLonAndLat = staionCompletionShowService.queryCellLonAndLat(cellName,cityName);
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		for (int i = 0; i < queryCellLonAndLat.size();i++) {
			PlanParamPojo pp = queryCellLonAndLat.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", pp.getLon() + "," + pp.getLat() + "," + i);
			map.put("text", pp.getCellName());
			data.add(map);
		}
		ActionContext.getContext().getValueStack().push(data);
		return ReturnType.JSON;
	}


	@Override
	public PlanParamPojo getModel() {
		return null;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return null;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the cityType
	 */
	public String getCityType() {
		return cityType;
	}

	/**
	 * @param cityType the cityType to set
	 */
	public void setCityType(String cityType) {
		this.cityType = cityType;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	

}
