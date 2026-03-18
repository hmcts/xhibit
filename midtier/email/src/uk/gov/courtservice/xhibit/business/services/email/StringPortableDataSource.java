package uk.gov.courtservice.xhibit.business.services.email;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * Title: String wrapping PortableDataSource
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * PortableDataSource supporting Strings.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class StringPortableDataSource implements PortableDataSource {
    private String filename = null;

    private String contentType;

    private String attachment;

    /**
     * Basic constructor, treating the attachment as text/plain.
     * 
     * @param attachment
     *            the attachment.
     * @param filename
     *            the filename for the attachment if it is to be treated as a
     *            file, can be null.
     */
    public StringPortableDataSource(String attachment, String filename) {
        this(attachment, PortableDataSource.DEFAULT_ATTACHMENT_TYPE, filename);
    }

    /**
     * Constructor allowing the content type of the attachment to be set.
     * 
     * @param attachment
     *            the attachment.
     * @param contentType
     *            the content type of the attachment as specified in RFC822.
     * @param filename
     *            the filename for the attachment if it is to be treated as a
     *            file, can be null.
     */
    public StringPortableDataSource(String attachment, String contentType, String filename) {
        this.contentType = contentType;
        this.filename = filename;
        this.attachment = attachment;
    }

    /**
     * Gets the attachment as an input stream.
     * 
     * @return the attachment as an input stream
     * @throws java.io.IOException
     *             when there is a problem in creating the stream.
     */
    public InputStream getInputStream() throws java.io.IOException {
        return new ByteArrayInputStream(attachment.getBytes());
    }

    /**
     * Unsupported operation.
     * 
     * @return nothing, only an exception
     * @throws java.io.IOException
     *             always as not supported.
     */
    public OutputStream getOutputStream() throws java.io.IOException {
        throw new IOException("Does not support an output stream");
    }

    /**
     * Gets the content type of the attachment as specified in RFC822.
     * 
     * @return the content type of the attachment.
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * The filename of the attachment, which may be null if the attachment is
     * not to be treated as a file.
     * 
     * @return the filename of the attachment.
     */
    public String getName() {
        return this.filename;
    }

    /**
     * Gets the original String.
     * 
     * @return the String attachment.
     */
    public String getAttachment() {
        return attachment;
    }
}