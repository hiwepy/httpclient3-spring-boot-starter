package org.apache.http.spring.boot.client.handler;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.apache.http.spring.boot.client.exception.HttpResponseException;
import org.apache.http.spring.boot.client.utils.IOUtils;

import com.thoughtworks.xstream.XStream;

/**
 * {@link ResponseHandler} implementation that deserialises an XML response body into a Java
 * object graph using XStream.
 * <p>A {@link HttpResponseException} is thrown when the status code is outside the 2xx range.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class ObjectResponseHandler implements ResponseHandler<Object> {

	/** XStream instance used to deserialise the XML response body. */
	protected XStream xstream = new XStream();
	
	@Override
	public void handleClient(HttpClient httpclient) {
		
	}
	
	/**
	 * Deserialise the response body into a Java object graph.
	 * @param httpMethod the executed HTTP method
	 * @return the deserialised object
	 * @throws IOException if the status is not 2xx or reading the body fails
	 */
	@Override
	public Object handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			InputStream input = null;
			try {
				// 从request中取得输入流
				input = httpMethod.getResponseBodyAsStream();
				return xstream.fromXML(input);
			} finally {
				// 释放资源
				IOUtils.closeQuietly(input);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}
 
}
