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
 * 测试模块通用配置
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:38:02
 * @version
 */
@SuppressWarnings("serial")
@XStreamAlias("GeneralItem")
@Entity
@Table(name = "IADS_TP_GENERAL_ITEM")
public class GeneralItem implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 测试方案是否受速度限制
	 */
	@XStreamAlias("SpeedCondition")
	private String speedCondition;
	/**
	 * 车辆最大速度
	 */
	@XStreamAlias("MaxSpeed")
	private String maxSpeed = "100";
	/**
	 * 超速行驶多长时间后停止测试
	 */
	@XStreamAlias("MaxSpeedTime")
	private String maxSpeedTime = "90";
	/**
	 * 车辆行驶最小速度
	 */
	@XStreamAlias("MinSpeed")
	private String minSpeed = "10";
	/**
	 * 低于最小速度多长时间后停止测试
	 */
	@XStreamAlias("MinSpeedTime")
	private String minSpeedTime = "30";
	/**
	 * GPS条件项目
	 */
	@XStreamAlias("GPSCondition")
	private String gpsCondition;
	/**
	 * 左上角经度
	 */
	@XStreamAlias("LeftTopLon")
	private String leftTopLon;
	/**
	 * 左上角纬度
	 */
	@XStreamAlias("LeftTopLat")
	private String leftTopLat;
	/**
	 * 右下角经度
	 */
	@XStreamAlias("RightBottomLon")
	private String rightBottomLon;
	/**
	 * 左上角纬度
	 */
	@XStreamAlias("RightBottomLat")
	private String rightBottomLat;
	/**
	 * 测试目标
	 */
	@XStreamAlias("TestTarget")
	private String testTarget = "DT";
	/**
	 * 测试点
	 */
	@XStreamAlias("TestPoint")
	private String testPoint;
	/**
	 * 测试类型
	 */
	@XStreamAlias("TestType")
	private String testType;

	/**
	 * @return the speedCondition
	 */
	@Column(name = "SPEED_CONDITION")
	public String getSpeedCondition() {
		return speedCondition;
	}

	/**
	 * @param speedCondition
	 *            the speedCondition to set
	 */
	public void setSpeedCondition(String speedCondition) {
		this.speedCondition = speedCondition;
	}

	/**
	 * @return the gpsCondition
	 */
	@Column(name = "GPS_CONDITION")
	public String getGpsCondition() {
		return gpsCondition;
	}

	/**
	 * @param gpsCondition
	 *            the gpsCondition to set
	 */
	public void setGpsCondition(String gpsCondition) {
		this.gpsCondition = gpsCondition;
	}

	/**
	 * @return the leftTopLon
	 */
	@Column(name = "LEFT_TOP_LON")
	public String getLeftTopLon() {
		return leftTopLon;
	}

	/**
	 * @param leftTopLon
	 *            the leftTopLon to set
	 */
	public void setLeftTopLon(String leftTopLon) {
		this.leftTopLon = leftTopLon;
	}

	/**
	 * @return the leftTopLat
	 */
	@Column(name = "LEFT_TOP_LAT")
	public String getLeftTopLat() {
		return leftTopLat;
	}

	/**
	 * @param leftTopLat
	 *            the leftTopLat to set
	 */
	public void setLeftTopLat(String leftTopLat) {
		this.leftTopLat = leftTopLat;
	}

	/**
	 * @return the rightBottomLon
	 */
	@Column(name = "RIGHT_BOTTOM_LON")
	public String getRightBottomLon() {
		return rightBottomLon;
	}

	/**
	 * @param rightBottomLon
	 *            the rightBottomLon to set
	 */
	public void setRightBottomLon(String rightBottomLon) {
		this.rightBottomLon = rightBottomLon;
	}

	/**
	 * @return the rightBottomLat
	 */
	@Column(name = "RIGHT_BOTTOM_LAT")
	public String getRightBottomLat() {
		return rightBottomLat;
	}

	/**
	 * @param rightBottomLat
	 *            the rightBottomLat to set
	 */
	public void setRightBottomLat(String rightBottomLat) {
		this.rightBottomLat = rightBottomLat;
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

	@Column(name = "MAX_SPEED")
	public String getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Column(name = "MAX_SPEED_TIME")
	public String getMaxSpeedTime() {
		return maxSpeedTime;
	}

	public void setMaxSpeedTime(String maxSpeedTime) {
		this.maxSpeedTime = maxSpeedTime;
	}

	@Column(name = "MIN_SPEED")
	public String getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(String minSpeed) {
		this.minSpeed = minSpeed;
	}

	@Column(name = "MIN_SPEED_TIME")
	public String getMinSpeedTime() {
		return minSpeedTime;
	}

	public void setMinSpeedTime(String minSpeedTime) {
		this.minSpeedTime = minSpeedTime;
	}

	@Column(name = "TEST_TARGET")
	public String getTestTarget() {

		return testTarget;
	}

	public void setTestTarget(String testTarget) {
		this.testTarget = testTarget;
	}

	@Column(name = "TEST_POINT")
	public String getTestPoint() {
		return testPoint;
	}

	public void setTestPoint(String testPoint) {
		this.testPoint = testPoint;
	}

	@Column(name = "TEST_TYPE")
	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		if (testType != null) {
			if (testType.equals("organizationCheck")) {
				this.testType = "1";
			} else if (testType.equals("dailyOptimiz")) {
				this.testType = "2";
			} else if (testType.equals("deviceDebug")) {
				this.testType = "3";
			} else {
				this.testType = testType;
			}
		} else {
			this.testType = null;
		}
	}

}
