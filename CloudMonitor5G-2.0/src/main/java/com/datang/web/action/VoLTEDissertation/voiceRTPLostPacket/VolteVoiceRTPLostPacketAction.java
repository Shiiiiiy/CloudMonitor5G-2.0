package com.datang.web.action.VoLTEDissertation.voiceRTPLostPacket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
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
import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.VolteVoiceRTPLostPacket;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.voiceRTPLostPacket.IVolteVoiceRTPLostPacketService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.util.DateUtil;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;

/**
 * VOLTE语音FTP连续丢包Action
 * 
 * @explain
 * @name VolteVoiceRTPLostPacketAction
 * @author shenyanwei
 * @date 2017年2月15日上午10:46:43
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class VolteVoiceRTPLostPacketAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(VolteVoiceRTPLostPacketAction.class);
	/**
	 * 丢包事件服务
	 */
	@Autowired
	private IVolteVoiceRTPLostPacketService volteVoiceRTPLostPacketService;
	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	/**
	 * 丢包事件ID
	 */
	private Long packetId;
	/**
	 * 报表导出index值
	 */
	private Integer indexNo;
	/**
	 * 路段名称回填参数
	 */
	private Long rowIDs[];
	private String rowRoadNames[];

	/**
	 * 跳转到"VOLTE语音FTP连续丢包"界面
	 * 
	 * @return
	 */
	public String goToLostPacketList() {
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
				volteVoiceRTPLostPacketService.addQBRRoadName(rowRoadNames[i],
						rowIDs[i]);
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
				List<VolteVoiceRTPLostPacket> queryVolteVoiceRTPLostPacketsByLogIds = volteVoiceRTPLostPacketService
						.queryVolteVoiceRTPLostPacketsByLogIds(ids);

				ActionContext
						.getContext()
						.getValueStack()
						.push(volteVoiceRTPLostPacketService.doWholeAnalysis(
								queryVolteVoiceRTPLostPacketsByLogIds,
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
				List<VolteVoiceRTPLostPacket> queryVolteVoiceRTPLostPacketsByLogIds = volteVoiceRTPLostPacketService
						.queryVolteVoiceRTPLostPacketsByLogIds(ids);
				ActionContext
						.getContext()
						.getValueStack()
						.set("lostPackets",
								queryVolteVoiceRTPLostPacketsByLogIds);
				ActionContext.getContext().getValueStack()
						.set("testLogItemIds", testLogItemIds);
			}
			ServletActionContext.getRequest().getSession()
					.setAttribute("testLogItemIds", attribute);
		}
		return "problem";
	}

	/**
	 * 问题详情分析
	 * 
	 * @return
	 */
	public String doProblemAnalysis() {
		if (packetId != null) {
			VolteVoiceRTPLostPacket queryLostPacketById = volteVoiceRTPLostPacketService
					.queryLostPacketById(packetId);
			ActionContext
					.getContext()
					.getValueStack()
					.push(volteVoiceRTPLostPacketService
							.doProblemAnalysis(queryLostPacketById));
			ServletActionContext.getRequest().getSession()
					.setAttribute("lostPacketIds", packetId);
		}
		return ReturnType.JSON;
	}

	/**
	 * 各端口RTP包报表导出
	 * 
	 * @return
	 */
	public String downloadLostPacketRTPExcel() {
		return "downloadLostPacketRTP";
	}

	/**
	 * 各端口RTP包报表导出
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadLostPacketRTP() {
		Long attribute = (Long) ServletActionContext.getRequest().getSession()
				.getAttribute("lostPacketIds");
		Map<String, Object> map = new HashMap<>();
		String sheetName = null;
		if (null != attribute) {
			VolteVoiceRTPLostPacket queryLostPacketById = volteVoiceRTPLostPacketService
					.queryLostPacketById(attribute);
			if (queryLostPacketById != null && indexNo != null) {
				EasyuiPageList publicRTPSend = volteVoiceRTPLostPacketService
						.getPublicRTPSend(queryLostPacketById, indexNo);
				map.put("lostPacket", publicRTPSend);

			}
		} else {
			map.put("lostPacket", new EasyuiPageList());
		}
		switch (indexNo) {
		case 0:
			sheetName = "发送端手机 RTP上行包";
			break;
		case 1:
			sheetName = "发送端S1接口RTP上行包";
			break;
		case 2:
			sheetName = "发送端SGi接口RTP上行包";
			break;
		case 3:
			sheetName = "接收端SGi接口RTP下行包";
			break;
		case 4:
			sheetName = "接收端S1接口RTP下行包";
			break;
		case 5:
			sheetName = "接收端UE接口RTP下行包";
			break;
		default:
			break;
		}
		sheetName = sheetName + "_" + DateUtil.getDateTimeStr(new Date())
				+ ".xls";
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
					"templates/RTP连续丢包报表.xls");
			if (null != transformToExcel) {
				transformToExcel.write(byteOutputStream);
			}
			ActionContext.getContext().put("fileName",
					new String(sheetName.getBytes(), "ISO8859-1"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the packetId
	 */
	public Long getPacketId() {
		return packetId;
	}

	/**
	 * @param the
	 *            packetId to set
	 */

	public void setPacketId(Long packetId) {
		this.packetId = packetId;
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

	/**
	 * @return the indexNo
	 */
	public Integer getIndexNo() {
		return indexNo;
	}

	/**
	 * @param the
	 *            indexNo to set
	 */

	public void setIndexNo(Integer indexNo) {
		this.indexNo = indexNo;
	}

}
