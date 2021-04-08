package com.datang.web.action.action5g.qualityBad;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem;
import com.datang.domain.domain5g.embbCover.EmbbCoverCellInfo;
import com.datang.domain.domain5g.qualityBad5g.InterfereRoadPojo;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.VoLTEDissertation.qualityBadRoad.IVoiceQualityBadRoadService;
import com.datang.service.service5g.qualityBad5g.QualityBadRoad5GService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class QualityBad5gAction extends PageAction {

	@Autowired
	private QualityBadRoad5GService qualityBadRoad5GService;
	
	@Autowired
	private IVoiceQualityBadRoadService voiceQualityBadRoadService;
	
	@Autowired
	private TerminalGroupService terminalGroupService;
	
	/**
	 * 路段名称回填参数
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * 问题路段id
	 */
	private Long roadId;
	
	/**
	 * 小区id
	 */
	private Long cellInfoId;


	/**
	 * 跳转到"eMBB覆盖专题"界面
	 * 
	 * @return
	 */
	public String goToQualityBadRoad5gJSP() {
		return ReturnType.LISTUI;
	}
	
	/**
	 * 跳转到"干扰"界面
	 * 
	 * @return
	 */
	public String goToDisturb() {
		Object attribute = ServletActionContext.getRequest().getSession().getAttribute("testLogItemIds");
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
				List<InterfereRoadPojo> disturbRoadsByLogIds = qualityBadRoad5GService.getDisturbRoadsByLogIds(ids);
				ActionContext.getContext().getValueStack().set("disturbRoads", disturbRoadsByLogIds);
				ActionContext.getContext().getValueStack().set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession().setAttribute("testLogItemIds", attribute);
		}
		return "disturb";
	}
	
	/**
	 * 干扰问题质差路段分析
	 * 
	 * @return
	 */
	public String doDisturbRoadAnalysis(){
		// VolteQualityBadRoadDisturbProblem disturbRoadById = voiceQualityBadRoadService.getDisturbRoadById(roadId);
		// System.out.println(disturbRoadById.getId());
		// ActionContext.getContext().getValueStack().push(voiceQualityBadRoadService.doDisturbAnalysis(disturbRoadById));
		ActionContext.getContext().getValueStack().push(qualityBadRoad5GService.doDisturbAnalysis(roadId));
		return ReturnType.JSON;
	}
	
	/**
	 * 回填质差路段的道路名称
	 * 
	 * @return
	 */
	public String roadNameCallBack() {
		if (null != rowIDs && rowIDs.length != 0) {
			for (int i = 0; i < rowIDs.length; i++) {
				voiceQualityBadRoadService.addQBRRoadName(rowRoadNames[i],rowIDs[i]);
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
		InterfereRoadPojo interfereRoadPojo = qualityBadRoad5GService.getVolteQualityBadRoad(roadId);
		// 获取渲染的小区图层名
		String drawCellFileName = null;
		if (null != interfereRoadPojo) {
			TestLogItem testLogItem = interfereRoadPojo.getTestLogItem();
			if (null != testLogItem) {
				List<TerminalGroup> allCityGroup = terminalGroupService.getAllCityGroup();
				String terminalGroupName = testLogItem.getTerminalGroup();
				if (StringUtils.hasText(terminalGroupName)) {
					for (TerminalGroup terminalGroup : allCityGroup) {
						String name = terminalGroup.getName();
						if (!StringUtils.hasText(name)) {
							continue;
						}
						if (terminalGroupName.equals(name)) {
							CellInfo cellInfo = terminalGroup.getCellInfo(ProjectParamInfoType.CH_MO);
							if (null == cellInfo || !StringUtils.hasText(cellInfo.getLteCellGisFileName())) {
								continue;
							}
							drawCellFileName = cellInfo.getLteCellGisFileName().trim();
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
	 * 获取波束详情
	 * 
	 * @return
	 */
	public String doEmbbCoverCellAnalysis() {
		ActionContext.getContext().getValueStack().push(qualityBadRoad5GService.getCellBeamInfoAnalysis(cellInfoId));
		return ReturnType.JSON;
	}
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return null;
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
	
	public Long getCellInfoId() {
		return cellInfoId;
	}

	public void setCellInfoId(Long cellInfoId) {
		this.cellInfoId = cellInfoId;
	}
}
