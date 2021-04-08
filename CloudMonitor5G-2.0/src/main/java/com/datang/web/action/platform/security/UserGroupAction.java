package com.datang.web.action.platform.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.security.Permission;
import com.datang.domain.security.UserGroup;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserGroupService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 用户组Action
 * 
 * @author yinzhipeng
 * @date:2015年10月10日 上午11:41:37
 * @version
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class UserGroupAction extends PageAction implements
		ModelDriven<UserGroup> {

	@Autowired
	private IUserGroupService userGroupService;
	@Autowired
	private IMenuManageService menuManageService;

	/**
	 * 表单对象
	 */
	private UserGroup userGroup = new UserGroup();
	/**
	 * 功能权限ID按','分隔字段
	 */
	private String permissionIds;
	/**
	 * 区域权限ID按','分隔字段
	 */
	private String areaIds;

	/**
	 * 跳转到用户组LIST页面
	 * 
	 * @return
	 */
	public String userGroupListUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到添加用户组页面
	 * 
	 * @return
	 */
	public String addUserGroupUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		valueStack.set("operType", "add");
		return ReturnType.ADD;
	}

	/**
	 * 跳转到用户组详情页面
	 * 
	 * @return
	 */
	public String showUserGroupInfo() {
		// 获取用户组信息
		UserGroup findRoleByRoleName = userGroupService
				.findUserGroupById(userGroup.getId());
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		valueStack.set("userGroup", findRoleByRoleName);
		valueStack.set("operType", "show");
		return ReturnType.ADD;

	}

	/**
	 * 跳转到修改用户组页面
	 * 
	 * @return
	 */
	public String editUserGroupUI() {
		// 获取编辑的用户组信息
		UserGroup findRoleByRoleName = userGroupService
				.findUserGroupById(userGroup.getId());
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		valueStack.set("userGroup", findRoleByRoleName);
		valueStack.set("operType", "edit");
		return ReturnType.ADD;

	}

	/**
	 * 添加用户组
	 * 
	 * @return
	 */
	public String addUserGroup() {
		UserGroup findRoleByRoleName = userGroupService
				.findUserGroupByUserGroupName(userGroup.getName());
		if (null != findRoleByRoleName) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "角色名已存在!");
			return ReturnType.JSON;
		}
		// 设置用户组的功能权限
		String[] split = permissionIds.split(",");
		Set<Permission> permissions = userGroup.getPermissions();
		for (String string : split) {
			Permission permission = new Permission();
			permission.setId(Long.parseLong(string));
			permissions.add(permission);
		}
		// 设置用户组的区域权限
		if (null != areaIds && 0 != areaIds.trim().length()) {
			String[] split2 = areaIds.split(",");
			Set<Menu> menus = userGroup.getMenus();
			for (String string : split2) {
				if (-1 == Long.parseLong(string)) {
					continue;
				}
				TerminalMenu menu = new TerminalMenu();
				menu.setId(Long.parseLong(string));
				menus.add(menu);
			}
		}
		userGroupService.addUserGroup(userGroup);
		return ReturnType.JSON;
	}

	/**
	 * 修改用户组
	 * 
	 * @return
	 */
	public String editUserGroup() {
		UserGroup findRoleByRoleName = userGroupService
				.findUserGroupByUserGroupName(userGroup.getName());
		// 判定用户组名时候存在
		if (null != findRoleByRoleName
				&& !findRoleByRoleName.getId().equals(userGroup.getId())) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "角色名已存在!");
			return ReturnType.JSON;
		}
		// 设置用户组的功能权限
		String[] split = permissionIds.split(",");
		Set<Permission> permissions = userGroup.getPermissions();
		for (String string : split) {
			Permission permission = new Permission();
			permission.setId(Long.parseLong(string));
			permissions.add(permission);
		}
		// 设置用户组的区域权限
		if (null != areaIds && 0 != areaIds.trim().length()) {
			String[] split2 = areaIds.split(",");
			Set<Menu> menus = userGroup.getMenus();
			for (String string : split2) {
				if (-1 == Long.parseLong(string)) {
					continue;
				}
				TerminalMenu menu = new TerminalMenu();
				menu.setId(Long.parseLong(string));
				menus.add(menu);
			}
		}

		// 复制编辑后的用户组信息至持久态原用户组
		UserGroup findUserGroupById = userGroupService
				.findUserGroupById(userGroup.getId());
		findUserGroupById.setName(userGroup.getName());
		findUserGroupById.setDescription(userGroup.getDescription());
		findUserGroupById.setPermissions(userGroup.getPermissions());
		findUserGroupById.setMenus(userGroup.getMenus());
		userGroupService.modifyUserGroup(findUserGroupById);
		return ReturnType.JSON;
	}

	/**
	 * 删除用户组
	 * 
	 * @return
	 */
	public String delUserGroup() {
		userGroupService.delUserGroup(userGroup.getId());
		return ReturnType.JSON;
	}

	/**
	 * 获取某个用户组的功能菜单JSON或者所有功能菜单JSON
	 * 
	 * @return
	 */
	public String findPermissions() {
		List<Permission> usergroupPermissions = new ArrayList<>();
		// 获取用户组的功能权限
		if (null != userGroup.getId()) {
			usergroupPermissions = userGroupService.findPermissions(userGroup
					.getId());
		}
		List<Permission> root = userGroupService.findTopPermissions();
		StringBuffer buffer = new StringBuffer();
		/**
		 * 递归生成JSON树
		 */
		buildPermissionJSON(root, buffer, usergroupPermissions);
		// 直接返回原始字符串
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(buffer.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取某个用户组的区域JSON或者所有区域JSON
	 * 
	 * @return
	 */
	public String findAreaMenus() {
		List<TerminalMenu> userGroupTerminalMenus = new ArrayList<>();
		// 获取用户组的区域权限
		if (null != userGroup.getId()) {
			UserGroup findUserGroupById = userGroupService
					.findUserGroupById(userGroup.getId());

			if (findUserGroupById.isPowerGroup()) {
				// 超级用户组的区域权限为all
				Collection<TerminalMenu> allTerminalMenus = menuManageService
						.getAllTerminalMenus();
				userGroupTerminalMenus.addAll(allTerminalMenus);
			} else {
				// 普通用户组的区域权限为其本身的区域权限
				Set<TerminalMenu> terminalmenus = findUserGroupById
						.getTerminalmenus();
				userGroupTerminalMenus.addAll(terminalmenus);
			}

		}
		List<TerminalMenu> allTerminalMenus = (List<TerminalMenu>) menuManageService
				.getAllTerminalMenus();
		List<TerminalMenu> topTerminalMenus = new ArrayList<>();
		for (TerminalMenu terminalMenu : allTerminalMenus) {
			if (null == terminalMenu.getPid()) {
				topTerminalMenus.add(terminalMenu);
			}
		}
		// 获取所有的终端菜单
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[{\"id\":\"-1\",\"text\":\"区域权限\",\"children\":");
		buildTerminalJSON(topTerminalMenus, allTerminalMenus, stringBuffer,
				userGroupTerminalMenus);
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
				if (StringUtils.equals(terminalMenu.getType(),
						MenuType.Terminal.toString())
						|| null == terminalMenu.getPid()
						|| pid.longValue() != terminalMenu.getPid().longValue()) {
					continue;
				} else {
					list.add(terminalMenu);
				}
			}
		}
		return list;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return userGroupService.pageList(pageList);
	}

	/**
	 * 根据用户组的功能权限生成菜单树json
	 * 
	 * @param allPermissions
	 * @param json
	 * @param permissions
	 */
	private static void buildPermissionJSON(List<Permission> allPermissions,
			StringBuffer json, List<Permission> permissions) {
		json.append("[");
		if (allPermissions != null && allPermissions.size() > 0) {
			for (int i = 0; i < allPermissions.size(); i++) {
				Permission permission = allPermissions.get(i);
				json.append("{");
				json.append("\"id\"");
				json.append(":");
				json.append(permission.getId());
				json.append(",");
				json.append("\"text\"");
				json.append(":");
				json.append("\"");
				json.append(permission.getText());
				json.append("\"");
				// if (null == permission.getParent()
				// || null == permission.getChildren()
				// || 0 == permission.getChildren().size()) {
				// json.append(",\"state\":\"open\"");
				// } else {
				// json.append(",\"state\":\"closed\"");
				// }

				if (permissions.contains(permission)) {
					json.append(",");
					json.append("\"checked\":true");
				}
				List<Permission> children = new ArrayList<Permission>();
				children.addAll(permission.getChildren());
				Permission[] permissionArray = children
						.toArray(new Permission[0]);
				Arrays.sort(permissionArray);
				if (permissionArray != null && permissionArray.length > 0) {
					json.append(",");
					json.append("\"children\"");
					json.append(":");
					buildPermissionJSON(Arrays.asList(permissionArray), json,
							permissions);
				}
				json.append("}");
				if (i != allPermissions.size() - 1) {
					json.append(",");
				}
			}
		}
		json.append("]");
	}

	/**
	 * 根据用户组的区域区域权限生成菜单树JSON
	 * 
	 * @param allTerminalMenus
	 * @param stringBuffer
	 * @param userGroupTerminalMenus
	 */
	private void buildTerminalJSON(List<TerminalMenu> topTerminalMenus,
			List<TerminalMenu> allTerminalMenus, StringBuffer json,
			List<TerminalMenu> userGroupTerminalMenus) {
		json.append("[");
		if (topTerminalMenus != null && topTerminalMenus.size() > 0) {
			for (int i = 0; i < topTerminalMenus.size(); i++) {
				TerminalMenu terminalMenu = topTerminalMenus.get(i);
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
				// if (null == permission.getParent()
				// || null == permission.getChildren()
				// || 0 == permission.getChildren().size()) {
				// json.append(",\"state\":\"open\"");
				// } else {
				// json.append(",\"state\":\"closed\"");
				// }

				if (userGroupTerminalMenus.contains(terminalMenu)) {
					json.append(",");
					json.append("\"checked\":true");
				} else {
					json.append(",");
					json.append("\"checked\":false");
				}
				List<TerminalMenu> children = new ArrayList<TerminalMenu>();
				children.addAll(findTerminalMenusFromListByPid(
						terminalMenu.getId(), allTerminalMenus));
				TerminalMenu[] terminalMenuArray = children
						.toArray(new TerminalMenu[0]);

				Arrays.sort(terminalMenuArray);
				if (terminalMenuArray != null && terminalMenuArray.length > 0) {
					json.append(",");
					json.append("\"children\"");
					json.append(":");
					buildTerminalJSON(Arrays.asList(terminalMenuArray),
							allTerminalMenus, json, userGroupTerminalMenus);
				}
				json.append("}");
				if (i != topTerminalMenus.size() - 1) {
					json.append(",");
				}
			}
		}
		json.append("]");

	}

	/**
	 * @return the userGroupuserGroup
	 */
	public UserGroup getUserGroup() {
		return userGroup;
	}

	/**
	 * @param userGroup
	 *            the userGroup to set
	 */
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	/**
	 * @return the permissionIdspermissionIds
	 */
	public String getPermissionIds() {
		return permissionIds;
	}

	/**
	 * @param permissionIds
	 *            the permissionIds to set
	 */
	public void setPermissionIds(String permissionIds) {
		this.permissionIds = permissionIds;
	}

	/**
	 * @return the areaIdsareaIds
	 */
	public String getAreaIds() {
		return areaIds;
	}

	/**
	 * @param areaIds
	 *            the areaIds to set
	 */
	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public UserGroup getModel() {
		return userGroup;
	}

}
