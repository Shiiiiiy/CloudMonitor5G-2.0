/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.videoQualityBad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频质差整体分析视频质差原因分布响应bean
 * 
 * @explain
 * @name VideoWholeResponseBean4
 * @author shenyanwei
 * @date 2017年5月16日下午3:07:19
 */
public class VideoWholeResponseBean4 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4592515641087824043L;
	/**
	 * 视频质差问题原因
	 */
	private String name;
	/**
	 * 各原因个数
	 */
	private List<Integer> data = new ArrayList<>();
	private String type = "bar";

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param the
	 *            name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the data
	 */
	public List<Integer> getData() {
		return data;
	}

	/**
	 * @param the
	 *            data to set
	 */

	public void setData(List<Integer> data) {
		this.data = data;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param the
	 *            type to set
	 */

	public void setType(String type) {
		this.type = type;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoWholeResponseBean4 [name=" + name + ", data=" + data
				+ ", type=" + type + "]";
	}

}
