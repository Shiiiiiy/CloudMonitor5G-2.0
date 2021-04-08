package com.datang.web.action.VoLTEDissertation.continueWirelessBadRoad;

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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoad;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.VolteContinueWirelessBadRoadDisturbProblem;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.VolteContinueWirelessBadRoadNbDeficiency;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.otherProblem.VolteContinueWirelessBadRoadOtherProblem;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.weakCover.VolteContinueWirelessBadRoadWeakCover;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.VoLTEDissertation.continueWirelessBadRoad.IVolteContinueWirelessBadRoadService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.opensymphony.xwork2.ActionContext;

/**
 * Volte质量专题--连续无线差Action
 * 
 * @explain
 * @name VolteContinueWirelessBadRoadAction
 * @author shenyanwei
 * @date 2016年5月31日下午4:46:58
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class VolteContinueWirelessBadRoadAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(VolteContinueWirelessBadRoadAction.class);
	@Autowired
	private IVolteContinueWirelessBadRoadService volteContinueWirelessBadRoadService;
	@Autowired
	private TerminalGroupService terminalGroupService;
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
	 * 跳转到"连续无线差"界面
	 * 
	 * @return
	 */
	public String goTocontinueWirelessBadRoadList() {
		return ReturnType.LISTUI;
	}

	/**
	 * 回填连续无线差路段的道路名称
	 * 
	 * @return
	 */
	public String roadNameCallBack() {
		if (null != rowIDs && rowIDs.length != 0) {
			for (int i = 0; i < rowIDs.length; i++) {
				volteContinueWirelessBadRoadService.addCWBRoadName(
						rowRoadNames[i], rowIDs[i]);
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
		VolteContinueWirelessBadRoad volteContinueWirelessBadRoad = volteContinueWirelessBadRoadService
				.getVolteContinueWirelessBadRoad(roadId);
		// 获取渲染的小区图层名
		String drawCellFileName = null;
		if (null != volteContinueWirelessBadRoad) {
			TestLogItem testLogItem = volteContinueWirelessBadRoad
					.getTestLogItem();
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
				List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds = volteContinueWirelessBadRoadService
						.getVolteContinueWirelessBadRoadsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.push(volteContinueWirelessBadRoadService
								.doWholeAnalysis(
										volteContinueWirelessBadRoadsByLogIds,
										queryTestLogItems, ids));
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return ReturnType.JSON;
	}

	/**
	 * 连续无线差路段报表导出
	 * 
	 * @return
	 */
	public String downloadVolteContinueWirelessBadRoadExcel() {

		return "downloadVolteCWBR";
	}

	/**
	 * 连续无线差路段报表导出
	 * 
	 * @return
	 */
	public InputStream getDownloadVolteCWBR() {
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
				List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds = volteContinueWirelessBadRoadService
						.getVolteContinueWirelessBadRoadsByLogIds(ids);
				// 整体分析
				Map<String, EasyuiPageList> doWholeAnalysis = volteContinueWirelessBadRoadService
						.doWholeAnalysis(volteContinueWirelessBadRoadsByLogIds,
								queryTestLogItems, ids);
				map.put("wholes", doWholeAnalysis);
				// 弱覆盖
				List<VolteContinueWirelessBadRoadWeakCover> weakCoverRoadsByLogIds = volteContinueWirelessBadRoadService
						.getWeakCoverRoadsByLogIds(ids);
				map.put("weakCovers", weakCoverRoadsByLogIds);
				// 干扰
				List<VolteContinueWirelessBadRoadDisturbProblem> disturbRoadsByLogIds = volteContinueWirelessBadRoadService
						.getDisturbRoadsByLogIds(ids);
				map.put("disturbs", disturbRoadsByLogIds);
				// 邻区
				List<VolteContinueWirelessBadRoadNbDeficiency> nbDeficiencyRoadsByLogIds = volteContinueWirelessBadRoadService
						.getNbDeficiencyRoadsByLogIds(ids);
				map.put("nbCells", nbDeficiencyRoadsByLogIds);
				// 其他
				List<VolteContinueWirelessBadRoadOtherProblem> otherRoadsByLogIds = volteContinueWirelessBadRoadService
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
			hashMap.put("wholeRoadIndex3", new EasyuiPageList());
			map.put("wholes", hashMap);
			map.put("weakCovers", new ArrayList<>());
			map.put("disturbs", new ArrayList<>());
			map.put("nbCells", new ArrayList<>());
			map.put("others", new ArrayList<>());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/连续无线差路段报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("连续无线差路段报表.xls".getBytes(), "ISO8859-1"));
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
				List<VolteContinueWirelessBadRoadWeakCover> weakCoverRoadsByLogIds = volteContinueWirelessBadRoadService
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
		VolteContinueWirelessBadRoadWeakCover weakCoverRoadById = volteContinueWirelessBadRoadService
				.getWeakCoverRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(volteContinueWirelessBadRoadService
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
				List<VolteContinueWirelessBadRoadDisturbProblem> disturbRoadsByLogIds = volteContinueWirelessBadRoadService
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
		VolteContinueWirelessBadRoadDisturbProblem disturbRoadById = volteContinueWirelessBadRoadService
				.getDisturbRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(volteContinueWirelessBadRoadService
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
				List<VolteContinueWirelessBadRoadNbDeficiency> nbDeficiencyRoadsByLogIds = volteContinueWirelessBadRoadService
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
		VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency = volteContinueWirelessBadRoadService
				.getNbDeficiencyRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(volteContinueWirelessBadRoadService
						.doNbDeficiencyAnalysis(volteContinueWirelessBadRoadNbDeficiency));
		return ReturnType.JSON;
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
				List<VolteContinueWirelessBadRoadOtherProblem> otherRoadsByLogIds = volteContinueWirelessBadRoadService
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
		VolteContinueWirelessBadRoadOtherProblem volteContinueWirelessBadRoadOtherProblem = volteContinueWirelessBadRoadService
				.getOtherRoadById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(volteContinueWirelessBadRoadService
						.doOtherAnalysis(volteContinueWirelessBadRoadOtherProblem));
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
			VolteContinueWirelessBadRoad volteContinueWirelessBadRoad = volteContinueWirelessBadRoadService
					.getVolteContinueWirelessBadRoad(roadId);
			if (null != volteContinueWirelessBadRoad) {
				pageList.putParam("roadId", roadId);
				pageList.putParam("recseqno", volteContinueWirelessBadRoad
						.getTestLogItem().getRecSeqNo());
				pageList.putParam("startTime",
						volteContinueWirelessBadRoad.getStartTime());
				pageList.putParam("endTime",
						volteContinueWirelessBadRoad.getEndTime());
			}
		}
		return volteContinueWirelessBadRoadService
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
