package com.datang.web.beans.VoLTEDissertation.callEstablishDelayException.whole;

import java.io.Serializable;

/**
 * 整体分析页面异常统计响应Bean
 * 
 * @explain
 * @name WholeBean2
 * @author shenyanwei
 * @date 2016年5月25日下午3:20:39
 */
public class WholeBean2 implements Serializable {

	private Integer CEDESum; // 呼叫建立时延次数
	private Long tryCallSum; // 尝试呼叫次数
	private Float housProportion; // 小区占比
	private Float terminalProportion; // 终端数占比

	public Integer getCEDESum() {
		return CEDESum;
	}

	public void setCEDESum(Integer cEDESum) {
		CEDESum = cEDESum;
	}

	public Long getTryCallSum() {
		return tryCallSum;
	}

	public void setTryCallSum(Long tryCallSum) {
		this.tryCallSum = tryCallSum;
	}

	public Float getHousProportion() {
		return housProportion;
	}

	public void setHousProportion(Float housProportion) {
		this.housProportion = housProportion;
	}

	public Float getTerminalProportion() {
		return terminalProportion;
	}

	public void setTerminalProportion(Float terminalProportion) {
		this.terminalProportion = terminalProportion;
	}

	@Override
	public String toString() {
		return "WholeBean2 [CEDESum=" + CEDESum + ", tryCallSum=" + tryCallSum
				+ ", housProportion=" + housProportion
				+ ", terminalProportion=" + terminalProportion + "]";
	}

}
