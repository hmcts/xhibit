package uk.gov.courtservice.xhibit.business.database.listdistribution.processor;

import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.ListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.ListProcessor;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.ListProcessorFactory;

/**
 * Class used to process a row and construct an associated
 * <code>ListWorkFlow</code> for it.
 * 
 * @author tz0d5m
 * @version $Id: ListSummaryRowProcessor.java,v 1.3 2005/11/23 16:21:03 bzjrnl
 *          Exp $
 */
public final class ListSummaryRowProcessor extends AbstractRowProcessor {
    /** The log4j logger instance */
    private static final Logger log = CSServices.getLogger(ListSummaryRowProcessor.class);

    // The database column name mappings...
    private static final String LIST_ID = "WLL_CONTROL_ID";

    private static final String XML_DOCUMENT = "XML_DOCUMENT";

    private static final String DOCUMENT_TYPE = "DOCUMENT_TYPE";

    private static final String COURT_ID = "COURT_ID";

    private static final String MAJOR_SCHEMA_VERSION = "MAJOR_SCHEMA_VERSION";

    private static final String MINOR_SCHEMA_VERSION = "MINOR_SCHEMA_VERSION";

    private static final String LANGUAGE = "LANGUAGE";

    private static final String COUNTRY = "COUNTRY";

    // ClassDescriptorResolver to use in reading the list
    private final ClassDescriptorResolver classDescriptorResolver;

    // The list
    private ListSummary summary;

    /**
     * Construct a ListSummaryRowProcessor with the ClassDescriptorResolver
     */
    public ListSummaryRowProcessor(ClassDescriptorResolver classDescriptorResolver) {
        if (classDescriptorResolver == null) {
            throw new IllegalArgumentException("classDescriptorResolver: null");
        }
        this.classDescriptorResolver = classDescriptorResolver;
    }

    /**
     * Get the list summary created when the last row was processed
     * 
     * @throws IllegalStateException
     *             if the summary has not been created
     */
    public ListSummary getListSummary() {
        if (summary == null) {
            throw new IllegalStateException("summary: null");
        }
        return summary;
    }

    /**
     * Use the <code>ListWorkFlowFactory</code> to create an implementation of
     * the <code>ListWorkFlow</code> interface for the document represented by
     * the <code>Row</code> passed in.
     * 
     * @param row
     *            The row to process.
     * @throws IllegalStateException
     *             if more than one row is processed.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        // first validate that we are only processing one row...
        if (summary != null) {
            throw new IllegalStateException("summary: " + summary);
        }

        // then read the row
        ListProcessor listProcessor = ListProcessorFactory.getListProcessor(row.getString(DOCUMENT_TYPE));

        Reader reader = row.getCharacterStream(XML_DOCUMENT);
        try {
            List list = listProcessor.readList(classDescriptorResolver, reader);
            summary = new ListSummary(row.getInteger(LIST_ID), list, listProcessor, row.getInteger(COURT_ID), row
                    .getInteger(MAJOR_SCHEMA_VERSION), row.getInteger(MINOR_SCHEMA_VERSION), row.getString(LANGUAGE),
                    row.getString(COUNTRY));
        } finally {
            closeReader(reader);
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
            } catch (final IOException e) {
                log.error("Error closing reader", e);
            }
        }
    }
}
