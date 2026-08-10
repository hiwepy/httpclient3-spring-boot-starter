/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.http.spring.boot;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.http.spring.boot.client.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory that produces Commons HttpClient 3.x {@link HttpClient} instances sharing an optional
 * pooled {@link HttpConnectionManager}.
 * <p>
 * When {@code userManager} is {@code true} every client created by {@link #getCloseableHttpClient()}
 * is wired to the shared connection manager; otherwise each client manages its own connections.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class HttpClientFactory {

	protected static Logger LOG = LoggerFactory.getLogger(HttpClientFactory.class);
	
	/**
	 * Underlying connection manager used to serve the HTTP requests of every client produced by
	 * this factory; may be {@code null} when no shared pool is required.
	 */
	private HttpConnectionManager httpConnectionManager = null;
	private boolean userManager = true;

	/**
	 * Create a factory that uses the given connection manager and shares it with all produced
	 * clients.
	 * @param httpConnectionManager the shared connection manager to use
	 */
	public HttpClientFactory(HttpConnectionManager httpConnectionManager) {
		this(httpConnectionManager,  true);
	}

	/**
	 * Create a factory with explicit control over whether the connection manager is shared.
	 * @param httpConnectionManager the connection manager to use when {@code userManager} is {@code true}
	 * @param userManager whether produced clients should share the given connection manager
	 */
	public HttpClientFactory(HttpConnectionManager httpConnectionManager,
			boolean userManager) {
		this.httpConnectionManager = httpConnectionManager;
		this.userManager = userManager;
	}
	
	/**
	 * Build a new {@link HttpClient}. When connection-manager sharing is enabled the shared
	 * manager is bound to the client; on failure a plain {@code new HttpClient()} is returned.
	 * @return a configured {@link HttpClient} instance
	 */
	public HttpClient getCloseableHttpClient() {
		HttpClient httpclient = null;
		try {
			httpclient = new HttpClient();
			if (userManager) {
				httpclient.setHttpConnectionManager(httpConnectionManager);
			}
			
			// sets读取timeout时间(单位毫秒)
			// httpClient.getParams().setParameter("http.socket.timeout",socket_timeout);
			// sets连接timeout时间(单位毫秒)
			// httpClient.getParams().setParameter("http.connection.timeout",connection_timeout);
			// httpClient.getParams().setParameter("http.connection-manager.timeout",100000000L);
		} catch (Exception e) {
			LOG.error("Exception", e);
			httpclient = new HttpClient();
		}
		//httpclient.getHostConfiguration().getParams().setParameter("http.default-headers", getDefaultHeaders());
		return httpclient;
	}
	
	
}
