package org.apache.http.spring.boot.client.multipart;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.methods.multipart.PartSource;

/**
 * Implementation of Commons HttpClient 3.x {@link PartSource} backed by an
 * {@link InputStream}, allowing an in-memory or streamed payload to be uploaded as a
 * {@code multipart/form-data} part without requiring an underlying file.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class InputStreamPartSource implements PartSource {


    /** Stream part input. */
    private InputStream input = null;

    /** File part file name. */
    private String fileName = null;
 
	/**
	 * Create a part source from the given stream and file name.
	 * @param fileName the file name to expose for the part, may be {@code null}
	 * @param input the input stream providing the part content
	 */
	public InputStreamPartSource( String fileName , InputStream input) {
		super();
		this.fileName = fileName;
		this.input = input;
	}

	/**
	 * Return the number of bytes available from the underlying stream, or {@code 0} if the
	 * length cannot be determined.
	 * @return the estimated number of bytes that can be read
	 */
	@Override
	public long getLength() {
		if (this.input != null) {
            try {
				return this.input.available();
			} catch (IOException e) {
				//	e.printStackTrace();
				return 0;
			}
        } else {
            return 0;
        }
	}

	/**
	 * Return the file name associated with this part, defaulting to {@code "noname"} when none
	 * has been set.
	 * @return the file name for this part
	 */
	@Override
	public String getFileName() {
		 return (fileName == null) ? "noname" : fileName;
	}

	/**
	 * Return the underlying input stream used to read the part content.
	 * @return the part content stream
	 * @throws IOException if the stream cannot be returned
	 */
	@Override
	public InputStream createInputStream() throws IOException {
		return input;
	}

}
