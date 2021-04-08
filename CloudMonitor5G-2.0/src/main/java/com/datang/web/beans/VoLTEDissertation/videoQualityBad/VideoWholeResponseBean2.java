/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.videoQualityBad;

import java.io.Serializable;

/**
 * 视频质差整体分析视频质差问题路段汇总响应bean
 * 
 * @explain
 * @name VideoWholeResponseBean2
 * @author shenyanwei
 * @date 2017年5月15日下午2:52:30
 */
public class VideoWholeResponseBean2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7578303260569676004L;
	/**
	 * 视频质差问题数量
	 */
	private Integer roadNum;
	/**
	 * 问题路段VMOS采样点个数占比
	 */
	private Float vmosPointNumRatio;
	/**
	 * 视频质差问题中作为服务小区的小区个数占比
	 */
	private Float cellNumRatio;

	/**
	 * 终端数占比
	 */
	private Float terminalNumRatio;

	/**
	 * @return the roadNum
	 */
	public Integer getRoadNum() {
		return roadNum;
	}

	/**
	 * @param the
	 *            roadNum to set
	 */

	public void setRoadNum(Integer roadNum) {
		this.roadNum = roadNum;
	}

	/**
	 * @return the vmosPointNumRatio
	 */
	public Float getVmosPointNumRatio() {
		return vmosPointNumRatio;
	}

	/**
	 * @param the
	 *            vmosPointNumRatio to set
	 */

	public void setVmosPointNumRatio(Float vmosPointNumRatio) {
		this.vmosPointNumRatio = vmosPointNumRatio;
	}

	/**
	 * @return the cellNumRatio
	 */
	public Float getCellNumRatio() {
		return cellNumRatio;
	}

	/**
	 * @param the
	 *            cellNumRatio to set
	 */

	public void setCellNumRatio(Float cellNumRatio) {
		this.cellNumRatio = cellNumRatio;
	}

	/**
	 * @return the terminalNumRatio
	 */
	public Float getTerminalNumRatio() {
		return terminalNumRatio;
	}

	/**
	 * @param the
	 *            terminalNumRatio to set
	 */

	public void setTerminalNumRatio(Float terminalNumRatio) {
		this.terminalNumRatio = terminalNumRatio;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoWholeResponseBean2 [roadNum=" + roadNum
				+ ", vmosPointNumRatio=" + vmosPointNumRatio
				+ ", cellNumRatio=" + cellNumRatio + ", terminalNumRatio="
				+ terminalNumRatio + "]";
	}

}
