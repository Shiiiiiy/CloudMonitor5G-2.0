package com.datang.web.action.oppositeOpen;

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
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.oppositeOpen.OppositeOpen3dCompletionShowService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;

/**
 * 反开3d报告生成Controller
 * @author maxuancheng
 *
 */
@Controller
@Scope("prototype")
public class OppositeOpen3dCompletionShowAction extends PageAction implements ModelDriven<Plan4GParam> {

	private static final long serialVersionUID = 1L;
	
	private Plan4GParam plan4GParam;
	
	@Autowired
	private OppositeOpen3dCompletionShowService oppositeOpen3dCompletionShowService;
	
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
	
	
	private String cityId;
	
	private String cityName;
	
	private String cityType;
	
	private String cellName;
	
	private String[] reportAnalysisname ={"全部通过","PING测试未通过","接入测试未通过","FTP测试未通过","CSFB测试未通过","VoLte测试未通过"};
	
	private long onlineNum=0L; //在线数量
	private long offlineNum=0L;//离线数量
	private long activeNUm =0L;//活跃数量
	
	private long allPassNum=0L;//全部通过数量
	private long pingNoPassNum=0L;//PING测试未通过
	private long accessNoPassNUm =0L;//接入测试未通过
	private long FtpTestNoPassNUm =0L;//FTP测试未通过
	private long CSFBTestNoPassNUm =0L;//CSFB测试未通过
	private long VoLteTestNoPassNUm =0L;//VoLte测试未通过

	
	public String goToOppositeOpen3dCompletionShowJsp(){
		HashMap<String, Long> dataMap = new HashMap<>();
		dataMap.put("allPassNum", 0L);
		dataMap.put("pingNoPassNum", 0L);
		dataMap.put("accessSwitchNoPassNUm", 0L);
		dataMap.put("FtpTestNoPassNUm", 0L);
		ActionContext.getContext().put("userNumber", JSONArray.fromObject(dataMap));
		
		/*Map<String, Long> userNumber = getUserNumber(cityId);
		ActionContext.getContext().put("userNumber", JSONArray.fromObject(userNumber));	*/
		return ReturnType.LISTUI;
	}
	
