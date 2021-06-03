/**
 * 
 */
package com.datang.web.action.action5g.compareAnalysis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.compareAnalysis.ITestLogItemGridService;
import com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 5G对比分析
 * 
 * @author _YZP
 * 
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CompareAnalysis5gAction extends PageAction {

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
	 * 跳转到"5g对比分析"或者"对比日志选择"界面
	 * 
	 * @return
	 */

	public String goToCompareAnalysis5gListUI() {
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
			// 未选择测试日志,跳转到"5G对比分析"
			return ReturnType.LISTUI;
		} else {
			// 已选择测试日志,跳转到"对比日志选择"
			return "compareTestLogItem5g" + ReturnType.LISTUI;
		}
	}

	/**
	 * 跳转到"对比日志选择"界面
	 * 
	 * @return
	 */

	public String goToCompareTestLogItem5gListUI() {
		return "compareTestLogItem5g" + ReturnType.LISTUI;
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
	 * 跳转到"5g对比分析"界面
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

				// 原始日志异常事件统计
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_COMP);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				AbstractPageList queryKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				valueStack.set("exceptionEvent", new HashMap<>());
				if (null != queryKpi && 1 == queryKpi.getRows().size()) {
					valueStack.set("exceptionEvent", queryKpi.getRows().get(0));
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
				ValueStack valueStack = ActionContext.getContext()
						.getValueStack();
				// 对比日志异常事件统计
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_COMP);
				lteWholePreviewParam.setTestLogItemIds(compareTestLogItemIds);
				AbstractPageList queryKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				valueStack.set("compareExceptionEvent", new HashMap<>());
				if (null != queryKpi && 1 == queryKpi.getRows().size()) {
					valueStack.set("compareExceptionEvent", queryKpi.getRows()
							.get(0));
				}
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
	 * 5G对比分析--日志基础信息和指标列表
	 * 
	 * @return
	 */
	public String wholePreDoAnalysis() {
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
				// 汇总开始时间和结束时间
				Map<String, Object> setBeginEndDate = TestLogItemUtils
						.amountBeginEndDate(queryTestLogItems);
				// 汇总其他信息
				TestLogItemUtils.amountTestLog(queryTestLogItems,
						setBeginEndDate);
				// 添加日志valueType是否是对比分析
				TestLogItemUtils.amountTestLogValueType(setBeginEndDate, 0);
				if (null != isCompare && isCompare) {
					TestLogItemUtils.amountTestLogValueType(setBeginEndDate, 1);
				}

				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_5G_COMP);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				AbstractPageList queryKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				List rows = queryKpi.getRows();
				if (null != rows && 1 == rows.size()) {
					Map<String, Object> rowMap = (Map<String, Object>) rows
							.get(0);
					rowMap.putAll(setBeginEndDate);
				}
				ActionContext.getContext().getValueStack().push(queryKpi);
			}
		} else {
			ActionContext.getContext().getValueStack()
					.push(new EasyuiPageList());
		}
		return ReturnType.JSON;
	}

	/**
	 * 5G对比分析详细报表导出
	 * 
	 * @return
	 */
	public String download5gCompareExcel() {
		return "download5gCompareExcel";
	}

	/**
	 * 5G对比分析详细报表导出
	 * 
	 * @return
	 */
	public InputStream getFgCompareExcel() {
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

				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);

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
				// 导出'分段占比统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.SUB_INDEX_PROPORTION);
				AbstractPageList subKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("subKpi", subKpi);
				List subKpirows = subKpi.getRows();
				if (null != subKpirows && 0 != subKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, subKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		} else {
			map.put("coverKpi", new EasyuiPageList());
			map.put("disturbKpi", new EasyuiPageList());
			map.put("subKpi", new EasyuiPageList());
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

				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_5G);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);

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
				// 导出'分段占比统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.SUB_INDEX_PROPORTION);
				AbstractPageList subKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				compareMap.put("subKpi", subKpi);
				List subKpirows = subKpi.getRows();
				if (null != subKpirows && 0 != subKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, subKpirows);
				}
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		} else {
			compareMap.put("coverKpi", new EasyuiPageList());
			compareMap.put("disturbKpi", new EasyuiPageList());
			compareMap.put("subKpi", new EasyuiPageList());
		}

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/5G对比分析报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("5G对比分析报表.xls".getBytes(), "ISO8859-1"));
		} catch (IOException e) {

		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
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
				.doTestLogItemGridAnalysisFG(ids, compareIds);
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
