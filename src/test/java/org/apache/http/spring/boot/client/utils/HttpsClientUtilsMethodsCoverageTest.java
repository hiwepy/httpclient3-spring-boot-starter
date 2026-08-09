package org.apache.http.spring.boot.client.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HttpsClientUtils}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HttpsClientUtilsMethodsCoverageTest {

    @Test
    void classIsAbstract() {
        assertThat(java.lang.reflect.Modifier.isAbstract(HttpsClientUtils.class.getModifiers())).isTrue();
    }
}
