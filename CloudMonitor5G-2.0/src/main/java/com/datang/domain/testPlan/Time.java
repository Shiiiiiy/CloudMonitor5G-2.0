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
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:42:21
 * @version
 */
@SuppressWarnings("serial")
@XStreamAlias("Time")
@Entity
@Table(name = "IADS_TP_TIME")
public class Time implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 开始时间
	 */
	@XStreamAlias("BeginTime")
	private String beginTime;
	/**
	 * 结束时间
	 */
	@XStreamAlias("EndTime")
	private String endTime;

	/**
	 * @return the beginTime
	 */
	@Column(name = "BEGIN_TIME")
	public String getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime
	 *            the beginTime to set
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTime
	 */
	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
