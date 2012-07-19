package org.csiro.ServiceTester.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Example usage for post:
 *
 * PostMethod httpMethod = new PostMethod(serviceUrl);
 * httpMethod.setRequestEntity(new StringRequestEntity(sb.toString(),"text/xml", "ISO-8859-1"));
 *
 * Example usage for Get:
 * GetMethod method = new GetMethod(wmsUrl);
 * List<NameValuePair> options = new ArrayList<NameValuePair>();
 * options.add(new NameValuePair("service", "WMS"));
 * method.setQueryString(options.toArray(new NameValuePair[options.size()]));
 *
 * @author tey006
 *
 */

public class HttpServiceCaller {

	private final Log log = LogFactory.getLog(getClass());
	 /**
     * Makes a call to a http GetMethod and returns the response as a string.
     *
     * (Creates a new HttpClient for use with this request)
     *
     * @param method The method to be executed
     * @return
     * @throws Exception
     */
    public String getMethodResponseAsString(HttpMethodBase method) throws ConnectException, UnknownHostException, ConnectTimeoutException, Exception{
        return getMethodResponseAsString(method, new HttpClient());
    }

    /**
     * Makes a call to a http GetMethod and returns the response as a string.
     *
     * @param method The method to be executed
     * @param httpClient The client that will be used
     * @return
     * @throws Exception
     */
    public String getMethodResponseAsString(HttpMethodBase method, HttpClient httpClient) throws ConnectException, UnknownHostException, ConnectTimeoutException, Exception{
        //invoke the method
        this.invokeTheMethod(method, httpClient);

        //get the reponse before we close the connection
        //String response = method.getResponseBodyAsString();

        String response = responseToString(new BufferedInputStream(method.getResponseBodyAsStream()));

        //release the connection
        method.releaseConnection();

        log.trace("XML response from server:");
        log.trace("\n" + response);
        //return it
        return response;
    }

    /**
     * Invokes a method and returns the binary response as a stream.
     * (Creates a new HttpClient for use with this request)
     *
     * WARNING - ensure you call method.releaseConnection() AFTER you have finished reading the input stream.
     *
     * @param method The method to be executed
     * @return
     */
    public InputStream getMethodResponseAsStream(HttpMethodBase method) throws Exception {
        return this.getMethodResponseAsStream(method, new HttpClient());
    }

    /**
     * Invokes a method and returns the binary response as a stream.
     *
     * WARNING - ensure you call method.releaseConnection() AFTER you have finished reading the input stream.
     *
     * @param method The method to be executed
     * @param httpClient The client that will be used
     * @return
     */
    public InputStream getMethodResponseAsStream(HttpMethodBase method, HttpClient httpClient) throws Exception {
        //invoke the method
        this.invokeTheMethod(method, httpClient);

        return new BufferedInputStream(method.getResponseBodyAsStream());
    }

