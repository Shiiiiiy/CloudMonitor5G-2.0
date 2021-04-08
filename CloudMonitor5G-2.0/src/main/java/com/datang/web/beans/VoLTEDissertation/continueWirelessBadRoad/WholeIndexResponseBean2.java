/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.continueWirelessBadRoad;

import java.io.Serializable;

/**
 * 整体分析各类型连续无线差路段占比响应bean
 * 
 * @explain
 * @name WholeIndexResponseBean2
 * @author shenyanwei
 * @date 2016年5月31日下午3:53:34
 */
public class WholeIndexResponseBean2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6789879791636203875L;
	private Integer weakCover;
	private Integer disturb;
	private Integer nbCell;
	private Integer otherProblem;

	/**
	 * @return the weakCoverweakCover
	 */
	public Integer getWeakCover() {
		return weakCover;
	}

	/**
	 * @param weakCover
	 *            the weakCover to set
	 */
	public void setWeakCover(Integer weakCover) {
		this.weakCover = weakCover;
	}

	/**
	 * @return the disturbdisturb
	 */
	public Integer getDisturb() {
		return disturb;
	}

	/**
	 * @param disturb
	 *            the disturb to set
	 */
	public void setDisturb(Integer disturb) {
		this.disturb = disturb;
	}

	/**
	 * @return the nbCellnbCell
	 */
	public Integer getNbCell() {
		return nbCell;
	}

	/**
	 * @param nbCell
	 *            the nbCell to set
	 */
	public void setNbCell(Integer nbCell) {
		this.nbCell = nbCell;
	}

	/**
	 * @return the otherProblemotherProblem
	 */
	public Integer getOtherProblem() {
		return otherProblem;
	}

	/**
	 * @param otherProblem
	 *            the otherProblem to set
	 */
	public void setOtherProblem(Integer otherProblem) {
		this.otherProblem = otherProblem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WholeIndexResponseBean2 [weakCover=" + weakCover + ", disturb="
				+ disturb + ", nbCell=" + nbCell + ",  otherProblem="
				+ otherProblem + "]";
	}

}
