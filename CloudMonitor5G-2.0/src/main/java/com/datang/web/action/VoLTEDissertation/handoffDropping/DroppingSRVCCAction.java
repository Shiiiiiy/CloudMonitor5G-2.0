package com.datang.web.action.VoLTEDissertation.handoffDropping;

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
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingSRVCC;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;

/**
 * volte切换失败---SRVCC失败
 * 
 * @author shenyanwei
 * @date 2016年4月20日下午5:23:56
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class DroppingSRVCCAction extends PageAction {

	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	/**
	 * 切换失败服务
	 */
	@Autowired(required = true)
	private VolteDroppingService droppingService;

	/**
	 * 失败事件ID
	 */
	private Long droppingId;

	/**
	 * 路段名称回填参数
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * 跳转到"SRVCC失败"界面
	 * 
	 * @return
	 */
	public String goToDroppingSRVCCList() {
		return ReturnType.LISTUI;
	}

	/**
	 * 回填切换失败的道路名称
	 * 
	 * @return
	 */
	public String roadNameCallBack() {
		if (null != rowIDs && rowIDs.length != 0) {
			for (int i = 0; i < rowIDs.length; i++) {
				droppingService.addQBRRoadName(rowRoadNames[i], rowIDs[i]);
			}
		}
		return null;
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
	 * 整体问题分析
	 * 
	 * @return
	 */
	public String doWholeAnalysis() {
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
				List<VolteDroppingSRVCC> volteDroppingSRVCCs = droppingService
						.getVolteDroppingSRVCCsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.push(droppingService.droppingSRVCCWholeAnalysis(
								volteDroppingSRVCCs, queryTestLogItems));

			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"问题分析"界面
	 * 
	 * @return
	 */
	public String goToProblem() {
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

				List<VolteDroppingSRVCC> volteDroppingSRVCCsByLogIds = droppingService
						.getVolteDroppingSRVCCsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("droppingSRVCCs", volteDroppingSRVCCsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "problem";
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
		if (null != droppingId) {
			VolteDroppingSRVCC volteDroppingSRVCC = droppingService
					.getVolteDroppingSRVCCById(droppingId);
			if (null != volteDroppingSRVCC) {
				pageList.putParam("droppingId", droppingId);
				// 查询本端信令
				pageList.putParam("recseqno", volteDroppingSRVCC
						.getTestLogItem().getRecSeqNo());
				pageList.putParam("startTime",
						volteDroppingSRVCC.getStartTime());
				pageList.putParam("endTime", volteDroppingSRVCC.getEndTime());
			}
		}

		return droppingService.droppingSignllingPageList(pageList);
	}

	/**
	 * 根据时间查询服务小区和邻区测量详情
	 * 
	 * @return
	 */
	public String haveTestLogMeasure() {
		Long begainDate = null;
		Long endDate = null;
		if (null != droppingId) {
			VolteDroppingSRVCC volteDroppingSRVCC = droppingService
					.getVolteDroppingSRVCCById(droppingId);
			if (null != volteDroppingSRVCC) {
				begainDate = volteDroppingSRVCC.getStartTime();
				endDate = volteDroppingSRVCC.getEndTime();
			}
		}
		ActionContext
				.getContext()
				.getValueStack()
				.push(droppingService.queryTestLogMeasuresByTime(begainDate,
						endDate));
		return ReturnType.JSON;
	}

	/**
	 * 
	 * @return the droppingId
	 */
	public Long getDroppingId() {
		return droppingId;
	}

	/**
	 * 
	 * @param droppingId
	 *            the droppingId to set
	 */
	public void setDroppingId(Long droppingId) {
		this.droppingId = droppingId;
	}

	/**
	 * 
	 * @return rowIDs
	 */
	public Long[] getRowIDs() {
		return rowIDs;
	}

	/**
	 * 
	 * @param rowIDs
	 *            the rowIDs to set
	 */
	public void setRowIDs(Long[] rowIDs) {
		this.rowIDs = rowIDs;
	}

	/**
	 * 
	 * @return the rowRoadNames
	 */
	public String[] getRowRoadNames() {
		return rowRoadNames;
	}

	/**
	 * 
	 * @param rowRoadNames
	 *            the rowRoadNames to set
	 */
	public void setRowRoadNames(String[] rowRoadNames) {
		this.rowRoadNames = rowRoadNames;
	}

}
