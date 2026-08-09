package org.apache.http.spring.boot.client;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ContentType}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class ContentTypeTest {
    @Test
    void constants() {
        assertThat(ContentType.APPLICATION_JSON).isEqualTo("application/json");
        assertThat(ContentType.APPLICATION_XML).isEqualTo("application/xml");
        assertThat(ContentType.APPLICATION_FORM_URLENCODED).isEqualTo("application/x-www-form-urlencoded");
        assertThat(ContentType.APPLICATION_OCTET_STREAM).isEqualTo("application/octet-stream");
        assertThat(ContentType.TEXT_HTML).isEqualTo("text/html");
        assertThat(ContentType.TEXT_PLAIN).isEqualTo("text/plain");
        assertThat(ContentType.TEXT_JSON).isEqualTo("text/json");
        assertThat(ContentType.TEXT_XML).isEqualTo("text/xml");
        assertThat(ContentType.UTF_8).isEqualTo("UTF-8");
        assertThat(ContentType.MULTIPART_FORM_DATA).isEqualTo("multipart/form-data");
        assertThat(ContentType.WILDCARD).isEqualTo("*/*");
    }
}
