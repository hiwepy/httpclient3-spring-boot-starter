package org.apache.http.spring.boot;

import java.util.Properties;

import org.apache.http.spring.boot.property.HttpConnectionManagerProperties;
import org.apache.http.spring.boot.property.HttpConnectionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = HttpclientProperties.PREFIX)
public class HttpclientProperties {

	public static final String PREFIX = "httpclient";
	
	@NestedConfigurationProperty
	private HttpConnectionManagerProperties connectionManager = new HttpConnectionManagerProperties();
	
	@NestedConfigurationProperty
	private HttpConnectionProperties connection = new HttpConnectionProperties();
	
	/**
	 * 详细参数参见：com.google.code.kaptcha.Constants
	 */
	private Properties headers = new Properties();

	/**
	 * 详细参数参见：com.google.code.kaptcha.Constants
	 * 
	 * 10.71.33.1-dns = 
	 * 10.71.33.1-keepAlive = 
	 * 
	 */
	private Properties hosts = new Properties();
	

	public HttpConnectionManagerProperties getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(HttpConnectionManagerProperties connectionManager) {
		this.connectionManager = connectionManager;
	}

	public HttpConnectionProperties getConnection() {
		return connection;
	}

	public void setConnection(HttpConnectionProperties connection) {
		this.connection = connection;
	}

	public Properties getHeaders() {
		return headers;
	}

	public void setHeaders(Properties headers) {
		this.headers = headers;
	}

	public Properties getHosts() {
		return hosts;
	}

	public void setHosts(Properties hosts) {
		this.hosts = hosts;
	}
	
}
