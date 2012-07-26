package org.csiro.ServiceTester.entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="namespaces")
public class Namespaces {

	private ArrayList<Namespace> namespace;

	@XmlElement(name="Namespace")
	public void setNamespace(ArrayList<Namespace> namespace) {
		this.namespace = namespace;
	}

	public ArrayList<Namespace> getNamespace() {
		return namespace;
	}
}
