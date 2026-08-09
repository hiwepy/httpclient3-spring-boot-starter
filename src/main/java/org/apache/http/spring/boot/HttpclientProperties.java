package org.apache.http.spring.boot;

import java.util.Properties;

import org.apache.http.spring.boot.client.property.HttpConnectionManagerProperties;
import org.apache.http.spring.boot.client.property.HttpConnectionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties bound to the {@value #PREFIX} prefix for tuning the Commons
 * HttpClient 3.x connection manager and underlying connection, plus optional default request
 * headers and per-host parameters.
 * <p>
 * Default request headers may be specified as key/value entries under {@code httpclient.headers};
 * per-host parameters may be specified as {@code <host>-<paramKey>=<paramValue>} entries under
 * {@code httpclient.hosts}.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = HttpclientProperties.PREFIX)
public class HttpclientProperties {

	public static final String PREFIX = "httpclient";
	
	@NestedConfigurationProperty
	private HttpConnectionManagerProperties connectionManager = new HttpConnectionManagerProperties();
	
	@NestedConfigurationProperty
	private HttpConnectionProperties connection = new HttpConnectionProperties();
	
	/**
	 * Default request headers to be sent with every request, expressed as a {@link Properties}
	 * map of header name to header value.
	 */
	private Properties headers = new Properties();

	/**
	 * Per-host parameter overrides expressed as a {@link Properties} map. Keys follow the
	 * pattern {@code <host>-<paramKey>}, for example {@code 10.71.33.1-keepAlive=...}.
	 */
	private Properties hosts = new Properties();
	

	/**
	 * Return the nested connection-manager tuning properties.
	 * @return the {@link HttpConnectionManagerProperties} bound under {@code connection-manager}
	 */
	public HttpConnectionManagerProperties getConnectionManager() {
		return connectionManager;
	}

	/**
	 * Set the nested connection-manager tuning properties.
	 * @param connectionManager the properties to bind
	 */
	public void setConnectionManager(HttpConnectionManagerProperties connectionManager) {
		this.connectionManager = connectionManager;
	}

	/**
	 * Return the nested connection tuning properties.
	 * @return the {@link HttpConnectionProperties} bound under {@code connection}
	 */
	public HttpConnectionProperties getConnection() {
		return connection;
	}

	/**
	 * Set the nested connection tuning properties.
	 * @param connection the properties to bind
	 */
	public void setConnection(HttpConnectionProperties connection) {
		this.connection = connection;
	}

	/**
	 * Return the default request headers to apply to every request.
	 * @return a {@link Properties} map of header name to header value
	 */
	public Properties getHeaders() {
		return headers;
	}

	/**
	 * Set the default request headers to apply to every request.
	 * @param headers a {@link Properties} map of header name to header value
	 */
	public void setHeaders(Properties headers) {
		this.headers = headers;
	}

	/**
	 * Return the per-host parameter overrides.
	 * @return a {@link Properties} map keyed by {@code <host>-<paramKey>}
	 */
	public Properties getHosts() {
		return hosts;
	}

	/**
	 * Set the per-host parameter overrides.
	 * @param hosts a {@link Properties} map keyed by {@code <host>-<paramKey>}
	 */
	public void setHosts(Properties hosts) {
		this.hosts = hosts;
	}
	
}
