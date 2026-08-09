package org.apache.http.spring.boot.client.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Unit tests for {@link IOUtils}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class IOUtilsCoverageTest {
    @Test
    void closeQuietlyInputStream() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        assertThatCode(() -> IOUtils.closeQuietly(is)).doesNotThrowAnyException();
    }

    @Test
    void closeQuietlyInputStreamNull() {
        assertThatCode(() -> IOUtils.closeQuietly((InputStream) null)).doesNotThrowAnyException();
    }

    @Test
    void closeQuietlyOutputStream() {
        OutputStream os = new ByteArrayOutputStream();
        assertThatCode(() -> IOUtils.closeQuietly(os)).doesNotThrowAnyException();
    }

    @Test
    void closeQuietlyOutputStreamNull() {
        assertThatCode(() -> IOUtils.closeQuietly((OutputStream) null)).doesNotThrowAnyException();
    }

    @Test
    void closeQuietlyCloseable() {
        Closeable c = new ByteArrayInputStream(new byte[0]);
        assertThatCode(() -> IOUtils.closeQuietly(c)).doesNotThrowAnyException();
    }

    @Test
    void closeQuietlyCloseableNull() {
        assertThatCode(() -> IOUtils.closeQuietly((Closeable) null)).doesNotThrowAnyException();
    }

    @Test
    void closeQuietlyCloseableThrowing() {
        Closeable c = () -> { throw new IOException("fail"); };
        assertThatCode(() -> IOUtils.closeQuietly(c)).doesNotThrowAnyException();
    }
}
