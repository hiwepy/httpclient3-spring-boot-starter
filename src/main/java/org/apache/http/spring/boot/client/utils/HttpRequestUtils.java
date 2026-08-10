package org.apache.http.spring.boot.client.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.spring.boot.client.ContentType;
import org.apache.http.spring.boot.client.exception.HttpResponseException;
import org.apache.http.spring.boot.client.multipart.InputStreamPartSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helpers for preparing Commons HttpClient 3.x requests: building URLs, encoding parameters,
 * assembling default/custom headers, constructing {@link RequestEntity} bodies (including
 * multipart uploads) and detecting redirect or gzip responses.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public abstract class HttpRequestUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpRequestUtils.class);

	/**
	 * Build a map of {@link RequestEntity} bodies from a parameter map, handling {@link File},
	 * {@link InputStream}, {@code byte[]} and {@link String} values.
	 * @param paramsMap the parameter map to inspect
	 * @return a map of parameter name to {@link RequestEntity}
	 * @throws IOException if a body cannot be constructed
	 */
	public static Map<String, RequestEntity> buildRequestEntity(Map<String, Object> paramsMap) throws IOException {
		Map<String, RequestEntity> contentBodies = new HashMap<String, RequestEntity>();
		if (paramsMap != null && !paramsMap.isEmpty()) {
			// 组织参数
			Iterator<String> iterator = paramsMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				Object value = paramsMap.get(key);
				if (value instanceof File) {
					// 对象强转
					File file = (File) value;
					// 把文件转换成流对象FileBody
					FileRequestEntity fileBody = new FileRequestEntity(file, FilemimeUtils.getFileMimeType(file));
					contentBodies.put(key, fileBody);
				} else if (value instanceof InputStream) {
					// 把输入流转换成流对象InputStreamBody
					InputStreamRequestEntity streamBody = new InputStreamRequestEntity((InputStream) value, ContentType.APPLICATION_OCTET_STREAM);
					contentBodies.put(key, streamBody);
				} else if (value instanceof byte[]) {
					byte[] byteVlue = (byte[]) value;
					// 把字节数组转换流对象ByteArrayBody
					ByteArrayRequestEntity byteArrayBody = new ByteArrayRequestEntity(byteVlue, ContentType.APPLICATION_OCTET_STREAM);
					contentBodies.put(key, byteArrayBody);
				} else if (value instanceof String) {
					StringRequestEntity byteArrayBody = new StringRequestEntity(value.toString(), ContentType.TEXT_PLAIN, ContentType.UTF_8);
					contentBodies.put(key, byteArrayBody);
				}
			}
		}
		return contentBodies;
	}
	

	/**
	 * Return the default browser-like request headers used by this helper.
	 * @return a mutable list of default {@link Header} objects
	 */
	public static List<Header> getDefaultHeaders() {
		List<Header> headers = new ArrayList<Header>();
		//setsdefaultrequest头info
		headers.add(new Header(HttpHeaders.CONNECTION, "keep-alive"));
		headers.add(new Header(HttpHeaders.ACCEPT, "*/*"));
		headers.add(new Header(HttpHeaders.CACHE_CONTROL, "max-age=0"));
		headers.add(new Header(HttpHeaders.ACCEPT_LANGUAGE,"zh-cn,zh;q=0.5"));
		headers.add(new Header(HttpHeaders.ACCEPT_CHARSET,"GBK,utf-8;q=0.7,*;q=0.7"));
		headers.add(new Header(HttpHeaders.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; SV1; .NET CLR 1.1.4322)"));
		return headers;
	}
	
	/**
	 * Return the default headers augmented with an {@code X-Forwarded-For} header carrying the
	 * given client IP.
	 * @param ip the client IP to set as {@code X-Forwarded-For}
	 * @return a mutable list of {@link Header} objects
	 */
	public static List<Header> getHeaders(String ip) {
		List<Header> headers = getDefaultHeaders();
		headers.add(new Header(HttpHeaders.X_FORWARDED_FOR, ip));
		//模拟Ajaxrequest
		//headers.add(new Header(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest"));
		//headers.add(new Header("Host", ""));
		return headers;
	}
	
	/**
	 * Apply the default browser-like request headers, then overlay any user-supplied headers,
	 * to the given HTTP method and return it.
	 * @param httpRequest the method to configure
	 * @param headers additional request headers, may be {@code null}
	 * @param <T> the concrete method type
	 * @return the configured method
	 */
	public static <T extends HttpMethodBase> T getHttpRequest(T httpRequest,Map<String, String> headers) {
		//setsdefaultrequest头info
		/*Accept表示浏览器支持的 MIME type；
	　　	MIME的英文全称是 Multipurpose Internet Mail Extensions（多功能 Internet 邮件扩充服务），它是一种多用途网际邮件扩充protocol，在1992年最早应用于电子邮件系统，但后来也应用到浏览器。
	　　	text/html,application/xhtml+xml,application/xml 都是 MIME type，也可以称为媒体type和内容type，斜杠前面的是 type（type），斜杠后面的是 subtype（子type）；type 指定大的范围，subtype 是 type 中范围更明确的type，即大类中的小类。
	　　	Text：用于标准化地表示的文本info，文本message可以是多种字符集和或者多种格式的；
	　　	text/html表示 html 文档；
	　　	Application：用于传输应用程序数据或者二进制数据；
	　　	application/xhtml+xml表示 xhtml 文档；
	　　	application/xml表示 xml 文档*/
		httpRequest.addRequestHeader(HttpHeaders.ACCEPT, "*/*");
		/*
		Accept-Encoding表示浏览器有能力解码的encodingtype；
		gzip是 GNU zip 的缩写，它是一个 GNU 自由软件的文件压缩程序，也经常用来表示 gzip 这种文件格式。
		deflate是同时使用了 LZ77 算法与哈夫曼encoding（Huffman Coding）的一个无损数据压缩算法。
		*/
		//httpRequest.addRequestHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
		/*Accept-Language表示浏览器所支持的语言type；
		　　zh-cn表示简体中文；zh 表示中文；
		　　q是权重系数，范围 0 =< q <= 1，q 值越大，request越倾向于获得其“;”之前的type表示的内容，若没有指定 q 值，则default为1，若被赋值为0，则用于提醒服务器哪些是浏览器不接受的内容type
		*/
		httpRequest.addRequestHeader(HttpHeaders.ACCEPT_LANGUAGE,"zh-cn,zh;q=0.5");
		/*Accept-Charset告诉 Web 服务器，浏览器可以接受哪些字符encoding；
		　　GB2312是中国国家标准简体中文字符集，全称《info交换用汉字encoding字符集·基本集》，又称GB0，由中国国家标准总局发布，1981年5月1日实施。GB2312 encoding通行于中国大陆；新加坡等地也采用此encoding。
		　　utf-8是 Unicode 的一种变长字符encoding又称万国码，由 Ken Thompson 于1992年creates，现在已经标准化为 RFC 3629。
		　　*表示任意字符encoding，虽然 q 都是等于 0.7，但明确指定的 GB2312,utf-8 比 * 具有更高的优先级。*/
		httpRequest.addRequestHeader(HttpHeaders.ACCEPT_CHARSET,"GB2312,utf-8;q=0.7,*;q=0.7");
		/*Connection表示客户端与服务连接type；Keep-Alive表示持久连接；*/
		httpRequest.addRequestHeader(HttpHeaders.CONNECTION, "keep-alive");
		httpRequest.addRequestHeader(HttpHeaders.CACHE_CONTROL, "max-age=0");
		//httpRequest.addHeader(HttpHeaders.HOST, "mp.weixin.qq.com");
		//httpRequest.addHeader(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
		/*User-Agent（user代理），简称 UA，它是一个特殊字符串头，使得服务器能够识别客户端使用的操作系统及版本、CPU type、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等。
	　　	Mozilla/5.0：Mozilla 是浏览器名，版本是 5.0；
	　　	compatible（兼容的）表示平台是兼容模式；*/
		httpRequest.addRequestHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; SV1; .NET CLR 1.1.4322)");
		//sets自定义request头info
        if (headers != null) {  
            Set<String> keys = headers.keySet();  
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {  
                String key = (String) i.next();  
                httpRequest.addRequestHeader(key, headers.get(key)); 
            }
        }
        return httpRequest;
	}
    
	/**
	 * Build a {@link GetMethod} for the given URL, ignoring any in-URL query string and applying
	 * the supplied headers.
	 * @param baseURL the target URL (query string is discarded)
	 * @param charset the request charset
	 * @param headers additional request headers, may be {@code null}
	 * @return a configured {@link GetMethod}
	 */
	public static GetMethod getHttpGet(String baseURL, String charset,Map<String, String> headers) {
		String[] paramStr = baseURL.split("[?]", 2);
		GetMethod httpGet = new GetMethod(paramStr[0]);
        return getHttpRequest(httpGet,headers);
	}
 
	/**
	 * Build a {@link PostMethod} for the given URL, ignoring any in-URL query string and
	 * applying the supplied headers.
	 * @param baseURL the target URL (query string is discarded)
	 * @param charset the request charset
	 * @param headers additional request headers, may be {@code null}
	 * @return a configured {@link PostMethod}
	 */
	public static PostMethod getHttpPost(String baseURL, String charset,Map<String, String> headers) {
		String[] paramStr = baseURL.split("[?]", 2);
		PostMethod httpPost  = new PostMethod(paramStr[0]); 
		return getHttpRequest(httpPost,headers);
	}
	
	/**
	 * Inspect the given status code and, when it indicates a redirect, build a new GET or POST
	 * method targeting the {@code location} response header.
	 * @param httpMethod the previously executed method
	 * @param statuscode the status code returned by the previous execution
	 * @param charset the charset used to encode the redirected request
	 * @param headers additional request headers, may be {@code null}
	 * @param <T> the concrete method type
	 * @return a new method targeting the redirect URL, or {@code null} when no redirect applies
	 * @throws HttpResponseException if a redirect response carries no {@code location} header
	 */
	@SuppressWarnings("unchecked")
	public static <T extends HttpMethodBase> T getHttpRedirect(T httpMethod,int statuscode, String charset,
			Map<String, String> headers) throws HttpResponseException {
		// 检查whether重定向
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// 从头中取出转向的address
			Header header = httpMethod.getResponseHeader("location");
			if (header != null) {
				// 从头中取出转向的address
				String redirectURI = header.getValue();
				if ((redirectURI == null) || (redirectURI.equals(""))) {
					redirectURI = "/";
				}
				LOG.debug("The page was redirected to:" + redirectURI);
				if(httpMethod instanceof GetMethod){
					return (T) getHttpRequest(new GetMethod(redirectURI),headers);
				}else if(httpMethod instanceof PostMethod){
					return (T) getHttpRequest(new PostMethod(redirectURI),headers);
				}
			} else {
				throw new HttpResponseException("Invalid redirect .");
			}
		} 
		return null;
	}
	
	/**
	 * Build a {@link MultipartRequestEntity} from the given parameter map when it contains at
	 * least one file, input stream or byte-array value; otherwise return {@code null}.
	 * @param httpRequest the method whose parameters seed the multipart body
	 * @param baseURL the original target URL
	 * @param paramsMap the parameter map to inspect
	 * @param charset the charset used for string parts
	 * @return a configured {@link MultipartRequestEntity}, or {@code null} when the map has no
	 *         binary payload
	 * @throws IOException if a part cannot be constructed
	 */
	public static MultipartRequestEntity getHttpEntity(HttpMethodBase httpRequest,String baseURL, Map<String, Object> paramsMap,String charset) throws IOException{
    	//有实体对象参数，表示可能有文件上传
    	if(HttpRequestUtils.isMultipart(paramsMap)){
    		List<Part> parts = new ArrayList<Part>();
            //初始path中的参数集合对象
            List<NameValuePair> nameValuePostBodies = HttpURIUtils.buildNameValuePairs(baseURL);
            for (NameValuePair nameValuePair : nameValuePostBodies) {
            	parts.add(new StringPart(nameValuePair.getName(), nameValuePair.getValue(), charset));
            }
            //初始参数对象
    		if(paramsMap != null && !paramsMap.isEmpty()){
    			//组织参数
    	        Iterator<String> iterator = paramsMap.keySet().iterator();
    	        while (iterator.hasNext()) {
    	            String name = iterator.next();
    	            Object value = paramsMap.get(name);
    	            if (value instanceof File) {
    	            	//文件对象
    	            	File file = (File) value;
    	            	//对应服务端类的同名properties<Filetype>
    	                parts.add(new FilePart(name, file));
    	                //用来封装上传文件的文件名
    	                parts.add(new StringPart(name + "FileName", file.getName(), charset));
    	                //用来封装上传文件的type
    	                parts.add(new StringPart(name + "ContentType", FilemimeUtils.getFileMimeType(file), charset));
    	            } else if (value instanceof InputStream) {
    	            	// 把输入流转换成流对象InputStreamPartSource
    	                parts.add(new FilePart(name, new InputStreamPartSource(name, (InputStream) value)));
    	            } else if (value instanceof byte[]) {
    	                byte[] byteVlue = (byte[]) value;
    	                // 把字节数组转换流对象ByteArrayPartSource
    	                parts.add(new FilePart(name, new ByteArrayPartSource(name, byteVlue)));
    	            } else {
    	            	parts.add(new StringPart(name, value.toString(), charset));
					}
    	        }
        	}
            return new MultipartRequestEntity(parts.toArray(new Part[parts.size()]),httpRequest.getParams());
    	}
    	return null;
	}
	
	/**
	 * Configure a {@link PostMethod} with the supplied {@code Content-Type} header and request
	 * body, switching to a multipart body when the parameter map contains a binary payload.
	 * @param httpMethod the POST method to configure
	 * @param baseURL the original target URL
	 * @param paramsMap the parameter map to send
	 * @param charset the charset used to encode the body
	 * @param contentType the {@code Content-Type} header value, may be {@code null}
	 * @param headers additional request headers, may be {@code null}
	 * @throws IOException if the body cannot be constructed
	 */
	public static void setHttpMethod(PostMethod httpMethod,String baseURL,
			Map<String, Object> paramsMap, String charset, String contentType,
			Map<String, String> headers) throws IOException {
		// 判断request头info
		if (contentType != null) {
			httpMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, contentType);
		} else {
			httpMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE,ContentType.APPLICATION_FORM_URLENCODED + ";charset=" + charset);
		}
		// 有实体对象参数，表示可能有文件上传
		if (HttpRequestUtils.isMultipart(paramsMap)) {
			// 有文件上传
			httpMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE,ContentType.MULTIPART_FORM_DATA + ";charset=" + charset);
			// 得到request实体对象
			RequestEntity requestEntity = getHttpEntity(httpMethod, baseURL,paramsMap, charset);
			httpMethod.setRequestEntity(requestEntity);
		}else {
			List<NameValuePair> nameValueList = HttpURIUtils.buildNameValuePairs(baseURL, paramsMap);
			NameValuePair[] nameValuePairs = nameValueList.toArray(new NameValuePair[nameValueList.size()]);
			httpMethod.setRequestBody(nameValuePairs);
		}
	}
	
	/**
	 * Return {@code true} when the parameter map contains at least one {@link File},
	 * {@link InputStream} or {@code byte[]} value, indicating that a multipart body is
	 * required.
	 * @param paramsMap the parameter map to inspect
	 * @return {@code true} when a multipart body is required
	 */
	public static boolean isMultipart(Map<String, Object> paramsMap){
		boolean isMultipart = false;
		if(paramsMap != null && !paramsMap.isEmpty()){
	        Iterator<String> iterator = paramsMap.keySet().iterator();
	        while (iterator.hasNext() && !isMultipart) {
	            String key = iterator.next();
	            Object value = paramsMap.get(key);
	            if (value instanceof File) {
	            	isMultipart = true;
	            } else if (value instanceof InputStream) {
	            	isMultipart = true;
	            } else if (value instanceof byte[]) {
	            	isMultipart = true;
	            }
	        }
    	}
		return isMultipart;
	}
	 
	
	/**
	 * Return {@code true} when the request advertises {@code Accept-Encoding: gzip}.
	 * @param httpMethod the method whose request headers should be inspected
	 * @return {@code true} when gzip is requested
	 */
	public static boolean isGzip(HttpMethodBase httpMethod) {
		boolean isGzip = false;
		Header[] headers = httpMethod.getRequestHeaders(HttpHeaders.ACCEPT_ENCODING);
		for (Header header : headers) {
			HeaderElement[] elements = header.getElements();
			for (HeaderElement elem : elements) {
				LOG.debug(elem.getName() + " = " + elem.getValue());
				if ("gzip".equalsIgnoreCase(elem.getName())) {
					isGzip = true;
					break;
				}
			}
			if (isGzip) {
				break;
			}
		}
		return isGzip;
	}
}
