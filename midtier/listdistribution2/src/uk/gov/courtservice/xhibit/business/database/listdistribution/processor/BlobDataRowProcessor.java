package uk.gov.courtservice.xhibit.business.database.listdistribution.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * <p>
 * Title: BlobDataRowProcessor
 * </p>
 * <p>
 * Description: Read the blob data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: BlobDataRowProcessor.java,v 1.3 2006/06/05 12:29:13 bzjrnl Exp $
 */
public final class BlobDataRowProcessor extends AbstractRowProcessor {

    // The list of strings containing the clob data
    private List stringList = new ArrayList();

    /**
     * Get the values created when the last row was processed
     * 
     * @throws IllegalStateException
     *             if the summary has not been created
     */
    public String[] getStrings() {
        return (String[]) stringList.toArray(new String[stringList.size()]);
    }

    /**
     * Create a String containg the contents of the clob 1 for each row returned
     * by the query.
     * 
     * @param row
     *            The row to process.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        stringList.add(row.getBlobAsString("blob_data"));
    }
}
