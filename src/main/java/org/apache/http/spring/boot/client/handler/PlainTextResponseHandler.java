package org.apache.http.spring.boot.client.handler;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;

import org.apache.http.spring.boot.client.exception.HttpResponseException;

/**
 * {@link ResponseHandler} implementation that returns the response body as a plain
 * {@link String}.
 * <p>A {@link HttpResponseException} is thrown when the status code is outside the 2xx range.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class PlainTextResponseHandler implements ResponseHandler<String> {

	@Override
	public void handleClient(HttpClient httpclient) {
		
	}

	/**
	 * Return the response body as a {@link String}.
	 * @param httpMethod the executed HTTP method
	 * @return the response body as a string
	 * @throws IOException if the status is not 2xx or reading the body fails
	 */
	@Override
	public String handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			try {
				// 响应内容
				return httpMethod.getResponseBodyAsString();
			}  finally {
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
