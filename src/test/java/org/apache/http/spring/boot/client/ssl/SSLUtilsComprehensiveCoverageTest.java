package org.apache.http.spring.boot.client.ssl;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Comprehensive unit tests for SSL utility classes.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class SSLUtilsComprehensiveCoverageTest {

    // --- SSLContextUtils ---

    @Test
    void createSSLContextWithNullManagers() throws Exception {
        SSLContext ctx = SSLContextUtils.createSSLContext("TLS", (javax.net.ssl.KeyManager[]) null, (javax.net.ssl.TrustManager[]) null);
        assertThat(ctx).isNotNull();
        assertThat(ctx.getProtocol()).isEqualTo("TLS");
    }

    @Test
    void createSSLContextWithSingleNullManagers() throws Exception {
        SSLContext ctx = SSLContextUtils.createSSLContext("TLS", (javax.net.ssl.KeyManager) null, (javax.net.ssl.TrustManager) null);
        assertThat(ctx).isNotNull();
    }

    @Test
    void createSSLContextWithSecureRandom() throws Exception {
        SSLContext ctx = SSLContextUtils.createSSLContext("TLS", null, null, new java.security.SecureRandom());
        assertThat(ctx).isNotNull();
    }

    // --- TrustManagerUtils ---

    @Test
    void getAcceptAllTrustManager() {
        X509TrustManager tm = TrustManagerUtils.getAcceptAllTrustManager();
        assertThat(tm).isNotNull();
    }

    @Test
    void getValidateServerCertificateTrustManager() {
        X509TrustManager tm = TrustManagerUtils.getValidateServerCertificateTrustManager();
        assertThat(tm).isNotNull();
    }

    @Test
    void acceptAllTrustManagerCheckClientTrusted() {
        X509TrustManager tm = TrustManagerUtils.getAcceptAllTrustManager();
        assertThatCode(() -> tm.checkClientTrusted(new X509Certificate[0], "RSA")).doesNotThrowAnyException();
    }

    @Test
    void acceptAllTrustManagerCheckServerTrusted() {
        X509TrustManager tm = TrustManagerUtils.getAcceptAllTrustManager();
        assertThatCode(() -> tm.checkServerTrusted(new X509Certificate[0], "RSA")).doesNotThrowAnyException();
    }

    @Test
    void acceptAllTrustManagerGetAcceptedIssuers() {
        X509TrustManager tm = TrustManagerUtils.getAcceptAllTrustManager();
        assertThat(tm.getAcceptedIssuers()).isEmpty();
    }

    @Test
    void validateServerTrustManagerCheckClientTrusted() {
        X509TrustManager tm = TrustManagerUtils.getValidateServerCertificateTrustManager();
        assertThatCode(() -> tm.checkClientTrusted(new X509Certificate[0], "RSA")).doesNotThrowAnyException();
    }

    @Test
    void validateServerTrustManagerCheckServerTrusted() {
        X509TrustManager tm = TrustManagerUtils.getValidateServerCertificateTrustManager();
        assertThatCode(() -> tm.checkServerTrusted(new X509Certificate[0], "RSA")).doesNotThrowAnyException();
    }

    @Test
    void validateServerTrustManagerGetAcceptedIssuers() {
        X509TrustManager tm = TrustManagerUtils.getValidateServerCertificateTrustManager();
        assertThat(tm.getAcceptedIssuers()).isEmpty();
    }

    @Test
    void getDefaultTrustManager() throws Exception {
        X509TrustManager tm = TrustManagerUtils.getDefaultTrustManager(null);
        assertThat(tm).isNotNull();
    }

    // --- KeyManagerUtils ---

    @Test
    void closeQuietlyNull() {
        assertThatCode(() -> KeyManagerUtils.closeQuietly(null)).doesNotThrowAnyException();
    }

    @Test
    void closeQuietlyNormal() {
        Closeable c = new ByteArrayInputStream(new byte[0]);
        assertThatCode(() -> KeyManagerUtils.closeQuietly(c)).doesNotThrowAnyException();
    }

    @Test
    void closeQuietlyThrowing() {
        Closeable c = () -> { throw new IOException("fail"); };
        assertThatCode(() -> KeyManagerUtils.closeQuietly(c)).doesNotThrowAnyException();
    }

    // --- SSLSocketUtils ---

    @Test
    void enableEndpointNameVerificationWithNull() {
        // SSLSocketUtils.enableEndpointNameVerification throws NPE on null input
        assertThatCode(() -> SSLSocketUtils.enableEndpointNameVerification(null))
                .isInstanceOf(NullPointerException.class);
    }
}
