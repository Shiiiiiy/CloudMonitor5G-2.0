/**
 * 
 */
package com.datang.web.beans.stream.streamVideoQualitybad;

import java.io.Serializable;

/**
 * 流媒体视频质差整体分析VMOS占比表单响应bean
 * 
 * @explain
 * @name StreamWholeResponseBean5
 * @author shenyanwei
 * @date 2017年10月23日下午1:44:29
 */
public class StreamWholeResponseBean5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4776786891569481698L;

	/**
	 * 占比范围
	 */
	private String name;
	/**
	 * 占比
	 */
	private Float value;

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
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param the
	 *            value to set
	 */

	public void setValue(Float value) {
		this.value = value;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoWholeResponseBean5 [name=" + name + ", value=" + value
				+ "]";
	}

}
