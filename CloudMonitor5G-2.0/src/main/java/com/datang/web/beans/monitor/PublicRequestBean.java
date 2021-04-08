package com.datang.web.beans.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.datang.common.util.StringUtils;

/**
 * 请求Bean公共参数
 * 
 * @explain
 * @name PublicRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:26:49
 */
public class PublicRequestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6616460491630813832L;
	/**
	 * 开始时间
	 */
	public Date beginDate;
	/**
	 * 结束时间
	 */
	public Date endDate;
	/**
	 * 市级区域id集合
	 */
	public String cityIds;
	/**
	 * 预存市级区域id集合
	 */
	public String prestorecityIds;
	/**
	 * 设备Id集合
	 */
	public String boxIds;
	/**
	 * 存放BOXID的集合,保存页面的传过来的boxID(cityid失效),保存页面传过来的cityid下的boxID(没有直接选中设备),
	 * 保存用户所有权限的boxID
	 */
	public Set<String> boxIdsSet = new HashSet<>();

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param the
	 *            beginDate to set
	 */

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param the
	 *            endDate to set
	 */

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the cityIds
	 */
	public String getCityIds() {
		return cityIds;
	}

	/**
	 * @param the
	 *            cityIds to set
	 */

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	/**
	 * @return the prestorecityIds
	 */
	public String getPrestorecityIds() {
		return prestorecityIds;
	}

	/**
	 * @param the
	 *            prestorecityIds to set
	 */

	public void setPrestorecityIds(String prestorecityIds) {
		this.prestorecityIds = prestorecityIds;
	}

	/**
	 * @return the boxIds
	 */
	public String getBoxIds() {
		return boxIds;
	}

	/**
	 * @param the
	 *            boxIds to set
	 */

	public void setBoxIds(String boxIds) {
		this.boxIds = boxIds;
	}

	/**
	 * @return the boxIdsSet
	 */
	public Set<String> getBoxIdsSet() {
		return boxIdsSet;
	}

	/**
	 * @param the
	 *            boxIdsSet to set
	 */

	public void setBoxIdsSet(Set<String> boxIdsSet) {
		this.boxIdsSet = boxIdsSet;
	}

	/**
	 * 获取请求参数boxids格式"a,b,c,d",按","解析成list(boxid)
	 * 
	 * @return
	 */
	public Set<String> getBoxIdsList() {
		Set<String> list = new HashSet<>();
		if (StringUtils.hasText(this.boxIds)) {
			String[] split = this.boxIds.trim().split(",");
			for (int i = 0; i < split.length; i++) {
				if (StringUtils.hasText(split[i])) {
					list.add(split[i].trim());
				}
			}
		}
		return list;
	}

	/**
	 * 获取请求参数cityIds
	 * 
	 * @return
	 */
	public List<Long> getCityIdsList() {
		List<Long> list = new ArrayList<>();
		if (StringUtils.hasText(this.cityIds)) {
			String[] split = this.cityIds.trim().split(",");
			for (int i = 0; i < split.length; i++) {
				if (StringUtils.hasText(split[i])) {
					try {
						list.add(Long.parseLong(split[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}
		return list;
	}

	public PublicRequestBean() {
		super();
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PublicRequestBean [beginDate=" + beginDate + ", endDate="
				+ endDate + ", cityIds=" + cityIds + ", prestorecityIds="
				+ prestorecityIds + ", boxIds=" + boxIds + ", boxIdsSet="
				+ boxIdsSet + "]";
	}

}
