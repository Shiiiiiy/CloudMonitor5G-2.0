package com.datang.web.beans.VoLTEDissertation.callEstablishDelayException.whole;

import java.io.Serializable;

/**
 * 整体分析页面节点占比响应Bean
 * 
 * @explain
 * @name WholeBean
 * @author shenyanwei
 * @date 2016年5月24日下午4:15:35
 */
public class WholeBean1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4774310739467098431L;
	private Integer callIn; // VoLTE呼叫接入时延占比
	private Integer callRAB; // VoLTE呼叫RAB建立时延占比
	private Integer callFunld; // VOLTE呼叫被叫寻呼时延占比
	private Integer callMov; // VoLTE呼叫被叫媒体类型上报时延占比
	private Integer callMain; // VoLTE呼叫主叫振铃时延占比
	private Integer callOther; // 其他节点占比

	/**
	 * 
	 * @return
	 */
	public Integer getCallIn() {
		return callIn;
	}

	/**
	 * 
	 * @param callIn
	 */
	public void setCallIn(Integer callIn) {
		this.callIn = callIn;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getCallRAB() {
		return callRAB;
	}

	/**
	 * 
	 * @param callRAB
	 */
	public void setCallRAB(Integer callRAB) {
		this.callRAB = callRAB;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getCallFunld() {
		return callFunld;
	}

	/**
	 * 
	 * @param callFunld
	 */
	public void setCallFunld(Integer callFunld) {
		this.callFunld = callFunld;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getCallMov() {
		return callMov;
	}

	/**
	 * 
	 * @param callMov
	 */
	public void setCallMov(Integer callMov) {
		this.callMov = callMov;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getCallMain() {
		return callMain;
	}

	/**
	 * 
	 * @param callMain
	 */
	public void setCallMain(Integer callMain) {
		this.callMain = callMain;
	}

	/**
	 * 
	 * @return the callOther
	 */
	public Integer getCallOther() {
		return callOther;
	}

	/**
	 * 
	 * @param callOther
	 *            to set
	 */
	public void setCallOther(Integer callOther) {
		this.callOther = callOther;
	}

	/**
	 * to String
	 */
	@Override
	public String toString() {
		return "WholeBean1 [callIn=" + callIn + ", callRAB=" + callRAB
				+ ", callFunld=" + callFunld + ", callMov=" + callMov
				+ ", callMain=" + callMain + ", callOther=" + callOther + "]";
	}

}
