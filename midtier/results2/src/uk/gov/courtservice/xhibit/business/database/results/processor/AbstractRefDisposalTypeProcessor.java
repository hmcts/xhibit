package uk.gov.courtservice.xhibit.business.database.results.processor;

// XHIBIT
import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: RefDisposalTypeProcessor
 * </p>
 * <p>
 * Description: Process the disposal type reference data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public abstract class AbstractRefDisposalTypeProcessor extends AbstractRowProcessor {

    /**
     * Process the row storing the DispsoalTypeReferenceValue in the list
     * 
     * @param row
     *            the row to process
     */
    public void processRow(Row row) {
        processValue(new DisposalReferenceValue(row.getInteger("ref_disposal_type_id"), row.getString("disposal_code"),
                row.getInteger("template_version"), row.getString("title"), row.getInteger("line_avail"), row
                        .getString("category")));
    }

    /**
     * Process the value
     * 
     * @param value
     *            the value to process
     */
    public abstract void processValue(DisposalReferenceValue value);
}