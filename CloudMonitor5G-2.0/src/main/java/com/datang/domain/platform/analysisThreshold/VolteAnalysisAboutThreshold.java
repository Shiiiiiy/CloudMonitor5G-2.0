/**
 * 
 */
package com.datang.domain.platform.analysisThreshold;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.datang.domain.security.User;

/**
 * Volte专题门限分析实体
 * 
 * @author shenyanwei
 * @date 2016年4月28日下午12:42:14
 */
@Entity
@Table(name = "IADS_VOLTE_ANA_AB_THR")
public class VolteAnalysisAboutThreshold implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 941522808613396439L;
	/**
	 * 门限ID
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	/**
	 * 门限类别
	 */
	@Column(name = "THRESHOLD_TYPE")
	private String thresholdType;
	/**
	 * 当前门限值
	 */
	@Column(name = "VALUE")
	private String value;
	/**
	 * 用户ID
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "USER_Id", nullable = false)
	private User user = null;
	@Column(name = "NAME_EN")
	private String nameEn;

	/**
	 * @return the id
	 */
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
	 * @return the thresholdTypethresholdType
	 */
	@Column(name = "THRESHOLD_TYPE")
	public String getThresholdType() {
		return thresholdType;
	}

	/**
	 * @param thresholdType
	 *            the thresholdType to set
	 */
	public void setThresholdType(String thresholdType) {
		this.thresholdType = thresholdType;
	}

	/**
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * 无参构造
	 */
	public VolteAnalysisAboutThreshold() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 添加有参构造方法
	 * 
	 * @param id
	 * @param thresholdType
	 * @param value
	 * @param user
	 */
	public VolteAnalysisAboutThreshold(Long id, String thresholdType,
			String nameEn, String value, User user) {
		super();
		this.id = id;
		this.thresholdType = thresholdType;
		this.value = value;
		this.user = user;
		this.nameEn = nameEn;
	}

	/**
	 * toString
	 */
	@Override
	public String toString() {
		return "VolteAnalysisAboutThreshold [id=" + id + ", thresholdType="
				+ thresholdType + ", value=" + value + ",  nameEn=" + nameEn
				+ ",user=" + user + "]";
	}

}
