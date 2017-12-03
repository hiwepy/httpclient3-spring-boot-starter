package org.apache.http.spring.boot.httputils.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;

import org.apache.http.spring.boot.httputils.exception.HttpResponseException;

/**
 * 
 * @author Administrator
 *
 */
public class StreamResponseHandler implements ResponseHandler<ByteArrayInputStream> {

	@Override
	public void handleClient(HttpClient httpclient) {
		
	}

	@Override
	public ByteArrayInputStream handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			try {
				// 响应内容
				return new ByteArrayInputStream(httpMethod.getResponseBody());
			} finally {
				
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
