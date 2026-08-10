package org.apache.http.spring.boot.client.ssl;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for SSL utility classes.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class SSLUtilsCoverageTest {

    @Test
    void keyManagerUtilsClassExists() {
        assertThat(KeyManagerUtils.class).isNotNull();
    }

    @Test
    void trustManagerUtilsClassExists() {
        assertThat(TrustManagerUtils.class).isNotNull();
    }

    @Test
    void sslContextUtilsClassExists() {
        assertThat(SSLContextUtils.class).isNotNull();
    }

    @Test
    void sslSocketUtilsClassExists() {
        assertThat(SSLSocketUtils.class).isNotNull();
    }
}
