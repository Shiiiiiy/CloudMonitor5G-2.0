package com.datang.web.action.testLogItem;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.*;
import com.datang.dao.customTemplate.CustomLogReportTaskDao;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testLogItem.UnicomLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.influx.InfluxService;
import com.datang.service.influx.InitialConfig;
import com.datang.service.influx.bean.AbEventConfig;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.testLogItem.UnicomLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.service.testPlan.TestPlanService;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.testLogItem.QuestionEventTitleBean;
import com.datang.web.beans.testLogItem.UnicomLogItemPageQueryRequestBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.ant.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class UnicomLogItemAction extends PageAction implements
	ModelDriven<UnicomLogItemPageQueryRequestBean> {
	private static Logger LOGGER = LoggerFactory
			.getLogger(UnicomLogItemAction.class);
	@Value("${stationReportFileLink}")
	private String reportUrl;

	@Value("${dzTestLogTtemUrl}")
	private String dzTestLogTtemUrl;

	@Autowired
	private InfluxService influxService;
	/**
	 * 测试日志服务
	 */
	@Autowired
	private UnicomLogItemService unicomLogItemService;
	/**
	 * 菜单管理服务
	 */
	@Autowired
	private IMenuManageService menuManageService;
	/**
	 * 区域管理服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * 用户管理服务
	 */
	@Autowired
	private IUserService userService;


	@Autowired
	private CustomLogReportTaskDao customLogReportTaskDao;


	/**
	 * 测试计划服务
	 */
	@Autowired
	private TestPlanService testPlanService;

	/**
	 * Description:终端服务
	 * @author lucheng
	 * @date 下午6:52:39
	 */
	@Autowired
	private TerminalService terminalService;

	/**
	 * 测试日志筛选参数
	 */
	private UnicomLogItemPageQueryRequestBean unicomLogItemPageQueryRequestBean = new UnicomLogItemPageQueryRequestBean();
	/**
	 * 测试日志id数组按','分隔
	 */
	private String testLogItemIds;
	/**
	 * 测试日志id
	 */
	private Long recSeqNo;

	/**
	 * 跳转到testlogitem list界面
	 *
	 * @return
	 */
	public String unicomLogItemListUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到未完成testlogitem list界面
	 *
	 * @return
	 */
	public String unfinishedTestLogItemListUI() {
		return "unfinished" + ReturnType.LISTUI;
	}







	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.datang.common.action.page.PageAction#doPageQuery(com.datang.common
	 * .action.page.PageList)
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {


		List<Long> cityIdsList = unicomLogItemPageQueryRequestBean
		.getCityIdsList();
		Set<String> regionname = new HashSet<>();
		for (Long refid: cityIdsList){
			List<TerminalMenu> menus = menuManageService.getCities(refid);
			if (menus != null) {
				for (TerminalMenu menu : menus) {
					if (menu.getType().equals(MenuType.City.name())) {
						// 键menu全部转化为组
						TerminalGroup group = terminalGroupService
								.findGroupById(menu.getRefId());
						if (group != null) {
							regionname.add(group.getName());
						}
					}
				}
			}
		}
		PageList pageList1 = new PageList();

//		List<CustomLogReportTask> statisticeTask = customLogReportTaskDao.findStatisticeTask(pageList1);
//		Set<Long> idSet = new HashSet<>();
//		for(CustomLogReportTask c:statisticeTask){
//			if(org.apache.commons.lang3.StringUtils.isNotBlank(c.getLogIds())){
//				String[] split = c.getLogIds().split(",");
//				ArrayList<String> idList = new ArrayList<>(Arrays.asList(split));
//				for(String sss : idList){
//					try{
//						idSet.add(Long.valueOf(sss));
//					}catch (Exception e){
//
//					}
//
//				}
//			}
//		}
		// unicomLogItemPageQueryRequestBean.setIds(idSet);
		unicomLogItemPageQueryRequestBean.setCityName(regionname);

		pageList.putParam("pageQueryBean", unicomLogItemPageQueryRequestBean);
		AbstractPageList pageRlt = unicomLogItemService.pageList(pageList);
		List<UnicomLogItem>  testLogList= pageRlt.getRows();

		// 查询KPI
		List<Map<String, Object>> kpiList = unicomLogItemService.doPageQueryKpi();

		Map<String,String> md5Map = new HashMap<>();
		Map<String,Map<String,Object>> kpiMap = new HashMap<>();


		for(Map<String, Object> m:kpiList){
			if(m.get("TASK_NAME")!=null && m.get("LOG_NAME")!=null){
				kpiMap.put((String)m.get("TASK_NAME" )  + (String)m.get("LOG_NAME" ),m);
			}
		}


		for (UnicomLogItem testLogItem : testLogList) {
			try{
				long td = testLogItem.getEndDate().getTime() - testLogItem.getStartDate().getTime();
				testLogItem.setTimeDiff(DateUtils.format(td,"HH:mm:ss"));
			}catch (Exception e){

			}

			// KPI
			Map<String, Object> colMap = kpiMap.get(testLogItem.getTaskName() + testLogItem.getFileName());
			testLogItem.setKpi1(getMap(colMap,"KPI1"));
			testLogItem.setKpi2(getMap(colMap,"KPI2"));
			testLogItem.setKpi3(getMap(colMap,"KPI3"));
			testLogItem.setKpi4(getMap(colMap,"KPI4"));
			testLogItem.setKpi5(getMap(colMap,"KPI5"));
			testLogItem.setKpi6(getMap(colMap,"KPI6"));
			testLogItem.setKpi7(getMap(colMap,"KPI7"));
			testLogItem.setKpi8(getMap(colMap,"KPI8"));

 		}
		return pageRlt;
	}



	public InputStream getDownloadQuestion(){

		List<Map<String, Object>> questionview = questionview();
		Map<String, Collection> hashMap1 = new HashMap<>();

		hashMap1.put("sqlObj1", questionview);
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(
					hashMap1, "templates/问题索引-在线分析.xlsx");

			ActionContext
					.getContext()
					.put("fileName",
							new String(
									("问题索引-在线分析-" + System.currentTimeMillis() + ".xlsx")
											.getBytes(),
									"ISO8859-1"));

			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			transformToExcel.write(byteOutputStream);
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}


	public String downloadQuestion(){
		return "downloadQuestion";
	}

	public String downloadMiddleOriginal() {
		return "downloadMiddleOriginal";
	}




	public InputStream getDownloadMiddleOriginal() {


		if (null == testLogItemIds) {return null;}
		String[] idsArry =  testLogItemIds.split(",");
		List<String> idSet = new ArrayList<>();
		for(String id:idsArry){
			idSet.add(id);
		}


		List<Map<String, Object>> sqlObj1 = new ArrayList<>();

			// 逐条调用
			for (String id : idSet) {
				List<String> idTemp = new ArrayList<>();
				idTemp.add(id);
				try {
					List<Map<String, Object>> sampPointByLogFiles = influxService.getSampPointByLogFiles(idTemp);
					sqlObj1.addAll(sampPointByLogFiles);
					LOGGER.error("middleOriginal Sheet1 ~ : " + sampPointByLogFiles.size());
				}catch (Exception e){
					LOGGER.error("influxdb service [" + idTemp + "]" ,e);
				}
			}





		List<Map<String, Object>> sqlObj2 = new ArrayList<>();

			// 逐条调用
			for(String id:idSet){
				List<String> idTemp = new ArrayList<>();
				idTemp.add(id);
				try {
					List<Map<String, Object>> eventByLogFiles = influxService.getEventByLogFiles(idTemp);
					sqlObj2.addAll(eventByLogFiles);
					LOGGER.error("middleOriginal Sheet2 ~ : " + eventByLogFiles.size());
				}catch (Exception e){
					LOGGER.error("influxdb service [" + idTemp + "]" ,e);
				}
			}



		// 补充ID
		TestLogItemUtils.addId(sqlObj1);
		TestLogItemUtils.addId(sqlObj2);




		Map<String, Collection> hashMap1 = new HashMap<>();
		TestLogItemUtils.formatNumber(sqlObj1);
		TestLogItemUtils.formatNumber(sqlObj2);
		hashMap1.put("sqlObj1", sqlObj1);
		hashMap1.put("sqlObj2", sqlObj2);
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(
					hashMap1, "templates/1.NR路测数据指标输出表-采样点.xlsx");

			ActionContext
					.getContext()
					.put("fileName",
							new String((
									"1.NR路测数据指标输出表-采样点-" + System.currentTimeMillis() + ".xlsx")
											.getBytes(),
									"ISO8859-1"));

			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			transformToExcel.write(byteOutputStream);
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	public String downloadMiddleGrid() {
		return "downloadMiddleGrid";
	}

	public InputStream getDownloadMiddleGrid() {
		if (null == testLogItemIds) {return null;}
		String[] idsArry =  testLogItemIds.split(",");
		List<String> idList = new ArrayList<>();
		for(String id:idsArry){
			idList.add(id);
		}

		Map<String, Collection> hashMap1 = new HashMap<>();

		List<Map<String, Object>> sqlObj1 = new ArrayList<>();

			for(String id:idList){
				List<String> idTemp = new ArrayList<>();
				idTemp.add(id);
				try{
					List<Map<String, Object>> maps = unicomLogItemService.middleGrid(idTemp);
					sqlObj1.addAll(maps);
				}catch (Exception e){
					LOGGER.error("influxdb service [" + idTemp + "]" ,e);
				}
			}
		TestLogItemUtils.formatNumber(sqlObj1);
		hashMap1.put("sqlObj1", sqlObj1);

		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(
					hashMap1, "templates/2.NR路测数据指标输出表-栅格.xlsx");

			ActionContext
					.getContext()
					.put("fileName",
							new String((
									"2.NR路测数据指标输出表-栅格-" + System.currentTimeMillis() + ".xlsx")
											.getBytes(),
									"ISO8859-1"));

			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			transformToExcel.write(byteOutputStream);
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}



	public String downloadDataOverview() {
		return "downloadDataOverview";
	}

	public InputStream getDownloadDataOverview() {

		List<Map<String, Object>> sqlObj = dataOverview();
		if(sqlObj==null){
			return null;
		}

		Map<String, Collection> hashMap1 = new HashMap<>();

		hashMap1.put("sqlObj", sqlObj);

		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(
					hashMap1, "templates/数据概览.xlsx");

			ActionContext
					.getContext()
					.put("fileName",
							new String((
									"数据概览-" + System.currentTimeMillis() + ".xlsx")
											.getBytes(),
									"ISO8859-1"));

			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			transformToExcel.write(byteOutputStream);
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}


	public List<Map<String,Object>> questionview() {
		if (null == testLogItemIds) {return null;}

		List<String> questionIndexList = unicomLogItemPageQueryRequestBean.getQuestionIndexList();
		String fileName = unicomLogItemPageQueryRequestBean.getFileName();
		String latitude = unicomLogItemPageQueryRequestBean.getLatitude();
		String longitude = unicomLogItemPageQueryRequestBean.getLongitude();

		String[] idsArry =  testLogItemIds.split(",");
		List<String> idList = new ArrayList<>();
		for(String id:idsArry){
			idList.add(id);
		}
		List<Map<String, Object>> abEvtAnaGerView = influxService.getAbEvtAnaGerView(idList);

		// 恶心心的条件
		List<Map<String, Object>> collect = abEvtAnaGerView.stream()
				// 事件名
				.filter(it -> {
					if (questionIndexList.size() == 0) {
						return true;
					} else {
						return questionIndexList.contains(MapUtil.mapSafeGet(it, "evtCnName"));
					}
				})
				// 经度
				.filter((it -> {
					if (longitude == null && (!StringUtils.hasText(longitude))) {
						return true;
					} else {
						return MapUtil.mapSafeGet(it, "Long").equals(longitude);
					}
				}))
				// 维度
				.filter(it -> {
					if (latitude == null && (!StringUtils.hasText(latitude))) {
						return true;
					} else {
						return MapUtil.mapSafeGet(it, "Lat").equals(latitude);
					}
				})
				// 日志名
				.filter(it -> {
					if (fileName == null && (!StringUtils.hasText(fileName))) {
						return true;
					} else {
						return MapUtil.mapSafeGet(it, "logName").contains(fileName);
					}
				})
				.collect(Collectors.toList());

		Integer index = 0;
		for(Map<String,Object> map:collect){
			map.put("id",index++);
		}



		return collect;
	}





	public List<Map<String,Object>> dataOverview(){
		if (null == testLogItemIds) {return null;}
		// 调试专用
		// testLogItemIds = testLogItemIds + ",1441";
		String[] idsArry =  testLogItemIds.split(",");
		String idStr = "";
		for(int i=0;i<idsArry.length;i++){
			//String t = "'" + idsArry[i]  + "'" ;
			String t =  idsArry[i]  ;
			if(i!=0){
				idStr = idStr +  " , " + t;
			}else{
				idStr = t;
			}
		}

		List<Map<String, Object>> sqlObj = unicomLogItemService.dataOverview(idStr);
		return sqlObj;
	}


	public String provInput(){

		List<Map<String, Object>> sqlObj = unicomLogItemService.provInput();

		String s = JSONArray.fromObject(sqlObj).toString();
		StringBuffer stringBuffer = new StringBuffer(s);


		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(stringBuffer.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public String cityInput(){

		List<Map<String, Object>> sqlObj = unicomLogItemService.cityInput(unicomLogItemPageQueryRequestBean.getProv());
		String s = JSONArray.fromObject(sqlObj).toString();
		StringBuffer stringBuffer = new StringBuffer(s);


		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(stringBuffer.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public String seeInfoData() {
		List<Map<String, Object>> sqlObj = dataOverview();
		ActionContext.getContext().getValueStack()
				.set("rows", sqlObj);
		ActionContext.getContext().getValueStack()
		.set("footer", new ArrayList());
		return ReturnType.JSON;
	}


	public String seeInfo() {
		List<Map<String, Object>> sqlObj = dataOverview();
		ActionContext.getContext().getValueStack()
				.set("sqlObj", sqlObj);
		return "dataOverview";
	}


	public String seeQuestionviewData() {
		List<Map<String, Object>> sqlObj = questionview();
		ActionContext.getContext().getValueStack()
				.set("rows", sqlObj);
		ActionContext.getContext().getValueStack()
				.set("footer", new ArrayList());
		return ReturnType.JSON;
	}


	public String seeQuestionview() {
//		List<Map<String, Object>> sqlObj = questionview();
//		ActionContext.getContext().getValueStack()
//				.set("sqlObj", sqlObj);
		return "questionview";
	}


	public String getMap(Map<String,Object> m,String key){
		if(m==null){
			return null;
		}
		if(m.get(key)==null){
			return null;
		}
		return m.get(key).toString();

	}

	public String getMd5Default(Map<String,String> m,String key,String def){
		if(m==null){
			return def;
		}
		if(m.get(key)==null){
			return def;
		}
		return m.get(key).toString();

	}
	/**
	 * 保存测试日志id数组
	 *
	 * @return
	 */
	public String saveAnalysisTestLogItem() {
		if (StringUtils.hasText(testLogItemIds)) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("testLogItemIds", testLogItemIds);
		}
		return ReturnType.JSON;
	}


	public String mapTrail() {
		if (null == testLogItemIds) {return null;}
		String[] idsArry =  testLogItemIds.split(",");
		List<String> idSet = new ArrayList<>();
		for(String id:idsArry){
			idSet.add(id);
		}

		// 调试专用
		// fileName.add("none38");
		LOGGER.error("下发日志id名称: " + idSet.toString());
		JSONObject json = new JSONObject();
		json.put("mapData",influxService.getMapTrailByLogFiles(idSet));
		ActionContext.getContext().getValueStack().push(json);
		return ReturnType.JSON;
	}






	/**
	 * 删除指定的测试日志
	 *
	 * @return
	 */
	public String deleteTestLogItem() {
		if (StringUtils.hasText(testLogItemIds)) {
			unicomLogItemService.deleteTestLogItem(testLogItemIds);
		}
		return ReturnType.JSON;
	}
	public String downloadFullCheck() {
		return "downloadFullCheck";
	}

	public InputStream getDownloadFullCheck() {

		Date beginDate = unicomLogItemPageQueryRequestBean.getBeginDate();
		Date endDate = unicomLogItemPageQueryRequestBean.getEndDate();


		List<Map<String, Object>> sqlObj1 = unicomLogItemService.logCheckMd5(beginDate,endDate,testLogItemIds);
		List<Map<String, Object>> sqlObj2 = unicomLogItemService.logCheckBiz(beginDate,endDate,testLogItemIds);

		Map<String, Collection> hashMap1 = new HashMap<>();

		hashMap1.put("sqlObj1", sqlObj1);
		hashMap1.put("sqlObj2", sqlObj2);

		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(
				hashMap1, "templates/日志校验结果导出.xlsx");

			ActionContext
					.getContext()
					.put("fileName",
							new String((
									"日志校验结果导出-" + System.currentTimeMillis() + ".xlsx")
											.getBytes(),
									"ISO8859-1"));


			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			transformToExcel.write(byteOutputStream);
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 下载测试日志
	 *
	 * @return
	 */
	public String downloadLog() {
		ServletOutputStream outputStream = null;
		List<String> unUsedStrList = new ArrayList<String>() ;
		String respString = "<!DOCTYPE html><html><head><title></title></head><body><h2>";
		if (null != testLogItemIds) {
			String[] idsArry =  testLogItemIds.split(",");
			for (String id : idsArry) {
				UnicomLogItem queryTestLogById = unicomLogItemService.queryTestLogById(Long.valueOf(id));
				if (null != queryTestLogById && StringUtils.hasText(queryTestLogById.getFilelink())) {
					if(queryTestLogById.getFilelink().contains("./")){
						String filePath = dzTestLogTtemUrl + queryTestLogById.getFilelink().replace("./", "/").replace(" ", "");
						if(filePath.indexOf(queryTestLogById.getFileName()) == -1){
							filePath = filePath + queryTestLogById.getFileName();
						}
						File log = new File(filePath);
						System.out.println(filePath);
						if (!log.exists() || !log.isFile()) {
							unUsedStrList.add(id);
							respString = respString+queryTestLogById.getFileName()+" 日志不存在!路径:"+filePath+"</br>";
						}
					}else{
						unUsedStrList.add(id);
						respString = respString+queryTestLogById.getFileName()+":日志名称的路径应该为相对路径!</br>";
					}
				}
			}
		}

		if(unUsedStrList==null || unUsedStrList.size()==0){
			return "downloadLog";
		}else{
			try {
			respString = respString + "</h2><input type='button' value='返回' onclick='self.window.history.go(-1);' /></body></html>";
			outputStream = ServletActionContext.getResponse().getOutputStream();
				outputStream.write(respString.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (null != outputStream) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// 文件不存在
		return null;
	}

	/**
	 * 下载测试日志
	 *
	 * @return
	 */
	public InputStream getDownloadTestLog() {
		try {
			if (null != testLogItemIds) {
				String[] idsArry =  testLogItemIds.split(",");

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String newDate = sdf.format(new Date());
				File file1 = new File(reportUrl+ "/testLog/");
				if (!file1.exists()) {
					file1.mkdirs();
				}

				deleteFile(file1);
				File zipFile = new File(reportUrl + "/testLog/" + newDate + "测试日志报告.zip");

				List<File> fileList = new ArrayList<File>();
				for (String id : idsArry) {
					UnicomLogItem queryTestLogById = unicomLogItemService.queryTestLogById(Long.valueOf(id));
					if (null != queryTestLogById && StringUtils.hasText(queryTestLogById.getFilelink())) {
						String filePath = dzTestLogTtemUrl + queryTestLogById.getFilelink().replace("./", "/");
						if(filePath.indexOf(queryTestLogById.getFileName()) == -1){
							filePath = filePath + queryTestLogById.getFileName();
						}

						File log = new File(filePath);
						if (log.exists() && log.isFile()) {
							fileList.add(log);
						}
					}
				}
				ActionContext.getContext().put("fileName",new String(zipFile.getName().getBytes(),"ISO8859-1"));
				ZipMultiFile.zipFiles(fileList, zipFile);
				FileInputStream zipIn = new FileInputStream(zipFile);
				zipFile.delete();
				return zipIn;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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
	 * 获取用户的权限范围内的区域菜单树
	 *
	 * @return
	 */
	public String questionTree() {

		List<AbEventConfig> abEventConfigs = InitialConfig.abEventConfigs;
		String json = QuestionEventTitleBean.jsonFromMap(abEventConfigs);


//		// 直接返回原始字符串
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public UnicomLogItemPageQueryRequestBean getModel() {
		return unicomLogItemPageQueryRequestBean;
	}

	/**
	 * @return the
	 *         testLogItemPageQueryRequestBeantestLogItemPageQueryRequestBean
	 */
	public UnicomLogItemPageQueryRequestBean getTestLogItemPageQueryRequestBean() {
		return unicomLogItemPageQueryRequestBean;
	}

	/**
	 * @param unicomLogItemPageQueryRequestBean
	 *            the unicomLogItemPageQueryRequestBean to set
	 */
	public void setUnicomLogItemPageQueryRequestBean(
			UnicomLogItemPageQueryRequestBean unicomLogItemPageQueryRequestBean) {
		this.unicomLogItemPageQueryRequestBean = unicomLogItemPageQueryRequestBean;
	}

	/**
	 * @return the testLogItemIdstestLogItemIds
	 */
	public String getTestLogItemIds() {
		return testLogItemIds;
	}

	/**
	 * @param testLogItemIds
	 *            the testLogItemIds to set
	 */
	public void setTestLogItemIds(String testLogItemIds) {
		this.testLogItemIds = testLogItemIds;
	}

	/**
	 * @return the recSeqNorecSeqNo
	 */
	public Long getRecSeqNo() {
		return recSeqNo;
	}

	/**
	 * @param recSeqNo
	 *            the recSeqNo to set
	 */
	public void setRecSeqNo(Long recSeqNo) {
		this.recSeqNo = recSeqNo;
	}



}
