package org.apache.http.spring.boot.client.handler;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for response handler classes.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class ResponseHandlerCoverageTest {

    @Test
    void jsonResponseHandlerInstantiation() {
        JSONResponseHandler handler = new JSONResponseHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    void binaryResponseHandlerInstantiation() {
        BinaryResponseHandler handler = new BinaryResponseHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    void plainTextResponseHandlerInstantiation() {
        PlainTextResponseHandler handler = new PlainTextResponseHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    void streamResponseHandlerInstantiation() {
        StreamResponseHandler handler = new StreamResponseHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    void xmlResponseHandlerInstantiation() {
        XMLResponseHandler handler = new XMLResponseHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    void redirectResponseHandlerInstantiation() {
        RedirectResponseHandler handler = new RedirectResponseHandler();
        assertThat(handler).isNotNull();
    }
}