    /**
     * Invokes a httpmethod and takes care of some error handling.
     * @param method
     * @param httpClient
     */
    private void invokeTheMethod(HttpMethodBase method, HttpClient httpClient) throws Exception {

        log.debug("method=" + method.getURI());

        //create the connection manager and add it to the client
        HttpConnectionManager man = new SimpleHttpConnectionManager();
        man.setParams(new HttpConnectionParam());
        httpClient.setHttpConnectionManager(man);

        log.trace("Outgoing request headers: " + Arrays.toString(method.getRequestHeaders()));


        //make the call
        int statusCode = httpClient.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
            log.error(method.getStatusLine());

            //if its unavailable then throw updateCSWRecords connection exception
            if (statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE)
                throw new ConnectException();

            //if the response is not OK then throw an error
            throw new Exception("Returned status line: " + method.getStatusLine());
        }
    }

    /**
     * Convert a Buffered stream into a String.
     * @param stream
     * @return
     * @throws IOException
     */
    public String responseToString(BufferedInputStream stream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        stream.close();
        return stringBuffer.toString();
    }

    private class HttpConnectionParam extends HttpConnectionManagerParams{
    	  /**
         * Defines the maximum number of connections allowed per host configuration.
         * These values only apply to the number of connections from a particular instance
         * of HttpConnectionManager.
         * <p>
         * This parameter expects a value of type {@link java.util.Map}.  The value
         * should map instances of {@link org.apache.commons.httpclient.HostConfiguration}
         * to {@link Integer integers}.  The default value can be specified using
         * {@link org.apache.commons.httpclient.HostConfiguration#ANY_HOST_CONFIGURATION}.
         * </p>
         */
        public static final String MAX_HOST_CONNECTIONS = "http.connection-manager.max-per-host";

        /**
         * Defines the maximum number of connections allowed overall. This value only applies
         * to the number of connections from a particular instance of HttpConnectionManager.
         * <p>
         * This parameter expects a value of type {@link Integer}.
         * </p>
         */
        public static final String MAX_TOTAL_CONNECTIONS = "http.connection-manager.max-total";

        /**
         * Sets the default maximum number of connections allowed for a given
         * host config.
         *
         * @param maxHostConnections The default maximum.
         *
         * @see #MAX_HOST_CONNECTIONS
         */
        public void setDefaultMaxConnectionsPerHost(int maxHostConnections) {
            setMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION, maxHostConnections);
        }

        /**
         * Sets the maximum number of connections to be used for the given host config.
         *
         * @param hostConfiguration The host config to set the maximum for.  Use
         * {@link HostConfiguration#ANY_HOST_CONFIGURATION} to configure the default value
         * per host.
         * @param maxHostConnections The maximum number of connections, <code>> 0</code>
         *
         * @see #MAX_HOST_CONNECTIONS
         */
        public void setMaxConnectionsPerHost(
            HostConfiguration hostConfiguration,
            int maxHostConnections) {

            if (maxHostConnections <= 0) {
                throw new IllegalArgumentException("maxHostConnections must be greater than 0");
            }

            Map currentValues = (Map) getParameter(MAX_HOST_CONNECTIONS);
            // param values are meant to be immutable so we'll make a copy
            // to modify
            Map newValues = null;
            if (currentValues == null) {
                newValues = new HashMap();
            } else {
                newValues = new HashMap(currentValues);
            }
            newValues.put(hostConfiguration, new Integer(maxHostConnections));
            setParameter(MAX_HOST_CONNECTIONS, newValues);
        }

        /**
         * Gets the default maximum number of connections allowed for a given
         * host config.
         *
         * @return The default maximum.
         *
         * @see #MAX_HOST_CONNECTIONS
         */
        public int getDefaultMaxConnectionsPerHost() {
            return getMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION);
        }

        /**
         * Gets the maximum number of connections to be used for a particular host config.  If
         * the value has not been specified for the given host the default value will be
         * returned.
         *
         * @param hostConfiguration The host config.
         * @return The maximum number of connections to be used for the given host config.
         *
         * @see #MAX_HOST_CONNECTIONS
         */
        public int getMaxConnectionsPerHost(HostConfiguration hostConfiguration) {

            Map m = (Map) getParameter(MAX_HOST_CONNECTIONS);
            if (m == null) {
                // MAX_HOST_CONNECTIONS have not been configured, using the default value
                return MultiThreadedHttpConnectionManager.DEFAULT_MAX_HOST_CONNECTIONS;
            } else {
                Integer max = (Integer) m.get(hostConfiguration);
                if (max == null && hostConfiguration != HostConfiguration.ANY_HOST_CONFIGURATION) {
                    // the value has not been configured specifically for this host config,
                    // use the default value
                    return getMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION);
                } else {
                    return (
                            max == null
                            ? MultiThreadedHttpConnectionManager.DEFAULT_MAX_HOST_CONNECTIONS
                            : max.intValue()
                        );
                }
            }
        }

        /**
         * Sets the maximum number of connections allowed.
         *
         * @param maxTotalConnections The maximum number of connections allowed.
         *
         * @see #MAX_TOTAL_CONNECTIONS
         */
        public void setMaxTotalConnections(int maxTotalConnections) {
            setIntParameter(
                HttpConnectionManagerParams.MAX_TOTAL_CONNECTIONS,
                maxTotalConnections);
        }

        /**
         * Gets the maximum number of connections allowed.
         *
         * @return The maximum number of connections allowed.
         *
         * @see #MAX_TOTAL_CONNECTIONS
         */
        public int getMaxTotalConnections() {
            return getIntParameter(
                HttpConnectionManagerParams.MAX_TOTAL_CONNECTIONS,
                MultiThreadedHttpConnectionManager.DEFAULT_MAX_TOTAL_CONNECTIONS);
        }

    }

}
