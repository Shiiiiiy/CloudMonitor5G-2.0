package com.datang.web.action.action5g.report;

import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import ch.qos.logback.core.util.ExecutorServiceUtil;
import com.datang.common.dao.jdbc.JdbcTemplate;
import com.datang.dao.customTemplate.AnalyFileReportDao;
import com.datang.dao.customTemplate.CustomLogReportTaskDao;
import com.datang.domain.customTemplate.AnalyFileReport;
import com.datang.domain.customTemplate.CustomLogReportTask;
import com.datang.domain.testLogItem.UnicomLogItem;
import com.datang.service.influx.InfluxService;
import com.datang.service.influx.QuesRoadService;
import com.datang.service.testLogItem.UnicomLogItemService;
import com.datang.web.beans.report.*;
import com.datang.web.beans.testLogItem.UnicomLogItemPageQueryRequestBean;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zeromq.ZMQ.Socket;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;
import com.datang.common.util.TestLogItemUtils;
import com.datang.domain.customTemplate.CustomReportTemplatePojo;
import com.datang.domain.customTemplate.MappingIeToKpiPojo;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.exception.ApplicationException;
import com.datang.service.customTemplate.CustomTemplateService;
import com.datang.service.report.IReportService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.util.DateUtil;
import com.datang.util.ReadExcelToHtml;
import com.datang.util.ZMQUtils;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.datang.web.action.report.ReportAction;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;

/**
 * 5G统计任务Action
 *
 * @author _YZP
 *
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class ReportFgAction extends PageAction implements
		ModelDriven<StatisticeTaskRequest> {
	private static Logger LOGGER = LoggerFactory.getLogger(ReportAction.class);
	private static String ANALYZE_TEMPLATE_PATH = "分析型报表";
	private static String STATISTICE_TEMPLATE_PATH = "uploadReportTemplate";
	/**
	 * 统计任务服务
	 */
	@Autowired
	private IReportService reportService;
	/**
	 * 统计任务服务
	 */
	@Autowired
	private CustomLogReportTaskDao customLogReportTaskDao;
	@Autowired
	private AnalyFileReportDao analyFileReportDao;
	/**
	 * 日志服务
	 */
	@Autowired
	private ITestLogItemService testLogItemService;

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource(name="influxService")
	private InfluxService influxService;
	@Resource(name="quesRoadService")
	private QuesRoadService quesRoadService;

	/**
	 * 日志服务
	 */
	@Autowired
	private UnicomLogItemService unicomLogItemService;


	/**
	 * 终端服务
	 */
	@Autowired
	private TerminalService trminalService;
	/**
	 * 区域管理服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;

	/**
	 * 自定义报表模板服务
	 */
	@Autowired
	private CustomTemplateService customTemplateService;

	@Value("${decode.signalling.ip}")
	private String taskIp;

	@Value("${decode.signalling.port}")
	private String taskPort;

	private String allLogNames;

	/** 多条件查询请求参数 */
	private ReportRequertBean reportRequertBean = new ReportRequertBean();
	/**
	 * 保存统计任务时收集参数
	 */
	private StatisticeTaskRequest statisticeTaskRequest = new StatisticeTaskRequest();
	private Integer[] collectTypes;
	private Integer[] testRanks;
	/**
	 * 删除统计任务时收集参数
	 */
	private String ids;
	/**
	 * 查看详情收集参数
	 */
	private Long idLong;
	/**
	 * 判断运营商0:中国移动1：中国联通2：中国电信
	 */
	private Integer typeNo;
	/**
	 * 要导出的sheet名
	 */
	private String sheetName;

	/**
	 * 目标界面
	 */
	private String dPage;

	private static final ExecutorService analyzeTaskExecutor = Executors.newFixedThreadPool(2);

	/**
	 * 分析型报表
	 * */
	public  static List<AnalyzeTemplate> analyzeTamplateList = new ArrayList<>();

	public  static Map<String,AnalyzeTemplate> analyzeTemplateMap =  new HashMap<>();


	@Value("${appTaskReportFileLink}")
	private String fileSaveUrl;

	@Value("${stationReportFileLink}")
	private String customFileSaveUrl;


	static{
		analyzeTamplateList.add(new AnalyzeEventTemplate());
		analyzeTamplateList.add(new AnalyzeNetworkTemplate());
		analyzeTamplateList.add(new AnalyzeVoiceTemplate());
		analyzeTamplateList.add(new QuesRoadTemplate());

		analyzeTemplateMap = ReportFgAction.analyzeTamplateList.stream().collect(Collectors.toMap(it->it.getId(), Function.identity(),(o1, o2)-> o1));
	}



	/**
	 * 跳转到 list界面
	 *
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到添加界面
	 *
	 * @return
	 */
	public String goAdd() {
		HttpSession session = ServletActionContext.getRequest()
				.getSession();
		if(dPage!=null && !dPage.equals("")){
			session.setAttribute("dPage", dPage);
		}
		return "add";
	}

	/**
	 * 跳转到统计结果查看界面
	 */
	public String goSee() {
		Long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("idLong");
		StatisticeTask statisticeTask = reportService.queryOneByID(id);
		String templateIds = statisticeTask.getTemplateIds();
		List<Map<String,Object>> teplateList = new ArrayList<Map<String,Object>>();
		if (StringUtils.hasText(templateIds)) {
			String[] idArry = templateIds.split(",");
			for (String idstr : idArry) {
				Map<String,Object> map = new HashMap<String,Object>();
				CustomReportTemplatePojo reportTemplateByid = customTemplateService.find(Long.valueOf(idstr));
				map.put("valueField",reportTemplateByid.getId());
				map.put("textField",reportTemplateByid.getTemplateName());
				teplateList.add(map);
			}
		}
		ActionContext.getContext().getValueStack().set("reportTemplate", teplateList);
		return "seeReport";
	}

	/**
	 * 跳转到数据业务报表统计界面
	 *
	 * @return
	 */
//	public String goData() {
//		if (null != typeNo) {
//			HttpSession session = ServletActionContext.getRequest()
//					.getSession();
//			session.setAttribute("typeNo", typeNo);
//		}
//		return "data";
//	}

	/**
	 * 跳转到异常事件报表统计界面
	 *
	 * @return
	 */
