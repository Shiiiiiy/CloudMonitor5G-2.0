package com.datang.web.beans.VoLTEDissertation.handoffDropping;

import java.io.Serializable;

/**
 * 切换失败响应Bean
 * @author shenyanwei
 * @date 2016年4月20日下午4:17:30
 */

public class DroppingBean implements Serializable {

	
	/**
	 * 异常事件数量
	 */
	private Integer eventNum;
	/**
	 * 异常事件数量1
	 */
	private Integer eventNum1;

	/**
	 * 小区个数占比
	 */
	private Float cellNumRatio;

	/**
	 * 终端数占比
	 */
	private Float terminalNumRatio;
     
	/**
	 * 
	 * @return the enentNum
	 */
	public Integer getEventNum() {
		return eventNum;
	}
	/**
	 * 
	 * @param eventNum
	 *                the enentNum to set
	 */
	public void setEventNum(Integer eventNum) {
		this.eventNum = eventNum;
	}
   /**
    * 
    * @return the eventNum1
    */
	public Integer getEventNum1() {
		return eventNum1;
	}
	/**
	 * 
	 * @param eventNum1  
	 *                  the eventNum1 to set
	 */
	public void setEventNum1(Integer eventNum1) {
		this.eventNum1 = eventNum1;
	}
    /**
     * 
     * @return the cellNumRatio
     */
	public Float getCellNumRatio() {
		return cellNumRatio;
	}
    /**
     * 
     * @param cellNumRatio 
     *                    the cellNumRatio to set
     */
	public void setCellNumRatio(Float cellNumRatio) {
		this.cellNumRatio = cellNumRatio;
	}
      /**
       * 
       * @return the terminalNumRatio
       */
	public Float getTerminalNumRatio() {
		return terminalNumRatio;
	}
    /**
     * 
     * @param terminalNumRatio 
     *                       the terminalNumRatio to set
     */
	public void setTerminalNumRatio(Float terminalNumRatio) {
		this.terminalNumRatio = terminalNumRatio;
	}
	
	/**
	 * see toString
	 */
	@Override
	public String toString() {
		return "DroppingBean [eventNum=" + eventNum + ", eventNum1="
				+ eventNum1 + ", cellNumRatio=" + cellNumRatio
				+ ", terminalNumRatio=" + terminalNumRatio + "]";
	}

	
	
}
