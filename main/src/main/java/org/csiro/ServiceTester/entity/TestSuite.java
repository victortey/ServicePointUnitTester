package org.csiro.ServiceTester.entity;

import java.util.ArrayList;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TestSuite")
public class TestSuite {


	private ArrayList<TestService> TestService;

	@XmlElement(name="TestService")
	public void setTestService(ArrayList<TestService> testService) {
		TestService = testService;
	}

	public ArrayList<TestService> getTestService() {
		return TestService;
	}
}
