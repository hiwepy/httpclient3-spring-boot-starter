package org.apache.http.spring.boot;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HttpClientFactory}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HttpClientFactoryCoverageTest {

    @Test
    void constructorWithNull() {
        HttpClientFactory factory = new HttpClientFactory(null);
        assertThat(factory).isNotNull();
    }

    @Test
    void getCloseableHttpClient() {
        HttpClientFactory factory = new HttpClientFactory(null);
        assertThat(factory.getCloseableHttpClient()).isNotNull();
    }

    @Test
    void constructorWithManagerAndFlag() {
        HttpClientFactory factory = new HttpClientFactory(null, true);
        assertThat(factory).isNotNull();
    }

    @Test
    void getCloseableHttpClientWithManager() {
        HttpClientFactory factory = new HttpClientFactory(null, true);
        assertThat(factory.getCloseableHttpClient()).isNotNull();
    }
}
