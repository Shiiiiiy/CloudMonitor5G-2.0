package com.datang.domain.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.datang.domain.platform.analysisThreshold.VolteAnalysisAboutThreshold;

/**
 * 用户BEAN
 * 
 * @author yinzhipeng
 * @date:2015年10月8日 下午5:41:03
 * @version
 */
@Entity
@Table(name = "IADS_USERS")
public class User implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2253741063473309986L;
	/**
	 * 用户ID
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 密码加密盐值
	 */
	private String salt;
	/**
	 * 所属部门
	 */
	private String department;
	/**
	 * 工作职位
	 */
	private String jobPosition;
	/**
	 * 用户描述
	 */
	private String description;
	/**
	 * 密码修改时间
	 */
	private Date passwordChangeTime;
	/**
	 * 是否是超级用户
	 */
	private boolean powerUser;
	/**
	 * 上一次登录的时间
	 */
	private Date loginDateLast;
	/**
	 * 本次登录的时间
	 */
	private Date loginDateNow;
	/**
	 * 创建用户时间
	 */
	private Date createDate;
	/**
	 * 用户组集合
	 */
	private Set<UserGroup> groups = new HashSet<UserGroup>();
	/**
	 * 用户组的组名','分隔字符串
	 */
	private String userGroupStr;
    
	/**
	 * 门限分析
	 */
	
	private List<VolteAnalysisAboutThreshold> aboutThresholdList;
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
	 * @return the usernameusername
	 */
	@Column(name = "USER_NAME")
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the passwordpassword
	 */
	@Column(name = "PASSWORD")
	@JSON(serialize = false)
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the phonephone
	 */
	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the emailemail
	 */
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the saltsalt
	 */
	@Column(name = "SALT")
	@JSON(serialize = false)
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the departmentdepartment
	 */
	@Column(name = "DEPARTMENT")
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the jobPositionjobPosition
	 */
	@Column(name = "JOB_POSITION")
	public String getJobPosition() {
		return jobPosition;
	}

	/**
	 * @param jobPosition
	 *            the jobPosition to set
	 */
	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
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
	 * @return the passwordChangeTimepasswordChangeTime
	 */
	@Column(name = "PASSWORD_CHANGE_TIME")
	@JSON(serialize = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPasswordChangeTime() {
		return passwordChangeTime;
	}

	/**
	 * @param passwordChangeTime
	 *            the passwordChangeTime to set
	 */
	public void setPasswordChangeTime(Date passwordChangeTime) {
		this.passwordChangeTime = passwordChangeTime;
	}

	/**
	 * @return the powerUserpowerUser
	 */
	@Column(name = "POWER_USER", columnDefinition = "number", length = 1)
	public boolean isPowerUser() {
		return powerUser;
	}

	/**
	 * @param powerUser
	 *            the powerUser to set
	 */
	public void setPowerUser(boolean powerUser) {
		this.powerUser = powerUser;
	}

	/**
	 * @return the loginDateLastloginDateLast
	 */
	@Column(name = "LOGIN_DATE_LAST")
	@Temporal(TemporalType.TIMESTAMP)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getLoginDateLast() {
		return loginDateLast;
	}

	/**
	 * @param loginDateLast
	 *            the loginDateLast to set
	 */
	public void setLoginDateLast(Date loginDateLast) {
		this.loginDateLast = loginDateLast;
	}

	/**
	 * @return the loginDateNowloginDateNow
	 */
	@Column(name = "LOGIN_DATE_NOW")
	@Temporal(TemporalType.TIMESTAMP)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getLoginDateNow() {
		return loginDateNow;
	}

	/**
	 * @param loginDateNow
	 *            the loginDateNow to set
	 */
	public void setLoginDateNow(Date loginDateNow) {
		this.loginDateNow = loginDateNow;
	}

	/**
	 * @return the createDatecreateDate
	 */
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JSON(serialize = false)
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
	 * @return the groupsgroups
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "IADS_USER_USERGROUP", joinColumns = { @JoinColumn(name = "USERS_ID") }, inverseJoinColumns = @JoinColumn(name = "GROUPS_ID"))
	@Fetch(FetchMode.SUBSELECT)
	@JSON(serialize = false)
	public Set<UserGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(Set<UserGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the userGroupStruserGroupStr
	 */
	@Transient
	public String getUserGroupStr() {
		if (null != groups && 0 != groups.size()) {
			StringBuffer stringBuffer = new StringBuffer();
			for (UserGroup group : groups) {
				stringBuffer.append(group.getName() + ",");
			}
			if (0 != stringBuffer.length()) {
				String substring = stringBuffer.substring(0,
						stringBuffer.length() - 1);
				setUserGroupStr(substring);
			}

		}
		return userGroupStr;
	}

	/**
	 * @param userGroupStr
	 *            the userGroupStr to set
	 */
	public void setUserGroupStr(String userGroupStr) {
		this.userGroupStr = userGroupStr;
	}

	@OneToMany( mappedBy = "user")
	public List<VolteAnalysisAboutThreshold> getAboutThresholdList() {
		return aboutThresholdList;
	}

	public void setAboutThresholdList(
			List<VolteAnalysisAboutThreshold> aboutThresholdList) {
		this.aboutThresholdList = aboutThresholdList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		User newObject = new User();
		newObject.setCreateDate(createDate);
		newObject.setDepartment(department);
		newObject.setDescription(description);
		newObject.setEmail(email);
		newObject.setGroups(groups);
		newObject.setId(id);
		newObject.setJobPosition(jobPosition);
		newObject.setLoginDateLast(loginDateLast);
		newObject.setLoginDateNow(loginDateNow);
		newObject.setPassword(password);
		newObject.setPasswordChangeTime(passwordChangeTime);
		newObject.setPhone(phone);
		newObject.setPowerUser(powerUser);
		newObject.setSalt(salt);
		newObject.setUsername(username);
		return newObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", phone=" + phone + ", email=" + email
				+ ", salt=" + salt + ", department=" + department
				+ ", jobPosition=" + jobPosition + ", description="
				+ description + ", passwordChangeTime=" + passwordChangeTime
				+ ", powerUser=" + powerUser + ", loginDateLast="
				+ loginDateLast + ", loginDateNow=" + loginDateNow
				+ ", createDate=" + createDate 
				+ ", userGroupStr=" + userGroupStr ;
	}
	/**
	 * 
	 */
	public User() {
		super();
	}

	

}