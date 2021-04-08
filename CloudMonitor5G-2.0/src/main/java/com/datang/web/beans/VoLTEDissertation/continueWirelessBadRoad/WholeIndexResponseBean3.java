/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.continueWirelessBadRoad;

import java.io.Serializable;

/**
 * 整体分析连续无线差路段优化建议各小区数量汇总
 * 
 * @explain
 * @name WholeIndexResponseBean3
 * @author shenyanwei
 * @date 2016年5月31日下午3:55:03
 */
public class WholeIndexResponseBean3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3469848643186021963L;
	private Integer tianKuiCellNum;
	private Integer nbCellNum;
	private Integer pciCellNum;
	private Integer totalCellNum;

	/**
	 * @return the tianKuiCellNumtianKuiCellNum
	 */
	public Integer getTianKuiCellNum() {
		return tianKuiCellNum;
	}

	/**
	 * @param tianKuiCellNum
	 *            the tianKuiCellNum to set
	 */
	public void setTianKuiCellNum(Integer tianKuiCellNum) {
		this.tianKuiCellNum = tianKuiCellNum;

	}

	/**
	 * @return the nbCellNumnbCellNum
	 */
	public Integer getNbCellNum() {
		return nbCellNum;
	}

	/**
	 * @param nbCellNum
	 *            the nbCellNum to set
	 */
	public void setNbCellNum(Integer nbCellNum) {
		this.nbCellNum = nbCellNum;
	}

	/**
	 * @return the pciCellNumpciCellNum
	 */
	public Integer getPciCellNum() {
		return pciCellNum;
	}

	/**
	 * @param pciCellNum
	 *            the pciCellNum to set
	 */
	public void setPciCellNum(Integer pciCellNum) {
		this.pciCellNum = pciCellNum;
	}

	/**
	 * @return the totalCellNumtotalCellNum
	 */
	public Integer getTotalCellNum() {

		return totalCellNum;
	}

	/**
	 * @param totalCellNum
	 *            the totalCellNum to set
	 */
	public void setTotalCellNum(Integer totalCellNum) {
		this.totalCellNum = totalCellNum;
	}

}
