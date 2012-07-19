package org.csiro.ServiceTester.Tests;

import java.net.ConnectException;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.ServiceTester.Abstracts.AbstractTestCase;
import org.csiro.ServiceTester.entity.Test;
import org.csiro.ServiceTester.entity.TestService;
import org.junit.Ignore;
import org.w3c.dom.Document;

@Ignore
public class DynamicTest extends AbstractTestCase {
	TestService testService;
	private final Log log = LogFactory.getLog(getClass());

	public DynamicTest(String s){
		super(s);
	}

	public DynamicTest(String s, TestService testService) {
		super(s);
		this.testService = testService;
	}

	public void runMe() throws ConnectException, ConnectTimeoutException, UnknownHostException, Exception {
		Document doc=null;
		if(testService.getTypeEnum().equals(TestService.Type.GET)){
			doc=this.getRequest(testService.getUrl());
		}else if (testService.getTypeEnum().equals(TestService.Type.POST)){
			doc=this.postRequest(testService.getUrl(), testService.getPostBody());
		}
		log.info(this.prettyString(doc));

		for(Test test:testService.getTests()){
			if(test.getEvaluationTypeEnum().equals(Test.EvaluationType.COUNT)){
				this.assertXpathCount(Integer.parseInt(test.getExpected()), test.getXpath(),doc);
			}

			if(test.getEvaluationTypeEnum().equals(Test.EvaluationType.EVALUATE)){
				this.assertXpathEvaluatesTo(test.getExpected(), test.getXpath(),doc);
			}
		}

		assertTrue(true);
	}




}
