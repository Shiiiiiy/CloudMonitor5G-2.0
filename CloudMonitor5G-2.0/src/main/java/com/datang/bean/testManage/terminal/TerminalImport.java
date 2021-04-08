package com.datang.bean.testManage.terminal;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端导入
 * 
 * @explain
 * @name TerminalImport
 * @author shenyanwei
 * @date 2017年10月19日下午3:10:06
 */
public class TerminalImport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 一级域
	 */
	private String provinceGroup;
	/**
	 * 二级域
	 */
	private String cityGroup;
	/**
	 * 终端名称
	 */
	private String name;
	/**
	 * BOXID
	 */
	private String boxId;
	/**
	 * 终端类型
	 */
	private String testTarget;
	/**
	 * 软件版本
	 */
	private String softwareVersion = "";
	/**
	 * 硬件版本
	 */
	private String hardwareVersion = "";
	/**
	 * 测试计划版本
	 */
	private String testPlanVersion = "";
	/**
	 * 生产日期
	 */
	private Date produceDate;

	/**
	 * 出厂日期
	 */
	private Date factoryDate;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 厂商
	 */
	private String manufacturer;
	/**
	 * 是否可用
	 */
	private String enable;

	/**
	 * @return the provinceGroup
	 */
	public String getProvinceGroup() {
		return provinceGroup;
	}

	/**
	 * @param the
	 *            provinceGroup to set
	 */

	public void setProvinceGroup(String provinceGroup) {
		this.provinceGroup = provinceGroup;
	}

	/**
	 * @return the cityGroup
	 */
	public String getCityGroup() {
		return cityGroup;
	}

	/**
	 * @param the
	 *            cityGroup to set
	 */

	public void setCityGroup(String cityGroup) {
		this.cityGroup = cityGroup;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param the
	 *            name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the boxId
	 */
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param the
	 *            boxId to set
	 */

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @return the testTarget
	 */
	public String getTestTarget() {
		return testTarget;
	}

	/**
	 * @param the
	 *            testTarget to set
	 */

	public void setTestTarget(String testTarget) {
		this.testTarget = testTarget;
	}

	/**
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * @param the
	 *            softwareVersion to set
	 */

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * @return the hardwareVersion
	 */
	public String getHardwareVersion() {
		return hardwareVersion;
	}

	/**
	 * @param the
	 *            hardwareVersion to set
	 */

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	/**
	 * @return the testPlanVersion
	 */
	public String getTestPlanVersion() {
		return testPlanVersion;
	}

	/**
	 * @param the
	 *            testPlanVersion to set
	 */

	public void setTestPlanVersion(String testPlanVersion) {
		this.testPlanVersion = testPlanVersion;
	}

	/**
	 * @return the produceDate
	 */
	public Date getProduceDate() {
		return produceDate;
	}

	/**
	 * @param the
	 *            produceDate to set
	 */

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	/**
	 * @return the factoryDate
	 */
	public Date getFactoryDate() {
		return factoryDate;
	}

	/**
	 * @param the
	 *            factoryDate to set
	 */

	public void setFactoryDate(Date factoryDate) {
		this.factoryDate = factoryDate;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param the
	 *            remark to set
	 */

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param the
	 *            manufacturer to set
	 */

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the enable
	 */
	public String getEnable() {
		return enable;
	}

	/**
	 * @param the
	 *            enable to set
	 */

	public void setEnable(String enable) {
		this.enable = enable;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TerminalImport [provinceGroup=" + provinceGroup
				+ ", cityGroup=" + cityGroup + ", name=" + name + ", boxId="
				+ boxId + ", testTarget=" + testTarget + ", softwareVersion="
				+ softwareVersion + ", hardwareVersion=" + hardwareVersion
				+ ", testPlanVersion=" + testPlanVersion + ", produceDate="
				+ produceDate + ", factoryDate=" + factoryDate + ", remark="
				+ remark + ", manufacturer=" + manufacturer + ", enable="
				+ enable + "]";
	}

}
