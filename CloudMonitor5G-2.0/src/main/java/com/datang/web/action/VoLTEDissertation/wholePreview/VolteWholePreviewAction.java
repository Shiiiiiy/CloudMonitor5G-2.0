/**
 * 
 */
package com.datang.web.action.VoLTEDissertation.wholePreview;

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

import org.apache.poi.ss.usermodel.Workbook;
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
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.exceptionEvent.IVolteExceptionEventService;
import com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService;
import com.datang.service.VoLTEDissertation.qualityBadRoad.IVoiceQualityBadRoadService;
import com.datang.service.VoLTEDissertation.videoQualityBad.IVideoQualityBadService;
import com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * volte专题.整体概览Action
 * 
 * @author yinzhipeng
 * @date:2015年11月18日 下午2:24:45
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class VolteWholePreviewAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(VolteWholePreviewAction.class);
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

	/**
	 * 质差路段分析service
	 */
	@Autowired
	private IVoiceQualityBadRoadService voiceQualityBadRoadService;

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
	 * 视频质差Service
	 */
	@Autowired
	private IVideoQualityBadService videoQualityBadService;

	/**
	 * 是否是对比分析
	 */
	private Boolean isCompare;

	/**
	 * 跳转到整体概览界面
	 * 
	 * @return
	 */
	public String wholePreList() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
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
				ValueStack valueStack = ActionContext.getContext()
						.getValueStack();
				valueStack.set("sumQBRDistance",
						voiceQualityBadRoadService.sumQBRDistance(ids));
				valueStack.set("exceptionEvent", volteExceptionEventService
						.sumEveryExceptionEventNum(ids));
				valueStack.set("dropping",
						droppingService.sumEveryVolteDroppingNum(ids));
				valueStack.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);

		}
		return ReturnType.LISTUI;
	}

	/**
	 * 整体分析
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
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
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
	 * 视频分析
	 * 
	 * @return
	 */
	public String wholePreDoAnalysisForVideo() {
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
				ArrayList<Long> ids = new ArrayList<Long>();
				for (TestLogItem testLogItem : queryTestLogItems) {
					if (testLogItem.getRecSeqNo() != null) {
						ids.add(testLogItem.getRecSeqNo());
					}
				}
				EasyuiPageList doWholeIndex00Analysis = videoQualityBadService
						.doWholeIndex00Analysis(videoQualityBadService
								.getVideoQualityBadsByLogIds(ids));
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				AbstractPageList queryKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
				AbstractPageList queryQuality = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
				AbstractPageList queryResource = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_PERCEPTION);
				AbstractPageList queryPerception = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				List kpiRows = queryKpi.getRows();
				List qualityRows = queryQuality.getRows();
				List resourceRows = queryResource.getRows();
				List perceptionRows = queryPerception.getRows();
				Map<String, Object> rowMaps = new HashMap<>();
				rowMaps.putAll(setBeginEndDate);
				if (null != kpiRows && 1 == kpiRows.size()) {
					Map<String, Object> rowMap = (Map<String, Object>) kpiRows
							.get(0);
					rowMaps.putAll(rowMap);
				}
				if (null != qualityRows && 1 == qualityRows.size()) {
					Map<String, Object> rowMap = (Map<String, Object>) qualityRows
							.get(0);
					rowMaps.putAll(rowMap);
				}
				if (null != resourceRows && 1 == resourceRows.size()) {
					Map<String, Object> rowMap = (Map<String, Object>) resourceRows
							.get(0);
					rowMaps.putAll(rowMap);
				}
				if (null != perceptionRows && 1 == perceptionRows.size()) {
					Map<String, Object> rowMap = (Map<String, Object>) perceptionRows
							.get(0);
					rowMaps.putAll(rowMap);
				}
				queryKpi.getRows().clear();
				queryKpi.getRows().add(rowMaps);
				ActionContext.getContext().getValueStack().push(queryKpi);
			}
		} else {
			ActionContext.getContext().getValueStack()
					.push(new EasyuiPageList());
		}
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
	 * volte语音详细报表导出
	 * 
	 * @return
	 */
	public String downloadVolteTotalExcel() {
		return "downloadVolteTotal";
	}

	/**
	 * volte语音详细报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadVolteTotal() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
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
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/VoLTE语音报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("VoLTE语音报表.xls".getBytes(), "ISO8859-1"));
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
	public String downloadVideoTotalExcel() {
		return "downloadVideoTotal";
	}

	/**
	 * volte视频详细报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadVideoTotal() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
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
				// 汇总开始时间和结束时间
				map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
				// 汇总其他信息
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
				lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(idsString);
				// 'KPI统计'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
				AbstractPageList statisticeKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("statisticeKpi", statisticeKpi);
				List statisticeKpirows = statisticeKpi.getRows();
				if (null != statisticeKpirows && 0 != statisticeKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, statisticeKpirows);
				}
				// '质量类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
				AbstractPageList qualityKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("qualityKpi", qualityKpi);
				List qualityKpirows = qualityKpi.getRows();
				if (null != qualityKpirows && 0 != qualityKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, qualityKpirows);
				}
				// '资源类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_RESOURCE);
				AbstractPageList resourceKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("resourceKpi", resourceKpi);
				List resourceKpirows = resourceKpi.getRows();
				if (null != resourceKpirows && 0 != resourceKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							listTestLogItem, resourceKpirows);
				}
				// '感知类'sheet
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_PERCEPTION);
				AbstractPageList perceptionKpi = wholePreviewService
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
					"templates/VoLTE视频报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = "VoLTE视频报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
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
