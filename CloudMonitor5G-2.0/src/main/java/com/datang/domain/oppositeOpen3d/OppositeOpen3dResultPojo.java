package com.datang.domain.oppositeOpen3d;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.domain.platform.projectParam.Plan4GParam;

import lombok.Data;

/**
 * 反开3d结果汇总表
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_OPPOSITE_OPEN3D_RESULT")
@Data
public class OppositeOpen3dResultPojo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 基本配置参数检查
	 */
	@Column(name = "PARAM_CENSOR")
	private Integer paramCensor;
	

	@Transient 
	private Integer paramCensorOpposite;

	/**
	 * 小区极好点吞平均吐量UL
	 */
	@Column(name = "CELL_MEAN_UL")
	private String cellMeanUl;

	/**
	 * 小区极好点平均吞吐量DL
	 */
	@Column(name = "CELL_MEAN_DL")
	private String cellMeanDl;

	/**
	 * 小区接入性
	 */
	@Column(name = "CELL_INSERT")
	private String cellInsert;

	/**
	 * 小区平均Ping时延
	 */
	@Column(name = "CELL_MEAN_PING")
	private String cellMeanPing;

	/**
	 * 基站内小区间切换
	 */
	@Column(name = "STATION_CHANGE")
	private String stationChange;

	/**
	 * 3\4G互操作（重选、PS重定向）
	 */
	@Column(name = "MUTUAL")
	private String mutual;

	/**
	 * VOLTE功能测试
	 */
	@Column(name = "VOLTE")
	private String volte;

	/**
	 * CSFB功能测试
	 */
	@Column(name = "CSFB")
	private String csfb;
	
	/**
	 * 工参
	 */
	@OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "plan4GParam")
	private Plan4GParam plan4GParam;
	
	/**
	 * 取相反值
	 * @return
	 */
	public Integer getParamCensorOpposite() {
		if(paramCensor==null){
			return null;
		}else if(paramCensor==1){
			return 0;
		}else{
			return 1;
		}
	}
	

}
