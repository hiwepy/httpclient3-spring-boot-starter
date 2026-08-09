package org.apache.http.spring.boot.client.exception;

import java.io.IOException;

/**
 * Signals that an HTTP request did not complete successfully, either because the server
 * returned a non-2xx status code or because the response body could not be parsed.
 * <p>The offending status code (defaulting to {@code 200}) is exposed via
 * {@link #getStatusCode()}.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class HttpResponseException extends IOException {
	
	private int statusCode = 200;

	/**
	 * Create a new exception with the given detail message and the default status code.
	 * @param message the detail message
	 */
	public HttpResponseException(String message) {
		super(message);
	}
	
	/**
	 * Create a new exception with the given detail message and underlying cause.
	 * @param message the detail message
	 * @param cause the cause of this exception
	 */
	public HttpResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new exception carrying the HTTP status code that triggered it.
	 * @param statusCode the HTTP status code returned by the server
	 * @param s the detail message, typically the status-line reason phrase
	 */
	public HttpResponseException(final int statusCode, final String s) {
		super(s);
		this.statusCode = statusCode;
	}

	/**
	 * Return the HTTP status code associated with this exception.
	 * @return the status code, or {@code 200} if none was supplied
	 */
	public int getStatusCode() {
		return this.statusCode;
	}

}
