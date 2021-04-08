package com.datang.domain.customTemplate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 自定义报表模板的IE指标和KPI指标映射表
 * @author lucheng
 * @date 2020年12月23日 上午11:34:40
 */
@Entity
@Table(name = "IADS_MAPPING_IE")
public class MappingIeToKpiPojo implements Serializable{


	private static final long serialVersionUID = 1L;

	//IE指标名称
	private String ieName;
	
	//区域名称
	private String kpiName;
	
	//对应表名称
	private String tableName;

	@Id
	@Column(name = "IE_NAME")
	public String getIeName() {
		return ieName;
	}

	public void setIeName(String ieName) {
		this.ieName = ieName;
	}

	@Column(name = "KPI_NAME",nullable=false)
	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	@Column(name = "TABLE_NAME",nullable=false)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
