/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.videoQualityBad;

import java.io.Serializable;

/**
 * 视频质差整体分析VMOS占比饼图响应bean
 * 
 * @explain
 * @name VideoWholeResponseBean1
 * @author shenyanwei
 * @date 2017年5月15日下午2:22:02
 */
public class VideoWholeResponseBean1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4776786891569481698L;

	/**
	 * 1-2.5占比
	 */
	private Float value1to25;
	/**
	 * 2.5-3占比
	 */
	private Float value25to3;
	/**
	 * 3-3.5占比
	 */
	private Float value3to35;
	/**
	 * 3.5-5占比
	 */
	private Float value35to5;

	/**
	 * @return the value1to25
	 */
	public Float getValue1to25() {
		return value1to25;
	}

	/**
	 * @param the
	 *            value1to25 to set
	 */

	public void setValue1to25(Float value1to25) {
		this.value1to25 = value1to25;
	}

	/**
	 * @return the value25to3
	 */
	public Float getValue25to3() {
		return value25to3;
	}

	/**
	 * @param the
	 *            value25to3 to set
	 */

	public void setValue25to3(Float value25to3) {
		this.value25to3 = value25to3;
	}

	/**
	 * @return the value3to35
	 */
	public Float getValue3to35() {
		return value3to35;
	}

	/**
	 * @param the
	 *            value3to35 to set
	 */

	public void setValue3to35(Float value3to35) {
		this.value3to35 = value3to35;
	}

	/**
	 * @return the value35to5
	 */
	public Float getValue35to5() {
		return value35to5;
	}

	/**
	 * @param the
	 *            value35to5 to set
	 */

	public void setValue35to5(Float value35to5) {
		this.value35to5 = value35to5;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoWholeResponseBean1 [value1to25=" + value1to25
				+ ", value25to3=" + value25to3 + ", value3to35=" + value3to35
				+ ", value35to5=" + value35to5 + "]";
	}

}
