/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.videoQualityBad;

import java.io.Serializable;

/**
 * 视频质差整体分析视频质差关键参数响应bean
 * 
 * @explain
 * @name VideoWholeResponseBean3
 * @author shenyanwei
 * @date 2017年5月15日下午5:19:17
 */
public class VideoWholeResponseBean3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3643953323268902477L;
	/**
	 * 视频质差问题原因
	 */
	private String cause;
	/**
	 * 乒乓切换个数
	 */
	private Integer pingPongNum = 0;
	/**
	 * 乒乓切换占比
	 */
	private Float pingPongRatio;
	/**
	 * 邻区缺失个数
	 */
	private Integer adjacentNum = 0;
	/**
	 * 邻区缺失占比
	 */
	private Float adjacentRatio;
	/**
	 * 弱覆盖个数
	 */
	private Integer weakCoverNum = 0;
	/**
	 * 弱覆盖占比
	 */
	private Float weakCoverRatio;
	/**
	 * 干扰个数
	 */
	private Integer disturbNum = 0;
	/**
	 * 干扰占比
	 */
	private Float disturbRatio;
	/**
	 * 重叠覆盖个数
	 */
	private Integer overCoverNum = 0;
	/**
	 * 重叠覆盖占比
	 */
	private Float overCoverRatio;
	/**
	 * 模式转换个数
	 */
	private Integer patternSwitchNum = 0;
	/**
	 * 模式转换占比
	 */
	private Float patternSwitchRatio;
	/**
	 * 下行调度数小个数
	 */
	private Integer downDispatchSmallNum = 0;
	/**
	 * 下行调度数小占比
	 */
	private Float downDispatchSmallRatio;
	/**
	 * 其他个数
	 */
	private Integer otherNum = 0;
	/**
	 * 其他占比
	 */
	private Float otherRatio;
	/**
	 * 汇总个数
	 */
	private Integer collectNum = 0;
	/**
	 * 汇总占比
	 */
	private Float collectRatio;

	/**
	 * @return the cause
	 */
	public String getCause() {
		return cause;
	}

	/**
	 * @param the
	 *            cause to set
	 */

	public void setCause(String cause) {
		this.cause = cause;
	}

	/**
	 * @return the pingPongNum
	 */
	public Integer getPingPongNum() {
		return pingPongNum;
	}

	/**
	 * @param the
	 *            pingPongNum to set
	 */

	public void setPingPongNum(Integer pingPongNum) {
		this.pingPongNum = pingPongNum;
	}

	/**
	 * @return the pingPongRatio
	 */
	public Float getPingPongRatio() {
		return pingPongRatio;
	}

	/**
	 * @param the
	 *            pingPongRatio to set
	 */

	public void setPingPongRatio(Float pingPongRatio) {
		this.pingPongRatio = pingPongRatio;
	}

	/**
	 * @return the adjacentNum
	 */
	public Integer getAdjacentNum() {
		return adjacentNum;
	}

	/**
	 * @param the
	 *            adjacentNum to set
	 */

	public void setAdjacentNum(Integer adjacentNum) {
		this.adjacentNum = adjacentNum;
	}

	/**
	 * @return the adjacentRatio
	 */
	public Float getAdjacentRatio() {
		return adjacentRatio;
	}

	/**
	 * @param the
	 *            adjacentRatio to set
	 */

	public void setAdjacentRatio(Float adjacentRatio) {
		this.adjacentRatio = adjacentRatio;
	}

	/**
	 * @return the weakCoverNum
	 */
	public Integer getWeakCoverNum() {
		return weakCoverNum;
	}

	/**
	 * @param the
	 *            weakCoverNum to set
	 */

	public void setWeakCoverNum(Integer weakCoverNum) {
		this.weakCoverNum = weakCoverNum;
	}

	/**
	 * @return the weakCoverRatio
	 */
	public Float getWeakCoverRatio() {
		return weakCoverRatio;
	}

	/**
	 * @param the
	 *            weakCoverRatio to set
	 */

	public void setWeakCoverRatio(Float weakCoverRatio) {
		this.weakCoverRatio = weakCoverRatio;
	}

	/**
	 * @return the disturbNum
	 */
	public Integer getDisturbNum() {
		return disturbNum;
	}

	/**
	 * @param the
	 *            disturbNum to set
	 */

	public void setDisturbNum(Integer disturbNum) {
		this.disturbNum = disturbNum;
	}

	/**
	 * @return the disturbRatio
	 */
	public Float getDisturbRatio() {
		return disturbRatio;
	}

	/**
	 * @param the
	 *            disturbRatio to set
	 */

	public void setDisturbRatio(Float disturbRatio) {
		this.disturbRatio = disturbRatio;
	}

	/**
	 * @return the overCoverNum
	 */
	public Integer getOverCoverNum() {
		return overCoverNum;
	}

	/**
	 * @param the
	 *            overCoverNum to set
	 */

	public void setOverCoverNum(Integer overCoverNum) {
		this.overCoverNum = overCoverNum;
	}

	/**
	 * @return the overCoverRatio
	 */
	public Float getOverCoverRatio() {
		return overCoverRatio;
	}

	/**
	 * @param the
	 *            overCoverRatio to set
	 */

	public void setOverCoverRatio(Float overCoverRatio) {
		this.overCoverRatio = overCoverRatio;
	}

	/**
	 * @return the otherNum
	 */
	public Integer getOtherNum() {
		return otherNum;
	}

	/**
	 * @param the
	 *            otherNum to set
	 */

	public void setOtherNum(Integer otherNum) {
		this.otherNum = otherNum;
	}

	/**
	 * @return the collectNum
	 */
	public Integer getCollectNum() {
		return collectNum;
	}

	/**
	 * @param the
	 *            collectNum to set
	 */

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	/**
	 * @return the collectRatio
	 */
	public Float getCollectRatio() {
		return collectRatio;
	}

	/**
	 * @param the
	 *            collectRatio to set
	 */

	public void setCollectRatio(Float collectRatio) {
		this.collectRatio = collectRatio;
	}

	/**
	 * @return the otherRatio
	 */
	public Float getOtherRatio() {
		return otherRatio;
	}

	/**
	 * @param the
	 *            otherRatio to set
	 */

	public void setOtherRatio(Float otherRatio) {
		this.otherRatio = otherRatio;
	}

	/**
	 * @return the patternSwitchNum
	 */
	public Integer getPatternSwitchNum() {
		return patternSwitchNum;
	}

	/**
	 * @param the
	 *            patternSwitchNum to set
	 */

	public void setPatternSwitchNum(Integer patternSwitchNum) {
		this.patternSwitchNum = patternSwitchNum;
	}

	/**
	 * @return the patternSwitchRatio
	 */
	public Float getPatternSwitchRatio() {
		return patternSwitchRatio;
	}

	/**
	 * @param the
	 *            patternSwitchRatio to set
	 */

	public void setPatternSwitchRatio(Float patternSwitchRatio) {
		this.patternSwitchRatio = patternSwitchRatio;
	}

	/**
	 * @return the downDispatchSmallNum
	 */
	public Integer getDownDispatchSmallNum() {
		return downDispatchSmallNum;
	}

	/**
	 * @param the
	 *            downDispatchSmallNum to set
	 */

	public void setDownDispatchSmallNum(Integer downDispatchSmallNum) {
		this.downDispatchSmallNum = downDispatchSmallNum;
	}

	/**
	 * @return the downDispatchSmallRatio
	 */
	public Float getDownDispatchSmallRatio() {
		return downDispatchSmallRatio;
	}

	/**
	 * @param the
	 *            downDispatchSmallRatio to set
	 */

	public void setDownDispatchSmallRatio(Float downDispatchSmallRatio) {
		this.downDispatchSmallRatio = downDispatchSmallRatio;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoWholeResponseBean3 [cause=" + cause + ", pingPongNum="
				+ pingPongNum + ", pingPongRatio=" + pingPongRatio
				+ ", adjacentNum=" + adjacentNum + ", adjacentRatio="
				+ adjacentRatio + ", weakCoverNum=" + weakCoverNum
				+ ", weakCoverRatio=" + weakCoverRatio + ", disturbNum="
				+ disturbNum + ", disturbRatio=" + disturbRatio
				+ ", overCoverNum=" + overCoverNum + ", overCoverRatio="
				+ overCoverRatio + ", patternSwitchNum=" + patternSwitchNum
				+ ", patternSwitchRatio=" + patternSwitchRatio
				+ ", downDispatchSmallNum=" + downDispatchSmallNum
				+ ", downDispatchSmallRatio=" + downDispatchSmallRatio
				+ ", otherNum=" + otherNum + ", otherRatio=" + otherRatio
				+ ", collectNum=" + collectNum + ", collectRatio="
				+ collectRatio + "]";
	}

}
