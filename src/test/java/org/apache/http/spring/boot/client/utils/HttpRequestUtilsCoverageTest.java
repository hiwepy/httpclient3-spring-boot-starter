package org.apache.http.spring.boot.client.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HttpRequestUtils}.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class HttpRequestUtilsCoverageTest {

    @Test
    void getDefaultHeaders() {
        var headers = HttpRequestUtils.getDefaultHeaders();
        assertThat(headers).isNotEmpty();
    }

    @Test
    void getHeadersWithIp() {
        var headers = HttpRequestUtils.getHeaders("192.168.1.1");
        assertThat(headers).isNotEmpty();
    }

    @Test
    void getHttpRequestGet() {
        GetMethod method = new GetMethod("http://localhost/test");
        GetMethod result = HttpRequestUtils.getHttpRequest(method, null);
        assertThat(result).isSameAs(method);
    }

    @Test
    void getHttpRequestGetWithHeaders() {
        GetMethod method = new GetMethod("http://localhost/test");
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Custom", "value");
        GetMethod result = HttpRequestUtils.getHttpRequest(method, headers);
        assertThat(result).isSameAs(method);
    }

    @Test
    void getHttpRequestPost() {
        PostMethod method = new PostMethod("http://localhost/test");
        PostMethod result = HttpRequestUtils.getHttpRequest(method, null);
        assertThat(result).isSameAs(method);
    }

    @Test
    void getHttpGet() {
        GetMethod method = HttpRequestUtils.getHttpGet("http://localhost/test?a=1", "UTF-8", null);
        assertThat(method).isNotNull();
        assertThat(method.getPath()).isEqualTo("/test");
    }

    @Test
    void getHttpPost() {
        PostMethod method = HttpRequestUtils.getHttpPost("http://localhost/test", "UTF-8", null);
        assertThat(method).isNotNull();
    }

    @Test
    void getHttpRedirectNoRedirect() throws Exception {
        GetMethod method = new GetMethod("http://localhost/test");
        Object result = HttpRequestUtils.getHttpRedirect(method, 200, "UTF-8", null);
        assertThat(result).isNull();
    }

    @Test
    void isMultipartNull() {
        assertThat(HttpRequestUtils.isMultipart(null)).isFalse();
    }

    @Test
    void isMultipartEmpty() {
        assertThat(HttpRequestUtils.isMultipart(new HashMap<>())).isFalse();
    }

    @Test
    void isMultipartWithString() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        assertThat(HttpRequestUtils.isMultipart(params)).isFalse();
    }

    @Test
    void isMultipartWithFile() {
        Map<String, Object> params = new HashMap<>();
        params.put("file", new File("test.txt"));
        assertThat(HttpRequestUtils.isMultipart(params)).isTrue();
    }

    @Test
    void isMultipartWithInputStream() {
        Map<String, Object> params = new HashMap<>();
        params.put("stream", new ByteArrayInputStream(new byte[0]));
        assertThat(HttpRequestUtils.isMultipart(params)).isTrue();
    }

    @Test
    void isMultipartWithByteArray() {
        Map<String, Object> params = new HashMap<>();
        params.put("bytes", new byte[10]);
        assertThat(HttpRequestUtils.isMultipart(params)).isTrue();
    }

    @Test
    void buildRequestEntityNull() throws Exception {
        Map<String, RequestEntity> result = HttpRequestUtils.buildRequestEntity(null);
        assertThat(result).isEmpty();
    }

    @Test
    void buildRequestEntityEmpty() throws Exception {
        Map<String, RequestEntity> result = HttpRequestUtils.buildRequestEntity(new HashMap<>());
        assertThat(result).isEmpty();
    }

    @Test
    void buildRequestEntityWithString() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        Map<String, RequestEntity> result = HttpRequestUtils.buildRequestEntity(params);
        assertThat(result).containsKey("key");
    }

    @Test
    void buildRequestEntityWithBytes() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("data", new byte[10]);
        Map<String, RequestEntity> result = HttpRequestUtils.buildRequestEntity(params);
        assertThat(result).containsKey("data");
    }

    @Test
    void buildRequestEntityWithFile() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("file", new File("test.txt"));
        Map<String, RequestEntity> result = HttpRequestUtils.buildRequestEntity(params);
        assertThat(result).containsKey("file");
    }

    @Test
    void buildRequestEntityWithInputStream() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("stream", new ByteArrayInputStream(new byte[0]));
        Map<String, RequestEntity> result = HttpRequestUtils.buildRequestEntity(params);
        assertThat(result).containsKey("stream");
    }

    @Test
    void isGzip() {
        GetMethod method = new GetMethod("http://localhost/test");
        assertThat(HttpRequestUtils.isGzip(method)).isFalse();
    }

    @Test
    void getHttpEntityNull() throws Exception {
        PostMethod method = new PostMethod("http://localhost/test");
        var result = HttpRequestUtils.getHttpEntity(method, "http://localhost/test", null, "UTF-8");
        assertThat(result).isNull();
    }

    @Test
    void getHttpEntityEmpty() throws Exception {
        PostMethod method = new PostMethod("http://localhost/test");
        var result = HttpRequestUtils.getHttpEntity(method, "http://localhost/test", new HashMap<>(), "UTF-8");
        assertThat(result).isNull();
    }

    @Test
    void setHttpMethodWithContentType() throws Exception {
        PostMethod method = new PostMethod("http://localhost/test");
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        HttpRequestUtils.setHttpMethod(method, "http://localhost/test", params, "UTF-8", "application/json", null);
        assertThat(method.getRequestHeader("Content-Type")).isNotNull();
    }

    @Test
    void setHttpMethodNullContentType() throws Exception {
        PostMethod method = new PostMethod("http://localhost/test");
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        HttpRequestUtils.setHttpMethod(method, "http://localhost/test", params, "UTF-8", null, null);
        assertThat(method.getRequestHeader("Content-Type")).isNotNull();
    }
}
