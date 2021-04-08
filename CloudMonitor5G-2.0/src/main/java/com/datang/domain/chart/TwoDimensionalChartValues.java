/**
 * 
 */
package com.datang.domain.chart;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 二维图结果集
 * 
 * @author yinzhipeng
 * @date:2016年1月4日 下午2:33:33
 * @version
 */
@Entity
@IdClass(TwoDimensionalChartValues.class)
@Table(name = "IADS_TWO_DIMENSIONAL_RESULT")
public class TwoDimensionalChartValues implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5558879071149559406L;
	private Long qbrId;
	private TwoDimensionalChartConfig twoDimensionalChartConfig;
	private String xAxis;// 横坐标
	private String yAxis;// 纵坐标
	private Long value;// 值,个数

	/**
	 * @return the qbrIdqbrId
	 */
	@Id
	@Column(name = "RID")
	public Long getQbrId() {
		return qbrId;
	}

	/**
	 * @param qbrId
	 *            the qbrId to set
	 */
	public void setQbrId(Long qbrId) {
		this.qbrId = qbrId;
	}

	/**
	 * @return the twoDimensionalChartConfigtwoDimensionalChartConfig
	 */
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TYPEID")
	public TwoDimensionalChartConfig getTwoDimensionalChartConfig() {
		return twoDimensionalChartConfig;
	}

	/**
	 * @param twoDimensionalChartConfig
	 *            the twoDimensionalChartConfig to set
	 */
	public void setTwoDimensionalChartConfig(
			TwoDimensionalChartConfig twoDimensionalChartConfig) {
		this.twoDimensionalChartConfig = twoDimensionalChartConfig;
	}

	/**
	 * @return the xAxisxAxis
	 */
	@Column(name = "X_AXIS")
	public String getxAxis() {
		return xAxis;
	}

	/**
	 * @param xAxis
	 *            the xAxis to set
	 */
	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}

	/**
	 * @return the yAxisyAxis
	 */
	@Column(name = "Y_AXIS")
	public String getyAxis() {
		return yAxis;
	}

	/**
	 * @param yAxis
	 *            the yAxis to set
	 */
	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}

	/**
	 * @return the valuevalue
	 */
	@Column(name = "VALUE")
	public Long getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Long value) {
		this.value = value;
	}

}
