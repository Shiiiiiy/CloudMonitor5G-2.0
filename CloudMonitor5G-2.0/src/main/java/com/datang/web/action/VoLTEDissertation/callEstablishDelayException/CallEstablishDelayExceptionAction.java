package com.datang.web.action.VoLTEDissertation.callEstablishDelayException;

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
import org.springframework.beans.factory.annotation.Value;
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
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.calledLocationUpdate.VolteCallEstablishDelayExceptionCalledLocationUpdate;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.coreNetwork.VolteCallEstablishDelayExceptionCoreNetwork;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.otherProblem.VolteCallEstablishDelayExceptionOtherProblem;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.overlapCover.VolteCallEstablishDelayExceptionOverlapCover;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCover;
import com.datang.domain.chart.OneDimensionalChartConfig;
import com.datang.domain.chart.TwoDimensionalChartConfig;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.VoLTEDissertation.callEstablishDelayException.IcallEstablishDelayExceptionService;
import com.datang.service.chart.IOneDimensionalChartService;
import com.datang.service.chart.ITwoDimensionalChartService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.opensymphony.xwork2.ActionContext;

/**
 * ??????????????????Action
 * 
 * @explain
 * @name CallEstablishDelayExceptionAction
 * @author shenyanwei
 * @date 2016???5???24?????????2:53:03
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CallEstablishDelayExceptionAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(CallEstablishDelayExceptionAction.class);
	@Value("${exceptionEvent.oppositeSignalling.timeDelay}")
	private String oppositeSignallingTimeDelay;
	@Autowired
	private IcallEstablishDelayExceptionService callEstablishDelayExceptionService;
	@Autowired
	private TerminalGroupService terminalGroupService;
	@Autowired
	private IOneDimensionalChartService oneDimensionalChartService;
	@Autowired
	private ITwoDimensionalChartService twoDimensionalChartService;
	/**
	 * ??????????????????
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	/**
	 * ?????????????????????
	 */
	private Boolean isOpposite;
	/**
	 * ????????????????????????id
	 */
	private Long CEDEId;

	/**
	 * ????????????????????????
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * ?????????"????????????????????????"??????
	 * 
	 * @return
	 */
	public String goToCallEstablishDelayExceptionList() {
		return ReturnType.LISTUI;

	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	public String roadNameCallBack() {
		if (null != rowIDs && rowIDs.length != 0) {
			for (int i = 0; i < rowIDs.length; i++) {
				callEstablishDelayExceptionService.addCEDERoadName(
						rowRoadNames[i], rowIDs[i]);
			}
		}
		return null;
	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	public String checkDrawCellFileName() {
		VolteCallEstablishDelayException volteCallEstablishDelayException = callEstablishDelayExceptionService
				.getVolteCallEstablishDelayException(CEDEId);
		// ??????????????????????????????
		String drawCellFileName = null;
		if (null != volteCallEstablishDelayException) {
			TestLogItem testLogItem = volteCallEstablishDelayException
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
	 * ?????????"??????"??????
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
				// ??????TestLogItem???id??????
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

				List<VolteCallEstablishDelayException> callEstablishDelayException = callEstablishDelayExceptionService
						.getVolteCallEstablishDelayExceptionsByLogIds(ids);

				ActionContext
						.getContext()
						.getValueStack()
						.set("callEstablishDelayExceptions",
								callEstablishDelayException);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);

		}

		return "whole";
	}

	/**
	 * ????????????
	 * 
	 * @return
	 */
	public String doCallEstablishDelayException() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				String[] logIds = testLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
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
				List<VolteCallEstablishDelayException> volteCallEstablishDelayExceptionsByLogIds = callEstablishDelayExceptionService
						.getVolteCallEstablishDelayExceptionsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.set("callEstablishDelayExceptions",
								volteCallEstablishDelayExceptionsByLogIds);
				ActionContext
						.getContext()
						.getValueStack()
						.push(callEstablishDelayExceptionService
								.doWholeAnalysis(ids,
										volteCallEstablishDelayExceptionsByLogIds));

			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}

		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @return
	 */
	public String downloadCallEstablishDelayExceptionExcel() {
		return "downloadCallEDE";
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @return
	 */
	public InputStream getDownloadCallEstablishDelayException() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				String[] logIds = testLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
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
				List<VolteCallEstablishDelayException> volteCallEstablishDelayExceptionsByLogIds = callEstablishDelayExceptionService
						.getVolteCallEstablishDelayExceptionsByLogIds(ids);
				// ????????????
				Map<String, EasyuiPageList> doWholeAnalysis = callEstablishDelayExceptionService
						.doWholeAnalysis(ids,
								volteCallEstablishDelayExceptionsByLogIds);
				map.put("wholes", doWholeAnalysis);
				// ?????????
				List<VolteCallEstablishDelayExceptionWeakCover> weakCoversByLogIds = callEstablishDelayExceptionService
						.getWeakCoversByLogIds(ids);
				map.put("weakCovers", weakCoversByLogIds);
				// ????????????
				List<VolteCallEstablishDelayExceptionOverlapCover> overlapCoversByLogIds = callEstablishDelayExceptionService
						.getVolteCallEstablishDelayExceptionOverlapCoversByLogIds(ids);

				map.put("overlapCovers", overlapCoversByLogIds);
				// ??????????????????
				List<VolteCallEstablishDelayExceptionCalledLocationUpdate> locationUpdatesByLogIds = callEstablishDelayExceptionService
						.getLocationUpdatesByLogIds(ids);

				map.put("locationUpdates", locationUpdatesByLogIds);
				// ???????????????
				List<VolteCallEstablishDelayExceptionCoreNetwork> coreNetworksByLogIds = callEstablishDelayExceptionService
						.getCoreNetworksByLogIds(ids);
				map.put("coreNets", coreNetworksByLogIds);
				// ??????
				List<VolteCallEstablishDelayExceptionOtherProblem> othersByLogIds = callEstablishDelayExceptionService
						.getOthersByLogIds(ids);
				map.put("others", othersByLogIds);

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
			map.put("overlapCovers", new ArrayList<>());
			map.put("locationUpdates", new ArrayList<>());
			map.put("coreNets", new ArrayList<>());
			map.put("others", new ArrayList<>());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/VoLTE??????????????????????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("VoLTE??????????????????????????????.xls".getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * ?????????"?????????"??????
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
				// ??????TestLogItem???id??????
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
				List<VolteCallEstablishDelayExceptionWeakCover> weakCoversByLogIds = callEstablishDelayExceptionService
						.getWeakCoversByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("weakCovers", weakCoversByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "weakCover";
	}

	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	public String doWeakCoverRoadAnalysis() {
		VolteCallEstablishDelayExceptionWeakCover weakCoverById = callEstablishDelayExceptionService
				.getWeakCoverById(CEDEId);

		ActionContext
				.getContext()
				.getValueStack()
				.push(callEstablishDelayExceptionService
						.doWeakCoverAnalysis(weakCoverById));
		return ReturnType.JSON;
	}

	/**
	 * ?????????"??????????????????"??????
	 * 
	 * @return
	 */
	public String goToLocationUpdate() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				String[] logIds = testLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
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
				List<VolteCallEstablishDelayExceptionCalledLocationUpdate> locationUpdatesByLogIds = callEstablishDelayExceptionService
						.getLocationUpdatesByLogIds(ids);

				ActionContext.getContext().getValueStack()
						.set("locationUpdates", locationUpdatesByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "locationUpdate";
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @return
	 */
	public String doLocationUpdate(PageList pageList) {
		VolteCallEstablishDelayExceptionCalledLocationUpdate locationUpdateById = callEstablishDelayExceptionService
				.getLocationUpdateById(CEDEId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(callEstablishDelayExceptionService
						.doLocationUpdateAnalysis(locationUpdateById));
		return ReturnType.JSON;
	}

	/**
	 * ?????????"????????????"??????
	 * 
	 * @return
	 */
	public String goToOverlapCover() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				String[] logIds = testLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
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
				List<VolteCallEstablishDelayExceptionOverlapCover> overlapCoversByLogIds = callEstablishDelayExceptionService
						.getVolteCallEstablishDelayExceptionOverlapCoversByLogIds(ids);

				ActionContext.getContext().getValueStack()
						.set("overlapCovers", overlapCoversByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "overlapCover";
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @return
	 */
	public String doOverlapCover() {
		VolteCallEstablishDelayExceptionOverlapCover overlapCoverById = callEstablishDelayExceptionService
				.getVolteCallEstablishDelayExceptionOverlapCoverById(CEDEId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(callEstablishDelayExceptionService
						.doOverlapCoverAnalysis(overlapCoverById));
		return ReturnType.JSON;
	}

	/**
	 * ?????????"?????????"??????
	 * 
	 * @return
	 */
	public String goToCoreNet() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		// ???????????????????????????????????????
		OneDimensionalChartConfig rtpPacketLostRatioHourChart = oneDimensionalChartService
				.findAxisCustomer(OneDimensionalChartType.CallEstDelay);

		// rsrp???sinr???????????????
		TwoDimensionalChartConfig rtpPacketLostRatioSinrChart = twoDimensionalChartService
				.findTwoDimensionalChartConfig(TwoDimensionalChartType.RsrpSinr);

		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				String[] logIds = testLogItemIds.trim().split(",");
				// ??????TestLogItem???id??????
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
				List<VolteCallEstablishDelayExceptionCoreNetwork> coreNetworksByLogIds = callEstablishDelayExceptionService
						.getCoreNetworksByLogIds(ids);

				Map<String, List> doCoreNetworkAnalysis = callEstablishDelayExceptionService
						.doCoreNetworkAnalysis(rtpPacketLostRatioHourChart, ids);
				ActionContext.getContext().getValueStack()
						.push(doCoreNetworkAnalysis);
				ActionContext.getContext().getValueStack()
						.set("coreNetworkRoads", coreNetworksByLogIds);
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
				.set("rtpPacketLostRatioSinrChartAxis",
						rtpPacketLostRatioSinrChart);

		return "coreNet";
	}

	/**
	 * ?????????"??????"??????
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
				// ??????TestLogItem???id??????
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
				List<VolteCallEstablishDelayExceptionOtherProblem> othersByLogIds = callEstablishDelayExceptionService
						.getOthersByLogIds(ids);

				ActionContext.getContext().getValueStack()
						.set("others", othersByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "other";
	}

	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	public String doOtherRoadAnalysis() {
		VolteCallEstablishDelayExceptionOtherProblem otherById = callEstablishDelayExceptionService
				.getOtherById(CEDEId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(callEstablishDelayExceptionService
						.doOtherAnalysis(otherById));
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????????????????????????????Action
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		if (null != CEDEId) {
			VolteCallEstablishDelayException volteCallEstablishDelayException = callEstablishDelayExceptionService
					.getVolteCallEstablishDelayException(CEDEId);
			if (null != volteCallEstablishDelayException) {
				pageList.putParam("CEDEId", CEDEId);
				if (null != isOpposite) {
					if (isOpposite) {// ??????????????????
						// ??????????????????id
						Long peerRecSeqNo = volteCallEstablishDelayException
								.getTestLogItem().getPeerRecSeqNo();
						if (null != peerRecSeqNo) {
							pageList.putParam("recseqno", peerRecSeqNo);
							pageList.putParam(
									"startTime",
									null == volteCallEstablishDelayException
											.getStartTime() ? null
											: volteCallEstablishDelayException
													.getStartTime()
													- Long.valueOf(oppositeSignallingTimeDelay));
							pageList.putParam(
									"endTime",
									null == volteCallEstablishDelayException
											.getEndTime() ? null
											: volteCallEstablishDelayException
													.getEndTime()
													+ Long.valueOf(oppositeSignallingTimeDelay));
						}
					} else {// ??????????????????
						pageList.putParam("recseqno",
								volteCallEstablishDelayException
										.getTestLogItem().getRecSeqNo());
						pageList.putParam("startTime",
								volteCallEstablishDelayException.getStartTime());
						pageList.putParam("endTime",
								volteCallEstablishDelayException.getEndTime());
					}
				}

			}

		}
		return callEstablishDelayExceptionService
				.doOtherSignllingPageList(pageList);
	}

	public Boolean getIsOpposite() {
		return isOpposite;
	}

	public void setIsOpposite(Boolean isOpposite) {
		this.isOpposite = isOpposite;
	}

	/**
	 * 
	 * @return
	 */
	public Long getCEDEId() {
		return CEDEId;
	}

	/**
	 * 
	 * @param cEDEId
	 */
	public void setCEDEId(Long cEDEId) {
		CEDEId = cEDEId;
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
