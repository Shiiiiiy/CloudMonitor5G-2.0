package com.datang.domain.customTemplate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.testManage.terminal.TerminalGroup;

/**
 * 自定义报表模板上传记录表
 * @author lucheng
 * @date 2020年12月22日 上午11:34:40
 */
@Entity
@Table(name = "IADS_CUSREPORT_TMEPLATE")
public class CustomReportTemplatePojo implements Serializable{


	private static final long serialVersionUID = 1L;

	private Long id;

	//模板名称
	private String templateName;


	//导入时间
	private Long importDate;

	//创建人
	private String userName;

	//模板保存路径
	private String saveFilePath;

	//普通IE汇总字段，以逗号隔开
	private String conmmonKpiNameSum;

	//时间IE汇总字段，以逗号隔开
	private String timeKpiNameSum;

	//里程IE汇总字段，以逗号隔开
	private String mileageKpiNameSum;
	/**
	 * 地理栅格类型   10： 10*10   50： 50*50
	 */
	private String mileageKpiType;

	@Column(name = "ID")
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TEMPLATE_NAME")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}


	@Column(name = "IMPORT_DATE")
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Long getImportDate() {
		return importDate;
	}

	public void setImportDate(Long importDate) {
		this.importDate = importDate;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "SAVE_FILE_PATH")
	@JSON(serialize = false)
	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	@Column(name = "KPINAME_SUM")
	@JSON(serialize = false)
	public String getConmmonKpiNameSum() {
		return conmmonKpiNameSum;
	}

	public void setConmmonKpiNameSum(String conmmonKpiNameSum) {
		this.conmmonKpiNameSum = conmmonKpiNameSum;
	}

	@Column(name = "TIME_KPINAME_SUM")
	@JSON(serialize = false)
	public String getTimeKpiNameSum() {
		return timeKpiNameSum;
	}

	public void setTimeKpiNameSum(String timeKpiNameSum) {
		this.timeKpiNameSum = timeKpiNameSum;
	}

	@Column(name = "MILEAGE_KPINAME_SUM")
	public String getMileageKpiNameSum() {
		return mileageKpiNameSum;
	}

	public void setMileageKpiNameSum(String mileageKpiNameSum) {
		this.mileageKpiNameSum = mileageKpiNameSum;
	}

	@Column(name = "MILEAGE_KPI_TYPE")
	public String getMileageKpiType() {
		return mileageKpiType;
	}

	public void setMileageKpiType(String mileageKpiType) {
		this.mileageKpiType = mileageKpiType;
	}

}
