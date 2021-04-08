/**
 * 
 */
package com.datang.domain.platform.analysisThreshold;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.TerminalMenu;

/**
 * 轨迹图阈值参数门限
 * 
 * @author lucheng
 * @date:2020年12月14日 下午3:23:02
 * @version
 */
@Entity
@Table(name = "IADS_MAP_TRAIL_THRESHOLD")
public class MapTrailThreshold implements Cloneable,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 941522808613396439L;
	/**
	 * 门限ID
	 */
	private Long id;
	/**
	 * 门限中文名
	 */
	private String nameCh;
	/**
	 * 门限英文名
	 */
	private String nameEn;
	/**
	 * 门限类别
	 */
	private String thresholdType;
	/**
	 * 门限专题类别
	 */
	private String subjectType;
	/**
	 * 当前门限值
	 */
	private String currentThreshold;
	/**
	 * 门限1
	 */
	private String threshold1;
	/**
	 * 门限2
	 */
	private String threshold2;
	/**
	 * 门限3
	 */
	private String threshold3;
	
	/**
	 * 是否是默认值，1：是  0：不是
	 */
	private Integer isDefault = 0;
	
	/**
	 * 区域
	 */
	private Menu menu;
	
	

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

	/**
	 * @return the nameChnameCh
	 */
	@Column(name = "NAME_CH")
	public String getNameCh() {
		return nameCh;
	}

	/**
	 * @param nameCh
	 *            the nameCh to set
	 */
	public void setNameCh(String nameCh) {
		this.nameCh = nameCh;
	}

	/**
	 * @return the nameEnnameEn
	 */
	@Column(name = "NAME_EN")
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn
	 *            the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * @return the currentThresholdcurrentThreshold
	 */
	@Column(name = "CURRENT_THRESHOLD")
	public String getCurrentThreshold() {
		return currentThreshold;
	}

	/**
	 * @param currentThreshold
	 *            the currentThreshold to set
	 */
	public void setCurrentThreshold(String currentThreshold) {
		this.currentThreshold = currentThreshold;
	}

	/**
	 * @return the threshold1threshold1
	 */
	@Column(name = "THRESHOLD1")
	public String getThreshold1() {
		return threshold1;
	}

	/**
	 * @param threshold1
	 *            the threshold1 to set
	 */
	public void setThreshold1(String threshold1) {
		this.threshold1 = threshold1;
	}

	/**
	 * @return the threshold2threshold2
	 */
	@Column(name = "THRESHOLD2")
	public String getThreshold2() {
		return threshold2;
	}

	/**
	 * @param threshold2
	 *            the threshold2 to set
	 */
	public void setThreshold2(String threshold2) {
		this.threshold2 = threshold2;
	}

	/**
	 * @return the threshold3threshold3
	 */
	@Column(name = "THRESHOLD3")
	public String getThreshold3() {
		return threshold3;
	}

	/**
	 * @param threshold3
	 *            the threshold3 to set
	 */
	public void setThreshold3(String threshold3) {
		this.threshold3 = threshold3;
	}

	/**
	 * @return the thresholdTypethresholdType
	 */
	@Column(name = "THRESHOLD_TYPE")
	public String getThresholdType() {
		return thresholdType;
	}

	/**
	 * @param thresholdType
	 *            the thresholdType to set
	 */
	public void setThresholdType(String thresholdType) {
		this.thresholdType = thresholdType;
	}

	/**
	 * @return the subjectType
	 */
	@Column(name = "SUBJECT_TYPE")
	public String getSubjectType() {
		return subjectType;
	}

	/**
	 * @param the
	 *            subjectType to set
	 */
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	
	@Column(name = "IS_DEFAULT")
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID")
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	@Transient
	public TerminalMenu getTerminalmenu() {

		TerminalMenu terminalMenu = new TerminalMenu();

		if (menu instanceof TerminalMenu) {
			terminalMenu = (TerminalMenu) menu;
		}
		return terminalMenu;
	}
	
	@Override  
    public MapTrailThreshold clone() {  
		MapTrailThreshold mapTrailThreshold = null;  
        try{  
        	mapTrailThreshold = (MapTrailThreshold)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return mapTrailThreshold;  
    }

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VolteAnalysisThreshold [id=" + id + ", nameCh=" + nameCh
				+ ", nameEn=" + nameEn + ", thresholdType=" + thresholdType
				+ ", subjectType=" + subjectType + ", currentThreshold="
				+ currentThreshold + ", threshold1=" + threshold1
				+ ", threshold2=" + threshold2 + ", threshold3=" + threshold3
				+ "]";
	}

	/**
	 * 根据页面传递的当前门限数字(1,2,3)设置当前门限的相应门限值
	 */
	public void updateCurrentThreshold() {
		String currentThreshold2 = this.currentThreshold;
		switch (currentThreshold2) {
		case "1":
			setCurrentThreshold(this.threshold1);
			break;
		case "2":
			setCurrentThreshold(this.threshold2);
			break;
		case "3":
			setCurrentThreshold(this.threshold3);
			break;
		default:
			break;
		}

	}

}
