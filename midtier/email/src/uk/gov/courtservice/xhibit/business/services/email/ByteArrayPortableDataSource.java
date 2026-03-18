package uk.gov.courtservice.xhibit.business.services.email;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * Title: Portable Datasource implementation for simple byte arrays.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Trivial implementation of PortableDataSource for passing around byte arrays.
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

public class ByteArrayPortableDataSource implements PortableDataSource {
    private String filename = null;

    private String contentType;

    private byte[] attachment;

    /**
     * Basic constructor, treating the attachment as text/plain.
     * 
     * @param attachment
     *            the attachment.
     * @param filename
     *            the filename for the attachment if it is to be treated as a
     *            file, can be null.
     */
    public ByteArrayPortableDataSource(byte[] attachment, String filename) {
        this(attachment, PortableDataSource.DEFAULT_ATTACHMENT_TYPE, filename);
    }

    /**
     * Constructor allowing the type of the attachment to be set.
     * 
     * @param attachment
     *            the attachment.
     * @param contentType
     *            the content type of the attachment as specified in RFC822.
     * @param filename
     *            the filename for the attachment if it is to be treated as a
     *            file, can be null.
     */
    public ByteArrayPortableDataSource(byte[] attachment, String contentType, String filename) {
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
        return new ByteArrayInputStream(attachment);
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
     * Gets the bytes making up the attachment.
     * 
     * @return The bytes making up the attachment.
     */
    public byte[] getAttachment() {
        return attachment;
    }

    public OutputStream getOutputStream() throws java.io.IOException {
        throw new IOException("Does not support an output stream");
    }
}