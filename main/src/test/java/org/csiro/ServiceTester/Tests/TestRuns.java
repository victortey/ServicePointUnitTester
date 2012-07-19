package org.csiro.ServiceTester.Tests;


import javax.xml.bind.JAXBException;

import junit.framework.TestSuite;

import org.csiro.ServiceTester.entity.TestService;
import org.csiro.ServiceTester.main.GetConfiguration;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

@RunWith(AllTests.class)
public class TestRuns {

	public static TestSuite suite() throws JAXBException {
		TestSuite suite = new TestSuite();
		GetConfiguration gc = new GetConfiguration();
		org.csiro.ServiceTester.entity.TestSuite ts = gc.loadTest("sepc.xml");
		for (TestService service : ts.getTestService()) {
			suite.addTest(new DynamicTest("runMe", service));
		}
		return suite;
	}

}