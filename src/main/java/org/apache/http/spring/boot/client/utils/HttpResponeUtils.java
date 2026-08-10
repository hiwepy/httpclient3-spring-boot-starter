package org.apache.http.spring.boot.client.utils;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethodBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helpers for inspecting Commons HttpClient 3.x responses.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public abstract class HttpResponeUtils{
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpResponeUtils.class);

	/**
	 * Return the raw {@code Content-Type} header value from the response.
	 * @param httpMethod the executed method whose response headers should be inspected
	 * @return the {@code Content-Type} header value
	 */
	public static String getContentType(HttpMethodBase httpMethod) {
		Header header = httpMethod.getResponseHeader(HttpHeaders.CONTENT_TYPE);
		/*HeaderElement[] elements = header.getElements();
		for (HeaderElement elem : elements) {
			LOG.debug(elem.getName() + " = " + elem.getValue());
			if ("gzip".equalsIgnoreCase(elem.getName())) {
				contentType = elem.getValue();
				break;
			}
		}*/
		return header.getValue();
	}
}
