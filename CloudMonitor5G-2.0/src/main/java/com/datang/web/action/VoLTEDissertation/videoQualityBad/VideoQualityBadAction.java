package com.datang.web.action.VoLTEDissertation.videoQualityBad;

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
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.videoQualityBad.VideoQualityBad;
import com.datang.domain.VoLTEDissertation.videoQualityBad.disturb.VideoQualtyBadDisturb;
import com.datang.domain.VoLTEDissertation.videoQualityBad.downDispatchSmall.VideoQualityBadDownDispatchSmall;
import com.datang.domain.VoLTEDissertation.videoQualityBad.neighbourPlot.VideoQualityBadNeighbourPlot;
import com.datang.domain.VoLTEDissertation.videoQualityBad.other.VideoQualityBadOther;
import com.datang.domain.VoLTEDissertation.videoQualityBad.overCover.VideoQualityBadOverCover;
import com.datang.domain.VoLTEDissertation.videoQualityBad.patternSwitch.VideoQualityBadPatternSwitch;
import com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong.VideoQualityBadPingPong;
import com.datang.domain.VoLTEDissertation.videoQualityBad.weakCover.VideoQualityBadWeakCover;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.VoLTEDissertation.videoQualityBad.IVideoQualityBadService;
import com.datang.service.report.IReportService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.opensymphony.xwork2.ActionContext;

