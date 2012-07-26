package org.csiro.ServiceTester.entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TestService")
public class TestService {

	private String id;
	private String url;
	private Namespaces namespaces;
	private String type;
	private String postBody;
	private ArrayList<Test> Tests;



	public static enum Type {
		GET, POST;
	}

	@XmlAttribute
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	@XmlElement(name="type")
	public void setType(String type) {
		this.type=type;
	}

	public String getType() {
		return type;
	}

	public Type getTypeEnum() {
		if(type.toUpperCase().equals("GET")){
			return Type.GET;
		}else if(type.toUpperCase().equals("POST")){
			return Type.POST;
		}else{
			return null;
		}

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

	@XmlAttribute(name="id")
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@XmlElement(name="namespaces")
	public void setNamespaces(Namespaces namespaces) {
		this.namespaces = namespaces;
	}

	public Namespaces getNamespaces() {
		return namespaces;
	}

}
