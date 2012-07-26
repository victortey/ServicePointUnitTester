package org.csiro.ServiceTester.Tests;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.ServiceTester.Abstracts.AbstractTestCase;
import org.csiro.ServiceTester.entity.Namespace;
import org.csiro.ServiceTester.entity.Test;
import org.csiro.ServiceTester.entity.TestService;
import org.junit.Ignore;
import org.w3c.dom.Document;

@Ignore
public class DynamicTest extends AbstractTestCase {
	TestService testService;
	private final Log log = LogFactory.getLog(getClass());

	public DynamicTest(String s) {
		super(s);
	}

	public DynamicTest(String s, TestService testService) {
		super(s);
		this.testService = testService;
	}

	public void runMe() throws Throwable {
		this.setName(testService.getId());
		if(testService.getNamespaces()!=null){
			ArrayList<Namespace> ns = testService.getNamespaces()
					.getNamespace();

			for (int i = 0; i < ns.size(); i++) {
				this.addNamespace(ns.get(i).getPrefix(), ns.get(i).getUri());
			}
		}
		Document doc = null;
		try {
			if (testService.getTypeEnum().equals(TestService.Type.GET)) {
				doc = this.getRequest(testService.getUrl());
			} else if (testService.getTypeEnum().equals(TestService.Type.POST)) {
				doc = this.postRequest(testService.getUrl(), testService
						.getPostBody());
			}

			for (Test test : testService.getTests()) {
				if (test.getEvaluationTypeEnum().equals(
						Test.EvaluationType.COUNT)) {
					this.assertXpathCount(Integer.parseInt(test.getExpected()),
							test.getXpath(), doc);
				}

				if (test.getEvaluationTypeEnum().equals(
						Test.EvaluationType.EVALUATE)) {
					this.assertXpathEvaluatesTo(test.getExpected(), test
							.getXpath(), doc);
				}
			}

		} catch (Throwable e) {
			log.error("\nTest Failure:" + testService.getUrl());
			log.error(this.prettyString(doc));
			e.printStackTrace();
			throw e;
		}
	}


//	public void testMe() throws Throwable {
//
//		Document doc = null;
//
//		doc = this
//				.getRequest("http://130.116.24.254:8080/nvcl-geoserver-test/wfs?request=getFeature&version=1.1.0&typeName=gsml:Borehole&maxFeatures=10&featureid=gsml.borehole.WTB5");
//		System.out.println(this.prettyString(doc));
//		this.assertXpathCount(2,
//				"/wfs:FeatureCollection/gml:featureMembers/gsml:Borehole", doc);
//
//	}
}
