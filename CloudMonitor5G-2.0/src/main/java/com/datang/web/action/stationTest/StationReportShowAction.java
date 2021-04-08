package com.datang.web.action.stationTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.jxls.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.ClassUtil;
import com.datang.common.util.Jxls2Utils;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationReportTemplatePojo;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationReportExcelPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.domain.testLogItem.TestLogEtgTrailKpiPojo;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.stationParam.StationParamService;
import com.datang.service.stationTest.StationReportCreatService;
import com.datang.service.stationTest.StationReportExcelService;
import com.datang.service.testLogItem.StationVerificationTestService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalMenuService;
import com.datang.util.ImageUtil;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import sun.misc.BASE64Decoder;

/**
 * 单站报告查看
 * @author maxuancheng
 *
 */
@Controller
@Scope("prototype")
public class StationReportShowAction extends PageAction implements
	ModelDriven<PlanParamPojo>{
	
	private static final long serialVersionUID = 1284972158355134539L;
	
	private static String[] wireStatusArray = {"J,H", "Z", "C"};
	
	private PlanParamPojo planParamPojo = new PlanParamPojo();
	
	@Value("${stationReportFileLink}")
	private String reportUrl;
	
	@Autowired
	private StationReportCreatService stationReportCreatService;
	
	@Autowired
	private StationVerificationTestService stationVerificationTestService;
	
	/**
	 * 设备组服务
	 */
	@Autowired
	private TerminalGroupService groupService;
	
	/**
	 * 菜单管理服务
	 */
	@Autowired
	private IMenuManageService menuManageService;
	
	@Autowired
	private TerminalMenuService terminalMenuService;
	
	@Autowired
	private StationReportExcelService stationReportExcelService;
	
	@Autowired
	private StationParamService stationParamService;
	
	
	@Value("${stationZipUrl}")
	private String stationZipUrl;
	
	@Value("${fileSaveUrl}")
	private String fileSaveUrl;
	
	private Long reportCreateTime;
	
	private String cityIds;
	
	private String testFinishCondition;
	
	private String wirelseeParamConsistency;
	
	private String idsStr;
	/**
	 * 指标
	 */
	private String event;
	
	private Long startTime;// 测试开始时间

	private Long endTime;// 测试结束时间

	private Integer testStatus;// 测试状态

	private Integer testService;// 测试业务
	
	private String wireStatus;

	private String cityNamesStr;
	
	private String reportName;
	
	//轨迹类别，基站类还是小区类
	private String currentOutType;
	
	/**
	 * 地图服务器需要保存图片的路径
	 */
	private String url;
	
	/**
	 * 需要保存的图例图片字节
	 */
	private String imageByte;
	
	public String goToStationReportShowJsp(){
		return ReturnType.LISTUI;
	}
	

	@Override
	public PlanParamPojo getModel() {
		return planParamPojo;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		// 初始化查询时，需要判断用户的区域权限
		if (!StringUtils.hasText(planParamPojo.getCity()) || "全部".equals(planParamPojo.getCity())) {
			if (cityNamesStr == null || !StringUtils.hasText(cityNamesStr)) {
				cityNamesStr = "";
				// 获取用户权限范围内的二级域menu
				List<TerminalMenu> cities = menuManageService.getCities();
				// 将二级域menu转化成terminalGroup
				List<TerminalGroup> groupsByMenus = groupService.getGroupsByMenus(cities);
				for (int i = 0; i < groupsByMenus.size(); i++) {
					cityNamesStr = cityNamesStr + groupsByMenus.get(i).getName();
					if (i != groupsByMenus.size() - 1) {
						cityNamesStr = cityNamesStr + ",";
					}
				}
			}
		}
		
		pageList.putParam("cityStr", cityNamesStr);
		//默认报告生成日期不能为空，必须大于1970年1月1日
		if(startTime!=null){
			pageList.putParam("startTime", startTime);
		}else{
			pageList.putParam("startTime", 19700101L);
		}
		pageList.putParam("endTime", endTime);
		pageList.putParam("reportName", reportName);
		pageList.putParam("reportName", reportName);
		pageList.putParam("model", "NR");
		pageList.putParam("planParamPojo", planParamPojo);
		return stationReportExcelService.doPageQuery(pageList);
	}
	
	public String getAllCell(){
		List<PlanParamPojo> pList = stationReportCreatService.findAll();
		List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
		for (PlanParamPojo planParamPojo : pList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", planParamPojo.getId());
			map.put("text", planParamPojo.getCellName());
			arrayList.add(map);
		}
		ActionContext.getContext().getValueStack().push(arrayList);
		return ReturnType.JSON;
	}
	
	/**
	 * 获取所有轨迹指标
	 * @author lucheng
	 * @date 2020年9月9日 下午8:14:22
	 * @return
	 */
	public String getInitTrailKpi() {
		PageList param = new PageList();

		String trailType = "0,1";
		// 查询所选小区是基站级还是小区级截图,注：主要用于报告生成页面的轨迹截图
		if (StringUtils.hasText(cityNamesStr)) {
			Menu menu = terminalMenuService.get(cityNamesStr);
			if (menu == null || menu.getStationParamPojo() == null) {
				// 查询条件，注：基站级比小区级多了一个PCI的指标，
				trailType = "0"; // 小区类
				ActionContext.getContext().getValueStack().set("outPutType", "0"); // 所选小区为小区级截图
			} else if (menu.getStationParamPojo() != null) {
				StationParamPojo spp = menu.getStationParamPojo();
				StationReportTemplatePojo findTemplateParam = stationParamService.findTemplateParam(0, Long.valueOf(spp.getSingleStationMOdelSelect()));
				// 全部区域模板都设置为小区类和基站类的指标合并，即包含PCI指标
				//所选小区，输出小区级截图或基站级截图或者全部输出
				ActionContext.getContext().getValueStack().set("outPutType", findTemplateParam.getMapTrailType()); // 所选小区，既要输出基站级也要输出小区级截图
			}
		}

		param.putParam("type", "单站验证轨迹");
		param.putParam("trailLevel", trailType);

		List<TestLogEtgTrailKpiPojo> tl = stationReportCreatService.findTrailKpiByParam(param);

		List<HashMap<String, Object>> tlList = new ArrayList<HashMap<String, Object>>();
		for (TestLogEtgTrailKpiPojo pojo : tl) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", pojo.getNameEn());
			map.put("name", pojo.getNameCn());
			tlList.add(map);
		}

		ActionContext.getContext().getValueStack().set("tlList", tlList);

		return ReturnType.JSON;

	}
	
	/**
	 * 得到速率的相关事件
	 * @author lucheng
	 * @date 2020年12月18日 上午11:31:59
	 * @return
	 */
	public String getInitChartKpi(){
		PageList param = new PageList();

		String trailType = "0";
		
		param.putParam("type", "单站验证速率");
		param.putParam("trailLevel", trailType);

		List<TestLogEtgTrailKpiPojo> tl = stationReportCreatService.findTrailKpiByParam(param);

		List<HashMap<String, Object>> tlList = new ArrayList<HashMap<String, Object>>();
		for (TestLogEtgTrailKpiPojo pojo : tl) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", pojo.getNameEn());
			map.put("name", pojo.getNameCn());
			tlList.add(map);
		}

		ActionContext.getContext().getValueStack().set("tlList", tlList);

		return ReturnType.JSON;
	}
	
	/**
	 * 获取轨迹点数据
	 * @author maxuancheng
	 * date:2020年2月25日 下午4:24:52
	 * @return
	 */
	public String getPoint(){
		if(!StringUtils.hasText(planParamPojo.getSiteName())){
			return null;
		}
		
		PageList param = new PageList();
		param.putParam("type", "单站验证轨迹");
		param.putParam("nameCn", event);
		List<TestLogEtgTrailKpiPojo> tl = stationReportCreatService.findTrailKpiByParam(param);
		
		Long createDate = null;
		String cellNameStr = "";
		String pciStr = ""; 
		Float lon = null;
		Float lat = null;
		
		Map<String,Map<String,String>> pciMap = new HashMap<String, Map<String,String>>();
		List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(planParamPojo.getSiteName());
		Integer index=0;
		String city= "";
		for (PlanParamPojo pojo : planParamPojoAscList) {
			city = pojo.getCity();
			Map<String,String> map = new HashMap<String, String>();
			cellNameStr = cellNameStr + pojo.getCellName() + ",";
			pciStr  = pciStr + pojo.getPci().toString() + ",";
			lon = pojo.getLon();
			lat = pojo.getLat();
			if(createDate==null){
				createDate = pojo.getReportCreateDate();
			}else{
				if(pojo.getReportCreateDate()!=null && createDate<pojo.getReportCreateDate()){
					createDate = pojo.getReportCreateDate();
				}
			}
			//对pci进行标注索引，指定pci和localcellid的一一对应
			map.put("pciIndex", index.toString());
			map.put("cellName", pojo.getCellName());
			map.put("color", null);
			pciMap.put(pojo.getPci().toString(), map);
			index++;
		}
		
		//1、通过cellname和测试业务员找到相关日志，筛选出测试日期距离生成日期最近的日志
		List<StationVerificationLogPojo> svlOne = findRecentLog(cellNameStr,tl.get(0).getTestService(),null,createDate);
		
		String fileName = "";
		for (StationVerificationLogPojo stationVerificationLogPojo : svlOne) {
			fileName = fileName + stationVerificationLogPojo.getFileName().replace(" ", "")+",";
		}
		//2.通过日志id去轨迹表中查询记录
		if(svlOne != null && svlOne.size()>0){
			String toCaseEvent = event.replace(" ", "").toLowerCase();
			Menu menu = terminalMenuService.get(city);
			//轨迹点显示是否采用抽样
			boolean trailSamDisplay = false;
			if (menu == null || menu.getStationParamPojo() == null) {
				trailSamDisplay = false;
			} else if (menu.getStationParamPojo() != null) {
				StationParamPojo spp = menu.getStationParamPojo();
				if(spp.getTrailSamDisplay() == null || spp.getTrailSamDisplay().equals("0")){
					trailSamDisplay = false;
				} else {
					trailSamDisplay = true;
				}
			}
			
			if(trailSamDisplay && (toCaseEvent.indexOf("rsrp")!=-1 || toCaseEvent.indexOf("sinr")!=-1 || toCaseEvent.indexOf("pci")!=-1)){
				//rsrp和sinr和pci的相关事件查找抽样表
				List<StationSAMTralPojo> setList = stationReportCreatService.findSamTralByReportId(fileName.substring(0, fileName.lastIndexOf(",")),pciStr,event);
				ActionContext.getContext().getValueStack().set("data", setList);
			}else{
				List<StationEtgTralPojo> setList = stationReportCreatService.findEtgTralByReportId(fileName.substring(0, fileName.lastIndexOf(",")),pciStr,event);
				ActionContext.getContext().getValueStack().set("data", setList);
			}
		}else{
			ActionContext.getContext().getValueStack().set("data", null);
		}
		ActionContext.getContext().getValueStack().set("pciMap", pciMap);
		ActionContext.getContext().getValueStack().set("lon", lon);
		ActionContext.getContext().getValueStack().set("lat", lat);
		
		return ReturnType.JSON;
	}
	
	/**
	 * 获取生成报告的轨迹数据
	 * @author lucheng
	 * date:2020年9月25日 下午4:24:52
	 * @return
	 */
	public String getCreatReportPoint(){
		PageList param = new PageList();
		param.putParam("type", "单站验证轨迹");
		param.putParam("nameCn", event);
		List<TestLogEtgTrailKpiPojo> tl = stationReportCreatService.findTrailKpiByParam(param);
		
		Long createDate = null;
		String cellNameStr = "";
		String pciStr = ""; 
		Float lon = null;
		Float lat = null;
		String city= "";
		Map<String,Map<String,String>> pciMap = new HashMap<String, Map<String,String>>();
		if(StringUtils.hasText(currentOutType)){
			PlanParamPojo find = stationReportCreatService.find(planParamPojo.getId());
			city = find.getCity();
			List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(find.getSiteName());
			if(currentOutType.equals("0")){
				Map<String,String> map = new HashMap<String, String>();
				cellNameStr = cellNameStr + find.getCellName() + ",";
				pciStr  = pciStr + find.getPci().toString() + ",";
				lon = find.getLon();
				lat = find.getLat();
				createDate = find.getReportCreateDate();
				//对pci进行标注索引，指定pci和localcellid的一一对应
				for (Integer i=0;i<planParamPojoAscList.size();i++) {
					if(planParamPojoAscList.get(i).getCellName().equals(find.getCellName())){
						map.put("pciIndex", i.toString());
					}
				}
				map.put("cellName", find.getCellName());
				map.put("color", null);
				pciMap.put(find.getPci().toString(), map);
			}else{
				Integer index=0;
				for (PlanParamPojo pojo : planParamPojoAscList) {
					Map<String,String> map = new HashMap<String, String>();
					cellNameStr = cellNameStr + pojo.getCellName() + ",";
					pciStr  = pciStr + pojo.getPci().toString() + ",";
					lon = pojo.getLon();
					lat = pojo.getLat();
					if(createDate==null){
						createDate = pojo.getReportCreateDate();
					}else{
						if(pojo.getReportCreateDate()!=null && createDate<pojo.getReportCreateDate()){
							createDate = pojo.getReportCreateDate();
						}
					}
					//对pci进行标注索引，指定pci和localcellid的一一对应
					map.put("pciIndex", index.toString());
					map.put("cellName", pojo.getCellName());
					map.put("color", null);
					pciMap.put(pojo.getPci().toString(), map);
					index++;
				}
			}
			
			if(cellNameStr.indexOf(",")!=-1){
				cellNameStr = cellNameStr.substring(0,cellNameStr.lastIndexOf(","));
			}
			if(pciStr.indexOf(",")!=-1){
				pciStr = pciStr.substring(0,pciStr.lastIndexOf(","));
			}
			
			//1、通过cellname和测试业务员找到相关日志，筛选出测试日期距离生成日期最近的日志
			List<StationVerificationLogPojo> svlOne = findRecentLog(cellNameStr,tl.get(0).getTestService(),null,createDate);
			Long time = null;
			
			String fileName = "";
			for (StationVerificationLogPojo stationVerificationLogPojo : svlOne) {
				fileName = fileName + stationVerificationLogPojo.getFileName().replace(" ", "")+",";
				time = stationVerificationLogPojo.getTestTime();
			}
			//2.通过日志id去轨迹表中查询记录
			if(svlOne != null && svlOne.size()>0){
				String toCaseEvent = event.replace(" ", "").toLowerCase();
				Menu menu = terminalMenuService.get(city);
				//轨迹点显示是否采用抽样
				boolean trailSamDisplay = false;
				if (menu == null || menu.getStationParamPojo() == null) {
					trailSamDisplay = false;
				} else if (menu.getStationParamPojo() != null) {
					StationParamPojo spp = menu.getStationParamPojo();
					if(spp.getTrailSamDisplay() == null || spp.getTrailSamDisplay().equals("0")){
						trailSamDisplay = false;
					} else {
						trailSamDisplay = true;
					}
				}
				
				if(trailSamDisplay && (toCaseEvent.indexOf("rsrp")!=-1 || toCaseEvent.indexOf("sinr")!=-1 || toCaseEvent.indexOf("pci")!=-1)){
					//rsrp和sinr和pci的相关事件查找抽样表
					List<StationSAMTralPojo> setList = stationReportCreatService.findSamTralByReportId(fileName.substring(0, fileName.lastIndexOf(",")),pciStr,event);
					ActionContext.getContext().getValueStack().set("data", setList);
				}else{
					List<StationEtgTralPojo> setList = stationReportCreatService.findEtgTralByReportId(fileName.substring(0, fileName.lastIndexOf(",")),pciStr,event);
					ActionContext.getContext().getValueStack().set("data", setList);
				}
			}else{
				ActionContext.getContext().getValueStack().set("data", null);
			}
			ActionContext.getContext().getValueStack().set("time", time);
			ActionContext.getContext().getValueStack().set("pciMap", pciMap);
			ActionContext.getContext().getValueStack().set("lon", lon);
			ActionContext.getContext().getValueStack().set("lat", lat);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * ARGIS的打印工具保存的地图轨迹截图
	 * @author lucheng
	 * @date 2020年9月8日 下午3:18:12
	 * @return
	 */
	public String savePrintToolsImge(){
		if(url != null){
			InputStream in = null;
			try{
				PlanParamPojo find = stationReportCreatService.find(planParamPojo.getId());
				 
				String cellNameStr = "";
				if(currentOutType.equals("0")){
					//保存小区级的截图
					cellNameStr = cellNameStr + find.getCellName() + ",";
				}else{
					//保存基站级的截图
					List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(find.getSiteName());
					for (PlanParamPojo pojo : planParamPojoAscList) {
						cellNameStr = cellNameStr + pojo.getCellName() + ",";
					}
				}
				
				if(cellNameStr.indexOf(",")!=-1){
					cellNameStr = cellNameStr.substring(0,cellNameStr.lastIndexOf(","));
				}
				//2.筛选出测试日期距离生成日期最近的
				Long time = reportCreateTime;
			
				GetMethod postMethod = new GetMethod(url);
				  
				  
				HttpClient httpClient = new HttpClient();
				  
			    httpClient.getHttpConnectionManager().getParams()
							.setConnectionTimeout(60000);// 链接时间
			    httpClient.getHttpConnectionManager().getParams()
							.setSoTimeout(60000);// 读取时间
					
					
				httpClient.executeMethod(postMethod);
					
					
				in = postMethod.getResponseBodyAsStream();
				
				String imageName = "";
				String fileSavePath = "";
				if(currentOutType.equals("0")){
					imageName= String.valueOf(time) + "_" + find.getCellName() + "_" + event +"_DT" + ".jpg";
					fileSavePath= reportUrl+ "/cellEtgTrailImage/"+find.getCellName();
				}else{
					imageName= String.valueOf(time) + "_" + find.getSiteName() + "_" + event +"_DT" + ".jpg";
					fileSavePath= reportUrl+ "/cellEtgTrailImage/"+find.getSiteName();
				}
				
				File file = new File(fileSavePath);
				if (!file.exists()) {
					  file.mkdirs();
				}
				
				File fileImge = new File(file+"/"+imageName);
				if (fileImge.exists()) {
					fileImge.delete();
				}
				  
				FileOutputStream fos = new FileOutputStream(file+"/"+imageName);  
				// 将输入流is写入文件输出流fos中  
				int ch = 0;  
				try {  
			         while((ch=in.read()) != -1){  
			            fos.write(ch);  
			         }  
				} catch (IOException e1) {  
				     e1.printStackTrace();  
				} finally{  
			         //关闭输入流等（略）  
			         fos.close();  
			         in.close();  
				}  
				System.out.println(">>>>>>>>>>>>>>>>>>>>>尝试链接地图服务  [ 链接: "+url+"  返回结果: 成功 ]");
				//继续保存图例
				creatImge(file+"/"+imageName);
				//ActionContext.getContext().getValueStack().set("success", ">>>>>>>>>>>>>>>>>>>>>尝试链接地图服务  [ 链接: "+url+"  返回结果: 成功 ]");
			}catch (Exception e){
				 e.printStackTrace();
				 System.out.println(">>>>>>>>>>>>>>>>>>>>>尝试链接地图服务  [ 链接: "+url+"  返回结果:失败 ]");
			     ActionContext.getContext().getValueStack().set("errorMsg", "保存轨迹图片失败,发生异常"+e);
			}
		}else{
			ActionContext.getContext().getValueStack().set("errorMsg", "保存轨迹图片url为空，请联系管理员");
		}
		return ReturnType.JSON;
	}
	
	/**
	 * html2canvas保存图例，和图层的图片合成一张图片
	 * @author lucheng
	 * @date 2020年11月25日 上午10:05:40
	 * imageByte:图片转为base64格式的字符串，要求已去除"data:image/png;base64,"才能转换为图片
	 * @return
	 */
	public String creatImge(String mapImagePath){
		if(imageByte != null){
			//对字节数组字符串进行Base64解码并生成图片  
	        BASE64Decoder decoder = new BASE64Decoder();  
	        try   
	        {  
				String imageName = "";
				imageName= event.replace(" ", "") +"_legend" + ".jpg";
				String fileSavePath = "";
				fileSavePath= reportUrl+ "/cellEtgTrailImage/";
				
				File file = new File(fileSavePath);
				if (!file.exists()) {
					  file.mkdirs();
				}
				
				File fileImge = new File(file+"/"+imageName);
				if (fileImge.exists()) {
					fileImge.delete();
				}
				  
	        	
	            //Base64解码  
	            byte[] b = decoder.decodeBuffer(imageByte);  
	            for(int i=0;i<b.length;++i)  
	            {  
	                if(b[i]<0)  
	                {//调整异常数据  
	                    b[i]+=256;  
	                }  
	            }  
	            //生成jpeg图片  
	            OutputStream out = new FileOutputStream(file+"/"+imageName);      
	            out.write(b);  
	            out.flush();  
	            out.close(); 
	            System.out.println("保存图例成功,图例名称:"+imageName);
	            //合成图片
	            boolean rlt = ImageUtil.overlyingImageTest(mapImagePath, file+"/"+imageName, mapImagePath, 40, 50, 1f);
	            if(rlt){
	            	ActionContext.getContext().getValueStack().set("success", "合成图片成功,图片名称:"+imageName);
	            }else{
	            	ActionContext.getContext().getValueStack().set("errorMsg", "合成图片失败,图片名称:"+imageName);
	            }
	        }   
	        catch (Exception e)   
	        {  
	        	e.printStackTrace();
	        	ActionContext.getContext().getValueStack().set("errorMsg", "保存图例发生异常:"+e.getMessage());
	        	return ReturnType.JSON; 
	        }  
		}else{
			ActionContext.getContext().getValueStack().set("errorMsg", "保存图例字节为空，请联系管理员");
		}
		return ReturnType.JSON;
	}
	
	/**
	 * html2canvas保存截图
	 * @author lucheng
	 * @date 2020年11月25日 上午10:05:40
	 * imageByte:图片转为base64格式的字符串，要求已去除"data:image/png;base64,"才能转换为图片
	 * @return
	 */
	public String saveImge(){
		if(imageByte != null){
			//对字节数组字符串进行Base64解码并生成图片  
	        BASE64Decoder decoder = new BASE64Decoder();  
	        try   
	        {  
	        	PlanParamPojo find = stationReportCreatService.find(planParamPojo.getId());
				 
				String cellNameStr = "";
				if(currentOutType.equals("0")){
					//保存小区级的截图
					cellNameStr = cellNameStr + find.getCellName() + ",";
				}else{
					//保存基站级的截图
					List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(find.getSiteName());
					for (PlanParamPojo pojo : planParamPojoAscList) {
						cellNameStr = cellNameStr + pojo.getCellName() + ",";
					}
				}
				
				if(cellNameStr.indexOf(",")!=-1){
					cellNameStr = cellNameStr.substring(0,cellNameStr.lastIndexOf(","));
				}
				//2.筛选出测试日期距离生成日期最近的
				Long time = reportCreateTime;
			
				String imageName = "";
				String fileSavePath = "";
				if(currentOutType.equals("0")){
					imageName= String.valueOf(time) + "_" + find.getCellName() + "_" + event +"_DT" + ".jpg";
					fileSavePath= reportUrl+ "/cellEtgTrailImage/"+find.getCellName();
				}else{
					imageName= String.valueOf(time) + "_" + find.getSiteName() + "_" + event +"_DT" + ".jpg";
					fileSavePath= reportUrl+ "/cellEtgTrailImage/"+find.getSiteName();
				}
				
				File file = new File(fileSavePath);
				if (!file.exists()) {
					  file.mkdirs();
				}
				
				File fileImge = new File(file+"/"+imageName);
				if (fileImge.exists()) {
					fileImge.delete();
				}
				  
				//FileOutputStream fos = new FileOutputStream(file+"/"+imageName);  
	        	
	            //Base64解码  
	            byte[] b = decoder.decodeBuffer(imageByte);  
	            for(int i=0;i<b.length;++i)  
	            {  
	                if(b[i]<0)  
	                {//调整异常数据  
	                    b[i]+=256;  
	                }  
	            }  
	            //生成jpeg图片  
	            OutputStream out = new FileOutputStream(file+"/"+imageName);      
	            out.write(b);  
	            out.flush();  
	            out.close(); 
	            System.out.println("保存图片成功,图片名称:"+imageName);
	        }   
	        catch (Exception e){  
	        	e.printStackTrace();
	        	ActionContext.getContext().getValueStack().set("errorMsg", "保存图片发生异常:"+e.getMessage());
	        	return ReturnType.JSON; 
	        }  
		}else{
			ActionContext.getContext().getValueStack().set("errorMsg", "保存图片url为空，请联系管理员");
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 获取速率chart数据
	 * @author lucheng
	 * @date 2020年12月17日 下午2:06:23
	 * @return
	 */
	public String gainRatePointData(){
		PageList param = new PageList();
		param.putParam("type", "单站验证速率");
		param.putParam("nameCn", event);
		List<TestLogEtgTrailKpiPojo> tl = stationReportCreatService.findTrailKpiByParam(param);
		
		PlanParamPojo find = stationReportCreatService.find(planParamPojo.getId());
		Long createDate = find.getReportCreateDate();
		if(createDate==null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newDate = sdf.format(new Date()); 
			createDate = Long.valueOf(newDate);
		}
		
		//1、通过cellname和测试业务员找到相关日志，筛选出测试日期距离生成日期最近的日志
		List<StationVerificationLogPojo> svlList = findRecentLog(find.getCellName(),tl.get(0).getTestService(),wireStatus,createDate);
		StationVerificationLogPojo svlone = new StationVerificationLogPojo(); 
		Long uploadedSize = null;
		for (StationVerificationLogPojo pojo : svlList) {
			if(uploadedSize==null && pojo.getUploadedSize()!=null){
				uploadedSize = Long.valueOf(pojo.getUploadedSize());
				svlone = pojo;
			}else if(pojo.getUploadedSize() != null && Long.valueOf(pojo.getUploadedSize())>uploadedSize){
				uploadedSize = Long.valueOf(pojo.getUploadedSize());
				svlone = pojo;
			}
		}
		
		Long time = null;
		String fileName = null;
		//3.通过日志名称、事件名称去轨迹表中查询事件的最大值对应的记录
		StationEtgTralPojo etgTralPojo = new StationEtgTralPojo();
		if(svlone.getFileName() != null){
			time = svlone.getTestTime();
			fileName = svlone.getFileName();
			etgTralPojo = stationReportCreatService.findMaxTralByReportId(fileName,event);
		}
		
		//4.通过日志名称、事件名称以及最大值去轨迹表中查询最大值前后指定时间的数据的数据
		Long timeLength = -1L; // -1默认为全部时间
		Menu menu = terminalMenuService.get(find.getCity());
		if (menu == null || menu.getStationParamPojo() == null) {
			timeLength = -1L;
		} else if (menu.getStationParamPojo() != null) {
			StationParamPojo spp = menu.getStationParamPojo();
			StationReportTemplatePojo findTemplateParam = stationParamService.findTemplateParam(0, Long.valueOf(spp.getSingleStationMOdelSelect()));
			timeLength = findTemplateParam.getChartRateTime();
			if(timeLength==null){
				timeLength = -1L;
			}
		}
		List<StationEtgTralPojo> setList = null;
		if(etgTralPojo != null && StringUtils.hasText(etgTralPojo.getTimeLong())){
			setList = stationReportCreatService.findTralByMaxTime(fileName,event,etgTralPojo.getTimeLong(),timeLength);
		}
		List<List<Object>> list = new ArrayList<List<Object>>();
		//判断是pdcpUl还是pdcpDl
		if(setList!=null && setList.size()>0){
			if(setList.get(0).getNrPdcpThrDL()!=null){
				for (StationEtgTralPojo stationEtgTralPojo : setList) {
					List<Object> culist= new ArrayList<Object>(); 
					culist.add(Long.valueOf(stationEtgTralPojo.getTimeLong()));
					culist.add(Float.valueOf(stationEtgTralPojo.getNrPdcpThrDL()));
					list.add(culist);
				}
			}else{
				for (StationEtgTralPojo stationEtgTralPojo : setList) {
					List<Object> culist= new ArrayList<Object>(); 
					culist.add(Long.valueOf(stationEtgTralPojo.getTimeLong()));
					culist.add(Float.valueOf(stationEtgTralPojo.getNrPdcpThrUL()));
					list.add(culist);
				}
			}
		}
		ActionContext.getContext().getValueStack().set("value", list);
		ActionContext.getContext().getValueStack().set("cellName", find.getCellName());
		ActionContext.getContext().getValueStack().set("time", time);

		return ReturnType.JSON;
	}
	
	/**
	 * html2canvas保存pdcp速率截图
	 * 
	 * @author lucheng
	 * @date 2020年12月17日 上午10:05:40
	 *       imageByte:图片转为base64格式的字符串，要求已去除"data:image/png;base64,"才能转换为图片
	 * @return
	 */
	public String savePdcpRateImge() {
		if (imageByte != null) {
			// 对字节数组字符串进行Base64解码并生成图片
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				PageList param = new PageList();
				param.putParam("type", "单站验证速率");
				param.putParam("nameCn", event);
				List<TestLogEtgTrailKpiPojo> tl = stationReportCreatService.findTrailKpiByParam(param);

				PlanParamPojo find = stationReportCreatService.find(planParamPojo.getId());

				// 2.筛选出测试日期距离生成日期最近的
				Long time = reportCreateTime;

				String imageName = find.getCellName() + "_" + String.valueOf(time) + "_" + tl.get(0).getTestService()
						+ "_" + wireStatus.replace(",", "") + "_Chart" + ".jpg";
				String fileSavePath = reportUrl + "/cellEtgTrailImage/" + find.getCellName();

				File file = new File(fileSavePath);
				if (!file.exists()) {
					file.mkdirs();
				}

				File fileImge = new File(file + "/" + imageName);
				if (fileImge.exists()) {
					fileImge.delete();
				}

				// FileOutputStream fos = new
				// FileOutputStream(file+"/"+imageName);

				// Base64解码
				byte[] b = decoder.decodeBuffer(imageByte);
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {// 调整异常数据
						b[i] += 256;
					}
				}
				// 生成jpeg图片
				OutputStream out = new FileOutputStream(file + "/" + imageName);
				out.write(b);
				out.flush();
				out.close();
				System.out.println("保存速率图片成功,图片名称:" + imageName);
			} catch (Exception e) {
				e.printStackTrace();
				ActionContext.getContext().getValueStack().set("errorMsg", "保存速率图片发生异常:"+e.getMessage());
				return ReturnType.JSON;
			}
		} else {
			ActionContext.getContext().getValueStack().set("errorMsg", "保存速率图片字节为空，请联系管理员");
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 删除报告
	 * @author lucheng
	 * @date 2020年11月20日 下午6:45:03
	 * @return
	 */
	public String deleteReport() {
		try {
			String[] ids = idsStr.split(",");
			List<Long> list = new ArrayList<Long>();
			for (String id : ids) {
				list.add(Long.valueOf(id));
				StationReportExcelPojo stationReportExcelPojo = stationReportExcelService.find(Long.valueOf(id));
				if(stationReportExcelPojo.getReportPath() != null){
					File file = new File(stationReportExcelPojo.getReportPath());
					if(file != null && file.exists() ){
						file.delete();
					}
				}
			}
			stationReportExcelService.delete(list);
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "删除发生错误,"+e);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 下载测试日志
	 * 
	 * @return
	 */
	public String downloadLog() {
		return "downloadLog";
	}
	
	public InputStream getDownloadTestLog() {
		List<File> fileList = new ArrayList<File>();
		String[] ids = idsStr.split(",");
		for (String id : ids) {
			StationReportExcelPojo stationReportExcelPojo = stationReportExcelService.find(Long.valueOf(id));
			if(stationReportExcelPojo.getReportPath() != null){
				File file = new File(stationReportExcelPojo.getReportPath());
				fileList.add(file);
			}
		}
		
		File file1 = new File(reportUrl+ "/downloadStation/");
		if (!file1.exists()) {
			file1.mkdirs();
		}
		
		File zipFile = new File(reportUrl + "/downloadStation/" + "单验报告.zip");
		FileInputStream zipIn = null ;
		try {
			ActionContext.getContext().put("fileName",new String(zipFile.getName().getBytes(),"ISO8859-1"));
			ZipMultiFile.zipFiles(fileList, zipFile);
			zipIn = new FileInputStream(zipFile);
			zipFile.delete();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return zipIn;
		
	}
	
	public List<StationVerificationLogPojo> findRecentLog(String cellNameStr,String eventStr,String wireStatus,Long createDate){
		//1.通过cellName 到日志表中找到相关小区,测试业务为绕点 的日志数据
		List<StationVerificationLogPojo> svflList = stationVerificationTestService.findByCorrelativeCell(cellNameStr,eventStr,wireStatus);
		//2.筛选出测试日期距离生成日期最近的日志
		if(createDate==null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newDate = sdf.format(new Date());
			createDate = Long.valueOf(newDate);
		}
		Long time = null;
		List<StationVerificationLogPojo> svlOne = new ArrayList<StationVerificationLogPojo>();
		for (StationVerificationLogPojo svl : svflList) {
			if(time == null){
				time = svl.getTestTime();
				svlOne.add(svl);
			}else if(svl.getTestTime() != null && createDate != null 
					&& Math.abs(svl.getTestTime() - createDate) < Math.abs(createDate - time)){
				time = svl.getTestTime();
				svlOne.clear();
				svlOne.add(svl);
			}else if(svl.getTestTime() != null && createDate != null 
					&& Math.abs(svl.getTestTime() - createDate) == Math.abs(createDate - time)){
				time = svl.getTestTime();
				svlOne.add(svl);
			}
		}
		return svlOne;
	}


	/**
	 * 保存报告
	 * @return
	 */
	public String saveStationReport() {
		String[] ids = idsStr.split(",");
		List<Long> idList = new ArrayList<Long>();
		for (String id : ids) {
			idList.add(Long.valueOf(id));
		}
		
		List<PlanParamPojo> pppList = stationReportCreatService.findByIds(idList);
		
		if(pppList == null || pppList.size() == 0){
			ActionContext.getContext().getValueStack().set("errorMsg", "报告保存中找不到id对应的工参");
		}
		
		Map<String, Map<String, Object>> pppMap = new HashMap<String, Map<String,Object>>();
		try {
			for (PlanParamPojo ppp : pppList) {
				// 获取对应的所有的localcellid从而判断属于几小区
				List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(ppp.getSiteName());
				Integer localCellId = null;
				for(int i=0; i<planParamPojoAscList.size();i++){
					if(planParamPojoAscList.get(i).getLocalCellID() == ppp.getLocalCellID()){
						localCellId = i+1;
						break;
					}
				}
				
				Map<String, Object> map = pppMap.get(ppp.getSiteName());
				String recentDate = String.valueOf(ppp.getReportCreateDate()).substring(0, 4)
										+"/"+String.valueOf(ppp.getReportCreateDate()).substring(4, 6)
										+"/"+String.valueOf(ppp.getReportCreateDate()).substring(6, 8);
				if(map == null){
					map = new HashMap<String, Object>();
					map.put("reportCreateDate", recentDate);
				}else if(map.get("reportCreateDate") == null){
					map.put("reportCreateDate", recentDate);
				}else {
					String oldDate =  String.valueOf(map.get("reportCreateDate")).replace("/", "");
					if(ppp.getReportCreateDate() > Long.valueOf(oldDate)){
						map.put("reportCreateDate", recentDate);
					}
				}
				
				if(ppp.getStationCellParamCensusList() == null || ppp.getStationCellParamCensusList().size() < 1){
					map.put("cellId" + localCellId + "cell", ppp.getStationCellParamCensusList());
					map.put("cellIdCellParam", ppp.getStationCellParamCensusList());
					map.put("tac",null);
					map.put("tacStatus",null);
				}else{
					map.put("cellId" + localCellId + "cell", ppp.getStationCellParamCensusList().get(0));
					map.put("cellIdCellParam", ppp.getStationCellParamCensusList().get(0));
					map.put("tac",ppp.getStationCellParamCensusList().get(0).getTac19());
					map.put("tacStatus",ppp.getStationCellParamCensusList().get(0).getTacStatus29());
				}
				
				if(ppp.getStationNetoptReceiveTestList() == null || ppp.getStationNetoptReceiveTestList().size() < 1){
					map.put("cellId" + localCellId + "net", ppp.getStationNetoptReceiveTestList());
					map.put("cellnetop", ppp.getStationNetoptReceiveTestList());
					map.put("dtWireCoverRate",null);
					map.put("nrSwitchSuccessAvg",null);
					map.put("nrStationSwitchSum",null);
					map.put("stationNrFailSum",null);
				}else{
					map.put("cellId" + localCellId + "net", ppp.getStationNetoptReceiveTestList().get(0));
					map.put("cellnetop", ppp.getStationNetoptReceiveTestList().get(0));
					map.put("dtWireCoverRate",ppp.getStationNetoptReceiveTestList().get(0).getDtWireCoverRate());
					map.put("nrSwitchSuccessAvg",ppp.getStationNetoptReceiveTestList().get(0).getNrSwitchSuccessAvg());
					map.put("nrStationSwitchSum",ppp.getStationNetoptReceiveTestList().get(0).getNrStationSwitchSum());
					map.put("stationNrFailSum",ppp.getStationNetoptReceiveTestList().get(0).getStationDtNrFailSumDegree());
				}
				
				if(ppp.getStationPerformanceReceiveList() == null || ppp.getStationPerformanceReceiveList().size() < 1){
					map.put("cellId" + localCellId + "per", ppp.getStationPerformanceReceiveList());
					map.put("lteSwitchSuccessRation37",null);
					map.put("nrSwitchSuccessRation38",null);
					map.put("nrEarfcnSuccessRation42",null);
					map.put("lteEarfcnSuccessRation46",null); 
					map.put("nrSwitchSuccessAvg50",null);
					map.put("wireCoverRate54",null);
				}else{
					map.put("cellId" + localCellId + "per", ppp.getStationPerformanceReceiveList().get(0));
					map.put("lteSwitchSuccessRation37",ppp.getStationPerformanceReceiveList().get(0).getLteSwitchSuccessRation37());
					map.put("nrSwitchSuccessRation38",ppp.getStationPerformanceReceiveList().get(0).getNrSwitchSuccessRation38());
					map.put("nrEarfcnSuccessRation42",ppp.getStationPerformanceReceiveList().get(0).getNrEarfcnSuccessRation42());
					map.put("lteEarfcnSuccessRation46",ppp.getStationPerformanceReceiveList().get(0).getLteEarfcnSuccessRation46());
					map.put("nrSwitchSuccessAvg50",ppp.getStationPerformanceReceiveList().get(0).getNrSwitchSuccessAvg50());
					map.put("wireCover",ppp.getStationPerformanceReceiveList().get(0).getWireCoverRate54());
				}
				
				if(ppp.getStationBasicParamPojoList() == null || ppp.getStationBasicParamPojoList().size() < 1){
					map.put("cellId" + localCellId + "basic", ppp.getStationBasicParamPojoList());
					map.put("cellIdParam", ppp.getStationBasicParamPojoList());
				}else{
					map.put("cellId" + localCellId + "basic", ppp.getStationBasicParamPojoList().get(0));
					map.put("cellIdParam", ppp.getStationBasicParamPojoList().get(0));
				}
		
				//获取速率相关参数
				map = getFtpRateLogParam(map,ppp,localCellId);

				Map<String, Object> byteArryMap = null;
				String templateName = "";
				StationReportTemplatePojo findTemplateParam = stationParamService.findTemplateParam(0, Long.valueOf(ppp.getStationTemplateSelect()));
				templateName = findTemplateParam.getExportEcxelName();
				if(templateName == null){
					templateName = "山西NSA单验报告模板.xlsx";
				}
				byteArryMap = findAllImgeByte(localCellId, ppp);
				
				
//				if (ppp.getStationTemplateSelect()==1) { // 山西NSA模板 
//					templateName = "山西NSA单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//					
//				} else if (ppp.getStationTemplateSelect()==2) { // 兰州模板
//					templateName = "兰州单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//					
//				} else if (ppp.getStationTemplateSelect()==3) { // 贵阳模板
//					templateName = "贵阳单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//					
//				} else if (ppp.getStationTemplateSelect()==4) { // 云南室外模板
//					byteArryMap = new HashMap<String,Object>();
//					templateName = "云南室外单验报告模板.xlsx";
//					
//				} else if (ppp.getStationTemplateSelect()==5) { // 云南室分模板
//					byteArryMap = new HashMap<String,Object>();
//					templateName = "云南室分单验报告模板.xlsx";
//				} else if (ppp.getStationTemplateSelect()==6) { // 河南联通模板
//					byteArryMap = new HashMap<String,Object>();
//					templateName = "河南联通单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//					//获取速率相关参数
//					map = getFtpRateLogParam(map,ppp,localCellId);
//				} else if (ppp.getStationTemplateSelect()==7 || ppp.getStationTemplateSelect()==8 ) { // 贵州电信模板或贵州联通模板
//					templateName = "贵州电信单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				} else if (ppp.getStationTemplateSelect()==9 || ppp.getStationTemplateSelect()==10 ) { // 辽宁电信模板或辽宁联通模板
//					if(ppp.getStationTemplateSelect()==9){
//						templateName = "辽宁电信单站报告模板.xlsx";
//					}else{
//						templateName = "辽宁联通单站报告模板.xlsx";
//					}
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				} else if (ppp.getStationTemplateSelect()==11 || ppp.getStationTemplateSelect()==12 ) { // 内蒙电信模板或内蒙联通模板
//					if(ppp.getStationTemplateSelect()==11){
//						templateName = "内蒙电信单站报告模板.xlsx";
//					}else{
//						templateName = "内蒙联通单站报告模板.xlsx";
//					}
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				} else if (ppp.getStationTemplateSelect()==13) { // 陕西模板
//					templateName = "陕西单站报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//					
//				} else if (ppp.getStationTemplateSelect()==14) { // 湖北移动SA模板
//					templateName = "湖北移动SA 单站报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				} else if (ppp.getStationTemplateSelect()==15 || ppp.getStationTemplateSelect()==16) { // 湖北电信联通SA模板
//					if(ppp.getStationTemplateSelect()==15){
//						templateName = "湖北电信2.1G单验报告模板.xlsx";
//					}else{
//						templateName = "湖北联通2.1G单验报告模板.xlsx";
//					}
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				} else if (ppp.getStationTemplateSelect()==17) { // 山西SA单验报告模板
//					templateName = "山西SA单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				} else if (ppp.getStationTemplateSelect()==18) { // 宁夏单验报告模板
//					templateName = "宁夏NSA单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				} else if (ppp.getStationTemplateSelect()==19) { // 江西电信单验报告模板
//					templateName = "江西电信单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				} else if (ppp.getStationTemplateSelect()==20) { // 湖北700MSA单验报告模板
//					templateName = "湖北700MSA单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp); 
//					//获取速率相关参数
//					map = getFtpRateLogParam(map,ppp,localCellId);
//				} else { //默认是山西模板
//					templateName = "山西NSA单验报告模板.xlsx";
//					byteArryMap = findAllImgeByte(localCellId, ppp);
//				}
				map.put("templateName", templateName);
				for (Map.Entry<String, Object> byteArry : byteArryMap.entrySet()) {
					map.put(byteArry.getKey(), byteArry.getValue());
				}
				
				pppMap.put(ppp.getSiteName(), map);
			}
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newDate = sdf.format(new Date());
			
			
			for (Map.Entry<String, Map<String, Object>> siteNamePpp : pppMap.entrySet()) {
				
				String excelName = siteNamePpp.getKey() + "_"+ newDate + "_" + siteNamePpp.getValue().get("templateName");
				
				File filePackage = new File(reportUrl+ "/downloadStation/");
				if (!filePackage.exists()) {
					filePackage.mkdirs();
				}
				
				File file = new File(filePackage +"/"+ excelName);
				if(file!=null && file.exists()){
					file.delete();
				}
				
				OutputStream fileOutputStream = null;
				try {
					fileOutputStream = new FileOutputStream(file);
					Jxls2Utils.exportExcel("templates/"+siteNamePpp.getValue().get("templateName"), fileOutputStream, siteNamePpp.getValue());
					
				} catch (Exception e) {
					e.printStackTrace();
					throw e; 
				} finally{  
	                 //关闭输入流等（略）  
					fileOutputStream.close();  
			    } 
				
				//保存报告信息到数据库
				PageList pageList = new PageList();
				pageList.putParam("reportName", excelName);
				pageList.putParam("model", "NR");
				List<StationReportExcelPojo> pojolist = stationReportExcelService.findByParam(pageList);
				StationReportExcelPojo stationReportPojo = null;
				if(pojolist != null && pojolist.size()>0){
					stationReportPojo = pojolist.get(0);
				}else{
					stationReportPojo = new StationReportExcelPojo();
				}
				stationReportPojo.setSiteName(siteNamePpp.getKey());
				stationReportPojo.setRegion(pppList.get(0).getCity());
				stationReportPojo.setReportName(excelName);
				stationReportPojo.setReportPath(reportUrl+ "/downloadStation/" + excelName);
				stationReportPojo.setReportCreatDate(newDate);
				stationReportPojo.setModel("NR");
				stationReportExcelService.save(stationReportPojo);
			}

			return ReturnType.JSON;
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "报告保存过程发生错误,"+e);
		}
		return null;
	}
	
	/**
	 * 获取所有需要插入报告的图片字节
	 * @author lucheng
	 * @date 2020年10月15日 下午4:28:51
	 * @param localCellId
	 * @param ppp
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findAllImgeByte(Integer localCellId, PlanParamPojo ppp)  throws Exception{
		PageList param = new PageList();
		param.putParam("type", "单站验证轨迹");
		List<TestLogEtgTrailKpiPojo> trailKpiList = stationReportCreatService.findTrailKpiByParam(param);
		
		PageList param2 = new PageList();
		param2.putParam("type", "单站验证速率");
		List<TestLogEtgTrailKpiPojo> rateChartKpiList = stationReportCreatService.findTrailKpiByParam(param2);
		
		//生成图片路径
		String imgPath = fileSaveUrl + ppp.getSiteName() +"/";
		//小区级的图片路径
		String cellTrailPointImgPath =  reportUrl + "/cellEtgTrailImage/" + ppp.getCellName() +"/";
		//基站级的图片路径
		String siteTrailPointImgPath =  reportUrl + "/cellEtgTrailImage/" + ppp.getSiteName() +"/";
		//保存所有图片字节的map
		Map<String,Object> byteArryMap = new HashMap<String,Object>();
		
		Map<String, String> imgNameMap = new HashMap<String, String>();
		imgNameMap.put("建筑物全景图", "buildingFullScene");
		imgNameMap.put("站点入口图", "statioEntry");
		imgNameMap.put("屋顶天面全景图", "roofFullScene");
		imgNameMap.put(localCellId+"小区覆盖方向图1", "cell"+localCellId+"CoverDirect1");
		imgNameMap.put(localCellId+"小区覆盖方向图2", "cell"+localCellId+"CoverDirect2");
		imgNameMap.put(localCellId+"小区覆盖方向图3", "cell"+localCellId+"CoverDirect3");
		imgNameMap.put(localCellId+"小区天面图", "cell"+localCellId+"Surface");
		
		//获取勘站上传的图片
		byteArryMap = getFile(imgPath,imgNameMap);
		
		//获取轨迹点最近时间,以cellname为单位插入轨迹图片
		Map<String, String> trailImgNameMap = new HashMap<String, String>();
		trailImgNameMap = getTrailImageFileName(trailImgNameMap,trailKpiList,ppp,localCellId,"0");
		byteArryMap = getTrailFile(cellTrailPointImgPath,trailImgNameMap,byteArryMap);
		//以sitename为单位插入轨迹图片
		trailImgNameMap = new HashMap<String, String>();
		trailImgNameMap = getTrailImageFileName(trailImgNameMap,trailKpiList,ppp,localCellId,"1");
		byteArryMap = getTrailFile(siteTrailPointImgPath,trailImgNameMap,byteArryMap);
		
		//以小区为单位插入速率图片
		trailImgNameMap = new HashMap<String, String>();
		trailImgNameMap = getRateChartImageName(trailImgNameMap,rateChartKpiList,ppp,localCellId,"0");
		byteArryMap = getTrailFile(cellTrailPointImgPath,trailImgNameMap,byteArryMap);
		
		return byteArryMap;
	}


	/**
	 * 查询文件夹中包含指定中文字符并且距离当前时间最近的勘察上传图片
	 * @param filePath 文件夹路径
	 * @param imgNameMap key：图片名中文字符，value：传入excel的英文字符
	 */
	public Map<String,Object> getFile(String filePath, Map<String,String> imgNameMap) throws Exception{
		File filePackage = new File(filePath);
		Map<String,Object> byteArryMap = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String newDate = sdf.format(new Date());
		try {
			if (filePackage != null) {
				if (filePackage.isDirectory()) {
					File[] fileArray = filePackage.listFiles();
					if (fileArray != null && fileArray.length > 0) {
						// 包含字符
						for (Map.Entry<String, String> imgName : imgNameMap.entrySet()) {
							Long testTime = null; 
							File defile = null; 
							// 包含指定文字的图片判断
							for (File f : fileArray) {
								String fileName = f.getName();
								if (fileName.indexOf(imgName.getKey()) != -1 && f.isFile()) {
									if(fileName.indexOf("#") != -1 
											&& fileName.indexOf("#", fileName.indexOf("#")+1) != -1 
											&& fileName.lastIndexOf("#") > fileName.indexOf("#", fileName.indexOf("#")+1)){
										Long imgTime = Long.valueOf(fileName.substring(fileName.indexOf("#", fileName.indexOf("#")+1)+1, fileName.lastIndexOf("#")));
										if (testTime == null 
												|| Math.abs(testTime - Long.valueOf(newDate)) > Math.abs(imgTime - Long.valueOf(newDate))) {
											testTime = imgTime;
											defile = f;
										}
									}
								}
							}
							if(defile != null){
								// 文件流，输入图片
								InputStream imageInputStream = ClassUtil.getResourceAsStream(defile.getPath());
//						        InputStream imageInputStream = new FileInputStream(defile.getPath());
						        byte[] imageBytes = Util.toByteArray(imageInputStream);
						        byteArryMap.put(imgName.getValue(), imageBytes);
							}
						}
					}
				}
			}
			return byteArryMap;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			throw new Exception("查询图片过程中发生错误");
		} 
	}
	
	/**
	 * 保存轨迹图片
	 * @author lucheng
	 * @date 2020年9月9日 下午3:32:39
	 * @param filePath 轨迹图片保存路径
	 * @param imgNameMap 轨迹图片保存的名称和放入报告的对应名称
	 * @param exitByteArryMap 已经保存了部分图片的字节数组
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getTrailFile(String filePath, Map<String, String> imgNameMap, Map<String, Object> byteArryMap) throws Exception {
		File filePackage = new File(filePath);
		try {
			if (filePackage != null) {
				if (filePackage.isDirectory()) {
					// 包含字符
					for (Map.Entry<String, String> imgName : imgNameMap.entrySet()) {
						File defile = new File(filePath+imgName.getKey());
						//如果图片名称包含NR RSRP或LTE RSRP或NR SINR或LTE SINR,优先插入下载类型图片，没有的话再插入上传图片
						if(imgName.getKey().contains("RSRP上传")||imgName.getKey().contains("SINR上传")){
							if (defile != null && defile.exists()){
								// 文件流，输入图片
								InputStream imageInputStream = ClassUtil.getResourceAsStream(defile.getPath());
						        byte[] imageBytes = Util.toByteArray(imageInputStream);
						        //保存上传本身图片名称
						        byteArryMap.put(imgName.getValue(), imageBytes);
						        
						        String name = imgName.getKey().replace("上传", "下载");
								File downfile = new File(filePath+name);
								if (downfile == null || !downfile.exists()){
									byteArryMap.put(imgName.getValue().replace("UL", ""), imageBytes);
								} 
							}
							
						}else if(imgName.getKey().contains("RSRP下载")||imgName.getKey().contains("SINR下载")){
							if (defile != null && defile.exists()){
								// 文件流，输入图片
								InputStream imageInputStream = ClassUtil.getResourceAsStream(defile.getPath());
						        byte[] imageBytes = Util.toByteArray(imageInputStream);
						        //保存下载本身图片名称
						        byteArryMap.put(imgName.getValue(), imageBytes);
						        //优先保存下载图片
						        byteArryMap.put(imgName.getValue().replace("DL", ""), imageBytes);
					        }
						}else{
							if (defile != null && defile.exists()){
								// 文件流，输入图片
								InputStream imageInputStream = ClassUtil.getResourceAsStream(defile.getPath());
						        byte[] imageBytes = Util.toByteArray(imageInputStream);
						        byteArryMap.put(imgName.getValue(), imageBytes);
					        }
						}
					}
				}
			}
			return byteArryMap;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			throw new Exception("查询图片过程中发生错误");
		} 
	}

	
	/**
	 * 通过指标和小区名称筛选出测试日期距离生成日期最近的日志
	 * @author lucheng
	 * @date 2020年9月30日 上午10:32:33
	 * @param pojo
	 * @param eventStr 测试业务名称
	 * @param outImageType  0：小区类，1：基站类
	 * @return
	 */
	public Long getEtgTrailTime(PlanParamPojo pojo,String eventStr ,String wireStatus, String outImageType)  throws Exception{
		 String cellNameStr ="";
		 if(outImageType.equals("0")){
			 cellNameStr = pojo.getCellName();
		 }else{
			 List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(pojo.getSiteName());
			 for (int i=0;i<planParamPojoAscList.size();i++) {
				 if(i!=0){
					 cellNameStr = cellNameStr + ",";
				 }
				 cellNameStr = cellNameStr + planParamPojoAscList.get(i).getCellName();
			 }
		 }
		 
		 //通过cellname和测试业务员找到相关日志，筛选出测试日期距离生成日期最近的日志
		 List<StationVerificationLogPojo> svlOne = findRecentLog(cellNameStr,eventStr,wireStatus,pojo.getReportCreateDate());
		 Long time = null;
		 
		 if(svlOne!=null && svlOne.size()>0){
			 time = svlOne.get(0).getTestTime();
		 }
		 return time;
	}
	
	/**
	 * 查询所有轨迹图片名称
	 * @author lucheng
	 * @date 2020年9月30日 上午10:30:58
	 * @param trailImgNameMap 已经保存的图片名称
	 * @param trailKpiList 轨迹的所有指标
	 * @param ppp 工参
	 * @param localCellId 
	 * @param type 0：小区类，1：基站类
	 * @return
	 */
	public Map<String, String> getTrailImageFileName(Map<String,String> trailImgNameMap , List<TestLogEtgTrailKpiPojo> trailKpiList,PlanParamPojo ppp, Integer localCellId,String type) throws Exception{
		Map<String,Long> eventMap = new HashMap<String, Long>();
		for(int i=0; i< trailKpiList.size();i++){
			 String eventStr = trailKpiList.get(i).getTestService();
			 if(eventMap.get(eventStr) == null){
				 Long etgTrailTime = getEtgTrailTime( ppp, eventStr,null,type);
				 eventMap.put(eventStr, etgTrailTime);
			 }
			 Long time = eventMap.get(eventStr);
			 if(time != null){
				 if(type.equals("0")){
					 //以cellname为单位插入图片
					 String imageName= String.valueOf(time) + "_" + ppp.getCellName() + "_" + trailKpiList.get(i).getNameCn() +"_DT" + ".jpg";
					 trailImgNameMap.put(imageName, "cell"+localCellId+ trailKpiList.get(i).getClassify());
				 }else{
					//以sitename为单位插入图片
					 String imageName= String.valueOf(time) + "_" + ppp.getSiteName() + "_" + trailKpiList.get(i).getNameCn() +"_DT" + ".jpg";
					 trailImgNameMap.put(imageName, "cell"+ trailKpiList.get(i).getClassify());
				 }
			 }
		}
		return trailImgNameMap;
	}
	
	/**
	 * 查询所有速率图片名称
	 * @author lucheng
	 * @date 2020年12月18日 下午3:18:25
	 * @param trailImgNameMap
	 * @param trailKpiList
	 * @param ppp
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getRateChartImageName(Map<String, String> trailImgNameMap,
			List<TestLogEtgTrailKpiPojo> trailKpiList, PlanParamPojo ppp, Integer localCellId, String type)
			throws Exception {
		Map<String, Long> eventMap = new HashMap<String, Long>();
		for (int i = 0; i < trailKpiList.size(); i++) {
			String eventStr = trailKpiList.get(i).getTestService();
			for (String wireStatus : wireStatusArray) {
				if (eventMap.get(eventStr) == null) {
					Long etgTrailTime = getEtgTrailTime(ppp, eventStr, wireStatus, "0");
					eventMap.put(eventStr, etgTrailTime);
				}
				Long time = eventMap.get(eventStr);
				if (time != null) {
					// 以cellname为单位插入图片
					String imageName = ppp.getCellName() + "_" + String.valueOf(time) + "_" + eventStr + "_"
							+ wireStatus.replace(",", "") + "_Chart" + ".jpg";
					if(wireStatus.equals(wireStatusArray[0])){
						trailImgNameMap.put(imageName, "cell" + localCellId + trailKpiList.get(i).getClassify());
					}else if(wireStatus.equals(wireStatusArray[1])){
						trailImgNameMap.put(imageName, "cell" + localCellId + trailKpiList.get(i).getClassify()+"Mid");
					}else if(wireStatus.equals(wireStatusArray[2])){
						trailImgNameMap.put(imageName, "cell" + localCellId + trailKpiList.get(i).getClassify()+"Bad");
					}
				}
			}
		}
		return trailImgNameMap;
	}
	
	public Map<String, Object> getFtpRateLogParam(Map<String, Object> map,PlanParamPojo ppp,Integer localCellId){
		//保存FTP速率业务相关的参数
		PageList param = new PageList();
		param.putParam("type", "单站验证速率");
		//找到速率指标名称
		List<TestLogEtgTrailKpiPojo> rateChartKpiList = stationReportCreatService.findTrailKpiByParam(param);
		for (TestLogEtgTrailKpiPojo testLogEtgTrailKpiPojo : rateChartKpiList) {
			for (String wireStatus : wireStatusArray) {
				//找到相关日志 
				List<StationVerificationLogPojo> findRecentLog = findRecentLog(ppp.getCellName(),testLogEtgTrailKpiPojo.getTestService(),wireStatus,ppp.getReportCreateDate());
				if(findRecentLog != null && findRecentLog.size()>0){
					List<String> logNameList = new ArrayList<String>();
					String fileName = "";
					for (StationVerificationLogPojo stationVerificationLogPojo : findRecentLog) {
						logNameList.add(stationVerificationLogPojo.getFileName());
						fileName = fileName + stationVerificationLogPojo.getFileName().replace(" ", "")+",";
					}
					//由日志名称得到详表指标
					String wireNameStr = wireStatus.replace("J", "极好点").replace("H", "好点").replace("Z", "中点").replace("C", "差点");
					List<EceptionCellLogPojo> cellLogList = stationReportCreatService.getExceptionCellLogOfCellLog(ppp.getCellName(), logNameList,testLogEtgTrailKpiPojo.getTestService(),wireNameStr);
					//保存参数
					if(cellLogList!=null && cellLogList.size()>0){
						if(testLogEtgTrailKpiPojo.getTestService().contains("上传")){
							map.put("cellId" + localCellId + "FtpRateUL"+wireStatus.replace(",", ""), cellLogList.get(0));
						}else if(testLogEtgTrailKpiPojo.getTestService().contains("下载")){
							map.put("cellId" + localCellId + "FtpRateDL"+wireStatus.replace(",", ""), cellLogList.get(0));
						}
						
					} 
					
					//通过日志名称、事件名称去轨迹表中查询记录的测试时长
					//求最大时间
					StationEtgTralPojo etgTralPojoMax = stationReportCreatService.findMaxTralTimeByReportName(fileName.substring(0, fileName.lastIndexOf(",")),"0");
					//求最小时间
					StationEtgTralPojo etgTralPojoMin = stationReportCreatService.findMaxTralTimeByReportName(fileName.substring(0, fileName.lastIndexOf(",")),"1");
					if(etgTralPojoMax!=null && etgTralPojoMin!=null && etgTralPojoMax.getTimeLong()!=null && etgTralPojoMin.getTimeLong()!=null){
						//得到测试时长 单位：秒
						Long time = (long) Math.round((Long.valueOf(etgTralPojoMax.getTimeLong()) - Long.valueOf(etgTralPojoMin.getTimeLong()))/1000F);
						if(testLogEtgTrailKpiPojo.getTestService().contains("上传")){
							map.put("time" + localCellId + "FtpRateUL", time.toString()+"s");
						}else if(testLogEtgTrailKpiPojo.getTestService().contains("下载")){
							map.put("time" + localCellId + "FtpRateDL", time.toString()+"s");
						}
					}
				}

			}
			
		}
		return map;
	}
	
	static int flag = 1;//用来判断文件是否删除成功
	
	public static void deleteFile(File file) {
		// 判断文件不为null或文件目录存在
		if (file == null || !file.exists()) {
			flag = 0;
			System.out.println("文件删除失败,请检查文件路径是否正确");
			return;
		}
		// 取得这个目录下的所有子文件对象
		File[] files = file.listFiles();
		// 遍历该目录下的文件对象
		for (File f : files) {
			f.delete();
		}
	}
	


	/**
	 * @return the reportCreateTime
	 */
	public Long getReportCreateTime() {
		return reportCreateTime;
	}


	/**
	 * @param reportCreateTime the reportCreateTime to set
	 */
	public void setReportCreateTime(Long reportCreateTime) {
		this.reportCreateTime = reportCreateTime;
	}


	/**
	 * @return the cityIds
	 */
	public String getCityIds() {
		return cityIds;
	}


	/**
	 * @param cityIds the cityIds to set
	 */
	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}


	/**
	 * @return the testFinishCondition
	 */
	public String getTestFinishCondition() {
		return testFinishCondition;
	}

	/**
	 * @param testFinishCondition the testFinishCondition to set
	 */
	public void setTestFinishCondition(String testFinishCondition) {
		this.testFinishCondition = testFinishCondition;
	}

	/**
	 * @return the wirelseeParamConsistency
	 */
	public String getWirelseeParamConsistency() {
		return wirelseeParamConsistency;
	}

	/**
	 * @param wirelseeParamConsistency the wirelseeParamConsistency to set
	 */
	public void setWirelseeParamConsistency(String wirelseeParamConsistency) {
		this.wirelseeParamConsistency = wirelseeParamConsistency;
	}

	public String getIdsStr() {
		return idsStr;
	}

	public void setIdsStr(String idsStr) {
		this.idsStr = idsStr;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}


	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * @return the startTime
	 */
	public Long getStartTime() {
		return startTime;
	}


	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}


	/**
	 * @return the endTime
	 */
	public Long getEndTime() {
		return endTime;
	}


	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}


	/**
	 * @return the testStatus
	 */
	public Integer getTestStatus() {
		return testStatus;
	}


	/**
	 * @param testStatus the testStatus to set
	 */
	public void setTestStatus(Integer testStatus) {
		this.testStatus = testStatus;
	}


	/**
	 * @return the testService
	 */
	public Integer getTestService() {
		return testService;
	}


	/**
	 * @param testService the testService to set
	 */
	public void setTestService(Integer testService) {
		this.testService = testService;
	}


	/**
	 * @return the cityNamesStr
	 */
	public String getCityNamesStr() {
		return cityNamesStr;
	}


	/**
	 * @param cityNamesStr the cityNamesStr to set
	 */
	public void setCityNamesStr(String cityNamesStr) {
		this.cityNamesStr = cityNamesStr;
	}


	/**
	 * @return the currentOutType
	 */
	public String getCurrentOutType() {
		return currentOutType;
	}


	/**
	 * @param currentOutType the currentOutType to set
	 */
	public void setCurrentOutType(String currentOutType) {
		this.currentOutType = currentOutType;
	}

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getImageByte() {
		return imageByte;
	}


	public void setImageByte(String imageByte) {
		this.imageByte = imageByte;
	}


	public String getWireStatus() {
		return wireStatus;
	}


	public void setWireStatus(String wireStatus) {
		this.wireStatus = wireStatus;
	}
	
	


}
