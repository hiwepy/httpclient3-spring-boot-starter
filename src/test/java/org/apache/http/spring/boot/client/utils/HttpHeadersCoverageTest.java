package org.apache.http.spring.boot.client.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HttpHeaders}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HttpHeadersCoverageTest {
    @Test
    void constants() {
        assertThat(HttpHeaders.ACCEPT).isEqualTo("Accept");
        assertThat(HttpHeaders.CONTENT_TYPE).isEqualTo("Content-Type");
        assertThat(HttpHeaders.CONTENT_LENGTH).isEqualTo("Content-Length");
        assertThat(HttpHeaders.AUTHORIZATION).isEqualTo("Authorization");
        assertThat(HttpHeaders.HOST).isEqualTo("Host");
        assertThat(HttpHeaders.USER_AGENT).isEqualTo("User-Agent");
        assertThat(HttpHeaders.CACHE_CONTROL).isEqualTo("Cache-Control");
        assertThat(HttpHeaders.CONNECTION).isEqualTo("Connection");
        assertThat(HttpHeaders.LOCATION).isEqualTo("Location");
        assertThat(HttpHeaders.X_FORWARDED_FOR).isEqualTo("x-forwarded-for");
        assertThat(HttpHeaders.X_REQUESTED_WITH).isEqualTo("X-Requested-With");
    }
}
