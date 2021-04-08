package com.datang.domain.security.menu;

import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.datang.domain.platform.analysisThreshold.MapTrailThreshold;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.security.UserGroup;

/**
 * 权限
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 上午10:40:43
 * @version
 */
@Entity
@Table(name = "IADS_MENU")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Menu implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * pid
	 */
	private Long pid;

	/**
	 * name
	 */
	private String name;

	/**
	 * url
	 */
	private String url;

	/**
	 * Image file to use as the icon. Uses default if not specified.
	 */
	private String icon;

	/**
	 * Image file to use as the open icon. Uses default if not specified.
	 */
	private String iconOpen;

	/**
	 * 对应的用户组
	 */
	private Set<UserGroup> groups;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "IADS_USERGROUP_MENU", joinColumns = { @JoinColumn(name = "MENUS_ID") }, inverseJoinColumns = @JoinColumn(name = "USERGROUP_ID"))
	@Fetch(FetchMode.SUBSELECT)
	@JSON(serialize = false)
	public Set<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<UserGroup> groups) {
		this.groups = groups;
	}
	
	/**
	 * 单站参数
	 */
	private StationParamPojo stationParamPojo;

	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "stationParamPojo")
	//@JSON(serialize = false)
	public StationParamPojo getStationParamPojo() {
		return stationParamPojo;
	}

	public void setStationParamPojo(StationParamPojo stationParamPojo) {
		this.stationParamPojo = stationParamPojo;
	}
	
	/**
	 * 轨迹阈值门限参数
	 */
	private Set<MapTrailThreshold> mapTrailThreshold = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = javax.persistence.CascadeType.ALL)
	@JSON(serialize = false)
	public Set<MapTrailThreshold> getMapTrailThreshold() {
		return mapTrailThreshold;
	}

	public void setMapTrailThreshold(Set<MapTrailThreshold> mapTrailThreshold) {
		this.mapTrailThreshold = mapTrailThreshold;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	 * @return the pid
	 */
	@Column(name = "PID")
	public Long getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            the pid to set
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the icon
	 */
	@Column(name = "ICON")
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the iconOpen
	 */
	@Column(name = "OPEN_ICON")
	public String getIconOpen() {
		return iconOpen;
	}

	/**
	 * @param iconOpen
	 *            the iconOpen to set
	 */
	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

}
