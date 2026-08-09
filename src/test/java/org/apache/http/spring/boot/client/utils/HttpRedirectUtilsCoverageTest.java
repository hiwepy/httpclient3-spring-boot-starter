package org.apache.http.spring.boot.client.utils;

import java.io.ByteArrayInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HttpRedirectUtils}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HttpRedirectUtilsCoverageTest {

    @Test
    void stripRedirectGetNoRedirect() throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod("http://localhost/test");
        GetMethod result = HttpRedirectUtils.stripRedirect(client, method, 200, "UTF-8", null, new org.apache.commons.httpclient.NameValuePair[0]);
        assertThat(result).isSameAs(method);
    }

    @Test
    void stripRedirectPostNoRedirect() throws Exception {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod("http://localhost/test");
        PostMethod result = HttpRedirectUtils.stripRedirect(client, method, 200, "UTF-8", null, null);
        assertThat(result).isSameAs(method);
    }

    @Test
    void stripRedirectPostWithParamsNoRedirect() throws Exception {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod("http://localhost/test");
        PostMethod result = HttpRedirectUtils.stripRedirect("http://localhost/test", client, method, 200, "UTF-8", "application/json", null, null);
        assertThat(result).isSameAs(method);
    }
}
