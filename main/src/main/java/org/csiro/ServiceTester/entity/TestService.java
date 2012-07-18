package org.csiro.ServiceTester.entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TestService")
public class TestService {

	private String url;
	private String type;
	private String postBody;
	private ArrayList<Test> Tests;

	@XmlAttribute
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	@XmlElement(name="type")
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@XmlElement(name="postBody")
	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}

	public String getPostBody() {
		return postBody;
	}

	@XmlElement(name="Test")
	public void setTests(ArrayList<Test> tests) {
		Tests = tests;
	}

	public ArrayList<Test> getTests() {
		return Tests;
	}

}
