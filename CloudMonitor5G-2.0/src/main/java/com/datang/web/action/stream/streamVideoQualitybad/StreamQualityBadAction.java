package com.datang.web.action.stream.streamVideoQualitybad;

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
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;
import com.datang.domain.stream.streamVideoQualitybad.disturb.StreamQualtyBadDisturb;
import com.datang.domain.stream.streamVideoQualitybad.downDispatchSmall.StreamQualityBadDownDispatchSmall;
import com.datang.domain.stream.streamVideoQualitybad.neighbourPlot.StreamQualityBadNeighbourPlot;
import com.datang.domain.stream.streamVideoQualitybad.other.StreamQualityBadOther;
import com.datang.domain.stream.streamVideoQualitybad.overCover.StreamQualityBadOverCover;
import com.datang.domain.stream.streamVideoQualitybad.pingPong.StreamQualityBadPingPong;
import com.datang.domain.stream.streamVideoQualitybad.weakCover.StreamQualityBadWeakCover;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.report.IReportService;
import com.datang.service.stream.streamVideoQualitybad.IStreamQualityBadService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.opensymphony.xwork2.ActionContext;

/**
 * 流媒体专题--流媒体视频质差Action
 * 
 * @explain
 * @name StreamQualityBadAction
 * @author shenyanwei
 * @date 2017年10月24日上午10:04:45
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class StreamQualityBadAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(StreamQualityBadAction.class);
	@Autowired
	private IStreamQualityBadService streamQualityBadService;
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * 统计任务服务
	 */
	@Autowired
	private IReportService reportService;
	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;

	/**
	 * 质差问题id
	 */
	private Long roadId;
	/**
	 * 路段名称回填参数
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * 跳转到"流媒体视频质差"界面
	 * 
	 * @return
	 */
	public String goToStreamQualityBadList() {
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
				streamQualityBadService.addQBRRoadName(rowRoadNames[i],
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

		StreamQualityBad streamQualityBad = streamQualityBadService
				.getStreamQualityBad(roadId);
		// 获取渲染的小区图层名
		String drawCellFileName = null;
		if (null != streamQualityBad) {
			TestLogItem testLogItem = streamQualityBad.getTestLogItem();
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
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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

				List<StreamQualityBad> streamQualityBadsByLogIds = streamQualityBadService
						.getStreamQualityBadsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.push(streamQualityBadService
								.doWholeAnalysis(streamQualityBadsByLogIds,
										listTestLogItem, ids));
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return ReturnType.JSON;
	}

	/**
	 * 视频质差报表导出
	 * 
	 * @return
	 */
	public String downloadStreamQualityBadExcel() {

		return "downloadStreamQualityBad";
	}

	/**
	 * 视频质差报表导出
	 * 
	 * @return
	 */
	public InputStream getDownloadStreamQualityBad() {
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
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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
				List<StreamQualityBad> streamQualityBadsByLogIds = streamQualityBadService
						.getStreamQualityBadsByLogIds(ids);

				map.put("others", streamQualityBadService
						.getDownloadInfo(streamQualityBadsByLogIds));
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		} else {
			map.put("others", new ArrayList<>());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/流媒体问题详情.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String("流媒体视频质差问题详情.xls".getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	// /**
	// * 视频质差报表导出
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
	// // 存储TestLogItem的id集合
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
	// // 整体分析
	// Map<String, EasyuiPageList> doWholeAnalysis = videoQualityBadService
	// .doWholeAnalysis(videoQualityBadsByLogIds,
	// listTestLogItem, ids);
	// map.put("wholes", doWholeAnalysis);
	// // 乒乓切换
	// List<VideoQualityBadPingPong> videoPingPongsByLogIds = new ArrayList<>();
	// // 邻区
	// List<VideoQualityBadNeighbourPlot> videoNeighbourPlotsByLogIds = new
	// ArrayList<>();
	// // 弱覆盖
	// List<VideoQualityBadWeakCover> videoWeakCoversByLogIds = new
	// ArrayList<>();
	// // 干扰
	// List<VideoQualtyBadDisturb> videoDisturbsByLogIds = new ArrayList<>();
	// // 重叠覆盖
	// List<VideoQualityBadOverCover> videoOverCoversByLogIds = new
	// ArrayList<>();
	// // 模式转换
	// List<VideoQualityBadPatternSwitch> videoPatternSwitchsByLogIds = new
	// ArrayList<>();
	// // 下行调度数小
	// List<VideoQualityBadDownDispatchSmall> videoDownDispatchSmallsByLogIds =
	// new ArrayList<>();
	// // 其他原因
	// List<VideoQualityBadOther> videoOthersByLogIds = new ArrayList<>();
	// if (null != videoQualityBadsByLogIds
	// && videoQualityBadsByLogIds.size() != 0) {
	// for (VideoQualityBad videoQualityBad : videoQualityBadsByLogIds) {
	// if (videoQualityBad.getTestLogItem().getCallType() != null) {
	// switch (videoQualityBad.getTestLogItem()
	// .getCallType()) {
	// case 0:
	// videoQualityBad.setCellTypeString("主叫");
	// break;
	// case 1:
	// videoQualityBad.setCellTypeString("被叫");
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
	// .setKeyParameterCauseString("丢包率");
	// break;
	// case 1:
	// videoQualityBad
	// .setKeyParameterCauseString("视频码率");
	// break;
	// case 2:
	// videoQualityBad
	// .setKeyParameterCauseString("视频帧率");
	// break;
	// case 3:
	// videoQualityBad
	// .setKeyParameterCauseString("音频码率");
	// break;
	// case -1:
	// videoQualityBad
	// .setKeyParameterCauseString("其它");
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
	// "templates/VoLTE视频质差路段报表.xls");
	// if (null != transformToExcel) {
	// transformToExcel.write(byteOutputStream);
	// }
	// ActionContext.getContext().put("fileName",
	// new String("VoLTE视频质差路段报表.xls".getBytes(), "ISO8859-1"));
	// } catch (IOException e) {
	// LOGGER.error(e.getMessage(), e);
	// }
	// return new ByteArrayInputStream(byteOutputStream.toByteArray());
	// }

	/**
	 * 跳转到"乒乓切换"界面
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
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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
				List<StreamQualityBadPingPong> streamPingPongsByLogIds = streamQualityBadService
						.getStreamPingPongsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("pingPongs", streamPingPongsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "pingPong";
	}

	/**
	 * 乒乓切换分析
	 * 
	 * @return
	 */
	public String doPingPongAnalysis() {
		StreamQualityBadPingPong streamPingPongById = streamQualityBadService
				.getStreamPingPongById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(streamQualityBadService
						.doStreamPingPongAnalysis(streamPingPongById));
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"邻区"界面
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
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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
				List<StreamQualityBadNeighbourPlot> streamNeighbourPlotsByLogIds = streamQualityBadService
						.getStreamNeighbourPlotsByLogIds(ids);

				ActionContext.getContext().getValueStack()
						.set("neighbourPlots", streamNeighbourPlotsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "neighbourPlot";
	}

	/**
	 * 邻区分析
	 * 
	 * @return
	 */
	public String doNeighbourPlotAnalysis() {
		StreamQualityBadNeighbourPlot streamNeighbourPlotById = streamQualityBadService
				.getStreamNeighbourPlotById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(streamQualityBadService
						.doStreamNeighbourPlotAnalysis(streamNeighbourPlotById));
		return ReturnType.JSON;
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
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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
				List<StreamQualityBadWeakCover> streamWeakCoversByLogIds = streamQualityBadService
						.getStreamWeakCoversByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("weakCovers", streamWeakCoversByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "weakCover";
	}

	/**
	 * 弱覆盖分析
	 * 
	 * @return
	 */
	public String doWeakCoverAnalysis() {
		StreamQualityBadWeakCover streamWeakCoverById = streamQualityBadService
				.getStreamWeakCoverById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(streamQualityBadService
						.doStreamWeakCoverAnalysis(streamWeakCoverById));
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
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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
				List<StreamQualtyBadDisturb> streamDisturbsByLogIds = streamQualityBadService
						.getStreamDisturbsByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("disturbs", streamDisturbsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "disturb";
	}

	/**
	 * 干扰分析
	 * 
	 * @return
	 */
	public String doDisturbAnalysis() {
		StreamQualtyBadDisturb streamDisturbById = streamQualityBadService
				.getStreamDisturbById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(streamQualityBadService
						.doStreamDisturbAnalysis(streamDisturbById));
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"重叠覆盖"界面
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
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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
				List<StreamQualityBadOverCover> streamOverCoversByLogIds = streamQualityBadService
						.getStreamOverCoversByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("overCovers", streamOverCoversByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "overCover";
	}

	/**
	 * 重叠覆盖分析
	 * 
	 * @return
	 */
	public String doOverCoverAnalysis() {
		StreamQualityBadOverCover streamOverCoverById = streamQualityBadService
				.getStreamOverCoverById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(streamQualityBadService
						.doStreamOverCoverAnalysis(streamOverCoverById));
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
				List<TestLogItem> queryTestLogItems = testlogItemService
						.queryTestLogItems(testLogItemIds);
				ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
				String idsString = null;
				if (queryTestLogItems != null && queryTestLogItems.size() != 0) {
					for (TestLogItem testLogItem : queryTestLogItems) {
						if (testLogItem.getServiceType() != null) {
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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
				List<StreamQualityBadOther> streamOthersByLogIds = streamQualityBadService
						.getStreamOthersByLogIds(ids);
				ActionContext.getContext().getValueStack()
						.set("others", streamOthersByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "other";
	}

	/**
	 * 其他分析
	 * 
	 * @return
	 */
	public String doOtherAnalysis() {
		StreamQualityBadOther streamOtherById = streamQualityBadService
				.getStreamOtherById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(streamQualityBadService
						.doStreamOtherAnalysis(streamOtherById));
		return ReturnType.JSON;
	}

	/**
	 * 跳转到"下行调度数小"界面
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
							if (testLogItem.getServiceType().indexOf("6,") != -1) {
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
				// 存储TestLogItem的id集合
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
				List<StreamQualityBadDownDispatchSmall> streamDownDispatchSmallsByLogIds = streamQualityBadService
						.getStreamDownDispatchSmallsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.set("downDispatchSmalls",
								streamDownDispatchSmallsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "downDispatchSmall";
	}

	/**
	 * 下行调度数小分析
	 * 
	 * @return
	 */
	public String doDownDispatchSmallAnalysis() {
		StreamQualityBadDownDispatchSmall streamDownDispatchSmallById = streamQualityBadService
				.getStreamDownDispatchSmallById(roadId);
		ActionContext
				.getContext()
				.getValueStack()
				.push(streamQualityBadService
						.doStreamDownDispatchSmallAnalysis(streamDownDispatchSmallById));
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
