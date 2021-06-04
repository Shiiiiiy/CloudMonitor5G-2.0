package com.datang.domain.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.TerminalMenu;

/**
 * 用户组BEAN
 * 
 * @author yinzhipeng
 * @date:2015年10月8日 下午5:41:26
 * @version
 */
@Entity
@Table(name = "IADS_USERGROUP")
public class UserGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8423042077528706214L;
	/**
	 * 用户组ID
	 */
	private Long id;
	/**
	 * 用户组名称
	 */
	private String name;
	/**
	 * 用户组包含的用户
	 */
	private Set<User> users = new HashSet<User>();
	/**
	 * 用户组可以操作的菜单
	 */
	private Set<Permission> permissions = new HashSet<Permission>();
	/**
	 * 用户组可以操作的设备菜单
	 */
	private Set<Menu> menus = new HashSet<Menu>();
	/**
	 * 是否是超级用户组
	 */
	private boolean powerGroup;
	/**
	 * 用户组描述
	 */
	private String description;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 该用户组是否被删除,false没有被删除,true被删除
	 */
	private boolean hasDelete = false;
	/**
	 * 删除时间
	 */
	private Date deleteDate;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the namename
	 */
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the usersusers
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "IADS_USER_USERGROUP", joinColumns = { @JoinColumn(name = "GROUPS_ID") }, inverseJoinColumns = @JoinColumn(name = "USERS_ID"))
	@Fetch(FetchMode.SUBSELECT)
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	/**
	 * @return the powerGrouppowerGroup
	 */
	@Column(name = "POWER_GROUP", columnDefinition = "boolean")
	public boolean isPowerGroup() {
		return powerGroup;
	}

	/**
	 * @param powerGroup
	 *            the powerGroup to set
	 */
	public void setPowerGroup(boolean powerGroup) {
		this.powerGroup = powerGroup;
	}

	/**
	 * @return the descriptiondescription
	 */
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the createDatecreateDate
	 */
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the hasDeletehasDelete
	 */
	@Column(name = "HAS_DELETE", columnDefinition = "boolean")
	public boolean isHasDelete() {
		return hasDelete;
	}

	/**
	 * @param hasDelete
	 *            the hasDelete to set
	 */
	public void setHasDelete(boolean hasDelete) {
		this.hasDelete = hasDelete;
	}

	/**
	 * @return the deleteDatedeleteDate
	 */
	@Column(name = "DELETE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDeleteDate() {
		return deleteDate;
	}

	/**
	 * @param deleteDate
	 *            the deleteDate to set
	 */
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	/**
	 * @return the permissionspermissions
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "IADS_USERGROUP_PERMISSION", joinColumns = { @JoinColumn(name = "USERGROUP_ID") }, inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"))
	@Fetch(FetchMode.SUBSELECT)
	public Set<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the menusmenus
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "IADS_USERGROUP_MENU", joinColumns = { @JoinColumn(name = "USERGROUP_ID") }, inverseJoinColumns = @JoinColumn(name = "MENUS_ID"))
	@Fetch(FetchMode.SUBSELECT)
	public Set<Menu> getMenus() {
		return menus;
	}

	/**
	 * @param menus
	 *            the menus to set
	 */
	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

	@Transient
	public Set<TerminalMenu> getTerminalmenus() {

		Set<TerminalMenu> terminalMenus = new HashSet<TerminalMenu>();

		for (Menu menu : this.menus) {
			if (menu == null) {
				continue;
			}
			if (menu instanceof TerminalMenu) {
				terminalMenus.add((TerminalMenu) menu);
			}
		}
		return terminalMenus;
	}

	/**
	 * 添加Menu
	 * 
	 * @param menu
	 *            Menu
	 */
	@Transient
	public void addMenu(Menu menu) {
		if (this.menus == null) {
			this.menus = new HashSet<Menu>();
		}
		this.menus.add(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserGroup [id=" + id + ", name=" + name + ", users=" + users
				+ ", permissions=" + permissions + ", menus=" + menus
				+ ", powerGroup=" + powerGroup + ", description=" + description
				+ ", createDate=" + createDate + ", hasDelete=" + hasDelete
				+ ", deleteDate=" + deleteDate + "]";
	}

}