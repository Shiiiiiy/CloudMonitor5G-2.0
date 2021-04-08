/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.exceptionEvent;

import java.io.Serializable;

/**
 * 异常事件专题----异常事件统计响应bean
 * 
 * @author yinzhipeng
 * @date:2016年4月18日 下午4:20:25
 * @version
 */
public class ExceptionEventResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8333577801013235112L;
	/**
	 * 异常事件数量
	 */
	private Integer eventNum;
	/**
	 * 异常事件数量1
	 */
	private Integer eventNum1;
	/**
	 * 异常事件数量2
	 */
	private Integer eventNum2;

	/**
	 * 语音质差问题路段中作为服务小区的小区个数占比
	 */
	private Float cellNumRatio;

	/**
	 * 终端数占比
	 */
	private Float terminalNumRatio;

	/**
	 * @return the eventNumeventNum
	 */
	public Integer getEventNum() {
		return eventNum;
	}

	/**
	 * @param eventNum
	 *            the eventNum to set
	 */
	public void setEventNum(Integer eventNum) {
		this.eventNum = eventNum;
	}

	/**
	 * @return the eventNum1eventNum1
	 */
	public Integer getEventNum1() {
		return eventNum1;
	}

	/**
	 * @param eventNum1
	 *            the eventNum1 to set
	 */
	public void setEventNum1(Integer eventNum1) {
		this.eventNum1 = eventNum1;
	}

	/**
	 * @return the cellNumRatiocellNumRatio
	 */
	public Float getCellNumRatio() {
		return cellNumRatio;
	}

	/**
	 * @param cellNumRatio
	 *            the cellNumRatio to set
	 */
	public void setCellNumRatio(Float cellNumRatio) {
		this.cellNumRatio = cellNumRatio;
	}

	/**
	 * @return the terminalNumRatioterminalNumRatio
	 */
	public Float getTerminalNumRatio() {
		return terminalNumRatio;
	}

	/**
	 * @param terminalNumRatio
	 *            the terminalNumRatio to set
	 */
	public void setTerminalNumRatio(Float terminalNumRatio) {
		this.terminalNumRatio = terminalNumRatio;
	}

	/**
	 * @return the eventNum2eventNum2
	 */
	public Integer getEventNum2() {
		return eventNum2;
	}

	/**
	 * @param eventNum2
	 *            the eventNum2 to set
	 */
	public void setEventNum2(Integer eventNum2) {
		this.eventNum2 = eventNum2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExceptionEventResponseBean [eventNum=" + eventNum
				+ ", eventNum1=" + eventNum1 + ", eventNum2=" + eventNum2
				+ ", cellNumRatio=" + cellNumRatio + ", terminalNumRatio="
				+ terminalNumRatio + "]";
	}

}
