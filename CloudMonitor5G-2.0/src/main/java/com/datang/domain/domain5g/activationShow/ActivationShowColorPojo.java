package com.datang.domain.domain5g.activationShow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ActivationShowColorPojo {
	
	@Value("${mostActiveColor}")
	private String mostActiveColor;
	
	@Value("${mediumActiveColor}")
	private String mediumActiveColor;
	
	@Value("${leastActiveColor}")
	private String leastActiveColor;

	/**
	 * 高活跃度颜色
	 */
	public String getMostActiveColor() {
		return mostActiveColor;
	}
	/**
	 * 高活跃度颜色
	 */
	public void setMostActiveColor(String mostActiveColor) {
		this.mostActiveColor = mostActiveColor;
	}
	/**
	 * 中度活跃度颜色
	 */
	public String getMediumActiveColor() {
		return mediumActiveColor;
	}
	/**
	 * 中度活跃度颜色
	 */
	public void setMediumActiveColor(String mediumActiveColor) {
		this.mediumActiveColor = mediumActiveColor;
	}
	/**
	 * 低度活跃度颜色
	 */
	public String getLeastActiveColor() {
		return leastActiveColor;
	}
	/**
	 * 低度活跃度颜色
	 */
	public void setLeastActiveColor(String leastActiveColor) {
		this.leastActiveColor = leastActiveColor;
	}

}
