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
 * 文件处理策略
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:39:15
 * @version
 */
@Entity
@Table(name = "IADS_TP_LOG_PROCESS")
@SuppressWarnings("all")
@XStreamAlias("LogProcess")
public class LogProcess implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	@XStreamAlias("SwitchLog")
	private SwitchLog switchLog = new SwitchLog();

	/**
	 * @return the switchLog
	 */
	@OneToOne
	@JoinColumn(name = "SWITCH_LOG_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public SwitchLog getSwitchLog() {
		return switchLog;
	}

	/**
	 * @param switchLog
	 *            the switchLog to set
	 */
	public void setSwitchLog(SwitchLog switchLog) {
		this.switchLog = switchLog;
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
