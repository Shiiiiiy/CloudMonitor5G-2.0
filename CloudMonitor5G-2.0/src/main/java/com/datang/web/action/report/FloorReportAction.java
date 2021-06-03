package com.datang.web.action.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

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
import com.datang.domain.report.CQTStatisticeTask;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.report.ICQTReportService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.gisSql.FloorGpsPointInfo;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.datang.web.beans.report.cqt.CQTStatisticeTaskRequest;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 楼宇统计Action
 * 
 * @explain
 * @name FloorReportAction
 * @author shenyanwei
 * @date 2016年11月2日上午9:17:01
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class FloorReportAction extends PageAction implements
		ModelDriven<CQTStatisticeTaskRequest> {
	private static Logger LOGGER = LoggerFactory
			.getLogger(FloorReportAction.class);

	private CQTStatisticeTaskRequest cqtStatisticeTaskRequest = new CQTStatisticeTaskRequest();
	private Integer[] testRanks;
	private String floorName;
	private String index;
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
	@Autowired
	private FloorGpsPointInfo floorGpsPointInfo;

	/**
	 * 跳转到统计界面
	 * 
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}

	@Override
	public CQTStatisticeTaskRequest getModel() {
		return cqtStatisticeTaskRequest;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 汇总单个任务信息并跳转到CQT添加页面
	 * 
	 * @return
	 */
	public String goAddInfo() {
		CQTStatisticeTask queryOneByID = new CQTStatisticeTask();
		HttpSession session = ServletActionContext.getRequest().getSession();
		queryOneByID.setLogIds(String.valueOf(session
				.getAttribute("floorLogIds")));
		queryOneByID.setCityIds(String.valueOf(session
				.getAttribute("floorCityIds")));
		queryOneByID.setBoxIds(String.valueOf(session
				.getAttribute("floorBoxIds")));
		queryOneByID.setTestRank(String.valueOf(session
				.getAttribute("floorTestRanks")));
		queryOneByID
				.setBeginDate((Date) session.getAttribute("floorBeginDate"));
		queryOneByID.setEndDate((Date) session.getAttribute("floorEndDate"));
		ActionContext.getContext().getValueStack()
				.set("statisticeTask", queryOneByID);
		return "add";
	}

	/**
	 * 分析
	 * 
	 * @return
	 */
	public String doAnalysis() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("floorLogIds",
				cqtStatisticeTaskRequest.getLogIds());
		session.setAttribute("floorBeginDate",
				cqtStatisticeTaskRequest.getBeginDate());
		session.setAttribute("floorEndDate",
				cqtStatisticeTaskRequest.getEndDate());
		session.setAttribute("floorCityIds",
				cqtStatisticeTaskRequest.getCityIds());
		session.setAttribute("floorBoxIds",
				cqtStatisticeTaskRequest.getBoxIds());
		String strtestRanks = null;
		for (Integer i : testRanks) {
			if (strtestRanks == null) {
				strtestRanks = String.valueOf(i);
			} else {
				strtestRanks = strtestRanks + "," + i;
			}
		}
		session.setAttribute("floorTestRanks", strtestRanks);
		HashMap<String, Object> returnMap = new HashMap<>();
		if (cqtStatisticeTaskRequest.getLogIds() != null) {
			List<TestLogItem> testLogItems = testLogItemService
					.queryTestLogItems(cqtStatisticeTaskRequest.getLogIds());
			if (testLogItems != null) {
				ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
				for (TestLogItem testLogItem : testLogItems) {
					if (arrayList == null || arrayList.size() == 0) {
						HashMap<String, Object> hashMap = new HashMap<>();
						hashMap.put("name", testLogItem.getFloorName());
						hashMap.put("id", testLogItem.getRecSeqNo());
						hashMap.put("floorLongItude",
								testLogItem.getFloorLongItude());
						hashMap.put("floorLatItude",
								testLogItem.getFloorLatItude());
						hashMap.put("value", testLogItem.getRecSeqNo());
						arrayList.add(hashMap);
					} else {
						int i = 0;
						for (Map<String, Object> map : arrayList) {
							if (map.get("name").equals(
									testLogItem.getFloorName().trim())) {
								map.put("value", map.get("value") + ","
										+ testLogItem.getRecSeqNo());
								i = 1;
								break;
							}
						}
						if (i == 0) {
							HashMap<String, Object> hashMap = new HashMap<>();
							hashMap.put("name", testLogItem.getFloorName());
							hashMap.put("id", testLogItem.getRecSeqNo());
							hashMap.put("floorLongItude",
									testLogItem.getFloorLongItude());
							hashMap.put("floorLatItude",
									testLogItem.getFloorLatItude());
							hashMap.put("value", testLogItem.getRecSeqNo());
							arrayList.add(hashMap);
						}
					}
				}
				ArrayList<Map<String, Object>> floorList = new ArrayList<>();
				ArrayList<Map<String, Object>> gpsList = new ArrayList<>();
				List<Object> gps = new ArrayList<>();
				for (Map<String, Object> map : arrayList) {
					VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
					lteWholePreviewParam
							.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
					// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
					// Object object = map.get("value");
					lteWholePreviewParam.setTestLogItemIds(String.valueOf(map
							.get("value")));
					AbstractPageList queryKpi = cqtReportService
							.queryKpi(lteWholePreviewParam);
					List rows = queryKpi.getRows();
					if (null != rows && 1 == rows.size()) {
						HashMap<String, Object> floorMap = new HashMap<>();
						HashMap<String, Object> gpsMap = new HashMap<>();
						Map<String, Object> rowMap = (Map<String, Object>) rows
								.get(0);
						floorMap.put("sinraverage", rowMap.get("sinraverage"));
						floorMap.put("rsrpaverage", rowMap.get("rsrpaverage"));
						floorMap.put("ltecoverage110rate",
								rowMap.get("ltecoverage110rate"));
						floorMap.put("floorName", map.get("name"));
						floorMap.put("id", map.get("id"));
						floorList.add(floorMap);
						gpsMap.put("id", map.get("id"));
						gpsMap.put("floorName", map.get("name"));
						gpsMap.put("longitude", map.get("floorLongItude"));
						gpsMap.put("latitude", map.get("floorLatItude"));
						gpsMap.put("index", rowMap.get("rsrpaverage"));
						gpsList.add(gpsMap);
					}
				}
				ArrayList<Map<String, Object>> jsonObjectList = new ArrayList<>();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("colors",
						floorGpsPointInfo.getIndexColorListMap(0));
				gps.add(0, gpsList);
				jsonObjectList.add(jsonObject);
				gps.add(1, jsonObjectList);
				returnMap.put("floor", floorList);
				returnMap.put("gps", gps);
			}
		}
		ActionContext.getContext().getValueStack().set("info", returnMap);
		return ReturnType.JSON;
	}

	/**
	 * 根据不同指标绘制楼宇
	 * 
	 * @return
	 */
	public String getFloorInfoByIndex() {
		List<Object> gps = new ArrayList<>();
		String attribute = String.valueOf(ServletActionContext.getRequest()
				.getSession().getAttribute("floorLogIds"));
		List<TestLogItem> queryCQTByFloorName = testLogItemService
				.queryTestLogItems(attribute);
		if (null != queryCQTByFloorName) {
			ServletActionContext.getRequest().getSession()
					.setAttribute("floorLogIds", attribute);
			StringBuilder moveLogIDBuilder = new StringBuilder();
			StringBuilder linkLogIDBuilder = new StringBuilder();
			StringBuilder telecomLogIDBuilder = new StringBuilder();
			ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
			for (TestLogItem testLogItem : queryCQTByFloorName) {
				if (arrayList == null || arrayList.size() == 0) {
					HashMap<String, Object> hashMap = new HashMap<>();
					hashMap.put("name", testLogItem.getFloorName());
					hashMap.put("id", testLogItem.getRecSeqNo());
					hashMap.put("floorLongItude",
							testLogItem.getFloorLongItude());
					hashMap.put("floorLatItude", testLogItem.getFloorLatItude());
					hashMap.put("value", testLogItem.getRecSeqNo());
					arrayList.add(hashMap);
				} else {
					int i = 0;
					for (Map<String, Object> map : arrayList) {
						if (map.get("name").equals(
								testLogItem.getFloorName().trim())) {
							map.put("value", map.get("value") + ","
									+ testLogItem.getRecSeqNo());
							i = 1;
							break;
						}
					}
					if (i == 0) {
						HashMap<String, Object> hashMap = new HashMap<>();
						hashMap.put("name", testLogItem.getFloorName());
						hashMap.put("id", testLogItem.getRecSeqNo());
						hashMap.put("floorLongItude",
								testLogItem.getFloorLongItude());
						hashMap.put("floorLatItude",
								testLogItem.getFloorLatItude());
						hashMap.put("value", testLogItem.getRecSeqNo());
						arrayList.add(hashMap);
					}
				}
			}
			ArrayList<Map<String, Object>> gpsList = new ArrayList<>();
			for (Map<String, Object> map : arrayList) {
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				// Object object = map.get("value");
				lteWholePreviewParam.setTestLogItemIds(String.valueOf(map
						.get("value")));
				AbstractPageList queryKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				List rows = queryKpi.getRows();
				if (null != rows && 1 == rows.size()) {
					HashMap<String, Object> floorMap = new HashMap<>();
					HashMap<String, Object> gpsMap = new HashMap<>();
					Map<String, Object> rowMap = (Map<String, Object>) rows
							.get(0);
					floorMap.put("sinraverage", rowMap.get("sinraverage"));
					floorMap.put("rsrpaverage", rowMap.get("rsrpaverage"));
					floorMap.put("ltecoverage110rate",
							rowMap.get("ltecoverage110rate"));
					gpsMap.put("id", map.get("id"));
					gpsMap.put("floorName", map.get("name"));
					gpsMap.put("longitude", map.get("floorLongItude"));
					gpsMap.put("latitude", map.get("floorLatItude"));
					if (null != index) {
						if (index.trim().equals("0")) {
							gpsMap.put("index", rowMap.get("rsrpaverage"));
						} else if (index.trim().equals("1")) {
							gpsMap.put("index", rowMap.get("sinraverage"));
						} else if (index.trim().equals("2")) {
							gpsMap.put("index",
									rowMap.get("ltecoverage110rate"));
						}
					} else {
						gpsMap.put("index", rowMap.get("rsrpaverage"));
					}
					gpsList.add(gpsMap);
				}
			}
			ArrayList<Map<String, Object>> jsonObjectList = new ArrayList<>();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("colors", floorGpsPointInfo
					.getIndexColorListMap(Integer.valueOf(index)));
			gps.add(0, gpsList);
			jsonObjectList.add(jsonObject);
			gps.add(1, jsonObjectList);
		}
		ActionContext.getContext().getValueStack().push(gps);
		return ReturnType.JSON;
	}

	/**
	 * 根据楼宇名称获取相关信息
	 * 
	 * @return
	 */
	public String getFloorInfo() {
		Map<String, Object> map = new HashMap<>();
		map.put("moveVoice", new EasyuiPageList());
		map.put("moveData", new EasyuiPageList());
		map.put("moveVideo", new EasyuiPageList());
		map.put("linkVoice", new EasyuiPageList());
		map.put("linkData", new EasyuiPageList());
		map.put("linkVideo", new EasyuiPageList());
		map.put("telecomVoice", new EasyuiPageList());
		map.put("telecomData", new EasyuiPageList());
		map.put("telecomVideo", new EasyuiPageList());
		if (null != floorName) {
			ServletActionContext.getRequest().getSession()
					.setAttribute("floorName", floorName);
			List<TestLogItem> queryCQTByFloorName = testLogItemService
					.queryCQTByFloorName(floorName);
			if (null != queryCQTByFloorName) {
				StringBuilder moveLogIDBuilder = new StringBuilder();
				StringBuilder linkLogIDBuilder = new StringBuilder();
				StringBuilder telecomLogIDBuilder = new StringBuilder();
				for (TestLogItem testLogItem : queryCQTByFloorName) {
					if (null != testLogItem.getOperatorName()) {
						if (testLogItem.getOperatorName().trim().equals("中国移动")) {
							if (0 != moveLogIDBuilder.toString().length()) {
								moveLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								moveLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						} else if (testLogItem.getOperatorName().trim()
								.equals("中国联通")) {
							if (0 != linkLogIDBuilder.toString().length()) {
								linkLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								linkLogIDBuilder.append(testLogItem
										.getRecSeqNo());
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
				VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
				lteWholePreviewParam
						.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
				if (0 != moveLogIDBuilder.toString().length()) {
					// 汇总移动信息
					// 语音信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
					// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(moveLogIDBuilder
							.toString());
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
					AbstractPageList totalKpi = cqtReportService
							.queryKpi(lteWholePreviewParam);

					map.put("moveVoice", totalKpi);
					// 数据信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_LTE);
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
					AbstractPageList indexKpi = cqtReportService
							.queryKpi(lteWholePreviewParam);
					List indexKpirows = indexKpi.getRows();
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
					AbstractPageList coverKpi = cqtReportService
							.queryKpi(lteWholePreviewParam);
					indexKpirows.addAll(coverKpi.getRows());
					map.put("moveData", indexKpi);
					// 视频信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
					AbstractPageList statisticeKpi = cqtReportService
							.queryKpi(lteWholePreviewParam);
					List statisticeKpirows = statisticeKpi.getRows();
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
					AbstractPageList qualityKpi = cqtReportService
							.queryKpi(lteWholePreviewParam);
					statisticeKpirows.addAll(qualityKpi.getRows());
					// map.put("moveVideo", statisticeKpi);
				}
				if (0 != linkLogIDBuilder.toString().length()) {
					// 汇总联通信息
					// 语音信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
					// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(linkLogIDBuilder
							.toString());
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
					AbstractPageList totalKpi2 = cqtReportService
							.queryKpi(lteWholePreviewParam);

					map.put("linkVoice", totalKpi2);
					// 数据信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_LTE);
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
					AbstractPageList indexKpi2 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					List indexKpirows2 = indexKpi2.getRows();
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
					AbstractPageList coverKpi2 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					indexKpirows2.addAll(coverKpi2.getRows());
					map.put("linkData", indexKpi2);
					// 视频信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
					AbstractPageList statisticeKpi2 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					List statisticeKpirows2 = statisticeKpi2.getRows();
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
					AbstractPageList qualityKpi2 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					statisticeKpirows2.addAll(qualityKpi2.getRows());
					map.put("linkVideo", statisticeKpi2);
				}
				if (0 != telecomLogIDBuilder.toString().length()) {
					// 汇总电信信息
					// 语音信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
					// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
					lteWholePreviewParam.setTestLogItemIds(telecomLogIDBuilder
							.toString());
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
					AbstractPageList totalKpi3 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					map.put("telecomVoice", totalKpi3);
					// 数据信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_LTE);
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
					AbstractPageList indexKpi3 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					List indexKpirows3 = indexKpi3.getRows();
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
					AbstractPageList coverKpi3 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					indexKpirows3.addAll(coverKpi3.getRows());
					map.put("telecomData", indexKpi3);
					// 视频信息
					lteWholePreviewParam
							.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
					AbstractPageList statisticeKpi3 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					List statisticeKpirows3 = statisticeKpi3.getRows();
					lteWholePreviewParam
							.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
					AbstractPageList qualityKpi3 = cqtReportService
							.queryKpi(lteWholePreviewParam);
					statisticeKpirows3.addAll(qualityKpi3.getRows());
					map.put("telecomVideo", statisticeKpi3);
				}
			}
		}
		// if (0==map.size()) {
		// map.put("moveVoice", null );
		// }
		ActionContext.getContext().getValueStack().set("info", map);
		// ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * 报表导出
	 * 
	 * @return
	 */
	public String downloadExcel() {
		return "download";
	}

	/**
	 * 导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownload() {
		String attribute = String.valueOf(ServletActionContext.getRequest()
				.getSession().getAttribute("floorName"));
		List<TestLogItem> queryCQTByFloorName = testLogItemService
				.queryCQTByFloorName(attribute);
		Map<String, Object> map = new HashMap<>();
		map.put("moveVoice", new EasyuiPageList());
		map.put("moveData", new EasyuiPageList());
		map.put("moveVideo", new EasyuiPageList());
		map.put("linkVoice", new EasyuiPageList());
		map.put("linkData", new EasyuiPageList());
		map.put("linkVideo", new EasyuiPageList());
		map.put("telecomVoice", new EasyuiPageList());
		map.put("telecomData", new EasyuiPageList());
		map.put("telecomVideo", new EasyuiPageList());
		if (null != queryCQTByFloorName) {
			StringBuilder moveVoiceLogIDBuilder = new StringBuilder();
			StringBuilder linkVoiceLogIDBuilder = new StringBuilder();
			StringBuilder telecomVoiceLogIDBuilder = new StringBuilder();
			StringBuilder moveDataLogIDBuilder = new StringBuilder();
			StringBuilder linkDataLogIDBuilder = new StringBuilder();
			StringBuilder telecomDataLogIDBuilder = new StringBuilder();
			StringBuilder moveVideoLogIDBuilder = new StringBuilder();
			StringBuilder linkVideoLogIDBuilder = new StringBuilder();
			StringBuilder telecomVideoLogIDBuilder = new StringBuilder();
			for (TestLogItem testLogItem : queryCQTByFloorName) {
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
			VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
			lteWholePreviewParam.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);

			// 汇总移动信息
			// 语音信息
			if (0 != moveVoiceLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(moveVoiceLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
				AbstractPageList totalKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);

				map.put("moveVoice", totalKpi);
			}
			// 数据信息
			if (0 != moveDataLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_LTE);
				lteWholePreviewParam.setTestLogItemIds(moveDataLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
				AbstractPageList indexKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				List indexKpirows = indexKpi.getRows();
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				indexKpirows.addAll(coverKpi.getRows());
				map.put("moveData", indexKpi);
			}

			// 视频信息
			if (0 != moveVideoLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
				lteWholePreviewParam.setTestLogItemIds(moveVideoLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
				AbstractPageList statisticeKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				List statisticeKpirows = statisticeKpi.getRows();
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
				AbstractPageList qualityKpi = cqtReportService
						.queryKpi(lteWholePreviewParam);
				statisticeKpirows.addAll(qualityKpi.getRows());
				map.put("moveVideo", statisticeKpi);
			}

			// 汇总联通信息
			// 语音信息
			if (0 != linkVoiceLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(linkVoiceLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
				AbstractPageList totalKpi2 = cqtReportService
						.queryKpi(lteWholePreviewParam);

				map.put("linkVoice", totalKpi2);
			}
			// 数据信息
			if (0 != linkDataLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_LTE);
				lteWholePreviewParam.setTestLogItemIds(linkDataLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
				AbstractPageList indexKpi2 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				List indexKpirows2 = indexKpi2.getRows();
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi2 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				indexKpirows2.addAll(coverKpi2.getRows());
				map.put("linkData", indexKpi2);
			}

			// 视频信息
			if (0 != linkVideoLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
				lteWholePreviewParam.setTestLogItemIds(linkVideoLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
				AbstractPageList statisticeKpi2 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				List statisticeKpirows2 = statisticeKpi2.getRows();
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
				AbstractPageList qualityKpi2 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				statisticeKpirows2.addAll(qualityKpi2.getRows());
				map.put("linkVideo", statisticeKpi2);
			}
			// 汇总电信信息
			// 语音信息
			if (0 != telecomVoiceLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
				// lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
				lteWholePreviewParam.setTestLogItemIds(telecomVoiceLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_TOTAL);
				AbstractPageList totalKpi3 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				map.put("telecomVoice", totalKpi3);
			}
			// 数据信息
			if (0 != telecomDataLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_LTE);
				lteWholePreviewParam.setTestLogItemIds(telecomDataLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX);
				AbstractPageList indexKpi3 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				List indexKpirows3 = indexKpi3.getRows();
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
				AbstractPageList coverKpi3 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				indexKpirows3.addAll(coverKpi3.getRows());
				map.put("telecomData", indexKpi3);
			}

			// 视频信息
			if (0 != telecomVideoLogIDBuilder.toString().length()) {
				lteWholePreviewParam
						.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
				lteWholePreviewParam.setTestLogItemIds(telecomVideoLogIDBuilder
						.toString());
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
				AbstractPageList statisticeKpi3 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				List statisticeKpirows3 = statisticeKpi3.getRows();
				lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
				AbstractPageList qualityKpi3 = cqtReportService
						.queryKpi(lteWholePreviewParam);
				statisticeKpirows3.addAll(qualityKpi3.getRows());
				map.put("telecomVideo", statisticeKpi3);
			}

		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/楼宇报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			String str = "楼宇报表.xls";
			ActionContext.getContext().put("fileName",
					new String(str.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
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

	/**
	 * @return the floorName
	 */
	public String getFloorName() {
		return floorName;
	}

	/**
	 * @param the
	 *            floorName to set
	 */

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param the
	 *            index to set
	 */

	public void setIndex(String index) {
		this.index = index;
	}

}
