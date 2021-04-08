/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.qualityBadRoad;

import java.io.Serializable;

/**
 * 整体分析语音质差路段主被叫位于不同制式下的mos指标
 * 
 * @author yinzhipeng
 * @date:2015年12月17日 下午4:27:07
 * @version
 */
public class WholeIndexResponseBean4 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8261505234936055644L;

	/**
	 * 主被叫都占用LTE时,主叫MOS平均值
	 */
	private Float lteLteCallingMosValueAvg;

	/**
	 * 主被叫都占用LTE时,被叫MOS平均值
	 */
	private Float lteLteCalledMosValueAvg;

	/**
	 * 主叫占用LTE，被叫占用2/3G时,主叫MOS平均值
	 */
	private Float lteCsCallingMosValueAvg;

	/**
	 * 主叫占用LTE，被叫占用2/3G时,被叫MOS平均值
	 */
	private Float lteCsCalledMosValueAvg;

	/**
	 * 主叫占用2/3G，被叫占用LTE时,主叫MOS平均值
	 */
	private Float csLteCallingMosValueAvg;

	/**
	 * 主叫占用2/3G，被叫占用LTE时,被叫MOS平均值
	 */
	private Float csLteCalledMosValueAvg;

	/**
	 * 主被叫都在2/3G时,主叫MOS平均值
	 */
	private Float csCsCallingMosValueAvg;

	/**
	 * 主被叫都在2/3G时,被叫MOS平均值
	 */
	private Float csCsCalledMosValueAvg;

	/**
	 * 主被叫都在LTE的采样点个数
	 */
	private Long lteLteMosPoint;
	/**
	 * 主叫LTE,被叫2/3G的采样点个数
	 */
	private Long lteCsMosPoint;

	/**
	 * 主叫2/3G,被叫LTE的采样点个数
	 */
	private Long csLteMosPoint;

	/**
	 * 主被叫都在2/3G的采样点个数
	 */
	private Long csCsMosPoint;

	/**
	 * @return the lteLteCallingMosValueAvglteLteCallingMosValueAvg
	 */
	public Float getLteLteCallingMosValueAvg() {
		return lteLteCallingMosValueAvg;
	}

	/**
	 * @param lteLteCallingMosValueAvg
	 *            the lteLteCallingMosValueAvg to set
	 */
	public void setLteLteCallingMosValueAvg(Float lteLteCallingMosValueAvg) {
		this.lteLteCallingMosValueAvg = lteLteCallingMosValueAvg;
	}

	/**
	 * @return the lteLteCalledMosValueAvglteLteCalledMosValueAvg
	 */
	public Float getLteLteCalledMosValueAvg() {
		return lteLteCalledMosValueAvg;
	}

	/**
	 * @param lteLteCalledMosValueAvg
	 *            the lteLteCalledMosValueAvg to set
	 */
	public void setLteLteCalledMosValueAvg(Float lteLteCalledMosValueAvg) {
		this.lteLteCalledMosValueAvg = lteLteCalledMosValueAvg;
	}

	/**
	 * @return the lteCsCallingMosValueAvglteCsCallingMosValueAvg
	 */
	public Float getLteCsCallingMosValueAvg() {
		return lteCsCallingMosValueAvg;
	}

	/**
	 * @param lteCsCallingMosValueAvg
	 *            the lteCsCallingMosValueAvg to set
	 */
	public void setLteCsCallingMosValueAvg(Float lteCsCallingMosValueAvg) {
		this.lteCsCallingMosValueAvg = lteCsCallingMosValueAvg;
	}

	/**
	 * @return the lteCsCalledMosValueAvglteCsCalledMosValueAvg
	 */
	public Float getLteCsCalledMosValueAvg() {
		return lteCsCalledMosValueAvg;
	}

	/**
	 * @param lteCsCalledMosValueAvg
	 *            the lteCsCalledMosValueAvg to set
	 */
	public void setLteCsCalledMosValueAvg(Float lteCsCalledMosValueAvg) {
		this.lteCsCalledMosValueAvg = lteCsCalledMosValueAvg;
	}

	/**
	 * @return the csLteCallingMosValueAvgcsLteCallingMosValueAvg
	 */
	public Float getCsLteCallingMosValueAvg() {
		return csLteCallingMosValueAvg;
	}

	/**
	 * @param csLteCallingMosValueAvg
	 *            the csLteCallingMosValueAvg to set
	 */
	public void setCsLteCallingMosValueAvg(Float csLteCallingMosValueAvg) {
		this.csLteCallingMosValueAvg = csLteCallingMosValueAvg;
	}

	/**
	 * @return the csLteCalledMosValueAvgcsLteCalledMosValueAvg
	 */
	public Float getCsLteCalledMosValueAvg() {
		return csLteCalledMosValueAvg;
	}

	/**
	 * @param csLteCalledMosValueAvg
	 *            the csLteCalledMosValueAvg to set
	 */
	public void setCsLteCalledMosValueAvg(Float csLteCalledMosValueAvg) {
		this.csLteCalledMosValueAvg = csLteCalledMosValueAvg;
	}

	/**
	 * @return the csCsCallingMosValueAvgcsCsCallingMosValueAvg
	 */
	public Float getCsCsCallingMosValueAvg() {
		return csCsCallingMosValueAvg;
	}

	/**
	 * @param csCsCallingMosValueAvg
	 *            the csCsCallingMosValueAvg to set
	 */
	public void setCsCsCallingMosValueAvg(Float csCsCallingMosValueAvg) {
		this.csCsCallingMosValueAvg = csCsCallingMosValueAvg;
	}

	/**
	 * @return the csCsCalledMosValueAvgcsCsCalledMosValueAvg
	 */
	public Float getCsCsCalledMosValueAvg() {
		return csCsCalledMosValueAvg;
	}

	/**
	 * @param csCsCalledMosValueAvg
	 *            the csCsCalledMosValueAvg to set
	 */
	public void setCsCsCalledMosValueAvg(Float csCsCalledMosValueAvg) {
		this.csCsCalledMosValueAvg = csCsCalledMosValueAvg;
	}

	/**
	 * @return the lteLteMosPointlteLteMosPoint
	 */
	public Long getLteLteMosPoint() {
		return lteLteMosPoint;
	}

	/**
	 * @param lteLteMosPoint
	 *            the lteLteMosPoint to set
	 */
	public void setLteLteMosPoint(Long lteLteMosPoint) {
		this.lteLteMosPoint = lteLteMosPoint;
	}

	/**
	 * @return the lteCsMosPointlteCsMosPoint
	 */
	public Long getLteCsMosPoint() {
		return lteCsMosPoint;
	}

	/**
	 * @param lteCsMosPoint
	 *            the lteCsMosPoint to set
	 */
	public void setLteCsMosPoint(Long lteCsMosPoint) {
		this.lteCsMosPoint = lteCsMosPoint;
	}

	/**
	 * @return the csLteMosPointcsLteMosPoint
	 */
	public Long getCsLteMosPoint() {
		return csLteMosPoint;
	}

	/**
	 * @param csLteMosPoint
	 *            the csLteMosPoint to set
	 */
	public void setCsLteMosPoint(Long csLteMosPoint) {
		this.csLteMosPoint = csLteMosPoint;
	}

	/**
	 * @return the csCsMosPointcsCsMosPoint
	 */
	public Long getCsCsMosPoint() {
		return csCsMosPoint;
	}

	/**
	 * @param csCsMosPoint
	 *            the csCsMosPoint to set
	 */
	public void setCsCsMosPoint(Long csCsMosPoint) {
		this.csCsMosPoint = csCsMosPoint;
	}

}
