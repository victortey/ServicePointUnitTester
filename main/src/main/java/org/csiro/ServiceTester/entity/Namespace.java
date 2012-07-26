package org.csiro.ServiceTester.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Namespace")
public class Namespace {

	private String prefix;
	private String uri;

	@XmlElement(name="prefix")
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getPrefix() {
		return prefix;
	}

	@XmlElement(name="uri")
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getUri() {
		return uri;
	}
}
