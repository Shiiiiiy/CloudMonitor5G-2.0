package com.datang.domain.VoLTEDissertation.videoQualityBad.patternSwitch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.videoQualityBad.VideoQualityBad;

/**
 * 视频质差分析---模式转换
 * 
 * @explain
 * @name VideoQualityBadPatternSwitch
 * @author shenyanwei
 * @date 2017年6月5日下午3:01:24
 */
@Entity
@Table(name = "IADS_DISS_VIDEO_QB_PS")
public class VideoQualityBadPatternSwitch extends VideoQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1166943804098075536L;

	/**
	 * 转换前TM
	 */
	private String beforeSwitchTM;
	/**
	 * 转换后TM
	 */
	private String afterSwitchTM;

	/**
	 * @return the beforeSwitchTM
	 */
	@Column(name = "BEFORE_SWITCHTM")
	public String getBeforeSwitchTM() {
		if (beforeSwitchTM != null) {
			String[] split = beforeSwitchTM.split("_");
			return split[0];
		}
		return beforeSwitchTM;
	}

	/**
	 * @param the
	 *            beforeSwitchTM to set
	 */

	public void setBeforeSwitchTM(String beforeSwitchTM) {
		this.beforeSwitchTM = beforeSwitchTM;
	}

	/**
	 * @return the afterSwitchTM
	 */
	@Column(name = "AFTER_SWITCHTM")
	public String getAfterSwitchTM() {
		if (afterSwitchTM != null) {
			String[] split = afterSwitchTM.split("_");
			return split[0];
		}
		return afterSwitchTM;
	}

	/**
	 * @param the
	 *            afterSwitchTM to set
	 */

	public void setAfterSwitchTM(String afterSwitchTM) {
		this.afterSwitchTM = afterSwitchTM;
	}

}
