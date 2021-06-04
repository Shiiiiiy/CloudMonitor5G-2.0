package com.datang.domain.testPlan;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.datang.util.DateUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 测试计划
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:41:18
 * @version
 */
@SuppressWarnings("all")
@XStreamAlias("root")
@Entity
@Table(name = "IADS_TP_TEST_PLAN")
public class TestPlan implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;

	/**
	 * 终端id
	 */
	@XStreamOmitField
	private Long terminalId;
	
	/**
	 * 终端的BOXID
	 */
	@XStreamAsAttribute
	private String boxId;

	@XStreamAlias("AutoTestUnit")
	private AutoTestUnit autoTestUnit = new AutoTestUnit();
	@XStreamOmitField
	private List<TestScheme> testSuit = new ArrayList<TestScheme>();

	@XStreamAlias("TestUnit")
	private TestUnit testUnit = new TestUnit();
	/**
	 * 名称
	 */
	@XStreamOmitField
	private String name = DateUtil.getCurDateStr("yyyyMMddHHmmss");
	/**
	 * 实际下发时间
	 */
	@XStreamOmitField
	private Date sendDate = null;
	/**
	 * 过期时间
	 */
	@XStreamOmitField
	private Date loseDate = null;
	/**
	 * 预计下发时间
	 */
	@XStreamOmitField
	private String planSendDate = "2100-01-01 00:00:00";
	/**
	 * 描述
	 */
	@XStreamOmitField
	private String description;
	/**
	 * 测试级别:organizationCheck组织巡检,dailyOptimiz日常优化 ,deviceDebug设备调试
	 * 
	 */
	@XStreamOmitField
	private String level = "dailyOptimiz"; // 默认选中日常优化

	/**
	 * 是否下发过
	 */
	@XStreamOmitField
	private boolean sended;
	/**
	 * 创建时间
	 */
	@XStreamOmitField
	private Date createDate = new Date();

	/**
	 * @return the autoTestUnit
	 */
	@OneToOne
	@JoinColumn(name = "AUTO_TESTUNIT_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public AutoTestUnit getAutoTestUnit() {
		return autoTestUnit;
	}

	/**
	 * @param autoTestUnit
	 *            the autoTestUnit to set
	 */
	public void setAutoTestUnit(AutoTestUnit autoTestUnit) {
		this.autoTestUnit = autoTestUnit;
	}

	/**
	 * @return the testSuit
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "TEST_PLAN_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@Fetch(FetchMode.SUBSELECT)
	public List<TestScheme> getTestSuit() {
		return testSuit;
	}

	/**
	 * @param testSuit
	 *            the testSuit to set
	 */
	public void setTestSuit(List<TestScheme> testSuit) {
		this.testSuit = testSuit;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
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

	/**
	 * @return the terminalIdterminalId
	 */
	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	/**
	 * @param terminalId
	 *            the terminalId to set
	 */
	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

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

	@Column(name = "LOSE_DATE")
	public Date getLoseDate() {
		return loseDate;
	}

	public void setLoseDate(Date loseDate) {
		this.loseDate = loseDate;
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
	 * @return the description
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
	 * 
	 * @return
	 */
	@Column(name = "TEST_LEVEL")
	public String getLevel() {
		return level;
	}

	/**
	 * 
	 * @param level
	 */
	public void setLevel(String level) {
		if (level != null) {
			if (level.equals("1")) {
				this.level = "organizationCheck";
			} else if (level.equals("2")) {
				this.level = "dailyOptimiz";
			} else if (level.equals("3")) {
				this.level = "deviceDebug";
			} else {
				this.level = level;
			}
		}
	}

	/**
	 * @return the sended
	 */
	@Column(name = "SENDED", columnDefinition = "boolean")
	public boolean isSended() {
		return sended;
	}

	/**
	 * @param sended
	 *            the sended to set
	 */
	public void setSended(boolean sended) {
		this.sended = sended;
	}

	/**
	 * @return the createDate
	 */
	@Column(name = "CREATE_DATE")
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

	@Transient
	public TestUnit getTestUnit() {
		return testUnit;
	}

	/**
	 * @return the planSendDateplanSendDate
	 */
	@Column(name = "PLAN_SENDDATE")
	public String getPlanSendDate() {
		return planSendDate;
	}

	/**
	 * @param planSendDate
	 *            the planSendDate to set
	 */
	public void setPlanSendDate(String planSendDate) {
		this.planSendDate = planSendDate;
	}

	@Transient
	public String getSendTime() {
		if (null != sendDate) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sendDate);
		}
		return null;
	}
	
	@Transient
	public String getLoseTime() {
		if (null != loseDate) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loseDate);
		}
		return null;
	}

	public void setTestUnit(TestUnit testUnit) {
		testUnit.setTestSuit(testSuit);
		this.testUnit = testUnit;
	}
	

	/**
	 * @return the boxId
	 */
	@Transient
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param boxId the boxId to set
	 */
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * 通过testSchemeName查TestScheme
	 * 
	 * @param testSuitName
	 *            String
	 * @return TestScheme
	 */
	public TestScheme findTestScheme(String testSuitName) {
		for (TestScheme testScheme : this.testSuit) {
			if ((testSuitName == null && testScheme.getName() == null)
					|| (testSuitName != null && testSuitName.equals(testScheme
							.getName()))) {
				return testScheme;
			}
		}
		return null;
	}

	/**
	 * 通过testSchemeName查TestScheme
	 * 
	 * @param testSuitName
	 *            String
	 * @return TestScheme
	 */
	public TestScheme findTestSchemeByMsNo(String msNo) {
		for (TestScheme testScheme : this.testSuit) {
			if ((msNo == null && testScheme.getMsNo() == null)
					|| (msNo != null && msNo.equals(testScheme.getMsNo()))) {
				return testScheme;
			}
		}
		return null;
	}

}
