/**
 * 
 */
package com.datang.web.action.action5g.embbCover;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.datang.common.util.StringUtils;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;
import com.datang.domain.domain5g.embbCover.EmbbCoverCellInfo;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.VoLTEDissertation.qualityBadRoad.VoiceQualityBadRoadAction;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.opensymphony.xwork2.ActionContext;

/**
 * 5G EMBB覆盖专题Action
 * 
 * @author _YZP
 * 
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class EmbbCoverAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(VoiceQualityBadRoadAction.class);
	@Autowired
	private IEmbbCoverBadRoadService embbCoverBadRoadService;
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
	 * 覆盖小区id
	 */
	private Long cellInfoId;

	/**
	 * 路段名称回填参数
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * 跳转到"eMBB覆盖专题"界面
	 * 
	 * @return
	 */
	public String goToEmbbCoverBadRoadList() {
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到"总体分析"界面
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
						.push(embbCoverBadRoadService.doWholeAnalysis(ids));
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "whole";
	}

	/**
	 * 跳转到"总体设计韦恩图"界面
	 * 
	 * @return
	 */
	public String goToWholeVenn() {
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
						.push(embbCoverBadRoadService.doWholeAnalysis(ids));
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "wholevenn";
	}

	/**
	 * 跳转到"弱覆盖专项分析"界面
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
				List<EmbbCoverBadRoad> embbCoverBadRoadByLogIds = embbCoverBadRoadService
						.getEmbbCoverBadRoadByLogIds(ids, 0);
				Map<String, Object> analysisResult = embbCoverBadRoadService
						.doEmbbCoverTotalIndexAnalysis(embbCoverBadRoadByLogIds);
				ActionContext.getContext().getValueStack()
						.set("embbCoverTotalIndex", analysisResult);

				ActionContext.getContext().getValueStack()
						.set("embbCoverRoads", embbCoverBadRoadByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "weakCover";
	}

	/**
	 * 跳转到"过覆盖专项分析"界面
	 * 
	 * @return
	 */
	public String goToOverCover() {
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
				List<EmbbCoverBadRoad> embbCoverBadRoadByLogIds = embbCoverBadRoadService
						.getEmbbCoverBadRoadByLogIds(ids, 1);
				Map<String, Object> analysisResult = embbCoverBadRoadService
						.doEmbbCoverTotalIndexAnalysis(embbCoverBadRoadByLogIds);
				ActionContext.getContext().getValueStack()
						.set("embbCoverTotalIndex", analysisResult);

				ActionContext.getContext().getValueStack()
						.set("embbCoverRoads", embbCoverBadRoadByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "overCover";
	}

	/**
	 * 跳转到"重叠覆盖专项分析"界面
	 * 
	 * @return
	 */
	public String goToOverlappingCover() {
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
				List<EmbbCoverBadRoad> embbCoverBadRoadByLogIds = embbCoverBadRoadService
						.getEmbbCoverBadRoadByLogIds(ids, 2);
				Map<String, Object> analysisResult = embbCoverBadRoadService
						.doEmbbCoverTotalIndexAnalysis(embbCoverBadRoadByLogIds);
				ActionContext.getContext().getValueStack()
						.set("embbCoverTotalIndex", analysisResult);

				ActionContext.getContext().getValueStack()
						.set("embbCoverRoads", embbCoverBadRoadByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "overlappingCover";
	}

	/**
	 * 弱过重叠覆盖问题路段分析
	 * 
	 * @return
	 */
	public String doEmbbCoverRoadAnalysis() {
		EmbbCoverBadRoad embbCoverBadRoad = embbCoverBadRoadService
				.getEmbbCoverBadRoad(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(embbCoverBadRoadService
						.doEmbbCoverAnalysis(embbCoverBadRoad));
		return ReturnType.JSON;
	}

	/**
	 * 弱过重叠覆盖问题路段小区分析
	 * 
	 * @return
	 */
	public String doEmbbCoverCellAnalysis() {
		EmbbCoverCellInfo embbCoverCellInfo = embbCoverBadRoadService
				.getEmbbCoverCellInfo(cellInfoId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(embbCoverBadRoadService
						.doEmbbCoverCellBeamInfoAnalysis(embbCoverCellInfo));
		return ReturnType.JSON;
	}

	/**
	 * 获取渲染小区图层的名称
	 * 
	 * @return
	 */
	public String checkDrawCellFileName() {
		EmbbCoverBadRoad embbCoverBadRoad = embbCoverBadRoadService
				.getEmbbCoverBadRoad(roadId);
		// 获取渲染的小区图层名
		String drawCellFileName = null;
		if (null != embbCoverBadRoad) {
			TestLogItem testLogItem = embbCoverBadRoad.getTestLogItem();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.action.page.PageAction#doPageQuery(com.datang.common
	 * .action.page.PageList)
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return new EasyuiPageList();
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

	public Long getCellInfoId() {
		return cellInfoId;
	}

	public void setCellInfoId(Long cellInfoId) {
		this.cellInfoId = cellInfoId;
	}
}
