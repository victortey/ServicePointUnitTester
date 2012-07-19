package org.csiro.ServiceTester.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Test")
public class Test {

	private String xpath;
	private String evaluationType;
	private String expected;
	private String id;

	public enum EvaluationType {
		COUNT, EVALUATE;
	}

	@XmlElement(name = "expected")
	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getExpected() {
		return expected;
	}

	@XmlElement(name = "evaluationType")
	public void setEvaluationType(String evaluationType) {
		this.evaluationType=evaluationType;
	}

	public String getEvaluationType() {
		return this.evaluationType;
	}


	public EvaluationType getEvaluationTypeEnum() {
		if (evaluationType.toUpperCase().equals("COUNT")) {
			return EvaluationType.COUNT;
		} else if (evaluationType.toUpperCase().equals("EVALUATE")) {
			return EvaluationType.EVALUATE;
		}else{
			return null;
		}
	}

	@XmlElement(name = "xpath")
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getXpath() {
		return xpath;
	}

	@XmlAttribute(name = "id")
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
