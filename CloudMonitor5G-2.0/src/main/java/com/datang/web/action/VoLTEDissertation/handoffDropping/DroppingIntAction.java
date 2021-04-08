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
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingInt;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;

/**
 * volte切换失败---系统内部切换失败
 * 
 * @author shenyanwei
 * @date 2016年4月20日下午5:49:25
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class DroppingIntAction extends PageAction {

	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	/**
	 * 切换失败服务
	 */
	@Autowired
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
	 * 跳转到"内部切换失败"界面
	 * 
	 * @return
	 */
	public String goToDroppingIntList() {
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
				List<VolteDroppingInt> volteDroppingInts = droppingService
						.getVolteDroppingIntsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.push(droppingService.DroppingIntWholeAnalysis(
								volteDroppingInts, queryTestLogItems));

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
				List<VolteDroppingInt> volteDroppingIntsByLogIds = droppingService
						.getVolteDroppingIntsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("droppingInts", volteDroppingIntsByLogIds);
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
			VolteDroppingInt volteDroppingIntById = droppingService
					.getVolteDroppingIntById(droppingId);
			if (null != volteDroppingIntById) {
				pageList.putParam("droppingId", droppingId);
				// 查询本端信令
				pageList.putParam("recseqno", volteDroppingIntById
						.getTestLogItem().getRecSeqNo());
				pageList.putParam("startTime",
						volteDroppingIntById.getStartTime());
				pageList.putParam("endTime", volteDroppingIntById.getEndTime());
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
			VolteDroppingInt volteDroppingIntById = droppingService
					.getVolteDroppingIntById(droppingId);
			if (null != volteDroppingIntById) {
				begainDate = volteDroppingIntById.getStartTime();
				endDate = volteDroppingIntById.getEndTime();
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
