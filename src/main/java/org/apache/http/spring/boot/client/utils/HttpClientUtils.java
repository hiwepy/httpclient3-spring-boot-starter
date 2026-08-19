package org.apache.http.spring.boot.client.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.spring.boot.client.ContentType;
import org.apache.http.spring.boot.client.handler.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience helpers for executing GET and POST requests with Commons HttpClient 3.x.
 * <p>
 * Each {@code httpRequestWith*} method creates an {@link HttpClient}, applies the supplied
 * {@link ResponseHandler} for client pre-processing, follows up to one redirect, processes the
 * final response through the handler and unconditionally releases the connection.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public abstract class HttpClientUtils extends HttpRequestUtils {

	protected static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);
	

	/**
	 * Execute a GET request against {@code baseURL} with no parameters and process the response
	 * with {@code handler}.
	 * @param baseURL the target URL
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithGet(String baseURL,ResponseHandler<T> handler) throws IOException {
		return httpRequestWithGet(baseURL, null, handler);
	}

	/**
	 * Execute a GET request against {@code baseURL} with the given parameters (UTF-8 charset)
	 * and process the response with {@code handler}.
	 * @param baseURL the target URL
	 * @param paramsMap the query parameters to append
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithGet(String baseURL,
			Map<String, Object> paramsMap, ResponseHandler<T> handler)
			throws IOException {
		return httpRequestWithGet(baseURL, paramsMap, ContentType.UTF_8, handler);
	}

	/**
	 * Execute a GET request against {@code baseURL} with the given parameters and charset and
	 * process the response with {@code handler}.
	 * @param baseURL the target URL
	 * @param paramsMap the query parameters to append
	 * @param charset the charset used to encode the query string
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithGet(String baseURL,
			Map<String, Object> paramsMap, String charset,
			ResponseHandler<T> handler) throws IOException {
		return httpRequestWithGet(baseURL, paramsMap, charset, null, handler);
	}

	/**
	 * Execute a GET request against {@code baseURL} with the given parameters, charset and
	 * custom request headers and process the response with {@code handler}.
	 * @param baseURL the target URL
	 * @param paramsMap the query parameters to append
	 * @param charset the charset used to encode the query string
	 * @param headers additional request headers, may be {@code null}
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithGet(String baseURL,
			Map<String, Object> paramsMap, String charset,
			Map<String, String> headers, ResponseHandler<T> handler)
			throws IOException {
		// createsdefault的httpClient实例.
		HttpClient httpclient = new HttpClient(new SimpleHttpConnectionManager());
		// GetMethod对象
		GetMethod httpMethod = null;
		try {
			//对HttpClient进行预处理
			handler.handleClient(httpclient);
			// createshttpget
			httpMethod = getHttpGet(baseURL, charset, headers);
			 //初始参数集合对象
	    	List<NameValuePair> nameValueList    = HttpURIUtils.buildNameValuePairs(baseURL , paramsMap);
	        NameValuePair[] nameValuePairs = nameValueList.toArray(new NameValuePair[nameValueList.size()]);
	        // sets参数
	        httpMethod.setQueryString(nameValuePairs);
			// 执行request
			int statuscode = httpclient.executeMethod(httpMethod);
			// 最终执行的方法，如果没有重定向,则与原始request对象是同一个对象
			GetMethod lastMethod = HttpRedirectUtils.stripRedirect(httpclient, httpMethod, statuscode, charset, headers, nameValuePairs);
			// 处理最终的response结果
			return handler.handleResponse(lastMethod);
		} catch (Exception e) {
			handleException(e);
			return null;
		} finally {
			releaseQuietly(httpMethod);
		}
	}

	/**
	 * Execute a POST request against {@code baseURL} with the given parameters (UTF-8 charset,
	 * {@code application/x-www-form-urlencoded} body) and process the response with
	 * {@code handler}.
	 * @param baseURL the target URL
	 * @param paramsMap the form parameters to send
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithPost(String baseURL,
			Map<String, Object> paramsMap, ResponseHandler<T> handler)
			throws IOException {
		return httpRequestWithPost(baseURL, paramsMap, ContentType.UTF_8, handler);
	}

	/**
	 * Execute a POST request with {@code Content-Type: application/x-www-form-urlencoded}.
	 */
	public static <T> T httpRequestWithPost(String baseURL,
			Map<String, Object> paramsMap, String charset,
			ResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, paramsMap, charset, ContentType.APPLICATION_FORM_URLENCODED + "; charset=" + charset, handler);
	}

	/**
	 * Execute a POST request against {@code baseURL} with the given parameters, charset and
	 * explicit {@code Content-Type} and process the response with {@code handler}.
	 * @param baseURL the target URL
	 * @param paramsMap the form parameters to send
	 * @param charset the charset used to encode the body
	 * @param contentType the {@code Content-Type} header value
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithPost(String baseURL,Map<String, Object> paramsMap, final String charset, String contentType, ResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, paramsMap, charset, contentType, null, handler);
	}

	/**
	 * Execute a POST request against {@code baseURL} with the given parameters, charset,
	 * {@code Content-Type} and custom request headers and process the response with
	 * {@code handler}.
	 * @param baseURL the target URL
	 * @param paramsMap the form parameters to send
	 * @param charset the charset used to encode the body
	 * @param contentType the {@code Content-Type} header value
	 * @param headers additional request headers, may be {@code null}
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithPost(String baseURL,
			Map<String, Object> paramsMap, String charset, String contentType,
			Map<String, String> headers, ResponseHandler<T> handler)
			throws IOException {
		// 定义初始对象
		PostMethod httpMethod = null;
		// createsdefault的httpClient实例.
		HttpClient httpclient = new HttpClient(new SimpleHttpConnectionManager());
		try {
			// 对HttpClient进行预处理
			handler.handleClient(httpclient);
			// 得到request方法
			httpMethod = getHttpPost(baseURL, charset, headers);
			// sets参数
			setHttpMethod(httpMethod, baseURL, paramsMap, charset, contentType, headers);
			// 执行request
			int statuscode = httpclient.executeMethod(httpMethod);
			// 最终执行的方法，如果没有重定向,则与原始request对象是同一个对象
			PostMethod lastMethod = HttpRedirectUtils.stripRedirect(baseURL, httpclient, httpMethod, statuscode, charset, contentType,  headers, paramsMap);
			// 处理最终的response结果
			return handler.handleResponse(lastMethod);
		} catch (Exception e) {
			handleException(e);
			return null;
		} finally {
			releaseQuietly(httpMethod);
		}
	}

	/**
	 * Execute a POST request against {@code baseURL} with a JSON body
	 * ({@code Content-Type: application/json}).
	 * @param baseURL the target URL
	 * @param json the JSON body to send
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithPost(String baseURL, String json,ResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, json, ContentType.UTF_8, null, handler);
	}

	/**
	 * Execute a POST request against {@code baseURL} with a JSON body
	 * ({@code Content-Type: application/json}).
	 * @param baseURL the target URL
	 * @param json the JSON body to send
	 * @param charset the charset used to encode the body
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithPost(String baseURL, String json,String charset, ResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, json, charset, null, handler);
	}

	/**
	 * Execute a POST request against {@code baseURL} with a JSON body
	 * ({@code Content-Type: application/json}) and the given custom headers.
	 * @param baseURL the target URL
	 * @param json the JSON body to send
	 * @param charset the charset used to encode the body
	 * @param headers additional request headers, may be {@code null}
	 * @param handler the response handler
	 * @param <T> the result type produced by the handler
	 * @return the handler result, or {@code null} if the request fails
	 * @throws IOException if an I/O error occurs that is not handled internally
	 */
	public static <T> T httpRequestWithPost(String baseURL, String json,String charset, Map<String, String> headers,ResponseHandler<T> handler) throws IOException {
		// 定义初始对象
		PostMethod httpMethod = null;
		// createsdefault的httpClient实例.
		HttpClient httpclient = new HttpClient(new SimpleHttpConnectionManager());
		try {
			//对HttpClient进行预处理
			handler.handleClient(httpclient);
			// 如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
			httpMethod = HttpRequestUtils.getHttpRequest(new PostMethod(baseURL), headers);
			// 将JSON进行UTF-8encoding,以便传输中文
			String encoderJson = URLEncoder.encode(json != null ? json : "{}", charset);
			// 构建字符串the configuration properties
			RequestEntity requestEntity = new StringRequestEntity(encoderJson,ContentType.TEXT_JSON, charset );
			// setsrequest头info
			httpMethod.setRequestHeader(HttpHeaders.CONTENT_ENCODING, charset);
			httpMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON);
			// sets参数
			httpMethod.setRequestEntity(requestEntity);
			// 执行request
			int statuscode = httpclient.executeMethod(httpMethod);
			// 最终执行的方法，如果没有重定向,则与原始request对象是同一个对象
			PostMethod lastMethod = HttpRedirectUtils.stripRedirect(httpclient, httpMethod, statuscode, charset, headers, requestEntity);
			// 处理最终的response结果
			return handler.handleResponse(lastMethod);
		} catch (Exception e) {
			handleException(e);
			return null;
		} finally {
			releaseQuietly(httpMethod);
		}

	}

	/**
	 * Unconditionally release the connection held by an {@link HttpMethodBase}, swallowing any
	 * exception. Intended for use from {@code finally} blocks.
	 * <p>
	 * Example Code:
	 * <pre>
	 * MethodPost httpRequest = null;
	 * try {
	 * 	 httpRequest = new MethodPost(baseURL);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	 HttpClientUtils.releaseQuietly(httpRequest);
	 * }
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @param httpRequest the {@link HttpMethodBase} whose connection should be released, may be
	 *                    {@code null} or already closed
	 */
	public static void releaseQuietly(HttpMethodBase httpRequest) {
		// 关闭连接,释放资源
		if (httpRequest != null) {
			try {
				httpRequest.releaseConnection();
				httpRequest = null;
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Log the given exception at the appropriate level and rethrow it as an {@link IOException}.
	 * @param e the exception to handle
	 * @throws IOException always, wrapping {@code e}
	 */
	public static void handleException(Exception e) throws IOException {
		if (e instanceof SocketTimeoutException) {
			LOG.error("连接timeout:" + e.getLocalizedMessage());
		} else if (e instanceof HttpException) {
			LOG.error("读取外部服务器数据failure:" + e.getLocalizedMessage());
		} else if (e instanceof UnknownHostException) {
			LOG.error("request的hostaddress无效:" + e.getLocalizedMessage());
		} else if (e instanceof IOException) {
			LOG.error("向外部接口发送数据failure:" + e.getLocalizedMessage());
		} 
		throw new IOException(e);
	}

}
