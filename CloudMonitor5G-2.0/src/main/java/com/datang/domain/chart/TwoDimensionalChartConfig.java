/**
 * 
 */
package com.datang.domain.chart;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.common.util.StringUtils;

/**
 * 二维图配置domain
 * 
 * @author yinzhipeng
 * @date:2016年1月4日 下午2:27:17
 * @version
 */
@Entity
@Table(name = "IADS_TWO_DIMENSIONAL")
public class TwoDimensionalChartConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -90741379713636288L;
	private Long id;
	private String xAxis;// 横坐标
	private String yAxis;// 纵坐标
	private String chartType;// 图表类型

	/**
	 * 无参构造
	 */
	public TwoDimensionalChartConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 有参构造
	 * 
	 * @param id
	 * @param xAxis
	 * @param yAxis
	 * @param chartType
	 */
	public TwoDimensionalChartConfig(Long id, String xAxis, String yAxis,
			String chartType) {
		super();
		this.id = id;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.chartType = chartType;
	}

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
	 * @return the xAxisxAxis
	 */
	@Column(name = "X_AXIS")
	public String getxAxis() {
		return xAxis;
	}

	/**
	 * 获取x坐标linkedlist
	 * 
	 * @return
	 */
	@Transient
	public LinkedList<String> getxAxisList() {
		LinkedList<String> linkedList = new LinkedList<>();
		if (StringUtils.hasText(xAxis)) {
			String[] split = xAxis.trim().split("@");
			List<String> asList = Arrays.asList(split);
			linkedList.addAll(asList);
		}
		return linkedList;
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
	 * 获取y坐标linkedlist
	 * 
	 * @return
	 */
	@Transient
	public LinkedList<String> getyAxisList() {
		LinkedList<String> linkedList = new LinkedList<>();
		if (StringUtils.hasText(yAxis)) {
			String[] split = yAxis.trim().split("@");
			List<String> asList = Arrays.asList(split);
			linkedList.addAll(asList);
		}
		return linkedList;
	}

	/**
	 * @param yAxis
	 *            the yAxis to set
	 */
	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
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

	@Override
	public String toString() {
		return "TwoDimensionalChartConfig [id=" + id + ", xAxis=" + xAxis
				+ ", yAxis=" + yAxis + ", chartType=" + chartType + "]";
	}

}
