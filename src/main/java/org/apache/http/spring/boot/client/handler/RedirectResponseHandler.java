package org.apache.http.spring.boot.client.handler;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.spring.boot.client.exception.HttpResponseException;

/**
 * {@link ResponseHandler} implementation that extracts the {@code Location} header from a
 * redirect response.
 * <p>The handler treats HTTP status codes {@code 301}, {@code 302}, {@code 303} and
 * {@code 307} as redirects and returns the target URL from the {@code location} response
 * header; any other status code produces an {@link HttpResponseException}.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class RedirectResponseHandler implements ResponseHandler<String> {

	protected static Logger LOG = LoggerFactory.getLogger(RedirectResponseHandler.class);

	@Override
	public void handleClient(HttpClient httpclient) {

	}

	/**
	 * If the response is a redirect, extract and return the target URL from the
	 * {@code location} response header.
	 * @param httpMethod the executed HTTP method
	 * @return the redirect target URL
	 * @throws HttpResponseException if the response is not a redirect or carries no location header
	 * @throws IOException if reading the header fails
	 */
	@Override
	public String handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		// 检查whether重定向
		int statuscode = statusLine.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// 读取新的 URL address
			Header header = httpMethod.getResponseHeader("location");
			if (header != null) {
				// 从头中取出转向的address
				String redirectURI = header.getValue();
				if ((redirectURI == null) || (redirectURI.equals(""))) {
					redirectURI = "/";
				}
				LOG.debug("Redirect:" + redirectURI);
				return redirectURI;
			} else {
				throw new HttpResponseException("Invalid redirect .");
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(),statusLine.getReasonPhrase());
		}
	}
}