/**
 * 
 */
package com.datang.web.action.VoLTEDissertation.qualityBadRoad;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.datang.common.util.StringUtils;
import com.datang.constant.OneDimensionalChartType;
import com.datang.constant.TwoDimensionalChartType;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.coreNetwork.VolteQualityBadRoadCoreNetwork;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.VolteQualityBadRoadNbDeficiency;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.otherProblem.VolteQualityBadRoadOther;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.paramError.VolteQualityBadRoadParamError;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover;
import com.datang.domain.chart.OneDimensionalChartConfig;
import com.datang.domain.chart.TwoDimensionalChartConfig;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.VoLTEDissertation.qualityBadRoad.IVoiceQualityBadRoadService;
import com.datang.service.chart.IOneDimensionalChartService;
import com.datang.service.chart.ITwoDimensionalChartService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.opensymphony.xwork2.ActionContext;

/**
 * volte质量专题---volte语音质差
 * 
 * @author yinzhipeng
 * @date:2015年11月6日 上午9:52:49
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class VoiceQualityBadRoadAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(VoiceQualityBadRoadAction.class);
	@Autowired
	private IVoiceQualityBadRoadService voiceQualityBadRoadService;
	@Autowired
	private TerminalGroupService terminalGroupService;
	@Autowired
	private IOneDimensionalChartService oneDimensionalChartService;
	@Autowired
	private ITwoDimensionalChartService twoDimensionalChartService;
	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;

	/**
	 * 问题路段id
	 */
	private Long roadId;

	/**
	 * 路段名称回填参数
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * 跳转到"VOLTE语音质差"界面
	 * 
	 * @return
	 */
	public String goToqualityBadRoadList() {
		return ReturnType.LISTUI;
	}

	/**
	 * 回填质差路段的道路名称
	 * 
	 * @return
	 */
	public String roadNameCallBack() {
		if (null != rowIDs && rowIDs.length != 0) {
			for (int i = 0; i < rowIDs.length; i++) {
				voiceQualityBadRoadService.addQBRRoadName(rowRoadNames[i],
						rowIDs[i]);
			}
		}
		return null;
	}

	/**
	 * 获取渲染小区图层的名称
	 * 
	 * @return
	 */
	public String checkDrawCellFileName() {
		VolteQualityBadRoad volteQualityBadRoad = voiceQualityBadRoadService
				.getVolteQualityBadRoad(roadId);
		// 获取渲染的小区图层名
		String drawCellFileName = null;
		if (null != volteQualityBadRoad) {
			TestLogItem testLogItem = volteQualityBadRoad.getTestLogItem();
			if (null != testLogItem) {
				List<TerminalGroup> allCityGroup = terminalGroupService
						.getAllCityGroup();
				String terminalGroupName = testLogItem.getTerminalGroup();
				if (StringUtils.hasText(terminalGroupName)) {
					for (TerminalGroup terminalGroup : allCityGroup) {
						String name = terminalGroup.getName();
						if (!StringUtils.hasText(name)) {
							continue;
						}
						if (terminalGroupName.equals(name)) {
							CellInfo cellInfo = terminalGroup
									.getCellInfo(ProjectParamInfoType.CH_MO);
							if (null == cellInfo
									|| !StringUtils.hasText(cellInfo
											.getLteCellGisFileName())) {
								continue;
							}
							drawCellFileName = cellInfo.getLteCellGisFileName()
									.trim();
							continue;
						}
					}
				}
			}
		}
		ActionContext.getContext().getValueStack().push(drawCellFileName);
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"整体"界面
	 * 
	 * @return
	 */
	public String goToWhole() {
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
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "whole";
	}

	/**
	 * 整体路段分析
	 * 
	 * @return
	 */
	public String doWholeRoadAnalysis() {
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
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds = voiceQualityBadRoadService
						.getVolteQualityBadRoadsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.push(voiceQualityBadRoadService.doWholeAnalysis(
								volteQualityBadRoadsByLogIds,
								queryTestLogItems, ids));
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return ReturnType.JSON;
	}

	/**
	 * volte语音质差路段报表导出
	 * 
	 * @return
	 */
	public String downloadVolteQBRExcel() {
		return "downloadVolteQBR";
	}

	/**
	 * volte语音质差路段报表导出
	 * 
	 * @return
	 */
	public InputStream getDownloadVolteQBR() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		Map<String, Object> map = new HashMap<>();
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
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds = voiceQualityBadRoadService
						.getVolteQualityBadRoadsByLogIds(ids);
				// 整体分析
				Map<String, EasyuiPageList> doWholeAnalysis = voiceQualityBadRoadService
						.doWholeAnalysis(volteQualityBadRoadsByLogIds,
								queryTestLogItems, ids);
				map.put("wholes", doWholeAnalysis);
				// 弱覆盖
				List<VolteQualityBadRoadWeakCover> weakCoverRoadsByLogIds = voiceQualityBadRoadService
						.getWeakCoverRoadsByLogIds(ids);
				map.put("weakCovers", weakCoverRoadsByLogIds);
				// 干扰
				List<VolteQualityBadRoadDisturbProblem> disturbRoadsByLogIds = voiceQualityBadRoadService
						.getDisturbRoadsByLogIds(ids);
				map.put("disturbs", disturbRoadsByLogIds);
				// 邻区
				List<VolteQualityBadRoadNbDeficiency> nbDeficiencyRoadsByLogIds = voiceQualityBadRoadService
						.getNbDeficiencyRoadsByLogIds(ids);
				map.put("nbCells", nbDeficiencyRoadsByLogIds);
				// 参数
				List<VolteQualityBadRoadParamError> paramErrorRoadsByLogIds = voiceQualityBadRoadService
						.getParamErrorRoadsByLogIds(ids);
				map.put("paramErrors", paramErrorRoadsByLogIds);
				// 其他
				List<VolteQualityBadRoadOther> otherRoadsByLogIds = voiceQualityBadRoadService
						.getOtherRoadsByLogIds(ids);
				map.put("others", otherRoadsByLogIds);

			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		} else {

			HashMap<String, EasyuiPageList> hashMap = new HashMap<>();
			hashMap.put("wholeRoadIndex0", new EasyuiPageList());
			hashMap.put("wholeRoadIndex1", new EasyuiPageList());
			hashMap.put("wholeRoadIndex2", new EasyuiPageList());
			map.put("wholes", hashMap);
			map.put("weakCovers", new ArrayList<>());
			map.put("disturbs", new ArrayList<>());
			map.put("nbCells", new ArrayList<>());
			map.put("paramErrors", new ArrayList<>());
			map.put("others", new ArrayList<>());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/VoLTE语音质差路段报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("VoLTE语音质差路段报表.xls".getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 跳转到"弱覆盖"界面
	 * 
	 * @return
	 */
	public String goToWeakCover() {
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
				List<VolteQualityBadRoadWeakCover> weakCoverRoadsByLogIds = voiceQualityBadRoadService
						.getWeakCoverRoadsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("weakCoverRoads", weakCoverRoadsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "weakCover";
	}

	/**
	 * 弱覆盖问题路段分析
	 * 
	 * @return
	 */
	public String doWeakCoverRoadAnalysis() {
		VolteQualityBadRoadWeakCover weakCoverRoadById = voiceQualityBadRoadService
				.getWeakCoverRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(voiceQualityBadRoadService
						.doWeakCoverAnalysis(weakCoverRoadById));
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"干扰"界面
	 * 
	 * @return
	 */
	public String goToDisturb() {
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
				List<VolteQualityBadRoadDisturbProblem> disturbRoadsByLogIds = voiceQualityBadRoadService
						.getDisturbRoadsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("disturbRoads", disturbRoadsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "disturb";
	}

	/**
	 * 干扰问题质差路段分析
	 * 
	 * @return
	 */
	public String doDisturbRoadAnalysis() {
		VolteQualityBadRoadDisturbProblem disturbRoadById = voiceQualityBadRoadService
				.getDisturbRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(voiceQualityBadRoadService
						.doDisturbAnalysis(disturbRoadById));
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"邻区配置"界面
	 * 
	 * @return
	 */
	public String goToNbCell() {
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
				List<VolteQualityBadRoadNbDeficiency> nbDeficiencyRoadsByLogIds = voiceQualityBadRoadService
						.getNbDeficiencyRoadsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("nbDeficiencyRoads", nbDeficiencyRoadsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "nbCell";
	}

	/**
	 * 邻区配置问题质差路段分析
	 * 
	 * @return
	 */
	public String doNbCellRoadAnalysis() {
		VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency = voiceQualityBadRoadService
				.getNbDeficiencyRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(voiceQualityBadRoadService
						.doNbDeficiencyAnalysis(volteQualityBadRoadNbDeficiency));
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"参数配置"界面
	 * 
	 * @return
	 */
	public String goToParamError() {
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
				List<VolteQualityBadRoadParamError> paramErrorRoadsByLogIds = voiceQualityBadRoadService
						.getParamErrorRoadsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("paramErrorRoads", paramErrorRoadsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "paramError";
	}

	/**
	 * 参数错误问题路段分析
	 * 
	 * @return
	 */
	public String doParamErrorRoadAnalysis() {
		VolteQualityBadRoadParamError volteQualityBadRoadParamError = voiceQualityBadRoadService
				.getParamErrorRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(voiceQualityBadRoadService
						.doParamErrorAnalysis(volteQualityBadRoadParamError));
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"核心网"界面
	 * 
	 * @return
	 */
	public String goToCoreNet() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		// rtp丢包率时间分布一维图配置
		OneDimensionalChartConfig rtpPacketLostRatioHourChart = oneDimensionalChartService
				.findAxisCustomer(OneDimensionalChartType.RtpPacketLostRatioHour);
		// rtp丢包率与rsrp二维图配置
		TwoDimensionalChartConfig rtpPacketLostRatioRsrpChart = twoDimensionalChartService
				.findTwoDimensionalChartConfig(TwoDimensionalChartType.RtpPacketLostRatioRsrp);
		// rtp丢包率与sinr二维图配置
		TwoDimensionalChartConfig rtpPacketLostRatioSinrChart = twoDimensionalChartService
				.findTwoDimensionalChartConfig(TwoDimensionalChartType.RtpPacketLostRatioSinr);
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
				List<VolteQualityBadRoadCoreNetwork> coreNetworkRoadsByLogIds = voiceQualityBadRoadService
						.getCoreNetworkRoadsByLogIds(ids);
				Map<String, List> doCoreNetworkAnalysis = voiceQualityBadRoadService
						.doCoreNetworkAnalysis(rtpPacketLostRatioHourChart, ids);
				ActionContext.getContext().getValueStack()
						.push(doCoreNetworkAnalysis);
				ActionContext.getContext().getValueStack()
						.set("coreNetworkRoads", coreNetworkRoadsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		ActionContext
				.getContext()
				.getValueStack()
				.set("rtpPacketLostRatioHourChartAxis",
						rtpPacketLostRatioHourChart);
		ActionContext
				.getContext()
				.getValueStack()
				.set("rtpPacketLostRatioRsrpChartAxis",
						rtpPacketLostRatioRsrpChart);
		ActionContext
				.getContext()
				.getValueStack()
				.set("rtpPacketLostRatioSinrChartAxis",
						rtpPacketLostRatioSinrChart);
		return "coreNet";
	}

	/**
	 * 跳转到"其他"界面
	 * 
	 * @return
	 */
	public String goToOther() {
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
				List<VolteQualityBadRoadOther> otherRoadsByLogIds = voiceQualityBadRoadService
						.getOtherRoadsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("otherRoads", otherRoadsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "other";
	}

	/**
	 * 其他问题路段分析
	 * 
	 * @return
	 */
	public String doOtherRoadAnalysis() {
		VolteQualityBadRoadOther volteQualityBadRoadOther = voiceQualityBadRoadService
				.getOtherRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(voiceQualityBadRoadService
						.doOtherAnalysis(volteQualityBadRoadOther));
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
		if (null != roadId) {
			VolteQualityBadRoad volteQualityBadRoad = voiceQualityBadRoadService
					.getVolteQualityBadRoad(roadId);
			if (null != volteQualityBadRoad) {
				pageList.putParam("roadId", roadId);
				pageList.putParam("recseqno", volteQualityBadRoad
						.getTestLogItem().getRecSeqNo());
				pageList.putParam("startTime",
						volteQualityBadRoad.getStartTime());
				pageList.putParam("endTime", volteQualityBadRoad.getEndTime());
			}
		}
		return voiceQualityBadRoadService
				.doOtherRoadSignllingPageList(pageList);
	}

	/**
	 * @return the roadIdroadId
	 */
	public Long getRoadId() {
		return roadId;
	}

	/**
	 * @param roadId
	 *            the roadId to set
	 */
	public void setRoadId(Long roadId) {
		this.roadId = roadId;
	}

	/**
	 * @return the rowIDsrowIDs
	 */
	public Long[] getRowIDs() {
		return rowIDs;
	}

	/**
	 * @param rowIDs
	 *            the rowIDs to set
	 */
	public void setRowIDs(Long[] rowIDs) {
		this.rowIDs = rowIDs;
	}

	/**
	 * @return the rowRoadNamesrowRoadNames
	 */
	public String[] getRowRoadNames() {
		return rowRoadNames;
	}

	/**
	 * @param rowRoadNames
	 *            the rowRoadNames to set
	 */
	public void setRowRoadNames(String[] rowRoadNames) {
		this.rowRoadNames = rowRoadNames;
	}

}
