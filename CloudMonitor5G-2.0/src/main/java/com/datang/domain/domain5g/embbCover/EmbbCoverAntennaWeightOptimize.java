/**
 * 
 */
package com.datang.domain.domain5g.embbCover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * embb覆盖专题优化建议详表----3小区波束异常，天线权值优化<br>
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_AWO")
public class EmbbCoverAntennaWeightOptimize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1919860663239580339L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(embb覆盖问题路段)
	 */
	private EmbbCoverBadRoad embbCoverBadRoad;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;

	/**
	 * 最强波束RSRP（dBm）,问题路段上，小区覆盖采样点中测量到的波束的SS-RSRP最大值，对其求平均
	 */
	private Float maxBeamRsrpAvg;

	/**
	 * 占用波束RSRP（dBm）,问题路段上，终端占用波束的SS-RSRP求平均
	 */
	private Float beamRsrpAvg;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public EmbbCoverBadRoad getEmbbCoverBadRoad() {
		return embbCoverBadRoad;
	}

	public void setEmbbCoverBadRoad(EmbbCoverBadRoad embbCoverBadRoad) {
		this.embbCoverBadRoad = embbCoverBadRoad;
	}

	/**
	 * @return the cellNamecellName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName
	 *            the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * @return the cellIdcellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	@Column(name = "MAX_BEAM_RSRP")
	public Float getMaxBeamRsrpAvg() {
		return maxBeamRsrpAvg;
	}

	public void setMaxBeamRsrpAvg(Float maxBeamRsrpAvg) {
		this.maxBeamRsrpAvg = maxBeamRsrpAvg;
	}

	@Column(name = "BEAM_RSRP")
	public Float getBeamRsrpAvg() {
		return beamRsrpAvg;
	}

	public void setBeamRsrpAvg(Float beamRsrpAvg) {
		this.beamRsrpAvg = beamRsrpAvg;
	}

}
