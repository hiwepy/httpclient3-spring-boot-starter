package org.apache.http.spring.boot.client.property;

import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.http.spring.boot.HttpclientProperties;
import org.apache.http.spring.boot.client.property.HttpConnectionManagerProperties.ManagerType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Comprehensive unit tests for property classes.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class PropertyClassesComprehensiveCoverageTest {

    // --- HttpConnectionManagerProperties ---

    @Test
    void managerTypeGet() {
        assertThat(ManagerType.SIMPLE.get()).isEqualTo("simple");
        assertThat(ManagerType.MULTI_THREADED.get()).isEqualTo("multi-threaded");
    }

    @Test
    void managerTypeEqualsManagerType() {
        assertThat(ManagerType.SIMPLE.equals(ManagerType.SIMPLE)).isTrue();
        assertThat(ManagerType.SIMPLE.equals(ManagerType.MULTI_THREADED)).isFalse();
    }

    @Test
    void managerTypeEqualsString() {
        assertThat(ManagerType.SIMPLE.equals("simple")).isTrue();
        assertThat(ManagerType.SIMPLE.equals("SIMPLE")).isTrue();
        assertThat(ManagerType.SIMPLE.equals("multi-threaded")).isFalse();
    }

    @Test
    void managerTypeValueOfIgnoreCase() {
        assertThat(ManagerType.valueOfIgnoreCase("simple")).isEqualTo(ManagerType.SIMPLE);
        assertThat(ManagerType.valueOfIgnoreCase("SIMPLE")).isEqualTo(ManagerType.SIMPLE);
        assertThat(ManagerType.valueOfIgnoreCase("multi-threaded")).isEqualTo(ManagerType.MULTI_THREADED);
    }

    @Test
    void managerTypeValueOfIgnoreCaseInvalid() {
        assertThatThrownBy(() -> ManagerType.valueOfIgnoreCase("invalid")).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void connectionManagerProperties() {
        HttpConnectionManagerProperties props = new HttpConnectionManagerProperties();
        assertThat(props.getType()).isEqualTo(ManagerType.SIMPLE);
        props.setType(ManagerType.MULTI_THREADED);
        assertThat(props.getType()).isEqualTo(ManagerType.MULTI_THREADED);
    }

    @Test
    void connectionManagerAlwaysClose() {
        HttpConnectionManagerProperties props = new HttpConnectionManagerProperties();
        assertThat(props.isAlwaysClose()).isFalse();
        props.setAlwaysClose(true);
        assertThat(props.isAlwaysClose()).isTrue();
    }

    @Test
    void connectionManagerTimeoutInterval() {
        HttpConnectionManagerProperties props = new HttpConnectionManagerProperties();
        assertThat(props.getTimeoutInterval()).isEqualTo(5000);
        props.setTimeoutInterval(10000);
        assertThat(props.getTimeoutInterval()).isEqualTo(10000);
    }

    @Test
    void connectionManagerGetInitedParams() {
        HttpConnectionManagerProperties props = new HttpConnectionManagerProperties();
        HttpConnectionManagerProperties result = props.getInitedParams();
        assertThat(result).isSameAs(props);
    }

    // --- HttpConnectionProperties ---

    @Test
    void connectionPropertiesGetParams() {
        HttpConnectionProperties props = new HttpConnectionProperties();
        assertThat(props.getHttpConnectionParams()).isSameAs(props);
    }

    // --- HttpclientProperties ---

    @Test
    void httpclientPropertiesPrefix() {
        assertThat(HttpclientProperties.PREFIX).isEqualTo("httpclient");
    }

    @Test
    void httpclientPropertiesConnectionManager() {
        HttpclientProperties props = new HttpclientProperties();
        assertThat(props.getConnectionManager()).isNotNull();
        HttpConnectionManagerProperties mgr = new HttpConnectionManagerProperties();
        props.setConnectionManager(mgr);
        assertThat(props.getConnectionManager()).isSameAs(mgr);
    }

    @Test
    void httpclientPropertiesConnection() {
        HttpclientProperties props = new HttpclientProperties();
        assertThat(props.getConnection()).isNotNull();
        HttpConnectionProperties conn = new HttpConnectionProperties();
        props.setConnection(conn);
        assertThat(props.getConnection()).isSameAs(conn);
    }

    @Test
    void httpclientPropertiesHeaders() {
        HttpclientProperties props = new HttpclientProperties();
        assertThat(props.getHeaders()).isNotNull();
        Properties headers = new Properties();
        headers.setProperty("X-Custom", "value");
        props.setHeaders(headers);
        assertThat(props.getHeaders()).containsKey("X-Custom");
    }

    @Test
    void httpclientPropertiesHosts() {
        HttpclientProperties props = new HttpclientProperties();
        assertThat(props.getHosts()).isNotNull();
        Properties hosts = new Properties();
        hosts.setProperty("localhost-keepAlive", "true");
        props.setHosts(hosts);
        assertThat(props.getHosts()).containsKey("localhost-keepAlive");
    }
}
