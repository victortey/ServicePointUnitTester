package org.csiro.ServiceTester.Tests;

import org.csiro.ServiceTester.Abstracts.AbstractTestCase;
import org.junit.Ignore;

@Ignore
public class DynamicTest extends AbstractTestCase{




	public DynamicTest(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}


	public void runMe() {
		System.out.println("Testing in progress");
		assertTrue(true);

//		Document doc=this.getRequest("http://auscope-services-test.arrc.csiro.au/gsnsw-earthresource/wfs?request=GetFeature&version=1.1.0&typename=er:Commodity&maxfeatures=20");
//		System.out.println(this.prettyString(doc));
//		assertTrue(true);
	}

}
