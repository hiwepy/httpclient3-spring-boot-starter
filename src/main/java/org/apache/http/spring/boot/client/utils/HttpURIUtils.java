package org.apache.http.spring.boot.client.utils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.httpclient.util.URIUtil;

/**
 * Helpers for working with URLs and query strings in Commons HttpClient 3.x requests: merging
 * in-URL query parameters with an additional parameter map, URL-encoding values and building
 * {@link NameValuePair} lists.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public abstract class HttpURIUtils {

	/**
	 * Merge the in-URL query string of {@code baseURL} with the supplied parameter map and
	 * return the resulting URL-encoded string.
	 * @param baseURL the target URL
	 * @param paramsMap additional parameters to append, may be {@code null}
	 * @param charset the charset used to encode the resulting query string
	 * @return the merged URL, or {@code baseURL} when no parameters are present
	 * @throws URIException if the URL cannot be parsed
	 */
	public static String buildURL(String baseURL, Map<String, Object> paramsMap,String charset) throws URIException {
		if (paramsMap == null) {
			return baseURL;
		}
		//初始参数集合对象
    	String[] paramStr = baseURL.split("[?]", 2);
        if (paramStr == null || paramStr.length != 2) {
           return baseURL;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
        	return baseURL;
        }
        //初始参数集合对象
    	List<NameValuePair> nameValueList    = buildNameValuePairs(baseURL , paramsMap);
        StringBuilder builder = new StringBuilder(paramStr[0]);
        NameValuePair[] nameValuePairs = nameValueList.toArray(new NameValuePair[nameValueList.size()]);
		return builder.append(builder.indexOf("?") > 0 ? "&" : "?").append(EncodingUtil.formUrlEncode(nameValuePairs, charset)).toString();
	}
	
	/**
	 * Build a list of {@link NameValuePair} objects from the supplied parameter map (ignoring
	 * {@link File} and {@code byte[]} values) and the in-URL query string of {@code baseURL}.
	 * @param baseURL the target URL whose in-URL query parameters should be merged
	 * @param paramsMap additional parameters to include, may be {@code null}
	 * @return a list of {@link NameValuePair} objects
	 * @throws URIException if the URL cannot be parsed
	 */
	public static List<NameValuePair> buildNameValuePairs(String baseURL, Map<String, Object> paramsMap) throws URIException {
    	//初始参数集合对象
    	List<NameValuePair> nameValueList    = new LinkedList<NameValuePair>();
    	if(paramsMap != null && !paramsMap.isEmpty()){
    		//组织参数
            Iterator<String> iterator = paramsMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = paramsMap.get(key);
                if (value instanceof File) {
                	//什么都不做
                } else if (value instanceof byte[]) {
                	//什么都不做
                } else {
                	if (value != null && !"".equals(value)) {
						nameValueList.add(new NameValuePair(key, URIUtil.encodeQuery(value.toString())));
					} else {
						nameValueList.add(new NameValuePair(key, ""));
					}
                }
            }
    	}
    	nameValueList.addAll(buildNameValuePairs(baseURL));
        return nameValueList;
    }
	
	/**
	 * Parse the in-URL query string of {@code baseURL} into a list of {@link NameValuePair}
	 * objects. Returns an empty list when the URL has no query string.
	 * @param baseURL the target URL whose query string should be parsed
	 * @return a list of {@link NameValuePair} objects, possibly empty
	 * @throws URIException if the URL cannot be parsed
	 */
	public static List<NameValuePair> buildNameValuePairs(String baseURL) throws URIException {
    	//初始参数集合对象
    	List<NameValuePair> nameValueList    = new LinkedList<NameValuePair>();
    	//初始参数集合对象
    	String[] paramStr = baseURL.split("[?]", 2);
        if (paramStr == null || paramStr.length != 2) {
           return nameValueList;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
        	return nameValueList;
        }
        for (String param : paramArray) {
            if (param == null || "".equals(param.trim())) {
                continue;
            }
            String[] keyValue = param.split("[=]", 2);
            if (keyValue == null || keyValue.length != 2) {
                continue;
            }
            nameValueList.add(new NameValuePair(keyValue[0], URIUtil.encodeQuery(keyValue[1])));
        }
        return nameValueList;
    }
	
}
