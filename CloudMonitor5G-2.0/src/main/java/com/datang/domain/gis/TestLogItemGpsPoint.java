/**
 * 
 */
package com.datang.domain.gis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * 测试日志全量指标GPS轨迹点公共参数
 * 
 * @author yinzhipeng
 * @date:2015年11月26日 上午10:30:24
 * @version
 */
@Entity
@Table(name = "IADS_TESTLOG_GPS_POINT_1974")
public class TestLogItemGpsPoint implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7035116700379149379L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 外键TestLogItem的id
	 */
	private Long recSeqNo;
	/**
	 * 纬度
	 */
	private Double latitude;
	/**
	 * 经度
	 */
	private Double longitude;

	/**
	 * 小区cellId
	 */
	private Long cellId;

	/**
	 * pci
	 */
	private Long pci;

	/**
	 * 最强波束编号
	 */
	private Integer maxBeamNo;

	/**
	 * 最强波束RSRP
	 */
	private Float maxRsrp;

	/**
	 * 占用波束编号
	 */
	private Integer beamNo;

	/**
	 * 占用波束RSRP
	 */
	private Float rsrp;

	/**
	 * 邻区1cellid
	 */
	private Long nc1;
	/**
	 * 邻区2cellid
	 */
	private Long nc2;
	/**
	 * 邻区3cellid
	 */
	private Long nc3;
	/**
	 * 邻区4cellid
	 */
	private Long nc4;
	/**
	 * 邻区5cellid
	 */
	private Long nc5;
	/**
	 * 邻区6cellid
	 */
	private Long nc6;
	/**
	 * 邻区7cellid
	 */
	private Long nc7;
	/**
	 * 邻区8cellid
	 */
	private Long nc8;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	@JSON(serialize = false)
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

	/**
	 * @return the recSeqNorecSeqNo
	 */
	@Column(name = "RECSEQNO")
	@JSON(serialize = false)
	public Long getRecSeqNo() {
		return recSeqNo;
	}

	/**
	 * @param recSeqNo
	 *            the recSeqNo to set
	 */
	public void setRecSeqNo(Long recSeqNo) {
		this.recSeqNo = recSeqNo;
	}

	/**
	 * @return the latitudelatitude
	 */
	@Column(name = "LATITUDE", columnDefinition = "float(53)")
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitudelongitude
	 */
	@Column(name = "LONGITUDE", columnDefinition = "float(53)")
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the cellIdcellId
	 */
	@Column(name = "CELL_ID")
	@JSON(serialize = false)
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

	@Column(name = "PCI")
	@JSON(serialize = false)
	public Long getPci() {
		return pci;
	}

	public void setPci(Long pci) {
		this.pci = pci;
	}

	@Column(name = "MAX_BEAM_NO")
	@JSON(serialize = false)
	public Integer getMaxBeamNo() {
		return maxBeamNo;
	}

	public void setMaxBeamNo(Integer maxBeamNo) {
		this.maxBeamNo = maxBeamNo;
	}

	@Column(name = "MAX_RSRP")
	@JSON(serialize = false)
	public Float getMaxRsrp() {
		return maxRsrp;
	}

	public void setMaxRsrp(Float maxRsrp) {
		this.maxRsrp = maxRsrp;
	}

	@Column(name = "BEAM_NO")
	@JSON(serialize = false)
	public Integer getBeamNo() {
		return beamNo;
	}

	public void setBeamNo(Integer beamNo) {
		this.beamNo = beamNo;
	}

	@Column(name = "RSRP")
	@JSON(serialize = false)
	public Float getRsrp() {
		return rsrp;
	}

	public void setRsrp(Float rsrp) {
		this.rsrp = rsrp;
	}

	@Column(name = "NC1")
	@JSON(serialize = false)
	public Long getNc1() {
		return nc1;
	}

	public void setNc1(Long nc1) {
		this.nc1 = nc1;
	}

	@Column(name = "NC2")
	@JSON(serialize = false)
	public Long getNc2() {
		return nc2;
	}

	public void setNc2(Long nc2) {
		this.nc2 = nc2;
	}

	@Column(name = "NC3")
	@JSON(serialize = false)
	public Long getNc3() {
		return nc3;
	}

	public void setNc3(Long nc3) {
		this.nc3 = nc3;
	}

	@Column(name = "NC4")
	@JSON(serialize = false)
	public Long getNc4() {
		return nc4;
	}

	public void setNc4(Long nc4) {
		this.nc4 = nc4;
	}

	@Column(name = "NC5")
	@JSON(serialize = false)
	public Long getNc5() {
		return nc5;
	}

	public void setNc5(Long nc5) {
		this.nc5 = nc5;
	}

	@Column(name = "NC6")
	@JSON(serialize = false)
	public Long getNc6() {
		return nc6;
	}

	public void setNc6(Long nc6) {
		this.nc6 = nc6;
	}

	@Column(name = "NC7")
	@JSON(serialize = false)
	public Long getNc7() {
		return nc7;
	}

	public void setNc7(Long nc7) {
		this.nc7 = nc7;
	}

	@Column(name = "NC8")
	@JSON(serialize = false)
	public Long getNc8() {
		return nc8;
	}

	public void setNc8(Long nc8) {
		this.nc8 = nc8;
	}

	@Override
	public String toString() {
		return "TestLogItemGpsPoint [id=" + id + ", recSeqNo=" + recSeqNo
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", cellId=" + cellId + ", pci=" + pci + ", maxBeamNo="
				+ maxBeamNo + ", maxRsrp=" + maxRsrp + ", beamNo=" + beamNo
				+ ", rsrp=" + rsrp + ", nc1=" + nc1 + ", nc2=" + nc2 + ", nc3="
				+ nc3 + ", nc4=" + nc4 + ", nc5=" + nc5 + ", nc6=" + nc6
				+ ", nc7=" + nc7 + ", nc8=" + nc8 + "]";
	}

}
