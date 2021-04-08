/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.qualityBadRoad;

import java.io.Serializable;

/**
 * 整体分析各类型质差路段占比响应bean
 * 
 * @author yinzhipeng
 * @date:2015年12月17日 上午10:48:54
 * @version
 */
public class WholeIndexResponseBean2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6789879791636203875L;
	private Integer weakCover;
	private Integer disturb;
	private Integer nbCell;
	private Integer paramError;
	private Integer coreNetwork;
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
	 * @return the paramErrorparamError
	 */
	public Integer getParamError() {
		return paramError;
	}

	/**
	 * @param paramError
	 *            the paramError to set
	 */
	public void setParamError(Integer paramError) {
		this.paramError = paramError;
	}

	/**
	 * @return the coreNetworkcoreNetwork
	 */
	public Integer getCoreNetwork() {
		return coreNetwork;
	}

	/**
	 * @param coreNetwork
	 *            the coreNetwork to set
	 */
	public void setCoreNetwork(Integer coreNetwork) {
		this.coreNetwork = coreNetwork;
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
				+ disturb + ", nbCell=" + nbCell + ", paramError=" + paramError
				+ ", coreNetwork=" + coreNetwork + ", otherProblem="
				+ otherProblem + "]";
	}

}
