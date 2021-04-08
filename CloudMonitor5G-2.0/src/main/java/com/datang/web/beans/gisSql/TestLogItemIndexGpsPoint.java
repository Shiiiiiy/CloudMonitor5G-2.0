/**
 * 
 */
package com.datang.web.beans.gisSql;

import java.io.Serializable;

import org.apache.struts2.json.annotations.JSON;

/**
 * 地图指标GPS返回bean
 * 
 * @author yinzhipeng
 * @date:2016年5月10日 下午1:55:17
 * @modify 2017年7月24日
 * @version 1.5.2
 */
public class TestLogItemIndexGpsPoint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4440024057018192876L;

	/**
	 * 纬度
	 */
	private Double latitude;
	/**
	 * 经度
	 */
	private Double longitude;

	/**
	 * 指标值
	 */
	private String indexValue;

	/**
	 * 指标类型或者事件图表类型
	 */
	private Integer indexType;

	/**
	 * CellId:小区标识
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
	 * 序号
	 */
	private Integer index;

	/**
	 * @return the latitudelatitude
	 */
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
	 * @return the indexValueindexValue
	 */
	public String getIndexValue() {
		return indexValue;
	}

	/**
	 * @param indexValue
	 *            the indexValue to set
	 */
	public void setIndexValue(String indexValue) {
		this.indexValue = indexValue;
	}

	/**
	 * @return the cellIdcellId
	 */
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

	/**
	 * @return the indexTypeindexType
	 */
	@JSON(name = "iconType")
	public Integer getIndexType() {
		return indexType;
	}

	/**
	 * @param indexType
	 *            the indexType to set
	 */
	public void setIndexType(Integer indexType) {
		this.indexType = indexType;
	}

	/**
	 * @return the indexindex
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	public Long getPci() {
		return pci;
	}

	public void setPci(Long pci) {
		this.pci = pci;
	}

	public Integer getMaxBeamNo() {
		return maxBeamNo;
	}

	public void setMaxBeamNo(Integer maxBeamNo) {
		this.maxBeamNo = maxBeamNo;
	}

	public Float getMaxRsrp() {
		return maxRsrp;
	}

	public void setMaxRsrp(Float maxRsrp) {
		this.maxRsrp = maxRsrp;
	}

	public Integer getBeamNo() {
		return beamNo;
	}

	public void setBeamNo(Integer beamNo) {
		this.beamNo = beamNo;
	}

	public Float getRsrp() {
		return rsrp;
	}

	public void setRsrp(Float rsrp) {
		this.rsrp = rsrp;
	}

	public Long getNc1() {
		return nc1;
	}

	public void setNc1(Long nc1) {
		this.nc1 = nc1;
	}

	public Long getNc2() {
		return nc2;
	}

	public void setNc2(Long nc2) {
		this.nc2 = nc2;
	}

	public Long getNc3() {
		return nc3;
	}

	public void setNc3(Long nc3) {
		this.nc3 = nc3;
	}

	public Long getNc4() {
		return nc4;
	}

	public void setNc4(Long nc4) {
		this.nc4 = nc4;
	}

	public Long getNc5() {
		return nc5;
	}

	public void setNc5(Long nc5) {
		this.nc5 = nc5;
	}

	public Long getNc6() {
		return nc6;
	}

	public void setNc6(Long nc6) {
		this.nc6 = nc6;
	}

	public Long getNc7() {
		return nc7;
	}

	public void setNc7(Long nc7) {
		this.nc7 = nc7;
	}

	public Long getNc8() {
		return nc8;
	}

	public void setNc8(Long nc8) {
		this.nc8 = nc8;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beamNo == null) ? 0 : beamNo.hashCode());
		result = prime * result + ((cellId == null) ? 0 : cellId.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result
				+ ((indexType == null) ? 0 : indexType.hashCode());
		result = prime * result
				+ ((indexValue == null) ? 0 : indexValue.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result
				+ ((maxBeamNo == null) ? 0 : maxBeamNo.hashCode());
		result = prime * result + ((maxRsrp == null) ? 0 : maxRsrp.hashCode());
		result = prime * result + ((nc1 == null) ? 0 : nc1.hashCode());
		result = prime * result + ((nc2 == null) ? 0 : nc2.hashCode());
		result = prime * result + ((nc3 == null) ? 0 : nc3.hashCode());
		result = prime * result + ((nc4 == null) ? 0 : nc4.hashCode());
		result = prime * result + ((nc5 == null) ? 0 : nc5.hashCode());
		result = prime * result + ((nc6 == null) ? 0 : nc6.hashCode());
		result = prime * result + ((nc7 == null) ? 0 : nc7.hashCode());
		result = prime * result + ((nc8 == null) ? 0 : nc8.hashCode());
		result = prime * result + ((pci == null) ? 0 : pci.hashCode());
		result = prime * result + ((rsrp == null) ? 0 : rsrp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestLogItemIndexGpsPoint other = (TestLogItemIndexGpsPoint) obj;
		if (beamNo == null) {
			if (other.beamNo != null)
				return false;
		} else if (!beamNo.equals(other.beamNo))
			return false;
		if (cellId == null) {
			if (other.cellId != null)
				return false;
		} else if (!cellId.equals(other.cellId))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (indexType == null) {
			if (other.indexType != null)
				return false;
		} else if (!indexType.equals(other.indexType))
			return false;
		if (indexValue == null) {
			if (other.indexValue != null)
				return false;
		} else if (!indexValue.equals(other.indexValue))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (maxBeamNo == null) {
			if (other.maxBeamNo != null)
				return false;
		} else if (!maxBeamNo.equals(other.maxBeamNo))
			return false;
		if (maxRsrp == null) {
			if (other.maxRsrp != null)
				return false;
		} else if (!maxRsrp.equals(other.maxRsrp))
			return false;
		if (nc1 == null) {
			if (other.nc1 != null)
				return false;
		} else if (!nc1.equals(other.nc1))
			return false;
		if (nc2 == null) {
			if (other.nc2 != null)
				return false;
		} else if (!nc2.equals(other.nc2))
			return false;
		if (nc3 == null) {
			if (other.nc3 != null)
				return false;
		} else if (!nc3.equals(other.nc3))
			return false;
		if (nc4 == null) {
			if (other.nc4 != null)
				return false;
		} else if (!nc4.equals(other.nc4))
			return false;
		if (nc5 == null) {
			if (other.nc5 != null)
				return false;
		} else if (!nc5.equals(other.nc5))
			return false;
		if (nc6 == null) {
			if (other.nc6 != null)
				return false;
		} else if (!nc6.equals(other.nc6))
			return false;
		if (nc7 == null) {
			if (other.nc7 != null)
				return false;
		} else if (!nc7.equals(other.nc7))
			return false;
		if (nc8 == null) {
			if (other.nc8 != null)
				return false;
		} else if (!nc8.equals(other.nc8))
			return false;
		if (pci == null) {
			if (other.pci != null)
				return false;
		} else if (!pci.equals(other.pci))
			return false;
		if (rsrp == null) {
			if (other.rsrp != null)
				return false;
		} else if (!rsrp.equals(other.rsrp))
			return false;
		return true;
	}

}
