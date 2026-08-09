package org.apache.http.spring.boot;


import javax.net.ssl.SSLContext;
import javax.security.cert.X509Certificate;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.http.spring.boot.client.property.HttpConnectionManagerProperties;
import org.apache.http.spring.boot.client.property.HttpConnectionManagerProperties.ManagerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for the Apache Commons HttpClient 3.x integration.
 * <p>
 * Registers the {@link HttpConnectionManager} bean (multi-threaded or simple, depending on
 * {@link HttpclientProperties#getConnectionManager()}) and an {@link IdleConnectionTimeoutThread}
 * that periodically evicts stale connections.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ HttpClient.class })
@EnableConfigurationProperties(HttpclientProperties.class)
public class HttpclientAutoConfiguration {

	protected static Logger LOG = LoggerFactory.getLogger(HttpclientAutoConfiguration.class);

	/**
	 * Build and start the background thread that closes idle/expired connections held by the
	 * configured connection manager.
	 * @param properties the {@link HttpclientProperties} driving timeout and interval settings
	 * @param httpConnectionManager the manager whose idle connections should be reaped
	 * @return a started {@link IdleConnectionTimeoutThread}, or {@code null} if initialisation fails
	 */
	@Bean
	public IdleConnectionTimeoutThread heartbeatThread(HttpclientProperties properties,
			HttpConnectionManager httpConnectionManager) {
		try {
			
			IdleConnectionTimeoutThread idleThread = new IdleConnectionTimeoutThread();
			HttpConnectionManagerProperties mgrProps = properties.getConnectionManager().getInitedParams();

			// 定时清除失效链接
			idleThread.setConnectionTimeout(mgrProps.getConnectionTimeout());
			idleThread.setTimeoutInterval(mgrProps.getTimeoutInterval());
			idleThread.addConnectionManager(httpConnectionManager);
			idleThread.start();

			return idleThread;
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Create the {@link HttpConnectionManager} bean based on the configured
	 * {@link HttpConnectionManagerProperties.ManagerType}. When the manager type is
	 * {@code MULTI_THREADED} a {@link MultiThreadedHttpConnectionManager} is returned; otherwise
	 * a {@link SimpleHttpConnectionManager} is returned honouring the {@code alwaysClose} flag.
	 * @param properties the {@link HttpclientProperties} driving manager selection
	 * @return the configured {@link HttpConnectionManager}, or {@code null} if creation fails
	 */
	@Bean
	public HttpConnectionManager httpConnectionManager(HttpclientProperties properties) {
		try {
			HttpConnectionManagerProperties mgrProps = properties.getConnectionManager();
			if (ManagerType.MULTI_THREADED.equals(mgrProps.getType())) {
				return new MultiThreadedHttpConnectionManager();
			}
			
			return new SimpleHttpConnectionManager(mgrProps.isAlwaysClose());
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}
		return null;
	}
	

}
