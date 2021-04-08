/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.qualityBadRoad;

import java.io.Serializable;

/**
 * 整体分析语音质差路段优化建议各小区数量汇总
 * 
 * @author yinzhipeng
 * @date:2015年12月17日 下午12:58:17
 * @version
 */
public class WholeIndexResponseBean3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3469848643186021963L;
	private Integer tianKuiCellNum;
	private Integer paramCellNum;
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
	 * @return the paramCellNumparamCellNum
	 */
	public Integer getParamCellNum() {
		return paramCellNum;
	}

	/**
	 * @param paramCellNum
	 *            the paramCellNum to set
	 */
	public void setParamCellNum(Integer paramCellNum) {
		this.paramCellNum = paramCellNum;
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
		if (null != tianKuiCellNum) {
			if (null != totalCellNum) {
				totalCellNum += tianKuiCellNum;
			} else {
				totalCellNum = tianKuiCellNum;
			}
		}
		if (null != paramCellNum) {
			if (null != totalCellNum) {
				totalCellNum += paramCellNum;
			} else {
				totalCellNum = paramCellNum;
			}
		}
		if (null != nbCellNum) {
			if (null != totalCellNum) {
				totalCellNum += nbCellNum;
			} else {
				totalCellNum = nbCellNum;
			}
		}
		if (null != pciCellNum) {
			if (null != totalCellNum) {
				totalCellNum += pciCellNum;
			} else {
				totalCellNum = pciCellNum;
			}
		}
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
