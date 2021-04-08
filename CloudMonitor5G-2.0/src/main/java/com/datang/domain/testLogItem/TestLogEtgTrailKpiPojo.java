/**
 * 
 */
package com.datang.domain.testLogItem;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.datang.domain.security.User;

import lombok.Data;

/**
 * 路测轨迹采样指标汇总
 * 
 * @author lucheng
 * @date 2020年9月9日下午12:42:14
 */
@Entity
@Table(name = "IADS_ETG_TRAIL_KPI")
@Data
public class TestLogEtgTrailKpiPojo implements Serializable {

	private static final long serialVersionUID = 941522808613396439L;
	/**
	 * 门限ID
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 指标中文全名
	 */
	@Column(name = "NAME_CN")
	private String nameCn;
	
	/**
	 * 指标英文全名
	 */
	@Column(name = "NAME_EN")
	private String nameEn;
	
	/**
	 * 大分类
	 */
	@Column(name = "CLASSIFY")
	private String classify;
	
	/**
	 * 指标对应测试业务属性
	 */
	@Column(name = "TEST_SERVICE")
	private String testService;
	
	/**
	 * 所属类型
	 */
	@Column(name = "TYPE")
	private String type;
	
	/**
	 * 所属级别,1:基站级，0:小区级
	 */
	@Column(name = "KPI_LEVEL")
	private String kpiLevel;
	
	/**
	 * 是否可用，0：可用 1：不可用(不需要的手动置为1，需要的再改为置0)
	 */
	@Column(name = "DELETE_TAG")
	private String deleteTag;
	
	/**
	 * 备注
	 */
	@Column(name = "COMMENTS")
	private String comment;
	

}
