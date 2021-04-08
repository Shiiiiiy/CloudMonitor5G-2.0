/**
 * 
 */
package com.datang.web.action.gisSql;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.datang.common.util.StringUtils;

/**
 * 楼宇指标颜色配置
 * 
 * @explain
 * @name FloorGpsPointInfo
 * @author shenyanwei
 * @date 2016年11月17日下午2:34:11
 */
@Component
@Scope("singleton")
public class FloorGpsPointInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7977872375482320312L;

	@Value("${gis.floor.rsrp}")
	private String rsrp;
	@Value("${gis.floor.rerpColor}")
	private String rsrpColor;
	@Value("${gis.floor.sinr}")
	private String sinr;
	@Value("${gis.floor.sinrColor}")
	private String sinrColor;
	@Value("${gis.floor.cover}")
	private String let;
	@Value("${gis.floor.coverColor}")
	private String letColor;

	/**
	 * 获取某个指标的[{"beginValue":108,"color":"#ffffff","endValue":110}...]形式
	 * 
	 * @param indexType
	 * @return
	 */
	public List<Map<String, Object>> getIndexColorListMap(Integer indexType) {
		List<Map<String, Object>> listmMaps = new LinkedList<>();
		if (null != indexType) {
			String index = null;
			String indexColor = null;
			switch (indexType) {
			case 0:
				index = rsrp;
				indexColor = rsrpColor;
				break;
			case 1:
				index = sinr;
				indexColor = sinrColor;
				break;
			case 2:
				index = let;
				indexColor = letColor;
				break;
			default:
				break;
			}
			// {
			// "beginValue" : 108,//指标区间开始值
			// "color" : "#ffffff",//指标位于该区间的颜色
			// "endValue" : 110//指标区间结束值
			// }
			if (null != index && null != indexColor
					&& StringUtils.hasText(index)
					&& StringUtils.hasText(indexColor)) {
				String[] indexs = index.split("@");
				String[] indexColors = indexColor.split(",");
				if ((0 != indexs.length && 0 != indexColors.length)
						&& (indexs.length == indexColors.length)) {
					for (int i = 0; i < indexs.length; i++) {
						if (StringUtils.hasText(indexs[i])) {
							// [0,2]@(2,3]@(3,-4]@(4,5]
							String[] indexSplit = indexs[i].trim()
									.substring(1, indexs[i].length() - 1)
									.split(",");
							Map<String, Object> map = new LinkedHashMap<String, Object>();
							map.put("beginValue", indexSplit[0]);
							map.put("color", indexColors[i]);
							map.put("endValue", indexSplit[1]);
							listmMaps.add(map);
						}
					}
				}
			}
		}
		return listmMaps;
	}

	/**
	 * @return the rsrp
	 */
	public String getRsrp() {
		return rsrp;
	}

	/**
	 * @param the
	 *            rsrp to set
	 */

	public void setRsrp(String rsrp) {
		this.rsrp = rsrp;
	}

	/**
	 * @return the rsrpColor
	 */
	public String getRsrpColor() {
		return rsrpColor;
	}

	/**
	 * @param the
	 *            rsrpColor to set
	 */

	public void setRsrpColor(String rsrpColor) {
		this.rsrpColor = rsrpColor;
	}

	/**
	 * @return the sinr
	 */
	public String getSinr() {
		return sinr;
	}

	/**
	 * @param the
	 *            sinr to set
	 */

	public void setSinr(String sinr) {
		this.sinr = sinr;
	}

	/**
	 * @return the sinrColor
	 */
	public String getSinrColor() {
		return sinrColor;
	}

	/**
	 * @param the
	 *            sinrColor to set
	 */

	public void setSinrColor(String sinrColor) {
		this.sinrColor = sinrColor;
	}

	/**
	 * @return the let
	 */
	public String getLet() {
		return let;
	}

	/**
	 * @param the
	 *            let to set
	 */

	public void setLet(String let) {
		this.let = let;
	}

	/**
	 * @return the letColor
	 */
	public String getLetColor() {
		return letColor;
	}

	/**
	 * @param the
	 *            letColor to set
	 */

	public void setLetColor(String letColor) {
		this.letColor = letColor;
	}

}