//	public String goEe() {
//		if (null != typeNo) {
//			HttpSession session = ServletActionContext.getRequest()
//					.getSession();
//			session.setAttribute("typeNo", typeNo);
//		}
//		return "ee";
//	}

	/**
	 * 跳转到NSA指标报表统计界面
	 *
	 * @return
	 */
	public String goNsaIndex() {
		if (null != typeNo) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("typeNo", typeNo);
		}
		return "nsaIndex";
	}

	/**
	 * 跳转到测试轨迹界面
	 * @return
	 */
	public String goTraii() {
		return "testTrail";
	}

	/**
	 * 跳转到自定义报表报表统计界面
	 * @return
	 */
	public String goTemplateExcel() {
		if (null != typeNo) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("typeNo", typeNo);
			session.setAttribute("templateIds", statisticeTaskRequest.getTemplateIds());
//				Long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("idLong");
//				StatisticeTask statisticeTask = reportService.queryOneByID(id);
//				if(statisticeTask!=null && !statisticeTask.getTaskStatus().equals("2")){
//					throw new ApplicationException("任务未解析，请等待解析完成");
//				}
//				String templateId = statisticeTaskRequest.getTemplateIds();
//				CustomReportTemplatePojo pojo = customTemplateService.find(Long.valueOf(templateId));
//
//				File file = new File(pojo.getSaveFilePath());
//				String newExcelname = file.getName().substring(0, file.getName().lastIndexOf(".")) + "_fin"
//						+ file.getName().substring(file.getName().lastIndexOf("."));
//				String newPath = file.getParentFile().getAbsolutePath() + "/" + newExcelname;
//				File exportfile = new File(newPath);
//
//				String exportFilePath = null;
//				Integer typeNo = (Integer) ServletActionContext.getRequest().getSession().getAttribute("typeNo");
//				if(exportfile==null || !exportfile.exists()){
//					Object attribute = null;
//					if (null != typeNo) {
//						if (0 == typeNo) {
//							attribute = ServletActionContext.getRequest().getSession()
//									.getAttribute("moveTestLogItemIds");
//						} else if (1 == typeNo) {
//							attribute = ServletActionContext.getRequest().getSession()
//									.getAttribute("linkTestLogItemIds");
//						} else if (2 == typeNo) {
//							attribute = ServletActionContext.getRequest().getSession()
//									.getAttribute("telecomTestLogItemIds");
//						}
//					}
//					Map<String, Object> map = new HashMap<>();
//					String idsString = null;
//					if (null != attribute) {
//						if (attribute instanceof String) {
//							idsString = (String) attribute;
//						}
//					}
//
//					if(StringUtils.hasText(pojo.getKpiNameSum())){
//						PageList page = new PageList();
//						page.putParam("taskName", statisticeTask.getName());
//						page.putParam("logids", idsString);
//						page.putParam("kpis", pojo.getKpiNameSum());
//						page.putParam("excelPath",pojo.getSaveFilePath());
//						page.putParam("exportFilePath", newPath);
//						//根据指标公式修改获取值保存到excel
//						exportFilePath = customTemplateService.modifyExcelValue(page);
//					}
//				}else{
//					exportFilePath = newPath;
//				}
//				List<Map<String, String>> readExcelToMap = ReadExcelToHtml.readExcelToMap(exportFilePath, true);
//
//				ActionContext.getContext().getValueStack().set("dataList", readExcelToMap);
//			} catch (ApplicationException e) {
//				e.printStackTrace();
//				ActionContext.getContext().getValueStack().set("errorMsg", "错误:"+e.getMessage());
//			}
		}
		return "templateExcel";
	}

	/**
	 * 根据日志名查询采样点数据
	 * @author maxuancheng
	 * date:2020年5月15日 下午4:16:50
	 * @return
	 */
	public String getGpsPointData(){
		//allLogNames = "5000001620200416164615_1XW-松林坡ADHV_1_H_DT.l5g";
		List<StationSAMTralPojo> point = testLogItemService.getGpsPointData(allLogNames);
		ActionContext.getContext().getValueStack().set("pointList", point);
		return ReturnType.JSON;
	}


	/**
	 * 获取某些boxIds相关的日志 testLogItem
	 * 周文千需求
	 * @return
	 */
	public String testUnicomLogItem() {
		Date beginDate = statisticeTaskRequest.getBeginDate();
		Date endDate = statisticeTaskRequest.getEndDate();
		// String boxIds = statisticeTaskRequest.getBoxIds();
		String cityIds = statisticeTaskRequest.getCityIds();
		String prov = statisticeTaskRequest.getProv();
		String city = statisticeTaskRequest.getCity();
		String filename = statisticeTaskRequest.getFilename();

		// String testRanks = statisticeTaskRequest.getTestRank();
		String boxIds = "";
		String testRanks = "";
		cityIds = "";
		if (
//				StringUtils.hasText(boxIds)
//				&&StringUtils.hasText(cityIds)
//				&& StringUtils.hasText(testRanks)
//				&&
				null != beginDate
						&& null != endDate) {
			String[] splitBox = boxIds.split(",");
			List<String> boxList = Arrays.asList(splitBox);
			String[] splitCity = cityIds.split(",");
			List<String> cityList = Arrays.asList(splitCity);
			String[] splitTestRank = testRanks.split(",");
			List<String> testRankList = new ArrayList<>();
			for (String string : splitTestRank) {
				if (string.trim().equals("1")) {
					testRankList.add("organizationCheck");
				} else if (string.trim().equals("2")) {
					testRankList.add("dailyOptimiz");
				} else if (string.trim().equals("3")) {
					testRankList.add("deviceDebug");
				}
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");

			// 取消条件
			boxList = new ArrayList<>();
			cityList = new ArrayList<>();
			testRankList = new ArrayList<>();

			List<UnicomLogItem> testLogItemsByBoxIds = unicomLogItemService
					.queryTestLogItemsByOther(prov,city,boxList, cityList, testRankList,
							filename,
							beginDate, endDate);
			List<TestInfoRequestBean> responseBeans = new ArrayList<>();
			if (null != testLogItemsByBoxIds
					&& 0 != testLogItemsByBoxIds.size()) {
				for (UnicomLogItem testLogItem : testLogItemsByBoxIds) {
					TestInfoRequestBean responseBean = new TestInfoRequestBean();
					responseBean.setFileName(testLogItem.getFileName());
					responseBean.setId(testLogItem.getRecSeqNo());
					responseBeans.add(responseBean);
				}
			}
			ActionContext.getContext().getValueStack()
					.set("testLog", responseBeans);
		}

		return ReturnType.JSON;
	}



	/**
	 * 获取某些boxIds相关的日志 testLogItem
	 *
	 * @return
	 */
	public String testLogItem() {
		Date beginDate = statisticeTaskRequest.getBeginDate();
		Date endDate = statisticeTaskRequest.getEndDate();
		String boxIds = statisticeTaskRequest.getBoxIds();
		String cityIds = statisticeTaskRequest.getCityIds();
		String testRanks = statisticeTaskRequest.getTestRank();

		List<String> boxList = new ArrayList<>();
		if (StringUtils.hasText(boxIds)) {
			String[] splitBox = boxIds.split(",");
			boxList = Arrays.asList(splitBox);
		}

		List<String> cityList = new ArrayList<>();
		if (StringUtils.hasText(cityIds)) {
			String[] splitCity = cityIds.split(",");
			cityList = Arrays.asList(splitCity);
		}
		List<String> testRankList = new ArrayList<>();
		if (StringUtils.hasText(testRanks)) {
			String[] splitTestRank = testRanks.split(",");
			testRankList = new ArrayList<>();

			for (String string : splitTestRank) {
				if (string.trim().equals("1")) {
					testRankList.add("organizationCheck");
				} else if (string.trim().equals("2")) {
					testRankList.add("dailyOptimiz");
				} else if (string.trim().equals("3")) {
					testRankList.add("deviceDebug");
				}
			}
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		List<TestLogItem> testLogItemsByBoxIds = testLogItemService
				.queryTestLogItemsByOther(boxList, cityList, testRankList,
						beginDate, endDate);
		List<TestInfoRequestBean> responseBeans = new ArrayList<>();
		if (null != testLogItemsByBoxIds
				&& 0 != testLogItemsByBoxIds.size()) {
			for (TestLogItem testLogItem : testLogItemsByBoxIds) {
				TestInfoRequestBean responseBean = new TestInfoRequestBean();
				responseBean.setFileName(testLogItem.getFileName());
				responseBean.setId(testLogItem.getRecSeqNo());
				responseBeans.add(responseBean);
			}
		}
		ActionContext.getContext().getValueStack()
				.set("testLog", responseBeans);


		return ReturnType.JSON;
	}


	/**
	 * 获取某区域获取相关的模板 CustomReportTemplatePojo
	 * 周文千需求
	 * @return
	 */
	public String reportTemplate() {
		String reportType = statisticeTaskRequest.getReportType();
		List<Map<String, Object>>  templatePojoList = null;

		List<Map<String,Object>> templateList = new ArrayList<Map<String,Object>>();

		if(reportType!=null && reportType.equals("1")){
			for(AnalyzeTemplate t:analyzeTamplateList){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",t.getId());
				map.put("fileName",t.getTemplate());
				templateList.add(map);
			}
			ActionContext.getContext().getValueStack()
					.set("reportTemplate", templateList);
			return ReturnType.JSON;
		}

		String sql = "SELECT  array_agg(ID) ID ,TEMPLATE_NAME FILE_NAME FROM IADS_CUSREPORT_TMEPLATE WHERE TEMPLATE_NAME IS NOT NULL " +
				" AND (TEMPLATE_NAME LIKE '%指标报表_CQT模板%' OR TEMPLATE_NAME LIKE '%指标报表_DT模板%'   OR TEMPLATE_NAME LIKE '%移动%'    OR TEMPLATE_NAME LIKE '%电联%' ) " +
				" GROUP BY TEMPLATE_NAME ORDER BY TEMPLATE_NAME";
		templatePojoList = jdbcTemplate.objectQueryAll(sql);
		if (null != templatePojoList
				&& 0 != templatePojoList.size()) {
			for (Map<String, Object> m : templatePojoList) {
				Map<String,Object> map = new HashMap<String,Object>();
				if(m.get("id")==null) continue;
				try{
					Object id = m.get("id");
					if(id instanceof Clob){
						map.put("id",ClobToString((Clob)id));
					}else{
						map.put("id",id.toString());
					}

					map.put("fileName",(String)m.get("file_name"));
					templateList.add(map);
				}catch (Exception e){

				}


			}
		}
		ActionContext.getContext().getValueStack()
				.set("reportTemplate", templateList);
		return ReturnType.JSON;
	}

	public String ClobToString(Clob clob) throws SQLException, IOException {

		String reString = "";
		java.io.Reader is = clob.getCharacterStream();// 得到流
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		return reString;
	}

	/**
	 * 获取某区域获取相关的模板 CustomReportTemplatePojo
	 *
	 * @return
	 */
	public String regionReportTemplate() {
		String cityIds = statisticeTaskRequest.getCityIds();
		List<Map<String,Object>> teplateList = new ArrayList<Map<String,Object>>();
		if (StringUtils.hasText(cityIds)) {
			PageList pageParam = new PageList();
			pageParam.putParam("cityIds", cityIds);
			List<CustomReportTemplatePojo> templatePojoList = customTemplateService.queryTemplateByParam(pageParam);

			if (null != templatePojoList
					&& 0 != templatePojoList.size()) {
				Set<String> set = new HashSet<>();
				for (CustomReportTemplatePojo pojo : templatePojoList) {

					if(set.add(pojo.getTemplateName())){
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("id",pojo.getId());
						map.put("fileName",pojo.getTemplateName());
						teplateList.add(map);
					}
				}
			}
			ActionContext.getContext().getValueStack()
					.set("regionReportTemplate", teplateList);
		}

		return ReturnType.JSON;
	}

	/**
	 * 根据LogIDs获取日志 testLogItem
	 *
	 * @return
	 */
	public String getTestLogItem() {
		String logIds = statisticeTaskRequest.getLogIds();
		if (StringUtils.hasText(logIds)) {
			List<TestLogItem> testLogItemsByBoxIds = testLogItemService
					.queryTestLogItems(logIds);
			List<TestInfoRequestBean> responseBeans = new ArrayList<>();
			if (null != testLogItemsByBoxIds
					&& 0 != testLogItemsByBoxIds.size()) {
				for (TestLogItem testLogItem : testLogItemsByBoxIds) {
					TestInfoRequestBean responseBean = new TestInfoRequestBean();
					responseBean.setFileName(testLogItem.getFileName());
					responseBean.setId(testLogItem.getRecSeqNo());
					responseBeans.add(responseBean);
				}
			}
			ActionContext.getContext().getValueStack()
					.set("testLog", responseBeans);
		}

		return ReturnType.JSON;
	}

	/**
	 * 根据templateIds获取报告模板
	 *
	 * @return
	 */
	public String getReportTemplate() {
		String templateIds = statisticeTaskRequest.getTemplateIds();
		List<Map<String,Object>> teplateList = new ArrayList<Map<String,Object>>();
		if (StringUtils.hasText(templateIds)) {
			String[] idArry = templateIds.split(",");
			for (String id : idArry) {
				Map<String,Object> map = new HashMap<String,Object>();
				CustomReportTemplatePojo reportTemplateByid = customTemplateService.find(Long.valueOf(id));
				map.put("id",reportTemplateByid.getId());
				map.put("fileName",reportTemplateByid.getTemplateName());
				teplateList.add(map);
			}
			ActionContext.getContext().getValueStack()
					.set("reportTemplate", teplateList);
		}

		return ReturnType.JSON;
	}

	/**
	 * 获取城市域信息
	 */
	public String getCityInfo() {
		String cityIds = statisticeTaskRequest.getCityIds();
		if (StringUtils.hasText(cityIds)) {
			String[] splitCity = cityIds.split(",");
			List<String> cityList = Arrays.asList(splitCity);
			List<CityInfoRequestBean> list = new ArrayList<>();
			for (String string : cityList) {
				if (null != string && !string.equals("")) {
					TerminalGroup terminalGroup = terminalGroupService
							.findGroupById(Long.valueOf(string));
					if (null != terminalGroup) {
						CityInfoRequestBean cityInfoRequestBean = new CityInfoRequestBean();
						cityInfoRequestBean.setCityId(string);
						cityInfoRequestBean.setName(terminalGroup.getName());
						list.add(cityInfoRequestBean);
					}
				}

			}
			ActionContext.getContext().getValueStack().set("cityInfo", list);
		}
		return ReturnType.JSON;
	}

	/**
	 * 多条件查询任务
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {

		reportRequertBean.setBeginDate(statisticeTaskRequest.getBeginDate());
		reportRequertBean.setEndDate(statisticeTaskRequest.getEndDate());
		reportRequertBean
				.setCreaterName(statisticeTaskRequest.getCreaterName());
		reportRequertBean.setName(statisticeTaskRequest.getName());
		pageList.putParam("pageQueryBean", reportRequertBean);
		return reportService.pageList(pageList);

	}

	@Override
	public StatisticeTaskRequest getModel() {
		return statisticeTaskRequest;
	}

	/**
	 * 查询单个任务信息并跳转到相关页面
	 *
	 * @return
	 */
	public String seeInfo() {
		HttpSession session = ServletActionContext.getRequest()
				.getSession();
		if(dPage!=null && !dPage.equals("")){
			session.setAttribute("dPage", dPage);
		}
		if (null != idLong) {
			StatisticeTask queryOneByID = reportService.queryOneByID(idLong);

			ActionContext.getContext().getValueStack()
					.set("statisticeTask", queryOneByID);
			ActionContext.getContext().getValueStack()
					.set("dPage", dPage);
		} else {
			String readPage = (String) ServletActionContext.getRequest().getSession()
					.getAttribute("dPage");
			Long id = (Long) ServletActionContext.getRequest().getSession()
					.getAttribute("idLong");
			if (null != id) {
				ActionContext.getContext().getValueStack()
						.set("statisticeTask", reportService.queryOneByID(id));
				ActionContext.getContext().getValueStack()
						.set("dPage", readPage);
			}
		}

		return "seeTask";
	}


	/**
	 * 添加或修改统计任务
	 *
	 * @return
	 */
	public String addUnicomReportTask() {
		// 测试
//		String ltt = statisticeTaskRequest.getLogIds();
//		if(ltt!=null && StringUtils.hasText(ltt)){
//			ltt = "," + ltt;
//		}else {
//			ltt = "";
//		}
//		statisticeTaskRequest.setLogIds("2070,2080" +ltt );


		if (null != statisticeTaskRequest) {
			// 创建人名称
			CustomLogReportTask customLogReportTask = new CustomLogReportTask();
			// StatisticeTask statisticeTask = new StatisticeTask();
			if (StringUtils.hasText(statisticeTaskRequest.getCreaterName())) {
				customLogReportTask.setCreaterName(statisticeTaskRequest.getCreaterName());
			}else{
				String userName = (String) SecurityUtils.getSubject()
						.getPrincipal();
				if (null != userName) {
					customLogReportTask.setCreaterName(userName.trim());
				}
			}

			// ?????
			StringBuilder taskName = new StringBuilder();

			// 参数传递
			StringBuilder terminalGroupNames = new StringBuilder();
//			if (null != statisticeTaskRequest.getBoxIds()) {
//				customLogReportTask.setBoxIds(statisticeTaskRequest.getBoxIds());
//			}
			if (null != statisticeTaskRequest.getLogIds()) {
				customLogReportTask.setLogIds(statisticeTaskRequest.getLogIds());
			}
			if (null != statisticeTaskRequest.getTemplateIds()) {
				String tmp = statisticeTaskRequest.getTemplateIds();
				String tmp1 = tmp.replace("{", "");
				String tmp2 = tmp1.replace("}", "");
				customLogReportTask.setTemplateIds(tmp2);
			}
			// 查找日志
			List<TestLogItem> queryTestLogItems = unicomLogItemService
					.queryTestLogItems(statisticeTaskRequest.getLogIds());

			// ???
			StringBuilder allLogIDBuilder = new StringBuilder();
			StringBuilder allLogNameBuilder = new StringBuilder();
			StringBuilder moveLogIDBuilder = new StringBuilder();
			StringBuilder linkLogIDBuilder = new StringBuilder();
			StringBuilder telecomLogIDBuilder = new StringBuilder();
			// StringBuilder nbiotLogIDBuilder = new StringBuilder();
			for (TestLogItem testLogItem : queryTestLogItems) {
				if (0 != allLogIDBuilder.toString().length()) {
					allLogIDBuilder.append(","
							+ testLogItem.getRecSeqNo());
					allLogNameBuilder.append(","
							+ testLogItem.getFileName());
				} else {
					allLogIDBuilder.append(testLogItem.getRecSeqNo());
					allLogNameBuilder.append(testLogItem.getFileName());
				}
				if (null != testLogItem.getOperatorName()) {
					if (testLogItem.getOperatorName().trim().equals("中国移动")||testLogItem.getOperatorName().trim().equals("China Mobile")) {
						if (0 != moveLogIDBuilder.toString().length()) {
							moveLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							moveLogIDBuilder.append(testLogItem.getRecSeqNo());
						}

					} else if (testLogItem.getOperatorName().trim()
							.equals("中国联通")) {
						if (0 != linkLogIDBuilder.toString().length()) {
							linkLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							linkLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("中国电信")) {
						if (0 != telecomLogIDBuilder.toString().length()) {
							telecomLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							telecomLogIDBuilder.append(testLogItem
									.getRecSeqNo());
						}
					}
				}
			}




			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("allTestLogItemIds",
					allLogIDBuilder.toString());
			session.setAttribute("allLogNames",
					allLogNameBuilder.toString());
			session.setAttribute("moveTestLogItemIds",
					moveLogIDBuilder.toString());
			session.setAttribute("linkTestLogItemIds",
					linkLogIDBuilder.toString());
			session.setAttribute("telecomTestLogItemIds",
					telecomLogIDBuilder.toString());
			// session.setAttribute("nbiotTestLogItemIds",
			// nbiotLogIDBuilder.toString());


			if (statisticeTaskRequest.getCityIds() != null) {
//				String[] cityIds = statisticeTaskRequest.getCityIds().trim()
//						.split(",");
//				// 存储TestLogItem的id集合
//				List<String> ids = new ArrayList<>();
//				for (int i = 0; i < cityIds.length; i++) {
//					if (StringUtils.hasText(cityIds[i])) {
//						try {
//							ids.add(cityIds[i].trim());
//						} catch (NumberFormatException e) {
//							continue;
//						}
//					}
//				}
//				if (ids.size() > 1) {
//					HashSet<String> nameSet = new HashSet<String>();
//					for (String cityId : ids) {
//						if (cityId == null) {
//							continue;
//						} else {
//							Long id = Long.valueOf(cityId);
//							String name = terminalGroupService
//									.getProvinceNameByCityGroup(terminalGroupService
//											.findGroupById(id));
//							nameSet.add(name);
//						}
//
//					}
//					if (nameSet.size() > 1) {
//						terminalGroupNames.append("全国");
//					} else {
//						terminalGroupNames
//								.append((String) nameSet.toArray()[0]);
//					}
//				} else {
//					Long id = Long.valueOf(ids.get(0));
//					TerminalGroup terminalGroup = terminalGroupService
//							.findGroupById(id);
//					if (terminalGroup != null) {
//						terminalGroupNames.append(terminalGroup.getName());
//					}
//				}
				if (taskName.length() >= 1) {
					taskName.deleteCharAt(taskName.length() - 1);
				}
				if (terminalGroupNames.length() > 0) {
					taskName.append(terminalGroupNames.toString().trim());
				}

				if ( statisticeTaskRequest.getProv() !=null  && statisticeTaskRequest.getProv().length() >= 1) {
					taskName.append(statisticeTaskRequest.getProv());
					taskName.append("-");
				}

				if (statisticeTaskRequest.getCity() !=null  && statisticeTaskRequest.getCity().length() >= 1) {
					taskName.append(statisticeTaskRequest.getCity());
					taskName.append("-");
				}
				taskName.append(DateUtil.getCurDateStr(new Date()));
				taskName.append('-');
				// 分析报表 / 通用报表
				taskName.append(CustomLogReportTask.typeIsAnylyFileReport(StatisticeTaskRequest.typeIsAnylyFileReport(statisticeTaskRequest.getReportType())));

//				taskName.append(DateUtil
//						.getShortDateTimeStr(statisticeTaskRequest
//								.getBeginDate()));
//				taskName.append('-');
//				taskName.append(DateUtil
//						.getShortDateTimeStr(statisticeTaskRequest.getEndDate()));

//				if(!StatisticeTaskRequest.typeIsAnylyFileReport(statisticeTaskRequest.getReportType())){
//					UnicomLogItem testLogItem = unicomLogItemService.queryTestLogById();
//					taskName = new StringBuilder();
//					taskName.append(testLogItem.getTaskName());
//				}


				if(!statisticeTaskRequest.getName().contains("定点测试-DD")){
					customLogReportTask.setName(taskName.toString().trim());
				}else{
					customLogReportTask.setName(statisticeTaskRequest.getName());
				}
				session.setAttribute("taskName", taskName.toString());
				customLogReportTask.setCityIds(statisticeTaskRequest.getCityIds());

			}


			if (null != statisticeTaskRequest.getBeginDate()) {
				customLogReportTask.setBeginDate(statisticeTaskRequest
						.getBeginDate());
			}
			if (null != collectTypes) {
				StringBuffer sBuffer = new StringBuffer();
				for (int i = 0; i < collectTypes.length; i++) {
					sBuffer = (i == (collectTypes.length - 1)) ? sBuffer
							.append(collectTypes[i]) : sBuffer
							.append(collectTypes[i] + ",");
				}
				customLogReportTask.setCollectType(sBuffer.toString());
			}
			if (null != testRanks) {
				StringBuffer sBuffer = new StringBuffer();
				for (int i = 0; i < testRanks.length; i++) {
					sBuffer = (i == (testRanks.length - 1)) ? sBuffer
							.append(testRanks[i]) : sBuffer.append(testRanks[i]
							+ ",");
				}
				customLogReportTask.setTestRank(sBuffer.toString());
			}
			if (null != statisticeTaskRequest.getEndDate()) {
				customLogReportTask.setEndDate(statisticeTaskRequest.getEndDate());
			}
			customLogReportTask.setCreatDate(new Date());
			if(StatisticeTaskRequest.typeIsAnylyFileReport(statisticeTaskRequest.getReportType())){
				customLogReportTask.setTaskStatus("1");
			}
			else {
				customLogReportTask.setTaskStatus("1");
				//customLogReportTask.setTaskStatus("2");
			}
			customLogReportTask.setTaskType("1");
			if (null != statisticeTaskRequest.getId()) {
				customLogReportTask.setId(statisticeTaskRequest.getId());
				customLogReportTaskDao.update(customLogReportTask);
			} else {
				session.setAttribute("idLong", null);
				customLogReportTaskDao.create(customLogReportTask);
			}

			PageList pageList = new PageList();
			pageList.putParam("workOrderId", customLogReportTask.getName());

			session.setAttribute("idLong", customLogReportTask.getId());

			//xxxxxxxxxxxxx
			if(StatisticeTaskRequest.typeIsAnylyFileReport(statisticeTaskRequest.getReportType())){
				// 线程执行
				File file1 = new File(fileSaveUrl+ "/" + ANALYZE_TEMPLATE_PATH + "/");
				if (!file1.exists()) {
					try{
						file1.mkdirs();
					}catch (Exception e){
						LOGGER.error("无法创建目录",e);
						ActionContext.getContext().getValueStack().set("errorMsg", "无法创建目录"+ file1.getAbsolutePath());
						return ReturnType.JSON;
					}
				}

				// 日志文件
//				UnicomLogItemPageQueryRequestBean pageParams = new UnicomLogItemPageQueryRequestBean();
//				PageList unicomLogPageList = new PageList();
//				unicomLogPageList.putParam("pageQueryBean", pageParams);
//				AbstractPageList abstractPageList = unicomLogItemService.pageList(unicomLogPageList);
//				List<UnicomLogItem> unicomLogItemList = abstractPageList.getRows();
				List<String> unicomLogItemIdList = new ArrayList<>();
				for(TestLogItem u:queryTestLogItems) {
					Long recSeqNo = u.getRecSeqNo();
					if (recSeqNo == null) continue;
					unicomLogItemIdList.add(recSeqNo.toString());
				}

				Session hibernateSession = analyFileReportDao.getSessionFactory().openSession();

				analyzeTaskExecutor.submit(new Thread(new Runnable() {
					@Override
					public void run() {

						AnalyzeEventTemplate analyzeEventTemplate = new AnalyzeEventTemplate();
						AnalyzeNetworkTemplate analyzeNetworkTemplate = new AnalyzeNetworkTemplate();
						AnalyzeVoiceTemplate analyzeVoiceTemplate = new AnalyzeVoiceTemplate();
						QuesRoadTemplate quesRoadTemplate = new QuesRoadTemplate();

						List<String> templateIds = Arrays.asList(customLogReportTask.getTemplateIds().split(","));
						Boolean result = true;
						if(templateIds.contains(analyzeNetworkTemplate.getId())){
							result = result && createAnalyFile(analyzeNetworkTemplate, unicomLogItemIdList, customLogReportTask,hibernateSession);
						}
						if(templateIds.contains(analyzeEventTemplate.getId())){
							result = result && createAnalyFile(analyzeEventTemplate, unicomLogItemIdList, customLogReportTask,hibernateSession);
						}

						if(templateIds.contains(analyzeVoiceTemplate.getId())){
							result = result && createAnalyFile(analyzeVoiceTemplate, unicomLogItemIdList, customLogReportTask,hibernateSession);
						}
						if(templateIds.contains(quesRoadTemplate.getId())){
							result = result && createAnalyFile(quesRoadTemplate, unicomLogItemIdList, customLogReportTask,hibernateSession);
						}
						customLogReportTask.setTaskStatus("3");
						hibernateSession.merge(customLogReportTask);
						hibernateSession.flush();
						hibernateSession.close();
					}
				}));
				return ReturnType.JSON;
			}

			// wuchch
			//else if(!StatisticeTaskRequest.typeIsAnylyFileReport(statisticeTaskRequest.getReportType())){
			else{
				File file1 = new File(customFileSaveUrl+ "/" + STATISTICE_TEMPLATE_PATH + "/");
				if (!file1.exists()) {
					try{
						file1.mkdirs();
					}catch (Exception e){
						LOGGER.error("无法创建目录",e);
						ActionContext.getContext().getValueStack().set("errorMsg", "无法创建目录"+ file1.getAbsolutePath());
						return ReturnType.JSON;
					}
				}

				List<Long> idList = new ArrayList<>();
				for(TestLogItem u:queryTestLogItems) {
					Long recSeqNo = u.getRecSeqNo();
					idList.add(recSeqNo);
				}

				Session hibernateSession = analyFileReportDao.getSessionFactory().openSession();

				analyzeTaskExecutor.submit(new Thread(new Runnable() {
					@Override
					public void run() {

						String[] ids = customLogReportTask.getTemplateIds().split(",");
						List<Long> idList = new ArrayList<>();
						for (String id : ids) {
							idList.add(Long.valueOf(id));
						}
						PageList pageList = new PageList();
						pageList.putParam("ids",idList);
						List<CustomReportTemplatePojo> queryTemplateByParam = customTemplateService.queryTemplateByParam(pageList);
						Set<String> unionTeplateSet = new HashSet<String>();
						for (CustomReportTemplatePojo customReportTemplatePojo : queryTemplateByParam) {
							unionTeplateSet.add(customReportTemplatePojo.getTemplateName());
						}

						boolean flag =true;

							for (String templateName : unionTeplateSet) {


								try{

									LOGGER.info(templateName + " 开始： " + System.currentTimeMillis() );

									//查询模板
									PageList selectConditions = new PageList();
									selectConditions.putParam("templateName", templateName);
									List<CustomReportTemplatePojo> templateList = customTemplateService.queryTemplateByParam(selectConditions);

									File file = new File(templateList.get(0).getSaveFilePath());
									if (file == null || !file.exists() || file.isDirectory()) {
										throw new ApplicationException("模板文件不存在");
									}
									String newExcelname = file.getName().substring(0, file.getName().lastIndexOf(".")) + "_"+customLogReportTask.getId()
											+ file.getName().substring(file.getName().lastIndexOf("."));
									String newPath = file.getParentFile().getAbsolutePath() + "/" + newExcelname;


									Map<String, Object> map = new HashMap<>();
									String idsString = customLogReportTask.getLogIds();

									PageList page = new PageList();
									page.putParam("taskName", customLogReportTask.getName());
									page.putParam("logids", idsString);
									page.putParam("templateName", templateName);
									page.putParam("excelPath",templateList.get(0).getSaveFilePath());
									page.putParam("exportFilePath", newPath);
									//根据指标公式修改获取值保存到excel

									LOGGER.info(templateName + " 生成文件： " + System.currentTimeMillis() );

									customTemplateService.modifyExcelValue(page);


									LOGGER.info(templateName + " 结束： " + System.currentTimeMillis() );


								}catch(Exception e){
									flag = false;
									LOGGER.info("====统计失败:"+templateName);
									LOGGER.info(e.getMessage(),e);
								}

							}
							if(flag){
								customLogReportTask.setTaskStatus("3");
							}else{
								customLogReportTask.setTaskStatus("-1");
							}
							hibernateSession.merge(customLogReportTask);
							hibernateSession.flush();
							hibernateSession.close();
					}
				}));
				return ReturnType.JSON;

			}

			//下发任务到后台
			//String errotMsg = goUnicomSocket(customLogReportTask);
			//if(StringUtils.hasText(errotMsg)){
			//	customLogReportTaskDao.delete(customLogReportTask.getId());
			//	LOGGER.info("下发自定义任务失败,无法保存任务:"+ errotMsg);
			//	ActionContext.getContext().getValueStack().set("errorMsg", "下发自定义任务失败,无法保存任务："+ errotMsg);
			//}


		}
		return ReturnType.JSON;
	}


	/**
	 * excel 文件生成
	 * */
	private boolean createAnalyFile(AnalyzeTemplate analyzeTemplate,List<String> unicomLogItemIdList,CustomLogReportTask customLogReportTask,Session session){

		String taskName = analyzeTemplate.getTemplateFileName() + "-" + UUID.randomUUID().toString().replaceAll("-", "")
				.toUpperCase().trim();
		File file = new File(fileSaveUrl + "/" + ANALYZE_TEMPLATE_PATH + "/" + taskName + analyzeTemplate.getTemplateSuffix());
		String filePath = fileSaveUrl + "/" + ANALYZE_TEMPLATE_PATH + "/" + taskName + analyzeTemplate.getTemplateSuffix();
		Map<String, Collection> hashMap1=null;
		if(analyzeTemplate instanceof QuesRoadTemplate){
			hashMap1 = analyzeTemplate.getData(quesRoadService, unicomLogItemIdList);
		}else{
			hashMap1 = analyzeTemplate.getData(influxService, unicomLogItemIdList);
		}
		AnalyFileReport report = new AnalyFileReport();

		report.setTaskId(customLogReportTask.getId());
		report.setReportId(analyzeTemplate.getId());

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			Workbook transformToExcel = POIExcelUtil.transformToExcel(
					hashMap1, analyzeTemplate.getTemplateInputStream(influxService));
			if (null != transformToExcel) {
				transformToExcel.write(fileOutputStream);
				report.setFilePath(filePath);
				session.save(report);
				session.flush();
				return true;
			}

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			report.setDescription("失败");
		}
		session.save(report);
		session.flush();
		return false;
	}



	/**
	 * 添加或修改统计任务
	 *
	 * @return
	 */
	public String addReportTask() {
		if (null != statisticeTaskRequest) {
			StatisticeTask statisticeTask = new StatisticeTask();
			if (StringUtils.hasText(statisticeTaskRequest.getCreaterName())) {
				statisticeTask.setCreaterName(statisticeTaskRequest.getCreaterName());
			}else{
				String userName = (String) SecurityUtils.getSubject()
						.getPrincipal();
				if (null != userName) {
					statisticeTask.setCreaterName(userName.trim());
				}
			}
			StringBuilder taskName = new StringBuilder();
			StringBuilder terminalGroupNames = new StringBuilder();
			if (null != statisticeTaskRequest.getBoxIds()) {
				statisticeTask.setBoxIds(statisticeTaskRequest.getBoxIds());
			}
			if (null != statisticeTaskRequest.getLogIds()) {
				statisticeTask.setLogIds(statisticeTaskRequest.getLogIds());
			}
			if (null != statisticeTaskRequest.getTemplateIds()) {
				statisticeTask.setTemplateIds(statisticeTaskRequest.getTemplateIds());
			}
			List<TestLogItem> queryTestLogItems = testLogItemService
					.queryTestLogItems(statisticeTaskRequest.getLogIds());
			StringBuilder allLogIDBuilder = new StringBuilder();
			StringBuilder allLogNameBuilder = new StringBuilder();
			StringBuilder moveLogIDBuilder = new StringBuilder();
			StringBuilder linkLogIDBuilder = new StringBuilder();
			StringBuilder telecomLogIDBuilder = new StringBuilder();
			// StringBuilder nbiotLogIDBuilder = new StringBuilder();
			for (TestLogItem testLogItem : queryTestLogItems) {
				if (0 != allLogIDBuilder.toString().length()) {
					allLogIDBuilder.append(","
							+ testLogItem.getRecSeqNo());
					allLogNameBuilder.append(","
							+ testLogItem.getFileName());
				} else {
					allLogIDBuilder.append(testLogItem.getRecSeqNo());
					allLogNameBuilder.append(testLogItem.getFileName());
				}
				if (null != testLogItem.getOperatorName()) {
					if (testLogItem.getOperatorName().trim().equals("中国移动")||testLogItem.getOperatorName().trim().equals("China Mobile")) {
						if (0 != moveLogIDBuilder.toString().length()) {
							moveLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							moveLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
						// if (testLogItem.getBoxId() != null) {
						// Terminal terminal = trminalService
						// .getTerminal(testLogItem.getBoxId());
						// if (terminal != null) {
						// List<TestModule> testModuls = terminal
						// .getTestModuls();
						// if (testModuls != null
						// && testModuls.size() != 0) {
						// for (TestModule testModule : testModuls) {
						// if (testModule.getModuleType().equals(
						// ChannelType.NB_IoT)) {
						// if (0 != nbiotLogIDBuilder
						// .toString().length()) {
						// nbiotLogIDBuilder.append(","
						// + testLogItem
						// .getRecSeqNo());
						// } else {
						// nbiotLogIDBuilder
						// .append(testLogItem
						// .getRecSeqNo());
						// }
						// }
						// }
						// }
						// }
						// }
					} else if (testLogItem.getOperatorName().trim()
							.equals("中国联通")) {
						if (0 != linkLogIDBuilder.toString().length()) {
							linkLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							linkLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("中国电信")) {
						if (0 != telecomLogIDBuilder.toString().length()) {
							telecomLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							telecomLogIDBuilder.append(testLogItem
									.getRecSeqNo());
						}
					}
				}
			}
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("allTestLogItemIds",
					allLogIDBuilder.toString());
			session.setAttribute("allLogNames",
					allLogNameBuilder.toString());
			session.setAttribute("moveTestLogItemIds",
					moveLogIDBuilder.toString());
			session.setAttribute("linkTestLogItemIds",
					linkLogIDBuilder.toString());
			session.setAttribute("telecomTestLogItemIds",
					telecomLogIDBuilder.toString());
			// session.setAttribute("nbiotTestLogItemIds",
			// nbiotLogIDBuilder.toString());
			if (statisticeTaskRequest.getCityIds() != null) {
				String[] cityIds = statisticeTaskRequest.getCityIds().trim()
						.split(",");
				// 存储TestLogItem的id集合
				List<String> ids = new ArrayList<>();
				for (int i = 0; i < cityIds.length; i++) {
					if (StringUtils.hasText(cityIds[i])) {
						try {
							ids.add(cityIds[i].trim());
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				if (ids.size() > 1) {
					HashSet<String> nameSet = new HashSet<String>();
					for (String cityId : ids) {
						if (cityId == null) {
							continue;
						} else {
							Long id = Long.valueOf(cityId);
							String name = terminalGroupService
									.getProvinceNameByCityGroup(terminalGroupService
											.findGroupById(id));
							nameSet.add(name);
						}

					}
					if (nameSet.size() > 1) {
						terminalGroupNames.append("全国");
					} else {
						terminalGroupNames
								.append((String) nameSet.toArray()[0]);
					}
				} else {
					Long id = Long.valueOf(ids.get(0));
					TerminalGroup terminalGroup = terminalGroupService
							.findGroupById(id);
					if (terminalGroup != null) {
						terminalGroupNames.append(terminalGroup.getName());
					}
				}
				if (taskName.length() >= 1) {
					taskName.deleteCharAt(taskName.length() - 1);
				}
				if (terminalGroupNames.length() > 0) {
					taskName.append(terminalGroupNames.toString().trim());
				}
				taskName.append('-');
				taskName.append(DateUtil.getCurDateStr(new Date()));
//				taskName.append(DateUtil
//						.getShortDateTimeStr(statisticeTaskRequest
//								.getBeginDate()));
//				taskName.append('-');
//				taskName.append(DateUtil
//						.getShortDateTimeStr(statisticeTaskRequest.getEndDate()));
				if(!statisticeTaskRequest.getName().contains("定点测试-DD")){
					statisticeTask.setName(taskName.toString().trim());
				}else{
					statisticeTask.setName(statisticeTaskRequest.getName());
				}
				session.setAttribute("taskName", taskName.toString());
				statisticeTask.setCityIds(statisticeTaskRequest.getCityIds());

			}
			if (null != statisticeTaskRequest.getBeginDate()) {
				statisticeTask.setBeginDate(statisticeTaskRequest
						.getBeginDate());
			}
			if (null != collectTypes) {
				StringBuffer sBuffer = new StringBuffer();
				for (int i = 0; i < collectTypes.length; i++) {
					sBuffer = (i == (collectTypes.length - 1)) ? sBuffer
							.append(collectTypes[i]) : sBuffer
							.append(collectTypes[i] + ",");
				}
				statisticeTask.setCollectType(sBuffer.toString());
			}
			if (null != testRanks) {
				StringBuffer sBuffer = new StringBuffer();
				for (int i = 0; i < testRanks.length; i++) {
					sBuffer = (i == (testRanks.length - 1)) ? sBuffer
							.append(testRanks[i]) : sBuffer.append(testRanks[i]
							+ ",");
				}
				statisticeTask.setTestRank(sBuffer.toString());
			}
			if (null != statisticeTaskRequest.getEndDate()) {
				statisticeTask.setEndDate(statisticeTaskRequest.getEndDate());
			}
			statisticeTask.setCreatDate(new Date());
			if(StatisticeTaskRequest.typeIsAnylyFileReport(statisticeTaskRequest.getReportType())) {
				statisticeTask.setTaskStatus("1");
			}
			else{
				statisticeTask.setTaskStatus("2");
			}
			if (null != statisticeTaskRequest.getId()) {
				statisticeTask.setId(statisticeTaskRequest.getId());
				reportService.update(statisticeTask);
			} else {
				session.setAttribute("idLong", null);
				reportService.save(statisticeTask);
			}
			PageList pageList = new PageList();
			pageList.putParam("workOrderId", statisticeTask.getName());
			List<StatisticeTask> statisticeTaskList = reportService.findStatisticeTask(pageList);
			session.setAttribute("idLong", statisticeTaskList.get(0).getId());
			if(statisticeTaskList!=null && statisticeTaskList.size()==1) {
				//下发任务到后台
				String errotMsg = goSocket(statisticeTaskList.get(0));
				if(StringUtils.hasText(errotMsg)){
					// 下发任务失败，删除刚刚保存的任务
					List<Long> idss = new ArrayList<>();
					idss.add(statisticeTaskList.get(0).getId());
					reportService.delete(idss);

					LOGGER.info("下发自定义任务失败,无法保存任务:"+ errotMsg);
					ActionContext.getContext().getValueStack().set("errorMsg", "下发自定义任务失败,无法保存任务："+ errotMsg);
				}
			}

		}
		return ReturnType.JSON;
	}

	/**
	 * 任务下发
	 * @author lucheng
	 * @date 2020年12月28日 下午1:44:53
	 * @param statisticeTask
	 * @return
	 */
	public String goSocket(StatisticeTask statisticeTask){
		if (statisticeTask != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", statisticeTask.getId());
			map.put("taskName", statisticeTask.getName());
			List<String> logidList = Arrays.asList(statisticeTask.getLogIds().split(","));
			map.put("logIds", logidList);
			String[] templateIdArry = statisticeTask.getTemplateIds().split(",");
			Set<String> kpiSet = new HashSet<String>();
			for (String idStr : templateIdArry) {
				CustomReportTemplatePojo pojo = customTemplateService.find(Long.valueOf(idStr));
				if(StringUtils.hasText(pojo.getConmmonKpiNameSum())){
					List<String> kpis = Arrays.asList(pojo.getConmmonKpiNameSum().split(","));
					kpiSet.addAll(kpis);

				}
			}
			List<String> kpiList = new ArrayList(kpiSet);
			map.put("kpiNames", kpiList);

			JSONObject requJson = new JSONObject();
			requJson.put("importCustomTeplate", map);
			String request = requJson.toString();
			System.out.println(request);
			Socket socket = ZMQUtils.getZMQSocket();
			try {
				socket.setReceiveTimeOut(20000);
				socket.connect("tcp://" + taskIp + ":" + taskPort); // 与response端建立连接
				System.out.println("tcp://" + taskIp + ":" + taskPort);
				socket.send(request.getBytes()); // 向reponse端发送数据
				byte[] responseBytes = socket.recv(); // 接收response发送回来的数据
				if (null == responseBytes || responseBytes.length==0) {
					throw new ApplicationException("后台通信异常!");
				} else {
					ZMQUtils.releaseZMQSocket(socket);
					String result = null;
					String response = new String(responseBytes, "UTF8");
					JSONObject respJson = JSONObject.fromObject(response);
					if (null != respJson) {
						result = respJson.getString("Result");
						if(result.equals("Success")){
							LOGGER.info("成功发送任务");
							return null;
						}else if(result.equals("Failure")){
							LOGGER.info("发送任务失败");
							throw new ApplicationException("后台返回结果异常!");
						}

					}else {
						throw new ApplicationException("后台返回结果异常!");
					}
				}
			} catch (Exception e) {
				ZMQUtils.releaseSocketException(socket);
				e.printStackTrace();
				if (e instanceof ApplicationException) {
					LOGGER.info(e.getMessage());
					return e.getMessage();
				} else {
					LOGGER.info("后台通信异常!");
					return "通信失败:"+e.getMessage();
				}
			}
		}
		return "发送任务失败";
	}


	/**
	 * 任务下发
	 * @author lucheng
	 * @date 2020年12月28日 下午1:44:53
	 * @param statisticeTask
	 * @return
	 */
	public String goUnicomSocket(CustomLogReportTask customLogReportTask){
		if (customLogReportTask != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", customLogReportTask.getId());
			map.put("taskName", customLogReportTask.getName());
			List<String> logidList = Arrays.asList(customLogReportTask.getLogIds().split(","));
			map.put("logIds", logidList);
			List<String> templateIds = Arrays.asList(customLogReportTask.getTemplateIds().split(","));
			map.put("templateIds", templateIds);
//			Set<String> kpiSet = new HashSet<String>();
//			for (String idStr : templateIdArry) {
//				CustomReportTemplatePojo pojo = customTemplateService.find(Long.valueOf(idStr));
//				if(StringUtils.hasText(pojo.getConmmonKpiNameSum())){
//					List<String> kpis = Arrays.asList(pojo.getConmmonKpiNameSum().split(","));
//					kpiSet.addAll(kpis);
//
//				}
//			}
//			List<String> kpiList = new ArrayList(kpiSet);
//			map.put("kpiNames", kpiList);

			JSONObject requJson = new JSONObject();
			requJson.put("importCustomTeplate", map);
			String request = requJson.toString();
			System.out.println(request);
			Socket socket = ZMQUtils.getZMQSocket();
			try {
				socket.setReceiveTimeOut(20000);
				socket.connect("tcp://" + taskIp + ":" + taskPort); // 与response端建立连接
				System.out.println("tcp://" + taskIp + ":" + taskPort);
				socket.send(request.getBytes()); // 向reponse端发送数据
				byte[] responseBytes = socket.recv(); // 接收response发送回来的数据
				if (null == responseBytes || responseBytes.length==0) {
					throw new ApplicationException("后台通信异常!");
				} else {
					ZMQUtils.releaseZMQSocket(socket);
					String result = null;

					try{
						String response = new String(responseBytes, "UTF8");
						JSONObject jsonObject = JSONObject.fromObject(response);
						JSONObject respJson = (JSONObject)jsonObject.get("importCustomTeplate");
						if (null != respJson) {
							result = respJson.getString("result");
							if(result.equals("0")){
								LOGGER.info("成功发送任务");
								return null;
							}else if(result.equals("-1")){
								LOGGER.info("发送任务失败");
								throw new ApplicationException("后台返回结果异常!");
							}

						}else {
							throw new ApplicationException("后台返回结果异常!");
						}
					}catch (Exception e){
						throw new ApplicationException("后台返回结果异常!");
					}

				}
			} catch (Exception e) {
				ZMQUtils.releaseSocketException(socket);
				e.printStackTrace();
				if (e instanceof ApplicationException) {
					LOGGER.info(e.getMessage());
					return e.getMessage();
				} else {
					LOGGER.info("后台通信异常!");
					return "通信失败:"+e.getMessage();
				}
			}
		}
		return "发送任务失败";
	}




	/**
	 * 查看统计任务
	 *
	 * @return
	 */
	public String seeReportTask() {
		if (null != statisticeTaskRequest) {

			StatisticeTask statisticeTask = reportService.queryOneByID(statisticeTaskRequest.getId());

			List<TestLogItem> queryTestLogItems = testLogItemService
					.queryTestLogItems(statisticeTask.getLogIds());
			StringBuilder allLogIDBuilder = new StringBuilder();
			StringBuilder allLogNameBuilder = new StringBuilder();
			StringBuilder moveLogIDBuilder = new StringBuilder();
			StringBuilder linkLogIDBuilder = new StringBuilder();
			StringBuilder telecomLogIDBuilder = new StringBuilder();
			// StringBuilder nbiotLogIDBuilder = new StringBuilder();
			for (TestLogItem testLogItem : queryTestLogItems) {
				if (0 != allLogIDBuilder.toString().length()) {
					allLogIDBuilder.append(","
							+ testLogItem.getRecSeqNo());
					allLogNameBuilder.append(","
							+ testLogItem.getFileName());
				} else {
					allLogIDBuilder.append(testLogItem.getRecSeqNo());
					allLogNameBuilder.append(testLogItem.getFileName());
				}
				if (null != testLogItem.getOperatorName()) {
					if (testLogItem.getOperatorName().trim().equals("中国移动")||testLogItem.getOperatorName().trim().equals("China Mobile")) {
						if (0 != moveLogIDBuilder.toString().length()) {
							moveLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							moveLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("中国联通")) {
						if (0 != linkLogIDBuilder.toString().length()) {
							linkLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							linkLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("中国电信")) {
						if (0 != telecomLogIDBuilder.toString().length()) {
							telecomLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							telecomLogIDBuilder.append(testLogItem
									.getRecSeqNo());
						}
					}
				}
			}
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("allTestLogItemIds",
					allLogIDBuilder.toString());
			session.setAttribute("allLogNames",
					allLogNameBuilder.toString());
			session.setAttribute("moveTestLogItemIds",
					moveLogIDBuilder.toString());
			session.setAttribute("linkTestLogItemIds",
					linkLogIDBuilder.toString());
			session.setAttribute("telecomTestLogItemIds",
					telecomLogIDBuilder.toString());
			// session.setAttribute("nbiotTestLogItemIds",
			// nbiotLogIDBuilder.toString());
			session.setAttribute("taskName", statisticeTask.getName());
			session.setAttribute("idLong", statisticeTask.getId());

		}
		return ReturnType.JSON;
	}

	/**
	 * 删除多个统计任务
	 *
	 * @return
	 * @throws Exception
	 */
	public String delReport() {
		if (null != ids) {
			String[] logIds = ids.trim().split(",");
			// 存储TestLogItem的id集合
			List<Long> idss = new ArrayList<>();
			for (int i = 0; i < logIds.length; i++) {
				if (StringUtils.hasText(logIds[i])) {
					try {
						idss.add(Long.parseLong(logIds[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
			reportService.delete(idss);
		}

		return ReturnType.JSON;
	}

	/**
	 * 查询异常事件报表信息
	 */
	public String quaryEeKpi() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					// if (testLogItem.getServiceType() != null) {
					// if (testLogItem.getServiceType().indexOf("0,") != -1||
					// testLogItem.getServiceType().indexOf("1,") != -1) {
					listTestLogItem.add(testLogItem);
					if (idsString == null) {
						idsString = String.valueOf(testLogItem.getRecSeqNo());
					} else {
						idsString = idsString + ","
								+ String.valueOf(testLogItem.getRecSeqNo());
					}
					// }
					// }
				}
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G_EE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);

				// '接入类异常事件'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_INSERT);
				AbstractPageList insertKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("insertKpi", insertKpi);
				List insertKpirows = insertKpi.getRows();
				if (null != insertKpirows && 0 != insertKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, insertKpirows);
				}
				// '移动类异常事件'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_MOVE);
				AbstractPageList moverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("moverKpi", moverKpi);
				List moverKpirows = moverKpi.getRows();
				if (null != moverKpirows && 0 != moverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, moverKpirows);
				}
				// '语音业务（4G）异常事件统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_VOICE_EE);
				AbstractPageList voiceEeKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("voiceEeKpi", voiceEeKpi);
				List voiceEeKpirows = voiceEeKpi.getRows();
				if (null != voiceEeKpirows && 0 != voiceEeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, voiceEeKpirows);
				}

				// '会话类异常事件'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_VOICE_PDU);
				AbstractPageList pduKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("pduKpi", pduKpi);
				List pduKpirows = pduKpi.getRows();
				if (null != pduKpirows && 0 != pduKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, pduKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("insertKpi", new EasyuiPageList());
			map.put("moverKpi", new EasyuiPageList());
			map.put("voiceEeKpi", new EasyuiPageList());
			map.put("pduKpi", new EasyuiPageList());
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * 查询5G数据业务报表信息
	 */
	public String quaryDataKpi() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					// if (testLogItem.getServiceType() != null) {
					// if (testLogItem.getServiceType().indexOf("0,") == -1
					// &&testLogItem.getServiceType().indexOf("1,") == -1 &&
					// testLogItem.getServiceType().indexOf("2,") == -1) {
					listTestLogItem.add(testLogItem);
					if (idsString == null) {
						idsString = String.valueOf(testLogItem.getRecSeqNo());
					} else {
						idsString = idsString + ","
								+ String.valueOf(testLogItem.getRecSeqNo());
					}
					// }
					// }
				}
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G_DATA);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '覆盖类指标统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER_5G);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '干扰类指标统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB_5G);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '分段占比统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.SUB_INDEX_PROPORTION);
				AbstractPageList subIndexKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("subIndexKpi", subIndexKpi);
				List subIndexKpirows = subIndexKpi.getRows();
				if (null != subIndexKpirows && 0 != subIndexKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, subIndexKpirows);
				}
				// '数据业务（5G）统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_EE);
				AbstractPageList eeKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("eeKpi", eeKpi);
				List eeKpirows = eeKpi.getRows();
				if (null != eeKpirows && 0 != eeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, eeKpirows);
				}
				// '波束类统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_BEAM);
				AbstractPageList beamKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("beamKpi", beamKpi);
				List beamKpirows = beamKpi.getRows();
				if (null != beamKpirows && 0 != beamKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, beamKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("subIndexKpi", new EasyuiPageList());
			map.put("eeKpi", new EasyuiPageList());
			map.put("beamKpi", new EasyuiPageList());
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * 查询NSA指标报表信息
	 */
	public String quaryNsaKpi() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					// if (testLogItem.getServiceType() != null) {
					// if (testLogItem.getServiceType().indexOf("0,") == -1
					// &&testLogItem.getServiceType().indexOf("1,") == -1 &&
					// testLogItem.getServiceType().indexOf("2,") == -1) {
					listTestLogItem.add(testLogItem);
					if (idsString == null) {
						idsString = String.valueOf(testLogItem.getRecSeqNo());
					} else {
						idsString = idsString + ","
								+ String.valueOf(testLogItem.getRecSeqNo());
					}
					// }
					// }
				}
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_NSA_INDEX);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '指标汇总'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX_SUMMARY);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("summaryKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '业务类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_BUSINESS);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("businessKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '覆盖类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList subIndexKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", subIndexKpi);
				List subIndexKpirows = subIndexKpi.getRows();
				if (null != subIndexKpirows && 0 != subIndexKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, subIndexKpirows);
				}
				// '移动类（5G）统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
				AbstractPageList eeKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("mobileKpi", eeKpi);
				List eeKpirows = eeKpi.getRows();
				if (null != eeKpirows && 0 != eeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, eeKpirows);
				}
				// '接入类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
				AbstractPageList beamKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("accessKpi", beamKpi);
				List beamKpirows = beamKpi.getRows();
				if (null != beamKpirows && 0 != beamKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, beamKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("summaryKpi", new EasyuiPageList());
			map.put("businessKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("mobileKpi", new EasyuiPageList());
			map.put("accessKpi", new EasyuiPageList());
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * 查询测试轨迹报表信息
	 */
	public String quaryTrailKpi() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("allTestLogItemIds");
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					// if (testLogItem.getServiceType() != null) {
					// if (testLogItem.getServiceType().indexOf("0,") == -1
					// &&testLogItem.getServiceType().indexOf("1,") == -1 &&
					// testLogItem.getServiceType().indexOf("2,") == -1) {
					listTestLogItem.add(testLogItem);
					if (idsString == null) {
						idsString = String.valueOf(testLogItem.getRecSeqNo());
					} else {
						idsString = idsString + ","
								+ String.valueOf(testLogItem.getRecSeqNo());
					}
					// }
					// }
				}
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				// 测试轨迹报表
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_TEST_TRAIL);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'RSRP指标统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_RSRP_TRAIL);
				AbstractPageList rsrpKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("rsrpKpi", rsrpKpi);
				List rsrpKpirows = rsrpKpi.getRows();
				if (null != rsrpKpirows && 0 != rsrpKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, rsrpKpirows);
				}
				// 'SINR指标统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_SINR_TRAIL);
				AbstractPageList sinrKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("sinrKpi", sinrKpi);
				List sinrKpirows = sinrKpi.getRows();
				if (null != sinrKpirows && 0 != sinrKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, sinrKpirows);
				}
				// 'FTP下行速率统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTPDL_TRAIL);
				AbstractPageList ftpDLRateKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("ftpDLRateKpi", ftpDLRateKpi);
				List ftpDLRateKpirows = ftpDLRateKpi.getRows();
				if (null != ftpDLRateKpirows && 0 != ftpDLRateKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, ftpDLRateKpirows);
				}
				// 'FTP上行速率指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTPUL_TRAIL);
				AbstractPageList ftpULRateKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("ftpULRateKpi", ftpULRateKpi);
				List ftpULRateKpirows = ftpULRateKpi.getRows();
				if (null != ftpULRateKpirows && 0 != ftpULRateKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, ftpULRateKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("rsrpKpi", new EasyuiPageList());
			map.put("sinrKpi", new EasyuiPageList());
			map.put("ftpDLRateKpi", new EasyuiPageList());
			map.put("ftpULRateKpi", new EasyuiPageList());
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * NSA指标报表导出
	 *
	 * @return
	 */
	public String downloadNsaTotal() {
		return "downloadNsaTotal";
	}

	/**
	 * NSA指标报表导出
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadNsaTotal() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					// if (testLogItem.getServiceType() != null) {
					// if (testLogItem.getServiceType().indexOf("0,") == -1
					// && testLogItem.getServiceType().indexOf("1,") == -1
					// && testLogItem.getServiceType().indexOf("2,") == -1) {
					listTestLogItem.add(testLogItem);
					if (idsString == null) {
						idsString = String.valueOf(testLogItem.getRecSeqNo());
					} else {
						idsString = idsString + ","
								+ String.valueOf(testLogItem.getRecSeqNo());
					}
					// }
					// }
				}

				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_NSA_INDEX);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '指标汇总'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX_SUMMARY);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("summaryKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '业务类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_BUSINESS);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("businessKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '覆盖类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList subIndexKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", subIndexKpi);
				List subIndexKpirows = subIndexKpi.getRows();
				if (null != subIndexKpirows && 0 != subIndexKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, subIndexKpirows);
				}
				// '移动类（5G）统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
				AbstractPageList eeKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("mobileKpi", eeKpi);
				List eeKpirows = eeKpi.getRows();
				if (null != eeKpirows && 0 != eeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, eeKpirows);
				}
				// '接入类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
				AbstractPageList beamKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("accessKpi", beamKpi);
				List beamKpirows = beamKpi.getRows();
				if (null != beamKpirows && 0 != beamKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, beamKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("summaryKpi", new EasyuiPageList());
			map.put("businessKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("mobileKpi", new EasyuiPageList());
			map.put("accessKpi", new EasyuiPageList());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/NSA指标报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_NSA指标报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * NSA指标报表单个Sheet报表导出
	 *
	 * @return
	 */
	public String downloadOneSheetNsaTotal() {
		return "downloadOneSheetNsaTotal";
	}

	/**
	 * NSA指标报表单个Sheet报表导出
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetNsaTotal() {
		if (null != sheetName) {
			Integer typeNo = (Integer) ServletActionContext.getRequest()
					.getSession().getAttribute("typeNo");
			Object attribute = null;
			if (null != typeNo) {
				if (0 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("moveTestLogItemIds");
				} else if (1 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("linkTestLogItemIds");
				} else if (2 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("telecomTestLogItemIds");
				}
			}
			Map<String, Object> map = new HashMap<>();
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
					String idsString = null;
					for (TestLogItem testLogItem : queryTestLogItems) {
						// if (testLogItem.getServiceType() != null) {
						// if (testLogItem.getServiceType().indexOf("0,") == -1
						// && testLogItem.getServiceType().indexOf(
						// "1,") == -1
						// && testLogItem.getServiceType().indexOf(
						// "2,") == -1) {
						listTestLogItem.add(testLogItem);
						if (idsString == null) {
							idsString = String.valueOf(testLogItem
									.getRecSeqNo());
						} else {
							idsString = idsString + ","
									+ String.valueOf(testLogItem.getRecSeqNo());
						}
						// }
						// }
					}
					// 汇总开始时间和结束时间
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// 汇总其他信息
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_NSA_INDEX);
					lteWholePreviewParam
							.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("summaryKpi", new EasyuiPageList());
					map.put("businessKpi", new EasyuiPageList());
					map.put("coverKpi", new EasyuiPageList());
					map.put("mobileKpi", new EasyuiPageList());
					map.put("accessKpi", new EasyuiPageList());
					// '指标汇总'sheet
					if (sheetName.equals("指标汇总")) {
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX_SUMMARY);
						AbstractPageList coverKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("summaryKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, coverKpirows);
						}
					} else if (sheetName.equals("业务类")) {
						// '业务类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_BUSINESS);
						AbstractPageList disturbKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("businessKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, disturbKpirows);
						}
					} else if (sheetName.equals("覆盖类")) {
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList subIndexKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("coverKpi", subIndexKpi);
						List subIndexKpirows = subIndexKpi.getRows();
						if (null != subIndexKpirows
								&& 0 != subIndexKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, subIndexKpirows);
						}
					} else if (sheetName.equals("移动类")) {
						// '移动类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList eeKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("mobileKpi", eeKpi);
						List eeKpirows = eeKpi.getRows();
						if (null != eeKpirows && 0 != eeKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, eeKpirows);
						}
					} else if (sheetName.equals("接入类")) {
						// '接入类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList beamKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("accessKpi", beamKpi);
						List beamKpirows = beamKpi.getRows();
						if (null != beamKpirows && 0 != beamKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, beamKpirows);
						}
					}
				}
			} else {
				if (sheetName.equals("指标汇总")) {
					map.put("summaryKpi", new EasyuiPageList());
				} else if (sheetName.equals("业务类")) {
					map.put("businessKpi", new EasyuiPageList());
				} else if (sheetName.equals("覆盖类")) {
					map.put("coverKpi", new EasyuiPageList());
				} else if (sheetName.equals("移动类")) {
					map.put("mobileKpi", new EasyuiPageList());
				} else if (sheetName.equals("接入类")) {
					map.put("accessKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/NSA指标报表.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_NSA指标报表_" + sheetName + ".xls";
				ActionContext.getContext().put("fileName",
						new String(str.getBytes(), "ISO8859-1"));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
		} else
			return null;
	}

	/**
	 * 测试轨迹指标报表导出
	 *
	 * @return
	 */
	public String downloadTestTrailTotal() {
		return "downloadTestTrailTotal";
	}

	/**
	 * NSA指标报表导出
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadTestTrailTotal() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("allTestLogItemIds");
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					// if (testLogItem.getServiceType() != null) {
					// if (testLogItem.getServiceType().indexOf("0,") == -1
					// &&testLogItem.getServiceType().indexOf("1,") == -1 &&
					// testLogItem.getServiceType().indexOf("2,") == -1) {
					listTestLogItem.add(testLogItem);
					if (idsString == null) {
						idsString = String.valueOf(testLogItem.getRecSeqNo());
					} else {
						idsString = idsString + ","
								+ String.valueOf(testLogItem.getRecSeqNo());
					}
					// }
					// }
				}
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				// 测试轨迹报表
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_TEST_TRAIL);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'RSRP指标统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_RSRP_TRAIL);
				AbstractPageList rsrpKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("rsrpKpi", rsrpKpi);
				List rsrpKpirows = rsrpKpi.getRows();
				if (null != rsrpKpirows && 0 != rsrpKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, rsrpKpirows);
				}
				// 'SINR指标统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_SINR_TRAIL);
				AbstractPageList sinrKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("sinrKpi", sinrKpi);
				List sinrKpirows = sinrKpi.getRows();
				if (null != sinrKpirows && 0 != sinrKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, sinrKpirows);
				}
				// 'FTP下行速率统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTPDL_TRAIL);
				AbstractPageList ftpDLRateKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("ftpDLRateKpi", ftpDLRateKpi);
				List ftpDLRateKpirows = ftpDLRateKpi.getRows();
				if (null != ftpDLRateKpirows && 0 != ftpDLRateKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, ftpDLRateKpirows);
				}
				// 'FTP上行速率指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTPUL_TRAIL);
				AbstractPageList ftpULRateKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("ftpULRateKpi", ftpULRateKpi);
				List ftpULRateKpirows = ftpULRateKpi.getRows();
				if (null != ftpULRateKpirows && 0 != ftpULRateKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, ftpULRateKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("rsrpKpi", new EasyuiPageList());
			map.put("sinrKpi", new EasyuiPageList());
			map.put("ftpDLRateKpi", new EasyuiPageList());
			map.put("ftpULRateKpi", new EasyuiPageList());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/测试轨迹指标报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_测试轨迹指标报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 测试轨迹指标报表单个Sheet报表导出
	 *
	 * @return
	 *//*
	public String downloadOneSheetTestTrail() {
		return "downloadOneSheetTestTrail";
	}

	*//**
	 * 测试轨迹指标报表单个Sheet报表导出
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 *//*
	public InputStream getDownloadOneSheetTestTrail() {
			Object attribute = ServletActionContext.getRequest().getSession()
					.getAttribute("allTestLogItemIds");
			Map<String, Object> map = new HashMap<>();
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
					String idsString = null;
					for (TestLogItem testLogItem : queryTestLogItems) {
						// if (testLogItem.getServiceType() != null) {
						// if (testLogItem.getServiceType().indexOf("0,") == -1
						// &&testLogItem.getServiceType().indexOf("1,") == -1 &&
						// testLogItem.getServiceType().indexOf("2,") == -1) {
						listTestLogItem.add(testLogItem);
						if (idsString == null) {
							idsString = String.valueOf(testLogItem.getRecSeqNo());
						} else {
							idsString = idsString + ","
									+ String.valueOf(testLogItem.getRecSeqNo());
						}
						// }
						// }
					}
					// 汇总开始时间和结束时间
					map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
					// 汇总其他信息
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					// 测试轨迹报表
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_TEST_TRAIL);
					lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("rsrpKpi", new EasyuiPageList());
					map.put("sinrKpi", new EasyuiPageList());
					map.put("ftpDLRateKpi", new EasyuiPageList());
					map.put("ftpULRateKpi", new EasyuiPageList());
					// 'RSRP指标统计'sheet
					if (sheetName.equals("NR RSRP")||sheetName.equals("LTE RSRP")) {
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_RSRP_TRAIL);
						AbstractPageList rsrpKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("rsrpKpi", rsrpKpi);
						List rsrpKpirows = rsrpKpi.getRows();
						if (null != rsrpKpirows && 0 != rsrpKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, rsrpKpirows);
						}
					} else if (sheetName.equals("NR SINR")||sheetName.equals("LTE SINR")) {
						// 'SINR指标统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_SINR_TRAIL);
						AbstractPageList disturbKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("sinrKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, disturbKpirows);
						}
					} else if (sheetName.equals("FTP下行速率")) {
						// 'FTP下行速率'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTPDL_TRAIL);
						AbstractPageList subIndexKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("ftpDLRateKpi", subIndexKpi);
						List subIndexKpirows = subIndexKpi.getRows();
						if (null != subIndexKpirows
								&& 0 != subIndexKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, subIndexKpirows);
						}
					} else if (sheetName.equals("FTP上行速率")) {
						// 'FTP上行速率'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTPUL_TRAIL);
						AbstractPageList eeKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("ftpULRateKpi", eeKpi);
						List eeKpirows = eeKpi.getRows();
						if (null != eeKpirows && 0 != eeKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, eeKpirows);
						}
					}
				}
			} else {
				if (sheetName.equals("NR RSRP")||sheetName.equals("LTE RSRP")) {
					map.put("rsrpKpi", new EasyuiPageList());
				} else if (sheetName.equals("NR SINR")||sheetName.equals("LTE SINR")) {
					map.put("sinrKpi", new EasyuiPageList());
				} else if (sheetName.equals("FTP下行速率")) {
					map.put("ftpDLRateKpi", new EasyuiPageList());
				} else if (sheetName.equals("FTP上行速率")) {
					map.put("ftpULRateKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/测试轨迹指标报表.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_测试轨迹指标报表_" + sheetName + ".xls";
				ActionContext.getContext().put("fileName",
						new String(str.getBytes(), "ISO8859-1"));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}*/

	/**
	 * 输出自定义模板
	 * @author lucheng
	 * @date 2020年12月30日 下午1:13:10
	 * @return
	 */
	public String exportKpiExcel() {
		try {
			Long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("idLong");
			StatisticeTask statisticeTask = reportService.queryOneByID(id);
			if(statisticeTask!=null && !statisticeTask.getTaskStatus().equals("2")){
				throw new ApplicationException("任务未解析，请等待解析完成");
			}
			String templateId = statisticeTaskRequest.getTemplateIds();
			CustomReportTemplatePojo pojo = customTemplateService.find(Long.valueOf(templateId));

			File file = new File(pojo.getSaveFilePath());
			String newExcelname = file.getName().substring(0, file.getName().lastIndexOf(".")) + "_"+statisticeTask.getId()
					+ file.getName().substring(file.getName().lastIndexOf("."));
			String newPath = file.getParentFile().getAbsolutePath() + "/" + newExcelname;

			String exportFilePath = null;
			Integer typeNo = (Integer) ServletActionContext.getRequest().getSession().getAttribute("typeNo");
			Object attribute = null;
			if (null != typeNo) {
				if (0 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("moveTestLogItemIds");
				} else if (1 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("linkTestLogItemIds");
				} else if (2 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("telecomTestLogItemIds");
				}
			}
			Map<String, Object> map = new HashMap<>();
			String idsString = null;
			if (null != attribute) {
				if (attribute instanceof String) {
					idsString = (String) attribute;
				}
			}

			if(StringUtils.hasText(pojo.getConmmonKpiNameSum())){
				PageList page = new PageList();
				page.putParam("taskName", statisticeTask.getName());
				page.putParam("logids", idsString);
				page.putParam("kpis", pojo.getConmmonKpiNameSum());
				page.putParam("excelPath",pojo.getSaveFilePath());
				page.putParam("exportFilePath", newPath);
				//根据指标公式修改获取值保存到excel
				exportFilePath = customTemplateService.modifyExcelValue(page);
			}
			List<Map<String, String>> readExcelToMap = ReadExcelToHtml.readExcelToMap(exportFilePath, true);

			ActionContext.getContext().getValueStack().set("dataList", readExcelToMap);
		} catch (ApplicationException e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "错误:"+e.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 自定义任务报表模板导出
	 *
	 * @return
	 */
	public String downloadTemplateExcelTotal() {
		return "downloadTemplateExcelTotal";
	}

	/**
	 * 自定义任务报表模板导出
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadTemplateExcel() {
		Long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("idLong");
		StatisticeTask statisticeTask = reportService.queryOneByID(id);
		if(statisticeTask!=null && !statisticeTask.getTaskStatus().equals("2")){
			throw new ApplicationException("任务未解析，请等待解析完成");
		}
		String templateId = statisticeTaskRequest.getTemplateIds();
		CustomReportTemplatePojo pojo = customTemplateService.find(Long.valueOf(templateId));

		File file = new File(pojo.getSaveFilePath());
		String newExcelname = file.getName().substring(0, file.getName().lastIndexOf(".")) + "_"+statisticeTask.getId()
				+ file.getName().substring(file.getName().lastIndexOf("."));
		String newPath = file.getParentFile().getAbsolutePath() + "/" + newExcelname;
		File exportfile = new File(newPath);

		if(exportfile!=null && exportfile.exists()){
			FileInputStream inputStream = null ;
			try {
				ActionContext.getContext().put("fileName",new String(exportfile.getName().getBytes(),"ISO8859-1"));
				inputStream = new FileInputStream(exportfile);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return inputStream;
		}
		return null;
	}

	/**
	 * 5G数据详细报表导出
	 *
	 * @return
	 */
	public String downloadFgDataTotalExcel() {
		return "downloadFgDataTotal";
	}

	/**
	 * 5G数据详细报表导出
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadFgDataTotal() {
		Integer typeNo = 0;
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					// if (testLogItem.getServiceType() != null) {
					// if (testLogItem.getServiceType().indexOf("0,") == -1
					// && testLogItem.getServiceType().indexOf("1,") == -1
					// && testLogItem.getServiceType().indexOf("2,") == -1) {
					listTestLogItem.add(testLogItem);
					if (idsString == null) {
						idsString = String.valueOf(testLogItem.getRecSeqNo());
					} else {
						idsString = idsString + ","
								+ String.valueOf(testLogItem.getRecSeqNo());
					}
					// }
					// }
				}

				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G_DATA);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '覆盖类指标统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER_5G);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '干扰类指标统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB_5G);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '分段占比统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.SUB_INDEX_PROPORTION);
				AbstractPageList subIndexKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("subIndexKpi", subIndexKpi);
				List subIndexKpirows = subIndexKpi.getRows();
				if (null != subIndexKpirows && 0 != subIndexKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, subIndexKpirows);
				}
				// '数据业务（5G）统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_EE);
				AbstractPageList eeKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("eeKpi", eeKpi);
				List eeKpirows = eeKpi.getRows();
				if (null != eeKpirows && 0 != eeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, eeKpirows);
				}
				// '波束类统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_BEAM);
				AbstractPageList beamKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("beamKpi", beamKpi);
				List beamKpirows = beamKpi.getRows();
				if (null != beamKpirows && 0 != beamKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, beamKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("subIndexKpi", new EasyuiPageList());
			map.put("eeKpi", new EasyuiPageList());
			map.put("beamKpi", new EasyuiPageList());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/5G数据业务报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_5G数据业务报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 5G异常事件报表导出
	 *
	 * @return
	 */
	public String downloadFgEeTotalExcel() {
		return "downloadFgEeTotal";
	}

	/**
	 * 5G异常事件报表导出
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadFgEeTotal() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					// if (testLogItem.getServiceType() != null) {
					// if (testLogItem.getServiceType().indexOf("0,") != -1||
					// testLogItem.getServiceType().indexOf("1,") != -1) {
					listTestLogItem.add(testLogItem);
					if (idsString == null) {
						idsString = String.valueOf(testLogItem.getRecSeqNo());
					} else {
						idsString = idsString + ","
								+ String.valueOf(testLogItem.getRecSeqNo());
					}
					// }
					// }
				}
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G_EE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);

				// '接入类异常事件'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_INSERT);
				AbstractPageList insertKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("insertKpi", insertKpi);
				List insertKpirows = insertKpi.getRows();
				if (null != insertKpirows && 0 != insertKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, insertKpirows);
				}
				// '移动类异常事件'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_MOVE);
				AbstractPageList moverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("moverKpi", moverKpi);
				List moverKpirows = moverKpi.getRows();
				if (null != moverKpirows && 0 != moverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, moverKpirows);
				}
				// '语音业务（4G）异常事件统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_VOICE_EE);
				AbstractPageList voiceEeKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("voiceEeKpi", voiceEeKpi);
				List voiceEeKpirows = voiceEeKpi.getRows();
				if (null != voiceEeKpirows && 0 != voiceEeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, voiceEeKpirows);
				}

				// '会话类异常事件'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_VOICE_PDU);
				AbstractPageList pduKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("pduKpi", pduKpi);
				List pduKpirows = pduKpi.getRows();
				if (null != pduKpirows && 0 != pduKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, pduKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("insertKpi", new EasyuiPageList());
			map.put("moverKpi", new EasyuiPageList());
			map.put("voiceEeKpi", new EasyuiPageList());
			map.put("pduKpi", new EasyuiPageList());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/5G异常事件报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_5G异常事件报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 5G数据表单个Sheet报表导出
	 *
	 * @return
	 */
	public String downloadOneSheetFgDataTotalExcel() {
		return "downloadOneSheetFgDataTotal";
	}

	/**
	 * 5G数据表单个Sheet报表导出
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetFgDataTotal() {
		if (null != sheetName) {
			Integer typeNo = (Integer) ServletActionContext.getRequest()
					.getSession().getAttribute("typeNo");
			Object attribute = null;
			if (null != typeNo) {
				if (0 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("moveTestLogItemIds");
				} else if (1 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("linkTestLogItemIds");
				} else if (2 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("telecomTestLogItemIds");
				}
			}
			Map<String, Object> map = new HashMap<>();
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
					String idsString = null;
					for (TestLogItem testLogItem : queryTestLogItems) {
						// if (testLogItem.getServiceType() != null) {
						// if (testLogItem.getServiceType().indexOf("0,") == -1
						// && testLogItem.getServiceType().indexOf(
						// "1,") == -1
						// && testLogItem.getServiceType().indexOf(
						// "2,") == -1) {
						listTestLogItem.add(testLogItem);
						if (idsString == null) {
							idsString = String.valueOf(testLogItem
									.getRecSeqNo());
						} else {
							idsString = idsString + ","
									+ String.valueOf(testLogItem.getRecSeqNo());
						}
						// }
						// }
					}
					// 汇总开始时间和结束时间
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// 汇总其他信息
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_5G_DATA);
					lteWholePreviewParam
							.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("coverKpi", new EasyuiPageList());
					map.put("disturbKpi", new EasyuiPageList());
					map.put("subIndexKpi", new EasyuiPageList());
					map.put("eeKpi", new EasyuiPageList());
					map.put("beamKpi", new EasyuiPageList());
					// '覆盖类指标统计'sheet
					if (sheetName.equals("覆盖类指标统计")) {
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER_5G);
						AbstractPageList coverKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, coverKpirows);
						}
					} else if (sheetName.equals("干扰类指标统计")) {
						// '干扰类指标统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB_5G);
						AbstractPageList disturbKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, disturbKpirows);
						}
					} else if (sheetName.equals("分段占比统计")) {
						// '分段占比统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.SUB_INDEX_PROPORTION);
						AbstractPageList subIndexKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("subIndexKpi", subIndexKpi);
						List subIndexKpirows = subIndexKpi.getRows();
						if (null != subIndexKpirows
								&& 0 != subIndexKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, subIndexKpirows);
						}
					} else if (sheetName.equals("数据业务（5G）统计指标")) {
						// '数据业务（5G）统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_EE);
						AbstractPageList eeKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("eeKpi", eeKpi);
						List eeKpirows = eeKpi.getRows();
						if (null != eeKpirows && 0 != eeKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, eeKpirows);
						}
					} else if (sheetName.equals("波束类统计指标")) {
						// '波束类统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_BEAM);
						AbstractPageList beamKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("beamKpi", beamKpi);
						List beamKpirows = beamKpi.getRows();
						if (null != beamKpirows && 0 != beamKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, beamKpirows);
						}
					}
				}
			} else {
				if (sheetName.equals("覆盖类指标统计")) {
					map.put("coverKpi", new EasyuiPageList());
				} else if (sheetName.equals("干扰类指标统计")) {
					map.put("disturbKpi", new EasyuiPageList());
				} else if (sheetName.equals("分段占比统计")) {
					map.put("subIndexKpi", new EasyuiPageList());
				} else if (sheetName.equals("数据业务（5G）统计指标")) {
					map.put("eeKpi", new EasyuiPageList());
				} else if (sheetName.equals("波束类统计指标")) {
					map.put("beamKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/5G数据业务报表.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_5G数据业务报表_" + sheetName + ".xls";
				ActionContext.getContext().put("fileName",
						new String(str.getBytes(), "ISO8859-1"));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
		} else
			return null;
	}

	/**
	 * 5G异常事件报表单个Sheet报表导出
	 *
	 * @return
	 */
	public String downloadOneSheetFgEeTotalExcel() {
		return "downloadOneSheetFgEeTotal";
	}

	/**
	 * 5G异常事件报表单个Sheet报表导出
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetFgEeTotal() {
		if (null != sheetName) {
			Integer typeNo = (Integer) ServletActionContext.getRequest()
					.getSession().getAttribute("typeNo");
			Object attribute = null;
			if (null != typeNo) {
				if (0 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("moveTestLogItemIds");
				} else if (1 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("linkTestLogItemIds");
				} else if (2 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("telecomTestLogItemIds");
				}
			}
			Map<String, Object> map = new HashMap<>();
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
					String idsString = null;
					for (TestLogItem testLogItem : queryTestLogItems) {
						// if (testLogItem.getServiceType() != null) {
						// if (testLogItem.getServiceType().indexOf("0,") != -1
						// || testLogItem.getServiceType().indexOf(
						// "1,") != -1) {
						listTestLogItem.add(testLogItem);
						if (idsString == null) {
							idsString = String.valueOf(testLogItem
									.getRecSeqNo());
						} else {
							idsString = idsString + ","
									+ String.valueOf(testLogItem.getRecSeqNo());
						}
						// }
						// }
					}

					// 汇总开始时间和结束时间
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// 汇总其他信息
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_5G_EE);
					lteWholePreviewParam
							.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("insertKpi", new EasyuiPageList());
					map.put("moverKpi", new EasyuiPageList());
					map.put("voiceEeKpi", new EasyuiPageList());
					map.put("pduKpi", new EasyuiPageList());
					if (sheetName.equals("接入类异常事件")) {
						// '接入类异常事件'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_INSERT);
						AbstractPageList insertKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, insertKpirows);
						}
					} else if (sheetName.equals("移动类异常事件")) {
						// '移动类异常事件'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_MOVE);
						AbstractPageList moverKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("moverKpi", moverKpi);
						List moverKpirows = moverKpi.getRows();
						if (null != moverKpirows && 0 != moverKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, moverKpirows);
						}
					} else if (sheetName.equals("语音业务（4G）异常事件统计指标")) {
						// '语音业务（4G）异常事件统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_VOICE_EE);
						AbstractPageList voiceEeKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("voiceEeKpi", voiceEeKpi);
						List voiceEeKpirows = voiceEeKpi.getRows();
						if (null != voiceEeKpirows
								&& 0 != voiceEeKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, voiceEeKpirows);
						}
					} else if (sheetName.equals("会话类异常事件")) {
						// '会话类异常事件'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_VOICE_PDU);
						AbstractPageList pduKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("pduKpi", pduKpi);
						List pduKpirows = pduKpi.getRows();
						if (null != pduKpirows && 0 != pduKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, pduKpirows);
						}
					}
				}
			} else {
				if (sheetName.equals("接入类异常事件")) {
					map.put("insertKpi", new EasyuiPageList());
				} else if (sheetName.equals("移动类异常事件")) {
					map.put("moverKpi", new EasyuiPageList());
				} else if (sheetName.equals("语音业务（4G）异常事件统计指标")) {
					map.put("voiceEeKpi", new EasyuiPageList());
				} else if (sheetName.equals("会话类异常事件")) {
					map.put("pduKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/5G异常事件报表.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_5G异常事件报表_" + sheetName + ".xls";
				ActionContext.getContext().put("fileName",
						new String(str.getBytes(), "ISO8859-1"));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
		} else
			return null;
	}

	/**
	 * 获取终端信息
	 *
	 * @return
	 */
	public String terminalTree() {
		String cityIds = statisticeTaskRequest.getCityIds();
		if (StringUtils.hasText(cityIds)) {
			String[] split = cityIds.split(",");
			List<String> asList = Arrays.asList(split);
			Map<Long, List<Terminal>> groupIdToTerminalMap = terminalGroupService
					.getGroupIdToTerminalMap();
			List<BoxInforRequestBean> responseBeans = new ArrayList<>();
			for (Entry<Long, List<Terminal>> entry : groupIdToTerminalMap
					.entrySet()) {
				Long key = entry.getKey();
				if (asList.contains(String.valueOf(key))) {
					List<Terminal> value = entry.getValue();
					if (null != value && 0 != value.size()) {
						for (Terminal terminal : value) {
							BoxInforRequestBean responseBean = new BoxInforRequestBean();
							responseBean.setAtuName(terminalGroupService
									.findGroupById(key).getName());
							responseBean.setTestTarget(getString(terminal
									.getTestTarget()));
							responseBean.setBoxId(terminal.getBoxId());
							responseBeans.add(responseBean);
						}
					}
				}
			}
			ActionContext.getContext().getValueStack()
					.set("boxInfo", responseBeans);
		}
		return ReturnType.JSON;
	}

	/**
	 * 获取终端类型
	 *
	 * 测试目标或者终端类型:0自动LTE ,1单模块商务终端 ,2LTE-FI
	 */
	private String getString(Integer i) {
		if (null != i) {
			if (0 == i) {
				return "自动LTE";
			} else if (1 == i) {
				return "单模块商务终端";
			} else if (2 == i) {
				return "LTE-FI";
			}
		}
		return null;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param the
	 *            ids to set
	 */

	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * @return the statisticeTaskRequest
	 */
	public StatisticeTaskRequest getStatisticeTaskRequest() {
		return statisticeTaskRequest;
	}

	/**
	 * @param the
	 *            statisticeTaskRequest to set
	 */

	public void setStatisticeTaskRequest(
			StatisticeTaskRequest statisticeTaskRequest) {
		this.statisticeTaskRequest = statisticeTaskRequest;
	}

	/**
	 * @return the id
	 */
	public Long getIdLong() {
		return idLong;
	}

	/**
	 * @param the
	 *            id to set
	 */

	public void setIdLong(Long id) {
		this.idLong = id;
	}

	/**
	 * @return the typeNo
	 */
	public Integer getTypeNo() {
		return typeNo;
	}

	/**
	 * @param the
	 *            typeNo to set
	 */

	public void setTypeNo(Integer typeNo) {
		this.typeNo = typeNo;
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param the
	 *            sheetName to set
	 */

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;

	}

	/**
	 * @return the collectTypes
	 */
	public Integer[] getCollectTypes() {
		return collectTypes;
	}

	/**
	 * @param the
	 *            collectTypes to set
	 */

	public void setCollectTypes(Integer[] collectTypes) {
		this.collectTypes = collectTypes;
	}

	/**
	 * @return the testRanks
	 */
	public Integer[] getTestRanks() {
		return testRanks;
	}

	/**
	 * @param the
	 *            testRanks to set
	 */

	public void setTestRanks(Integer[] testRanks) {
		this.testRanks = testRanks;
	}

	public String getdPage() {
		return dPage;
	}

	public void setdPage(String dPage) {
		this.dPage = dPage;
	}

	public String getAllLogNames() {
		return allLogNames;
	}

	public void setAllLogNames(String allLogNames) {
		this.allLogNames = allLogNames;
	}


}
