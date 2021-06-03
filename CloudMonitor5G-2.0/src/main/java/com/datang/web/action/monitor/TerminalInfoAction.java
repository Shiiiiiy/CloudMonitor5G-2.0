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
import com.datang.service.monitor.TerminalInfoService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.monitor.TerminalInfoRequestBean;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 终端信息Action
 * 
 * @explain
 * @name TerminalInfoAction
 * @author shenyanwei
 * @date 2016年8月15日上午11:10:53
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class TerminalInfoAction extends PageAction implements
		ModelDriven<TerminalInfoRequestBean> {
	/**
	 * 硬件状态服务
	 */
	@Autowired
	private TerminalInfoService terminalInfoService;
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
	private TerminalInfoRequestBean terminalInfoRequestBean = new TerminalInfoRequestBean();

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
		Set<String> boxIdsList = terminalInfoRequestBean.getBoxIdsList();
		List<Long> cityIdsList = terminalInfoRequestBean.getCityIdsList();
		Set<String> boxIdsSet = terminalInfoRequestBean.getBoxIdsSet();
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

		pageList.putParam("pageQueryBean", terminalInfoRequestBean);
		return terminalInfoService.pageList(pageList);
	}

	@Override
	public TerminalInfoRequestBean getModel() {

		return terminalInfoRequestBean;
	}

}
