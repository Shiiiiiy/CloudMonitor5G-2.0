package com.datang.domain.platform.stationParam;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.datang.domain.security.menu.Menu;

import lombok.Data;

/**
 * 报告模板参数实体类
 * @author Lucheng
 */
@Entity
@Data
@Table(name = "IADS_REPORT_TEMPLATE")
public class StationReportTemplatePojo implements Serializable{
	
	private static final long serialVersionUID = 2646423832182549992L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 区域模板名称
	 */
	@Column(name = "TEMPLATE_NAME")
	private String templateName;
	
	/**
	 * 区域模板值
	 */
	@Column(name = "TEMPLATE_VALUE")
	private Long templateValue;
	
	/**
	 * 区域模板类型     0：单站   1：反开3d
	 */
	@Column(name = "TEMPLATE_TYPE")
	private Integer templateType;
	
	/**
	 * 区域模板输出excel保存的模板名称
	 */
	@Column(name = "EXPORT_EXCEL_NAME")
	private String exportEcxelName;
	
	/**
	 * 区域模板输出的轨迹图类型 -1：全部   0：小区级截图  1：基站级截图
	 */
	@Column(name = "MAP_TRAIL_TYPE")
	private Integer mapTrailType;
	
	/**
	 * 区域模板输出速率图的时间范围   默认：-1（全部时间）单位:秒
	 */
	@Column(name = "CHART_RATE_TIME")
	private Long chartRateTime;
	
	/**
	 * 区域模板样例保存的模板名称
	 */
	@Column(name = "TEMPLATE_EXMPLE_NAME")
	private String templateExmpleName;
	
	/**
	 * 区域模板样例输出的下载名称
	 */
	@Column(name = "TEMPLATE_EXMPLE_DOWNLOAD")
	private String templateExmpleDownload;

	
}