	/**
	 * 根据城市名获取shp文件名
	 * @author maxuancheng
	 * date:2020年3月4日 下午5:24:27
	 * @return
	 */
	public String getShpName(){
		List<Plan4GParam> ps = oppositeOpen3dCompletionShowService.getDataOfCityName(cityName);
		
		boolean flag = false;
		int testFinish = 0;
		ArrayList<Long> testFinishList = new ArrayList<>();
		int testing = 0;
		ArrayList<Long> testingList = new ArrayList<>();
		int allTestFinish = 0;
		ArrayList<Long> allTestFinishList = new ArrayList<>();
		HashMap<String, Object> dataMap = new HashMap<>();
		
		
		for (Plan4GParam planParamPojo : ps) {
			if(planParamPojo.getCellInfo() != null && flag == false){
				flag = true;
				dataMap.put("shpName", planParamPojo.getCellInfo().getLteCellGisFileName()+".shp");
				dataMap.put("lon", planParamPojo.getLongitude());
				dataMap.put("lat", planParamPojo.getLatitude());
			}
			if("全部通过".equals(planParamPojo.getNoPassTestEvent())){
				allTestFinish++;
				allTestFinishList.add(planParamPojo.getCellId());
			}else if((planParamPojo.getFtpDownloadGood() == 1 || planParamPojo.getFtpDownloadGood() == 2)
					&& (planParamPojo.getFtpdownloadBad() == 1 || planParamPojo.getFtpdownloadBad() == 2)
					&& (planParamPojo.getFtpdownloadMid() == 1 || planParamPojo.getFtpdownloadMid() == 2)
					&& (planParamPojo.getFtpUploadGood() == 1 || planParamPojo.getFtpUploadGood() == 2)
					&& (planParamPojo.getFtpUploadMid() == 1 || planParamPojo.getFtpUploadMid() == 2)
					&& (planParamPojo.getFtpUploadBad() == 1 || planParamPojo.getFtpUploadBad() == 2)
					&& (planParamPojo.getRaodianTest() == 1 || planParamPojo.getRaodianTest() == 2)
					&& (planParamPojo.getPing32Good() == 1 || planParamPojo.getPing32Good() == 2)
					&& (planParamPojo.getPing32Mid() == 1 || planParamPojo.getPing32Mid() == 2)
					&& (planParamPojo.getPing32Bad() == 1 || planParamPojo.getPing32Bad() == 2)
					&& (planParamPojo.getPing1500Good() == 1 || planParamPojo.getPing1500Good() == 2)
					&& (planParamPojo.getPing1500Mid() == 1 || planParamPojo.getPing1500Mid() == 2)
					&& (planParamPojo.getPing1500Bad() == 1 || planParamPojo.getPing1500Bad() == 2)
					&& (planParamPojo.getGoodEndcSuccessRatio() == 1 || planParamPojo.getGoodEndcSuccessRatio() == 2)
					&& (planParamPojo.getCsfTest() == 1 || planParamPojo.getCsfTest() == 2)
					&& (planParamPojo.getVolteTest() == 1 || planParamPojo.getVolteTest() == 2)){
				testFinish++;
				testFinishList.add(planParamPojo.getCellId());
			}else if((planParamPojo.getFtpDownloadGood() == 1 || planParamPojo.getFtpDownloadGood() == 2)
					|| (planParamPojo.getFtpdownloadBad() == 1 || planParamPojo.getFtpdownloadBad() == 2)
					|| (planParamPojo.getFtpdownloadMid() == 1 || planParamPojo.getFtpdownloadMid() == 2)
					|| (planParamPojo.getFtpUploadGood() == 1 || planParamPojo.getFtpUploadGood() == 2)
					|| (planParamPojo.getFtpUploadMid() == 1 || planParamPojo.getFtpUploadMid() == 2)
					|| (planParamPojo.getFtpUploadBad() == 1 || planParamPojo.getFtpUploadBad() == 2)
					|| (planParamPojo.getRaodianTest() == 1 || planParamPojo.getRaodianTest() == 2)
					|| (planParamPojo.getPing32Good() == 1 || planParamPojo.getPing32Good() == 2)
					|| (planParamPojo.getPing32Mid() == 1 || planParamPojo.getPing32Mid() == 2)
					|| (planParamPojo.getPing32Bad() == 1 || planParamPojo.getPing32Bad() == 2)
					|| (planParamPojo.getPing1500Good() == 1 || planParamPojo.getPing1500Good() == 2)
					|| (planParamPojo.getPing1500Mid() == 1 || planParamPojo.getPing1500Mid() == 2)
					|| (planParamPojo.getPing1500Bad() == 1 || planParamPojo.getPing1500Bad() == 2)
					|| (planParamPojo.getGoodEndcSuccessRatio() == 1 || planParamPojo.getGoodEndcSuccessRatio() == 2)
					|| (planParamPojo.getCsfTest() == 1 || planParamPojo.getCsfTest() == 2)
					|| (planParamPojo.getVolteTest() == 1 || planParamPojo.getVolteTest() == 2)){
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
		userNumber.put("accessSwitchNoPassNUm", accessNoPassNUm);
		userNumber.put("FtpTestNoPassNUm", FtpTestNoPassNUm);
		userNumber.put("CSFBTestNoPassNUm", CSFBTestNoPassNUm);
		userNumber.put("VoLteTestNoPassNUm", VoLteTestNoPassNUm);
		
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
			long num = oppositeOpen3dCompletionShowService.getReportAnalysssResult(reportAnalysisname[i],city);
			if(num>0){
				switch (i) {
				case 0://全部通过数量
					allPassNum = allPassNum+num;
					break;
				case 1://PING测试未通过
					pingNoPassNum = pingNoPassNum+num;
					break;
				case 2://接入测试未通过
					accessNoPassNUm = accessNoPassNUm+num;
					break;
				case 3://FTP测试未通过
					FtpTestNoPassNUm = FtpTestNoPassNUm+num;
					break;
				case 4://CSFB测试未通过
					CSFBTestNoPassNUm = CSFBTestNoPassNUm+num;
					break;
				case 5://VoLte测试未通过
					VoLteTestNoPassNUm = VoLteTestNoPassNUm+num;
					break;
				}
			}
		}
	}
	
	/**
	 * 根据小区名查询小区经纬度
	 * @author maxuancheng
	 * date:2020年5月6日 上午10:13:47
	 * @return
	 */
	public String queryCellLonAndLat(){
		List<Plan4GParam> queryCellLonAndLat = oppositeOpen3dCompletionShowService.queryCellLonAndLat(cellName,cityName);
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		for (int i = 0; i < queryCellLonAndLat.size();i++) {
			Plan4GParam pp = queryCellLonAndLat.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", pp.getLongitude() + "," + pp.getLatitude() + "," + i);
			map.put("text", pp.getCellName());
			data.add(map);
		}
		ActionContext.getContext().getValueStack().push(data);
		return ReturnType.JSON;
	}
	

	@Override
	public Plan4GParam getModel() {
		
		return plan4GParam;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		
		return null;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityType() {
		return cityType;
	}

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
