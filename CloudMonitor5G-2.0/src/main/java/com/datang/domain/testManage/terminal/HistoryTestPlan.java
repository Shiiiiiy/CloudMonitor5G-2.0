package com.datang.domain.testManage.terminal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 终端执行过的测试计划
 * 
 * @author songzhigang
 * 
 */
// @Entity
// @Table(name = "IADS_HISTORY_TESTPLAN")
public class HistoryTestPlan implements Serializable,
		Comparable<HistoryTestPlan> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7996929215854278337L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 测试计划版本
	 */
	private Long version;

	/**
	 * 历史计划的名字
	 */
	private String planName;

	/**
	 * 测试级别
	 */
	private String testLevel;

	/**
	 * 测试计划Id
	 */
	private Integer testPlanId;

	/**
	 * 测试计划的执行状态
	 */
	private TestPlanStatus testPlanStatus;

	/**
	 * 测试计划的下发时间
	 */
	private Date sendDate;

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
	 * @return the testPlanStatus
	 */
	@Column(name = "STATUS")
	public TestPlanStatus getTestPlanStatus() {
		return testPlanStatus;
	}

	/**
	 * @param testPlanStatus
	 *            the testPlanStatus to set
	 */
	public void setTestPlanStatus(TestPlanStatus testPlanStatus) {
		this.testPlanStatus = testPlanStatus;
	}

	/**
	 * @return the sendDate
	 */
	@Column(name = "SEND_DATE")
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate
	 *            the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the testPlanId
	 */
	@Column(name = "TESTPLAN_ID")
	public Integer getTestPlanId() {
		return testPlanId;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	@Column(name = "VERSION")
	public Long getVersion() {
		return version;
	}

	/**
	 * @param planName
	 *            the planName to set
	 */
	public void setPlanName(String planName) {
		this.planName = planName;
	}

	/**
	 * @return the planName
	 */
	@Column(name = "PLAN_NAME")
	public String getPlanName() {
		return planName;
	}

	/**
	 * @param testLevel
	 *            the testLevel to set
	 */
	public void setTestLevel(String testLevel) {
		this.testLevel = testLevel;
	}

	/**
	 * @return the testLevel
	 */
	@Column(name = "TEST_LEVEL")
	public String getTestLevel() {
		return testLevel;
	}

	/**
	 * @param testPlanId
	 *            the testPlanId to set
	 */
	public void setTestPlanId(Integer testPlanId) {
		this.testPlanId = testPlanId;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof HistoryTestPlan)) {
			return false;
		}
		HistoryTestPlan tm = (HistoryTestPlan) o;
		return tm.getSendDate() == getSendDate();
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + getSendDate().hashCode();
		return result;

	}

	@Override
	public int compareTo(HistoryTestPlan o) {
		if (null != o) {
			return this.compareTo(o);
		}
		return 0;
	}

}
