/**
 * 
 */
package com.datang.web.action.stream.streamWholePreview;

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
import com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService;
import com.datang.service.stream.streamVideoQualitybad.IStreamQualityBadService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.util.NumberFormatUtils;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.datang.web.beans.stream.streamVideoQualitybad.StreamWholeResponseBean5;
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
public class StreamWholePreviewAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(StreamWholePreviewAction.class);
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
	 * 视频质差Service
	 */
	@Autowired
	private IStreamQualityBadService streamQualityBadService;

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
	 * VMOS占比分析
	 * 
	 * @return
	 */
	public String wholeVmosDoAnalysis() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != isCompare && isCompare) {
			attribute = ServletActionContext.getRequest().getSession()
					.getAttribute("compareTestLogItemIds");
		}
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				// List<TestLogItem> queryTestLogItems = testlogItemService
				// .queryTestLogItems(testLogItemIds);
				ArrayList<Long> arrayList = new ArrayList<Long>();
				String[] split = testLogItemIds.split(",");
				for (String string : split) {
					arrayList.add(Long.valueOf(string));
				}
				List queryVmosValue = streamQualityBadService
						.queryVmosValue(arrayList);
				EasyuiPageList easyuiPageList1 = new EasyuiPageList();
				EasyuiPageList easyuiPageList2 = new EasyuiPageList();
				ArrayList<StreamWholeResponseBean5> list1 = new ArrayList<StreamWholeResponseBean5>();
				ArrayList<StreamWholeResponseBean5> list2 = new ArrayList<StreamWholeResponseBean5>();
				StreamWholeResponseBean5 vmos1080to13 = new StreamWholeResponseBean5();
				StreamWholeResponseBean5 vmos1080to35 = new StreamWholeResponseBean5();
				StreamWholeResponseBean5 vmosOtherto125 = new StreamWholeResponseBean5();
				StreamWholeResponseBean5 vmosOtherto255 = new StreamWholeResponseBean5();
				vmos1080to13.setName("[1,3)");
				vmos1080to13
						.setValue(Float.valueOf(String.valueOf(queryVmosValue
								.get(0))) != 0 ? NumberFormatUtils.format(
								Float.valueOf(String.valueOf(queryVmosValue
										.get(1)))
										/ Float.valueOf(String
												.valueOf(queryVmosValue.get(0)))
										* 100, 3)
								: 0f);
				vmos1080to35.setName("[3.5)");
				vmos1080to35
						.setValue(Float.valueOf(String.valueOf(queryVmosValue
								.get(0))) != 0 ? NumberFormatUtils.format(
								Float.valueOf(String.valueOf(queryVmosValue
										.get(2)))
										/ Float.valueOf(String
												.valueOf(queryVmosValue.get(0)))
										* 100, 3)
								: 0f);
				vmosOtherto125.setName("[1,2.5)");
				vmosOtherto125
						.setValue(Float.valueOf(String.valueOf(queryVmosValue
								.get(3))) != 0 ? NumberFormatUtils.format(
								Float.valueOf(String.valueOf(queryVmosValue
										.get(4)))
										/ Float.valueOf(String
												.valueOf(queryVmosValue.get(3)))
										* 100, 3)
								: 0f);
				vmosOtherto255.setName("[2.5,5)");
				vmosOtherto255
						.setValue(Float.valueOf(String.valueOf(queryVmosValue
								.get(3))) != 0 ? NumberFormatUtils.format(
								Float.valueOf(String.valueOf(queryVmosValue
										.get(5)))
										/ Float.valueOf(String
												.valueOf(queryVmosValue.get(3)))
										* 100, 3)
								: 0f);
				list1.add(vmos1080to13);
				list1.add(vmos1080to35);
				list2.add(vmosOtherto125);
				list2.add(vmosOtherto255);
				easyuiPageList1.setRows(list1);
				easyuiPageList2.setRows(list2);
				Map<String, EasyuiPageList> map = new HashMap<>();
				map.put("wholeRoadIndex0", easyuiPageList1);
				map.put("wholeRoadIndex1", easyuiPageList2);
				ActionContext.getContext().getValueStack().push(map);
			}
		} else {
			ActionContext.getContext().getValueStack()
					.push(new EasyuiPageList());
		}
		return ReturnType.JSON;
	}

	/**
	 * 流媒体业务指标分析
	 * 
	 * @return
	 */
	public String streamOperationDoAnalysis() {
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
				ArrayList<Long> ids = new ArrayList<Long>();
				for (TestLogItem testLogItem : queryTestLogItems) {
					if (testLogItem.getRecSeqNo() != null) {
						ids.add(testLogItem.getRecSeqNo());
					}
				}
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_LTE);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				AbstractPageList queryKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList queryQuality = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList queryResource = wholePreviewService
						.queryKpi(lteWholePreviewParam);

				List kpiRows = queryKpi.getRows();
				List qualityRows = queryQuality.getRows();
				List resourceRows = queryResource.getRows();
				Map<String, Object> rowMaps = new HashMap<>();
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

	/**
	 * 流媒体感知指标分析
	 * 
	 * @return
	 */
	public String streamPerceptionDoAnalysis() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != isCompare && isCompare) {
			attribute = ServletActionContext.getRequest().getSession()
					.getAttribute("compareTestLogItemIds");
		}
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_STREAM);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_PERCEPTION_INDEX);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				AbstractPageList queryKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
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
	 * 详细报表导出
	 * 
	 * @return
	 */
	public String downloadTotalExcel() {
		return "downloadTotal";
	}

	/**
	 * 详细报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadTotal() {
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
						.setReportType(ParamConstant.REPOR_TTYPE_LTE);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				AbstractPageList queryKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList queryQuality = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_DISTURB);
				AbstractPageList queryResource = wholePreviewService
						.queryKpi(lteWholePreviewParam);

				List kpiRows = queryKpi.getRows();
				List qualityRows = queryQuality.getRows();
				List resourceRows = queryResource.getRows();
				Map<String, Object> rowMaps = new HashMap<>();
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
				queryKpi.getRows().clear();
				queryKpi.getRows().add(rowMaps);
				map.put("totalKpi", queryKpi);
				List totalKpirows = queryKpi.getRows();
				if (null != totalKpirows && 0 != totalKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, totalKpirows);
				}
				// 导出'感知类指标'sheet
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_STREAM);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_PERCEPTION_INDEX);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(testLogItemIds);
				AbstractPageList volteKpi = wholePreviewService
						.queryKpi(lteWholePreviewParam);
				map.put("volteKpi", volteKpi);
				List volteKpirows = volteKpi.getRows();
				if (null != volteKpirows && 0 != volteKpirows.size()) {
					TestLogItemUtils.amountFileNameAndTerminalGroup(
							queryTestLogItems, volteKpirows);
				}

			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		} else {
			map.put("totalKpi", new EasyuiPageList());
			map.put("volteKpi", new EasyuiPageList());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/流媒体业务报表模板.xlsx");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("流媒体业务报表模板.xlsx".getBytes(), "ISO8859-1"));
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
