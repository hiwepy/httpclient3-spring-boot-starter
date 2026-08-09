package org.apache.http.spring.boot.client.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HttpURIUtils}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HttpURIUtilsCoverageTest {

    @Test
    void buildNameValuePairsFromUrl() throws Exception {
        List<NameValuePair> pairs = HttpURIUtils.buildNameValuePairs("http://localhost/test?a=1&b=2");
        assertThat(pairs).hasSize(2);
    }

    @Test
    void buildNameValuePairsFromUrlNoQuery() throws Exception {
        List<NameValuePair> pairs = HttpURIUtils.buildNameValuePairs("http://localhost/test");
        assertThat(pairs).isEmpty();
    }

    @Test
    void buildNameValuePairsWithMap() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        List<NameValuePair> pairs = HttpURIUtils.buildNameValuePairs("http://localhost/test", params);
        assertThat(pairs).isNotEmpty();
    }

    @Test
    void buildNameValuePairsWithNullMap() throws Exception {
        List<NameValuePair> pairs = HttpURIUtils.buildNameValuePairs("http://localhost/test", null);
        assertThat(pairs).isEmpty();
    }

    @Test
    void buildNameValuePairsWithEmptyMap() throws Exception {
        List<NameValuePair> pairs = HttpURIUtils.buildNameValuePairs("http://localhost/test", new HashMap<>());
        assertThat(pairs).isEmpty();
    }

    @Test
    void buildNameValuePairsWithFileValue() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("file", new java.io.File("test.txt"));
        List<NameValuePair> pairs = HttpURIUtils.buildNameValuePairs("http://localhost/test", params);
        assertThat(pairs).isEmpty();
    }

    @Test
    void buildNameValuePairsWithBytesValue() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("bytes", new byte[10]);
        List<NameValuePair> pairs = HttpURIUtils.buildNameValuePairs("http://localhost/test", params);
        assertThat(pairs).isEmpty();
    }

    @Test
    void buildNameValuePairsWithEmptyValue() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "");
        List<NameValuePair> pairs = HttpURIUtils.buildNameValuePairs("http://localhost/test", params);
        assertThat(pairs).isNotEmpty();
    }

    @Test
    void buildURLNullParams() throws Exception {
        String url = HttpURIUtils.buildURL("http://localhost/test", null, "UTF-8");
        assertThat(url).isEqualTo("http://localhost/test");
    }

    @Test
    void buildURLWithParams() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        String url = HttpURIUtils.buildURL("http://localhost/test?a=1", params, "UTF-8");
        assertThat(url).contains("key");
    }

    @Test
    void buildURLNoQueryString() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        String url = HttpURIUtils.buildURL("http://localhost/test", params, "UTF-8");
        assertThat(url).isEqualTo("http://localhost/test");
    }
}
