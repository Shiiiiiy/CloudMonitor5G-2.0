/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.continueWirelessBadRoad;

import java.io.Serializable;

/**
 * 整体分析连续无线差路段统计响应Bean
 * 
 * @explain
 * @name WholeIndexResponseBean1
 * @author shenyanwei
 * @date 2016年5月31日下午3:50:59
 */
public class WholeIndexResponseBean1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -653299120708651568L;
	/**
	 * 连续无线差问题数量
	 */
	private Integer roadNum;
	/**
	 * 问题路段里程:单位m
	 */
	private Float distance;
	/**
	 * 问题路段持续测试时间:ms
	 */
	private Long continueTime;
	/**
	 * 连续无线差问题路段中作为服务小区的小区个数占比
	 */
	private Float cellNumRatio;

	/**
	 * 终端数占比
	 */
	private Float terminalNumRatio;

	/**
	 * @return the roadNumroadNum
	 */
	public Integer getRoadNum() {
		return roadNum;
	}

	/**
	 * @param roadNum
	 *            the roadNum to set
	 */
	public void setRoadNum(Integer roadNum) {
		this.roadNum = roadNum;
	}

	/**
	 * @return the distancedistance
	 */
	public Float getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
	}

	/**
	 * @return the continueTimecontinueTime
	 */
	public Long getContinueTime() {
		return continueTime;
	}

	/**
	 * @param continueTime
	 *            the continueTime to set
	 */
	public void setContinueTime(Long continueTime) {
		this.continueTime = continueTime;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WholeIndexResponseBean1 [roadNum=" + roadNum + ", distance="
				+ distance + ", continueTime=" + continueTime
				+ ",  cellNumRatio=" + cellNumRatio + ", terminalNumRatio="
				+ terminalNumRatio + "]";
	}

}
