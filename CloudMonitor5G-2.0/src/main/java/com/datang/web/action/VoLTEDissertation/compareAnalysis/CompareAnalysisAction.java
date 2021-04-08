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
 * volte质量专题---volte对比分析
 * 
 * @author yinzhipeng
 * @date:2016年5月23日 下午1:15:25
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CompareAnalysisAction extends PageAction {
	/**
	 * 异常事件分析service
	 */
	@Autowired
	private IVolteExceptionEventService volteExceptionEventService;
	/**
	 * 切换失败分析Service
	 */
	@Autowired
	private VolteDroppingService droppingService;

	/**
	 * 质差路段分析service
	 */
	@Autowired
	private IVoiceQualityBadRoadService voiceQualityBadRoadService;

	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;

	/**
	 * 整体概览分析service
	 */
	@Resource(name = "voLTEDissWholePreviewBean")
	private IVoLTEService wholePreviewService;

	@Autowired
	private ITestLogItemGridService testLogItemGridService;

	/**
	 * 测试日志id数组按','分隔
	 */
	private String compareTestLogItemIds;

	/**
	 * 是否是对比分析
	 */
	private Boolean isCompare;

	/**
	 * 跳转到"VOLTE对比分析"或者"对比日志选择"界面
	 * 
	 * @return
	 */
	public String goToCompareAnalysisListUI() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		ServletActionContext.getRequest().getSession()
				.setAttribute("compareTestLogItemIds", null);
		// 存储TestLogItem的id集合
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
		// 对比TestLogItem的id集合
		List<Long> compareIds = new ArrayList<>();
		if (null != compareTestLogItemIdsObj) {
			if (compareTestLogItemIdsObj instanceof String) {
				String compareTestLogItemIds = (String) compareTestLogItemIdsObj;
				String[] logIds = compareTestLogItemIds.trim().split(",");
				// 存储TestLogItem的id集合
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
			// 未选择测试日志,跳转到"VOLTE对比分析"
			return ReturnType.LISTUI;
		} else {
			// 已选择测试日志,跳转到"对比日志选择"
			return "compareTestLogItem" + ReturnType.LISTUI;
		}
	}

	/**
	 * 跳转到"对比日志选择"界面
	 * 
	 * @return
	 */
	public String goToCompareTestLogItemListUI() {
		return "compareTestLogItem" + ReturnType.LISTUI;
	}

	/**
	 * 对比日志选择完成
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
	 * 跳转到"VOLTE对比分析"界面
	 * 
	 * @return
	 */
	public String goToCompareAnalysis() {
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到"整体概览"界面
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
				// 存储TestLogItem的id集合
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
				// 存储TestLogItem的id集合
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
	 * 对比分析,volte语音详细报表导出
	 * 
	 * @return
	 */
	public String downloadVolteCompareExcel() {
		return "downloadVolteCompareExcel";
	}

	/**
	 * 对比分析,volte语音详细报表导出
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
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// 汇总其他信息
				// TestLogItemUtils.amountTestLog(queryTestLogItems,
				// setBeginEndDate);

				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				// 导出'KPI汇总'sheet
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
				// 导出'VOLTE统计指标'sheet
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
				// 导出'CS域语音统计指标'sheet
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
				// 导出'覆盖类'sheet
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
				// 导出'干扰类'sheet
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
				// 导出'调度类'sheet
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
				// 导出'MOS统计'sheet
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
				// 汇总开始时间和结束时间
				compareMap.putAll(TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems));
				// 汇总其他信息
				// TestLogItemUtils.amountTestLog(queryTestLogItems,
				// setBeginEndDate);

				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				// 导出'KPI汇总'sheet
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
				// 导出'VOLTE统计指标'sheet
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
				// 导出'CS域语音统计指标'sheet
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
				// 导出'覆盖类'sheet
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
				// 导出'干扰类'sheet
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
				// 导出'调度类'sheet
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
				// 导出'MOS统计'sheet
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
					"templates/VoLTE语音对比分析报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("VoLTE语音对比分析报表.xls".getBytes(), "ISO8859-1"));
		} catch (IOException e) {

		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 跳转到"异常事件对比分析"界面
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
				// 存储TestLogItem的id集合
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
				// 存储TestLogItem的id集合
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
	 * 跳转到"MOS质差黑点对比分析"界面
	 * 
	 * @return
	 */
	public String goToMosQualityBad() {
		return "mosQualityBad";
	}

	/**
	 * MOS差黑点分析
	 * 
	 * @return
	 */
	public String doMosQualityBadAnalysis() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		Object testLogItemIdsObj = session.getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = session
				.getAttribute("compareTestLogItemIds");

		// 将存储结果放置session,session中的key
		StringBuffer sessionIDS = new StringBuffer();
		if (null != testLogItemIdsObj) {
			if (testLogItemIdsObj instanceof String) {
				String testLogItemIds = (String) testLogItemIdsObj;
				sessionIDS.append(testLogItemIds);
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			session.setAttribute("testLogItemIds", testLogItemIdsObj);
		}
		// 处理对比日志id
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

		// 存储TestLogItem的id集合
		List<Long> ids = new ArrayList<>();
		List<Long> compareIds = new ArrayList<>();
		// 处理原始日志id
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
		// 处理对比日志id
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
	 * 跳转到"SRVCC切换失败对比分析"界面
	 * 
	 * @return
	 */
	public String goToSrvccHo() {
		Object testLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		// 存储TestLogItem的id集合
		List<Long> ids = new ArrayList<>();
		List<Long> compareIds = new ArrayList<>();

		// 处理原始日志id
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
		// 处理对比日志id
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
				// 日志类型:原始日志
				volteDroppingSRVCC.setTestLogType(0);
			} else {
				// 日志类型:对比日志
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
	 * 切换对比分析的共用整体指标分析
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
					// 添加日志valueType是否是对比分析
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
	 * 跳转到"系统内切换失败对比分析"界面
	 * 
	 * @return
	 */
	public String goToSystemHo() {
		Object testLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		// 存储TestLogItem的id集合
		List<Long> ids = new ArrayList<>();
		List<Long> compareIds = new ArrayList<>();

		// 处理原始日志id
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
		// 处理对比日志id
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
				// 日志类型:原始日志
				volteDroppingInt.setTestLogType(0);
			} else {
				// 日志类型:对比日志
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
	 * 跳转到"栅格对比分析"界面
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
				// 存储TestLogItem的id集合
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
				// 存储TestLogItem的id集合
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
	 * 栅格对比分析
	 * 
	 * @return
	 */
	public String doLatticeCellAnalysis() {
		Object testLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("testLogItemIds");
		Object compareTestLogItemIdsObj = ServletActionContext.getRequest()
				.getSession().getAttribute("compareTestLogItemIds");
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		// 存储TestLogItem的id集合
		List<Long> ids = new ArrayList<>();
		List<Long> compareIds = new ArrayList<>();

		// 处理原始日志id
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
		// 处理对比日志id
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
