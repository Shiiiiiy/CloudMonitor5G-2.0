package com.datang.web.action.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;
import com.datang.common.util.TestLogItemUtils;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.report.IReportService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.util.DateUtil;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.datang.web.beans.report.BoxInforRequestBean;
import com.datang.web.beans.report.CityInfoRequestBean;
import com.datang.web.beans.report.ReportRequertBean;
import com.datang.web.beans.report.StatisticeTaskRequest;
import com.datang.web.beans.report.TestInfoRequestBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ????????????Action
 * 
 * @explain
 * @name ReportAction
 * @author shenyanwei
 * @date 2016???9???13?????????1:17:44
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class ReportAction extends PageAction implements
		ModelDriven<StatisticeTaskRequest> {
	private static Logger LOGGER = LoggerFactory.getLogger(ReportAction.class);
	/**
	 * ??????????????????
	 */
	@Autowired
	private IReportService reportService;
	/**
	 * ????????????
	 */
	@Autowired
	private ITestLogItemService testLogItemService;
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
		return "add";
	}

	/**
	 * ?????????????????????????????????
	 */
	public String goSee() {

		return "see";
	}

	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	public String goData() {
		if (null != typeNo) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("typeNo", typeNo);
		}
		return "data";
	}

	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	public String goVoice() {
		if (null != typeNo) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("typeNo", typeNo);
		}
		return "voice";
	}

	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	public String goNbiot() {
		if (null != typeNo) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("typeNo", typeNo);
		}
		return "nbiot";
	}

	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	public String goVideo() {
		if (null != typeNo) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("typeNo", typeNo);
		}
		return "video";
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
		if (StringUtils.hasText(boxIds) && StringUtils.hasText(cityIds)
				&& StringUtils.hasText(testRanks) && null != beginDate
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
		if (null != idLong) {
			StatisticeTask queryOneByID = reportService.queryOneByID(idLong);

			ActionContext.getContext().getValueStack()
					.set("statisticeTask", queryOneByID);
		} else {
			Long id = (Long) ServletActionContext.getRequest().getSession()
					.getAttribute("idLong");
			if (null != id) {
				ActionContext.getContext().getValueStack()
						.set("statisticeTask", reportService.queryOneByID(id));
			}
		}

		return "add";
	}

	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	public String addReportTask() {
		if (null != statisticeTaskRequest) {
			StatisticeTask statisticeTask = new StatisticeTask();
			String userName = (String) SecurityUtils.getSubject()
					.getPrincipal();
			if (null != userName) {
				statisticeTask.setCreaterName(userName.trim());
			}
			StringBuilder taskName = new StringBuilder();
			StringBuilder terminalGroupNames = new StringBuilder();
			if (null != statisticeTaskRequest.getBoxIds()) {
				statisticeTask.setBoxIds(statisticeTaskRequest.getBoxIds());
			}
			if (null != statisticeTaskRequest.getLogIds()) {
				statisticeTask.setLogIds(statisticeTaskRequest.getLogIds());
			}
			List<TestLogItem> queryTestLogItems = testLogItemService
					.queryTestLogItems(statisticeTaskRequest.getLogIds());
			StringBuilder moveLogIDBuilder = new StringBuilder();
			StringBuilder linkLogIDBuilder = new StringBuilder();
			StringBuilder telecomLogIDBuilder = new StringBuilder();
			// StringBuilder nbiotLogIDBuilder = new StringBuilder();
			for (TestLogItem testLogItem : queryTestLogItems) {
				if (null != testLogItem.getOperatorName()) {
					if (testLogItem.getOperatorName().trim().equals("????????????")) {
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
				taskName.append(DateUtil
						.getShortDateTimeStr(statisticeTaskRequest
								.getBeginDate()));
				taskName.append('-');
				taskName.append(DateUtil
						.getShortDateTimeStr(statisticeTaskRequest.getEndDate()));
				statisticeTask.setName(taskName.toString().trim());
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
			if (null != statisticeTaskRequest.getId()) {
				session.setAttribute("idLong", statisticeTaskRequest.getId());
				statisticeTask.setId(statisticeTaskRequest.getId());
				reportService.update(statisticeTask);
			} else {
				session.setAttribute("idLong", null);
				reportService.save(statisticeTask);
			}
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
	 * ????????????????????????
	 */
	public String quaryVoiceKpi() {
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
					if (testLogItem.getServiceType() != null) {
						if (testLogItem.getServiceType().indexOf("0,") != -1
								|| testLogItem.getServiceType().indexOf("1,") != -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem
										.getRecSeqNo());
							} else {
								idsString = idsString
										+ ","
										+ String.valueOf(testLogItem
												.getRecSeqNo());
							}
						}
					}
				}
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'KPI??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
				AbstractPageList totalKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("totalKpi", totalKpi);
				List totalKpirows = totalKpi.getRows();
				if (null != totalKpirows && 0 != totalKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, totalKpirows);
				}
				// 'VOLTE????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
				AbstractPageList volteKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("volteKpi", volteKpi);
				List volteKpirows = volteKpi.getRows();
				if (null != volteKpirows && 0 != volteKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, volteKpirows);
				}
				// 'CS?????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
				AbstractPageList csKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("csKpi", csKpi);
				List csKpirows = csKpi.getRows();
				if (null != csKpirows && 0 != csKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, csKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, dispatcherKpirows);
				}
				// 'MOS??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
				AbstractPageList mosKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("mosKpi", mosKpi);
				List mosKpirows = mosKpi.getRows();
				if (null != mosKpirows && 0 != mosKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, mosKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("totalKpi", new EasyuiPageList());
			map.put("volteKpi", new EasyuiPageList());
			map.put("csKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("mosKpi", new EasyuiPageList());
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * ??????NB-IoT????????????
	 */
	public String quaryNbiotKpi() {
		// Integer typeNo = (Integer) ServletActionContext.getRequest()
		// .getSession().getAttribute("typeNo");
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("moveTestLogItemIds");
		// if (null != typeNo) {
		// if (0 == typeNo) {
		// attribute = ServletActionContext.getRequest().getSession()
		// .getAttribute("moveTestLogItemIds");
		// } else if (1 == typeNo) {
		// attribute = ServletActionContext.getRequest().getSession()
		// .getAttribute("linkTestLogItemIds");
		// } else if (2 == typeNo) {
		// attribute = ServletActionContext.getRequest().getSession()
		// .getAttribute("telecomTestLogItemIds");
		// }
		// }
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					if (testLogItem.getServiceType() != null) {
						if (testLogItem.getServiceType().indexOf("8,") != -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem
										.getRecSeqNo());
							} else {
								idsString = idsString
										+ ","
										+ String.valueOf(testLogItem
												.getRecSeqNo());
							}
						}
					}
				}
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_NBIOT);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '??????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.BUSINESS_STATISTICS_INDEX);
				AbstractPageList businessStatisticsIndex = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("businessKpi", businessStatisticsIndex);
				List businessStatisticsIndexrows = businessStatisticsIndex
						.getRows();
				if (null != businessStatisticsIndexrows
						&& 0 != businessStatisticsIndexrows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, businessStatisticsIndexrows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, dispatcherKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
				AbstractPageList moveKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("moveKpi", moveKpi);
				List moveKpirows = moveKpi.getRows();
				if (null != moveKpirows && 0 != moveKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, moveKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
				AbstractPageList insertKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("insertKpi", insertKpi);
				List insertKpirows = insertKpi.getRows();
				if (null != insertKpirows && 0 != insertKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, insertKpirows);
				}
				// '???????????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.COVER_INDEX_PROPORTION);
				AbstractPageList coverProportionKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverProportionKpi", coverProportionKpi);
				List coverProportionKpirows = coverProportionKpi.getRows();
				if (null != coverProportionKpirows
						&& 0 != coverProportionKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverProportionKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("businessKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("moveKpi", new EasyuiPageList());
			map.put("insertKpi", new EasyuiPageList());
			map.put("coverProportionKpi", new EasyuiPageList());
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????
	 */
	public String quaryVideoKpi() {
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
					if (testLogItem.getServiceType() != null) {
						if (testLogItem.getServiceType().indexOf("2,") != -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem
										.getRecSeqNo());
							} else {
								idsString = idsString
										+ ","
										+ String.valueOf(testLogItem
												.getRecSeqNo());
							}
						}
					}
				}
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'KPI??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
				AbstractPageList statisticeKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("statisticeKpi", statisticeKpi);
				List statisticeKpirows = statisticeKpi.getRows();
				if (null != statisticeKpirows && 0 != statisticeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, statisticeKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
				AbstractPageList qualityKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("qualityKpi", qualityKpi);
				List qualityKpirows = qualityKpi.getRows();
				if (null != qualityKpirows && 0 != qualityKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, qualityKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
				AbstractPageList resourceKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("resourceKpi", resourceKpi);
				List resourceKpirows = resourceKpi.getRows();
				if (null != resourceKpirows && 0 != resourceKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, resourceKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_PERCEPTION);
				AbstractPageList perceptionKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("perceptionKpi", perceptionKpi);
				List perceptionKpirows = perceptionKpi.getRows();
				if (null != perceptionKpirows && 0 != perceptionKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, perceptionKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("statisticeKpi", new EasyuiPageList());
			map.put("qualityKpi", new EasyuiPageList());
			map.put("resourceKpi", new EasyuiPageList());
			map.put("perceptionKpi", new EasyuiPageList());

		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????
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
					if (testLogItem.getServiceType() != null) {
						if (testLogItem.getServiceType().indexOf("0,") == -1
								&& testLogItem.getServiceType().indexOf("1,") == -1
								&& testLogItem.getServiceType().indexOf("2,") == -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem
										.getRecSeqNo());
							} else {
								idsString = idsString
										+ ","
										+ String.valueOf(testLogItem
												.getRecSeqNo());
							}
						}
					}
				}
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_LTE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '????????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
				AbstractPageList indexKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("indexKpi", indexKpi);
				List indexKpirows = indexKpi.getRows();
				if (null != indexKpirows && 0 != indexKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, indexKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, dispatcherKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
				AbstractPageList moveKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("moveKpi", moveKpi);
				List moveKpirows = moveKpi.getRows();
				if (null != moveKpirows && 0 != moveKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, moveKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
				AbstractPageList insertKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("insertKpi", insertKpi);
				List insertKpirows = insertKpi.getRows();
				if (null != insertKpirows && 0 != insertKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, insertKpirows);
				}
				// '?????????FTP??????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
				AbstractPageList ftpSectionKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("ftpSectionKpi", ftpSectionKpi);
				List ftpSectionKpirows = ftpSectionKpi.getRows();
				if (null != ftpSectionKpirows && 0 != ftpSectionKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, ftpSectionKpirows);
				}
				// '?????????????????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
				AbstractPageList networkWerkcoverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("networkWerkcoverKpi", networkWerkcoverKpi);
				List networkWerkcoverKpirows = networkWerkcoverKpi.getRows();
				if (null != networkWerkcoverKpirows
						&& 0 != networkWerkcoverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, networkWerkcoverKpirows);
				}
				// '????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
				AbstractPageList stopKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("stopKpi", stopKpi);
				List stopKpirows = stopKpi.getRows();
				if (null != stopKpirows && 0 != stopKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, stopKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("indexKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("moveKpi", new EasyuiPageList());
			map.put("insertKpi", new EasyuiPageList());
			map.put("ftpSectionKpi", new EasyuiPageList());
			map.put("networkWerkcoverKpi", new EasyuiPageList());
			map.put("stopKpi", new EasyuiPageList());
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * lte????????????????????????
	 * 
	 * @return
	 */
	public String downloadLteDataTotalExcel() {
		return "downloadLteDataTotal";
	}

	/**
	 * lte????????????????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadLteDataTotal() {
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
				if (null != typeNo) {
					HttpSession session = ServletActionContext.getRequest()
							.getSession();
					if (0 == typeNo) {
						session.setAttribute("moveTestLogItemIds", attribute);
					} else if (1 == typeNo) {
						session.setAttribute("linkTestLogItemIds", attribute);
					} else if (2 == typeNo) {
						session.setAttribute("telecomTestLogItemIds", attribute);
					}
					session.setAttribute("typeNo", typeNo);
				}
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					if (testLogItem.getServiceType() != null) {
						if (testLogItem.getServiceType().indexOf("0,") == -1
								&& testLogItem.getServiceType().indexOf("1,") == -1
								&& testLogItem.getServiceType().indexOf("2,") == -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem
										.getRecSeqNo());
							} else {
								idsString = idsString
										+ ","
										+ String.valueOf(testLogItem
												.getRecSeqNo());
							}
						}
					}
				}
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_LTE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '????????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
				AbstractPageList indexKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("indexKpi", indexKpi);
				List indexKpirows = indexKpi.getRows();
				if (null != indexKpirows && 0 != indexKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, indexKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, dispatcherKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
				AbstractPageList moveKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("moveKpi", moveKpi);
				List moveKpirows = moveKpi.getRows();
				if (null != moveKpirows && 0 != moveKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, moveKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
				AbstractPageList insertKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("insertKpi", insertKpi);
				List insertKpirows = insertKpi.getRows();
				if (null != insertKpirows && 0 != insertKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, insertKpirows);
				}
				// '?????????FTP??????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
				AbstractPageList ftpSectionKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("ftpSectionKpi", ftpSectionKpi);
				List ftpSectionKpirows = ftpSectionKpi.getRows();
				if (null != ftpSectionKpirows && 0 != ftpSectionKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, ftpSectionKpirows);
				}
				// '?????????????????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
				AbstractPageList networkWerkcoverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("networkWerkcoverKpi", networkWerkcoverKpi);
				List networkWerkcoverKpirows = networkWerkcoverKpi.getRows();
				if (null != networkWerkcoverKpirows
						&& 0 != networkWerkcoverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, networkWerkcoverKpirows);
				}
				// '????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
				AbstractPageList stopKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("stopKpi", stopKpi);
				List stopKpirows = stopKpi.getRows();
				if (null != stopKpirows && 0 != stopKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, stopKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("indexKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("moveKpi", new EasyuiPageList());
			map.put("insertKpi", new EasyuiPageList());
			map.put("ftpSectionKpi", new EasyuiPageList());
			map.put("networkWerkcoverKpi", new EasyuiPageList());
			map.put("stopKpi", new EasyuiPageList());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/LTE??????????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_LTE??????????????????.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * volte????????????????????????
	 * 
	 * @return
	 */
	public String downloadVolteVoiceTotalExcel() {
		return "downloadVolteVoiceTotal";
	}

	/**
	 * volte????????????????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadVolteVoiceTotal() {
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
				if (null != typeNo) {
					HttpSession session = ServletActionContext.getRequest()
							.getSession();
					if (0 == typeNo) {
						session.setAttribute("moveTestLogItemIds", attribute);
					} else if (1 == typeNo) {
						session.setAttribute("linkTestLogItemIds", attribute);
					} else if (2 == typeNo) {
						session.setAttribute("telecomTestLogItemIds", attribute);
					}
					session.setAttribute("typeNo", typeNo);
				}
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					if (testLogItem.getServiceType() != null) {
						if (testLogItem.getServiceType().indexOf("0,") != -1
								|| testLogItem.getServiceType().indexOf("1,") != -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem
										.getRecSeqNo());
							} else {
								idsString = idsString
										+ ","
										+ String.valueOf(testLogItem
												.getRecSeqNo());
							}
						}
					}
				}
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'KPI??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
				AbstractPageList totalKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("totalKpi", totalKpi);
				List totalKpirows = totalKpi.getRows();
				if (null != totalKpirows && 0 != totalKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, totalKpirows);
				}
				// 'VOLTE????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
				AbstractPageList volteKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("volteKpi", volteKpi);
				List volteKpirows = volteKpi.getRows();
				if (null != volteKpirows && 0 != volteKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, volteKpirows);
				}
				// 'CS?????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
				AbstractPageList csKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("csKpi", csKpi);
				List csKpirows = csKpi.getRows();
				if (null != csKpirows && 0 != csKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, csKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, dispatcherKpirows);
				}
				// 'MOS??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
				AbstractPageList mosKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("mosKpi", mosKpi);
				List mosKpirows = mosKpi.getRows();
				if (null != mosKpirows && 0 != mosKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, mosKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("totalKpi", new EasyuiPageList());
			map.put("volteKpi", new EasyuiPageList());
			map.put("csKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("mosKpi", new EasyuiPageList());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/VoLTE????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_VoLTE????????????.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * NB-IoT??????????????????
	 * 
	 * @return
	 */
	public String downloadNbiotTotalExcel() {
		return "downloadNbiotTotal";
	}

	/**
	 * NB-IoT??????????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadNbiotTotal() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("moveTestLogItemIds");
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				HttpSession session = ServletActionContext.getRequest()
						.getSession();
				session.setAttribute("moveTestLogItemIds", attribute);
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				for (TestLogItem testLogItem : queryTestLogItems) {
					if (testLogItem.getServiceType() != null) {
						if (testLogItem.getServiceType().indexOf("8,") != -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem
										.getRecSeqNo());
							} else {
								idsString = idsString
										+ ","
										+ String.valueOf(testLogItem
												.getRecSeqNo());
							}
						}
					}
				}
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_NBIOT);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// '??????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.BUSINESS_STATISTICS_INDEX);
				AbstractPageList businessStatisticsIndex = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("businessKpi", businessStatisticsIndex);
				List businessStatisticsIndexrows = businessStatisticsIndex
						.getRows();
				if (null != businessStatisticsIndexrows
						&& 0 != businessStatisticsIndexrows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, businessStatisticsIndexrows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, disturbKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, dispatcherKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
				AbstractPageList moveKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("moveKpi", moveKpi);
				List moveKpirows = moveKpi.getRows();
				if (null != moveKpirows && 0 != moveKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, moveKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
				AbstractPageList insertKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("insertKpi", insertKpi);
				List insertKpirows = insertKpi.getRows();
				if (null != insertKpirows && 0 != insertKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, insertKpirows);
				}
				// '???????????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.COVER_INDEX_PROPORTION);
				AbstractPageList coverProportionKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverProportionKpi", coverProportionKpi);
				List coverProportionKpirows = coverProportionKpi.getRows();
				if (null != coverProportionKpirows
						&& 0 != coverProportionKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, coverProportionKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("businessKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("moveKpi", new EasyuiPageList());
			map.put("insertKpi", new EasyuiPageList());
			map.put("coverProportionKpi", new EasyuiPageList());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/NbIoT??????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_Nb-IoT??????.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * volte????????????????????????
	 * 
	 * @return
	 */
	public String downloadVolteVideoTotalExcel() {
		return "downloadVolteVideoTotal";
	}

	/**
	 * volte????????????????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadVolteVideoTotal() {
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
					if (testLogItem.getServiceType() != null) {
						if (testLogItem.getServiceType().indexOf("2,") != -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem
										.getRecSeqNo());
							} else {
								idsString = idsString
										+ ","
										+ String.valueOf(testLogItem
												.getRecSeqNo());
							}
						}
					}
				}
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// ??????????????????
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'KPI??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
				AbstractPageList statisticeKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("statisticeKpi", statisticeKpi);
				List statisticeKpirows = statisticeKpi.getRows();
				if (null != statisticeKpirows && 0 != statisticeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, statisticeKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
				AbstractPageList qualityKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("qualityKpi", qualityKpi);
				List qualityKpirows = qualityKpi.getRows();
				if (null != qualityKpirows && 0 != qualityKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, qualityKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
				AbstractPageList resourceKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("resourceKpi", resourceKpi);
				List resourceKpirows = resourceKpi.getRows();
				if (null != resourceKpirows && 0 != resourceKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, resourceKpirows);
				}
				// '?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_PERCEPTION);
				AbstractPageList perceptionKpi = reportService
						.queryKpi(lteWholePreviewParam);
				map.put("perceptionKpi", perceptionKpi);
				List perceptionKpirows = perceptionKpi.getRows();
				if (null != perceptionKpirows && 0 != perceptionKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, perceptionKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			map.put("statisticeKpi", new EasyuiPageList());
			map.put("qualityKpi", new EasyuiPageList());
			map.put("resourceKpi", new EasyuiPageList());
			map.put("perceptionKpi", new EasyuiPageList());

		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/VoLTE????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_VoLTE????????????.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * lte???????????????Sheet????????????
	 * 
	 * @return
	 */
	public String downloadOneSheetLteDataTotalExcel() {
		return "downloadOneSheetLteDataTotal";
	}

	/**
	 * lte???????????????Sheet????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetLteDataTotal() {
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
					if (null != typeNo) {
						HttpSession session = ServletActionContext.getRequest()
								.getSession();
						if (0 == typeNo) {
							session.setAttribute("moveTestLogItemIds",
									attribute);
						} else if (1 == typeNo) {
							session.setAttribute("linkTestLogItemIds",
									attribute);
						} else if (2 == typeNo) {
							session.setAttribute("telecomTestLogItemIds",
									attribute);
						}
						session.setAttribute("typeNo", typeNo);
					}
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
					String idsString = null;
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("0,") == -1
									&& testLogItem.getServiceType().indexOf(
											"1,") == -1
									&& testLogItem.getServiceType().indexOf(
											"2,") == -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
					// ?????????????????????????????????
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// ??????????????????
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_LTE);
					lteWholePreviewParam
							.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("indexKpi", new EasyuiPageList());
					map.put("coverKpi", new EasyuiPageList());
					map.put("disturbKpi", new EasyuiPageList());
					map.put("dispatcherKpi", new EasyuiPageList());
					map.put("moveKpi", new EasyuiPageList());
					map.put("insertKpi", new EasyuiPageList());
					map.put("ftpSectionKpi", new EasyuiPageList());
					map.put("networkWerkcoverKpi", new EasyuiPageList());
					map.put("stopKpi", new EasyuiPageList());
					// '????????????????????????'sheet
					if (sheetName.equals("????????????????????????")) {
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
						AbstractPageList indexKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("indexKpi", indexKpi);
						List indexKpirows = indexKpi.getRows();
						if (null != indexKpirows && 0 != indexKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, indexKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, coverKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, disturbKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, dispatcherKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList moveKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("moveKpi", moveKpi);
						List moveKpirows = moveKpi.getRows();
						if (null != moveKpirows && 0 != moveKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, moveKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList insertKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, insertKpirows);
						}
					} else if (sheetName.equals("?????????FTP??????????????????")) {
						// '?????????FTP??????????????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
						AbstractPageList ftpSectionKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("ftpSectionKpi", ftpSectionKpi);
						List ftpSectionKpirows = ftpSectionKpi.getRows();
						if (null != ftpSectionKpirows
								&& 0 != ftpSectionKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, ftpSectionKpirows);
						}
					} else if (sheetName.equals("?????????????????????????????????")) {
						// '?????????????????????????????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
						AbstractPageList networkWerkcoverKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("networkWerkcoverKpi", networkWerkcoverKpi);
						List networkWerkcoverKpirows = networkWerkcoverKpi
								.getRows();
						if (null != networkWerkcoverKpirows
								&& 0 != networkWerkcoverKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, networkWerkcoverKpirows);
						}
					} else if (sheetName.equals("????????????")) {
						// '????????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
						AbstractPageList stopKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("stopKpi", stopKpi);
						List stopKpirows = stopKpi.getRows();
						if (null != stopKpirows && 0 != stopKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, stopKpirows);
						}
					}
				}
			} else {
				if (sheetName.equals("????????????????????????")) {
					map.put("indexKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("coverKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("disturbKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("dispatcherKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("moveKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("insertKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????FTP??????????????????")) {
					map.put("ftpSectionKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????????????????????????????")) {
					map.put("networkWerkcoverKpi", new EasyuiPageList());
				} else if (sheetName.equals("????????????")) {
					map.put("stopKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/LTE??????????????????.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_LTE??????????????????_" + sheetName + ".xls";
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
	 * volte???????????????Sheet????????????
	 * 
	 * @return
	 */
	public String downloadOneSheetVolteVoiceTotalExcel() {
		return "downloadOneSheetVolteVoiceTotal";
	}

	/**
	 * volte???????????????Sheet????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetVolteVoiceTotal() {
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
					if (null != typeNo) {
						HttpSession session = ServletActionContext.getRequest()
								.getSession();
						if (0 == typeNo) {
							session.setAttribute("moveTestLogItemIds",
									attribute);
						} else if (1 == typeNo) {
							session.setAttribute("linkTestLogItemIds",
									attribute);
						} else if (2 == typeNo) {
							session.setAttribute("telecomTestLogItemIds",
									attribute);
						}
						session.setAttribute("typeNo", typeNo);
					}
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
					String idsString = null;
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("0,") != -1
									|| testLogItem.getServiceType().indexOf(
											"1,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
					// ?????????????????????????????????
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// ??????????????????
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
					lteWholePreviewParam
							.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("totalKpi", new EasyuiPageList());
					map.put("volteKpi", new EasyuiPageList());
					map.put("csKpi", new EasyuiPageList());
					map.put("coverKpi", new EasyuiPageList());
					map.put("disturbKpi", new EasyuiPageList());
					map.put("dispatcherKpi", new EasyuiPageList());
					map.put("mosKpi", new EasyuiPageList());
					if (sheetName.equals("KPI??????")) {
						// 'KPI??????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
						AbstractPageList totalKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("totalKpi", totalKpi);
						List totalKpirows = totalKpi.getRows();
						if (null != totalKpirows && 0 != totalKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, totalKpirows);
						}
					} else if (sheetName.equals("VOLTE????????????")) {
						// 'VOLTE????????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
						AbstractPageList volteKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("volteKpi", volteKpi);
						List volteKpirows = volteKpi.getRows();
						if (null != volteKpirows && 0 != volteKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, volteKpirows);
						}
					} else if (sheetName.equals("CS?????????????????????")) {
						// 'CS?????????????????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
						AbstractPageList csKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("csKpi", csKpi);
						List csKpirows = csKpi.getRows();
						if (null != csKpirows && 0 != csKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, csKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, coverKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, disturbKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, dispatcherKpirows);
						}
					} else if (sheetName.equals("MOS??????")) {
						// 'MOS??????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
						AbstractPageList mosKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("mosKpi", mosKpi);
						List mosKpirows = mosKpi.getRows();
						if (null != mosKpirows && 0 != mosKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, mosKpirows);
						}
					}
				}
			} else {
				if (sheetName.equals("KPI??????")) {
					map.put("totalKpi", new EasyuiPageList());
				} else if (sheetName.equals("VOLTE????????????")) {
					map.put("volteKpi", new EasyuiPageList());
				} else if (sheetName.equals("CS?????????????????????")) {
					map.put("csKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("coverKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("disturbKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("dispatcherKpi", new EasyuiPageList());
				} else if (sheetName.equals("MOS??????")) {
					map.put("mosKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/VoLTE????????????.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_VoLTE????????????" + sheetName + ".xls";
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
	 * NB-IoT?????????Sheet????????????
	 * 
	 * @return
	 */
	public String downloadOneSheetNbiotTotalExcel() {
		return "downloadOneSheetNbiotTotal";
	}

	/**
	 * NB-IoT?????????Sheet????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetNbiotTotal() {
		if (null != sheetName) {
			Object attribute = ServletActionContext.getRequest().getSession()
					.getAttribute("moveTestLogItemIds");
			Map<String, Object> map = new HashMap<>();
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					HttpSession session = ServletActionContext.getRequest()
							.getSession();
					session.setAttribute("moveTestLogItemIds", attribute);
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
					String idsString = null;
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("8,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
					// ?????????????????????????????????
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// ??????????????????
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_NBIOT);
					lteWholePreviewParam
							.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("businessKpi", new EasyuiPageList());
					map.put("coverKpi", new EasyuiPageList());
					map.put("disturbKpi", new EasyuiPageList());
					map.put("dispatcherKpi", new EasyuiPageList());
					map.put("moveKpi", new EasyuiPageList());
					map.put("insertKpi", new EasyuiPageList());
					map.put("coverProportionKpi", new EasyuiPageList());
					if (sheetName.equals("??????????????????")) {
						// '??????????????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.BUSINESS_STATISTICS_INDEX);
						AbstractPageList businessStatisticsIndex = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("businessKpi", businessStatisticsIndex);
						List businessStatisticsIndexrows = businessStatisticsIndex
								.getRows();
						if (null != businessStatisticsIndexrows
								&& 0 != businessStatisticsIndexrows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem,
									businessStatisticsIndexrows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, coverKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, disturbKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, dispatcherKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList moveKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("moveKpi", moveKpi);
						List moveKpirows = moveKpi.getRows();
						if (null != moveKpirows && 0 != moveKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, moveKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList insertKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, insertKpirows);
						}
					} else if (sheetName.equals("???????????????????????????")) {
						// '???????????????????????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.COVER_INDEX_PROPORTION);
						AbstractPageList coverProportionKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("coverProportionKpi", coverProportionKpi);
						List coverProportionKpirows = coverProportionKpi
								.getRows();
						if (null != coverProportionKpirows
								&& 0 != coverProportionKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, coverProportionKpirows);
						}
					}
				}
			} else {
				if (sheetName.equals("??????????????????")) {
					map.put("businessKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("coverKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("disturbKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("dispatcherKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("moveKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("insertKpi", new EasyuiPageList());
				} else if (sheetName.equals("???????????????????????????")) {
					map.put("coverProportionKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/NbIoT??????.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_Nb-IoT??????" + sheetName + ".xls";
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
	 * volte???????????????Sheet????????????
	 * 
	 * @return
	 */
	public String downloadOneSheetVolteVideoTotalExcel() {
		return "downloadOneSheetVolteVideoTotal";
	}

	/**
	 * volte???????????????Sheet????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetVolteVideoTotal() {
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
					if (null != typeNo) {
						HttpSession session = ServletActionContext.getRequest()
								.getSession();
						if (0 == typeNo) {
							session.setAttribute("moveTestLogItemIds",
									attribute);
						} else if (1 == typeNo) {
							session.setAttribute("linkTestLogItemIds",
									attribute);
						} else if (2 == typeNo) {
							session.setAttribute("telecomTestLogItemIds",
									attribute);
						}
						session.setAttribute("typeNo", typeNo);
					}
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
					String idsString = null;
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
					// ?????????????????????????????????
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(listTestLogItem));
					// ??????????????????
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
					lteWholePreviewParam
							.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(idsString);
					map.put("statisticeKpi", new EasyuiPageList());
					map.put("qualityKpi", new EasyuiPageList());
					map.put("resourceKpi", new EasyuiPageList());
					map.put("perceptionKpi", new EasyuiPageList());
					if (sheetName.equals("KPI??????")) {
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
						AbstractPageList statisticeKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("statisticeKpi", statisticeKpi);
						List statisticeKpirows = statisticeKpi.getRows();
						if (null != statisticeKpirows
								&& 0 != statisticeKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, statisticeKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
						AbstractPageList qualityKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("qualityKpi", qualityKpi);
						List qualityKpirows = qualityKpi.getRows();
						if (null != qualityKpirows
								&& 0 != qualityKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, qualityKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
						AbstractPageList resourceKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("resourceKpi", resourceKpi);
						List resourceKpirows = resourceKpi.getRows();
						if (null != resourceKpirows
								&& 0 != resourceKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, resourceKpirows);
						}
					} else if (sheetName.equals("?????????")) {
						// '?????????'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_PERCEPTION);
						AbstractPageList perceptionKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("perceptionKpi", perceptionKpi);
						List perceptionKpirows = perceptionKpi.getRows();
						if (null != perceptionKpirows
								&& 0 != perceptionKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, perceptionKpirows);
						}
					}
				}
			} else {
				if (sheetName.equals("KPI??????")) {
					map.put("statisticeKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("qualityKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("resourceKpi", new EasyuiPageList());
				} else if (sheetName.equals("?????????")) {
					map.put("perceptionKpi", new EasyuiPageList());
				}
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/VoLTE????????????.xls", sheetName);
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_VoLTE????????????" + sheetName + ".xls";
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

}
