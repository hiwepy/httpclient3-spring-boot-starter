package org.apache.http.spring.boot.client.exception;

import java.io.IOException;

/**
 * @类名称 : HttpResponseException.java
 * @类描述 ：
 * @创建人 ：hiwepy
 * @创建时间 ：2016年4月27日 上午11:56:28
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
@SuppressWarnings("serial")
public class HttpResponseException extends IOException {
	
	private int statusCode = 200;

	public HttpResponseException(String message) {
		super(message);
	}
	
	public HttpResponseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HttpResponseException(final int statusCode, final String s) {
		super(s);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

}
