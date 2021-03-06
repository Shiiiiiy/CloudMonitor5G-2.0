/**
 * 
 */
package com.datang.web.action.VoLTEDissertation.compareAnalysis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;
import com.datang.common.util.TestLogItemUtils;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEvent;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingInt;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingSRVCC;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.compareAnalysis.ITestLogItemGridService;
import com.datang.service.VoLTEDissertation.exceptionEvent.IVolteExceptionEventService;
import com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService;
import com.datang.service.VoLTEDissertation.qualityBadRoad.IVoiceQualityBadRoadService;
import com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * volte????????????---volte????????????
 * 
 * @author yinzhipeng
 * @date:2016???5???23??? ??????1:15:25
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CompareAnalysisAction extends PageAction {
	/**
	 * ??????????????????service
	 */
	@Autowired
	private IVolteExceptionEventService volteExceptionEventService;
	/**
	 * ??????????????????Service
	 */
	@Autowired
	private VolteDroppingService droppingService;

	/**
	 * ??????????????????service
	 */
	@Autowired
	private IVoiceQualityBadRoadService voiceQualityBadRoadService;

	/**
	 * ??????????????????
	 */
	@Autowired
	private ITestLogItemService testlogItemService;

	/**
	 * ??????????????????service
	 */
	@Resource(name = "voLTEDissWholePreviewBean")
	private IVoLTEService wholePreviewService;

	@Autowired
	private ITestLogItemGridService testLogItemGridService;

	/**
	 * ????????????id?????????','??????
	 */
	private String compareTestLogItemIds;

	/**
	 * ?????????????????????
	 */
	private Boolean isCompare;

	/**
	 * ?????????"VOLTE????????????"??????"??????????????????"??????
	 * 
	 * @return
	 */
	public String goToCompareAnalysisListUI() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		ServletActionContext.getRequest().getSession()
				.setAttribute("compareTestLogItemIds", null);
		// ??????TestLogItem???id??????
		List<Long> ids = new ArrayList<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				String[] logIds = testLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}

		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		// ??????TestLogItem???id??????
		List<Long> compareIds = new ArrayList<>();
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							compareIds.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				ActionContext.getContext().getValueStack()
						.set("compareTestLogItemIds", compareTestLogItemIds);
			}
			ServletActionContext
					.getRequest()
					.getSession()
					.setAttribute("compareTestLogItemIds",
							compareTestLogItemIdsObj);
		}
		if (0 == ids.size()) {
			// ?????????????????????,?????????"VOLTE????????????"
			return ReturnType.LISTUI;
		} else {
			// ?????????????????????,?????????"??????????????????"
			return "compareTestLogItem" + ReturnType.LISTUI;
		}
	}

	/**
	 * ?????????"??????????????????"??????
	 * 
	 * @return
	 */
	public String goToCompareTestLogItemListUI() {
		return "compareTestLogItem" + ReturnType.LISTUI;
	}

	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	public String saveCompareTestLogItem() {
		if (StringUtils.hasText(compareTestLogItemIds)) {
			ServletActionContext
					.getRequest()
					.getSession()
					.setAttribute("compareTestLogItemIds",
							compareTestLogItemIds);
		}
		ActionContext.getContext().getValueStack().push("SUCCESS");
		return ReturnType.JSON;
	}

	/**
	 * ?????????"VOLTE????????????"??????
	 * 
	 * @return
	 */
	public String goToCompareAnalysis() {
		return ReturnType.LISTUI;
	}

	/**
	 * ?????????"????????????"??????
	 * 
	 * @return
	 */
	public String goToWhole() {
		Object testLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("testLogItemIds");
		if (null != testLogItemIdsObj) {
			if (testLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) testLogItemIdsObj;
				String[] logIds = testLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				ValueStack valueStack = ActionContext.getContext()
						.getValueStack();
				valueStack.set("exceptionEvent", volteExceptionEventService
						.sumEveryExceptionEventNum(ids));
				valueStack.set("dropping",
						droppingService.sumEveryVolteDroppingNum(ids));
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", testLogItemIdsObj);
		}
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				ValueStack valueStack = ActionContext.getContext()
						.getValueStack();
				valueStack.set("compareExceptionEvent",
						volteExceptionEventService
								.sumEveryExceptionEventNum(ids));
				valueStack.set("compareDropping",
						droppingService.sumEveryVolteDroppingNum(ids));
				valueStack.set("compareTestLogItemIds", compareTestLogItemIds);
			}
			ServletActionContext
					.getRequest()
					.getSession()
					.setAttribute("compareTestLogItemIds",
							compareTestLogItemIdsObj);
		}
		return "whole";
	}

	/**
	 * ????????????,volte????????????????????????
	 * 
	 * @return
	 */
	public String downloadVolteCompareExcel() {
		return "downloadVolteCompareExcel";
	}

	/**
	 * ????????????,volte????????????????????????
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getVolteCompareExcel() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				// ?????????????????????????????????
				map.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// ??????????????????
				// TestLogItemUtils.amountTestLog(queryTestLogItems,
				// setBeginEndDate);

				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				// ??????'KPI??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
				AbstractPageList totalKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("totalKpi", totalKpi);
				List totalKpirows = totalKpi.getRows();
				if (null != totalKpirows && 0 != totalKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, totalKpirows);
				}
				// ??????'VOLTE????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
				AbstractPageList volteKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("volteKpi", volteKpi);
				List volteKpirows = volteKpi.getRows();
				if (null != volteKpirows && 0 != volteKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, volteKpirows);
				}
				// ??????'CS?????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
				AbstractPageList csKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("csKpi", csKpi);
				List csKpirows = csKpi.getRows();
				if (null != csKpirows && 0 != csKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, csKpirows);
				}
				// ??????'?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, coverKpirows);
				}
				// ??????'?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, disturbKpirows);
				}
				// ??????'?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, dispatcherKpirows);
				}
				// ??????'MOS??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
				AbstractPageList mosKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("mosKpi", mosKpi);
				List mosKpirows = mosKpi.getRows();
				if (null != mosKpirows && 0 != mosKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, mosKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		} else {
			map.put("totalKpi", new EasyuiPageList());
			map.put("volteKpi", new EasyuiPageList());
			map.put("csKpi", new EasyuiPageList());
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("dispatcherKpi", new EasyuiPageList());
			map.put("mosKpi", new EasyuiPageList());
		}

		Map<String, Object> compareMap = new HashMap<>();
		map.put("compareMap", compareMap);
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) compareTestLogItemIdsObj;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				// ?????????????????????????????????
				compareMap.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// ??????????????????
				// TestLogItemUtils.amountTestLog(queryTestLogItems,
				// setBeginEndDate);

				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				// ??????'KPI??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_TOTAL_KPI);
				AbstractPageList totalKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				compareMap.put("totalKpi", totalKpi);
				List totalKpirows = totalKpi.getRows();
				if (null != totalKpirows && 0 != totalKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, totalKpirows);
				}
				// ??????'VOLTE????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE);
				AbstractPageList volteKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				compareMap.put("volteKpi", volteKpi);
				List volteKpirows = volteKpi.getRows();
				if (null != volteKpirows && 0 != volteKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, volteKpirows);
				}
				// ??????'CS?????????????????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_CS);
				AbstractPageList csKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				compareMap.put("csKpi", csKpi);
				List csKpirows = csKpi.getRows();
				if (null != csKpirows && 0 != csKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, csKpirows);
				}
				// ??????'?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				compareMap.put("coverKpi", coverKpi);
				List coverKpirows = coverKpi.getRows();
				if (null != coverKpirows && 0 != coverKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, coverKpirows);
				}
				// ??????'?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList disturbKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				compareMap.put("disturbKpi", disturbKpi);
				List disturbKpirows = disturbKpi.getRows();
				if (null != disturbKpirows && 0 != disturbKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, disturbKpirows);
				}
				// ??????'?????????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISPATCHER);
				AbstractPageList dispatcherKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				compareMap.put("dispatcherKpi", dispatcherKpi);
				List dispatcherKpirows = dispatcherKpi.getRows();
				if (null != dispatcherKpirows && 0 != dispatcherKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, dispatcherKpirows);
				}
				// ??????'MOS??????'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOS);
				AbstractPageList mosKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				compareMap.put("mosKpi", mosKpi);
				List mosKpirows = mosKpi.getRows();
				if (null != mosKpirows && 0 != mosKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, mosKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		} else {
			compareMap.put("totalKpi", new EasyuiPageList());
			compareMap.put("volteKpi", new EasyuiPageList());
			compareMap.put("csKpi", new EasyuiPageList());
			compareMap.put("coverKpi", new EasyuiPageList());
			compareMap.put("disturbKpi", new EasyuiPageList());
			compareMap.put("dispatcherKpi", new EasyuiPageList());
			compareMap.put("mosKpi", new EasyuiPageList());
		}

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/VoLTE????????????????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("VoLTE????????????????????????.xls".getBytes(), "ISO8859-1"));
		} catch (IOException e) {

		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * ?????????"????????????????????????"??????
	 * 
	 * @return
	 */
	public String goToExceptionEvent() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				String[] logIds = testLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				List<VolteExceptionEvent> volteExceptionEventByLogIds = volteExceptionEventService
						.getVolteExceptionEventByIds(ids);
				valueStack.set("events", volteExceptionEventByLogIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				List<VolteExceptionEvent> volteExceptionEventByLogIds = volteExceptionEventService
						.getVolteExceptionEventByIds(ids);
				valueStack.set("compareEvents", volteExceptionEventByLogIds);
			}
			ServletActionContext
					.getRequest()
					.getSession()
					.setAttribute("compareTestLogItemIds",
							compareTestLogItemIdsObj);
		}
		return "exceptionEvent";
	}

	/**
	 * ?????????"MOS????????????????????????"??????
	 * 
	 * @return
	 */
	public String goToMosQualityBad() {
		return "mosQualityBad";
	}

	/**
	 * MOS???????????????
	 * 
	 * @return
	 */
	public String doMosQualityBadAnalysis() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		Object testLogItemIdsObj = session.getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = session
				.getAttribute("compareTestLogItemIds");

		// ?????????????????????session,session??????key
		StringBuffer sessionIDS = new StringBuffer();
		if (null != testLogItemIdsObj) {
			if (testLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) testLogItemIdsObj;
				sessionIDS.append(testLogItemIds);
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			session.setAttribute("testLogItemIds", testLogItemIdsObj);
		}
		// ??????????????????id
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				sessionIDS.append("_");
				sessionIDS.append(compareTestLogItemIds);
				valueStack.set("compareTestLogItemIds", compareTestLogItemIds);
			}
			session.setAttribute("compareTestLogItemIds",
					compareTestLogItemIdsObj);
		}
		Object sessionResult = session.getAttribute(sessionIDS.toString());
		if (null != sessionResult) {
			if (sessionResult instanceof Map) {
				Map<String, EasyuiPageList> sessionMap = (Map<String, EasyuiPageList>) sessionResult;
				session.setAttribute(sessionIDS.toString(), sessionMap);
				valueStack.push(sessionMap);
				return ReturnType.JSON;
			}
		}

		// ??????TestLogItem???id??????
		List<Long> ids = new ArrayList<>();
		List<Long> compareIds = new ArrayList<>();
		// ??????????????????id
		if (null != testLogItemIdsObj) {
			if (testLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) testLogItemIdsObj;
				String[] logIds = testLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			session.setAttribute("testLogItemIds", testLogItemIdsObj);
		}
		// ??????????????????id
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							compareIds.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("compareTestLogItemIds", compareTestLogItemIds);
			}
			session.setAttribute("compareTestLogItemIds",
					compareTestLogItemIdsObj);
		}

		Map<String, EasyuiPageList> doMosBadAnalysis = voiceQualityBadRoadService
				.doMosBadAnalysis(ids, compareIds);
		session.setAttribute(sessionIDS.toString(), doMosBadAnalysis);
		valueStack.push(doMosBadAnalysis);
		return ReturnType.JSON;
	}

	/**
	 * ?????????"SRVCC????????????????????????"??????
	 * 
	 * @return
	 */
	public String goToSrvccHo() {
		Object testLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		// ??????TestLogItem???id??????
		List<Long> ids = new ArrayList<>();
		List<Long> compareIds = new ArrayList<>();

		// ??????????????????id
		if (null != testLogItemIdsObj) {
			if (testLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) testLogItemIdsObj;
				String[] logIds = testLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", testLogItemIdsObj);
		}
		// ??????????????????id
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							compareIds.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("compareTestLogItemIds", compareTestLogItemIds);
			}
			ServletActionContext
					.getRequest()
					.getSession()
					.setAttribute("compareTestLogItemIds",
							compareTestLogItemIdsObj);
		}

		List<VolteDroppingSRVCC> volteDroppingSRVCCsByLogIdsAndCompareLogIds = droppingService
				.getVolteDroppingSRVCCsByLogIdsAndCompareLogIds(ids, compareIds);
		for (VolteDroppingSRVCC volteDroppingSRVCC : volteDroppingSRVCCsByLogIdsAndCompareLogIds) {
			if (ids.contains(volteDroppingSRVCC.getTestLogItem().getRecSeqNo())) {
				// ????????????:????????????
				volteDroppingSRVCC.setTestLogType(0);
			} else {
				// ????????????:????????????
				volteDroppingSRVCC.setTestLogType(1);
			}
		}
		if (0 != ids.size() && 0 != compareIds.size()) {
			valueStack.set("srvccHos",
					volteDroppingSRVCCsByLogIdsAndCompareLogIds);
		}
		return "srvccHo";
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String doHOFAnalysis() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != isCompare && isCompare) {
			attribute = ServletActionContext.getRequest().getSession()
					.getAttribute("compareTestLogItemIds");
		}
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				EasyuiPageList droppingSRVCCWholeIndex0Analysis = droppingService
						.droppingSRVCCWholeIndex0Analysis(queryTestLogItems);
				List rows = droppingSRVCCWholeIndex0Analysis.getRows();
				if (null != rows && 1 == rows.size()) {
					Map<String, Object> rowMap = (Map<String, Object>) rows
							.get(0);
					// ????????????valueType?????????????????????
					TestLogItemUtils.amountTestLogValueType(rowMap, 0);
					if (null != isCompare && isCompare) {
						TestLogItemUtils.amountTestLogValueType(rowMap, 1);
					}
				}
				ActionContext.getContext().getValueStack()
						.push(droppingSRVCCWholeIndex0Analysis);
			}
		} else {
			ActionContext.getContext().getValueStack()
					.push(new EasyuiPageList());
		}
		return ReturnType.JSON;
	}

	/**
	 * ?????????"?????????????????????????????????"??????
	 * 
	 * @return
	 */
	public String goToSystemHo() {
		Object testLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		// ??????TestLogItem???id??????
		List<Long> ids = new ArrayList<>();
		List<Long> compareIds = new ArrayList<>();

		// ??????????????????id
		if (null != testLogItemIdsObj) {
			if (testLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) testLogItemIdsObj;
				String[] logIds = testLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", testLogItemIdsObj);
		}
		// ??????????????????id
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							compareIds.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("compareTestLogItemIds", compareTestLogItemIds);
			}
			ServletActionContext
					.getRequest()
					.getSession()
					.setAttribute("compareTestLogItemIds",
							compareTestLogItemIdsObj);
		}

		List<VolteDroppingInt> volteDroppingIntsByLogIdsAndCompareLogIds = droppingService
				.getVolteDroppingIntsByLogIdsAndCompareLogIds(ids, compareIds);
		for (VolteDroppingInt volteDroppingInt : volteDroppingIntsByLogIdsAndCompareLogIds) {
			if (ids.contains(volteDroppingInt.getTestLogItem().getRecSeqNo())) {
				// ????????????:????????????
				volteDroppingInt.setTestLogType(0);
			} else {
				// ????????????:????????????
				volteDroppingInt.setTestLogType(1);
			}
		}
		if (0 != ids.size() && 0 != compareIds.size()) {
			valueStack.set("systemHos",
					volteDroppingIntsByLogIdsAndCompareLogIds);
		}
		return "systemHo";
	}

	/**
	 * ?????????"??????????????????"??????
	 * 
	 * @return
	 */
	public String goToLatticeCell() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		Object testLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("testLogItemIds");
		if (null != testLogItemIdsObj) {
			if (testLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) testLogItemIdsObj;
				String[] logIds = testLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", testLogItemIdsObj);
		}
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("compareTestLogItemIds", compareTestLogItemIds);
			}
			ServletActionContext
					.getRequest()
					.getSession()
					.setAttribute("compareTestLogItemIds",
							compareTestLogItemIdsObj);
		}
		return "latticeCell";
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	public String doLatticeCellAnalysis() {
		Object testLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		// ??????TestLogItem???id??????
		List<Long> ids = new ArrayList<>();
		List<Long> compareIds = new ArrayList<>();

		// ??????????????????id
		if (null != testLogItemIdsObj) {
			if (testLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) testLogItemIdsObj;
				String[] logIds = testLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							ids.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", testLogItemIdsObj);
		}
		// ??????????????????id
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				for (int i = 0; i < logIds.length; i++) {
					if (StringUtils.hasText(logIds[i])) {
						try {
							compareIds.add(Long.parseLong(logIds[i].trim()));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
				valueStack.set("compareTestLogItemIds", compareTestLogItemIds);
			}
			ServletActionContext
					.getRequest()
					.getSession()
					.setAttribute("compareTestLogItemIds",
							compareTestLogItemIdsObj);
		}
		Map<String, EasyuiPageList> doTestLogItemGridAnalysis = testLogItemGridService
				.doTestLogItemGridAnalysis(ids, compareIds);
		valueStack.push(doTestLogItemGridAnalysis);
		return ReturnType.JSON;
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the compareTestLogItemIdscompareTestLogItemIds
	 */
	public String getCompareTestLogItemIds() {
		return compareTestLogItemIds;
	}

	/**
	 * @param compareTestLogItemIds
	 *            the compareTestLogItemIds to set
	 */
	public void setCompareTestLogItemIds(String compareTestLogItemIds) {
		this.compareTestLogItemIds = compareTestLogItemIds;
	}

	/**
	 * @return the isCompareisCompare
	 */
	public Boolean getIsCompare() {
		return isCompare;
	}

	/**
	 * @param isCompare
	 *            the isCompare to set
	 */
	public void setIsCompare(Boolean isCompare) {
		this.isCompare = isCompare;
	}

}
