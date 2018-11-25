/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
import org.apache.http.spring.boot.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientFactory {

	protected static Logger LOG = LoggerFactory.getLogger(HttpClientFactory.class);
	
	/**
	 * 普通http请求的HttpClient连接池
	 */
	private HttpConnectionManager httpConnectionManager = null;
	private boolean userManager = true;

	public HttpClientFactory(HttpConnectionManager httpConnectionManager) {
		this(httpConnectionManager,  true);
	}

	public HttpClientFactory(HttpConnectionManager httpConnectionManager,
			boolean userManager) {
		this.httpConnectionManager = httpConnectionManager;
		this.userManager = userManager;
	}
	
	public HttpClient getCloseableHttpClient() {
		HttpClient httpclient = null;
		try {
			httpclient = new HttpClient();
			if (userManager) {
				httpclient.setHttpConnectionManager(httpConnectionManager);
			}
			
			// 设置读取超时时间(单位毫秒)
			// httpClient.getParams().setParameter("http.socket.timeout",socket_timeout);
			// 设置连接超时时间(单位毫秒)
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
