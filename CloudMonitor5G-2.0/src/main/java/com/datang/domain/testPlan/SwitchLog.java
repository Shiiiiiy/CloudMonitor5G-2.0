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
 * @date:2016年7月8日 下午1:40:27
 * @version
 */
@SuppressWarnings("serial")
@XStreamAlias("SwitchLog")
@Entity
@Table(name = "IADS_TP_SWITH_LOG")
public class SwitchLog implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 是否切换，当值为0时其他参数无意义 0不需要切换LOG文件，测试时只生成一个LOG文件 1需要切换LOG文件
	 */
	@XStreamAlias("Enable")
	private String enable = "1";
	/**
	 * 0按固定测试时间切换测试LOG，1按固定文件大小 切换测试LOG
	 */
	@XStreamAlias("Type")
	private String type = "0";

	/**
	 * 文件大小
	 */
	@XStreamAlias("FileSize")
	private String pfileSize = "5120";

	/**
	 * 测试时间，单位分钟
	 */
	@XStreamAlias("TestTime")
	private String testTime = "60";
	/**
	 * 0表示当条件满足时强行切换测试LOG文件， 如果当前测试任务没有完成，就强行中断，并产生新的测试log文件
	 * 1表示当条件满足时，如果当前测试任务没有完成，就需要等待任务完成后才能产生新的测试log文件
	 */
	@XStreamAlias("Condition")
	private String condition = "1";

	/**
	 * @return the enable
	 */
	@Column(name = "ENABLE")
	public String getEnable() {
		return enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the testTime
	 */
	@Column(name = "TEST_TIME")
	public String getTestTime() {
		return testTime;
	}

	/**
	 * @param testTime
	 *            the testTime to set
	 */
	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}

	/**
	 * @return the condition
	 */
	@Column(name = "CONDITION")
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
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

	/**
	 * @return the pfileSize
	 */
	@Column(name = "FILE_SIZE")
	public String getPfileSize() {
		return pfileSize;
	}

	/**
	 * @param pfileSize
	 *            the pfileSize to set
	 */
	public void setPfileSize(String pfileSize) {
		this.pfileSize = pfileSize;
	}
}
