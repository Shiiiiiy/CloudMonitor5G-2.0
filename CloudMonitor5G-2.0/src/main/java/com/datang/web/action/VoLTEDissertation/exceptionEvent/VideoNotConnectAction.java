/**
 * 
 */
package com.datang.web.action.VoLTEDissertation.exceptionEvent;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VideoExceptionEventNotConnect;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEvent;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.exceptionEvent.IVolteExceptionEventService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;

/**
 * volte异常事件---volte视频未接通
 * 
 * @explain
 * @name VideoNotConnectAction
 * @author shenyanwei
 * @date 2017年5月11日上午10:07:13
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class VideoNotConnectAction extends PageAction {
	@Value("${exceptionEvent.oppositeSignalling.timeDelay}")
	private String oppositeSignallingTimeDelay;
	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	/**
	 * 异常事件服务
	 */
	@Autowired
	private IVolteExceptionEventService volteExceptionEventService;

	/**
	 * 异常事件ID
	 */
	private Long exceptionEventId;
	/**
	 * 是否为对端信令
	 */
	private Boolean isOpposite;
	/**
	 * 路段名称回填参数
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * 跳转到"VOLTE语音未接通"界面
	 * 
	 * @return
	 */
	public String goToNotConnectList() {
		return ReturnType.LISTUI;
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
				List<VideoExceptionEventNotConnect> videoExceptionEventNotConnectByLogIds = volteExceptionEventService
						.getVideoExceptionEventNotConnectByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.push(volteExceptionEventService
								.doVideoNotConnectWholeAnalysis(
										videoExceptionEventNotConnectByLogIds,
										queryTestLogItems));
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
				List<VideoExceptionEventNotConnect> videoExceptionEventNotConnectByLogIds = volteExceptionEventService
						.getVideoExceptionEventNotConnectByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.set("VideoNotConnects",
								videoExceptionEventNotConnectByLogIds);
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
		if (null != exceptionEventId) {
			VolteExceptionEvent volteExceptionEvent = volteExceptionEventService
					.getVolteExceptionEvent(exceptionEventId);
			if (null != volteExceptionEvent) {
				pageList.putParam("exceptionEventId", exceptionEventId);
				if (null != isOpposite) {
					if (isOpposite) {// 查询对端信令
						// 获取对端日志id
						Long peerRecSeqNo = volteExceptionEvent
								.getTestLogItem().getPeerRecSeqNo();
						if (null != peerRecSeqNo) {
							pageList.putParam("recseqno", peerRecSeqNo);
							pageList.putParam(
									"startTime",
									null == volteExceptionEvent.getStartTime() ? null
											: volteExceptionEvent
													.getStartTime()
													- Long.valueOf(oppositeSignallingTimeDelay));
							pageList.putParam(
									"endTime",
									null == volteExceptionEvent.getEndTime() ? null
											: volteExceptionEvent.getEndTime()
													+ Long.valueOf(oppositeSignallingTimeDelay));
						}
					} else {// 查询本端信令
						pageList.putParam("recseqno", volteExceptionEvent
								.getTestLogItem().getRecSeqNo());
						pageList.putParam("startTime",
								volteExceptionEvent.getStartTime());
						pageList.putParam("endTime",
								volteExceptionEvent.getEndTime());
					}
				}

			}
		}
		return volteExceptionEventService
				.doExceptionEventSignllingPageList(pageList);
	}

	/**
	 * @return the exceptionEventIdexceptionEventId
	 */
	public Long getExceptionEventId() {
		return exceptionEventId;
	}

	/**
	 * @param exceptionEventId
	 *            the exceptionEventId to set
	 */
	public void setExceptionEventId(Long exceptionEventId) {
		this.exceptionEventId = exceptionEventId;
	}

	/**
	 * @return the isOppositeisOpposite
	 */
	public Boolean getIsOpposite() {
		return isOpposite;
	}

	/**
	 * @param isOpposite
	 *            the isOpposite to set
	 */
	public void setIsOpposite(Boolean isOpposite) {
		this.isOpposite = isOpposite;
	}

	/**
	 * @return the oppositeSignallingTimeDelayoppositeSignallingTimeDelay
	 */
	public String getOppositeSignallingTimeDelay() {
		return oppositeSignallingTimeDelay;
	}

	/**
	 * @param oppositeSignallingTimeDelay
	 *            the oppositeSignallingTimeDelay to set
	 */
	public void setOppositeSignallingTimeDelay(
			String oppositeSignallingTimeDelay) {
		this.oppositeSignallingTimeDelay = oppositeSignallingTimeDelay;
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
