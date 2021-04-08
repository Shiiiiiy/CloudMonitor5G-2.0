/**
 * 
 */
package com.datang.domain.chart;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 一维图配置domain
 * 
 * @author yinzhipeng
 * @date:2015年12月30日 下午1:31:49
 * @version
 */
@Entity
@Table(name = "IADS_ONE_DIMENSIONAL")
public class OneDimensionalChartConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8822813351920897993L;
	private Long id;
	private String axisDefault;// 横坐标默认值
	private String axisCustomer;// 横坐标配置值
	private String chartType;// 图表类型

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
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
	 * @return the axisDefaultaxisDefault
	 */
	@Column(name = "AXIS_DEFAULT")
	public String getAxisDefault() {
		return axisDefault;
	}

	/**
	 * @param axisDefault
	 *            the axisDefault to set
	 */
	public void setAxisDefault(String axisDefault) {
		this.axisDefault = axisDefault;
	}

	/**
	 * @return the axisCustomeraxisCustomer
	 */
	@Column(name = "AXIS_CUSTOMER")
	public String getAxisCustomer() {
		return axisCustomer;
	}

	/**
	 * @param axisCustomer
	 *            the axisCustomer to set
	 */
	public void setAxisCustomer(String axisCustomer) {
		this.axisCustomer = axisCustomer;
	}

	/**
	 * @return the chartTypechartType
	 */
	@Column(name = "CHART_TYPE")
	public String getChartType() {
		return chartType;
	}

	/**
	 * @param chartType
	 *            the chartType to set
	 */
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

}
