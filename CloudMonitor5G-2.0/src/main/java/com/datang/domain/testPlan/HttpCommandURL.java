package com.datang.domain.testPlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * HttpCommand测试业务URL
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:38:28
 * @version
 */
@Entity
@Table(name = "IADS_TP_HTTPCOMMAND_URL")
@SuppressWarnings("serial")
public class HttpCommandURL implements Serializable {

	private Integer id;// 主键
	private String urlName;// 名称
	private String url;// 地址
	private String isUrlMust;// 是否是必选地址
	private Boolean selected = false;// 是否选中

	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "URL_NAME")
	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "IS_URLMUST")
	@JSON(serialize = false)
	public String getIsUrlMust() {
		return isUrlMust;
	}

	public void setIsUrlMust(String isUrlMust) {
		this.isUrlMust = isUrlMust;
	}

	/**
	 * @return the selectedselected
	 */
	@Transient
	public Boolean getSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

}
