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

@Configuration
@ConditionalOnClass({ HttpClient.class })
@EnableConfigurationProperties(HttpclientProperties.class)
public class HttpclientAutoConfiguration {

	protected static Logger LOG = LoggerFactory.getLogger(HttpclientAutoConfiguration.class);
	
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
