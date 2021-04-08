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
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:38:45
 * @version
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "IADS_TP_INFO")
@XStreamAlias("Info")
public class Info implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 测试类型
	 */
	@XStreamOmitField
	private String testType = "DT";

	/**
	 * @param testType
	 *            the testType to set
	 */
	public void setTestType(String testType) {
		this.testType = testType;
	}

	/**
	 * @return the testType
	 */
	@Column(name = "TEST_TYPE")
	public String getTestType() {
		return testType;
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
