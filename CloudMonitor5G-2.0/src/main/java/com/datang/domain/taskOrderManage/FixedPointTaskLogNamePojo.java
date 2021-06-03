package com.datang.domain.taskOrderManage;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.testManage.terminal.Terminal;
/**
 * 定点测试任务日志列表
 * @author lucheng
 * @date 2020年8月18日 下午2:52:14
 */
@Entity
@Table(name = "IADS_FIXEDPODIN_LOGNAME")
public class FixedPointTaskLogNamePojo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	/**
	 * 工单编号
	 */
	private String workOrderId;
	
	/**
	 * 测试日志名称
	 */
	private String testLogFileName;
	
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the workOrderId
	 */
	@Column(name = "WPRK_ORDER_ID")
	public String getWorkOrderId() {
		return workOrderId;
	}
	
	/**
	 * @param workOrderId the workOrderId to set
	 */
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
	
	/**
	 * @return the testLogFileName
	 */
	@Column(name = "TESTLOG_FILE_NAME")
	public String getTestLogFileName() {
		return testLogFileName;
	}
	
	/**
	 * @param testLogFileName the testLogFileName to set
	 */
	public void setTestLogFileName(String testLogFileName) {
		this.testLogFileName = testLogFileName;
	}
	
}
