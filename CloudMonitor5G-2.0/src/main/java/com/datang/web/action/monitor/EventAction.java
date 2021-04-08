package com.datang.web.action.monitor;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.domain.security.User;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.service.monitor.EventService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.monitor.EventRequestBean;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 事件Action
 * 
 * @explain
 * @name EventAction
 * @author shenyanwei
 * @date 2016年7月11日下午3:05:23
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class EventAction extends PageAction implements
		ModelDriven<EventRequestBean> {

	/**
	 * 事件服务Service
	 */
	@Autowired
	private EventService eventService;

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
	 * 筛选参数
	 */
	private EventRequestBean eventRequestBean = new EventRequestBean();

	/**
	 * 跳转到list界面
	 * 
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		Set<String> boxIdsList = eventRequestBean.getBoxIdsList();
		List<Long> cityIdsList = eventRequestBean.getCityIdsList();
		Set<String> boxIdsSet = eventRequestBean.getBoxIdsSet();
		if (0 == boxIdsList.size() && 0 == cityIdsList.size()) {// 默认没选中区域也没选中设备,则获取权限范围内的数据
			String username = (String) SecurityUtils.getSubject()
					.getPrincipal();
			User user = userService.findByUsername(username);
			if (!user.isPowerUser()) {
				List<String> terminalBoxIDs = menuManageService
						.getTerminalBoxIDs();
				if (null == terminalBoxIDs || 0 == terminalBoxIDs.size()) {// 用户权限范围内的设备为null
					return new EasyuiPageList();
				} else {
					boxIdsSet.addAll(terminalBoxIDs);
				}
			}
		} else if (0 == boxIdsList.size() && 0 != cityIdsList.size()) {// 选中区域没选中设备,则获取区域范围内的数据
			for (Long cityId : cityIdsList) {
				List<Terminal> allTerminalByGroupId = terminalGroupService
						.getAllTerminalByGroupId(cityId);
				for (Terminal terminal : allTerminalByGroupId) {
					boxIdsSet.add(terminal.getBoxId());
				}
			}
		} else {// 选中设备或者选中区域和设备,则获取设备范围内的数据
			boxIdsSet.addAll(boxIdsList);
		}
		pageList.putParam("pageQueryBean", eventRequestBean);
		return eventService.pageList(pageList);
	}

	@Override
	public EventRequestBean getModel() {

		return eventRequestBean;
	}

	/**
	 * @return the eventRequestBean
	 */
	public EventRequestBean getEventRequestBean() {
		return eventRequestBean;
	}

	/**
	 * @param the
	 *            eventRequestBean to set
	 */

	public void setEventRequestBean(EventRequestBean eventRequestBean) {
		this.eventRequestBean = eventRequestBean;
	}

}
