package com.datang.domain.testPlan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("TestUnit")
public class TestUnit implements Serializable {
	/**
	 * serialVersionUID
	 */
	@XStreamOmitField
	private static final long serialVersionUID = -118044382955418038L;

	@XStreamImplicit(itemFieldName = "TestScheme")
	private List<TestScheme> testSuit = new ArrayList<TestScheme>();

	public List<TestScheme> getTestSuit() {
		return testSuit;
	}

	public void setTestSuit(List<TestScheme> testSuit) {
		this.testSuit = testSuit;
	}
}
