package org.apache.http.spring.boot.client.utils;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTPS-aware companion to {@link HttpClientUtils} that, on class load, registers a Commons
 * HttpClient 3.x {@link Protocol} for {@code https://} using the default
 * {@link SSLProtocolSocketFactory} on port {@code 443}.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
public abstract class HttpsClientUtils extends HttpClientUtils {

	protected static Logger LOG = LoggerFactory.getLogger(HttpsClientUtils.class);
	
	static {

		try {
			
			Protocol easyhttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);  
		    Protocol.registerProtocol("https", easyhttps);   
	        
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}
	}

}
