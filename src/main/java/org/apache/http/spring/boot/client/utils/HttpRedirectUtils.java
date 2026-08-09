package org.apache.http.spring.boot.client.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * Helpers for transparently following HTTP redirect responses during Commons HttpClient 3.x
 * request execution, preserving the request parameters, body or {@code RequestEntity} across
 * the redirect chain.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public abstract class HttpRedirectUtils {

	/**
	 * Follow a redirect for a POST request that carries a {@link RequestEntity} body.
	 * @param httpclient the client used to execute the redirected request
	 * @param httpMethod the original POST method
	 * @param statuscode the status code returned by the previous execution
	 * @param charset the charset used to encode the request
	 * @param headers additional request headers, may be {@code null}
	 * @param requestEntity the request entity to re-send on the redirected request
	 * @return the final executed {@link PostMethod} (possibly the original method when no
	 *         redirect occurs)
	 * @throws HttpException if HTTP-level processing fails
	 * @throws IOException if an I/O error occurs
	 */
	public static <T> PostMethod stripRedirect(HttpClient httpclient,PostMethod httpMethod,int statuscode, String charset,
			Map<String, String> headers,RequestEntity requestEntity) throws HttpException, IOException {
		// 如果是重定向则需进一步处理
		PostMethod redirectMethod = HttpRequestUtils.getHttpRedirect(httpMethod, statuscode , charset, headers);
		// 方法被重定向
		if(redirectMethod != null){
			// sets参数
			redirectMethod.setRequestEntity(requestEntity);
			// 执行request
			int newStatuscode = httpclient.executeMethod(redirectMethod);
			// 如果是重定向则需进一步处理
			PostMethod newRedirectMethod = HttpRequestUtils.getHttpRedirect(redirectMethod, newStatuscode , charset, headers);
			// 再次方法被重定向
			if(newRedirectMethod != null){
				return stripRedirect(httpclient,httpMethod,statuscode,charset,headers,requestEntity);
			}
			return redirectMethod;
		}
		return httpMethod;
	}

	/**
	 * Follow a redirect for a GET request whose query parameters are expressed as an array of
	 * {@link NameValuePair}.
	 * @param httpclient the client used to execute the redirected request
	 * @param httpMethod the original GET method
	 * @param statuscode the status code returned by the previous execution
	 * @param charset the charset used to encode the request
	 * @param headers additional request headers, may be {@code null}
	 * @param nameValuePairs the query parameters to re-send on the redirected request
	 * @return the final executed {@link GetMethod} (possibly the original method when no
	 *         redirect occurs)
	 * @throws HttpException if HTTP-level processing fails
	 * @throws IOException if an I/O error occurs
	 */
	public static GetMethod stripRedirect(HttpClient httpclient,GetMethod httpMethod, int statuscode, String charset,
			Map<String, String> headers, NameValuePair[] nameValuePairs) throws HttpException, IOException {
		// 如果是重定向则需进一步处理
		GetMethod redirectMethod = HttpRequestUtils.getHttpRedirect(httpMethod, statuscode , charset, headers);
		// 方法被重定向
		if(redirectMethod != null){
			// sets参数
			httpMethod.setQueryString(nameValuePairs);
			// 执行request
			int newStatuscode = httpclient.executeMethod(redirectMethod);
			// 如果是重定向则需进一步处理
			GetMethod newRedirectMethod = HttpRequestUtils.getHttpRedirect(redirectMethod, newStatuscode , charset, headers);
			// 再次方法被重定向
			if(newRedirectMethod != null){
				return stripRedirect(httpclient,httpMethod,statuscode,charset,headers,nameValuePairs);
			}
			return redirectMethod;
		}
		return httpMethod;
	}

	/**
	 * Follow a redirect for a POST request whose body is expressed as a map of form parameters.
	 * @param baseURL the original target URL
	 * @param httpclient the client used to execute the redirected request
	 * @param httpMethod the original POST method
	 * @param statuscode the status code returned by the previous execution
	 * @param charset the charset used to encode the request
	 * @param contentType the {@code Content-Type} header value
	 * @param headers additional request headers, may be {@code null}
	 * @param paramsMap the form parameters to re-send on the redirected request
	 * @return the final executed {@link PostMethod} (possibly the original method when no
	 *         redirect occurs)
	 * @throws HttpException if HTTP-level processing fails
	 * @throws IOException if an I/O error occurs
	 */
	public static PostMethod stripRedirect(String baseURL, HttpClient httpclient,PostMethod httpMethod, int statuscode, String charset,
			String contentType, Map<String, String> headers, Map<String, Object> paramsMap) throws HttpException, IOException {
		// 如果是重定向则需进一步处理
		PostMethod redirectMethod = HttpRequestUtils.getHttpRedirect(httpMethod, statuscode , charset, headers);
		// 方法被重定向
		if(redirectMethod != null){
			// sets参数
			HttpRequestUtils.setHttpMethod(httpMethod, baseURL, paramsMap, charset, contentType, headers);
			// 执行request
			int newStatuscode = httpclient.executeMethod(redirectMethod);
			// 如果是重定向则需进一步处理
			PostMethod newRedirectMethod = HttpRequestUtils.getHttpRedirect(redirectMethod, newStatuscode , charset, headers);
			// 再次方法被重定向
			if(newRedirectMethod != null){
				return stripRedirect(baseURL, httpclient, httpMethod, statuscode, charset, contentType, headers, paramsMap);
			}
			return redirectMethod;
		}
		return httpMethod;
	}
	
	
}
