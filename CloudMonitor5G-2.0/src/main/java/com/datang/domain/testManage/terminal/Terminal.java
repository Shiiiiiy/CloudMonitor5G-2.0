/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.domain.testManage.terminal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.datang.common.util.CollectionUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 终端设备
 * 
 * @author songzhigang
 * @version 1.0.0, 2010-12-22
 * @author dingzhongchang 2011-01-11 添加配置文件版本属性
 * @author dingzhongchang 2011-01-13 添加终端密码属性
 * @author dingzhongchang 2011-02-14 添加终端是否在线状态属性
 */
@Entity
@Table(name = "IADS_TERMINAL")
public class Terminal implements java.io.Serializable, Comparable<Terminal> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5758705424162279966L;

	/**
	 * 终端id
	 */
	private Long id;
	/**
	 * 终端的BOXID
	 */
	@XStreamAsAttribute
	private String boxId;

	/**
	 * 终端登录密码
	 */
	@XStreamOmitField
	private String password = "";

	/**
	 * 名称
	 */
	@XStreamAsAttribute
	private String name;

	/**
	 * 厂商
	 */
	@XStreamAsAttribute
	private String manufacturer = "";

	/**
	 * 硬件版本
	 */
	@XStreamAsAttribute
	private String hardwareVersion = "";
	
	/**
	 * 下发版本
	 */
	@XStreamAsAttribute
	private String downVersion = "";

	/**
	 * 软件版本
	 */
	@XStreamAsAttribute
	private String softwareVersion = "";

	/**
	 * 配置文件版本
	 */
	@XStreamAsAttribute
	private Long configVersion;

	/**
	 * 是否可用
	 */
	@XStreamAsAttribute
	private boolean enable = true;

	/**
	 * 是否在线
	 */
	@XStreamOmitField
	private boolean online = false;
	/**
	 * 状态字符串
	 */
	private String onlineStr = "";

	/**
	 * 备注
	 */
	@XStreamAsAttribute
	private String remark = "";

	/**
	 * 生产日期
	 */
	@XStreamAsAttribute
	private Date produceDate;

	/**
	 * 出厂日期
	 */
	@XStreamAsAttribute
	private Date factoryDate;
	
	/**
	 * 安装日期
	 */
	@XStreamAsAttribute
	private Long installDate;
	
	/**
	 * 安装日期 :yyyy-MM-dd HH:mm:ss
	 */
	@XStreamAsAttribute
	private String installDateFormt;

	/**
	 * 测试计划的版本
	 */
	@XStreamOmitField
	private String testPlanVersion = "";

	@XStreamOmitField
	private String testPlanFileLink;

	/**
	 * 终端相关的监控记录
	 */
	// yzp
	// @XStreamOmitField
	// private Set<MonitorEntity> monitorEntitys;

	/**
	 * 终端执行过的测试计划
	 */
	@XStreamOmitField
	private Set<HistoryTestPlan> historyTestPlans;

	// /**
	// * 终端下发过的历史软件更新信息
	// */
	// @XStreamOmitField
	// private Set<HistorySoftUpgrade> historySoftUpgrades;

	/**
	 * 终端包含的测试模块
	 */
	@XStreamImplicit(itemFieldName = "Module")
	private List<TestModule> testModuls = new ArrayList<>();
	// yzp
	// @XStreamOmitField
	// private Set<GPS> gpsInfos;

	/**
	 * 终端所属的分组
	 */
	@XStreamAsAttribute
	@XStreamAlias("Group")
	private TerminalGroup terminalGroup;

	/**
	 * 测试目标或者终端类型:0自动LTE ,1单模块商务终端 ,2LTE-FI,3 5G单模块商务终端,4 PC测试软件
	 */
	private Integer testTarget;
	
	private String testTargetStr = "";
	
	/**
	 * 多线程个数
	 */
	@XStreamOmitField
	private Integer mountThread;

	/**
	 * @param mountThread
	 *            the mountThread to set
	 */
	public void setMountThread(Integer mountThread) {
		this.mountThread = mountThread;
	}

	/**
	 * @return the mountThread
	 */
	@Column(name = "MOUNT_THREAD")
	public Integer getMountThread() {
		return mountThread;
	}

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
	 * @return the boxId
	 */
	@Column(name = "BOX_ID")
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param boxId
	 *            the boxId to set
	 */
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	@Column(name = "PASSWORD")
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the manufacturer
	 */
	@Column(name = "MANUFACTURER")
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer
	 *            the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the hardwareVersion
	 */
	@Column(name = "HARDWRE_VERSION")
	public String getHardwareVersion() {
		return hardwareVersion;
	}

	/**
	 * @param hardwareVersion
	 *            the hardwareVersion to set
	 */
	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}
	
	/**
	 * 下发版本
	 * @return
	 */@Column(name = "DOWN_VERSION")
	public String getDownVersion() {
		return downVersion;
	}

	/**
	 * 下发版本
	 * @param downVersion
	 */
	public void setDownVersion(String downVersion) {
		this.downVersion = downVersion;
	}

	/**
	 * @param configVersion
	 *            the configVersion to set
	 */
	public void setConfigVersion(Long configVersion) {
		this.configVersion = configVersion;
	}

	/**
	 * @return the configVersion
	 */
	@Column(name = "CONFIG_VERSION")
	public Long getConfigVersion() {
		return configVersion;
	}

	/**
	 * @return the softwareVersion
	 */
	@Column(name = "SOFTWARE_VERSION")
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * @param softwareVersion
	 *            the softwareVersion to set
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * @return the enable
	 */
	@Column(name = "P_ENABLE", columnDefinition = "boolean")
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

	/**
	 * 
	 * @param online
	 *            the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * 
	 * @return the online
	 */
	@Column(name = "P_ONLINE", columnDefinition = "boolean")
	public boolean isOnline() {
		return online;
	}

	/**
	 * @return the remark
	 */
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the produceDate
	 */
	@Column(name = "PRODUCE_DATE")
	public Date getProduceDate() {
		return produceDate;
	}

	/**
	 * @param produceDate
	 *            the produceDate to set
	 */
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	/**
	 * @return the factoryDate
	 */
	@Column(name = "FACTORY_DATE")
	public Date getFactoryDate() {
		return factoryDate;
	}

	/**
	 * @param factoryDate
	 *            the factoryDate to set
	 */
	public void setFactoryDate(Date factoryDate) {
		this.factoryDate = factoryDate;
	}
	
	@Column(name = "INSTALL_DATE")
	public Long getInstallDate() {
		return installDate;
	}

	public void setInstallDate(Long installDate) {
		this.installDate = installDate;
	}

	@Transient
	public String getInstallDateFormt() {
		if(installDateFormt==null && installDate!=null){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date installTime = new Date(installDate);
			String time = simpleDateFormat.format(installTime);
			return time;
		}
		return installDateFormt;
	}

	public void setInstallDateFormt(String installDateFormt) {
		this.installDateFormt = installDateFormt;
	}

	/**
	 * @return the testPlans
	 */
	// @OneToMany(fetch = FetchType.LAZY)
	// @Fetch(FetchMode.SUBSELECT)
	// @JoinColumn(name = "HISTORY_TESTPLAN_ID")
	// @Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	// @JSON(serialize = false)
	@Transient
	public Set<HistoryTestPlan> getHistoryTestPlans() {
		return historyTestPlans;
	}

	/**
	 * @param testPlans
	 *            the testPlans to set
	 */
	public void setHistoryTestPlans(Set<HistoryTestPlan> historyTestPlans) {
		this.historyTestPlans = historyTestPlans;
	}

	/**
	 * @return the testModuls
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@JoinColumn(name = "TERMINAL_ID")
	// @Fetch(FetchMode.SUBSELECT)
	// @OrderBy("channelsNo")
	@JSON(serialize = false)
	public List<TestModule> getTestModuls() {
		if (!CollectionUtils.isEmpty(this.testModuls)) {
			Collections.sort(this.testModuls);// 自然排序
		}
		return testModuls;
	}

	/**
	 * @param testModuls
	 *            the testModuls to set
	 */
	public void setTestModuls(List<TestModule> testModuls) {
		this.testModuls = testModuls;
	}

	/**
	 * @return the terminalGroup
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TERMINAL_GROUP_ID")
	@JSON(serialize = true)
	public TerminalGroup getTerminalGroup() {
		return terminalGroup;
	}

	/**
	 * @param terminalGroups
	 *            the terminalGroups to set
	 */
	public void setTerminalGroup(TerminalGroup terminalGroup) {
		this.terminalGroup = terminalGroup;
	}

	/**
	 * @return the monitorEntitys
	 */
	// yzp
	// @OneToMany(mappedBy = "terminal", fetch = FetchType.LAZY)
	// @Fetch(FetchMode.SUBSELECT)
	// // @Cascade( { CascadeType.DELETE, CascadeType.DELETE_ORPHAN })
	// public Set<MonitorEntity> getMonitorEntitys() {
	// return monitorEntitys;
	// }

	/**
	 * @param monitorEntitys
	 *            the monitorEntitys to set
	 */
	// yzp
	// public void setMonitorEntitys(Set<MonitorEntity> monitorEntitys) {
	// this.monitorEntitys = monitorEntitys;
	// }

	/**
	 * @return the gpsInfos
	 */
	// yzp
	// @OneToMany(mappedBy = "terminal", fetch = FetchType.LAZY)
	// @Fetch(FetchMode.SUBSELECT)
	// // @Cascade( { CascadeType.DELETE, CascadeType.DELETE_ORPHAN })
	// public Set<GPS> getGpsInfos() {
	// return gpsInfos;
	// }

	/**
	 * @param gpsInfos
	 *            the gpsInfos to set
	 */
	// yzp
	// public void setGpsInfos(Set<GPS> gpsInfos) {
	// this.gpsInfos = gpsInfos;
	// }

	// /**
	// * @return the gpsInfos
	// */
	// @OneToMany(mappedBy = "terminal", fetch = FetchType.LAZY)
	// @Fetch(FetchMode.SUBSELECT)
	// @Cascade( { CascadeType.DELETE, CascadeType.DELETE_ORPHAN })
	// public Set<HistorySoftUpgrade> getHistorySoftUpgrades() {
	// return historySoftUpgrades;
	// }
	//
	// /**
	// * @param gpsInfos the gpsInfos to set
	// */
	// public void setHistorySoftUpgrades(Set<HistorySoftUpgrade>
	// historySoftUpgrades) {
	// this.historySoftUpgrades = historySoftUpgrades;
	// }

	/**
	 * @return the name
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
	 * 
	 * @param testPlanVersion
	 */
	@Column(name = "TESTPLAN_VERSION")
	public void setTestPlanVersion(String testPlanVersion) {
		this.testPlanVersion = testPlanVersion;
	}

	/**
	 * 
	 * @return testPlanVersion
	 */
	public String getTestPlanVersion() {
		return testPlanVersion;
	}

	/**
	 * 添加测试模块
	 * 
	 * @param testModule
	 *            TestModule
	 */
	@Transient
	public void addTestModule(TestModule testModule) {
		// testModule.setTerminal(this);
		if (this.testModuls == null) {
			this.testModuls = new ArrayList<TestModule>();
		}
		this.testModuls.add(testModule);
		// Collections.sort(this.testModuls);
	}

	/**
	 * 添加历史计划
	 * 
	 * @param historyTestPlan
	 *            HistoryTestPlan
	 */
	@Transient
	public void addHistoryTestPlan(HistoryTestPlan historyTestPlan) {
		if (this.historyTestPlans == null) {
			this.historyTestPlans = new TreeSet<HistoryTestPlan>();
		}
		this.historyTestPlans.add(historyTestPlan);
	}

	/**
	 * 得到终端
	 * 
	 * @param msNo
	 *            终端编号
	 */
	@Transient
	public TestModule getTestModule(String msNo) {
		if (testModuls == null) {
			return null;
		}
		for (TestModule testModule : testModuls) {
			if (testModule == null) {
				continue;
			}
			int no = Integer.parseInt(msNo);
			if (no == testModule.getChannelsNo()) {
				return testModule;
			}
		}
		return null;
	}

	/**
	 * 得到当前终端的运行的历史测试计划
	 * 
	 * @return HistoryTestPlan
	 */
	@Transient
	public HistoryTestPlan getCurrentHistory() {
		if (!CollectionUtils.isEmpty(this.historyTestPlans)) {
			for (HistoryTestPlan plan : this.historyTestPlans) {
				if (plan.getTestPlanStatus() == TestPlanStatus.RUNNING) {
					return plan;
				}
			}
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Terminal o) {
		if (null != o) {
			return getBoxId().compareTo(o.getBoxId());
		}
		return 0;
	}

	@Column(name = "TESTTARGET")
	public Integer getTestTarget() {
		return testTarget;
	}

	public void setTestTarget(Integer testTarget) {
		this.testTarget = testTarget;
	}

	/**
	 * @return the testPlanFileLinktestPlanFileLink
	 */
	@Column(name = "TESTPLAN_FILELINK")
	public String getTestPlanFileLink() {
		return testPlanFileLink;
	}

	/**
	 * @param testPlanFileLink
	 *            the testPlanFileLink to set
	 */
	public void setTestPlanFileLink(String testPlanFileLink) {
		this.testPlanFileLink = testPlanFileLink;
	}
	
	//0自动LTE ,1单模块商务终端 ,2LTE-FI,3 5G单模块商务终端,4 PC测试软件
	@Transient
	public String getTestTargetStr() {
		if(testTarget == null){
			return "";
		}
		if(testTarget == 0){
			return "自动LTE";
		}else if(testTarget == 1){
			return "单模块商务终端";
		}else if(testTarget == 2){
			return "LTE-FI";
		}else if(testTarget == 3){
			return "5G单模块商务终端";
		}else if(testTarget == 4){
			return "PC测试软件";
		}
		return "";
	}

	public void setTestTargetStr(String testTargetStr) {
		this.testTargetStr = testTargetStr;
	}
	
	@Transient
	public String getOnlineStr() {
		if(online){
			return "在线";
		}else{
			return "离线";
		}
	}

	public void setOnlineStr(String onlineStr) {
		this.onlineStr = onlineStr;
	}



	private String longitude;
	private String latitude;
	private String fileName;
	private String exception;
	@Transient
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	@Transient
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@Transient
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Transient
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Terminal [id=" + id + ", boxId=" + boxId + ", password="
				+ password + ", name=" + name + ", manufacturer="
				+ manufacturer + ", hardwareVersion=" + hardwareVersion
				+ ", softwareVersion=" + softwareVersion + ", configVersion="
				+ configVersion + ", enable=" + enable + ", online=" + online
				+ ", remark=" + remark + ", produceDate=" + produceDate
				+ ", factoryDate=" + factoryDate + ", testPlanVersion="
				+ testPlanVersion + ", historyTestPlans=" + historyTestPlans
				+ ", testModuls=" + testModuls + ", terminalGroup="
				+ terminalGroup + ", testTarget=" + testTarget
				+ ", mountThread=" + mountThread + "]";
	}

}