package com.datang.web.action.testManage.terminal;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.security.User;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.exception.ApplicationException;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.testManage.terminal.TermianlTreeResponseBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 终端组即区域组(一二级域)Action
 * 
 * @author yinzhipeng
 * @date:2015年10月12日 上午10:59:25
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class TerminalGroupAction extends PageAction implements
		ModelDriven<TerminalGroup> {
	/**
	 * 日志
	 */
	private static Logger LOGGER = LoggerFactory
			.getLogger(TerminalAction.class);

	/**
	 * 区域菜单服务(查询)
	 */
	@Autowired
	private IMenuManageService menuManageService;
	/**
	 * 区域组服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	
	/**
	 * 设备服务
	 */
	@Autowired
	private TerminalService terminalService;
	
	/**
	 * 用户服务
	 */
	@Autowired
	private IUserService userService;
	/**
	 * 表单对象
	 */
	private TerminalGroup group = new TerminalGroup();
	/**
	 * 省级ID,用于添加市级区域
	 */
	private Long provinceId;

	/**
	 * 市级域ID按,分隔
	 */
	private String cityIds;

	/**
	 * 跳转到设备组(区域组)list页面
	 * 
	 * @return
	 */
	public String terminalGroupListUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * 获取用户的权限范围内的区域菜单树
	 * 
	 * @return
	 */
	public String terminalGroupTree() {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userService.findByUsername(username);
		List<Menu> menus = menuManageService.getMenus(user);
		// 获得用户权限范围类的区域权限和根权限
		List<TerminalMenu> userTerminalMenus = new ArrayList<>();
		List<TerminalMenu> topTerminalMenus = new ArrayList<>();
		for (Menu menu : menus) {
			if (menu instanceof TerminalMenu) {
				userTerminalMenus.add((TerminalMenu) menu);
				if (null == menu.getPid()) {
					topTerminalMenus.add((TerminalMenu) menu);
				}
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
	 * 获取用户的权限范围内的终端菜单树
	 * 
	 * @return
	 */
	public String testPlanTerminalTree() {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userService.findByUsername(username);
		List<Menu> menus = menuManageService.getMenus(user);
		// 获得用户权限范围类的区域权限和根权限
		List<TerminalMenu> userTerminalMenus = new ArrayList<>();
		List<TerminalMenu> topTerminalMenus = new ArrayList<>();
		for (Menu menu : menus) {
			if (menu instanceof TerminalMenu) {
				userTerminalMenus.add((TerminalMenu) menu);
				if (null == menu.getPid()) {
					topTerminalMenus.add((TerminalMenu) menu);
				}
			}
		}
		// 获取用户权限范围内的终端权限
		List<TerminalMenu> terminals = menuManageService.getTerminals();
		userTerminalMenus.addAll(terminals);

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[{\"id\":\"-1\",\"text\":\"终端\",\"children\":");
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
				if (StringUtils
						.equals(terminalMenu.getType(), MenuType.Terminal.toString())) {
					Terminal terminal = terminalService.getTerminalType(terminalMenu.getRefId());
					if(terminal!=null){
						switch (terminal.getTestTarget()) {
						case 0:
							json.append(" 自动LTE");
							break;
						case 1:
							json.append(" LTE单模块商务终端");
							break;
						case 2:
							json.append(" LTE-FI");
							break;
						case 3:
							json.append(" 5g单模块商务终端");
							break;
						case 4:
							json.append(" pc测试软件");
							break;
						default:
							break;
						}
					}
				}
				json.append("\"");
				json.append(",");
				json.append("\"attributes\":");
				json.append("{\"refId\":");
				json.append(terminalMenu.getRefId());
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
	 * 获取某些city域中的设备并且以boxid和名称构造成tree
	 * 
	 * @return
	 */
	public String terminalTree() {
		if (StringUtils.hasText(cityIds)) {
			String[] split = cityIds.split(",");
			List<String> asList = Arrays.asList(split);
			Map<Long, List<Terminal>> groupIdToTerminalMap = terminalGroupService
					.getGroupIdToTerminalMap();
			List<TermianlTreeResponseBean> responseBeans = new ArrayList<>();
			for (Entry<Long, List<Terminal>> entry : groupIdToTerminalMap
					.entrySet()) {
				Long key = entry.getKey();
				if (asList.contains(String.valueOf(key))) {
					List<Terminal> value = entry.getValue();
					if (null != value && 0 != value.size()) {
						for (Terminal terminal : value) {
							TermianlTreeResponseBean responseBean = new TermianlTreeResponseBean();
							responseBean.setGroup(terminal.getTerminalGroup()
									.getName());
							responseBean.setText(terminal.getName());
							responseBean.setValue(terminal.getBoxId());
							responseBeans.add(responseBean);
						}
					}
				}
			}
			ActionContext.getContext().getValueStack().push(responseBeans);
		}
		return ReturnType.JSON;
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
				if (null == terminalMenu.getPid()
						|| pid.longValue() != terminalMenu.getPid().longValue()) {
					continue;
				} else {
					list.add(terminalMenu);
				}
			}
		}
		return list;
	}

	/**
	 * 新增省级子域信息组信息
	 * 
	 * @return
	 */
	public String addProvinceGroup() {
		try {
			terminalGroupService.addProvinceGroup(group);
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 新增市级子域信息组信息
	 * 
	 * @return
	 */
	public String addCityGroup() {
		try {
			terminalGroupService.addCityGroup(group, provinceId);
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 删除省级或者市级子域信息
	 * 
	 * @return
	 */
	public String delSelectedGroup() {
		Long id = group.getId();
		try {
			terminalGroupService.deleteCheckedGroups(new Long[] { id });
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 编辑修改组信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String editTerminalGroup() {
		TerminalGroup oldGroup = terminalGroupService.findGroupById(group
				.getId());
		String oldName = oldGroup.getName();
		oldGroup.setName(group.getName());
		TerminalMenu editMenu = null;
		if (!oldName.equals(group.getName())) {
			try {
				editMenu = terminalGroupService.updateGroupAndMenu(oldGroup);
			} catch (ApplicationException appEx) {
				ActionContext.getContext().getValueStack()
						.set("errorMsg", appEx.getMessage());
			}
		}
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the groupgroup
	 */
	public TerminalGroup getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(TerminalGroup group) {
		this.group = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public TerminalGroup getModel() {
		return group;
	}

	/**
	 * @return the provinceIdprovinceId
	 */
	public Long getProvinceId() {
		return provinceId;
	}

	/**
	 * @param provinceId
	 *            the provinceId to set
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * @return the cityIdscityIds
	 */
	public String getCityIds() {
		return cityIds;
	}

	/**
	 * @param cityIds
	 *            the cityIds to set
	 */
	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

}
