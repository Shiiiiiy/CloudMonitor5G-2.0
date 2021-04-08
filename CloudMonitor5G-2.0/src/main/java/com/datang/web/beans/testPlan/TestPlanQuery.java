/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.web.beans.testPlan;

import java.util.Set;

import com.datang.bean.AbstractPagedQuery;
import com.datang.domain.testPlan.TestPlan;

/**
 * @author songzhigang
 * @version 1.0.0, 2010-12-29
 */
public class TestPlanQuery extends AbstractPagedQuery<TestPlan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8473875882854452607L;

	private String name;

	private Boolean sended;

	private Set<Integer> testPlanIds;

	private Integer version;

	/**
	 * @return the name
	 */
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
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the testPlanIds
	 */
	public Set<Integer> getTestPlanIds() {
		return testPlanIds;
	}

	/**
	 * @param testPlanIds
	 *            the testPlanIds to set
	 */
	public void setTestPlanIds(Set<Integer> testPlanIds) {
		this.testPlanIds = testPlanIds;
	}

	/**
	 * @return the sended
	 */
	public Boolean getSended() {
		return sended;
	}

	/**
	 * @param sended
	 *            the sended to set
	 */
	public void setSended(Boolean sended) {
		this.sended = sended;
	}

}
