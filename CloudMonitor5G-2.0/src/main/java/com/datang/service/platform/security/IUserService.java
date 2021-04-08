package com.datang.service.platform.security;

import java.util.List;
import java.util.Set;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.security.User;
import com.datang.domain.security.UserGroup;

/**
 * 用户Servie接口
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 下午1:33:52
 * @version
 */
public interface IUserService {
	/**
	 * 创建用户
	 * 
	 * @param user
	 * @param userGroupIds
	 * @param usergroupIds
	 */
	public void createUser(User user, List<Long> userGroupIds);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 */
	public void deleteUser(Long userId); // 删除用户

	/**
	 * 更新用户
	 * 
	 * @param user
	 */
	public void modifyUser(User user);

	/**
	 * 修改密码
	 * 
	 * @param username
	 * @param newPassword
	 */
	public void changePassword(String username, String newPassword);

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param password
	 */
	public void changePassword(Long userId, String password);

	/**
	 * 根据用户名查询
	 * 
	 * @param username
	 * @return
	 */
	public User findByUsername(String username);

	/**
	 * 更具id查询用户
	 * 
	 * @param id
	 * @return
	 */
	public User findByUserId(long id);

	/**
	 * 获取某个用户的所有用户组
	 * 
	 * @param username
	 * @return
	 */
	public Set<UserGroup> findUserGroupSet(String username);

	/**
	 * 获取某个用户的所有用户组名
	 * 
	 * @param username
	 * @return
	 */
	public Set<String> findUserGroupNames(String username);// 根据用户名查找其角色

	/**
	 * 获取某个用户的所有功能权限名称
	 * 
	 * @param username
	 * @return
	 */
	public Set<String> findPermissions(String username);

	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);
	
	/**
	 * 查询所有的用户
	 * @author lucheng
	 * @date 2020年8月21日 下午4:11:44
	 * @return
	 */
	public List<User> findAllUsers();

}
