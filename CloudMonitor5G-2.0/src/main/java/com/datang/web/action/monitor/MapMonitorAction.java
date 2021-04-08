package com.datang.web.action.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.security.User;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.monitor.ATULoginService;
import com.datang.service.monitor.MapMonitorService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.monitor.GPSRequestBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @explain 地图Action
 * @name MapMonitorAction
 * @author shenyanwei
 * @date 2016年7月11日下午3:05:14
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class MapMonitorAction extends PageAction implements
		ModelDriven<TerminalGroup> {
	/**
	 * 地图服务
	 */
	@Autowired
	private MapMonitorService mapMonitorService;
	@Autowired
	private ATULoginService atuLoginService;
	@Autowired
	private TerminalService terminalService;

	/**
	 * 菜单管理服务
	 */
	@Autowired
	private IMenuManageService menuManageService;
	/**
	 * 区域管理服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * 用户管理服务
	 */
	@Autowired
	private IUserService userService;
	/**
	 * 表单对象
	 */
	private TerminalGroup group = new TerminalGroup();

	/**
	 * 开始时间
	 */
	private Date beginDate;
	/**
	 * 结束时间
	 */
	private Date endDate;
	/**
	 * 历史轨迹开始时间
	 */
	private String beginDates;
	/**
	 * 历史轨迹结束时间
	 */
	private String endDates;
	/**
	 * 终端boxID
	 */
	private String boxID;

	/**
	 * boxID数组 以，隔开
	 */
	private List<String> boxIDs;
	/**
	 * 通道号数组以，隔开
	 */
	private String galleryIDs;
	/**
	 * 
	 */
	private List<String> time;

	/**
	 * 跳转到 list界面
	 * 
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {

		return null;
	}

	/**
	 * 获取所有在线终端
	 * 
	 * @return
	 */
	public String allTerminal() {
		List<Terminal> terminals = terminalService.getAllTerminals();
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		for (Terminal terminal : terminals) {
			if (terminal.isOnline() == true) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", terminal.getBoxId());
				map.put("value", terminal.getBoxId());
				returnList.add(map);
			}
		}
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;

	}

	/**
	 * 根据时间获取所有有轨迹数据的终端
	 * 
	 * @return
	 */
	public String allHaveGpsTerminal() {
		Set<String> queryGpsByTime = atuLoginService.queryGpsByTime(beginDate,
				endDate);
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		for (String string : queryGpsByTime) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", string);
			map.put("value", string);
			returnList.add(map);
		}
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;

	}

	/**
	 * 获取所有在线终端个数
	 * 
	 * @return
	 */
	public String allTerminalNumber() {
		List<Terminal> terminals = terminalService.getAllTerminals();
		List<Terminal> returnList = new ArrayList<Terminal>();
		for (Terminal terminal : terminals) {
			if (terminal.isOnline() == true) {
				returnList.add(terminal);
			}
		}
		ActionContext.getContext().getValueStack().push(returnList.size());
		return ReturnType.JSON;

	}

	/**
	 * 获取多个终端的GPS信息
	 */

	/**
	 * 获取用户的权限范围内的区域菜单树
	 * 
	 * @return
	 */
	public String terminalGroupTree() {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userService.findByUsername(username);
		List<TerminalMenu> menus = (List<TerminalMenu>) menuManageService
				.getAllTerminalMenus();
		// 获得用户权限范围类的区域权限和根权限
		List<TerminalMenu> userTerminalMenus = new ArrayList<>();
		List<TerminalMenu> topTerminalMenus = new ArrayList<>();
		for (TerminalMenu menu : menus) {

			userTerminalMenus.add((TerminalMenu) menu);
			if (null == menu.getPid()) {
				topTerminalMenus.add((TerminalMenu) menu);
			}
		}

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[{\"id\":\"-1\",\"text\":\"区域\",\"children\":");
		Collections.sort(topTerminalMenus);
		buildTreeJson(topTerminalMenus, stringBuffer, userTerminalMenus);
		stringBuffer.append("}]");
		// 直接返回原始字符串
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(stringBuffer.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成菜单树JSON
	 * 
	 * @param menus
	 * @param stringBuffer
	 */
	private void buildTreeJson(List<TerminalMenu> menus, StringBuffer json,
			List<TerminalMenu> userTerminalMenus) {
		json.append("[");
		if (menus != null && menus.size() > 0) {
			for (int i = 0; i < menus.size(); i++) {
				TerminalMenu terminalMenu = menus.get(i);
				json.append("{");
				json.append("\"id\"");
				json.append(":");
				json.append(terminalMenu.getId());
				json.append(",");
				json.append("\"text\"");
				json.append(":");
				json.append("\"");
				json.append(terminalMenu.getName());
				json.append("\"");
				json.append(",");
				json.append("\"attributes\":");
				json.append("{\"refId\":");
				json.append(terminalMenu.getRefId());
				if (MenuType.Terminal.toString().equals(terminalMenu.getType())) {
					Terminal terminal = terminalService
							.getTerminal(terminalMenu.getRefId());
					if (null != terminal) {
						json.append(",\"online\":");
						json.append(terminal.isOnline());
						json.append(",\"boxID\":\"");
						json.append(terminal.getBoxId());
						json.append("\"");
					}
				}

				json.append(",\"type\":\"");
				json.append(terminalMenu.getType());
				json.append("\"}");
				List<TerminalMenu> children = new ArrayList<TerminalMenu>();
				children.addAll(findTerminalMenusFromListByPid(
						terminalMenu.getId(), userTerminalMenus));
				Collections.sort(children);
				if (children != null && children.size() > 0) {
					json.append(",");
					json.append("\"children\"");
					json.append(":");
					buildTreeJson(children, json, userTerminalMenus);
				}
				json.append("}");
				if (i != menus.size() - 1) {
					json.append(",");
				}
			}
		}
		json.append("]");
	}

	/**
	 * 在menus中找符合pid的menus
	 * 
	 * @param pid
	 * @param menus
	 * @return
	 */
	public List<TerminalMenu> findTerminalMenusFromListByPid(Long pid,
			List<TerminalMenu> menus) {
		List<TerminalMenu> list = new ArrayList<>();
		if (null != menus) {
			for (TerminalMenu terminalMenu : menus) {
				if (null != terminalMenu.getPid()
						&& pid.longValue() == terminalMenu.getPid().longValue()) {
					list.add(terminalMenu);
				} else {
					continue;
				}
			}
		}
		return list;
	}

	@Override
	public TerminalGroup getModel() {
		return group;
	}

	/**
	 * 根据boxID初次获取多个终端轨迹信息
	 * 
	 * @return
	 */
	public String queryTerminalGpsPoint() {
		String[] logIds = boxIDs.get(0).trim().split(",");
		// 存储TestLogItem的id集合
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < logIds.length; i++) {
			if (StringUtils.hasText(logIds[i])) {
				try {
					ids.add(logIds[i].trim());
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}
		ActionContext.getContext().getValueStack()
				.push(mapMonitorService.queryTerminalGpsPoint(ids));
		return ReturnType.JSON;
	}

	/**
	 * 根据时间和boxID获取多个终端实时地图监控实时刷新轨迹
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String queryGPSRefreshRequestParam() {
		String[] logIds = boxIDs.get(0).trim().split(",");
		// 存储TestLogItem的id集合
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < logIds.length; i++) {
			if (StringUtils.hasText(logIds[i])) {
				try {
					ids.add(logIds[i].trim());
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		Map<String, Date> map = new HashMap<String, Date>();
		if (time != null && time.size() != 0) {
			if (ids.size() > 1 && 1 == time.size()) {
				String timeString = time.get(0);
				String[] split = timeString.trim().split(",");
				try {
					map.put(split[0], dateFormat.parse(split[1]));
					map.put(split[2], dateFormat.parse(split[3]));
				} catch (Exception e) {

					System.out.println(e.getMessage());
				}
			}
			for (String string : time) {
				// System.out.println(string);
				if (string != null) {
					String[] log = string.trim().split(",");
					try {
						map.put(log[0], dateFormat.parse(log[1]));
					} catch (ParseException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		ActionContext.getContext().getValueStack()
				.push(mapMonitorService.queryGPSRefreshRequestParam(ids, map));
		return ReturnType.JSON;
	}

	/**
	 * 根据通道号、起止时间和boxID获取历史地图监控事件轨迹点信息
	 * 
	 * @return
	 */
	public String queryGPSEventRequestParam() {
		if (null != galleryIDs && 0 != galleryIDs.length()) {

			String[] logIds = boxIDs.get(0).trim().split(",");
			// 存储TestLogItem的id集合
			List<String> ids = new ArrayList<>();
			for (int i = 0; i < logIds.length; i++) {
				if (StringUtils.hasText(logIds[i])) {
					try {
						ids.add(logIds[i].trim());
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
			String[] logIds1 = galleryIDs.trim().split(",");
			// 存储TestLogItem的id集合
			List<Integer> nos = new ArrayList<>();
			for (int i = 0; i < logIds1.length; i++) {
				if (StringUtils.hasText(logIds1[i])) {
					try {
						nos.add(Integer.valueOf(logIds1[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
			Date begain = null;
			Date end = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			if (time != null) {
				String[] log = time.get(0).trim().split(",");

				try {
					begain = dateFormat.parse(log[0]);

					end = dateFormat.parse(log[1]);

				} catch (ParseException e) {
					System.out.println(e.getMessage());
				}
			}
			ActionContext
					.getContext()
					.getValueStack()
					.push(mapMonitorService.queryGPSEventRequestParam(begain,
							end, nos, ids));
		} else {
			ActionContext.getContext().getValueStack().push(null);
		}
		return ReturnType.JSON;
	}

	/**
	 * 根据通道号和boxID获取实时地图监控事件轨迹点信息
	 * 
	 * @return
	 */
	public String queryGPSRefreshEventRequestParam() {
		String[] logIds = boxIDs.get(0).trim().split(",");
		// 存储TestLogItem的id集合
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < logIds.length; i++) {
			if (StringUtils.hasText(logIds[i])) {
				try {
					ids.add(logIds[i].trim());
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}
		String[] logIds1 = galleryIDs.trim().split(",");
		// 存储TestLogItem的id集合
		List<Integer> nos = new ArrayList<>();
		for (int i = 0; i < logIds1.length; i++) {
			if (StringUtils.hasText(logIds1[i])) {
				try {
					nos.add(Integer.valueOf(logIds1[i].trim()));
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date begain = cal.getTime();
		Date end = null;
		ActionContext
				.getContext()
				.getValueStack()
				.push(mapMonitorService.queryGPSEventRequestParam(begain, end,
						nos, ids));
		return ReturnType.JSON;
	}

	/**
	 * 根据时间和boxID查询单个终端历史轨迹信息
	 * 
	 * @return
	 */
	public String queryHistoryTerminalGpsPoint() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		try {
			ActionContext
					.getContext()
					.getValueStack()
					.push(mapMonitorService.queryHistoryTerminalGpsPoint(
							dateFormat.parse(beginDates),
							dateFormat.parse(endDates), boxID));
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 根据时间和boxID查询历史信息
	 * 
	 * @return
	 */
	public String queryHistoryOtherInformation() {
		GPSRequestBean gpsRequestBean = new GPSRequestBean();
		gpsRequestBean.setBeginDate(beginDate);
		gpsRequestBean.setEndDate(endDate);
		gpsRequestBean.setBoxID(boxID);
		ActionContext.getContext().getValueStack()
				.push(mapMonitorService.queryOtherInformation(gpsRequestBean));
		return ReturnType.JSON;
	}

	/**
	 * @return the group
	 */
	public TerminalGroup getGroup() {
		return group;
	}

	/**
	 * @param the
	 *            group to set
	 */

	public void setGroup(TerminalGroup group) {
		this.group = group;
	}

	/**
	 * @return the boxIDs
	 */
	public List<String> getBoxIDs() {
		return boxIDs;
	}

	/**
	 * @param the
	 *            boxIDs to set
	 */

	public void setBoxIDs(List<String> boxIDs) {
		this.boxIDs = boxIDs;
	}

	/**
	 * @return the time
	 */
	public List<String> getTime() {
		return time;
	}

	/**
	 * @param the
	 *            time to set
	 */

	public void setTime(List<String> time) {
		this.time = time;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param the
	 *            beginDate to set
	 */

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param the
	 *            endDate to set
	 */

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the boxID
	 */
	public String getBoxID() {
		return boxID;
	}

	/**
	 * @param the
	 *            boxID to set
	 */

	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}

	/**
	 * @return the beginDates
	 */
	public String getBeginDates() {
		return beginDates;
	}

	/**
	 * @param the
	 *            beginDates to set
	 */

	public void setBeginDates(String beginDates) {
		this.beginDates = beginDates;
	}

	/**
	 * @return the endDates
	 */
	public String getEndDates() {
		return endDates;
	}

	/**
	 * @param the
	 *            endDates to set
	 */

	public void setEndDates(String endDates) {
		this.endDates = endDates;
	}

	/**
	 * @return the galleryIDs
	 */
	public String getGalleryIDs() {
		return galleryIDs;
	}

	/**
	 * @param the
	 *            galleryIDs to set
	 */

	public void setGalleryIDs(String galleryIDs) {
		this.galleryIDs = galleryIDs;
	}

}
