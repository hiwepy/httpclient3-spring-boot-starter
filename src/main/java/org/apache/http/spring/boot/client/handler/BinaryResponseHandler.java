package org.apache.http.spring.boot.client.handler;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;

import org.apache.http.spring.boot.client.exception.HttpResponseException;

public class BinaryResponseHandler implements ResponseHandler<byte[]> {

	@Override
	public void handleClient(HttpClient httpclient) {
		
	}
	
	@Override
	public byte[] handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			byte[] content = null;
			try {
				content = httpMethod.getResponseBody();
			}  finally {
			}
			return content;
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
