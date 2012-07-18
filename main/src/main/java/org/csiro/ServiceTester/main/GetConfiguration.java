package org.csiro.ServiceTester.main;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.ServiceTester.entity.TestSuite;

public class GetConfiguration {

	private final Log log = LogFactory.getLog(getClass());

	public TestSuite loadTest(String filename) throws JAXBException {

		try {

			InputStream xmlStream = GetConfiguration.class
					.getResourceAsStream("/" + filename);

			JAXBContext jaxbContext = JAXBContext.newInstance(TestSuite.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			TestSuite testSuite = (TestSuite) jaxbUnmarshaller
					.unmarshal(xmlStream);
			return testSuite;
		} catch (JAXBException e) {
			throw e;
		}

	}
}
