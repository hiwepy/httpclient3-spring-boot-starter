package org.apache.http.spring.boot.client.handler;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;

/**
 * Strategy interface for processing an HttpClient 3.x response, encapsulating both client-side
 * pre-processing and the conversion of the response into a typed result.
 * @param <T> the type produced by this handler
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public interface ResponseHandler<T> {

	/**
	 * Pre-process the {@link HttpClient} before the request is executed (for example, to set
	 * credentials or default parameters).
	 * @param httpclient the client about to execute the request
	 */
	void handleClient(HttpClient httpclient);
	
    /**
     * Process the executed HTTP method and produce a value from its response.
     *
     * @param httpMethod the executed {@link HttpMethodBase}
     * @return a value determined by the response
     *
     * @throws IOException in case of a problem or the connection was aborted
     */
    T handleResponse(HttpMethodBase httpMethod) throws IOException;

}
