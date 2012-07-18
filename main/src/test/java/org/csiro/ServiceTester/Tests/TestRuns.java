package org.csiro.ServiceTester.Tests;


import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

@RunWith(AllTests.class)
public class TestRuns {

	public static TestSuite suite() {
	    TestSuite suite = new TestSuite();
	    suite.addTest(new DynamicTest("runMe"));
	    return suite;
	  }

}