/**
 * Volte????????????--????????????Action
 * 
 * @explain
 * @name VideoQualityBadAction
 * @author shenyanwei
 * @date 2017???5???8?????????1:30:55
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class VideoQualityBadAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(VideoQualityBadAction.class);
	@Autowired
	private IVideoQualityBadService videoQualityBadService;
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * ??????????????????
	 */
	@Autowired
	private IReportService reportService;
	/**
	 * ??????????????????
	 */
	@Autowired
	private ITestLogItemService testlogItemService;

	/**
	 * ????????????id
	 */
	private Long roadId;
	/**
	 * ????????????????????????
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * ?????????"???????????????"??????
	 * 
	 * @return
	 */
	public String goTovideoQualityBadList() {
		return ReturnType.LISTUI;
	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	public String roadNameCallBack() {
		if (null != rowIDs && rowIDs.length != 0) {
			for (int i = 0; i < rowIDs.length; i++) {
				videoQualityBadService.addQBRRoadName(rowRoadNames[i],
						rowIDs[i]);
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

		VideoQualityBad videoQualityBad = videoQualityBadService
				.getVideoQualityBad(roadId);
		// ??????????????????????????????
		String drawCellFileName = null;
		if (null != videoQualityBad) {
			TestLogItem testLogItem = videoQualityBad.getTestLogItem();
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
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "whole";
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	public String doWholeAnalysis() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}

				List<VideoQualityBad> videoQualityBadsByLogIds = videoQualityBadService
						.getVideoQualityBadsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.push(videoQualityBadService.doWholeAnalysis(
								videoQualityBadsByLogIds, listTestLogItem, ids));
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	public String downloadVideoQualityBadExcel() {

		return "downloadVideoQualityBad";
	}

	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	public InputStream getDownloadVideoQualityBad() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		Map<String, Object> map = new HashMap<>();
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;

				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}

				List<VideoQualityBad> videoQualityBadsByLogIds = videoQualityBadService
						.getVideoQualityBadsByLogIds(ids);

				map.put("others", videoQualityBadService
						.getDownloadInfo(videoQualityBadsByLogIds));
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		} else {
			map.put("others", new ArrayList<>());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/VoLTE????????????.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("VoLTE????????????????????????.xls".getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	// /**
	// * ????????????????????????
	// *
	// * @return
	// */
	// public InputStream getDownloadVideoQualityBad() {
	// Object attribute = ServletActionContext.getRequest().getSession()
	// .getAttribute("testLogItemIds");
	// Map<String, Object> map = new HashMap<>();
	// if (null != attribute) {
	// if (attribute instanceof String) {
	// String testLogItemIds = (String) attribute;
	//
	// List<TestLogItem> queryTestLogItems = testlogItemService
	// .queryTestLogItems(testLogItemIds);
	// ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
	// String idsString = null;
	// for (TestLogItem testLogItem : queryTestLogItems) {
	// if (testLogItem.getServiceType() != null) {
	// if (testLogItem.getServiceType() == 2) {
	// listTestLogItem.add(testLogItem);
	// if (idsString == null) {
	// idsString = String.valueOf(testLogItem
	// .getRecSeqNo());
	// } else {
	// idsString = idsString
	// + ","
	// + String.valueOf(testLogItem
	// .getRecSeqNo());
	// }
	// }
	// }
	// }
	// String[] logIds = idsString.trim().split(",");
	// // ??????TestLogItem???id??????
	// List<Long> ids = new ArrayList<>();
	// for (int i = 0; i < logIds.length; i++) {
	// if (StringUtils.hasText(logIds[i])) {
	// try {
	// ids.add(Long.parseLong(logIds[i].trim()));
	// } catch (NumberFormatException e) {
	// continue;
	// }
	// }
	// }
	// List<VideoQualityBad> videoQualityBadsByLogIds = videoQualityBadService
	// .getVideoQualityBadsByLogIds(ids);
	// // ????????????
	// Map<String, EasyuiPageList> doWholeAnalysis = videoQualityBadService
	// .doWholeAnalysis(videoQualityBadsByLogIds,
	// listTestLogItem, ids);
	// map.put("wholes", doWholeAnalysis);
	// // ????????????
	// List<VideoQualityBadPingPong> videoPingPongsByLogIds = new ArrayList<>();
	// // ??????
	// List<VideoQualityBadNeighbourPlot> videoNeighbourPlotsByLogIds = new
	// ArrayList<>();
	// // ?????????
	// List<VideoQualityBadWeakCover> videoWeakCoversByLogIds = new
	// ArrayList<>();
	// // ??????
	// List<VideoQualtyBadDisturb> videoDisturbsByLogIds = new ArrayList<>();
	// // ????????????
	// List<VideoQualityBadOverCover> videoOverCoversByLogIds = new
	// ArrayList<>();
	// // ????????????
	// List<VideoQualityBadPatternSwitch> videoPatternSwitchsByLogIds = new
	// ArrayList<>();
	// // ??????????????????
	// List<VideoQualityBadDownDispatchSmall> videoDownDispatchSmallsByLogIds =
	// new ArrayList<>();
	// // ????????????
	// List<VideoQualityBadOther> videoOthersByLogIds = new ArrayList<>();
	// if (null != videoQualityBadsByLogIds
	// && videoQualityBadsByLogIds.size() != 0) {
	// for (VideoQualityBad videoQualityBad : videoQualityBadsByLogIds) {
	// if (videoQualityBad.getTestLogItem().getCallType() != null) {
	// switch (videoQualityBad.getTestLogItem()
	// .getCallType()) {
	// case 0:
	// videoQualityBad.setCellTypeString("??????");
	// break;
	// case 1:
	// videoQualityBad.setCellTypeString("??????");
	// break;
	//
	// default:
	// break;
	// }
	// }
	// if (videoQualityBad.getTestLogItem().getCallType() != null) {
	// switch (videoQualityBad.getKeyParameterCause()) {
	// case 0:
	// videoQualityBad
	// .setKeyParameterCauseString("?????????");
	// break;
	// case 1:
	// videoQualityBad
	// .setKeyParameterCauseString("????????????");
	// break;
	// case 2:
	// videoQualityBad
	// .setKeyParameterCauseString("????????????");
	// break;
	// case 3:
	// videoQualityBad
	// .setKeyParameterCauseString("????????????");
	// break;
	// case -1:
	// videoQualityBad
	// .setKeyParameterCauseString("??????");
	// break;
	//
	// default:
	// break;
	// }
	// }
	// if (videoQualityBad instanceof VideoQualityBadPingPong) {
	// videoPingPongsByLogIds
	// .add((VideoQualityBadPingPong) videoQualityBad);
	// } else if (videoQualityBad instanceof VideoQualityBadNeighbourPlot) {
	// videoNeighbourPlotsByLogIds
	// .add((VideoQualityBadNeighbourPlot) videoQualityBad);
	// } else if (videoQualityBad instanceof VideoQualityBadWeakCover) {
	// videoWeakCoversByLogIds
	// .add((VideoQualityBadWeakCover) videoQualityBad);
	// } else if (videoQualityBad instanceof VideoQualtyBadDisturb) {
	// videoDisturbsByLogIds
	// .add((VideoQualtyBadDisturb) videoQualityBad);
	// } else if (videoQualityBad instanceof VideoQualityBadOverCover) {
	// videoOverCoversByLogIds
	// .add((VideoQualityBadOverCover) videoQualityBad);
	// } else if (videoQualityBad instanceof VideoQualityBadOther) {
	// videoOthersByLogIds
	// .add((VideoQualityBadOther) videoQualityBad);
	// } else if (videoQualityBad instanceof VideoQualityBadPatternSwitch) {
	// videoPatternSwitchsByLogIds
	// .add((VideoQualityBadPatternSwitch) videoQualityBad);
	// } else if (videoQualityBad instanceof VideoQualityBadDownDispatchSmall) {
	// videoDownDispatchSmallsByLogIds
	// .add((VideoQualityBadDownDispatchSmall) videoQualityBad);
	// }
	// }
	// }
	//
	// map.put("pingPongs", videoPingPongsByLogIds);
	// map.put("neighbourPlots", videoNeighbourPlotsByLogIds);
	// map.put("weakCovers", videoWeakCoversByLogIds);
	// map.put("disturbs", videoDisturbsByLogIds);
	// map.put("overCovers", videoOverCoversByLogIds);
	// map.put("patternSwitchs", videoPatternSwitchsByLogIds);
	// map.put("downDispatchSmalls", videoDownDispatchSmallsByLogIds);
	// map.put("others", videoOthersByLogIds);
	// }
	//
	// ServletActionContext.getRequest().getSession()
	// .setAttribute("testLogItemIds", attribute);
	// } else {
	//
	// HashMap<String, EasyuiPageList> hashMap = new HashMap<>();
	// hashMap.put("wholeRoadIndex0", new EasyuiPageList());
	// hashMap.put("wholeRoadIndex1", new EasyuiPageList());
	// hashMap.put("wholeRoadIndex2", new EasyuiPageList());
	// hashMap.put("wholeRoadIndex3", new EasyuiPageList());
	// hashMap.put("wholeRoadIndex4", new EasyuiPageList());
	// hashMap.put("wholeRoadIndex5", new EasyuiPageList());
	// map.put("wholes", hashMap);
	// map.put("pingPongs", new ArrayList<>());
	// map.put("neighbourPlots", new ArrayList<>());
	// map.put("weakCovers", new ArrayList<>());
	// map.put("disturbs", new ArrayList<>());
	// map.put("overCovers", new ArrayList<>());
	// map.put("patternSwitchs", new ArrayList<>());
	// map.put("downDispatchSmalls", new ArrayList<>());
	// map.put("others", new ArrayList<>());
	// }
	// ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
	// try {
	// Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
	// "templates/VoLTE????????????????????????.xls");
	// if (null != transformToExcel) {
	// transformToExcel.write(byteOutputStream);
	// }
	// ActionContext.getContext().put("fileName",
	// new String("VoLTE????????????????????????.xls".getBytes(), "ISO8859-1"));
	// } catch (IOException e) {
	// LOGGER.error(e.getMessage(), e);
	// }
	// return new ByteArrayInputStream(byteOutputStream.toByteArray());
	// }

	/**
	 * ?????????"????????????"??????
	 * 
	 * @return
	 */
	public String goToPingPong() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
				List<VideoQualityBadPingPong> videoPingPongsByLogIds = videoQualityBadService
						.getVideoPingPongsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("pingPongs", videoPingPongsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "pingPong";
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	public String doPingPongAnalysis() {
		VideoQualityBadPingPong videoPingPongById = videoQualityBadService
				.getVideoPingPongById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(videoQualityBadService
						.doVideoPingPongAnalysis(videoPingPongById));
		return ReturnType.JSON;
	}

	/**
	 * ?????????"??????"??????
	 * 
	 * @return
	 */
	public String goToNeighbourPlot() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
				List<VideoQualityBadNeighbourPlot> videoNeighbourPlotsByLogIds = videoQualityBadService
						.getVideoNeighbourPlotsByLogIds(ids);

				ActionContext.getContext().getValueStack()
						.set("neighbourPlots", videoNeighbourPlotsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "neighbourPlot";
	}

	/**
	 * ????????????
	 * 
	 * @return
	 */
	public String doNeighbourPlotAnalysis() {
		VideoQualityBadNeighbourPlot videoNeighbourPlotById = videoQualityBadService
				.getVideoNeighbourPlotById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(videoQualityBadService
						.doVideoNeighbourPlotAnalysis(videoNeighbourPlotById));
		return ReturnType.JSON;
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
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
				List<VideoQualityBadWeakCover> videoWeakCoversByLogIds = videoQualityBadService
						.getVideoWeakCoversByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("weakCovers", videoWeakCoversByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "weakCover";
	}

	/**
	 * ???????????????
	 * 
	 * @return
	 */
	public String doWeakCoverAnalysis() {
		VideoQualityBadWeakCover videoWeakCoverById = videoQualityBadService
				.getVideoWeakCoverById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(videoQualityBadService
						.doVideoWeakCoverAnalysis(videoWeakCoverById));
		return ReturnType.JSON;
	}

	/**
	 * ?????????"??????"??????
	 * 
	 * @return
	 */
	public String goToDisturb() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
				List<VideoQualtyBadDisturb> videoDisturbsByLogIds = videoQualityBadService
						.getVideoDisturbsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("disturbs", videoDisturbsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "disturb";
	}

	/**
	 * ????????????
	 * 
	 * @return
	 */
	public String doDisturbAnalysis() {
		VideoQualtyBadDisturb videoDisturbById = videoQualityBadService
				.getVideoDisturbById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(videoQualityBadService
						.doVideoDisturbAnalysis(videoDisturbById));
		return ReturnType.JSON;
	}

	/**
	 * ?????????"????????????"??????
	 * 
	 * @return
	 */
	public String goToOverCover() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
				List<VideoQualityBadOverCover> videoOverCoversByLogIds = videoQualityBadService
						.getVideoOverCoversByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("overCovers", videoOverCoversByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "overCover";
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	public String doOverCoverAnalysis() {
		VideoQualityBadOverCover videoOverCoverById = videoQualityBadService
				.getVideoOverCoverById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(videoQualityBadService
						.doVideoOverCoverAnalysis(videoOverCoverById));
		return ReturnType.JSON;
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
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
				List<VideoQualityBadOther> videoOthersByLogIds = videoQualityBadService
						.getVideoOthersByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("others", videoOthersByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "other";
	}

	/**
	 * ????????????
	 * 
	 * @return
	 */
	public String doOtherAnalysis() {
		VideoQualityBadOther videoOtherById = videoQualityBadService
				.getVideoOtherById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(videoQualityBadService
						.doVideoOtherAnalysis(videoOtherById));
		return ReturnType.JSON;
	}

	/**
	 * ?????????"????????????"??????
	 * 
	 * @return
	 */
	public String goToPatternSwitch() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}

				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
				List<VideoQualityBadPatternSwitch> videoPatternSwitchsByLogIds = videoQualityBadService
						.getVideoPatternSwitchsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("patternSwitchs", videoPatternSwitchsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "patternSwitch";
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	public String doPatternSwitchAnalysis() {
		VideoQualityBadPatternSwitch videoPatternSwitchById = videoQualityBadService
				.getVideoPatternSwitchById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(videoQualityBadService
						.doVideoPatternSwitchAnalysis(videoPatternSwitchById));
		return ReturnType.JSON;
	}

	/**
	 * ?????????"??????????????????"??????
	 * 
	 * @return
	 */
	public String goToDownDispatchSmall() {
		Object attribute = ServletActionContext.getRequest().getSession()
				.getAttribute("testLogItemIds");
		if (null != attribute) {
			if (attribute instanceof String) {
				String testLogItemIds = (String) attribute;
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("2,") != -1) {
								listTestLogItem.add(testLogItem);
								if (idsString == null) {
									idsString = String.valueOf(testLogItem
											.getRecSeqNo());
								} else {
									idsString = idsString
											+ ","
											+ String.valueOf(testLogItem
													.getRecSeqNo());
								}
							}
						}
					}
				}
				String[] logIds = null;
				if (idsString != null) {
					logIds = idsString.trim().split(",");
				}
				// ??????TestLogItem???id??????
				List<Long> ids = new ArrayList<>();
				if (logIds != null && logIds.length != 0) {
					for (int i = 0; i < logIds.length; i++) {
						if (StringUtils.hasText(logIds[i])) {
							try {
								ids.add(Long.parseLong(logIds[i].trim()));
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
				List<VideoQualityBadDownDispatchSmall> videoDownDispatchSmallsByLogIds = videoQualityBadService
						.getVideoDownDispatchSmallsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.set("downDispatchSmalls",
								videoDownDispatchSmallsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "downDispatchSmall";
	}

	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	public String doDownDispatchSmallAnalysis() {
		VideoQualityBadDownDispatchSmall videoDownDispatchSmallById = videoQualityBadService
				.getVideoDownDispatchSmallById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(videoQualityBadService
						.doVideoDownDispatchSmallAnalysis(videoDownDispatchSmallById));
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

	/**
	 * @return the rowIDs
	 */
	public Long[] getRowIDs() {
		return rowIDs;
	}

	/**
	 * @param the
	 *            rowIDs to set
	 */

	public void setRowIDs(Long[] rowIDs) {
		this.rowIDs = rowIDs;
	}

	/**
	 * @return the rowRoadNames
	 */
	public String[] getRowRoadNames() {
		return rowRoadNames;
	}

	/**
	 * @param the
	 *            rowRoadNames to set
	 */

	public void setRowRoadNames(String[] rowRoadNames) {
		this.rowRoadNames = rowRoadNames;
	}

}
