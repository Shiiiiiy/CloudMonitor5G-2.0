package com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.videoQualityBad.VideoQualityBad;

/**
 * 视频质差分析---乒乓切换问题
 * 
 * @explain
 * @name VideoQualityBadPingPong
 * @author shenyanwei
 * @date 2017年5月10日下午4:25:43
 */
@Entity
@Table(name = "IADS_DISS_VIDEO_QB_PINGPONG")
public class VideoQualityBadPingPong extends VideoQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1166943804098075536L;
	/**
	 * 小区2
	 */
	private String cellName2;
	private Long cellId2;
	/**
	 * 服务小区ID
	 */
	private Long cellId1;
	/**
	 * 服务小区
	 */
	private String cellName1;

	/**
	 * @return the cellId
	 */
	@Column(name = "CELL_ID1")
	public Long getCellId1() {
		return cellId1;
	}

	/**
	 * @param the
	 *            cellId to set
	 */

	public void setCellId1(Long cellId1) {
		this.cellId1 = cellId1;
	}

	/**
	 * @return the callName
	 */
	@Column(name = "CELL_NAME1")
	public String getCellName1() {
		return cellName1;
	}

	/**
	 * @param the
	 *            callName to set
	 */

	public void setCellName1(String cellName1) {
		this.cellName1 = cellName1;
	}

	/**
	 * @return the callName2
	 */
	@Column(name = "CELL_NAME2")
	public String getCellName2() {
		return cellName2;
	}

	/**
	 * @param the
	 *            callName2 to set
	 */

	public void setCellName2(String cellName2) {
		this.cellName2 = cellName2;
	}

	/**
	 * @return the cellId2
	 */
	@Column(name = "CELL_ID2")
	public Long getCellId2() {
		return cellId2;
	}

	/**
	 * @param the
	 *            cellId2 to set
	 */

	public void setCellId2(Long cellId2) {
		this.cellId2 = cellId2;
	}

}
