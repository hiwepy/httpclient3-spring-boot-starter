package org.apache.http.spring.boot.client.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.StatusLine;
import org.apache.http.spring.boot.client.exception.HttpResponseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Comprehensive unit tests for all response handler classes.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HandlerResponseCoverageTest {

    // --- BinaryResponseHandler ---

    @Test
    void binaryHandlerSuccess() throws Exception {
        BinaryResponseHandler handler = new BinaryResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(method.getResponseBody()).thenReturn("hello".getBytes());
        byte[] result = handler.handleResponse(method);
        assertThat(result).isNotNull();
    }

    @Test
    void binaryHandlerError() throws Exception {
        BinaryResponseHandler handler = new BinaryResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(statusLine.getReasonPhrase()).thenReturn("Server Error");
        assertThatThrownBy(() -> handler.handleResponse(method)).isInstanceOf(HttpResponseException.class);
    }

    @Test
    void binaryHandlerClient() {
        BinaryResponseHandler handler = new BinaryResponseHandler();
        handler.handleClient(new HttpClient());
    }

    // --- PlainTextResponseHandler ---

    @Test
    void plainTextHandlerSuccess() throws Exception {
        PlainTextResponseHandler handler = new PlainTextResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(method.getResponseBodyAsString()).thenReturn("hello");
        String result = handler.handleResponse(method);
        assertThat(result).isEqualTo("hello");
    }

    @Test
    void plainTextHandlerError() throws Exception {
        PlainTextResponseHandler handler = new PlainTextResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(404);
        when(statusLine.getReasonPhrase()).thenReturn("Not Found");
        assertThatThrownBy(() -> handler.handleResponse(method)).isInstanceOf(HttpResponseException.class);
    }

    @Test
    void plainTextHandlerClient() {
        PlainTextResponseHandler handler = new PlainTextResponseHandler();
        handler.handleClient(new HttpClient());
    }

    // --- StreamResponseHandler ---

    @Test
    void streamHandlerSuccess() throws Exception {
        StreamResponseHandler handler = new StreamResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(method.getResponseBody()).thenReturn("hello".getBytes());
        ByteArrayInputStream result = handler.handleResponse(method);
        assertThat(result).isNotNull();
    }

    @Test
    void streamHandlerError() throws Exception {
        StreamResponseHandler handler = new StreamResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(400);
        when(statusLine.getReasonPhrase()).thenReturn("Bad Request");
        assertThatThrownBy(() -> handler.handleResponse(method)).isInstanceOf(HttpResponseException.class);
    }

    @Test
    void streamHandlerClient() {
        StreamResponseHandler handler = new StreamResponseHandler();
        handler.handleClient(new HttpClient());
    }

    // --- RedirectResponseHandler ---

    @Test
    void redirectHandlerClient() {
        RedirectResponseHandler handler = new RedirectResponseHandler();
        handler.handleClient(new HttpClient());
    }

    // --- XMLResponseHandler ---

    @Test
    void xmlHandlerClient() {
        XMLResponseHandler handler = new XMLResponseHandler();
        handler.handleClient(new HttpClient());
    }

    @Test
    void xmlHandlerError() throws Exception {
        XMLResponseHandler handler = new XMLResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(statusLine.getReasonPhrase()).thenReturn("Error");
        assertThatThrownBy(() -> handler.handleResponse(method)).isInstanceOf(HttpResponseException.class);
    }

    // --- ObjectResponseHandler ---

    @Test
    void objectHandlerClient() {
        ObjectResponseHandler handler = new ObjectResponseHandler();
        handler.handleClient(new HttpClient());
    }

    @Test
    void objectHandlerError() throws Exception {
        ObjectResponseHandler handler = new ObjectResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(statusLine.getReasonPhrase()).thenReturn("Error");
        assertThatThrownBy(() -> handler.handleResponse(method)).isInstanceOf(HttpResponseException.class);
    }

    // --- JSONResponseHandler ---

    @Test
    void jsonHandlerClient() {
        JSONResponseHandler handler = new JSONResponseHandler();
        handler.handleClient(new HttpClient());
    }

    @Test
    void jsonHandlerError() throws Exception {
        JSONResponseHandler handler = new JSONResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(statusLine.getReasonPhrase()).thenReturn("Error");
        assertThatThrownBy(() -> handler.handleResponse(method)).isInstanceOf(HttpResponseException.class);
    }

    @Test
    void jsonHandlerJsonContent() throws Exception {
        JSONResponseHandler handler = new JSONResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(method.getResponseHeader("Content-Type")).thenReturn(
                new org.apache.commons.httpclient.Header("Content-Type", "application/json"));
        when(method.getResponseBodyAsString()).thenReturn("{\"key\":\"value\"}");
        var result = handler.handleResponse(method);
        assertThat(result).isNotNull();
    }

    @Test
    void jsonHandlerUnexpectedContentType() throws Exception {
        JSONResponseHandler handler = new JSONResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(method.getResponseHeader("Content-Type")).thenReturn(
                new org.apache.commons.httpclient.Header("Content-Type", "text/html"));
        assertThatThrownBy(() -> handler.handleResponse(method)).isInstanceOf(HttpResponseException.class);
    }

    @Test
    void jsonHandlerXmlContent() throws Exception {
        JSONResponseHandler handler = new JSONResponseHandler();
        HttpMethodBase method = mock(HttpMethodBase.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(method.getResponseHeader("Content-Type")).thenReturn(
                new org.apache.commons.httpclient.Header("Content-Type", "application/xml"));
        String xml = "<root><item>value</item></root>";
        when(method.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream(xml.getBytes()));
        var result = handler.handleResponse(method);
        assertThat(result).isNotNull();
    }
}
