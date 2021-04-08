/**
 * 
 */
package com.datang.web.beans.platform.projectParam;

import java.io.Serializable;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Date;
import java.util.Locale;

import org.apache.struts2.json.annotations.JSON;

/**
 * 工参List返回bean
 * 
 * @author yinzhipeng
 * @date:2015年10月16日 下午2:44:44
 * @version
 */
public class ProjectParamResponsePageBean implements
		Comparable<ProjectParamResponsePageBean>, Serializable {
	private RuleBasedCollator collator = (RuleBasedCollator) Collator
			.getInstance(Locale.CHINA);

	/**
	 * 
	 */
	private static final long serialVersionUID = -6806947692171412577L;
	private Long id;
	/**
	 * 一级域,省级域名称
	 */
	private String provinceGroupName;
	/**
	 * 二级域,市级域,当前域名称
	 */
	private String cityGroupName;
	/**
	 * 创建人,实则为最新工参表导入的用户名，若该二级域工程表未导入，显示”-”
	 */
	private String userName;
	/**
	 * 最新工参表导入时间， 若该二级域工程表未导入，显示”-”
	 */
	private Date importDate;
	/**
	 * 5G小区表是否导入
	 */
	private boolean is5gImport = false;
	/**
	 * LTE小区表时候导入
	 */
	private boolean isLteImport = false;
	/**
	 * GSM小区表是否导入
	 */
	private boolean isGsmImport = false;
	
	/**
	 * 5G-5G邻区是否导入
	 */
	private boolean is5g5gImport = false;
	
	/**
	 * 5G-LTE邻区是否导入
	 */
	private boolean is5gLteImport = false;
	/**
	 * TDL邻区表是否导入
	 */
	private boolean isTdlImport = false;
	/**
	 * TDL-GSM邻区表是否导入
	 */
	private boolean isTdlGsmImport = false;
	/**
	 * LTE-5G配对邻区表是否导入
	 */
	private boolean isLte5GImport = false;

	/**
	 * @return the provinceGroupNameprovinceGroupName
	 */
	public String getProvinceGroupName() {
		return provinceGroupName;
	}

	/**
	 * @param provinceGroupName
	 *            the provinceGroupName to set
	 */
	public void setProvinceGroupName(String provinceGroupName) {
		this.provinceGroupName = provinceGroupName;
	}

	/**
	 * @return the cityGroupNamecityGroupName
	 */
	public String getCityGroupName() {
		return cityGroupName;
	}

	/**
	 * @param cityGroupName
	 *            the cityGroupName to set
	 */
	public void setCityGroupName(String cityGroupName) {
		this.cityGroupName = cityGroupName;
	}

	/**
	 * @return the userNameuserName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the importDateimportDate
	 */
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getImportDate() {
		return importDate;
	}

	/**
	 * @param importDate
	 *            the importDate to set
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	/**
	 * @return the isLteImportisLteImport
	 */
	public boolean isLteImport() {
		return isLteImport;
	}

	/**
	 * @param isLteImport
	 *            the isLteImport to set
	 */
	public void setLteImport(boolean isLteImport) {
		this.isLteImport = isLteImport;
	}

	/**
	 * @return the isGsmImportisGsmImport
	 */
	public boolean isGsmImport() {
		return isGsmImport;
	}

	/**
	 * @param isGsmImport
	 *            the isGsmImport to set
	 */
	public void setGsmImport(boolean isGsmImport) {
		this.isGsmImport = isGsmImport;
	}

	/**
	 * @return the isTdlImportisTdlImport
	 */
	public boolean isTdlImport() {
		return isTdlImport;
	}

	/**
	 * @param isTdlImport
	 *            the isTdlImport to set
	 */
	public void setTdlImport(boolean isTdlImport) {
		this.isTdlImport = isTdlImport;
	}

	/**
	 * @return the isTdlGsmImportisTdlGsmImport
	 */
	public boolean isTdlGsmImport() {
		return isTdlGsmImport;
	}

	/**
	 * @param isTdlGsmImport
	 *            the isTdlGsmImport to set
	 */
	public void setTdlGsmImport(boolean isTdlGsmImport) {
		this.isTdlGsmImport = isTdlGsmImport;
	}

	/**
	 * lte-5g配对邻区表是否导入
	 * @return
	 */
	public boolean isLte5GImport() {
		return isLte5GImport;
	}

	public void setIsLte5GImport(boolean isLte5GImport) {
		this.isLte5GImport = isLte5GImport;
	}

	/**
	 * @return the idid
	 */
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
	 * @param id
	 * @param provinceGroupName
	 * @param cityGroupName
	 * @param userName
	 * @param importDate
	 * @param isLteImport
	 * @param isGsmImport
	 * @param isTdlImport
	 * @param isTdlGsmImport
	 */
	public ProjectParamResponsePageBean(Long id, String provinceGroupName,
			String cityGroupName, String userName, Date importDate,
			boolean isLteImport, boolean isGsmImport, boolean isTdlImport,
			boolean isTdlGsmImport,boolean isLte5GImport) {
		super();
		this.id = id;
		this.provinceGroupName = provinceGroupName;
		this.cityGroupName = cityGroupName;
		this.userName = userName;
		this.importDate = importDate;
		this.isLteImport = isLteImport;
		this.isGsmImport = isGsmImport;
		this.isTdlImport = isTdlImport;
		this.isTdlGsmImport = isTdlGsmImport;
		this.isLte5GImport = isLte5GImport;
	}

	/**
	 * @param id
	 * @param provinceGroupName
	 * @param cityGroupName
	 */
	public ProjectParamResponsePageBean(Long id, String provinceGroupName,
			String cityGroupName) {
		super();
		this.id = id;
		this.provinceGroupName = provinceGroupName;
		this.cityGroupName = cityGroupName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cityGroupName == null) ? 0 : cityGroupName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((importDate == null) ? 0 : importDate.hashCode());
		result = prime * result + (isGsmImport ? 1231 : 1237);
		result = prime * result + (isLteImport ? 1231 : 1237);
		result = prime * result + (isTdlGsmImport ? 1231 : 1237);
		result = prime * result + (isTdlImport ? 1231 : 1237);
		result = prime * result + (isLte5GImport ? 1231 : 1237);
		result = prime
				* result
				+ ((provinceGroupName == null) ? 0 : provinceGroupName
						.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectParamResponsePageBean other = (ProjectParamResponsePageBean) obj;
		if (cityGroupName == null) {
			if (other.cityGroupName != null)
				return false;
		} else if (!cityGroupName.equals(other.cityGroupName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (importDate == null) {
			if (other.importDate != null)
				return false;
		} else if (!importDate.equals(other.importDate))
			return false;
		if (isGsmImport != other.isGsmImport)
			return false;
		if (isLteImport != other.isLteImport)
			return false;
		if (isTdlGsmImport != other.isTdlGsmImport)
			return false;
		if (isTdlImport != other.isTdlImport)
			return false;
		if (isLte5GImport != other.isLte5GImport)
			return false;
		if (provinceGroupName == null) {
			if (other.provinceGroupName != null)
				return false;
		} else if (!provinceGroupName.equals(other.provinceGroupName))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	

	public boolean isIs5gImport() {
		return is5gImport;
	}

	public void setIs5gImport(boolean is5gImport) {
		this.is5gImport = is5gImport;
	}

	public boolean isIs5g5gImport() {
		return is5g5gImport;
	}

	public void setIs5g5gImport(boolean is5g5gImport) {
		this.is5g5gImport = is5g5gImport;
	}

	public boolean isIs5gLteImport() {
		return is5gLteImport;
	}

	public void setIs5gLteImport(boolean is5gLteImport) {
		this.is5gLteImport = is5gLteImport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ProjectParamResponsePageBean o) {
		if (o == null)
			return -1;
		if (this == o)
			return 0;
		if ((this.provinceGroupName == o.getProvinceGroupName())
				&& (this.cityGroupName == o.getCityGroupName()))
			return 0;
		return collator.compare(this.provinceGroupName,
				o.getProvinceGroupName()) == 0 ? collator.compare(
				this.cityGroupName, o.getCityGroupName()) : collator.compare(
				this.provinceGroupName, o.getProvinceGroupName());
	}
}
