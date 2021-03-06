package com.datang.web.action.action5g.report;

import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
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
 * 5G????????????Action
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
	private static String ANALYZE_TEMPLATE_PATH = "???????????????";
	private static String STATISTICE_TEMPLATE_PATH = "uploadReportTemplate";
	/**
	 * ??????????????????
	 */
	@Autowired
	private IReportService reportService;
	/**
	 * ??????????????????
	 */
	@Autowired
	private CustomLogReportTaskDao customLogReportTaskDao;
	@Autowired
	private AnalyFileReportDao analyFileReportDao;
	/**
	 * ????????????
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
	 * ????????????
	 */
	@Autowired
	private UnicomLogItemService unicomLogItemService;


	/**
	 * ????????????
	 */
	@Autowired
	private TerminalService trminalService;
	/**
	 * ??????????????????
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;

	/**
	 * ???????????????????????????
	 */
	@Autowired
	private CustomTemplateService customTemplateService;

	@Value("${decode.signalling.ip}")
	private String taskIp;

	@Value("${decode.signalling.port}")
	private String taskPort;

	private String allLogNames;

	/** ??????????????????????????? */
	private ReportRequertBean reportRequertBean = new ReportRequertBean();
	/**
	 * ?????????????????????????????????
	 */
	private StatisticeTaskRequest statisticeTaskRequest = new StatisticeTaskRequest();
	private Integer[] collectTypes;
	private Integer[] testRanks;
	/**
	 * ?????????????????????????????????
	 */
	private String ids;
	/**
	 * ????????????????????????
	 */
	private Long idLong;
	/**
	 * ???????????????0:????????????1???????????????2???????????????
	 */
	private Integer typeNo;
	/**
	 * ????????????sheet???
	 */
	private String sheetName;

	/**
	 * ????????????
	 */
	private String dPage;

	private static final ExecutorService analyzeTaskExecutor = Executors.newFixedThreadPool(2);

	/**
	 * ???????????????
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
	 * ????????? list??????
	 *
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * ?????????????????????
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
	 * ?????????????????????????????????
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
	 * ???????????????????????????????????????
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
	 * ???????????????????????????????????????
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
	 * ?????????NSA????????????????????????
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
	 * ???????????????????????????
	 * @return
	 */
	public String goTraii() {
		return "testTrail";
	}

	/**
	 * ??????????????????????????????????????????
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
//					throw new ApplicationException("???????????????????????????????????????");
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
//						//??????????????????????????????????????????excel
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
//				ActionContext.getContext().getValueStack().set("errorMsg", "??????:"+e.getMessage());
//			}
		}
		return "templateExcel";
	}

	/**
	 * ????????????????????????????????????
	 * @author maxuancheng
	 * date:2020???5???15??? ??????4:16:50
	 * @return
	 */
	public String getGpsPointData(){
		//allLogNames = "5000001620200416164615_1XW-?????????ADHV_1_H_DT.l5g";
		List<StationSAMTralPojo> point = testLogItemService.getGpsPointData(allLogNames);
		ActionContext.getContext().getValueStack().set("pointList", point);
		return ReturnType.JSON;
	}


	/**
	 * ????????????boxIds??????????????? testLogItem
	 * ???????????????
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

			// ????????????
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
	 * ????????????boxIds??????????????? testLogItem
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
	 * ???????????????????????????????????? CustomReportTemplatePojo
	 * ???????????????
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
				" AND (TEMPLATE_NAME LIKE '%????????????_CQT??????%' OR TEMPLATE_NAME LIKE '%????????????_DT??????%'   OR TEMPLATE_NAME LIKE '%??????%'    OR TEMPLATE_NAME LIKE '%??????%' ) " +
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
		java.io.Reader is = clob.getCharacterStream();// ?????????
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {// ?????????????????????????????????????????????StringBuffer???StringBuffer??????STRING
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		return reString;
	}

	/**
	 * ???????????????????????????????????? CustomReportTemplatePojo
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
	 * ??????LogIDs???????????? testLogItem
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
	 * ??????templateIds??????????????????
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
	 * ?????????????????????
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
	 * ?????????????????????
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
	 * ????????????????????????????????????????????????
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
	 * ???????????????????????????
	 *
	 * @return
	 */
	public String addUnicomReportTask() {
		// ??????
//		String ltt = statisticeTaskRequest.getLogIds();
//		if(ltt!=null && StringUtils.hasText(ltt)){
//			ltt = "," + ltt;
//		}else {
//			ltt = "";
//		}
//		statisticeTaskRequest.setLogIds("2070,2080" +ltt );


		if (null != statisticeTaskRequest) {
			// ???????????????
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

			// ????????????
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
			// ????????????
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
					if (testLogItem.getOperatorName().trim().equals("????????????")||testLogItem.getOperatorName().trim().equals("China Mobile")) {
						if (0 != moveLogIDBuilder.toString().length()) {
							moveLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							moveLogIDBuilder.append(testLogItem.getRecSeqNo());
						}

					} else if (testLogItem.getOperatorName().trim()
							.equals("????????????")) {
						if (0 != linkLogIDBuilder.toString().length()) {
							linkLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							linkLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("????????????")) {
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
//				// ??????TestLogItem???id??????
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
//						terminalGroupNames.append("??????");
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
				// ???????????? / ????????????
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


				if(!statisticeTaskRequest.getName().contains("????????????-DD")){
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
				// ????????????
				File file1 = new File(fileSaveUrl+ "/" + ANALYZE_TEMPLATE_PATH + "/");
				if (!file1.exists()) {
					try{
						file1.mkdirs();
					}catch (Exception e){
						LOGGER.error("??????????????????",e);
						ActionContext.getContext().getValueStack().set("errorMsg", "??????????????????"+ file1.getAbsolutePath());
						return ReturnType.JSON;
					}
				}

				// ????????????
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
						LOGGER.error("??????????????????",e);
						ActionContext.getContext().getValueStack().set("errorMsg", "??????????????????"+ file1.getAbsolutePath());
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

									LOGGER.info(templateName + " ????????? " + System.currentTimeMillis() );

									//????????????
									PageList selectConditions = new PageList();
									selectConditions.putParam("templateName", templateName);
									List<CustomReportTemplatePojo> templateList = customTemplateService.queryTemplateByParam(selectConditions);

									File file = new File(templateList.get(0).getSaveFilePath());
									if (file == null || !file.exists() || file.isDirectory()) {
										throw new ApplicationException("?????????????????????");
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
									//??????????????????????????????????????????excel

									LOGGER.info(templateName + " ??????????????? " + System.currentTimeMillis() );

									customTemplateService.modifyExcelValue(page);


									LOGGER.info(templateName + " ????????? " + System.currentTimeMillis() );


								}catch(Exception e){
									flag = false;
									LOGGER.info("====????????????:"+templateName);
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

			//?????????????????????
			//String errotMsg = goUnicomSocket(customLogReportTask);
			//if(StringUtils.hasText(errotMsg)){
			//	customLogReportTaskDao.delete(customLogReportTask.getId());
			//	LOGGER.info("???????????????????????????,??????????????????:"+ errotMsg);
			//	ActionContext.getContext().getValueStack().set("errorMsg", "???????????????????????????,?????????????????????"+ errotMsg);
			//}


		}
		return ReturnType.JSON;
	}


	/**
	 * excel ????????????
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
			List<Map<String,Object>> list = (List<Map<String,Object>>)hashMap1.get("sqlObj1");
			List<Map<String,Object>> list2 = new ArrayList<>();
			LOGGER.info(" createAnalyFile after getData list.size():  " + list.size());
			if(list.size() > 0)
			{
				Long timestamp = 0L;
				String logName = "";
				for (Map<String, Object> map : list)
				{
					timestamp = 0L;
					logName = "";
					for (String key : map.keySet()) {
						String val = map.get(key).toString();
						if(key.equals("logName") && (val.length() > 0)){
							logName = val;
							LOGGER.info(" createAnalyFile logName:  " + logName);
						}
						if(key.equals("time") && (val.length() > 0)){
							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
								Date date = format.parse(val);
								timestamp = date.getTime();
								LOGGER.info(" createAnalyFile time:  " + val + "  " + timestamp);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					if((timestamp > 0) && (logName.length() > 0)) {
						map.putAll(getPcapData(logName, timestamp));
						list2.add(map);
					}
				}
				hashMap1.put("sqlObj1", list2);
			}
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
			report.setDescription("??????");
		}
		session.save(report);
		session.flush();
		return false;
	}

	public Map<String, Object> getPcapData(String log, Long timestamp) {
/*
		String sqlTrue1 =
				"select \n" +
						"m.nbruserplanednssucc, m.nbruserplanednsreq, \n" +
						"m.nbrtcpconnupsucc, m.nbrtcpconnupreq, m.nbrtcpconndownsucc, m.nbrtcpconndownreq, \n" +
						"m.alltcpuppackages,  m.retrtcpuppackages,  m.alltcpdownpackages,  m.retrtcpdownpackages, \n" +
						"m.nbrtexthttpacksucc, m.nbrtexthttpackfail, m.nbrpichttpacksucc,  m.nbrpichttpackfail,  m.nbrvidhttpacksucc,  m.nbrvidhttpackfail,  m.nbraudhttpacksucc,  m.nbraudhttpackfail \n" +
						"from \n" +
						" ( \n" +
						" select file_name \n" +
						" from iads_testlog_item  where recseqno = " + log +  "\n" +
						") t join iads_knowfeeling_traffic m on m.logname = t.file_name;";
*/
		String sqlTrue1 =
				"select \n" +
						"m.nbruserplanednssucc, m.nbruserplanednsreq, \n" +
						"m.nbrtcpconnupsucc, m.nbrtcpconnupreq, m.nbrtcpconndownsucc, m.nbrtcpconndownreq, \n" +
						"m.alltcpuppackages,  m.retrtcpuppackages,  m.alltcpdownpackages,  m.retrtcpdownpackages, \n" +
						"m.nbrtexthttpacksucc, m.nbrtexthttpackfail, m.nbrpichttpacksucc,  m.nbrpichttpackfail,  m.nbrvidhttpacksucc,  m.nbrvidhttpackfail,  m.nbraudhttpacksucc,  m.nbraudhttpackfail \n" +
						" from iads_knowfeeling_traffic m where m.logname = '" + log +"';";
		////
		long nbruserplanednssucc = 0;	// DNS???????????????  ??????
		long nbruserplanednsreq = 0;	// ??????

		long nbrtcpconnupsucc = 0;		// TCP??????????????? ??????
		long nbrtcpconnupreq = 0;		// ??????
		long nbrtcpconndownsucc = 0;	// ??????
		long nbrtcpconndownreq = 0;		// ??????

		long alltcpuppackages = 0;		// TCP????????? ??????
		long retrtcpuppackages = 0;		// ??????
		long alltcpdownpackages = 0;	// ??????
		long retrtcpdownpackages = 0;	// ??????

		long nbrtexthttpacksucc = 0;	// HTTP??????????????? ??????
		long nbrtexthttpackfail = 0;	// ??????
		long nbrpichttpacksucc = 0;		// ??????
		long nbrpichttpackfail = 0;		// ??????
		long nbrvidhttpacksucc = 0;		// ??????
		long nbrvidhttpackfail = 0;		// ??????
		long nbraudhttpacksucc = 0;		// ??????
		long nbraudhttpackfail = 0;		// ??????
/*
		String sqlTrue2 =
				"select \n" +
						"m.alluserplanednsdelay, m.nbruserplanednssucc, \n" +
						"m.alltcpconnupdelay, m.alltcpconndowndelay, m.nbrtcpconnupsucc, m.nbrtcpconndownsucc, \n" +
						"m.nbrtexthttpacksucc, m.nbrtexthttpackfail, m.nbrpichttpacksucc, m.nbrpichttpackfail, \n" +
						"m.nbrvidhttpacksucc, m.nbrvidhttpackfail, m.nbraudhttpacksucc, m.nbraudhttpackfail, \n" +
						"m.alltexthttpsuccackdelay, m.allpichttpsuccackdelay, m.allvidhttpsuccackdelay, m.allaudhttpsuccackdelay, \n" +
						"m.nbrtexthttpdlsucc, m.alltexthttpsuccdldelay, \n" +
						"m.nbrpichttpdlsucc, m.allpichttpsuccdldelay, \n" +
						"m.nbrvidhttpdlsucc, m.allvidhttpsuccdldelay, \n" +
						"m.nbraudhttpdlsucc, m.allaudhttpsuccdldelay \n" +
						"from ( \n" +
						"        select file_name \n" +
						"        from iads_testlog_item  where recseqno = " + log + "\n" +
						") t join iads_knowfeeling_traffic m on m.logname = t.file_name \n" +
						"and m.starttime <= " + timestamp + "and m.endtime >= " + timestamp + " ;";
*/
		String sqlTrue2 =
				"select \n" +
				"m.alluserplanednsdelay, m.nbruserplanednssucc, \n" +
				"m.alltcpconnupdelay, m.alltcpconndowndelay, m.nbrtcpconnupsucc, m.nbrtcpconndownsucc, \n" +
				"m.nbrtexthttpacksucc, m.nbrtexthttpackfail, m.nbrpichttpacksucc, m.nbrpichttpackfail, \n" +
				"m.nbrvidhttpacksucc, m.nbrvidhttpackfail, m.nbraudhttpacksucc, m.nbraudhttpackfail, \n" +
				"m.alltexthttpsuccackdelay, m.allpichttpsuccackdelay, m.allvidhttpsuccackdelay, m.allaudhttpsuccackdelay, \n" +
				"m.nbrtexthttpdlsucc, m.alltexthttpsuccdldelay, \n" +
				"m.nbrpichttpdlsucc, m.allpichttpsuccdldelay, \n" +
				"m.nbrvidhttpdlsucc, m.allvidhttpsuccdldelay, \n" +
				"m.nbraudhttpdlsucc, m.allaudhttpsuccdldelay \n" +
				"from \n" +
				"iads_knowfeeling_traffic m where m.logname = '" + log +
				"' and m.starttime <= " + timestamp +  " and m.endtime >= " + timestamp + " ;";
		////
		long alluserplanednsdelay2 = 0;
		long nbruserplanednssucc2 = 0;
		long alltcpconnupdelay2 = 0;
		long alltcpconndowndelay2 = 0;
		long nbrtcpconnupsucc2 = 0;
		long nbrtcpconndownsucc2 = 0;
		long nbrtexthttpacksucc2 = 0;
		long nbrtexthttpackfail2 = 0;
		long nbrpichttpacksucc2 = 0;
		long nbrpichttpackfail2 = 0;
		long nbrvidhttpacksucc2 = 0;
		long nbrvidhttpackfail2 = 0;
		long nbraudhttpacksucc2 = 0;
		long nbraudhttpackfail2 = 0;
		long alltexthttpsuccackdelay2 = 0;
		long allpichttpsuccackdelay2 = 0;
		long allvidhttpsuccackdelay2 = 0;
		long allaudhttpsuccackdelay2 = 0;
		long nbrtexthttpdlsucc2 = 0;
		long alltexthttpsuccdldelay2 = 0;
		long nbrpichttpdlsucc2 = 0;
		long allpichttpsuccdldelay2 = 0;
		long nbrvidhttpdlsucc2 = 0;
		long allvidhttpsuccdldelay2 = 0;
		long nbraudhttpdlsucc2 = 0;
		long allaudhttpsuccdldelay2 = 0;

		Map<String, Object> rm=new HashMap<>();
		List<Map<String, Object>> objectQueryAll = jdbcTemplate.objectQueryAll(sqlTrue1);
		if(objectQueryAll.size() > 0)       // ????????????
		{
			for (Map<String, Object> map : objectQueryAll) {
				if (map.get("nbruserplanednssucc") != null) {
					nbruserplanednssucc += Float.valueOf(map.get("nbruserplanednssucc").toString()).longValue();
				}
				if (map.get("nbruserplanednsreq") != null) {
					nbruserplanednsreq += Float.valueOf(map.get("nbruserplanednsreq").toString()).longValue();
				}
				if (map.get("nbrtcpconnupsucc") != null) {
					nbrtcpconnupsucc += Float.valueOf(map.get("nbrtcpconnupsucc").toString()).longValue();
				}
				if (map.get("nbrtcpconnupreq") != null) {
					nbrtcpconnupreq += Float.valueOf(map.get("nbrtcpconnupreq").toString()).longValue();
				}
				if (map.get("nbrtcpconndownsucc") != null) {
					nbrtcpconndownsucc += Float.valueOf(map.get("nbrtcpconndownsucc").toString()).longValue();
				}
				if (map.get("nbrtcpconndownreq") != null) {
					nbrtcpconndownreq += Float.valueOf(map.get("nbrtcpconndownreq").toString()).longValue();
				}
				if (map.get("alltcpuppackages") != null) {
					alltcpuppackages += Float.valueOf(map.get("alltcpuppackages").toString()).longValue();
				}
				if (map.get("retrtcpuppackages") != null) {
					retrtcpuppackages += Float.valueOf(map.get("retrtcpuppackages").toString()).longValue();
				}
				if (map.get("alltcpdownpackages") != null) {
					alltcpdownpackages += Float.valueOf(map.get("alltcpdownpackages").toString()).longValue();
				}
				if (map.get("retrtcpdownpackages") != null) {
					retrtcpdownpackages += Float.valueOf(map.get("retrtcpdownpackages").toString()).longValue();
				}
				if (map.get("nbrtexthttpacksucc") != null) {
					nbrtexthttpacksucc += Float.valueOf(map.get("nbrtexthttpacksucc").toString()).longValue();
				}
				if (map.get("nbrtexthttpackfail") != null) {
					nbrtexthttpackfail += Float.valueOf(map.get("nbrtexthttpackfail").toString()).longValue();
				}
				if (map.get("nbrpichttpacksucc") != null) {
					nbrpichttpacksucc += Float.valueOf(map.get("nbrpichttpacksucc").toString()).longValue();
				}
				if (map.get("nbrpichttpackfail") != null) {
					nbrpichttpackfail += Float.valueOf(map.get("nbrpichttpackfail").toString()).longValue();
				}
				if (map.get("nbrvidhttpacksucc") != null) {
					nbrvidhttpacksucc += Float.valueOf(map.get("nbrvidhttpacksucc").toString()).longValue();
				}
				if (map.get("nbrvidhttpackfail") != null) {
					nbrvidhttpackfail += Float.valueOf(map.get("nbrvidhttpackfail").toString()).longValue();
				}
				if (map.get("nbraudhttpacksucc") != null) {
					nbraudhttpacksucc += Float.valueOf(map.get("nbraudhttpacksucc").toString()).longValue();
				}
				if (map.get("nbraudhttpackfail") != null) {
					nbraudhttpackfail += Float.valueOf(map.get("nbraudhttpackfail").toString()).longValue();
				}
			}
			double dnssuccratio = 0.0;
			double tcpconnratio = 0.0;
			double tcpretrratio = 0.0;
			double httpacksuccratio = 0.0;

			if(nbruserplanednsreq > 0) {
				dnssuccratio = (double)nbruserplanednssucc / (double)nbruserplanednsreq;
			}
			long tcpconnreq = nbrtcpconnupreq + nbrtcpconndownreq;
			if(tcpconnreq > 0){
				tcpconnratio = (double)(nbrtcpconnupsucc + nbrtcpconndownsucc) / (double)tcpconnreq;
			}
			long retrtcp = retrtcpuppackages + retrtcpdownpackages;
			if(tcpconnreq > 0){
				tcpretrratio = (double)(retrtcp) / (double)(alltcpuppackages + alltcpdownpackages);
			}
			long httpack = nbrtexthttpacksucc + nbrtexthttpackfail +
					nbrpichttpacksucc + nbrpichttpackfail +
					nbrvidhttpacksucc + nbrvidhttpackfail +
					nbraudhttpacksucc + nbraudhttpackfail;
			long httpacksucc = nbrtexthttpacksucc + nbrpichttpacksucc + nbrvidhttpacksucc + nbraudhttpacksucc;
			if(httpack > 0){
				httpacksuccratio = (double)httpacksucc / (double)httpack;
			}
			// DNS?????????????????? TCP?????????????????? TCP???????????? HTTP???????????????
			rm.put("dnssuccratio",dnssuccratio);
			rm.put("tcpconnratio",tcpconnratio);
			rm.put("tcpretrratio",tcpretrratio);
			rm.put("httpacksuccratio",httpacksuccratio);
		}
		List<Map<String, Object>> objectQueryAll2 = jdbcTemplate.objectQueryAll(sqlTrue2);
		if(objectQueryAll2.size() == 1) {
			for (Map<String, Object> map : objectQueryAll2) {
				if (map.get("alluserplanednsdelay") != null) {
					alluserplanednsdelay2 = Float.valueOf(map.get("alluserplanednsdelay").toString()).longValue();
				}
				if (map.get("nbruserplanednssucc") != null) {
					nbruserplanednssucc2 += Float.valueOf(map.get("nbruserplanednssucc").toString()).longValue();
				}
				if (map.get("alltcpconnupdelay") != null) {
					alltcpconnupdelay2 += Float.valueOf(map.get("alltcpconnupdelay").toString()).longValue();
				}
				if (map.get("alltcpconndowndelay") != null) {
					alltcpconndowndelay2 += Float.valueOf(map.get("alltcpconndowndelay").toString()).longValue();
				}
				if (map.get("nbrtcpconnupsucc") != null) {
					nbrtcpconnupsucc2 += Float.valueOf(map.get("nbrtcpconnupsucc").toString()).longValue();
				}
				if (map.get("nbrtcpconndownsucc") != null) {
					nbrtcpconndownsucc2 += Float.valueOf(map.get("nbrtcpconndownsucc").toString()).longValue();
				}
				if (map.get("nbrtexthttpacksucc") != null) {
					nbrtexthttpacksucc2 += Float.valueOf(map.get("nbrtexthttpacksucc").toString()).longValue();
				}
				if (map.get("nbrtexthttpackfail") != null) {
					nbrtexthttpackfail2 += Float.valueOf(map.get("nbrtexthttpackfail").toString()).longValue();
				}
				if (map.get("nbrpichttpacksucc") != null) {
					nbrpichttpacksucc2 += Float.valueOf(map.get("nbrpichttpacksucc").toString()).longValue();
				}
				if (map.get("nbrpichttpackfail") != null) {
					nbrpichttpackfail2 += Float.valueOf(map.get("nbrpichttpackfail").toString()).longValue();
				}
				if (map.get("nbrvidhttpacksucc") != null) {
					nbrvidhttpacksucc2 += Float.valueOf(map.get("nbrvidhttpacksucc").toString()).longValue();
				}
				if (map.get("nbrvidhttpackfail") != null) {
					nbrvidhttpackfail2 += Float.valueOf(map.get("nbrvidhttpackfail").toString()).longValue();
				}
				if (map.get("nbraudhttpacksucc") != null) {
					nbraudhttpacksucc2 += Float.valueOf(map.get("nbraudhttpacksucc").toString()).longValue();
				}
				if (map.get("nbraudhttpackfail") != null) {
					nbraudhttpackfail2 += Float.valueOf(map.get("nbraudhttpackfail").toString()).longValue();
				}
				if (map.get("alltexthttpsuccackdelay") != null) {
					alltexthttpsuccackdelay2 += Float.valueOf(map.get("alltexthttpsuccackdelay").toString()).longValue();
				}
				if (map.get("allpichttpsuccackdelay") != null) {
					allpichttpsuccackdelay2 += Float.valueOf(map.get("allpichttpsuccackdelay").toString()).longValue();
				}
				if (map.get("allvidhttpsuccackdelay") != null) {
					allvidhttpsuccackdelay2 += Float.valueOf(map.get("allvidhttpsuccackdelay").toString()).longValue();
				}
				if (map.get("allaudhttpsuccackdelay") != null) {
					allaudhttpsuccackdelay2 += Float.valueOf(map.get("allaudhttpsuccackdelay").toString()).longValue();
				}
				if (map.get("nbrtexthttpdlsucc") != null) {
					nbrtexthttpdlsucc2 += Float.valueOf(map.get("nbrtexthttpdlsucc").toString()).longValue();
				}
				if (map.get("alltexthttpsuccdldelay") != null) {
					alltexthttpsuccdldelay2 += Float.valueOf(map.get("alltexthttpsuccdldelay").toString()).longValue();
				}
				if (map.get("nbrpichttpdlsucc") != null) {
					nbrpichttpdlsucc2 += Float.valueOf(map.get("nbrpichttpdlsucc").toString()).longValue();
				}
				if (map.get("allpichttpsuccdldelay") != null) {
					allpichttpsuccdldelay2 += Float.valueOf(map.get("allpichttpsuccdldelay").toString()).longValue();
				}
				if (map.get("nbrvidhttpdlsucc") != null) {
					nbrvidhttpdlsucc2 += Float.valueOf(map.get("nbrvidhttpdlsucc").toString()).longValue();
				}
				if (map.get("allvidhttpsuccdldelay") != null) {
					allvidhttpsuccdldelay2 += Float.valueOf(map.get("allvidhttpsuccdldelay").toString()).longValue();
				}
				if (map.get("nbraudhttpdlsucc") != null) {
					nbraudhttpdlsucc2 += Float.valueOf(map.get("nbraudhttpdlsucc").toString()).longValue();
				}
				if (map.get("allaudhttpsuccdldelay") != null) {
					allaudhttpsuccdldelay2 += Float.valueOf(map.get("allaudhttpsuccdldelay").toString()).longValue();
				}
			}
            /*
                ??????????????????DNS??????????????????	alluserplanednsdelay2 / nbruserplanednssucc2
                ??????????????????TCP??????????????????	( alltcpconnupdelay2 + alltcpconndowndelay2 ) / ( nbrtcpconnupsucc2 + nbrtcpconndownsucc2 )
                ??????????????????HTTP??????????????????	( alltexthttpsuccackdelay 2+ allpichttpsuccackdelay2 + allvidhttpsuccackdelay2 + allaudhttpsuccackdelay2 ) /
                                        ( nbrtexthttpacksucc2 + nbrtexthttpackfail2 + nbrpichttpacksucc2 + nbrpichttpackfail2 +
                                          nbrvidhttpacksucc2 + nbrvidhttpackfail2  + nbraudhttpacksucc2 + nbraudhttpackfail2 );
                ??????????????????????????????????????????	alltexthttpsuccdldelay2 / nbrtexthttpdlsucc2
                ??????????????????????????????????????????	allpichttpsuccdldelay2 / nbrpichttpdlsucc2
                ??????????????????????????????????????????	allvidhttpsuccdldelay2 / nbrvidhttpdlsucc2
                ??????????????????????????????????????????	allaudhttpsuccdldelay2 / nbraudhttpdlsucc2
             */
			long avgdnsdelay = 0;
			long avgtcpconndelay = 0;
			long avghttpackdelay = 0;
			long avgtexthttpdldelay = 0;
			long avgpichttpdldelay = 0;
			long avgvidhttpdldelay = 0;
			long avgaudhttpdldelay = 0;

			if(nbruserplanednssucc > 0){
				avgdnsdelay = alluserplanednsdelay2 / nbruserplanednssucc2;
			}
			if((nbrtcpconnupsucc2 + nbrtcpconndownsucc2) > 0){
				avgtcpconndelay = ( alltcpconnupdelay2 + alltcpconndowndelay2 ) / ( nbrtcpconnupsucc2 + nbrtcpconndownsucc2 );
			}
			long allhttpack = nbrtexthttpacksucc2 + nbrtexthttpackfail2 + nbrpichttpacksucc2 + nbrpichttpackfail2 +
					nbrvidhttpacksucc2  + nbrvidhttpackfail2  + nbraudhttpacksucc2 + nbraudhttpackfail2;
			if(allhttpack > 0){
				avghttpackdelay = ( alltexthttpsuccackdelay2 + allpichttpsuccackdelay2 + allvidhttpsuccackdelay2 + allaudhttpsuccackdelay2 ) / allhttpack;
			}
			if(nbrtexthttpdlsucc2 > 0){
				avgtexthttpdldelay = alltexthttpsuccdldelay2 / nbrtexthttpdlsucc2;
			}
			if(nbrpichttpdlsucc2 > 0){
				avgpichttpdldelay = allpichttpsuccdldelay2 / nbrpichttpdlsucc2;
			}
			if(nbrvidhttpdlsucc2 > 0){
				avgvidhttpdldelay = allvidhttpsuccdldelay2 / nbrvidhttpdlsucc2;
			}
			if(nbraudhttpdlsucc2 > 0){
				avgaudhttpdldelay = allaudhttpsuccdldelay2 / nbraudhttpdlsucc2;
			}
			rm.put("avgdnsdelay",avgdnsdelay);
			rm.put("avgtcpconndelay",avgtcpconndelay);
			rm.put("avghttpackdelay",avghttpackdelay);
			rm.put("avgtexthttpdldelay",avgtexthttpdldelay);
			rm.put("avgpichttpdldelay",avgpichttpdldelay);
			rm.put("avgvidhttpdldelay",avgvidhttpdldelay);
			rm.put("avgaudhttpdldelay",avgaudhttpdldelay);
		}
		return rm;
	}


	/**
	 * ???????????????????????????
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
					if (testLogItem.getOperatorName().trim().equals("????????????")||testLogItem.getOperatorName().trim().equals("China Mobile")) {
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
							.equals("????????????")) {
						if (0 != linkLogIDBuilder.toString().length()) {
							linkLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							linkLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("????????????")) {
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
				// ??????TestLogItem???id??????
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
						terminalGroupNames.append("??????");
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
				if(!statisticeTaskRequest.getName().contains("????????????-DD")){
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
				//?????????????????????
				String errotMsg = goSocket(statisticeTaskList.get(0));
				if(StringUtils.hasText(errotMsg)){
					// ????????????????????????????????????????????????
					List<Long> idss = new ArrayList<>();
					idss.add(statisticeTaskList.get(0).getId());
					reportService.delete(idss);

					LOGGER.info("???????????????????????????,??????????????????:"+ errotMsg);
					ActionContext.getContext().getValueStack().set("errorMsg", "???????????????????????????,?????????????????????"+ errotMsg);
				}
			}

		}
		return ReturnType.JSON;
	}

	/**
	 * ????????????
	 * @author lucheng
	 * @date 2020???12???28??? ??????1:44:53
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
				socket.connect("tcp://" + taskIp + ":" + taskPort); // ???response???????????????
				System.out.println("tcp://" + taskIp + ":" + taskPort);
				socket.send(request.getBytes()); // ???reponse???????????????
				byte[] responseBytes = socket.recv(); // ??????response?????????????????????
				if (null == responseBytes || responseBytes.length==0) {
					throw new ApplicationException("??????????????????!");
				} else {
					ZMQUtils.releaseZMQSocket(socket);
					String result = null;
					String response = new String(responseBytes, "UTF8");
					JSONObject respJson = JSONObject.fromObject(response);
					if (null != respJson) {
						result = respJson.getString("Result");
						if(result.equals("Success")){
							LOGGER.info("??????????????????");
							return null;
						}else if(result.equals("Failure")){
							LOGGER.info("??????????????????");
							throw new ApplicationException("????????????????????????!");
						}

					}else {
						throw new ApplicationException("????????????????????????!");
					}
				}
			} catch (Exception e) {
				ZMQUtils.releaseSocketException(socket);
				e.printStackTrace();
				if (e instanceof ApplicationException) {
					LOGGER.info(e.getMessage());
					return e.getMessage();
				} else {
					LOGGER.info("??????????????????!");
					return "????????????:"+e.getMessage();
				}
			}
		}
		return "??????????????????";
	}


	/**
	 * ????????????
	 * @author lucheng
	 * @date 2020???12???28??? ??????1:44:53
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
				socket.connect("tcp://" + taskIp + ":" + taskPort); // ???response???????????????
				System.out.println("tcp://" + taskIp + ":" + taskPort);
				socket.send(request.getBytes()); // ???reponse???????????????
				byte[] responseBytes = socket.recv(); // ??????response?????????????????????
				if (null == responseBytes || responseBytes.length==0) {
					throw new ApplicationException("??????????????????!");
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
								LOGGER.info("??????????????????");
								return null;
							}else if(result.equals("-1")){
								LOGGER.info("??????????????????");
								throw new ApplicationException("????????????????????????!");
							}

						}else {
							throw new ApplicationException("????????????????????????!");
						}
					}catch (Exception e){
						throw new ApplicationException("????????????????????????!");
					}

				}
			} catch (Exception e) {
				ZMQUtils.releaseSocketException(socket);
				e.printStackTrace();
				if (e instanceof ApplicationException) {
					LOGGER.info(e.getMessage());
					return e.getMessage();
				} else {
					LOGGER.info("??????????????????!");
					return "????????????:"+e.getMessage();
				}
			}
		}
		return "??????????????????";
	}




	/**
	 * ??????????????????
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
					if (testLogItem.getOperatorName().trim().equals("????????????")||testLogItem.getOperatorName().trim().equals("China Mobile")) {
						if (0 != moveLogIDBuilder.toString().length()) {
							moveLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							moveLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("????????????")) {
						if (0 != linkLogIDBuilder.toString().length()) {
							linkLogIDBuilder.append(","
									+ testLogItem.getRecSeqNo());
						} else {
							linkLogIDBuilder.append(testLogItem.getRecSeqNo());
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("????????????")) {
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
	 * ????????????????????????
	 *
	 * @return
	 * @throws Exception
	 */
	public String delReport() {
		if (null != ids) {
			String[] logIds = ids.trim().split(",");
			// ??????TestLogItem???id??????
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
	 * ??????????????????????????????
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
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G_EE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);

				// '?????????????????????'sheet
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
				// '?????????????????????'sheet
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
				// '???????????????4G???????????????????????????'sheet
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

				// '?????????????????????'sheet
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
	 * ??????5G????????????????????????
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
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G_DATA);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '?????????????????????'sheet
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
				// '?????????????????????'sheet
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
				// '??????????????????'sheet
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
				// '???????????????5G???????????????'sheet
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
				// '?????????????????????'sheet
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
	 * ??????NSA??????????????????
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
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_NSA_INDEX);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '????????????'sheet
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
				// '?????????'sheet
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
				// '?????????'sheet
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
				// '????????????5G???????????????'sheet
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
				// '?????????'sheet
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
	 * ??????????????????????????????
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
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				// ??????????????????
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_TEST_TRAIL);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'RSRP????????????'sheet
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
				// 'SINR????????????'sheet
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
				// 'FTP??????????????????'sheet
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
				// 'FTP??????????????????'sheet
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
	 * NSA??????????????????
	 *
	 * @return
	 */
	public String downloadNsaTotal() {
		return "downloadNsaTotal";
	}

	/**
	 * NSA??????????????????
	 *`
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

				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_NSA_INDEX);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '????????????'sheet
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
				// '?????????'sheet
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
				// '?????????'sheet
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
				// '????????????5G???????????????'sheet
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
				// '?????????'sheet
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
					"templates/NSA????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_NSA????????????.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * NSA??????????????????Sheet????????????
	 *
	 * @return
	 */
	public String downloadOneSheetNsaTotal() {
		return "downloadOneSheetNsaTotal";
	}

	/**
	 * NSA??????????????????Sheet????????????
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
					// ?????????????????????????????????
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// ??????????????????
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
					// '????????????'sheet
					if (sheetName.equals("????????????")) {
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
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
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
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
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
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
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
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
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
				if (sheetName.equals("????????????")) {
					map.put("summaryKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("businessKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("coverKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("mobileKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("accessKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/NSA????????????.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_NSA????????????_" + sheetName + ".xls";
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
	 * ??????????????????????????????
	 *
	 * @return
	 */
	public String downloadTestTrailTotal() {
		return "downloadTestTrailTotal";
	}

	/**
	 * NSA??????????????????
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
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				// ??????????????????
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_TEST_TRAIL);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'RSRP????????????'sheet
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
				// 'SINR????????????'sheet
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
				// 'FTP??????????????????'sheet
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
				// 'FTP??????????????????'sheet
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
					"templates/????????????????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_????????????????????????.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * ??????????????????????????????Sheet????????????
	 *
	 * @return
	 *//*
	public String downloadOneSheetTestTrail() {
		return "downloadOneSheetTestTrail";
	}

	*//**
	 * ??????????????????????????????Sheet????????????
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
					// ?????????????????????????????????
					map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
					// ??????????????????
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					// ??????????????????
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_TEST_TRAIL);
					lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("rsrpKpi", new EasyuiPageList());
					map.put("sinrKpi", new EasyuiPageList());
					map.put("ftpDLRateKpi", new EasyuiPageList());
					map.put("ftpULRateKpi", new EasyuiPageList());
					// 'RSRP????????????'sheet
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
						// 'SINR????????????'sheet
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
					} else if (sheetName.equals("FTP????????????")) {
						// 'FTP????????????'sheet
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
					} else if (sheetName.equals("FTP????????????")) {
						// 'FTP????????????'sheet
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
				} else if (sheetName.equals("FTP????????????")) {
					map.put("ftpDLRateKpi", new EasyuiPageList());
				} else if (sheetName.equals("FTP????????????")) {
					map.put("ftpULRateKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/????????????????????????.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_????????????????????????_" + sheetName + ".xls";
				ActionContext.getContext().put("fileName",
						new String(str.getBytes(), "ISO8859-1"));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}*/

	/**
	 * ?????????????????????
	 * @author lucheng
	 * @date 2020???12???30??? ??????1:13:10
	 * @return
	 */
	public String exportKpiExcel() {
		try {
			Long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("idLong");
			StatisticeTask statisticeTask = reportService.queryOneByID(id);
			if(statisticeTask!=null && !statisticeTask.getTaskStatus().equals("2")){
				throw new ApplicationException("???????????????????????????????????????");
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
				//??????????????????????????????????????????excel
				exportFilePath = customTemplateService.modifyExcelValue(page);
			}
			List<Map<String, String>> readExcelToMap = ReadExcelToHtml.readExcelToMap(exportFilePath, true);

			ActionContext.getContext().getValueStack().set("dataList", readExcelToMap);
		} catch (ApplicationException e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "??????:"+e.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	public String downloadTemplateExcelTotal() {
		return "downloadTemplateExcelTotal";
	}

	/**
	 * ?????????????????????????????????
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadTemplateExcel() {
		Long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("idLong");
		StatisticeTask statisticeTask = reportService.queryOneByID(id);
		if(statisticeTask!=null && !statisticeTask.getTaskStatus().equals("2")){
			throw new ApplicationException("???????????????????????????????????????");
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
	 * 5G????????????????????????
	 *
	 * @return
	 */
	public String downloadFgDataTotalExcel() {
		return "downloadFgDataTotal";
	}

	/**
	 * 5G????????????????????????
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

				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G_DATA);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '?????????????????????'sheet
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
				// '?????????????????????'sheet
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
				// '??????????????????'sheet
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
				// '???????????????5G???????????????'sheet
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
				// '?????????????????????'sheet
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
					"templates/5G??????????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_5G??????????????????.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 5G????????????????????????
	 *
	 * @return
	 */
	public String downloadFgEeTotalExcel() {
		return "downloadFgEeTotal";
	}

	/**
	 * 5G????????????????????????
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
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G_EE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);

				// '?????????????????????'sheet
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
				// '?????????????????????'sheet
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
				// '???????????????4G???????????????????????????'sheet
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

				// '?????????????????????'sheet
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
					"templates/5G??????????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_5G??????????????????.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 5G???????????????Sheet????????????
	 *
	 * @return
	 */
	public String downloadOneSheetFgDataTotalExcel() {
		return "downloadOneSheetFgDataTotal";
	}

	/**
	 * 5G???????????????Sheet????????????
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
					// ?????????????????????????????????
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// ??????????????????
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
					// '?????????????????????'sheet
					if (sheetName.equals("?????????????????????")) {
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
					} else if (sheetName.equals("?????????????????????")) {
						// '?????????????????????'sheet
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
					} else if (sheetName.equals("??????????????????")) {
						// '??????????????????'sheet
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
					} else if (sheetName.equals("???????????????5G???????????????")) {
						// '???????????????5G???????????????'sheet
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
					} else if (sheetName.equals("?????????????????????")) {
						// '?????????????????????'sheet
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
				if (sheetName.equals("?????????????????????")) {
					map.put("coverKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????????????????")) {
					map.put("disturbKpi", new EasyuiPageList());
				} else if (sheetName.equals("??????????????????")) {
					map.put("subIndexKpi", new EasyuiPageList());
				} else if (sheetName.equals("???????????????5G???????????????")) {
					map.put("eeKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????????????????")) {
					map.put("beamKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/5G??????????????????.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_5G??????????????????_" + sheetName + ".xls";
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
	 * 5G????????????????????????Sheet????????????
	 *
	 * @return
	 */
	public String downloadOneSheetFgEeTotalExcel() {
		return "downloadOneSheetFgEeTotal";
	}

	/**
	 * 5G????????????????????????Sheet????????????
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

					// ?????????????????????????????????
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// ??????????????????
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
					if (sheetName.equals("?????????????????????")) {
						// '?????????????????????'sheet
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
					} else if (sheetName.equals("?????????????????????")) {
						// '?????????????????????'sheet
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
					} else if (sheetName.equals("???????????????4G???????????????????????????")) {
						// '???????????????4G???????????????????????????'sheet
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
					} else if (sheetName.equals("?????????????????????")) {
						// '?????????????????????'sheet
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
				if (sheetName.equals("?????????????????????")) {
					map.put("insertKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????????????????")) {
					map.put("moverKpi", new EasyuiPageList());
				} else if (sheetName.equals("???????????????4G???????????????????????????")) {
					map.put("voiceEeKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????????????????")) {
					map.put("pduKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/5G??????????????????.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_5G??????????????????_" + sheetName + ".xls";
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
	 * ??????????????????
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
	 * ??????????????????
	 *
	 * ??????????????????????????????:0??????LTE ,1????????????????????? ,2LTE-FI
	 */
	private String getString(Integer i) {
		if (null != i) {
			if (0 == i) {
				return "??????LTE";
			} else if (1 == i) {
				return "?????????????????????";
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
