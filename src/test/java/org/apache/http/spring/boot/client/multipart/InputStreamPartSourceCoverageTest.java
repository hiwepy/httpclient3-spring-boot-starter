package org.apache.http.spring.boot.client.multipart;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link InputStreamPartSource}.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class InputStreamPartSourceCoverageTest {

    @Test
    void constructorAndGetFileName() {
        InputStreamPartSource source = new InputStreamPartSource("test.txt", new ByteArrayInputStream(new byte[0]));
        assertThat(source.getFileName()).isEqualTo("test.txt");
    }

    @Test
    void constructorWithNullFileName() {
        InputStreamPartSource source = new InputStreamPartSource(null, new ByteArrayInputStream(new byte[0]));
        assertThat(source.getFileName()).isEqualTo("noname");
    }

    @Test
    void createInputStream() throws Exception {
        byte[] data = "hello".getBytes();
        InputStreamPartSource source = new InputStreamPartSource("test.txt", new ByteArrayInputStream(data));
        assertThat(source.createInputStream()).isNotNull();
    }
}
