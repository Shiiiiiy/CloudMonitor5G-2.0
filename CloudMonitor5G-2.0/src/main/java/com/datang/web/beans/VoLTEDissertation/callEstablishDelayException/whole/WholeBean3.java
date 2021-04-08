package com.datang.web.beans.VoLTEDissertation.callEstablishDelayException.whole;

import java.io.Serializable;

/**
 * 整体分析页面异常原因占比
 * 
 * @explain
 * @name WholeBean3
 * @author shenyanwei
 * @date 2016年5月25日下午3:22:07
 */
public class WholeBean3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7389130372202380599L;
	private Integer weakCover;
	private Integer overlapCover;
	private Integer coreNet;
	private Integer locationUpdate;
	private Integer other;

	public Integer getWeakCover() {
		return weakCover;
	}

	public void setWeakCover(Integer weakCover) {
		this.weakCover = weakCover;
	}

	public Integer getOverlapCover() {
		return overlapCover;
	}

	public void setOverlapCover(Integer overlapCover) {
		this.overlapCover = overlapCover;
	}

	public Integer getCoreNet() {
		return coreNet;
	}

	public void setCoreNet(Integer coreNet) {
		this.coreNet = coreNet;
	}

	public Integer getLocationUpdate() {
		return locationUpdate;
	}

	public void setLocationUpdate(Integer locationUpdate) {
		this.locationUpdate = locationUpdate;
	}

	public Integer getOther() {
		return other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}

	@Override
	public String toString() {
		return "WholeBean3 [weakCover=" + weakCover + ", overlapCover="
				+ overlapCover + ", coreNet=" + coreNet + ", locationUpdate="
				+ locationUpdate + ", other=" + other + "]";
	}

}
