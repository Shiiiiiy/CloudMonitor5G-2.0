package com.datang.web.action.platform.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
import com.datang.domain.security.User;
import com.datang.domain.security.UserGroup;
import com.datang.service.platform.security.IUserGroupService;
import com.datang.service.platform.security.IUserService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 用户Action
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 下午2:55:35
 * @version
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class UserAction extends PageAction implements ModelDriven<User> {

	/**
	 * 用户服务
	 */
	@Autowired
	private IUserService userService;
	/**
	 * 用户组服务
	 */
	@Autowired
	private IUserGroupService userGroupService;
	/**
	 * 表单对象
	 */
	private User user = new User();
	/**
	 * 用户组id集合,新增和修改用
	 */
	private List<Long> userGroupIds;

	/**
	 * 跳转到用户LIST界面
	 * 
	 * @return
	 */
	public String userListUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * 新增用户
	 * 
	 * @return
	 */
	public String addUser() {
		User findByUsername = userService.findByUsername(user.getUsername());
		if (null != findByUsername) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "用户名已存在!");
			return ReturnType.JSON;
		}
		userService.createUser(user, userGroupIds);
		ActionContext.getContext().getValueStack().push("success");
		return ReturnType.JSON;
	}

	/**
	 * 修改用户
	 * 
	 * @return
	 */
	public String editUser() {
		User findByUsername = userService.findByUsername(user.getUsername());
		if (null != findByUsername
				&& !findByUsername.getId().equals(user.getId())) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "用户名已存在!");
			return ReturnType.JSON;
		}
		// 设置用户的用户组
		if (null != userGroupIds && 0 != userGroupIds.size()) {
			Set<UserGroup> groups = user.getGroups();
			for (Long groupId : userGroupIds) {
				UserGroup group = new UserGroup();
				group.setId(groupId);
				groups.add(group);
			}
		}
		// 复制编辑后的用户信息至持久态原用户
		User findByUserId = userService.findByUserId(user.getId());
		findByUserId.setUsername(user.getUsername());
		findByUserId.setDescription(user.getDescription());
		findByUserId.setDepartment(user.getDepartment());
		findByUserId.setEmail(user.getEmail());
		findByUserId.setJobPosition(user.getJobPosition());
		findByUserId.setPhone(user.getPhone());
		findByUserId.setGroups(user.getGroups());
		userService.modifyUser(findByUserId);
		return ReturnType.JSON;
	}

	/**
	 * 删除用户
	 * 
	 * @return
	 */
	public String delUser() {
		userService.deleteUser(user.getId());
		return ReturnType.JSON;
	}

	/**
	 * 获取某个用户的用户组JSON或者所有用户组JSON
	 * 
	 * @return
	 */
	public String findUserGroups() {
		List<UserGroup> usergroups = new ArrayList<>();
		if (null != user.getId()) {
			usergroups.addAll(userService.findByUserId(user.getId())
					.getGroups());
		}
		List<UserGroup> all = new ArrayList<>();
		all.addAll(userGroupService.findUserGroups());
		StringBuffer buffer = new StringBuffer();
		/**
		 * 递归生成JSON树
		 */
		buildJSON(all, buffer, usergroups);
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
	 * 根据用户组的功能权限生成菜单树json
	 * 
	 * @param all
	 * @param json
	 * @param groups
	 */
	private static void buildJSON(List<UserGroup> all, StringBuffer json,
			List<UserGroup> groups) {
		json.append("[");
		if (all != null && all.size() > 0) {
			for (int i = 0; i < all.size(); i++) {
				UserGroup userGroup = all.get(i);
				json.append("{");
				json.append("\"id\"");
				json.append(":");
				json.append(userGroup.getId());
				json.append(",");
				json.append("\"name\"");
				json.append(":");
				json.append("\"");
				json.append(userGroup.getName());
				json.append("\"");
				if (groups.contains(userGroup)) {
					json.append(",");
					json.append("\"selected\":true");
				}
				json.append("}");
				if (i != (all.size() - 1)) {
					json.append(",");
				}
			}
		}
		json.append("]");
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public String changePassword() {
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		Long userId = user.getId();
		if (userId != null) {
			userService.changePassword(userId, user.getPassword());
		} else {
			userService.changePassword(username, user.getPassword());
		}
		return ReturnType.JSON;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return userService.pageList(pageList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public User getModel() {
		return user;
	}

	/**
	 * @return the useruser
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the userGroupIdsuserGroupIds
	 */
	public List<Long> getUserGroupIds() {
		return userGroupIds;
	}

	/**
	 * @param userGroupIds
	 *            the userGroupIds to set
	 */
	public void setUserGroupIds(List<Long> userGroupIds) {
		this.userGroupIds = userGroupIds;
	}

}
