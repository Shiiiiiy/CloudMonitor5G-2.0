/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.domain.testPlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 网络配置对象
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:40:06
 * @version
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "IADS_TP_NETWORK")
@XStreamAlias("Network")
public class NetWork implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 通讯服务器IP地址
	 */
	@XStreamAlias("PortalIP")
	private String portalIP;
	/**
	 * 通讯服务器端口
	 */
	@XStreamAlias("PortalPort")
	private String portalPort;
	/**
	 * 登录用户名
	 */
	@XStreamAlias("User")
	private String user = "gprs";
	/**
	 * 登陆密码
	 */
	@XStreamAlias("Password")
	private String password = "gprs";
	/**
	 * 0表示不使用lan回传，1表示使用
	 * 
	 * @author yinzhipeng 2015-04-17
	 *         UseLAN的新定义：在原定义的基础上，增加2表示“PPP拨号”，3表示“虚拟网卡拨号”。
	 */
	@XStreamAlias("UseLAN")
	private String useLAN = "3";
	/**
	 * 指明使用哪一个测试模块做回传数据用途 0表示不回传，1-10为回传模块号
	 */
	@XStreamAlias("SendData")
	private String sendData = "1";
	/**
	 * 回传时拨号连接使用的拨叫号码
	 */
	@XStreamAlias("DialNumber")
	private String dialNumber = "*99#";
	/**
	 * 接入点
	 */
	@XStreamAlias("APN")
	private String apn = "CMNET";
	/**
	 * 拨号网络用户名
	 */
	@XStreamAlias("DialUpUser")
	private String dialUpUser = "gprs";
	/**
	 * 拨号网络密码
	 */
	@XStreamAlias("DialUpPassword")
	private String dialUpPassword = "gprs";

	/**
	 * @return the portalIP
	 */
	@Column(name = "PROTAL_IP")
	public String getPortalIP() {
		return portalIP;
	}

	/**
	 * @param portalIP
	 *            the portalIP to set
	 */
	public void setPortalIP(String portalIP) {
		this.portalIP = portalIP;
	}

	/**
	 * @return the portalPort
	 */
	@Column(name = "PROTAL_PORT")
	public String getPortalPort() {
		return portalPort;
	}

	/**
	 * @param portalPort
	 *            the portalPort to set
	 */
	public void setPortalPort(String portalPort) {
		this.portalPort = portalPort;
	}

	/**
	 * @return the user
	 */
	@Column(name = "USER_NAME")
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	@Column(name = "PASSWORD")
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
	 * @return the useLAN
	 */
	@Column(name = "USE_LAN")
	public String getUseLAN() {
		return useLAN;
	}

	/**
	 * @param useLAN
	 *            the useLAN to set
	 */
	public void setUseLAN(String useLAN) {
		this.useLAN = useLAN;
	}

	/**
	 * @return the sendData
	 */
	@Column(name = "SEND_DATA")
	public String getSendData() {
		return sendData;
	}

	/**
	 * @param sendData
	 *            the sendData to set
	 */
	public void setSendData(String sendData) {
		this.sendData = sendData;
	}

	/**
	 * @return the dialNumber
	 */
	@Column(name = "DIAL_NUMBER")
	public String getDialNumber() {
		return dialNumber;
	}

	/**
	 * @param dialNumber
	 *            the dialNumber to set
	 */
	public void setDialNumber(String dialNumber) {
		this.dialNumber = dialNumber;
	}

	/**
	 * @return the apn
	 */
	@Column(name = "APN")
	public String getApn() {
		return apn;
	}

	/**
	 * @param apn
	 *            the apn to set
	 */
	public void setApn(String apn) {
		this.apn = apn;
	}

	/**
	 * @return the dialUpUser
	 */
	@Column(name = "DIAL_UP_USER")
	public String getDialUpUser() {
		return dialUpUser;
	}

	/**
	 * @param dialUpUser
	 *            the dialUpUser to set
	 */
	public void setDialUpUser(String dialUpUser) {
		this.dialUpUser = dialUpUser;
	}

	/**
	 * @return the dialUpPassword
	 */
	@Column(name = "DIAL_UP_PASSWORD")
	public String getDialUpPassword() {
		return dialUpPassword;
	}

	/**
	 * @param dialUpPassword
	 *            the dialUpPassword to set
	 */
	public void setDialUpPassword(String dialUpPassword) {
		this.dialUpPassword = dialUpPassword;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
