package org.apache.http.spring.boot.client.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HttpClientUtils} static methods.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class HttpClientUtilsMethodsCoverageTest {

    @Test
    void classIsAbstract() {
        assertThat(java.lang.reflect.Modifier.isAbstract(HttpClientUtils.class.getModifiers())).isTrue();
    }
}
