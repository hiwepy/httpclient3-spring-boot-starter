package org.apache.http.spring.boot.client;

/**
 * Common content-type and character-encoding constants used when issuing HTTP requests and
 * inspecting responses.
 * <p>Holds the well-known MIME media types (JSON, XML, form-data, octet-stream, etc.) together
 * with the character set names referenced by the helpers and response handlers in this
 * starter.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public abstract class ContentType {
	
	// constants
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	public static final String APPLICATION_SVG_XML = "application/svg+xml";
	public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";
	public static final String APPLICATION_XML = "application/xml";
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";
	public static final String TEXT_HTML = "text/html";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_JSON = "text/json";
	public static final String TEXT_XML = "text/xml";
	public static final String WILDCARD = "*/*";

	public static final String UTF_8 = "UTF-8";
	public static final String ASCII = "US-ASCII";
	public static final String ISO_8859_1 = "ISO-8859-1";
 
	
}
