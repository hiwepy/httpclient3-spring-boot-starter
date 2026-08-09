package org.apache.http.spring.boot.client.utils;

import java.io.File;
import java.util.Properties;

import org.springframework.util.StringUtils;

/**
 * Helper for resolving a file's MIME type from its extension.
 * <p>
 * Loads a {@code mimeTypes.properties} resource from the classpath (mapping extensions to MIME
 * types) on class load and exposes lookup helpers that fall back to
 * {@code application/octet-stream} when the extension is unknown.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public abstract class FilemimeUtils {

	/** Name of the classpath resource holding the extension-to-MIME-type mapping. */
	protected static final String MIMETYPES_PROPERTIES = "mimeTypes.properties";
	/** MIME type returned when no mapping can be resolved. */
	protected static final String DEFAULT_MIME = "application/octet-stream";
	protected static Properties properties;

	static {
		try {
			properties = new Properties();
			properties.load(FilemimeUtils.class
					.getResourceAsStream(MIMETYPES_PROPERTIES));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resolve the MIME type for the given file, falling back to {@link #DEFAULT_MIME} when the
	 * file is {@code null} or its extension is unknown.
	 * @param file the file whose MIME type should be resolved
	 * @return the resolved MIME type, or {@link #DEFAULT_MIME}
	 */
	public static String getFileMimeType(File file) {
		if (file == null) {
			return DEFAULT_MIME;
		}
		return getFileMimeType(file.getName());
	}

	/**
	 * Resolve the MIME type for the given file name, falling back to {@link #DEFAULT_MIME} when
	 * the name has no extension or the extension is unknown.
	 * @param fileName the file name whose MIME type should be resolved
	 * @return the resolved MIME type, or {@link #DEFAULT_MIME}
	 */
	public static String getFileMimeType(String fileName) {
		if ((StringUtils.isEmpty(fileName)) || (fileName.indexOf(".") == -1)) {
			return DEFAULT_MIME;
		}
		fileName = fileName.substring(fileName.lastIndexOf("."));
		return getExtensionMimeType(fileName);
	}

	/**
	 * Resolve the MIME type for the given extension (with or without a leading dot), returning
	 * {@code null} when the extension is empty.
	 * @param extension the file extension to resolve
	 * @return the resolved MIME type, {@code null} for an empty extension, or
	 *         {@link #DEFAULT_MIME} when unknown
	 */
	public static String getExtensionMimeType(String extension) {
		String result = null;
		if (StringUtils.isEmpty(extension)) {
			return result;
		}
		extension = extension.toLowerCase();
		if (!(extension.startsWith("."))) {
			extension = "." + extension;
		}
		result = (String) properties.getProperty(extension, DEFAULT_MIME);
		return result;
	}

	/**
	 * Standalone entry point that prints the MIME type for a few common extensions.
	 * @param args ignored
	 */
	public static void main(String[] args) {
		System.out.println("FileMimeUtils.getExtensionMimeType(gif)="
				+ getExtensionMimeType("gif"));
		System.out.println("FileMimeUtils.getExtensionMimeType(.pdf)="
				+ getExtensionMimeType(".pdf"));
		System.out.println("FileMimeUtils.getExtensionMimeType(.xls)="
				+ getExtensionMimeType(".xls"));
		System.out.println("FileMimeUtils.getFileMimeType(foo.gif)="
				+ getFileMimeType("foo.gif"));
		System.out.println("FileMimeUtils.getFileMimeType(foo.pdf)="
				+ getFileMimeType("foo.pdf"));
		System.out.println("FileMimeUtils.getFileMimeType(foo.xls)="
				+ getFileMimeType("foo.xls"));
		System.out.println("FileMimeUtils.getFileMimeType(foo.badextension)="
				+ getFileMimeType("foo.badextension"));
	}
}