package com.datang.domain.embbCover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 站间距工参表
 * @author maxuancheng
 * @date 2020年5月7日
 */
@Entity
@Table(name = "IADS_5G_EMBB_STATIONDISTANCE")
public class StationDistanceNRPojo implements Serializable{

	/**
	 * @author maxuancheng
	 * date:2020年5月12日 上午9:44:01
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String cellName;
	
	private String regin;
	
	private Integer distance;
	
	private String nCellNames;

	@Column(name = "ID")
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	@Column(name = "REGIN")
	public String getRegin() {
		return regin;
	}

	public void setRegin(String regin) {
		this.regin = regin;
	}

	@Column(name = "DISTANCE")
	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	@Column(name = "NCELLNAMES",length=1024)
	public String getnCellNames() {
		return nCellNames;
	}

	public void setnCellNames(String nCellNames) {
		this.nCellNames = nCellNames;
	}

	
	
	
	
}
