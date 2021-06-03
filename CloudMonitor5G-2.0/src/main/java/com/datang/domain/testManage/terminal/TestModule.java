/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.domain.testManage.terminal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 测试命令
 * 
 * @author songzhigang
 * @version 1.0.0, 2010-12-22 终端设备中的模块
 */
@Entity
@Table(name = "IADS_TEST_MODULE")
public class TestModule implements java.io.Serializable, Comparable<TestModule> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8900729295750658306L;
	/**
	 * 终端中模块的id
	 */
	private Long id;
	/**
	 * 通道号
	 */
	private int channelsNo = -1;
	/**
	 * 模块类型
	 */
	private ChannelType moduleType;
	/**
	 * 芯片类型
	 */
	private String cmoschipType;
	/**
	 * phase
	 */
	private String phase;
	/**
	 * sim卡
	 */
	private String simCard;
	/**
	 * Class
	 */
	private String clazz;
	/**
	 * 运营商
	 */
	private String operator;
	/**
	 * 短消息中心号
	 */
	private String msgCenterNo;
	/**
	 * 短消息告警号
	 */
	private String msgAlarmNo;
	/**
	 * 波特率
	 */
	private long baudRate;
	/**
	 * 拨号超时时间
	 */
	private long timeout;
	/**
	 * ppt拨号失败门限
	 */
	private long pptDialFailBarrier;
	/**
	 * 登录失败门限
	 */
	private long loginFailBarrier;
	/**
	 * 连接失败门限
	 */
	private long connectFailBarrier;
	/**
	 * 是否独立
	 */
	private boolean independency;
	/**
	 * 是否可用
	 */
	private boolean enable = true;
	/**
	 * 测试设备类型
	 */
	private String testTerminalType;

	/**
	 * 测试模块所属的终端
	 */
	// private Terminal terminal;
	/**
	 * @return the id
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
	 * @return the channelsNo
	 */
	@Column(name = "CHANNELS_NO")
	public int getChannelsNo() {
		return channelsNo;
	}

	/**
	 * @param channelsNo
	 *            the channelsNo to set
	 */
	public void setChannelsNo(int channelsNo) {
		this.channelsNo = channelsNo;
	}

	/**
	 * @return the moduleType
	 */
	@Column(name = "MODULE_TYPE")
	public ChannelType getModuleType() {
		return moduleType;
	}

	/**
	 * @param moduleType
	 *            the moduleType to set
	 */
	public void setModuleType(ChannelType moduleType) {
		this.moduleType = moduleType;
	}

	/**
	 * @return the cmoschipType
	 */
	@Column(name = "COMSCHIP_TYPE")
	public String getCmoschipType() {
		return cmoschipType;
	}

	/**
	 * @param cmoschipType
	 *            the cmoschipType to set
	 */
	public void setCmoschipType(String cmoschipType) {
		this.cmoschipType = cmoschipType;
	}

	/**
	 * @return the phase
	 */
	@Column(name = "PHASE")
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

	/**
	 * @return the simCard
	 */
	@Column(name = "SIM_CARD")
	public String getSimCard() {
		return simCard;
	}

	/**
	 * @param simCard
	 *            the simCard to set
	 */
	public void setSimCard(String simCard) {
		this.simCard = simCard;
	}

	/**
	 * @return the clazz
	 */
	@Column(name = "CLAZZ")
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz
	 *            the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return the operator
	 */
	@Column(name = "OPERATOR")
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the msgCenterNo
	 */
	@Column(name = "MSG_CENTER_NO")
	public String getMsgCenterNo() {
		return msgCenterNo;
	}

	/**
	 * @param msgCenterNo
	 *            the msgCenterNo to set
	 */
	public void setMsgCenterNo(String msgCenterNo) {
		this.msgCenterNo = msgCenterNo;
	}

	/**
	 * @return the msgAlarmNo
	 */
	@Column(name = "MSG_ALARM_NO")
	public String getMsgAlarmNo() {
		return msgAlarmNo;
	}

	/**
	 * @param msgAlarmNo
	 *            the msgAlarmNo to set
	 */
	public void setMsgAlarmNo(String msgAlarmNo) {
		this.msgAlarmNo = msgAlarmNo;
	}

	/**
	 * @return the baudRate
	 */
	@Column(name = "BAUD_RATE")
	public long getBaudRate() {
		return baudRate;
	}

	/**
	 * @param baudRate
	 *            the baudRate to set
	 */
	public void setBaudRate(long baudRate) {
		this.baudRate = baudRate;
	}

	/**
	 * @return the timeout
	 */
	@Column(name = "TIMEOUT")
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the pptDialFailBarrier
	 */
	@Column(name = "PPT_DIAL_FAIL_BARRIER")
	public long getPptDialFailBarrier() {
		return pptDialFailBarrier;
	}

	/**
	 * @param pptDialFailBarrier
	 *            the pptDialFailBarrier to set
	 */
	public void setPptDialFailBarrier(long pptDialFailBarrier) {
		this.pptDialFailBarrier = pptDialFailBarrier;
	}

	/**
	 * @return the loginFailBarrier
	 */
	@Column(name = "LOGIN_FAIL_BARRIER")
	public long getLoginFailBarrier() {
		return loginFailBarrier;
	}

	/**
	 * @param loginFailBarrier
	 *            the loginFailBarrier to set
	 */
	public void setLoginFailBarrier(long loginFailBarrier) {
		this.loginFailBarrier = loginFailBarrier;
	}

	/**
	 * @return the connectFailBarrier
	 */
	@Column(name = "CONNECT_FAIL_BARRIER")
	public long getConnectFailBarrier() {
		return connectFailBarrier;
	}

	/**
	 * @param connectFailBarrier
	 *            the connectFailBarrier to set
	 */
	public void setConnectFailBarrier(long connectFailBarrier) {
		this.connectFailBarrier = connectFailBarrier;
	}

	/**
	 * @return the independency
	 */
	@Column(name = "INDEPENDENCY", columnDefinition = "number", length = 1)
	public boolean isIndependency() {
		return independency;
	}

	/**
	 * @param independency
	 *            the independency to set
	 */
	public void setIndependency(boolean independency) {
		this.independency = independency;
	}

	/**
	 * @return the enable
	 */
	@Column(name = "P_ENABLE", columnDefinition = "number", length = 1)
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	// /**
	// * @return the terminal
	// */
	// @ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "TERMINAL_ID")
	// public Terminal getTerminal() {
	// return terminal;
	// }
	//
	// /**
	// * @param terminal the terminal to set
	// */
	// public void setTerminal(Terminal terminal) {
	// this.terminal = terminal;
	// }

	/**
	 * @return the testTerminalTypetestTerminalType
	 */
	@Column(name = "TEST_TERMINAL_TYPE")
	public String getTestTerminalType() {
		return testTerminalType;
	}

	/**
	 * @param testTerminalType
	 *            the testTerminalType to set
	 */
	public void setTestTerminalType(String testTerminalType) {
		this.testTerminalType = testTerminalType;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof TestModule)) {
			return false;
		}
		TestModule tm = (TestModule) o;
		return tm.getChannelsNo() == getChannelsNo();
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + getChannelsNo();
		return result;

	}

	@Override
	public int compareTo(TestModule o) {
		if (null != o) {
			if (getChannelsNo() > o.getChannelsNo()) {
				return 1;
			} else if (getChannelsNo() < o.getChannelsNo()) {
				return -1;
			}
		}
		return 0;
	}
}