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
package org.apache.http.spring.boot.client.property;

import org.apache.commons.httpclient.params.HttpConnectionParams;

/**
 * Extended {@link HttpConnectionParams} bound under {@code httpclient.connection}, providing
 * per-connection tuning (socket timeouts, buffer sizes, linger, etc.) for the Commons
 * HttpClient 3.x integration.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class HttpConnectionProperties extends HttpConnectionParams {

	/**
	 * Return this instance typed as {@link HttpConnectionParams} so it can be supplied directly
	 * to the underlying Commons HttpClient API.
	 * @return the bound {@link HttpConnectionParams}
	 */
	public HttpConnectionParams getHttpConnectionParams() {
		
		HttpConnectionParams params = this;
		
		return params;
		
	}
	
}
