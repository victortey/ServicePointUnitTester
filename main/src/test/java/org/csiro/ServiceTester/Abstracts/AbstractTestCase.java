package org.csiro.ServiceTester.Abstracts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.csiro.ServiceTester.util.HttpServiceCaller;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Unit test for simple App.
 */
public abstract class AbstractTestCase extends TestCase{

	private XpathEngine xpathEngine;

	@SuppressWarnings("serial")
	private final Map<String, String> WFS_NAMESPACES = new HashMap<String, String>() {
				{
					put("wfs", "http://www.opengis.net/wfs");
					put("ows", "http://www.opengis.net/ows");
					put("ogc", "http://www.opengis.net/ogc");
					put("xs", "http://www.w3.org/2001/XMLSchema");
					put("xsd", "http://www.w3.org/2001/XMLSchema");
					put("gml", "http://www.opengis.net/gml");
					put("xlink", "http://www.w3.org/1999/xlink");
					put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
					put("wms", "http://www.opengis.net/wms"); // NC - wms added
					put("er","urn:cgi:xmlns:GGIC:EarthResource:1.1");
					put("gsml","urn:cgi:xmlns:CGI:GeoSciML:2.0");
					put("sa","http://www.opengis.net/sampling/1.0");
																// for wms tests
				}
			};


	public AbstractTestCase(String s) {
		super(s);
	}


	protected Document getRequest(String request) throws ConnectException, ConnectTimeoutException, UnknownHostException, Exception {
		HttpServiceCaller caller= new HttpServiceCaller();
		GetMethod method=new GetMethod(request);

		List<NameValuePair> options = new ArrayList<NameValuePair>();
		options.addAll(this.extractQueryParams(request));
		method.setQueryString(options.toArray(new NameValuePair[options.size()]));
		String response=caller.getMethodResponseAsString(method);

		Document doc=XMLUnit.buildControlDocument(response);

		return doc;
	}

	 /**
     * Returns a list of NameValuePair objects representing the
     * URL query parameters of url (if any)
     * @param url
     * @return
     */
    protected List<NameValuePair> extractQueryParams(String url) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String[] parts = url.split("\\?");
        if (parts.length != 2) {
            return params;
        }

        String[] queryParams = parts[1].split("&");
        for (String queryParam : queryParams) {
            String[] kvp = queryParam.split("=");
            if (kvp.length == 2) {
                params.add(new NameValuePair(kvp[0], kvp[1]));
            }
        }

        return params;
    }

	protected Document postRequest(String request, String xml) throws ConnectException, ConnectTimeoutException, UnknownHostException, Exception {
		HttpServiceCaller caller= new HttpServiceCaller();
		PostMethod method=new PostMethod(request);
		method.setRequestEntity(new StringRequestEntity(xml,"text/xml", "ISO-8859-1"));
		String response=caller.getMethodResponseAsString(method);
		Document doc=XMLUnit.buildControlDocument(response);
		return doc;
	}

	protected String prettyString(Document document) {
		OutputStream output = new ByteArrayOutputStream();
		prettyPrint(document, output);
		return output.toString();
	}

	/**
	 * Pretty-print a {@link Document} to an {@link OutputStream}.
	 *
	 * @param document
	 *            document to be prettified
	 * @param output
	 *            stream to which output is written
	 */
	protected void prettyPrint(Document document, OutputStream output) {
		OutputFormat format = new OutputFormat(document);
		// setIndenting must be first as it resets indent and line width
		format.setIndenting(true);
		format.setIndent(4);
		format.setLineWidth(200);
		XMLSerializer serializer = new XMLSerializer(output, format);
		try {
			serializer.serialize(document);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Assert that there are count matches of and XPath expression in a
	 * document.
	 *
	 * @param count
	 *            expected number of matches
	 * @param xpath
	 *            XPath expression
	 * @param document
	 *            document under test
	 */
	protected void assertXpathCount(int count, String xpath, Document document) {
		assertEquals(count, getMatchingNodes(xpath, document).getLength());
	}

	/**
	 * Return the list of nodes in a document that match an XPath expression.
	 *
	 * @param xpath
	 *            XPath expression
	 * @param document
	 *            the document under test
	 * @return list of matching nodes
	 */
	protected NodeList getMatchingNodes(String xpath, Document document) {
		try {
			return getXpathEngine().getMatchingNodes(xpath, document);
		} catch (XpathException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Return the XpathEngine, configured for this namespace context.
	 *
	 * <p>
	 *
	 * Note that the engine is configured lazily, to ensure that the mock data
	 * has been created and is ready to report data namespaces, which are then
	 * put into the namespace context.
	 *
	 * @return configured XpathEngine
	 */
	private XpathEngine getXpathEngine() {
		if (xpathEngine == null) {
			xpathEngine = XMLUnit.newXpathEngine();
			Map<String, String> namespaces = new HashMap<String, String>();
			namespaces.putAll(WFS_NAMESPACES);
			xpathEngine.setNamespaceContext(new SimpleNamespaceContext(
					namespaces));
		}
		return xpathEngine;
	}

	/**
     * Assertion that the flattened value of an XPath expression in document is equal to the
     * expected value.
     *
     * @param expected
     *            expected value of expression
     * @param xpath
     *            XPath expression
     * @param document
     *            the document under test
     */
    protected void assertXpathEvaluatesTo(String expected, String xpath, Document document) {
        assertEquals(expected, evaluate(xpath, document));
    }

    /**
     * Return the flattened value corresponding to an XPath expression from a document.
     *
     * @param xpath
     *            XPath expression
     * @param document
     *            the document under test
     * @return flattened string value
     */
    protected String evaluate(String xpath, Document document) {
        try {
            return getXpathEngine().evaluate(xpath, document);
        } catch (XpathException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addNamespace(String prefix,String uri){
    	this.WFS_NAMESPACES.put(prefix, uri);
    }
}
