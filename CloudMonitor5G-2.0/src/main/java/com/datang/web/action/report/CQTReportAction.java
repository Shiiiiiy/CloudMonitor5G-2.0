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
import com.datang.domain.report.CQTStatisticeTask;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.report.ICQTReportService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.util.DateUtil;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.datang.web.beans.report.BoxInforRequestBean;
import com.datang.web.beans.report.CityInfoRequestBean;
import com.datang.web.beans.report.TestInfoRequestBean;
import com.datang.web.beans.report.cqt.CQTReportRequertBean;
import com.datang.web.beans.report.cqt.CQTStatisticeTaskRequest;
import com.datang.web.beans.report.cqt.FloorInfoRequestBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * CQT统计任务Action
 * 
 * @explain
 * @name CQTReportAction
 * @author shenyanwei
 * @date 2016年10月31日上午9:17:06
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CQTReportAction extends PageAction implements
		ModelDriven<CQTStatisticeTaskRequest> {
	private static Logger LOGGER = LoggerFactory
			.getLogger(CQTReportAction.class);
	/**
	 * 统计任务服务
	 */
	@Autowired
	private ICQTReportService cqtReportService;
	/**
	 * 日志服务
	 */
	@Autowired
	private ITestLogItemService testLogItemService;
	/**
	 * 区域管理服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;

	/** 多条件查询请求参数 */
	private CQTReportRequertBean cqtReportRequertBean = new CQTReportRequertBean();
	/**
	 * 保存统计任务时收集参数
	 */
	private CQTStatisticeTaskRequest cqtStatisticeTaskRequest = new CQTStatisticeTaskRequest();
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
		return "add";
	}

	/**
	 * 跳转到统计结果查看界面
	 */
	public String goSee() {

		return "see";
	}

	/**
	 * 跳转到数据界面
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
	 * 跳转到语音界面
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
	 * 跳转到视频界面
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
	 * 获取某些boxIds相关的日志 testLogItem
	 * 
	 * @return
	 */
	public String testLogItem() {
		Date beginDate = cqtStatisticeTaskRequest.getBeginDate();
		Date endDate = cqtStatisticeTaskRequest.getEndDate();
		String boxIds = cqtStatisticeTaskRequest.getBoxIds();
		String cityIds = cqtStatisticeTaskRequest.getCityIds();
		String testRanks = cqtStatisticeTaskRequest.getTestRank();
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
					.queryCQTTestLogItemsByOther(boxList, cityList,
							testRankList, beginDate, endDate);
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
	 * 获取楼宇
	 * 
	 * @return
	 */
	public String getfloors() {
		Date beginDate = cqtStatisticeTaskRequest.getBeginDate();
		Date endDate = cqtStatisticeTaskRequest.getEndDate();
		String boxIds = cqtStatisticeTaskRequest.getBoxIds();
		String cityIds = cqtStatisticeTaskRequest.getCityIds();
		String testRanks = cqtStatisticeTaskRequest.getTestRank();
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
			List<FloorInfoRequestBean> responseBeans = new ArrayList<>();
			if (null != testLogItemsByBoxIds
					&& 0 != testLogItemsByBoxIds.size()) {
				for (TestLogItem testLogItem : testLogItemsByBoxIds) {
					FloorInfoRequestBean responseBean = new FloorInfoRequestBean();
					responseBean.setAtuName(testLogItem.getTerminalGroup());
					responseBean.setFloorName(testLogItem.getFloorName());
					responseBeans.add(responseBean);
				}
			}
			ActionContext.getContext().getValueStack()
					.set("floors", responseBeans);
		}

		return ReturnType.JSON;
	}

	/**
	 * 根据LogIDs获取日志 testLogItem
	 * 
	 * @return
	 */
	public String getTestLogItem() {
		String logIds = cqtStatisticeTaskRequest.getLogIds();
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
	 * 获取城市域信息
	 */
	public String getCityInfo() {
		String cityIds = cqtStatisticeTaskRequest.getCityIds();
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

		cqtReportRequertBean.setBeginDate(cqtStatisticeTaskRequest
				.getBeginDate());
		cqtReportRequertBean.setEndDate(cqtStatisticeTaskRequest.getEndDate());
		cqtReportRequertBean.setCreaterName(cqtStatisticeTaskRequest
				.getCreaterName());
		cqtReportRequertBean.setName(cqtStatisticeTaskRequest.getName());
		pageList.putParam("pageQueryBean", cqtReportRequertBean);
		return cqtReportService.pageList(pageList);

	}

	@Override
	public CQTStatisticeTaskRequest getModel() {
		return cqtStatisticeTaskRequest;
	}

	/**
	 * 查询单个任务信息并跳转到相关页面
	 * 
	 * @return
	 */
	public String seeInfo() {
		if (null != idLong) {
			CQTStatisticeTask queryOneByID = cqtReportService
					.queryOneByID(idLong);

			ActionContext.getContext().getValueStack()
					.set("statisticeTask", queryOneByID);
		} else {
			Long id = (Long) ServletActionContext.getRequest().getSession()
					.getAttribute("idLong");
			if (null != id) {
				ActionContext
						.getContext()
						.getValueStack()
						.set("statisticeTask",
								cqtReportService.queryOneByID(id));
			}
		}

		return "add";
	}

	/**
	 * 添加或修改统计任务
	 * 
	 * @return
	 */
	public String addReportTask() {
		if (null != cqtStatisticeTaskRequest) {
			CQTStatisticeTask statisticeTask = new CQTStatisticeTask();
			String userName = (String) SecurityUtils.getSubject()
					.getPrincipal();
			if (null != userName) {
				statisticeTask.setCreaterName(userName.trim());
			}
			StringBuilder taskName = new StringBuilder();
			StringBuilder terminalGroupNames = new StringBuilder();
			if (null != cqtStatisticeTaskRequest.getBoxIds()) {
				statisticeTask.setBoxIds(cqtStatisticeTaskRequest.getBoxIds());
			}
			if (null != cqtStatisticeTaskRequest.getFloors()) {
				statisticeTask.setFloors(cqtStatisticeTaskRequest.getFloors());
			}
			if (null != cqtStatisticeTaskRequest.getLogIds()) {
				statisticeTask.setLogIds(cqtStatisticeTaskRequest.getLogIds());
			}
			List<TestLogItem> queryTestLogItems = testLogItemService
					.queryTestLogItems(cqtStatisticeTaskRequest.getLogIds());
			StringBuilder moveVoiceLogIDBuilder = new StringBuilder();
			StringBuilder linkVoiceLogIDBuilder = new StringBuilder();
			StringBuilder telecomVoiceLogIDBuilder = new StringBuilder();
			StringBuilder moveDataLogIDBuilder = new StringBuilder();
			StringBuilder linkDataLogIDBuilder = new StringBuilder();
			StringBuilder telecomDataLogIDBuilder = new StringBuilder();
			StringBuilder moveVideoLogIDBuilder = new StringBuilder();
			StringBuilder linkVideoLogIDBuilder = new StringBuilder();
			StringBuilder telecomVideoLogIDBuilder = new StringBuilder();
			for (TestLogItem testLogItem : queryTestLogItems) {
				if (null != testLogItem.getOperatorName()) {
					if (testLogItem.getOperatorName().trim().equals("中国移动")) {
						if (testLogItem.getServiceType().indexOf("0,") != -1
								|| testLogItem.getServiceType().indexOf("1,") != -1) {
							if (0 != moveVoiceLogIDBuilder.toString().length()) {
								moveVoiceLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								moveVoiceLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						} else if (testLogItem.getServiceType().indexOf("2,") != -1) {
							if (0 != moveVideoLogIDBuilder.toString().length()) {
								moveVideoLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								moveVideoLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						} else if (testLogItem.getServiceType().indexOf("0,") == -1
								&& testLogItem.getServiceType().indexOf("1,") == -1
								&& testLogItem.getServiceType().indexOf("2,") == -1) {
							if (0 != moveDataLogIDBuilder.toString().length()) {
								moveDataLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								moveDataLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}

					} else if (testLogItem.getOperatorName().trim()
							.equals("中国联通")) {
						if (testLogItem.getServiceType().indexOf("0,") != -1
								|| testLogItem.getServiceType().indexOf("1,") != -1) {
							if (0 != linkVoiceLogIDBuilder.toString().length()) {
								linkVoiceLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								linkVoiceLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						} else if (testLogItem.getServiceType().indexOf("2,") != -1) {
							if (0 != linkVideoLogIDBuilder.toString().length()) {
								linkVideoLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								linkVideoLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						} else if (testLogItem.getServiceType().indexOf("0,") == -1
								&& testLogItem.getServiceType().indexOf("1,") == -1
								&& testLogItem.getServiceType().indexOf("2,") == -1) {
							if (0 != linkDataLogIDBuilder.toString().length()) {
								linkDataLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								linkDataLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
					} else if (testLogItem.getOperatorName().trim()
							.equals("中国电信")) {
						if (testLogItem.getServiceType().indexOf("0,") != -1
								|| testLogItem.getServiceType().indexOf("1,") != -1) {
							if (0 != telecomVoiceLogIDBuilder.toString()
									.length()) {
								telecomVoiceLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								telecomVoiceLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						} else if (testLogItem.getServiceType().indexOf("2,") != -1) {
							if (0 != telecomVideoLogIDBuilder.toString()
									.length()) {
								telecomVideoLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								telecomVideoLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						} else if (testLogItem.getServiceType().indexOf("0,") == -1
								&& testLogItem.getServiceType().indexOf("1,") == -1
								&& testLogItem.getServiceType().indexOf("2,") == -1) {
							if (0 != telecomDataLogIDBuilder.toString()
									.length()) {
								telecomDataLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								telecomDataLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
					}
				}
			}
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("moveVoiceTestLogItemIds",
					moveVoiceLogIDBuilder.toString());
			session.setAttribute("linkVoiceTestLogItemIds",
					linkVoiceLogIDBuilder.toString());
			session.setAttribute("telecomVoiceTestLogItemIds",
					telecomVoiceLogIDBuilder.toString());
			session.setAttribute("moveVideoTestLogItemIds",
					moveVideoLogIDBuilder.toString());
			session.setAttribute("linkVideoTestLogItemIds",
					linkVideoLogIDBuilder.toString());
			session.setAttribute("telecomVideoTestLogItemIds",
					telecomVideoLogIDBuilder.toString());
			session.setAttribute("moveDataTestLogItemIds",
					moveDataLogIDBuilder.toString());
			session.setAttribute("linkDataTestLogItemIds",
					linkDataLogIDBuilder.toString());
			session.setAttribute("telecomDataTestLogItemIds",
					telecomDataLogIDBuilder.toString());
			if (cqtStatisticeTaskRequest.getCityIds() != null) {
				String[] cityIds = cqtStatisticeTaskRequest.getCityIds().trim()
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
				taskName.append("CQT");
				taskName.append('-');
				if (terminalGroupNames.length() > 0) {
					taskName.append(terminalGroupNames.toString().trim());
				}
				taskName.append('-');
				taskName.append(DateUtil
						.getShortDateTimeStr(cqtStatisticeTaskRequest
								.getBeginDate()));
				taskName.append('-');
				taskName.append(DateUtil
						.getShortDateTimeStr(cqtStatisticeTaskRequest
								.getEndDate()));
				statisticeTask.setName(taskName.toString().trim());
				session.setAttribute("taskName", taskName.toString());
				statisticeTask
						.setCityIds(cqtStatisticeTaskRequest.getCityIds());

			}
			if (null != cqtStatisticeTaskRequest.getBeginDate()) {
				statisticeTask.setBeginDate(cqtStatisticeTaskRequest
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
				session.setAttribute("collectTypes", sBuffer.toString());
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
			if (null != cqtStatisticeTaskRequest.getEndDate()) {
				statisticeTask
						.setEndDate(cqtStatisticeTaskRequest.getEndDate());
			}
			statisticeTask.setCreatDate(new Date());
			if (null != cqtStatisticeTaskRequest.getId()) {
				session.setAttribute("idLong", cqtStatisticeTaskRequest.getId());
				statisticeTask.setId(cqtStatisticeTaskRequest.getId());
				cqtReportService.update(statisticeTask);
			} else {
				session.setAttribute("idLong", null);
				cqtReportService.save(statisticeTask);
			}
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
			cqtReportService.delete(idss);
		}

		return ReturnType.JSON;
	}

	/**
	 * 查询语音报表信息
	 */
	public String quaryVoiceKpi() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		String collectTypes = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("collectTypes");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveVoiceTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkVoiceTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomVoiceTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		List<Map<String, AbstractPageList>> list = new ArrayList<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// 收集汇总方式
				map.put("collectTypes", ServletActionContext.getRequest()
						.getSession().getAttribute("collectTypes"));
				// 汇总其他信息
				if (collectTypes.indexOf("1") != -1) {
					list.add(getVoiceKpi(testLogItemIds, queryTestLogItems,
							"全国", 1));
				}
				if (collectTypes.indexOf("2") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()))
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 2);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("3") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 3);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("4") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 4);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("5") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 5);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("6") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI汇总'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
						AbstractPageList totalKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("totalKpi", totalKpi);
						List totalKpirows = totalKpi.getRows();
						if (null != totalKpirows && 0 != totalKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, totalKpirows, null, 6,
									provinceName);
						}
						// 'VOLTE统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
						AbstractPageList volteKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("volteKpi", volteKpi);
						List volteKpirows = volteKpi.getRows();
						if (null != volteKpirows && 0 != volteKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, volteKpirows, null, 6,
									provinceName);
						}
						// 'CS域语音统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
						AbstractPageList csKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("csKpi", csKpi);
						List csKpirows = csKpi.getRows();
						if (null != csKpirows && 0 != csKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, csKpirows, null, 6,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 6,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						map.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 6,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									6, provinceName);
						}
						// 'MOS统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
						AbstractPageList mosKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("mosKpi", mosKpi);
						List mosKpirows = mosKpi.getRows();
						if (null != mosKpirows && 0 != mosKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, mosKpirows, null, 6,
									provinceName);
						}
					} else {
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("7") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFileName()
											+ "," + testLogItem.getFloorName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFileName()
											+ "," + testLogItem.getFloorName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 7);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("8") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_KEYPOINT_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.KEYPOINTNO + ","
										+ ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI汇总'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
						AbstractPageList totalKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("totalKpi", totalKpi);
						List totalKpirows = totalKpi.getRows();
						if (null != totalKpirows && 0 != totalKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, totalKpirows, null, 8,
									provinceName);
						}
						// 'VOLTE统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
						AbstractPageList volteKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("volteKpi", volteKpi);
						List volteKpirows = volteKpi.getRows();
						if (null != volteKpirows && 0 != volteKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, volteKpirows, null, 8,
									provinceName);
						}
						// 'CS域语音统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
						AbstractPageList csKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("csKpi", csKpi);
						List csKpirows = csKpi.getRows();
						if (null != csKpirows && 0 != csKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, csKpirows, null, 8,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 8,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 8,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									8, provinceName);
						}
						// 'MOS统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
						AbstractPageList mosKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("mosKpi", mosKpi);
						List mosKpirows = mosKpi.getRows();
						if (null != mosKpirows && 0 != mosKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, mosKpirows, null, 8,
									provinceName);
						}
					} else {
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("9") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.KEYPOINTNO + ","
										+ ParamConstant.RECSEQNO + ","
										+ ParamConstant.FLOORNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI汇总'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
						AbstractPageList totalKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("totalKpi", totalKpi);
						List totalKpirows = totalKpi.getRows();
						if (null != totalKpirows && 0 != totalKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, totalKpirows, null, 9,
									provinceName);
						}
						// 'VOLTE统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
						AbstractPageList volteKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("volteKpi", volteKpi);
						List volteKpirows = volteKpi.getRows();
						if (null != volteKpirows && 0 != volteKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, volteKpirows, null, 9,
									provinceName);
						}
						// 'CS域语音统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
						AbstractPageList csKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("csKpi", csKpi);
						List csKpirows = csKpi.getRows();
						if (null != csKpirows && 0 != csKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, csKpirows, null, 9,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 9,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 9,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									9, provinceName);
						}
						// 'MOS统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
						AbstractPageList mosKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("mosKpi", mosKpi);
						List mosKpirows = mosKpi.getRows();
						if (null != mosKpirows && 0 != mosKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, mosKpirows, null, 9,
									provinceName);
						}
					} else {
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}

			}
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("totalKpi", new EasyuiPageList());
			addMap.put("volteKpi", new EasyuiPageList());
			addMap.put("csKpi", new EasyuiPageList());
			addMap.put("coverKpi", new EasyuiPageList());
			addMap.put("disturbKpi", new EasyuiPageList());
			addMap.put("dispatcherKpi", new EasyuiPageList());
			addMap.put("mosKpi", new EasyuiPageList());
			for (Map<String, AbstractPageList> map2 : list) {
				if (map2 != null && map2.size() != 0) {
					if (null != map2.get("totalKpi").getRows()
							&& 0 != map2.get("totalKpi").getRows().size()) {
						addMap.get("totalKpi").getRows()
								.addAll(map2.get("totalKpi").getRows());
					}
					if (null != map2.get("volteKpi").getRows()
							&& 0 != map2.get("volteKpi").getRows().size()) {
						addMap.get("volteKpi").getRows()
								.addAll(map2.get("volteKpi").getRows());
					}
					if (null != map2.get("csKpi").getRows()
							&& 0 != map2.get("csKpi").getRows().size()) {
						addMap.get("csKpi").getRows()
								.addAll(map2.get("csKpi").getRows());
					}
					if (null != map2.get("coverKpi").getRows()
							&& 0 != map2.get("coverKpi").getRows().size()) {
						addMap.get("coverKpi").getRows()
								.addAll(map2.get("coverKpi").getRows());
					}
					if (map2.get("disturbKpi") != null) {
						if (null != map2.get("disturbKpi").getRows()
								&& 0 != map2.get("disturbKpi").getRows().size()) {
							addMap.get("disturbKpi").getRows()
									.addAll(map2.get("disturbKpi").getRows());
						}
					}
					if (null != map2.get("dispatcherKpi").getRows()
							&& 0 != map2.get("dispatcherKpi").getRows().size()) {
						addMap.get("dispatcherKpi").getRows()
								.addAll(map2.get("dispatcherKpi").getRows());
					}
					if (null != map2.get("mosKpi").getRows()
							&& 0 != map2.get("mosKpi").getRows().size()) {
						addMap.get("mosKpi").getRows()
								.addAll(map2.get("mosKpi").getRows());
					}
				}

			}
			map.put("info", addMap);
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("totalKpi", new EasyuiPageList());
			addMap.put("volteKpi", new EasyuiPageList());
			addMap.put("csKpi", new EasyuiPageList());
			addMap.put("coverKpi", new EasyuiPageList());
			addMap.put("disturbKpi", new EasyuiPageList());
			addMap.put("dispatcherKpi", new EasyuiPageList());
			addMap.put("mosKpi", new EasyuiPageList());
			map.put("info", addMap);
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * 查询语音信息公用
	 * 
	 * @param testLogItemIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, AbstractPageList> getVoiceKpi(String testLogItemIds,
			List<TestLogItem> queryTestLogItems, String collectType,
			Integer index) {
		Map<String, AbstractPageList> map = new HashMap<>();
		if (null != testLogItemIds) {
			VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
			lteWholePreviewParam.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
			lteWholePreviewParam.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
			// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
			lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
			// 'KPI汇总'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
			AbstractPageList totalKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("totalKpi", totalKpi);
			List totalKpirows = totalKpi.getRows();
			if (null != totalKpirows && 0 != totalKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						totalKpirows, collectType, index, null);
			}
			// 'VOLTE统计指标'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
			AbstractPageList volteKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("volteKpi", volteKpi);
			List volteKpirows = volteKpi.getRows();
			if (null != volteKpirows && 0 != volteKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						volteKpirows, collectType, index, null);
			}
			// 'CS域语音统计指标'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
			AbstractPageList csKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("csKpi", csKpi);
			List csKpirows = csKpi.getRows();
			if (null != csKpirows && 0 != csKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						csKpirows, collectType, index, null);
			}
			// '覆盖类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
			AbstractPageList coverKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("coverKpi", coverKpi);
			List coverKpirows = coverKpi.getRows();
			if (null != coverKpirows && 0 != coverKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						coverKpirows, collectType, index, null);
			}
			// '干扰类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
			AbstractPageList disturbKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("disturbKpi", disturbKpi);
			List disturbKpirows = disturbKpi.getRows();
			if (null != disturbKpirows && 0 != disturbKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						disturbKpirows, collectType, index, null);
			}
			// '调度类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
			AbstractPageList dispatcherKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("dispatcherKpi", dispatcherKpi);
			List dispatcherKpirows = dispatcherKpi.getRows();
			if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						dispatcherKpirows, collectType, index, null);
			}
			// 'MOS统计'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
			AbstractPageList mosKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("mosKpi", mosKpi);
			List mosKpirows = mosKpi.getRows();
			if (null != mosKpirows && 0 != mosKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						mosKpirows, collectType, index, null);
			}
		} else {
			map.put("totalKpi", new EasyuiPageList());
			map.put("volteKpi", new EasyuiPageList());
			map.put("csKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("mosKpi", new EasyuiPageList());
		}
		return map;
	}

	/**
	 * 查询视频报表信息
	 */
	public String quaryVideoKpi() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		String collectTypes = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("collectTypes");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveVideoTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkVideoTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomVideoTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		List<Map<String, AbstractPageList>> list = new ArrayList<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// 收集汇总方式
				map.put("collectTypes", ServletActionContext.getRequest()
						.getSession().getAttribute("collectTypes"));
				// 汇总其他信息
				if (collectTypes.indexOf("1") != -1) {
					list.add(getVideoKpi(testLogItemIds, queryTestLogItems,
							"全国", 1));
				}
				if (collectTypes.indexOf("2") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()))
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 2);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("3") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 3);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("4") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 4);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("5") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 5);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("6") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						// 汇总其他信息
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
						// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
						AbstractPageList statisticeKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("statisticeKpi", statisticeKpi);
						List statisticeKpirows = statisticeKpi.getRows();
						if (null != statisticeKpirows
								&& 0 != statisticeKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, statisticeKpirows, null,
									6, provinceName);
						}
						// '质量类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
						AbstractPageList qualityKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("qualityKpi", qualityKpi);
						List qualityKpirows = qualityKpi.getRows();
						if (null != qualityKpirows
								&& 0 != qualityKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, qualityKpirows, null, 6,
									provinceName);
						}
						// '资源类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
						AbstractPageList resourceKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("resourceKpi", resourceKpi);
						List resourceKpirows = resourceKpi.getRows();
						if (null != resourceKpirows
								&& 0 != resourceKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, resourceKpirows, null,
									6, provinceName);
						}
					} else {
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("7") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFloorName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFloorName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFloorName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 7);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("8") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_KEYPOINT_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.KEYPOINTNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
						AbstractPageList statisticeKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("statisticeKpi", statisticeKpi);
						List statisticeKpirows = statisticeKpi.getRows();
						if (null != statisticeKpirows
								&& 0 != statisticeKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, statisticeKpirows, null,
									8, provinceName);
						}
						// '质量类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
						AbstractPageList qualityKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("qualityKpi", qualityKpi);
						List qualityKpirows = qualityKpi.getRows();
						if (null != qualityKpirows
								&& 0 != qualityKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, qualityKpirows, null, 8,
									provinceName);
						}
						// '资源类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
						AbstractPageList resourceKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("resourceKpi", resourceKpi);
						List resourceKpirows = resourceKpi.getRows();
						if (null != resourceKpirows
								&& 0 != resourceKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, resourceKpirows, null,
									8, provinceName);
						}
					} else {
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("9") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.FLOORNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
						AbstractPageList statisticeKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("statisticeKpi", statisticeKpi);
						List statisticeKpirows = statisticeKpi.getRows();
						if (null != statisticeKpirows
								&& 0 != statisticeKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, statisticeKpirows, null,
									9, provinceName);
						}
						// '质量类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
						AbstractPageList qualityKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("qualityKpi", qualityKpi);
						List qualityKpirows = qualityKpi.getRows();
						if (null != qualityKpirows
								&& 0 != qualityKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, qualityKpirows, null, 9,
									provinceName);
						}
						// '资源类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
						AbstractPageList resourceKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("resourceKpi", resourceKpi);
						List resourceKpirows = resourceKpi.getRows();
						if (null != resourceKpirows
								&& 0 != resourceKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, resourceKpirows, null,
									9, provinceName);
						}
					} else {
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}

			}
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("statisticeKpi", new EasyuiPageList());
			addMap.put("qualityKpi", new EasyuiPageList());
			addMap.put("resourceKpi", new EasyuiPageList());
			for (Map<String, AbstractPageList> map2 : list) {
				if (map2 != null && map2.size() != 0) {
					if (null != map2.get("statisticeKpi").getRows()
							&& 0 != map2.get("statisticeKpi").getRows().size()) {
						addMap.get("statisticeKpi").getRows()
								.addAll(map2.get("statisticeKpi").getRows());
					}
					if (null != map2.get("qualityKpi").getRows()
							&& 0 != map2.get("qualityKpi").getRows().size()) {
						addMap.get("qualityKpi").getRows()
								.addAll(map2.get("qualityKpi").getRows());
					}
					if (null != map2.get("resourceKpi").getRows()
							&& 0 != map2.get("resourceKpi").getRows().size()) {
						addMap.get("resourceKpi").getRows()
								.addAll(map2.get("resourceKpi").getRows());
					}
				}

			}
			map.put("info", addMap);
		} else {
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("statisticeKpi", new EasyuiPageList());
			addMap.put("qualityKpi", new EasyuiPageList());
			addMap.put("resourceKpi", new EasyuiPageList());
			map.put("info", addMap);
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * 查询视频信息公用
	 * 
	 * @param testLogItemIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, AbstractPageList> getVideoKpi(String testLogItemIds,
			List<TestLogItem> queryTestLogItems, String collectType,
			Integer index) {
		Map<String, AbstractPageList> map = new HashMap<>();
		if (null != testLogItemIds) {
			// 汇总其他信息
			VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
			lteWholePreviewParam.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
			lteWholePreviewParam
					.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
			// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
			lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
			// 'KPI统计'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
			AbstractPageList statisticeKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("statisticeKpi", statisticeKpi);
			List statisticeKpirows = statisticeKpi.getRows();
			if (null != statisticeKpirows && 0 != statisticeKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						statisticeKpirows, collectType, index, null);
			}
			// '质量类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
			AbstractPageList qualityKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("qualityKpi", qualityKpi);
			List qualityKpirows = qualityKpi.getRows();
			if (null != qualityKpirows && 0 != qualityKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						qualityKpirows, collectType, index, null);
			}
			// '资源类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
			AbstractPageList resourceKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("resourceKpi", resourceKpi);
			List resourceKpirows = resourceKpi.getRows();
			if (null != resourceKpirows && 0 != resourceKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						resourceKpirows, collectType, index, null);
			}
		} else {
			map.put("statisticeKpi", new EasyuiPageList());
			map.put("qualityKpi", new EasyuiPageList());
			map.put("resourceKpi", new EasyuiPageList());
		}
		return map;
	}

	/**
	 * 查询数据业务报表信息
	 */
	public String quaryDataKpi() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		String collectTypes = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("collectTypes");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveDataTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkDataTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomDataTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		List<Map<String, AbstractPageList>> list = new ArrayList<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// 收集汇总方式
				map.put("collectTypes", ServletActionContext.getRequest()
						.getSession().getAttribute("collectTypes"));
				// 汇总其他信息
				if (collectTypes.indexOf("1") != -1) {
					list.add(getDataKpi(testLogItemIds, queryTestLogItems,
							"全国", 1));
				}
				if (collectTypes.indexOf("2") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()))
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 2);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("3") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 3);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("4") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 4);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("5") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 5);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("6") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						// 汇总其他信息
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_LTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// '数据业务统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
						AbstractPageList indexKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("indexKpi", indexKpi);
						List indexKpirows = indexKpi.getRows();
						if (null != indexKpirows && 0 != indexKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, indexKpirows, null, 6,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 6,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 6,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									6, provinceName);
						}
						// '移动类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList moveKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("moveKpi", moveKpi);
						List moveKpirows = moveKpi.getRows();
						if (null != moveKpirows && 0 != moveKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, moveKpirows, null, 6,
									provinceName);
						}
						// '接入类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList insertKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, insertKpirows, null, 6,
									provinceName);
						}
						// '应用层FTP速率分段占比'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
						AbstractPageList ftpSectionKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("ftpSectionKpi", ftpSectionKpi);
						List ftpSectionKpirows = ftpSectionKpi.getRows();
						if (null != ftpSectionKpirows
								&& 0 != ftpSectionKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, ftpSectionKpirows, null,
									6, provinceName);
						}
						// '网络覆盖类分段占比统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
						AbstractPageList networkWerkcoverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("networkWerkcoverKpi", networkWerkcoverKpi);
						List networkWerkcoverKpirows = networkWerkcoverKpi
								.getRows();
						if (null != networkWerkcoverKpirows
								&& 0 != networkWerkcoverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, networkWerkcoverKpirows,
									null, 6, provinceName);
						}
						// '停车测试'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
						AbstractPageList stopKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("stopKpi", stopKpi);
						List stopKpirows = stopKpi.getRows();
						if (null != stopKpirows && 0 != stopKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, stopKpirows, null, 6,
									provinceName);
						}
					} else {
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("7") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFileName()
											+ "," + testLogItem.getFloorName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFileName()
											+ "," + testLogItem.getFloorName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 7);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("8") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_KEYPOINT_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_LTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.RECSEQNO + ","
										+ ParamConstant.KEYPOINTNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// '数据业务统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
						AbstractPageList indexKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("indexKpi", indexKpi);
						List indexKpirows = indexKpi.getRows();
						if (null != indexKpirows && 0 != indexKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, indexKpirows, null, 8,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 8,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 8,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									8, provinceName);
						}
						// '移动类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList moveKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("moveKpi", moveKpi);
						List moveKpirows = moveKpi.getRows();
						if (null != moveKpirows && 0 != moveKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, moveKpirows, null, 8,
									provinceName);
						}
						// '接入类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList insertKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, insertKpirows, null, 8,
									provinceName);
						}
						// '应用层FTP速率分段占比'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
						AbstractPageList ftpSectionKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("ftpSectionKpi", ftpSectionKpi);
						List ftpSectionKpirows = ftpSectionKpi.getRows();
						if (null != ftpSectionKpirows
								&& 0 != ftpSectionKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, ftpSectionKpirows, null,
									8, provinceName);
						}
						// '网络覆盖类分段占比统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
						AbstractPageList networkWerkcoverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("networkWerkcoverKpi", networkWerkcoverKpi);
						List networkWerkcoverKpirows = networkWerkcoverKpi
								.getRows();
						if (null != networkWerkcoverKpirows
								&& 0 != networkWerkcoverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, networkWerkcoverKpirows,
									null, 8, provinceName);
						}
						// '停车测试'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
						AbstractPageList stopKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("stopKpi", stopKpi);
						List stopKpirows = stopKpi.getRows();
						if (null != stopKpirows && 0 != stopKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, stopKpirows, null, 8,
									provinceName);
						}
					} else {
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("9") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_LTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.RECSEQNO + ","
										+ ParamConstant.KEYPOINTNO + ","
										+ ParamConstant.FLOORNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// '数据业务统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
						AbstractPageList indexKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("indexKpi", indexKpi);
						List indexKpirows = indexKpi.getRows();
						if (null != indexKpirows && 0 != indexKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, indexKpirows, null, 9,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 9,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 9,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									9, provinceName);
						}
						// '移动类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList moveKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("moveKpi", moveKpi);
						List moveKpirows = moveKpi.getRows();
						if (null != moveKpirows && 0 != moveKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, moveKpirows, null, 9,
									provinceName);
						}
						// '接入类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList insertKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, insertKpirows, null, 9,
									provinceName);
						}
						// '应用层FTP速率分段占比'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
						AbstractPageList ftpSectionKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("ftpSectionKpi", ftpSectionKpi);
						List ftpSectionKpirows = ftpSectionKpi.getRows();
						if (null != ftpSectionKpirows
								&& 0 != ftpSectionKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, ftpSectionKpirows, null,
									9, provinceName);
						}
						// '网络覆盖类分段占比统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
						AbstractPageList networkWerkcoverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("networkWerkcoverKpi", networkWerkcoverKpi);
						List networkWerkcoverKpirows = networkWerkcoverKpi
								.getRows();
						if (null != networkWerkcoverKpirows
								&& 0 != networkWerkcoverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, networkWerkcoverKpirows,
									null, 9, provinceName);
						}
						// '停车测试'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
						AbstractPageList stopKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("stopKpi", stopKpi);
						List stopKpirows = stopKpi.getRows();
						if (null != stopKpirows && 0 != stopKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, stopKpirows, null, 9,
									provinceName);
						}
					} else {
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}

			}
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("indexKpi", new EasyuiPageList());
			addMap.put("coverKpi", new EasyuiPageList());
			addMap.put("disturbKpi", new EasyuiPageList());
			addMap.put("dispatcherKpi", new EasyuiPageList());
			addMap.put("moveKpi", new EasyuiPageList());
			addMap.put("insertKpi", new EasyuiPageList());
			addMap.put("ftpSectionKpi", new EasyuiPageList());
			addMap.put("networkWerkcoverKpi", new EasyuiPageList());
			addMap.put("stopKpi", new EasyuiPageList());
			for (Map<String, AbstractPageList> map2 : list) {
				if (map2 != null && map2.size() != 0) {
					if (null != map2.get("indexKpi").getRows()
							&& 0 != map2.get("indexKpi").getRows().size()) {
						addMap.get("indexKpi").getRows()
								.addAll(map2.get("indexKpi").getRows());
					}
					if (null != map2.get("coverKpi").getRows()
							&& 0 != map2.get("coverKpi").getRows().size()) {
						addMap.get("coverKpi").getRows()
								.addAll(map2.get("coverKpi").getRows());
					}
					if (null != map2.get("disturbKpi").getRows()
							&& 0 != map2.get("disturbKpi").getRows().size()) {
						addMap.get("disturbKpi").getRows()
								.addAll(map2.get("disturbKpi").getRows());
					}
					if (null != map2.get("dispatcherKpi").getRows()
							&& 0 != map2.get("dispatcherKpi").getRows().size()) {
						addMap.get("dispatcherKpi").getRows()
								.addAll(map2.get("dispatcherKpi").getRows());
					}
					if (null != map2.get("moveKpi").getRows()
							&& 0 != map2.get("moveKpi").getRows().size()) {
						addMap.get("moveKpi").getRows()
								.addAll(map2.get("moveKpi").getRows());
					}
					if (null != map2.get("insertKpi").getRows()
							&& 0 != map2.get("insertKpi").getRows().size()) {
						addMap.get("insertKpi").getRows()
								.addAll(map2.get("insertKpi").getRows());
					}
					if (null != map2.get("ftpSectionKpi").getRows()
							&& 0 != map2.get("ftpSectionKpi").getRows().size()) {
						addMap.get("ftpSectionKpi").getRows()
								.addAll(map2.get("ftpSectionKpi").getRows());
					}
					if (null != map2.get("networkWerkcoverKpi").getRows()
							&& 0 != map2.get("networkWerkcoverKpi").getRows()
									.size()) {
						addMap.get("networkWerkcoverKpi")
								.getRows()
								.addAll(map2.get("networkWerkcoverKpi")
										.getRows());
					}
					if (null != map2.get("stopKpi").getRows()
							&& 0 != map2.get("stopKpi").getRows().size()) {
						addMap.get("stopKpi").getRows()
								.addAll(map2.get("stopKpi").getRows());
					}
				}

			}
			map.put("info", addMap);
		} else {
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("indexKpi", new EasyuiPageList());
			addMap.put("coverKpi", new EasyuiPageList());
			addMap.put("disturbKpi", new EasyuiPageList());
			addMap.put("dispatcherKpi", new EasyuiPageList());
			addMap.put("moveKpi", new EasyuiPageList());
			addMap.put("insertKpi", new EasyuiPageList());
			addMap.put("ftpSectionKpi", new EasyuiPageList());
			addMap.put("networkWerkcoverKpi", new EasyuiPageList());
			addMap.put("stopKpi", new EasyuiPageList());
			map.put("info", addMap);
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * 查询数据业务信息公用
	 * 
	 * @param testLogItemIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, AbstractPageList> getDataKpi(String testLogItemIds,
			List<TestLogItem> queryTestLogItems, String collectType,
			Integer index) {
		Map<String, AbstractPageList> map = new HashMap<>();
		if (null != testLogItemIds) {
			// 汇总其他信息
			VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
			lteWholePreviewParam.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
			lteWholePreviewParam.setReportType(ParamConstant.REPOR_TTYPE_LTE);
			// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
			lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
			// '数据业务统计指标'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
			AbstractPageList indexKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("indexKpi", indexKpi);
			List indexKpirows = indexKpi.getRows();
			if (null != indexKpirows && 0 != indexKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						indexKpirows, collectType, index, null);
			}
			// '覆盖类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
			AbstractPageList coverKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("coverKpi", coverKpi);
			List coverKpirows = coverKpi.getRows();
			if (null != coverKpirows && 0 != coverKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						coverKpirows, collectType, index, null);
			}
			// '干扰类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
			AbstractPageList disturbKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("disturbKpi", disturbKpi);
			List disturbKpirows = disturbKpi.getRows();
			if (null != disturbKpirows && 0 != disturbKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						disturbKpirows, collectType, index, null);
			}
			// '调度类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
			AbstractPageList dispatcherKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("dispatcherKpi", dispatcherKpi);
			List dispatcherKpirows = dispatcherKpi.getRows();
			if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						dispatcherKpirows, collectType, index, null);
			}
			// '移动类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
			AbstractPageList moveKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("moveKpi", moveKpi);
			List moveKpirows = moveKpi.getRows();
			if (null != moveKpirows && 0 != moveKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						moveKpirows, collectType, index, null);
			}
			// '接入类'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
			AbstractPageList insertKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("insertKpi", insertKpi);
			List insertKpirows = insertKpi.getRows();
			if (null != insertKpirows && 0 != insertKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						insertKpirows, collectType, index, null);
			}
			// '应用层FTP速率分段占比'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
			AbstractPageList ftpSectionKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("ftpSectionKpi", ftpSectionKpi);
			List ftpSectionKpirows = ftpSectionKpi.getRows();
			if (null != ftpSectionKpirows && 0 != ftpSectionKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						ftpSectionKpirows, collectType, index, null);
			}
			// '网络覆盖类分段占比统计'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
			AbstractPageList networkWerkcoverKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("networkWerkcoverKpi", networkWerkcoverKpi);
			List networkWerkcoverKpirows = networkWerkcoverKpi.getRows();
			if (null != networkWerkcoverKpirows
					&& 0 != networkWerkcoverKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						networkWerkcoverKpirows, collectType, index, null);
			}
			// '停车测试'sheet
			lteWholePreviewParam
					.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
			AbstractPageList stopKpi = cqtReportService
					.queryKpi(lteWholePreviewParam);
			map.put("stopKpi", stopKpi);
			List stopKpirows = stopKpi.getRows();
			if (null != stopKpirows && 0 != stopKpirows.size()) {
				TestLogItemUtils.amountInfoByCollectTypes(queryTestLogItems,
						stopKpirows, collectType, index, null);
			}
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
		return map;
	}

	/**
	 * lte数据详细报表导出
	 * 
	 * @return
	 */
	public String downloadLteDataTotalExcel() {
		return "downloadLteDataTotal";
	}

	/**
	 * lte数据详细报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadLteDataTotal() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		String collectTypes = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("collectTypes");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveDataTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkDataTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomDataTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		List<Map<String, AbstractPageList>> list = new ArrayList<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// 收集汇总方式
				map.put("collectTypes", ServletActionContext.getRequest()
						.getSession().getAttribute("collectTypes"));
				// 汇总其他信息
				if (collectTypes.indexOf("1") != -1) {
					list.add(getDataKpi(testLogItemIds, queryTestLogItems,
							"全国", 1));
				}
				if (collectTypes.indexOf("2") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()))
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 2);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("3") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 3);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("4") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 4);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("5") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 5);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("6") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						// 汇总其他信息
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_LTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// '数据业务统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
						AbstractPageList indexKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("indexKpi", indexKpi);
						List indexKpirows = indexKpi.getRows();
						if (null != indexKpirows && 0 != indexKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, indexKpirows, null, 6,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 6,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 6,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									6, provinceName);
						}
						// '移动类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList moveKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("moveKpi", moveKpi);
						List moveKpirows = moveKpi.getRows();
						if (null != moveKpirows && 0 != moveKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, moveKpirows, null, 6,
									provinceName);
						}
						// '接入类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList insertKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, insertKpirows, null, 6,
									provinceName);
						}
						// '应用层FTP速率分段占比'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
						AbstractPageList ftpSectionKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("ftpSectionKpi", ftpSectionKpi);
						List ftpSectionKpirows = ftpSectionKpi.getRows();
						if (null != ftpSectionKpirows
								&& 0 != ftpSectionKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, ftpSectionKpirows, null,
									6, provinceName);
						}
						// '网络覆盖类分段占比统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
						AbstractPageList networkWerkcoverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("networkWerkcoverKpi", networkWerkcoverKpi);
						List networkWerkcoverKpirows = networkWerkcoverKpi
								.getRows();
						if (null != networkWerkcoverKpirows
								&& 0 != networkWerkcoverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, networkWerkcoverKpirows,
									null, 6, provinceName);
						}
						// '停车测试'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
						AbstractPageList stopKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("stopKpi", stopKpi);
						List stopKpirows = stopKpi.getRows();
						if (null != stopKpirows && 0 != stopKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, stopKpirows, null, 6,
									provinceName);
						}
					} else {
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("7") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFileName()
											+ "," + testLogItem.getFloorName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFileName()
											+ "," + testLogItem.getFloorName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("indexKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("moveKpi", new EasyuiPageList());
					addMap.put("insertKpi", new EasyuiPageList());
					addMap.put("ftpSectionKpi", new EasyuiPageList());
					addMap.put("networkWerkcoverKpi", new EasyuiPageList());
					addMap.put("stopKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getDataKpi(
								buStringBuilder.toString(), value, key, 7);
						if (null != voiceKpi.get("indexKpi").getRows()
								&& 0 != voiceKpi.get("indexKpi").getRows()
										.size()) {
							addMap.get("indexKpi").getRows()
									.addAll(voiceKpi.get("indexKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("moveKpi").getRows()
								&& 0 != voiceKpi.get("moveKpi").getRows()
										.size()) {
							addMap.get("moveKpi").getRows()
									.addAll(voiceKpi.get("moveKpi").getRows());
						}
						if (null != voiceKpi.get("insertKpi").getRows()
								&& 0 != voiceKpi.get("insertKpi").getRows()
										.size()) {
							addMap.get("insertKpi")
									.getRows()
									.addAll(voiceKpi.get("insertKpi").getRows());
						}
						if (null != voiceKpi.get("ftpSectionKpi").getRows()
								&& 0 != voiceKpi.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(voiceKpi.get("ftpSectionKpi")
											.getRows());
						}
						if (null != voiceKpi.get("networkWerkcoverKpi")
								.getRows()
								&& 0 != voiceKpi.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(voiceKpi.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != voiceKpi.get("stopKpi").getRows()
								&& 0 != voiceKpi.get("stopKpi").getRows()
										.size()) {
							addMap.get("stopKpi").getRows()
									.addAll(voiceKpi.get("stopKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("8") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_KEYPOINT_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_LTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.RECSEQNO + ","
										+ ParamConstant.KEYPOINTNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// '数据业务统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
						AbstractPageList indexKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("indexKpi", indexKpi);
						List indexKpirows = indexKpi.getRows();
						if (null != indexKpirows && 0 != indexKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, indexKpirows, null, 8,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 8,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 8,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									8, provinceName);
						}
						// '移动类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList moveKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("moveKpi", moveKpi);
						List moveKpirows = moveKpi.getRows();
						if (null != moveKpirows && 0 != moveKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, moveKpirows, null, 8,
									provinceName);
						}
						// '接入类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList insertKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, insertKpirows, null, 8,
									provinceName);
						}
						// '应用层FTP速率分段占比'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
						AbstractPageList ftpSectionKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("ftpSectionKpi", ftpSectionKpi);
						List ftpSectionKpirows = ftpSectionKpi.getRows();
						if (null != ftpSectionKpirows
								&& 0 != ftpSectionKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, ftpSectionKpirows, null,
									8, provinceName);
						}
						// '网络覆盖类分段占比统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
						AbstractPageList networkWerkcoverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("networkWerkcoverKpi", networkWerkcoverKpi);
						List networkWerkcoverKpirows = networkWerkcoverKpi
								.getRows();
						if (null != networkWerkcoverKpirows
								&& 0 != networkWerkcoverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, networkWerkcoverKpirows,
									null, 8, provinceName);
						}
						// '停车测试'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
						AbstractPageList stopKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("stopKpi", stopKpi);
						List stopKpirows = stopKpi.getRows();
						if (null != stopKpirows && 0 != stopKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, stopKpirows, null, 8,
									provinceName);
						}
					} else {
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("9") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_LTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.RECSEQNO + ","
										+ ParamConstant.KEYPOINTNO + ","
										+ ParamConstant.FLOORNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// '数据业务统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
						AbstractPageList indexKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("indexKpi", indexKpi);
						List indexKpirows = indexKpi.getRows();
						if (null != indexKpirows && 0 != indexKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, indexKpirows, null, 9,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 9,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 9,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									9, provinceName);
						}
						// '移动类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList moveKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("moveKpi", moveKpi);
						List moveKpirows = moveKpi.getRows();
						if (null != moveKpirows && 0 != moveKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, moveKpirows, null, 9,
									provinceName);
						}
						// '接入类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList insertKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("insertKpi", insertKpi);
						List insertKpirows = insertKpi.getRows();
						if (null != insertKpirows && 0 != insertKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, insertKpirows, null, 9,
									provinceName);
						}
						// '应用层FTP速率分段占比'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
						AbstractPageList ftpSectionKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("ftpSectionKpi", ftpSectionKpi);
						List ftpSectionKpirows = ftpSectionKpi.getRows();
						if (null != ftpSectionKpirows
								&& 0 != ftpSectionKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, ftpSectionKpirows, null,
									9, provinceName);
						}
						// '网络覆盖类分段占比统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
						AbstractPageList networkWerkcoverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("networkWerkcoverKpi", networkWerkcoverKpi);
						List networkWerkcoverKpirows = networkWerkcoverKpi
								.getRows();
						if (null != networkWerkcoverKpirows
								&& 0 != networkWerkcoverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, networkWerkcoverKpirows,
									null, 9, provinceName);
						}
						// '停车测试'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
						AbstractPageList stopKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("stopKpi", stopKpi);
						List stopKpirows = stopKpi.getRows();
						if (null != stopKpirows && 0 != stopKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, stopKpirows, null, 9,
									provinceName);
						}
					} else {
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}

			}
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("indexKpi", new EasyuiPageList());
			addMap.put("coverKpi", new EasyuiPageList());
			addMap.put("disturbKpi", new EasyuiPageList());
			addMap.put("dispatcherKpi", new EasyuiPageList());
			addMap.put("moveKpi", new EasyuiPageList());
			addMap.put("insertKpi", new EasyuiPageList());
			addMap.put("ftpSectionKpi", new EasyuiPageList());
			addMap.put("networkWerkcoverKpi", new EasyuiPageList());
			addMap.put("stopKpi", new EasyuiPageList());
			for (Map<String, AbstractPageList> map2 : list) {
				if (map2 != null && map2.size() != 0) {
					if (null != map2.get("indexKpi").getRows()
							&& 0 != map2.get("indexKpi").getRows().size()) {
						addMap.get("indexKpi").getRows()
								.addAll(map2.get("indexKpi").getRows());
					}
					if (null != map2.get("coverKpi").getRows()
							&& 0 != map2.get("coverKpi").getRows().size()) {
						addMap.get("coverKpi").getRows()
								.addAll(map2.get("coverKpi").getRows());
					}
					if (null != map2.get("disturbKpi").getRows()
							&& 0 != map2.get("disturbKpi").getRows().size()) {
						addMap.get("disturbKpi").getRows()
								.addAll(map2.get("disturbKpi").getRows());
					}
					if (null != map2.get("dispatcherKpi").getRows()
							&& 0 != map2.get("dispatcherKpi").getRows().size()) {
						addMap.get("dispatcherKpi").getRows()
								.addAll(map2.get("dispatcherKpi").getRows());
					}
					if (null != map2.get("moveKpi").getRows()
							&& 0 != map2.get("moveKpi").getRows().size()) {
						addMap.get("moveKpi").getRows()
								.addAll(map2.get("moveKpi").getRows());
					}
					if (null != map2.get("insertKpi").getRows()
							&& 0 != map2.get("insertKpi").getRows().size()) {
						addMap.get("insertKpi").getRows()
								.addAll(map2.get("insertKpi").getRows());
					}
					if (null != map2.get("ftpSectionKpi").getRows()
							&& 0 != map2.get("ftpSectionKpi").getRows().size()) {
						addMap.get("ftpSectionKpi").getRows()
								.addAll(map2.get("ftpSectionKpi").getRows());
					}
					if (null != map2.get("networkWerkcoverKpi").getRows()
							&& 0 != map2.get("networkWerkcoverKpi").getRows()
									.size()) {
						addMap.get("networkWerkcoverKpi")
								.getRows()
								.addAll(map2.get("networkWerkcoverKpi")
										.getRows());
					}
					if (null != map2.get("stopKpi").getRows()
							&& 0 != map2.get("stopKpi").getRows().size()) {
						addMap.get("stopKpi").getRows()
								.addAll(map2.get("stopKpi").getRows());
					}
				}

			}
			map.put("info", addMap);
		} else {
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("indexKpi", new EasyuiPageList());
			addMap.put("coverKpi", new EasyuiPageList());
			addMap.put("disturbKpi", new EasyuiPageList());
			addMap.put("dispatcherKpi", new EasyuiPageList());
			addMap.put("moveKpi", new EasyuiPageList());
			addMap.put("insertKpi", new EasyuiPageList());
			addMap.put("ftpSectionKpi", new EasyuiPageList());
			addMap.put("networkWerkcoverKpi", new EasyuiPageList());
			addMap.put("stopKpi", new EasyuiPageList());
			map.put("info", addMap);
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/CQTLTE数据业务报表.xls");
			if (transformToExcel.getNumberOfSheets() != 0) {
				for (int i = 0; i < transformToExcel.getNumberOfSheets(); i++) {
					if (collectTypes.indexOf("1") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(0, true);
					}
					if (collectTypes.indexOf("2") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(1, true);
					}
					if (collectTypes.indexOf("3") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(2, true);
					}
					if (collectTypes.indexOf("4") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(3, true);
					}
					if (collectTypes.indexOf("5") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(4, true);
					}
					if (collectTypes.indexOf("6") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(5, true);
					}
					if (collectTypes.indexOf("7") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(6, true);
					}
					if (collectTypes.indexOf("8") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(7, true);
					}
					if (collectTypes.indexOf("9") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(8, true);
					}
				}
			}
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_CQT_LTE数据业务报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * volte语音详细报表导出
	 * 
	 * @return
	 */
	public String downloadVolteVoiceTotalExcel() {
		return "downloadVolteVoiceTotal";
	}

	/**
	 * volte语音详细报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadVolteVoiceTotal() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		String collectTypes = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("collectTypes");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveVoiceTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkVoiceTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomVoiceTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		List<Map<String, AbstractPageList>> list = new ArrayList<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// 收集汇总方式
				map.put("collectTypes", ServletActionContext.getRequest()
						.getSession().getAttribute("collectTypes"));
				// 汇总其他信息
				if (collectTypes.indexOf("1") != -1) {
					list.add(getVoiceKpi(testLogItemIds, queryTestLogItems,
							"全国", 1));
				}
				if (collectTypes.indexOf("2") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()))
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 2);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("3") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 3);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("4") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 4);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("5") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 5);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("6") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI汇总'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
						AbstractPageList totalKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("totalKpi", totalKpi);
						List totalKpirows = totalKpi.getRows();
						if (null != totalKpirows && 0 != totalKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, totalKpirows, null, 6,
									provinceName);
						}
						// 'VOLTE统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
						AbstractPageList volteKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("volteKpi", volteKpi);
						List volteKpirows = volteKpi.getRows();
						if (null != volteKpirows && 0 != volteKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, volteKpirows, null, 6,
									provinceName);
						}
						// 'CS域语音统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
						AbstractPageList csKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("csKpi", csKpi);
						List csKpirows = csKpi.getRows();
						if (null != csKpirows && 0 != csKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, csKpirows, null, 6,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 6,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						map.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 6,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									6, provinceName);
						}
						// 'MOS统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
						AbstractPageList mosKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("mosKpi", mosKpi);
						List mosKpirows = mosKpi.getRows();
						if (null != mosKpirows && 0 != mosKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, mosKpirows, null, 6,
									provinceName);
						}
					} else {
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("7") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFileName()
											+ "," + testLogItem.getFloorName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFileName()
											+ "," + testLogItem.getFloorName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVoiceKpi(
								buStringBuilder.toString(), value, key, 7);
						if (null != voiceKpi.get("totalKpi").getRows()
								&& 0 != voiceKpi.get("totalKpi").getRows()
										.size()) {
							addMap.get("totalKpi").getRows()
									.addAll(voiceKpi.get("totalKpi").getRows());
						}
						if (null != voiceKpi.get("volteKpi").getRows()
								&& 0 != voiceKpi.get("volteKpi").getRows()
										.size()) {
							addMap.get("volteKpi").getRows()
									.addAll(voiceKpi.get("volteKpi").getRows());
						}
						if (null != voiceKpi.get("csKpi").getRows()
								&& 0 != voiceKpi.get("csKpi").getRows().size()) {
							addMap.get("csKpi").getRows()
									.addAll(voiceKpi.get("csKpi").getRows());
						}
						if (null != voiceKpi.get("coverKpi").getRows()
								&& 0 != voiceKpi.get("coverKpi").getRows()
										.size()) {
							addMap.get("coverKpi").getRows()
									.addAll(voiceKpi.get("coverKpi").getRows());
						}
						if (null != voiceKpi.get("disturbKpi").getRows()
								&& 0 != voiceKpi.get("disturbKpi").getRows()
										.size()) {
							addMap.get("disturbKpi")
									.getRows()
									.addAll(voiceKpi.get("disturbKpi")
											.getRows());
						}
						if (null != voiceKpi.get("dispatcherKpi").getRows()
								&& 0 != voiceKpi.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(voiceKpi.get("dispatcherKpi")
											.getRows());
						}
						if (null != voiceKpi.get("mosKpi").getRows()
								&& 0 != voiceKpi.get("mosKpi").getRows().size()) {
							addMap.get("mosKpi").getRows()
									.addAll(voiceKpi.get("mosKpi").getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("8") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_KEYPOINT_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.KEYPOINTNO + ","
										+ ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI汇总'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
						AbstractPageList totalKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("totalKpi", totalKpi);
						List totalKpirows = totalKpi.getRows();
						if (null != totalKpirows && 0 != totalKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, totalKpirows, null, 8,
									provinceName);
						}
						// 'VOLTE统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
						AbstractPageList volteKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("volteKpi", volteKpi);
						List volteKpirows = volteKpi.getRows();
						if (null != volteKpirows && 0 != volteKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, volteKpirows, null, 8,
									provinceName);
						}
						// 'CS域语音统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
						AbstractPageList csKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("csKpi", csKpi);
						List csKpirows = csKpi.getRows();
						if (null != csKpirows && 0 != csKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, csKpirows, null, 8,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 8,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 8,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									8, provinceName);
						}
						// 'MOS统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
						AbstractPageList mosKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("mosKpi", mosKpi);
						List mosKpirows = mosKpi.getRows();
						if (null != mosKpirows && 0 != mosKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, mosKpirows, null, 8,
									provinceName);
						}
					} else {
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("9") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.KEYPOINTNO + ","
										+ ParamConstant.RECSEQNO + ","
										+ ParamConstant.FLOORNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI汇总'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
						AbstractPageList totalKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("totalKpi", totalKpi);
						List totalKpirows = totalKpi.getRows();
						if (null != totalKpirows && 0 != totalKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, totalKpirows, null, 9,
									provinceName);
						}
						// 'VOLTE统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
						AbstractPageList volteKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("volteKpi", volteKpi);
						List volteKpirows = volteKpi.getRows();
						if (null != volteKpirows && 0 != volteKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, volteKpirows, null, 9,
									provinceName);
						}
						// 'CS域语音统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
						AbstractPageList csKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("csKpi", csKpi);
						List csKpirows = csKpi.getRows();
						if (null != csKpirows && 0 != csKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, csKpirows, null, 9,
									provinceName);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList coverKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("coverKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, coverKpirows, null, 9,
									provinceName);
						}
						// '干扰类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
						AbstractPageList disturbKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("disturbKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows
								&& 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, disturbKpirows, null, 9,
									provinceName);
						}
						// '调度类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
						AbstractPageList dispatcherKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("dispatcherKpi", dispatcherKpi);
						List dispatcherKpirows = dispatcherKpi.getRows();
						if (null != dispatcherKpirows
								&& 0 != dispatcherKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, dispatcherKpirows, null,
									9, provinceName);
						}
						// 'MOS统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
						AbstractPageList mosKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("mosKpi", mosKpi);
						List mosKpirows = mosKpi.getRows();
						if (null != mosKpirows && 0 != mosKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, mosKpirows, null, 9,
									provinceName);
						}
					} else {
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}

			}
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("totalKpi", new EasyuiPageList());
			addMap.put("volteKpi", new EasyuiPageList());
			addMap.put("csKpi", new EasyuiPageList());
			addMap.put("coverKpi", new EasyuiPageList());
			addMap.put("disturbKpi", new EasyuiPageList());
			addMap.put("dispatcherKpi", new EasyuiPageList());
			addMap.put("mosKpi", new EasyuiPageList());
			for (Map<String, AbstractPageList> map2 : list) {
				if (map2 != null && map2.size() != 0) {
					if (null != map2.get("totalKpi").getRows()
							&& 0 != map2.get("totalKpi").getRows().size()) {
						addMap.get("totalKpi").getRows()
								.addAll(map2.get("totalKpi").getRows());
					}
					if (null != map2.get("volteKpi").getRows()
							&& 0 != map2.get("volteKpi").getRows().size()) {
						addMap.get("volteKpi").getRows()
								.addAll(map2.get("volteKpi").getRows());
					}
					if (null != map2.get("csKpi").getRows()
							&& 0 != map2.get("csKpi").getRows().size()) {
						addMap.get("csKpi").getRows()
								.addAll(map2.get("csKpi").getRows());
					}
					if (null != map2.get("coverKpi").getRows()
							&& 0 != map2.get("coverKpi").getRows().size()) {
						addMap.get("coverKpi").getRows()
								.addAll(map2.get("coverKpi").getRows());
					}
					if (map2.get("disturbKpi") != null) {
						if (null != map2.get("disturbKpi").getRows()
								&& 0 != map2.get("disturbKpi").getRows().size()) {
							addMap.get("disturbKpi").getRows()
									.addAll(map2.get("disturbKpi").getRows());
						}
					}
					if (null != map2.get("dispatcherKpi").getRows()
							&& 0 != map2.get("dispatcherKpi").getRows().size()) {
						addMap.get("dispatcherKpi").getRows()
								.addAll(map2.get("dispatcherKpi").getRows());
					}
					if (null != map2.get("mosKpi").getRows()
							&& 0 != map2.get("mosKpi").getRows().size()) {
						addMap.get("mosKpi").getRows()
								.addAll(map2.get("mosKpi").getRows());
					}
				}

			}
			map.put("info", addMap);
			ServletActionContext.getRequest().getSession()
					.setAttribute("KPItestLogItemIds", attribute);
		} else {
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("totalKpi", new EasyuiPageList());
			addMap.put("volteKpi", new EasyuiPageList());
			addMap.put("csKpi", new EasyuiPageList());
			addMap.put("coverKpi", new EasyuiPageList());
			addMap.put("disturbKpi", new EasyuiPageList());
			addMap.put("dispatcherKpi", new EasyuiPageList());
			addMap.put("mosKpi", new EasyuiPageList());
			map.put("info", addMap);
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/CQTVoLTE语音报表.xls");
			int numberOfSheets = transformToExcel.getNumberOfSheets();
			if (transformToExcel.getNumberOfSheets() != 0) {
				for (int i = 0; i < transformToExcel.getNumberOfSheets(); i++) {
					if (collectTypes.indexOf("1") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(0, true);
					}
					if (collectTypes.indexOf("2") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(1, true);
					}
					if (collectTypes.indexOf("3") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(2, true);
					}
					if (collectTypes.indexOf("4") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(3, true);
					}
					if (collectTypes.indexOf("5") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(4, true);
					}
					if (collectTypes.indexOf("6") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(5, true);
					}
					if (collectTypes.indexOf("7") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(6, true);
					}
					if (collectTypes.indexOf("8") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(7, true);
					}
					if (collectTypes.indexOf("9") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(8, true);
					}
				}
			}
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_CQT_VoLTE语音报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * volte视频详细报表导出
	 * 
	 * @return
	 */
	public String downloadVolteVideoTotalExcel() {
		return "downloadVolteVideoTotal";
	}

	/**
	 * volte视频详细报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadVolteVideoTotal() {
		Integer typeNo = (Integer) ServletActionContext.getRequest()
				.getSession().getAttribute("typeNo");
		String collectTypes = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("collectTypes");
		Object attribute = null;
		if (null != typeNo) {
			if (0 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("moveVideoTestLogItemIds");
			} else if (1 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("linkVideoTestLogItemIds");
			} else if (2 == typeNo) {
				attribute = ServletActionContext.getRequest().getSession()
						.getAttribute("telecomVideoTestLogItemIds");
			}
		}
		Map<String, Object> map = new HashMap<>();
		List<Map<String, AbstractPageList>> list = new ArrayList<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testLogItemService
						.queryTestLogItems(testLogItemIds);
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// 收集汇总方式
				map.put("collectTypes", ServletActionContext.getRequest()
						.getSession().getAttribute("collectTypes"));
				// 汇总其他信息
				if (collectTypes.indexOf("1") != -1) {
					list.add(getVideoKpi(testLogItemIds, queryTestLogItems,
							"全国", 1));
				}
				if (collectTypes.indexOf("2") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()))
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 2);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("3") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 3);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("4") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 4);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("5") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 5);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("6") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						// 汇总其他信息
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
						// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
						AbstractPageList statisticeKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("statisticeKpi", statisticeKpi);
						List statisticeKpirows = statisticeKpi.getRows();
						if (null != statisticeKpirows
								&& 0 != statisticeKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, statisticeKpirows, null,
									6, provinceName);
						}
						// '质量类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
						AbstractPageList qualityKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("qualityKpi", qualityKpi);
						List qualityKpirows = qualityKpi.getRows();
						if (null != qualityKpirows
								&& 0 != qualityKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, qualityKpirows, null, 6,
									provinceName);
						}
						// '资源类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
						AbstractPageList resourceKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("resourceKpi", resourceKpi);
						List resourceKpirows = resourceKpi.getRows();
						if (null != resourceKpirows
								&& 0 != resourceKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, resourceKpirows, null,
									6, provinceName);
						}
					} else {
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("7") != -1) {
					Map<String, List<TestLogItem>> logMap = new HashMap<>();
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (logMap.size() != 0) {
							List<TestLogItem> testLogItemList = logMap
									.get("全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFloorName());
							if (testLogItemList != null
									&& testLogItemList.size() != 0) {
								logMap.get(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFloorName())
										.add(testLogItem);
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						} else {
							List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
							arrayList.add(testLogItem);
							logMap.put(
									"全国,"
											+ terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup())
											+ ","
											+ testLogItem.getTerminalGroup()
											+ "," + testLogItem.getBoxId()
											+ ","
											+ testLogItem.getTestPlanName()
											+ "," + testLogItem.getFloorName(),
									arrayList);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map.Entry<String, List<TestLogItem>> entry : logMap
							.entrySet()) {
						List<TestLogItem> value = entry.getValue();
						String key = entry.getKey();
						String valueString = null;
						StringBuilder buStringBuilder = new StringBuilder();
						for (TestLogItem testLogItem : value) {
							if (0 != buStringBuilder.toString().length()) {
								buStringBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								buStringBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
						Map<String, AbstractPageList> voiceKpi = getVideoKpi(
								buStringBuilder.toString(), value, key, 7);
						if (null != voiceKpi.get("statisticeKpi").getRows()
								&& 0 != voiceKpi.get("statisticeKpi").getRows()
										.size()) {
							addMap.get("statisticeKpi")
									.getRows()
									.addAll(voiceKpi.get("statisticeKpi")
											.getRows());
						}
						if (null != voiceKpi.get("qualityKpi").getRows()
								&& 0 != voiceKpi.get("qualityKpi").getRows()
										.size()) {
							addMap.get("qualityKpi")
									.getRows()
									.addAll(voiceKpi.get("qualityKpi")
											.getRows());
						}
						if (null != voiceKpi.get("resourceKpi").getRows()
								&& 0 != voiceKpi.get("resourceKpi").getRows()
										.size()) {
							addMap.get("resourceKpi")
									.getRows()
									.addAll(voiceKpi.get("resourceKpi")
											.getRows());
						}
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("8") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_KEYPOINT_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.KEYPOINTNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
						AbstractPageList statisticeKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("statisticeKpi", statisticeKpi);
						List statisticeKpirows = statisticeKpi.getRows();
						if (null != statisticeKpirows
								&& 0 != statisticeKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, statisticeKpirows, null,
									8, provinceName);
						}
						// '质量类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
						AbstractPageList qualityKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("qualityKpi", qualityKpi);
						List qualityKpirows = qualityKpi.getRows();
						if (null != qualityKpirows
								&& 0 != qualityKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, qualityKpirows, null, 8,
									provinceName);
						}
						// '资源类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
						AbstractPageList resourceKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("resourceKpi", resourceKpi);
						List resourceKpirows = resourceKpi.getRows();
						if (null != resourceKpirows
								&& 0 != resourceKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, resourceKpirows, null,
									8, provinceName);
						}
					} else {
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}
				if (collectTypes.indexOf("9") != -1) {
					Map<String, AbstractPageList> addMap = new HashMap<>();
					if (null != testLogItemIds) {
						Map<String, String> provinceName = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							provinceName
									.put(testLogItem.getBoxId(),
											terminalGroupService
													.getProvinceNameByCityGroup(testLogItem
															.getTerminal()
															.getTerminalGroup()));
						}
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
						lteWholePreviewParam
								.setGroupByField(ParamConstant.FLOORNO);
						lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
						// 'KPI统计'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
						AbstractPageList statisticeKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("statisticeKpi", statisticeKpi);
						List statisticeKpirows = statisticeKpi.getRows();
						if (null != statisticeKpirows
								&& 0 != statisticeKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, statisticeKpirows, null,
									9, provinceName);
						}
						// '质量类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
						AbstractPageList qualityKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("qualityKpi", qualityKpi);
						List qualityKpirows = qualityKpi.getRows();
						if (null != qualityKpirows
								&& 0 != qualityKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, qualityKpirows, null, 9,
									provinceName);
						}
						// '资源类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
						AbstractPageList resourceKpi = cqtReportService
								.queryKpi(lteWholePreviewParam);
						addMap.put("resourceKpi", resourceKpi);
						List resourceKpirows = resourceKpi.getRows();
						if (null != resourceKpirows
								&& 0 != resourceKpirows.size()) {
							TestLogItemUtils.amountInfoByCollectTypes(
									queryTestLogItems, resourceKpirows, null,
									9, provinceName);
						}
					} else {
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
					}
					list.add(addMap);
				}

			}
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("statisticeKpi", new EasyuiPageList());
			addMap.put("qualityKpi", new EasyuiPageList());
			addMap.put("resourceKpi", new EasyuiPageList());
			for (Map<String, AbstractPageList> map2 : list) {
				if (map2 != null && map2.size() != 0) {
					if (null != map2.get("statisticeKpi").getRows()
							&& 0 != map2.get("statisticeKpi").getRows().size()) {
						addMap.get("statisticeKpi").getRows()
								.addAll(map2.get("statisticeKpi").getRows());
					}
					if (null != map2.get("qualityKpi").getRows()
							&& 0 != map2.get("qualityKpi").getRows().size()) {
						addMap.get("qualityKpi").getRows()
								.addAll(map2.get("qualityKpi").getRows());
					}
					if (null != map2.get("resourceKpi").getRows()
							&& 0 != map2.get("resourceKpi").getRows().size()) {
						addMap.get("resourceKpi").getRows()
								.addAll(map2.get("resourceKpi").getRows());
					}
				}

			}
			map.put("info", addMap);
		} else {
			Map<String, AbstractPageList> addMap = new HashMap<>();
			addMap.put("statisticeKpi", new EasyuiPageList());
			addMap.put("qualityKpi", new EasyuiPageList());
			addMap.put("resourceKpi", new EasyuiPageList());
			map.put("info", addMap);
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/CQTVoLTE语音报表.xls");
			int numberOfSheets = transformToExcel.getNumberOfSheets();
			if (transformToExcel.getNumberOfSheets() != 0) {
				for (int i = 0; i < transformToExcel.getNumberOfSheets(); i++) {
					if (collectTypes.indexOf("1") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(0, true);
					}
					if (collectTypes.indexOf("2") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(1, true);
					}
					if (collectTypes.indexOf("3") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(2, true);
					}
					if (collectTypes.indexOf("4") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(3, true);
					}
					if (collectTypes.indexOf("5") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(4, true);
					}
					if (collectTypes.indexOf("6") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(5, true);
					}
					if (collectTypes.indexOf("7") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(6, true);
					}
					if (collectTypes.indexOf("8") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(7, true);
					}
					if (collectTypes.indexOf("9") == -1) {
						transformToExcel.getSheetAt(i).setColumnHidden(8, true);
					}
				}
			}
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("taskName")
					+ "_CQT_VoLTE语音报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * lte数据表单个Sheet报表导出
	 * 
	 * @return
	 */
	public String downloadOneSheetLteDataTotalExcel() {
		return "downloadOneSheetLteDataTotal";
	}

	/**
	 * lte数据表单个Sheet报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetLteDataTotal() {
		if (null != sheetName) {
			Integer typeNo = (Integer) ServletActionContext.getRequest()
					.getSession().getAttribute("typeNo");
			String collectTypes = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("collectTypes");
			Object attribute = null;
			if (null != typeNo) {
				if (0 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("moveDataTestLogItemIds");
				} else if (1 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("linkDataTestLogItemIds");
				} else if (2 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("telecomDataTestLogItemIds");
				}
			}
			Map<String, Object> map = new HashMap<>();
			List<Map<String, AbstractPageList>> list = new ArrayList<>();
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					// 汇总开始时间和结束时间
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(queryTestLogItems));
					// 收集汇总方式
					map.put("collectTypes", ServletActionContext.getRequest()
							.getSession().getAttribute("collectTypes"));
					// 汇总其他信息
					if (collectTypes.indexOf("1") != -1) {
						list.add(getDataKpiForOneSheet(testLogItemIds,
								queryTestLogItems, "全国", 1, sheetName));
					}
					if (collectTypes.indexOf("2") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup()))
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup()),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getDataKpiForOneSheet(
									buStringBuilder.toString(), value, key, 2,
									sheetName);
							if (null != voiceKpi.get("indexKpi").getRows()
									&& 0 != voiceKpi.get("indexKpi").getRows()
											.size()) {
								addMap.get("indexKpi")
										.getRows()
										.addAll(voiceKpi.get("indexKpi")
												.getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("moveKpi").getRows()
									&& 0 != voiceKpi.get("moveKpi").getRows()
											.size()) {
								addMap.get("moveKpi")
										.getRows()
										.addAll(voiceKpi.get("moveKpi")
												.getRows());
							}
							if (null != voiceKpi.get("insertKpi").getRows()
									&& 0 != voiceKpi.get("insertKpi").getRows()
											.size()) {
								addMap.get("insertKpi")
										.getRows()
										.addAll(voiceKpi.get("insertKpi")
												.getRows());
							}
							if (null != voiceKpi.get("ftpSectionKpi").getRows()
									&& 0 != voiceKpi.get("ftpSectionKpi")
											.getRows().size()) {
								addMap.get("ftpSectionKpi")
										.getRows()
										.addAll(voiceKpi.get("ftpSectionKpi")
												.getRows());
							}
							if (null != voiceKpi.get("networkWerkcoverKpi")
									.getRows()
									&& 0 != voiceKpi.get("networkWerkcoverKpi")
											.getRows().size()) {
								addMap.get("networkWerkcoverKpi")
										.getRows()
										.addAll(voiceKpi.get(
												"networkWerkcoverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("stopKpi").getRows()
									&& 0 != voiceKpi.get("stopKpi").getRows()
											.size()) {
								addMap.get("stopKpi")
										.getRows()
										.addAll(voiceKpi.get("stopKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("3") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getDataKpiForOneSheet(
									buStringBuilder.toString(), value, key, 3,
									sheetName);
							if (null != voiceKpi.get("indexKpi").getRows()
									&& 0 != voiceKpi.get("indexKpi").getRows()
											.size()) {
								addMap.get("indexKpi")
										.getRows()
										.addAll(voiceKpi.get("indexKpi")
												.getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("moveKpi").getRows()
									&& 0 != voiceKpi.get("moveKpi").getRows()
											.size()) {
								addMap.get("moveKpi")
										.getRows()
										.addAll(voiceKpi.get("moveKpi")
												.getRows());
							}
							if (null != voiceKpi.get("insertKpi").getRows()
									&& 0 != voiceKpi.get("insertKpi").getRows()
											.size()) {
								addMap.get("insertKpi")
										.getRows()
										.addAll(voiceKpi.get("insertKpi")
												.getRows());
							}
							if (null != voiceKpi.get("ftpSectionKpi").getRows()
									&& 0 != voiceKpi.get("ftpSectionKpi")
											.getRows().size()) {
								addMap.get("ftpSectionKpi")
										.getRows()
										.addAll(voiceKpi.get("ftpSectionKpi")
												.getRows());
							}
							if (null != voiceKpi.get("networkWerkcoverKpi")
									.getRows()
									&& 0 != voiceKpi.get("networkWerkcoverKpi")
											.getRows().size()) {
								addMap.get("networkWerkcoverKpi")
										.getRows()
										.addAll(voiceKpi.get(
												"networkWerkcoverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("stopKpi").getRows()
									&& 0 != voiceKpi.get("stopKpi").getRows()
											.size()) {
								addMap.get("stopKpi")
										.getRows()
										.addAll(voiceKpi.get("stopKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("4") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getDataKpiForOneSheet(
									buStringBuilder.toString(), value, key, 4,
									sheetName);
							if (null != voiceKpi.get("indexKpi").getRows()
									&& 0 != voiceKpi.get("indexKpi").getRows()
											.size()) {
								addMap.get("indexKpi")
										.getRows()
										.addAll(voiceKpi.get("indexKpi")
												.getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("moveKpi").getRows()
									&& 0 != voiceKpi.get("moveKpi").getRows()
											.size()) {
								addMap.get("moveKpi")
										.getRows()
										.addAll(voiceKpi.get("moveKpi")
												.getRows());
							}
							if (null != voiceKpi.get("insertKpi").getRows()
									&& 0 != voiceKpi.get("insertKpi").getRows()
											.size()) {
								addMap.get("insertKpi")
										.getRows()
										.addAll(voiceKpi.get("insertKpi")
												.getRows());
							}
							if (null != voiceKpi.get("ftpSectionKpi").getRows()
									&& 0 != voiceKpi.get("ftpSectionKpi")
											.getRows().size()) {
								addMap.get("ftpSectionKpi")
										.getRows()
										.addAll(voiceKpi.get("ftpSectionKpi")
												.getRows());
							}
							if (null != voiceKpi.get("networkWerkcoverKpi")
									.getRows()
									&& 0 != voiceKpi.get("networkWerkcoverKpi")
											.getRows().size()) {
								addMap.get("networkWerkcoverKpi")
										.getRows()
										.addAll(voiceKpi.get(
												"networkWerkcoverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("stopKpi").getRows()
									&& 0 != voiceKpi.get("stopKpi").getRows()
											.size()) {
								addMap.get("stopKpi")
										.getRows()
										.addAll(voiceKpi.get("stopKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("5") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getDataKpiForOneSheet(
									buStringBuilder.toString(), value, key, 5,
									sheetName);
							if (null != voiceKpi.get("indexKpi").getRows()
									&& 0 != voiceKpi.get("indexKpi").getRows()
											.size()) {
								addMap.get("indexKpi")
										.getRows()
										.addAll(voiceKpi.get("indexKpi")
												.getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("moveKpi").getRows()
									&& 0 != voiceKpi.get("moveKpi").getRows()
											.size()) {
								addMap.get("moveKpi")
										.getRows()
										.addAll(voiceKpi.get("moveKpi")
												.getRows());
							}
							if (null != voiceKpi.get("insertKpi").getRows()
									&& 0 != voiceKpi.get("insertKpi").getRows()
											.size()) {
								addMap.get("insertKpi")
										.getRows()
										.addAll(voiceKpi.get("insertKpi")
												.getRows());
							}
							if (null != voiceKpi.get("ftpSectionKpi").getRows()
									&& 0 != voiceKpi.get("ftpSectionKpi")
											.getRows().size()) {
								addMap.get("ftpSectionKpi")
										.getRows()
										.addAll(voiceKpi.get("ftpSectionKpi")
												.getRows());
							}
							if (null != voiceKpi.get("networkWerkcoverKpi")
									.getRows()
									&& 0 != voiceKpi.get("networkWerkcoverKpi")
											.getRows().size()) {
								addMap.get("networkWerkcoverKpi")
										.getRows()
										.addAll(voiceKpi.get(
												"networkWerkcoverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("stopKpi").getRows()
									&& 0 != voiceKpi.get("stopKpi").getRows()
											.size()) {
								addMap.get("stopKpi")
										.getRows()
										.addAll(voiceKpi.get("stopKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("6") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							// 汇总其他信息
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_LTE);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							// '数据业务统计指标'sheet
							if (sheetName.equals("数据业务统计指标")) {
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
								AbstractPageList indexKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("indexKpi", indexKpi);
								List indexKpirows = indexKpi.getRows();
								if (null != indexKpirows
										&& 0 != indexKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, indexKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("覆盖类")) {
								// '覆盖类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
								AbstractPageList coverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("coverKpi", coverKpi);
								List coverKpirows = coverKpi.getRows();
								if (null != coverKpirows
										&& 0 != coverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, coverKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("干扰类")) {
								// '干扰类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
								AbstractPageList disturbKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("disturbKpi", disturbKpi);
								List disturbKpirows = disturbKpi.getRows();
								if (null != disturbKpirows
										&& 0 != disturbKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, disturbKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("调度类")) {
								// '调度类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
								AbstractPageList dispatcherKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("dispatcherKpi", dispatcherKpi);
								List dispatcherKpirows = dispatcherKpi
										.getRows();
								if (null != dispatcherKpirows
										&& 0 != dispatcherKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											dispatcherKpirows, null, 6,
											provinceName);
								}
							} else if (sheetName.equals("移动类")) {
								// '移动类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
								AbstractPageList moveKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("moveKpi", moveKpi);
								List moveKpirows = moveKpi.getRows();
								if (null != moveKpirows
										&& 0 != moveKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, moveKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("接入类")) {
								// '接入类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
								AbstractPageList insertKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("insertKpi", insertKpi);
								List insertKpirows = insertKpi.getRows();
								if (null != insertKpirows
										&& 0 != insertKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, insertKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("应用层FTP速率分段占比")) {
								// '应用层FTP速率分段占比'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
								AbstractPageList ftpSectionKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("ftpSectionKpi", ftpSectionKpi);
								List ftpSectionKpirows = ftpSectionKpi
										.getRows();
								if (null != ftpSectionKpirows
										&& 0 != ftpSectionKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											ftpSectionKpirows, null, 6,
											provinceName);
								}
							} else if (sheetName.equals("网络覆盖类分段占比统计")) {
								// '网络覆盖类分段占比统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
								AbstractPageList networkWerkcoverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("networkWerkcoverKpi",
										networkWerkcoverKpi);
								List networkWerkcoverKpirows = networkWerkcoverKpi
										.getRows();
								if (null != networkWerkcoverKpirows
										&& 0 != networkWerkcoverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											networkWerkcoverKpirows, null, 6,
											provinceName);
								}
							} else if (sheetName.equals("停车测试")) {
								// '停车测试'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
								AbstractPageList stopKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("stopKpi", stopKpi);
								List stopKpirows = stopKpi.getRows();
								if (null != stopKpirows
										&& 0 != stopKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, stopKpirows,
											null, 6, provinceName);
								}
							}
						} else {

						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("7") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName()
													+ ","
													+ testLogItem.getFileName()
													+ ","
													+ testLogItem
															.getFloorName())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName()
													+ ","
													+ testLogItem.getFileName()
													+ ","
													+ testLogItem
															.getFloorName(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getDataKpiForOneSheet(
									buStringBuilder.toString(), value, key, 7,
									sheetName);
							if (null != voiceKpi.get("indexKpi").getRows()
									&& 0 != voiceKpi.get("indexKpi").getRows()
											.size()) {
								addMap.get("indexKpi")
										.getRows()
										.addAll(voiceKpi.get("indexKpi")
												.getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("moveKpi").getRows()
									&& 0 != voiceKpi.get("moveKpi").getRows()
											.size()) {
								addMap.get("moveKpi")
										.getRows()
										.addAll(voiceKpi.get("moveKpi")
												.getRows());
							}
							if (null != voiceKpi.get("insertKpi").getRows()
									&& 0 != voiceKpi.get("insertKpi").getRows()
											.size()) {
								addMap.get("insertKpi")
										.getRows()
										.addAll(voiceKpi.get("insertKpi")
												.getRows());
							}
							if (null != voiceKpi.get("ftpSectionKpi").getRows()
									&& 0 != voiceKpi.get("ftpSectionKpi")
											.getRows().size()) {
								addMap.get("ftpSectionKpi")
										.getRows()
										.addAll(voiceKpi.get("ftpSectionKpi")
												.getRows());
							}
							if (null != voiceKpi.get("networkWerkcoverKpi")
									.getRows()
									&& 0 != voiceKpi.get("networkWerkcoverKpi")
											.getRows().size()) {
								addMap.get("networkWerkcoverKpi")
										.getRows()
										.addAll(voiceKpi.get(
												"networkWerkcoverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("stopKpi").getRows()
									&& 0 != voiceKpi.get("stopKpi").getRows()
											.size()) {
								addMap.get("stopKpi")
										.getRows()
										.addAll(voiceKpi.get("stopKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("8") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_KEYPOINT_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_LTE);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO
											+ "," + ParamConstant.KEYPOINTNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							// '数据业务统计指标'sheet
							if (sheetName.equals("数据业务统计指标")) {
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
								AbstractPageList indexKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("indexKpi", indexKpi);
								List indexKpirows = indexKpi.getRows();
								if (null != indexKpirows
										&& 0 != indexKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, indexKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("覆盖类")) {
								// '覆盖类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
								AbstractPageList coverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("coverKpi", coverKpi);
								List coverKpirows = coverKpi.getRows();
								if (null != coverKpirows
										&& 0 != coverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, coverKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("干扰类")) {
								// '干扰类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
								AbstractPageList disturbKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("disturbKpi", disturbKpi);
								List disturbKpirows = disturbKpi.getRows();
								if (null != disturbKpirows
										&& 0 != disturbKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, disturbKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("调度类")) {
								// '调度类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
								AbstractPageList dispatcherKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("dispatcherKpi", dispatcherKpi);
								List dispatcherKpirows = dispatcherKpi
										.getRows();
								if (null != dispatcherKpirows
										&& 0 != dispatcherKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											dispatcherKpirows, null, 8,
											provinceName);
								}
							} else if (sheetName.equals("移动类")) {
								// '移动类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
								AbstractPageList moveKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("moveKpi", moveKpi);
								List moveKpirows = moveKpi.getRows();
								if (null != moveKpirows
										&& 0 != moveKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, moveKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("接入类")) {
								// '接入类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
								AbstractPageList insertKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("insertKpi", insertKpi);
								List insertKpirows = insertKpi.getRows();
								if (null != insertKpirows
										&& 0 != insertKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, insertKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("应用层FTP速率分段占比")) {
								// '应用层FTP速率分段占比'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
								AbstractPageList ftpSectionKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("ftpSectionKpi", ftpSectionKpi);
								List ftpSectionKpirows = ftpSectionKpi
										.getRows();
								if (null != ftpSectionKpirows
										&& 0 != ftpSectionKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											ftpSectionKpirows, null, 8,
											provinceName);
								}
							} else if (sheetName.equals("网络覆盖类分段占比统计")) {
								// '网络覆盖类分段占比统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
								AbstractPageList networkWerkcoverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("networkWerkcoverKpi",
										networkWerkcoverKpi);
								List networkWerkcoverKpirows = networkWerkcoverKpi
										.getRows();
								if (null != networkWerkcoverKpirows
										&& 0 != networkWerkcoverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											networkWerkcoverKpirows, null, 8,
											provinceName);
								}
							} else if (sheetName.equals("停车测试")) {
								// '停车测试'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
								AbstractPageList stopKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("stopKpi", stopKpi);
								List stopKpirows = stopKpi.getRows();
								if (null != stopKpirows
										&& 0 != stopKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, stopKpirows,
											null, 8, provinceName);
								}
							}
						} else {

						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("9") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("indexKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("moveKpi", new EasyuiPageList());
						addMap.put("insertKpi", new EasyuiPageList());
						addMap.put("ftpSectionKpi", new EasyuiPageList());
						addMap.put("networkWerkcoverKpi", new EasyuiPageList());
						addMap.put("stopKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_LTE);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO
											+ "," + ParamConstant.KEYPOINTNO
											+ "," + ParamConstant.FLOORNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							// '数据业务统计指标'sheet
							if (sheetName.equals("数据业务统计指标")) {
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
								AbstractPageList indexKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("indexKpi", indexKpi);
								List indexKpirows = indexKpi.getRows();
								if (null != indexKpirows
										&& 0 != indexKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, indexKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("覆盖类")) {
								// '覆盖类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
								AbstractPageList coverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("coverKpi", coverKpi);
								List coverKpirows = coverKpi.getRows();
								if (null != coverKpirows
										&& 0 != coverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, coverKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("干扰类")) {
								// '干扰类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
								AbstractPageList disturbKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("disturbKpi", disturbKpi);
								List disturbKpirows = disturbKpi.getRows();
								if (null != disturbKpirows
										&& 0 != disturbKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, disturbKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("调度类")) {
								// '调度类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
								AbstractPageList dispatcherKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("dispatcherKpi", dispatcherKpi);
								List dispatcherKpirows = dispatcherKpi
										.getRows();
								if (null != dispatcherKpirows
										&& 0 != dispatcherKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											dispatcherKpirows, null, 9,
											provinceName);
								}
							} else if (sheetName.equals("移动类")) {
								// '移动类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
								AbstractPageList moveKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("moveKpi", moveKpi);
								List moveKpirows = moveKpi.getRows();
								if (null != moveKpirows
										&& 0 != moveKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, moveKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("接入类")) {
								// '接入类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
								AbstractPageList insertKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("insertKpi", insertKpi);
								List insertKpirows = insertKpi.getRows();
								if (null != insertKpirows
										&& 0 != insertKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, insertKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("应用层FTP速率分段占比")) {
								// '应用层FTP速率分段占比'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
								AbstractPageList ftpSectionKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("ftpSectionKpi", ftpSectionKpi);
								List ftpSectionKpirows = ftpSectionKpi
										.getRows();
								if (null != ftpSectionKpirows
										&& 0 != ftpSectionKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											ftpSectionKpirows, null, 9,
											provinceName);
								}
							} else if (sheetName.equals("网络覆盖类分段占比统计")) {
								// '网络覆盖类分段占比统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
								AbstractPageList networkWerkcoverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("networkWerkcoverKpi",
										networkWerkcoverKpi);
								List networkWerkcoverKpirows = networkWerkcoverKpi
										.getRows();
								if (null != networkWerkcoverKpirows
										&& 0 != networkWerkcoverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											networkWerkcoverKpirows, null, 9,
											provinceName);
								}
							} else if (sheetName.equals("停车测试")) {
								// '停车测试'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
								AbstractPageList stopKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("stopKpi", stopKpi);
								List stopKpirows = stopKpi.getRows();
								if (null != stopKpirows
										&& 0 != stopKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, stopKpirows,
											null, 9, provinceName);
								}
							}
						} else {

						}
						list.add(addMap);
					}

				}
				Map<String, AbstractPageList> addMap = new HashMap<>();
				addMap.put("indexKpi", new EasyuiPageList());
				addMap.put("coverKpi", new EasyuiPageList());
				addMap.put("disturbKpi", new EasyuiPageList());
				addMap.put("dispatcherKpi", new EasyuiPageList());
				addMap.put("moveKpi", new EasyuiPageList());
				addMap.put("insertKpi", new EasyuiPageList());
				addMap.put("ftpSectionKpi", new EasyuiPageList());
				addMap.put("networkWerkcoverKpi", new EasyuiPageList());
				addMap.put("stopKpi", new EasyuiPageList());
				for (Map<String, AbstractPageList> map2 : list) {
					if (map2 != null && map2.size() != 0) {
						if (null != map2.get("indexKpi").getRows()
								&& 0 != map2.get("indexKpi").getRows().size()) {
							addMap.get("indexKpi").getRows()
									.addAll(map2.get("indexKpi").getRows());
						}
						if (null != map2.get("coverKpi").getRows()
								&& 0 != map2.get("coverKpi").getRows().size()) {
							addMap.get("coverKpi").getRows()
									.addAll(map2.get("coverKpi").getRows());
						}
						if (null != map2.get("disturbKpi").getRows()
								&& 0 != map2.get("disturbKpi").getRows().size()) {
							addMap.get("disturbKpi").getRows()
									.addAll(map2.get("disturbKpi").getRows());
						}
						if (null != map2.get("dispatcherKpi").getRows()
								&& 0 != map2.get("dispatcherKpi").getRows()
										.size()) {
							addMap.get("dispatcherKpi")
									.getRows()
									.addAll(map2.get("dispatcherKpi").getRows());
						}
						if (null != map2.get("moveKpi").getRows()
								&& 0 != map2.get("moveKpi").getRows().size()) {
							addMap.get("moveKpi").getRows()
									.addAll(map2.get("moveKpi").getRows());
						}
						if (null != map2.get("insertKpi").getRows()
								&& 0 != map2.get("insertKpi").getRows().size()) {
							addMap.get("insertKpi").getRows()
									.addAll(map2.get("insertKpi").getRows());
						}
						if (null != map2.get("ftpSectionKpi").getRows()
								&& 0 != map2.get("ftpSectionKpi").getRows()
										.size()) {
							addMap.get("ftpSectionKpi")
									.getRows()
									.addAll(map2.get("ftpSectionKpi").getRows());
						}
						if (null != map2.get("networkWerkcoverKpi").getRows()
								&& 0 != map2.get("networkWerkcoverKpi")
										.getRows().size()) {
							addMap.get("networkWerkcoverKpi")
									.getRows()
									.addAll(map2.get("networkWerkcoverKpi")
											.getRows());
						}
						if (null != map2.get("stopKpi").getRows()
								&& 0 != map2.get("stopKpi").getRows().size()) {
							addMap.get("stopKpi").getRows()
									.addAll(map2.get("stopKpi").getRows());
						}
					}

				}
				map.put("info", addMap);
			} else {
				Map<String, AbstractPageList> addMap = new HashMap<>();
				addMap.put("indexKpi", new EasyuiPageList());
				addMap.put("coverKpi", new EasyuiPageList());
				addMap.put("disturbKpi", new EasyuiPageList());
				addMap.put("dispatcherKpi", new EasyuiPageList());
				addMap.put("moveKpi", new EasyuiPageList());
				addMap.put("insertKpi", new EasyuiPageList());
				addMap.put("ftpSectionKpi", new EasyuiPageList());
				addMap.put("networkWerkcoverKpi", new EasyuiPageList());
				addMap.put("stopKpi", new EasyuiPageList());
				map.put("info", addMap);
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/CQTLTE数据业务报表.xls", sheetName);
				if (transformToExcel.getNumberOfSheets() != 0) {
					for (int i = 0; i < transformToExcel.getNumberOfSheets(); i++) {
						if (collectTypes.indexOf("1") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(0,
									true);
						}
						if (collectTypes.indexOf("2") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(1,
									true);
						}
						if (collectTypes.indexOf("3") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(2,
									true);
						}
						if (collectTypes.indexOf("4") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(3,
									true);
						}
						if (collectTypes.indexOf("5") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(4,
									true);
						}
						if (collectTypes.indexOf("6") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(5,
									true);
						}
						if (collectTypes.indexOf("7") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(6,
									true);
						}
						if (collectTypes.indexOf("8") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(7,
									true);
						}
						if (collectTypes.indexOf("9") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(8,
									true);
						}
					}
				}
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_CQT_LTE数据业务报表_" + sheetName + ".xls";
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
	 * 单个sheet导出时查询数据业务信息公用
	 * 
	 * @param testLogItemIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, AbstractPageList> getDataKpiForOneSheet(
			String testLogItemIds, List<TestLogItem> queryTestLogItems,
			String collectType, Integer index, String sheetName) {
		Map<String, AbstractPageList> map = new HashMap<>();
		if (null != testLogItemIds) {
			// 汇总其他信息
			VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
			lteWholePreviewParam.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
			lteWholePreviewParam.setReportType(ParamConstant.REPOR_TTYPE_LTE);
			// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
			lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
			map.put("indexKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("moveKpi", new EasyuiPageList());
			map.put("insertKpi", new EasyuiPageList());
			map.put("ftpSectionKpi", new EasyuiPageList());
			map.put("networkWerkcoverKpi", new EasyuiPageList());
			map.put("stopKpi", new EasyuiPageList());
			// '数据业务统计指标'sheet
			if (sheetName.equals("数据业务统计指标")) {
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
				AbstractPageList indexKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("indexKpi", indexKpi);
				List indexKpirows = indexKpi.getRows();
				if (null != indexKpirows && 0 != indexKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, indexKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("覆盖类")) {
				// '覆盖类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, coverKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("干扰类")) {
				// '干扰类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, disturbKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("调度类")) {
				// '调度类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, dispatcherKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("移动类")) {
				// '移动类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
				AbstractPageList moveKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("moveKpi", moveKpi);
				List moveKpirows = moveKpi.getRows();
				if (null != moveKpirows && 0 != moveKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, moveKpirows, collectType, index,
							null);
				}
			} else if (sheetName.equals("接入类")) {
				// '接入类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
				AbstractPageList insertKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("insertKpi", insertKpi);
				List insertKpirows = insertKpi.getRows();
				if (null != insertKpirows && 0 != insertKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, insertKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("应用层FTP速率分段占比")) {
				// '应用层FTP速率分段占比'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_FTP_SECTION);
				AbstractPageList ftpSectionKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("ftpSectionKpi", ftpSectionKpi);
				List ftpSectionKpirows = ftpSectionKpi.getRows();
				if (null != ftpSectionKpirows && 0 != ftpSectionKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, ftpSectionKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("网络覆盖类分段占比统计")) {
				// '网络覆盖类分段占比统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_NETWORK_WERKCOVER);
				AbstractPageList networkWerkcoverKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("networkWerkcoverKpi", networkWerkcoverKpi);
				List networkWerkcoverKpirows = networkWerkcoverKpi.getRows();
				if (null != networkWerkcoverKpirows
						&& 0 != networkWerkcoverKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, networkWerkcoverKpirows,
							collectType, index, null);
				}
			} else if (sheetName.equals("停车测试")) {
				// '停车测试'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_STOP);
				AbstractPageList stopKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("stopKpi", stopKpi);
				List stopKpirows = stopKpi.getRows();
				if (null != stopKpirows && 0 != stopKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, stopKpirows, collectType, index,
							null);
				}
			}
		}
		return map;
	}

	/**
	 * volte语音表单个Sheet报表导出
	 * 
	 * @return
	 */
	public String downloadOneSheetVolteVoiceTotalExcel() {
		return "downloadOneSheetVolteVoiceTotal";
	}

	/**
	 * volte语音表单个Sheet报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetVolteVoiceTotal() {
		if (null != sheetName) {
			Integer typeNo = (Integer) ServletActionContext.getRequest()
					.getSession().getAttribute("typeNo");
			String collectTypes = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("collectTypes");
			Object attribute = null;
			if (null != typeNo) {
				if (0 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("moveVoiceTestLogItemIds");
				} else if (1 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("linkVoiceTestLogItemIds");
				} else if (2 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("telecomVoiceTestLogItemIds");
				}
			}
			Map<String, Object> map = new HashMap<>();
			List<Map<String, AbstractPageList>> list = new ArrayList<>();
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					// 汇总开始时间和结束时间
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(queryTestLogItems));
					// 收集汇总方式
					map.put("collectTypes", ServletActionContext.getRequest()
							.getSession().getAttribute("collectTypes"));
					// 汇总其他信息
					if (collectTypes.indexOf("1") != -1) {
						list.add(getVoiceKpiForOneSheet(testLogItemIds,
								queryTestLogItems, "全国", 1, sheetName));
					}
					if (collectTypes.indexOf("2") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup()))
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup()),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVoiceKpiForOneSheet(
									buStringBuilder.toString(), value, key, 2,
									sheetName);
							if (null != voiceKpi.get("totalKpi").getRows()
									&& 0 != voiceKpi.get("totalKpi").getRows()
											.size()) {
								addMap.get("totalKpi")
										.getRows()
										.addAll(voiceKpi.get("totalKpi")
												.getRows());
							}
							if (null != voiceKpi.get("volteKpi").getRows()
									&& 0 != voiceKpi.get("volteKpi").getRows()
											.size()) {
								addMap.get("volteKpi")
										.getRows()
										.addAll(voiceKpi.get("volteKpi")
												.getRows());
							}
							if (null != voiceKpi.get("csKpi").getRows()
									&& 0 != voiceKpi.get("csKpi").getRows()
											.size()) {
								addMap.get("csKpi")
										.getRows()
										.addAll(voiceKpi.get("csKpi").getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("mosKpi").getRows()
									&& 0 != voiceKpi.get("mosKpi").getRows()
											.size()) {
								addMap.get("mosKpi")
										.getRows()
										.addAll(voiceKpi.get("mosKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("3") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVoiceKpiForOneSheet(
									buStringBuilder.toString(), value, key, 3,
									sheetName);
							if (null != voiceKpi.get("totalKpi").getRows()
									&& 0 != voiceKpi.get("totalKpi").getRows()
											.size()) {
								addMap.get("totalKpi")
										.getRows()
										.addAll(voiceKpi.get("totalKpi")
												.getRows());
							}
							if (null != voiceKpi.get("volteKpi").getRows()
									&& 0 != voiceKpi.get("volteKpi").getRows()
											.size()) {
								addMap.get("volteKpi")
										.getRows()
										.addAll(voiceKpi.get("volteKpi")
												.getRows());
							}
							if (null != voiceKpi.get("csKpi").getRows()
									&& 0 != voiceKpi.get("csKpi").getRows()
											.size()) {
								addMap.get("csKpi")
										.getRows()
										.addAll(voiceKpi.get("csKpi").getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("mosKpi").getRows()
									&& 0 != voiceKpi.get("mosKpi").getRows()
											.size()) {
								addMap.get("mosKpi")
										.getRows()
										.addAll(voiceKpi.get("mosKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("4") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVoiceKpiForOneSheet(
									buStringBuilder.toString(), value, key, 4,
									sheetName);
							if (null != voiceKpi.get("totalKpi").getRows()
									&& 0 != voiceKpi.get("totalKpi").getRows()
											.size()) {
								addMap.get("totalKpi")
										.getRows()
										.addAll(voiceKpi.get("totalKpi")
												.getRows());
							}
							if (null != voiceKpi.get("volteKpi").getRows()
									&& 0 != voiceKpi.get("volteKpi").getRows()
											.size()) {
								addMap.get("volteKpi")
										.getRows()
										.addAll(voiceKpi.get("volteKpi")
												.getRows());
							}
							if (null != voiceKpi.get("csKpi").getRows()
									&& 0 != voiceKpi.get("csKpi").getRows()
											.size()) {
								addMap.get("csKpi")
										.getRows()
										.addAll(voiceKpi.get("csKpi").getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("mosKpi").getRows()
									&& 0 != voiceKpi.get("mosKpi").getRows()
											.size()) {
								addMap.get("mosKpi")
										.getRows()
										.addAll(voiceKpi.get("mosKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("5") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVoiceKpiForOneSheet(
									buStringBuilder.toString(), value, key, 5,
									sheetName);
							if (null != voiceKpi.get("totalKpi").getRows()
									&& 0 != voiceKpi.get("totalKpi").getRows()
											.size()) {
								addMap.get("totalKpi")
										.getRows()
										.addAll(voiceKpi.get("totalKpi")
												.getRows());
							}
							if (null != voiceKpi.get("volteKpi").getRows()
									&& 0 != voiceKpi.get("volteKpi").getRows()
											.size()) {
								addMap.get("volteKpi")
										.getRows()
										.addAll(voiceKpi.get("volteKpi")
												.getRows());
							}
							if (null != voiceKpi.get("csKpi").getRows()
									&& 0 != voiceKpi.get("csKpi").getRows()
											.size()) {
								addMap.get("csKpi")
										.getRows()
										.addAll(voiceKpi.get("csKpi").getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("mosKpi").getRows()
									&& 0 != voiceKpi.get("mosKpi").getRows()
											.size()) {
								addMap.get("mosKpi")
										.getRows()
										.addAll(voiceKpi.get("mosKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("6") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_LTE);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							if (sheetName.equals("KPI汇总")) {
								// 'KPI汇总'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
								AbstractPageList totalKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("totalKpi", totalKpi);
								List totalKpirows = totalKpi.getRows();
								if (null != totalKpirows
										&& 0 != totalKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, totalKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("VOLTE统计指标")) {
								// 'VOLTE统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
								AbstractPageList volteKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("volteKpi", volteKpi);
								List volteKpirows = volteKpi.getRows();
								if (null != volteKpirows
										&& 0 != volteKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, volteKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("CS域语音统计指标")) {
								// 'CS域语音统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
								AbstractPageList csKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("csKpi", csKpi);
								List csKpirows = csKpi.getRows();
								if (null != csKpirows && 0 != csKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, csKpirows, null,
											6, provinceName);
								}
							} else if (sheetName.equals("覆盖类")) {
								// '覆盖类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
								AbstractPageList coverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("coverKpi", coverKpi);
								List coverKpirows = coverKpi.getRows();
								if (null != coverKpirows
										&& 0 != coverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, coverKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("干扰类")) {
								// '干扰类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
								AbstractPageList disturbKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("disturbKpi", disturbKpi);
								List disturbKpirows = disturbKpi.getRows();
								if (null != disturbKpirows
										&& 0 != disturbKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, disturbKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("调度类")) {
								// '调度类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
								AbstractPageList dispatcherKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("dispatcherKpi", dispatcherKpi);
								List dispatcherKpirows = dispatcherKpi
										.getRows();
								if (null != dispatcherKpirows
										&& 0 != dispatcherKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											dispatcherKpirows, null, 6,
											provinceName);
								}
							} else if (sheetName.equals("MOS统计")) {
								// 'MOS统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
								AbstractPageList mosKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("mosKpi", mosKpi);
								List mosKpirows = mosKpi.getRows();
								if (null != mosKpirows
										&& 0 != mosKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, mosKpirows,
											null, 6, provinceName);
								}
							}
							list.add(addMap);
						}

					}
					if (collectTypes.indexOf("7") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName()
													+ ","
													+ testLogItem.getFileName()
													+ ","
													+ testLogItem
															.getFloorName())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName()
													+ ","
													+ testLogItem.getFileName()
													+ ","
													+ testLogItem
															.getFloorName(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVoiceKpiForOneSheet(
									buStringBuilder.toString(), value, key, 7,
									sheetName);
							if (null != voiceKpi.get("totalKpi").getRows()
									&& 0 != voiceKpi.get("totalKpi").getRows()
											.size()) {
								addMap.get("totalKpi")
										.getRows()
										.addAll(voiceKpi.get("totalKpi")
												.getRows());
							}
							if (null != voiceKpi.get("volteKpi").getRows()
									&& 0 != voiceKpi.get("volteKpi").getRows()
											.size()) {
								addMap.get("volteKpi")
										.getRows()
										.addAll(voiceKpi.get("volteKpi")
												.getRows());
							}
							if (null != voiceKpi.get("csKpi").getRows()
									&& 0 != voiceKpi.get("csKpi").getRows()
											.size()) {
								addMap.get("csKpi")
										.getRows()
										.addAll(voiceKpi.get("csKpi").getRows());
							}
							if (null != voiceKpi.get("coverKpi").getRows()
									&& 0 != voiceKpi.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi")
										.getRows()
										.addAll(voiceKpi.get("coverKpi")
												.getRows());
							}
							if (null != voiceKpi.get("disturbKpi").getRows()
									&& 0 != voiceKpi.get("disturbKpi")
											.getRows().size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(voiceKpi.get("disturbKpi")
												.getRows());
							}
							if (null != voiceKpi.get("dispatcherKpi").getRows()
									&& 0 != voiceKpi.get("dispatcherKpi")
											.getRows().size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(voiceKpi.get("dispatcherKpi")
												.getRows());
							}
							if (null != voiceKpi.get("mosKpi").getRows()
									&& 0 != voiceKpi.get("mosKpi").getRows()
											.size()) {
								addMap.get("mosKpi")
										.getRows()
										.addAll(voiceKpi.get("mosKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("8") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_KEYPOINT_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_LTE);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO
											+ "," + ParamConstant.KEYPOINTNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							if (sheetName.equals("KPI汇总")) {
								// 'KPI汇总'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
								AbstractPageList totalKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("totalKpi", totalKpi);
								List totalKpirows = totalKpi.getRows();
								if (null != totalKpirows
										&& 0 != totalKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, totalKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("VOLTE统计指标")) {
								// 'VOLTE统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
								AbstractPageList volteKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("volteKpi", volteKpi);
								List volteKpirows = volteKpi.getRows();
								if (null != volteKpirows
										&& 0 != volteKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, volteKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("CS域语音统计指标")) {
								// 'CS域语音统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
								AbstractPageList csKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("csKpi", csKpi);
								List csKpirows = csKpi.getRows();
								if (null != csKpirows && 0 != csKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, csKpirows, null,
											8, provinceName);
								}
							} else if (sheetName.equals("覆盖类")) {
								// '覆盖类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
								AbstractPageList coverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("coverKpi", coverKpi);
								List coverKpirows = coverKpi.getRows();
								if (null != coverKpirows
										&& 0 != coverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, coverKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("干扰类")) {
								// '干扰类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
								AbstractPageList disturbKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("disturbKpi", disturbKpi);
								List disturbKpirows = disturbKpi.getRows();
								if (null != disturbKpirows
										&& 0 != disturbKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, disturbKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("调度类")) {
								// '调度类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
								AbstractPageList dispatcherKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("dispatcherKpi", dispatcherKpi);
								List dispatcherKpirows = dispatcherKpi
										.getRows();
								if (null != dispatcherKpirows
										&& 0 != dispatcherKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											dispatcherKpirows, null, 8,
											provinceName);
								}
							} else if (sheetName.equals("MOS统计")) {
								// 'MOS统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
								AbstractPageList mosKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("mosKpi", mosKpi);
								List mosKpirows = mosKpi.getRows();
								if (null != mosKpirows
										&& 0 != mosKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, mosKpirows,
											null, 8, provinceName);
								}
							}
							list.add(addMap);
						}

					}
					if (collectTypes.indexOf("9") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("totalKpi", new EasyuiPageList());
						addMap.put("volteKpi", new EasyuiPageList());
						addMap.put("csKpi", new EasyuiPageList());
						addMap.put("coverKpi", new EasyuiPageList());
						addMap.put("disturbKpi", new EasyuiPageList());
						addMap.put("dispatcherKpi", new EasyuiPageList());
						addMap.put("mosKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_LTE);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO
											+ "," + ParamConstant.KEYPOINTNO
											+ "," + ParamConstant.FLOORNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							if (sheetName.equals("KPI汇总")) {
								// 'KPI汇总'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
								AbstractPageList totalKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("totalKpi", totalKpi);
								List totalKpirows = totalKpi.getRows();
								if (null != totalKpirows
										&& 0 != totalKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, totalKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("VOLTE统计指标")) {
								// 'VOLTE统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
								AbstractPageList volteKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("volteKpi", volteKpi);
								List volteKpirows = volteKpi.getRows();
								if (null != volteKpirows
										&& 0 != volteKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, volteKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("CS域语音统计指标")) {
								// 'CS域语音统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
								AbstractPageList csKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("csKpi", csKpi);
								List csKpirows = csKpi.getRows();
								if (null != csKpirows && 0 != csKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, csKpirows, null,
											9, provinceName);
								}
							} else if (sheetName.equals("覆盖类")) {
								// '覆盖类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
								AbstractPageList coverKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("coverKpi", coverKpi);
								List coverKpirows = coverKpi.getRows();
								if (null != coverKpirows
										&& 0 != coverKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, coverKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("干扰类")) {
								// '干扰类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
								AbstractPageList disturbKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("disturbKpi", disturbKpi);
								List disturbKpirows = disturbKpi.getRows();
								if (null != disturbKpirows
										&& 0 != disturbKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, disturbKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("调度类")) {
								// '调度类'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
								AbstractPageList dispatcherKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("dispatcherKpi", dispatcherKpi);
								List dispatcherKpirows = dispatcherKpi
										.getRows();
								if (null != dispatcherKpirows
										&& 0 != dispatcherKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems,
											dispatcherKpirows, null, 9,
											provinceName);
								}
							} else if (sheetName.equals("MOS统计")) {
								// 'MOS统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
								AbstractPageList mosKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("mosKpi", mosKpi);
								List mosKpirows = mosKpi.getRows();
								if (null != mosKpirows
										&& 0 != mosKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, mosKpirows,
											null, 9, provinceName);
								}
							}
							list.add(addMap);
						}

					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("totalKpi", new EasyuiPageList());
					addMap.put("volteKpi", new EasyuiPageList());
					addMap.put("csKpi", new EasyuiPageList());
					addMap.put("coverKpi", new EasyuiPageList());
					addMap.put("disturbKpi", new EasyuiPageList());
					addMap.put("dispatcherKpi", new EasyuiPageList());
					addMap.put("mosKpi", new EasyuiPageList());
					for (Map<String, AbstractPageList> map2 : list) {
						if (map2 != null && map2.size() != 0) {
							if (null != map2.get("totalKpi").getRows()
									&& 0 != map2.get("totalKpi").getRows()
											.size()) {
								addMap.get("totalKpi").getRows()
										.addAll(map2.get("totalKpi").getRows());
							}
							if (null != map2.get("volteKpi").getRows()
									&& 0 != map2.get("volteKpi").getRows()
											.size()) {
								addMap.get("volteKpi").getRows()
										.addAll(map2.get("volteKpi").getRows());
							}
							if (null != map2.get("csKpi").getRows()
									&& 0 != map2.get("csKpi").getRows().size()) {
								addMap.get("csKpi").getRows()
										.addAll(map2.get("csKpi").getRows());
							}
							if (null != map2.get("coverKpi").getRows()
									&& 0 != map2.get("coverKpi").getRows()
											.size()) {
								addMap.get("coverKpi").getRows()
										.addAll(map2.get("coverKpi").getRows());
							}
							if (null != map2.get("disturbKpi").getRows()
									&& 0 != map2.get("disturbKpi").getRows()
											.size()) {
								addMap.get("disturbKpi")
										.getRows()
										.addAll(map2.get("disturbKpi")
												.getRows());
							}
							if (null != map2.get("dispatcherKpi").getRows()
									&& 0 != map2.get("dispatcherKpi").getRows()
											.size()) {
								addMap.get("dispatcherKpi")
										.getRows()
										.addAll(map2.get("dispatcherKpi")
												.getRows());
							}
							if (null != map2.get("mosKpi").getRows()
									&& 0 != map2.get("mosKpi").getRows().size()) {
								addMap.get("mosKpi").getRows()
										.addAll(map2.get("mosKpi").getRows());
							}
						}

					}
					map.put("info", addMap);
				}
			} else {
				Map<String, AbstractPageList> addMap = new HashMap<>();
				addMap.put("totalKpi", new EasyuiPageList());
				addMap.put("volteKpi", new EasyuiPageList());
				addMap.put("csKpi", new EasyuiPageList());
				addMap.put("coverKpi", new EasyuiPageList());
				addMap.put("disturbKpi", new EasyuiPageList());
				addMap.put("dispatcherKpi", new EasyuiPageList());
				addMap.put("mosKpi", new EasyuiPageList());
				map.put("info", addMap);
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/CQTVoLTE语音报表.xls", sheetName);
				if (transformToExcel.getNumberOfSheets() != 0) {
					for (int i = 0; i < transformToExcel.getNumberOfSheets(); i++) {
						if (collectTypes.indexOf("1") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(0,
									true);
						}
						if (collectTypes.indexOf("2") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(1,
									true);
						}
						if (collectTypes.indexOf("3") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(2,
									true);
						}
						if (collectTypes.indexOf("4") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(3,
									true);
						}
						if (collectTypes.indexOf("5") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(4,
									true);
						}
						if (collectTypes.indexOf("6") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(5,
									true);
						}
						if (collectTypes.indexOf("7") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(6,
									true);
						}
						if (collectTypes.indexOf("8") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(7,
									true);
						}
						if (collectTypes.indexOf("9") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(8,
									true);
						}
					}
				}
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_CQT_VoLTE语音报表_" + sheetName + ".xls";
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
	 * 单个sheet导出时查询语音业务信息公用
	 * 
	 * @param testLogItemIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, AbstractPageList> getVoiceKpiForOneSheet(
			String testLogItemIds, List<TestLogItem> queryTestLogItems,
			String collectType, Integer index, String sheetName) {
		Map<String, AbstractPageList> map = new HashMap<>();
		map.put("totalKpi", new EasyuiPageList());
		map.put("volteKpi", new EasyuiPageList());
		map.put("csKpi", new EasyuiPageList());
		map.put("coverKpi", new EasyuiPageList());
		map.put("disturbKpi", new EasyuiPageList());
		map.put("dispatcherKpi", new EasyuiPageList());
		map.put("mosKpi", new EasyuiPageList());
		if (null != testLogItemIds) {
			// 汇总其他信息
			VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
			lteWholePreviewParam.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
			lteWholePreviewParam.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
			// lteWholePreviewParam
			// .setGroupByField(ParamConstant.RECSEQNO);
			lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
			if (sheetName.equals("KPI汇总")) {
				// 'KPI汇总'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
				AbstractPageList totalKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("totalKpi", totalKpi);
				List totalKpirows = totalKpi.getRows();
				if (null != totalKpirows && 0 != totalKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, totalKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("VOLTE统计指标")) {
				// 'VOLTE统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
				AbstractPageList volteKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("volteKpi", volteKpi);
				List volteKpirows = volteKpi.getRows();
				if (null != volteKpirows && 0 != volteKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, volteKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("CS域语音统计指标")) {
				// 'CS域语音统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
				AbstractPageList csKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("csKpi", csKpi);
				List csKpirows = csKpi.getRows();
				if (null != csKpirows && 0 != csKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, csKpirows, collectType, index,
							null);
				}
			} else if (sheetName.equals("覆盖类")) {
				// '覆盖类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, coverKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("干扰类")) {
				// '干扰类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, disturbKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("调度类")) {
				// '调度类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, dispatcherKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("MOS统计")) {
				// 'MOS统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
				AbstractPageList mosKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("mosKpi", mosKpi);
				List mosKpirows = mosKpi.getRows();
				if (null != mosKpirows && 0 != mosKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, mosKpirows, collectType, index,
							null);
				}
			}
		}

		return map;
	}

	/**
	 * volte视频表单个Sheet报表导出
	 * 
	 * @return
	 */
	public String downloadOneSheetVolteVideoTotalExcel() {
		return "downloadOneSheetVolteVideoTotal";
	}

	/**
	 * volte视频表单个Sheet报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadOneSheetVolteVideoTotal() {
		if (null != sheetName) {
			Integer typeNo = (Integer) ServletActionContext.getRequest()
					.getSession().getAttribute("typeNo");
			String collectTypes = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("collectTypes");
			Object attribute = null;
			if (null != typeNo) {
				if (0 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("moveVideoTestLogItemIds");
				} else if (1 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("linkVideoTestLogItemIds");
				} else if (2 == typeNo) {
					attribute = ServletActionContext.getRequest().getSession()
							.getAttribute("telecomVideoTestLogItemIds");
				}
			}
			Map<String, Object> map = new HashMap<>();
			List<Map<String, AbstractPageList>> list = new ArrayList<>();
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					List<TestLogItem> queryTestLogItems = testLogItemService
							.queryTestLogItems(testLogItemIds);
					// 汇总开始时间和结束时间
					map.putAll(TestLogItemUtils
							.amountBeginEndDate(queryTestLogItems));
					// 收集汇总方式
					map.put("collectTypes", ServletActionContext.getRequest()
							.getSession().getAttribute("collectTypes"));
					// 汇总其他信息
					if (collectTypes.indexOf("1") != -1) {
						list.add(getVideoKpiForOneSheet(testLogItemIds,
								queryTestLogItems, "全国", 1, sheetName));
					}
					if (collectTypes.indexOf("2") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup()))
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup()),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVideoKpiForOneSheet(
									buStringBuilder.toString(), value, key, 2,
									sheetName);
							if (null != voiceKpi.get("statisticeKpi").getRows()
									&& 0 != voiceKpi.get("statisticeKpi")
											.getRows().size()) {
								addMap.get("statisticeKpi")
										.getRows()
										.addAll(voiceKpi.get("statisticeKpi")
												.getRows());
							}
							if (null != voiceKpi.get("qualityKpi").getRows()
									&& 0 != voiceKpi.get("qualityKpi")
											.getRows().size()) {
								addMap.get("qualityKpi")
										.getRows()
										.addAll(voiceKpi.get("qualityKpi")
												.getRows());
							}
							if (null != voiceKpi.get("resourceKpi").getRows()
									&& 0 != voiceKpi.get("resourceKpi")
											.getRows().size()) {
								addMap.get("resourceKpi")
										.getRows()
										.addAll(voiceKpi.get("resourceKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("3") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVideoKpiForOneSheet(
									buStringBuilder.toString(), value, key, 3,
									sheetName);
							if (null != voiceKpi.get("statisticeKpi").getRows()
									&& 0 != voiceKpi.get("statisticeKpi")
											.getRows().size()) {
								addMap.get("statisticeKpi")
										.getRows()
										.addAll(voiceKpi.get("statisticeKpi")
												.getRows());
							}
							if (null != voiceKpi.get("qualityKpi").getRows()
									&& 0 != voiceKpi.get("qualityKpi")
											.getRows().size()) {
								addMap.get("qualityKpi")
										.getRows()
										.addAll(voiceKpi.get("qualityKpi")
												.getRows());
							}
							if (null != voiceKpi.get("resourceKpi").getRows()
									&& 0 != voiceKpi.get("resourceKpi")
											.getRows().size()) {
								addMap.get("resourceKpi")
										.getRows()
										.addAll(voiceKpi.get("resourceKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("4") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVideoKpiForOneSheet(
									buStringBuilder.toString(), value, key, 4,
									sheetName);
							if (null != voiceKpi.get("statisticeKpi").getRows()
									&& 0 != voiceKpi.get("statisticeKpi")
											.getRows().size()) {
								addMap.get("statisticeKpi")
										.getRows()
										.addAll(voiceKpi.get("statisticeKpi")
												.getRows());
							}
							if (null != voiceKpi.get("qualityKpi").getRows()
									&& 0 != voiceKpi.get("qualityKpi")
											.getRows().size()) {
								addMap.get("qualityKpi")
										.getRows()
										.addAll(voiceKpi.get("qualityKpi")
												.getRows());
							}
							if (null != voiceKpi.get("resourceKpi").getRows()
									&& 0 != voiceKpi.get("resourceKpi")
											.getRows().size()) {
								addMap.get("resourceKpi")
										.getRows()
										.addAll(voiceKpi.get("resourceKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("5") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVideoKpiForOneSheet(
									buStringBuilder.toString(), value, key, 5,
									sheetName);
							if (null != voiceKpi.get("statisticeKpi").getRows()
									&& 0 != voiceKpi.get("statisticeKpi")
											.getRows().size()) {
								addMap.get("statisticeKpi")
										.getRows()
										.addAll(voiceKpi.get("statisticeKpi")
												.getRows());
							}
							if (null != voiceKpi.get("qualityKpi").getRows()
									&& 0 != voiceKpi.get("qualityKpi")
											.getRows().size()) {
								addMap.get("qualityKpi")
										.getRows()
										.addAll(voiceKpi.get("qualityKpi")
												.getRows());
							}
							if (null != voiceKpi.get("resourceKpi").getRows()
									&& 0 != voiceKpi.get("resourceKpi")
											.getRows().size()) {
								addMap.get("resourceKpi")
										.getRows()
										.addAll(voiceKpi.get("resourceKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("6") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							if (sheetName.equals("KPI统计")) {
								// 'KPI统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
								AbstractPageList totalKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("statisticeKpi", totalKpi);
								List totalKpirows = totalKpi.getRows();
								if (null != totalKpirows
										&& 0 != totalKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, totalKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("质量类")) {
								// 'VOLTE统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
								AbstractPageList volteKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("qualityKpi", volteKpi);
								List volteKpirows = volteKpi.getRows();
								if (null != volteKpirows
										&& 0 != volteKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, volteKpirows,
											null, 6, provinceName);
								}
							} else if (sheetName.equals("资源类")) {
								// 'MOS统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
								AbstractPageList mosKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("resourceKpi", mosKpi);
								List mosKpirows = mosKpi.getRows();
								if (null != mosKpirows
										&& 0 != mosKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, mosKpirows,
											null, 6, provinceName);
								}
							}
							list.add(addMap);
						}

					}
					if (collectTypes.indexOf("7") != -1) {
						Map<String, List<TestLogItem>> logMap = new HashMap<>();
						for (TestLogItem testLogItem : queryTestLogItems) {
							if (logMap.size() != 0) {
								List<TestLogItem> testLogItemList = logMap
										.get("全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName());
								if (testLogItemList != null
										&& testLogItemList.size() != 0) {
									logMap.get(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName()
													+ ","
													+ testLogItem.getFileName()
													+ ","
													+ testLogItem
															.getFloorName())
											.add(testLogItem);
								} else {
									List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
									arrayList.add(testLogItem);
									logMap.put(
											"全国,"
													+ terminalGroupService
															.getProvinceNameByCityGroup(testLogItem
																	.getTerminal()
																	.getTerminalGroup())
													+ ","
													+ testLogItem
															.getTerminalGroup()
													+ ","
													+ testLogItem.getBoxId()
													+ ","
													+ testLogItem
															.getTestPlanName()
													+ ","
													+ testLogItem.getFileName()
													+ ","
													+ testLogItem
															.getFloorName(),
											arrayList);
								}
							} else {
								List<TestLogItem> arrayList = new ArrayList<TestLogItem>();
								arrayList.add(testLogItem);
								logMap.put(
										"全国,"
												+ terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup())
												+ ","
												+ testLogItem
														.getTerminalGroup()
												+ "," + testLogItem.getBoxId()
												+ ","
												+ testLogItem.getTestPlanName()
												+ ","
												+ testLogItem.getFileName()
												+ ","
												+ testLogItem.getFloorName(),
										arrayList);
							}
						}
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
						for (Map.Entry<String, List<TestLogItem>> entry : logMap
								.entrySet()) {
							List<TestLogItem> value = entry.getValue();
							String key = entry.getKey();
							String valueString = null;
							StringBuilder buStringBuilder = new StringBuilder();
							for (TestLogItem testLogItem : value) {
								if (0 != buStringBuilder.toString().length()) {
									buStringBuilder.append(","
											+ testLogItem.getRecSeqNo());
								} else {
									buStringBuilder.append(testLogItem
											.getRecSeqNo());
								}
							}
							Map<String, AbstractPageList> voiceKpi = getVideoKpiForOneSheet(
									buStringBuilder.toString(), value, key, 7,
									sheetName);
							if (null != voiceKpi.get("statisticeKpi").getRows()
									&& 0 != voiceKpi.get("statisticeKpi")
											.getRows().size()) {
								addMap.get("statisticeKpi")
										.getRows()
										.addAll(voiceKpi.get("statisticeKpi")
												.getRows());
							}
							if (null != voiceKpi.get("qualityKpi").getRows()
									&& 0 != voiceKpi.get("qualityKpi")
											.getRows().size()) {
								addMap.get("qualityKpi")
										.getRows()
										.addAll(voiceKpi.get("qualityKpi")
												.getRows());
							}

							if (null != voiceKpi.get("resourceKpi").getRows()
									&& 0 != voiceKpi.get("resourceKpi")
											.getRows().size()) {
								addMap.get("resourceKpi")
										.getRows()
										.addAll(voiceKpi.get("resourceKpi")
												.getRows());
							}
						}
						list.add(addMap);
					}
					if (collectTypes.indexOf("8") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO
											+ "," + ParamConstant.KEYPOINTNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							if (sheetName.equals("KPI统计")) {
								// 'KPI统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
								AbstractPageList totalKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("statisticeKpi", totalKpi);
								List totalKpirows = totalKpi.getRows();
								if (null != totalKpirows
										&& 0 != totalKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, totalKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("质量类")) {
								// 'VOLTE统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
								AbstractPageList volteKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("qualityKpi", volteKpi);
								List volteKpirows = volteKpi.getRows();
								if (null != volteKpirows
										&& 0 != volteKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, volteKpirows,
											null, 8, provinceName);
								}
							} else if (sheetName.equals("资源类")) {
								// 'MOS统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
								AbstractPageList mosKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("resourceKpi", mosKpi);
								List mosKpirows = mosKpi.getRows();
								if (null != mosKpirows
										&& 0 != mosKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, mosKpirows,
											null, 8, provinceName);
								}
							}
							list.add(addMap);
						}
					}
					if (collectTypes.indexOf("9") != -1) {
						Map<String, AbstractPageList> addMap = new HashMap<>();
						addMap.put("statisticeKpi", new EasyuiPageList());
						addMap.put("qualityKpi", new EasyuiPageList());
						addMap.put("resourceKpi", new EasyuiPageList());
						if (null != testLogItemIds) {
							Map<String, String> provinceName = new HashMap<>();
							for (TestLogItem testLogItem : queryTestLogItems) {
								provinceName
										.put(testLogItem.getBoxId(),
												terminalGroupService
														.getProvinceNameByCityGroup(testLogItem
																.getTerminal()
																.getTerminalGroup()));
							}
							VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
							lteWholePreviewParam
									.setQueryTable(ParamConstant.VOLTE_DATA_FLOORS_TABLE);
							lteWholePreviewParam
									.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
							lteWholePreviewParam
									.setGroupByField(ParamConstant.RECSEQNO
											+ "," + ParamConstant.KEYPOINTNO
											+ "," + ParamConstant.FLOORNO);
							lteWholePreviewParam
									.setTestLogItemIds(testLogItemIds);
							if (sheetName.equals("KPI统计")) {
								// 'KPI统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
								AbstractPageList totalKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("statisticeKpi", totalKpi);
								List totalKpirows = totalKpi.getRows();
								if (null != totalKpirows
										&& 0 != totalKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, totalKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("质量类")) {
								// 'VOLTE统计指标'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
								AbstractPageList volteKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("qualityKpi", volteKpi);
								List volteKpirows = volteKpi.getRows();
								if (null != volteKpirows
										&& 0 != volteKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, volteKpirows,
											null, 9, provinceName);
								}
							} else if (sheetName.equals("资源类")) {
								// 'MOS统计'sheet
								lteWholePreviewParam
										.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
								AbstractPageList mosKpi = cqtReportService
										.queryKpi(lteWholePreviewParam);
								addMap.put("resourceKpi", mosKpi);
								List mosKpirows = mosKpi.getRows();
								if (null != mosKpirows
										&& 0 != mosKpirows.size()) {
									TestLogItemUtils.amountInfoByCollectTypes(
											queryTestLogItems, mosKpirows,
											null, 9, provinceName);
								}
							}
							list.add(addMap);
						}
					}
					Map<String, AbstractPageList> addMap = new HashMap<>();
					addMap.put("statisticeKpi", new EasyuiPageList());
					addMap.put("qualityKpi", new EasyuiPageList());
					addMap.put("resourceKpi", new EasyuiPageList());
					for (Map<String, AbstractPageList> map2 : list) {
						if (map2 != null && map2.size() != 0) {
							if (null != map2.get("statisticeKpi").getRows()
									&& 0 != map2.get("statisticeKpi").getRows()
											.size()) {
								addMap.get("statisticeKpi")
										.getRows()
										.addAll(map2.get("statisticeKpi")
												.getRows());
							}
							if (null != map2.get("qualityKpi").getRows()
									&& 0 != map2.get("qualityKpi").getRows()
											.size()) {
								addMap.get("qualityKpi")
										.getRows()
										.addAll(map2.get("qualityKpi")
												.getRows());
							}
							if (null != map2.get("resourceKpi").getRows()
									&& 0 != map2.get("resourceKpi").getRows()
											.size()) {
								addMap.get("resourceKpi")
										.getRows()
										.addAll(map2.get("resourceKpi")
												.getRows());
							}
						}
					}
					map.put("info", addMap);
				}
			} else {
				Map<String, AbstractPageList> addMap = new HashMap<>();
				addMap.put("statisticeKpi", new EasyuiPageList());
				addMap.put("qualityKpi", new EasyuiPageList());
				addMap.put("resourceKpi", new EasyuiPageList());
				map.put("info", addMap);
			}
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			try {
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/CQTVoLTE视频报表.xls", sheetName);
				if (transformToExcel.getNumberOfSheets() != 0) {
					for (int i = 0; i < transformToExcel.getNumberOfSheets(); i++) {
						if (collectTypes.indexOf("1") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(0,
									true);
						}
						if (collectTypes.indexOf("2") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(1,
									true);
						}
						if (collectTypes.indexOf("3") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(2,
									true);
						}
						if (collectTypes.indexOf("4") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(3,
									true);
						}
						if (collectTypes.indexOf("5") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(4,
									true);
						}
						if (collectTypes.indexOf("6") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(5,
									true);
						}
						if (collectTypes.indexOf("7") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(6,
									true);
						}
						if (collectTypes.indexOf("8") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(7,
									true);
						}
						if (collectTypes.indexOf("9") == -1) {
							transformToExcel.getSheetAt(i).setColumnHidden(8,
									true);
						}
					}
				}
				if (null != transformToExcel) {
					transformToExcel.write(byteOutputStream);
				}
				String str = (String) ServletActionContext.getRequest()
						.getSession().getAttribute("taskName")
						+ "_CQT_VoLTE视频报表_" + sheetName + ".xls";
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
	 * 单个sheet导出时查询视频业务信息公用
	 * 
	 * @param testLogItemIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, AbstractPageList> getVideoKpiForOneSheet(
			String testLogItemIds, List<TestLogItem> queryTestLogItems,
			String collectType, Integer index, String sheetName) {
		Map<String, AbstractPageList> map = new HashMap<>();
		map.put("statisticeKpi", new EasyuiPageList());
		map.put("qualityKpi", new EasyuiPageList());
		map.put("resourceKpi", new EasyuiPageList());
		if (null != testLogItemIds) {
			// 汇总其他信息
			VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
			lteWholePreviewParam.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
			lteWholePreviewParam
					.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
			// lteWholePreviewParam
			// .setGroupByField(ParamConstant.RECSEQNO);
			lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
			if (sheetName.equals("KPI统计")) {
				// 'KPI汇总'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
				AbstractPageList totalKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("statisticeKpi", totalKpi);
				List totalKpirows = totalKpi.getRows();
				if (null != totalKpirows && 0 != totalKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, totalKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("质量类")) {
				// 'VOLTE统计指标'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
				AbstractPageList volteKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("volteKpi", volteKpi);
				List volteKpirows = volteKpi.getRows();
				if (null != volteKpirows && 0 != volteKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, volteKpirows, collectType,
							index, null);
				}
			} else if (sheetName.equals("资源类")) {
				// 'MOS统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
				AbstractPageList mosKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("mosKpi", mosKpi);
				List mosKpirows = mosKpi.getRows();
				if (null != mosKpirows && 0 != mosKpirows.size()) {
					TestLogItemUtils.amountInfoByCollectTypes(
							queryTestLogItems, mosKpirows, collectType, index,
							null);
				}
			}
		}

		return map;
	}

	/**
	 * 获取终端信息
	 * 
	 * @return
	 */
	public String terminalTree() {
		String cityIds = cqtStatisticeTaskRequest.getCityIds();
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
	 * @return the cqtReportRequertBean
	 */
	public CQTReportRequertBean getCqtReportRequertBean() {
		return cqtReportRequertBean;
	}

	/**
	 * @param the
	 *            cqtReportRequertBean to set
	 */

	public void setCqtReportRequertBean(
			CQTReportRequertBean cqtReportRequertBean) {
		this.cqtReportRequertBean = cqtReportRequertBean;
	}

	/**
	 * @return the cqtStatisticeTaskRequest
	 */
	public CQTStatisticeTaskRequest getCqtStatisticeTaskRequest() {
		return cqtStatisticeTaskRequest;
	}

	/**
	 * @param the
	 *            cqtStatisticeTaskRequest to set
	 */

	public void setCqtStatisticeTaskRequest(
			CQTStatisticeTaskRequest cqtStatisticeTaskRequest) {
		this.cqtStatisticeTaskRequest = cqtStatisticeTaskRequest;
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
