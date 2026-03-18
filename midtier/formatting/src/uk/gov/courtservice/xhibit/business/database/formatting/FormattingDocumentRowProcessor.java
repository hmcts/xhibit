package uk.gov.courtservice.xhibit.business.database.formatting;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exception.formatting.FormattingException;
import uk.gov.courtservice.xhibit.business.services.formatting.FormattingServices;
import uk.gov.courtservice.xhibit.business.vos.formatting.FormattingValue;

/**
 * A custom row processor used to process a <code>FormattingValue</code> from
 * the database and perform the appropriate transformations as defined in the
 * <code>FormattingServices</code> instance passed in.
 * 
 * @author tz0d5m
 * @version $Id: FormattingDocumentRowProcessor.java,v 1.4 2005/12/01 15:20:33
 *          bzjrnl Exp $
 */
public final class FormattingDocumentRowProcessor extends AbstractRowProcessor {
    /** The log4j logger instance */
    private static final Logger log = CSServices.getLogger(FormattingDocumentRowProcessor.class);

    // The constants used to define the column names for use whilst
    // processing the datbase rows...
    private static final String DISTRIBUTION_TYPE = "DISTRIBUTION_TYPE";

    private static final String MIME_TYPE = "MIME_TYPE";

    private static final String DOCUMENT_TYPE = "DOCUMENT_TYPE";

    private static final String MAJOR_SCHEMA_VERSION = "MAJOR_SCHEMA_VERSION";

    private static final String MINOR_SCHEMA_VERSION = "MINOR_SCHEMA_VERSION";

    private static final String LANGUAGE = "LANGUAGE";

    private static final String COUNTRY = "COUNTRY";

    private static final String XML_DOCUMENT = "XML_DOCUMENT";

    private static final String FORMATTED_DOCUMENT = "FORMATTED_DOCUMENT";
    
    private static final String COURT_ID = "COURT_ID";
    
    private static final String XML_DOCUMENT_CLOB_ID = "XML_DOCUMENT_CLOB_ID";
    
    private static final String FORMATTING_ID = "FORMATTING_ID";

    /** The <code>FormattingServices</code> used to process the results */
    private final FormattingServices services;

    /**
     * Initialise a new <code>FormattingDocumentRowProcessor</code> with the
     * specified <code>FormattingServices</code>.
     * 
     * @param services
     *            The <code>FormattingServices</code> to inject into this
     *            instance for use by the processRow(..) method to process the
     *            row.
     * 
     * @see #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public FormattingDocumentRowProcessor(final FormattingServices services) {
        if (services == null) {
            throw new IllegalArgumentException("services cannot be null");
        }

        this.services = services;
    }

    /**
     * Construct a new <code>FormattingValue</code> from the values taken from
     * the passed in <code>Row</code> and process using the
     * <code>processDocument</code> method on the
     * <code>FormattingServices</code> instance passed at construction.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     * 
     * @throws FormattingException
     *             If an error occurs whilst formatting the document.
     */
    public void processRow(final Row row) throws FormattingException {
        Reader reader = null;
        OutputStream outputStream = null;
        try {
            reader = row.getCharacterStream(XML_DOCUMENT);
            outputStream = row.getBinaryOutputStream(FORMATTED_DOCUMENT);

            final FormattingValue value = new FormattingValue(row.getString(DISTRIBUTION_TYPE), row
                    .getString(MIME_TYPE), row.getString(DOCUMENT_TYPE), row.getInteger(MAJOR_SCHEMA_VERSION), row
                    .getInteger(MINOR_SCHEMA_VERSION), row.getString(LANGUAGE), row.getString(COUNTRY), reader,
                    outputStream, row.getInteger(COURT_ID));
            value.setXmlDocumentClobId(row.getLong(XML_DOCUMENT_CLOB_ID));
            value.setFormattingId(row.getInteger(FORMATTING_ID));
            this.services.processDocument(value);
        } finally {
            closeReader(reader);
            closeOutputStream(outputStream);
        }
    }

    /**
     * Extracted helper method to handle all <code>IOException</code>s thrown
     * whilst closing the passed in <code>OutputStream</code>. The error will
     * be logged, but execution allowed to continue. This means that the stream
     * is not guaranteed to be flushed in this method, therefore must be done
     * prior to calling this.
     * 
     * @param outputStream
     *            The stream to close.
     */
    private void closeOutputStream(final OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("Error closing outputStream", e);
            }
        }
    }

    /**
     * Extracted helper method to handle all <code>IOException</code>s thrown
     * whilst closing the passed in <code>Reader</code>. The error will be
     * logged, but execution allowed to continue.
     * 
     * @param reader
     *            The reader to close.
     */
    private void closeReader(final Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                log.error("Error closing reader", e);
            }
        }
    }
}
