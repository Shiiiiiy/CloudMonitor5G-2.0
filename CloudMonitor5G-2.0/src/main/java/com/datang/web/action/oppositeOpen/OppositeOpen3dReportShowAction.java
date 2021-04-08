package com.datang.web.action.oppositeOpen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
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
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.stationParam.StationReportTemplatePojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.service.oppositeOpen.OppositeOpen3dReportCreateService;
import com.datang.service.platform.stationParam.StationParamService;
import com.datang.service.stationTest.StationReportCreatService;
import com.datang.service.testLogItem.StationVerificationTestService;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 单站报告查看
 * @author maxuancheng
 *
 */
@Controller
@Scope("prototype")
public class OppositeOpen3dReportShowAction extends PageAction implements
	ModelDriven<Plan4GParam>{
	
	private static final long serialVersionUID = 1284972158355134539L;
	
	private Plan4GParam planParamPojo = new Plan4GParam();
	
	@Autowired
	private StationReportCreatService stationReportCreatService;
	
	@Autowired
	private StationVerificationTestService stationVerificationTestService;
	
	@Autowired
	private OppositeOpen3dReportCreateService oppositeOpen3dReportCreateService;
	
	
	@Autowired
	private StationParamService stationParamService;
	
	@Value("${stationReportFileLink}")
	private String reportUrl;
	
	@Value("${stationZipUrl}")
	private String stationZipUrl;
	
	@Value("${fileSaveUrl}")
	private String fileSaveUrl;
	
	private Long reportCreateTime;
	
	private String cityIds;
	
	private String testFinishCondition;
	
	private String wirelseeParamConsistency;
	
	private String idsStr;
	
	private Long startTime;//测试开始时间
	
	private Long endTime;//测试结束时间
	
//	private Integer testStatus;//测试状态
//	
//	private Integer testService;//测试业务
	
	private String cityNamesStr;
	/**
	 * 指标
	 */
	private String event;
	
	public String goToOppositeOpen3dReportShowJsp(){
		return ReturnType.LISTUI;
	}
	

	@Override
	public Plan4GParam getModel() {
		return planParamPojo;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		pageList.putParam("cityStr", cityNamesStr);
		pageList.putParam("startTime", startTime);
		pageList.putParam("endTime", endTime);
		pageList.putParam("planParamPojo", planParamPojo);
		return oppositeOpen3dReportCreateService.doPageQuery(pageList);
	}
	
	public String getAllCell(){
		List<Plan4GParam> pList = oppositeOpen3dReportCreateService.findAll();
		List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
		for (Plan4GParam planParamPojo : pList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", planParamPojo.getId());
			map.put("text", planParamPojo.getCellName());
			arrayList.add(map);
		}
		ActionContext.getContext().getValueStack().push(arrayList);
		return ReturnType.JSON;
	}
	
	/**
	 * 获取轨迹点数据
	 * @author maxuancheng
	 * date:2020年2月25日 下午4:24:52
	 * @return
	 */
	public String getPoint(){
		Plan4GParam find = oppositeOpen3dReportCreateService.find(planParamPojo.getId());
		//1.通过cellName 到日志表中找到相关小区,测试业务为绕点 的日志数据
		event = "DT";
		List<StationVerificationLogPojo> svflList = stationVerificationTestService.findByCorrelativeCell(planParamPojo.getCellName(),event,null);
		//2.筛选出测试日期距离生成日期最近的日志
		Long time = null;
		List<StationVerificationLogPojo> svlOne = new ArrayList<StationVerificationLogPojo>();
		for (StationVerificationLogPojo svl : svflList) {
			if(time == null){
				time = svl.getTestTime();
				svlOne.add(svl);
			}else if(svl.getTestTime() != null && find.getCreateReportDate() != null 
					&& Math.abs(svl.getTestTime() - find.getCreateReportDate()) < Math.abs(find.getCreateReportDate() - time)){
				time = svl.getTestTime();
				svlOne.clear();
				svlOne.add(svl);
			}else if(svl.getTestTime() != null && find.getCreateReportDate() != null 
					&& Math.abs(svl.getTestTime() - find.getCreateReportDate()) == Math.abs(find.getCreateReportDate() - time)){
				time = svl.getTestTime();
				svlOne.add(svl);
			}
		}
		String fileName = "";
		for (StationVerificationLogPojo stationVerificationLogPojo : svlOne) {
			fileName = fileName + stationVerificationLogPojo.getFileName().replace(" ", "")+",";
		}
		//3.通过日志id去轨迹表中查询记录
		List<StationEtgTralPojo> setList = null;
		if(svlOne != null && svlOne.size()>0){
			setList = stationReportCreatService.findTralById(fileName,find.getPci().toString());
		}
		ActionContext.getContext().getValueStack().set("data", setList);
		
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

	/**
	 * 下载测试日志
	 * 
	 * @return
	 */
	public InputStream getDownloadTestLog() {
		try {
			String[] ids = idsStr.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String id : ids) {
				idList.add(Long.valueOf(id));
			}
			
			List<Plan4GParam> pppList = oppositeOpen3dReportCreateService.findByIds(idList);
			Map<String, Map<String, Object>> pppMap = new HashMap<String, Map<String,Object>>();
			for (Plan4GParam ppp : pppList) {
				List<Plan4GParam> plan4GParamAscList = oppositeOpen3dReportCreateService.getAllLocalCellId(ppp.getEnbId());
				Integer localCellId = null;
				for(int i=0; i<plan4GParamAscList.size();i++){
					if(plan4GParamAscList.get(i).getLocalCellId() == ppp.getLocalCellId()){
						localCellId = i+1;
						break;
					}
				}
				if(localCellId != null){
					Map<String, Object> map = pppMap.get(ppp.getSiteName());
					String recentDate = String.valueOf(ppp.getCreateReportDate()).substring(0, 4)
							+"/"+String.valueOf(ppp.getCreateReportDate()).substring(4, 6)
							+"/"+String.valueOf(ppp.getCreateReportDate()).substring(6, 8);
					if(map == null){
						map = new HashMap<String, Object>();
						map.put("longitude",ppp.getLongitude());
						map.put("latitude",ppp.getLatitude());
						map.put("enbId",ppp.getOppositeOpen3dWirelessPojo().getEnbId());
						map.put("tac",ppp.getOppositeOpen3dWirelessPojo().getTac());
						map.put("enbIdReality",ppp.getOppositeOpen3dWirelessPojo().getEnbIdReality());
						map.put("enbIdContrast",ppp.getOppositeOpen3dWirelessPojo().getEnbIdContrastStr());
						map.put("tacContrast",ppp.getOppositeOpen3dWirelessPojo().getTacContrastStr());
						map.put("reportCreateDate", recentDate);
					}else if(map.get("reportCreateDate") == null){
						map.put("reportCreateDate", recentDate);
					}else {
						String oldDate =  String.valueOf(map.get("reportCreateDate")).replace("/", "");
						if(ppp.getCreateReportDate() > Long.valueOf(oldDate)){
							map.put("reportCreateDate", recentDate);
						}
					}
					
	//				map.put("stationId" + localCellId + "Info",ppp);
					map.put("cellId" + localCellId + "result", ppp.getOppositeOpen3dResultPojo());
					map.put("cellId" + localCellId + "wire", ppp.getOppositeOpen3dWirelessPojo());
					map.put("cellId" + localCellId + "per", ppp.getOppositeOpen3dPerformanceReceivePojo());
					
					if(ppp.getStationBasicParamPojoList() == null || ppp.getStationBasicParamPojoList().size() < 1){
						map.put("cellId" + localCellId + "basic", ppp.getStationBasicParamPojoList());
						map.put("cellIdParam", ppp.getStationBasicParamPojoList());
					}else{
						map.put("cellId" + localCellId + "basic", ppp.getStationBasicParamPojoList().get(0));
						map.put("cellIdParam", ppp.getStationBasicParamPojoList().get(0));
					}
					
					String imgPath = fileSaveUrl + ppp.getSiteName() +"/";
					Map<String,String> imgNameMap = null;
					Map<String, Object> byteArryMap = null;
					StationReportTemplatePojo findTemplateParam = stationParamService.findTemplateParam(1, Long.valueOf(ppp.getOpposOpenTemplateSelect()));
					String templateName = findTemplateParam.getExportEcxelName();
					if(templateName == null){
						templateName = "山西反开3d报告模板.xlsx";
					}
					imgNameMap = new HashMap<String, String>();
					imgNameMap.put("建筑物全景图", "buildingFullScene");
					imgNameMap.put("屋顶天面全景图", "roofFullScene");
					imgNameMap.put(localCellId+"小区天面图", "cell"+localCellId+"Surface");
					imgNameMap.put(localCellId+"小区覆盖方向图", "cell"+localCellId+"CoverDirect");
					byteArryMap = getFile(imgPath,imgNameMap);
					
//					if (ppp.getOpposOpenTemplateSelect()==1) { // 山西模板
//						templateName = "山西反开3d报告模板.xlsx";
//						imgNameMap = new HashMap<String, String>();
//						imgNameMap.put("建筑物全景图", "buildingFullScene");
//						imgNameMap.put("屋顶天面全景图", "roofFullScene");
//						imgNameMap.put(localCellId+"小区天面图", "cell"+localCellId+"Surface");
//						imgNameMap.put(localCellId+"小区覆盖方向图", "cell"+localCellId+"CoverDirect");
//						byteArryMap = getFile(imgPath,imgNameMap);
//						
//					} else if (ppp.getOpposOpenTemplateSelect()==2) { // 兰州模板
//						templateName = "兰州反开3d报告模板.xlsx";
//						imgNameMap = new HashMap<String, String>();
//						imgNameMap.put(localCellId+"小区天面图", "cell"+localCellId+"Surface");
//						imgNameMap.put(localCellId+"小区覆盖方向图", "cell"+localCellId+"CoverDirect");
//						byteArryMap = getFile(imgPath,imgNameMap);
//						
//					} else { //默认是山西模板
//						templateName = "山西反开3d报告模板.xlsx";
//						imgNameMap = new HashMap<String, String>();
//						imgNameMap.put("建筑物全景图", "buildingFullScene");
//						imgNameMap.put("屋顶天面全景图", "roofFullScene");
//						imgNameMap.put(localCellId+"小区天面图", "cell"+localCellId+"Surface");
//						imgNameMap.put(localCellId+"小区覆盖方向图", "cell"+localCellId+"CoverDirect");
//						byteArryMap = getFile(imgPath,imgNameMap);
//					}
					map.put("templateName", templateName);
					for (Map.Entry<String, Object> byteArry : byteArryMap.entrySet()) {
						map.put(byteArry.getKey(), byteArry.getValue());
					}
					
					pppMap.put(ppp.getSiteName(), map);
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newDate = sdf.format(new Date());
			File file1 = new File(reportUrl+ "/downloadOppositeOpen3d/");
			if (!file1.exists()) {
				file1.mkdirs();
			}
			
			deleteFile(file1);
			File zipFile = new File(reportUrl + "/downloadOppositeOpen3d/" + newDate + "反开3d报告.zip");
	        
			List<File> fileList = new ArrayList<File>();
			
			for (Map.Entry<String, Map<String, Object>> siteNamePpp : pppMap.entrySet()) {
				File file = new File(reportUrl + "/downloadOppositeOpen3d/" + siteNamePpp.getKey()+"反开3d报告表.xls");
				try {
					// 导出excel2003
//					transformToExcel = POIExcelUtil.transformToExcel(siteNamePpp.getValue(), "templates/"+siteNamePpp.getValue().get("templateName"));
//					FileOutputStream fileOutputStream = new FileOutputStream(file);
//					if (null != transformToExcel) {
//						transformToExcel.write(fileOutputStream);
//						fileList.add(file);
//					}
					OutputStream fileOutputStream = new FileOutputStream(file);
					Jxls2Utils.exportExcel("templates/"+siteNamePpp.getValue().get("templateName"), fileOutputStream, siteNamePpp.getValue());
					fileList.add(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ActionContext.getContext().put("fileName",new String(zipFile.getName().getBytes(),"ISO8859-1"));
			ZipMultiFile.zipFiles(fileList, zipFile);
			FileInputStream zipIn = new FileInputStream(zipFile);
			zipFile.delete();
			return zipIn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询文件夹中包含指定中文字符并且距离当前时间最近的图片
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
			throw new Exception("查询图片过程中发生错误");
		} 
	}
	
	
	public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
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


	public Long getStartTime() {
		return startTime;
	}


	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}


	public Long getEndTime() {
		return endTime;
	}


	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}


	public String getCityNamesStr() {
		return cityNamesStr;
	}


	public void setCityNamesStr(String cityNamesStr) {
		this.cityNamesStr = cityNamesStr;
	}
}
