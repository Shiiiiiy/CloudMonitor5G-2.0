package com.datang.util.monitor;

public class AlarmCode {
	private String name;
	private String type;
	private String reson;

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

	/**
	 * @return the reson
	 */
	public String getReson() {
		return reson;
	}

	/**
	 * @param the
	 *            reson to set
	 */

	public void setReson(String reson) {
		this.reson = reson;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlarmCode [name=" + name + ", type=" + type + ", reson="
				+ reson + "]";
	}

}
