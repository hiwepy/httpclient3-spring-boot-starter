package org.apache.http.spring.boot.client.utils;

import java.io.File;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link FilemimeUtils}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class FilemimeUtilsCoverageTest {
    @Test
    void getFileMimeTypeNullFile() {
        assertThat(FilemimeUtils.getFileMimeType((File) null)).isEqualTo("application/octet-stream");
    }

    @Test
    void getFileMimeTypeNullString() {
        assertThat(FilemimeUtils.getFileMimeType((String) null)).isEqualTo("application/octet-stream");
    }

    @Test
    void getFileMimeTypeEmptyString() {
        assertThat(FilemimeUtils.getFileMimeType("")).isEqualTo("application/octet-stream");
    }

    @Test
    void getFileMimeTypeNoExtension() {
        assertThat(FilemimeUtils.getFileMimeType("noext")).isEqualTo("application/octet-stream");
    }

    @Test
    void getFileMimeTypeWithExtension() {
        assertThat(FilemimeUtils.getFileMimeType("test.txt")).isEqualTo("text/plain");
    }

    @Test
    void getExtensionMimeTypeEmpty() {
        assertThat(FilemimeUtils.getExtensionMimeType("")).isNull();
    }

    @Test
    void getExtensionMimeTypeWithDot() {
        assertThat(FilemimeUtils.getExtensionMimeType(".txt")).isEqualTo("text/plain");
    }

    @Test
    void getExtensionMimeTypeWithoutDot() {
        assertThat(FilemimeUtils.getExtensionMimeType("txt")).isEqualTo("text/plain");
    }

    @Test
    void getExtensionMimeTypeUnknown() {
        assertThat(FilemimeUtils.getFileMimeType("file.unknownext")).isEqualTo("application/octet-stream");
    }

    @Test
    void getFileMimeTypeFile() {
        File f = new File("test.pdf");
        assertThat(FilemimeUtils.getFileMimeType(f)).contains("application/pdf");
    }
}
