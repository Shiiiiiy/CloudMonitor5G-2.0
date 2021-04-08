package com.datang.domain.testPlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 测试单元配置
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:31:53
 * @version
 */
@SuppressWarnings("all")
@Entity
@Table(name = "IADS_TP_AUTO_TESTUNIT")
@XStreamAlias("AutoTestUnit")
public class AutoTestUnit implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 版本号
	 */
	@XStreamAlias("Version")
	private long version;

	/**
	 * 网络配置
	 */
	@XStreamAlias("Network")
	private NetWork netWork = new NetWork();
	/**
	 * 日志处理策略配置
	 */
	@XStreamAlias("LogProcess")
	private LogProcess logProcess = new LogProcess();
	/**
	 * 测试模块通用配置
	 */
	@XStreamAlias("GeneralItem")
	private GeneralItem generalItem = new GeneralItem();
	/**
	 * 测试信息
	 */
	@XStreamAlias("FPTestInfo")
	private String fpTestInfo;

	/**
	 * @return the version
	 */
	@Column(name = "VERSION")
	public long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the netWork
	 */
	@OneToOne
	@JoinColumn(name = "NETWORK_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public NetWork getNetWork() {
		return netWork;
	}

	/**
	 * @param netWork
	 *            the netWork to set
	 */
	public void setNetWork(NetWork netWork) {
		this.netWork = netWork;
	}

	/**
	 * @return the logProcess
	 */
	@OneToOne
	@JoinColumn(name = "LOG_PROCESS_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public LogProcess getLogProcess() {
		return logProcess;
	}

	/**
	 * @param logProcess
	 *            the logProcess to set
	 */
	public void setLogProcess(LogProcess logProcess) {
		this.logProcess = logProcess;
	}

	/**
	 * @return the generalItem
	 */
	@OneToOne
	@JoinColumn(name = "GENERAL_ITEM_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public GeneralItem getGeneralItem() {
		return generalItem;
	}

	/**
	 * @param generalItem
	 *            the generalItem to set
	 */
	public void setGeneralItem(GeneralItem generalItem) {
		this.generalItem = generalItem;
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

	@Column(name = "FPTESTINFO")
	public String getFpTestInfo() {
		return fpTestInfo;
	}

	public void setFpTestInfo(String fpTestInfo) {
		this.fpTestInfo = fpTestInfo;
	}

}
